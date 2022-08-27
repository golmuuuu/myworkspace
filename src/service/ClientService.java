package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.MusicController;
import dao.ClientDAO;
import dao.RelMemshipDAO;
import util.DateUtil;
import util.ScanUtil;
import util.View;

public class ClientService {
	
	private static ClientService instance = null;
	private ClientService() {}
	public static ClientService getInstnace() {
		if(instance == null) instance = new ClientService(); 
		return instance;
	}
	
	ClientDAO dao = ClientDAO.getInstance();
	RelMemshipDAO relDao = RelMemshipDAO.getInstance();
	
	public int login() {
		System.out.println("----로그인 ----");
		System.out.println("아이디 입력");
		String id = ScanUtil.nextLine();
		System.out.println("비밀번호 입력");
		String pwd = ScanUtil.nextLine();
		Map<String, Object> row = dao.login(id, pwd);
		if(row == null) {
			System.out.println("존재하지 않는 회원입니다");
			return View.CLIENT_LOGIN;
		}else{
			System.out.println("로그인 되었습니다.");
			MusicController.login_num = 1;
			MusicController.login_id = id;
			MusicController.login_pwd = pwd;
			paycheckUpdate();
			
			return View.CLIENT_HOME;
			
		}
	}
	
	// 로그인 하면 결제유무 갱신 메소드 
	private void paycheckUpdate() {
		Map<String, Object> myinfo = relDao.myMeminfo(MusicController.login_id);
		if(DateUtil.toDate(myinfo.get("REL_EXPDATE")).before(new Date())) {
			List<Object> param = new ArrayList<>();
			param.add(MusicController.login_id);
			param.add("N");
			int result = dao.paycheck(param);
			if(result >0) System.out.println("결제유무갱신됨 !");
		}else {
			System.out.println("결제유무 그대로 ");
		}
	}
	
	
	public int client_home() {
		System.out.println("---- 회원 홈 ----");
		System.out.println("1.마이페이지 2.노래차트 3.이용권구매 4.고객센터 5.로그아웃 0.종료 ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.CLIENT_MYPAGE;
		case 3: return View.MEMBERSHIP_LIST;
		default : return View.CLIENT_HOME;
		}
	}
	
	public int mypage() {
		System.out.println("---- 마이페이지 ----");
		System.out.println("1.내정보관리 2.이용권관리 3.내플레이리스트 4.멜론라운지 0.종료 ");
		System.out.println("번호를 입력해주세요 ");
		switch(ScanUtil.nextInt()) {
		case 2: return View.REL_MEMBSHIP_SETTING;
		default : return View.CLIENT_HOME;
		}
	}
	

}
