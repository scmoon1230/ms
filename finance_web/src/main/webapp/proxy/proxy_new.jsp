<%@page import="java.nio.charset.StandardCharsets" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@page import="java.net.*,java.io.*,java.util.*, kr.co.ucp.egov.com.cmm.util.EgovResourceCloseHelper" %>
<%
	HttpURLConnection connection = null;
	InputStream istream = null;
	OutputStream ostream = null;
	InputStream ristream = null;
	OutputStream rostream = null;

	try {
		StringBuilder url = new StringBuilder(request.getParameter("targetUrl"));
		System.out.println("==== targetUrl >>>> " + url.toString());
		if (url.length() > 0) {
			Enumeration<String> enumeration = request.getParameterNames();
			int i = 0;
			if (url.toString().contains("?")) i++;
			while (enumeration.hasMoreElements()) {
				String parameterNames = enumeration.nextElement();
				if (!"targetUrl".equals(parameterNames)) {
					String parameter = URLEncoder.encode(request.getParameter(parameterNames), StandardCharsets.UTF_8);
					System.out.println("==== parameter >>>> " + parameterNames + " = " +  parameter);
					if (i == 0) {
						url.append("?");
					} else {
						url.append("&");
					}
					url.append(parameterNames).append("=").append(parameter);
					i++;
				}
			}

			System.out.println("==== url       >>>> " + url.toString() );
			URL targetUrl = new URL(url.toString());
			connection = (HttpURLConnection) targetUrl.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(request.getMethod());

			int cLength = request.getContentLength();
			if (cLength > 0) {
				connection.setDoInput(true);
				istream = request.getInputStream();
				ostream = connection.getOutputStream();
				final int length = 5000;
				byte[] bytes = new byte[length];
				int bytesRead = 0;
				while ((bytesRead = istream.read(bytes, 0, length)) > 0) {
					ostream.write(bytes, 0, bytesRead);
				}
			}
			// what's this for
			out.clear();
			out = pageContext.pushBody();
			rostream = response.getOutputStream();
			response.setContentType(connection.getContentType());
			ristream = connection.getInputStream();
			final int length = 5000;
			byte[] bytes = new byte[length];
			int bytesRead;
			while ((bytesRead = ristream.read(bytes, 0, length)) > 0) {
				rostream.write(bytes, 0, bytesRead);
			}
		} else {
			return;
		}
	} catch (FileNotFoundException e) {
		try {
			String contextPath = request.getContextPath();
			if (!"".equals(contextPath)) {
				contextPath = "/" + contextPath;
			}

			String url = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + contextPath + "/images/mntr/gis/transparentImage.png";
			URL targetUrl = new URL(url);
			connection = (HttpURLConnection) targetUrl.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(request.getMethod());

			int clength = request.getContentLength();
			if (clength > 0) {
				connection.setDoInput(true);
				istream = request.getInputStream();
				ostream = connection.getOutputStream();
				final int length = 5000;
				byte[] bytes = new byte[length];
				int bytesRead = 0;
				while ((bytesRead = istream.read(bytes, 0, length)) > 0) {
					ostream.write(bytes, 0, bytesRead);
				}
			}
			out.clear();
			out = pageContext.pushBody();
			rostream = response.getOutputStream();
			response.setContentType(connection.getContentType());
			ristream = connection.getInputStream();
			final int length = 5000;
			byte[] bytes = new byte[length];
			int bytesRead = 0;
			while ((bytesRead = ristream.read(bytes, 0, length)) > 0) {
				rostream.write(bytes, 0, bytesRead);
			}
		} catch (Exception ex) {
			response.setStatus(404);
		}
	} catch (Exception e) {
		response.setStatus(404);
	} finally {
		EgovResourceCloseHelper.close(istream, ostream, ristream, rostream);
	}
%>
