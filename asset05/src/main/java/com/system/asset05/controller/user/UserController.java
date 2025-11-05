package com.system.asset05.controller.user;

import com.system.asset05.entity.User;
import com.system.asset05.service.UserService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user) {
        User result = userService.userServiceLogin(user);
        if (result == null) {
            return R.failed("登陆失败");
        }
        return R.success("登陆成功", result);
    }

    @PostMapping("/add")
    public R<Object> addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        if (result == 0) {
            return R.failed("添加失败");
        }
        return R.success("添加成功");
    }

    @DeleteMapping("/del/{id}")
    public R<Object> deleteUser(@PathVariable int id) {
        int result = userService.deleteUser(id);
        if (result == 0) {
            return R.failed("删除失败");
        }
        return R.success("删除成功");
    }

    @PutMapping("/update")
    public R<Object> updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        if (result == 0) {
            return R.failed("更新失败");
        }
        return R.success("更新成功");
    }

    @GetMapping("/list/{page}/{pageSize}")
    public R<HashMap<String, Object>> selectUsers(@PathVariable int page, @PathVariable int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            pageSize = 10;
            page = 1;
        }
        page = (page - 1) * pageSize;
        HashMap<String, Object> result = userService.selectAllUsers(page, pageSize);
        if (result == null) {
            return R.success("暂无数据");
        }
        return R.success("查询成功", result);
    }
}