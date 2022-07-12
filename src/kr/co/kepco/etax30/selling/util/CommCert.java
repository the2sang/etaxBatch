package kr.co.kepco.etax30.selling.util;

//import java.io.IOException;

import signgate.crypto.util.Base64Util;
import signgate.crypto.util.CipherUtil;
import signgate.crypto.util.KeyUtil;
import java.security.spec.InvalidKeySpecException;

import signgate.crypto.asn1.Asn1Exception;
//import signgate.crypto.util.Base64Util;
//import signgate.crypto.util.CipherUtil;
import signgate.crypto.util.InvalidBase64Exception;
//import signgate.crypto.util.KeyUtil;

public class CommCert {
	/**
	 * 일반문자 암호화
	 * @param strOrg : 암호화 할 문자
	 * @return 암호화된 데이터로 출력
	 */
	public static String getEncrypt(String strOrg){
		String encData = null;
		CipherUtil cu = new CipherUtil();

        cu.encryptInit(); //암호화 초기화
//        System.out.println(cu.getErrorMsg());

        encData = Base64Util.encode(cu.encryptUpdate(strOrg.getBytes())); //데이터 암호화1
        encData = Base64Util.encode(cu.encryptUpdate(encData.getBytes())); //데이터 암호화2
        if (encData == null)
        {
            System.out.println("# data encryption failed");
            System.exit( -1);
        }
        cu.encryptFinal(); //암호화 종료
        return encData;
	}




	/**
	 * 암호화된 문자를 복호화 하는 메소드
	 * @param encString 복호화 할 암호화된 문자
	 * @return
	 */
	public static String getDecrypt(String encString){
		return  CipherUtil.decryptString( CipherUtil.decryptString(encString) );
	}


	/**
	 * 암호 체크 . 주어진 일반 암호와 암호키를 이용하여 암호 여부 판단
	 * @param passwd : 암호화 되지 않은 문자열
	 * @param key       : signPri.key 파일 위치
	 * @return  암호가 맞다면 true , 틀리면 false
	 */
	public static boolean isValidatePassWord(String passwd, String key){
    	KeyUtil keyutil = null;
		byte[] signkeybyte = null;
		try {
			keyutil = new KeyUtil(key);
			System.out.println(" KeyUtil 객체생성성공");
		} catch (Exception e2) {
			System.out.println("IOException : " + e2.toString());
			e2.printStackTrace();
			return false;
		}

		try {
//			System.out.println( "keyutil.getDecPrivateKeyPEM(passwd ) : \n"+keyutil.getDecPrivateKeyPEM(passwd ));
			keyutil.getDecPrivateKeyPEM(passwd );
			System.out.println( "keyutil.getDecPrivateKeyPEM(passwd ) : \n");
		} catch (Exception e1) {
			System.out.println("?? : "+e1.toString());;
			return false;
		}
		signkeybyte = keyutil.getKeyBytes();

		if(signkeybyte == null){
			System.out.println("signkeybyte : null");
			return false;
		}
		return true;
	}

/*	public static void isValidatePassWord(String passwd, String key) throws InvalidKeySpecException, Asn1Exception, InvalidBase64Exception, Exception {
    	KeyUtil keyutil = null;
		byte[] signkeybyte = null;
		try {
			keyutil = new KeyUtil("C:/KICASecuXML/cert/web/signPri.key");
		} catch (IOException e2) {
			System.out.println("IOException : " + e2.toString());
			e2.printStackTrace();
		}
//		String passwd = "3XVR87b3LwZRXqYSxNTruQbOOF7UiD++fj0hGqAvS28=";
		try {
//			System.out.println( "keyutil.getDecPrivateKeyPEM(passwd ) : \n"+keyutil.getDecPrivateKeyPEM(passwd ));
			System.out.println( "keyutil.getDecPrivateKeyPEM(passwd ) : \n"+keyutil.getDecPrivateKeyPEM(passwd ));
		} catch (Exception e1) {
			System.out.println("?? : "+e1.toString());;
		}
		signkeybyte = keyutil.getKeyBytes();

		if(signkeybyte == null){
			System.out.println("signkeybyte : null");
			return ;
		}
		return ;
	}
*/



	public static void main(String[] args) throws InvalidKeySpecException, Asn1Exception, InvalidBase64Exception, Exception {
/*		System.out.println(getDecrypt("encString") == null);
		System.out.println(getDecrypt("ushJ/S3HmNM+t2/JgmAZL0ZjiiZeKFi91MOsepz2IEc="));
		System.out.println(getEncrypt("encString"));*/
		String passwd = "a1123456A";
		   isValidatePassWord ( passwd, "C:/KICASecuXML/cert/web/signPri.key")  ;
		System.out.println("3333333333");

	}
}
