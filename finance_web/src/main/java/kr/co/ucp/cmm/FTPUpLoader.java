/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : FTPUpLoader.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2016 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE          AUTHOR      DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2016. 7. 28.   seungJun    최초작성
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.cmm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class FTPUpLoader
{
	static Logger logger = LoggerFactory.getLogger(FTPUpLoader.class);

	private final int port;
	private final String ip;
	private final String id;
	private final String password;
	private final String targetDir;

	private FTPClient ftpClient;

	public FTPUpLoader(String ip, int port, String id, String password, String targetDir)
	{
		this.ip = ip;
		this.port = port;
		this.id = id;
		this.password = password;
		this.targetDir = targetDir;
	}

	public void connect()
	{
		logger.debug("[{}] port connecting....", port);
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ip, port);
			ftpClient.login(id, password);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
		} catch(FTPConnectionClosedException e) {
			logger.error("FTPConnectionClosedException connect ============>>  [{}]", e.getMessage());
		} catch(Exception e) {
			logger.error("Exception connect ============>>  [{}]", e.getMessage());
		}
	}

	public void disconnect()
	{
		try {
			ftpClient.logout();
			ftpClient.disconnect();
			logger.debug("disconnecting....");
		} catch (IOException e) {
			logger.error("IOException disconnect ============>>  [{}]", e.getMessage());
		}
	}

	public boolean upload(String fileName)
	{
		FileInputStream fis = null;
		boolean done = false;
		connect();
		try {
			ftpClient.changeWorkingDirectory(targetDir);
			File file = new File(fileName);
			fis = new FileInputStream(file);
			String utfFileName = new String(file.getName().getBytes("UTF-8"),"iso_8859_1");
			done = ftpClient.storeFile(utfFileName, fis);
			fis.close();
			if (done) {
				logger.debug("The file is uploaded successfully.");
			}
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException upload ============>>  [{}]", e.getMessage());
		} catch(FTPConnectionClosedException e) {
			logger.error("FTPConnectionClosedException upload ============>>  [{}]", e.getMessage());
		} catch (IOException e) {
			logger.error("IOException upload ============>>  [{}]", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception upload ============>>  [{}]", e.getMessage());
		} finally {
			disconnect();
		}
		return done;
	}

	public void upload(String fileName, String newFileName)
	{
		FileInputStream fis = null;
		connect();
		try {
			ftpClient.changeWorkingDirectory(targetDir);
			File file = new File(fileName);
			fis = new FileInputStream(file);
			String utfFileName = new String(newFileName.getBytes("UTF-8"),"iso_8859_1");
			boolean done = ftpClient.storeFile(utfFileName, fis);
			fis.close();
			if (done) {
				logger.debug("The file is uploaded successfully.");
			}
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException upload ============>>  [{}]", e.getMessage());
		} catch(FTPConnectionClosedException e) {
			logger.error("FTPConnectionClosedException upload ============>>  [{}]", e.getMessage());
		} catch (IOException e) {
			logger.error("IOException upload ============>>  [{}]", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception upload ============>>  [{}]", e.getMessage());
		} finally {
			disconnect();
		}
	}
}
