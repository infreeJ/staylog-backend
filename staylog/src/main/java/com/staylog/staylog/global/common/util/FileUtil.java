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
        // 파일 확장자 추출
        String extension = "";
		if (originalName != null && originalName.contains(".")) {
			extension = originalName.substring(originalName.lastIndexOf("."));
		}
        // UUID와 확장자를 조합하여 완전히 새로운 파일명 생성
        String safeFileName = UUID.randomUUID().toString() + extension;
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 시스템 기본 경로 구분자 대신 URL에 적합한 '/'를 사용하도록 변경
        String savedPath = Paths.get(datePath, safeFileName).toString().replace("\\\\", "/");
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