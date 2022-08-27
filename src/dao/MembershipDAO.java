package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MembershipDAO {
	private static MembershipDAO instance = null;
	private MembershipDAO () {}
	public static MembershipDAO getInstance() {
		if(instance == null) instance = new MembershipDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> list() {
		return jdbc.selectList(" SELECT ROWNUM, MEM_NAME, MEM_PRICE, MEM_ID, MEM_PERIOD "
				+ " FROM MEMBERSHIP ORDER BY MEM_PRICE ");
	}
	
	
	
	
}
