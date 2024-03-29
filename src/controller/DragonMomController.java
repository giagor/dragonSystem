package controller;

import entity.DragonGroup;
import entity.DragonMom;
import entity.DragonTrainer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;
import util.*;
import util.table.DragonGroupTable;
import util.table.DragonTrainerTable;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.TextInputDialogTool;

import java.io.IOException;
import java.util.*;

/**
 * 为了可以初始化，所以继承接口Initializable.
 */
public class DragonMomController extends BaseController {
    @FXML
    private TreeTableView<DragonTrainer> trainerTreeTableView;
    @FXML
    private TreeTableView<DragonGroup> groupTreeTableView;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button changeUser;
    @FXML
    private Tab trainerTab;
    @FXML
    private Tab groupTab;

    private DragonMom dragonMom = null;

    private IDragonMomDAO iDragonMomDAO = DAOFactory.getDragonMomDAOInstance();

    private IDragonTrainerDAO iDragonTrainerDAO = DAOFactory.getDragonTrainerDAOInstance();

    private IDragonGroupDAO iDragonGroupDAO = DAOFactory.getDragonGroupDAOInstance();

    private TreeItem<DragonTrainer> trainerRoot = new TreeItem<DragonTrainer>(new DragonTrainer());

    private TreeItem<DragonGroup> groupRoot = new TreeItem<DragonGroup>(new DragonGroup());

    /**
     * 因为多列树控件中删除一项数据时，表要同步更新，就需要从treeItemList中移除原来加载进去的那个TreeItem对象，所以这里先把
     * TreeItem存起来，方便到时候从treeItemList中移除。
     */
    private List<TreeItem<DragonTrainer>> trainerTreeItemList = new ArrayList<>();

    private List<TreeItem<DragonGroup>> groupTreeItemList = new ArrayList<>();

    /**
     * 默认先显示驯龙高手的信息
     */
    public void init() {
        initTrainerTreeTable();
        initTrainerTreeData();
        initGroupTreeTable();
        initGroupTreeData();
        tabPaneListener();
    }

    /**
     * TabPane监听器，用户点击不同的Pane则切换不同的表的信息
     */
    @FXML
    @Override
    public void tabPaneListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab newTab) {
                if (newTab.getText().equals(trainerTab.getText())) {
                    trainerTreeTableView.setVisible(true);
                    groupTreeTableView.setVisible(false);
                } else if (newTab.getText().equals(groupTab.getText())) {
                    groupTreeTableView.setVisible(true);
                    trainerTreeTableView.setVisible(false);
                }
            }
        });
    }

    /**
     * 切换账号切换为登陆界面.
     */
    @FXML
    @Override
    public void switchAccount(ActionEvent actionEvent) {
        SwitchAccount.changeUser(changeUser);
    }


    /**
     * 添加驯龙高手信息.
     */
    @FXML
    public void addDragonTrainer(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        TextField t_groupId = new TextField();
        TextField t_name = new TextField();
        TextField t_username = new TextField();
        TextField t_password = new TextField();

        t_groupId.setPromptText("已存在的族群Id");
        t_name.setPromptText("驯龙高手名字");
        t_username.setPromptText("用户名");
        t_password.setPromptText("密码");

        vBox.getChildren().addAll(t_groupId, t_name, t_username, t_password);

        //使用了自定义控件，弹出弹窗
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加驯龙高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText();
            String username = t_username.getText().trim();
            String password = t_password.getText().trim();
            int dragonGroupId;
            try {
                //判读输入的ID是否为整数
                dragonGroupId = Integer.parseInt(t_groupId.getText().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "非法输入");
                return;
            }

            if (CheckValid.isEmpty(name, username, password, t_groupId.getText().trim()) ||
                    CheckValid.isInvalidUsername(username)) {
                //判断是否有空的信息以及用户名是否重复
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "信息填写不完整" +
                        "或者用户名已注册");
                return;
            }

            //数据库保存数据,items记录影响的数据条数
            int items = iDragonTrainerDAO.save(dragonGroupId, name, username, password);

            if (items == 0) {//说明没有插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是族群不存在或者用户名已注册");
            } else {
                DragonTrainer dragonTrainer = iDragonTrainerDAO.get(username, password);
                TreeItem<DragonTrainer> treeItem = new TreeItem<>(dragonTrainer);
                trainerTreeItemList.add(treeItem);
                trainerRoot.getChildren().add(treeItem);
            }
        }

    }

    /**
     * 删除驯龙高手信息.
     * 从treeItemList找到驯龙高手相匹配的treeItem,然后从树控件中移除
     */
    @FXML
    public void deleteDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("删除驯龙高手信息",
                "请输入驯龙高手的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonTrainerId;
            try {
                dragonTrainerId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            DragonTrainer dragonTrainer = iDragonTrainerDAO.get(dragonTrainerId);
            int items = iDragonTrainerDAO.delete(dragonTrainerId);

            if (items == 0) {//说明没有数据删除
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "可能是没有与id匹配的驯龙高手");
            } else {
                for (TreeItem<DragonTrainer> treeItem : trainerTreeItemList) {
                    if (treeItem.getValue().getDragonTrainerId() == dragonTrainerId) {
                        trainerTreeItemList.remove(treeItem);
                        trainerRoot.getChildren().remove(treeItem);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 查询驯龙高手信息.
     */
    @FXML
    public void queryDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("查询驯龙高手信息",
                "请输入驯龙高手的Id", "Id:");
        if (result.isPresent()) {
            int dragonTrainerId;
            try {
                dragonTrainerId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            //找驯龙高手
            DragonTrainer trainer = iDragonTrainerDAO.get(dragonTrainerId);

            //没找到的情况下
            if (trainer == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
                return;
            }

            //找到的情况下
            int dragonGroupId = trainer.getDragonGroupId();
            VBox vBox = new VBox(10);

            Text t_trainerName = new Text("名字:" + trainer.getName());
            Text t_id = new Text("Id:" + dragonGroupId);
            Text t_groupName = new Text("族群名字:" + iDragonTrainerDAO.get(dragonGroupId).getName());
            Text t_groupId = new Text("族群Id:" + trainer.getDragonGroupId());
            Text t_username = new Text("用户名:" + trainer.getUsername());
            Text t_password = new Text("密码:" + trainer.getPassword());

            vBox.getChildren().addAll(t_trainerName, t_id, t_groupName, t_groupId, t_username, t_password);

            DialogTool.showDialog("驯龙高手信息", vBox, "确定", null).showAndWait();
        }
    }

    /**
     * 修改驯龙高手信息.
     * 基本思路：查询->显示原来信息->进行修改
     */
    @FXML
    public void changeDragonTrainer(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput(null, "请输入驯龙高手的Id",
                "Id:");
        if (result.isPresent()) {
            int dragonTrainerId;

            try {
                dragonTrainerId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            //找驯龙高手
            DragonTrainer trainer = iDragonTrainerDAO.get(dragonTrainerId);

            //没找到的情况下
            if (trainer == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该驯龙高手的信息");
                return;
            }

            //找到的情况下，修改信息
            GridPane gridPane = new GridPane();

            Label l_name = new Label("名字:");
            Label l_id = new Label("族群Id:");
            Label l_password = new Label("密码:");

            TextField t_name = new TextField(trainer.getName());
            TextField t_id = new TextField(String.valueOf(trainer.getDragonGroupId()));
            TextField t_password = new TextField(trainer.getPassword());

            gridPane.add(l_name, 0, 0);
            gridPane.add(t_name, 1, 0);
            gridPane.add(l_id, 0, 1);
            gridPane.add(t_id, 1, 1);
            gridPane.add(l_password, 0, 2);
            gridPane.add(t_password, 1, 2);

            gridPane.setVgap(10);

            Optional<ButtonType> choice = DialogTool.showDialog("修改驯龙高手信息", gridPane, "确定",
                    null).showAndWait();

            if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                String name = t_name.getText().trim();
                String password = t_password.getText().trim();
                int dragonGroupId;
                try {
                    //输入的ID是否为整数
                    dragonGroupId = Integer.parseInt(t_id.getText().trim());
                } catch (Exception e) {
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                    return;
                }

                if (CheckValid.isEmpty(name, password, t_id.getText().trim())) {
                    //判断是否有空的信息
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                    return;
                }

                int items = iDragonTrainerDAO.update(dragonTrainerId, dragonGroupId, name, password);

                if (items == 0) {//说明没有数据修改
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "可能族群不存在");
                } else {
                    DragonTrainerTable.getInstance().flushTrainer(trainerTreeItemList, trainerRoot);
                }
            }
        }
    }

    /**
     * 添加族群信息.
     */
    @FXML
    public void addDragonGroup(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        TextField t_name = new TextField();
        TextField t_profile = new TextField();
        TextField t_location = new TextField();
        TextField t_size = new TextField();

        t_name.setPromptText("族群名字");
        t_profile.setPromptText("简介");
        t_location.setPromptText("地理位置");
        t_size.setPromptText("大小");

        vBox.getChildren().addAll(t_name, t_profile, t_location, t_size);

        //使用了自定义控件
        Dialog<ButtonType> dialog = DialogTool.showDialog("添加族群高手信息", vBox, "确定", "取消");
        Optional<ButtonType> result = dialog.showAndWait();
        //如果用户点击了确定按钮
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = t_name.getText().trim();
            String profile = t_profile.getText().trim();
            String location = t_location.getText().trim();
            double size;
            try {
                size = Double.parseDouble(t_size.getText().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            if (CheckValid.isEmpty(name, profile, location, t_location.getText().trim())) {
                //判断是否有空的信息
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                return;
            }

            int items = iDragonGroupDAO.save(name, profile, location, size);

            if (items == 0) {//说明没有插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "添加失败", "可能是该名字已存在");
            } else {
                DragonGroup dragonGroup = iDragonGroupDAO.get(name);
                TreeItem<DragonGroup> treeItem = new TreeItem<>(dragonGroup);
                groupTreeItemList.add(treeItem);
                groupRoot.getChildren().add(treeItem);
            }
        }

    }

    /**
     * 删除族群信息.
     * 注意要从treeItemList找到族群相匹配的treeItem,然后从树控件中移除
     */
    @FXML
    public void deleteDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("删除族群信息",
                "请输入族群的Id", "Id:");
        //如果用户点击了确定按钮
        if (result.isPresent()) {
            int dragonGroupId;
            try {
                dragonGroupId = Integer.parseInt(result.get().trim());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "非法输入");
                return;
            }

            int items = iDragonGroupDAO.delete(dragonGroupId);

            if (items == 0) {//说明没有数据删除
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "删除失败", "请确保id存在或者" +
                        "族群内无驯龙高手");
            } else {
                for (TreeItem<DragonGroup> treeItem : groupTreeItemList) {
                    if (treeItem.getValue().getDragonGroupId() == dragonGroupId) {
                        groupTreeItemList.remove(treeItem);
                        groupRoot.getChildren().remove(treeItem);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 查询族群信息.
     */
    @FXML
    public void queryDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("查询族群信息",
                "请输入族群的Id", "Id:");
        if (result.isPresent()) {
            int dragonGroupId;
            try {
                dragonGroupId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            //找族群
            DragonGroup group = iDragonGroupDAO.get(dragonGroupId);

            //没找到的情况下
            if (group == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
                return;
            }

            //找到的情况下
            VBox vBox = new VBox(10);

            Text t_name = new Text("名字:" + group.getName());
            Text t_id = new Text("Id:" + group.getDragonGroupId());
            Text t_profile = new Text("简介:" + group.getProfile());
            Text t_location = new Text("地理位置:" + group.getLocation());

            vBox.getChildren().addAll(t_name, t_id, t_profile, t_location);

            DialogTool.showDialog("族群信息", vBox, "确定", null).showAndWait();
        }
    }

    /**
     * 修改族群信息.
     * 基本思路：查询->显示原来信息->进行修改
     * flushGroup()方法是为了刷新一下显示和groupTreeItemList
     */
    @FXML
    public void changeDragonGroup(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput(null, "请输入族群的Id",
                "Id:");
        if (result.isPresent()) {
            int dragonGroupId;
            try {
                dragonGroupId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                return;
            }

            //找族群
            DragonGroup group = iDragonGroupDAO.get(dragonGroupId);

            //没找到的情况下，修改信息
            if (group == null) {
                AlertTool.showAlert(Alert.AlertType.ERROR, null, "错误提示", "查询不到该族群的信息");
                return;
            }

            //找到的情况下
            GridPane gridPane = new GridPane();

            Label l_name = new Label("名字:");
            Label l_profile = new Label("简介:");
            Label l_location = new Label("地理位置:");
            Label l_size = new Label("大小:");

            TextField t_name = new TextField(group.getName());
            TextField t_profile = new TextField(group.getProfile());
            TextField t_location = new TextField(group.getLocation());
            TextField t_size = new TextField(String.valueOf(group.getSize()));

            gridPane.add(l_name, 0, 0);
            gridPane.add(t_name, 1, 0);
            gridPane.add(l_profile, 0, 1);
            gridPane.add(t_profile, 1, 1);
            gridPane.add(l_location, 0, 2);
            gridPane.add(t_location, 1, 2);
            gridPane.add(l_size, 0, 3);
            gridPane.add(t_size, 1, 3);

            gridPane.setVgap(10);

            Optional<ButtonType> choice = DialogTool.showDialog("修改族群信息", gridPane, "确定",
                    null).showAndWait();

            if (choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                String name = t_name.getText().trim();
                String profile = t_profile.getText().trim();
                String location = t_location.getText().trim();
                double size = 0;
                try {
                    size = Double.parseDouble(t_size.getText().trim());
                } catch (Exception e) {
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "非法输入");
                    return;
                }

                if (CheckValid.isEmpty(name, profile, location, t_size.getText().trim())) {
                    //判断是否有空的信息
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "信息填写不完整");
                    return;
                }

                int items = iDragonGroupDAO.update(name, profile, location, size, dragonGroupId);

                if (items == 0) {//说明没有数据修改
                    AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "修改失败", "可能是该名字已存在");
                } else {
                    DragonGroupTable.getInstance().flushGroup(groupTreeItemList, groupRoot);
                }
            }
        }
    }


    /**
     * 驯龙高手表：
     * 设置列名、列宽
     * 调用工具类
     */
    public void initTrainerTreeTable() {
        String[] columnName = {"驯龙高手名字", "Id", "族群Id", "族群名字"};
        double[] columnPrefWidth = {150, 80, 80, 120};
        String[] columnId = {DragonTrainerTable.NAME, DragonTrainerTable.ID,
                DragonTrainerTable.DRAGON_GROUP_ID, DragonTrainerTable.DRAGON_GROUP_NAME};
        DragonTrainerTable.getInstance().initTrainerTreeTable(trainerTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 驯龙高手表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initTrainerTreeData() {
        DragonTrainerTable.getInstance().initTrainerTreeData(trainerTreeTableView, trainerRoot, trainerTreeItemList);
    }

    /**
     * 族群表：
     * 设置列名、列宽
     * 调用工具类
     */
    public void initGroupTreeTable() {
        String[] columnName = {"族群名字", "Id", "简介", "地理位置", "大小"};
        double[] columnPrefWidth = {120, 80, 120, 120, 80};
        String[] columnId = {DragonGroupTable.NAME, DragonGroupTable.ID, DragonGroupTable.PROFILE,
                DragonGroupTable.LOCATION, DragonGroupTable.SIZE};
        DragonGroupTable.getInstance().initGroupTreeTable(groupTreeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 族群表：
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initGroupTreeData() {
        DragonGroupTable.getInstance().initGroupTreeData(groupTreeTableView, groupRoot, groupTreeItemList);
    }

    /**
     * 点击事件，弹出弹窗显示金库中的钱.
     */
    @FXML
    public void showMoneyTub(ActionEvent actionEvent) {
        dragonMom = iDragonMomDAO.get();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(25));
        Text text = new Text("金库余额:" + dragonMom.getMoneyTub());
        text.setFont(new Font(15));
        vBox.getChildren().add(text);
        vBox.setAlignment(Pos.CENTER);

        DialogTool.showDialog("金库", vBox, "确定",
                null).showAndWait();
    }

    public void setDragonMom(DragonMom dragonMom) {
        this.dragonMom = dragonMom;
    }

    /**
     * 点击事件，处理外邦人的退票处理.
     */
    @FXML
    public void dealBackTickets(ActionEvent actionEvent) {
        try {
            ViewManager.openView(ViewManager.BACK_TICKETS_URL, null, 400.0, 400.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开活动列表.
     */
    @FXML
    public void showActivity(ActionEvent actionEvent) {
        try {
            ViewManager.openView(ViewManager.MOM_ACTIVITY_URL, "活动信息", 600.0, 400.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开(所有)评价的界面
     */
    @FXML
    public void showAllEvaluation(ActionEvent actionEvent) {
        try {
            ViewManager.openView(ViewManager.MOM_EVALUATION_URL, "评价信息", 730.0, 450.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看所有的账目.
     * */
    @FXML
    public void showAllAccount(ActionEvent actionEvent) {
        try {
            ViewManager.openView(ViewManager.MOM_ACCOUNT_URL, "账目信息", 730.0, 450.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
