package org.redrock.save;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class newDButil {
    private Connection connection=Database.getInstance().getConn();
    public List findTarget(String sortby, String sort, String name, String stuId){
        String sql="";
        List result=new ArrayList();
        try{
            System.out.println(name);
            sql="SELECT * From student WHERE `stuId` like '%"+stuId+"%' and `name` like '%"+name+"%'  ORDER BY "+sortby+" "+sort;
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            System.out.println(sql);
            stu tempStu=new stu();
            while (resultSet.next()){
                tempStu.id=resultSet.getInt("id");
                tempStu.stuId=resultSet.getString("stuId");
                tempStu.name=resultSet.getString("name");
                tempStu.gender=resultSet.getInt("gender");
                tempStu.grade=resultSet.getInt("grade");
                tempStu.college=resultSet.getString("college");
                tempStu.major=resultSet.getString("major");
                tempStu.classId=resultSet.getString("class");
                result.add(tempStu);
                System.out.println(tempStu+"e");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


}
