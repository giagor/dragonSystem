package util.table;

import entity.Dragon;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import model.IDragonDAO;
import util.DAOFactory;

import java.util.List;

/**
 * 负责族群中龙的列表TreeTableView的初始化(列名、列宽、数据)，封转成一个工具类.
 * 可以根据需要显示对应的列
 */
public class DragonTable {
    private volatile static DragonTable instance = null;

    private DragonTable() {
    }

    public static DragonTable getInstance() {
        if (instance == null) {
            synchronized (DragonTable.class) {
                if (instance == null) {
                    instance = new DragonTable();
                }
            }
        }
        return instance;
    }

    private IDragonDAO iDragonDAO = DAOFactory.getDragonDAOInstance();

    /**
     * 一系列columnID常量.
     * */
    public static final String ID = "Id";
    public static final String NAME = "name";
    public static final String SEX = "sex";
    public static final String AGE = "age";
    public static final String PROFILE = "profile";
    public static final String TRAINING = "training";
    public static final String HEALTHY = "healthy";

    /**
     * 龙的表.
     * 设置列名、列宽
     */
    public void initDragonTreeTable(TreeTableView<Dragon> dragonTreeTableView, String[] columnName,
                                    double[] columnPrefWidth, String[] columnId) {
        //列的数量
        int columnNum = columnName.length;

        //这里的警告实在不会解决...
        Callback cellValueFactory = o -> {
            TreeTableColumn.CellDataFeatures<Dragon,Dragon> p = (TreeTableColumn.CellDataFeatures) o;
            return p.getValue().valueProperty();
        };

        for (int i = 0; i < columnNum; i++) {
            int finalI = i;
            TreeTableColumn<Dragon, Dragon> treeTableColumn = new TreeTableColumn<>(columnName[i]);
            //添加列
            dragonTreeTableView.getColumns().add(treeTableColumn);
            treeTableColumn.setCellValueFactory(cellValueFactory);
            //设置列的宽度
            treeTableColumn.setPrefWidth(columnPrefWidth[i]);
            //设置CellFactory,定义每一列单元格的显示
            treeTableColumn.setCellFactory((param) -> {
                return new DragonTableTreeCell(columnId[finalI]);
            });
        }

    }

    /**
     * 龙的表.
     * 初始化数据时，显示某个族群内的龙。
     * 根节点进行了隐藏
     * 对某个族群的龙操作
     * 即给"驯龙高手"提供.
     */
    public void initDragonTreeData(TreeTableView<Dragon> dragonTreeTableView, TreeItem<Dragon> dragonRoot,
                                   List<TreeItem<Dragon>> dragonTreeItemList, int dragonGroupId) {
        dragonTreeTableView.setRoot(dragonRoot);
        dragonTreeTableView.setShowRoot(false);

        flushDragon(dragonTreeItemList, dragonRoot, dragonGroupId);
    }

    /**
     * 龙的表.
     * 初始化数据时，显示所有的龙.
     * 根节点进行了隐藏
     * 重载，对所有的龙操作。
     * 即给"外邦人"提供.
     */
    public void initDragonTreeData(TreeTableView<Dragon> dragonTreeTableView, TreeItem<Dragon> dragonRoot,
                                   List<TreeItem<Dragon>> dragonTreeItemList) {
        dragonTreeTableView.setRoot(dragonRoot);
        dragonTreeTableView.setShowRoot(false);

        flushDragon(dragonTreeItemList, dragonRoot);
    }

    /**
     * 龙的表.
     * 单元格内容的显示
     */
    private static class DragonTableTreeCell extends TreeTableCell<Dragon, Dragon> {
        String columnID;

        public DragonTableTreeCell(String columnID) {
            this.columnID = columnID;
        }

        @Override
        protected void updateItem(Dragon item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || null == item) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);

                switch (columnID) {
                    case ID:
                        setText(String.valueOf(item.getDragonId()));
                        break;
                    case NAME:
                        setText(item.getName());
                        break;
                    case SEX:
                        setText(String.valueOf(item.getSex()));
                        break;
                    case AGE:
                        setText(String.valueOf(item.getAge()));
                        break;
                    case PROFILE:
                        setText(item.getProfile());
                        break;
                    case TRAINING:
                        setText(String.valueOf(item.isTraining()));
                        break;
                    case HEALTHY:
                        setText(String.valueOf(item.isHealthy()));
                        break;
                }
            }
        }
    }

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot.
     * 对某个族群的龙操作。
     */
    public void flushDragon(List<TreeItem<Dragon>> dragonTreeItemList,
                            TreeItem<Dragon> dragonRoot, int dragonGroupId) {
        dragonTreeItemList.clear();
        dragonRoot.getChildren().clear();
        List<Dragon> dragonList = iDragonDAO.getList(dragonGroupId);
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem<>(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }
        }
    }

    /**
     * 刷新trainerTreeItemList(储存treeItem的)和trainerRoot.
     * 对所有的龙操作。重载
     */
    public void flushDragon(List<TreeItem<Dragon>> dragonTreeItemList, TreeItem<Dragon> dragonRoot) {
        dragonTreeItemList.clear();
        dragonRoot.getChildren().clear();
        List<Dragon> dragonList = iDragonDAO.getList();
        if (dragonList != null) {
            for (Dragon dragon : dragonList) {
                TreeItem<Dragon> treeItem = new TreeItem<>(dragon);
                dragonTreeItemList.add(treeItem);
                dragonRoot.getChildren().add(treeItem);
            }
        }
    }
}
