/******************************************************************************
 * 저작권				: 

 * 프로젝트	명          : 전자세금계산서 프로젝트
 * 프로그램	명          : BtmpTaxBillInfoDao
 * 프로그램 아이디      : BtmpTaxBillInfoDao.java
 * 프로그램	개요	    : BTMP_TAX_BILL_INFO DAO 클래스
 * 관련	테이블		    : BTMP_TAX_BILL_INFO
 * 관련 모듈			: 
 * 작성자				: 양형공
 * 작성일자			    : 2009-12-01

 * 개정이력(성명 | 일자 | 내용)	: 양형공 | 2009-12-01 | (WEB DEV TEAM), v1.0,	최초작성

 * <METHOD>
 * - assignMachineThread()   ; MACHINE 과 THREAD 별로 작업 분류작업
 * .....
 * </METHOD>
******************************************************************************/

package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;

public class BtmpTaxBillInfoDao {
	
	/**
	 * MACHINE 과 THREAD 별로 작업 분류작업
	 * @param machine 머신 갯수
	 * @param thread  쓰레드 갯수
	 * @param targetMonth 처리월
	 * @param con
	 * @return boolean
	 * @throws SQLException
	 * @throws Exception
	 */
    public boolean assignMachineThread (int machine, int thread, String targetMonth, Connection con) throws SQLException, Exception{
    	PreparedStatement pstm=null;
    	boolean bl = false;
    	int i= 0;
    	try{
    		StringBuffer query= new StringBuffer()
    		
    		.append(" INSERT INTO BTMP_TAX_BILL_INFO                                                  			\n")
    		.append("  (ROW_NO                                                                        			\n")
    		.append("  ,MACHINE_IDX                                                            					\n")
    		.append("  ,THREAD_IDX                                                 								\n")
    		.append("  ,ROW_ID                                                                         			\n")
    		.append("  ,CHARGE_TOTAL_AMOUNT                                                                     \n")
    		.append("  ,TAX_TOTAL_AMOUNT                                                                        \n")
    		.append("  ,FLAG)                                                                                     \n")
    		.append(" SELECT /*+ INDEX_FFS(A TB_TAX_BILL_INFO_IX1) PARALLEL_INDEX(A, TB_TAX_BILL_INFO_IX1, 16) */ \n")
    		.append("        ROWNUM AS ROW_NO                                                                   \n")
    		.append("       ,MOD(ROWNUM, ? ) AS MACHINE_IDX                                        				\n")
    		.append("       ,TRUNC(MOD(ROWNUM, ?*?) / ?) AS THREAD_IDX 											\n")
    		.append("       ,A.ROWID AS ROW_ID                                                                  \n")
    		.append("       ,A.CHARGE_TOTAL_AMOUNT                                                              \n")
    		.append("       ,A.TAX_TOTAL_AMOUNT                                                                 \n")
    		.append("       ,'N'                                                                 				\n")
    		.append(" FROM   TB_TAX_BILL_INFO A                                                                 \n")
    		.append(" WHERE  1 > 0           																	\n")
    		.append(" AND    A.IO_CODE = '1'                                   				 					\n")//매입매출코드 1:매출, 2:매입
    		.append(" AND    A.ISSUE_DAY BETWEEN ?|| '01' AND           			 							\n")//작성일자 
    		.append("                    TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')           			\n")
    		.append(" AND    A.STATUS_CODE = '01'                            									\n")//진행상태
    		.append(" AND 	 A.ONLINE_GUB_CODE = '2'              												\n")//
    		.append(" AND    A.ELECTRONIC_REPORT_YN = 'N'                  										\n");//전자신고완료여부
//    		.append(" --AND    JOB_GUB_CODE = '310010'           												\n");//업무구분코드(전력매출,공사매출 등)
    		//System.out.println(query.toString());
    		CommUtil.logWriter(String.valueOf(query),1);
    		pstm = con.prepareStatement(query.toString());
    		pstm.setInt(++i, machine);
    		pstm.setInt(++i, thread);
    		pstm.setInt(++i, machine);
    		pstm.setInt(++i, machine);
    		pstm.setString(++i, targetMonth);
    		pstm.setString(++i, targetMonth);
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    	}catch(SQLException e){
    		e.printStackTrace();
    		throw e;
    	}catch(Exception e1){
    		e1.printStackTrace();
    		throw e1;
    	}finally{
    		Dbcon.close(pstm);
    		
    	}
    	return bl;
    }
    
//쓰레드별 데이터 조회쿼리
    /*public List selectList(Connection conn, String machine_idx, int thread_idx ) throws SQLException{
		
		StringBuffer query = new StringBuffer()
		.append(" SELECT  														\n")
		.append(" ROW_NO, MACHINE_IDX,THREAD_IDX, ROW_ID,  						\n")
		.append(" CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT 						\n")
		.append(" FROM   BTMP_TAX_BILL_INFO 									\n") 
		.append(" WHERE  MACHINE_IDX = ? 										\n") 
		.append(" AND    THREAD_IDX  = ?				   						\n")
		.append(" AND    FLAG  = 'N'				   							\n");
		   	
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List threadList = null;
		
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, machine_idx);
			pstmt.setInt(2, thread_idx);
			rs = pstmt.executeQuery();
			
			threadList = new ArrayList();
			
			while(rs.next()){
				threadList.add(makeTbTaxInfo(rs));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return threadList;
	}*/
    
    /*protected BtmpTaxBillInfoVo makeTbTaxInfo(ResultSet rs) throws SQLException{
		
    	BtmpTaxBillInfoVo tbTaxBill = new BtmpTaxBillInfoVo();
    	
		tbTaxBill.setRow_no(rs.getString("ROW_NO"));
		tbTaxBill.setMachine_idx(rs.getString("MACHINE_IDX"));
		tbTaxBill.setThread_idx(rs.getString("THREAD_IDX"));
		tbTaxBill.setRow_id(rs.getString("ROW_ID"));
		tbTaxBill.setCharge_total_amount(rs.getString("CHARGE_TOTAL_AMOUNT"));
		tbTaxBill.setTax_total_amount(rs.getString("TAX_TOTAL_AMOUNT"));
  
		return tbTaxBill;
	}*/
    
 
    /**
     *  쓰레드별 데이터 조회쿼리
     */
    public String[][] selectRowIdList(Connection conn, String machine_idx, int thread_idx ) throws SQLException{
		
		StringBuffer query = new StringBuffer()
		.append(" SELECT  														\n")
		.append(" ROW_ID, CHARGE_TOTAL_AMOUNT + TAX_TOTAL_AMOUNT AS AMOUNT  	\n")
		.append(" FROM   BTMP_TAX_BILL_INFO 									\n") 
		.append(" WHERE  MACHINE_IDX = ? 										\n") 
		.append(" AND    THREAD_IDX  = ?				   						\n")
		.append(" AND    FLAG  = 'N'				   							\n")
		//2015.7.02 처리 순서 조건 추가 
		.append(" ORDER BY ROW_NO                                               \n");
		   	
		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int threadSize = getCount(conn, machine_idx, thread_idx );
		String[][] threadArr = new String[threadSize][2];
		
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, machine_idx);
			pstmt.setInt(2, thread_idx);
			rs = pstmt.executeQuery();
			
			for(int i=0; i< threadSize; i++){
				if(rs.next()){
					threadArr[i][0] = rs.getString("ROW_ID");
					threadArr[i][1] = rs.getString("AMOUNT");
				}
			}
			
		} catch (SQLException e) { 
			e.printStackTrace();
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return threadArr;
	}
    
    /**
     * 분배된 ROW_ID를 String[]으로 반환
     * @param conn
     * @return String[]
     * @throws SQLException
     */
    public String[] selectRowIdList(Connection conn) throws SQLException{
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT  														\n")
    	.append(" ROW_ID													 	\n")
    	.append(" FROM BTMP_TAX_BILL_INFO 										\n");
    	    	
		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	int threadSize = getCount(conn);
    	String[] threadArr = new String[threadSize];
    	
    	try{
    		pstmt = conn.prepareStatement(query.toString());

    		rs = pstmt.executeQuery();
    		
    		for(int i=0; i< threadSize; i++){
    			if(rs.next()){
    				threadArr[i] = rs.getString("ROW_ID");
    			}
    		}
    		
    	} catch (SQLException e) { 
    		e.printStackTrace();
    		throw e;
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return threadArr;
    }
    /**
     * 분배된 ROW_ID를 String[]으로 반환
     * @param conn
     * @return String[]
     * @throws SQLException
     */
    public String[] selectRowIdListFlagY(Connection conn) throws SQLException{
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT  														\n")
    	.append(" ROW_ID													 	\n")
    	.append(" FROM BTMP_TAX_BILL_INFO 										\n")
    	.append(" WHERE FLAG = 'Y' 												\n");
    	    	
		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	int threadSize = getCountFlagY(conn);
    	String[] threadArr = new String[threadSize];
    	
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		
    		rs = pstmt.executeQuery();
    		
    		for(int i=0; i< threadSize; i++){
    			if(rs.next()){
    				threadArr[i] = rs.getString("ROW_ID");
    			}
    		}
    		
    	} catch (SQLException e) { 
    		e.printStackTrace();
    		throw e;
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return threadArr;
    }
    /**
     * 총갯수 반환
     * @param conn
     * @return int
     */
    public int getCount(Connection conn){
		
		StringBuffer query = new StringBuffer()
		.append(" SELECT /*+ FULL(A) PARALLEL(A 16) */                      \n")
		.append("        COUNT(A.ROW_ID) AS CNT         					\n")
		.append(" FROM   BTMP_TAX_BILL_INFO A  								\n");									                      
		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		int count = 0;
		try{
			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				count = rs.getInt("CNT");
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return count;
	
	}
    /**
     * 완료된 총갯수 반환
     * @param conn
     * @return int
     */
    public int getCountFlagY(Connection conn){
    	
    	StringBuffer query = new StringBuffer()
		.append(" SELECT /*+ FULL(A) PARALLEL(A 16) */                      \n")
		.append("        COUNT(A.ROW_ID) AS CNT         					\n")
		.append(" FROM   BTMP_TAX_BILL_INFO A  								\n")
		.append(" WHERE  A.FLAG = 'Y' 										\n");								                      
    	
		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	int count = 0;
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			count = rs.getInt("CNT");
    		}
    	} catch (SQLException e) { 
    		e.printStackTrace();
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return count;
    	
    }
    /**
     * 해당 머신, 해당 쓰레드의 처리되지않은 총갯수를 반환
     * @param conn
     * @param machine_idx 머신 넘버
     * @param thread_idx 쓰레드 넘버
     * @return int
     */
    public int getCount(Connection conn, String machine_idx, int thread_idx ){
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT  														\n")
    	.append(" COUNT(ROW_ID) AS CNT 										\n")
    	.append(" FROM   BTMP_TAX_BILL_INFO 									\n") 
    	.append(" WHERE  MACHINE_IDX = ? 										\n") 
    	.append(" AND    THREAD_IDX  = ?				   						\n")
    	.append(" AND    FLAG  = 'N'				   							\n");
    	    	
		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	int count = 0;
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, machine_idx);
    		pstmt.setInt(2, thread_idx);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			count = rs.getInt("CNT");
    		}
    	} catch (SQLException e) { 
    		e.printStackTrace();
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return count;
    	
    }
    /**
     * 해당머신의 총금액 합을 반환
     * @param conn
     * @param machine_idx 머신 넘버
     * @return long
     */
    public long getAmount(Connection conn, String machine_idx){
    	
    	StringBuffer query = new StringBuffer()
		.append(" SELECT  														\n")
		.append(" SUM(CHARGE_TOTAL_AMOUNT+TAX_TOTAL_AMOUNT) AS TOTAL     		\n")
		.append(" FROM   BTMP_TAX_BILL_INFO 									\n") 
		.append(" WHERE  MACHINE_IDX = ? 										\n");
											                      		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		long amt = 0;
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, machine_idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				amt = rs.getLong("TOTAL");
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return amt;
    	
    }
    /**
     * 해당 머신의 처리개수를 반환
     * @param conn
     * @param machine_idx 머신넘버
     * @return long
     */
    public long getCount(Connection conn, String machine_idx){
    	
    	StringBuffer query = new StringBuffer()
		.append(" SELECT  														\n")
		.append(" COUNT(1)      												\n")
		.append(" FROM   BTMP_TAX_BILL_INFO 									\n") 
		.append(" WHERE  MACHINE_IDX = ? 										\n");
											                      
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		long count = 0;
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, machine_idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				count = rs.getLong(1);
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return count;
    	
    }
    /**
     * 해당 머신의 작업 개수와 금액의 합을 반환
     * @param conn
     * @param machine_idx 머신넘버
     * @return long[]
     */
    public long[] getCountAmountTotal(Connection conn){
    	
    	StringBuffer query = new StringBuffer()
		.append(" SELECT /*+ INDEX_FFS(A BTMP_TAX_BILL_INFO_IX1) 						\n")
		.append("            PARALLEL_INDEX(A, BTMP_TAX_BILL_INFO_IX1, 16) */           \n")
		.append("         COUNT(1) AS CNT                								\n")
		.append("        ,SUM(A.CHARGE_TOTAL_AMOUNT+A.TAX_TOTAL_AMOUNT) AS TOTAL   		\n") 
		.append(" FROM   BTMP_TAX_BILL_INFO  A        									\n");
											                      		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		long[] totalBtmp = new long[2];
		try{
			pstmt = conn.prepareStatement(query.toString());

			rs = pstmt.executeQuery();
			
			if(rs.next()){
				totalBtmp[0] = rs.getLong("CNT");
				totalBtmp[1] = rs.getLong("TOTAL");
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return totalBtmp;
    	
    }
    /**
     * 해당 머신의 작업 개수와 금액의 합을 반환
     * @param conn
     * @param machine_idx 머신넘버
     * @return long[]
     */
    public long[] getCountAmount(Connection conn, String machine_idx){
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT /*+ INDEX_FFS(A BTMP_TAX_BILL_INFO_IX1) 						\n")
    	.append("            PARALLEL_INDEX(A, BTMP_TAX_BILL_INFO_IX1, 16) */           \n")
    	.append("         COUNT(1) AS CNT                								\n")
    	.append("        ,SUM(A.CHARGE_TOTAL_AMOUNT+A.TAX_TOTAL_AMOUNT) AS TOTAL   		\n") 
    	.append(" FROM   BTMP_TAX_BILL_INFO  A        									\n")
    	.append(" WHERE  A.MACHINE_IDX = ?					   							\n");
    	
		CommUtil.logWriter(String.valueOf(query),1);    	
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	long[] totalBtmp = new long[2];
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, machine_idx);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			totalBtmp[0] = rs.getLong("CNT");
    			totalBtmp[1] = rs.getLong("TOTAL");
    		}
    	} catch (SQLException e) { 
    		e.printStackTrace();
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return totalBtmp;
    	
    }
    
    /**
     * flag가 'Y'인 해당 머신의 작업 개수와 금액의 합을 반환
     * @param conn
     * @param machine_idx 머신넘버
     * @return long[]
     */
    public long[] getCountAmountFlagY(Connection conn, String machine_idx){
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT /*+ INDEX_FFS(A BTMP_TAX_BILL_INFO_IX1) 								\n")
    	.append("            PARALLEL_INDEX(A, BTMP_TAX_BILL_INFO_IX1, 16) */              		\n")
    	.append("        COUNT(1) AS CNT                										\n")
    	.append("       ,SUM(A.CHARGE_TOTAL_AMOUNT+A.TAX_TOTAL_AMOUNT) AS TOTAL   				\n") 
    	.append(" FROM   BTMP_TAX_BILL_INFO A     												\n")
    	.append(" WHERE  A.MACHINE_IDX = ?         												\n")
    	.append(" AND    A.FLAG = 'Y'  															\n");
    	
		CommUtil.logWriter(String.valueOf(query),1);    	
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	long[] totalBtmp = new long[2];
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, machine_idx);
    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			totalBtmp[0] = rs.getLong("CNT");
    			totalBtmp[1] = rs.getLong("TOTAL");
    		}
    	} catch (SQLException e) { 
    		e.printStackTrace();
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return totalBtmp;
    	
    }
    /**
     * flag가 'Y'인 전체 작업 개수와 금액의 합을 반환 
     * @param conn
     * @return
     */
    public long[] getCountAmountTotalFlagY(Connection conn){
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT /*+ INDEX_FFS(A BTMP_TAX_BILL_INFO_IX1) 								\n")
    	.append("            PARALLEL_INDEX(A, BTMP_TAX_BILL_INFO_IX1, 16) */              		\n")
    	.append("       COUNT(1) AS CNT,                										\n")
    	.append("       SUM(A.CHARGE_TOTAL_AMOUNT+A.TAX_TOTAL_AMOUNT) AS TOTAL   				\n") 
    	.append(" FROM   BTMP_TAX_BILL_INFO A     												\n")
    	.append(" WHERE  A.FLAG = 'Y'  															\n");
    	
  		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	long[] totalBtmp = new long[2];
    	try{
    		pstmt = conn.prepareStatement(query.toString());

    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			totalBtmp[0] = rs.getLong("CNT");
    			totalBtmp[1] = rs.getLong("TOTAL");
    		}
    	} catch (SQLException e) { 
    		e.printStackTrace();
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return totalBtmp;
    	
    }
    /**
     * 분배된 temp 테이블의 완료된 데이터의 FALG 값을 'Y'로 업데이트
     * @param conn
     * @param rowId
     * @return int
     * @throws Exception
     */
    public int update (Connection conn, String rowId) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE BTMP_TAX_BILL_INFO SET	                                               \n")
		.append("	 FLAG = 'Y'	                                                                   \n")
		.append("    WHERE ROW_ID = ?					                         		           \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, rowId);


			result = pstmt.executeUpdate(); 
			   
		}catch(Exception e) {
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
		
	}
    
    
    /**
     * BTMP_TAX_BILL_INFO 비움
     * @param conn
     * @throws SQLException
     */
    public void truncate(Connection conn) throws SQLException{
    	
    	PreparedStatement pstmt = null;
    	//int result = 0;
		
    	StringBuffer query = new StringBuffer()
		.append(" TRUNCATE TABLE BTMP_TAX_BILL_INFO  							\n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.executeUpdate();

		} catch (SQLException e) { 
			e.printStackTrace();
				throw e;
		}finally{
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		//return 0;	
    }

}
