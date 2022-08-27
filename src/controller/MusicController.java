package controller;

import service.ClientService;
import service.MembershipService;
import service.RelMemshipService;
import util.ScanUtil;
import util.View;

public class MusicController {
	
	public static int login_num;
	public static String login_id;
	public static String login_pwd;
	
	MembershipService membershipService = MembershipService.getInstance();
	ClientService clientService = ClientService.getInstnace();
	RelMemshipService relMemshipService = RelMemshipService.getInstance();
	
	public static void main(String[] args) {
		new MusicController().start();
	}

	private void start() {
		int view = View.HOME;
		while(true) {
			switch (view) {
			// 메인
			case View.HOME: view = home(); break;
			
			//고객 
			case View.CLIENT_LOGIN: view = clientService.login(); break;
			case View.CLIENT_HOME: view = clientService.client_home(); break;
			case View.CLIENT_MYPAGE: view = clientService.mypage(); break;
			
			
			
			
			// 이용권
			case View.MEMBERSHIP_LIST: view = membershipService.memlist(); break;
			
			// 구매 이용권 
			case View.REL_MEMBSHIP_SETTING: view = relMemshipService.myMemshiptSet(); break;
			case View.REL_MEMBSHIP_MY : view = relMemshipService.myMemShow(); break;
			case View.REL_MEMSHIP_PAY: view = relMemshipService.myMemPay(); break;
			case View.REL_MEMBSHIP_CANCEL: view = relMemshipService.memCancel(); break;
			case View.REL_MEMBSHIP_CHANGE: view = relMemshipService.memChange(); break;
			}
			
		}
	}

	private int home() {
		System.out.println("---Main---");
		System.out.println("1.로그인 2.노래차트 3.이용권 구매 \n4.고객센터 5.회원가입 6.자유게시판\n0.종료");
		System.out.print("번호를 입력해주세요! >");
		switch(ScanUtil.nextInt()) {
		case 1 : return View.CLIENT_LOGIN;
		case 3 : return View.MEMBERSHIP_LIST;
		default : return View.HOME;
		}
		
	}
}
