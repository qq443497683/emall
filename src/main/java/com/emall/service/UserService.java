package com.emall.service;

import com.emall.common.ServerResponse;
import com.emall.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/11/30 0030.
 */
@Repository
public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

}
