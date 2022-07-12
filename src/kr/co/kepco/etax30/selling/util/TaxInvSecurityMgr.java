package kr.co.kepco.etax30.selling.util;

//20161020  import java.sql.Connection;
//20161020  import java.sql.PreparedStatement;
//20161020  import java.sql.ResultSet;
//20161020  import java.sql.SQLException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

//20161020  import kr.co.bizframe.ebxml.ebms.app.kepco.tax.common.CommonUtil;
//20161020  import kr.co.bizframe.ebxml.ebms.app.kepco.tax.common.TaxInvoiceException;
//20161020  import kr.co.bizframe.ebxml.ebms.app.kepco.tax.rdb.manager.DBConnector;
//20161020  import kr.co.bizframe.ebxml.ebms.app.kepco.tax.rdb.TaxInvSecurityMgrDAO;


public class TaxInvSecurityMgr {
	
	//String todaychk = "201610"; //20161020  CommonUtil.getCurrentTimeFormat("yyyyMM");
	//public String key = "kepco" +todaychk+ "ebxml";
	public String key = CommProperties.getString("AES_KEY4MAIL");

	public byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		
		byte[] ba = new byte[hex.length() /2];
		for (int i=0;i<ba.length;i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2),16);
		}		
		return ba;
	}
	
	public String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0){
			return null;
		}
		
		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x=0;x<ba.length;x++) {
			hexNumber ="0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length()-2));
		}
		return sb.toString();
	}
	
	public String encrypt(String message) throws Exception {
		
		String encrypted ="";
		
		try {
		
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		
		//byte[] encrypted = cipher.doFinal(message.getBytes());
		//return byteArrayToHex(encrypted);
		
		encrypted = new BASE64Encoder().encode(cipher.doFinal(message.getBytes()));		
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return encrypted;
	}
	
	
	public String decrypt(String encrypted) throws Exception {
		
		String originalString ="";
		
		try {
		
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		
		//byte[] original = cipher.doFinal(hexToByteArray(encrypted));
		
		byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(encrypted));		
		originalString = new String(original);
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originalString;
	}

	public String TaxInvEncrypt(String en_str) {
		
		String encrypted_str ="";
		
		try {
			
		encrypted_str = encrypt(en_str);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return encrypted_str;
	}
	
	
	// 특수문자 GET방식 넘김시 깨짐현상 방지를 위해 문자열 encode처리
	// 문제있는 문자열들 빈공백, and연산자, + 연산자 
	//  EX)  ~'()! +&12       
	public String encodeURIComponent(String s)
	  {
	    String result = null;
	 
	    try
	    {
	      result = URLEncoder.encode(s, "UTF-8")
	                         .replaceAll("\\+", "%20")
	                         .replaceAll("\\%21", "!")
	                         .replaceAll("\\%27", "'")
	                         .replaceAll("\\%28", "(")
	                         .replaceAll("\\%29", ")")
	                         .replaceAll("\\%7E", "~");
	    }
	 
	    // This exception should never occur.
	    catch (UnsupportedEncodingException e)
	    {
	      result = s;
	    }
	 
	    return result;
	  }
/*//	20161020  	
	public boolean compareAES(String supBizNo, String encBizNo, String Uuid, String DocuCode, String Ipaddr, String checkType) throws SQLException, TaxInvoiceException {

		Connection conn = null;
		DBConnector dbconn = new DBConnector();
				
		String Sabun  = "-";
		String IpGubun = "-";
		boolean AesResult = false;
		String ln_Status = "FALSE";
		int result = 0;
		
		try{
			
			conn = dbconn.getConnection();
				
			String encrypted_BizNo = encBizNo;			
			String plain_BizNo = supBizNo;			
			String decrypted_BizNo = decrypt(encrypted_BizNo);
						
			if (decrypted_BizNo.equals(null) || decrypted_BizNo.equals("")) {  // 20130305 장지호 , 복호화 값 null 체크.
				
				System.out.println("복원된 AES 인증값이 없습니다. 사업자번호 : " + plain_BizNo + ", 암호화된 사업자번호  : " + encrypted_BizNo + ", 복원된 사업자번호 : " + decrypted_BizNo);
									
			} else {
				
				if(decrypted_BizNo.equals(plain_BizNo)){
					
					AesResult = true;
					ln_Status = "TRUE";
					
					System.out.println("사업자번호		: " + plain_BizNo);
					System.out.println("암호화된 사업자번호  : " + encrypted_BizNo);
					System.out.println("복원된 사업자번호 	: " + decrypted_BizNo);
				
				} else {
					
					System.out.println("AES 인증값이 일치하지 않습니다. 사업자번호 : " + plain_BizNo + ", 암호화된 사업자번호  : " + encrypted_BizNo + ", 복원된 사업자번호 	: " + decrypted_BizNo);
				}
				
			}
			
			result = new TaxInvSecurityMgrDAO().insertTaxInvSecurityInfo(conn, Uuid, Sabun, DocuCode, Ipaddr, ln_Status, IpGubun, checkType);
						
			if (result >= 1) conn.commit();
			else conn.rollback();
			
		} catch (SQLException se){
	    	conn.rollback();
	    	result = -99;
	    	dbconn.closeConnection(conn);
	    	throw new TaxInvoiceException(this, se);
		} catch (Exception e) {
			conn.rollback();
	    	result = -99;
	    	dbconn.closeConnection(conn);
	    	throw new TaxInvoiceException(this, e);
		} finally {
			try { dbconn.closeConnection(conn);
			} catch (Exception e){System.out.println(e.toString()); }
		}
		return AesResult;		
	}
	
	public boolean compareAES(String Uuid, String Uuid_enc, String DocuCode, String Ipaddr, String checkType) throws SQLException, TaxInvoiceException {

		Connection conn = null;
		DBConnector dbconn = new DBConnector();
		
		String Sabun  = "-";
		String IpGubun = "-";
		boolean AesResult = false;
		String ln_Status = "FALSE";
		int result = 0;
				
		try{			
			conn = dbconn.getConnection();
						
			String encrypted_uuid = Uuid_enc;
			String plain_uid = Uuid;					
			String decrypted_uuid = decrypt(encrypted_uuid);
			
			if (decrypted_uuid.equals(null) || decrypted_uuid.equals("")) {  // 20130305 장지호 , 복호화 값 null 체크.
				
				System.out.println("복원된 AES 인증값이 없습니다. 문서번호 : " + plain_uid + ", 암호화된 문서번호  : " + encrypted_uuid + ", 복원된 문서번호 : " + decrypted_uuid);
				
			} else {
				
				if(decrypted_uuid.equals(plain_uid)){
				
					AesResult = true;
					ln_Status = "TRUE";	
					
					System.out.println("문서번호: " + plain_uid);
					System.out.println("암호화된 문서번호  : " + encrypted_uuid);
					System.out.println("복원된 문서번호 	: " + decrypted_uuid);
					
				
				} else {					
					System.out.println("AES 인증값이 일치하지 않습니다. 문서번호 : " + plain_uid + ", 암호화된 문서번호  : " + encrypted_uuid + ", 복원된 문서번호 	: " + decrypted_uuid);
				}
			
			}
			
			result = new TaxInvSecurityMgrDAO().insertTaxInvSecurityInfo(conn, Uuid, Sabun, DocuCode, Ipaddr, ln_Status, IpGubun, checkType);	
			
			if (result >= 1) conn.commit();
			else conn.rollback();
			
		} catch (SQLException se){
	    	conn.rollback();
	    	result = -99;
	    	dbconn.closeConnection(conn);
	    	throw new TaxInvoiceException(this, se);
		} catch (Exception e) {
			conn.rollback();
	    	result = -99;
	    	dbconn.closeConnection(conn);
	    	throw new TaxInvoiceException(this, e);
		} finally {
			try { dbconn.closeConnection(conn);
			} catch (Exception e){System.out.println(e.toString()); }
		}
		return AesResult;		
	}

	
public boolean setTaxInvSecurityMgr(String Uuid, String Sabun, String DocuCode, String Ipaddr, String IpGubun, String checkType)throws SQLException, Exception {
		
		System.out.println("setTaxBuywSecurityMgr Items  ========>" + Ipaddr + "," + Uuid + "," + Sabun + "," + DocuCode + "," + Ipaddr +"," + IpGubun + "," + checkType);
		
		Connection conn = null;
		DBConnector dbconn = new DBConnector();
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		int idx	= 1;
		int cnt = 0;
		int result = 0;
	      
		String ipaddress = Ipaddr;
		String[] ip = ipaddress.split("[\\.]");
		String ipLike = ip[0]+"."+ip[1]+".%";
		    
	    boolean IpResult = false;
	    String ln_Status = "FALSE";
	    
	    String sql =
	    	"SELECT  								" +
	    	"	CEIL(COUNT(*)) TOTALCNT				" +
	    	"	FROM TAX_CONNECT_T					" +
	    	"	WHERE IP_FROM LIKE ?				" +
	    	"		AND IP_GUBUN =	?				";
	    
	    try {
	    	
	    	conn = dbconn.getConnection();
		
	    	pstmt = conn.prepareStatement(sql);
	    	conn.setAutoCommit(false);
		
	    	pstmt.setString(idx++, ipLike);
	    	pstmt.setString(idx++, IpGubun);
		
			
	    	rs = pstmt.executeQuery();
	    	//pstmt.close();
	    	
		if (rs.next()) {
			cnt = rs.getInt("TOTALCNT");			
			if (cnt > 0) {
				IpResult = true;
				ln_Status = "TRUE";
				
			}     

			result = new TaxInvSecurityMgrDAO().insertTaxInvSecurityInfo(conn, Uuid, Sabun, DocuCode, Ipaddr, ln_Status, IpGubun, checkType);
			
			if (result >= 1) conn.commit();
			else conn.rollback();
		}	
		
		} catch (SQLException se){
			conn.rollback();
			result = -99;
	    	dbconn.closeConnection(conn);
			throw new TaxInvoiceException(this, se);
		}  catch (Exception e){
			conn.rollback();
			result = -99;
	    	dbconn.closeConnection(conn);
			throw new TaxInvoiceException(this, e);
		} finally {
			try { dbconn.closeConnection(conn);
			} catch (Exception e){System.out.println(e.toString()); }
		}
				
		return IpResult;

	}	
*///	20161020  	
	
}
