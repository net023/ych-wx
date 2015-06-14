package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "mtser_unit_cost")
public class MtserUnitCostModel extends Model<MtserUnitCostModel> {

	private static final long serialVersionUID = -5903013364587435841L;
	public static final MtserUnitCostModel dao = new MtserUnitCostModel();

	public MtserUnitCostModel getMtserUnitCost(Integer mID) {
		return this
				.findFirst(
						"select id, ms_id, unit_cost as cost from mtser_unit_cost where ms_id = ?",
						mID);
	}

}
