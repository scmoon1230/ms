package kr.co.ucp.cmm;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUpLoader {

	static Logger logger = LoggerFactory.getLogger(FileUpLoader.class);

	public static void uploadFile(InputStream stream, String fileName, String filePath) throws Exception {
		OutputStream bos = null;
		try {
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
			else {		   						filePath = filePath.replace("/", "\\");	}
			logger.info("--> uploadFile(): {}", filePath + File.separator + fileName);

			File dir = new File(filePath);
			if (!dir.isDirectory()) {	Files.createDirectories(Paths.get(filePath));	}

			bos = new FileOutputStream(filePath + File.separator + fileName);

			int bytesRead = 0;
			byte[] buffer = new byte[2048];

			while ((bytesRead = stream.read(buffer, 0, 2048)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {
			close(bos, stream);
		}
	}
	
	public static void close(Closeable  ... resources) {
		for (Closeable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception ignore) {
					logger.info("Occurred Exception to close resource is ingored!!");
				}
			}
		}
	}
	
}
