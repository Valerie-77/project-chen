package com.system.asset05.controller.Categories;

import com.system.asset05.entity.Categories;
import com.system.asset05.service.CategoriesService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;


    @GetMapping("/list")
    public R<List<Categories>> getAllCategories() {
        List<Categories> categories = categoriesService.getCategories();
        return R.success("查询成功", categories);
    }

    @PostMapping("/add")
    public R<Object> addCategories(@RequestBody Categories categories) {
        int rows = categoriesService.addCategory(categories);
        return rows > 0 ? R.success("添加成功") : R.failed("添加失败");
    }


    @PutMapping("/update")
    public R<Object> updateCategories(@RequestBody Categories categories) {
        int rows = categoriesService.updateCategory(categories);
        return rows > 0 ? R.success("更新成功") : R.failed("更新失败");
    }


    @DeleteMapping("/del/{id}")
    public R<Object> deleteCategories(@PathVariable Integer id) {

        boolean hasChildren = categoriesService.hasChildren(id);
        if (hasChildren) {
            return R.failed("该分类含有子分类，请先删除子分类后再操作！");
        }

        int rows = categoriesService.deleteCategory(id);
        return rows > 0 ? R.success("删除成功") : R.failed("删除失败，分类可能已不存在");
    }

    @GetMapping("/search")
    public R<List<Categories>> searchCategory(@RequestParam(required = false) String keyword) {
        List<Categories> allList = categoriesService.getCategories();
        if (keyword == null || keyword.trim().isEmpty()) {
            return R.success("查询成功", allList);
        }

        String kw = keyword.toLowerCase().trim();
        Set<Integer> matchedIds = new HashSet<>();

        // 1. 先找出所有直接匹配的节点
        for (Categories c : allList) {
            if ((c.getName() != null && c.getName().toLowerCase().contains(kw)) ||
                    (c.getRemark() != null && c.getRemark().toLowerCase().contains(kw))) {
                matchedIds.add(c.getId());
            }
        }

        // 2. 递归把所有父节点和子节点都加进来
        Set<Integer> finalIds = new HashSet<>(matchedIds);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Categories c : allList) {
                if (finalIds.contains(c.getId())) {
                    // 加父节点
                    if (c.getParentId() != null && c.getParentId() != 0 && !finalIds.contains(c.getParentId())) {
                        finalIds.add(c.getParentId());
                        changed = true;
                    }
                    // 加所有子节点
                    for (Categories child : allList) {
                        if (child.getParentId() != null && child.getParentId().equals(c.getId()) && !finalIds.contains(child.getId())) {
                            finalIds.add(child.getId());
                            changed = true;
                        }
                    }
                }
            }
        }

        List<Categories> result = allList.stream()
                .filter(item -> finalIds.contains(item.getId()))
                .collect(Collectors.toList());

        return R.success("查询成功", result);
    }
}