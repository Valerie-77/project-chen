package com.system.asset05.controller.Asset;

import com.system.asset05.entity.Asset;
import com.system.asset05.service.AssetService;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;  // 小写！不是 AssetService

    @GetMapping("/list")
    public R<List<Asset>> list() {
        return R.success("查询成功", assetService.getAllAssets());  // 用实例！
    }

    @PostMapping("/add")
    public R<Object> add(@RequestBody Asset asset) {
        return assetService.addAsset(asset) > 0 ? R.success("添加成功") : R.failed("添加失败");
    }

    @PutMapping("/update")
    public R<Object> update(@RequestBody Asset asset) {
        return assetService.updateAsset(asset) > 0 ? R.success("更新成功") : R.failed("更新失败");
    }

    @DeleteMapping("/del/{id}")
    public R<Object> delete(@PathVariable Integer id) {
        return assetService.deleteAsset(id) > 0 ? R.success("删除成功") : R.failed("删除失败");
    }

    @GetMapping("/search")
    public R<List<Asset>> search(@RequestParam(required = false) String keyword) {
        return R.success("查询成功", assetService.searchAssets(keyword));
    }
}