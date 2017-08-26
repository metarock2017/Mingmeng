package org.redrock.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Random;

public class DBUtil {
    public static final int JUDGE = 0;

    private Connection getconn() {
        Database database = Database.getInstance();
        return database.getConn();
    }

    private final Connection connection = this.getconn();

    public DBUtil() throws SQLException {
        connection.createStatement().executeQuery("SET NAMES utf8");
    }

    public int isCreater(String userid) throws SQLException {
        int result = 0;
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT `room_id` FROM `users` WHERE  `user_id` = '" + userid + "' AND `character_id`=0 ORDER BY `room_id` ASC ");
        while (resultSet.next())
            result = resultSet.getInt("room_id");
        return result;
    }

    public boolean createRoom(String creater_id) throws SQLException {
        String sql = "INSERT INTO `room` " +
                "(`creater_id`,`room_id`,`word_1`,`word_2`,`create_time`,`unused_time`)" +
                " VALUES (?,?,?,?,?,?)";
        Timestamp create_time = new Timestamp(System.currentTimeMillis());
        Timestamp unused_time = new Timestamp(System.currentTimeMillis() + 1800000);
        int room_id = getLastRoomId() + 1;
        String[] wordSet = this.getWords();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, creater_id);
        preparedStatement.setInt(2, room_id);
        preparedStatement.setString(3, wordSet[0]);
        preparedStatement.setString(4, wordSet[1]);
        preparedStatement.setTimestamp(5, create_time);
        preparedStatement.setTimestamp(6, unused_time);
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();
        preparedStatement.close();
        getIntoTheRoom(room_id, creater_id, JUDGE);
        return true;
    }

    public String updateRoomNums(int nums, int roomId) throws SQLException {
            int spys[] = randomArray(1, nums, 3);
            int undercoverNums=0;
            if (nums >= 4 && nums <= 6) {
                spys[1] = 99;
                spys[2] = 99;
                undercoverNums=1;
            } else if (nums >= 7 && nums <= 11) {
                spys[2] = 99;
                undercoverNums=2;
            }else
                undercoverNums=3;
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `room` SET " +
                    "`sum_peoples_nums` = ? , `spy_1_id`=? , `spy_2_id`=? , `spy_3_id`=? WHERE `room_id`=?");
            preparedStatement.setInt(1, nums);
            preparedStatement.setInt(2, spys[0]);
            preparedStatement.setInt(3, spys[1]);
            preparedStatement.setInt(4, spys[2]);
            preparedStatement.setInt(5, roomId);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            ResultSet resultSet=connection.createStatement().executeQuery("SELECT `word_1`,`word_2` FROM `room` WHERE `room_id`="+roomId+" ORDER BY `id` ASC ");
            String[] words=new String[2];
            while (resultSet.next()){
                words[0]=resultSet.getString("word_1");
                words[1]=resultSet.getString("word_2");
            }
            String responseText="您是法官，请让参与游戏的玩家对我回复["+roomId+"]进入房间。\n" +
                    "房    号："+roomId+"\n" +
                    "配    置："+undercoverNums+"个卧底，"+(nums-undercoverNums)+"个平民\n" +
                    "卧底词："+words[0]+"\n" +
                    "平民词："+words[1]+"\n" +
                    "卧    底："+spys[0]+"号;";
            if (spys[1]!=99)
                responseText=responseText+spys[1]+"号;";
            if (spys[2]!=99)
                responseText=responseText+spys[2]+"号;";
            return responseText;
    }

    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }


    public boolean getIntoTheRoom(int RoomId, String user_id, int character_id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` " +
                "(`user_id`,`room_id`,`character_id`) VALUES (?,?,?)");
        preparedStatement.setString(1, user_id);
        preparedStatement.setInt(2, RoomId);
        preparedStatement.setInt(3, character_id);
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();
        return true;
    }

    public String getIntoTheRoom(int RoomId, String userID) throws SQLException {
            int characterID = 0;
            characterID = getNumsInRoom(RoomId) + 1;
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` " +
                    "(`user_id`,`room_id`,`character_id`) VALUES (?,?,?)");
            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, RoomId);
            preparedStatement.setInt(3, characterID);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            ResultSet resultSet=connection.createStatement().executeQuery("SELECT " +
                    "`sum_peoples_nums`,`word_1`,`word_2`,`spy_1_id`,`spy_2_id`,`spy_3_id` FROM `room` WHERE `room_id`="+RoomId);
            int sumNums=0;
            String[] word=new String[2];
            int[] undercoverID=new int[3];
            while (resultSet.next()){
                sumNums=resultSet.getInt("sum_peoples_nums");
                word[0]=resultSet.getString("word_1");
                word[1]=resultSet.getString("word_2");
                undercoverID[0]=resultSet.getInt("spy_1_id");
                undercoverID[1]=resultSet.getInt("spy_2_id");
                undercoverID[2]=resultSet.getInt("spy_3_id");
            }
            int undercoverNums=1;
            if(undercoverID[1]!=99)
                undercoverNums=2;
            if(undercoverID[2]!=99)
                undercoverNums=3;
            if(characterID==undercoverID[0]||characterID==undercoverID[1]||characterID==undercoverID[2]) {
                return "房号：" + RoomId + "\n" +
                        "词语：" + word[0] + "\n" +
                        "你是：" + characterID + "号\n" +
                        "配置：" + undercoverNums + "个卧底，" + (sumNums - undercoverNums) + "个平民\n";
            }
            return "房号：" + RoomId + "\n" +
                    "词语：" + word[1] + "\n" +
                    "你是：" + characterID + "号\n" +
                    "配置：" + undercoverNums + "个卧底，" + (sumNums - undercoverNums) + "个平民\n";

    }

    private int getNumsInRoom(int roomId) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT count(`id`) AS `nums` FROM `users` WHERE `room_id`=" + roomId + " AND `character_id`>0");
        int result = 0;
        while (resultSet.next())
            result = resultSet.getInt("nums");
        return result;
    }

    private int getLastRoomId() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `room_id` FROM `room` ORDER BY `room_id` DESC LIMIT 0,1");
        int result = 0;
        while (rs.next()) {
            result = rs.getInt("room_id");
        }
        statement.close();
        return result;
    }


    private String[] getWords() {
        String[] result = new String[2];
        try {
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
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            writer.close();
            connection.disconnect();
            //System.out.println(builder.toString());
            JSONObject json = new JSONObject(builder.toString());
            result[0] = json.getString("pingmin");
            result[1] = json.getString("wodi");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean checkIsCreater(String userID) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT `id`,`sum_peoples_nums` FROM `room` WHERE `creater_id`='" + userID + "' ORDER BY `id` ASC");
        int resultNums = 0;
        int sumNums = 0;
        while (resultSet.next()) {
            resultNums = resultSet.getInt("id");
            sumNums = resultSet.getInt("sum_peoples_nums");
        }
        if (resultNums > 0 && sumNums == 0)
            return false;
        return true;
    }

    public int checkRoomStatus(int roomID,String userID) throws SQLException{
        ResultSet queryResult=connection.createStatement().executeQuery("SELECT `id`,`sum_peoples_nums` FROM `room` WHERE `room_id`="+roomID);
        int num=1;
        while (queryResult.next())
            num=queryResult.getInt("sum_peoples_nums");
        if (!queryResult.next()&&num==0)
            return 0;
        queryResult=connection.createStatement().executeQuery("SELECT `id` FROM `users` WHERE `user_id`='"+userID+"' AND `room_id`="+roomID);
        if (queryResult.next())
            return 2;
        return 1;
    }

    public boolean isFull(int roomID) throws SQLException{
        ResultSet userNumSet=connection.createStatement().executeQuery("SELECT count(`id`) AS `nums` FROM `users` WHERE `room_id`="+roomID+" AND `character_id`>0");
        ResultSet maxNumSet=connection.createStatement().executeQuery("SELECT `sum_peoples_nums` FROM `room` WHERE `room_id`="+roomID);
        int nowNums=0;
        int maxNums=0;
        while (userNumSet.next())
            nowNums=userNumSet.getInt("nums");
        while (maxNumSet.next())
            maxNums=maxNumSet.getInt("sum_peoples_nums");
        System.out.println(nowNums);
        System.out.println(maxNums);
        if(nowNums<maxNums)
            return true;
        return false;
    }

    public String getUserStatus(int roomID,String userID) throws SQLException{
        ResultSet resultSet=connection.createStatement().executeQuery("SELECT " +
                "`sum_peoples_nums`,`word_1`,`word_2`,`spy_1_id`,`spy_2_id`,`spy_3_id` FROM `room` WHERE `room_id`="+roomID);
        ResultSet userInfo=connection.createStatement().executeQuery("SELECT `character_id` FROM `users` WHERE `room_id`="+roomID+" AND `user_id`='"+userID+"'");
        int sumNums=0;
        String[] word=new String[2];
        int[] undercoverID=new int[3];
        int characterID=0;
        while (resultSet.next()){
            sumNums=resultSet.getInt("sum_peoples_nums");
            word[0]=resultSet.getString("word_1");
            word[1]=resultSet.getString("word_2");
            undercoverID[0]=resultSet.getInt("spy_1_id");
            undercoverID[1]=resultSet.getInt("spy_2_id");
            undercoverID[2]=resultSet.getInt("spy_3_id");
        }
        int undercoverNums=1;
        if(undercoverID[1]!=99)
            undercoverNums=2;
        if(undercoverID[2]!=99)
            undercoverNums=3;
        while (userInfo.next())
            characterID=userInfo.getInt("character_id");

        if(characterID==0){
            String res="您是法官，请让参与游戏的玩家对我回复["+roomID+"]进入房间。\n" +
                    "房    号："+roomID+"\n" +
                    "配    置："+undercoverNums+"个卧底，"+(sumNums-undercoverNums)+"个平民\n" +
                    "卧底词："+word[0]+"\n" +
                    "平民词："+word[1]+"\n" +
                    "卧    底："+undercoverID[0]+"号;";
            if (undercoverNums>1) {
                res = res + undercoverID[1] + "号;";
                if (undercoverNums>2) {
                    res = res + undercoverID[2] + "号;";
                }
            }


            return res;
        } else if(characterID==undercoverID[0]||characterID==undercoverID[1]||characterID==undercoverID[2])
            return "房号：" + roomID + "\n" +
                    "词语：" + word[0] + "\n" +
                    "你是：" + characterID + "号\n" +
                    "配置：" + undercoverNums + "个卧底，" + (sumNums - undercoverNums) + "个平民\n";
        else
            return "房号：" + roomID + "\n" +
                    "词语：" + word[1] + "\n" +
                    "你是：" + characterID + "号\n" +
                    "配置：" + undercoverNums + "个卧底，" + (sumNums - undercoverNums) + "个平民\n";

    }

}
