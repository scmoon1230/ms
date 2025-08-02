package kr.co.ucp.cmns;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestUtilMA {
	private static Logger logger = LoggerFactory.getLogger(RestUtilMA.class);

	// tm 0이면  default
	public static Map<String, Object> postHttp(String url, Map<String, Object> map, int tm, String auth_no)  {
		String response_code = "000";
		String response_msg  = "success";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		logger.info("--> postHttp() url => {}",url);

		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig.Builder requestBuilder = RequestConfig.custom();
			if (tm > 0) {
				requestBuilder.setSocketTimeout(tm * 1000);
				requestBuilder.setConnectTimeout(tm * 1000);
				requestBuilder.setConnectionRequestTimeout(tm * 1000);
			}
			RequestConfig requestConfig = requestBuilder.build();
			httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setHeader("x-auth-no", auth_no);

			JSONObject json = new JSONObject(map);
		//	logger.info("--> postHttp() json.toString() => {}", json.toString());
		//	logger.info("--> postHttp() StringEntity(json.toString(),UTF-8) => {}", new StringEntity(json.toString(),"UTF-8"));
			
			httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));

			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpPost);

			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
//				logger.info("--> return body >>>> responseMap : {}", body);

//				gson integer double로 변환으로 	jackjson으로 변경
//				Gson gson = new Gson();
//				responseMap = gson.fromJson(body, map.getClass());

				ObjectMapper om = new ObjectMapper();
				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});
				logger.info("--> postHttp(), responseMap : {}", responseMap.toString());

				response_code = responseMap.get("result").toString();
				response_msg  = responseMap.get("description").toString();
			} else {
				response_code = response.getStatusLine().getStatusCode() + "";
				response_msg = response.getStatusLine().toString();
				logger.error("--> postHttp(), response is error : code:{}, message:{} ",response_code, response_msg);
			}
		} catch (Exception e) {
			logger.error("--> postHttp ERROR Exception >>>> {}", e.getMessage());
			response_msg 	= e.getMessage();
			response_code = "901";
			e.printStackTrace();
		}
		logger.info("--> return message >>>> code:{}, msg:{}", response_code, response_msg);
		responseMap.put("response_code", response_code);
		responseMap.put("response_msg", response_msg);
		return responseMap;
	}

	// tm 0이면  default
	public static Map<String, Object> postHttp(String url, Map<String, Object> map, int tm, Map<String, String> headMap)  {
		String response_msg  = "success";
		String response_code = "000";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		logger.info("--> postHttp() url => {}",url);

		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig.Builder requestBuilder = RequestConfig.custom();
			if (tm > 0) {
				requestBuilder.setSocketTimeout(tm * 1000);
				requestBuilder.setConnectTimeout(tm * 1000);
				requestBuilder.setConnectionRequestTimeout(tm * 1000);
			}
			RequestConfig requestConfig = requestBuilder.build();
			httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			for (Map.Entry<String, String> entry: headMap.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}

			JSONObject json = new JSONObject(map);
			httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));

			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpPost);

			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
//				logger.info("--> return body >>>> responseMap : {}", body);

//				gson integer double로 변환으로 	jackjson으로 변경
//				Gson gson = new Gson();
//				responseMap = gson.fromJson(body, map.getClass());

				ObjectMapper om = new ObjectMapper();
				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});

				if ( responseMap.get("message") != null ) response_msg  = responseMap.get("message").toString();
				if ( responseMap.get("code") != null ) response_code = responseMap.get("code").toString();
				
			} else {
				response_msg = response.getStatusLine().toString();
				response_code = response.getStatusLine().getStatusCode() + "";
				logger.error("response is error : code:{},message:{} ",response_code,response_msg);
			}
		} catch (Exception e) {
			logger.error("--> postHttp ERROR Exception >>>> {}", e.getMessage());
			response_msg 	= e.getMessage();
			response_code = "901";
		}
		logger.info("--> return message >>>> code:{} ,msg:{}", response_code, response_msg);
		responseMap.put("response_msg", response_msg);
		responseMap.put("response_code", response_code);
		return responseMap;
	}
	
	// tm 0이면  default
	public static Map<String, Object> getHttp(String url, int tm, String auth_no)  {
		String response_msg  = "success";
		String response_code = "000";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		logger.info("--> getHttp() url => {}",url);

		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig.Builder requestBuilder = RequestConfig.custom();
			if (tm > 0) {
				requestBuilder.setSocketTimeout(tm * 1000);
				requestBuilder.setConnectTimeout(tm * 1000);
				requestBuilder.setConnectionRequestTimeout(tm * 1000);
			}
			RequestConfig requestConfig = requestBuilder.build();
			httpGet.setConfig(requestConfig);

			httpGet.setHeader("x-auth-no", auth_no);
			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");

//			JSONObject json = new JSONObject(map);
//			httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));

			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpGet);

			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				ObjectMapper om = new ObjectMapper();
				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});

				if ( responseMap.get("message") != null ) response_msg = responseMap.get("message").toString();
				if ( responseMap.get("code") != null ) response_code = responseMap.get("code").toString();
			} else {
				response_msg = response.getStatusLine().toString();
				response_code = response.getStatusLine().getStatusCode() + "";
				logger.error("response is error : code:{},message:{} ",response_code,response_msg);
			}
		} catch (Exception e) {
			logger.error("--> postHttp ERROR Exception >>>> {}", e.getMessage());
			response_msg  = e.getMessage();
			response_code = "901";
		}
		logger.info("--> return message >>>> code:{} ,msg:{}", response_code, response_msg);
		responseMap.put("response_msg", response_msg);
		responseMap.put("response_code", response_code);
		return responseMap;
	}

	// tm 0이면  default
	public static Map<String, Object> getHttp(String url, int tm, Map<String, String> headMap)  {
		String response_msg  = "success";
		String response_code = "000";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		logger.info("--> getHttp() url => {}",url);

		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig.Builder requestBuilder = RequestConfig.custom();
			if (tm > 0) {
				requestBuilder.setSocketTimeout(tm * 1000);
				requestBuilder.setConnectTimeout(tm * 1000);
				requestBuilder.setConnectionRequestTimeout(tm * 1000);
			}
			RequestConfig requestConfig = requestBuilder.build();
			httpGet.setConfig(requestConfig);

			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
//			httpGet.setHeader("Accept-Charset", "utf-8");
//			httpGet.setHeader("x-user-id", URLEncoder.encode("한글", "UTF-8"));
//			httpGet.setHeader("x-pwd", URLEncoder.encode("한글", "UTF-8"));

//			for (Map.Entry<String, String> entry: headMap.entrySet()) {
//				httpGet.setHeader(entry.getKey(), entry.getValue());
//			}
		    for(String entry : headMap.keySet())
		    	httpGet.setHeader(entry, headMap.get(entry));

//			JSONObject json = new JSONObject(map);
//			httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));

			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpGet);

			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				ObjectMapper om = new ObjectMapper();
				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});

				response_msg = responseMap.get("message").toString();
				response_code = responseMap.get("code").toString();
			} else {
				response_msg = response.getStatusLine().toString();
				response_code = response.getStatusLine().getStatusCode() + "";
				logger.error("response is error : code:{},message:{} ",response_code,response_msg);
			}
		} catch (Exception e) {
			logger.error("--> postHttp ERROR Exception >>>> {}", e.getMessage());
			response_msg  = e.getMessage();
			response_code = "901";
		}
		logger.info("--> return message >>>> code:{} ,msg:{}", response_code, response_msg);
		responseMap.put("response_msg", response_msg);
		responseMap.put("response_code", response_code);
		return responseMap;
	}


}
