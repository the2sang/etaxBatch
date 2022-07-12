package kr.co.kepco.etax30.selling.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class SmsPross {
    
    private String driver = null;
    private String url    = null;
    private String user   = null;
    private String pass   = null;
    
    public static void main(String[] args){}
    
    
    public SmsPross(String driver,String url, String user, String pass){
	
	this.driver = driver;
	this.url = url;
	this.user = user;
	this.pass = pass;
    }
    
    public  void sms_email(){
	SmsDao sd = new SmsDao();//  실제로  Query를 통해 작업이 일어나는 Class;	
	Connection con = null;
	String msg = null;
	try{

	    Vector vc =  null;


	    Class.forName(driver);
	    con  = DriverManager.getConnection(url, user, pass);
	    con.setAutoCommit(false);	
	    
	    vc = sd.get_sms_data(con);
	    if(vc!=null && vc.size()>0){
		msg = sd.tran_sms(vc, con);
		msg = sd.tran_email(vc, con);
	    }
	    System.out.println(msg);
	    
    	} catch (SQLException e) {
	    try {
		con.rollback();
		e.printStackTrace();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    try {
		con.rollback();
		e1.printStackTrace();
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
