package org.redrock.util;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RoomStatus {
    private Connection getConn() {
        Database database = Database.getInstance();
        return database.getConn();
    }

    private final Connection connection = getConn();
    private int roomId;
    private String userId;
    private int undercoverNums;
    private int sumNums;
    private String undercoverWord;
    private String publicWord;
    private int[] undercoverIds=new int[3];

    public RoomStatus(int RoomId, String user_id) throws SQLException {
        this.roomId = RoomId;
        this.userId = user_id;
        String sql="SELECT * FROM `room` WHERE `room_id`=" + RoomId;
        connection.createStatement().executeQuery("SET NAMES utf8");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM `room` WHERE `room_id`=" + RoomId);
        while (resultSet.next()) {
            this.undercoverIds[0] = resultSet.getInt("spy_1_id");
            this.undercoverIds[1] = resultSet.getInt("spy_2_id");
            this.undercoverIds[2] = resultSet.getInt("spy_3_id");
            this.sumNums = resultSet.getInt("sum_peoples_nums");
            this.undercoverWord = resultSet.getString("word_1");
            this.publicWord = resultSet.getString("word_2");
        }
        System.out.println("ID是"+undercoverIds[0]);
        if (this.sumNums >= 4 && this.sumNums <= 6)
            this.undercoverNums = 1;
        else if (this.sumNums >= 7 && this.sumNums <= 11)
            this.undercoverNums = 2;
        else if (this.sumNums >= 12 && this.sumNums <= 13)
            this.undercoverNums = 3;
    }

    public Map getRoomInfo() throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT `character_id` FROM `users` WHERE `user_id`='" + this.userId+"'");
        int characterQueryResult = 0;
        Map result = new HashMap();
        while (resultSet.next())
            characterQueryResult = resultSet.getInt("character_id");
        result.put("room_id", roomId);
        result.put("user_id", userId);
        result.put("publicNums", sumNums - undercoverNums);
        result.put("undercoverNums", undercoverNums);
        result.put("sumNums",sumNums);
        if (characterQueryResult == undercoverIds[0] || characterQueryResult == undercoverIds[1] || characterQueryResult == undercoverIds[2]) {
            result.put("word", undercoverWord);
            result.put("character_id", characterQueryResult);
        } else if (characterQueryResult == 0) {
            result.put("undercoverWord", undercoverWord);
            result.put("publicWord", publicWord);
            if (undercoverIds[0] != 99) {
                result.put("undercover", undercoverIds[0] + "号");
                if (undercoverIds[1] != 99)
                {
                    result.put("undercover", result.get("undercover")+","+undercoverIds[1] + "号");
                    if (undercoverIds[2]!=99)
                        result.put("undercover", result.get("undercover")+","+undercoverIds[2] + "号");
                }
            }
        }else {
            result.put("word", publicWord);
            result.put("character_id", characterQueryResult);
        }

        return result;
    }
}
