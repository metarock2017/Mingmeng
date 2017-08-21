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

@WebServlet(name = "login",value = "/log")
public class loginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        user user=new user();
        user.setName(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        DBUtil DB=new DBUtil();
        Map<String,String> info=new HashMap<>();
        String result="";
        if(DB.checkUser(user.getName()))
        {
            if(DB.isRealUser(user.getName(),user.getPassword())==true)
                result="login success!";
            else
                result="your password is not true!";
        }
        else
            result="this user is not existed!";



        info.put("info",result);
        String json=new Gson().toJson(info);

        PrintWriter out=new PrintWriter(response.getOutputStream());
        out.print(json);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
