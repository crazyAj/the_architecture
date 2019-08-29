package cn.aj.commons.resource;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 自定义属性配置类,在SPRING的XML中配置加载所有的属性文件
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

    private static Properties properties;

    @Override
    public void setProperties(Properties properties) {
        PropertyConfigurer.properties = properties;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getProperty(String name) {
        return properties.getProperty(name, "");
    }

    public static String getProperty(String name,String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

}
