package com.system.asset05.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("asset_location")
public class AssetLocation {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer parentId = 0;

    private String name;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}