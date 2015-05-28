package com.jfinal.weixin.sdk.api;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author 蔡陶军
 *
 */
public class JsApiTicket {
	private Integer errcode;
	private String errmsg;
	private String ticket;
	private Integer expires_in;
	private Long expiredTime;

	public JsApiTicket(String jsonStr) {
		try {
			Map<?, ?> map = new ObjectMapper().readValue(jsonStr, Map.class);
			ticket = (String) map.get("ticket");
			expires_in = (Integer) map.get("expires_in");
			errcode = (Integer) map.get("errcode");
			errmsg = (String) map.get("errmsg");

			if (expires_in != null)
				expiredTime = System.currentTimeMillis() + ((expires_in - 5) * 1000);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public boolean isAvailable() {
		System.out.println("aaa");
		if (expiredTime == null)
			return false;
		if (errcode == null)
			return false;
		if (expiredTime < System.currentTimeMillis())
			return false;
		return ticket != null;
	}

}
