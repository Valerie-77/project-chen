package com.system.asset05.controller.departments;

import com.system.asset05.entity.Parent;
import com.system.asset05.service.ParentService; // 修正类名首字母大写
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService parentService; // 修正类名首字母大写

    @PostMapping("/add")
    public R<Object> addParent(@RequestBody Parent parent) {
        int result = parentService.addParent(parent);
        if (result == 0) {
            return R.failed("添加失败");
        }
        return R.success("添加成功");
    }

    @DeleteMapping("/del/{id}")
    public R<Object> deleteParent(@PathVariable("id") int id) { // 添加参数名
        int result = parentService.deleteParent(id);
        if (result == 0) {
            return R.failed("删除失败");
        }
        return R.success("删除成功");
    }

    @PutMapping("/update")
    public R<Object> updateParent(@RequestBody Parent parent) {
        int result = parentService.updateParent(parent);
        if (result == 0) {
            return R.failed("更新失败");
        }
        return R.success("更新成功");
    }

    @GetMapping("/list/{page}/{pageSize}")
    public R<HashMap<String, Object>> selectParent(
            @PathVariable("page") int page,  // 添加参数名
            @PathVariable("pageSize") int pageSize) {  // 添加参数名
        if (pageSize <= 0 || page <= 0) {
            pageSize = 10;
            page = 1;
        }
        page = (page - 1) * pageSize;
        HashMap<String, Object> result = parentService.selectAllParent(page, pageSize);
        if (result == null) {
            return R.success("暂无数据");
        }
        return R.success("查询成功", result);
    }
}