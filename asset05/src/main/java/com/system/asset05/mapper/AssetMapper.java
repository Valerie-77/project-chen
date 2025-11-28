package com.system.asset05.mapper;

import com.system.asset05.entity.Asset;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetMapper {
    List<Asset> getAllAssets();
    int addAsset(Asset asset);
    int updateAsset(Asset asset);
    int deleteAsset(int id);
    List<Asset> searchAssets(String keyword);

    // 新增：统计当天编号数量
    int countByCodePrefix(String prefix);
}