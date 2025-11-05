package com.system.asset05.service;

import com.system.asset05.entity.User;
import com.system.asset05.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User userServiceLogin(User user) {
        return userMapper.userMapperLogin(user);
    }

    public HashMap<String, Object> selectAllUsers(int page, int pageSize) {
        HashMap<String, Object> userMap = new HashMap<>();
        List<User> users = userMapper.selectAllUsers(page, pageSize);
        int total = userMapper.selectUserTotal();
        userMap.put("total", total);
        userMap.put("data", users);
        return userMap;
    }

    public int addUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        // 自动生成用户编码
        if (user.getUserCode() == null || user.getUserCode().trim().isEmpty()) {
            String userCode = generateUserCode();
            user.setUserCode(userCode);
        }

        // 设置默认值
        if (user.getGender() == null) {
            user.setGender(1);
        }
        if (user.getUserType() == null) {
            user.setUserType("1");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        return userMapper.addUser(user);
    }

    public int updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateUser(user);
    }

    public int deleteUser(int id) {
        return userMapper.deleteUser(id);
    }

    /**
     * 生成用户编码
     */
    private String generateUserCode() {
        // 使用UUID生成唯一编码
        return "U" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}