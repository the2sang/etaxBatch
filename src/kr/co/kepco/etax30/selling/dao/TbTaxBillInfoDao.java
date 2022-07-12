package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import kr.co.kepco.etax30.common.Env;
//import kr.co.kepco.etax30.log.LoggableStatement;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.util.CommUtil;

/**
 * 
 * @author Yang,Hyungkong
 * 
 */
public class TbTaxBillInfoDao {
	// Logger logger = Logger.getLogger(TbTaxBillInfoDao.class);
	final static String OFF_MODIFY_DT = CommProperties
			.getString("OFF_MODIFY_DT");

	private static StringBuffer QUERY = new StringBuffer()
			.append(
					"SELECT 				                                                                                    \n")
			.append(
					"BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,					                    \n")
			.append(
					"BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,									                \n")
			.append(
					"IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,			            \n")
			.append(
					"INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,						                    \n")
			.append(
					"INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,						                    \n")
			.append(
					"INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,								                            \n")
			.append(
					"INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,								                            \n")
			.append(
					"INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,					                \n")
			.append(
					"INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,		                \n")
			.append(
					"INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,         \n")
			.append(
					"INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,         \n")
			.append(
					"BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,					                                \n")
			.append(
					"BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,					                                \n")
			.append(
					"BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,								                                \n")
			.append(
					"BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,								                                \n")
			.append(
					"PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,			                                \n")
			.append(
					"PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,			                                \n")
			.append(
					"CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,					                                \n")
			.append(
					"STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID, 									                                \n")
			.append(
					"ELECTRONIC_REPORT_YN,REGIST_DT, MODIFY_DT, REGIST_ID,MODIFY_ID, UPPER_MANAGE_ID	                        \n")
			.append(
					"FROM TB_TAX_BILL_INFO											 			                                \n")
			.append(
					"WHERE ROWID = CHARTOROWID(?)		                                                                        \n")
			.append(
					"AND   ISSUE_DAY BETWEEN ? || '01' AND TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')    				\n");

	/**
	 * rowId에 해당하는 세금계산서 정보 SELECT
	 * 
	 * @param conn
	 * @param rowId
	 * @param targetMonth
	 * @return
	 * @throws SQLException
	 */
	public TbTaxBillInfoVo select(Connection conn, String rowId,
			String targetMonth) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		TbTaxBillInfoVo tbTaxBill = null;
		CommUtil.logWriter(String.valueOf(QUERY), 1);
		try {
			pstmt = conn.prepareStatement(QUERY.toString());
			pstmt.setString(1, rowId);
			pstmt.setString(2, targetMonth);
			pstmt.setString(3, targetMonth);

			rs = pstmt.executeQuery();

			tbTaxBill = new TbTaxBillInfoVo();
			if (rs.next()) {
				tbTaxBill = makeTbTaxBill(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
		}
		return tbTaxBill;
	}

	/**
	 * VO 만들기
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected TbTaxBillInfoVo makeTbTaxBill(ResultSet rs) throws SQLException {

		TbTaxBillInfoVo tbTaxBill = new TbTaxBillInfoVo();
		tbTaxBill.setBiz_manage_id(rs.getString("BIZ_MANAGE_ID"));
		tbTaxBill.setSvc_manage_id(rs.getString("SVC_MANAGE_ID"));
		tbTaxBill.setIssue_dt(rs.getString("ISSUE_DT"));
		tbTaxBill.setSignature(rs.getString("SIGNATURE"));
		tbTaxBill.setIssue_id(rs.getString("ISSUE_ID"));
		tbTaxBill.setIssue_day(rs.getString("ISSUE_DAY"));
		tbTaxBill.setBill_type_code(rs.getString("BILL_TYPE_CODE"));
		tbTaxBill.setPurpose_code(rs.getString("PURPOSE_CODE"));
		tbTaxBill.setAmendment_code(rs.getString("AMENDMENT_CODE"));
		tbTaxBill.setDescription(rs.getString("DESCRIPTION"));
		tbTaxBill.setImport_doc_id(rs.getString("IMPORT_DOC_ID"));
		tbTaxBill.setImport_period_start_day(rs
				.getString("IMPORT_PERIOD_START_DAY"));
		tbTaxBill.setImport_period_end_day(rs
				.getString("IMPORT_PERIOD_END_DAY"));
		tbTaxBill.setImport_item_quantity(rs.getInt("IMPORT_ITEM_QUANTITY"));
		tbTaxBill.setInvoicer_party_id(rs.getString("INVOICER_PARTY_ID"));
		tbTaxBill.setInvoicer_tax_regist_id(rs
				.getString("INVOICER_TAX_REGIST_ID"));
		tbTaxBill.setInvoicer_party_name(rs.getString("INVOICER_PARTY_NAME"));
		tbTaxBill.setInvoicer_ceo_name(rs.getString("INVOICER_CEO_NAME"));
		tbTaxBill.setInvoicer_addr(rs.getString("INVOICER_ADDR"));
		tbTaxBill.setInvoicer_type(rs.getString("INVOICER_TYPE"));
		tbTaxBill.setInvoicer_class(rs.getString("INVOICER_CLASS"));
		tbTaxBill.setInvoicer_contact_depart(rs
				.getString("INVOICER_CONTACT_DEPART"));
		tbTaxBill.setInvoicer_contact_name(rs
				.getString("INVOICER_CONTACT_NAME"));
		tbTaxBill.setInvoicer_contact_phone(rs
				.getString("INVOICER_CONTACT_PHONE"));
		tbTaxBill.setInvoicer_contact_email(rs
				.getString("INVOICER_CONTACT_EMAIL"));
		tbTaxBill.setInvoicee_business_type_code(rs
				.getString("INVOICEE_BUSINESS_TYPE_CODE"));
		tbTaxBill.setInvoicee_party_id(rs.getString("INVOICEE_PARTY_ID"));
		tbTaxBill.setInvoicee_tax_regist_id(rs
				.getString("INVOICEE_TAX_REGIST_ID"));
		tbTaxBill.setInvoicee_party_name(rs.getString("INVOICEE_PARTY_NAME"));
		tbTaxBill.setInvoicee_ceo_name(rs.getString("INVOICEE_CEO_NAME"));
		tbTaxBill.setInvoicee_addr(rs.getString("INVOICEE_ADDR"));
		tbTaxBill.setInvoicee_type(rs.getString("INVOICEE_TYPE"));
		tbTaxBill.setInvoicee_class(rs.getString("INVOICEE_CLASS"));
		tbTaxBill.setInvoicee_contact_depart1(rs
				.getString("INVOICEE_CONTACT_DEPART1"));
		tbTaxBill.setInvoicee_contact_name1(rs
				.getString("INVOICEE_CONTACT_NAME1"));
		tbTaxBill.setInvoicee_contact_phone1(rs
				.getString("INVOICEE_CONTACT_PHONE1"));
		tbTaxBill.setInvoicee_contact_email1(rs
				.getString("INVOICEE_CONTACT_EMAIL1"));
		tbTaxBill.setInvoicee_contact_depart2(rs
				.getString("INVOICEE_CONTACT_DEPART2"));
		tbTaxBill.setInvoicee_contact_name2(rs
				.getString("INVOICEE_CONTACT_NAME2"));
		tbTaxBill.setInvoicee_contact_phone2(rs
				.getString("INVOICEE_CONTACT_PHONE2"));
		tbTaxBill.setInvoicee_contact_email2(rs
				.getString("INVOICEE_CONTACT_EMAIL2"));
		tbTaxBill.setBroker_party_id(rs.getString("BROKER_PARTY_ID"));
		tbTaxBill.setBroker_tax_regist_id(rs.getString("BROKER_TAX_REGIST_ID"));
		tbTaxBill.setBroker_party_name(rs.getString("BROKER_PARTY_NAME"));
		tbTaxBill.setBroker_ceo_name(rs.getString("BROKER_CEO_NAME"));
		tbTaxBill.setBroker_addr(rs.getString("BROKER_ADDR"));
		tbTaxBill.setBroker_type(rs.getString("BROKER_TYPE"));
		tbTaxBill.setBroker_class(rs.getString("BROKER_CLASS"));
		tbTaxBill.setBroker_contact_depart(rs
				.getString("BROKER_CONTACT_DEPART"));
		tbTaxBill.setBroker_contact_name(rs.getString("BROKER_CONTACT_NAME"));
		tbTaxBill.setBroker_contact_phone(rs.getString("BROKER_CONTACT_PHONE"));
		tbTaxBill.setBroker_contact_email(rs.getString("BROKER_CONTACT_EMAIL"));
		tbTaxBill.setPayment_type_code1(rs.getString("PAYMENT_TYPE_CODE1"));
		tbTaxBill.setPay_amount1(rs.getLong("PAY_AMOUNT1"));
		tbTaxBill.setPayment_type_code2(rs.getString("PAYMENT_TYPE_CODE2"));
		tbTaxBill.setPay_amount2(rs.getLong("PAY_AMOUNT2"));
		tbTaxBill.setPayment_type_code3(rs.getString("PAYMENT_TYPE_CODE3"));
		tbTaxBill.setPay_amount3(rs.getLong("PAY_AMOUNT3"));
		tbTaxBill.setPayment_type_code4(rs.getString("PAYMENT_TYPE_CODE4"));
		tbTaxBill.setPay_amount4(rs.getLong("PAY_AMOUNT4"));
		tbTaxBill.setCharge_total_amount(rs.getLong("CHARGE_TOTAL_AMOUNT"));
		tbTaxBill.setTax_total_amount(rs.getLong("TAX_TOTAL_AMOUNT"));
		tbTaxBill.setGrand_total_amount(rs.getLong("GRAND_TOTAL_AMOUNT"));
		tbTaxBill.setStatus_code(rs.getString("STATUS_CODE"));
		tbTaxBill.setJob_gub_code(rs.getString("JOB_GUB_CODE"));
		tbTaxBill.setRel_system_id(rs.getString("REL_SYSTEM_ID"));
		tbTaxBill.setElectronic_report_yn(rs.getString("ELECTRONIC_REPORT_YN"));
		tbTaxBill.setRegist_dt(rs.getString("REGIST_DT"));
		tbTaxBill.setModify_dt(rs.getString("MODIFY_DT"));
		tbTaxBill.setRegist_id(rs.getString("REGIST_ID"));
		tbTaxBill.setModify_id(rs.getString("MODIFY_ID"));
		tbTaxBill.setUpper_manage_id(rs.getString("UPPER_MANAGE_ID"));

		return tbTaxBill;
	}

	public TbTaxBillInfoVo makeTbTaxBillPb(ResultSet rs) throws SQLException {
		TbTaxBillInfoVo tbTaxBillPb = new TbTaxBillInfoVo();
		tbTaxBillPb = makeTbTaxBill(rs);
		return tbTaxBillPb;
	}

	/*
	 * public List selectKeyList(Connection conn, String targetMonth) throws
	 * SQLException{
	 * 
	 * StringBuffer query = new StringBuffer() .append("SELECT \n")
	 * .append("BIZ_MANAGE_ID \n") .append("FROM TB_TAX_BILL_INFO A \n")
	 * .append("WHERE MODIFY_DT BETWEEN TO_DATE(? || '01000000',
	 * 'YYYYMMDDHH24MISS') \n") .append(" AND
	 * TO_DATE(TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM'))+5, 'YYYYMMDD') ||
	 * '235959', 'YYYYMMDDHH24MISS') \n") .append("AND A.ONLINE_GUB_CODE = '2'
	 * \n") // .append("AND A.REL_SYSTEM_ID LIKE 'K1NCIS100%' \n") //
	 * .append("AND A.JOB_GUB_CODE IN ('310010') \n") .append("AND A.IO_CODE =
	 * '1' \n") .append("AND A.ISSUE_DAY BETWEEN ? || '01' AND
	 * TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD') \n");
	 * 
	 * 
	 * PreparedStatement pstmt = null; ResultSet rs = null;
	 * 
	 * 
	 * List tbTaxBillList = null; try{ pstmt =
	 * conn.prepareStatement(query.toString()); pstmt.setString(1, targetMonth);
	 * pstmt.setString(2, targetMonth); pstmt.setString(3, targetMonth);
	 * pstmt.setString(4, targetMonth); rs = pstmt.executeQuery();
	 * 
	 * tbTaxBillList = new ArrayList();
	 * 
	 * while(rs.next()){ tbTaxBillList.add(rs.getString("BIZ_MANAGE_ID")); } }
	 * catch (SQLException e) { e.printStackTrace(); throw e; }finally{ if (rs !=
	 * null) try{rs.close();}catch(SQLException e){System.out.println(e);} if
	 * (pstmt != null) try{pstmt.close();}catch(SQLException
	 * e){System.out.println(e);} } return tbTaxBillList; }
	 */

	/*
	 * public int getCount(Connection conn, String targetMonth){
	 * 
	 * StringBuffer query = new StringBuffer() .append("SELECT \n")
	 * .append("COUNT(1) AS COUNT \n") .append("FROM TB_TAX_BILL_INFO A \n")
	 * .append("WHERE MODIFY_DT BETWEEN TO_DATE(? || '01000000',
	 * 'YYYYMMDDHH24MISS') \n") .append(" AND
	 * TO_DATE(TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM'))+ ?, 'YYYYMMDD') ||
	 * '235959', 'YYYYMMDDHH24MISS') \n") .append("AND A.ONLINE_GUB_CODE = '2'
	 * \n") // .append("AND A.REL_SYSTEM_ID LIKE 'K1NCIS100%' \n") //
	 * .append("AND A.JOB_GUB_CODE IN ('310010') \n") .append("AND A.IO_CODE =
	 * '1' \n") .append("AND A.ISSUE_DAY BETWEEN ? || '01' AND
	 * TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD') \n");
	 *  // .append("AND REL_SYSTEM_ID <> 'K1NCIS1000' \n");
	 * 
	 * PreparedStatement pstmt = null; ResultSet rs = null;
	 * 
	 * int count = 0; try{ pstmt = conn.prepareStatement(query.toString());
	 * pstmt.setString(1, targetMonth); pstmt.setString(2, targetMonth);
	 * pstmt.setString(3, OFF_MODIFY_DT); pstmt.setString(4, targetMonth);
	 * pstmt.setString(5, targetMonth); rs = pstmt.executeQuery();
	 * 
	 * if(rs.next()){ count = rs.getInt("COUNT"); } } catch (SQLException e) {
	 * e.printStackTrace(); }finally{ if (rs != null)
	 * try{rs.close();}catch(SQLException e){System.out.println(e);} if (pstmt !=
	 * null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} }
	 * return count;
	 *  }
	 */
	/*
	 * public long selectAmount (Connection conn, String bizManageId){
	 * 
	 * long amount = 0; ResultSet rs = null; PreparedStatement pstmt = null;
	 * 
	 * try { pstmt = conn.prepareStatement("select GRAND_TOTAL_AMOUNT from
	 * tb_tax_bill_info where biz_manage_id = ? "); pstmt.setString(1,
	 * bizManageId); rs = pstmt.executeQuery();
	 * 
	 * if(rs.next()) { amount = rs.getInt("GRAND_TOTAL_AMOUNT"); } } catch
	 * (Exception e) { e.printStackTrace(); }finally{ if (rs != null)
	 * try{rs.close();}catch(SQLException e){System.out.println(e);} if ( pstmt !=
	 * null ) try{pstmt.close();}catch(Exception
	 * e){System.out.println(getClass().getName()+":"+e.getMessage());} } return
	 * amount;
	 *  }
	 */
	/*
	 * public long selectTotalAmount (Connection conn, String targetMonth){
	 * 
	 * StringBuffer query = new StringBuffer()
	 * 
	 * .append("SELECT \n") .append("SUM(GRAND_TOTAL_AMOUNT) AS
	 * GRAND_TOTAL_AMOUNT \n") .append("FROM TB_TAX_BILL_INFO A \n")
	 * .append("WHERE MODIFY_DT BETWEEN TO_DATE(? || '01000000',
	 * 'YYYYMMDDHH24MISS') \n") .append(" AND
	 * TO_DATE(TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM'))+5, 'YYYYMMDD') ||
	 * '235959', 'YYYYMMDDHH24MISS') \n") .append("AND A.ONLINE_GUB_CODE = '2'
	 * \n") .append("AND A.ISSUE_DAY BETWEEN ? || '01' AND
	 * TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD') \n"); // .append("AND
	 * REL_SYSTEM_ID LIKE 'K1NCIS100_' \n") // .append("AND JOB_GUB_CODE IN
	 * ('310010') \n");
	 * 
	 * 
	 * long amount = 0; ResultSet rs = null; PreparedStatement pstmt = null;
	 * 
	 * try { pstmt = conn.prepareStatement(query.toString()); pstmt.setString(1,
	 * targetMonth); pstmt.setString(2, targetMonth); pstmt.setString(3,
	 * targetMonth); pstmt.setString(4, targetMonth); rs = pstmt.executeQuery();
	 * 
	 * if(rs.next()) { amount = rs.getLong("GRAND_TOTAL_AMOUNT"); } } catch
	 * (Exception e) { e.printStackTrace(); }finally{ if (rs != null)
	 * try{rs.close();}catch(SQLException e){System.out.println(e);} if ( pstmt !=
	 * null ) try{pstmt.close();}catch(Exception
	 * e){System.out.println(getClass().getName()+":"+e.getMessage());} } return
	 * amount;
	 *  }
	 */

	/**
	 * 분배작업시작이력을 위해서 TB_TAX_BILL_INFO에서 대상 갯수와 금액을 가져옴
	 * 
	 * @param conn
	 * @param targetMonth
	 * @return
	 * @throws SQLException
	 */
	public String[] getTotalCountAmt(Connection conn, String targetMonth)
			throws SQLException {

		StringBuffer query = new StringBuffer()

				.append(
						" SELECT /*+ PARALLEL_INDEX(A, TB_TAX_BILL_INFO_1IX, 32) */                         \n")
				.append(
						"    COUNT(1) AS COUNT                         										\n")
				.append(
						"   ,SUM(A.CHARGE_TOTAL_AMOUNT + A.TAX_TOTAL_AMOUNT ) AS AMOUNT                     \n")
				.append(
						" FROM   TB_TAX_BILL_INFO A                                             	        \n")
				.append(
						" WHERE  1 > 0                                                           	        \n")
				.append(
						" AND    A.IO_CODE = '1'                                                   	        \n")
				.append(
						" AND    A.ISSUE_DAY BETWEEN ? || '01' AND                                	    	\n")
				.append(
						"                    TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')	    	\n")
				.append(
						" AND    A.STATUS_CODE = '01'                                            	        \n")
				.append(
						" AND    A.ONLINE_GUB_CODE = '2'                                          	        \n")
				.append(" AND    A.ELECTRONIC_REPORT_YN = 'N'   											\n");

		CommUtil.logWriter(String.valueOf(query), 1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String[] cntAmt = new String[2];

		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, targetMonth);
			pstmt.setString(2, targetMonth);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				cntAmt[0] = rs.getString("COUNT");
				cntAmt[1] = rs.getString("AMOUNT");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
		}
		return cntAmt;
	}

	/**
	 * 테이블에 XML 파일 INSERT 후 TB_TAX_BILL_INFO에 업데이트
	 * 
	 * @param conn
	 * @param bizManageId
	 * @param IOcode
	 * @param issueDay
	 * @return
	 * @throws Exception
	 */
	public int update(Connection conn, String bizManageId, String IOcode,
			String issueDay) throws Exception {

		PreparedStatement pstmt = null;
		int result = 0;

		StringBuffer query = new StringBuffer()
				/*
				 * .append(" UPDATE TB_TAX_BILL_INFO SET \n") .append("
				 * ELECTRONIC_REPORT_YN = 'Y', \n") .append(" STATUS_CODE =
				 * '05', \n") .append(" MODIFY_DT = SYSDATE \n") .append(" WHERE
				 * BIZ_MANAGE_ID = ? \n") .append(" AND IO_CODE = ? \n")
				 * .append(" AND ISSUE_DAY = ? \n");
				 */

				.append(
						"	 UPDATE  /*+ index(TB_TAX_BILL_INFO TB_TAX_BILL_INFO_PK) */ TB_TAX_BILL_INFO SET	                                                 \n")
				.append(
						"	 ELECTRONIC_REPORT_YN = 'Y',	                                              \n")
				.append(
						"	 STATUS_CODE = '05',	                                              \n")
				.append(
						"	 MODIFY_DT = SYSDATE	                                              \n")
				.append(
						"    WHERE IO_CODE = ?					                                    		           \n")
				.append(
						"    AND BIZ_MANAGE_ID = ?                           	            							           \n")
				.append(
						"    AND ISSUE_DAY = ?                         	            							           \n");

		CommUtil.logWriter(String.valueOf(query), 1);
		try {

			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, IOcode);
			pstmt.setString(2, bizManageId);
			pstmt.setString(3, issueDay);

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
		}

		return result;

	}

	/**
	 * 에러발생시 TB_TAX_BILL_INFO에 95로 업데이트
	 * 
	 * @param conn
	 * @param bizManageId
	 * @param IOcode
	 * @param issueDay
	 * @return
	 * @throws Exception
	 */
	public int updateError(Connection conn, String bizManageId, String IOcode,
			String issueDay) throws Exception {

		PreparedStatement pstmt = null;
		int result = 0;

		StringBuffer query = new StringBuffer()
				.append(
						"	 UPDATE TB_TAX_BILL_INFO SET	                                      \n")
				.append(
						"	 STATUS_CODE = '95',	                                              \n")
				.append(
						"	 MODIFY_DT = SYSDATE	                                              \n")
				.append(
						"    WHERE BIZ_MANAGE_ID = ?					                          \n")
				.append(
						"    AND IO_CODE = ?                           	            			  \n")
				.append(
						"    AND ISSUE_DAY = ?                         	            			  \n");

		CommUtil.logWriter(String.valueOf(query), 1);
		try {

			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, bizManageId);
			pstmt.setString(2, IOcode);
			pstmt.setString(3, issueDay);

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
			conn.setAutoCommit(true);
		}

		return result;

	}

	/**
	 * 매입에서 호출
	 * 
	 * @param conn
	 * @param billInfo
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, TbTaxBillInfoVo billInfo)
			throws Exception {
		PreparedStatement pstmt = null;
		int result = 0;
		int i = 1;

		StringBuffer query = new StringBuffer()

				.append("INSERT INTO TB_TAX_BILL_INFO 																			\n")
				.append(
						"    (IO_CODE, BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,                   \n")
				.append(
						"    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,                                         \n")
				.append(
						"    IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,               \n")
				.append(
						"    INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                    \n")
				.append(
						"    INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                   \n")
				.append(
						"    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                    \n")
				.append(
						"    INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                    \n")
				.append(
						"    INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                            \n")
				.append(
						"    INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,              \n")
				.append(
						"    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1, \n")
				.append(
						"    INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2, \n")
				.append(
						"    BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,                                          \n")
				.append(
						"    BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,                                           \n")
				.append(
						"    BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,                                                        \n")
				.append(
						"    BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                                                        \n")
				.append(
						"    PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                   \n")
				.append(
						"    PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                   \n")
				.append(
						"    CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                         \n")
				.append(
						"    STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID,                                                          \n")
				.append(
						"    ELECTRONIC_REPORT_YN,REGIST_id, MODIFY_id, REGIST_DT, MODIFY_DT, ADD_TAX_YN, ONLINE_GUB_CODE,      \n")
				.append(
						"    INVOICEE_GUB_CODE, UPPER_MANAGE_ID, ESERO_ISSUE_ID )                                                               \n")
				.append(
						" VALUES (                                                                                              \n")
				.append(" '2', ?,  																								\n")
				.append(
						"  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                                                     \n")
				.append(
						"  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                                               \n")
				.append(
						"  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,                                               \n")
				.append(
						"  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, '01', ?, ?, fn_GetTermsFlag(?), ?, ?, SYSDATE, SYSDATE, ?, ?, ?, ?, ? \n")
				.append(
						" )                                                                                                     \n");

		/*
		 * .append("INSERT INTO TB_TAX_BILL_INFO \n") .append(" (IO_CODE,
		 * BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID,
		 * ISSUE_DAY, \n") .append(" BILL_TYPE_CODE, PURPOSE_CODE,
		 * AMENDMENT_CODE, DESCRIPTION, \n") .append(" IMPORT_DOC_ID,
		 * IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,
		 * \n") .append(" INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID,
		 * INVOICER_PARTY_NAME, \n") .append(" INVOICER_CEO_NAME, INVOICER_ADDR,
		 * INVOICER_TYPE, INVOICER_CLASS, \n") .append("
		 * INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, \n") .append("
		 * INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL, \n") .append("
		 * INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID,
		 * INVOICEE_TAX_REGIST_ID, \n") .append(" INVOICEE_PARTY_NAME,
		 * INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS, \n")
		 * .append(" INVOICEE_CONTACT_DEPART1,
		 * INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1,
		 * INVOICEE_CONTACT_EMAIL1, \n") .append(" INVOICEE_CONTACT_DEPART2,
		 * INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2,
		 * INVOICEE_CONTACT_EMAIL2, \n") .append(" BROKER_PARTY_ID,
		 * BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME, \n") .append("
		 * BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS, \n")
		 * .append(" BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME, \n") .append("
		 * BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL, \n") .append("
		 * PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2, \n")
		 * .append(" PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4,
		 * PAY_AMOUNT4, \n") .append(" CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,
		 * GRAND_TOTAL_AMOUNT, \n") .append(" STATUS_CODE, JOB_GUB_CODE,
		 * REL_SYSTEM_ID, \n") .append(" ELECTRONIC_REPORT_YN,REGIST_id,
		 * MODIFY_id, REGIST_DT, MODIFY_DT, ADD_TAX_YN,ONLINE_GUB_CODE,
		 * INVOICEE_GUB_CODE) \n") .append(" VALUES ( \n") .append(" '2', ?,
		 * \n") .append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \n")
		 * .append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \n")
		 * .append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \n")
		 * .append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '01', ?, ?, ?, ?, ?, SYSDATE,
		 * SYSDATE, ?, ?, ? \n") .append(" ) \n");
		 */

		CommUtil.logWriter(String.valueOf(query), 1);
		try {
			pstmt = conn.prepareStatement(query.toString());

			/*
			 * System.out.println("query: "+query.toString());
			 * System.out.println( " billInfo.getSvc_manage_id() ) "+
			 * billInfo.getSvc_manage_id() ) ; System.out.println( "
			 * billInfo.getIssue_dt() ) "+ billInfo.getIssue_dt() ) ;
			 * System.out.println( " billInfo.getSignature() ) "+
			 * billInfo.getSignature() ) ; System.out.println( "
			 * billInfo.getIssue_id() ) "+ billInfo.getIssue_id() ) ;
			 * System.out.println( " billInfo.getIssue_day()) "+
			 * billInfo.getIssue_day()) ; System.out.println( "
			 * billInfo.getBill_type_code() ) "+ billInfo.getBill_type_code() ) ;
			 * System.out.println( " billInfo.getPurpose_code() ) "+
			 * billInfo.getPurpose_code() ) ; System.out.println( "
			 * billInfo.getAmendment_code() ) "+ billInfo.getAmendment_code() ) ;
			 * System.out.println( " billInfo.getDescription() ) "+
			 * billInfo.getDescription() ) ; System.out.println( "
			 * billInfo.getImport_doc_id() ) "+ billInfo.getImport_doc_id() ) ;
			 * System.out.println( " billInfo.getImport_period_start_day() ) "+
			 * billInfo.getImport_period_start_day() ) ; System.out.println( "
			 * billInfo.getImport_period_end_day() ) "+
			 * billInfo.getImport_period_end_day() ) ; System.out.println( "
			 * billInfo.getImport_item_quantity() ) "+
			 * billInfo.getImport_item_quantity() ) ; System.out.println( "
			 * billInfo.getInvoicer_party_id() ) "+
			 * billInfo.getInvoicer_party_id() ) ; System.out.println( "
			 * billInfo.getInvoicer_tax_regist_id() ) "+
			 * billInfo.getInvoicer_tax_regist_id() ) ; System.out.println( "
			 * billInfo.getInvoicer_party_name() ) "+
			 * billInfo.getInvoicer_party_name() ) ; System.out.println( "
			 * billInfo.getInvoicer_ceo_name() ) "+
			 * billInfo.getInvoicer_ceo_name() ) ; System.out.println( "
			 * billInfo.getInvoicer_addr() ) "+ billInfo.getInvoicer_addr() ) ;
			 * System.out.println( " billInfo.getInvoicer_type() ) "+
			 * billInfo.getInvoicer_type() ) ; System.out.println( "
			 * billInfo.getInvoicer_class() ) "+ billInfo.getInvoicer_class() ) ;
			 * System.out.println( " billInfo.getInvoicer_contact_depart() ) "+
			 * billInfo.getInvoicer_contact_depart() ) ; System.out.println( "
			 * billInfo.getInvoicer_contact_name() ) "+
			 * billInfo.getInvoicer_contact_name() ) ; System.out.println( "
			 * billInfo.getInvoicer_contact_phone() ) "+
			 * billInfo.getInvoicer_contact_phone() ) ; System.out.println( "
			 * billInfo.getInvoicer_contact_email() ) "+
			 * billInfo.getInvoicer_contact_email() ) ; System.out.println( "
			 * billInfo.getInvoicee_business_type_code() )"+
			 * billInfo.getInvoicee_business_type_code() ) ; System.out.println( "
			 * billInfo.getInvoicee_party_id() ) "+
			 * billInfo.getInvoicee_party_id() ) ; System.out.println( "
			 * billInfo.getInvoicee_tax_regist_id() ) "+
			 * billInfo.getInvoicee_tax_regist_id() ) ; System.out.println( "
			 * billInfo.getInvoicee_party_name() ) "+
			 * billInfo.getInvoicee_party_name() ) ; System.out.println( "
			 * billInfo.getInvoicee_ceo_name() ) "+
			 * billInfo.getInvoicee_ceo_name() ) ; System.out.println( "
			 * billInfo.getInvoicee_addr() ) "+ billInfo.getInvoicee_addr() ) ;
			 * System.out.println( " billInfo.getInvoicee_type() ) "+
			 * billInfo.getInvoicee_type() ) ; System.out.println( "
			 * billInfo.getInvoicee_class() ) "+ billInfo.getInvoicee_class() ) ;
			 * System.out.println( " billInfo.getInvoicee_contact_depart1() ) "+
			 * billInfo.getInvoicee_contact_depart1() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_name1() ) "+
			 * billInfo.getInvoicee_contact_name1() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_phone1() ) "+
			 * billInfo.getInvoicee_contact_phone1() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_email1() ) "+
			 * billInfo.getInvoicee_contact_email1() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_depart2() ) "+
			 * billInfo.getInvoicee_contact_depart2() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_name2() ) "+
			 * billInfo.getInvoicee_contact_name2() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_phone2() ) "+
			 * billInfo.getInvoicee_contact_phone2() ) ; System.out.println( "
			 * billInfo.getInvoicee_contact_email2() ) "+
			 * billInfo.getInvoicee_contact_email2() ) ; System.out.println( "
			 * billInfo.getBroker_party_id() ) "+ billInfo.getBroker_party_id() ) ;
			 * System.out.println( " billInfo.getBroker_tax_regist_id() ) "+
			 * billInfo.getBroker_tax_regist_id() ) ; System.out.println( "
			 * billInfo.getBroker_party_name() ) "+
			 * billInfo.getBroker_party_name() ) ; System.out.println( "
			 * billInfo.getBroker_ceo_name() ) "+ billInfo.getBroker_ceo_name() ) ;
			 * System.out.println( " billInfo.getBroker_addr() ) "+
			 * billInfo.getBroker_addr() ) ; System.out.println( "
			 * billInfo.getBroker_type() ) "+ billInfo.getBroker_type() ) ;
			 * System.out.println( " billInfo.getBroker_class() ) "+
			 * billInfo.getBroker_class() ) ; System.out.println( "
			 * billInfo.getBroker_contact_depart() ) "+
			 * billInfo.getBroker_contact_depart() ) ; System.out.println( "
			 * billInfo.getBroker_contact_name() ) "+
			 * billInfo.getBroker_contact_name() ) ; System.out.println( "
			 * billInfo.getBroker_contact_phone() ) "+
			 * billInfo.getBroker_contact_phone() ) ; System.out.println( "
			 * billInfo.getBroker_contact_email() ) "+
			 * billInfo.getBroker_contact_email() ) ; System.out.println( "
			 * billInfo.getPayment_type_code1() ) "+
			 * billInfo.getPayment_type_code1() ) ; System.out.println( "
			 * billInfo.getPay_amount1() ) "+ billInfo.getPay_amount1() ) ;
			 * System.out.println( " billInfo.getPayment_type_code2() ) "+
			 * billInfo.getPayment_type_code2() ) ; System.out.println( "
			 * billInfo.getPay_amount2() ) "+ billInfo.getPay_amount2() ) ;
			 * System.out.println( " billInfo.getPayment_type_code3() ) "+
			 * billInfo.getPayment_type_code3() ) ; System.out.println( "
			 * billInfo.getPay_amount3() ) "+ billInfo.getPay_amount3() ) ;
			 * System.out.println( " billInfo.getPayment_type_code4() ) "+
			 * billInfo.getPayment_type_code4() ) ; System.out.println( "
			 * billInfo.getPay_amount4() ) "+ billInfo.getPay_amount4() ) ;
			 * System.out.println( " billInfo.getCharge_total_amount() ) "+
			 * billInfo.getCharge_total_amount() ) ; System.out.println( "
			 * billInfo.getTax_total_amount() ) "+
			 * billInfo.getTax_total_amount() ) ; System.out.println( "
			 * billInfo.getGrand_total_amount() ) "+
			 * billInfo.getGrand_total_amount() ) ; System.out.println( "
			 * billInfo.getStatus_code() ) "+ billInfo.getStatus_code() ) ;
			 * System.out.println( " billInfo.getJob_gub_code() ) "+
			 * billInfo.getJob_gub_code() ) ; System.out.println( "
			 * billInfo.getRel_system_id() ) "+ billInfo.getRel_system_id() ) ;
			 * System.out.println( " billInfo.getElectronic_report_yn() ) "+
			 * billInfo.getElectronic_report_yn() ) ; System.out.println( "
			 * billInfo.getRegist_id() ) "+ billInfo.getRegist_id() ) ;
			 * System.out.println( " billInfo.getModify_id() ) "+
			 * billInfo.getModify_id() ) ; System.out.println( "
			 * billInfo.getAdd_tax_yn() ) "+ billInfo.getAdd_tax_yn() ) ;
			 * System.out.println( " billInfo.getOnline_gub_code() ) "+
			 * billInfo.getOnline_gub_code() ) ; System.out.println( "
			 * billInfo.getInvoicee_gub_code() ) "+
			 * billInfo.getInvoicee_gub_code() ) ; System.out.println( "
			 * billInfo.getUpper_manage_id() ) "+ billInfo.getUpper_manage_id() ) ;
			 */

			pstmt.setString(i++, billInfo.getBiz_manage_id());
			pstmt.setString(i++, billInfo.getSvc_manage_id());
			pstmt.setString(i++, billInfo.getIssue_dt());
			pstmt.setString(i++, billInfo.getSignature());
			pstmt.setString(i++, billInfo.getIssue_id());
			pstmt.setString(i++, billInfo.getIssue_day());
			pstmt.setString(i++, billInfo.getBill_type_code());
			pstmt.setString(i++, billInfo.getPurpose_code());
			pstmt.setString(i++, billInfo.getAmendment_code());
			pstmt.setString(i++, billInfo.getDescription());
			pstmt.setString(i++, billInfo.getImport_doc_id());
			pstmt.setString(i++, billInfo.getImport_period_start_day());
			pstmt.setString(i++, billInfo.getImport_period_end_day());
			pstmt.setLong(i++, billInfo.getImport_item_quantity());
			pstmt.setString(i++, billInfo.getInvoicer_party_id());
			pstmt.setString(i++, billInfo.getInvoicer_tax_regist_id());
			pstmt.setString(i++, billInfo.getInvoicer_party_name());
			pstmt.setString(i++, billInfo.getInvoicer_ceo_name());
			pstmt.setString(i++, billInfo.getInvoicer_addr());
			pstmt.setString(i++, billInfo.getInvoicer_type());
			pstmt.setString(i++, billInfo.getInvoicer_class());
			pstmt.setString(i++, billInfo.getInvoicer_contact_depart());
			pstmt.setString(i++, billInfo.getInvoicer_contact_name());
			pstmt.setString(i++, billInfo.getInvoicer_contact_phone());
			pstmt.setString(i++, billInfo.getInvoicer_contact_email());
			pstmt.setString(i++, billInfo.getInvoicee_business_type_code());
			pstmt.setString(i++, billInfo.getInvoicee_party_id());
			pstmt.setString(i++, billInfo.getInvoicee_tax_regist_id());
			pstmt.setString(i++, billInfo.getInvoicee_party_name());
			pstmt.setString(i++, billInfo.getInvoicee_ceo_name());
			pstmt.setString(i++, billInfo.getInvoicee_addr());
			pstmt.setString(i++, billInfo.getInvoicee_type());
			pstmt.setString(i++, billInfo.getInvoicee_class());
			pstmt.setString(i++, billInfo.getInvoicee_contact_depart1());
			pstmt.setString(i++, billInfo.getInvoicee_contact_name1());
			pstmt.setString(i++, billInfo.getInvoicee_contact_phone1());
			pstmt.setString(i++, billInfo.getInvoicee_contact_email1());
			pstmt.setString(i++, billInfo.getInvoicee_contact_depart2());
			pstmt.setString(i++, billInfo.getInvoicee_contact_name2());
			pstmt.setString(i++, billInfo.getInvoicee_contact_phone2());
			pstmt.setString(i++, billInfo.getInvoicee_contact_email2());
			pstmt.setString(i++, billInfo.getBroker_party_id());
			pstmt.setString(i++, billInfo.getBroker_tax_regist_id());
			pstmt.setString(i++, billInfo.getBroker_party_name());
			pstmt.setString(i++, billInfo.getBroker_ceo_name());
			pstmt.setString(i++, billInfo.getBroker_addr());
			pstmt.setString(i++, billInfo.getBroker_type());
			pstmt.setString(i++, billInfo.getBroker_class());
			pstmt.setString(i++, billInfo.getBroker_contact_depart());
			pstmt.setString(i++, billInfo.getBroker_contact_name());
			pstmt.setString(i++, billInfo.getBroker_contact_phone());
			pstmt.setString(i++, billInfo.getBroker_contact_email());
			pstmt.setString(i++, billInfo.getPayment_type_code1());
			pstmt.setLong(i++, billInfo.getPay_amount1());
			pstmt.setString(i++, billInfo.getPayment_type_code2());
			pstmt.setLong(i++, billInfo.getPay_amount2());
			pstmt.setString(i++, billInfo.getPayment_type_code3());
			pstmt.setLong(i++, billInfo.getPay_amount3());
			pstmt.setString(i++, billInfo.getPayment_type_code4());
			pstmt.setLong(i++, billInfo.getPay_amount4());
			pstmt.setLong(i++, billInfo.getCharge_total_amount());
			pstmt.setLong(i++, billInfo.getTax_total_amount());
			pstmt.setLong(i++, billInfo.getGrand_total_amount());
			// pstmt.setString(i++, billInfo.getStatus_code() ) ;
			pstmt.setString(i++, billInfo.getJob_gub_code());
			pstmt.setString(i++, billInfo.getRel_system_id());

			// pstmt.setString(i++, billInfo.getElectronic_report_yn() ) ;
			pstmt.setString(i++, billInfo.getInvoicer_party_id()); // fn_GetTermsFlag
																	// 로 대체함.

			pstmt.setString(i++, billInfo.getRegist_id());
			pstmt.setString(i++, billInfo.getModify_id());
			pstmt.setString(i++, billInfo.getAdd_tax_yn());
			pstmt.setString(i++, billInfo.getOnline_gub_code());
			pstmt.setString(i++, billInfo.getInvoicee_gub_code());
			pstmt.setString(i++, billInfo.getUpper_manage_id());
			// 2012.4.25 추가
			pstmt.setString(i++, billInfo.getEsero_issue_id());

			// System.out.prinln("\n ["+this.getClass()+"."+new
			// Exception().getStackTrace()[0].getMethodName()+"]\n " +
			// ((LoggableStatement)pstmt).getQueryString());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
		}

		return result;
	}

	//1일 ~ 15일까지 1차 16일 ~ 31일까지 2차로 차수 설정
	
	public int updateGroupNo(Connection conn, TbBatchConfigVo configInfo)
			throws Exception {

		PreparedStatement pstmt = null;
		int result = 0;
		//  20171012  유종일  날짜 별로 나누어 총 6 차수로 설정 
		StringBuffer query = new StringBuffer()
		
				.append("	 UPDATE IF_TAX_BILL_INFO SET	                                                 \n")
				//.append("	 GRP_NO = LPAD (TRUNC(TO_NUMBER (SUBSTR (ISSUE_DAY, 7, 2)) / 8, 0) + 1, 2, '0') \n");
				.append("	 GRP_NO =	                                                                     \n")
				.append("	 (CASE	                                                                         \n")
				.append("	 WHEN 	                                                                         \n")
				.append("	 (SUBSTR (ISSUE_DAY, 7, 2) >= '01' AND  SUBSTR (ISSUE_DAY, 7, 2) <= '06' )	     \n")
				.append("	 THEN '01'	                                                                     \n") 
				.append("	 WHEN 	                                                                         \n")
				.append("	 (SUBSTR (ISSUE_DAY, 7, 2) >= '07' AND  SUBSTR (ISSUE_DAY, 7, 2) <= '10' )	     \n")
				.append("	 THEN '02'	                                                                     \n")
				.append("	 WHEN 	                                                                         \n")
				.append("	 (SUBSTR (ISSUE_DAY, 7, 2) >= '11' AND  SUBSTR (ISSUE_DAY, 7, 2) <= '16' )	     \n")
				.append("	 THEN '03'	                                                                     \n")
				.append("	 WHEN 	                                                                         \n")
				.append("	 (SUBSTR (ISSUE_DAY, 7, 2) >= '17' AND  SUBSTR (ISSUE_DAY, 7, 2) <= '22' )	     \n")
				.append("	 THEN '04'	                                                                     \n")
				.append("	 WHEN	                                                                         \n")
				.append("	 (SUBSTR (ISSUE_DAY, 7, 2) >= '23' AND  SUBSTR (ISSUE_DAY, 7, 2) <= '27' )	     \n")
				.append("	 THEN '05'	                                                                     \n")
				.append("	 ELSE '06' 	                                                                     \n")
				.append("	 END)	                                                                         \n");
				

		CommUtil.logWriter(String.valueOf(query), 1);
		try {

			//conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query.toString());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
			conn.setAutoCommit(true);
		}

		return result;

	}

}
