package kr.co.ucp.link.base.cctv.service.impl;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.link.base.cctv.service.BaseCctvMapper;
import kr.co.ucp.link.base.cctv.service.BaseCctvService;

//import kr.co.ucp.cmmn.CommUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("baseCctvService")
public class BaseCctvServiceImpl implements BaseCctvService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "baseCctvMapper")
    private BaseCctvMapper baseCctvMapper;

    @Override
    public Map<String, Object> linkBaseCctvListData(Map<String, Object> rqMap) throws Exception {
		String dstrtCd = prprtsService.getString("DSTRT_CD");
		
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String result_msg = "success";
        String result_code = "000";

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("dstrtCd", dstrtCd);
            params.put("stateYn", "N");

            List<Map<String, Object>> data = selectCctvList(params);
            int dataCnt = 0;
            if (data != null && !data.isEmpty()) dataCnt = data.size();
            rtnMap.put("dataCnt", dataCnt);
            rtnMap.put("data", data);
        } catch (Exception e) {
            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
            result_code = "901";
            result_msg = "Exception " + e.getMessage();
        }

        rtnMap.put("responseCd", result_code);
        rtnMap.put("responseMsg", result_msg);
        rtnMap.put("dstrtCd", dstrtCd);
        return rtnMap;
    }

    @Override
    public Map<String, Object> linkBaseCctvStateData(Map<String, Object> rqMap) throws Exception {
		String dstrtCd = prprtsService.getString("DSTRT_CD");

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String result_msg = "success";
        String result_code = "000";

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("dstrtCd", dstrtCd);
            params.put("stateYn", "Y");

            List<Map<String, Object>> data = selectCctvList(params);
            int dataCnt = 0;
            if (data != null && !data.isEmpty()) dataCnt = data.size();
            rtnMap.put("dataCnt", dataCnt);
            rtnMap.put("data", data);
        } catch (Exception e) {
            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
            result_code = "901";
            result_msg = "Exception " + e.getMessage();
        }

        rtnMap.put("responseCd", result_code);
        rtnMap.put("responseMsg", result_msg);
        rtnMap.put("dstrtCd", dstrtCd);
        return rtnMap;
    }

    @Override
    public Map<String, Object> linkBaseCctvPresetData(Map<String, Object> rqMap) throws Exception {
		String dstrtCd = prprtsService.getString("DSTRT_CD");

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String result_msg = "success";
        String result_code = "000";

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("dstrtCd", dstrtCd);

            List<Map<String, Object>> data = selectCctvPresetList(params);
            int dataCnt = 0;
            if (data != null && !data.isEmpty()) dataCnt = data.size();
            rtnMap.put("dataCnt", dataCnt);
            rtnMap.put("data", data);
        } catch (Exception e) {
            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
            result_code = "901";
            result_msg = "Exception " + e.getMessage();
        }

        rtnMap.put("responseCd", result_code);
        rtnMap.put("responseMsg", result_msg);
        rtnMap.put("dstrtCd", dstrtCd);
        return rtnMap;
    }

    @Override
    public List<Map<String, Object>> selectCctvList(Map<String, Object> params) throws Exception {
        return baseCctvMapper.selectCctvList(params);
    }

    @Override
    public List<Map<String, Object>> selectCctvPresetList(Map<String, Object> params) throws Exception {
        return baseCctvMapper.selectCctvPresetList(params);
    }
}
