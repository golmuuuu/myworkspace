package service;

import java.util.List;
import java.util.Map;

import dao.MembershipDAO;
import util.ScanUtil;
import util.View;

public class MembershipService {
	private static MembershipService instance = null;
	private MembershipService () {}
	public static MembershipService getInstance() {
		if(instance == null) instance = new MembershipService();
		return instance;
	}
	
	MembershipDAO dao = MembershipDAO.getInstance();
	
	// list보여주기 
	public int memlist() {
		System.out.println("-- 이용권 구매하기 --");
		listShow();
		System.out.println("--------------------------");
		System.out.println("1. 구매하기  2. 돌아가기");
		System.out.println("번호를 선택해주세요 >>");
		switch(ScanUtil.nextInt()) {
		case 1 : return View.REL_MEMSHIP_PAY;
		case 2 : return View.HOME;
		default : System.out.println("잘못 입력하셨습니다. 홈으로 돌아갑니다. ");
				  return View.HOME;	
		}
	}
	
	public void listShow() {
		List<Map<String, Object>> list = dao.list();
		
		for(Map<String, Object> item : list) {
			System.out.println("┌─────────────────────────┐");
			System.out.println(" " +item.get("ROWNUM")+ ". " + item.get("MEM_NAME"));
			System.out.println("\t\t   " + item.get("MEM_PRICE")+"원");
			System.out.println("└─────────────────────────┘\r\n");
		}
	}
	
	
}
