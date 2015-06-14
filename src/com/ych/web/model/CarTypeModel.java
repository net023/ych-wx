package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "car_type")
public class CarTypeModel extends Model<CarTypeModel> {

	private static final long serialVersionUID = 7116190418955684882L;
	public static CarTypeModel dao = new CarTypeModel();
	
	public List<CarTypeModel> getTypes(Integer sID) {
		return this.find("select id, name, s_id from car_type where s_id = ? and status = 0", sID);
	}

}
