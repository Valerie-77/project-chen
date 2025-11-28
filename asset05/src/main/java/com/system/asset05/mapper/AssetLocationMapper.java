// src/main/java/com/system/asset05/mapper/AssetLocationMapper.java
package com.system.asset05.mapper;

import com.system.asset05.entity.AssetLocation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AssetLocationMapper {
    List<AssetLocation> getLocations();
    int addLocation(AssetLocation location);
    int updateLocation(AssetLocation location);
    int deleteLocation(int id);
    int countChildrenByParentId(Integer parentId);
    List<AssetLocation> searchLocations(String keyword);
}