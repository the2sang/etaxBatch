package kr.co.kepco.etax30.selling.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BatchUpdateetaxinfo {

    
    /**
     * 배치 프로시져 Main;.
     * @param args 기준일자
     */     
    
    private String driver = null;
    private String url    = null;
    private String user   = null;
    private String pass   = null;   
    
    public BatchUpdateetaxinfo(String driver,String url, String user, String pass){
	this.driver = driver;
	this.url = url;
	this.user = user;
	this.pass = pass;
    }   
    
    
    public void pross_xml_tran(){
	Connection con = null;
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
	boolean bl = false;
	
	    
	try{
            Class.forName(driver);
            con  = DriverManager.getConnection(url, user, pass);  
		
	    con.setAutoCommit(false);	
	    
	    /*배치 프로시져 전송결과 업데이트  Start*/
	    //작업전 템프테이블 비우기
	    bl = bq.xml_info_tran(con);
	    //국세청 전송전 에러건 확인
	    //System.out.println("b10 :"+bl);		    
	    if(bl){
		con.commit();
	    }
	    /*배치 프로시져 종료*/
	    
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		con.rollback();
		throw e;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    e1.printStackTrace();
	    try {
		con.rollback();
		throw e1;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}finally{
            try{
            	if (con != null){
            		con.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
	}
    }
    
    
    
    
    
    
    public void pross_update(){
	Connection con = null;
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
	boolean bl = false;
	
	    
	try{
            Class.forName(driver);
            con  = DriverManager.getConnection(url, user, pass);  
		
	    con.setAutoCommit(false);	
	    
	    /*배치 프로시져 전송결과 업데이트  Start*/
	    //작업전 템프테이블 비우기
	    bl = bq.trun_up_temp(con);
	    //국세청 전송전 에러건 확인
	    bl = bq.tb_tax_bill_info_update_topido(con);//-30 -> -60 일로
	    
	    if(bl){
		System.out.println("국세청 처리결과(국세청 전송전 에러건)반영_______________________________");		
		bl = bq.tax_result_info_update_error(con);
		
//		공가 매출전자세금계산서연계 관련
//		수정일 : 2013.11.11 빼빼로DAY
//		내용 ERP FI에 공가매출세금계산서 처리결과 추가 연계를 위해 IF_TAX_BILL_RESULT_INFO에 ERP FI연계용 INSERT 처리
//		대상조건 ONLINE_GUB_CODE = 3, REL_SYSTEM_ID = K1NCIS1000,JOB_GUB_CODE = 350010
//	    2013.11.15 프로세스 변경으로 필요없게됨 
		//bq.tax_result_info_update_copy4FI(con, "K1ERP11000");

		System.out.println("인터페이스 반영 완료________________________________");
		//3. 전송결과 업데이트 TB_TAX_BILL_IFNO
		bl = bq.tb_tax_bill_info_update(con);
		System.out.println("관리테이블 반영 완료________________________________");		
		//5. 전송결과 이력갱신	    
		bl = bq.tb_status_hist_up01(con);
		System.out.println("이력테이블 갱신_________________________________________");		
		//6. 5처리 후 다음 단계 인서트	    
		bl = bq.tb_status_hist_in03(con);
		System.out.println("새이력 등록_________________________________________");		
	    }	    
	    
	    //작업전 템프테이블 비우기
	    bl = bq.trun_up_temp(con);
	    //상태가 변경된것 즉, ETAXCONN에서 처리 중인것 국세청에서 응답(성공 OR 실패)이 온것은 제외  
	    bl = bq.tb_tax_bill_info_update_ready(con);
	    
	    if(bl){
		System.out.println("국세청 처리결과 반영(매출만)_______________________________");		
		bl = bq.tax_result_info_update_(con);

//		공가 매출전자세금계산서연계 관련
//		수정일 : 2013.11.11 빼빼로DAY
//		내용 ERP FI에 공가매출세금계산서 처리결과 추가 연계를 위해 IF_TAX_BILL_RESULT_INFO에 ERP FI연계용 INSERT 처리
//		대상조건 ONLINE_GUB_CODE = 3, REL_SYSTEM_ID = K1NCIS1000,JOB_GUB_CODE = 350010
//	    2013.11.15 프로세스 변경으로 필요없게됨 
		//bq.tax_result_info_update_copy4FI(con, "K1ERP11000");

		System.out.println("인터페이스 반영 완료(매출만)________________________________");
		//3. 전송결과 업데이트 TB_TAX_BILL_IFNO
		bl = bq.tb_tax_bill_info_update(con);
		System.out.println("관리테이블 반영 완료(매입,매출 공통)__________________");		
		//5. 전송결과 이력갱신	    
		bl = bq.tb_status_hist_up01(con);
		System.out.println("이력테이블 갱신_________________________________________");		
		//6. 5처리 후 다음 단계 인서트	    
		bl = bq.tb_status_hist_in03(con);
		System.out.println("새이력 등록_________________________________________");		
	    }
	    //작업전 템프테이블 비우기
	    bl = bq.trun_up_temp(con);
	    //국세청에서 응답이 온것(매입,매출)
	    bl = bq.tb_tax_bill_info_update_ready2(con);
	    
	    System.out.println("################################################################");
	    System.out.println("		국세청 신고가 완료된것 (true / false)= "+bl);
	    System.out.println("################################################################");
	    if(bl){
		System.out.println("국세청 신고완료 반영(매출만 해당)__________________________________");		
		bl = bq.tax_result_info_update(con);	
		System.out.println("4. 전송결과 업데이트 TB_TAX_BILL_IFNO	(매출,매입)");		

//		공가 매출전자세금계산서연계 관련
//		수정일 : 2013.11.11 빼빼로DAY
//		내용 ERP FI에 공가매출세금계산서 처리결과 추가 연계를 위해 IF_TAX_BILL_RESULT_INFO에 ERP FI연계용 INSERT 처리
//		대상조건 ONLINE_GUB_CODE = 3, REL_SYSTEM_ID = K1NCIS1000,JOB_GUB_CODE = = 350010
//	    2013.11.15 프로세스 변경으로 필요없게됨 
		//bq.tax_result_info_update_copy4FI(con, "K1ERP11000");
		
		//4. 전송결과 업데이트 TB_TAX_BILL_IFNO	(매출,매입)    
		bl = bq.tb_tax_bill_info_update(con);
		//5. 전송결과 이력갱신	  
		System.out.println("관리테이블 반영 완료________________________________");		
		bl = bq.tb_status_hist_up01(con);
		System.out.println("이력테이블 갱신_________________________________________");		
		//6. 5처리 후 다음 단계 인서트	    
		bl = bq.tb_status_hist_in03(con);		
		System.out.println("새이력 등록_________________________________________");		
	    }

		//7. 국세청신고 결과 폐업사업자 오류시 TAX_BIZ_CONFIRM_T(폐업사업자 관리 정보)에 등록 
	    //                                               2011.10.21 ADD BY CONAN	    
		System.out.println("폐업사업자 정보 등록 시작__________________________________");		
		bl = bq.tax_biz_confirm_insert(con);	
		System.out.println("폐업사업자 정보 등록 종료__________________________________");		
	    
		// 신규추가  2012.06.14 ADDED BY MOPUIM
		System.out.println("배전공사 세금계산서 국세청신고성공/실패  KLT0340.STATUS=04,99 INSERT 시작___________");		
		bl = bq.KBillKLT0340Status_insert(con);	
		System.out.println("배전공사 세금계산서 국세청신고성공/실패  KLT0340.STATUS=04,99 INSERT 종료___________");		

		// 신규추가  2012.06.14 ADDED BY MOPUIM
		System.out.println("내선계기  세금계산서 국세청신고성공/실패  EAI_TAX_STATUS_INFO_TB.STATUS=04,99 UPDATE 시작___________");		
		bl = bq.KBillEAI_TAX_Status_update(con);	
		System.out.println("내선계기  세금계산서 국세청신고성공/실패  EAI_TAX_STATUS_INFO_TB.STATUS=04,99 UPDATE 종료___________");		

		// 신규추가  2012.06.14 ADDED BY MOPUIM
		System.out.println("한전EDI 매입세금계산서 국세청신고성공 04 XMLEDI_STATUS TX2_RST_QTY 갱신 시작_");		
		bl = bq.XmlediStatusTx2Qty04_update(con);	
		System.out.println("한전EDI 매입세금계산서 국세청신고성공 04 XMLEDI_STATUS TX2_RST_QTY 갱신 종료_");		

		// 신규추가  2012.06.14 ADDED BY MOPUIM
		// 잠시 막기 
		//System.out.println("한전EDI 매입세금계산서 국세청신고실패 96 XMLEDI_STATUS TX2_RST_QTY 갱신 시작_");		
		//bl = bq.XmlediStatusTx2Qty96_update(con);	
		//System.out.println("한전EDI 매입세금계산서 국세청신고실패 96 XMLEDI_STATUS TX2_RST_QTY 갱신 종료_");		
	    
	    //*. Sms 전송 tb_status_hist_up04가 실행되기전 같은 조건의 데이터를 대상으로 Sms전송한다.
	    SmsPross  sp = new SmsPross(driver, url, user, pass);
	    sp.sms_email();	     
            //10. 전송결과 이력갱신 - 신고완료건
	    bl = bq.tb_status_hist_up04(con);
	    //System.out.println("b10 :"+bl);		    
	    if(bl){
		con.commit();
	    }
	    /*배치 프로시져 종료*/
	    
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		con.rollback();
		throw e;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    e1.printStackTrace();
	    try {
		con.rollback();
		throw e1;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}finally{
            try{
            	if (con != null){
            		con.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
	}
    }
    
    public static void main(String[] args) {}
    
}
