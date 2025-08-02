package kr.co.ucp.mntr.main.service;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import kr.co.ucp.cmm.service.PrprtsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.ucp.cmm.util.CommonUtil;
//import kr.co.ucp.service.util.ScrtKey;

@Component
public class DivList {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "divService")
	private DivService divService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@PostConstruct
	public void getCheckKey() throws Exception {
		
//		String scmpYn = prprtsService.getGlobals("Globals.ScmpYn", "Y");
//
//		if (!"N".equals(scmpYn)) {
//			//String c0 = prprtsService.getString("SCMP_SCRT_KEY");
//			String c0 = "";
//			//String c2 = ScrtKey.chkAES(c0);
//			String c2 = "";
//
//			logger.info("c0: {}, c2: {}", c0, c2);
//
//			String expirationYmd = c2.substring(0, 8);
//			String macAddr = c2.substring(8);
//			//boolean macCheck = ScrtKey.chkMAC(macAddr);
//			boolean macCheck = true;
//
//			if (expirationYmd.compareTo(CommonUtil.getCurrentYmd()) < 0) {
//				logger.info("--------------------------------------");
//				logger.info("--> 인증기간이 지났습니다. =================");
//				logger.info("---------------------------------------");
//				System.exit(0);
//			}
//			if (!macCheck) {
//				logger.info("--------------------------------------");
//				logger.info("--> 인증된 키가 아닙니다. ==================");
//				logger.info("--------------------------------------");
//				System.exit(0);
//			}
//		}
//		logger.info("--------------------------------------");
//		logger.info("--> 정상적으로 인증되었습니다.================");
//		logger.info("--------------------------------------");
	}

	private List<DivVO> divList;

	public void setDivList(DivVO vo) throws Exception {
		List<DivVO> list = divService.selectDivList(vo);
		this.divList = list;
	}

	public List<DivVO> getDivList() {
		return divList;
	}
}
