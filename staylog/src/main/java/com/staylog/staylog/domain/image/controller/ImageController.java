package com.staylog.staylog.domain.image.controller;

import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.dto.ImageResponse;
import com.staylog.staylog.domain.image.dto.ImageUpdateRequest;
import com.staylog.staylog.domain.image.service.ImageService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "ImageController", description = "Image Load & Upload")
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MessageUtil messageUtil;

    @Operation(summary = "이미지 업로드", description = "이미지를 업로드하는 대상 ID와 연결하여 DB에 저장합니다.")
    @PostMapping("/images")
    public ResponseEntity<SuccessResponse<ImageResponse>> uploadImages(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("targetType") String targetType,
                                           @RequestParam("targetId") long targetId) {
        ImageResponse imagesResponse = imageService.saveImages(files, targetType, targetId);
        String message = messageUtil.getMessage(SuccessCode.IMAGE_UPLOAD_SUCCESS.getMessageKey());
        String code = SuccessCode.IMAGE_UPLOAD_SUCCESS.getCode();
        SuccessResponse<ImageResponse> success = SuccessResponse.of(code, message, imagesResponse);
        return ResponseEntity.ok(success);
    }

    @Operation(summary = "대상 타입과 고유번호로 이미지 불러오기", description = "대상 정보(type,id)에 해당하는 모든 이미지를 List로 불러옵니다.")
    @GetMapping("/images/{targetType}/{targetId}")
    public ResponseEntity<SuccessResponse<ImageResponse>> getImagesByTarget(@PathVariable String targetType,
                                                                             @PathVariable long targetId) {
    	ImageResponse imagesResponse = imageService.getImagesByTarget(targetType, targetId);

        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey()); // 일반적인 성공 메시지 가정
        String code = SuccessCode.SUCCESS.getCode();
        SuccessResponse<ImageResponse> success = SuccessResponse.of(code, message, imagesResponse);
        return ResponseEntity.ok(success);
    }
    
    @Operation(summary = "단일 이미지 삭제", description = "단일 이미지 삭제, 삭제하면서 displayOrder를 앞당김")
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<SuccessResponse<Void>> deleteImage(@PathVariable long imageId) {
        imageService.deleteImage(imageId);
        
        // dptodo: 임시 코드 = 추후 수정해야함
        String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
        String code = SuccessCode.SUCCESS.getCode();

        // 데이터가 없는 성공 응답을 생성
        SuccessResponse<Void> success = SuccessResponse.of(code, message, null);

        return ResponseEntity.ok(success);
    }
    
    @Operation(summary = "다중 이미지 삭제", description = "다중 이미지 삭제")
    @DeleteMapping("/images/{targetType}/{targetId}")
    public ResponseEntity<SuccessResponse<Void>> deleteImagesByTarget(String targetType, long targetId){
    	imageService.deleteImagesByTarget(targetType, targetId);

    	// dptodo = 코드 나중에 추가
	    String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
	    String code = SuccessCode.SUCCESS.getCode();
	
	    SuccessResponse<Void> success = SuccessResponse.of(code, message, null);
	
	    return ResponseEntity.ok(success);
    }
    
    /*
     *  이 엔드포인트는 JSON 데이터(ImageUpdateRequest)와 파일 데이터(List<MultipartFile>)를
     *  동시에 받아야 한다. 이런 multipart/form-data 요청을 처리할 때는 @RequestParam 대신 @RequestPart를 사용한다.
     */
    /**
     * @param request JSON 형식의 문자열이 담겨 올 것이고,</br>Spring이 이를 ImageUpdateRequest 객체로 자동 변환한다.
     * @param files files라는 이름의 파트에는 이미지 파일들이 담겨온다.</br>required = false 는 새로 추가하는 이미지가 없을 수도 있기 때문
     */
    @Operation(summary = "이미지 목록 일괄 업데이트", description = "이미지 추가, 삭제, 순서 변경을 한 번에 처리.")
    @PutMapping("/images")
    public ResponseEntity<SuccessResponse<Void>> updateImages(@RequestPart("request") ImageUpdateRequest request,
										@RequestPart(value = "files", required = false) List<MultipartFile> files){
    	// imageService.updateImages(request, files); // 서비스 메소드 호출
    	
    	// 임시 성공 응답
	    String message = "이미지 목록이 성공적으로 업데이트되었습니다."; // 임시 메시지
	    String code = "200"; // 임시 코드
	    SuccessResponse<Void> success = SuccessResponse.of(code, message, null);
	    return ResponseEntity.ok(success);
    }
    
    @PostMapping("/images/imageId/draft") //정나영 : 보드 아이디만 미리 발급
    public ResponseEntity<SuccessResponse<Long>> selectImageId(){
    	
    	Long imageId = imageService.getBoardId();
    	String message = messageUtil.getMessage(SuccessCode.SUCCESS.getMessageKey());
    	String code = SuccessCode.SUCCESS.getCode();
    	
    	SuccessResponse<Long> success = SuccessResponse.of(code,message,imageId);
    	
    	return ResponseEntity.ok(success);
    	
    }
    
}