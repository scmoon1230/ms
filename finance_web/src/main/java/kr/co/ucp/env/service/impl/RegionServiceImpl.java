package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.RegionMapper;
import kr.co.ucp.env.service.RegionService;

@Service("regionService")
public class RegionServiceImpl implements RegionService {

	@Resource(name="regionMapper")
	private RegionMapper regionMapper;

	// 구역 조건검색
	@Override
	public List<Map<String, String>> selectRegion(Map<String, String> args) throws Exception {
		return regionMapper.selectRegion(args);
	}

	// 구역 입력
	@Override
	public int insertRegion(Map<String, Object> args)  throws Exception {
		return regionMapper.insertRegion(args);
	}

	// 구역 수정
	@Override
	public int updateRegion(Map<String, Object> args) throws Exception {
		return regionMapper.updateRegion(args);
	}

	// 구역 삭제
	@Override
	public int deleteRegion(Map<String, String> args) throws Exception {
		return regionMapper.deleteRegion(args);
	}

	// 구역 다중삭제
	@Override
	public int deleteRegionMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			regionMapper.deleteRegion(arg);
		}

		return 1;
	}

}
