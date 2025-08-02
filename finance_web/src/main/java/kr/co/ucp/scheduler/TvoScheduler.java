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
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@Configuration
@EnableScheduling
public class TvoScheduler {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name="outService")
	private OutService outService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	// 마크애니 영상입수 결과파일 result.xml 을 읽어서 DB에 등록하고 결과파일은 이동한다.
	//@Scheduled(fixedDelay = 60000)			// 이전 종료시각으로부터 매 60초마다 동작
	public void responseMaOrgVdo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		//logger.info("--> responseMaOrgVdo(), G_SYS_ID => "+G_SYS_ID);
		
		if ("PVEWIDE".equalsIgnoreCase(G_SYS_ID)) {				// 광역일 때
			return;
		}
		
		
		
		logger.info("--> responseMaOrgVdo(), 영상이동 함수 시작 <--");

		String maRootPaths = outService.getMaOrderRootPaths();	// order.xml 경로는 복수개 가능
		String[] rootPaths = maRootPaths.split(",");
		
		for ( int i=0 ; i<rootPaths.length ; i++ ) {
			//String rootPath = rootPaths[i];
			//logger.info("--> responseMaOrgVdo(), rootPath => "+rootPath);
			String resultPath = rootPaths[i] + File.separator + "vms" + File.separator + "result";
			logger.info("--> responseMaOrgVdo(), resultPath => "+resultPath);

			File dir = new File(resultPath);
			String[] filenames = dir.list();
			
			if ( filenames != null ) {
				//	if ( filenames.length == 0 ) {	logger.info("--> responseMaOrgVdo(), result 파일 없음");	}
					
				for (String filename : filenames) {
					String resultXmlFullPath = resultPath + File.separator + filename;
					//logger.info("--> responseMaOrgVdo(), resultXmlFullPath => " + resultXmlFullPath);
					
					boolean isTempFile = false;
					File tempFile = new File(resultXmlFullPath);
					if (tempFile.exists()) {
						if (tempFile.isDirectory()) {
							//System.out.println("디렉토리가 존재합니다");
						} else if (tempFile.isFile()) {
							isTempFile = true;		//System.out.println("파일이 존재합니다");
						}
					} else {
						//System.out.println("파일이나 디렉토리가 존재하지 않습니다");
					}
					
					if ( isTempFile ) { // 파일일 때

						Map<String, Object> resultMap = new HashMap();
						List<Map<String, Object>> fileMapList = new ArrayList<Map<String, Object>>();
						//int fileSeq = -1;
					//	Document document = DocumentHelper.parseText(str);			// 1-1. 문자열 파싱 시
						Document document = new SAXReader().read(resultXmlFullPath);// 1-2. 파일 파싱 시
						Element rootElement =  document.getRootElement();			// 2. Root Element
						List<Element> branchElements = rootElement.elements();		// 3. Branch Element
						for(Element branchElement : branchElements) {
						//	String branchAttributeId = branchElement.attributeValue("id");
						//	System.out.println("branch attribute id : " + branchAttributeId);
							String name = branchElement.getName();
							String value = branchElement.getText().trim();
							if ( !"".equalsIgnoreCase(value) ) {
								// System.out.println("--- "+name + " : " + value);
								resultMap.put(name, value);
							}
							if ( "File".equalsIgnoreCase(name) ) {
								List<Element> childElements =  branchElement.elements();
								// 4. Children Element
								if ( childElements.size() != 0 ) {
									Map<String, Object> fileMap = new HashMap();
									for(Element childElement : childElements){
										String childName = childElement.getName();
										String childValue = childElement.getText();
										// System.out.println("----- "+childName + " : " + childValue);
										fileMap.put(childName, childValue);
									}
									fileMapList.add(fileMap);
								}
							}
						}
						resultMap.put("File", fileMapList);
						logger.info("--> responseMaOrgVdo(), resultMap => " + resultMap.toString());
						
						outService.actMaResultMap(rootPaths[i], rootPaths[0], resultMap);	// 마크애니 원본영상 요청결과 처리

						// result 파일을 log 폴더로 이동시킨다.
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						String yyyyMMddHHmmss = df.format(new Date());
						String tmp = filename.substring(0,filename.indexOf("."));
						String ext = filename.substring(filename.indexOf(".")+1);
					//	String logXmlPath = rootPath + File.separator + "vms" + File.separator + "log";
						String logXmlPath = rootPaths[0] + File.separator + "vms" + File.separator + "result" + File.separator + "complete";	// dataHome으로 복사한다.
						String logFileName = tmp+"_"+yyyyMMddHHmmss.substring(0,8)+"_"+yyyyMMddHHmmss.substring(8,14)+"."+ext;
						if ( !"1".equalsIgnoreCase(resultMap.get("Status").toString()) ) {	// 성공이 아닐 때 다른 폴더에 보관한다.
							logXmlPath = rootPaths[0] + File.separator + "vms" + File.separator + "result" + File.separator + "error";	// dataHome으로 복사한다.
							logFileName = tmp+"_"+yyyyMMddHHmmss.substring(0,8)+"_"+yyyyMMddHHmmss.substring(8,14)+"-error."+ext;
						}
					//	logger.info("--> responseMaOrgVdo(), logFileName => {}", logFileName);
						String logXmlFullPath = logXmlPath + File.separator + logFileName;
						logger.info("--> responseMaOrgVdo(), logXmlFullPath => "+logXmlFullPath);
						
						File resultXmlFile = new File(resultXmlFullPath);
						File logXmlFile = new File(logXmlFullPath);
						FileUtils.copyFile(resultXmlFile, logXmlFile);	// 파일복사
						resultXmlFile.delete();							// 파일삭제
						logger.info("--> responseMaOrgVdo() => " + filename +" 파일을 log 폴더로 이동하였음. <--");
					}
				}
					
			}
		}
		
	}
	
	
	// 암호화대기 상태의 파일을 DB에서 조회하여 작업한다.
	//@Scheduled(fixedDelay = 60000)			// 이전 종료시각으로부터 매 60초마다 동작
	public void requestMaEncOrgVdo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		if ("PVEWIDE".equalsIgnoreCase(G_SYS_ID)) {				// 광역일 때
			return;
		}
		
		
		
		
		logger.info("--> requestMaEncOrgVdo(), 영상암호화 함수 시작 <--");
		
		OutSrchVO vo = new OutSrchVO();
		vo.setOutFilePrgrsCd("70");				// 암호화대기
		vo.setRecordCountPerPage(9999);
		vo.setFirstRecordIndex(0);
		List<EgovMap> list = outService.selectOutFileList(vo);
		if ( list.size() != 0 ) {
			logger.debug("--> requestMaEncOrgVdo(), 암호화를 요청한다. <--");

			String outFilePlayCnt = prprtsService.getString("OUT_FILE_PLAY_CNT");	// 영상재생횟수
			if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
				int cnt = Integer.parseInt(outFilePlayCnt);
				if ( cnt < 1 ) {
					outFilePlayCnt = "-1";	// 재생횟수 무제한
				}
			} else {
				outFilePlayCnt = "-1";	// 재생횟수 무제한
			}
			
			for ( int i=0;i<list.size();i++) {
				EgovMap tvoOutFile = list.get(i);
				Map<String, String> params = new HashMap<String, String>();
				params.put("playCnt"   , outFilePlayCnt                          );
				params.put("dstrtCd"   , tvoOutFile.get("dstrtCd").toString()    );
				params.put("outRqstNo" , tvoOutFile.get("outRqstNo").toString()  );
				params.put("fileSeq"   , tvoOutFile.get("outFileSeq").toString() );
				params.put("filePath"  , tvoOutFile.get("outFilePath").toString());
				params.put("cptnFileNm", tvoOutFile.get("cptnFileNm").toString() );		// 자막파일명
				EgovMap outRqst = outService.selectOutRqstDtl(params);
				
				String objFileNm = tvoOutFile.get("outFileNm").toString();				// 대상파일명 : 원본 파일
				if ( "Y".equalsIgnoreCase(outRqst.get("maskingYn").toString()) ) {		// 마스킹할 때
					objFileNm = tvoOutFile.get("outFileNmMsk").toString();				// 대상파일명 : 마스킹된 파일
				}
				params.put("objFileNm", objFileNm);	// 대상파일명

				//String drmFileNm = objFileNm.replaceAll("_mask", "");
				//drmFileNm = drmFileNm.substring(0,objFileNm.lastIndexOf('.')+1)+"cctv";
				//String drmFileNm = list.get(i).get("cctvId").toString()+"_"+list.get(i).get("outVdoYmdhmsFr").toString()
				//		+"_"+list.get(i).get("outVdoYmdhmsTo").toString()+".cctv";
				String drmFileNm = tvoOutFile.get("outRqstNo").toString()+"_"+tvoOutFile.get("fcltLblNm").toString()
						+"_"+tvoOutFile.get("outVdoYmdhmsFr").toString().substring(0,12)
						+"_"+tvoOutFile.get("outVdoYmdhmsTo").toString().substring(0,12)+".cctv";
				params.put("drmFileNm", drmFileNm);	// 암호화파일명
				
				logger.info("--> requestMaEncOrgVdo(), params => {}", params.toString());
				params = outService.sendRequestEncrypt(params);		// 암호화 요청
				
			}
			logger.debug("--> requestMaEncOrgVdo(), 암호화를 요청을 완료한다. <--");
		}
		
	}

	
	
	// mp4 파일을 생성하고 DB에 기록한다.
	//@Scheduled(cron = "0 30 19 * * *")		// 매일 19시 30분에 동작
	public void createMp4Vdo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		if ("PVEWIDE".equalsIgnoreCase(G_SYS_ID)) {				// 광역일 때
			return;
		}
		
		
		
		logger.info("--> createMp4Vdo(), 영상변환 함수 시작 <--");

		List<EgovMap> list = outService.selectOutFileNeedMp4List();
		if ( list.size() != 0 ) {
			logger.debug("--> createMp4Vdo(), mp4 파일로 변환한다. <--");
			
			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
			
			logger.info("--> createMp4Vdo() rootPath => "+rootPath);

			for ( int i=0;i<list.size();i++) {
				list.get(i).put("rootPath", rootPath);
				outService.transOrgVdoFile(list.get(i));	// mp4 파일로 변환 후 입수완료 or 암호화대기 상태로 변경
				Thread.sleep(1000);
			}
			
			// 특정시각이 되면 작업을 중지한다.
			// todo...
			
			
			
			
			
		}
		
	}
	
	// 보관기간이 만료된 영상을 삭제하고 DB에 기록한다.
	//@Scheduled(cron = "0 30 7 * * *")		// 매일 7시 30분에 동작
	public void deleteExpiredVdo() throws Exception {

		String G_SYS_ID = prprtsService.getString("G_SYS_ID");
		if ("PVEWIDE".equalsIgnoreCase(G_SYS_ID)) {				// 광역일 때
			return;
			
		} else if ("PVEBASE".equalsIgnoreCase(G_SYS_ID)) {		// 기초일 때
			
			
		}
		
		logger.info("--> deleteExpiredVdo() 시작 <--");

		outService.deleteExpiredVdo();
		
	}

}
