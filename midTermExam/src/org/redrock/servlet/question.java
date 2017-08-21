package org.redrock.servlet;

import com.google.gson.Gson;
import org.redrock.save.DBUtil;
import org.redrock.save.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "question",value = "/resetpassword")
public class question extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        user user=new user();
        user.setName(req.getParameter("username"));
        user.setPassword(req.getParameter("password"));
        user.answer=req.getParameter("answer");
        DBUtil DB=new DBUtil();
        Map<String,String> info=new HashMap<>();
        String result="";
        try{
            if(user.getName()!=null&&user.answer!=null)
            {
                if(DB.checkUser(user.getName()))
                    result=DB.checkAnswer(user.getName(),user.getPassword(),user.answer);
                else
                    result="the user is not existed";
            }
            else
                result="one of parameters is null";
        }catch (Exception e){
            e.printStackTrace();
        }
        info.put("info",result);
        String json=new Gson().toJson(info);

        PrintWriter out=new PrintWriter(resp.getOutputStream());
        out.print(json);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
