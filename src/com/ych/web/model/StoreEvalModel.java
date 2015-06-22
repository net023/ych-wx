package com.ych.web.model;

import java.util.Date;

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

	public boolean commitEval(Integer uID, Integer sID, String evaluate,
			Integer oID, Integer grade) {
		StoreEvalModel model = this
				.findFirst(
						"select * from store_eval where `user` = ? and store = ? and o_id = ?",
						uID, sID, oID);
		if (model != null)
			return true;
		StoreEvalModel eval = new StoreEvalModel();
		eval.set("user", uID);
		eval.set("store", sID);
		eval.set("evaluate", evaluate);
		eval.set("o_id", oID);
		eval.set("grade", grade);
		eval.set("c_time", new Date());
		return eval.save();
	}
}
