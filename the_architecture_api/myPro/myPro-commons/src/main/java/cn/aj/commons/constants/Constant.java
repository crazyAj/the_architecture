package cn.aj.commons.constants;

import cn.aj.commons.resource.PropertyConfigurer;

/**
 * 系统常量
 */
public class Constant
{
	/** 系统定义属性 */
	//系统域名
	public static final String APP_DOMAIN = PropertyConfigurer.getProperty("app.domain");
	//系统名称
	public static final String APP_NAME = PropertyConfigurer.getProperty("app.name");
	//系统版本
	public static final String APP_VERSION = PropertyConfigurer.getProperty("app.version");
	//配置路径
	public static final String CONFIG_PATH=PropertyConfigurer.getProperty("config.path");
	//日志路径
	public static final String LOG_PATH=PropertyConfigurer.getProperty("log.path");
	//模板路径
	public static final String TEMPLATE_PATH=PropertyConfigurer.getProperty("template.path");

	/** 业务参数 */

}
