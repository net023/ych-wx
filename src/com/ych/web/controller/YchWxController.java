package com.ych.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ych.tools.DbConstants;
import com.ych.tools.YchConstants;
import com.ych.web.model.CarBrandModel;
import com.ych.web.model.CarSeriesModel;
import com.ych.web.model.CarouselModel;
import com.ych.web.model.HelpModel;
import com.ych.web.model.OilPriceModel;
import com.ych.web.model.OrderCommodityModel;
import com.ych.web.model.OrderModel;
import com.ych.web.model.ProductModel;
import com.ych.web.model.StoreEvalModel;
import com.ych.web.model.StoreModel;
import com.ych.web.model.StorePicModel;
import com.ych.web.model.WxUserCarModel;
import com.ych.web.model.WxUserModel;

@Control(controllerKey = "/ych")
public class YchWxController extends BaseController {

	private static final Logger LOG = Logger.getLogger(YchWxController.class);
	private static final String WX_SER_URL = YchConstants.YCH_BASEURL;
	private static final String OAUTH2_URL = YchConstants.WXOAUTH2URL+"appid="+ DbConstants.APPID
			+ "&redirect_uri={0}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

	/**
	 * 保养预约
	 */
	public void yyby() {
		try {
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "/ych/yyby", "UTF-8")));
				return;
			}
			List<CarouselModel> carousels = CarouselModel.dao.getTopThree();
			setAttr("carousels", carousels);
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			// TODO 此处有一个BUG
			System.out.println(infos);
			Integer uID = (Integer) infos.get("id");
			System.out.println("uID: " + uID);
			WxUserCarModel car = WxUserCarModel.dao.getCar(uID);
			setAttr("car", car);
			setAttr("tell", YchConstants.YCH_TELL);
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
	 * 吐槽园地
	 */
	public void tcyd() {
		renderHtml(YchConstants.INCONSTRUCTION);
	}
	
	/**
	 * 反馈信息
	 */
	public void fkxx(){
		renderHtml(YchConstants.INCONSTRUCTION);
	}

	/**
	 * 保养档案
	 */
	public void byda() {
		try {
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "/ych/byda", "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("id");
			System.out.println("uID: " + uID);
			Page<OrderModel> pager = OrderModel.dao.getOrders(1, 10, uID);
			setAttr("orders", pager.getList());
			setAttr("uID", uID);
			setAttr("page", pager.getPageNumber());
			setAttr("totalPage", pager.getTotalPage());
			render("/ych/order_form_record");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保养查询
	 */
	public void bycx() {
		try {
			String code = getPara("code");
			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + "/ych/bycx", "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("id");
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
						URLEncoder.encode(WX_SER_URL + "/ych/bcqc?car=" + cID
								+ (query != null ? "&query=1" : ""), "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("id");
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
		Integer msID = getParaToInt("mtser");
		String cLyID = getPara("carlyid");
		String lonStr = getPara("lon");
		String latStr = getPara("lat");
		Float lon = null;
		Float lat = null;
		if (lonStr != null && latStr != null) {
			lon = Float.parseFloat(lonStr);
			lat = Float.parseFloat(latStr);
		}
		if (lat != null && lon != null) {
			setAttr("lat", lat);
			setAttr("lon", lon);
		}
		List<ProductModel> recommends = ProductModel.dao.getRecommends(cLyID,
				msID);
		for (ProductModel recommend : recommends) {
			Integer type = Integer.parseInt(Long.toString(recommend
					.getLong("type")));
			switch (type) {
			case 1:
				if (getAttr("filter1") == null)
					setAttr("filter1", recommend);
				continue;
			case 2:
				if (getAttr("filter2") == null)
					setAttr("filter2", recommend);
				continue;
			case 3:
				if (getAttr("filter3") == null)
					setAttr("filter3", recommend);
				continue;
			case 5:
				Integer litre = recommend.getInt("litre");
				Integer bID = recommend.getInt("b_id");
				if (litre == 1 && bID == 1 && getAttr("oil1") == null) {
					setAttr("oil1", recommend);
					continue;
				}
				if (litre == 2 && bID == 1 && getAttr("oil2") == null) {
					setAttr("oil2", recommend);
					continue;
				}
				continue;
			default:
				break;
			}
		}
		setAttr("msID", msID);
		// TODO MySQL的SQL语句中实现距离查询，可能有性能问题
		Map<String, Object> params = new HashMap<String, Object>();
		if (lat != null && lon != null) {
			params.put("lat", lat);
			params.put("lon", lon);
		}
		params.put("ms_id", msID);
		Page<StoreModel> pager = StoreModel.dao.getPager(0, 10, params);
		setAttr("stores", pager.getList());
		setAttr("page", pager.getPageNumber());
		setAttr("totalPage", pager.getTotalPage());
		render("/ych/submit_orders");
	}

	/**
	 * 切换机油品牌
	 */
	public void qhjypp() {
		Map<String, Object> result = getResultMap();
		try {
			Integer oil1 = getParaToInt("oil1");
			Integer oil2 = getParaToInt("oil2");
			Integer brand = getParaToInt("brand");
			if ((oil1 == null && oil2 == null) || brand == null) {
				result.put(RESULT, 1);
				result.put(ERROR, "缺少必要的参数");
				renderJson(result);
				return;
			}
			List<OilPriceModel> oilPrices = new ArrayList<OilPriceModel>();
			OilPriceModel oilPrice = null;
			if (oil1 != null) {
				oilPrice = OilPriceModel.dao.getOtherBrandOilPrice(oil1, brand);
				if (oilPrice == null) {
					result.put(RESULT, 2);
					result.put(ERROR, "无法找到该品牌对应的商品数据");
					renderJson(result);
					return;
				}
				oilPrices.add(oilPrice);
			}
			if (oil2 != null) {
				oilPrice = OilPriceModel.dao.getOtherBrandOilPrice(oil2, brand);
				if (oilPrice == null) {
					result.put(RESULT, 2);
					result.put(ERROR, "无法找到该品牌对应的商品数据");
					renderJson(result);
					return;
				}
				oilPrices.add(oilPrice);
			}
			result.put(RESULT, 0);
			result.put(DATA, oilPrices);
			renderJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(RESULT, 3);
			result.put(ERROR, "未得到正确的数据");
			renderJson(result);
		}
	}

	/**
	 * 确认详情
	 */
	public void qrxq() {
		try {
			Integer msID = getParaToInt("msID");
			String oil1 = getPara("oil1");
			String oil2 = getPara("oil2");
			String oilNone = getPara("oil-none");
			String filter1 = getPara("filter1");
			String filter2 = getPara("filter2");
			String filter3 = getPara("filter3");
			Integer sID = getParaToInt("store");
			String totalPriceStr = getPara("totalPrice");
			Double totalPrice = null;
			if (totalPriceStr != null)
				totalPrice = Double.parseDouble(totalPriceStr);

			String code = getPara("code");
			if (code == null) {
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append(WX_SER_URL).append("/ych/qrxq?msID=")
						.append(msID);
				if (oil1 != null)
					urlBuilder.append("&oil1=").append(oil1);
				if (oil2 != null)
					urlBuilder.append("&oil2=").append(oil2);
				if (oilNone != null)
					urlBuilder.append("&oil-none=").append(oilNone);
				if (filter1 != null)
					urlBuilder.append("&filter1=").append(filter1);
				if (filter2 != null)
					urlBuilder.append("&filter2=").append(filter2);
				if (filter3 != null)
					urlBuilder.append("&filter3=").append(filter3);
				if (sID != null)
					urlBuilder.append("&store=").append(sID);
				if (totalPriceStr != null)
					urlBuilder.append("&totalPrice=").append(totalPriceStr);
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(urlBuilder.toString(), "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			String phone = (String) infos.get("phone");
			if (phone != null)
				setAttr("phone", phone);
			System.out
					.printf("msID: %s, oil1: %s, oil2: %s, oil-none: %s, filter1: %s, filter2: %s, filter3: %s, store: %s\n",
							msID, oil1, oil2, oilNone, filter1, filter2,
							filter3, sID);
			setAttr("msID", msID);
			if (oil1 != null)
				setAttr("oil1", oil1);
			if (oil2 != null)
				setAttr("oil2", oil2);
			if (oilNone != null)
				setAttr("oil_none", oilNone);
			if (filter1 != null)
				setAttr("filter1", filter1);
			if (filter2 != null)
				setAttr("filter2", filter2);
			if (filter3 != null)
				setAttr("filter3", filter3);
			if (totalPrice != null)
				setAttr("totalPrice", totalPrice);
			setAttr("sID", sID);
			render("/ych/submit_contact");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 完成预约
	 * 
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void wcyy() throws Exception {
		try {
			Integer msID = getParaToInt("msID");
			String oil1 = getPara("oil1");
			String oil2 = getPara("oil2");
			String oilNone = getPara("oil-none");
			String filter1 = getPara("filter1");
			String filter2 = getPara("filter2");
			String filter3 = getPara("filter3");
			Integer sID = getParaToInt("sID");
			String phone = getPara("phone");
			Date resTime = getParaToDate("date");
			String totalPriceStr = getPara("totalPrice");
			Double totalPrice = null;
			if (totalPriceStr != null)
				totalPrice = Double.parseDouble(totalPriceStr);

			if (msID == null || totalPriceStr == null || resTime == null
					|| sID == null) {
				setAttr("error", "请确定保养类型、总价、预约时间不为空");
				render("/ych/order_form_fail");
				return;
			}
			if (oil1 == null && oil2 == null && oilNone == null
					|| filter1 == null) {
				setAttr("error", "保养信息不全请");
				render("/ych/order_form_fail");
				return;
			}

			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(WX_SER_URL).append("/ych/wcyy?msID=").append(msID);
			if (oil1 != null)
				urlBuilder.append("&oil1=").append(oil1);
			if (oil2 != null)
				urlBuilder.append("&oil2=").append(oil2);
			if (oilNone != null)
				urlBuilder.append("&oil-none=").append(oilNone);
			if (filter1 != null)
				urlBuilder.append("&filter1=").append(filter1);
			if (filter2 != null)
				urlBuilder.append("&filter2=").append(filter2);
			if (filter3 != null)
				urlBuilder.append("&filter3=").append(filter3);
			if (sID != null)
				urlBuilder.append("&sID=").append(sID);
			if (phone != null)
				urlBuilder.append("&phone=").append(phone);
			if (resTime != null) {
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				urlBuilder.append("&date=").append(formatter.format(resTime));
			}
			if (totalPrice != null)
				urlBuilder.append("&totalPrice=").append(totalPrice);
			String code = getPara("code");

			if (code == null) {
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(urlBuilder.toString(), "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);

			Integer uID = (Integer) infos.get("id");
			if (phone != null) {
				if (!WxUserModel.dao.bindPhone(uID, phone)) {
					setAttr("error", "手机绑定失败");
				}
			}

			int oID = OrderModel.dao.makeOrder(uID, msID, sID, resTime,
					totalPrice);
			OrderCommodityModel.dao.saveBatch(oID, oil1, oil2, oilNone,
					filter1, filter2, filter3);

			System.out
					.printf("msID: %s, oil1: %s, oil2: %s, oil-none: %s, filter1: %s, filter2: %s, filter3: %s, store: %s, phone: %s, date: %s, totalPrice: %s\n",
							msID, oil1, oil2, oilNone, filter1, filter2,
							filter3, sID, phone, resTime, totalPrice);

			// TODO 校验日期是否符合规范
			render("/ych/order_form_success");
		} catch (Exception e) {
			e.printStackTrace();
			render("/ych/order_form_fail");
			throw e;
		}
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
					sID, 1, 10);
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
	 * 获取评论
	 */
	public void hqpl() {
		Map<String, Object> result = getResultMap();
		try {
			Integer sID = getParaToInt("sID");
			Integer pageNumber = getParaToInt("page");
			if (sID == null) {
				result.put(RESULT, 1);
				result.put(ERROR, "门店id不能为空");
				renderJson(result);
				return;
			}
			if (pageNumber == null) {
				result.put(RESULT, 2);
				result.put(ERROR, "页码不能为空");
				return;
			}
			Page<StoreEvalModel> evalsPage = StoreEvalModel.dao.getEvalutates(
					sID, pageNumber, 10);
			result.put(RESULT, 0);
			result.put(DATA, evalsPage.getList());
			renderJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(RESULT, 3);
			result.put(ERROR, "请求错误");
			renderJson(result);
		}
	}

	/**
	 * 获取订单
	 */
	public void hqdd() {
		Map<String, Object> result = getResultMap();
		try {
			Integer uID = getParaToInt("uID");
			Integer pageNumber = getParaToInt("page");
			if (uID == null) {
				result.put(RESULT, 1);
				result.put(ERROR, "用户id不能为空");
				renderJson(result);
				return;
			}
			if (pageNumber == null) {
				result.put(RESULT, 2);
				result.put(ERROR, "页码不能为空");
				return;
			}
			Page<OrderModel> pager = OrderModel.dao.getOrders(pageNumber, 10,
					uID);
			result.put(RESULT, 0);
			result.put(DATA, pager.getList());
			renderJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(RESULT, 3);
			result.put(ERROR, "请求错误");
			renderJson(result);
		}
	}

	/**
	 * 查看地图
	 */
	public void ckdt() {
		setAttr("lon", getPara("lon"));
		setAttr("lat", getPara("lat"));
		setAttr("name", getPara("name"));
		setAttr("address", getPara("address"));
		render("/ych/store_map");
	}

	/**
	 * 评价门店
	 */
	public void pjmd() {
		Integer oID = getParaToInt("oID");
		String latStr = getPara("lat");
		String lonStr = getPara("lon");
		Float lat = null;
		Float lon = null;
		if (latStr != null && lonStr != null) {
			lat = Float.parseFloat(latStr);
			lon = Float.parseFloat(lonStr);
		}
		OrderModel order = OrderModel.dao.getOrder(oID);
		Integer sID = order.getInt("s_id");
		StoreModel store = StoreModel.dao.getStore(sID, lat, lon);
		setAttr("order", order);
		setAttr("store", store);
		render("/ych/order_appraise");
	}

	/**
	 * 提交评论
	 */
	public void tjpl() {
		// TODO 此处BUG，微信有时会重定向两次，在StoreEvalModel中进行了判断，防止同一订单重复提交评论
		try {
			Integer oID = getParaToInt("o_id");
			Integer sID = getParaToInt("s_id");
			String code = getPara("code");
			String content = getPara("content");
			System.out.println("content: " + content);
			Integer grade = getParaToInt("grade");
			if (code == null) {
				StringBuilder urlBuilder = new StringBuilder();
				urlBuilder.append(WX_SER_URL).append("/ych/tjpl?o_id=")
						.append(oID).append("&s_id=").append(sID)
						.append("&content=").append(content).append("&grade=")
						.append(grade);
				redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(urlBuilder.toString(), "UTF-8")));
				return;
			}
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			System.out.println(infos);
			Integer uID = (Integer) infos.get("id");
			boolean result = StoreEvalModel.dao.commitEval(uID, sID, content,
					oID, grade);
			if (result)
				redirect("/ych/yyby");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLEncoder.encode(
				WX_SER_URL + "/ych/tjpl?content=中文", "UTF-8"));
	}
}
