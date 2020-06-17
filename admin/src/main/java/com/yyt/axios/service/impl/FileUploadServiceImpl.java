package com.yyt.axios.service.impl;

import com.yyt.axios.service.FileUploadService;
import com.yyt.axios.util.Base64UrlUtil;
import com.yyt.axios.util.OSSUtil;
import com.yyt.axios.vo.FileUploadReplyVO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.List;
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${com.yyt.fileUpload.fileSizeMax}")
    private long fileSizeMax;

    @Value("${com.yyt.fileUpload.sizeMax}")
    private long sizeMax;

    @Autowired
    OSSUtil ossUtil;

    @Override
    public FileUploadReplyVO doFileUpload(MultipartFile multipartFile) throws FileUploadException, IOException {
//        // 如果是文件上传的请求则处理 否则抛异常
//        if (ServletFileUpload.isMultipartContent(request)) {
//            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
//            servletFileUpload.setFileSizeMax(fileSizeMax);
//            servletFileUpload.setSizeMax(sizeMax);
//            servletFileUpload.setHeaderEncoding("UTF-8");
//            List<FileItem> fileItems = servletFileUpload.parseRequest(request);
//            System.out.println(fileItems.size());
//            for (FileItem item : fileItems) {
//                if(item.isFormField()) {
//                    continue;
//                } else {
//                    // 如果是image/XX类型即图片类型则处理
//                    if(item.getContentType().startsWith("image")) {
//                        String fileName = System.currentTimeMillis() % 1000 + "-" + item.getName();
//                        String path = ossUtil.uploadFile(item.getInputStream(), fileName);
//                        return new FileUploadReplyVO().setUrl(path).setTmp_path(path);
//                    }
//                }
//            }
//        } else {
//            throw new RuntimeException("请求必须是enctype:multipart/form-data类型");
//        }
//        return null;
        String[] split = multipartFile.getOriginalFilename().split("\\.");
        String fileName = System.currentTimeMillis() % 1000 + "-" + Base64UrlUtil.encode(split[0]) + "." + split[1];
        String path = ossUtil.uploadFile(multipartFile.getInputStream(), fileName);
        return new FileUploadReplyVO().setUrl(path).setTmp_path(path);
    }

    public long getFileSizeMax() {
        return fileSizeMax;
    }

    public void setFileSizeMax(long fileSizeMax) {
        this.fileSizeMax = fileSizeMax;
    }

    public long getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(long sizeMax) {
        this.sizeMax = sizeMax;
    }
}
