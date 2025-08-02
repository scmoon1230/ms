package kr.co.ucp.cmm.service.impl;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.springframework.stereotype.Component;

public class PrprtsServiceTag extends SimpleTagSupport {

	private String key;

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void doTag() throws JspException, IOException {
		try {
			getJspContext().getOut().write(PrprtsServiceImpl.getPrprtsVal(key));
		} catch (Exception e) {
			e.printStackTrace();
			getJspContext().getOut().write("");
		}
	}
}
