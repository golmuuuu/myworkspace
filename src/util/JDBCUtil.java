package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	/*
	 * JDBC를 좀 더 쉽고 편하게 사용하기 위한  Utility 클래스
	 * - singleton 개념으로 동시에 쓸수 없도록 할것 ! 메모리 누수가 생길수 있으므로
	 * 한명 쓰고 다쓰면 반환 다른사람 쓰고 반환 이런 식으로 !
	 * 
	 * Map<String, Object> selectOne(String sql) // 조건 없이 모두다 조회할때
	 * Map<String, Object> selectOne(String sql, List<Object> param) //sql문과 ?를 채워줄 list
	 * List<Map<String, Object>> selectList(String sql)
	 * List<Map<String, Object>> selectList(String sql, List<Object> param)
	 * int update(String sql)
	 * int uqdate(Stromg sql, List<Object> param)
	 * */
	
	// 싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴 
					// 싱글톤 패턴을 쓰려고 하면 밑에 모양을 기억해서 클래스 명만 달라짐 !!!!!!!!!! 외워놓자 !	
	//인스턴스를 보관할 변수
	private static JDBCUtil instance = null;
	
	private JDBCUtil() {} // JDBCUtil 객체를 만들 수 없게(인스턴스화 할 수 없게) private으로 제한함
	
	public static JDBCUtil getInstance() {
		if(instance == null) instance = new JDBCUtil();
		return instance;
	}
	
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "PROJECT_MUSIC";
	String password = "java";		
	SimpleDateFormat SDF = new SimpleDateFormat("MM-dd");		
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement ps = null;
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		// sql => "select * from member where mem_add1 like '%||?||%' ?"
		// sql => "select * from java_board where writer = ?"
		// sql => "select * from java_board where board_number > ?"
		List<Map<String, Object>> result = null;
		try{
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			for(int i = 0 ; i < param.size(); i++) {
				ps.setObject(i+1, param.get(i));
				
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				if(result == null) result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for(int i = 0; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	public List<Map<String, Object>> selectList(String sql){
		// sql => "select * from member"
		// sql => "select * from java_board"
		// sql => "select * from java_board where board_number > 10"
		List<Map<String, Object>> result = null;
		try{
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				if(result == null) result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for(int i = 1 ;  i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		
		return result;
	}
	
	public int update(String sql, List<Object> param) {
		// sql => "delete from java_board where board_number = ?" 
		// sql => "update java_board set title = '하하' where board_number = ?"
		// sql => "insert my_member(mem_id, mem_pass, mem_name) values (?, ?, ?)"
		
		int result = 0; 
		try {
			conn = DriverManager.getConnection(url, user,password);
			ps = conn.prepareStatement(sql);
			for(int i = 0 ; i < param.size() ; i++) {
				ps.setObject(i+1, param.get(i));
			}
			result = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)	try { rs.close(); } catch (Exception e) {}
			if(ps != null)  try { ps.close(); } catch (Exception e) {}
			if(conn != null) try { conn.close(); } catch (Exception e) {} 
		}
		
		return result;
	}
	public int update(String sql) { //?가 없는 sql where절이나 조건이 없는 sql을 의미하는 것 이아니다 !
		 // sql => "delete from java_board" 
		 // sql => "update java_board set title = '하하'"
		 // sql => "insert my_member(mem_id, mem_pass, mem_name) values ('admin', '1234', '홍길동')"
		 int result = 0; // 0 = 진행이 되지 않았다. 
		 try {
 			 conn = DriverManager.getConnection(url, user,password);
 			 ps = conn.prepareStatement(sql);
 			 result = ps.executeUpdate();
 		 }catch(SQLException e) {
			 e.printStackTrace();
		 }finally {
			 if(rs != null)	try { rs.close(); } catch (Exception e) {}
			 if(ps != null)  try { ps.close(); } catch (Exception e) {}
			 if(conn != null) try { conn.close(); } catch (Exception e) {} 
		 }
		
		return result;
	 }
	
	
	// 한줄만 알고 싶을때 쓰는 메소드 !
	// 만약에 sql문을 잘못 써서 값이 여러개가 도출되면
	// 계속 덮어씌워져서 마지막 값만 나온다 ! 
	
	public Map<String, Object> selectOne(String sql, List<Object> param){  // sql문에 ?가 있을때 paramdmf Tma
		// sql = "SELECT * FROM JAVA_BOARD WHERE BOARD_NUMBER=?" // ?가 1개일때
		// param => [1]

		// sql = "SELECT * FROM JAVA_BOARD WHERE WRITER= ? AND TITLE = ?" // ?가 여러개 일때
		// param => ["홍길동", "안녕"]
		Map<String, Object> row = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			for(int i = 0 ; i <param.size() ; i ++){  // list는 0 부터 시작
				ps.setObject(i+1, param.get(i));	// ps는 1부터 시작이므로 +1
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData(); // 컬럼과 값을 리턴할 것 ! 우리는 지금 컬럼명을 모르기 때문에 컬럼명을 알고 싶을때 메타데이터를 씀 !
			int columnCount = rsmd.getColumnCount();
			while(rs.next()){
				row = new HashMap<>();
				for(int i = 1 ; i<= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				//{key = value, key = value ,key = value key = value  } 이런식을 ㅗ들어감
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)	try { rs.close(); } catch (Exception e) {}
			if(ps != null)  try { ps.close(); } catch (Exception e) {}
			if(conn != null) try { conn.close(); } catch (Exception e) {} 
		}
		return row;
	}
	
	public Map<String, Object> selectOne(String sql){
		// sql = "SELECT * FROM JAVA_BOARD WHERE BOARD_NUMBER=(SELECT MAX(BOARD_NUMBER)
		//  FROM JAVA_BOARD)"
		
		// sql = "SELECT * FROM MEMBER MEM_ID='A001' AND MEM_PASS= '123'
		
		Map<String, Object> row = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData(); // 컬럼과 값을 리턴할 것 ! 우리는 지금 컬럼명을 모르기 때문에 컬럼명을 알고 싶을때 메타데이터를 씀 !
			int columnCount = rsmd.getColumnCount();
			while(rs.next()){ // 결과 테이블이 없으면 row 는 null 형태임
				row = new HashMap<>();
				for(int i = 1 ; i<= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					// getColumnName vs getColumnLabel
					// getColumnName : 원본 컬럼명을 가져옴
					// getColumnLabel : as 로 선언된 별명을 가져옴, 없으면 원본 컬럼명
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)	try { rs.close(); } catch (Exception e) {}
			if(ps != null)  try { ps.close(); } catch (Exception e) {}
			if(conn != null) try { conn.close(); } catch (Exception e) {} 
		}
		return row;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
