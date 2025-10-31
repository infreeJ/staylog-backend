package com.staylog.staylog.domain.board.service;

import com.staylog.staylog.domain.board.dto.BoardDto;
import com.staylog.staylog.domain.board.dto.request.BoardListRequest;
import com.staylog.staylog.domain.board.dto.request.BoardRequest;
import com.staylog.staylog.domain.board.dto.response.BoardListResponse;
import com.staylog.staylog.domain.board.mapper.BoardMapper;
import com.staylog.staylog.global.common.dto.PageRequest;
import com.staylog.staylog.global.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;



    @Override
    public BoardListResponse getByBoardType(BoardListRequest boardListRequest, PageRequest pageRequest) {


        return boardMapper.getByBoardType(boardListRequest);

    }

    @Override
    public void insert(BoardRequest boardRequest) {

        // Request -> DTO
        BoardDto boardDto = BoardDto.builder()
                .boardId(boardRequest.getBookingId())
                .accommodationId(boardRequest.getAccommodationId())
                .bookingId(boardRequest.getBookingId())
                .userId(boardRequest.getUserId())
                .boardType(boardRequest.getBoardType())
                .regionCode(boardRequest.getRegionCode())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .rating(boardRequest.getRating())
                .build();


        boardMapper.insert(boardDto);

    }

    @Override
    public void update(BoardRequest boardRequest) {

        // Request -> DTO
        BoardDto boardDto = BoardDto.builder()
                .boardId(boardRequest.getBookingId())

                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .rating(boardRequest.getRating())
                .build();

        boardMapper.update(boardDto);
    }

    @Override
    public void delete(long boardId) {

    }
}
