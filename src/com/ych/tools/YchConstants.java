package com.ych.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class YchConstants {
	private static final Prop PROP = PropKit.use("ych.properties");
	public static final String YCH_TELL = PROP.get("ych.tell");
	public static final String YCH_BASEURL = PROP.get("ych.baseUrl");
	public static final String YCH_TITLE = PROP.get("ych.title");
	public static final String YCH_DESC = PROP.get("ych.desc");
	public static final String YCH_PICURL = PROP.get("ych.picUrl");
	public static final String YCH_TARGETURL = PROP.get("ych.targetUrl");
	public static final String YCH_SERVERURL = PROP.get("ych.serverUrl");
	public static final String WXOAUTH2URL = PROP.get("wxOauth2Url");
}
