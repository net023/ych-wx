package com.ych.web.controller;

import com.jfinal.log.Logger;
import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;

@Control(controllerKey = "/ych")
public class YchWxController extends BaseController {

	private static final Logger LOG = Logger.getLogger(YchWxController.class);
	private static final String WX_SER_URL = "http://czp.tunnel.mobi/ych-wx/";
	private static final String OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
			+ SysConstants.WX_APPID
			+ "&redirect_uri={0}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

	/**
	 * 保养预约
	 */
	public void yyby() {
		try {
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "ych/yyby", "UTF-8")));
				return;
			}
			List<CarouselModel> carousels = CarouselModel.dao.getTopThree();
			setAttr("carousels", carousels);
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			// TODO 此处有一个BUG
			System.out.println(infos);
			Integer uID = (Integer) infos.get("did");
			System.out.println("uID: " + uID);
			WxUserCarModel car = WxUserCarModel.dao.getCar(uID);
			setAttr("car", car);
		} catch (Exception e) {
			LOG.error("失败", e);
		}
		render("/ych/index");
	}
}
