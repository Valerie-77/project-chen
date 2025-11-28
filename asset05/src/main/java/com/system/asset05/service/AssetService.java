package com.system.asset05.service;

import com.system.asset05.entity.Asset;
import com.system.asset05.mapper.AssetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetMapper assetMapper;

    // 这几个你暂时不用，先删掉警告（后面前端要用再加回来）
    // @Autowired private CategoriesService categoriesService;
    // @Autowired private AssetLocationService locationService;
    // @Autowired private ParentService parentService;
    // @Autowired private UserService userService;

    private String generateAssetCode() {
        String prefix = "ZC" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int todayCount = assetMapper.countByCodePrefix(prefix + "%");
        return prefix + String.format("%04d", todayCount + 1);  // 修复格式化问题
    }

    public List<Asset> getAllAssets() {
        return assetMapper.getAllAssets();
    }

    public int addAsset(Asset asset) {
        asset.setAssetCode(generateAssetCode());
        LocalDateTime now = LocalDateTime.now();
        asset.setCreateTime(now);
        asset.setUpdateTime(now);
        return assetMapper.addAsset(asset);  // 修复：asset → asset
    }

    public int updateAsset(Asset asset) {
        asset.setUpdateTime(LocalDateTime.now());
        return assetMapper.updateAsset(asset);
    }

    public int deleteAsset(int id) {
        return assetMapper.deleteAsset(id);
    }

    public List<Asset> searchAssets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAssets();
        }
        return assetMapper.searchAssets("%" + keyword.trim() + "%");
    }
}