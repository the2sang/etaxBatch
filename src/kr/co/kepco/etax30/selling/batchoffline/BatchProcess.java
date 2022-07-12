package kr.co.kepco.etax30.selling.batchoffline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;
import kr.co.kepco.etax30.selling.vo.TbXmlDownInfoVo;

//import org.apache.log4j.Logger;

/*******************************************************************************
 * 저작권 : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved
 * 
 * 프로젝트 명 : KEPCO ETAX 프로젝트 프로그램 명 : BatchProcess 프로그램 아이디 : BatchProcess.java
 * 프로그램 개요 : 배치작업 메서드 관련 테이블 : 관련 모듈 : 작성자 : 양형공 작성일자 : 2009-12-31
 * 
 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0, 최초작성
 * 
 * <METHOD> - btProcess() - transferData() - assignWork() - makeXML() -
 * compressFiles() - checkExecution() </METHOD>
 ******************************************************************************/

public class BatchProcess {

	// private static Logger log = Logger.getLogger(BatchProcess.class);

	final static String FILEPATH = CommProperties.getString("FILEPATH");

	private static long interval = 1 * 60 * 1000; // 분단위

	/**
	 * 전체 배치작업 실행 메서드
	 * 
	 * @param targetMonth :
	 *            대상년월
	 * @param machineNum :
	 *            작업서버갯수
	 * @param threadNum :
	 *            쓰레드 갯수
	 * @param configInfo :
	 *            배치환경정보
	 * @param machine :
	 *            현재 실행 머신 넘버
	 */
	public void btProcess(String targetMonth) {

		boolean b1 = false;

		long totalCnt = 0;
		long totalAmt = 0;
		long[] totalBtmp = null;

		TaxXmlService txs = new TaxXmlService();

		// 배치정보조회=====================================================================================
		TbBatchConfigVo configInfo = null;
		int machineNum = 0;
		int threadNum = 0;
		String machine = "";

		try {
			// 환경설정
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

			// 자바 실행 변수 가져옴
			machine = System.getProperty("machine");

			System.out.println("targetMonth:" + targetMonth + " machineNum:"
					+ machineNum + " threadNum:" + threadNum + " configInfo:"
					+ configInfo + " machine:" + machine + "\n");

		} catch (Exception e2) {
			e2.printStackTrace();
		}

		String now_grp_no = configInfo.getNow_grp_no();

		System.out.println("배치그룹 차수 :" + now_grp_no);

		// 배치실행여부 조회
		// TB_BATCH_CONFIG 테이블의 NEXT_EXEC_DT 체크해서 처리
		// checkExecution(machine, configInfo);

		// TbBatchJobHistVo batchHist = null;
		// String[] total = null;
		try {

			if (machine.equals("0")) {
				// 실행시간 체크
				System.out.println("현재시간 : "
						+ CommUtil.getKST("yyyyMMddHHmmss"));
				// System.out.println("실행 설정 시간: " +
				// configInfo.getNext_exec_dt());

				// IF에서 TB 테이블로
				// 데이터이관=========================================================================
				// System.out.println(" batch process ->>>>> 이관시작 ");
				b1 = transferData(targetMonth, now_grp_no);
				// System.out.println(" batch process ->>>>> 이관끝 ->> b1 ="+b1);
				// b1 = true;
				// 분배작업=============================================================================================
				if (b1) {
					b1 = false;
					System.out.println("배치그룹차수:" + now_grp_no + " 분배작업 ->>");
					b1 = assignWork(targetMonth, machineNum, threadNum, now_grp_no );
				}

			} else {// 서버가 0이 아닌경우 loop를 돌면서 0번서버의 분배작업 완료를 기다림

				// System.out.println("서버" + machine + "시작 + interval" +
				// interval);
				// TbBatchJobHistVo batchInfo = null;

				// batchHist = new TbBatchJobHistVo();
				int count = 0;

				while (count < 240) {// loop count 설정

					try {
						// getStartTime()의 반환값이 1인경우 break
						if (txs.getStartTime(targetMonth, machine).equals("1")) {
							b1 = true;
							System.out.println("break");
							break;
						}

						// System.out.println("서버" + machine + "SLEEP 시작");
						Thread.sleep(interval); // 설정된 주기만큼 Sleep
						// System.out.println("서버" + machine + "SLEEP 종료");
						count++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}// END while

			}

			// XML
			// 생성====================================================================================
			if (b1) {
				b1 = false;
				b1 = makeXML(targetMonth, machine, threadNum, machineNum,
						now_grp_no);
			}

			// XML
			// 압축작업================================================================================

			if (machine.equals("0") && b1) {

				// System.out.println("작업 Server 0 번 이면서 XML 생성작업 이 true 입니다.
				// xml 압축작업 시작 : "+ CommUtil.getKST("yyyyMMddHHmmss"));

				// System.out.println("XML 압축작업 1");

				if (machineNum > 1) {// 서버하나에서 돌릴경우 skip
					System.out.println("작업서버가 하나 이상일 경우 !! ");

					while (true) {
						try {
							if (txs.getOutofLoop(targetMonth, machineNum)) {
								b1 = true;
								System.out.println("break");
								break;
							}
							System.out.println("sleep start");
						} catch (Exception e) {
							e.printStackTrace();
						}
						Thread.sleep(interval); // 설정된 주기만큼 Sleep
					}// END while
				}// END if

				// String filename = null;
				long grandTotalCnt = 0;
				long grandTotalAmount = 0;
				// TbXmlDownInfoVo downInfo = null;

				for (int i = 0; i < machineNum; i++) {

					// 분배완료된 대상 테이블인 BTMP_TAX_BILL_INFO에서 개수와 금액 합을 long[]으로 반환
					totalBtmp = txs.getTotalBtmp(String.valueOf(i));
					totalCnt = totalCnt + totalBtmp[0];
					totalAmt = totalAmt + totalBtmp[1];

					TbBatchJobHistVo batchHist0 = new TbBatchJobHistVo();

					batchHist0.setYyyymm(targetMonth);
					batchHist0.setGrp_no(now_grp_no);
					batchHist0.setServer_no(String.valueOf(i));
					batchHist0.setBatch_job_code("03");// 3번이력으로 설정
					batchHist0.setDest_cnt(totalBtmp[0]);
					batchHist0.setDest_amt(totalBtmp[1]);

					// 작업한서버합계
					grandTotalCnt = grandTotalCnt
							+ txs.getTbBatchJobHist(batchHist0).getWork_cnt();
					grandTotalAmount = grandTotalAmount
							+ txs.getTbBatchJobHist(batchHist0).getWork_amt();

				}
				System.out.println("=======================");
				System.out.println("grandTotalAmount:" + grandTotalAmount);
				System.out.println("totalAmt:" + totalAmt);
				System.out.println("=======================");

				if ((totalAmt == grandTotalAmount)
						&& (totalCnt == grandTotalCnt)) {

					try {

						// ########################################################
						CommUtil.logWriter("배치그룹차수:" + now_grp_no
								+ "압축배치작업 시작", 4);

						compressFiles(targetMonth, machineNum, now_grp_no);

						CommUtil.logWriter("배치그룹차수:" + now_grp_no
								+ "압축배치작업 정상 종료", 4);

						// ########################################################
						// 2011.11.30 세금계산서 통합개발 관련 처리 작업
						// 사용하지 않아 처리 로직 comment 처리 2015.06.29
						/*
						 * CommUtil.logWriter("요금 매출 CD분 자료 저장 시작 ", 4);
						 * 
						 * int TbBillCnt; int TbItemCnt; //
						 * TAX_BUSINESS_BILL_INFO 테이블에 INSERT TbBillCnt =
						 * txs.insertTaxBusinessBillInfo(targetMonth); //
						 * TAX_BUSINESS_ITEM_LIST 테이블에 INSERT if (TbBillCnt > 0) {
						 * TbItemCnt = txs
						 * .insertTaxBusinessItemList(targetMonth); // 두 테이블에
						 * insert 갯수가 같은경우만 처리 완료 및 성공 처리 if ((TbBillCnt ==
						 * TbItemCnt)) { CommUtil.logWriter("요금 매출 CD분 자료 저장 완료 ",
						 * 4); } else { CommUtil .logWriter( "요금 매출 CD분 자료 저장중
						 * TAX_BUSINESS_BILL_INFO 갯수와 TAX_BUSINESS_ITEM_LIST 갯수가
						 * 다릅니다. ", 4); CommUtil.logWriter(
						 * "TAX_BUSINESS_BILL_INFO cnt:" + TbBillCnt, 4);
						 * CommUtil.logWriter( "TAX_BUSINESS_ITEM_LIST cnt:" +
						 * TbItemCnt, 4); } } else if (TbBillCnt == 0) {
						 * CommUtil.logWriter("요금 매출 CD분 자료 저장 처리 대상이 없음 ", 4); }
						 * else if (TbBillCnt < 0) { CommUtil.logWriter("요금 매출
						 * CD분 자료 저장 처리 오류발생 ", 4); }
						 */
						// ########################################################
						System.out.println("#####################################################");
						CommUtil.logWriter("배치그룹차수:" + now_grp_no
								+ "배치작업 분배-생성-압축 정상 완료", 4);
						System.out.println("#####################################################");

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("배치그룹차수:" + now_grp_no
							+ "대상데이터와 처리데이터가 일치하지 않습니다.");
					System.out.println("##########################처리 방법#####################");
					System.out.println("TB_TAX_BILL_INFO 테이블의 STATUS_CODE가");
					System.out.println("05가 아닌 세금계산서만 IF테이블 FLG를 N으로");
					System.out.println("변경해서 재처리 바랍니다.");
					System.out.println("######################################################");
					try {

						TbBatchJobHistVo bh = new TbBatchJobHistVo();
						bh.setYyyymm(targetMonth);
						bh.setServer_no("0");
						bh.setBatch_job_code("04");
						bh.setWork_stat("F");
						bh.setDest_cnt(totalCnt);
						bh.setDest_amt(totalAmt);
						bh.setWork_cnt(0);// 처리개수 0 설정
						bh.setWork_amt(0);// 처리금액 0설정
						bh.setGrp_no(now_grp_no);

						txs.startBatchJob(bh);// 4번단계 시작 이력 입력

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}

			int grp_cnt_int = Integer.parseInt(configInfo.getGrp_cnt());
			int now_grp_cnt_int = Integer.parseInt(configInfo.getNow_grp_no());

			CommUtil.logWriter("grp_cnt_int:" + grp_cnt_int + "      now_grp_cnt_int:" + now_grp_cnt_int ,4);

			//"00" 포맷에 맞춰 차수값 구하기 
			String next_grp_no = "00"+String.valueOf(now_grp_cnt_int + 1);
			next_grp_no = next_grp_no.substring(next_grp_no.length()-2);
			
			if (grp_cnt_int > now_grp_cnt_int ) {
				System.out.println("#####################################################");
				CommUtil.logWriter("배치그룹차수:" + now_grp_no + "완료후 다음 배치그룹["+next_grp_no+"] 처리 준비",1);
				System.out.println("#####################################################");
				// 현재 배치그룹차수가 마지막 차수인지 확인
				txs.setNextGrpNo(next_grp_no);
				// 배치그룹 다음차수 배치 시작
				btProcess(targetMonth);
			}else{
				System.out.println("#####################################################");
				CommUtil.logWriter("배치그룹차수:" + grp_cnt_int + "로 모든 배치처리가 최종 완료되었습니다.",1);
				System.out.println("#####################################################");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * IF테이블에서 TB테이블로 데이터 이전
	 * 
	 * @param targetMonth :
	 *            대상년월
	 */
	public boolean transferData(String targetMonth, String now_grp_no) {

		boolean b1 = false;
		String[] total = null;
		TaxXmlService txs = new TaxXmlService();
		// log.info("Data 이전 시작: "+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
		// System.out.println("Data 이전 시작: "+CommUtil.getKST("yyyy-MM-dd
		// HH:mm:ss.SSS"));

		try {
			// 0번서버의 01단계 객체 설정
			TbBatchJobHistVo batchHist = new TbBatchJobHistVo();
			batchHist.setYyyymm(targetMonth);
			batchHist.setServer_no("0");// 0번서버
			batchHist.setBatch_job_code("01");// 01단계
			batchHist.setDest_cnt(0);// 대상갯수 0
			batchHist.setDest_amt(0);// 대상금액 0
			batchHist.setWork_stat("S");
			batchHist.setGrp_no(now_grp_no);

			// 0번서버 01단계 시작 이력저장
			CommUtil.logWriter("배치그룹차수:" + now_grp_no
					+ " 0번서버 01단계 시작 이력저장 ->>", 4);
			txs.startBatchJob(batchHist);
			CommUtil.logWriter("배치그룹차수:" + now_grp_no
					+ " 0번서버 01단계 시작 이력저장 끝->>", 4);

			// System.out.println(" 이관작업 start ->>");
			CommUtil.logWriter("배치그룹차수:" + now_grp_no + " 이관작업 start ->>",
					4);
			b1 = new BatchOfflineInterface().BatchInterface(batchHist,now_grp_no);
			CommUtil.logWriter("배치그룹차수:" + now_grp_no + "  이관작업 end ->> b1 ->>" + b1 + " true 이면 ->>", 4);

			if (b1) {
				// 인터페이스 Temp 테이블에서 대상 총금액을 가져옴
				total = txs.getTotal();
				System.out.println("배치그룹차수:" + now_grp_no
						+ " 총 합계 정보 가져옴 total[0](CNT)=" + total[0]+"  total[1](AMT)=" + total[1]);

				if (total == null) {
					b1 = false; // 처리할 값이 없을경우 실행하지 않음
					TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
					endBatchHist = txs.getTbBatchJobHist(batchHist);
					endBatchHist.setWork_stat("F");
					// 0번서버 01단계 완료 이력저장
					txs.endBatchJob(endBatchHist);
					endBatchHist = null;
					// System.out.println(" total == null 과 같아 처리 아니되고 이력
					// work_stat = F END ");
				} else {
					TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
					endBatchHist = txs.getTbBatchJobHist(batchHist);
					endBatchHist.setDest_cnt(CommUtil.nvl(total[0], 0));
					endBatchHist.setDest_amt(CommUtil.nvl(total[1], 0));
					endBatchHist.setWork_cnt(CommUtil.nvl(total[0], 0));
					endBatchHist.setWork_amt(CommUtil.nvl(total[1], 0));
					System.out.println(endBatchHist.getStart_dt());
					// 0번서버 01단계 완료 이력저장
					txs.endBatchJob(endBatchHist);
					endBatchHist = null;
				}
				batchHist = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b1;
	}

	/**
	 * 압축작업
	 * 
	 * @param targetMonth :
	 *            대상년월
	 * @param machineNum :
	 *            서버 갯수
	 */
	public void compressFiles(String targetMonth, int machineNum,
			String now_grp_no) throws Exception {

		// System.out.println("압축배치작업시작");

		boolean b1 = false;
		String filename = null;
		// long grandTotalAmount = 0;
		String delCode = "";
		TbXmlDownInfoVo downInfo = null;

		TaxXmlService txs = new TaxXmlService();

		long totalCnt = 0;
		long totalAmt = 0;
		long[] totalBtmp = null;
		long totalCntY = 0;
		long totalAmtY = 0;
		long[] totalBtmpY = null;

		// 분배전체 데이터
		totalBtmp = txs.getTotalBtmpTotal();
		totalCnt = totalBtmp[0];
		totalAmt = totalBtmp[1];

		// 분배완료된 대상 테이블인 BTMP_TAX_BILL_INFO에서 개수와 금액 합을 long[]으로 반환
		totalBtmpY = txs.getTotalBtmpTotalFlagY();// 분배된데이터중 생성처리 완료된값
		totalCntY = totalBtmpY[0];
		totalAmtY = totalBtmpY[1];

		try {

			TbBatchJobHistVo batchHist0 = new TbBatchJobHistVo();
			batchHist0.setYyyymm(targetMonth);
			batchHist0.setServer_no("0");
			batchHist0.setBatch_job_code("04");
			batchHist0.setWork_stat("S");
			batchHist0.setDest_cnt(totalCnt);
			batchHist0.setDest_amt(totalAmt);
			batchHist0.setWork_cnt(0);// 처리개수 0 설정
			batchHist0.setWork_amt(0);// 처리금액 0설정
			batchHist0.setGrp_no(now_grp_no);// 대상 배치그룹 차수

			txs.startBatchJob(batchHist0);// 4번단계 시작 이력 입력
			String[] keys = txs.getBtmpRowIDListFlagY();// 분배된 데이터중 완료된 값을 가져옴
			// System.out.println("keys number: "+keys.length);
			TaxZip tz = new TaxZip();
			// b1 = tz.makeFiles(targetMonth, FILEPATH, keys);

			if (totalCnt == totalCntY) {// 분배데이터가 모두 완료되었을경우 압축 작업 진행
				// System.out.println(" 분배데이터가 모두 완료되었을경우 압축 작업 진행!! \n");

				// R VALUE 생성
				Rvalue.getFile(FILEPATH + "/" + targetMonth + now_grp_no + "/");
				System.out.println("r value 생성완료");

				// 배치 환경에서 삭제값 가져옴
				delCode = txs.getTbBatchConfig().getXml_del_code();

				// 파일이름을 현재시간으로 설정
				filename = now_grp_no + "_" + CommUtil.getKSTDateTime();
				System.out.println("filename: " + filename);
				System.out.println("del code: " + delCode);

				// 압축 셀을 실행
				CommUtil.runTarZip(targetMonth, now_grp_no, filename, delCode);
				System.out.println("파일경로: " + FILEPATH + "/" + filename
						+ ".tar.gz");
				b1 = false;
				if (new File(FILEPATH + "/" + filename + ".tar.gz").exists()) {
					System.out.println("file 존재함");
					b1 = true;
				}
				System.out.println("파일사이즈"
						+ new File(FILEPATH + "/" + filename + ".tar.gz")
								.length() / 1024 / 1024);

				// 파일삭제여부에 따라 삭제
				if (delCode.equals("01")) {// 익월 배치작업 삭제

					List list = new ArrayList();
					tz.getFileList(new File(FILEPATH), list, targetMonth);

					for (int i = 0; i < list.size(); i++) {
						tz.deleteDir(FILEPATH + "/" + (String) list.get(i));
					}
				} else if (delCode.equals("02")) {// 압축파일생성 삭제
					tz.deleteDir(FILEPATH + "/" + targetMonth);
				}
				// TbXmlDownInfoVo 설정
				if (b1) {
					downInfo = new TbXmlDownInfoVo();

					downInfo.setDate(targetMonth);
					downInfo.setGrp_no(now_grp_no);
					downInfo.setFile_name(filename);
					downInfo.setTotal_amount(totalAmt);
					downInfo.setTotal_count(keys.length);
					downInfo.setFile_size(new File(FILEPATH + "/" + filename
							+ ".tar.gz").length() / 1024 / 1024);
					downInfo.setFile_path(FILEPATH);

					txs.setTbXmlDownInfo(downInfo);// 압축파일정보 저장
				}
				TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
				batchHist0.setServer_no("0");// 0번서버
				batchHist0.setBatch_job_code("04");// 4번단계 설정

				batchHist0.setDest_cnt(totalCnt);// 대상개수설정
				batchHist0.setDest_amt(totalAmt);// 대상금액설정

				// System.out.println("배치이력 가져옴 : "
				// + CommUtil.getKST("yyyyMMddHHmmss"));
				endBatchHist = txs.getTbBatchJobHist(batchHist0);
				endBatchHist.setGrp_no(now_grp_no);

				if (b1) {
					endBatchHist.setWork_cnt(totalCnt);// 처리개수 설정
					endBatchHist.setWork_amt(totalAmt);// 처리금액 설정
				} else {
					endBatchHist.setWork_stat("F");
					System.out.println("setWork_stat F로 설정 ");
				}
				System.out.println("endBatchHist.getGrp_no()"
						+ endBatchHist.getGrp_no());
				System.out.println("endBatchHist.getWork_amt()"
						+ endBatchHist.getWork_amt());
				System.out.println("endBatchHist.getWork_cnt()"
						+ endBatchHist.getWork_cnt());

				txs.endBatchJob(endBatchHist);// 4번이력 종료 저장

			} else {// 분배테이블에서 완료된 'Y'값만 부분적으로 처리하는 경우

				// System.out.println(" 분배테이블에서 완료된 'Y' 값만 부분적으로 처리 경우 !"
				// +CommUtil.getKST("yyyyMMddHHmmss"));

				TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
				batchHist0.setServer_no("0");// 0번서버
				batchHist0.setBatch_job_code("04");// 4번단계 설정

				batchHist0.setDest_cnt(totalCnt);// 대상개수설정
				batchHist0.setDest_amt(totalAmt);// 대상금액설정

				endBatchHist = txs.getTbBatchJobHist(batchHist0);

				endBatchHist.setWork_cnt(totalCntY);// 처리개수 설정
				endBatchHist.setWork_amt(totalAmtY);// 처리금액 설정

				endBatchHist.setWork_stat("S");

				// System.out.println("endBatchHist.getWork_amt()"
				// + endBatchHist.getWork_amt());
				// System.out.println("endBatchHist.getWork_cnt()"
				// + endBatchHist.getWork_cnt());

				txs.endBatchJob(endBatchHist);// 4번이력 종료 저장
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			downInfo = null;
			txs = null;
		}
	}

	/**
	 * 분배작업
	 * 
	 * @param targetMonth :
	 *            대상년월
	 * @param machineNum :
	 *            서버갯수
	 * @param threadNum :
	 *            쓰레드갯수
	 * @return boolean
	 * @throws Exception
	 */
	public boolean assignWork(String targetMonth, int machineNum, int threadNum, String now_grp_no)
			throws Exception {

		boolean b1 = false;
		long totalCnt = 0;
		long totalAmt = 0;
		long[] totalBtmp = null;

		String[] total = null;

		System.out.println("분배 시작: "
				+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
		TaxXmlService txs = new TaxXmlService();

		// TB_TAX_BILL_INFO에서 작업 대상 개수와 금액을 가져옴
		total = txs.getTotalCountAmtTbTax(targetMonth);

		System.out.println("dest cnt :" + total[0]);
		System.out.println("dest amt :" + total[1]);

		TbBatchJobHistVo batchHist = new TbBatchJobHistVo();
		batchHist.setYyyymm(targetMonth);
		batchHist.setServer_no("0");
		batchHist.setBatch_job_code("02");
		batchHist.setWork_stat("S");
		batchHist.setDest_cnt(CommUtil.nvl(total[0], 0));
		batchHist.setDest_amt(CommUtil.nvl(total[1], 0));
		batchHist.setGrp_no(now_grp_no);

		try {
			// 0번서버 02단계 시작 이력 설정
			txs.startBatchJob(batchHist);

			// 분배작업
			b1 = txs.setBtmpTaxBillInfo(machineNum, threadNum, targetMonth);

			for (int i = 0; i < machineNum; i++) {

				// 분배완료된 대상 테이블인 BTMP_TAX_BILL_INFO에서 개수와 금액 합을 long[]으로 반환
				totalBtmp = txs.getTotalBtmp(String.valueOf(i));
				totalCnt = totalCnt + totalBtmp[0];
				totalAmt = totalAmt + totalBtmp[1];

			}
			// System.out.println("분배작업:" + b1);
			if (b1) {
				TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
				endBatchHist = txs.getTbBatchJobHist(batchHist);

				endBatchHist.setDest_cnt(CommUtil.nvl(total[0], 0));
				endBatchHist.setDest_amt(CommUtil.nvl(total[1], 0));
				endBatchHist.setWork_cnt(totalCnt);
				endBatchHist.setWork_amt(totalAmt);

				txs.endBatchJob(endBatchHist);

				batchHist = null;
				endBatchHist = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return b1;
	}

	/**
	 * xml 생성작업
	 * 
	 * @param targetMonth :
	 *            대상년월
	 * @param machine :
	 *            실행서버
	 * @param threadNum :
	 *            쓰레드갯수
	 */
	public boolean makeXML(String targetMonth, String machine, int threadNum,
			int machineNum, String now_grp_no) {

		File targetFolder = null;

		targetFolder = new File(FILEPATH + "/" + targetMonth + now_grp_no);
		if (!targetFolder.exists()) {
			if (!targetFolder.mkdirs()) {
				System.out.println("폴더 생성 실패");
				System.exit(-1);
			}
		}

		long[] totalBtmp = null;
		long totalCnt = 0;
		long totalAmt = 0;

		TaxXmlService txs = new TaxXmlService();

		// for (int i = 0; i < machineNum; i++) {

		// 분배완료된 대상 테이블인 BTMP_TAX_BILL_INFO에서 개수와 금액 합을 long[]으로 반환
		totalBtmp = txs.getTotalBtmp(machine);
		totalCnt = totalCnt + totalBtmp[0];
		totalAmt = totalAmt + totalBtmp[1];
		// }

		TbBatchJobHistVo batchHist = new TbBatchJobHistVo();
		batchHist.setYyyymm(targetMonth);
		batchHist.setServer_no(machine);
		batchHist.setBatch_job_code("03");
		batchHist.setWork_stat("S");
		batchHist.setDest_cnt(totalCnt);
		batchHist.setDest_amt(totalAmt);
		batchHist.setGrp_no(now_grp_no);
		
		try {
			txs.startBatchJob(batchHist);
			// System.out.println("XML 생성 시작: "
			// + CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));

			new MultiThreadCaller().Caller(threadNum, batchHist, targetMonth, now_grp_no);

			TbBatchJobHistVo endBatchHist = new TbBatchJobHistVo();
			endBatchHist = txs.getTbBatchJobHist(batchHist);

			txs.endDtBatchJob(endBatchHist);
			batchHist = null;
			endBatchHist = null;
			txs = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 배치실행 여부 체크 현재일시와 테이블에 등록한 실행 예정시간을 체크해서 처리
	 * 
	 * @param machine :
	 *            실행서버
	 * @param configInfo :
	 *            배치환경정보
	 */
	public void checkExecution(String machine, TbBatchConfigVo configInfo) {

		int serverNo = Integer.parseInt(machine);
		TaxXmlService txs = new TaxXmlService();

		switch (serverNo) {
		case 0:
			if (Long.parseLong(CommUtil.getKST("yyyyMMddHHmmss")) < Long
					.parseLong(configInfo.getNext_exec_dt())) {
				System.out.println("SERVER" + machine + " End : "
						+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				System.exit(-1);
			} else {
				try {
					txs.setNextExecTime(serverNo);
					System.out.println("SERVER" + machine
							+ " Next execution time update");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 1:
			if (Long.parseLong(CommUtil.getKST("yyyyMMddHHmmss")) < Long
					.parseLong(configInfo.getNext_exec_dt1())) {
				System.out.println("SERVER" + machine + " End : "
						+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				System.exit(-1);
			} else {
				try {
					txs.setNextExecTime(serverNo);
					System.out.println("SERVER" + machine
							+ " Next execution time update");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 2:
			if (Long.parseLong(CommUtil.getKST("yyyyMMddHHmmss")) < Long
					.parseLong(configInfo.getNext_exec_dt2())) {
				System.out.println("SERVER" + machine + " End : "
						+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				System.exit(-1);
			} else {
				try {
					txs.setNextExecTime(serverNo);
					System.out.println("SERVER" + machine
							+ " Next execution time update");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case 3:
			if (Long.parseLong(CommUtil.getKST("yyyyMMddHHmmss")) < Long
					.parseLong(configInfo.getNext_exec_dt3())) {
				System.out.println("SERVER" + machine + " End : "
						+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				System.exit(-1);
			} else {
				try {
					txs.setNextExecTime(serverNo);
					System.out.println("SERVER" + machine
							+ " Next execution time update");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}

	}

	public boolean assignGroupNO(TbBatchConfigVo configInfo) throws Exception {

		boolean b1 = false;
		int rtn = -1;

		TaxXmlService txs = new TaxXmlService();

		try {
			rtn = txs.setGroupNo(configInfo);
			if (rtn > 0)
				b1 = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return b1;
	}

}
