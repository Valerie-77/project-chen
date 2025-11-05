package com.system.asset05.service;

import com.system.asset05.entity.Categories;
import com.system.asset05.mapper.CategoriesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoriesService {
    @Autowired
    private CategoriesMapper categoriesMapper;

    public List<Categories> getCategories() {
        return categoriesMapper.getCategories();
    }

    public int addCategory(Categories categories) {
        LocalDateTime now = LocalDateTime.now();
        categories.setCreateTime(now);
        categories.setUpdateTime(now);
        return categoriesMapper.addCategories(categories);
    }

    public int updateCategory(Categories categories) {
        categories.setUpdateTime(LocalDateTime.now());
        return categoriesMapper.updateCategories(categories);
    }

    public int deleteCategory(int id) {
        return categoriesMapper.deleteCategories(id);
    }

    public boolean hasChildren(Integer id) {
        int count = categoriesMapper.countChildrenByParentId(id);
        return count > 0;
    }
}