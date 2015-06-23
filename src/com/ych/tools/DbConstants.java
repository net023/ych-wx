package com.ych.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class DbConstants {
	private static final Prop PROP = PropKit.use(DevConstants.DB_CONFIG_FILENAME);
	public static final String JDBCURL = PROP.get("jdbcUrl");
	public static final String USER = PROP.get("user");
	public static final String PASSWORD = PROP.get("password");
	public static final Integer INITIALSIZE = PROP.getInt("initialSize");
	public static final Integer MINIDLE = PROP.getInt("minIdle");
	public static final Integer MAXACTIVE = PROP.getInt("maxActive");
	public static final String APPID = PROP.get("appId");
	public static final String APPSECRET = PROP.get("appSecret");
	public static final String TOKEN = PROP.get("token");
	public static final Boolean ENCRYPTMESSAGE = PROP.getBoolean("encryptMessage");
}
