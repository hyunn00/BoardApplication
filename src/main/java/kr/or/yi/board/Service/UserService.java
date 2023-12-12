package kr.or.yi.board.Service;

import kr.or.yi.board.DTO.Board;
import kr.or.yi.board.DTO.User;

import java.util.List;

public interface UserService {
    User select(String userid);
    int insert(User user);
    int update(User user);
    int delete(String userid);
}
