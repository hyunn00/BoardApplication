module kr.or.yi.boardappilcation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires mysql.connector.j;


    opens kr.or.yi.board to javafx.fxml;
    opens kr.or.yi.board.Controller to javafx.fxml;
    opens kr.or.yi.board.DTO to javafx.base;
    exports kr.or.yi.board;
    exports kr.or.yi.board.Utils;
    opens kr.or.yi.board.Utils to javafx.fxml;
}