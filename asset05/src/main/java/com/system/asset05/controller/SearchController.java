package com.system.asset05.controller;

import com.system.asset05.entity.Categories;
import com.system.asset05.entity.Parent;
import com.system.asset05.entity.User;
import com.system.asset05.service.CategoriesService;
import com.system.asset05.service.ParentService;
import com.system.asset05.service.UserService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private ParentService parentService;

    @GetMapping("/search")
    public R<List<?>> search(@RequestParam String type,
                             @RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "";
        }

        List<?> result = new ArrayList<>();

        if ("user".equals(type)) {
            result = userService.searchUser(keyword);
        } else if ("category".equals(type)) {
            result = categoriesService.searchCategories(keyword);
        } else if ("dept".equals(type) || "location".equals(type)) {
            result = parentService.searchParent(keyword);
        }

        return R.success("查询成功", result);
    }
}