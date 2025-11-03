package com.staylog.staylog.global.common.util;

import com.staylog.staylog.global.common.dto.FileUploadDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileUtil {

    public static FileUploadDto saveFile(MultipartFile file, String uploadPath) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String savedPath = Paths.get(datePath, uuid + "_" + originalName).toString().replace("\\", "/"); // 시스템 기본 경로 구분자 대신 URL에 적합한 '/'를 사용하도록 변경
        File destinationFile = new File(uploadPath, savedPath);

        destinationFile.getParentFile().mkdirs();
        file.transferTo(destinationFile);

        return new FileUploadDto(destinationFile.getName(), originalName, savedPath); // savedPath는 YYYY/MM/DD/UUID_OriginalName // savedPath는 YYYY/MM/DD/UUID_OriginalName
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}