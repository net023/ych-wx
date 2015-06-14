package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "news")
public class NewsModel extends Model<NewsModel> {

	private static final long serialVersionUID = 1170961349785036646L;
	public static final NewsModel dao = new NewsModel();
	
}
