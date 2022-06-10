package com.marvin.demo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserBean implements Serializable {
    private Integer id;
    private String username;
    private String pwd;

    public UserBean(){}

    public UserBean(Integer id, String username, String pwd) {
        this.id = id;
        this.username = username;
        this.pwd = pwd;
    }


    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
