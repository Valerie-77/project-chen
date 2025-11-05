package com.system.asset05.controller.Categories;

import com.system.asset05.entity.Categories;
import com.system.asset05.service.CategoriesService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
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
}