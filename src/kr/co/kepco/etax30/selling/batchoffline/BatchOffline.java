package kr.co.kepco.etax30.selling.batchoffline;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : BatchOffline
 * 프로그램 아이디      : BatchOffline.java
 * 프로그램 개요        : 전체 배치작업 실행(main())                                                  
 * 관련 테이블          : 
 * 관련 모듈            : BatchProcess
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - main()
 * </METHOD>
******************************************************************************/
public class BatchOffline {
    
	/**
	 * 배치 프로시져 Main;.
	 * @param args 기준일자
	 */     
	public static String OnOffbatch = "ON";   
	public static int Log_Mode = -1;
	//public static String TARGET_MONTH = "";   

	public static void main(String[] args) throws Exception {
		
		//System.out.println("----------전체 배치작업 실행-----------");
		CommUtil.logWriter("----------전체 배치작업 실행 시작-----------(2022)",4);
        //System.out.println( );
        //System.out.println("  line: " + new Throwable().getStackTrace()[0].getLineNumber());	      
		TaxXmlService txs = new TaxXmlService();
		
    	Log_Mode = Integer.parseInt(CommProperties.getString("LOG_MODE"));
		// 처리월 변수를 받음================================================================================
		String targetMonth = "";
		String now_grp_no = "";
		try {
			//System.out.println("그룹차수 args = "+args+"\n");
			for(int i =0;i<args.length;i++){
				   System.out.println("쉘 파리미터 args["+i+"]="+args[i]+"\n");
			}
			targetMonth = CommUtil.getWorkYM(args);
			
			System.out.println("targetMonth:"+targetMonth);
			if(targetMonth.length()==6){
			   BatchOffline.OnOffbatch = "OFF";
			}			
			//targetMonth = "201007";
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		System.out.println("기준일자_처리월(targetMonth):" + targetMonth);
		
		// 배치정보조회=====================================================================================
		TbBatchConfigVo configInfo = null;
		BatchProcess batchP = new BatchProcess();
		
		try {
			//환경설정
			if(now_grp_no.equals("")){ //쉘 파라미터에 배치그룹이 지정되지 않는 경우만 배치그룹 배정 작업 진행)
				txs.setNextGrpNo("01");
			}else{
				txs.setNextGrpNo(now_grp_no);
			}
			configInfo = txs.getTbBatchConfig();
			
			//배치그룹 배정 작업(2015.6.29)
			if(now_grp_no.equals("")){ //쉘 파라미터에 배치그룹이 지정되지 않는 경우만 배치그룹 배정 작업 진행)
				batchP.assignGroupNO(configInfo);
			}
		
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		
		System.out.println("배치그룹 차수 :" + configInfo.getNow_grp_no());
		//배치작업 실행
		batchP.btProcess(targetMonth);
    }

}
