package widget;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.util.Optional;

/**
 * 这里将对话框封装成工具类
 * */
public class DialogTool {
    private DialogTool(){

    }

    public static Dialog<ButtonType> dialog(String dialogTitle, Node dialogPaneContent,
                                            String okBtnText, String cancelBtnText){
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(dialogPaneContent);

        if(cancelBtnText != null){
            ButtonType cancel = new ButtonType(cancelBtnText, ButtonBar.ButtonData.CANCEL_CLOSE);
            dialogPane.getButtonTypes().add(cancel);
        }
        if(okBtnText != null){
            ButtonType ok = new ButtonType(okBtnText, ButtonBar.ButtonData.OK_DONE);
            dialogPane.getButtonTypes().add(ok);
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(dialogTitle);
        return dialog;
    }
}
