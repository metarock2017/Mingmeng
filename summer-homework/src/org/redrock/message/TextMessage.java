package org.redrock.message;

/**
 * Created by jx on 2017/7/22.
 */
public class TextMessage extends BaseMessage{
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;



    @Override
    public String toXml() {
        StringBuilder builder = new StringBuilder();
        builder.append("<xml><ToUserName><![CDATA[")
                .append(toUserName)
                .append("]]></ToUserName><FromUserName><![CDATA[")
                .append(fromUserName)
                .append("]]></FromUserName><CreateTime>")
                .append(createTime)
                .append("</CreateTime><MsgType><![CDATA[")
                .append(msgType)
                .append("]]></MsgType><Content><![CDATA[")
                .append(content)
                .append("]]></Content></xml>");
        return builder.toString();
    }
}
