package kr.co.ucp.wrks.sstm.code.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.code.service.CodeSggcdMapper;
import kr.co.ucp.wrks.sstm.code.service.CodeSggcdService;

import org.springframework.stereotype.Service;

@Service("codeSggcdService")
public class CodeSggcdServiceImpl implements CodeSggcdService {

	@Resource(name="codeSggcdMapper")
	private CodeSggcdMapper codeSggcdMapper;

	// 시군구코드 시도명 검색
//	@Override
//	public List<Map<String, Object>>  sidoNm_SigunguList(Map<String, Object> args) throws Exception {
//		return codeSggcdMapper.sidoNm_SigunguList(args);
//	}

	// 시군구코드 시군구명 조건검색
//	@Override
//	public List<Map<String, String>>  sigunguCdList(Map<String, Object> args) throws Exception {
//		return codeSggcdMapper.sigunguCdList(args);
//	}

	// 시군구코드 조건검색
	@Override
	public List<Map<String, String>> selectWorship(Map<String, String> args) throws Exception {
		return codeSggcdMapper.selectWorship(args);
	}

	// 시군구코드 입력
	@Override
	public int insertWorship(Map<String, Object> args)  throws Exception {
		return codeSggcdMapper.insertWorship(args);
	}

	// 시군구코드 수정
	@Override
	public int updateWorship(Map<String, Object> args) throws Exception {
		return codeSggcdMapper.updateWorship(args);
	}

	// 시군구코드 삭제
	@Override
	public int deleteWorship(Map<String, String> args) throws Exception {
		return codeSggcdMapper.deleteWorship(args);
	}

	// 시군구코드 다중삭제
	@Override
	public int deleteWorshipMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			codeSggcdMapper.deleteWorship(arg);
		}

		return 1;
	}
}
