package kr.co.ucp.tvo.cmn.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Component("tvoUtil")
public class TvoUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String getBase64Content(String fileFullPath) {
		String strEnc = "";
		try {
			String strContent = Files.readString(Paths.get(fileFullPath));
			System.out.println("##### strContent => "+strContent);
			strEnc = new String(Base64.encodeBase64(strContent.getBytes()));
			System.out.println("##### strEnc => "+strEnc);
		} catch (IOException e) {	e.printStackTrace();	}
		return strEnc;
	}

	// 현재 시각이 인자로 받은 시간 이내인지 판별한다.
	public boolean checkProdTime(String st, String en) {
		boolean rtn = false;

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		int now = Integer.parseInt(yyyyMMddHHmmss.substring(8,10));
		int start = Integer.parseInt(st);
		int end = Integer.parseInt(en);
		
		if ( start < end ) {
			if ( start<=now && now<end ) {
				rtn = true;
			}
		} else if ( start > end ) {
			if ( start>now && now>=end ) {
			} else {
				rtn = true;
			}
		}
		return rtn;
	}

	// 마크애니용 order.xml 파일 내용을 생성한다.
	public String getOrderXmlText(EgovMap outFileMap) {
		logger.info("--> getOrderXmlText(), outFileMap => {}", outFileMap.toString());

		String fcltSysCd = outFileMap.get("fcltSysCd").toString();		// VMS구분
		
		String outRqstNo = outFileMap.get("outRqstNo").toString();		// 반출신청번호
		String outFileSeq = outFileMap.get("outFileSeq").toString();	// 반출파일연번
		String reqId = outRqstNo+"-"+outFileSeq;
		String vmsUid = outFileMap.get("linkVmsUid").toString();		// VMS CCTV아이디
		String ymdhmsFr = outFileMap.get("outVdoYmdhmsFr").toString();	// 영상시작일시
		String ymdhmsTo = outFileMap.get("outVdoYmdhmsTo").toString();	// 영상종료일시
		String fileRootPath = "/contents";
		String fileSubPath = outFileMap.get("outFilePath").toString();
		fileSubPath = fileSubPath+"/_temp"+outFileMap.get("tempFolderIndex").toString();	// 임시폴더에 담아야 한다.
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if ( "MTV".equalsIgnoreCase(fcltSysCd) ) {	// 마일스톤일 때

			String reqFormat = "MKV";	// 암호화만 할 때
			String maskingYn = outFileMap.get("maskingYn").toString();
			if ( "Y".contentEquals(maskingYn)) {
				reqFormat = "AVI";	// 암호화, 마스킹 할 때
			}
			
			String custom = "reqFormat="+reqFormat;
			
			stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			.append("<OrderInfo>")
			.append("<Priority>0</Priority>")
			.append("<Custom>"+custom+"</Custom>")
			.append("<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
			.append("<RootPath>"+fileRootPath+"</RootPath>")
			.append("<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
		//	.append("<VMSList>")
		//		.append("<VMS Id=\"gurobanchul\" Pwd=\"gr@2021\">http://172.25.15.40</VMS>")
		//	.append("</VMSList>")
			.append("<ExportList>")
				.append("<Export>")
					.append("<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
					.append("<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
					.append("<CameraList>")
						.append("<Camera>"+vmsUid+"</Camera>")	// VMS CCTV아이디
					.append("</CameraList>")
				.append("</Export>")
			.append("</ExportList>")
			.append("</OrderInfo>");
			
		} else {	// 마일스톤이 아닐 때
			String custom = "seq="+outFileSeq+";type=avi";
			
			stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			.append("<OrderInfo>")
			.append("<CameraCode>"+vmsUid+"</CameraCode>")	// VMS CCTV아이디
			.append("<StartDate>"+ymdhmsFr+"</StartDate>")	// 영상시작일시
			.append("<EndDate>"+ymdhmsTo+"</EndDate>")		// 영상종료일시
			.append("<Priority>0</Priority>")
			.append("<Custom>"+custom+"</Custom>")
			.append("<ReqId>"+reqId+"</ReqId>")				// 반출신청번호
			.append("<RootPath>"+fileRootPath+"</RootPath>")
			.append("<OrgFilePath>"+"/"+fileSubPath+"/"+"</OrgFilePath>")	// 영상파일위치
			.append("</OrderInfo>");
		}
		
		return stringBuilder.toString();
	}
	
	
	
	
	
}
