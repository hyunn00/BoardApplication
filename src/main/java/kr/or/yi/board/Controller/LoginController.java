package kr.or.yi.board.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kr.or.yi.board.DTO.User;
import kr.or.yi.board.MainController;
import kr.or.yi.board.Service.UserService;
import kr.or.yi.board.Service.UserServiceImpl;
import kr.or.yi.board.Utils.DragWindowUtil;
import kr.or.yi.board.Utils.SceneUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField tfPw;
    @FXML
    private TextField tfId;

    private UserService userService = new UserServiceImpl();
    private Alert alert;
    public void login(ActionEvent event) throws IOException {
        if (tfId.getText().isEmpty() || tfPw.getText().isEmpty()) {
            showAlert(ErrorCode.LOGIN_INPUT_ERROR, null, Alert.AlertType.ERROR);
            return;
        }  else {
            User user = userService.select(tfId.getText());
            System.out.println(user);
            if (user.getUserid().isEmpty()) {
                showAlert(ErrorCode.USER_NOT_FOUND, null, Alert.AlertType.ERROR);
                return;
            } else {
                if (!user.getPassword().equals(tfPw.getText())) {
                    showAlert(ErrorCode.LOGIN_FAILED, null, Alert.AlertType.ERROR);
                    return;
                } else {
                    showAlert(ErrorCode.LOGIN_SUCCESS, user.getUserid(), Alert.AlertType.INFORMATION);

                    MainController mainController = SceneUtil.getInstance().getController(UI.LIST.getPath());
                    mainController.read(tfId.getText());
                    Parent root = SceneUtil.getInstance().getRoot();
                    SceneUtil.getInstance().switchScene(event, "", root);
//                    SceneUtil.getInstance().switchScene(event, UI.LIST.getPath());
                }
            }
        }
//        MainController mainController = new MainController();
    }

    public void close(ActionEvent event) {
        System.exit(0);
    }

    public void showAlert(String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showAlert(ErrorCode errorCode, String parameter, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        String message = StringResources.getErrorMessage(errorCode);
        if (parameter != null) {
            message = String.format(message, parameter);
        }
        showAlert("로그인 " + (alertType == Alert.AlertType.ERROR ? "오류" : "알림"), null, message);
    }



    public enum ErrorCode {
        LOGIN_INPUT_ERROR,
        USER_NOT_FOUND,
        LOGIN_FAILED,
        LOGIN_SUCCESS
    }
    public class StringResources {
        private static final Map<ErrorCode, String> errorMessages = new HashMap<>();

        static {
            errorMessages.put(ErrorCode.LOGIN_INPUT_ERROR, "ID나 PW를 입력하지 않았습니다.");
            errorMessages.put(ErrorCode.USER_NOT_FOUND, "사용자가 없습니다.");
            errorMessages.put(ErrorCode.LOGIN_FAILED, "비밀번호가 일치하지 않습니다.\n다시 확인 바랍니다.");
            errorMessages.put(ErrorCode.LOGIN_SUCCESS, "%s님, 환영합니다.\n프로그램에 접속합니다.");
        }

        public static String getErrorMessage(ErrorCode errorCode) {
            return errorMessages.get(errorCode);
        }
    }

    private void __initializeMouseHandler() {
        Platform.runLater( () -> {
            DragWindowUtil.addDragListeners((Stage) rootPane.getScene().getWindow(), rootPane);
        });
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        __initializeMouseHandler();
    }
}
