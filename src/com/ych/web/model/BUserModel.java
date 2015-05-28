package com.ych.web.model;

import com.jfinal.kit.EncryptionKit;
import com.jfinal.plugin.activerecord.Model;
import com.ych.core.plugin.annotation.Table;

@Table(tableName = "buser")
public class BUserModel extends Model<BUserModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2500346355554845512L;
	public static final BUserModel dao = new BUserModel();
	
	public BUserModel login(String username, String password) {
		return this.findFirst("select * from buser where status=0 and username=? and password=?", username, EncryptionKit.md5Encrypt(password));
	}

}
