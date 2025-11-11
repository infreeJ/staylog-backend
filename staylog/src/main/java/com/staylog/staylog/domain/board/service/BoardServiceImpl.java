package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;

import com.staylog.staylog.domain.board.dto.BookingDto;
import com.staylog.staylog.domain.board.dto.request.BoardListRequest;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.domain.image.assembler.ImageAssembler;
import com.staylog.staylog.global.common.dto.PageRequest;
import com.staylog.staylog.global.common.response.PageResponse;
import com.staylog.staylog.global.event.ReviewCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageAssembler imageAssembler;


    @Override
    public BoardListResponse getByBoardType(BoardListRequest boardListRequest) {

        String boardType = boardListRequest.getBoardType();
        if (boardType == null) {
            boardType = "BOARD_JOURNAL"; // 기본값
        }


        // 전체 게시글 수
        int totalCount = boardMapper.countByBoardType(boardListRequest.getBoardType());

        int totalPage = (int) Math.ceil((double) totalCount / boardListRequest.getPageSize());



        // 페이지 계산 결과
        PageResponse pageResponse = new PageResponse();
        pageResponse.calculate(boardListRequest, totalCount);



        // 게시글 목록
        List<BoardDto> boardList = boardMapper.getByBoardType(boardListRequest);

        // 썸네일
        imageAssembler.assembleFirstImage(
                boardList,
                BoardDto::getBoardId,
                BoardDto::setImageData,
                "IMG_FROM_BOARD_JOURNAL_CONTENT"
        );

        System.out.println("🧩 [BoardServiceImpl] 조립 전 게시글 수: " + boardList.size());

        for (BoardDto dto : boardList) {
            System.out.println("➡️ boardId=" + dto.getBoardId() +
                    ", imageData=" + (dto.getImageData() != null ? dto.getImageData().getImageUrl() : "null"));
        }



        // 4️⃣ BoardListResponse로 묶어서 반환
        BoardListResponse boardListResponse = new BoardListResponse();
        boardListResponse.setBoardList(boardList);
        boardListResponse.setPageResponse(pageResponse);


        return boardListResponse;

    }

    // 게시글 상세보기
    @Override
    public BoardDto getByBoardId(long boardId) {
        boardMapper.updateLikeCount(boardId);
        boardMapper.updateViewsCount(boardId);
        return boardMapper.getByBoardId(boardId);
    }

    // 게시글 등록
    @Override
    @Transactional
    public BoardDto insert(BoardDto boardDto) {

        boardMapper.insert(boardDto);

        // =============== 리뷰 게시글 작성 이벤트 발행(알림 발송) ==================
        if(boardDto.getBoardType().equals("BOARD_REVIEW")) { // 리뷰 게시글만 알림 전송
            ReviewCreatedEvent event = new ReviewCreatedEvent(boardDto.getBoardId(), boardDto.getAccommodationId(), boardDto.getBookingId(), boardDto.getUserId());
            eventPublisher.publishEvent(event);
        }

        return boardDto;
    }

    @Override
    public void update(BoardDto boardDto) {

        boardMapper.update(boardDto);

    }

    @Override
    public void delete(long boardId) {

        boardMapper.delete(boardId);

    }

    @Override
    public List<BookingDto> bookingList(long userId) {

        return boardMapper.bookingList(userId);
    }




}
