package com.staylog.staylog.domain.image.assembler;

import com.staylog.staylog.domain.image.dto.ImageData;
import com.staylog.staylog.domain.image.dto.ImageDto;
import com.staylog.staylog.domain.image.dto.ImageResponse;
import com.staylog.staylog.domain.image.mapper.ImageMapper;
import com.staylog.staylog.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Map<Long, ImageResponse> imageMap = imageService.getImagesByTargets(targetType, targetIds);

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
    public <T> void assembleFirstImage(
            List<T> mainDataList,
            Function<T, Long> idExtractor,
            BiConsumer<T, ImageData> imageSetter,
            String targetType) {

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
            mainDataList.forEach(data -> imageSetter.accept(data, null)); // 대표 이미지가 없으면 null 설정
            return;
        }

        // 2. ImageMapper를 통해 여러 targetId에 대한 대표 이미지들을 조회합니다. (ImageDto 반환)
        List<ImageDto> representativeImageDtos = imageMapper.selectFirstImageByTargetIds(targetType, targetIds);

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

        // 4. 메인 데이터 목록을 순회하며 각 데이터에 맞는 대표 이미지 정보를 설정합니다.
        mainDataList.forEach(data -> {
            Long id = idExtractor.apply(data);
            if (id != null) {
                ImageData representativeImage = representativeImageMap.get(id);
                imageSetter.accept(data, representativeImage); // 대표 이미지가 없으면 null이 설정됨
            } else {
                imageSetter.accept(data, null); // ID가 없으면 null 설정
            }
        });
    }
}