package com.marvin.demo.model;

import com.marvin.demo.entity.UserBean;
import lombok.Data;

import java.io.Serializable;

/**
 * 这是接收消息的模型
 */
@Data
public class UserRequestModel implements Serializable {
    private String type;
    private String status;
    private String desc;
    private UserBean content;

    @Override
    public String toString() {
        return "UserRequestModel{" +
                "type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", content=" + content +
                '}';
    }
}
