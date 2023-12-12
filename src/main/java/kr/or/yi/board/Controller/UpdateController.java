package kr.or.yi.board.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfWriter;
    @FXML
    private TextArea taContent;
    @FXML
    private ImageView imageView;
    @FXML
    private  FileChooser fileChooser;
    @FXML
    private TextField tfFilePath;

    int boardNo;
    private Image image;
    private File file;
    Stage stage;

    BoardService boardService = new BoardServiceImpl();
    public void read(int boardNo) {
        this.boardNo = boardNo;
        Board board = boardService.select(boardNo);
        tfTitle.setText(board.getTitle());
        tfWriter.setText(board.getWriter());
        taContent.setText(board.getContent());
        if (board.getIsfile() != null) {
            image = new Image(board.getIsfile(), 200, 224, true, true);
            imageView.setImage(image);
        }
    }

    public void update(ActionEvent event) throws IOException {
        Board board = new Board(tfTitle.getText(), tfWriter.getText(), taContent.getText(), file);
        board.setBoard_no(boardNo);
        int result = boardService.update(board);
        if(result >0) {
            System.out.println("글수정 완료");
            moveToMain(event);
        }
    }
     public void moveToMain(ActionEvent event) throws IOException {
         SceneUtil.getInstance().switchScene(event, UI.LIST.getPath());
    }

    public void backToRead(ActionEvent event) throws IOException{
        ReadController readController = SceneUtil.getInstance().getController(UI.READ.getPath());
        readController.read(boardNo);
        Parent root = SceneUtil.getInstance().getRoot();
        SceneUtil.getInstance().switchScene(event, UI.READ.getPath(), root);
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
