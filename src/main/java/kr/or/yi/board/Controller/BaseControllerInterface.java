package kr.or.yi.board.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import kr.or.yi.board.Utils.DragWindowUtil;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseControllerInterface implements Initializable {
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        __initializeMouseHandler( getRootPane() );
    }
    public abstract Parent getRootPane();
    private void __initializeMouseHandler( Parent rootPanel_ ) {
        Platform.runLater( () -> {
            DragWindowUtil.addDragListeners((Stage) rootPanel_.getScene().getWindow(), rootPanel_);
        });
    }

    @FXML
    public void onClose(ActionEvent event) {
        System.exit( 0 );
    }
}
