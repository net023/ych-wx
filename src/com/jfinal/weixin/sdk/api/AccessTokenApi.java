/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.api;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.kit.ParaMap;

/**
 * 认证并获取 access_token API
 * http://mp.weixin.qq.com/wiki/index.php?title=%E8%8E%B7%E5%8F%96access_token
 */
public class AccessTokenApi {

	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	private static AccessToken accessToken;

	public static AccessToken getAccessToken() {
		if (accessToken != null && accessToken.isAvailable()) {
			System.out.println("token没失效。。。。。");
			return accessToken;
		}

		refreshAccessToken();
		return accessToken;
	}

	public static void refreshAccessToken() {
		accessToken = requestAccessToken();
	}

	private static synchronized AccessToken requestAccessToken() {
		AccessToken result = null;
		ApiConfig ac = ApiConfigKit.getApiConfig();
		for (int i = 0; i < 3; i++) {
			String appId = ac.getAppId();
			String appSecret = ac.getAppSecret();
			Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", appSecret).getData();
			String json = HttpKit.get(url, queryParas);
			result = new AccessToken(json);

			if (result.isAvailable())
				break;
		}
		return result;
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ApiConfig ac = new ApiConfig();
		ac.setAppId("wx9803d1188fa5fbda");
		ac.setAppSecret("db859c968763c582794e7c3d003c3d87");
		ApiConfigKit.setThreadLocalApiConfig(ac);

		AccessToken at = getAccessToken();
		if (at.isAvailable())
			System.out.println("access_token : " + at.getAccessToken());
		else
			System.out.println(at.getErrorCode() + " : " + at.getErrorMsg());
	}
}
