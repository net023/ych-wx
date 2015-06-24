package com.ych.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.weixin.sdk.api.AccessUserInfoByOAuth2;
import com.ych.tools.DbConstants;
import com.ych.tools.YchConstants;

public class SessionInterceptor implements Interceptor {
	
	private static final String WX_SER_URL = YchConstants.YCH_BASEURL;
	private static final String OAUTH2_URL = YchConstants.WXOAUTH2URL+"appid="+ DbConstants.APPID
			+ "&redirect_uri={0}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";

	@Override
	public void intercept(ActionInvocation arg) {
		// TODO Auto-generated method stub
		/*System.out.println("========sessionintercept==========start==");
		String actionKey = arg.getActionKey();
		System.out.println(actionKey);
		String controllerKey = arg.getControllerKey();
		System.out.println(controllerKey);
		String methodName = arg.getMethodName();
		System.out.println(methodName);
		String viewPath = arg.getViewPath();
		System.out.println(viewPath);
		System.out.println("========sessionintercept==========end==");*/
//		arg.getController().getSession(true);
		System.out.println(arg.getActionKey());
		Map<String, Object> user_info = arg.getController().getSessionAttr("user_info");
		String code = arg.getController().getPara("code");
		if(null!=user_info){
			arg.invoke();
		}else if(null!=code){
			Map<String, Object> infos = AccessUserInfoByOAuth2
					.getWxUserInfo(code);
			arg.getController().getSession().setAttribute("user_info", infos);
			arg.invoke();
		}else{
			String actionKey = arg.getActionKey();
			try {
				System.out.println("转发...........................");
				arg.getController().redirect(MessageFormat.format(OAUTH2_URL,
						URLEncoder.encode(WX_SER_URL + actionKey, "UTF-8")));
				return;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

}
