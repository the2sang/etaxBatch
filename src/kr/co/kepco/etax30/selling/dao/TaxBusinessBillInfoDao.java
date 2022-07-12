package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.kepco.etax30.selling.util.CommProperties;
//import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.util.CommUtil;
/**
 *
 * 2011. 11. 24   by mopuim
 *
 */

public class TaxBusinessBillInfoDao {
		//Logger logger = Logger.getLogger(TbTaxBillInfoDao.class);
	final static String OFF_MODIFY_DT = CommProperties.getString("OFF_MODIFY_DT");

	/**
	 * 분배작업시작이력을 위해서 TB_TAX_BILL_INFO에서 대상 갯수와 금액을 가져옴
	 * @param conn
	 * @param targetMonth
	 * @return
	 * @throws SQLException
	 */
	public String[] getTotalCountAmt(Connection conn, String targetMonth) throws SQLException{

    	StringBuffer query = new StringBuffer()

    	.append(" SELECT /*+ PARALLEL_INDEX(A, TB_TAX_BILL_INFO_1IX, 32) */                         \n")
    	.append("    COUNT(1) AS COUNT                         										\n")
    	.append("   ,SUM(A.CHARGE_TOTAL_AMOUNT + A.TAX_TOTAL_AMOUNT ) AS AMOUNT                     \n")
    	.append(" FROM   TB_TAX_BILL_INFO A                                             	        \n")
    	.append(" WHERE  1 > 0                                                           	        \n")
    	.append(" AND    A.IO_CODE = '1'                                                   	        \n")
    	.append(" AND    A.ISSUE_DAY BETWEEN ? || '01' AND                                	    	\n")
    	.append("                    TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')	    	\n")
    	.append(" AND    A.STATUS_CODE = '01'                                            	        \n")
    	.append(" AND    A.ONLINE_GUB_CODE = '2'                                          	        \n")
    	.append(" AND    A.ELECTRONIC_REPORT_YN = 'N'   											\n");

		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;

    	String[] cntAmt = new String[2];

    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, targetMonth);
    		pstmt.setString(2, targetMonth);
    		rs = pstmt.executeQuery();

    		if(rs.next()){
    			cntAmt[0] = rs.getString("COUNT");
    			cntAmt[1] = rs.getString("AMOUNT");
    		}

    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw e;
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);}
    	}
    	return cntAmt;
    }
	
	/**
	 * 매입에서 호출
	 * @param conn
	 * @param billInfo
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, String targetmonth) throws  Exception {
		PreparedStatement pstmt = null;
		int result = 0;
		int i = 1;

		StringBuffer query = new StringBuffer()

		//.append("INSERT INTO TAX_BUSINESS_BILL_INFO                                                                                                     \n")
		.append("INSERT /*+ append */ INTO TAX_BUSINESS_BILL_INFO                                                                                                     \n")
		.append("(IO_CODE,                ISSUE_DAY,           BIZ_MANAGE_ID,          SVC_MANAGE_ID,              ISSUE_DT,                            \n")
		.append(" SIGNATURE,              ISSUE_ID,            BILL_TYPE_CODE,         PURPOSE_CODE,               AMENDMENT_CODE,                      \n")
		.append(" DESCRIPTION,            INVOICER_PARTY_ID,   INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,        INVOICER_CEO_NAME,                   \n")
		.append(" INVOICER_ADDR,          INVOICER_TYPE,       INVOICER_CLASS,         INVOICEE_BUSINESS_TYPE_CODE,INVOICEE_PARTY_ID,                   \n")
		.append(" INVOICEE_TAX_REGIST_ID, INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME,      INVOICEE_ADDR,              INVOICEE_TYPE,                       \n")
		.append(" INVOICEE_CLASS,         PAYMENT_TYPE_CODE1,  PAY_AMOUNT1,            CHARGE_TOTAL_AMOUNT,        TAX_TOTAL_AMOUNT,                    \n")
		.append(" GRAND_TOTAL_AMOUNT,     STATUS_CODE,         REL_SYSTEM_ID,          JOB_GUB_CODE,               ELECTRONIC_REPORT_YN,                \n")
		.append(" ADD_TAX_YN,             REGIST_DT,           MODIFY_DT,              REGIST_ID,                  MODIFY_ID,                           \n")
		.append(" ONLINE_GUB_CODE,        INVOICEE_GUB_CODE,   UPPER_MANAGE_ID,        CUST_NO,                    ESERO_SND_STAT,                      \n")
		.append(" ESERO_SND_CDDT)                                                                                                                       \n")                                                                                                        
		//.append(" (SELECT                                                                                                                               \n")
		.append(" (SELECT /*+ FULL(A) FULL(B) */                                                                                                        \n")
		.append(" A.IO_CODE,                A.ISSUE_DAY,           A.BIZ_MANAGE_ID,          A.SVC_MANAGE_ID,              A.ISSUE_DT,                  \n")
		.append(" A.SIGNATURE,             A. ISSUE_ID,            A.BILL_TYPE_CODE,         A.PURPOSE_CODE,               A.AMENDMENT_CODE,            \n")
		.append(" A.DESCRIPTION,            A.INVOICER_PARTY_ID,   A.INVOICER_TAX_REGIST_ID, A.INVOICER_PARTY_NAME,        A.INVOICER_CEO_NAME,         \n")
		.append(" A.INVOICER_ADDR,         A.INVOICER_TYPE,       A.INVOICER_CLASS,         A.INVOICEE_BUSINESS_TYPE_CODE, A.INVOICEE_PARTY_ID,         \n")
		.append(" A.INVOICEE_TAX_REGIST_ID, A.INVOICEE_PARTY_NAME, A.INVOICEE_CEO_NAME,      A.INVOICEE_ADDR,              A.INVOICEE_TYPE,             \n")
		.append(" A.INVOICEE_CLASS,         A.PAYMENT_TYPE_CODE1,  A.PAY_AMOUNT1,            A.CHARGE_TOTAL_AMOUNT,        A.TAX_TOTAL_AMOUNT,          \n")
		.append(" A.GRAND_TOTAL_AMOUNT,     A.STATUS_CODE,         A.REL_SYSTEM_ID,          A.JOB_GUB_CODE,               A.ELECTRONIC_REPORT_YN,      \n")
		.append(" A.ADD_TAX_YN,             A.REGIST_DT,           A.MODIFY_DT,              A.REGIST_ID,                  A.MODIFY_ID,                 \n")
		.append(" A.ONLINE_GUB_CODE,        A.INVOICEE_GUB_CODE,   A.UPPER_MANAGE_ID,        B.CUST_NO,                    null,                        \n")
		.append(" null                                                                                                                                  \n")
		.append(" FROM TB_TAX_BILL_INFO A, IF_TAX_BILL_INFO B                                                                                           \n")
		.append(" WHERE  A.ISSUE_ID = B.ISSUE_ID                                                                                                        \n")
		.append(" AND A.ISSUE_DAY = B.ISSUE_DAY                                                                                                         \n")
		.append(" AND A.IO_CODE = '1'                                                                                                                   \n")
		.append(" AND A.ISSUE_DAY BETWEEN ?|| '01' AND                                                                                           \n")
		.append("                    TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')                                                         \n")
		.append(" AND A.STATUS_CODE = '05'                                                                                                              \n")
		.append(" AND A.ONLINE_GUB_CODE = '2'                                                                                                           \n")                                                                 
		.append(" AND A.ELECTRONIC_REPORT_YN = 'Y'   )                                                                                                  \n");

		CommUtil.logWriter(String.valueOf(query),1);
		try{
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(i++, targetmonth )				;
			pstmt.setString(i++, targetmonth )				;

			result = pstmt.executeUpdate();//처리한 행의 갯수가 반환됨.

		}catch(Exception e) {
			System.out.println(e.getMessage());
			CommUtil.logWriter(String.valueOf(query),4);
			result = -1;
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){
				System.out.println(getClass().getName()+":"+e.toString());
				CommUtil.logWriter(String.valueOf(query),4);
				result = -1;
				}
		}

		return result;
	}
}
