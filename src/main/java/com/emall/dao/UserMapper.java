package com.emall.dao;

import com.emall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(User record);
    int insertSelective(User record);
    User selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(User record);
    int updateByPrimaryKey(User record);

    int selectByName(String username);
    int selectByEmail(String email);
    User selectLogin(@Param("username") String username, @Param("password") String password);
    String selectQuestionByUsername(String username);
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);
    int checkPassword(@Param("password")String password,@Param("user_id")Integer user_id);
    int checkEmailByUserid(@Param("email")String email,@Param("user_id")Integer user_id);

}