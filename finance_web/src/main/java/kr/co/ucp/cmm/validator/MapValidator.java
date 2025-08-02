package kr.co.ucp.cmm.validator;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import kr.co.ucp.cmm.CamelMap;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MessageSource messageSource;

    private List<EgovMap> tableInfoList;

    public MapValidator(MessageSource messageSource, List<EgovMap> tableInfoList) {
        this.messageSource = messageSource;
        this.tableInfoList = tableInfoList;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, ?> params = (Map<String, ?>) target;
        for (EgovMap table : tableInfoList) {
            String columnName = EgovStringUtil.nullConvert(table.get("columnName"));
            String columnComment = EgovStringUtil.nullConvert(table.get("columnComment"));
            String dataType = EgovStringUtil.nullConvert(table.get("dataType"));
            String dataLength = EgovStringUtil.nullConvert(table.get("dataLength"));
            String nullable = EgovStringUtil.nullConvert(table.get("nullable"));
            String primaryKeyYn = EgovStringUtil.nullConvert(table.get("primaryKeyYn"));
            // String regExp = "^[0-9a-zA-Z가-힣._()-]+$";
            for (String key : params.keySet()) {
                String columnNameCamel = CamelMap.convertUnderscoreNameToPropertyName(columnName);
                if (columnNameCamel.equals(key)) {
                    Object obj = params.get(key);
                    String[] values = null;
                    if (obj.getClass() == String.class) {
                        values = new String[]{EgovStringUtil.nullConvert(obj)};
                    } else if (obj.getClass() == String[].class) {
                        values = (String[]) params.get(key);
                    }

                    if (values != null) {
                        for (String value : values) {
                            // value = StringEscapeUtils.escapeHtml(value);
                            int length = Integer.parseInt(dataLength);
                            int lengthByte = value.getBytes().length;
//                            logger.info(">>>>>>>>>>" + columnName + ", " + columnComment + ", " + dataType + ", " + dataLength + ", " + nullable + ", " + primaryKeyYn);
//                            logger.info(">>>>>>>>>>" + key + " : " + value + ", " + dataType + "(" + length + "), " + lengthByte + ", ");

                            /*
                            if ("Y".equals(primaryKeyYn) && !CommonUtil.match(value, regExp)) {
                                errors.rejectValue(key, "errors.primarykey", null, messageSource.getMessage("errors.primarykey", new String[]{columnComment}, Locale.KOREAN));
                            }
                            */

                            if (EgovStringUtil.lowerCase(dataType).contains("varchar") && length < lengthByte) {
                                // 길이 체크 varchar
                                errors.rejectValue(key, "errors.maxlength.byte", null, messageSource.getMessage("errors.maxlength.byte", new String[]{columnComment, dataLength, String.valueOf(lengthByte), value}, Locale.KOREAN));
                            } else if (EgovStringUtil.lowerCase(dataType).contains("char") && length < lengthByte) {
                                // 길이 체크 char
                                errors.rejectValue(key, "errors.maxlength", null, messageSource.getMessage("errors.maxlength", new String[]{columnComment, dataLength, String.valueOf(lengthByte)}, Locale.KOREAN));
                            } else if (EgovStringUtil.lowerCase(dataType).contains("number")) {
                                if (length < lengthByte) {
                                    // 길이 체크 number
                                    errors.rejectValue(key, "errors.maxlength", null, messageSource.getMessage("errors.maxlength", new String[]{columnComment, dataLength, String.valueOf(lengthByte)}, Locale.KOREAN));
                                } else if (!NumberUtils.isCreatable(value)) {
                                    // 숫자 유형 체크
                                    errors.rejectValue(key, "errors.number", null, messageSource.getMessage("errors.number", new String[]{columnComment}, Locale.KOREAN));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
