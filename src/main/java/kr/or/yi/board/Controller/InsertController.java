package kr.or.yi.board.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kr.or.yi.board.DTO.Board;
import kr.or.yi.board.Main;
import kr.or.yi.board.Service.BoardService;
import kr.or.yi.board.Service.BoardServiceImpl;
import kr.or.yi.board.Utils.DragWindowUtil;
import kr.or.yi.board.Utils.SceneUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfWriter;
    @FXML
    private TextArea taContent;

    @FXML
    private FileChooser fileChooser;
    @FXML
    private TextField tfFilePath;
    @FXML
    private ImageView imageView;

    private Image image;
    private File file;

    Stage stage;

    BoardService boardService = new BoardServiceImpl();

    public void insert(ActionEvent event) throws IOException {
        Board board = new Board(tfTitle.getText(), tfWriter.getText(), taContent.getText(), file);
        int result = boardService.insert(board);
        if(result >0) {
            System.out.println("글쓰기 완료");
            moveToMain(event);
        }
    }

    public void moveToMain(ActionEvent event) throws IOException {
        System.out.println("목록보기 화면 전환");
        SceneUtil.getInstance().switchScene(event, UI.LIST.getPath());
    }

    public void fileUpload(ActionEvent event) throws IOException {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jepg", "*.gif"),
                new FileChooser.ExtensionFilter("Test Files", "*.txt"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.acc"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            tfFilePath.setText(file.getAbsolutePath());
            image = new Image(file.toURI().toString(), 180, 220, true, true);
            imageView.setImage(image);
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
