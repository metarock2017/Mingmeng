package org.redrock.message.handle;

import org.redrock.message.BaseMessage;

import java.util.Map;

/**
 * Created by jx on 2017/7/22.
 */
public abstract class BaseMessageHandle<T extends BaseMessage> {
    protected Map<String, String> data;

    public BaseMessageHandle(Map<String, String> data) {
        this.data = data;
    }

    public abstract T processRequest();
}
