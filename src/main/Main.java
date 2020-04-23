package main;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.IActivityDAO;
import util.Constants;
import util.DAOFactory;
import util.DBUtils;
import util.ViewManager;

import java.sql.Connection;

/**
 * 启动类.
 * */
public class Main extends Application {

    /**
     * 先加载登录界面.
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fx = ViewManager.openView(ViewManager.LOGIN_URL,null,420.0,280.0);

        //这里是判断用户是否有保存登陆信息，有的话自动登录
        LoginController loginController= (LoginController)fx.getController();
        loginController.init();

    }

    /**
     * 最后程序结束的时候，关闭连接对象connection
     * */
    @Override
    public void stop() throws Exception {
        super.stop();
//        Connection conn = DBUtils.getConnection();
//        DBUtils.close(conn,null,null);
    }

    public static void main(String[] args) {
        launch(args);

//        IActivityDAO iActivityDAO = DAOFactory.getActivityDAOInstance();
//        for(int i=0;i<20;i++){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    iActivityDAO.save(1,"a","good","2020","2020");
//                }
//            }).start();
//        }
    }
}
