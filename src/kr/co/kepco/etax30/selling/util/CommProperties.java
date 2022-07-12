package kr.co.kepco.etax30.selling.util;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : CommProperties
 * 프로그램 아이디      : CommProperties.java
 * 프로그램 개요        : 디스크에 저장되어 있는 물리적 화일변수를 읽어들이는 역할을 한다. (Properties 상속)                                                 
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 정명수
 * 작성일자             : 2009-10-07

 * 개정이력(성명 | 일자 | 내용) : 정명수 | 2009-10-07 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - loadFromFile()
 * - saveToFile()
 * - getInt()
 * - getStr()
 * - getObject()
 * - setObject()
 * - setValue()
 * - isAvailable()
 * - getString()
 * </METHOD>
******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.util.Properties;

public class CommProperties extends Properties
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3575882710079099702L;

	public CommProperties()
    {
        super();
    }

    /**
     * 설  명 : 화일에 있는 내용을 key, value형태로 loading
     * @param   Dir     정보를 읽기 위한 file full_path name
     * @return  N/A
     * @pre (Dir != null), "Dir cannot be null"
     */ 
    public void loadFromFile(String Dir) 
    {   
        try
        {
            FileInputStream fin = new FileInputStream(Dir);
            this.load(fin);
            fin.close();
        }
        catch(IOException e)
        {
            System.err.println(e);  
        }
    }//end method  


    /**
     * 설  명 : key, value형태로 정보를 화일로 저장하기 위한 것
     * 주  의 : key and value가 영문일 경우에 사용
     * @param   Dir     정보를 저장하기 위한 file full_path name
     * @return  N/A
     * @pre (Dir != null), "Dir cannot be null"
     */
    public void saveToFile(String Dir)
    {   
        try
        {
            FileOutputStream fout = new FileOutputStream(Dir);
            // this.save(fout, "properties Save");
            this.store(fout, "properties Save");
            fout.close();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }//end method
    
    /**
     * 설  명 : key에 해당되는 value를 int값으로 얻기 위한 것
     * @param   value를 얻기 위한 key
     * @return  value       int형
     * @pre (key != null), "key cannot be null"
     */
    public int getInt(String key)
    {
        Integer m_int = new Integer(getProperty(key));
        return m_int.intValue();        
    }//end mehtod


    /**
     * 설  명 : key에 해당되는 value를 string값으로 얻기 위한 것
     * @param   value를 얻기 위한 key
     * @return  value       int형
     * @pre (key != null), "key cannot be null"
     */ 
    public String getStr(String key)
    {
        return getProperty(key);
    }//end method


    /**
     * 설  명 : key에 해당되는 value를 cast하여 사용
     * 주  의 : 해당 정보에 대한 저장은 hashtable의 put method에 해당
     *          setObject() method에 의한 value를 위하여 사용
     * 사용법 : Integer a = (Integer)CommProperties.getObject("Type");
     * @param   value를 얻기 위한 key
     * @return  value       Object형 받을때 casting
     * @pre (key != null), "key cannot be null"
     */
    public Object getObject(String key)
    {
        return get(key);
    }//end mehtod


    /**
     * 설  명 : key에 해당되는 value를 저장시 string외의 다른 형태로 저장할 때
     * @param   key         String형
     * @param   value       AnyType object possible 
     * @return  N/A
     */
    public boolean setObject(String key, Object value)
    {
        if(isAvailable(key))
        {
            put(key, value);
            return true;
        }
        else
        {
            return false;
        }
    }//end method
    

    /**
     * 설  명 : key에 해당되는 value를 저장시 string형태로 저장할 때
     * 주  의 : getStr()과 getInt()로 데이타를 추출할수 있음.
     * @param   key         String형
     * @param   value       String형
     * @param   value       AnyType object possible 
     * @return  N/A
     */
    public boolean setValue(String key, String value)
    {
        if(isAvailable(key)) 
        {
            setProperty(key, value);    
            return true;
        }
        else
        {
            return false;   
        }
    }//end method


    /**
     * 설  명 : 현재 key가 존재하는지를 확인 하기 위한 함수
     * @param   key         String형
     * @return  boolean     현재 존재하면 false, 없으면 true
     */
    public boolean isAvailable(String key)
    {
        if(containsKey(key))
        {
            return false;   
        }
        else
        {
            return true;
        }
    }//end method


    /**
     * <PRE>
     * 지정된 위치(/{WEB-ROOT}/WEB-INF/classes)의 properties파일을 읽어 key에 대한 value리턴
     *
     * </PRE>
     * @param   String Key
     * @return  String Value         
     */
    public static String getString(String key)
    {
        Properties props   = null;
		//InputStream  in = null;
		FileInputStream in = null;
		String strReturn = "";
		
		// 임시테스트용으로 설정 운영시 막기 2011.11.15
		//if(key.equals("BUSINESS_NO")){
		//   return "1111111119";	
		//}
		
		
 		/**
		 * 프로퍼티 파일명
		 */
		final String CONFIG_FILE = "ap.property";
//		final String CONFIG_FILE = "kr/co/kepco/etax30/selling/util/ap.property";
        
		try {
			//File f = new File(CommProperties.class.getResource(CONFIG_FILE).getPath());
			File f = new File("/data5/ebxml/kepcobill2/kepcobill2/WEB-INF/batch/ap.property");
			//File f = new File("C:/ap.property");
			//System.out.println(CommProperties.class.getResource(CONFIG_FILE).getPath());
			in = new FileInputStream(f);
		}
		catch (Exception e) {
			System.out.println(CONFIG_FILE+" 화일이 없습니다.");
			e.printStackTrace();
		}
		
            
        if (in == null)
        {
			System.out.println(CONFIG_FILE+" 화일이 없습니다.");
			return strReturn;
        }
        else
        {
	        try
	        {
				props = new Properties();     
	            props.load(in);

				strReturn = props.getProperty(key);
	        }
	        catch (IOException ioe)
	        {
	                System.out.println("Exception 발생(CommProperties.java) : " + ioe.getMessage());
					ioe.printStackTrace();                
	        }
	        finally
	        {
	            try 
	            { 
	                if(in != null) in.close(); 
	            } 
	            catch (Exception e)                 
	            { 
	                System.out.println("[CommProperties.java] 스트림 해제 에러");
	                e.printStackTrace(); 
	            }
	        }
	        return strReturn;
        }
    }//END METHOD 
    public static void main(String[] args) {
    	
    	System.out.println(CommProperties.getString("DB_ACCOUNT"));
		
	}
}//END CLASS
