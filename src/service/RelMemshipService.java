package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.MusicController;
import dao.ClientDAO;
import dao.MembershipDAO;
import dao.RelMemshipDAO;
import util.DateUtil;
import util.ScanUtil;
import util.View;

public class RelMemshipService {
	private static RelMemshipService instance = null;
	private RelMemshipService () {}
	public static RelMemshipService getInstance() {
		if(instance == null) instance = new RelMemshipService();
		return instance;
	}
	
	MembershipService memService = MembershipService.getInstance();
	MembershipDAO dao = MembershipDAO.getInstance();
	RelMemshipDAO relDao = RelMemshipDAO.getInstance();
	ClientDAO cliDao = ClientDAO.getInstance();
	
	Calendar cal = Calendar.getInstance();
	
	
	// 이용권 구매하기 
	public int myMemPay() {
		Map<String, Object> myinfo = cliDao.myinfo(MusicController.login_id, MusicController.login_pwd);
		System.out.println("구매할 이용권의 번호를 입력해 주세요! >>");
		int num = ScanUtil.nextInt();
		
		
		//로그인 되어있는지 확인하는 메소드 
		if(MusicController.login_num == 0) {
			System.out.println("로그인이 필요합니다.");
			System.out.print("로그인 창으로 넘어갑니다");
			try {
				for (int i = 0; i < 3; i++) {
					Thread.sleep(1000);
					System.out.print(".");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
			return View.CLIENT_LOGIN;
			
		// 로그인 되어있고 결제가 되어있으면 
		}else if(MusicController.login_num == 1 
				&& ( myinfo.get("CLI_PAYCHECK").equals("Y") 
						|| myinfo.get("CLI_PAYCHECK").equals("y"))) {
			System.out.println("유효한 이용권이 존재합니다 ! ");
			System.out.println("홈으로 돌아갑니다. ");
			
			return View.CLIENT_HOME;
		
		// 로그인 되어있고 결제 되어있지 않으면 	
		}else if(MusicController.login_num == 1 
				&& (!myinfo.get("CLI_PAYCHECK").equals("Y") 
					|| !myinfo.get("CLI_PAYCHECK").equals("y"))){
			
			//비밀번호 확인 메소드 
			boolean check = pwdcheck();

			// true 이면 결제 가능 
			if(check == false) {
				System.out.println("1분 후에 다시 시도해주세요.");
				System.out.println("홈으로 돌아갑니다.");
				//비번 틀리면 홈으로 돌아감 
				return View.CLIENT_HOME;
			}else {
				relPayUpdate(num);
			}
		}
		return View.CLIENT_HOME;
	}
	
	// 결제하면 구매한테이블 정보데이터 삽입
	public boolean relPayUpdate(int num) {
		List<Map<String, Object>> list = dao.list();
		// 결제하면 구매한 멤버쉽에 들어갈 내용 
		List<Object> payinfo = new ArrayList<>();
//			payinfo.add(list.get(num-1).get("MEM_ID")); // 결제 넘버  
		payinfo.add(list.get(num-1).get("MEM_ID")); // 멤버쉽 코드 
		payinfo.add(MusicController.login_id); // 고객아이디 
		payinfo.add(list.get(num-1).get("MEM_PRICE")); //금액 
		payinfo.add(list.get(num-1).get("MEM_PERIOD")); // 멤버쉽 기간개월수 
		
//			System.out.println(payinfo.toString()); // 결제 정보 출력 
//			System.out.println(Integer.parseInt(payinfo.get(3).toString())); // 결제 개월수 출력 
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, Integer.parseInt(payinfo.get(3).toString())); // 다음 날짜일 계산 
		
		//결제정보 rel_memship db에 넘기기(update)
		int result = relDao.payMemship(payinfo);
		System.out.println(result);
		List<Object> paycheck = new ArrayList<>();
		paycheck.add("Y");
		paycheck.add(MusicController.login_id);
		int cliResult = cliDao.paycheck(paycheck);
		System.out.println(cliResult + "Y" + MusicController.login_id);
		if(result > 0 && cliResult > 0) {
			System.out.println("결제가 완료되었습니다 ! ");
			System.out.println("다음 결제일은" + DateUtil.dateformat(cal.getTime()) + "입니다." );
			return true;
		}else {
			System.out.println("결제실패 ");
			return false;
		}
	}

	// 비밀번호 확인 메소드 
	private boolean pwdcheck() {
		boolean check = false;
		pwdcheck :
		while(!check) {
			System.out.println("결제를 진행하기 위해서 비밀번호를 입력해주세요 >>");
			System.out.println(MusicController.login_pwd);
			for(int i = 0 ; i < 3 ; i++) {
				if(!ScanUtil.nextLine().equals(MusicController.login_pwd)) {
					System.out.println(MusicController.login_pwd);
					System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요 "
							+ "(" + (i+1) + "/3)");
					check = false;
				}else {
					System.out.println("(확인용)결제가 완료되었습니다. !");
					check = true;
					break pwdcheck;
				}
			}
		}
		return check;
	}
		
	// 내 이용권 관리 	
	public int myMemshiptSet() {
		System.out.println("--- 내 이용권 관리 --- ");
		System.out.println("1.이용권 보유 현황 2.이용권 해지 3.이용권 종류 변경 0.돌아가기");
		System.out.println("번호를 입력해주세요 >>");
		switch(ScanUtil.nextInt()) {
		case 1: return View.REL_MEMBSHIP_MY;
		case 2: return View.REL_MEMBSHIP_CANCEL;
		case 3: return View.REL_MEMBSHIP_CHANGE;
		case 0: return View.CLIENT_MYPAGE;
		default : 
			System.out.println("번호를 다시 입력해주세요 >>");
			return View.REL_MEMBSHIP_SETTING;
		}
			
	}
	
	// 이용권 보유 현황 
	public int myMemShow() {
		
		System.out.println("--------- 내 이용권 ----------");
		Map<String, Object> myinfo = cliDao.myinfo(MusicController.login_id, MusicController.login_pwd);
		if(myinfo.get("CLI_PAYCHECK").equals("Y")) {
			Map<String, Object> myMem = relDao.myMeminfo(MusicController.login_id);
			System.out.println(myMem.get("MEM_ID"));
			System.out.println("\t만료일 : " + DateUtil.dateformat(myMem.get("REL_EXPDATE")));
			System.out.println("\t가격 : " + myMem.get("REL_PRICE")+ "원");	
		}else {
			System.out.println("구매하신 이용권이 없습니다");
		}
		System.out.println("----------------------------");
		System.out.println("돌아가려면 아무키나 눌러주세요 ! ");
		ScanUtil.nextLine();
		
		return View.REL_MEMBSHIP_SETTING;
		
	}
	
	// 이용권 해지하기  
	public int memCancel() {
		System.out.println("이용권을 해지하시겠습니까 ? (노래를 들을수가 없어요....)");
		System.out.println("해지하려면 y를 취소하려면 n를 눌러주세요 !");
		switch(ScanUtil.nextLine()) {
		case "y" : case "Y" : 
			System.out.println("해지 시작");
			
			// 지울 번호 가져오기 
			Map<String, Object> myinfo = relDao.myMeminfo(MusicController.login_id);
			int num = Integer.parseInt(myinfo.get("REL_NO").toString()); 
			// 지우고 결제유무 N로 바꾸기 
			int result = relDao.cancel(num);
			int result2 = cliDao.paycheck("N", MusicController.login_id);
			if(result > 0 && result2 > 0) {
				System.out.println("이용권 해지가 완료되었습니다.");
			}else {
				System.out.println("이용권 해지 실패 ! ");
			}
			return View.REL_MEMBSHIP_SETTING;
		case "n" : case "N" : 
			System.out.println("취소하셨습니다 ! 이전으로 돌아갑니다. ");
			return View.REL_MEMBSHIP_SETTING;
		default : 
			System.out.println("잘못 입력하였습니다");
			System.out.println("다시 입력해주세요 ! ");
			return View.REL_MEMBSHIP_CANCEL; 	
		}
	}
	
	// 이용권 변경하기
	public int memChange() {
		Map<String, Object> myinfo = cliDao.myinfo(MusicController.login_id, MusicController.login_pwd);
		Map<String, Object> myMem = relDao.myMeminfo(MusicController.login_id);
		if(myinfo.get("CLI_PAYCHECK").equals("Y")) {
			System.out.println("--------- 현재 이용권 ----------");
			System.out.println(myMem.get("MEM_ID"));
			System.out.println("\t만료일 : " + DateUtil.dateformat(myMem.get("REL_EXPDATE")));
			System.out.println("\t가격 : " + myMem.get("REL_PRICE")+ "원");	
			System.out.println("----------------------------");	
		}else {
			System.out.println("구매하신 이용권이 없습니다");
			System.out.println("이용권 구매 페이지로 이동합니다.");
			return View.REL_MEMSHIP_PAY;
		}
		memService.listShow();
		System.out.println("변경하실 이용권을 선택해주세요 ");
		// 현재 이용권 지우고 
		int num = Integer.parseInt(myinfo.get("REL_NO").toString()); 
		int result = relDao.cancel(num);
		int result2 = cliDao.paycheck("N", MusicController.login_id);
		// 새로운 이용권 넣기
		boolean result3 = relPayUpdate(ScanUtil.nextInt());
		if((result + result2) > 1 && result3) {
			System.out.println("이용권이 변경되었습니다 !");
			return View.REL_MEMBSHIP_SETTING;
		}else {
			System.out.println("이용권 변경 실패ㅜㅜ ");
			return View.REL_MEMBSHIP_SETTING;
		}
		
	}
	
}
