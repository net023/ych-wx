package com.ych.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.EncryptionKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;
import com.ych.tools.SysConstants;
import com.ych.tools.YchConstants;
import com.ych.web.model.BUserModel;
import com.ych.web.model.OrderModel;
import com.ych.web.model.SysLoginLogModel;

/**
 * 无图标app
 */
@Control(controllerKey = {"/blogin"})
public class BLoginController extends BaseController {

	private static final Logger LOG = Logger.getLogger(BLoginController.class);

	public void index() {
		try {
			BUserModel user = getUser();
			if(null!=user){
				setAttr("tell", YchConstants.YCH_TELL);
				setAttr("nick", user.get("name"));
				render("/ych/mdht/main");
				return;
			}
		} catch (Exception e) {
			LOG.error("失败", e);
		}
		render("/ych/mdht/index");
	}

	public void loginCheck() {
		Map<String, Object> result = getResultMap();
		String username = getPara("username");
		String password = getPara("password");
		if (StrKit.isBlank(username) || StrKit.isBlank(password)) {
			result.put(ERROR, "用户名/密码不能为空");
			renderJson(result);
		}
		try {
			BUserModel user = BUserModel.dao.login(username, password);
			if (null != user) {
				result.put(RESULT, true);
				getSession().setAttribute(SysConstants.SESSION_USER, user);
				new SysLoginLogModel().set("userID", user.get("id")).set("username", user.get("username")).set("loginIP", getRealIpAddr(getRequest())).set("loginDate", new Date()).save();
			}else{
				result.put(ERROR, "用户名/密码错误!");
			}
		} catch (Exception e) {
			result.put(ERROR, "登录失败");
			LOG.error("登录失败", e);
		}
		renderJson(result);
	}

	public void logout() {
		getSession().removeAttribute(SysConstants.SESSION_USER);
		redirect("/blogin");
	}
	
	
	public void changePass(){
		Map<String, Object> result = getResultMap();
		try {
			String newPassword = getPara("np");
			Integer uid = getParaToInt("id");
			boolean isChangePassword = BUserModel.dao.changePassword(uid, newPassword);
			if(isChangePassword){
				result.put(RESULT, true);
			}else{
				result.put(ERROR, "服务器修改失败");
			}
		} catch (Exception e) {
			result.put(ERROR, "服务器异常");
			LOG.error("修改密码失败", e);
		}
		renderJson(result);
	}
	

	public void main() {
		BUserModel user = getUser();
		if(user==null){
			redirect("/blogin");
		}else{
//			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			setAttr("sysDate", format.format(Calendar.getInstance().getTime()));
			setAttr("nick", user.get("name"));
			setAttr("tell", YchConstants.YCH_TELL);
			render("/ych/mdht/main");
		}
	}
	
	
	public void data(){
		Map<String, Object> result = getResultMap();
		try {
			//获取请求次数
			Integer draw = getParaToInt("draw");
			//数据起始位置
			Integer start = getParaToInt("start");
			
			if(start==0){
				start = 1;
			}
			
			//数据长度
			Integer lenght = getParaToInt("length");
			
			Page<Record> data = OrderModel.dao.getData(start, lenght, null);
			
			result.put("data", data.getList());
			result.put("draw", draw);
			result.put("recordsTotal", data.getTotalRow());
			result.put("recordsFiltered", data.getTotalRow());
			
		} catch (Exception e) {
			result.put(ERROR, "获取数据失败");
			e.printStackTrace();
			LOG.error("获取数据失败", e);
		}
		
		renderJson(result);
		
//		System.out.println("data--------------------------");
//		renderNull();
	}
	
	public void data1(){
		//数据起始位置
		Integer start = getParaToInt("offset");
		//数据长度
		Integer lenght = getParaToInt("limit");
		
		Integer uid = getParaToInt("id");
		
		//s_d
		String sd = getPara("s_d");
		//e_d
		String ed = getPara("e_d");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sd", sd);param.put("ed", ed);param.put("uid", uid);
		
		Page<Record> page = OrderModel.dao.getData1(start, lenght, param);
		
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		
		renderJson();
	}
	
	
	public static void main(String[] args) {
		String md5Encrypt = EncryptionKit.md5Encrypt("123");
		System.out.println(md5Encrypt);
	}
}
