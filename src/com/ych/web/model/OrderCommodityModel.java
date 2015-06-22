package com.ych.web.model;

import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "order_commodity")
public class OrderCommodityModel extends Model<OrderCommodityModel> {

	private static final long serialVersionUID = 5679555348852080042L;
	public static final OrderCommodityModel dao = new OrderCommodityModel();

	public void saveBatch(Integer oID, String... infos) {
		OrderCommodityModel orderCommodity = null;
		for (int i = 0; i < infos.length; i++) {
			String info = infos[i];
			if (info != null) {
				String[] contents = info.split("-");
				switch (contents.length) {
				case 1:
					orderCommodity = new OrderCommodityModel();
					orderCommodity.set("o_id", oID);
					orderCommodity.set("type", contents[0]);
					orderCommodity.save();
					break;
				case 3:
					orderCommodity = new OrderCommodityModel();
					orderCommodity.set("o_id", oID);
					orderCommodity.set("type", contents[0]);
					orderCommodity.set("c_id", contents[1]);
					orderCommodity.set("num", contents[2]);
					orderCommodity.save();
					break;
				default:
					break;
				}
			}
		}
	}

}
