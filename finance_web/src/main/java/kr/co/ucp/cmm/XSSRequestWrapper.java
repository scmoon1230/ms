package kr.co.ucp.cmm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
    
    static final Logger LOGGER = LoggerFactory.getLogger(XSSRequestWrapper.class);

    public XSSRequestWrapper(HttpServletRequest servletRequest) {  
        super(servletRequest);  
    }  
      
    public String[] getParameterValues(String parameter) {  
        String[] values = super.getParameterValues(parameter);  
      
        if (values == null)  {  
            return null;  
        }  
      
        int count = values.length;  
        String[] encodedValues = new String[count];  
      
        for (int i = 0; i < count; i++) {  
            encodedValues[i] = cleanXSS(values[i]);  
        }    
        return encodedValues;   
    }  
      
    public String getParameter(String parameter) {  
        String value = super.getParameter(parameter);  
        if (value == null) {  
            return null;   
        }
          
        return cleanXSS(value);  
    }  
      
    public String getHeader(String name) {  
        String value = super.getHeader(name);  
        if (value == null)  
            return null;  
        return cleanXSS(value);  
    }  
 
    private String cleanXSS(String pValue) {
        /*if (logger.isDebugEnabled()) {
            logger.debug("*********************************************************************");
            logger.debug("value : {}", value);
            logger.debug("*********************************************************************");
        }
        */
        if (pValue != null) {
            try {
                String value = URLDecoder.decode(pValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            	LOGGER.error("value UnsupportedEncodingException : {}", e.getMessage());
            }
            
            String value = pValue.replaceAll("<", "").replaceAll(">", "");  
//            value = value.replaceAll("'", "''");
//            value = value.replaceAll(";", "");
//            value = value.replaceAll("--", "");
//            value = value.replaceAll("select", "");
//            value = value.replaceAll("insert", "");
//            value = value.replaceAll("update", "");
//            value = value.replaceAll("delete", "");
//            value = value.replaceAll("drop", "");
//            value = value.replaceAll("union", "");
//            value = value.replaceAll("and", "");
//            value = value.replaceAll("or", "");
//            value = value.replaceAll("1=1", "");
//            value = value.replaceAll("sp_", "");
//            value = value.replaceAll("xp_", "");
//            value = value.replaceAll("@variable", "");
//            value = value.replaceAll("@@variable", "");
//            value = value.replaceAll("exec", "");
//            value = value.replaceAll("sysobject", "");
            
            /*value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");  
            value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");  
            value = value.replaceAll("'", "& #39;");            
            value = value.replaceAll("eval\\((.*)\\)", "");  
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");  
            value = value.replaceAll("script", "");*/   
        }
        
        return pValue;  
    } 
}
