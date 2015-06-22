package com.ych.web.model;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "filter_price")
public class FilterPriceModel extends Model<FilterPriceModel> {

	private static final long serialVersionUID = -1380894333880459455L;
	public static final FilterPriceModel dao = new FilterPriceModel();

	public List<FilterPriceModel> getFilterPrice(Integer bID, Integer tID,
			String num, BigDecimal price) {
		return this.find(SqlXmlKit.getSql("FilterPrice.recommendAll"), bID,
				tID, num, price);
	}

}
