package com.yyt.axios.controller.goods;

import com.yyt.axios.enums.CodeEnum;
import com.yyt.axios.service.FileUploadService;
import com.yyt.axios.vo.BaseVO;
import com.yyt.axios.vo.FileUploadReplyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;

    @Value("${com.yyt.fileUpload.fileSizeMax}")
    private long fileSizeMax;

    @Value("${com.yyt.fileUpload.sizeMax}")
    private long sizeMax;

    @PostMapping("/upload")
    public BaseVO<FileUploadReplyVO> fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return new BaseVO<FileUploadReplyVO>()
                    .setData(fileUploadService.doFileUpload(multipartFile))
                    .setState(CodeEnum.UPLOAD_SUCCESS);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            log.error(String.format("上传文件超出了%dM", fileSizeMax / 1024 / 1024), e);
            return new BaseVO<FileUploadReplyVO>().setState(CodeEnum.UPLOAD_ERROR);
        } catch (FileUploadBase.SizeLimitExceededException e) {
            log.error(String.format("上传文件超出了 %dM", sizeMax / 1024 / 1024), e);
            return new BaseVO<FileUploadReplyVO>().setState(CodeEnum.UPLOAD_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new BaseVO<FileUploadReplyVO>().setState(CodeEnum.UPLOAD_ERROR);
        }
    }
}
