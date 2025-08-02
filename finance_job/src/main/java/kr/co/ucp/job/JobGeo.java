package kr.co.ucp.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import kr.co.ucp.cmns.EgovStringUtil;
import kr.co.ucp.geo.controller.GeoController;

// QuartzJobBean은 복잡한스케줄링에 적합한 유연한 스케줄러.
// 상속받아서 스케줄러를 구현.
public class JobGeo extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 실제 실행될 태스크
	private GeoController geoController;

	// 실제 실행될 태스크를 setter방식으로 주입
	public void setGeoController(GeoController geoController) {
		this.geoController = geoController;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info(" ==== gis job start >>>>  ");
			
			String rtn = geoController.geoJobStart();
			
			if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(rtn)) ) {
				logger.info(" ==== "+EgovStringUtil.nullConvert(rtn)+" ====");
			}
			
			logger.info(" ==== gis job end >>>>  ");
		} catch (Exception e) {
			logger.error(" ==== JobExecutionContext  Exception >>>> {} ", e.getMessage());
		}
	}
}
