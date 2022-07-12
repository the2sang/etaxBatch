package kr.co.kepco.etax30.selling.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





public class Pross_purchase {

    private String driver = null;
    private String url    = null;
    private String user   = null;
    private String pass   = null;
    
    public Pross_purchase(String driver,String url, String user, String pass){
	
	this.driver = driver;
	this.url = url;
	this.user = user;
	this.pass = pass;
    }
    
    public void pross_purchase(){
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
	Connection con = null;
	//boolean bl = false; 
	try{
		Class.forName(driver);
		con  = DriverManager.getConnection(url, user, pass);  
		
				System.out.println(" 한전 매입 세금 계산서중 취소된것을 찾아 관리테이블에서 삭제한다  start ");
                bq.search_del_state(con);
                System.out.println(" 한전 매입 세금 계산서중 취소된것을 찾아 관리테이블에서 삭제한다  end ");

                System.out.println(" 한전 매입 세금계산서중 승인된것을 찾아 처리 start ");
                bq.search_update_state(con);
                System.out.println(" 한전 매입 세금계산서중 승인된것을 찾아 처리 end ");

	} catch(Exception e1){
	    try {
		e1.printStackTrace();
		throw e1;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}finally{
            try{
            	if (con != null){
            	    con.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
	}
	
    }
}