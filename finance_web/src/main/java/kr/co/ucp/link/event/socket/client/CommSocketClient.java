/**
* ----------------------------------------------------------------------------------------------
* @Class Name : CommSocketClient.java
* @Description : 소켓클라이언트
* @Version : 1.0
* Copyright (c) 2015 by KR.CO.UCP All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2015.01.08.   widecube Space  최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.link.event.socket.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommSocketClient {

	private String targetIp;
	private String sendYn = "Y";
	private int targetPort;
	private int connTimeout;
	private int respTimeout;
	private int returnLength;

	private ClassLoader cl;

	static Logger logger = LoggerFactory.getLogger(CommSocketClient.class);

	public CommSocketClient(){
		logger.info(" ==== cCommSocketClient Start==== >>>> ");

		cl = Thread.currentThread().getContextClassLoader();

		if(cl == null){
			cl = ClassLoader.getSystemClassLoader();
		}
	}

	/**
	 * 소켓기본정보
	 * @param dest_code
	 */
	private void setSocket(String destcode){
		//'220.73.136.26|5773|10000|10000|5|Y'
		try {
			String[] destCodeArr 	= destcode.split("\\|");
			this.targetIp 			= destCodeArr[0].trim();
			this.targetPort 		= Integer.parseInt(destCodeArr[1].trim());
			this.connTimeout 		= Integer.parseInt(destCodeArr[2].trim());
			this.respTimeout 		= Integer.parseInt(destCodeArr[3].trim());
			this.returnLength 		= Integer.parseInt(destCodeArr[4].trim());
			this.sendYn 			= destCodeArr[5].trim();

			logger.debug("targetIp :" + targetIp);
			logger.debug("targetPort :" + targetPort);
			logger.debug("connTimeout :" + connTimeout);
			logger.debug("respTimeout :" + respTimeout);
			logger.debug("returnLength :" + returnLength);
			logger.debug("sendYn :" + sendYn);
		} catch (PatternSyntaxException e) {
			logger.error("setSocket PatternSyntaxException : {}", e.getMessage());
		} catch (NumberFormatException e) {
			logger.error("setSocket NumberFormatException : {}", e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("setSocket ArrayIndexOutOfBoundsException : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("setSocket Exception : {}", e.getMessage());
		}

		/*
		URL url = cl.getResource("service.properties");

		//클래스 패스를 통해 info.property 있는 위치를 찾기
		File propFile = new File(url.getPath());
		FileInputStream is;

		try {
			is = new FileInputStream(propFile);
			Properties props = new Properties();
			props.load(is);

			this.targetIp 		= props.getProperty(dest_code+".target.ip").trim();
			this.targetPort 		= Integer.parseInt(props.getProperty(dest_code+".target.port").trim());
			this.connTimeout 	= Integer.parseInt(props.getProperty(dest_code+".target.connection.timeout").trim());
			this.respTimeout 	= Integer.parseInt(props.getProperty(dest_code+".target.resp.timeout").trim());
			this.returnLength 	= Integer.parseInt(props.getProperty(dest_code+".return.length").trim());
			this.sendYn 			= props.getProperty(dest_code+".send.yn").trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * 메세지 전송 A형
	 * @param msg
	 * @param dest_code
	 * @return
	 */
	public Map<String, Object> msgSend(String msg, String destCode, int type)  {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		String resp = "00000";
		String errMsg = "";

		setSocket(destCode); //소켓연결정보

		String respStr = "";
		if ("Y".equals(sendYn)) {
			Socket socketServer = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				logger.info(" ====0 {} msg send >>>> [{}] ",destCode, msg);
				byte[] body = msg.getBytes("UTF-8");


				socketServer = new Socket();
				socketServer.connect(new InetSocketAddress(targetIp, targetPort), connTimeout);		// 소켓 연결 타입아웃 시간 설정
				socketServer.setSoTimeout(respTimeout);

				bos = new BufferedOutputStream(socketServer.getOutputStream());

				bos.write(body);
				bos.flush();

				bis = new BufferedInputStream(socketServer.getInputStream());

				byte [] b = new byte[returnLength];
				int offset = 0;

				offset = bis.read(b, 0, b.length);
				respStr = new String(b, 0, offset, "UTF-8");

				logger.info(respStr);

				resp =  respStr;
			}catch(SocketException ce){
				errMsg = "SocketException";
				logger.error("Exception msgSend : {}",ce.getMessage());
				try { Thread.sleep(300); } catch (InterruptedException e) {
					logger.error("InterruptedException : {}",e.getMessage());
				}
				resp =  "11111";
			}catch(SocketTimeoutException ste) {
				errMsg = "SocketTimeoutException";
				logger.error("Exception msgSend : {}",ste.getMessage());
				resp =  "11111";
			}catch(Exception ex){
				errMsg = "Exception";
				logger.error("Exception msgSend : {}",ex.getMessage());
				resp =  "11111";
			}finally{
				try{
					if(is != null)  is.close();
					if(bos != null)	bos.close();
					if(bis != null)	bis.close();
					if(socketServer != null) socketServer.close();
				}catch(Exception e){
					logger.error("Exception msgSend : {}",e.getMessage());
				}
			}
		}
		if(type == 1) {
			//Exception이 나지 않았을 경우
			if(!resp.equals("11111")) {
				resp = respStr.substring(88,  89);
				mapRet.put("msg", respStr.substring(89,  189).trim());
			}
			else {
				resp = "1";
				mapRet.put("msg", errMsg);
			}
		}
		mapRet.put("code", resp);
		return mapRet;
	}
	
	/**
	 * 메세지 전송 B형
	 * @param msg
	 * @param dest_code
	 * @return
	 */
	public Map<String, Object> msgSendB(String msg, String destCode, int type)  {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		String resp = "00000";
		String errMsg = "";

		setSocket(destCode); //소켓연결정보

		String respStr = "";
		if ("Y".equals(sendYn)) {
			Socket socketServer = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				//logger.info(" ====0 {} msg send >>>> [{}] ",dest_code, msg);
				byte[] body = msg.getBytes("UTF-8");

				socketServer = new Socket();
				socketServer.connect(new InetSocketAddress(targetIp, targetPort), connTimeout);		// 소켓 연결 타입아웃 시간 설정
				socketServer.setSoTimeout(respTimeout);

				bos = new BufferedOutputStream(socketServer.getOutputStream());

				bos.write(body);
				bos.flush();

				bis = new BufferedInputStream(socketServer.getInputStream());

				byte [] b = new byte[returnLength];
				int offset = 0;
				offset = bis.read(b, 0, b.length);
				respStr = new String(b, 0, offset, "UTF-8");

				logger.info(respStr);
				resp =  respStr;
			}catch(SocketException ce){
				errMsg = "SocketException";
				logger.error("Exception msgSend : {}",ce.getMessage());
				try { Thread.sleep(300); } catch (InterruptedException e) {
					logger.error("InterruptedException : {}",e.getMessage());
				}
				resp =  "11111";
			}catch(SocketTimeoutException ste) {
				errMsg = "SocketTimeoutException";
				logger.error("Exception msgSend : {}",ste.getMessage());
				resp =  "11111";
			}catch(Exception ex){
				errMsg = "Exception";
				logger.error("Exception msgSend : {}",ex.getMessage());
				resp =  "11111";
			}finally{
				try{
					if(is != null)  is.close();
					if(bos != null)	bos.close();
					if(bis != null)	bis.close();
					if(socketServer != null) socketServer.close();
				}catch(Exception e){
					logger.error("Exception msgSend : {}",e.getMessage());
				}
			}
		}

		if(type == 1) {
			//Exception이 나지 않았을 경우
			if(!resp.equals("11111")) {
				//resp_ = respStr.substring(88,  89);	//A형
				resp = respStr.substring(0,  1);		//B형
				//map_ret.put("msg", respStr.substring(89,  189).trim());	//A형 처리결과메시지
				mapRet.put("msg", respStr.substring(1,  101).trim());	//B형 처리결과메시지
			}
			else {
				resp = "1";
				mapRet.put("msg", errMsg);
			}
		}
		mapRet.put("code", resp);
		return mapRet;
	}
	/**
	 * 메세지 전송
	 * @param msg
	 * @param dest_code
	 * @return
	 */
	public Map<String, Object> smsSend(String msg, String destCode)  {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		String resp = "00000";

		setSocket(destCode); //소켓연결정보

		logger.debug("SMS Send Packet :" + msg);

		String respStr = "";
		if ("Y".equals(sendYn)) {
			Socket socketServer = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				//logger.info(" ====0 {} msg send >>>> [{}] ",dest_code, msg);
				byte[] body = msg.getBytes("UTF-8");


				socketServer = new Socket();
				socketServer.connect(new InetSocketAddress(targetIp, targetPort), connTimeout);		// 소켓 연결 타입아웃 시간 설정
				socketServer.setSoTimeout(respTimeout);

				bos = new BufferedOutputStream(socketServer.getOutputStream());

				bos.write(body);
				bos.flush();

				bis = new BufferedInputStream(socketServer.getInputStream());

				byte [] b = new byte[returnLength];
				int offset = 0;

				offset = bis.read(b, 0, b.length);
				respStr = new String(b, 0, offset, "UTF-8");

				logger.info(respStr);

				resp =  respStr;
			}catch(SocketException ce){
				logger.error("Exception smsSend : {}",ce.getMessage());
				try { Thread.sleep(300); } catch (InterruptedException e) {
					logger.error("InterruptedException : {}",e.getMessage());
				}
				resp =  "11111";
			}catch(SocketTimeoutException ste) {
				logger.error("Exception smsSend : {}",ste.getMessage());
				resp =  "11111";
			}catch(Exception ex){
				logger.error("Exception smsSend : {}",ex.getMessage());
				resp =  "11111";
			}finally{
				try{
					if(is != null)  is.close();
					if(bos != null)	bos.close();
					if(bis != null)	bis.close();
					if(socketServer != null) socketServer.close();
				}catch(Exception e){
					logger.error("Exception smsSend : {}",e.getMessage());
				}
			}
		}
		mapRet.put("code", resp);
		return mapRet;
	}
}
