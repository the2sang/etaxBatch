package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchErrHistVo;

public class TbBatchErrHistDao {
		
		public int insert(Connection conn, TbBatchErrHistVo batchErrHist) throws  Exception {
			
			PreparedStatement pstmt = null;
			int result = 0;
			
			StringBuffer query = new StringBuffer()
			.append("	 INSERT INTO TB_BATCH_ERR_HIST (                                                      \n")
			.append("	     YYYYMM,                                                                          \n")
			.append("	     SERVER_NO,                                                                       \n")
			.append("	     BATCH_JOB_CODE,                                                                  \n")
			.append("	     START_DT,                                                                        \n")
			.append("	     OCCUR_DT,                                                                        \n")
			.append("	     ERR_DATA_KEY,                                                                    \n")
			.append("	     ERR_DESC,                                                                        \n")
			.append("	     REGIST_DT,                                                                       \n")
			.append("	     MODIFY_DT                                                                        \n")
			.append("	 )                                                                                    \n")
			.append("	 VALUES ( ?, ?, ?,                                        							  \n")
			.append("	     TO_DATE(?, 'YYYYMMDDHH24MISS'), SYSDATE, 					      				  \n")
			.append("	     ?, ?,                          										  		  \n")
			.append("	     SYSDATE, SYSDATE) 															      \n");
								            			                                    
			CommUtil.logWriter(String.valueOf(query),1);
			try{  
				conn.setAutoCommit(false);

				pstmt = conn.prepareStatement(query.toString());
				
				pstmt.setString(1, batchErrHist.getYyyymm());
				pstmt.setString(2, batchErrHist.getServer_no());
				pstmt.setString(3, batchErrHist.getBatch_job_code());
				pstmt.setString(4, batchErrHist.getStart_dt());
				pstmt.setString(5, batchErrHist.getErr_data_key());
				pstmt.setString(6, batchErrHist.getErr_desc());
				
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
	}
