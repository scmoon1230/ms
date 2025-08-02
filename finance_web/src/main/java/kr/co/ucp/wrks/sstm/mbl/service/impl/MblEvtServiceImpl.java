package kr.co.ucp.wrks.sstm.mbl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.mbl.service.MblEvtMapper;
import kr.co.ucp.wrks.sstm.mbl.service.MblEvtService;

@Service("mblEvtService")
public class MblEvtServiceImpl implements MblEvtService {

    @Resource(name="mblEvtMapper")
    private MblEvtMapper mblEvtMapper;

	@Override
	public List<Map<String, String>> getEventList(Map<String, String> map) throws Exception {
		return mblEvtMapper.getEventList(map);
	}

	@Override
	public List<Map<String, String>> getEventNmList(Map<String, String> map) throws Exception {
		return mblEvtMapper.getEventNmList(map);
	}

	@Override
	public List<Map<String, String>> getEventNmListPopup(Map<String, String> map) throws Exception {
		return mblEvtMapper.getEventNmListPopup(map);
	}

	@Override
	public int updateMbl(Map<String, Object> map) throws Exception {
		return mblEvtMapper.updateMbl(map);
	}

	@Override
	public int deleteMbl(Map<String, Object> map) throws Exception {
		return mblEvtMapper.deleteMbl(map);
	}

	@Override
	public int insertMblUser(Map<String, Object> map) throws Exception {
		return mblEvtMapper.insertMblUser(map);
	}

	@Override
	public int checkMblUser(Map<String, Object> map) throws Exception {
		return mblEvtMapper.checkMblUser(map);
	}
}



