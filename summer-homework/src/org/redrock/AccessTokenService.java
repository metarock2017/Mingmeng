package org.redrock;

import com.google.gson.Gson;
import org.redrock.util.Const;
import org.redrock.util.CurlUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class AccessTokenService {
    public static Jedis j = new Jedis("localhost");

    public static String getAccessToken(){
        String access_token = j.get("access_token");

        if ( access_token == null || access_token.equals("null")) {
            System.out.println("AccessToken was out of time");
            String accessTokenTemp = sendGet();

            setAccessToken(accessTokenTemp);

            return accessTokenTemp;

        }else {
            return access_token;
        }
    }

    public static String sendGet(){
        String accessTokenJson = CurlUtil.getContent(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Const.AppId +
                        "&secret=" + Const.AppSecret + "",
                null,
                "GET");

        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(accessTokenJson,Map.class);

        return map.get("access_token");
    }

    public static void setAccessToken(String accessToken){
        j.set("access_token",accessToken);

        j.expire("access_token",7200);
    }

    public static void main(String[] args) {
        System.out.println(AccessTokenService.getAccessToken());
    }
}
