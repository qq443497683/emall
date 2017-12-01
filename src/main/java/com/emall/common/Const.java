package com.emall.common;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
public class Const {
    public static final String CURRENT_USER="currentUser";
    public interface Role{
        int ROLE_USER=0;//用户权限
        int ROLE_ADMIN=1;//管理员权限
    }
    public interface ValidType{
        String EMAIL="email";
        String USERNAME="username";
    }
}
