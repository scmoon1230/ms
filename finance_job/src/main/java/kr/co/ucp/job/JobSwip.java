package kr.co.ucp.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import kr.co.ucp.cmns.EgovStringUtil;
import kr.co.ucp.swip.controller.SwipController;

// QuartzJobBean은 복잡한스케줄링에 적합한 유연한 스케줄러.
// 상속받아서 스케줄러를 구현.
public class JobSwip extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 실제 실행될 태스크
	private SwipController swipController;

	// 실제 실행될 태스크를 setter방식으로 주입
	public void setSwipController(SwipController swipController) {
		this.swipController = swipController;
	}

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info(" ==== swip job start >>>>  ");
			
			String rtn = swipController.swipJobStart();
			
			if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(rtn)) ) {
				logger.info(" ==== "+EgovStringUtil.nullConvert(rtn)+" ====");
			}
			
			logger.info(" ==== swip job end >>>>  ");
		} catch (Exception e) {
			logger.error(" ==== JobExecutionContext  Exception >>>> {} ", e.getMessage());
		}
	}
}
