package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "car_brand")
public class CarBrandModel extends Model<CarBrandModel> {

	private static final long serialVersionUID = -8891905195538539874L;
	public static final CarBrandModel dao = new CarBrandModel();
	
	public List<CarBrandModel> getBrands() {
		return this.find("select group_concat(id) as ids, group_concat(`name`) as `names`, sort as letter from car_brand where status = 0 group by sort order by sort");
	}

}
