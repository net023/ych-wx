package com.ych.web.controller;

import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.NewsModel;

@Control(controllerKey = "/news")
public class NewsController extends BaseController {

	public void show() {
		Integer nID = getParaToInt("nID");
		NewsModel news = NewsModel.dao.findById(nID);
		setAttr("news", news);
		render("/ych/news");
	}
}
