package kr.co.kepco.etax30.selling.batchoffline;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : BatchOffline2
 * 프로그램 아이디      : BatchOffline2.java
 * 프로그램 개요        : XML 생성배치작업 실행(main())                                                  
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

public class BatchOffline2 {
	
	final static String FILEPATH = CommProperties.getString("FILEPATH");

	private static long interval = 1 * 60 * 1000; // 분단위

	public static String OnOffbatch = "ON";
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 처리월 변수를 받음================================================================================
		String targetMonth = "";
		String now_grp_no = "";
		try {
			targetMonth = CommUtil.getWorkYM(args);
			if(targetMonth.length()==6 && args[1].length()==2){ //두번째 항목은 배치그룹 차수임
				   BatchOffline2.OnOffbatch = "OFF";
					now_grp_no = args[1];
			}else{
				System.out.println("배치그룹 차수가 지정되지 않아 배치프로세스를 종료합니다.");
				System.exit(-1);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		System.out.println("처리월:" + targetMonth);
		System.out.println("배치그룹 차수 :" + now_grp_no);
		
		
		// 배치정보조회=====================================================================================
		TbBatchConfigVo configInfo = null;
		int machineNum = 0;
		int threadNum = 0;
		
		try {
			configInfo = new TaxXmlService().getTbBatchConfig();
			// THREAD 수
			threadNum = configInfo.getThread_cnt();
			
			// 실행SERVER 수
			if (configInfo.getSvr0_exec_yn().equals("Y")) 			machineNum++;
			if (configInfo.getSvr1_exec_yn().equals("Y")) 			machineNum++;
			if (configInfo.getSvr2_exec_yn().equals("Y")) 			machineNum++;
			if (configInfo.getSvr3_exec_yn().equals("Y")) 			machineNum++;

			System.out.println("실행 서버 수: " + machineNum);
			System.out.println("Thread : " + threadNum);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		// 자바 실행 변수 가져옴
		String machine = System.getProperty("machine");
		
		//분배 및 XML 생성작업 실행
		BatchProcess bp = new BatchProcess();
		boolean b1 = false;
		
		if (machine.equals("0")) {
			// 실행시간 체크
			System.out.println("현재시간 : "
					+ CommUtil.getKST("yyyyMMddHHmmss"));
			System.out.println("실행 설정 시간: " + configInfo.getNext_exec_dt());
			
			// 분배작업=============================================================================================

				try {
					b1 = bp.assignWork(targetMonth, machineNum, threadNum, now_grp_no);
				} catch (Exception e) {
					e.printStackTrace();
				}


		} else {// 서버가 0이 아닌경우 loop를 돌면서 0번서버의 분배작업 완료를 기다림

			//System.out.println("서버" + machine + "시작 + interval" + interval);

			TaxXmlService txs = new TaxXmlService();
			int count = 0;

			while (count < 240) {// loop count 설정

				try {						
					if (txs.getStartTime(targetMonth, machine).equals("1")) {
						b1 = true;
						System.out.println("break");
						break;
					}
					
					//System.out.println("서버" + machine + "SLEEP 시작");
					Thread.sleep(interval); // 설정된 주기만큼 Sleep
					//System.out.println("서버" + machine + "SLEEP 종료");
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}// END while
		}

		// XML
		// 생성==============================================================================================
		if (b1) {
			bp.makeXML(targetMonth, machine, threadNum, machineNum, now_grp_no);
		}

	}

}
