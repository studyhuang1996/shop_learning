package com.studyhuang.dao;

import com.studyhuang.pojo.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     *判断用户名是否存在
     * @param username
     * @return
     */
    int checkName(String username);

    int checkEmail(String email);

    User login(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUserName(String username);

    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updatePassword(@Param("username") String username,@Param("passwordNew") String pwdmd5);

    int updateOldPassword(@Param("password") String oldpwd,@Param("user_id") Integer id);

    int selectEmial(@Param("userId") Integer id,@Param("email") String email);
}