package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "car_series")
public class CarSeriesModel extends Model<CarSeriesModel> {

	private static final long serialVersionUID = 2398384488561056081L;
	public static final CarSeriesModel dao = new CarSeriesModel();

	public List<CarSeriesModel> getSeries(Integer bID) {
		return this.find(SqlXmlKit.getSql("CarSeries.seriesWithTypes"), bID);
	}

}
