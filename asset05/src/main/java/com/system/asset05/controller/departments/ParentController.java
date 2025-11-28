package com.system.asset05.controller.departments;

import com.system.asset05.entity.Parent;
import com.system.asset05.service.ParentService; // 修正类名首字母大写
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parent")
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
    @GetMapping("/search")
    public R<List<Parent>> searchParent(@RequestParam(required = false) String keyword) {
        // 用你现有的全量查询方法
        HashMap<String, Object> allMap = parentService.selectAllParent(0, Integer.MAX_VALUE);
        List<Parent> allList =  (List<Parent>) allMap.get("data");

        if (keyword == null || keyword.trim().isEmpty()) {
            return R.success("查询成功", allList);
        }

        String kw = keyword.toLowerCase().trim();
        Set<Integer> matchedIds = new HashSet<>();

        // 1. 找出直接匹配的节点
        for (Parent p : allList) {
            if ((p.getDeptName() != null && p.getDeptName().toLowerCase().contains(kw)) ||
                    (p.getContact() != null && p.getContact().toLowerCase().contains(kw))) {
                matchedIds.add(p.getId());
            }
        }

        // 2. 把所有父节点和子节点都加进来
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Parent p : allList) {
                if (matchedIds.contains(p.getId())) {
                    // 加父节点
                    if (p.getParentId() != null && p.getParentId() != 0 && !matchedIds.contains(p.getParentId())) {
                        matchedIds.add(p.getParentId());
                        changed = true;
                    }
                    // 加所有子节点
                    for (Parent child : allList) {
                        if (child.getParentId() != null &&
                                child.getParentId().equals(p.getId()) &&
                                !matchedIds.contains(child.getId())) {
                            matchedIds.add(child.getId());
                            changed = true;
                        }
                    }
                }
            }
        }

        List<Parent> result = allList.stream()
                .filter(p -> matchedIds.contains(p.getId()))
                .collect(Collectors.toList());

        return R.success("查询成功", result);
    }
}

