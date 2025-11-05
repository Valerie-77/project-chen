// FileUploadController.java
package com.system.asset05.controller;

import com.system.asset05.utils.FileUtil;
import com.system.asset05.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.failed("文件不能为空");
        }

        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return R.failed("只能上传图片文件");
            }

            // 检查文件大小
            if (file.getSize() > 10 * 1024 * 1024) {
                return R.failed("文件大小不能超过10MB");
            }

            // 上传文件
            String fileName = FileUtil.uploadFile(file, uploadPath);

            return R.success("上传成功", fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return R.failed("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("上传过程中出现错误");
        }
    }
}