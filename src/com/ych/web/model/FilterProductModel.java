package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "filter_product")
public class FilterProductModel extends Model<FilterProductModel> {

	private static final long serialVersionUID = -415709672898188077L;
	public static final FilterProductModel dao = new FilterProductModel();

	public FilterProductModel getFilterProduct(String lyID) {
		return this.findFirst(SqlXmlKit.getSql("FilterProduct.get"), lyID);
	}

}
