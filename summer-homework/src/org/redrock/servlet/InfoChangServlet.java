package org.redrock.servlet;

import org.redrock.util.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "InfoChangeServlet",value = "/changeInfo")
public class InfoChangServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Database database = Database.getInstance();
            Connection connection = database.getConn();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `room` SET `word_1`=?,`word_2`=? WHERE `creater_id`=?");
            preparedStatement.setString(1,req.getParameter("wodi"));
            preparedStatement.setString(2,req.getParameter("pingmin"));
            preparedStatement.setString(3,req.getParameter("userId"));
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().println("修改成功,可以关闭页面了");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
