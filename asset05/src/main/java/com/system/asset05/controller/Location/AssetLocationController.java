// src/main/java/com/system/asset05/controller/location/AssetLocationController.java
package com.system.asset05.controller.Location;

import com.system.asset05.entity.AssetLocation;
import com.system.asset05.service.AssetLocationService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/location")
public class AssetLocationController {

    @Autowired
    private AssetLocationService locationService;

    @GetMapping("/list")
    public R<List<AssetLocation>> getAll() {
        return R.success("查询成功", locationService.getLocations());
    }

    @PostMapping("/add")
    public R<Object> add(@RequestBody AssetLocation location) {
        int rows = locationService.addLocation(location);
        return rows > 0 ? R.success("添加成功") : R.failed("添加失败");
    }

    @PutMapping("/update")
    public R<Object> update(@RequestBody AssetLocation location) {
        int rows = locationService.updateLocation(location);
        return rows > 0 ? R.success("更新成功") : R.failed("更新失败");
    }

    @DeleteMapping("/del/{id}")
    public R<Object> delete(@PathVariable Integer id) {
        boolean hasChildren = locationService.hasChildren(id);
        if (hasChildren) {
            return R.failed("该位置含有子位置，请先删除子位置后再操作！");
        }
        int rows = locationService.deleteLocation(id);
        return rows > 0 ? R.success("删除成功") : R.failed("删除失败");
    }

    @GetMapping("/search")
    public R<List<AssetLocation>> search(@RequestParam(required = false) String keyword) {
        List<AssetLocation> allList = locationService.getLocations();
        if (keyword == null || keyword.trim().isEmpty()) {
            return R.success("查询成功", allList);
        }

        String kw = keyword.toLowerCase().trim();
        Set<Integer> matchedIds = new HashSet<>();

        // 找出直接匹配的节点
        for (AssetLocation c : allList) {
            if ((c.getName() != null && c.getName().toLowerCase().contains(kw)) ||
                    (c.getRemark() != null && c.getRemark().toLowerCase().contains(kw))) {
                matchedIds.add(c.getId());
            }
        }

        // 把所有父节点和子节点都加进来
        Set<Integer> finalIds = new HashSet<>(matchedIds);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (AssetLocation c : allList) {
                if (finalIds.contains(c.getId())) {
                    if (c.getParentId() != null && c.getParentId() != 0 && !finalIds.contains(c.getParentId())) {
                        finalIds.add(c.getParentId());
                        changed = true;
                    }
                    for (AssetLocation child : allList) {
                        if (child.getParentId() != null &&
                                child.getParentId().equals(c.getId()) &&
                                !finalIds.contains(child.getId())) {
                            finalIds.add(child.getId());
                            changed = true;
                        }
                    }
                }
            }
        }

        List<AssetLocation> result = allList.stream()
                .filter(item -> finalIds.contains(item.getId()))
                .toList();

        return R.success("查询成功", result);
    }
}