package kr.co.ucp.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.link.job.cctv.service.JobCctvListService;
import kr.co.ucp.link.job.cctv.service.JobCctvStateService;
import kr.co.ucp.link.job.cctvlog.service.JobCctvLogCnnctService;
import kr.co.ucp.link.job.user.service.JobUserBaseService;
import kr.co.ucp.link.job.user.service.JobUserWideService;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@Configuration
@EnableScheduling
public class CmmScheduler {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name = "jobCctvListService")
	private JobCctvListService jobCctvListService;

    @Resource(name = "jobCctvStateService")
    private JobCctvStateService jobCctvStateService;

	@Resource(name = "jobCctvLogCnnctService")
	private JobCctvLogCnnctService jobCctvLogCnnctService;

	@Resource(name = "jobUserBaseService")
	private JobUserBaseService jobUserBaseService;

	@Resource(name = "jobUserWideService")
	private JobUserWideService jobUserWideService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 카메라 정보와 사용자 정보를 광역과 기초간에 동기화한다.
	//@Scheduled(cron = "0 30 6 * * *")		// 매일 6시 30분에 동작
	public void syncCctvUserInfo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		if ("PVEBASE".equalsIgnoreCase(G_SYS_ID)) {		// 기초일 때
			return;
		}
		
		logger.info("--> syncCctvUserInfo() 시작 <--");

		jobCctvListService.jobCctvListChk("ALL");		// 기초 카메라등록정보를 광역으로 가져옴
		
		//jobUserBaseService.jobUserBaseChk("ALL");		// 기초 사용자등록정보를 광역으로 가져옴

		jobUserWideService.jobUserWideChk("ALL");		// 광역 사용자등록정보를 기초로 보냄
		
	}

	// 카메라 정보와 사용자 정보를 광역과 기초간에 동기화한다.
	//@Scheduled(fixedDelay = 3600000)		// 이전 종료시각으로부터 매 3600초마다 동작
	public void syncCctvInfo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		if ("PVEBASE".equalsIgnoreCase(G_SYS_ID)) {		// 기초일 때
			return;
		}
		
		logger.info("--> syncCctvInfo() 시작 <--");

        jobCctvStateService.jobCctvStateChk("ALL");			// 기초 카메라상태정보를 광역으로 가져옴
		
		jobCctvLogCnnctService.jobCctvLogCnnctChk("ALL");	// 광역 영상접속정보를 기초로 보냄

		
	}
				
	
	

	//@Scheduled(cron = "0 0 5 * * *")		// 매일 5시에 동작
	//@Scheduled(cron = "0 33 16 * * *")	// 매일 16시 33분에 동작
	//@Scheduled(fixedRate = 5000)			// 이전 시작시각으로부터 매 5초마다 동작
	//@Scheduled(fixedDelay = 5000)			// 이전 종료시각으로부터 매 5초마다 동작
	public void testPrint() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
		logger.info("--> testPrint() => " + sdf.format(now));
	}
	
	//@Scheduled(fixedDelay = 10000)		// 이전 task 종료 시간으로부터 매 10초마다 동작
	public void testPrint2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
		logger.info("--> testPrint2() => " + sdf.format(now));
	}
	
}
