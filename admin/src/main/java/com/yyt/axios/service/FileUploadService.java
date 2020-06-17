package com.yyt.axios.service;

import com.yyt.axios.vo.FileUploadReplyVO;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileUploadService {
    FileUploadReplyVO doFileUpload(MultipartFile request) throws FileUploadException, IOException;
}
