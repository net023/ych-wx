package com.jfinal.weixin.core;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.EncryptionKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.JsApiTicket;
import com.jfinal.weixin.sdk.api.JsApiTicketApi;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.ych.tools.DevConstants;

public class WeixinApiController extends ApiController {

	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();

		// 配置微信 API 相关常量
		Prop prop = PropKit.getProp(DevConstants.DB_CONFIG_FILENAME);
		ac.setToken(prop.get("token"));
		ac.setAppId(prop.get("appId"));
		ac.setAppSecret(prop.get("appSecret"));

		/**
		 * 是否对消息进行加密，对应于微信平台的消息加解密方式：
		 * 1：true进行加密且必须配置 encodingAesKey
		 * 2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(prop.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(prop.get("encodingAesKey", "setting it in config file"));
		return ac;
	}

	/*
	 * public void index() {
	 * render("/api/index.html");
	 * }
	 */

	/**
	 * 获取公众号菜单
	 */
	public void getMenu() {
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}

	/**
	 * 获取公众号关注用户
	 */
	public void getFollowers() {
		ApiResult apiResult = UserApi.getFollows();
		// TODO 用 jackson 解析结果出来
		renderText(apiResult.getJson());
	}
	
	public void getJsConfig() {
		getResponse().addHeader("Access-Control-Allow-Origin", "*");
		JsApiTicket jsTicket = JsApiTicketApi.getJsApiTicket();
		long timestamp = System.currentTimeMillis() / 1000;
		String url = getPara("url");
		String noncestr = EncryptionKit.sha1Encrypt(timestamp + "");

		System.out.println(url);
		if (!jsTicket.isAvailable() || url == null) {
			renderText("js ticket or url unavailable");
			return;
		}

		String signature = generateSignature(jsTicket.getTicket(), timestamp, url, noncestr);
		System.out.println("jsapi_ticket=" + jsTicket.getTicket());
		System.out.println("timestamp=" + timestamp);
		System.out.println("nonceStr=" + noncestr);
		System.out.println("url=" + url);
		System.out.println("signature=" + signature);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("timestamp", timestamp);
		result.put("signature", signature);
		result.put("nonceStr", noncestr);
		result.put("appId", ApiConfigKit.getApiConfig().getAppId());
		renderJson(result);

	}

	private String generateSignature(String jsTicket, long timestamp, String url, String noncestr) {
		StringBuilder builder = new StringBuilder();
		builder.append("jsapi_ticket=").append(jsTicket).append("&").append("noncestr=").append(noncestr)
				.append("&").append("timestamp=").append(timestamp).append("&").append("url=").append(url);
		System.out.println(builder.toString());
		return EncryptionKit.sha1Encrypt(builder.toString());
	}
}
