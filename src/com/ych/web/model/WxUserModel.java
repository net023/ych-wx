package com.ych.web.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "wxuser")
public class WxUserModel extends Model<WxUserModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2500346355554845512L;
	public static final WxUserModel dao = new WxUserModel();
	
	public int delByOpenId(String openid){
		String sql = "delete from wxuser where openid = ?";
		return Db.update(sql, openid);
	}

}
