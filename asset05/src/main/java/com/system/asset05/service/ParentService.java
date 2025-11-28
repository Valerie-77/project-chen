package com.system.asset05.service;

import com.system.asset05.entity.Parent;
import com.system.asset05.mapper.ParentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ParentService {

    @Autowired
    private ParentMapper parentMapper;

    // 分页查询所有
    public HashMap<String, Object> selectAllParent(int page, int pageSize) {
        HashMap<String, Object> parentMap = new HashMap<>();
        List<Parent> parents = parentMapper.selectAllParents(page, pageSize);
        int total = parentMapper.selectParentTotal();
        parentMap.put("total", total);
        parentMap.put("data", parents);
        return parentMap;
    }

    // 新增
    public int addParent(Parent parent) {
        if (parent.getDeptName() == null || parent.getDeptName().trim().isEmpty()) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        if (parent.getDeptCode() == null || parent.getDeptCode().trim().isEmpty()) {
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder randomStr = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int index = random.nextInt(str.length());
                randomStr.append(str.charAt(index));
            }
            String deptCode = randomStr.toString();
            System.out.println("生成的部门代码: " + deptCode);
            parent.setDeptCode(deptCode);
        }

        return parentMapper.addParent(parent);
    }

    // 修改
    public int updateParent(Parent parent) {
        return parentMapper.updateParent(parent);
    }

    // 删除
    public int deleteParent(int id) {
        return parentMapper.deleteParent(id);
    }

    public List<Parent> searchParent(String keyword) {
        return parentMapper.searchParent(keyword);
    }
}