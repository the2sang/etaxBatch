package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbXmlDownInfoVo;

public class TbXmlDownInfoDao {
	
	public int insert(Connection conn, TbXmlDownInfoVo downInfo) throws  Exception { 
		PreparedStatement pstmt = null;
		int result = 0;
		
		StringBuffer query = new StringBuffer()
		.append(" INSERT INTO TB_XML_DOWN_INFO ( 	    	          	       \n")                                         
		.append("     YYYYMM,			    		                      	   \n")
		.append("     GRP_NO,			    		                      	   \n")
		.append("     FILE_NAME,			                          	       \n")
		.append("     TOTAL_AMOUNT,			                          	       \n")
		.append("     TOTAL_COUNT,			                                   \n")
		.append("     FILE_SIZE,	    		                               \n")
		.append("     FILE_PATH,		    	                               \n")
		.append("     REG_DT,	    			                               \n")
		.append("     MODIFY_DT	    		                      		       \n") 
		.append(" )                                                            \n")
		.append(" VALUES (                                                     \n")
		.append(" ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE) \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			
			pstmt.setString(1, downInfo.getDate());
			pstmt.setString(2,downInfo.getGrp_no());
			pstmt.setString(3, downInfo.getFile_name());
			pstmt.setLong(4, downInfo.getTotal_amount());
			pstmt.setLong(5, downInfo.getTotal_count());
			pstmt.setLong(6, downInfo.getFile_size());
			pstmt.setString(7, downInfo.getFile_path());
			
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
