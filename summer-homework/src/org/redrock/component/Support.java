package org.redrock.component;


import org.redrock.aes.AesException;
import org.redrock.aes.WXBizMsgCrypt;
import org.redrock.message.BaseMessage;
import org.redrock.message.handle.BaseMessageHandle;
import org.redrock.message.handle.TextMessageHandle;
import org.redrock.util.SignUtil;
import org.redrock.message.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.redrock.util.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class Support {

    private static final String FORMAT = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";

    public static void bindServer(HttpServletRequest request, HttpServletResponse response) {
        try {
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String signature = request.getParameter("signature");
            String echostr = request.getParameter("echostr");
            if (SignUtil.checkSignature(timestamp, nonce, signature, echostr)) {
                response.getWriter().print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> receiveMessage(HttpServletRequest request, HttpServletResponse response) {
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String signature = request.getParameter("signature");
        String token = Const.Token;
        String encodingAESKey = Const.EncodingAESKey;
        String appId = Const.AppId;
        Map<String, String> data = null;
        try {
            if (!StringUtil.hasBlank(timestamp, nonce, signature)) {
                InputStream inputStream = request.getInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document dom = builder.parse(inputStream);
                data = StringUtil.parseXml(dom);
                if (ifEncrypt(request)) {
                    String msgSignature = request.getParameter("msg_signature");
                    String postData = String.format(FORMAT, data.get("Encrypt"));
                    WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appId);
                    String decodeXml = pc.decryptMsg(msgSignature, timestamp, nonce, postData);
                    StringReader sr = new StringReader(decodeXml);
                    InputSource source = new InputSource(sr);
                    Document decodeDom = builder.parse(source);
                    data = StringUtil.parseXml(decodeDom);
                }
            }
        } catch (AesException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            return data;
        }
    }

    public static String getAccessToken() throws JSONException {
        Jedis jedis = JedisUtil.getJedis();
        String key = Const.KeyOfAccessToken;
        if (isInvalid(jedis, key)) {
            synchronized (Support.class) {
                if (isInvalid(jedis, key)) {
                    Map<String, Object> data = curlForAccessToken();
                    String accessToken = (String) data.get("accessToken");
                    int expires_in = (int) data.get("expiresIn");
                    long timestamp = System.currentTimeMillis() + expires_in * 1000;
                    jedis.hset(key, "accessToken", accessToken);
                    jedis.hset(key, "timestamp", timestamp + "");
                    jedis.expire(key, expires_in);
                }
            }
        }
        return jedis.hget(key, "accessToken");
    }

    private static boolean isInvalid(Jedis jedis, String key) {
        return !jedis.exists(key) ||
                StringUtil.hasBlank(jedis.hget(key, "accessToken"), jedis.hget(key, "timestamp")) ||
                Long.valueOf(jedis.hget(key, "timestamp")) < System.currentTimeMillis();
    }

    private static Map<String, Object> curlForAccessToken() throws JSONException {
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, Object> params = new HashMap<>();
        params.put("appid", Const.AppId);
        params.put("secret", Const.AppSecret);
        params.put("grant_type", "client_credential");
        String jsonStr = CurlUtil.getContent(url, params, "GET");
        Map<String, Object> result = new HashMap<>();
        JSONObject json = new JSONObject(jsonStr);
        String accessToken = json.getString("access_token");
        int expiresIn = json.getInt("expires_in");
        result.put("accessToken", accessToken);
        result.put("expiresIn", expiresIn);
        return result;
    }

    public static void getReplyMessage(HttpServletRequest request, HttpServletResponse response, Map<String, String> receiveMessage) {
        try {
            String messageType = receiveMessage.get("MsgType");
            BaseMessageHandle handle;
            BaseMessage message = null;
            switch (messageType) {
//                {Content=fsdfsdf, CreateTime=1500813625, ToUserName=gh_b6a171776f25, FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk, MsgType=text, MsgId=6445945437180380102}
                case "text":
                    handle = new TextMessageHandle(receiveMessage);
                    message = handle.processRequest();
                    break;
//                {MediaId=PL4cME3axznx-l9VktVKLyXNvVgbd3cSw83uAWOWDt9VtAeD6kokOabgY1Lh-b9f, CreateTime=1500813904, ToUserName=gh_b6a171776f25, FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk, MsgType=image, PicUrl=http://mmbiz.qpic.cn/mmbiz_jpg/47SbIhETSCpmq5auHVcOjoNMiax7fEyeECBLKV0w2zZhGxEgMicVLpxMlaqqBbaDFHSutHEB1IygsnciatPlYr7Mg/0, MsgId=6445946635476255707}
//            case "image" :
//                break;
//                {Format=amr, MediaId=kUIyyyYmDLVAnEDNMpd_atiWk8Ry1JOp3vLKxfmopkVHDhwphtsz95HKSBE4AceX, CreateTime=1500813944, ToUserName=gh_b6a171776f25, FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk, MsgType=voice, MsgId=6445946806860775424, Recognition=哈哈哈！}
//            case "voice" :
//                break;

//            case "video" :
//                break;
//                {Location_X=29.532058, CreateTime=1500813982, Location_Y=106.606881, Label=重庆市南岸区重庆邮电大学, Scale=15, ToUserName=gh_b6a171776f25, FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk, MsgType=location, MsgId=6445946970483704801}
//            case "location" :
//                break;
//            case "link" :
//                break;
//            {CreateTime=1500813720, EventKey=, Event=unsubscribe, ToUserName=gh_b6a171776f25, FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk, MsgType=event}
                case "event" :
                    break;
                default:
                    break;
            }
            if (message != null) {
                String result = ifEncrypt(request) ? message.encodeXml(request) : message.toXml();
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean ifEncrypt (HttpServletRequest request) {
        String encryptType = request.getParameter("encrypt_type");
        return encryptType != null && "aes".equals(encryptType);
    }
}

