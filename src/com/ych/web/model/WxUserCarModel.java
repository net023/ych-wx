package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "wxuser_car")
public class WxUserCarModel extends Model<WxUserCarModel> {

	private static final long serialVersionUID = 8430289867070866886L;
	public static final WxUserCarModel dao = new WxUserCarModel();

	public WxUserCarModel getCar(Integer uID) {
		return this
				.findFirst(
						"select c_id as id, car.name, ly_id from (select c_id from wxuser_car where wxuser_car.u_id = ?) t left join car on t.c_id = car.id",
						uID);
	}

	public WxUserCarModel getWxUserCarModel(Integer uID) {
		return this.findFirst(
				"select id, u_id, c_id from wxuser_car where u_id = ?", uID);
	}

}
