
package cn.ztuo.bitrade.controller;

import cn.ztuo.bitrade.service.LocaleMessageSourceService;
import cn.ztuo.bitrade.util.GeneratorUtil;
import cn.ztuo.bitrade.util.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
public class UploadController {

    private String allowedFormat = ".jpg,.gif,.png,.jpeg";

    @Autowired
    private Drive googleDriveService;
    @Autowired
    private LocaleMessageSourceService sourceService;

    @RequestMapping(value = "upload/drive/image", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult uploadDriveImage(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam("file") MultipartFile file) throws Exception {
        Assert.notNull(file, sourceService.getMessage("MISSING_FILE"));

        String fileName = file.getOriginalFilename();
        Assert.notNull(fileName, sourceService.getMessage("MISSING_FILE"));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        Assert.isTrue(allowedFormat.contains(suffix.toLowerCase()), sourceService.getMessage("FORMAT_NOT_SUPPORTED"));

        String directory = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String key = directory + GeneratorUtil.getUUID() + suffix;

        InputStream inputStream = file.getInputStream();
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent("application/octet-stream", inputStream);

        File fileMetadata = new File();
        fileMetadata.setName(key);
        File uploadedFile = googleDriveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id")
                .execute();

        String fileUrl = "https://drive.google.com/uc?id=" + uploadedFile.getId();

        MessageResult mr = new MessageResult(0, sourceService.getMessage("UPLOAD_SUCCESS"));
        mr.setData(fileUrl);
        mr.setMessage(sourceService.getMessage("UPLOAD_SUCCESS"));
        log.debug("Upload successful, key: {}", key);
        return mr;
    }
}
