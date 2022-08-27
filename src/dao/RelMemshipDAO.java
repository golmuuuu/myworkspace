package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class RelMemshipDAO {
	private static RelMemshipDAO instance = null;
	private RelMemshipDAO () {}
	public static RelMemshipDAO getInstance() {
		if(instance == null) instance = new RelMemshipDAO();
		return instance;
	}
	
JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int payMemship(List<Object> payinfo) {
		return jdbc.update(" INSERT INTO REL_MEMSHIP"
				+ " (REL_NO, MEM_ID, CLIENT_ID, REL_PRICE,"
				+ " REL_STARTDATE, REL_EXPDATE) "
				+ " VALUES (SEQ_NUMBER.NEXTVAL, ?, ?, ?, SYSDATE , ADD_MONTHS(SYSDATE, ?) )", payinfo);
	}
	
	// (최근) 내 이용권 정보 가져오기 
	public Map<String, Object> myMeminfo(String id){
		return jdbc.selectOne("SELECT * " 
							+ "  FROM (SELECT * FROM REL_MEMSHIP "
							+ "  		WHERE CLIENT_ID ='" + id + "'" 
							+ "         ORDER BY REL_EXPDATE DESC) " 
							+ " WHERE ROWNUM =1 ");
	}
	
	public int cancel(int num) {
		return jdbc.update(" DELETE FROM REL_MEMSHIP "
				+ " WHERE REL_NO = " + num);
	}
}
