package com.ych.web.controller;

import com.jfinal.log.Logger;
import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;

@Control(controllerKey = "/ych")
public class YchWxController extends BaseController {

	private static final Logger LOG = Logger.getLogger(YchWxController.class);

	/**
	 * 保养预约
	 */
	public void yyby() {
		try {

		} catch (Exception e) {
			LOG.error("失败", e);
		}
		render("/ych/index");
	}
}
