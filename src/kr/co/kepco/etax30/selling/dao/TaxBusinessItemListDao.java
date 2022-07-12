package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;

//import kr.co.kepco.etax30.selling.vo.ItemListVo;
import kr.co.kepco.etax30.selling.util.CommUtil;

/**
 * 
 * @author Yang,Hyungkong
 *
 */
public class TaxBusinessItemListDao {
	
	/**
	 * 매입 상품 목록 
	 * @param conn
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, String targetmonth) throws  Exception { 
		PreparedStatement pstmt = null;
		int result = 0;
		int i = 1;

		StringBuffer query = new StringBuffer()

		//.append("INSERT INTO TAX_BUSINESS_ITEM_LIST                                               \n")                                                   
		.append("INSERT /*+ append */ INTO TAX_BUSINESS_ITEM_LIST                                               \n")                                                   
		.append("(IO_CODE,        ISSUE_DAY,     BIZ_MANAGE_ID,    SEQ_NO,                        \n")
		.append("PURCHASE_DAY,    ITEM_NAME,     UNIT_QUANTITY,    UNIT_AMOUNT,                   \n")
		.append("INVOICE_AMOUNT,  TAX_AMOUNT    )                                                 \n")                                                                                                                                                                         
		//.append(" (SELECT                                                                         \n")                                                   
		.append(" (SELECT /*+ FULL(A) FULL(B) */                                                  \n")                                                   
		.append(" A.IO_CODE,      A.ISSUE_DAY,   A.BIZ_MANAGE_ID,  A.SEQ_NO,                      \n")   
		.append(" A.PURCHASE_DAY, A.ITEM_NAME,   A.UNIT_QUANTITY,  A.UNIT_AMOUNT,                 \n")
		.append(" A.INVOICE_AMOUNT,A.TAX_AMOUNT                                                   \n")
		.append(" FROM TB_TRADE_ITEM_LIST A, TB_TAX_BILL_INFO B                                   \n")                                                     
		.append(" WHERE  A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                        \n")                                                             
		.append(" AND A.ISSUE_DAY = B.ISSUE_DAY                                                   \n")                                                   
		.append(" AND A.IO_CODE = '1'                                                             \n")                                                   
		.append(" AND A.ISSUE_DAY BETWEEN ?|| '01' AND                                            \n")                                                          
		.append("                    TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')          \n")                                                          
		.append(" AND B.STATUS_CODE = '05'                                                        \n")                                                   
		.append(" AND B.ONLINE_GUB_CODE = '2'                                                     \n")                                                                                                               
		.append(" AND B.ELECTRONIC_REPORT_YN = 'Y'   )                                            \n");                                                   

		CommUtil.logWriter(String.valueOf(query),1);
		try{
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(i++, targetmonth );
			pstmt.setString(i++, targetmonth );

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
