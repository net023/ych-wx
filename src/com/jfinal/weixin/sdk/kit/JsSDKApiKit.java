package com.jfinal.weixin.sdk.kit;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.JsApiTicket;
import com.jfinal.weixin.sdk.api.JsApiTicketApi;
import com.ych.tools.DevConstants;

/**
 * 
 * @author 蔡陶军
 *
 */
public class JsSDKApiKit {
	private static Prop prop = PropKit.getProp(DevConstants.DB_CONFIG_FILENAME);

	public static String getWxParam(HttpServletRequest request) {
		JsApiTicket ticket = JsApiTicketApi.getJsApiTicket();
		String url = request.getScheme() + "://" + request.getServerName() + request.getRequestURI();
		String queryString = request.getQueryString();
		if (StrKit.notBlank(queryString)) {
			url = url.concat("?").concat(queryString);
		}
		System.out.println(url);
		Map<String, Object> sign = sign(ticket.getTicket(), url);
		return JsonKit.toJson(sign);
	}

	private static Map<String, Object> sign(String jsapi_ticket, String url) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		//注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		System.out.println(string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		ret.put("url", url);
//		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("debug", false);
		ret.put("appId", prop.get("appId"));
		ret.put("timestamp", timestamp);
		ret.put("nonceStr", nonce_str);
		ret.put("signature", signature);
		ret.put("jsApiList", new String[]{"checkJsApi", "onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo", "hideMenuItems", "showMenuItems",
				"hideAllNonBaseMenuItem", "showAllNonBaseMenuItem", "translateVoice", "startRecord", "stopRecord", "onRecordEnd", "playVoice", "pauseVoice", "stopVoice", "uploadVoice",
				"downloadVoice", "chooseImage", "previewImage", "uploadImage", "downloadImage", "getNetworkType", "openLocation", "getLocation", "hideOptionMenu", "showOptionMenu", "closeWindow",
				"scanQRCode", "chooseWXPay", "openProductSpecificView", "addCard", "chooseCard", "openCard"});

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
