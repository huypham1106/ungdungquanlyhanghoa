package inventory.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader // để đọc các file config dc lưu trong config.properties
{
	private Properties  properties=null;
	private static ConfigLoader instance = null;
	String proFileName = "config.properties";
	private ConfigLoader()
	{
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(proFileName);
		if(inputStream!=null)  // file đọc lên không bị null
		{
			properties = new Properties();
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		
	public static ConfigLoader getInstance() // trả về đối tượng config
	{
		if(instance == null)
		{
			//synchronized (ConfigLoader.class)// đa luồng {
				instance = new ConfigLoader(); // khởi tạo ra các đối tượng config ,đơn luồng
			//}
		}
		return instance;
	}
	
	public String getValue(String key)
	{
		if(properties.containsKey(key))
		{
			return properties.getProperty(key);
		}
		return null;
	}
	
}
