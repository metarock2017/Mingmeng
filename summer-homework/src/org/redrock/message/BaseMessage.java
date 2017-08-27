package org.redrock.message;

import org.redrock.aes.AesException;
import org.redrock.aes.WXBizMsgCrypt;
import org.redrock.util.Const;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jx on 2017/7/21.
 */
public abstract class BaseMessage {

    public abstract String toXml();

    public String encodeXml(HttpServletRequest request) {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String token = Const.Token;
        String encodingAesKey = Const.EncodingAESKey;
        String appId = Const.AppId;
        String preXml = this.toXml();
        String xml = null;
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            xml = pc.encryptMsg(preXml, timestamp, nonce);
        } catch (AesException e) {
            e.printStackTrace();
        }
        return xml;
    }
}
