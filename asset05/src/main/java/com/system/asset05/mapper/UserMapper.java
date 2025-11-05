package com.system.asset05.mapper;

import com.system.asset05.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User userMapperLogin(User user);


    List<User> selectAllUsers(@Param("page") int page, @Param("pageSize") int pageSize);

    int selectUserTotal();

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(int id);


}
