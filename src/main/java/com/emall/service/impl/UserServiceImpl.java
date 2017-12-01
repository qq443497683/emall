package com.emall.service.impl;

import com.emall.common.Const;
import com.emall.common.ServerResponse;
import com.emall.common.TokenCache;
import com.emall.dao.UserMapper;
import com.emall.pojo.User;
import com.emall.service.UserService;
import com.emall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.selectByName(username);
        if (resultCount == 0) {
            return ServerResponse.createSuccessResponse("用户名不存在！");
        }
        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createErrorResponse("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createSuccessResponse("登陆成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse=this.checkValid(user.getUsername(),Const.ValidType.USERNAME);
        if(!validResponse.isSuccess()){
            return ServerResponse.createErrorResponse("用户名已存在");
        }
        validResponse=this.checkValid(user.getEmail(),Const.ValidType.EMAIL);
        if(!validResponse.isSuccess()){
            return ServerResponse.createErrorResponse("邮箱已存在");
        }
        user.setRole(Const.Role.ROLE_USER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createErrorResponse("注册失败");
        }
        return ServerResponse.createSuccessResponse("注册成功");
    }
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(str)) {
            if (Const.ValidType.USERNAME.equals(type)) {
                if (userMapper.selectByName(str) > 0) {
                    return ServerResponse.createErrorResponse("用户已存在");
                }
            } else if (Const.ValidType.EMAIL.equals(type)) {
                if (userMapper.selectByEmail(str) > 0) {
                    return ServerResponse.createErrorResponse("邮箱已存在");
                }
            } else {
                return ServerResponse.createErrorResponse("参数类型错误");
            }
        }
        return ServerResponse.createSuccessResponse("用户名或邮箱可用，校验成功");
    }

    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse resultResponse=this.checkValid(username,Const.ValidType.USERNAME);
        if(resultResponse.isSuccess()){
            return ServerResponse.createErrorResponse("用户名不存在");
        }
        String resultCount=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(resultCount)){
            return ServerResponse.createSuccessResponse("这里是提示问题",resultCount);
        }
        return ServerResponse.createErrorResponse("用户没有设置密码找回问题");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount=userMapper.checkAnswer(username, question, answer);
        if(resultCount>0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("Token_" + username, forgetToken);
            return ServerResponse.createSuccessResponse(forgetToken);
        }
        return ServerResponse.createErrorResponse("答案错误，无法修改密码");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isNotBlank(forgetToken)){
            return ServerResponse.createErrorResponse("参数错误，请传递Token令牌");
        }
        ServerResponse serverResponse=this.checkValid(username,Const.ValidType.USERNAME);
        if(serverResponse.isSuccess()){
            return ServerResponse.createErrorResponse("用户名为空");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createErrorResponse("Token无效");
        }
        if(StringUtils.equals(token,forgetToken)){
            String md5password=MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,passwordNew);
            if(rowCount>0){
                return ServerResponse.createSuccessResponse("密码修改成功");
            }else {
                ServerResponse.createErrorResponse("Token无效");
            }
        }
        return ServerResponse.createErrorResponse("密码修改失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int rowCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(rowCount==0) {
            return ServerResponse.createErrorResponse("旧密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        rowCount=userMapper.updateByPrimaryKey(user);
        if(rowCount>0){
            return ServerResponse.createSuccessResponse("密码修改成功");
        }
        return ServerResponse.createErrorResponse("密码修改失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount=userMapper.checkEmailByUserid(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createErrorResponse("邮箱已被使用");
        }
        User newUser=new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setQuestion(user.getQuestion());
        newUser.setPassword(user.getPassword());
        int rowCount=userMapper.updateByPrimaryKey(newUser);
        if(rowCount>0){
            return ServerResponse.createSuccessResponse("个人信息修改成功",newUser);
        }
        return ServerResponse.createErrorResponse("个人信息修改失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createErrorResponse("找不到用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createSuccessResponse(user);
    }
}
