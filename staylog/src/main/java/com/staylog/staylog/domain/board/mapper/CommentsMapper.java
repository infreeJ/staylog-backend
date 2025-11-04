package com.staylog.staylog.domain.board.mapper;

import com.staylog.staylog.domain.board.dto.CommentsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper {

	// 게시판 고유 번호로 댓글 목록 조회
	List<CommentsDto> getByBoardId(Long boardId);
	// 댓글 등록
    int insert(CommentsDto dto);
    // 댓글 삭제 (논리 삭제 이용)
    int delete(CommentsDto dto);
    // 댓글 수정
    int update(CommentsDto dto);
}