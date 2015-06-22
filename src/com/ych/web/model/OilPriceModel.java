package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "oil_price")
public class OilPriceModel extends Model<OilPriceModel> {

	private static final long serialVersionUID = -7888384800154620266L;
	public static final OilPriceModel dao = new OilPriceModel();

	public List<OilPriceModel> getOilPrice(Integer eID, Integer type) {
		return this
				.find(SqlXmlKit.getSql("OilPrice.recommendedAll"), eID, type);
	}

	public OilPriceModel getOtherBrandOilPrice(Integer id, Integer brand) {
		return this.findFirst(SqlXmlKit.getSql("OilPrice.otherBrandOilPrice"),
				id, brand);
	}

}
