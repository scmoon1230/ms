package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.menu.service.MenuMenuMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuMenuService;

import org.springframework.stereotype.Service;

@Service("menuMenuService")
public class MenuMenuServiceImpl implements MenuMenuService {
	
	@Resource(name="menuMenuMapper")	
	private MenuMenuMapper menuMenuMapper;
	
	// 프로그램메뉴 조건검색
	@Override
	public List<Map<String, Object>> select_pgm_menu(Map<String, Object> args)
			throws Exception {
		return menuMenuMapper.select_pgm_menu(args);
	}
	
	// 프로그램메뉴 프로그램목록 조회
	@Override
	public List<Map<String, Object>> list_prgm(Map<String, Object> args)
			throws Exception {
		return menuMenuMapper.list_prgm(args);
	}
	
	// 프로그램메뉴 등록
	@Override
	public int insert_pgm_menu(Map<String, Object> args) throws Exception {
		return menuMenuMapper.insert_pgm_menu(args);
	}

	// 프로그램메뉴 수정
	@Override
	public int update_pgm_menu(Map<String, Object> args) throws Exception {
		return menuMenuMapper.update_pgm_menu(args);
	}

	// 프로그램메뉴 삭제
	@Override
	public int delete_pgm_menu(Map<String, Object> args) throws Exception {
		return menuMenuMapper.delete_pgm_menu(args);
	}

}




