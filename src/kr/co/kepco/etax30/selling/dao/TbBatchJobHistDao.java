package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import kr.co.kepco.etax30.selling.batchoffline.TaxXmlService;
//import kr.co.kepco.etax30.selling.util.Dbcon;
//import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;

public class TbBatchJobHistDao {
	
	public int updateEndDt (Connection conn, TbBatchJobHistVo batchJobHist) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_BATCH_JOB_HIST SET	                            		\n")
		.append("	 END_DT   	   		= 	SYSDATE,                                  	\n")
		.append("	 MODIFY_DT     		= 	SYSDATE                                 	\n")
		.append("    WHERE YYYYMM  		= 	?											\n")
		.append("    AND SERVER_NO 		= 	?									    	\n")
		.append("    AND BATCH_JOB_CODE = 	?      		           						\n")
		.append("    AND START_DT = TO_DATE(?,'YYYYMMDDHH24MISS')						 \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, batchJobHist.getYyyymm());
			pstmt.setString(2, batchJobHist.getServer_no());
			pstmt.setString(3, batchJobHist.getBatch_job_code());
			pstmt.setString(4, batchJobHist.getStart_dt());

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
	/**
	 * 
	 * @param conn
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public int update (Connection conn, TbBatchJobHistVo batchJobHist) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_BATCH_JOB_HIST SET	                            		\n")
		.append("	 END_DT   	   		= 	SYSDATE,                                  	\n")
		.append("	 MODIFY_DT     		= 	SYSDATE,                                 	\n")
		.append("	 DEST_CNT     		= 	?,                                 	\n")
		.append("	 DEST_AMT     		= 	?,                                 	\n")
		.append("	 WORK_CNT     		= 	?,                                 	\n")
		.append("	 WORK_AMT     		= 	?,                                 	\n")
		.append("	 WORK_STAT     		= 	?                                 	\n")
		.append("    WHERE YYYYMM  		= 	?											\n")
		.append("    AND GRP_NO 		= 	?									    	\n")
		.append("    AND SERVER_NO 		= 	?									    	\n")
		.append("    AND BATCH_JOB_CODE = 	?      		           						\n")
		.append("    AND START_DT = TO_DATE(?,'YYYYMMDDHH24MISS') 					\n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setLong(1, batchJobHist.getDest_cnt());
			pstmt.setDouble(2, batchJobHist.getDest_amt());
			pstmt.setLong(3, batchJobHist.getWork_cnt());
			pstmt.setDouble(4, batchJobHist.getWork_amt());
			pstmt.setString(5, batchJobHist.getWork_stat());
			pstmt.setString(6, batchJobHist.getYyyymm());
			pstmt.setString(7, batchJobHist.getGrp_no());
			pstmt.setString(8, batchJobHist.getServer_no());
			pstmt.setString(9, batchJobHist.getBatch_job_code());
			pstmt.setString(10, batchJobHist.getStart_dt());

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
	/**
	 * 
	 * @param conn
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public int updateCNT (Connection conn, TbBatchJobHistVo batchJobHist, long cnt) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_BATCH_JOB_HIST SET	                            		\n")
		//.append("	 END_DT   	   		= 	SYSDATE,                                  	\n")
		.append("	 WORK_CNT   	   	= 	WORK_CNT + ?,                               \n")
		.append("	 MODIFY_DT     		= 	SYSDATE                                 	\n")
		.append("    WHERE YYYYMM  		= 	?											\n")
		.append("    AND GRP_NO 		= 	?									    	\n")
		.append("    AND SERVER_NO 		= 	?									    	\n")
		.append("    AND BATCH_JOB_CODE = 	?      		           						\n")
		.append("    AND START_DT = TO_DATE(?,'YYYYMMDDHH24MISS')                       \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setLong(1, cnt);
			pstmt.setString(2, batchJobHist.getYyyymm());
			pstmt.setString(3, batchJobHist.getGrp_no());
			pstmt.setString(4, batchJobHist.getServer_no());
			pstmt.setString(5, batchJobHist.getBatch_job_code());
			pstmt.setString(6, batchJobHist.getStart_dt());

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
	/**
	 * 
	 * @param conn
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public int updateAMT (Connection conn, TbBatchJobHistVo batchJobHist, long amount) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_BATCH_JOB_HIST SET	                            		\n")
		//.append("	 END_DT   	   		= 	SYSDATE,                                  	\n")
		.append("	 WORK_AMT   	   	= 	WORK_AMT + ?,                               \n")
		.append("	 MODIFY_DT     		= 	SYSDATE                                 	\n")
		.append("    WHERE YYYYMM  		= 	?											\n")
		.append("    AND GRP_NO 		= 	?									    	\n")
		.append("    AND SERVER_NO 		= 	?									    	\n")
		.append("    AND BATCH_JOB_CODE = 	?      		           						\n")
		.append("    AND START_DT = TO_DATE(?,'YYYYMMDDHH24MISS')                       \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			//pstmt.setDouble(1, batchJobHist.getWork_amt());
			pstmt.setDouble(1, amount);
			pstmt.setString(2, batchJobHist.getYyyymm());
			pstmt.setString(3, batchJobHist.getGrp_no());
			pstmt.setString(4, batchJobHist.getServer_no());
			pstmt.setString(5, batchJobHist.getBatch_job_code());
			pstmt.setString(6, batchJobHist.getStart_dt());

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
	/**
	 * 상태값 F로 업데이트
	 * @param conn
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public int updateWorkStat (Connection conn, TbBatchJobHistVo batchJobHist) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;

		
		StringBuffer query = new StringBuffer()		
		.append("	 UPDATE TB_BATCH_JOB_HIST SET	                            		\n")
		.append("	 END_DT   	   		= 	SYSDATE,                                  	\n")
		.append("	 WORK_STAT   	   	= 	'F',                                  		\n")
		.append("	 MODIFY_DT     		= 	SYSDATE                                 	\n")
		.append("    WHERE YYYYMM  		= 	?											\n")
		.append("    AND SERVER_NO 		= 	?									    	\n")
		.append("    AND BATCH_JOB_CODE = 	?      		           						\n")
		.append("    AND START_DT = TO_DATE(?,'YYYYMMDDHH24MISS')                       \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, batchJobHist.getYyyymm());
			pstmt.setString(2, batchJobHist.getServer_no());
			pstmt.setString(3, batchJobHist.getBatch_job_code());
			pstmt.setString(4, batchJobHist.getStart_dt());

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

	/**
	 * 
	 * @param conn
	 * @param batchJobHist
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, TbBatchJobHistVo batchJobHist) throws  Exception {
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()
		.append("	 INSERT INTO TB_BATCH_JOB_HIST (         \n")
		.append("	     YYYYMM,                             \n")//1 getYyyymm
		.append("	     SERVER_NO,                          \n")//2 getServer_no
		.append("	     BATCH_JOB_CODE,                     \n")//3 getBatch_job_code
		.append("	     START_DT,                           \n")//SYSDATE
		.append("	     END_DT,                             \n")//TO_DATE(99991231235959,'YYYYMMDDHH24MISS')
		.append("	     DEST_CNT,                           \n")//4 getDest_cnt
		.append("	     WORK_CNT,                           \n")//"0"
		.append("	     DEST_AMT,                           \n")//5 getDest_amt
		.append("	     WORK_AMT,                           \n")//"0"
		.append("	     WORK_STAT,                          \n")//6 getWork_stat
		.append("	     REGIST_DT,                          \n")//SYSDATE
		.append("	     MODIFY_DT,                          \n")//SYSDATE 
		.append("	     GRP_NO                              \n") //7 getGrp_no
		.append("	 )                                       \n")
		.append("	 VALUES ( ?, ?, ?,                       \n")
		.append("	     SYSDATE, TO_DATE(99991231235959,'YYYYMMDDHH24MISS'),	\n")
		.append("	     ?, 0, ?, 0, ?,   					  \n")
		.append("	     SYSDATE, SYSDATE, ?)       	      \n");
							            			                                    
		CommUtil.logWriter(String.valueOf(query),1);
		
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, batchJobHist.getYyyymm());
			pstmt.setString(2, batchJobHist.getServer_no());
			pstmt.setString(3, batchJobHist.getBatch_job_code());
			pstmt.setLong(4, batchJobHist.getDest_cnt());
			pstmt.setDouble(5, batchJobHist.getDest_amt());
			pstmt.setString(6, batchJobHist.getWork_stat());
			pstmt.setString(7,batchJobHist.getGrp_no());
			
			result = pstmt.executeUpdate(); 
			 
			conn.commit();  
		}catch(Exception e) {
			conn.rollback(); 
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
			if ( conn != null) try{ conn.setAutoCommit(true);}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
	
	}
	public TbBatchJobHistVo select(Connection conn, TbBatchJobHistVo batchJobHist){
		StringBuffer query = new StringBuffer()
		.append(" SELECT  									                      	   \n")
		.append("  A.YYYYMM,        							                       \n")
		.append("  A.SERVER_NO,     							                       \n")
		.append("  A.BATCH_JOB_CODE,							                       \n")
		.append("  TO_CHAR(A.START_DT, 'YYYYMMDDHH24MISS') START_DT,                   \n")
		.append("  TO_CHAR(A.END_DT, 'YYYYMMDDHH24MISS') END_DT,                       \n")
		.append("  A.DEST_CNT,      							                       \n")
		.append("  A.WORK_CNT,      							                       \n")
		.append("  A.DEST_AMT,      							                       \n")
		.append("  A.WORK_AMT,      							                       \n")
		.append("  A.WORK_STAT,     							                       \n")
		.append("  A.REGIST_DT,     							                       \n")
		.append("  A.MODIFY_DT,      							                       \n")
		.append("  A.GRP_NO         							                       \n")
		.append("  FROM TB_BATCH_JOB_HIST A					                           \n")
		.append("  WHERE 1 > 0								                           \n")
		.append("  AND A.START_DT = (SELECT MAX(B.START_DT) FROM TB_BATCH_JOB_HIST B   \n")
		.append("  					WHERE B.YYYYMM         = ?  					   \n")
		.append("  					AND   B.GRP_NO      = ?			                   \n")
		.append("  					AND   B.SERVER_NO      = ?			               \n")
		.append("  					AND   B.BATCH_JOB_CODE = ?						)  \n")
		.append("  AND A.YYYYMM         = ?        							           \n")
		.append("  AND A.GRP_NO         = ?        							           \n")
		.append("  AND A.SERVER_NO      = ?    							               \n")
		.append("  AND A.BATCH_JOB_CODE = ?							                   \n")
		.append("  AND A.DEST_CNT       = ?     							           \n") 
		.append("  AND A.DEST_AMT       = ?     							           \n"); 
		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		TbBatchJobHistVo batchJob =  new TbBatchJobHistVo();
		try{
			pstmt = conn.prepareStatement(query.toString());

			pstmt.setString(1, batchJobHist.getYyyymm());
			pstmt.setString(2, batchJobHist.getGrp_no());
			pstmt.setString(3, batchJobHist.getServer_no());
			pstmt.setString(4, batchJobHist.getBatch_job_code());
			pstmt.setString(5, batchJobHist.getYyyymm());
			pstmt.setString(6, batchJobHist.getGrp_no());
			pstmt.setString(7, batchJobHist.getServer_no());
			pstmt.setString(8, batchJobHist.getBatch_job_code());
			pstmt.setLong(9, batchJobHist.getDest_cnt());
			pstmt.setDouble(10, batchJobHist.getDest_amt());
			
			rs = pstmt.executeQuery();
					
			if(rs.next()){

				batchJob.setYyyymm(rs.getString("YYYYMM"));
				batchJob.setServer_no(rs.getString("SERVER_NO"));
				batchJob.setBatch_job_code(rs.getString("BATCH_JOB_CODE"));
				batchJob.setStart_dt(rs.getString("START_DT"));
				batchJob.setEnd_dt(rs.getString("END_DT"));
				batchJob.setDest_cnt(rs.getLong("DEST_CNT"));
				batchJob.setWork_cnt(rs.getLong("WORK_CNT"));
				batchJob.setDest_amt(rs.getLong("DEST_AMT"));
				batchJob.setWork_amt(rs.getLong("WORK_AMT"));
				batchJob.setWork_stat(rs.getString("WORK_STAT"));
				batchJob.setGrp_no(rs.getString("GRP_NO"));
			}

		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		
		return batchJob;
		
	}
	/**
	 * XML 생성 시작 시점 판단
	 * --마스터 이외의 서버가 XML서명 작업을 해야할 시점을 찾는 SQL
	 * @param conn
	 * @param targetMonth
	 * @param machine
	 * @return
	 */
	public String checkStart(Connection conn, String targetMonth, String machine){
		
		StringBuffer query = new StringBuffer()
		
		.append("  WITH LAST_DBJOB_01 AS (                                                             \n")
		.append("  SELECT *                                                                            \n")
		.append("  FROM   TB_BATCH_JOB_HIST                                                            \n")
		.append("  WHERE  YYYYMM = ?                                                             \n")
		.append("  AND    BATCH_JOB_CODE = '02'                                                        \n")
		.append("  AND    WORK_STAT = 'S'                                                              \n")
		.append("  AND    END_DT < TO_DATE('99991231235959', 'YYYYMMDDHH24MISS')                       \n")
		.append("  AND    START_DT =                                                                   \n")
		.append("                  (                                                                   \n")
		.append("                  SELECT MAX(START_DT)                                                \n")
		.append("                  FROM   TB_BATCH_JOB_HIST                                            \n")
		.append("                  WHERE  YYYYMM = ?                                                   \n")
		.append("                  AND    BATCH_JOB_CODE = '02'                                        \n")
		.append("                  )                                                                   \n")
		.append("  ),                                                                                  \n")
		.append("  LAST_DBJOB_02 AS (                                                                  \n")
		.append("  SELECT *                                                                            \n")
		.append("  FROM   TB_BATCH_JOB_HIST                                                            \n")
		.append("  WHERE  YYYYMM = ?                                                                   \n")
		.append("  AND    BATCH_JOB_CODE = '03'                                                        \n")
		.append("  AND    WORK_STAT = 'S'  \n")
		.append("  AND    SERVER_NO = 0                                                                 \n") 
		.append("  AND    END_DT <= TO_DATE('99991231235959', 'YYYYMMDDHH24MISS')                       \n") 
		.append("  AND    START_DT >= (SELECT START_DT FROM LAST_DBJOB_01)                             \n") 
		.append("  )                          \n") 
		.append("  SELECT 1                                                                              \n") 
		.append("  FROM   DUAL A                                                                       \n") 
		.append("  WHERE  EXISTS (SELECT 1 FROM LAST_DBJOB_02 b )                                       \n") 
		.append("  AND    NOT EXISTS (                                                                   \n") 
		.append("                     SELECT 1                                                            \n") 
		.append("                     FROM   TB_BATCH_JOB_HIST                                            \n") 
		.append("                     WHERE  YYYYMM = ?                                                    \n") 
		.append("                     AND    BATCH_JOB_CODE = '03'                                       \n") 
		.append("                     AND    SERVER_NO = ?                                               \n") 
		.append("                     AND    START_DT >= (SELECT MAX(START_DT) FROM LAST_DBJOB_02)           \n")                                                    
		.append("                    )  \n")
		.append("   \n"); 
		
		CommUtil.logWriter(String.valueOf(query),1);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String value = "";

		try{
			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, targetMonth);
			pstmt.setString(2, targetMonth);
			pstmt.setString(3, targetMonth);
			pstmt.setString(4, targetMonth);
			pstmt.setString(5, machine);

			System.out.println(targetMonth);
			System.out.println(machine);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				value = rs.getString(1);
			
			}
			
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		
		return value;
		
	}

}
