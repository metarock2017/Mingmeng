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


@WebServlet(name = "register", value = "/reg")
public class IndexServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request ,HttpServletResponse response) throws ServletException,IOException {
        try{
            user user=new user();
            user.setName(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.question=request.getParameter("question");
            user.answer=request.getParameter("answer");
            DBUtil DB=new DBUtil();
            Map<String,String> info=new HashMap<>();
            String result="";
            if(user.getName()!=null&&user.getPassword()!=null&&user.question!=null&&user.answer!=null) {
                if (DB.checkUser(user.getName())==false) {
                    boolean saveResult = DB.saveUser(user.getName(), user.getPassword(), user.question, user.answer);
                    if (!saveResult)
                        result = "your name or password is not normal";
                    else
                        result="register success!";
                } else
                    result = "this user is existed in the database!";
            }
            else
                result="one of name,password,question,answer is null";

            info.put("info",result);
            String json=new Gson().toJson(info);

            PrintWriter out=new PrintWriter(response.getOutputStream());
            out.print(json);
            out.flush();



        }catch(IOException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws  ServletException,IOException{
        this.doPost(request,response);
    }
}
