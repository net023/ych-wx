package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "store_eval")
public class StoreEvalModel extends Model<StoreEvalModel> {

	private static final long serialVersionUID = 558820340251518431L;
	public static final StoreEvalModel dao = new StoreEvalModel();

	public Page<StoreEvalModel> getEvalutates(Integer sID, Integer pageNumber,
			Integer pageSize) {
		return this.paginate(pageNumber, pageSize, "select *",
				SqlXmlKit.getSql("StoreEval.evalutates"), sID, sID);
	}

}
