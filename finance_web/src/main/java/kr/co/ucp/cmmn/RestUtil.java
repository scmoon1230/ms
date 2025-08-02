/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : CommUtil.java
 * @Description : 공통유틸리티
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015.01.08.   widecube Space  최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.cmmn;

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
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestUtil {
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
    
    private static String auth_key;
//    private static GblVal gVal = GblVal.getInstance();

    // tm 0이면  default
    public static Map<String, Object> postHttp(String pUrl, Map<String, Object> map, int tm, Map<String, String> headMap) {
        String response_msg = "";
        String response_cd = "000";
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String linkUrl = pUrl;
        if (!linkUrl.toLowerCase().startsWith("http")) {
            linkUrl = "http://" + linkUrl;
        }
        logger.debug("==== postHttp >>>> url:{}", linkUrl);

        try {
            HttpPost httpPost = new HttpPost(linkUrl);
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            if (tm > 0) {
                requestBuilder.setSocketTimeout(tm * 1000);
                requestBuilder.setConnectTimeout(tm * 1000);
                requestBuilder.setConnectionRequestTimeout(tm * 1000);
            }
            RequestConfig requestConfig = requestBuilder.build();
            httpPost.setConfig(requestConfig);

            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
//			httpPost.setHeader("x-auth-key", auth_key);

            JSONObject json = new JSONObject(map);
            httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost);

            // Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);

                ObjectMapper om = new ObjectMapper();
                responseMap = om.readValue(body, new TypeReference<Map<String, Object>>() {
                });
            } else {
                response_msg = response.getStatusLine().toString();
                response_cd = response.getStatusLine().getStatusCode() + "";
                logger.error("response is error : code:{},message:{} ", response_cd, response_msg);
            }
        } catch (Exception e) {
            logger.error("==== postHttp ERROR Exception >>>> {},{}", linkUrl, e.getMessage());
            response_msg = e.getMessage();
            response_cd = "901";
//            e.printStackTrace();
        }

        logger.debug("==== return message >>>> code:{},msg:{},url:{}", response_cd, response_msg, linkUrl);
        if (response_cd.length() != 3) {
            if (response_cd.length() > 3) {
                response_cd = response_cd.substring(0, 3);
            } else {
                response_cd = "909";
            }
        }

        if (!"000".equals(response_cd)) {
            responseMap.put("responseMsg", response_msg);
            responseMap.put("responseCd", response_cd);
        }
        return responseMap;
    }

    public static Map<String, Object> postHttp2(String pUrl, Map<String, Object> map, int tm, String auth_Key) {
        String response_msg = "success";
        String response_code = "000";
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String linkUrl = pUrl;
        if (!linkUrl.toLowerCase().startsWith("http")) {
            linkUrl = "http://" + linkUrl;
        }
        logger.debug("==== postHttp >>>> url:{}", linkUrl);

        try {
            HttpPost httpPost = new HttpPost(linkUrl);
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            if (tm > 0) {
                requestBuilder.setSocketTimeout(tm * 1000);
                requestBuilder.setConnectTimeout(tm * 1000);
                requestBuilder.setConnectionRequestTimeout(tm * 1000);
            }
            RequestConfig requestConfig = requestBuilder.build();
            httpPost.setConfig(requestConfig);

            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("x-auth-key", auth_Key);

            JSONObject json = new JSONObject(map);
            httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost);

            // Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
//				logger.debug("==== return body >>>> responseMap:{}", body);

//				gson integer double로 자동변환으로 	jackjson으로 변경 20200301
//				Gson gson = new Gson();
//				responseMap = gson.fromJson(body, map.getClass());

                ObjectMapper om = new ObjectMapper();
                responseMap = om.readValue(body, new TypeReference<Map<String, Object>>() {
                });

                response_msg = responseMap.get("message").toString();
                response_code = responseMap.get("code").toString();
            } else {
                response_msg = response.getStatusLine().toString();
                response_code = response.getStatusLine().getStatusCode() + "";
                logger.error("response is error : code:{},message:{} ", response_code, response_msg);
            }
        } catch (Exception e) {
            logger.error("==== postHttp ERROR Exception >>>> {},{}", linkUrl, e.getMessage());
            response_msg = e.getMessage();
            response_code = "901";
        }

        logger.debug("==== return message >>>> code:{},msg:{},url:{}", response_code, response_msg, linkUrl);
        if (response_code.length() != 3) {
            if (response_code.length() > 3) {
                response_code = response_code.substring(0, 3);
            } else {
                response_code = "909";
            }
        }

        responseMap.put("response_msg", response_msg);
        responseMap.put("response_code", response_code);
        return responseMap;
    }

    public static void postHttpNoReturn(String pUrl, Map<String, Object> map, int tm, String auth_Key) {
        String response_msg = "success";
        String response_code = "000";
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String linkUrl = pUrl;
        if (!linkUrl.toLowerCase().startsWith("http")) {
            linkUrl = "http://" + linkUrl;
        }
        logger.debug("==== postHttp >>>> url:{}", linkUrl);

        try {
            HttpPost httpPost = new HttpPost(linkUrl);
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            if (tm > 0) {
                requestBuilder.setSocketTimeout(tm * 1000);
                requestBuilder.setConnectTimeout(tm * 1000);
                requestBuilder.setConnectionRequestTimeout(tm * 1000);
            }
            RequestConfig requestConfig = requestBuilder.build();
            httpPost.setConfig(requestConfig);

            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("x-auth-key", auth_Key);

            JSONObject json = new JSONObject(map);
            httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

            CloseableHttpClient client = HttpClients.createDefault();
            client.execute(httpPost);

//			// Response 출력
//			if (response.getStatusLine().getStatusCode() == 200) {
//				ResponseHandler<String> handler = new BasicResponseHandler();
//				String body = handler.handleResponse(response);
////				logger.debug("==== return body >>>> responseMap:{}", body);
//
////				gson integer double로 자동변환으로 	jackjson으로 변경 20200301
////				Gson gson = new Gson();
////				responseMap = gson.fromJson(body, map.getClass());
//
//				ObjectMapper om = new ObjectMapper();
//				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});
//
//				response_msg  = responseMap.get("message").toString();
//				response_code = responseMap.get("code").toString();
//			} else {
//				response_msg = response.getStatusLine().toString();
//				response_code = response.getStatusLine().getStatusCode() + "";
//				logger.error("response is error : code:{},message:{} ",response_code,response_msg);
//			}
        } catch (Exception e) {
            logger.error("==== postHttp ERROR Exception >>>> {},{}", linkUrl, e.getMessage());
            response_msg = e.getMessage();
            response_code = "901";
        }

        logger.debug("==== return message >>>> code:{},msg:{},url:{}", response_code, response_msg, linkUrl);
        if (response_code.length() != 3) {
            if (response_code.length() > 3) {
                response_code = response_code.substring(0, 3);
            } else {
                response_code = "909";
            }
        }

        responseMap.put("response_msg", response_msg);
        responseMap.put("response_code", response_code);
    }

    // tm 0이면  default
    public static Map<String, Object> getHttp(String pUrl, int tm, Map<String, String> headMap) {
        String response_msg = "";
        String response_cd = "000";
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String linkUrl = pUrl;
        if (!linkUrl.toLowerCase().startsWith("http")) {
            linkUrl = "http://" + linkUrl;
        }
        logger.debug("==== getHttp >>>> url:{}", linkUrl);

        try {
            HttpGet httpGet = new HttpGet(linkUrl);
            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            if (tm > 0) {
                requestBuilder.setSocketTimeout(tm * 1000);
                requestBuilder.setConnectTimeout(tm * 1000);
                requestBuilder.setConnectionRequestTimeout(tm * 1000);
            }
            RequestConfig requestConfig = requestBuilder.build();
            httpGet.setConfig(requestConfig);

//			httpGet.setHeader("x-auth-key", auth_key);
            httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
            for (String entry : headMap.keySet())
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
                responseMap = om.readValue(body, new TypeReference<Map<String, Object>>() {
                });
            } else {
                response_msg = response.getStatusLine().toString();
                response_cd = response.getStatusLine().getStatusCode() + "";
                logger.error("response is error : code:{},message:{} ", response_cd, response_msg);
            }
        } catch (Exception e) {
            logger.error("==== postHttp ERROR Exception >>>> {},{}", linkUrl, e.getMessage());
            response_msg = e.getMessage();
            response_cd = "901";
        }
        logger.debug("==== return message >>>> code:{},msg:{},url:{}", response_cd, response_msg, linkUrl);

        if (response_cd.length() != 3) {
            if (response_cd.length() > 3) {
                response_cd = response_cd.substring(0, 3);
            } else {
                response_cd = "909";
            }
        }
        if (!"000".equals(response_cd)) {
            responseMap.put("responseMsg", response_msg);
            responseMap.put("responseCd", response_cd);
        }
        return responseMap;
    }

//    public static HashMap<String, Object> authKeyCheck(MultiValueMap<String, String> headers) {
//        auth_key = gVal.getAuthKey();
//
//        HashMap<String, Object> map = new HashMap<>();
//        String result_msg = GlobalVariable.G_SUCCESS_MSG;
//        String result_code = GlobalVariable.G_SUCCESS_CODE;
//        String x_auth_key = "";
//
//        // authKey check
//        try {
//            x_auth_key = headers.get("x-auth-key").toString().replaceAll("\\[", "").replaceAll("\\]", "");
//            if (!x_auth_key.equals(auth_key)) {
//                logger.error("==== ERROR auth_key >>>> auth_key:{},x_auth_key{}", auth_key, x_auth_key);
//                result_msg = "ERROR" + " auth_key!!!!";
//                result_code = GlobalVariable.G_FAILED_CODE;
//            }
//        } catch (Exception e) {
//            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
//            result_msg = "ERROR Exception";
//            result_code = GlobalVariable.G_FAILED_CODE;
//        }
//
//        map.put("message", result_msg);
//        map.put("code", result_code);
//        return map;
//    }

//    public static HashMap<String, Object> authNoCheck(MultiValueMap<String, String> headers) {
//        String auth_no = gVal.getAuthNo();
//
//        HashMap<String, Object> map = new HashMap<>();
//        String result_msg = GlobalVariable.G_SUCCESS_MSG;
//        String result_code = GlobalVariable.G_SUCCESS_CODE;
//        String x_auth_no = "";
//
//        // authKey check
//        try {
//            x_auth_no = headers.get("x-auth-no").toString().replaceAll("\\[", "").replaceAll("\\]", "");
//            if (!x_auth_no.equals(auth_no)) {
//                logger.error("==== ERROR auth_no >>>> auth_no:{},x_auth_no{}", auth_no, x_auth_no);
//                result_msg = "ERROR" + " auth_key!!!!";
//                result_code = GlobalVariable.G_FAILED_CODE;
//            }
//        } catch (Exception e) {
//            logger.error("==== ERROR Exception >>>> {}", e.getMessage());
//            result_msg = "ERROR Exception";
//            result_code = GlobalVariable.G_FAILED_CODE;
//        }
//
//        map.put("message", result_msg);
//        map.put("code", result_code);
//        return map;
//    }

	public static Map<String, Object> postHttpLpr(String pUrl, Map<String, Object> map, int tm)  {
		String response_msg  = "";
		String response_cd = "000";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String linkUrl = pUrl;
		if (!linkUrl.toLowerCase().startsWith("http")) {
			linkUrl = "http://" + linkUrl;
		}
		logger.debug("==== URL::{}",linkUrl);

		try {
			HttpPost httpPost = new HttpPost(linkUrl);
			RequestConfig.Builder requestBuilder = RequestConfig.custom();
			if (tm > 0) {
				requestBuilder.setSocketTimeout(tm * 1000);
				requestBuilder.setConnectTimeout(tm * 1000);
				requestBuilder.setConnectionRequestTimeout(tm * 1000);
			}
			RequestConfig requestConfig = requestBuilder.build();
			httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
//			httpPost.setHeader("x-auth-key", gVal.getAuthKey());

			JSONObject json = new JSONObject(map);
			httpPost.setEntity(new StringEntity(json.toString(),"UTF-8"));

			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpPost);

			// Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);

				ObjectMapper om = new ObjectMapper();
				responseMap = om.readValue(body, new TypeReference<Map<String, Object>>(){});

				response_cd = responseMap.get("responseCd").toString();
				response_msg = responseMap.get("responseMsg").toString();
			} else {
				response_msg = response.getStatusLine().toString();
				response_cd = response.getStatusLine().getStatusCode() + "";
				logger.error("response is error : code:{},message:{} ",response_cd,response_msg);
			}
		} catch (Exception e) {
			logger.error("==== postHttp ERROR Exception >>>> {},{}", linkUrl, e.getMessage());
			response_msg 	= e.getMessage();
			response_cd = "901";
		}

		logger.debug("==== return message >>>> code:{},msg:{},url:{}", response_cd, response_msg,linkUrl);
		if (response_cd.length() != 3) {
			if (response_cd.length() > 3) {
				response_cd = response_cd.substring(0,3);
			} else {
				response_cd = "909";
			}
		}

		if (!"000".equals(response_cd)) {
			responseMap.put("responseMsg", response_msg);
			responseMap.put("responseCd", response_cd);
		} else {
			responseMap.put("responseMsg", "success");
			responseMap.put("responseCd", "000");
		}
		return responseMap;
	}

}
