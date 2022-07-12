package kr.co.kepco.etax30.selling.batchoffline;

import java.sql.Connection;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : BatchOfflineInterface
 * 프로그램 아이디      : BatchOfflineInterface.java
 * 프로그램 개요        : 이관 배치작업                                                   
 * 관련 테이블          : 
 * 관련 모듈            : BatchQueryOff
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - BatchInterface()
 * </METHOD>
******************************************************************************/
public class BatchOfflineInterface {
     
	public boolean BatchInterface(TbBatchJobHistVo batchHist, String now_grp_no){
		
		BatchQueryOff bq = new BatchQueryOff();//  실제로  Query를 통해 작업이 일어나는 Class;	
    	Connection con = null;
    	boolean bl = false;
		try{
    		con = new Dbcon().getConnection();
    		con.setAutoCommit(false);	
	    
    		/*배치 프로시져 (인터페이스) Start*/
    	    
    		 //6.  IF_TAX_BILL_INFO_TEMP TABLE 비우기	    
            bl = bq. trun_if_temp(con);
    	    CommUtil.logWriter("b :"+bl,4);
    		
    	    //0. 처리할 대상이 있으면 TEMP 테이블에 대상을 잡아 이하를 실행한다.   		
    	    bl = bq.tax_info_trans_temp(con, batchHist.getYyyymm(), now_grp_no);
    	    CommUtil.logWriter("처리대상 잡기 b0 true(처리실행) : b0 :"+bl,4);
    	    if(bl){ //처리대상 이 없으면 이하는 실행되지않는다.
    	    			System.out.println(" /// 이관작업 프로세스 시작  /// ->> ");
                        //1. 세금 계산서 정보 이관 IF_TAX_BILL_INFO => TB_TAX_BILL_INFO	    
                        bl = bq.tax_info_trans(con);
                   	    CommUtil.logWriter("1.end : bl :"+bl,4);
                        
                        //2.  아이템정보 이관 IF_TAX_BILL_ITEM_LIST => TB_TRADE_ITEM_LIST
                        bl = bq.item_list_trans(con);
                   	    CommUtil.logWriter("2.end : b2 :"+bl,4);
                        
                        //3.  처리결과정보 초기값 세팅 IF_TAX_BILL_RESULT_INFO
                        bl = bq.tax_result_info_insert(con);
                   	    CommUtil.logWriter("3.end : b3 :"+bl,4);
                        
                        //4.  처리결과정보 초기값 세팅 TB_STATUS_HIST
                        bl = bq.tax_hist_info_insert(con);
                   	    CommUtil.logWriter("4.end : b4 :"+bl,4);
                        
                        //5.  IF_TAX_BILL_INFO FLG 값 업데이트 'N' --> 'Y'
                        bl = bq.if_tax_bill_info_flg_update(con);
                   	    CommUtil.logWriter("5.end : b5 :"+bl,4);
    	    	}

    	    if(bl){
    		con.commit();
    	    }
    	    /*배치 프로시져 종료*/
    	}catch(SQLException e) {
    	    try {
    	    	bl = false;
    	    	con.rollback();
    	    	e.printStackTrace();
    	    	return bl;
    	    } catch (SQLException e1) {
    	    	e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    	    	bl = false;
    	    	con.rollback();
    	    	e1.printStackTrace();
    	    	return bl;
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	    }
    	}finally{
    		try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	return bl;
    }

}
