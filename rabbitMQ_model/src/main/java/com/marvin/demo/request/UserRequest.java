package com.marvin.demo.request;

import com.marvin.demo.entity.UserBean;

import java.io.Serializable;

/**
 * 这是接收消息的模型
 */
public class UserRequest implements Serializable {
    private String type;
    private String status;
    private String desc;
    private UserBean content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserBean getContent() {
        return content;
    }

    public void setContent(UserBean content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", content=" + content +
                '}';
    }
}
