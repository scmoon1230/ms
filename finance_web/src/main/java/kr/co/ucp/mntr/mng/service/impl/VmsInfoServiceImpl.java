package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.mntr.mng.service.VmsInfoService;

@Service("vmsInfoService")
public class VmsInfoServiceImpl implements VmsInfoService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "vmsInfoMapper")
	private VmsInfoMapper vmsInfoMapper;

	@Override
	public List<Map<String, Object>> selectVmsList(Map<String, Object> params) throws Exception {
		return vmsInfoMapper.selectVmsList(params);
	}

	@Override
	public List<Map<String, String>> vmsGrpList(Map<String, Object> params) throws Exception {
		return vmsInfoMapper.vmsGrpList(params);
	}

	@Override
	public List<Map<String, Object>> dstrtNmCd(Map<String, Object> params) throws Exception {
		return vmsInfoMapper.dstrtNmCd(params);
	}

	@Override
	public int insertVmsInfo(Map<String, Object> params) throws Exception {
		return vmsInfoMapper.insertVmsInfo(params);
	}

	@Override
	public int updateVmsInfo(Map<String, Object> map) throws Exception {
		return vmsInfoMapper.updateVmsInfo(map);
	}

	@Override
	public int deleteVmsInfo(Map<String, String> map) throws Exception {
		return vmsInfoMapper.deleteVmsInfo(map);
	}
}
