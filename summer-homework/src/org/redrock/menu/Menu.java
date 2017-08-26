package org.redrock.menu;

import org.redrock.component.Support;
import org.redrock.util.CurlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void main(String[] args) {
        Menu menu = new Menu();

        Button button1 = new Button();
        button1.setType("click");
        button1.setName("谁是卧底");
        button1.setKey("V1001_TODAY_MUSIC");

        Button button2 = new Button();
        button2.setType("click");
        button2.setName("修改词语");
        button2.setKey("changeWords");

        Button WhoIsSpy=new Button();
        WhoIsSpy.setType("click");
        WhoIsSpy.setName("创建房间");
        WhoIsSpy.setKey("NewRoom");

        Button button3 = new Button();
        button3.setType("view");
        button3.setName("join us");
        button3.setKey("JoinUs");
        button3.setUrl("http://hongyan.cqupt.edu.cn/aboutus/");

        button1.addButton(WhoIsSpy);
        menu.addButton(WhoIsSpy);
        menu.addButton(button2);
        menu.addButton(button3);

        String result = null;
        try {
            result = updateMenu(menu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    public static String updateMenu(Menu menu) throws JSONException {
        String accessToken = Support.getAccessToken();
        System.out.println(accessToken);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        String menuData = menu.toJson();
        return CurlUtil.postData(url, menuData);
    }

    private List<Button> button;

    public String toJson() throws JSONException {
        JSONArray buttonData = new JSONArray();
        JSONObject menuData = new JSONObject();
        for (int i = 0; i < button.size(); i++) {
            Button b = button.get(i);
            buttonData.put(i, b.toJson());
        }
        menuData.put("button", buttonData);
        return menuData.toString();
    }

    public void addButton(Button button) {
        if (this.button == null) {
            this.button = new ArrayList<>();
        }
        this.button.add(button);
    }
}
