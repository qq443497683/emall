package com.emall.controller;

import com.emall.common.Const;
import com.emall.common.ServerResponse;
import com.emall.pojo.User;
import com.emall.service.UserService;
import com.emall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

@Controller
@RequestMapping(value = "/user/")
public class UserAction {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    //用户登陆
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> responseResult = userService.login(username, MD5Util.MD5EncodeUtf8(password));
        if (responseResult.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, responseResult.getData());
        }
        return responseResult;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    //退出登录
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createSuccessResponse();
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
    @ResponseBody
    //验证用户名或邮箱是否可用
    public ServerResponse<String> checkvalid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    //注册
    public ServerResponse<String> regeister(User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.GET)
    @ResponseBody
    //忘记密码，获取找回密码问题
    public ServerResponse<String> getQuestionByName(String username) {
        return userService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
    @ResponseBody
    //验证找回密码问题
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_set_password.do", method = RequestMethod.GET)
    @ResponseBody
    //重设密码
    public ServerResponse<String> setForgetPassword(String username, String passwordNew, String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.GET)
    @ResponseBody
    //登陆状态下重设密码
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createErrorResponse("用户未登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, user);
    }
    @RequestMapping(value = "update_information.do", method = RequestMethod.GET)
    @ResponseBody
    //登录状态下更新用户信息
    public ServerResponse<User> updateInformation(User user,HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createErrorResponse("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse serverResponse=userService.updateInformation(user);
        if(serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
       return serverResponse;
    }
    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    @ResponseBody
    //获取用户信息
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createErrorResponse("用户未登录");
        }
        return userService.getInformation(currentUser.getId());
    }
}
