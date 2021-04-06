package com.heaven.mvc.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.heaven.mvc.board.domain.BoardVO;


@Repository
public class BoardDaoMybatis implements BoardDao{
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Autowired
	public void setSqlMapClient(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	@Override
	public List<BoardVO> list() {
		return sqlSessionTemplate.selectList("list");
	}
}
