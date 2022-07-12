package kr.co.kepco.etax30.selling.batchoffline;

import kr.co.kepco.etax30.selling.dao.TbTaxBillInfoDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kr.co.kepco.etax30.selling.dao.BtmpTaxBillInfoDao;
import kr.co.kepco.etax30.selling.dao.TbBatchConfigDao;
import kr.co.kepco.etax30.selling.dao.TbBatchErrHistDao;
import kr.co.kepco.etax30.selling.dao.TbBatchJobHistDao;
import kr.co.kepco.etax30.selling.dao.TbCertInfoDao;
import kr.co.kepco.etax30.selling.dao.TbStatusHistDao;
import kr.co.kepco.etax30.selling.dao.TbTradeItemListDao;
import kr.co.kepco.etax30.selling.dao.TbXmlDownInfoDao;
import kr.co.kepco.etax30.selling.dao.TbXmlInfoDao;
import kr.co.kepco.etax30.selling.dao.TaxBusinessBillInfoDao;
import kr.co.kepco.etax30.selling.dao.TaxBusinessItemListDao;
import kr.co.kepco.etax30.selling.util.Dbcon;
import kr.co.kepco.etax30.selling.vo.ItemListVo;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;
import kr.co.kepco.etax30.selling.vo.TbBatchErrHistVo;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.vo.TbXmlDownInfoVo;


/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : TaxXmlService
 * 프로그램 아이디      : TaxXmlService.java
 * 프로그램 개요        : 비지니스로직처리 메서드 모음                                                 
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - getTbCertInfo()
 * - getTotal()
 * - ...
 * </METHOD>
******************************************************************************/
public class TaxXmlService {
	/**
	 * //인증서 정보 가져옴
	 */
	public static synchronized String[] getTbCertInfo() throws Exception {
		Connection conn = null;
		String[] result = null;
		TbCertInfoDao info = new TbCertInfoDao();
		try {
			conn = new Dbcon().getConnection();
			result = info.select(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return result;
	}

	// IF 테이블 합 정보 가져옴
	public String[] getTotal() throws Exception {
		Connection conn = null;
		String[] total = new String[2];
		try {
			conn = new Dbcon().getConnection();
			total = new BatchQueryOff().getSumCntAmt(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return total;
	}

	/**
	 * temp 테이블 삭제
	 * @return boolean
	 * @throws Exception
	 */
	public boolean initTemp() throws Exception {
		Connection conn = null;
		boolean bl = false;
		try {
			conn = new Dbcon().getConnection();
			bl = new BatchQueryOff().trun_if_temp(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return bl;
	}

	/**
	 * 서버0의 압축작업 시작 여부 판별
	 * @param targetMonth
	 * @return
	 * @throws Exception
	 */
	public boolean getOutofLoop(String targetMonth, int machineNum) throws Exception {

		Connection conn = null;
		boolean bl = false;
		List list = null;
		List list2 = null;
		try {
			conn = new Dbcon().getConnection();
			TbStatusHistDao hist  = new TbStatusHistDao();
			list = hist.checkCreation(conn, targetMonth);
			if (list.size() == 2) {
				if (((String[]) list.get(0))[0]
						.equals(((String[]) list.get(1))[0])
						&& ((String[]) list.get(0))[1].equals(((String[]) list
								.get(1))[1])) {
					bl = true;
				}
				//완료 여부 체크
				System.out.println("서버0 압축작업 완료여부 체크 ->>>");
				list2 = hist.checkCompletion(conn, targetMonth);
				if(list2.size() == machineNum){
					bl = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return bl;
	}

	/**
	 * 0서버의 분배 작업 완료 판별
	 * @param targetMonth
	 * @param totalCnt
	 * @param totalAmt
	 * @return
	 * @throws Exception
	 */
	public boolean getOutofLoop1(String targetMonth, long totalCnt, long totalAmt) throws Exception {

		Connection conn = null;
		boolean bl = false;
		String[] str = null;
		try {
			conn = new Dbcon().getConnection();

			str = new TbStatusHistDao().checkAssign(conn, targetMonth);
			//str[0]: DEST_CNT 대상개수
			//str[1]: WORK_CNT 작업개수
			//str[2]: DEST_AMT 대상금액
			//str[3]: WORK_AMT 작업금액
			if (str[0] != null) {
				if (str[0].equals(String.valueOf(totalCnt))
						&& str[2].equals(String.valueOf(totalAmt))) {//대상개수와 대상금액 일치 여부
					if (str[0].equals(str[1]) && str[2].equals(str[3])) {//배분작업 완료 여부
						bl = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return bl;
	}
	
	/**
	 * 배치이력 가져옴
	 * 
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public TbBatchJobHistVo getTbBatchJobHist(TbBatchJobHistVo batchJobHist)
			throws Exception {

		Connection conn = null;
		TbBatchJobHistDao batchHist = new TbBatchJobHistDao();
		TbBatchJobHistVo vo = null;
		try {
			conn = new Dbcon().getConnection();

			vo = batchHist.select(conn, batchJobHist);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return vo;
	}

	// 배치환경 설정 가져옴
	public TbBatchConfigVo getTbBatchConfig() throws Exception {

		Connection conn = null;
		TbBatchConfigDao configInfo = new TbBatchConfigDao();
		try {
			conn = new Dbcon().getConnection();
			return configInfo.select(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// 배치 다음 실행시간 UPDATE
	public void setNextExecTime(int server) throws Exception {
		Connection conn = null;
		TbBatchConfigDao configInfo = new TbBatchConfigDao();

		try {
			conn = new Dbcon().getConnection();
			configInfo.update(conn, server);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// 배치그룹차수  UPDATE
	public void setNextGrpNo(String next_grp_no) throws Exception {
		Connection conn = null;
		TbBatchConfigDao configInfo = new TbBatchConfigDao();

		try {
			conn = new Dbcon().getConnection();
			configInfo.udpateGrpNo(conn,next_grp_no);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}
	
	// 배치 분배작업
	public boolean setBtmpTaxBillInfo(int machine, int thread,
			String targetMonth) throws SQLException, Exception {
		Connection conn = null;
		boolean result = false;
		BtmpTaxBillInfoDao BtmpTaxBillInfo = new BtmpTaxBillInfoDao();
		try {
			conn = new Dbcon().getConnection();
			conn.setAutoCommit(false);

			BtmpTaxBillInfo.truncate(conn);
			result = BtmpTaxBillInfo.assignMachineThread(machine, thread,
					targetMonth, conn);

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return result;
	}

	// TB_BATCH_JOB_HIST UPDATE
	public void endDtBatchJob(TbBatchJobHistVo batchJobHist) throws Exception {
		Connection conn = null;
		TbBatchJobHistDao batch = new TbBatchJobHistDao();

		try {
			conn = new Dbcon().getConnection();
			batch.updateEndDt(conn, batchJobHist);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// TB_BATCH_JOB_HIST UPDATE
	public void endBatchJob(TbBatchJobHistVo batchJobHist) throws Exception {
		Connection conn = null;
		TbBatchJobHistDao batch = new TbBatchJobHistDao();

		try {
			conn = new Dbcon().getConnection();
			batch.update(conn, batchJobHist);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// TB_BATCH_JOB_HIST UPDATE
	public void setCountBatchJob(Connection conn,
			TbBatchJobHistVo batchJobHist, long cnt) throws Exception {
		TbBatchJobHistDao batch = new TbBatchJobHistDao();

		try {
			batch.updateCNT(conn, batchJobHist, cnt);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// TB_BATCH_JOB_HIST UPDATE
	public void setAmountBatchJob(Connection conn,
			TbBatchJobHistVo batchJobHist, long amount) throws Exception {

		TbBatchJobHistDao batch = new TbBatchJobHistDao();

		try {
			batch.updateAMT(conn, batchJobHist, amount);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// TB_BATCH_JOB_HIST UPDATE
	public void setStatBatchJob(Connection conn, TbBatchJobHistVo batchJobHist)
			throws Exception {

		TbBatchJobHistDao batch = new TbBatchJobHistDao();

		try {
			conn = new Dbcon().getConnection();
			batch.updateWorkStat(conn, batchJobHist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TB_BATCH_JOB_HIST INSERT
	public void startBatchJob(TbBatchJobHistVo batchJobHist) throws Exception {
		Connection conn = null;
		TbBatchJobHistDao batch = new TbBatchJobHistDao();
		try {
			conn = new Dbcon().getConnection();
			batch.insert(conn, batchJobHist);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// TB_BATCH_ERR_HIST INSERT
	public void setBatchErr(Connection conn, TbBatchErrHistVo batchErrHist) {

		TbBatchErrHistDao batch = new TbBatchErrHistDao();
		try {
			batch.insert(conn, batchErrHist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 세금계산서 테이블 키 리스트
	public String[][] getBtmpRowIDList(Connection conn, String machine_idx,
			int thread_idx) throws Exception {

		BtmpTaxBillInfoDao BtmpRowIDList = new BtmpTaxBillInfoDao();
		try {
			return BtmpRowIDList.selectRowIdList(conn, machine_idx, thread_idx);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 세금계산서 테이블 전체키 리스트
	 */
	public String[] getBtmpRowIDList() throws Exception {
		Connection conn = null;
		BtmpTaxBillInfoDao BtmpRowIDList = new BtmpTaxBillInfoDao();
		try {
			conn = new Dbcon().getConnection();
			return BtmpRowIDList.selectRowIdList(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}
	/**
	 * 세금계산서 분배테이블의 처리된 전체키 리스트
	 */
	public String[] getBtmpRowIDListFlagY() throws Exception {
		Connection conn = null;
		BtmpTaxBillInfoDao BtmpRowIDList = new BtmpTaxBillInfoDao();
		try {
			conn = new Dbcon().getConnection();
			return BtmpRowIDList.selectRowIdListFlagY(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	// 해당키 세금계산정보
	public TbTaxBillInfoVo getTbTaxBillInfo(String rowId, Connection conn,
			String targetMonth) throws Exception {

		TbTaxBillInfoDao tbTaxBill = new TbTaxBillInfoDao();
		return tbTaxBill.select(conn, rowId, targetMonth);
	}

	// 상품리스트
	public List getTbTradeItemList(String rowId, Connection conn) throws Exception {
		List list = null;
		TbTradeItemListDao TbTradeItemList = new TbTradeItemListDao();
		try {
			list = TbTradeItemList.select(conn, rowId);
			
			if(list.size() == 0){
				throw new Exception("상품목록이 없습니다.");
			}
		} catch (SQLException e) {
			System.out
					.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			System.out
					.println(" +++++++++++++          상품리스트                          +++++++++++++");
			System.out.println("EXCEPTION : " + e.toString());
			System.out
					.println(" +++++++++++++          상품리스트                          +++++++++++++");
			System.out
					.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		}
		return list;
	}

	/*
	 * //완료된 xml 파일 등록 public void setXml(Connection conn, String bizManageId,
	 * String orgXml, String issuDay, String rValue )throws Exception{
	 * 
	 * TbXmlInfoDao xmlInfo = new TbXmlInfoDao(); xmlInfo.insert(conn,
	 * bizManageId, orgXml, issuDay, rValue);
	 * 
	 * }
	 */

	// TB_STATUS_HIST UPDATE
	public void setStatusElectronicReport(Connection conn, String bizManageId,
			String IOcode, String issueDay) {

		TbTaxBillInfoDao info = new TbTaxBillInfoDao();

		try {
			info.update(conn, bizManageId, IOcode, issueDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TB_STATUS_HIST UPDATE
	public void setStatusError(Connection conn, String bizManageId,
			String IOcode, String issueDay) {

		TbTaxBillInfoDao info = new TbTaxBillInfoDao();

		try {
			info.updateError(conn, bizManageId, IOcode, issueDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TB_STATUS_HIST UPDATE
	public void endStatus(Connection conn, String bizManageId, String IOcode,
			String issueDay) {

		TbStatusHistDao status = new TbStatusHistDao();

		try {
			status.update(conn, bizManageId, IOcode, issueDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TB_STATUS_HIST INSERT
	public void startStatus(Connection conn, String bizManageId, String IOcode,
			String issueDay, String statusCode, String statusDesc) {

		TbStatusHistDao status = new TbStatusHistDao();
		try {
			status.insert(conn, bizManageId, IOcode, issueDay, statusCode,
					statusDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	// 온라인 세금계산서 테이블 키 리스트
	public List getTbTaxBillInfoKeyList(String dayInfo) throws Exception {

		Connection conn = null;
		List TbTaxBillInfoList = null;

		try {
			conn = new Dbcon().getConnection();
			TbTaxBillInfoList = new TbTaxBillInfoDao().selectKeyList(conn,
					dayInfo);

		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return TbTaxBillInfoList;
	}*/

	public String[] getXML(Connection conn, String rowId) {

		String[] xml = null;
		try {
			xml = new TbXmlInfoDao().select(conn, rowId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;

	}

/*	// 머신별 할당된 금액합
	public long getTotalAmt(String machine) {
		Connection conn = null;
		long amount = 0;
		try {
			conn = new Dbcon().getConnection();
			amount = new BtmpTaxBillInfoDao().getAmount(conn, machine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return amount;
	}

	// 머신별 할당된 카운트 합
	public long getTotalCnt(String machine) {
		Connection conn = null;
		long cnt = 0;
		try {
			conn = new Dbcon().getConnection();
			cnt = new BtmpTaxBillInfoDao().getCount(conn, machine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return cnt;
	}*/
	
	
	/**
	 * 머신별 할당된 카운트와 금액 합
	 */
	public long[] getTotalBtmp(String machine) {
		Connection conn = null;
		long[] totalBtmp = null;
		try {
			conn = new Dbcon().getConnection();
			totalBtmp = new BtmpTaxBillInfoDao().getCountAmount(conn, machine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return totalBtmp;
	}
	/**
	 * 
	 * @return
	 */
	public long[] getTotalBtmpTotal() {
		Connection conn = null;
		long[] totalBtmp = null;
		try {
			conn = new Dbcon().getConnection();
			totalBtmp = new BtmpTaxBillInfoDao().getCountAmountTotal(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return totalBtmp;
	}
	/**
	 * 
	 * @return
	 */
	public long[] getTotalBtmpTotalFlagY() {
		Connection conn = null;
		long[] totalBtmp = null;
		try {
			conn = new Dbcon().getConnection();
			totalBtmp = new BtmpTaxBillInfoDao().getCountAmountTotalFlagY(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return totalBtmp;
	}
	/**
	 * 머신별 할당된 카운트와 금액 합(FlagY)
	 */
	public long[] getTotalBtmpFlagY(String machine) {
		Connection conn = null;
		long[] totalBtmp = null;
		try {
			conn = new Dbcon().getConnection();
			totalBtmp = new BtmpTaxBillInfoDao().getCountAmountFlagY(conn, machine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return totalBtmp;
	}

	/**
	 * 분배작업 시작전 대상월의 작업 갯수와 금액의 합을 가져옴
	 * 
	 * @param targetMonth
	 * @return
	 */
	public String[] getTotalCountAmtTbTax(String targetMonth) {
		Connection conn = null;
		String[] cntAmt = null;
		try {
			conn = new Dbcon().getConnection();
			cntAmt = new TbTaxBillInfoDao().getTotalCountAmt(conn, targetMonth);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return cntAmt;
	}

	/*public long getAmount(Connection conn, String bizManageId) {

		long amount = 0;
		try {
			amount = new TbTaxBillInfoDao().selectAmount(conn, bizManageId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}*/

	/*public long getTotalAmount(String dayInfo) {

		long amount = 0;
		Connection con = null;
		try {
			con = new Dbcon().getConnection();
			amount = new TbTaxBillInfoDao().selectTotalAmount(con, dayInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return amount;

	}*/

/*	public int getTotalCount(String dayInfo) {

		int count = 0;
		Connection con = null;
		try {
			con = new Dbcon().getConnection();
			count = new TbTaxBillInfoDao().getCount(con, dayInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return count;
	}*/

	public void setTbXmlDownInfo(TbXmlDownInfoVo downInfo) {
		Connection conn = null;
		TbXmlDownInfoDao downInfodao = new TbXmlDownInfoDao();
		try {
			conn = new Dbcon().getConnection();
			downInfodao.insert(conn, downInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}

	/**
	 * CD 파일 생성이 실패했을경우 IF_TAX_BILL_RESULT_INFO 테이블에 이력 저장
	 * @param realSystemId
	 * @param jobGubCode
	 * @param manageId
	 * @param issueId
	 * @param registDt
	 * @param flag : 에러코드 95
	 */
	public void setStatus(String realSystemId, String jobGubCode, String manageId, String issueId, String registDt, String flag) {

		Connection conn = null;
		BatchQueryOff ifResult = new BatchQueryOff();

		try {
			conn = new Dbcon().getConnection();
			ifResult.insertIfTaxBillResultInfo(conn, realSystemId, jobGubCode, manageId, issueId, registDt,flag, "CD파일생성실패");

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}
	/**
	 * 0번서버가 아닌경우 XML생성 시작 타임을 판별
	 * @param targetMonth
	 * @param machine
	 * @return
	 */
	public String getStartTime(String targetMonth, String machine) {
		
		Connection conn = null;
		String start = "";
		TbBatchJobHistDao job = new TbBatchJobHistDao();
		try {
			conn = new Dbcon().getConnection();
			start = job.checkStart(conn, targetMonth, machine);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		return start;
	}
	/**
	 * 생성된 XML 저장 및  이력 업데이트
	 * @param conn
	 * @param IOcode
	 * @param bizManageId
	 * @param orgXml
	 * @param issueDay
	 * @param rValue
	 * @param statusCode
	 * @param statusDesc
	 * @param rowId
	 * @param realSystemId
	 * @param jobGubCode
	 * @param manageId
	 * @throws Exception
	 */
	public boolean setResultXml(Connection conn, String IOcode,
			String bizManageId, String orgXml, String issueDay, String rValue,
			String statusCode, String statusDesc, String rowId,
			String realSystemId, String jobGubCode, String manageId, String issueId, String registDt)
			throws Exception {

		//TbXmlInfoDao xmlInfo = new TbXmlInfoDao(); //xml insert
		TbStatusHistDao status = new TbStatusHistDao();
		TbStatusHistDao status2 = new TbStatusHistDao();
		TbTaxBillInfoDao info = new TbTaxBillInfoDao();
		BatchQueryOff ifResult = new BatchQueryOff();
		BtmpTaxBillInfoDao flag = new BtmpTaxBillInfoDao();
		boolean rslt = false; 

		try {
			conn.setAutoCommit(false);

			/* 20100831 xml insert IO 문제로 막아놓음 */
			//xmlInfo.insert(conn, bizManageId, orgXml, issueDay, rValue,
			//				"BATCH");
			
			status.update2(conn, bizManageId, IOcode, issueDay);
			status2.insert2(conn, bizManageId, IOcode, issueDay, statusCode,
					statusDesc);
			info.update(conn, bizManageId, IOcode, issueDay);
			ifResult.insertIfTaxBillResultInfo(conn, realSystemId, jobGubCode, manageId, issueId, registDt, "05", "CD파일생성완료");
			flag.update(conn, rowId);

			conn.commit();
			rslt = true;

		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
			//xmlInfo = null;
			status = null;
			status2 = null;
			ifResult = null;
			info = null;
		}
        return rslt;
	}
 
	// **********************************매입
	// 서비스*******************************************************
	// TB_TRADE_ITEM_LIST 테이블에 등록
	public void setTbTradeItem(Connection conn, List tbTradeItemList)
			throws Exception {

		ItemListVo tbTradeItem = null;
		TbTradeItemListDao item = new TbTradeItemListDao();

		for (int i = 0; i < tbTradeItemList.size(); i++) {
			tbTradeItem = (ItemListVo) tbTradeItemList.get(i);
			try {
				item.insert(conn, tbTradeItem);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	} 

	// TB_TAX_BILL_INFO 테이블에 등록
	public void setTbTaxBillInfo(Connection conn, TbTaxBillInfoVo billInfo)
			throws Exception {
		TbTaxBillInfoDao info = new TbTaxBillInfoDao();
		try {
			info.insert(conn, billInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	// 완료된 xml 파일 등록
	public void setXml2(Connection conn, String bizManageId, String orgXml,
			String issuDay, String rValue) throws Exception {
		TbXmlInfoDao xmlInfo = new TbXmlInfoDao();
		try {
			xmlInfo.insert2(conn, bizManageId, orgXml, issuDay, rValue,
					"ONLINE2");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	// 전자서명 완료된 매출 xml 파일 등록
	public void setXmlForSelling(Connection conn, String IoCode, String bizManageId, String orgXml,
			String issuDay, String rValue, String RegId) throws Exception {
		TbXmlInfoDao xmlInfo = new TbXmlInfoDao();
		try {
			xmlInfo.InsertForSelling(conn, IoCode, bizManageId, orgXml, 
					                 issuDay, rValue, RegId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
//		//롤백 처리가 자동으로 안되어 롤백 처리 추가	
//		}catch(SQLException e) {
//		    try {
//				conn.rollback();
//				e.printStackTrace();
//				throw e;
//			    } catch (SQLException e1) {
//				e1.printStackTrace();
//			    }
//		}
	}
	
	// TB_STATUS_HIST INSERT
	public void insertStatus(Connection conn, String bizManageId,
			String IOcode, String issueDay, String statusCode, String statusDesc)
			throws Exception {

		TbStatusHistDao status = new TbStatusHistDao();
		try {
			status.insert2(conn, bizManageId, IOcode, issueDay, statusCode,
					statusDesc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// TAX_BUSINESS_BILL_INFO 테이블에 등록
	public int insertTaxBusinessBillInfo(String targetmonth){
	Connection conn = null;
	int rtn = -1;
	TaxBusinessBillInfoDao info = new TaxBusinessBillInfoDao();
		try {
			conn = new Dbcon().getConnection();
			rtn = info.insert(conn,targetmonth);// rtn 값은 insert 처리한 행의 갯수를 반환함.
			
		} catch (Exception e) {
			e.printStackTrace();
			rtn = -1;
		}finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
					rtn = -1;
				}
		}
	     return rtn;
	}
	
	// TAX_BUSINESS_ITEM_LIST 테이블에 등록
	public int insertTaxBusinessItemList(String targetmonth){
	Connection conn = null;
	int rtn = -1;
	TaxBusinessItemListDao info = new TaxBusinessItemListDao();
		try {
			conn = new Dbcon().getConnection();
			rtn = info.insert(conn,targetmonth);// rtn 값은 insert 처리한 행의 갯수를 반환함.
			
		} catch (Exception e) {
			e.printStackTrace();
			rtn = -1;
		}finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
					rtn = -1;
				}
		}
	     return rtn;
	}
	
	// **********************************매입
	// 서비스*******************************************************
	public static void main(String[] args) {
		Connection conn = null;
		try {
			conn = new Dbcon().getConnection();
			String contents[] = new TaxXmlService().getXML(conn, "AAAK19AAkAAAC8pAAK");
			
			System.out.println(contents[1]);
			System.out.println(contents[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
					System.out.println(e);
				}
		}
	}
	
	// 그룹차수 정보 등록
	public int setGroupNo(TbBatchConfigVo configInfo)
			throws Exception {
		Connection conn = null;
		int rtn = -1;

		TbTaxBillInfoDao tbTaxbill = new TbTaxBillInfoDao();

		try {
			conn = new Dbcon().getConnection();
			rtn = tbTaxbill.updateGroupNo(conn, configInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("그룹차수 업데이트 완료 건수 : "+rtn);
		return rtn;
	}

}
