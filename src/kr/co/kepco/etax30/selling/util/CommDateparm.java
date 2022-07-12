package kr.co.kepco.etax30.selling.util;

import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.Date;
//import java.util.GregorianCalendar;



public class CommDateparm {
    public CommDateparm(){
	
    }

    
/*    public static void main(String[] args) {
	
		// Format the current time.
	SimpleDateFormat formatter	= new SimpleDateFormat ("dd");
	Date currentTime_1          = new Date();

	
	System.out.println(getKSTDate());
	
	
    }*/
    
    /*
     * 9일 이전이면 false(9일부터는 true)  
     * 
     * */
    public static boolean getKSTDate(String start){
	    boolean dayflg = true;
		int stdt = 0 ;
		stdt = Integer.parseInt(start);
		
		SimpleDateFormat sf	= new SimpleDateFormat ("ddHH");
		Date date          = new Date();
		
		dayflg = stdt <=  Integer.parseInt(sf.format(date)); 
		 
	return dayflg;
	}//end method

}



