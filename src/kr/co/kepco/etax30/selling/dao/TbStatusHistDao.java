package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.kepco.etax30.selling.util.CommUtil;

public class TbStatusHistDao {
	
	/**
	 * 생성작업완료 판별
	 * @param conn
	 * @param targetMonth
	 * @return
	 * @throws SQLException
	 */
	public List checkCompletion(Connection conn, String targetMonth) throws SQLException{
    	
    	StringBuffer query = new StringBuffer()

    	.append("  SELECT WORK_CNT, WORK_AMT                                                            \n")
    	.append("  FROM   TB_BATCH_JOB_HIST                                                          	\n")
    	.append("  WHERE  YYYYMM = ?                                                              		\n")
    	.append("  AND    BATCH_JOB_CODE = '03'                                                      	\n")
    	.append("  AND    END_DT < TO_DATE('99991231115959', 'YYYYMMDDHH24MISS')                     	\n")
    	.append("  AND    START_DT >                                                                 	\n")
    	.append("                  (                                                                 	\n")
    	.append("                  SELECT MAX(START_DT)                                              	\n")
    	.append("                  FROM   TB_BATCH_JOB_HIST                                          	\n")
    	.append("                  WHERE  YYYYMM = ?                                              		\n")
    	.append("                  AND    BATCH_JOB_CODE = '02'                                      	\n")
    	.append("                  )  																	\n");

		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	List list = new ArrayList();
 	
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, targetMonth);
    		pstmt.setString(2, targetMonth);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			String[] sum = new String[2];
    			sum[0] = rs.getString("WORK_CNT");
    			sum[1] = rs.getString("WORK_AMT");
    			list.add(sum);
    			sum =null;
    		}
    		
    	} catch (SQLException e) { 
    		e.printStackTrace();
    		throw e;
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return list;
    }
	
	/**
	 * 서버0번의 압축 작업 시작 판별
	 * @param conn
	 * @param targetMonth
	 * @return
	 * @throws SQLException
	 */
	public List checkCreation(Connection conn, String targetMonth) throws SQLException{
		
		StringBuffer query = new StringBuffer()
		
		.append(" SELECT SUM(WORK_CNT) AS WORK_CNT ,SUM(WORK_AMT) AS WORK_AMT										\n")
		.append(" FROM   (																	\n")
		.append("         SELECT * FROM TB_BATCH_JOB_HIST									\n")
		.append("         WHERE BATCH_JOB_CODE = '02'										\n")
		.append("         AND   YYYYMM = ?													\n")
		.append("         AND   WORK_STAT = 'S'												\n")
		.append("         ORDER BY  START_DT DESC											\n")
		.append("        ) A																\n")
		.append(" WHERE ROWNUM = 1															\n")
		.append(" UNION ALL																	\n")
		.append(" SELECT SUM(WORK_CNT),SUM(WORK_AMT)   										\n")
		.append(" FROM   TB_BATCH_JOB_HIST													\n")
		.append(" WHERE (YYYYMM, BATCH_JOB_CODE, SERVER_NO, START_DT) IN 					\n")
		.append("         (																	\n")
		.append("             SELECT YYYYMM, BATCH_JOB_CODE, SERVER_NO, MAX(START_DT)		\n")
		.append("             FROM TB_BATCH_JOB_HIST										\n")
		.append("             WHERE BATCH_JOB_CODE = '03'									\n")
		.append("             AND   YYYYMM = ?												\n")
		.append("             AND   WORK_STAT = 'S'											\n")
		.append("             GROUP BY YYYYMM, BATCH_JOB_CODE, SERVER_NO					\n")
		.append("         ) 																\n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List list = new ArrayList();
		
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, targetMonth);
			pstmt.setString(2, targetMonth);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				String[] sum = new String[2];
				sum[0] = rs.getString("WORK_CNT");
				sum[1] = rs.getString("WORK_AMT");
				list.add(sum);
				sum =null;
			}
			
		} catch (SQLException e) { 
			e.printStackTrace();
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return list;
	}
	
	public String[] checkAssign(Connection conn, String targetMonth) throws SQLException{
    	
    	StringBuffer query = new StringBuffer()
    	.append(" SELECT DEST_CNT, WORK_CNT, DEST_AMT, WORK_AMT                      			\n")
    	.append(" FROM   (                                                    					\n")
    	.append("         SELECT * FROM TB_BATCH_JOB_HIST                      					\n")
    	.append("         WHERE BATCH_JOB_CODE = '02'                          					\n")
    	.append("         AND   YYYYMM = ?                                          			\n")
    	.append("         AND   WORK_STAT = 'S'                                   				\n")
    	.append("         ORDER BY  START_DT DESC                                  				\n")
    	.append("        ) A                                                   					\n")
    	.append(" WHERE ROWNUM = 1 																\n");

		CommUtil.logWriter(String.valueOf(query),1);
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	String[] value = new String[4];
 	
    	try{
    		pstmt = conn.prepareStatement(query.toString());
    		pstmt.setString(1, targetMonth);

    		rs = pstmt.executeQuery();
    		
    		if(rs.next()){
    			value[0] = rs.getString("DEST_CNT");
    			value[1] = rs.getString("WORK_CNT");
    			value[2] = rs.getString("DEST_AMT");
    			value[3] = rs.getString("WORK_AMT");

    		}
    		
    	} catch (SQLException e) { 
    		e.printStackTrace();
    		throw e;
    	}finally{
    		if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
    		if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
    	}
    	return value;
    }
	
	
	
	public int update (Connection conn, String bizManageId, String IOcode,  String issueDay) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_STATUS_HIST SET	                                                                                           \n")
		.append("	 AVL_END_DT = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')	                                                                   \n")
		.append("    WHERE BIZ_MANAGE_ID = ?									                                        		           \n")
		.append("    AND seq_no = (SELECT NVL(MAX(seq_no),0) FROM TB_STATUS_HIST WHERE BIZ_MANAGE_ID = ? AND IO_CODE = ? AND ISSUE_DAY = ?) \n")
		.append("    AND IO_CODE = ?                                        			            							           \n")
		.append("    AND ISSUE_DAY = ?                                        			            							           \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, bizManageId);
			pstmt.setString(2, bizManageId);
			pstmt.setString(3, IOcode);
			pstmt.setString(4, issueDay);
			pstmt.setString(5, IOcode);
			pstmt.setString(6, issueDay);

			result = pstmt.executeUpdate(); 
			 
			conn.commit();  
		}catch(Exception e) {
			conn.rollback(); 
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
			conn.setAutoCommit(true);
		}
		
		return result;
		
	}
	
	public int insert(Connection conn, String bizManageId, String IOcode,  String issueDay, String statusCode, String statusDesc ) throws  Exception { 
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()
		.append("	 INSERT INTO TB_STATUS_HIST ( 	    	                                                                          \n")
		.append("	     IO_CODE,			    		                                                                              \n")
		.append("	     ISSUE_DAY,			    		                                                                              \n")
		.append("	     BIZ_MANAGE_ID,			    	                                                                              \n")
		.append("	     SEQ_NO,			    			                                                                          \n")
		.append("	     AVL_BEGIN_DT,	    			                                                                              \n")
		.append("	     AVL_END_DT,		    			                                                                          \n")
		.append("	     STATUS_CODE,	    			                                                                              \n")
		.append("	     STATUS_DESC,	    			                                                                              \n")
		.append("	     REGIST_DT,		    			                                                                              \n")
		.append("	     REGIST_ID		    			                                                                              \n")
		.append("	 )                                                                                                                \n")
		.append("	 VALUES (?, ?, ?,             																                      \n")
		.append("	 (SELECT NVL( MAX(SEQ_NO), 0) + 1 FROM TB_STATUS_HIST WHERE BIZ_MANAGE_ID = ? AND IO_CODE = ? AND ISSUE_DAY = ?), \n")
		.append("	 TO_CHAR(SYSDATE + INTERVAL '00:01' MINUTE TO SECOND, 'YYYYMMDDHH24MISS'), '99991231235959', 						                                  \n")
		.append("	 ?, ?, SYSDATE, 'BATCH')							            			                                      \n");

		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, IOcode);
			pstmt.setString(2, issueDay);
			pstmt.setString(3, bizManageId);
			pstmt.setString(4, bizManageId);
			pstmt.setString(5, IOcode);
			pstmt.setString(6, issueDay);
			pstmt.setString(7, statusCode);
			pstmt.setString(8, statusDesc);
			
			result = pstmt.executeUpdate(); 
			 
			conn.commit();  
		}catch(Exception e) {
			conn.rollback(); 
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
			if ( conn != null) try{ conn.setAutoCommit(true);}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
	
	}
	
	public int update2 (Connection conn, String bizManageId, String IOcode,  String issueDay) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_STATUS_HIST SET	                                                                                           \n")
		.append("	 AVL_END_DT = TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')	                                                                   \n")
		.append("    WHERE BIZ_MANAGE_ID = ?									                                        		           \n")
		.append("    AND seq_no = (SELECT NVL(MAX(seq_no),0) FROM TB_STATUS_HIST WHERE BIZ_MANAGE_ID = ? AND IO_CODE = ? AND ISSUE_DAY = ?) \n")
		.append("    AND IO_CODE = ?                                        			            							           \n")
		.append("    AND ISSUE_DAY = ?                                        			            							           \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, bizManageId);
			pstmt.setString(2, bizManageId);
			pstmt.setString(3, IOcode);
			pstmt.setString(4, issueDay);
			pstmt.setString(5, IOcode);
			pstmt.setString(6, issueDay);

			result = pstmt.executeUpdate(); 
			   
		}catch(Exception e) {
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param conn
	 * @param bizManageId
	 * @param IOcode
	 * @param issueDay
	 * @param statusCode
	 * @param statusDesc
	 * @return
	 * @throws Exception
	 */	
	public int insert2(Connection conn, String bizManageId, String IOcode,  String issueDay, String statusCode, String statusDesc ) throws  Exception { 
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()
		.append("	 INSERT INTO TB_STATUS_HIST ( 	    	                                                                          \n")
		.append("	     IO_CODE,			    		                                                                              \n")
		.append("	     ISSUE_DAY,			    		                                                                              \n")
		.append("	     BIZ_MANAGE_ID,			    	                                                                              \n")
		.append("	     SEQ_NO,			    			                                                                          \n")
		.append("	     AVL_BEGIN_DT,	    			                                                                              \n")
		.append("	     AVL_END_DT,		    			                                                                          \n")
		.append("	     STATUS_CODE,	    			                                                                              \n")
		.append("	     STATUS_DESC,	    			                                                                              \n")
		.append("	     REGIST_DT,		    			                                                                              \n")
		.append("	     REGIST_ID		    			                                                                              \n")
		.append("	 )                                                                                                                \n")
		.append("	 VALUES (?, ?, ?,             																                      \n")
		.append("	 (SELECT NVL( MAX(SEQ_NO), 0) + 1 FROM TB_STATUS_HIST WHERE BIZ_MANAGE_ID = ? AND IO_CODE = ? AND ISSUE_DAY = ?), \n")
		.append("	 TO_CHAR(SYSDATE + INTERVAL '00:01' MINUTE TO SECOND, 'YYYYMMDDHH24MISS'), '99991231235959', 						                                  \n")
		.append("	 ?, ?, SYSDATE, 'BATCH')							            			                                      \n");

		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			
			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, IOcode);
			pstmt.setString(2, issueDay);
			pstmt.setString(3, bizManageId);
			pstmt.setString(4, bizManageId);
			pstmt.setString(5, IOcode);
			pstmt.setString(6, issueDay);
			pstmt.setString(7, statusCode);
			pstmt.setString(8, statusDesc);
			
			result = pstmt.executeUpdate(); 
			
 
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
		
	}

}
