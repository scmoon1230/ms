package kr.co.ucp.cmns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TvoUtil {

	private final static Logger logger = LoggerFactory.getLogger(TvoUtil.class);

	public static String getBase64Content(String fileFullPath) {
		String strEnc = "";
		try {
			String strContent = Files.readString(Paths.get(fileFullPath));
			//System.out.println("##### strContent => "+strContent);
			strEnc = new String(Base64.encodeBase64(strContent.getBytes()));
			//System.out.println("##### strEnc => "+strEnc);
		} catch (IOException e) {	e.printStackTrace();	}
		return strEnc;
	}

	// 마크애니용 order.xml 파일 내용을 생성한다.
	public static String getOrderXmlText(HashMap outFileMap) {
		String xmlText = "";
		
		try {
			logger.info("--> getOrderXmlText(), outFileMap => {}", outFileMap.toString());
	
			String fcltSysCd = outFileMap.get("fcltSysCd").toString();		// VMS구분
			
			String outRqstNo  = outFileMap.get("outRqstNo").toString();		// 반출신청번호
			//String outFileSeq = outFileMap.get("outFileSeq").toString();	// 반출파일연번
			String outFileSeq = String.valueOf(outFileMap.get("outFileSeq"));	// 반출파일연번
			String reqId      = outRqstNo+"-"+outFileSeq;
			String fcltUid    = outFileMap.get("fcltUid").toString();		// VMS CCTV아이디
			String vmsUid     = outFileMap.get("linkVmsUid").toString();		// VMS CCTV아이디
			String ymdhmsFr   = outFileMap.get("outVdoYmdhmsFr").toString();	// 영상시작일시
			String ymdhmsTo   = outFileMap.get("outVdoYmdhmsTo").toString();	// 영상종료일시
			String fileRootPath = "/contents";
			String fileSubPath = outFileMap.get("outFilePath").toString();
			fileSubPath = fileSubPath+"/_temp"+outFileMap.get("tempfolderindex").toString();	// 임시폴더에 담아야 한다.
			
			StringBuilder stringBuilder = new StringBuilder();
			
			if ( "MTV".equalsIgnoreCase(fcltSysCd) ) {	// 마일스톤일 때
	
				String reqFormat = "MKV";	// 암호화만 할 때
				String maskingYn = outFileMap.get("maskingYn").toString();
				if ( "Y".contentEquals(maskingYn)) {
					reqFormat = "AVI";	// 암호화, 마스킹 할 때
				}
				
				String custom = "reqFormat="+reqFormat;
				
				stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("\n<OrderInfo>")
				.append("\n\t<Priority>0</Priority>")
				.append("\n\t<Custom>"+custom+"</Custom>")
				.append("\n\t<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
				.append("\n\t<RootPath>"+fileRootPath+"</RootPath>")
				.append("\n\t<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
				.append("\n\t<ExportList>")
				.append("\n\t\t<Export>")
				.append("\n\t\t\t<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
				.append("\n\t\t\t<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
				.append("\n\t\t\t<CameraList>")
				.append("\n\t\t\t\t<Camera>"+vmsUid+"</Camera>")	// VMS CCTV아이디
				.append("\n\t\t\t</CameraList>")
				.append("\n\t\t</Export>")
				.append("\n\t</ExportList>")
				.append("\n</OrderInfo>");
				
			} else if ( "VRX".equalsIgnoreCase(fcltSysCd) ) {	// 뷰릭스일 때

				stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("\n<OrderInfo>")
				.append("\n\t<CameraCode>"+fcltUid+"</CameraCode>")	// VMS CCTV아이디
				.append("\n\t<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
				.append("\n\t<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
				.append("\n\t<Custom></Custom>")
				.append("\n\t<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
				.append("\n\t<RootPath>"+fileRootPath+"</RootPath>")
				.append("\n\t<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
				.append("\n</OrderInfo>");

			} else if ( "DNV".equalsIgnoreCase(fcltSysCd) ) {	// 다누시스일 때

				stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("\n<OrderInfo>")
				.append("\n\t<CameraCode>"+fcltUid+"</CameraCode>")	// VMS CCTV아이디
				.append("\n\t<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
				.append("\n\t<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
				.append("\n\t<Custom></Custom>")
				.append("\n\t<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
				.append("\n\t<RootPath>"+fileRootPath+"</RootPath>")
				.append("\n\t<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
				.append("\n</OrderInfo>");

			} else {	// 기타
				String custom = "seq="+outFileSeq+";type=avi";
				
				stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("\n<OrderInfo>")
				.append("\n\t<CameraCode>"+fcltUid+"</CameraCode>")	// VMS CCTV아이디
				.append("\n\t<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
				.append("\n\t<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
				.append("\n\t<Priority>0</Priority>")
				.append("\n\t<Custom>"+custom+"</Custom>")
				.append("\n\t<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
				.append("\n\t<RootPath>"+fileRootPath+"</RootPath>")
				.append("\n\t<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
				.append("\n</OrderInfo>");
			}
			
			xmlText = stringBuilder.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--> getOrderXmlText() ERROR Exception >>>> {}", e.toString()+" #####");
		}
		
		return xmlText;
	}
	


    public static String selectRootPath(String flag, String[] rootPaths, String[] options) {
		logger.error("--> selectRootPath() flag:{}, rootPaths:{}, options:{}", flag, rootPaths.toString(), options.toString());

    	String rootPath = "";

		if ("WEIGHT".equalsIgnoreCase(flag)) {		// 경로 가중치에 따라 order파일 위치를 선택

	        TreeMap<Integer, String> map = new TreeMap<>();
	        int cumulativeWeight = 0;

	        for (int i = 0; i < rootPaths.length; i++) {
	            cumulativeWeight += Integer.parseInt(options[i]);
	            map.put(cumulativeWeight, rootPaths[i]);
	        }

	        Random random = new Random();
	        int randomNum = random.nextInt(cumulativeWeight) + 1;

	    	rootPath = map.ceilingEntry(randomNum).getValue();
	    	
	    	
		} else if ("CROWD".equalsIgnoreCase(flag)) {	// 입수작업중인 건수를 비교하여 order파일 위치를 선택
			/*	vo.setOutFilePrgrsCd("25");		// 입수중
				vo.setRecordCountPerPage(9999);
				vo.setFirstRecordIndex(0);
				List<EgovMap> ingFileList = pveMapper.selectOutFileList(vo);
				String ingFilePath = "";
				for ( int j=0 ; j<ingFileList.size() ; j++ ) {
					EgovMap ingFileMap = ingFileList.get(j);
					ingFilePath += ","+ingFileMap.get("orderFilePath").toString()+":"+ingFileMap.get("outVdoDuration").toString();
				}
				if ( !"".equalsIgnoreCase(ingFilePath)) ingFilePath = ingFilePath.substring(1);
				logger.info("--> makeOrderXmlFile() [{}], ingFilePath => {}", i+1, ingFilePath);	*/
				
	    	
		} else {
			
			
		}

		logger.error("--> selectRootPath() rootPath:{}", rootPath);
        return rootPath;
    }
	
	

	
	
	public static String getPrprtsValue(List<HashMap> egovMapList, String key) {
		String val = "";
		Iterator<HashMap> iterator = egovMapList.iterator();
		while (iterator.hasNext()) {
			HashMap egovMap = (HashMap) iterator.next();
			if ( key.equalsIgnoreCase((String)egovMap.get("prprtsKey"))) {
				val = (String)egovMap.get("prprtsVal");
			}
		}
		return val;
	}

	public static String getAddedPrprtsValue(List<HashMap> egovMapList, String dv) {
		String val = "";

		Iterator<HashMap> iterator = egovMapList.iterator();
		while (iterator.hasNext()) {
			HashMap egovMap = (HashMap) iterator.next();
			val = val + dv + (String)egovMap.get("prprtsVal");
		}
		if ( !"".equalsIgnoreCase(val) ) val = val.substring(dv.length());
		
		return val;
	}
		
	
	
	
}
