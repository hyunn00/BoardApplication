package kr.or.yi.board.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReadController implements Initializable{
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

    private Image image;

    BoardService boardService = new BoardServiceImpl();
    int boardNo;
    List<Integer> numArr = new ArrayList<>();
    int targetValue = boardNo;
    int preValue = -1;
    int nextValue = -1;

    private void __initializeMouseHandler() {
        Platform.runLater( () -> {
            DragWindowUtil.addDragListeners((Stage) rootPane.getScene().getWindow(), rootPane);
        });
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        __initializeMouseHandler();
    }

    public void read(int boardNo) {
        numArr = listNum();
        this.targetValue = boardNo;
        this.boardNo = boardNo;
        Board board = boardService.select(boardNo);
        tfTitle.setText(board.getTitle());
        tfWriter.setText(board.getWriter());
        taContent.setText(board.getContent());
        if (board.getIsfile() != null) {
            image = new Image(board.getIsfile(), 200, 224, true, true);
            imageView.setImage(image);
        } else {
            image = new Image(String.valueOf(Main.class.getResource("images/no-image.png")));
            imageView.setImage(image);
        }
    }

    public void moveToMain(ActionEvent event) throws IOException {
        System.out.println("목록보기 화면 전환");
        SceneUtil.getInstance().switchScene(event, UI.LIST.getPath());
    }
    public void moveToUpdate(ActionEvent event) throws IOException {
        UpdateController updateController = SceneUtil.getInstance().getController(UI.UPDATE.getPath());
        updateController.read(boardNo);

        Parent root = SceneUtil.getInstance().getRoot();
        SceneUtil.getInstance().switchScene(event, UI.UPDATE.getPath(), root);
    }
    public void delete(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("게시물 삭제");
        alert.setHeaderText("정말로 삭제하시겠습니까?");
        alert.setContentText(tfTitle.getText() + " 게시물이 삭제됩니다." +
                "\n" + "삭제 후에는 되돌릴 수 없습니다.");

        int result = 0;
        if(alert.showAndWait().get() == ButtonType.OK) {
            result = boardService.delete(boardNo);
        }

        if(result > 0) {
            System.out.println("게시물 삭제");
            moveToMain(event);
        }
    }

    public void moveToPrev(ActionEvent event) {
//        List<Integer> numArr = listNum();
//        int nextBoardNo =  numArr.indexOf( boardNo ) - 1;
//        if ( 0 <= nextBoardNo )
//            read( numArr.get( nextBoardNo ) );
//        if(boardNo > 0) {
//            boardNo--;
//            read(listNum().get(showNum(boardNo)));
//        }
        numArr = listNum();
        read(preValue);
    }


    public void moveToNext(ActionEvent event) {
        numArr = listNum();
        read(nextValue);
    }

    public List<Integer> listNum() {
        List<Board> boardList = new ArrayList<>();
        boardList = boardService.list();
        System.out.println("111->"+ boardList.size());
        numArr.clear();
        for (Board board : boardList) {
            numArr.add(board.getBoard_no());
        }
        targetValue = boardNo;

        int idx = numArr.indexOf(targetValue);
        if(idx > 0)
            preValue = numArr.get(idx - 1);
        if(idx < numArr.size() -1)
            nextValue = numArr.get(idx + 1);

        return numArr;
    }
}
