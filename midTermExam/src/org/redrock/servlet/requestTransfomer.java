package org.redrock.servlet;


import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "request",value = "/transfomer")
public class requestTransfomer extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{

        String url = "http://172.20.2.52:84/userpostservice.asmx";
        String test = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "<soap12:Body>\n" +
                "    <GetUserPostTypes xmlns=\"http://172.20.2.52:84/\" />\n" +
                "</soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = postData(url, test);
        System.out.println(result);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1 = factory.newDocumentBuilder();
        Document document = builder1.parse(
                    new InputSource(
                            new StringReader(result)
                    )
            );
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getFirstChild().getFirstChild().getChildNodes();

            Map<String,String> test1=new HashMap<>();
            Map<String,Map<String,String>> test2=new HashMap<>();
            for(int i=0;i<nodeList.getLength();i++)
                test1.put(nodeList.item(i).getNodeName(),nodeList.item(i).getTextContent());
            test2.put(element.getFirstChild().getFirstChild().getNodeName(),test1);
            Map<String, Map<String, Map<String, String>>> test3=new HashMap<>();
            test3.put(element.getFirstChild().getNodeName(),test2);
            Map<String, Map<String, Map<String, Map<String, String>>>> test4=new HashMap<>();
            test4.put(element.getNodeName(),test3);
            JSONObject json=new JSONObject(test4);
            PrintWriter out=new PrintWriter(resp.getOutputStream());
            out.print(json);
            out.flush();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }



    public static String postData(String strUrl, String data) {
        String result = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction","http://172.20.2.52:84/GetUserPostTypes");
            connection.setRequestProperty("Host","172.20.2.52");
            connection.setConnectTimeout(3000);
            connection.connect();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            connection.getOutputStream(), "UTF-8"
                    )
            );
            writer.write(data);
            writer.flush();
            int code=connection.getResponseCode();
            if(code==connection.HTTP_INTERNAL_ERROR){
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                connection.getErrorStream(), "UTF-8"
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
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
