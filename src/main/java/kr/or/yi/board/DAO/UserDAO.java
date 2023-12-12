package kr.or.yi.board.DAO;

import kr.or.yi.board.DTO.User;

import java.io.IOException;
import java.sql.SQLException;

public class UserDAO extends JDBCConnection{

    public int insert(User user){

        return 0;
    }

    public User select(String userid){
        User user = new User();
        String sql = "SELECT * "
                + " FROM User"
                + " WHERE userid = ?";

        try{
            psmt = con.prepareStatement(sql);
            psmt.setString(1, userid);
            rs = psmt.executeQuery();

            if(rs.next()) {
                user.setUserid(rs.getString("userid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            } else {
                System.out.println(userid + " 사용자가 없습니다.");
                userid=null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int update(User user){

        return 0;
    }

    public int delete(String userid){

        return 0;
    }
}