package controller;

import entity.Activity;
import entity.DragonGroup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.IActivityDAO;
import util.DAOFactory;
import util.control.AlertTool;
import util.control.DialogTool;
import util.control.TextInputDialogTool;
import util.table.ActivityTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ActivityController implements Initializable {
    @FXML
    private TreeTableView<Activity> treeTableView;
    @FXML
    private Button viewActivity;
    @FXML
    private Button addActivity;

    IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();

    private TreeItem<Activity> activityRoot = new TreeItem<Activity>(new Activity());

    List<TreeItem<Activity>> activityTreeItemList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initActivityTreeTable();
        initActivityTreeData();
    }

    /**
     * 查看活动内容.
     * */
    @FXML
    public void viewActivity(ActionEvent actionEvent) {
        Optional<String> result = TextInputDialogTool.showTextInput("观看活动","请输入活动Id","Id:");
        if(result.isPresent()){
            int activityId = 0;
            try {
                activityId = Integer.parseInt(result.get());
            } catch (Exception e) {
                AlertTool.showAlert(Alert.AlertType.WARNING, "错误", "查询失败", "非法输入");
                return;
            }

            Activity activity = iActivityDAO.getById(Integer.parseInt(result.get().trim()));

            TextArea t_content = new TextArea();//展示文本内容
            t_content.setEditable(false);
            t_content.setText("活动内容: " + activity.getContent());
            t_content.setWrapText(true);
            t_content.setPrefColumnCount(25);//设置框的大小

            Optional<ButtonType> choice = DialogTool.showDialog("活动内容",t_content,"去评价",
                    "溜了溜了").showAndWait();

            if(choice.isPresent() && choice.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                //用户点击了"去评价"

            }
        }
    }

    /**
     * 添加活动.
     * */
    public void addActivity(ActionEvent actionEvent) {
        VBox vBox = new VBox(10);

        TextField t_groupId = new TextField();
        TextField t_name = new TextField();
        TextArea t_content = new TextArea();
        DatePicker d_startTime = new DatePicker();
        DatePicker d_overTime = new DatePicker();

        t_groupId.setPromptText("族群Id");
        t_name.setPromptText("活动名字");
        t_content.setPromptText("活动内容");
        t_content.setPrefColumnCount(25);//设置TextArea输入框大小
        t_content.setWrapText(true);//自动换行
        d_startTime.setEditable(false);
        d_startTime.setPromptText("开始时间");
        d_overTime.setEditable(false);
        d_overTime.setPromptText("结束时间");


        vBox.getChildren().addAll(t_groupId,t_name,t_content,d_startTime,d_overTime);

        Optional<ButtonType> result = DialogTool.showDialog("添加活动",vBox,"确定",null).showAndWait();

        if(result.isPresent()){
            int groupId = Integer.parseInt(t_groupId.getText().trim());
            String name = t_name.getText().trim();
            String content = t_content.getText().trim();
            String startTime = d_startTime.getValue().toString();
            String overTime = d_overTime.getValue().toString();

            int items = iActivityDAO.save(groupId,name,content,startTime,overTime);

            if(items == 0){
                //说明没有成功插入数据
                AlertTool.showAlert(Alert.AlertType.WARNING,null,"添加失败","可能该族群并不存在");
            }else{
                //说明数据插入成功
                AlertTool.showAlert(Alert.AlertType.INFORMATION,null,"添加成功",null);
            }
        }

    }

    /**
     * 活动表.
     * 设置列名、列宽
     * 调用工具类
     */
    public void initActivityTreeTable() {
        String[] columnName = {"活动Id","活动名字","举办的族群","开始时间","结束时间"};
        double[] columnPrefWidth = {80,120,120, 100, 100};
        String[] columnId = {"activityId","activityName","groupName",  "startTime", "overTime"};
        ActivityTable.getInstance().initActivityTable(treeTableView, columnName, columnPrefWidth, columnId);
    }

    /**
     * 活动表.
     * 数据的显示。
     * 根节点进行了隐藏
     * 调用工具类
     */
    public void initActivityTreeData() {
        ActivityTable.getInstance().initActivityTreeData(treeTableView, activityRoot, activityTreeItemList);
    }

    public Button getViewActivity() {
        return viewActivity;
    }

    public Button getAddActivity() {
        return addActivity;
    }



}
