package kr.co.ucp.wrks.sstm.mpg.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.wrks.sstm.mpg.service.MpgInfoMapper;
import kr.co.ucp.wrks.sstm.mpg.service.MpgInfoService;

/**
 * @author user
 *
 */
@Service("mpgInfoService")
public class MpgInfoServiceImpl implements MpgInfoService
{
	@Resource(name="mpgInfoMapper")
	private MpgInfoMapper mpgInfoMapper;

	@Override
	public List<Map<String, String>> selectMpgInfoList(Map<String, String> args) throws Exception {
		return mpgInfoMapper.selectMpgInfoList(args);
	}
	@Override
	public int insertMpgInfo(Map<String, Object> args) throws Exception {
		args.put("rgsUserId", SessionUtil.getUserId());
		return mpgInfoMapper.insertMpgInfo(args);
	}
	@Override
	public int updateMpgInfo(Map<String, Object> args) throws Exception {
		args.put("rgsUserId", SessionUtil.getUserId());
		return mpgInfoMapper.updateMpgInfo(args);
	}
	@Override
	public int deleteMpgInfo(Map<String, Object> args) throws Exception {
		return mpgInfoMapper.deleteMpgInfo(args);
	}
	@Override
	public int deleteMpgInfoMulti(List<Map<String, Object>> args) throws Exception {

	int ret = 0;
	for(int i = 0; i < args.size(); i++) {

		Map<String, Object> arg = (Map<String, Object>) args.get(i);

		ret = mpgInfoMapper.deleteMpgInfo(arg);

	}
	return ret;
}
}