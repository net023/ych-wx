package com.ych.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.api.AccessUserInfoByOAuth2;
import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;
import com.ych.tools.SysConstants;
import com.ych.web.model.CarBrandModel;
import com.ych.web.model.CarSeriesModel;
import com.ych.web.model.CarouselModel;
import com.ych.web.model.HelpModel;
import com.ych.web.model.MtserUnitCostModel;
import com.ych.web.model.ProductModel;
import com.ych.web.model.StoreEvalModel;
import com.ych.web.model.StoreModel;
import com.ych.web.model.StorePicModel;
import com.ych.web.model.WxUserCarModel;

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
	 * 关于我们
	 */
	public void gywm() {
		render("/ych/about_us");
	}

	/**
	 * 帮助中心
	 */
	public void bzzx() {
		List<HelpModel> helpList = HelpModel.dao.getHots();
		setAttr("helpList", helpList);
		render("/ych/help_center");
	}

	/**
	 * 保养档案
	 */
	public void byda() {
		render("/ych/order_form_record");
	}

	/**
	 * 保养查询
	 */
	public void bycx() {
		try {
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "ych/bycx", "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("did");
			System.out.println("uID: " + uID);
			WxUserCarModel car = WxUserCarModel.dao.getCar(uID);
			setAttr("car", car);
		} catch (UnsupportedEncodingException e) {
			LOG.error("失败", e);
		}
		render("/ych/car_maintenance_inquiry");
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

	/**
	 * 保养详情
	 */
	public void byxq() {
		Integer mID = getParaToInt("mtser");
		String cLyID = getPara("carlyid");
		String lonStr = getPara("lon");
		String latStr = getPara("lat");
		Float lon = null;
		Float lat = null;
		if (lonStr != null && latStr != null) {
			lon = Float.parseFloat(lonStr);
			lat = Float.parseFloat(latStr);
		}
		List<ProductModel> recommends = ProductModel.dao.getRecommends(cLyID,
				mID);
		setAttr("recommends", recommends);
		MtserUnitCostModel mtser = MtserUnitCostModel.dao.getMtserUnitCost(mID);
		setAttr("mtser", mtser);
		// TODO MySQL的SQL语句中实现距离查询，可能有性能问题
		Map<String, Object> params = new HashMap<String, Object>();
		if (lat != null && lon != null) {
			params.put("lat", lat);
			params.put("lon", lon);
		}
		// List<StoreModel> stores = StoreModel.dao.getStores();
		// setAttr("stores", stores);
		Page<StoreModel> pager = StoreModel.dao.getPager(0, 20, params);
		setAttr("stores", pager.getList());
		setAttr("page", pager.getPageNumber());
		setAttr("totalPage", pager.getTotalPage());
		render("/ych/submit_orders");
	}

	/**
	 * 确认详情
	 */
	public void qrxq() {
		render("/ych/submit_contact");
	}

	/**
	 * 完成预约
	 */
	@Before(Tx.class)
	public void wcyy() {
		Integer mID = getParaToInt("mtser");
		Integer oID = getParaToInt("order");
		Date date = getParaToDate("date");
		String[] commodities = getParaValues("commodities");
		System.out.println(date);
		Map<String, Object> result = getResultMap();
		// TODO 校验日期是否符合规范
		// result.put(RESULT, true);
		// renderJson(result);
		render("/ych/order_form_success");
	}

	/**
	 * 查看门店
	 */
	public void ckmd() {
		try {
			Integer sID = getParaToInt("sID");
			String latStr = getPara("lat");
			String lonStr = getPara("lon");
			Float lat = null;
			Float lon = null;
			if (latStr != null && lonStr != null) {
				lat = Float.parseFloat(latStr);
				lon = Float.parseFloat(lonStr);
			}
			List<StorePicModel> pics = StorePicModel.dao.getStorePics(sID);
			StoreModel store = StoreModel.dao.getStore(sID, lat, lon);
			Page<StoreEvalModel> evalsPage = StoreEvalModel.dao.getEvalutates(
					sID, 1, 20);
			setAttr("pics", pics);
			setAttr("store", store);
			setAttr("evals", evalsPage.getList());
			setAttr("page", evalsPage.getPageNumber());
			setAttr("totalPage", evalsPage.getTotalPage());
			render("/ych/store_introduction");
		} catch (Exception e) {
			LOG.error("查看门店信息失败", e);
		}
	}
	
	/**
	 * 查看地图
	 */
	public void ckdt() {
		setAttr("lon", getPara("lon"));
		setAttr("lat", getPara("lat"));
		render("/ych/store_map");
	}
}
