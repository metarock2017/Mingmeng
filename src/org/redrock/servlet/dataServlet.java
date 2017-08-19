package org.redrock.servlet;

import org.redrock.save.newDButil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dataservlet",value = "/data")
public class dataServlet extends HttpServlet {
    public class useClass{
        public String sort;
        public String sortby;
        public String name;
        public String stuId;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        useClass temp=new useClass();
        temp.sort=req.getParameter("sort");
        temp.sortby=req.getParameter("sortby");
        temp.name=req.getParameter("name");
        temp.stuId=req.getParameter("stuId");
        System.out.println(req.getParameter("name"));
        newDButil exe=new newDButil();
        List result=exe.findTarget(temp.sortby,temp.sort,temp.name,temp.stuId);
        System.out.println(result.size());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
