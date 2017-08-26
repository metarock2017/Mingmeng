package org.redrock.message.handle;

import org.redrock.message.TextMessage;

import java.util.Map;

/**
 * Created by jx on 2017/7/22.
 */
public class TextMessageHandle extends BaseMessageHandle<TextMessage> {
//    接收的文本消息格式
//    Content=收拾收拾,
//    CreateTime=1500710718,
//    ToUserName=gh_b6a171776f25,
//    FromUserName=oiL6j0e16ZpAXBKLIprkQ43fPtgk,
//    MsgType=text,
//    MsgId=6445503454980839326}

//    需返回的文本消息格式
//    ToUserName	是	接收方帐号（收到的OpenID）
//    FromUserName	是	开发者微信号
//    CreateTime	是	消息创建时间 （整型）
//    MsgType	是	text
//    Content	是	回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
    public TextMessageHandle(Map<String, String> data) {
        super(data);
    }

    @Override
    public TextMessage processRequest() {
        String timestamp = System.currentTimeMillis() / 1000 + "";
        TextMessage message = new TextMessage();
        message.setFromUserName(data.get("ToUserName"));
        message.setToUserName(data.get("FromUserName"));
        message.setMsgType("text");
        message.setCreateTime(timestamp);
        message.setContent("hello,我是蒋天星");
        return message;
    }
}
