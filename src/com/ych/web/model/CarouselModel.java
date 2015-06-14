package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "carousel")
public class CarouselModel extends Model<CarouselModel> {

	private static final long serialVersionUID = 2752235654049707967L;
	public static final CarouselModel dao = new CarouselModel();
	
	public List<CarouselModel> getTopThree() {
		return this.find("select f_id, n_id from carousel where status = 0 order by `order`");
	}

}
