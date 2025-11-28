// src/main/java/com/system/asset05/service/AssetLocationService.java
package com.system.asset05.service;

import com.system.asset05.entity.AssetLocation;
import com.system.asset05.mapper.AssetLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetLocationService {

    @Autowired
    private AssetLocationMapper locationMapper;

    public List<AssetLocation> getLocations() {
        return locationMapper.getLocations();
    }

    public int addLocation(AssetLocation location) {
        LocalDateTime now = LocalDateTime.now();
        location.setCreateTime(now);
        location.setUpdateTime(now);
        return locationMapper.addLocation(location);
    }

    public int updateLocation(AssetLocation location) {
        location.setUpdateTime(LocalDateTime.now());
        return locationMapper.updateLocation(location);
    }

    public int deleteLocation(int id) {
        return locationMapper.deleteLocation(id);
    }

    public boolean hasChildren(Integer id) {
        int count = locationMapper.countChildrenByParentId(id);
        return count > 0;
    }

    public List<AssetLocation> searchLocations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return locationMapper.getLocations();
        }
        return locationMapper.searchLocations("%" + keyword.trim() + "%");
    }
}