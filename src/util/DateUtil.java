package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	Date today = new Date();
	
	public static String dateformat(Object obj){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		return sdf.format(obj);
	}
	
	public static String dateformat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		return sdf.format(date);
		
	}

	
	public static Date toDate(Object day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String sdate = sdf.format(day);
		Date dateformat = null;
		try {
			dateformat = sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateformat;
	}
}
