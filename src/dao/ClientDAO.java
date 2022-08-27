package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ClientDAO {

	private int hello = 0;
	private static ClientDAO instance = null;
	private ClientDAO() {}
	public static ClientDAO getInstance() {
		if(instance == null) instance = new ClientDAO();
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();

	// 로그인 DAO메소드
	public Map<String, Object> login(String id, String pwd) {
		return jdbc.selectOne(" SELECT * FROM CLIENT "
				+ " WHERE CLI_ID = '" + id
				+ "' AND CLI_PW ='" + pwd + "'");
	}

	//결제 유뮤UPDATE DAO메소드
	public int paycheck(String paycheck, String id) {
		return jdbc.update(" UPDATE CLIENT SET "
				+ " CLI_PAYCHECK = '" + paycheck + "' "
				+ " WHERE CLI_ID = '" + id + "' " );
	}

	//결제 유뮤UPDATE DAO메소드
	public int paycheck(List<Object> param) {
		return jdbc.update("UPDATE CLIENT SET CLI_PAYCHECK = ? WHERE CLI_ID = ? " , param);
	}

	// 회원 정보 가져오는 DAO메소드
	public Map<String, Object> myinfo(String id, String pwd){
		return jdbc.selectOne(" SELECT * FROM CLIENT "
				+ " WHERE CLI_ID = '" + id
				+ "' AND CLI_PW ='" + pwd + "'");
	}



}
