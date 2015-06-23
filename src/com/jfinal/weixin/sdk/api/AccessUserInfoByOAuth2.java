package com.jfinal.weixin.sdk.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
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
			Map<String, Object> infos = new HashMap<String, Object>();
			String finalInte = MessageFormat.format(URL, code);
			String json = HttpKit.get(finalInte);
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> result = mapper.readValue(json, Map.class);
			System.out.println("result===>:"+result);
			String openid = result.get("openid").toString();
			System.out.println("openId===>:"+openid);
			List<WxUserModel> userList = WxUserModel.dao.getPage(openid);
			Map<String, Object> userInfos = new HashMap<String, Object>();
			if(userList.size()==0){
				ApiResult userInfo = UserApi.getUserInfo(openid + "");
				userInfos.put("openid", userInfo.get("openid"));
				userInfos.put("headimgurl", userInfo.get("headimgurl"));
				userInfos.put("nickname", userInfo.get("nickname"));
				infos.put("userInfo", userInfos);
			}else{
				userInfos.put("openid", userList.get(0).get("openid"));
				userInfos.put("headimgurl", userList.get(0).get("pUrl"));
				userInfos.put("nickname", userList.get(0).get("nick"));
				userInfos.put("pAccount", userList.get(0).get("pAccount"));
				userInfos.put("phone", userList.get(0).get("phone"));
				infos.put("userInfo", userInfos);
				infos.put("did", userList.get(0).get("id"));
			}
			infos.put("openid", openid);
			System.out.println("infos===>:"+infos);
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
