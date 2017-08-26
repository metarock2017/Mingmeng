package org.redrock.controller;

import org.redrock.component.Support;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
/**
 * Created by jx on 2017/7/20.
 */
@WebServlet(name = "IndexServlet"/*, value = "/"*/)
public class IndexServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> receiveMessage = Support.receiveMessage(request, response);
        System.out.println(receiveMessage.toString());
        Support.getReplyMessage(request, response, receiveMessage);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Support.bindServer(request, response);
    }

//    @Override
//    public void init() throws ServletException {
//        super.init();
//        Jedis jedis = JedisUtil.getJedis();
//        if (jedis == null) {
//            throw new ServletException("redis init failed!");
//        } else {
//            jedis.close();
//        }
//    }
}
