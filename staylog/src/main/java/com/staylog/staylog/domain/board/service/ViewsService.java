package com.staylog.staylog.domain.board.service;

public interface ViewsService {

    public int CountByBoardId(Long boardId);
    public void addView(Long boardId, Long userId);

}
