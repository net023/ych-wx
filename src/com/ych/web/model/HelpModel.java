package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "news")
public class HelpModel extends Model<HelpModel> {

	private static final long serialVersionUID = -9211618829637536633L;
	public static final HelpModel dao = new HelpModel();

	public List<HelpModel> getHots() {
		String sql = "SELECT n_title AS header, n_content AS content FROM news WHERE n_type = 2 AND `status` = 0";
		return this.find(sql);
	}
}
