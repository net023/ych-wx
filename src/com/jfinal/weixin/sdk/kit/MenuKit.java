package com.jfinal.weixin.sdk.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.ych.tools.DbConstants;

/**
 * 封装自定义菜单
 * 
 * @author 曾芸杰
 * @since 2015-5-6 下午10:17:58
 */
public class MenuKit {

	/**
	 * 
	 马上保养 --保养预约
	 * 
	 * 个人中心 --我的订单 --保养记录
	 * 
	 * 服务中心 --反馈信息 --帮众中心 --拨打热线 --门店后台 --吐槽园地
	 */
	public static String createYchMenu() {
		String wxSerUrl = "http://xmn.tunnel.mobi/ych-wx/";
		String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ DbConstants.APPID
				+ "&redirect_uri={0}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

		Map<String, Object> menuMap = new HashMap<String, Object>();
		List<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
		Map<String, Object> btn1 = new HashMap<String, Object>();
		Map<String, Object> btn2 = new HashMap<String, Object>();
		Map<String, Object> btn3 = new HashMap<String, Object>();
		// List<Map<String, Object>> btn1_sbList = new ArrayList<Map<String,
		// Object>>();
		List<Map<String, Object>> btn2_sbList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> btn3_sbList = new ArrayList<Map<String, Object>>();
		Map<String, Object> btn1_sb1 = new HashMap<String, Object>();
		Map<String, Object> btn2_sb1 = new HashMap<String, Object>();
		Map<String, Object> btn2_sb2 = new HashMap<String, Object>();
		Map<String, Object> btn2_sb3 = new HashMap<String, Object>();
		Map<String, Object> btn3_sb1 = new HashMap<String, Object>();
		Map<String, Object> btn3_sb2 = new HashMap<String, Object>();
		Map<String, Object> btn3_sb3 = new HashMap<String, Object>();
		Map<String, Object> btn3_sb4 = new HashMap<String, Object>();
		Map<String, Object> btn3_sb5 = new HashMap<String, Object>();

		// btn1_sb1.put("type", "view");
		// btn1_sb1.put("name", "保养预约");
		// btn1_sb1.put("url", wxSerUrl+"ych/yyby");
		btn1.put("type", "view");
		btn1.put("name", "保养预约");
		btn1.put("url", wxSerUrl + "ych/yyby");

		btn2_sb1.put("type", "view");
		btn2_sb1.put("name", "我的订单");
		btn2_sb1.put("url", wxSerUrl + "ych/byda");

		btn2_sb2.put("type", "view");
		btn2_sb2.put("name", "保养档案");
		btn2_sb2.put("url", wxSerUrl + "ych/byda");

		btn2_sb3.put("type", "view");
		btn2_sb3.put("name", "保养查询");
		btn2_sb3.put("url", wxSerUrl + "ych/bycx");

		btn3_sb1.put("type", "view");
		btn3_sb1.put("name", "反馈信息");
		btn3_sb1.put("url", wxSerUrl + "ych/fkxx");

		btn3_sb2.put("type", "view");
		btn3_sb2.put("name", "帮助中心");
		btn3_sb2.put("url", wxSerUrl + "ych/bzzx");

		btn3_sb3.put("type", "click");
		btn3_sb3.put("name", "拨打热线");
		btn3_sb3.put("key", "key_tell");

		btn3_sb4.put("type", "view");
		btn3_sb4.put("name", "门店后台");
		btn3_sb4.put("url", wxSerUrl + "blogin");

		btn3_sb5.put("type", "view");
		btn3_sb5.put("name", "吐槽园地");
		btn3_sb5.put("url", wxSerUrl + "ych/tcyd");

		// btn1_sbList.add(btn1_sb1);

		btn2_sbList.add(btn2_sb1);
		btn2_sbList.add(btn2_sb2);
		btn2_sbList.add(btn2_sb3);

		btn3_sbList.add(btn3_sb1);
		btn3_sbList.add(btn3_sb2);
		btn3_sbList.add(btn3_sb3);
		btn3_sbList.add(btn3_sb4);
		btn3_sbList.add(btn3_sb5);

		// btn1.put("name", "马上保养");
		btn2.put("name", "个人中心");
		btn3.put("name", "服务中心");
		// btn1.put("sub_button", btn1_sbList);
		btn2.put("sub_button", btn2_sbList);
		btn3.put("sub_button", btn3_sbList);

		buttons.add(btn1);
		buttons.add(btn2);
		buttons.add(btn3);

		menuMap.put("button", buttons);
		return JsonKit.toJson(menuMap);
	}

	public static void main(String[] args) {
		String menu = createYchMenu();
		System.out.println(menu);
	}
}
