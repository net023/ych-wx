package com.jfinal.weixin.sdk.api;

import java.text.MessageFormat;

import com.jfinal.kit.HttpKit;

/**
 * 
 * @author 蔡陶军
 *
 */
public class JsApiTicketApi {
	private static String URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";
	private static JsApiTicket ticket;

	public static JsApiTicket getJsApiTicket() {
		if (ticket != null && ticket.isAvailable()) {
			System.out.println("ticket没失效。。。。。");
			return ticket;
		}

		refreshJsApiTicket();
		return ticket;
	}

	public static void refreshJsApiTicket() {
		ticket = requestJsApiTicket();
	}

	private static synchronized JsApiTicket requestJsApiTicket() {
		JsApiTicket result = null;
		String accessToken = AccessTokenApi.getAccessToken().getAccessToken();
		String finalInte = MessageFormat.format(URL, accessToken);
		for (int i = 0; i < 3; i++) {
			String json = HttpKit.get(finalInte);
			result = new JsApiTicket(json);
			if (result.isAvailable()) {
				break;
			}
		}
		return result;
	}
}
