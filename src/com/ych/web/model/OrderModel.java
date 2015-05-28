package com.ych.web.model;

import java.util.LinkedList;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.ych.core.kit.SqlXmlKit;

public class OrderModel extends Model<OrderModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2500346355554845512L;
	public static final OrderModel dao = new OrderModel();
	
	public Page<Record> getData(Integer start,Integer pageSize,Map<String, Object> params){
		Integer pageNum = start/pageSize;
		if(pageNum==0){
			pageNum = 1;
		}
//		LinkedList<Object> param = new LinkedList<Object>();
		return Db.paginate(pageNum, pageSize, "select *", SqlXmlKit.getSql("Order.pager"));
//		return Db.paginate(pageNum, pageSize, "select *", SqlXmlKit.getSql("Order.pager"),param.toArray());
//		return Db.paginate(pageNum, pageSize, "select *", SqlXmlKit.getSql("Order.pager",params, param),param.toArray());
	}
	
	public Page<Record> getData1(Integer start,Integer pageSize,Map<String, Object> params){
		LinkedList<Object> param = new LinkedList<Object>();
		Integer pageNum = start/pageSize+1;
//		return Db.paginate(pageNum, pageSize, "select *", SqlXmlKit.getSql("Order.pager", params));
		return Db.paginate(pageNum, pageSize, "select *", SqlXmlKit.getSql("Order.pager", params,param),param.toArray());
	}
}
