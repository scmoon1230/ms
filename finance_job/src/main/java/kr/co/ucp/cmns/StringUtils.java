package kr.co.ucp.cmns;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author cubegram
 *
 */

public final class StringUtils {

	private StringUtils() {
        throw new UnsupportedOperationException();
    }

	/**
	 * 로그
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 입력값인 문자열이 null일 경우 ""을 리턴하고 null이 아니라면 입력받은 문자열의 앞뒤 공백이 없어진 문자열이 리턴된다
	 *
	 * @param stText		입력받은 문자열
	 * @return String		입력받은 문자열 값에 따라 null 또는 입력받은 문자열의 앞뒤공백이 제거된 문자열이 리턴된다
	 */
	public static String getNull(final String stText){
		try {
			if(stText == null) return "";
			if("null".equals(stText) ) return "";
			return stText.trim();
		}catch(Exception e) {
			LOGGER.error(" ==== Exception >>>> {}", e.getMessage());
			//e.printStackTrace();
			return "";
		}
	}

	/**
	 * 입력값인 문자열이 null일 경우 ""을 리턴하고 null이 아니라면 입력받은 문자열의 앞뒤 공백이 없어진 문자열이 리턴된다
	 *
	 * @param objText		입력받은 문자열
	 * @return String		입력받은 문자열 값에 따라 null 또는 입력받은 문자열의 앞뒤공백이 제거된 문자열이 리턴된다
	 */
	public static String getNull(final Object objText) {
		if (objText == null) {
			return "";
		}
		return objText.toString().trim();
	}

	/**
	 * 입력값인 문자열이 null이거나 ""일 경우 원하는 값으로 바꾸어서 리턴한다.
	 *
	 * @param stText			입력받은 문자열
	 * @param stResult			입력받은 문자열이 null이거나 "" 일 경우 바뀌어서 리턴되길 원하는 문자열
	 * @return String
	 */
	public static String getNull(final String stText, final String stResult){
		try {
			if(stText == null || "".equals(stText)) return stResult;
			return stText;
		}catch(Exception e) {
			LOGGER.error(" ==== Exception >>>> {}", e.getMessage());
			//e.printStackTrace();
			return stResult;
		}
	}

	/**
	 *
	 * @param collection
	 * @param separator
	 * @return
	 */
    public static String join(final Collection<?> collection, final String separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    /**
     *
     * @param iterator
     * @param separator
     * @return
     */
    public static String join(final Iterator<?> iterator, final String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }

        // two or more elements
        StrBuilder buf = new StrBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * avoidRegexp
     * @param str
     * @return
     */
	public static String avoidRegexp(final String tmpStr) {
		String str = tmpStr.replace("\\", "\\\\");
		str = str.replace("^", "\\^");
		str = str.replace("[", "\\[");
		str = str.replace("]", "\\]");
		str = str.replace("$", "\\$");
		str = str.replace("(", "\\(");
		str = str.replace(")", "\\)");
		str = str.replace("|", "\\|");
		str = str.replace("*", "\\*");
		str = str.replace("+", "\\+");
		str = str.replace("?", "\\?");
		str = str.replace("{", "\\{");
		str = str.replace("}", "\\}");
		return str;
	}

}
