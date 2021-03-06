package com.heaven.mvc.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.heaven.mvc.board.domain.BoardVO;
import com.heaven.mvc.board.service.BoardService;

@Controller
@SessionAttributes("boardVO")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	// ResponseBody 어노테이션이 적용된 메서드는 반환하는 문자열을 그대로 웹 브라우저에 응답한다 jsp파일 없어도 된다.
	@RequestMapping(value="board/listtest")
	@ResponseBody
	public String listTest() {
		return boardService.list().toString();
	}
	
	// 리스트
	@RequestMapping(value="board/list")
	public String list(Model model) {
		List<BoardVO> list = boardService.list();
		for(BoardVO board : list) {
			System.out.println(board.getSeq());
		}
		model.addAttribute("boardList", boardService.list());
		return "board/list";
	}
	
	// 글의 상세뷰
	@RequestMapping(value="board/read/{seq}")
	public String read(Model model, @PathVariable int seq) {
		model.addAttribute("boardVO", boardService.read(seq));
		return "/board/read";
	}
	
	// 글 작성 뷰
	@RequestMapping(value="board/write", method=RequestMethod.GET)
	public String write(Model model) {
		model.addAttribute("boardVO", new BoardVO());
		return "/board/write";
	}
	
	// 글 작성 + 오버로딩 + 유효성검사
	@RequestMapping(value="board/write", method=RequestMethod.POST)
	public String write(@Valid BoardVO boardVO, BindingResult bindingResult, SessionStatus sessionStatus) {
		if(bindingResult.hasErrors()) {
			return "/board/write";
		} else {
			boardService.write(boardVO);
			sessionStatus.setComplete();
			return "redirect:/board/list";
		}
	}
	
	// 수정 뷰 테스
	@RequestMapping(value="board/edit/{seq}test", method = RequestMethod.GET)
	public String editTest(@PathVariable int seq, Model model) {
		BoardVO boardVO = boardService.read(seq);
		model.addAttribute("boardVO", boardVO);
		return "/board/edit";
	}
	// 수정 뷰
	@RequestMapping(value="board/edit", method = RequestMethod.GET)
	public String edit(@ModelAttribute BoardVO boardVO) {
		return "/board/edit";
	}
	
	// 수정 + 오버로딩 + 유효성검사
	@RequestMapping(value="board/edit", method = RequestMethod.POST)
	public String edit(@Valid @ModelAttribute BoardVO boardVO, BindingResult bindingResult, int pwd, SessionStatus sessionStatus, Model model) {
		if(bindingResult.hasErrors()) {
			return "board/edit";
		} else {
			if(boardVO.getPassword() == pwd) {
				boardService.edit(boardVO);
				sessionStatus.setComplete();
				return "redirect:/board/list";
			}
			
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "/board/edit";
		}
	}
	
	// 삭제 뷰
	@RequestMapping(value="board/delete/{seq}", method = RequestMethod.GET)
	public String delete(@PathVariable int seq, Model model) {
		model.addAttribute("seq", seq);
		return "/board/delete";
	}
	
	// 삭제뷰 + 오버로딩 + 유효성검
	@RequestMapping(value="board/delete", method = RequestMethod.POST)
	public String delete(int seq, int pwd, Model model) {
		int rowCount; // 레코드 개수
		BoardVO boardVO = new BoardVO();
		boardVO.setSeq(seq);
		boardVO.setPassword(pwd);
		
		rowCount = boardService.delete(boardVO);
		
		if(rowCount == 0) {
			model.addAttribute("seq", seq);
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "/board/delete";
		} else {
			return "redirect:/board/list";
		}
	}
	
}
