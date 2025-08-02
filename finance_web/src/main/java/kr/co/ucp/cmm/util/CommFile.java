/**
 *----------------------------------------------------------------------------------
 * @File Name	: CommUtil.java
 * @Description	: 스트링 유틸
 * @author		: seungJun
 * @since		: 2013. 7. 16.
 * @version		: 1.0
 *----------------------------------------------------------------------------------
 * @Copyright (c)2013 남양주, WideCUBE, All Rights Reserved.
 *----------------------------------------------------------------------------------
 * 수정일			수정자			수정내용
 *----------------------------------------------------------------------------------
 * 2013. 7. 16.		seungJun		최초작성
 *----------------------------------------------------------------------------------
 */
package kr.co.ucp.cmm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommFile {
	static Logger logger = LoggerFactory.getLogger(CommFile.class);
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static void fileCopy(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;

			while((data=fis.read())!=-1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			logger.error("IOException:{}", e.getMessage());
		}
	}
	public static void fileMove(String inFileName, String outFileName){
		byte[] buf = new byte[1024];
		FileInputStream fin = null;
		FileOutputStream fout = null;

		File file = new File(inFileName);
		if(!file.renameTo(new File(outFileName))){    // renameTo로 이동 실패할 경우

			buf = new byte[1024];
			try {
				fin = new FileInputStream(inFileName);
				fout = new FileOutputStream(outFileName);
				int read = 0;
				while((read=fin.read(buf,0,buf.length))!=-1){
					fout.write(buf, 0, read);
				}

				fin.close();
				fout.close();
				file.delete();                        // 복사 후 파일 삭제
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("IOException:{}", e.getMessage());
			}
		}
	}

	public boolean nioFileCopy(String inFileName, String outFileName) {
		Path source = Paths.get(inFileName);
		Path target = Paths.get(outFileName);

		// 사전체크
		if (source == null) {
			throw new IllegalArgumentException("source must be specified");
		}
		if (target == null) {
			throw new IllegalArgumentException("target must be specified");
		}

		// 소스파일이 실제로 존재하는지 체크
		if (!Files.exists(source, new LinkOption[] {})) {
			throw new IllegalArgumentException("Source file doesn't exist: " + source.toString());
		}

		/* // 소스경로와 복사후 경로가 일치하면 리턴 (일단 주석처리)
		if (source == target) {
			return;
		}
		*/

		try {
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);   // 파일복사

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException:{}", e.getMessage());
			return false;
		}

		if(Files.exists(target, new LinkOption[]{})){        // 파일이 정상적으로 생성이 되었다면
			// System.out.println("File Copied");
			return true;                                     // true 리턴
		} else {
			logger.error("{}", "File Copy Failed");
			return false;                                    // 실패시 false
		}
	}

	public static boolean nioFileMove(String inFileName, String outFileName) {
		Path source = Paths.get(inFileName);
		Path target = Paths.get(outFileName);

		if (source == null) {
			throw new IllegalArgumentException("source must be specified");
		}
		if (target == null) {
			throw new IllegalArgumentException("target must be specified");
		}

		if (!Files.exists(source, new LinkOption[] {})) {
			throw new IllegalArgumentException("Source file doesn't exist: " + source.toString());
		}

		// 파일속성이 jpeg 가 아인경우 jpeg로 변경
	    String mimeType = "";
		try {
			mimeType = Files.probeContentType(source);
			logger.debug("--> filename >>>> {},{}", inFileName, mimeType);
			// jpeg가 아닌경우 jpeg형태로 변환
			if (!"image/jpeg".equals(mimeType)) {
				File frFile = new File(inFileName);
				BufferedImage frImage = ImageIO.read(frFile);

				File toFile = new File(outFileName);
				ImageIO.write(frImage, "jpg", toFile);
				Files.delete(source);
				return true;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//	e1.printStackTrace();
		}


		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);    // 파일 이동
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException:{}", e.getMessage());
			return false;
		}

		if(Files.exists(target, new LinkOption[]{})){
			// System.out.println("File Moved");
			return true;
		} else {
			logger.error("{}", "File Move Failed");
			return false;
		}
	}

	public static void fileDelete(String file_path) {
		if(null!=file_path){
			File del_File = new File(file_path);
			if(del_File.exists()){
				// System.out.println(del_File.getName() + " 파일을 삭제합니다");
				del_File.delete();
			}else{
				logger.error("{}", del_File.getName() + " 파일이 존재하지 않습니다.");
			}
		}
	}

//		deleteDirectory(new File(delete_path));    // 삭제할 디렉토리 경로
	public static boolean deleteDirectory(File path) {

		if(!path.exists()) {                 // 경로 존재 여부
			return false;                     // 없으면 false 리턴
		}

		File[] files = path.listFiles();     // 경로 내의 파일 리스트
		for (File file : files) {
			if (file.isDirectory()) {         // 파일 정보가  디렉토리 라면
				deleteDirectory(file);        // 재귀? 다시 이 메서드를 불러서 삭제작업
			} else {
				file.delete();                // 디렉토리가 아니라면 파일삭제
			}
		}

		return path.delete();                 // 디렉토리삭제. 삭제되면 true
	}

	public static void fileChmod(String file_path) {
		if(null!=file_path){
			File targetFile  = new File(file_path);
			if(targetFile.exists())  {
			    targetFile.setReadable(true, false);
			    targetFile.setWritable(true, false);
			    targetFile.setExecutable(true, false);
			    if (isWindows()) {
				    targetFile.setReadable(true, false);
				    targetFile.setWritable(true, false);
				    targetFile.setExecutable(true, false);
			    } else {
				    targetFile.setReadable(true, false);
				    targetFile.setWritable(true, false);
				    targetFile.setExecutable(true, false);
			    }
	        }
//		    if (!isWindows()) {
//				try {
//			        String cmd = "chmod 777 -R " + file_path;
//			        Runtime rt = Runtime.getRuntime();
//			        Process prc;
//					prc = rt.exec(cmd);
//					prc.waitFor();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					//e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					// e.printStackTrace();
//				}
//		    }
		}
	}
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
}
