package com.zy.demo.pojo;

import com.zy.demo.model.ZyUser;

/**
 * 登录DTO
 *
 * @author zy
 */
public class LoginDto {
    private ZyUser zyUser;

    public void setUser(ZyUser zyUser) {
        this.zyUser = zyUser;
    }

    public ZyUser getUser() {
        return this.zyUser;
    }
}
