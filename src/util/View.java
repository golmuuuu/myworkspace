package util;

public interface View {
	
	//팀장
	int HOME = 1;
	
	// 고
	int CLIENT = 2;
	int CLIENT_LOGIN = 21;
	int CLIENT_HOME = 22;
	int CLIENT_MYPAGE = 23;
	
	
	

	// 이용권 
	int MEMEBERSHIP = 3;
	int MEMBERSHIP_LIST = 31;  //이용권 종류 보여주
	
	// 구매한 이용권 관리 
	int REL_MEMBSHIP = 4;
	int REL_MEMSHIP_PAY = 41; // 이용권 결제 페이지 
	int REL_MEMBSHIP_SETTING = 42; // 이용권 관리 페이지
	int REL_MEMBSHIP_MY = 43;
	int REL_MEMBSHIP_CANCEL = 44;
	int REL_MEMBSHIP_CHANGE = 45;
	




}
