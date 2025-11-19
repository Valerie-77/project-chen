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

    public HashMap<String, Object> selectAllParent(int page, int pageSize) {
        HashMap<String, Object> parentMap = new HashMap<>();
        List<Parent> parents = parentMapper.selectAllParents(page, pageSize);
        int total = parentMapper.selectParentTotal();
        parentMap.put("total", total);
        parentMap.put("data", parents);
        return parentMap;
    }

    public int addParent(Parent parent) {
        if (parent.getDeptName() == null || parent.getDeptName().trim().isEmpty()) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        if (parent.getDeptCode() == null || parent.getDeptCode().trim().isEmpty()) {
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuffer randomStr = new StringBuffer();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                int index = random.nextInt(str.length());
                randomStr.append(str.charAt(index));
            }
            String deptCode = randomStr.toString();
            System.out.println("生成的部门代码: " + deptCode);
            parent.setDeptCode(deptCode);
        }
        // 设置默认值
        if (parent.getStatus() == 0) {
            parent.setStatus(1);
        }
        if (parent.getLevel() == 0) {
            parent.setLevel(1);
        }
        return parentMapper.addParent(parent);
    }

    public int updateParent(Parent parent) {
        return parentMapper.updateParent(parent);
    }

    public int deleteParent(int id) {
        return parentMapper.deleteParent(id);
    }

}