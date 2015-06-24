package com.jfinal.weixin.sdk.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.kit.HttpKit;
import com.ych.tools.DbConstants;
import com.ych.web.model.WxUserModel;

public class AccessUserInfoByOAuth2 {
	private static String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+DbConstants.APPID+"&secret="+ApiConfigKit.getApiConfig().getAppSecret()+"&code={0}&grant_type=authorization_code";

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getWxUserInfo(String code) {
		try {
			System.out.println("code===>:"+code);
			String finalInte = MessageFormat.format(URL, code);
			String json = HttpKit.get(finalInte);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> result = mapper.readValue(json, Map.class);
			System.out.println("result===>:"+result);
			String openid = result.get("openid").toString();
			System.out.println("openId===>:"+openid);
			Map<String, Object> userInfos = new HashMap<String, Object>();
			if(null!=openid){
				WxUserModel user = WxUserModel.dao.getUser(openid);
				userInfos.put("openid", user.get("openid"));
				userInfos.put("head", user.get("head"));
				userInfos.put("name", user.get("name"));
				userInfos.put("phone", user.get("phone"));
				userInfos.put("city", user.get("city"));
				userInfos.put("id", user.get("id"));
			}else{
				ApiResult user = UserApi.getUserInfo(openid + "");
				userInfos.put("openid", user.get("openid"));
				userInfos.put("head", user.get("headimgurl"));
				userInfos.put("name", user.get("nickname"));
				userInfos.put("city", user.get("city"));
			}
			System.out.println("infos===>:"+userInfos);
			return userInfos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
