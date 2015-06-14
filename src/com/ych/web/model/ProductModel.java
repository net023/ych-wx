package com.ych.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "oil_product")
public class ProductModel extends Model<ProductModel> {

	private static final long serialVersionUID = 5004605709771027848L;
	public static final ProductModel dao = new ProductModel();

	public List<ProductModel> getRecommends(String lyID, Integer mtser) {
		if (mtser == 1)
			return this.find(SqlXmlKit.getSql("Product.recommend"), lyID, lyID);
		return this.find(SqlXmlKit.getSql("Product.recommend1"), lyID, lyID);
	}

}
