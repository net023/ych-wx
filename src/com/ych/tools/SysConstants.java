package com.ych.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class SysConstants {

	private static final Prop prop = PropKit.use(DevConstants.SYS_CONFIG_FILENAME);


	/**
	 * DEBUG模式
	 */
	public static final boolean DEBUG = false;

	/**
	 * 是否压缩HTML代码
	 */
	public static final boolean HTML_IS_COMPRESS = true;

	/**
	 * session user
	 */
	public static final String SESSION_USER = "bSessionUser";

	/**
	 * 视图默认路径
	 */
	public static final String VIEW_BASE_PATH = "biz-logic";



	/**
	 * 图片Dir 图片文件的存放地址
	 */
	public static final String IMG_DIR = prop.get("imgDir");


	/**
	 * 文件上传大小限制 默认200M
	 */
	public static final int MAX_POST_SIZE = prop.getInt("maxPostSize", 500 * 1024 * 1024);

	
	
}
