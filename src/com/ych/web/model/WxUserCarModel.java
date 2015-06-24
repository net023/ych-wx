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
						"SELECT c_id AS id, CONCAT(car_brand.`name`,' ',car. `name`) AS `name`, ly_id FROM ( SELECT c_id FROM wxuser_car WHERE wxuser_car.u_id =? ) t LEFT JOIN car ON t.c_id = car.id LEFT JOIN car_type ON car.t_id = car_type.id LEFT JOIN car_series ON car_type.s_id = car_series.id LEFT JOIN car_brand ON car_series.b_id = car_brand.id",
						uID);
	}

	public WxUserCarModel getWxUserCarModel(Integer uID) {
		return this.findFirst(
				"select id, u_id, c_id from wxuser_car where u_id = ?", uID);
	}

}
