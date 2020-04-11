package main;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 启动类.
 * */
public class Main extends Application {

    /**
     * 先加载登录界面.
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(fx.getClassLoader().getResource("view/login.fxml"));
        Pane root = (Pane) fx.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setHeight(280);
        primaryStage.setWidth(420);
        primaryStage.show();

        //这里是判断用户是否有保存登陆信息，自动登录
        LoginController loginController= (LoginController)fx.getController();
        loginController.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
