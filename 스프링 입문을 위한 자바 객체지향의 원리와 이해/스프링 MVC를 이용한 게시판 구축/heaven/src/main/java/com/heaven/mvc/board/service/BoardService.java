package com.heaven.mvc.board.service;

import java.util.List;

import com.heaven.mvc.board.domain.BoardVO;

public interface BoardService {
	List<BoardVO> list();
	
	int delete(BoardVO boardVO);
	
	int edit(BoardVO boardVO);
	
	void write(BoardVO boardVO);
	
	BoardVO read(int seq);
}
