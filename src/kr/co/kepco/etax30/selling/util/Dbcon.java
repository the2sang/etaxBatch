package kr.co.kepco.etax30.selling.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import kr.co.kepco.etax30.selling.batchoffline.*;


public class Dbcon {
    
	final static String DRIVER = CommProperties.getString("DB_DRIVER");
	final static String URL    = CommProperties.getString("DB_URL");
	final static String USER   = CommProperties.getString("DB_USER");
	final static String PASS   = CommCipher.StringDecipher(CommProperties.getString("DB_PASS"));

	final static String DRIVER_OFF = CommProperties.getString("DB_DRIVER_OFF");
	final static String URL_OFF    = CommProperties.getString("DB_URL_OFF");
	final static String USER_OFF   = CommProperties.getString("DB_USER_OFF");
	final static String PASS_OFF   = CommCipher.StringDecipher(CommProperties.getString("DB_PASS_OFF"));
	
	//int local_i = 0;
	
	public Connection getConnection() throws SQLException{

		Connection  conn    = null;
		try{
			if(BatchOffline.OnOffbatch.equals("OFF") ||
			   BatchOffline1.OnOffbatch.equals("OFF") ||
			   BatchOffline2_3.OnOffbatch.equals("OFF") ||
			   BatchOffline2.OnOffbatch.equals("OFF") ||
			   BatchOffline3.OnOffbatch.equals("OFF")){
			   CommUtil.logWriter("conn = DriverManager.getConnection(URL_OFF, USER_OFF, PASS_OFF)",2);
			   Class.forName(DRIVER_OFF);

			   conn = DriverManager.getConnection(URL_OFF, USER_OFF, PASS_OFF);
			}else{
				CommUtil.logWriter("conn = DriverManager.getConnection(URL, USER, PASS)",2);
			   Class.forName(DRIVER);
               conn = DriverManager.getConnection(URL, USER, PASS);
			}			
		}catch(Exception e) { 
			e.printStackTrace();
		}
		return conn;
	}
		
		
	/*
	 * 제  목 : DB연결 실패시 재귀호출로 일정횟수 Retry할수 있는 기능
	 * 작  성 : 2015.7.7 박상종 
	 * 수  정 : -
	 * 내  용 : 네트워크 불안, DB상태 불안등으로 일시적인 연결오류로 DB연결이 안되는 경우
	 *       일정횟수 반복 retry해서 일시적 연결오류에 대응되도록 수정
	 * PARAMETER :  int  DbConRecursiveCallCnt- Retry 반복 최대 횟수
	 *              int DbConErrorRetryInterval - 1회 Retry Interval Time(1/1000초)
	 *              
	 */	
/*
	public Connection getConnection(int  DbConRecursiveCallCnt, int DbConErrorRetryInterval) throws SQLException, InterruptedException{

		Connection  conn    = null;

		while(local_i <= DbConRecursiveCallCnt){
			try{
				CommUtil.logWriter("DB연결 시도횟수: ["+local_i+"/"+DbConRecursiveCallCnt+"]",2);
				conn = getConnection();
	   		   break;
			}catch(Exception e) {
				local_i++;
	    		StackTraceElement ste[] = new Throwable().getStackTrace();	    		
	    		CommUtil.logWriter(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + 
         	    		           " " + ste[1].getFileName() +"(" + ste[1].getLineNumber() + "line)" +
         	    		           " " + ste[1].getClassName() +"." + ste[1].getMethodName() + "()",4);
	    		CommUtil.logWriter(local_i+"회 에러 발생[getConnection METHOD]\n",4);
	    		CommUtil.logWriter(e.toString(),4);

				try{
					//오류 발생후 recursive 호출시 term을 두기 위해
	               Thread.sleep(DbConErrorRetryInterval);//dbErrorRetryInterval 1/1000초
				}catch(Exception e2) {
		    		CommUtil.logWriter(e2.toString(),4);
				}
				if(local_i>= DbConRecursiveCallCnt){
					CommUtil.logWriter("최대 에러발생 한계점 "+DbConRecursiveCallCnt+"회를 초과해서 비정상 종료합니다.["+local_i+"]회 에러 발생",4);
					e.printStackTrace();
					break;
			    }
			}
		}
		local_i = 0;
		
        return conn;
	}
*/		

		public static void close(ResultSet rs){
			try{
				if (rs != null){
					rs.close();
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

		public static void close(Connection con){
			try{
				if (con != null){
					con.close();
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		public static void close(PreparedStatement ps){
			try{
				if (ps != null){
					ps.close();
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}		
		
		
		
	}