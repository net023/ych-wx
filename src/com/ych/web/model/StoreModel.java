package com.ych.web.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "store")
public class StoreModel extends Model<StoreModel> {

	private static final long serialVersionUID = -7450713695232379463L;
	public static final StoreModel dao = new StoreModel();

	public Page<StoreModel> getPager(Integer start, Integer pageSize,
			Map<String, Object> params) {
		LinkedList<Object> param = new LinkedList<Object>();
		Integer pageNumber = start / pageSize;
		if (pageNumber == 0) {
			pageNumber = 1;
		}

		String sql = SqlXmlKit.getSql("Store.pager", params, param);
		return this.paginate(pageNumber, pageSize, "select *", sql,
				param.toArray());
	}

	public List<StoreModel> getStores() {
		return this.find("select * from store");
	}

	public StoreModel getStore(Integer sID, Float lat, Float lon) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", sID);
		params.put("lat", lat);
		params.put("lon", lon);
		LinkedList<Object> param = new LinkedList<Object>();
		return this.findFirst(SqlXmlKit.getSql("Store.store", params, param),
				param.toArray());
	}

}
