package com.ych.web.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.core.kit.SqlXmlKit;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "order")
public class OrderModel extends Model<OrderModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2500346355554845512L;
	public static final OrderModel dao = new OrderModel();

	public Page<Record> getData(Integer start, Integer pageSize,
			Map<String, Object> params) {
		Integer pageNum = start / pageSize;
		if (pageNum == 0) {
			pageNum = 1;
		}
		// LinkedList<Object> param = new LinkedList<Object>();
		return Db.paginate(pageNum, pageSize, "select *",
				SqlXmlKit.getSql("Order.pager"));
		// return Db.paginate(pageNum, pageSize, "select *",
		// SqlXmlKit.getSql("Order.pager"),param.toArray());
		// return Db.paginate(pageNum, pageSize, "select *",
		// SqlXmlKit.getSql("Order.pager",params, param),param.toArray());
	}

	public Page<Record> getData1(Integer start, Integer pageSize,
			Map<String, Object> params) {
		LinkedList<Object> param = new LinkedList<Object>();
		Integer pageNum = start / pageSize + 1;
		// return Db.paginate(pageNum, pageSize, "select *",
		// SqlXmlKit.getSql("Order.pager", params));
		return Db
				.paginate(pageNum, pageSize, "select *",
						SqlXmlKit.getSql("Order.pager", params, param),
						param.toArray());
	}

	public int makeOrder(Integer uID, Integer msID, Integer sID, Date resTime,
			Double totalPrice) {
		OrderModel order = new OrderModel();
		order.set("c_time", new Date());
		order.set("res_time", resTime);
		order.set("b_id", sID);
		order.set("ms_id", msID);
		order.set("price", totalPrice);
		order.set("wx_id", uID);
		order.set("status", 0);
		order.save();
		return order.getInt("id");
	}

	public Page<OrderModel> getOrders(Integer pageNumber, Integer pageSize,
			Integer uID) {
		return this.paginate(pageNumber, pageSize, "SELECT *",
				SqlXmlKit.getSql("Order.selfOrderPager"), uID);
	}

	public OrderModel getOrder(Integer oID) {
		return this.findFirst(SqlXmlKit.getSql("Order.order"), oID);
	}
}
