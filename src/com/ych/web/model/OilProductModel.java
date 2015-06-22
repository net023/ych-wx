package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "oil_product")
public class OilProductModel extends Model<OilProductModel> {

	private static final long serialVersionUID = -6602810270402918205L;
	public static final OilProductModel dao = new OilProductModel();

	public OilProductModel getOilProduct(String lyID) {
		return this
				.findFirst(
						"select ly_id, e_id, fill, type from oil_product where ly_id = ?",
						lyID);
	}

}
