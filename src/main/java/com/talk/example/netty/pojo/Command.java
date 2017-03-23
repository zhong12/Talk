package com.talk.example.netty.pojo;

import java.io.Serializable;

/**
 * Created by zhongjing on 2016/06/08 0008.
 */
public class Command implements Serializable {
    private static final long serialVersionUID = 7590999461767050471L;

    private String actionName;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
