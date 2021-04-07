package com.heaven.mvc.board.dao;

import java.util.List;

import com.heaven.mvc.board.domain.BoardVO;

public interface BoardDao {
	
	List<BoardVO> list();
	
	int delete(BoardVO boardVO);
	
	int deleteAll();
	
	int update(BoardVO boardVO);
	
	void insert(BoardVO boardVO);
	
	BoardVO select(int seq);
	
	int updateReadCount(int seq);
}
