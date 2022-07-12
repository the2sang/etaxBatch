/******************************************************************************
 * 저작권            : Copyright⒞ 2009 by KEPCO Corp. All Rights Reserved

 * 프로젝트 명       : 한전 매출-세금계산서 시스템 개발 프로젝트
 * 프로그램 명       : CommUtil
 * 프로그램 아이디   : CommUtil
 * 프로그램 개요     :  시스템에서 사용하는 유틸모음                     
 * 관련 테이블       : 
 * 관련 모듈         : 
 * 작성자            : 정명수
 * 작성일자          : 2009-11-18

 * 개정이력(성명 | 일자 | 내용) : 정명수 | 2009-11-18 | (DEV TEAM), v1.0, 최초작성
 
 * <METHOD>
 * - main()   ; 스케쥴러 실행(실행용 클래스)
 * </METHOD>
******************************************************************************/

package kr.co.kepco.etax30.selling.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommCipher;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;

/**
 * @author msjeong
 * 
 */
public class RunningScheduler {
	
	final static String DRIVER = CommProperties.getString("DB_DRIVER");
	final static String URL    = CommProperties.getString("DB_URL");
	final static String USER   = CommProperties.getString("DB_USER");
	final static String PASS   = CommCipher.StringDecipher(CommProperties.getString("DB_PASS"));
	private static long interval = 1 * 60 * 1000*5; //1 * 60 * 1000*5 ==> 5분 

	public static int Log_Mode = -1;
	public static String amIAlive = "NO";
	
	final static String DB_ENC   = CommProperties.getString("DB_ENC");
	
	// 차세대 TP_SUPPLIER_TBL_VIEW의  BUSINESS_NO 암호화 적용 유무 20160223
	final static String BUSI_ENC   = CommProperties.getString("BUSI_ENC");
	
	// 국세청 익일전송 적용 일자 일반:작성일자, 수정:세금계산서 만든일자
	//public static String TAX2NDSIGN_STARTDAY = "20120615";
	public static String TAX2NDSIGN_STARTDAY = "20120701";

	public static void main(String args[]) {
		try {
			System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
		    if("".equals(CommUtil.NullChange(DRIVER))) {
		     throw (new Exception("프로퍼티 파일 로드 실패!"));
		    }
			Log_Mode = Integer.parseInt(CommProperties.getString("LOG_MODE"));
			System.out.println("Log_Mode:"+Log_Mode);
  		    CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값 :"+DB_ENC+" 처음  읽어오기",1);
  		    
  		    //차세대 TP_SUPPLIER_TBL_VIEW의  BUSINESS_NO 암호화 적용 유무 20160223 1 암호화, 0 : 비암호화
  		    CommUtil.logWriter("차세대 BUSINESS_NO 암호화 설정정보(1 암호화, 0 : 비암호화) 값 :"+BUSI_ENC+" 처음  읽어오기",1);
  		    
		    start_job();
		   } 
		   catch (Exception e) {
		    e.printStackTrace();
		    System.exit(-1);
		   }		
	}

	private static void start_job() throws Exception {

		//long i = 0;
		JobClient job = null;
		
		while (true) {
			//i++;
			try {
//				System.out.println("Start BatchOnline");	
//			    	System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
			    
/*			    if("".equals(CommUtil.NullChange(DRIVER))) {
				 System.out.println("프로퍼티 파일 로드 실패! 현재 프로퍼티 경로:" );
//				 System.out.println("프로퍼티 파일 로드 실패! 현재 프로퍼티 경로:" + RunningScheduler.class.getResource("ap.property").getPath());
				 System.exit(-1);
				} */
			    
			    
				job = new JobClient(DRIVER, URL, USER, PASS);
				amIAlive = "YES";
				CommUtil.logWriter("amIAlive:"+amIAlive,4);
				//Thread.sleep(interval); //설정된 주기만큼 Sleep
				
				job.runSchedule();
				CommUtil.logWriter("amIAlive:"+amIAlive,4);
				amIAlive = "NO";
				CommUtil.logWriter("amIAlive:"+amIAlive,4);
				
				Thread.sleep(interval); //설정된 주기만큼 Sleep
			}
			catch(Exception e) {
				e.printStackTrace();
				Thread.sleep(interval);
				continue;
			}
			finally {
				job = null;
			}
		}//END while
	}

	public static long getInterval() {
		return interval;
	}

	public static void setInterval(long interval) {
		RunningScheduler.interval = interval;
	}

}

class JobClient {
	// DB연결정보
	static String DRIVER = null;
	static String URL    = null;
	static String USER   = null;
	static String PASS   = null;
	
	long thread_number;
	String thread_name;

	// 생성자(Default)
	public JobClient() {

	}

	// 생성자
	public JobClient(long thread_number) {
		this.thread_number = thread_number;
	}

	// 생성자
	public JobClient(String thread_name) {
		this.thread_name = thread_name;
	}
	
	// 생성자
	public JobClient(String driver, String url, String user, String pass) {
		DRIVER = driver;
		URL = url;
		USER = user;
		PASS = pass;
	}
	
	public void runSchedule() throws Exception {
		
		/* 아래에 주기별로 수행할 작업내용을 작성합니다. */

		// 1. DB에서 수행주기 정보를 가져와서 만약 틀리다면 갱신합니다.
		chkInterval();
		
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);
		
		// 2. 인터페이스로부터작업대상을 잡아 국세청 전송 
		System.out.println("Start BatchOnline_tran");		    
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
		BatchInterfaceTrans  bf = new BatchInterfaceTrans(DRIVER, URL, USER, PASS);
		
		System.out.println(" 0.아이템 오류 에러체크 start ");
		bf.error_chk(); 		//아이템 오류 에러체크
		System.out.println(" 0.아이템 오류 에러체크 end ");
		
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);
		/* 1. */
		System.out.println(" 1.관리테이블로 이관작업 start ");
		bf.pross_tran();		//관리테이블로 이관작업1
		System.out.println(" 1.관리테이블로 이관작업 end ");
		/* 1. */
		
		/* 2. 영업온라인매출 및 ERP매출 수정세금계산서 공급받는자  승인  강제 처리 '01'->'02'로변경 */
		System.out.println(" 2. 오프라인 미완료건 온라인으로 처리 승인 start ");
		bf.updateNcis_tran();
		System.out.println(" 2. 오프라인 미완료건 온라인으로 처리 승인 end ");
		/* 2. */
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);
		
		//System.out.println(" 솔루션으로 이관작업 start ");
		System.out.println(" 매출세금계산서 xml생성 및 etaxconn으로 이관 시작 start ");
		
		bf.pross_tran1();		//솔루션으로 이관작업     2012.12.13 
		//System.out.println(" 솔루션으로 이관작업 end ");
		
		System.out.println(" 매출세금계산서 xml생성 및 etaxconn으로 이관 완료 end ");
		
		System.out.println("End BatchOnline_tran");
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
		System.out.println("\n\n");
		
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);
		// 3. 국세청 전송,수신 결과를 체크하여 이력 테이블 갱신
		// 이력 갱신 시 신고완료된 건에 대해선 ,sms 송신 처
		System.out.println("Start BatchOnline_update");		    
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));		
		BatchUpdateetaxinfo  fut = new BatchUpdateetaxinfo(DRIVER, URL, USER, PASS);

		
		//####################################################################
		// 프로그램 수정 2013.10.22 시작
		// 제목 : 매출세금계산서 xml생성 토피도 Tid 기능 걷어내기
		// 수정내용 :  etaxconn에서  가져와 tb_xml_info에 저장하는 코드 삭제
		//----------------------------------------------------
		//------변경 전 기능 ---------------------------------------
		//System.out.println(" 온라인 매출건에 대한 xml 데이터 etaxconn에서  가져와 tb_xml_info에 저장 start ");
		//fut.pross_xml_tran();		//온라인 매출건에 대한 xml 데이터를 가져온다.	
		//System.out.println(" 온라인 매출건에 대한 xml 데이터 etaxconn에서  가져와 tb_xml_info에 저장 end ");
		//----------------------------------------------------
		// 프로그램 수정 2013.10.22 끝
		//####################################################################
		
		System.out.println(" 전송후 상태 변경을 위한 솔루션 상태 비교 start ");
		fut.pross_update();		//전송후 상태 변경을 위한 솔루션 상태 비교
		System.out.println(" 전송후 상태 변경을 위한 솔루션 상태 비교 end ");
		
		System.out.println("End BatchOnline_update");
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
		System.out.println("\n\n");		

		// 3.1 매출세금계산서 전표정보 수신후 메일/SMS 송신 start ADD 2011.11.30 by mopuim
		//2011.11.30 추가 
		/* 제   목 : 공가 매출전자세금계산서연계 관련
		 * 수정일 : 2013.11.11 빼빼로DAY
         * 수정 내용 : ERP 매출 (공가매출은 제외하고) 전표완료된 것만 송신 처리 되도록 수정  */
		System.out.println(" 3.1 ERP 매출세금계산서 전표정보 수신후 메일/SMS 송신 START ");
		bf.pross_mail_sms_tran();		
		System.out.println(" 3.1 ERP 매출세금계산서 전표정보 수신후 메일/SMS 송신 END ");
		/* 3.1 매출세금계산서 전표정보 수신후 메일/SMS 송신 end*/

		
		/* 3.2 매출세금계산서 국세청 전송후 성공인경우 이메일유통에 송신 ADD 2011.11.30 by mopuim */
		//2011.11.30 추가 
		System.out.println(" 3.2 매출세금계산서 국세청 전송후 성공인경우 이메일유통에 송신 START ");
		bf.pross_emailTransfer_tran();		
		System.out.println(" 3.2 매출세금계산서 국세청 전송후 성공인경우 이메일유통에 송신 END ");
		/* 3.1 매출세금계산서 전표정보 수신후 메일/SMS 송신 end*/

		System.out.println("======================한전매입처리===================\n");
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);
		// 4. 매입정보를 체크하여 변동사항이 있으면 이력 갱신		
		System.out.println("Start BatchOnline_out_pross");		    
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));		
		Pross_purchase  pp = new Pross_purchase(DRIVER, URL, USER, PASS);
		
		System.out.println(" 매입 처리 start ");
		pp.pross_purchase();		//매입 처리
		//단 국세청 신고 처리후 결과 상태(04,96,99등) 반영은 위 200line pross_update()에서 매출,매입 같이 처리함
		System.out.println(" 매입 처리 end ");
		
		System.out.println("End BatchOnline_out_pross");
		System.out.println(CommUtil.getKST("yyyy-MM-dd HH:mm:ss"));
		System.out.println("===========================================================\n\n");		
		//CommUtil.logWriter("amIAlive:"+RunningScheduler.amIAlive,0);

		
	}		
	
	//DB에서 수행주기 설정정보를 가져와 동기화 하는 함수
	private boolean chkInterval() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName(DRIVER);
			con  = DriverManager.getConnection(URL, USER, PASS);  
			
			StringBuffer sb = new StringBuffer()
			.append("SELECT EXEC_CYCLE_MINUTE  \n")
			.append("  FROM TB_BATCH_CONFIG    \n");  
			
			pstmt = con.prepareStatement(sb.toString());
	    	
			rs = pstmt.executeQuery();
			
			rs.next();
			//ret = rs.getString(1);
			long ret = rs.getLong("EXEC_CYCLE_MINUTE");
//System.out.println("3"+ret);
			
			rs.close();
			pstmt.close();
			sb.setLength(0);
			
			if (ret <= 0) 
				System.exit(-1); //데몬 종료
			
			if (ret * 60 * 1000 != RunningScheduler.getInterval() )
			{
				//System.out.println("값이 틀림");
				
				//새로운 수행주기 값으로 설정
				RunningScheduler.setInterval(ret * 60 * 1000);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				pstmt = null;
				rs = null;
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
}
