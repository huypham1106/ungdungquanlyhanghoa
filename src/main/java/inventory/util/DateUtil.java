package inventory.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// hàm convert từ kiểu date sang kiểu string
public class DateUtil {

	public static String dateToString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(date);
	}
}
