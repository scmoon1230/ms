package kr.co.ucp.mntr.mng.service.impl;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.mng.service.MngPresetService;
import kr.co.ucp.mntr.mng.service.MngSrchVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("mngPresetService")
public class MngPresetServiceImpl implements MngPresetService {

	@Resource(name = "mngPresetMapper")
	private MngPresetMapper mngPresetMapper;

	@Override
	public List<EgovMap> selectMngPresetList(MngSrchVO vo) throws Exception {
		return mngPresetMapper.selectMngPresetDataList(vo);
	}

	@Override
	public EgovMap selectPresetList(Map<String, String> map) throws Exception {
		EgovMap egovMap = new EgovMap();
		List<EgovMap> result = mngPresetMapper.selectPresetList(map);
		if (result == null || result.isEmpty()) {
			result = new ArrayList<EgovMap>();
			egovMap.put("size", 0);
		} else {
			egovMap.put("size", result.size());
		}
		egovMap.put("list", result);
		return egovMap;
	}

	@Override
	public EgovMap mergePreset(Map<String, String> map) throws Exception {
		String userId = SessionUtil.getUserId();
		map.put("userId", userId);

		String sPresetNum = EgovStringUtil.nullConvert(map.get("presetNum"));
		EgovMap egovMap = new EgovMap();
		if ("0".equals(sPresetNum)) {
			int nRsult = mngPresetMapper.updateFcltOsvt(map);
			egovMap.put("size", nRsult);
		} else {
			int nRsult = mngPresetMapper.mergePreset(map);
			egovMap.put("size", nRsult);
		}
		return egovMap;
	}

	@Override
	public EgovMap deletePreset(Map<String, String> map) throws Exception {
		String userId = SessionUtil.getUserId();
		map.put("userId", userId);

		String sPresetNum = EgovStringUtil.nullConvert(map.get("presetNum"));
		EgovMap egovMap = new EgovMap();

		if ("0".equals(sPresetNum)) {
			map.put("pointX", "0");
			map.put("pointY", "0");
			int nRsult = mngPresetMapper.updateFcltOsvt(map);
			egovMap.put("size", nRsult);
		} else {
			int nRsult = mngPresetMapper.deletePreset(map);
			egovMap.put("size", nRsult);
		}
		return egovMap;
	}

}
