package kr.or.yi.board.Utils;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kr.or.yi.board.Controller.BaseControllerInterface;

public class DragWindowUtil {
//    private double x = 0;
//    private double y = 0;
//    Stage stage;
//    @FXML
//    private AnchorPane rootPane;
//    public void HandleMouseClicked(MouseEvent event) {
//        Platform.runLater(() -> {
//            rootPane = (AnchorPane) event.getTarget();
//            stage = (Stage) rootPane.getScene().getWindow();
//
//            rootPane.setOnMousePressed((event1) -> {
//                x = event1.getSceneX();
//                y = event1.getSceneY();
//            });
//
//            rootPane.setOnMouseDragged((event1) -> {
//                stage.setX(event1.getScreenX() - x);
//                stage.setY(event1.getScreenY() - y);
//                stage.setOpacity(0.8f);
//            });
//
//            rootPane.setOnMouseReleased((event1) -> {
//                stage.setOpacity(1.0f);
//            });
//        });
//    }
//    public void makeDraggable(Stage stage, Parent root) {
//        root.setOnMousePressed((MouseEvent event) -> {
//            x = event.getSceneX();
//            y = event.getSceneY();
//        });
//
//        root.setOnMouseDragged((MouseEvent event) -> {
//            stage.setX(event.getScreenX() - x);
//            stage.setY(event.getScreenY() - y);
//            stage.setOpacity(0.8);
//        });
//
//        root.setOnMouseReleased((MouseEvent event) -> {
//            stage.setOpacity(1);
//        });
//    }
//
//    public void setStageStyle(Stage stage) {
//        stage.initStyle(StageStyle.TRANSPARENT);
//    }

    public static void addDragListeners(Stage stage, Parent root) {
        final double[] x = { 0.0 };
        final double[] y = { 0.0 };

        root.setOnMousePressed(event -> {
            x[0] = event.getSceneX();
            y[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x[0]);
            stage.setY(event.getScreenY() - y[0]);
        });
    }


}