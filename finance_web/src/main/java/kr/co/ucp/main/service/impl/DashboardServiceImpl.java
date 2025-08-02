package kr.co.ucp.main.service.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.main.service.DashboardMapper;
import kr.co.ucp.main.service.DashboardService;

@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "dashboardMapper")
    private DashboardMapper dashboardMapper;
    
    @Override
    public EgovMap selectTvoViewState() {
        return dashboardMapper.selectTvoViewState();
    }
    @Override
    public EgovMap selectTvoViewExtnState() {
        return dashboardMapper.selectTvoViewExtnState();
    }
    @Override
    public EgovMap selectTvoOutState() {
        return dashboardMapper.selectTvoOutState();
    }
    @Override
    public EgovMap selectTvoOutExtnState() {
        return dashboardMapper.selectTvoOutExtnState();
    }

    @Override
    public EgovMap selectTvoCctvState() {
        return dashboardMapper.selectTvoCctvState();
    }

    @Override
    public List<EgovMap> selectTvoViewAprvStateDaily() {
        return dashboardMapper.selectTvoViewAprvStateDaily();
    }

    @Override
    public List<EgovMap> selectTvoOutAprvStateDaily() {
        return dashboardMapper.selectTvoOutAprvStateDaily();
    }

    @Override
    public EgovMap selectTvoViewAprvStateMonthly() {
        return dashboardMapper.selectTvoViewAprvStateMonthly();
    }
    
    @Override
    public EgovMap selectTvoOutAprvStateMonthly() {
        return dashboardMapper.selectTvoOutAprvStateMonthly();
    }

    @Override
    public EgovMap getStorageState() {
    	
    	String dirHome = prprtsService.getString("DIR_WRKS_HOME");
    	
		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	dirHome = dirHome.replace("\\", "/");
		} else {							dirHome = dirHome.replace("/", "\\");
		}
		
    	String driveName = "";
    	double totalSizeTb = 0;
    	double freeSizeTb = 0;
    	double usedSizeTb = 0;
    	int totalSizeGb = 0;
		int freeSizeGb = 0;
		int usedSizeGb = 0;
		int freeRate = 0;
		int usedRate = 0;

		File[] drives = File.listRoots();
		for(File drive : drives) {
			driveName = drive.getAbsolutePath();

			double tSizeGb = drive.getTotalSpace() / Math.pow(1024, 3);		totalSizeGb = (int) tSizeGb;
			double fSizeGb = drive.getUsableSpace() / Math.pow(1024, 3);	freeSizeGb = (int) fSizeGb;
			usedSizeGb = totalSizeGb - freeSizeGb;

			double fRate = fSizeGb / tSizeGb * 100.0;						freeRate = (int) fRate;
			usedRate = 100 - freeRate;
						
			double tSizeTb = tSizeGb / 1024;				totalSizeTb = Math.round(tSizeTb * 10) / 10.0;	
			double fSizeTb = fSizeGb / 1024;				freeSizeTb = Math.round(fSizeTb * 10) / 10.0;
			usedSizeTb = tSizeTb - fSizeTb;					usedSizeTb = Math.round(usedSizeTb * 10) / 10.0;

			System.out.println("--> driveName="+driveName+", totalSize="+totalSizeTb+"TB, "+totalSizeGb+"GB, freeSize="+freeSizeTb+"TB, "+freeSizeGb+"GB, usedSize="+usedSizeTb+"TB, "+usedSizeGb+"GB, freeRate="+freeRate+"%, usedRate="+usedRate+"%");
			if ( dirHome.indexOf(driveName) != -1 ) {
				break;
			}
		}
    	
		EgovMap storageStatus = new EgovMap();
		storageStatus.put("totalSizeGb", String.valueOf(totalSizeGb));
		storageStatus.put("usedSizeGb", String.valueOf(usedSizeGb));
		storageStatus.put("freeSizeGb", String.valueOf(freeSizeGb));
		storageStatus.put("totalSizeTb", String.valueOf(totalSizeTb));
		storageStatus.put("usedSizeTb", String.valueOf(usedSizeTb));
		storageStatus.put("freeSizeTb", String.valueOf(freeSizeTb));
		storageStatus.put("freeRate", String.valueOf(freeRate));
		storageStatus.put("usedRate", String.valueOf(usedRate));
		return storageStatus;
    }

}
