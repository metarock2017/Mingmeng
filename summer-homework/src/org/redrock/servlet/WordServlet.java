package org.redrock.servlet;

import org.redrock.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@WebServlet(name = "WordServlet",value = "/getWords")
public class WordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result="";
        URL url = new URL("http://zhuoyouwx.weapp.me/game/ajax_words");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        connection.getOutputStream(), "UTF-8"
                )
        );
        writer.flush();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(), "UTF-8"
                )
        );
        String line = null;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        writer.close();
        connection.disconnect();
        result = builder.toString();
        resp.getWriter().println(result);
    }


}
