package kr.co.ucp.link.base.cctvlog.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.link.base.cctvlog.service.BaseCctvLogCnnctMapper;
import kr.co.ucp.link.base.cctvlog.service.BaseCctvLogCnnctService;

@Service("baseCctvLogCnnctService")
public class BaseCctvLogCnnctServiceImpl  implements BaseCctvLogCnnctService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	private GblVal gVal = GblVal.getInstance();

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "cctvViewLogIdGnr")
    private EgovIdGnrService viewIdGnr;

	@Resource(name="baseCctvLogCnnctMapper")
	private BaseCctvLogCnnctMapper baseCctvLogCnnctMapper;

	@Override
	public int updateCctvLogCnnct(Map<String, Object> map) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		String result_msg = "success";
		
//		String gLogStoreTy = gVal.getCctvLogType();
//		String gLogStoreTy = "FILE";
		String cctvLogPath="D:/cubeapps/data/log";
		String gLogStoreTy = "DB";
//		String gDstrtCd = gVal.getDstrtCd();
//		String gDstrtCd = prprtsService.getString("DSTRT_CD");
		int resultCode = 0;

		String trnsDstrtCd = CommUtil.objNullToVal(map.get("trnsDstrtCd"), ""); // 발송처지구코드;

		List<LinkedHashMap<String, String>> dataMapList = (List<LinkedHashMap<String, String>>) map.get("data");
		try {
			int dataMapListCnt = dataMapList.size();
			if (dataMapList == null || dataMapList.isEmpty()) return resultCode;
			
			if ("FILE".equals(gLogStoreTy)) {
				String gFilePath = cctvLogPath + File.separator + CommUtil.getTimeStr("yyyy");
				String fileName = "cctv_connect_log_" + trnsDstrtCd + "_" + CommUtil.getCurrentTime17() + ".csv";
				int f = CommUtil.listMapToCsv(dataMapList, gFilePath, fileName);
				
			} else {
				for (int i = 0; i < dataMapListCnt; i++) {
					Map<String, String> dataMap = dataMapList.get(i);

					dataMap.put("seqNo", String.valueOf(viewIdGnr.getNextLongId()));
					resultCode = baseCctvLogCnnctMapper.updateCctvLogCnnct(dataMap);
				}
			}
		} catch (Exception e) {
			logger.error("==== ERROR Exception >>>> {}", e.getMessage());
			result_msg = "Exception" + e.getMessage();
		}

		return resultCode;
	}

}
