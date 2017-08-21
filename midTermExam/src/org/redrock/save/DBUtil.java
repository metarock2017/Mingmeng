package org.redrock.save;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.sql.*;

public class DBUtil {
    private Connection getConn(){
        Database database=Database.getInstance();
        Connection connection=database.getConn();
        return connection;
    }

    public static String getBase64(String str)
    {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }


    public boolean checkUser(String username){
        String sql="SELECT `id` FROM users WHERE `user`= "+username;

        boolean response=false;
        try{
            Statement statement=this.getConn().createStatement();
            ResultSet rs= statement.executeQuery(sql);
            boolean queryResult=rs.next();
            //System.out.println(queryResult);
            response=queryResult;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return response;
    }

    public boolean saveUser(String username,String password,String question,String answer){
        boolean checkPassword=password.matches(("^.*[0-9]+.*$"));
        if(!checkPassword)
        {
            return false;
        }
        String sql ="INSERT INTO users (`user`,`password`,`questionForProtected`,`answerForProtected`) VALUES (?,?,?,?)";
        Connection connection=this.getConn();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,getBase64(password));
            preparedStatement.setString(3,question);
            preparedStatement.setString(4,answer);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }


    public boolean isRealUser(String username,String password) {
        String sql="SELECT * FROM users WHERE `user` = "+username;
        Connection connection=this.getConn();
        boolean result=false;
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            String passwordInDB="";
            while (resultSet.next())
                passwordInDB=resultSet.getString("password");

            if(passwordInDB.compareTo(getBase64(password))==0)
                result=true;
            else
                result=false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String checkAnswer(String username,String password,String answer){
        Connection connection=this.getConn();
        String sql="SELECT `answerForProtected` FROM users WHERE user ="+username;
        String result="";
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            String answerInDB="";
            while (resultSet.next())
                answerInDB=resultSet.getString("answerForProtected");
            if(answerInDB.compareTo(answer)==0)
            {
                sql="UPDATE users SET `password` = ? WHERE `user`="+username;
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,getBase64(password));
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
                result="update password successfully";
            }
            else
                result="answer is wrong";
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
