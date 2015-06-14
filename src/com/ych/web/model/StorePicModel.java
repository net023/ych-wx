package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "store_pic")
public class StorePicModel extends Model<StorePicModel> {

	private static final long serialVersionUID = -3681793186435338967L;
	public static final StorePicModel dao = new StorePicModel();

	public List<StorePicModel> getStorePics(Integer sID) {
		return this.find("select * from store_pic where s_id = ?", sID);
	}
}
