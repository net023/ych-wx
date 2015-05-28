package com.ych.common;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.beetl.core.Template;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.ych.tools.SysConstants;
import com.ych.web.model.BUserModel;

public class BaseController extends Controller {

	// private static final Logger LOG = Logger.getLogger(BaseController.class);
	protected final String DATA = "d";
	protected final String ERROR = "e";
	protected final String RESULT = "r";
	protected final String MESSAGE = "m";
	private int defaultPageSize = 40;

	/**
	 * 创建分页对象
	 * 
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public Pager createPager(int pageSize, int pageNo) {
		Pager pager = null;
		if (pageSize <= 0) {
			pageSize = defaultPageSize;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		pager = new Pager(pageSize, pageNo);
		return pager;
	}
	
	public BUserModel getUser() {
		return (BUserModel) getSession().getAttribute(SysConstants.SESSION_USER);
	}

	/**
	 * 创建分页对象
	 * 
	 * @param request
	 * @return
	 */
	public Pager createPager() {
		Pager pager = null;
		String pageSize = getPara("rows");
		String pageNo = getPara("page");
		if (StrKit.isBlank(pageSize)) {
			pageSize = String.valueOf(defaultPageSize);
		}
		if (StrKit.isBlank(pageNo)) {
			pageNo = "1";
		}
		pager = new Pager(Integer.valueOf(pageSize), Integer.valueOf(pageNo));
		return pager;
	}

	/**
	 * 创建查询对象
	 * 
	 * @param paramNames
	 *            需要从Request的参数集合
	 * @return
	 */
	public Pager createPager(String... paramNames) {
		Pager pager = createPager();
		String param = null;
		for (String paramName : paramNames) {
			param = getPara(paramName);
			if (!StringUtils.isEmpty(param)) {
				pager.addParam(paramName, param);
			}
		}

		return pager;
	}

	/**
	 * 获取result Map对象
	 * 
	 * @return
	 */
	public Map<String, Object> getResultMap() {
		return new HashMap<String, Object>();
	}

	/**
	 * 生成表格数据格式
	 * 
	 * @param pager
	 * @return
	 */
	public Map<String, Object> getGridData(Pager pager) {
		Map<String, Object> result = getResultMap();
		result.put("total", pager.getTotalRecord());
		result.put("rows", pager.getResult());
		return result;
	}

	/**
	 * 获取用户真实ip地址 request.getRemoteAddr(); nginx做跳转不能获取
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

	// public BasePage createPage(){
	// String pageSize = getPara("rows");
	// String pageNo = getPara("page");
	// if (StrKit.isBlank(pageSize)) {
	// pageSize = String.valueOf(defaultPageSize);
	// }
	// if (StrKit.isBlank(pageNo)) {
	// pageNo = "1";
	// }
	// return new BasePage(Integer.valueOf(pageSize), Integer.valueOf(pageNo));
	// };
	// public BasePage createPage(String... params){
	// return createPage().addParamsNotBlank(getRequest(), params);
	// };
	//
	// public void setPage(BasePage page){
	// setAttr("total", page.getTotalRow());
	// setAttr("rows", page.getList());
	// if(page.getTotal()!=null){
	// setAttr("sum", page.getTotal());
	// }
	// }
	//
	public void setMessage(String successMsg, String failMsg) {
		setAttr("sm", successMsg);
		setAttr("fm", failMsg);
	}

	public void renderExcel(String template) {

		renderExcel(template, template);

	}

	public void renderExcel(String template, String fileName) {

		// FileResourceLoader resourceLoader = new
		// FileResourceLoader(PathKit.getWebRootPath(), "UTF-8");
		// Configuration cfg = null;
		// try {
		// cfg = Configuration.defaultConfiguration();
		// } catch (IOException e1) {
		// }
		// GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = BeetlRenderFactory.groupTemplate.getTemplate("template/" + template + ".html");

		HttpServletRequest request = getRequest();
		Enumeration<String> attrs = request.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String attrName = attrs.nextElement();
			t.binding(attrName, request.getAttribute(attrName));
		}

		try {
			HttpServletResponse response = getResponse();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
			response.setContentType("application/msexcel");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			t.renderTo(writer);
			writer.close();
			response.flushBuffer();
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
			renderError(404);
		}

	}

}
