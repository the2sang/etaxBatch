package kr.co.kepco.etax30.selling.batchoffline;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;

/*******************************************************************************
 * 저작권 : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved
 * 
 * 프로젝트 명 : KEPCO ETAX 프로젝트 프로그램 명 : BatchOffline3 프로그램 아이디 : BatchOffline3.java
 * 프로그램 개요 : 압축 배치작업 실행(main()) 관련 테이블 : 관련 모듈 : BatchProcess 작성자 : 양형공 작성일자 :
 * 2009-12-31
 * 
 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0, 최초작성
 * 
 * <METHOD> - main() </METHOD>
 ******************************************************************************/

public class BatchOffline3 {

	final static String FILEPATH = CommProperties.getString("FILEPATH");

	// private static long interval = 1 * 30 * 1000; // 분단위
	public static String OnOffbatch = "ON";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 처리월 변수를
		// 받음================================================================================
		String targetMonth = "";
		String now_grp_no = "";
		try {
			targetMonth = CommUtil.getWorkYM(args);
			if (targetMonth.length() == 6 && args[1].length() == 2) { // 두번째 항목은 배치그룹 차수임
				BatchOffline3.OnOffbatch = "OFF";
				now_grp_no = args[1];
			} else {
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
			if (configInfo.getSvr0_exec_yn().equals("Y")) {
				machineNum++;
			}
			if (configInfo.getSvr1_exec_yn().equals("Y")) {
				machineNum++;
			}
			if (configInfo.getSvr2_exec_yn().equals("Y")) {
				machineNum++;
			}
			if (configInfo.getSvr3_exec_yn().equals("Y")) {
				machineNum++;
			}

			System.out.println("실행 서버 수: " + machineNum);
			System.out.println("Thread : " + threadNum);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		// 압축작업 실행
		try {
			new BatchProcess().compressFiles(targetMonth, machineNum,
					now_grp_no);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
