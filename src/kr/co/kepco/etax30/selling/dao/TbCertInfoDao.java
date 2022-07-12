package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;

public class TbCertInfoDao {
	final static String BUSINESS_NO = CommProperties.getString("BUSINESS_NO");

	public String[] select (Connection conn){
		
		CommUtil.logWriter("한전사업자번호:"+BUSINESS_NO,4);	

		StringBuffer query = new StringBuffer()     
		
		.append("SELECT REPOSITORY, PASS_WORD                                                       \n") 
		.append("  FROM TB_CERT_INFO                                     							\n") 
		.append(" WHERE USE_YN = 'Y'                                     		                    \n") 
		.append("   AND VALIDITY_END_DT > TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')	                    \n") 
		.append("   AND BUSINESS_NO = ?			         					                        \n");
		                                                                                                                 
		CommUtil.logWriter(String.valueOf(query),1);
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String[] info = new String[2]; 
		
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, BUSINESS_NO);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				info[0] = rs.getString("REPOSITORY");
				info[1] = rs.getString("PASS_WORD");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.getMessage());}
		}
		return info;
		
	}
	

}
