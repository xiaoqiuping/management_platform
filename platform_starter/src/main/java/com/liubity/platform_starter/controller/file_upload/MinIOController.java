package com.liubity.platform_starter.controller.file_upload;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.model.common.ReturnCode;
import com.liubity.platform_starter.model.file_upload.MinIODTO;
import com.liubity.platform_starter.utils.DateUtil;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-28
 */
@RestController
@RequestMapping("/minio")
@Log4j2
public class MinIOController {
    
    @Autowired
    private MinioClient minioClient;
    
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.bucketName}")
    private String bucketName;
    
    @PostMapping("/upload")
    @LogOperation("上传文件")
    public CommonResponse update(@RequestParam("file") MultipartFile file) {
        
        log.info("minio.endpoint => " + endpoint);
        log.info("minio.endpoint => " + bucketName);
        if (Objects.isNull(file)) {
            return CommonResponse.build(ReturnCode.ERROR, "请上传文件");
        }
        try {
            boolean b=minioClient.bucketExists(bucketName);
            if (b) {
                log.info(bucketName + " 已存在");
            } else {
                minioClient.makeBucket(bucketName);
                minioClient.setBucketPolicy(bucketName, "*.*", PolicyType.READ_WRITE);
                log.info(bucketName + " 成功创建");
            }
            
            String fileName=file.getOriginalFilename();
            if (Objects.isNull(fileName)) {
                return CommonResponse.build(ReturnCode.ERROR, "文件名为空");
            }
            int i=fileName.lastIndexOf(".");
            String fileNamePre=fileName.substring(0, i);
            String fileSuff=fileName.substring(i);
            String currentDateTime=DateUtil.getCurrentDateTime();
            String objName=fileNamePre + "_" + currentDateTime + fileSuff;
            minioClient.putObject(bucketName, objName, file.getInputStream(), file.getContentType());
            log.info("文件【" + fileName + "】上传成功");
            
            MinIODTO iodto=new MinIODTO();
            iodto.setName(fileName)
                    .setFileName(objName)
                    .setUrl(endpoint + "/" + bucketName + "/" + objName)
                    .setSize(file.getSize())
                    .setType(file.getContentType());
            return CommonResponse.build(Result.success(), iodto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResponse.build(Result.fail());
        }
    }
    
    @PostMapping("/delete")
    @LogOperation("删除文件")
    public CommonResponse delete(@RequestParam("fileName") String fileName) {
        try {
            minioClient.removeObject(bucketName, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResponse.build(Result.fail());
        }
        return CommonResponse.build(Result.success());
    }
}
