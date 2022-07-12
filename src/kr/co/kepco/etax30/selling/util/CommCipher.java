package kr.co.kepco.etax30.selling.util;

/**************************************************************************************
 * 저작권            : Copyright⒞ 2009 by KEPCO Corp. All Rights Reserved

 * 프로젝트 명       : 한전 매출-세금계산서 시스템 개발 프로젝트
 * 프로그램 명       : CommCipher
 * 프로그램 아이디   : CommCipher.java
 * 프로그램 개요     : 스트링 암복호화 모듈                      
 * 관련 테이블       : 
 * 관련 모듈         : 
 * 작성자            : 정명수
 * 작성일자          : 2008-04-01

 * 개정이력(성명 | 일자 | 내용) : 정명수 | 2008-04-01 | (DEV TEAM), v1.0, 최초작성
 *                                정명수 | 2009-11-18 | (DEV TEAM), v1.1, 일부수정         
 
 * <METHOD>
 * - StringCipher() 
 * - StringDecipher() 
 * </METHOD>
**************************************************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**                          
 * 스트링 암복호화 클래스로서 영문,한글 모두 지원한다. 네트워크 데이터 전송시 
 * 사용할 수 있으며, java.util.zip Class를 이용하면 데이터의 증가된 부분을                   
 * 보완할 수 있다.
 */ 
public class CommCipher 
{
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			String str = CommCipher.StringCipher(reader.readLine());
			
				System.out.println("암호화 : " + str);
		        System.out.println("복호화 : " + CommCipher.StringDecipher(str));
		        //System.out.println("복호화 : " + CommCipher.StringDecipher("3230303931323230343230303030313330303030383035333D55505049414048414E4D41494C2E4E4554"));
		} catch (Exception e) {
			e.printStackTrace();
		}
       
	}
	
	
	
	/**                          
	 * 일반 스트링을 암호화 String으로 변환하는 함수
	 * @param String TempString                
	 * @return String              
	 */ 
	public static String StringCipher(String TempString) throws Exception
	{
		String ReturnString = "";
		
		try
		{
			byte[] byteTmp = TempString.getBytes("KSC5601");
			ReturnString = BytesToHexString(byteTmp);
			
		}
		catch (Exception e){}

		return ReturnString;
		
	}

	
	/**                          
	 * 암호화 스트링을  일반 스트링으로 복원하는 함수
	 * @param String TempString                
	 * @return String               
	 */ 
	public static String StringDecipher(String TempString) {
		String newString = "";
		
		try
		{
			byte[] byteStr = HexStringToBytes(TempString);
			newString = new String(byteStr, "KSC5601");
		}
		catch(Exception e)
		{
			
		}
		return newString;
	}

	/**                          
	 * 바이트 배열을 Hex String으로...by 정명수 2005.3.8
	 * @param byte[] TempByte                
	 * @return String             
	 */ 
	private static String BytesToHexString(byte[] TempByte) throws Exception
    {
        String ReturnString = "";
        for (int i=0; i<TempByte.length; i++)
        {
            if( (TempByte[i] > 15) || (TempByte[i] < 0) )
            {
                ReturnString += java.lang.Integer.toHexString(TempByte[i] & 0xff).toUpperCase();
            }
            else
            {
                ReturnString += "0"+java.lang.Integer.toHexString(TempByte[i] & 0xff).toUpperCase();
            }
        }
        return ReturnString;
    }

	/**                          
	 * Hex String 을 바이트 배열로...by 정명수 2005.3.8
	 * @param String TempString                
	 * @return byte[]             
	 */ 
	private static byte[] HexStringToBytes(String TempString) throws Exception
    {
        byte[] ReturnByte = new byte[TempString.length()/2];
        int ReturnBytePointer=0;

        for (int i=0; i<TempString.length(); i++)
        {
            if(TempString.charAt(i)=='0')
            {
                i++;
                if((TempString.charAt(i)>='0') && (TempString.charAt(i)<='9'))
                {
                    ReturnByte[ReturnBytePointer]=(byte)(TempString.charAt(i)-'0');
                }
                else if((TempString.charAt(i)>='A') && (TempString.charAt(i)<='F'))
                {
                    ReturnByte[ReturnBytePointer]=(byte)(10+TempString.charAt(i)-'A');
                }
                ReturnBytePointer++;
            }
            else
            {
                byte TempUpper;
                byte TempLower;

                TempUpper=0;
                if((TempString.charAt(i)>='0') && (TempString.charAt(i)<='9'))
                {
                    TempUpper=(byte)(TempString.charAt(i)-'0');
                }
                else if((TempString.charAt(i)>='A') && (TempString.charAt(i)<='F'))
                {
                    TempUpper=(byte)(10+TempString.charAt(i)-'A');
                }
                TempUpper=(byte)(TempUpper*16);

                i++;

                TempLower=0;
                if((TempString.charAt(i)>='0') && (TempString.charAt(i)<='9'))
                {
                    TempLower=(byte)(TempString.charAt(i)-'0');
                }
                else if((TempString.charAt(i)>='A') && (TempString.charAt(i)<='F'))
                {
                    TempLower=(byte)(10+TempString.charAt(i)-'A');
                }

                ReturnByte[ReturnBytePointer]=(byte)(TempUpper+TempLower);
                ReturnBytePointer++;
            }
        }
        return ReturnByte;
    }
}
