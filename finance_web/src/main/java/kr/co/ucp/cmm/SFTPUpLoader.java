/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : SFTPUpLoader.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUpLoader
{
	static Logger logger = LoggerFactory.getLogger(SFTPUpLoader.class);
	
	private final int port;
	private final String ip;
	private final String id;
	private final String password;
	private final String targetDir;
	
	private JSch jsch;
	private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;

	public SFTPUpLoader(String ip, int port, String id, String password, String targetDir)
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
			jsch = new JSch();
			session = jsch.getSession(id, ip, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp)channel;
		} catch (JSchException e) {
			logger.error("JSchException connect ============>>  [{}]", e.getMessage());
		}
	}

	public void disconnect()
	{
		try {
			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();
			logger.debug("disconnecting....");
		} catch (Exception e) {
			logger.error("Exception disconnect ============>>  [{}]", e.getMessage());
		}
		
	}

	public boolean upload(String fileName)
	{
		FileInputStream fis = null;
		boolean done = false;
		connect();
		try {
			sftpChannel.cd(targetDir);

			File file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());
			done = true;
			fis.close();
		} catch (FileNotFoundException e) {
			done = false;
			logger.error("FileNotFoundException upload ============>>  [{}]", e.getMessage());
		} catch (IOException e) {
			done = false;
			logger.error("IOException upload ======================>>  [{}]", e.getMessage());
		} catch (SftpException e) {
			done = false;
			logger.error("SftpException upload ====================>>  [{}]", e.getMessage());
		} catch (Exception e) {
			done = false;
			logger.error("Exception upload ========================>>  [{}]", e.getMessage());
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
			sftpChannel.cd(targetDir);

			File file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, newFileName);

			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException upload ============>>  [{}]", e.getMessage());
		} catch (IOException e) {
			logger.error("IOException upload ======================>>  [{}]", e.getMessage());
		} catch (SftpException e) {
			logger.error("SftpException upload ====================>>  [{}]", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception upload ========================>>  [{}]", e.getMessage());
		} finally {
			disconnect();
		}
	}
}
