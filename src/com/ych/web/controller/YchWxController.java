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
	
	/**
	 * 添加汽车
	 */
	public void tjqc() {
		List<CarBrandModel> brands = CarBrandModel.dao.getBrands();
		setAttr("brands", brands);
		render("/ych/add_car");
	}
	
	/**
	 * 选择汽车
	 */
	public void xzqc() {
		Integer bID = getParaToInt("brand");
		List<CarSeriesModel> series = CarSeriesModel.dao.getSeries(bID);
		setAttr("series", series);
		render("/ych/select_car");
	}

	/**
	 * 保存选中汽车
	 */
	public void bcqc() {
		String query = getPara("query");
		try {
			Integer cID = getParaToInt("car");
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(
						OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "ych/bcqc?car=" + cID
								+ (query != null ? "&query=1" : ""), "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("did");
			WxUserCarModel carModel = WxUserCarModel.dao.getWxUserCarModel(uID);
			boolean result = false;
			if (carModel == null) {
				carModel = new WxUserCarModel();
				carModel.set("u_id", uID);
				carModel.set("c_id", cID);
				result = carModel.save();
			} else {
				carModel.set("c_id", cID);
				result = carModel.update();
			}
			if (!result) {
				render("/ych/add_car_error");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (query == null)
			redirect("/ych/yyby");
		else
			redirect("/ych/bycx");
	}
}
