package kr.or.yi.board;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import kr.or.yi.board.Controller.ReadController;
import kr.or.yi.board.Controller.UI;
import kr.or.yi.board.DTO.Board;
import kr.or.yi.board.DTO.User;
import kr.or.yi.board.Service.BoardService;
import kr.or.yi.board.Service.BoardServiceImpl;
import kr.or.yi.board.Service.UserService;
import kr.or.yi.board.Service.UserServiceImpl;
import kr.or.yi.board.Utils.DragWindowUtil;
import kr.or.yi.board.Utils.SceneUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Board> boardTableView;
    @FXML
    private TableColumn<Board, Integer> colBoardNo;
    @FXML
    private TableColumn<Board, String> colTitle;
    @FXML
    private TableColumn<Board, String> colWriter;
    @FXML
    private TableColumn<Board, String> colRegDate;
    @FXML
    private TableColumn<Board, String> colUpdDate;
    @FXML
    private TableColumn<Board, CheckBox> colCbDelete;
    @FXML
    private CheckBox cbAll;
    @FXML
    private Pagination pagination;
    @FXML
    private Label logout;
    @FXML
    private Label welcome;

    private int totalCount = 0;
    private final int pageSize = 10;

    public int getPageSize() {
        return pageSize;
    }

    private int totalPage;

    BoardService boardService = new BoardServiceImpl();
    UserService userService = new UserServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalCount = boardService.totalListCount();
        totalCount = totalCount == 0 ? 1 : totalCount;
        totalPage = totalCount / pageSize;
        if (totalPage % pageSize > 0) {
            ++totalPage;
        }

        pagination.setPageCount(totalPage);
        pagination.setMaxPageIndicatorCount(pageSize);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer integer) {
                pageListAll(pagination.getCurrentPageIndex());
                return new Label(String.format("현재의 페이지 : %d", pagination.getCurrentPageIndex()+1));
            }
        });

        boardTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2 && boardTableView.getSelectionModel().getSelectedItem() != null) {
                    int boardNo = boardTableView.getSelectionModel().getSelectedItem().getBoard_no();
                    System.out.println(boardNo);
                    try {
                        ReadController readController = SceneUtil.getInstance().getController(UI.READ.getPath());
                        readController.read(boardNo);
                        Parent root = SceneUtil.getInstance().getRoot();
                        SceneUtil.getInstance().switchScene(mouseEvent, UI.READ.getPath(), root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        __initializeMouseHandler();
    }

    private void __initializeMouseHandler() {
        Platform.runLater( () -> {
            DragWindowUtil.addDragListeners((Stage) rootPane.getScene().getWindow(), rootPane);
        });
    };

    public void read(String userid) {
        User user = userService.select(userid);
        welcome.setText(String.format("%s님 환영합니다.", user.getUserid()));
        System.out.println(user.getUserid());
    }
    public void pageListAll(int pageIndex) {
        List<Board> boardList;
        boardList = boardService.pageList(pageIndex);
        totalCount = boardList.size();

        ObservableList<Board> list = FXCollections.observableArrayList(boardList);

        colCbDelete.setCellValueFactory(new PropertyValueFactory<>("CbDelete"));
        colBoardNo.setCellValueFactory(new PropertyValueFactory<>("Board_no"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colWriter.setCellValueFactory(new PropertyValueFactory<>("Writer"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("Reg_date"));
        colUpdDate.setCellValueFactory(new PropertyValueFactory<>("Upd_date"));
        boardTableView.setItems(list);

        cbAll.setSelected(false);
        cbAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CheckBox checkBox = (CheckBox) mouseEvent.getSource();
                boolean checkAll = checkBox.isSelected();
                boardTableView.getItems().forEach(board -> {
                    board.getCbDelete().setSelected(checkAll);
                });
            }
        });
    }

    public void moveToInsert(ActionEvent event) throws IOException {
        System.out.println("글등록 화면 전환");
        SceneUtil.getInstance().switchScene(event, UI.INSERT.getPath());
    }

    public void deleteSelected(ActionEvent event) throws IOException {
        boardTableView.getItems().forEach(board -> {
            CheckBox cbDelete = board.getCbDelete();
            boolean checked = cbDelete.isSelected();
            if(checked) {
                int boardNo = board.getBoard_no();
                boardService.delete(boardNo);
            }
        });

        initialize(null, null);
    }

    public void logoutMethod(MouseEvent event) throws IOException {
        SceneUtil.getInstance().switchScene(event, UI.LOGIN.getPath());
    }

    public void close(ActionEvent event) {
        SceneUtil.getInstance().close(event);
    }
}