package com.jfinal.weixin.core;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.beetl.ext.tag.TrimHtml;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.kit.MenuKit;
import com.ych.common.MyBeetlRenderFactory;
import com.ych.core.plugin.annotation.ControlPlugin;
import com.ych.core.plugin.annotation.TablePlugin;
import com.ych.core.plugin.sqlinxml.SqlXmlPlugin;
import com.ych.tools.DevConstants;
import com.ych.web.interceptor.SessionInterceptor;

public class WeixinConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile(DevConstants.DB_CONFIG_FILENAME);
		me.setMainRenderFactory(new MyBeetlRenderFactory());
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;
		groupTemplate.registerTag("compress", TrimHtml.class);
		me.setDevMode(DevConstants.DEV_MODE);
//		me.setBaseViewPath("");
//		ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(DevConstants.DEV_MODE);
		// 创建自定义菜单
		new MenuApi().createMenu(MenuKit.createYchMenu());
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/msg", WeixinMsgController.class);
		me.add("/api", WeixinApiController.class, "/api");
		// 自动扫描Controller
		new ControlPlugin(me).start();
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置druid数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		druidPlugin.setInitialSize(getPropertyToInt("initialSize", 10));
		druidPlugin.setMinIdle(getPropertyToInt("minIdle", 10));
		druidPlugin.setMaxActive(getPropertyToInt("maxActive", 100));
		me.add(druidPlugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(true);
		me.add(arp);

		// 自动扫描bean和数据库表映射
		new TablePlugin(arp).start();

		// 配置定时器
		//me.add(new QuartzPlugin());

		// 加载sqlxml
		me.add(new SqlXmlPlugin());

		// 配置缓存
		me.add(new EhCachePlugin());

	}

	@Override
	public void configInterceptor(Interceptors me) {
//		me.add(new SessionInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {

	}

	public static void main(String[] args) {
		JFinal.start("webapp", 8090, "/", 5);
	}
}
