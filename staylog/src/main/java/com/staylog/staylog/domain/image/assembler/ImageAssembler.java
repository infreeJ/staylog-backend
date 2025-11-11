package com.staylog.staylog.domain.image.assembler;

import com.staylog.staylog.domain.image.dto.ImageData;
import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.dto.ImageResponse;
import com.staylog.staylog.domain.image.mapper.ImageMapper;
import com.staylog.staylog.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageAssembler {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    /**
     * 메인 데이터 목록에 '이미지 목록 전체'를 조합하는 범용 메서드.
     *
     * @param mainDataList 메인 데이터 목록 (e.g., List<AdminAccommodationDetailResponse>)
     * @param idExtractor  메인 데이터에서 targetId를 추출하는 함수 (e.g., AdminAccommodationDetailResponse::getAccommodationId)
     * @param imageSetter  메인 데이터에 이미지 목록을 설정하는 BiConsumer (e.g., (dto, images) -> dto.setImages(images))
     * @param targetType   이미지를 조회할 targetType (e.g., "ACCOMMODATION")
     * @param <T>          메인 데이터의 타입
     */
    public <T> void assembleImages(
            List<T> mainDataList,
            Function<T, Long> idExtractor,
            BiConsumer<T, List<ImageData>> imageSetter,
            String targetType) {
    		String prefixedTargetType = "IMG_FROM_"+targetType;

        if (mainDataList == null || mainDataList.isEmpty()) {
            return;
        }

        // 1. 메인 데이터 목록에서 Long 타입의 ID 목록을 추출합니다.
        List<Long> targetIds = mainDataList.stream()
                .map(idExtractor)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        // ID가 없으면 이미지 조회 불필요
        if (targetIds.isEmpty()) {
            mainDataList.forEach(data -> imageSetter.accept(data, java.util.Collections.emptyList()));
            return;
        }

        // 2. ID 목록을 사용하여 이미지 정보를 한 번의 쿼리로 가져옵니다.
        //    결과는 Map<Long, ImageResponse> 형태로 받습니다.
        Map<Long, ImageResponse> imageMap = imageService.getImagesByTargets(prefixedTargetType, targetIds);

        // 3. 메인 데이터 목록을 순회하며 각 데이터에 맞는 이미지 정보를 설정합니다.
        mainDataList.forEach(data -> {
            Long id = idExtractor.apply(data);
            if (id != null) {
                ImageResponse images = imageMap.get(id);
                if (images != null && images.getImages() != null) {
                    imageSetter.accept(data, images.getImages());
                } else {
                    imageSetter.accept(data, java.util.Collections.emptyList());
                }
            } else {
                imageSetter.accept(data, java.util.Collections.emptyList());
            }
        });
    }

    /**
     * 메인 데이터 목록에 '대표 이미지'를 조합하는 범용 메서드.
     *
     * @param mainDataList 메인 데이터 목록
     * @param idExtractor  메인 데이터에서 targetId를 추출하는 함수
     * @param imageSetter  메인 데이터에 대표 이미지를 설정하는 BiConsumer
     * @param targetType   이미지를 조회할 targetType
     * @param <T>          메인 데이터의 타입
     */
    public <T> void assembleMainImage(
            List<T> mainDataList,
            Function<T, Long> idExtractor,
            BiConsumer<T, ImageData> imageSetter,
            String targetType) {
    		String prefixedTargetType = "IMG_FROM_"+targetType;
        if (mainDataList == null || mainDataList.isEmpty()) {
            return;
        }

        // 1. 메인 데이터 목록에서 Long 타입의 ID 목록을 추출합니다.
        List<Long> targetIds = mainDataList.stream()
                .map(idExtractor)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        
        log.info("==> [ImageAssembler] assembleFirstImage 호출");
        log.info("==> targetType: {}", prefixedTargetType);
        log.info("==> targetIds: {}", targetIds);

        // ID가 없으면 이미지 조회 불필요
        if (targetIds.isEmpty()) {
            mainDataList.forEach(data -> imageSetter.accept(data, null)); // 대표 이미지가 없으면 null 설정
            log.info("==> targetIds가 비어있어 null로 설정 후 반환");
            return;
        }

        // 2. ImageMapper를 통해 여러 targetId에 대한 대표 이미지들을 조회합니다. (ImageDto 반환)
        List<ImageDto> representativeImageDtos = imageMapper.selectFirstImageByTargetIds(prefixedTargetType, targetIds);
        log.info("==> DB에서 조회된 대표 이미지 DTO 개수: {}", representativeImageDtos.size());
        if (!representativeImageDtos.isEmpty()) {
        	log.info("==> 첫 번째 조회된 이미지 DTO: {}", representativeImageDtos.get(0));
        }

        // 3. 조회된 ImageDto 리스트를 targetId를 키로, 변환된 ImageData를 값으로 하는 Map으로 변환합니다.
        Map<Long, ImageData> representativeImageMap = representativeImageDtos.stream()
                .collect(Collectors.toMap(
                        ImageDto::getTargetId, // 키는 ImageDto에서 가져옵니다.
                        imageDto -> ImageData.builder() // 값은 ImageDto를 ImageData로 변환하여 생성합니다.
                                .imageId(imageDto.getImageId())
                                .imageUrl("/images/" + imageDto.getSavedUrl())
                                .displayOrder(imageDto.getDisplayOrder())
                                .originalName(imageDto.getOriginalName())
                                .build()
                ));
        log.info("==> 생성된 대표 이미지 Map 크기: {}", representativeImageMap.size());

        // 4. 메인 데이터 목록을 순회하며 각 데이터에 맞는 대표 이미지 정보를 설정합니다.
        mainDataList.forEach(data -> {
            Long id = idExtractor.apply(data);
            if (id != null) {
                ImageData representativeImage = representativeImageMap.get(id);
                imageSetter.accept(data, representativeImage); // 대표 이미지가 없으면 null이 설정됨
                if (representativeImage != null) {
                    log.info("==> targetId {}에 대표 이미지 설정 완료: {}", id, representativeImage.getImageUrl());
                } else {
                    log.info("==> targetId {}에 해당하는 대표 이미지가 없어 null로 설정", id);
                }
            } else {
                imageSetter.accept(data, null); // ID가 없으면 null 설정
            }
        });
    }

    /**
     * 메인 데이터 목록에 '대표 이미지 URL' (String)을 조합하는 범용 메서드.
     *
     * @param mainDataList   메인 데이터 목록
     * @param idExtractor    메인 데이터에서 targetId를 추출하는 함수
     * @param imageUrlSetter 메인 데이터에 대표 이미지 URL(String)을 설정하는 BiConsumer
     * @param targetType     이미지를 조회할 targetType
     * @param <T>            메인 데이터의 타입
     */
    public <T> void assembleMainImageUrl(
            List<T> mainDataList,
            Function<T, Long> idExtractor,
            BiConsumer<T, String> imageUrlSetter, // String 타입의 URL을 설정
            String targetType) {
    		String prefixedTargetType = "IMG_FROM_"+targetType;
        if (mainDataList == null || mainDataList.isEmpty()) {
        		log.info("==> targetIds가 비어있어 null로 설정 후 반환");
            return;
        }

        // 1. 메인 데이터 목록에서 Long 타입의 ID 목록을 추출합니다.
        List<Long> targetIds = mainDataList.stream()
                .map(idExtractor)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        log.info("==> [ImageAssembler] assembleFirstImage 호출");
        log.info("==> targetType: {}", targetType);
        log.info("==> targetIds: {}", targetIds);
        
        // ID가 없으면 이미지 조회 불필요
        if (targetIds.isEmpty()) {
            mainDataList.forEach(data -> imageUrlSetter.accept(data, null));
            return;
        }

        // 2. ImageMapper를 통해 여러 targetId에 대한 대표 이미지들을 조회합니다. (ImageDto 반환)
        List<ImageDto> representativeImageDtos = imageMapper.selectFirstImageByTargetIds(prefixedTargetType, targetIds);

        // 3. 조회된 ImageDto 리스트를 targetId를 키로, 완성된 이미지 URL(String)을 값으로 하는 Map으로 변환합니다.
        Map<Long, String> representativeImageUrlMap = representativeImageDtos.stream()
                .collect(Collectors.toMap(
                        ImageDto::getTargetId,
                        imageDto -> "/images/" + imageDto.getSavedUrl() // 값으로 URL 문자열을 생성
                ));

        // 4. 메인 데이터 목록을 순회하며 각 데이터에 맞는 대표 이미지 URL을 설정합니다.
        mainDataList.forEach(data -> {
            Long id = idExtractor.apply(data);
            if (id != null) {
                String imageUrl = representativeImageUrlMap.get(id);
                imageUrlSetter.accept(data, imageUrl); // 이미지가 없으면 null이 설정됨
                if (imageUrl != null) {
                    log.info("==> targetId {}에 대표 이미지 설정 완료: {}", id, imageUrl);
                } else {
                    log.info("==> targetId {}에 해당하는 대표 이미지가 없어 null로 설정", id);
                }
            } else {
                imageUrlSetter.accept(data, null);
            }
        });
    }

    /**
     * [새로 추가] 메인 데이터 목록에 '대표 이미지 URL'을 조합하는 범용 메서드.
     * targetType을 각 데이터에서 동적으로 추출합니다.
     *
     * @param mainDataList        메인 데이터 목록
     * @param idExtractor         메인 데이터에서 targetId를 추출하는 함수
     * @param imageUrlSetter      메인 데이터에 대표 이미지 URL(String)을 설정하는 BiConsumer
     * @param targetTypeExtractor 메인 데이터에서 targetType(String)을 추출하는 함수
     * @param <T>                 메인 데이터의 타입
     */
    public <T> void assembleMainImageUrl(
            List<T> mainDataList,
            Function<T, Long> idExtractor,
            BiConsumer<T, String> imageUrlSetter,
            Function<T, String> targetTypeExtractor) { // targetType을 동적으로 생성하는 함수

        if (mainDataList == null || mainDataList.isEmpty()) {
            return;
        }

        // 1. targetType별로 메인 데이터 목록을 그룹화합니다.
        Map<String, List<T>> groupedData = mainDataList.stream()
                .collect(Collectors.groupingBy(targetTypeExtractor));

        // 2. 각 그룹(targetType)에 대해 기존의 assembleMainImageUrl 메소드를 호출합니다.
        //    기존 메소드에서 "IMG_FROM_" 접두사를 붙여줄 것이므로, 여기서는 순수한 targetType만 전달합니다.
        groupedData.forEach((targetType, group) -> {
            assembleMainImageUrl(group, idExtractor, imageUrlSetter, targetType);
        });
    }
}