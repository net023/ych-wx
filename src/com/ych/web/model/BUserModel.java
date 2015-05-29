package com.ych.web.model;

import com.jfinal.kit.EncryptionKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "store")
public class BUserModel extends Model<BUserModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2500346355554845512L;
	public static final BUserModel dao = new BUserModel();
	
	public BUserModel login(String username, String password) {
		return this.findFirst("select * from store where status=0 and username=? and password=?", username, EncryptionKit.md5Encrypt(password));
	}
	
	public boolean changePassword(Integer uid,String password){
		String sql = "update store set password=? where id = ?";
		return Db.update(sql, EncryptionKit.md5Encrypt(password),uid)==1;
	}

}
