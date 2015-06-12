package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "car")
public class CarModel extends Model<CarModel> {

	private static final long serialVersionUID = 3993359256205848380L;
	public static final CarModel dao = new CarModel();

	public List<CarModel> getCars(Integer tID) {
		return this.find("select id, `name` from car where t_id = ?", tID);
	}

}
