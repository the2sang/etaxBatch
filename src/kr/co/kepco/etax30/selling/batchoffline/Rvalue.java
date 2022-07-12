package kr.co.kepco.etax30.selling.batchoffline;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommCert;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;
import signgate.crypto.asn1.Asn1Exception;
import signgate.crypto.util.Base64Util;
import signgate.crypto.util.InvalidBase64Exception;
import signgate.crypto.util.KeyUtil;
//import kr.co.kepco.etax30.selling.util.CommUtil;

public class Rvalue {


		/**
		 * 한전 대표사업자 일경우는 현재 메소드 호출(1208200052) 셋팅
		 * @return String 암호화된 rvalue 반환
		 */
		public static String getRvalue() {
			String KEPCO_BIZ_NO = CommProperties.getString("BUSINESS_NO");
			
			CommUtil.logWriter("한전사업자번호:"+KEPCO_BIZ_NO,3);
			return getRvalue(KEPCO_BIZ_NO);
			//return getRvalue("1208200052");
			//return getRvalue("1111111119");
		}

		/**
		 * R value 값 반환
		 * @param biz_id 구하고자 하는 R value 값 반환
		 * @return String 암호화된 R value 값 반환
		 */
		public static String getRvalue(String biz_id) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT REPOSITORY, PASS_WORD  " );
			sql.append("FROM TB_CERT_INFO  " );
			sql.append("WHERE    BUSINESS_NO = ?  AND  USE_YN = 'Y' " );//한전 사업자 번호
			sql.append("    AND VALIDITY_END_DT > TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS' ) " );//유효기간 남아 있는거

			Connection con = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;

		   String base64R  = "";
		   String repository = null;
		   String passwd = null;
		   KeyUtil keyutil = null;

			CommUtil.logWriter("getRvalue 쿼리시작",0);

			try
	        {
				con = new Dbcon().getConnection();
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, biz_id);
				rs = pstmt.executeQuery();


				if ( rs.next() ){
					repository = rs.getString("REPOSITORY");
					passwd =   CommCert.getDecrypt( rs.getString("PASS_WORD") );
				CommUtil.logWriter("repository:"+repository,0);
				CommUtil.logWriter("rs.getString(PASS_WORD):"+rs.getString("PASS_WORD"),0);
				CommUtil.logWriter("passwd:"+passwd,0);
				}else {
					return null;
				}
				keyutil = new KeyUtil(repository +"/signPri.key"); // 인증서 개인키

				/// base인코딩 R값
				base64R = keyutil.getRandom(passwd);
				CommUtil.logWriter("base64R:"+base64R,0);
				//System.out.println(passwd);
				//System.out.println(base64R);
	        } catch (Asn1Exception e){
	        	e.printStackTrace();
	            return "Asn1Exception check ."+e.toString() ;

	        } catch (InvalidKeySpecException e){
	        	e.printStackTrace();
	        	return "InvalidKeySpecException check ." +e.toString();

	        } catch (InvalidBase64Exception e){
	        	e.printStackTrace();
	        	return "InvalidBase64Exception check ."+e.toString() ;

	        } catch (IOException e){
	        	e.printStackTrace();
	        	return "IOException check ."+e.toString() ;
	        } catch (SQLException e){
	        	e.printStackTrace();
	        	return "SQLException check ."+e.toString() ;

	        } catch (Exception e){
	            System.out.println("please check password." );
	            System.out.println(keyutil.getErrorMsg() );
	            return "please check password." ;
	        }finally{
				if(rs      != null) try{	rs.close();		rs = null;	}catch (Exception e) {}
				if(pstmt != null) try{	pstmt.close();pstmt = null;}catch (Exception e) {}
				if (con != null) try{con.close(); }catch(Exception e){System.out.println(e);}
	        }
		return base64R;
	}

	/**
	 *  rvalue 파일 생성
	 * @param outputPath rvalue 파일 저장경로
	 * @return String 파일의 저장여부
	 */
	public static String getFile(String outputPath){
		if ( outputPath == null )
			return "경로가 입력되지 않았습니다.";

		try {
			String encrValue = Rvalue.getRvalue();
			if ( encrValue == null )
				return "정상적인 공인인증서가 없습니다.";

			return makeFile ( Base64Util.decode( encrValue )  , outputPath );
		} catch (InvalidBase64Exception e) {
			e.printStackTrace();
        	return "InvalidBase64Exception check ."+e.toString() ;
		}

	}

	/**
	 * rvalue 파일 생성
	 * @param outputPath
	 * @param encRvalue
	 * @return
	 */
	public static String getFile(String outputPath, String encRvalue){
		if ( outputPath == null )
			return "경로가 입력되지 않았습니다.";
		if ( encRvalue == null )
			return "암호화된 R값이 입력되지 않았습니다.";

		try {
			return makeFile ( Base64Util.decode( encRvalue  )  , outputPath );
		} catch (InvalidBase64Exception e) {
			e.printStackTrace();
			return "InvalidBase64Exception check ."+e.toString() ;
		}
	}

	/**
	 * rvalue 파일 생성
	 * @param outputPath  저장 할 경로
	 * @param biz_id        사업자 번호
	 * @param nothing      사용하지 않음 인자값에 String 간략히 입력 ex) rr ( overload 기능 회피)
	 * @return
	 */
	public static String getFile(String outputPath,  String biz_id, String nothing, Connection con){
		if ( outputPath == null )
			return "경로가 입력되지 않았습니다.";
		if ( biz_id == null )
			return "사업자 번호가 입력되지 않았습니다.";

		try {
			return makeFile ( Base64Util.decode( Rvalue.getRvalue(biz_id) )  , outputPath );
		} catch (InvalidBase64Exception e) {
			e.printStackTrace();
			return "InvalidBase64Exception check ."+e.toString() ;
		}
	}


	/**
	 * rValue 값을 바이너리 파일로 저장 (저장 경로 + rvalue  파일 저장)
	 * @param b 2진 byte[] 형태로 입력(Base64Util.decode )
	 * @param outputPath 파일 저장경로
	 * @return String 성공하면 성공메시지 그렇지 않으면 Exception 메시지
	 */
	private static String makeFile( byte[] b, String outputPath){
		ByteArrayOutputStream out = null;
		String KEPCO_BIZ_NO = CommProperties.getString("BUSINESS_NO");
		String filename = outputPath+KEPCO_BIZ_NO+".r"; //운영
		//String filename = outputPath+"1111111119.r"; //테스트
		try {
			out = new ByteArrayOutputStream();
			FileOutputStream fo = 	new FileOutputStream(filename);
			 for (int i = 0; i < b.length; i++){
			      out.write(b[i]);
			      fo.write(b[i]);
			 }

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "성공적으로 파일이 생성 되었습니다.";
	}

	public static void main(String[] args) {

		Rvalue.getFile("c:/");
	}

}
