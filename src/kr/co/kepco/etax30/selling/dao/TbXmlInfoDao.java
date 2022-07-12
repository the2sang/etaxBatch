package kr.co.kepco.etax30.selling.dao;

import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
//import java.io.InputStream;
import java.io.OutputStream;
//import java.io.StringBufferInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommUtil;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
//import signgate.xml.util.XmlUtil;

public class TbXmlInfoDao {
	
	/**
	 *
	 * @param conn
	 * @param bizManageId
	 * @param orgXml
	 * @param issueDay
	 * @param rValue
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, String bizManageId, String orgXml, 
			 String issueDay, String rValue, String id) throws  Exception { 
		
			 StringBuffer queryInsert = new StringBuffer()
			 .append("	INSERT INTO TB_XML_INFO ( 	                                                                                                 \n")
			 .append("	IO_CODE,			    	                                                                                                 \n")
			 .append("	ISSUE_DAY,			    	                                                                                                 \n")
			 .append("	BIZ_MANAGE_ID,			                                                                                                     \n")
			 .append("	ORG_XML_DOC,			                                                                                                     \n")
			 .append("	R_VALUE,				                                                                                                 \n")
			 .append("	REGIST_DT,					                                                                                                 \n")
			 .append("	MODIFY_DT,					                                                                                                 \n")
			 .append("	REGIST_ID,					                                                                                                 \n")
			 .append("	MODIFY_ID					                                                                                                 \n")
			 .append("	) 				                                                              			                                     \n")
			 .append("	values ('1', ?, ?, EMPTY_BLOB(), ?, SYSDATE, SYSDATE, ?, ?)  \n");
	 
		 StringBuffer queryBlob = new StringBuffer()		
			.append("	SELECT ORG_XML_DOC FROM TB_XML_INFO                                                              \n")
			.append("	WHERE IO_CODE = '1'                                                                              \n")
			.append("	AND ISSUE_DAY = ?                                                                                \n")
			.append("	AND BIZ_MANAGE_ID = ?                                                                            \n")
			.append("	FOR UPDATE                                                                     					 \n");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int result = 0;
		
		CommUtil.logWriter(String.valueOf(queryInsert),1);
		CommUtil.logWriter(String.valueOf(queryBlob),1);
		try{  

			pstmt = conn.prepareStatement(queryInsert.toString());

			pstmt.setString(1, issueDay);
			pstmt.setString(2, bizManageId);
			pstmt.setString(3, rValue);
			pstmt.setString(4, id);
			pstmt.setString(5, id);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			pstmt = conn.prepareStatement(queryBlob.toString());
			
			pstmt.setString(1, issueDay);
			pstmt.setString(2, bizManageId);
			
			rs = pstmt.executeQuery();
		
			if(rs.next()) {
				/*BufferedOutputStream out =  null;
				BufferedInputStream in =  null;
				byte[] buf = new byte[1024];
				int bytesRead= -1; 
				BLOB blob = ((OracleResultSet)rs).getBLOB("ORG_XML_DOC");
				out = new BufferedOutputStream(blob.setBinaryStream(0L));
				in = new BufferedInputStream(new ByteArrayInputStream(orgXml.getBytes("utf-8")));
				   //(CharConversion.K2E(orgXml)).getBytes("8859_1")

				//out = new BufferedOutputStream(blob.getBinaryOutputStream());					
				//in = new BufferedInputStream(new StringBufferInputStream(orgXml));
					
				int nFileSize = (int)orgXml.length();
				buf = new byte[nFileSize];

				while ((bytesRead = in.read(buf)) != -1){
					out.write(buf, 0, bytesRead);
				}
				in.close();
				out.close();*/
/*			       BLOB blob = ((OracleResultSet)rs).getBLOB("ORG_XML_DOC");
			       InputStream inputStream = blob.getBinaryStream();
			       byte[] buffer = new byte[ blob.getBufferSize() ];
			       OutputStream outputStream = blob.getBinaryOutputStream();*/
				
				
				 oracle.sql.BLOB blobDest = (oracle.sql.BLOB) ((OracleResultSet) rs).getBlob("ORG_XML_DOC");
				   byte[] buffer = new byte[ blobDest.getBufferSize() ];
				   OutputStream outputStream = blobDest.getBinaryOutputStream();
			       
				   
				   //BufferedInputStream inputStream = new BufferedInputStream(new StringBufferInputStream(orgXml));
				   BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(orgXml.getBytes("utf-8")));
				   int length = -1;
				   while ((length = inputStream.read(buffer)) != -1) {
				   outputStream.write(buffer,0,length);
				   outputStream.flush();
				   }
				   outputStream.close();
				   inputStream.close();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(TbXmlInfoDao.class.getName()+":"+e.toString());}
		}
		return result;
	
	}
	/**
	 *
	 * @param conn
	 * @param bizManageId
	 * @param orgXml
	 * @param issueDay
	 * @param rValue
	 * @return
	 * @throws Exception
	 */
	public int insert2(Connection conn, String bizManageId, String orgXml, 
			 String issueDay, String rValue, String id) throws  Exception { 
		
			 StringBuffer queryInsert = new StringBuffer()
			 .append("	INSERT INTO TB_XML_INFO ( 	                                                                                                 \n")
			 .append("	IO_CODE,			    	                                                                                                 \n")
			 .append("	ISSUE_DAY,			    	                                                                                                 \n")
			 .append("	BIZ_MANAGE_ID,			                                                                                                     \n")
			 .append("	ORG_XML_DOC,			                                                                                                     \n")
			 .append("	R_VALUE,				                                                                                                 \n")
			 .append("	REGIST_DT,					                                                                                                 \n")
			 .append("	MODIFY_DT,					                                                                                                 \n")
			 .append("	REGIST_ID,					                                                                                                 \n")
			 .append("	MODIFY_ID					                                                                                                 \n")
			 .append("	) 				                                                              			                                     \n")
			 .append("	values ('2', ?, ?, EMPTY_BLOB(), ?, SYSDATE, SYSDATE, ?, ?)  \n");
	 
		 StringBuffer queryBlob = new StringBuffer()		
			.append("	SELECT ORG_XML_DOC FROM TB_XML_INFO                                                              \n")
			.append("	WHERE IO_CODE = '2'                                                                              \n")
			.append("	AND ISSUE_DAY = ?                                                                                \n")
			.append("	AND BIZ_MANAGE_ID = ?                                                                            \n")
			.append("	FOR UPDATE                                                                     					 \n");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int result = 0;
		
		CommUtil.logWriter(String.valueOf(queryInsert),1);
		CommUtil.logWriter(String.valueOf(queryBlob),1);
		try{  

			pstmt = conn.prepareStatement(queryInsert.toString());

			pstmt.setString(1, issueDay);
			pstmt.setString(2, bizManageId);
			pstmt.setString(3, rValue);
			pstmt.setString(4, id);
			pstmt.setString(5, id);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			pstmt = conn.prepareStatement(queryBlob.toString());
			
			pstmt.setString(1, issueDay);
			pstmt.setString(2, bizManageId);
			
			rs = pstmt.executeQuery();
		
			if(rs.next()) {
				/*BufferedOutputStream out =  null;
				BufferedInputStream in =  null;
				byte[] buf = new byte[1024];
				int bytesRead= -1; 
				BLOB blob = ((OracleResultSet)rs).getBLOB("ORG_XML_DOC");
				out = new BufferedOutputStream(blob.setBinaryStream(0L));
				in = new BufferedInputStream(new ByteArrayInputStream(orgXml.getBytes("utf-8")));
				   //(CharConversion.K2E(orgXml)).getBytes("8859_1")

				//out = new BufferedOutputStream(blob.getBinaryOutputStream());					
				//in = new BufferedInputStream(new StringBufferInputStream(orgXml));
					
				int nFileSize = (int)orgXml.length();
				buf = new byte[nFileSize];

				while ((bytesRead = in.read(buf)) != -1){
					out.write(buf, 0, bytesRead);
				}
				in.close();
				out.close();*/
/*			       BLOB blob = ((OracleResultSet)rs).getBLOB("ORG_XML_DOC");
			       InputStream inputStream = blob.getBinaryStream();
			       byte[] buffer = new byte[ blob.getBufferSize() ];
			       OutputStream outputStream = blob.getBinaryOutputStream();*/
				
				
				 oracle.sql.BLOB blobDest = (oracle.sql.BLOB) ((OracleResultSet) rs).getBlob("ORG_XML_DOC");
				   byte[] buffer = new byte[ blobDest.getBufferSize() ];
				   OutputStream outputStream = blobDest.getBinaryOutputStream();
			       
				   
				   //BufferedInputStream inputStream = new BufferedInputStream(new StringBufferInputStream(orgXml));
				   BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(orgXml.getBytes("utf-8")));
				   int length = -1;
				   while ((length = inputStream.read(buffer)) != -1) {
				   outputStream.write(buffer,0,length);
				   outputStream.flush();
				   }
				   outputStream.close();
				   inputStream.close();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(TbXmlInfoDao.class.getName()+":"+e.toString());}
		}
		return result;
	
	}

	public int InsertForSelling(Connection conn, String IoCode, String bizManageId, String orgXml, 
			 String issueDay, String rValue, String id) throws  Exception { 
		
			 StringBuffer queryInsert = new StringBuffer()
			 .append("	INSERT INTO TB_XML_INFO ( 	                                                                                                 \n")
			 .append("	IO_CODE,			    	                                                                                                 \n")
			 .append("	ISSUE_DAY,			    	                                                                                                 \n")
			 .append("	BIZ_MANAGE_ID,			                                                                                                     \n")
			 .append("	ORG_XML_DOC,			                                                                                                     \n")
			 .append("	R_VALUE,				                                                                                                 \n")
			 .append("	REGIST_DT,					                                                                                                 \n")
			 .append("	MODIFY_DT,					                                                                                                 \n")
			 .append("	REGIST_ID,					                                                                                                 \n")
			 .append("	MODIFY_ID					                                                                                                 \n")
			 .append("	) 				                                                              			                                     \n")
			 .append("	values (?, ?, ?, EMPTY_BLOB(), ?, SYSDATE, SYSDATE, ?, ?)  \n");
	 
		 StringBuffer queryBlob = new StringBuffer()		
			.append("	SELECT ORG_XML_DOC FROM TB_XML_INFO                                                              \n")
			.append("	WHERE IO_CODE = ?                                                                              \n")
			.append("	AND ISSUE_DAY = ?                                                                                \n")
			.append("	AND BIZ_MANAGE_ID = ?                                                                            \n")
			.append("	FOR UPDATE                                                                     					 \n");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int result = 0;
		
		CommUtil.logWriter(String.valueOf(queryInsert),1);
		CommUtil.logWriter(String.valueOf(queryBlob),1);
		try{  

			pstmt = conn.prepareStatement(queryInsert.toString());

			pstmt.setString(1, IoCode);
			pstmt.setString(2, issueDay);
			pstmt.setString(3, bizManageId);
			pstmt.setString(4, rValue);
			pstmt.setString(5, id);
			pstmt.setString(6, id);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			pstmt = conn.prepareStatement(queryBlob.toString());
			
			pstmt.setString(1, IoCode);
			pstmt.setString(2, issueDay);
			pstmt.setString(3, bizManageId);
			
			rs = pstmt.executeQuery();
		
			if(rs.next()) {
				 oracle.sql.BLOB blobDest = (oracle.sql.BLOB) ((OracleResultSet) rs).getBlob("ORG_XML_DOC");
				   byte[] buffer = new byte[ blobDest.getBufferSize() ];
				   OutputStream outputStream = blobDest.getBinaryOutputStream();
			       
				   
				   //BufferedInputStream inputStream = new BufferedInputStream(new StringBufferInputStream(orgXml));
				   BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(orgXml.getBytes("utf-8")));
				   int length = -1;
				   while ((length = inputStream.read(buffer)) != -1) {
				   outputStream.write(buffer,0,length);
				   outputStream.flush();
				   }
				   outputStream.close();
				   inputStream.close();
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(TbXmlInfoDao.class.getName()+":"+e.toString());}
		}
		return result;
	
	}
	
	public String[] select (Connection conn, String rowId){
		
		StringBuffer query = new StringBuffer()
		 .append("	SELECT B.ORG_XML_DOC AS ORG_XML_DOC, A.ISSUE_ID AS ISSUE_ID       	\n")
		 .append("	  FROM TB_TAX_BILL_INFO A, TB_XML_INFO B        					\n")
		 .append("	 WHERE 1 > 0                                    					\n")
		 .append("	   AND A.ROWID = CHARTOROWID (?)                					\n")
		 .append("	   AND A.IO_CODE = B.IO_CODE                    					\n")
		 .append("	   and A.ISSUE_DAY = B.ISSUE_DAY                					\n")
		 .append("     AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID							\n");
		
		String[] contents = new String[2];
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		CommUtil.logWriter(String.valueOf(query),1);
		try {
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, rowId);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {

		          BLOB blob = ((OracleResultSet)rs).getBLOB(1);

		          BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
		          int nFileSize = (int)blob.length();
		          byte[] buf = new byte [nFileSize];    
		          in.read(buf, 0, nFileSize);

		          in.close();
		          contents[0] = new String(buf, "UTF-8");
		          
		          contents[1] = rs.getString("ISSUE_ID");
		     }	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(TbXmlInfoDao.class.getName()+":"+e.getMessage());}
		}
		return contents;
		
	}

}
