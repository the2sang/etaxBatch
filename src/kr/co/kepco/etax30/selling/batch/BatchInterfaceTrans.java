package kr.co.kepco.etax30.selling.batch;

import java.net.InetAddress;
//import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import signgate.crypto.util.CertUtil;
import signgate.xml.util.XmlSecu;
import signgate.xml.util.XmlSignature;
import signgate.xml.util.XmlUtil;
//import signgate.xml.util.XmlSignature;

//import kr.co.kepco.etax30.selling.batchoffline.MultiThreadClient;
import kr.co.kepco.etax30.selling.batchoffline.Rvalue;
//import kr.co.kepco.etax30.selling.batchoffline.Rvalue;
import kr.co.kepco.etax30.selling.batchoffline.SignPostionData;
import kr.co.kepco.etax30.buying.lib.TaxCreateXml;
import kr.co.kepco.etax30.selling.batchoffline.TaxXmlService;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
//import kr.co.kepco.etax30.selling.util.Dbcon;
//import kr.co.kepco.etax30.selling.vo.ItemListVo;
//import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;

import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
//import kr.co.kepco.etax30.selling.vo.ItemListVo;
//import kr.co.kepco.etax30.selling.util.CommDateparm;
//import kr.co.kepco.etax30.selling.util.CommProperties;

public class BatchInterfaceTrans {
    /**
     * 배치 프로시져 Main;.
     * @param args 기준일자
     */     
        
    private String driver = null;    
    private String url    = null;
    private String user   = null;
    private String pass   = null;
	
	XmlSecu XmlSecu = null;
	public XmlSignature sign = null;
	public String[] cert =null;
	public boolean CertIsvalid = true;
	public String rValue = null;

    public BatchInterfaceTrans(String driver,String url, String user, String pass){
	
	this.driver = driver;
	this.url = url;
	this.user = user;
	this.pass = pass;
	String host_ip = null;
    // r-vaule 가져오기
	rValue = Rvalue.getRvalue();
	CommUtil.logWriter("#############   R-VALUE:"+rValue,1);		
	
    //전자서명용 초기화 작업
		try {
			cert = TaxXmlService.getTbCertInfo();
		} catch (Exception e2) {
			e2.printStackTrace();
			//System.exit(-1);//종료
		}
		
 		String secu_cfg_path = CommProperties.getString("CERT_SECU_CFG_PATH");
		CommUtil.logWriter("[매출세금계산서용]인증모듈 설정화일경로:"+secu_cfg_path,1);		
		
		XmlSecu = new XmlSecu(secu_cfg_path);
		XmlSecu.setEncTypeEncoding("UTF-8");
		XmlSecu.setEncEncoding("UTF-8");
		XmlSecu.setSignEncoding("UTF-8");
		XmlSecu.setFileEncoding("UTF-8");
		
		try {
			sign = new XmlSignature(XmlSecu);
			
			CommUtil.logWriter("[매출세금계산서용] 인증서 화일경로(cert[0]):"+cert[0],1);		
			CertUtil certutil = new CertUtil(cert[0]+"/signCert.der");
            String signAlgo = certutil.getPublicKey().getAlgorithm().toLowerCase();
			String signatureAlgorithm = certutil.getSigAlgName().toLowerCase();
			
			CommUtil.logWriter("signAlgo:"+signAlgo,1);		
			CommUtil.logWriter("signatureAlgorithm:"+signatureAlgorithm,1);		
			
			try {
				host_ip = InetAddress.getLocalHost().getHostAddress();
			}catch(Exception e3) {
				e3.printStackTrace();
			}
			CommUtil.logWriter("host_ip:"+host_ip,1);		
			
	
			if(certutil.isValid()){
				CommUtil.logWriter("인증서 유효성 검사 성공",1);
			}else{
				CommUtil.logWriter("host_ip:"+host_ip,4);		
				CommUtil.logWriter("인증서 유효성 검사 실패",4);
				//if(!"168.78.201.224".equals(host_ip)){//운영 서버만  정보인증서버 통신이 되므로 유효성검사 실패 처리 로직 태우기(개발서버는 skip됨)
				if("168.78.201.56".equals(host_ip) || "168.78.201.57".equals(host_ip)){//운영서버만 정보인증서버 통신이 되므로 체크 처리 
					CertIsvalid = false;
				}   
			}

			sign.setCanonicalAlgo("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
			//sign.setSignatureAlgo("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
			
			//2011.2.21 고도화작업 적용 KDY
			if (signAlgo.indexOf("kcdsa") > -1) {
				sign.setSignatureAlgo("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
			}else{
				String xmlsignAlgo = XmlUtil.replAlgo(certutil.getSigAlgName());
				sign.setSignatureAlgo(xmlsignAlgo);
			}//end		
	
			// 2011.08.24 thread static 변수 에러 체크(debugging)를 위해 설정
			//sleep(5000);
			
			sign.setSchemaLocationURL("http://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd");
			sign.addReference("\"\"");
			sign.addTransformElement(
					"http://www.w3.org/TR/2001/REC-xml-c14n-20010315", null);
			sign.addTransformElement(
							"http://www.w3.org/TR/1999/REC-xpath-19991116",
							"not(self::*[name()='TaxInvoice'] | ancestor-or-self::*[name()='ExchangedDocument'] | ancestor-or-self::ds:Signature)");
			
			//2011.2.21 고도화작업 적용 KDY
			if (signatureAlgorithm.equals("sha256withrsa"))
				sign.addDigestMethod("http://www.w3.org/2001/04/xmlenc#sha256");
			//end
			sign.setTransformElementFinal();
			sign.isClear(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
    }
    
    
    public void error_chk() {
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
	boolean bl = false;
	Connection con = null;
	try{
            Class.forName(driver);
            con  = DriverManager.getConnection(url, user, pass);  
            con.setAutoCommit(false);
            
            bl = bq.trun_if_temp(con);
	System.out.println("ITEM존재 하지않을 시 에러 체크___________________________________________");            
            bl = bq.error_info_trans_temp(con);
            
            if(bl){
       	System.out.println("ITEM존재오류 결과 입력___________________________________________________");        	
        	bl = bq.error_result_info_insert(con);
      	System.out.println("ITEM존재오류 FLG변경(IF_TAX_BILL_INFO)___________________________________");        	
        	bl = bq.if_tax_bill_info_flg_update(con);
            }
            
	    if(bl){
		con.commit();
	    }
            
	}catch(SQLException e) {
	    try {
		con.rollback();
		e.printStackTrace();
		throw e;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    try {
		con.rollback();
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
    
    
    public void pross_tran(){

	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
	boolean bl = false;
	Connection con = null;
	try{
            Class.forName(driver);
            con  = DriverManager.getConnection(url, user, pass);  
            con.setAutoCommit(false);	
	    /*배치 프로시져 (인터페이스) Start*/
	    
	    //0. 처리할 대상이 있으면 TEMP 테이블에 대상을 잡아 이하를 실행한다. 
            //8.  IF_TAX_BILL_INFO_TEMP TABLE 비우기
            bl = bq.trun_if_temp(con);
	    bl = bq.tax_info_trans_temp(con);
	    
	    if(bl){ //처리대상 이 없으면 이하는 실행되지않는다.
                    //1. 세금 계산서 정보 이관 IF_TAX_BILL_INFO_TEMP => TB_TAX_BILL_INFO	    
		System.out.println("이관 작업 시작___________________________________________________________");
		    bl = bq.tax_info_trans(con);
		    
                    //2.  아이템정보 이관 IF_TAX_BILL_ITEM_LIST => TB_TRADE_ITEM_LIST
                    bl = bq.item_list_trans(con);
                    
                    //3.  처리결과정보 초기값 세팅 IF_TAX_BILL_RESULT_INFO
                System.out.println("이관 후 결과 초기값 세팅 IF_TAX_BILL_RESULT_INFO_________________________");
                    bl = bq.tax_result_info_insert(con);
                    
                    //4.  처리결과정보 초기값 세팅 TB_STATUS_HIST
                System.out.println("처리결과정보 등록 TB_STATUS_HIST_________________________________________");
                    bl = bq.tax_hist_info_insert(con);
                    
                    //공가매출 접수 후 eamil,sms 보내기
                    //제   목 : 공가 매출전자세금계산서연계 관련
                    //수정일 : 2013.11.11 빼빼로DAY
            		// 공가매출 프로세스 2차 변경 적용  2013.12.26
                    // 메일/SMS 송신 comment처리 
                    //System.out.println("공가매출 세금계산서 등록정보 SMS, EMAIL 전송______________________________________");
                    //bl = bq.get_sms_email(con);
                    
                    //제   목 : 공가 매출전자세금계산서연계 관련
                    //수정일 : 2013.11.11 빼빼로DAY
                    //내용 ERP FI에 공가매출세금계산서 정보 연계를 위해 IF_TAX_BILL_INFO에 ERP FI연계용 INSERT 처리
                    //대상조건 ONLINE_GUB_CODE = 3, REL_SYSTEM_ID = K1NCIS1000,JOB_GUB_CODE = ?
                    //2013.11.15 프로세스 변경으로 필요없게됨 
            		//System.out.println("공가매출전자세금계산서 기본정보 ERP FI용 COPY INSERT 시작   _____________________");
                    //bq.tax_info_copy4FI(con, "K1ERP11000");
            		//System.out.println("공가매출전자세금계산서 품목정보 ERP FI용 COPY INSERT 시작   _____________________");
                    //2013.11.15 프로세스 변경으로 필요없게됨 
                    //bq.item_list_copy4FI(con, "K1ERP11000");
                System.out.println("접수 정보 갱신 FLG 변경__________________________________________________");
                    //7.  IF_TAX_BILL_INFO FLG 값 업데이트 'N' --> 'Y'
                    bl = bq.if_tax_bill_info_flg_update(con);
                    //System.out.println("b7 :"+bl);
	    	}
	    
	    if(bl){
		con.commit();
	    }
	    
    
	    /*배치 프로시져 종료*/
	}catch(SQLException e) {
	    try {
		con.rollback();
		e.printStackTrace();
		throw e;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    try {
		con.rollback();
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
    
    // 프로그램 수정
    // 2012.03.19 by mopuim
    // 수정 내용 : 전월, 금월 구분 없이 한번에 처리
    //           batch 테이블에 대상을 insert 먼저하고 batch 테이블
    //           대상만 처리함.
    // 기타 : 이전 pross_tran1() 소스전체를 함수아래 comment으로 백업함.
    public void pross_tran1(){

	BatchQuery bq = new BatchQuery();	
	boolean tempRslt = false;
	Connection con = null;
	//String tran_start = "";
	try{
            Class.forName(driver);
            con  = DriverManager.getConnection(url, user, pass);  
            con.setAutoCommit(false);	
        	System.out.println("매출 승인대상 처리(전월,금월)>>>>>>>>>>>");
        	
        	tempRslt = bq.trun_up_temp(con); 		//temp 비우기
            if(tempRslt){
            	tempRslt = bq.after_trans_pro_b(con); 	//temp 대상잡기
                //2014. 12. 9 temp 대상잡기 계산서 부분 추가
            	
            }

            //####################################################################
    		// 프로그램 수정 2013.10.22 시작
    		// 제목 : 세금계산서 발행일자(서명일자) ISSUE_DT를 sysdate로 변경(기능 추가)
    		// 수정내용 : 변경해 놓지 않으면 ERP온라인 발행분은 issue_dt가 최초 발행 날짜로 되어 있어 익일전송 처리 문제 발생함
    		//----------------------------------------------------
            if(tempRslt){
        		tempRslt = bq.tax_info_Update_IssueDt(con);   
            }
            
        	if(tempRslt){
        		//####################################################################
        		// 프로그램 수정 2013.10.22 시작
        		// 제목 : 매출세금계산서 xml생성 토피도 Tid 기능 걷어내기
        		// 수정내용 : 이전에는  ET_TID_MAIN, ET_TID_ITEM에 넣어서 처리 하던것을
        		//          직접 xml 생성-> 전자서명처리 -> ET_TIDOC_IF에 입력 처리로 변경
        		//----------------------------------------------------
        		//------변경 전 기능 ---------------------------------------
                //5. 세금 계산서 정보 이관 TB_TAX_BILL_INFO => ET_TID_MAIN 
        		//tempRslt = bq.tax_info_etax_trans02(con);
        		//6. 아이템정보 이관 TB_TRADE_ITEM_LIST => ET_TID_ITEM    
       		    //tempRslt = bq.item_etax_trans02(con);
        		//----------------------------------------------------
        		//------변경 후 기능 ---------------------------------------
        		
        		
        		
        		tempRslt = tax_info_etax_trans02_NEW(con);
        		//----------------------------------------------------
        		// 프로그램 수정 2013.10.22 끝
        		//####################################################################
            	System.out.println("tempRslt:"+tempRslt+" 승인 대상 체크_________________________________________");	        	
            }
        	
    		//####################################################################
    		// 프로그램 수정 2013.10.22 시작
    		// 제목 : 매출세금계산서 xml생성 토피도 Tid 기능 걷어내기
    		// 수정내용 : etaxconn ET_TIDOC_IF에 테이블에 등록 기능 추가 
    		//----------------------------------------------------
    		//------변경 전 기능 ---------------------------------------
        	//       없음
    		//------변경 후 기능 ---------------------------------------
        	if(tempRslt){
               tempRslt = bq.InsertEtaxconn(con);         		
         	}
    		//####################################################################
        	//처리 결과 상태 및 이력 정보 갱신
            if(tempRslt){ //전송건수가 있으면 ..
            	tempRslt = bq.after_trans_pro1(con);	//IF_TAX_BILL_RESULT_INFO 전송완료 이력 등록
            	tempRslt = bq.after_trans_pro2(con);	//TB_TAX_BILL_INFO 상태이력 갱신
            	tempRslt = bq.tb_status_hist_up01(con);	//TB_STATUS_HIST 상태이력 갱신
            	tempRslt = bq.tb_status_hist_in03(con);	//TB_STATUS_HIST 새이력 등록
	        	System.out.println(tempRslt+" : (true)승인 이력갱신 완료_________________________________________");	        	
                con.commit();
            }
        
            /*System.out.println("매출 금월 승인대상 처리>>>>>>>>>>>");
    	    // 금월대상체크 수정세금계산서 예외처리 제외
            	bl = bq.tax_info_etax_trans_now(con);
            if(bl){
            	bl = bq.item_etax_trans_now(con);
        		System.out.println(bl+" : (true)금월 승인 대상 체크_________________________________________");	        	
            }	    	
    	
            if(bl){ //전송건수가 있으면 
    	    	bl = bq.trun_up_temp(con); 		//temp 비우기
	        	bl = bq.after_trans_pro_c(con); 	//temp 대상잡기
	        	bl = bq.after_trans_pro1(con);	//IF_TAX_BILL_RESULT_INFO 전송완료 이력 등록
	        	bl = bq.after_trans_pro2(con);	//TB_TAX_BILL_INFO 상태이력 갱신
	        	bl = bq.tb_status_hist_up01(con);	//TB_STATUS_HIST 상태이력 갱신
	        	bl = bq.tb_status_hist_in03(con);	//TB_STATUS_HIST 새이력 등록
	        	System.out.println(bl+" (true)금월 승인대상 이력 갱신 완료__________________________________");	        	
            }
            if(bl){
                con.commit();
            }*/
	    
	    /*배치 프로시져 종료*/
	}catch(SQLException e) {
	    try {
		con.rollback();
		e.printStackTrace();
        CommUtil.logWriter("매출세금계산서 xml생성 및 etaxconn으로 이관 SQLException"+e.toString(),4);
		throw e;
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}catch(Exception e1){
	    try {
		con.rollback();
		e1.printStackTrace();
        CommUtil.logWriter("매출세금계산서 xml생성 및 etaxconn으로 이관 Exception"+e1.toString(),4);
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
    
    /*public void pross_tran1(){

    	BatchQuery bq = new BatchQuery();	
    	boolean bl = false;
    	Connection con = null;
    	String tran_start = "";
    	try{
                Class.forName(driver);
                con  = DriverManager.getConnection(url, user, pass);  
                con.setAutoCommit(false);	
    	    //배치 프로시져 (인터페이스) Start
    	    
    	    //0. 처리할 대상이 있으면 TEMP 테이블에 대상을 잡아 이하를 실행한다. 
    	    //System.out.println("b0 :"+bl);
    	   
                //9일 기준 9일 이전데이터는 승인코드만 국세청 전송, 9일부터는 승인코드 없이도 국세청 전송(매출 기준)
                tran_start =  CommProperties.getString("TRAN_START_DT");
                
    	    
//    	    if(CommDateparm.getKSTDate(tran_start)){ //properties 설정값에 해당되면 즉, 기준일자를 지났으면...
//    		    	//5.  세금 계산서 정보 이관 TB_TAX_BILL_INFO => ET_TID_MAIN
//
//    	    		System.out.println("강제전송 대상 체크_________________________________________");
//    	            bl = bq.tax_info_etax_trans(con);
//    	            //System.out.println("b5 :"+bl);
//    	            //6.  아이템정보 이관 TB_TRADE_ITEM_LIST => ET_TID_ITEM
//    	            if(bl){
//    	            	bl = bq.item_etax_trans(con);
//    	            	System.out.println("강제전송 완료_________________________________________");	        	
//    	            }            
//    	            //System.out.println("b6 :"+bl);
//    			    if(bl){ //전송건수가 있으면 
//    			    	bl = bq.trun_up_temp(con); 		//temp 비우기
//    			    	bl = bq.after_trans_pro_a(con); 	//temp 대상잡기
//    					bl = bq.after_trans_pro1(con);	//IF_TAX_BILL_RESULT_INFO 전송완료 이력 등록
//    					bl = bq.after_trans_pro2(con);	//TB_TAX_BILL_INFO 상태이력 갱신
//    					bl = bq.tb_status_hist_up01(con);	//TB_STATUS_HIST 상태이력 갱신
//    					bl = bq.tb_status_hist_in03(con);	//TB_STATUS_HIST 새이력 등록
//    					System.out.println("강제전송건 이력 갱신 완료_________________________________________");	        	
//    		            con.commit();
//    		        } 
//    		        	//강제전송 주석 처리 end
//    	    	}else{
//    	    	}
        	
                	System.out.println("매출 전월 승인대상 처리>>>>>>>>>>>");
    	    		//5. 세금 계산서 정보 이관 TB_TAX_BILL_INFO => ET_TID_MAIN 
    	            bl = bq.tax_info_etax_trans02(con);
    	            //6. 아이템정보 이관 TB_TRADE_ITEM_LIST => ET_TID_ITEM
    	            
    	            if(bl){
    	            	bl = bq.item_etax_trans02(con);
    	            	System.out.println(bl+" : (true)전월 승인 대상 체크_________________________________________");	        	
    	            }
    	            //System.out.println("b6 :"+bl);	
    	            
    	            if(bl){ //전송건수가 있으면 ..
    	    	    	bl = bq.trun_up_temp(con); 		//temp 비우기
    		        	bl = bq.after_trans_pro_b(con); 	//temp 대상잡기
    		        	bl = bq.after_trans_pro1(con);	//IF_TAX_BILL_RESULT_INFO 전송완료 이력 등록
    		        	bl = bq.after_trans_pro2(con);	//TB_TAX_BILL_INFO 상태이력 갱신
    		        	bl = bq.tb_status_hist_up01(con);	//TB_STATUS_HIST 상태이력 갱신
    		        	bl = bq.tb_status_hist_in03(con);	//TB_STATUS_HIST 새이력 등록
    		        	System.out.println(bl+" : (true)전월 승인 이력갱신 완료_________________________________________");	        	
    	                con.commit();
    	            }
    	        
    	            System.out.println("매출 금월 승인대상 처리>>>>>>>>>>>");
    	    	    // 금월대상체크 수정세금계산서 예외처리 제외
    	            	bl = bq.tax_info_etax_trans_now(con);
    	            if(bl){
    	            	bl = bq.item_etax_trans_now(con);
    	        		System.out.println(bl+" : (true)금월 승인 대상 체크_________________________________________");	        	
    	            }	    	
    	    	
    	            if(bl){ //전송건수가 있으면 
    	    	    	bl = bq.trun_up_temp(con); 		//temp 비우기
    		        	bl = bq.after_trans_pro_c(con); 	//temp 대상잡기
    		        	bl = bq.after_trans_pro1(con);	//IF_TAX_BILL_RESULT_INFO 전송완료 이력 등록
    		        	bl = bq.after_trans_pro2(con);	//TB_TAX_BILL_INFO 상태이력 갱신
    		        	bl = bq.tb_status_hist_up01(con);	//TB_STATUS_HIST 상태이력 갱신
    		        	bl = bq.tb_status_hist_in03(con);	//TB_STATUS_HIST 새이력 등록
    		        	System.out.println(bl+" (true)금월 승인대상 이력 갱신 완료__________________________________");	        	
    	            }	
    	    
                    if(bl){
                        con.commit();
                    }
    	    
    	    //배치 프로시져 종료
    	}catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
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
        }*/
    
   /* pross_mail_sms_tran
    * kepcobill에서 작성한 매출세금계산서에 대해 ERP에서 수신 받은후
    * 한전에서 회계년도, 전표번호를 if_tax_bill_confirm_info에 입력하면
    * 공급받는자(고객)에게 메일, sms로 세금계산서 발행 사실을 알림
    */   
    public void pross_mail_sms_tran(){

    	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
    	boolean bl = false;
    	Connection con = null;
    	try{
                Class.forName(driver);
                con  = DriverManager.getConnection(url, user, pass);  
                con.setAutoCommit(false);	
    	    /*배치 프로시져 (인터페이스) Start*/
    	    
    	    //0. 처리할 대상이 있으면 TEMP 테이블에 대상을 잡아 이하를 실행한다. 
                //8.  IF_TAX_BILL_CONFIRM_INFO_TEMP TABLE 비우기
                bl = bq.trun_if_confirm_temp(con);
    	        bl = bq.tax_confirm_info_trans_temp(con);
    	    
    	    if(bl){ //처리대상 이 없으면 이하는 실행되지않는다.
                        //접수 후 eamil,sma 보내기
                    System.out.println("세금계산서 등록정보 SMS, EMAIL 전송______________________________________");
                        bl = bq.get_sms_email2(con);
                        
                    System.out.println("세금계산서 등록정보 SMS, EMAIL 전송 유무 갱신 FLG 변경__________________________________________________");
                        //7.  IF_TAX_BILL_CONFIRM_INFO MAIL_FLG 값 업데이트 null --> 'Y'
                        bl = bq.if_tax_bill_confirm_info_mail_flg_update(con);
                        //System.out.println("b7 :"+bl);
    	    	}
    	    
    	    if(bl){
    		con.commit();
    	    }
    	    
        
    	    /*배치 프로시져 종료*/
    	}catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
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
      
    /* pross_mail_sms_tran_To_2ndSign
     * kepcobill에서 작성한 매출세금계산서에 대해 ERP에서 수신 받은후
     * 한전에서 회계년도, 전표번호를 if_tax_bill_confirm_info에 입력하면
     * 공급받는자(고객)에게 메일, sms로 세금계산서 발행 사실을 알림
     */   
/*     public boolean pross_mail_sms_tran_To_2ndSign(Connection con){

     	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
     	boolean bl = false;
     	//Connection con = null;
     	try{
             //접수 후 eamil,sma 보내기
           System.out.println("매입세금계산서 2ndSign 요청  SMS, EMAIL 전송 START_______________________");
        	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
           bl = bq.get_sms_email3(con);
           System.out.println("매입세금계산서 2ndSign 요청  SMS, EMAIL 전송 END______________________");
             
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }*/

    /* pross_emailTransfer_tran
     * kepcobill에서 작성한 매출세금계산서에 대해 국세청 신고 완료후
     * 이메일유통 시스템에 xml 데이타 송신 처리
     */   
     public void pross_emailTransfer_tran(){

     	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
     	boolean bl = false;
     	Connection con = null;
     	try{
                 Class.forName(driver);
                 con  = DriverManager.getConnection(url, user, pass);  
                 con.setAutoCommit(false);	
     	    /*배치 프로시져 (인터페이스) Start*/
     	    
     	    //0. 처리할 대상이 있으면 TEMP 테이블에 대상을 잡아 이하를 실행한다. 
                 //8. TB_XML_INFO_TEMP TABLE 비우기
                 bl = bq.trun_tb_xml_info_temp(con);
                 //9. TB_XML_INFO_TEMP TABLE 송신 대상 입력
      	         if(bl){bl = bq.email_tb_xml_info_temp(con);}
     	    
     	    if(bl){ //처리대상 이 없으면 이하는 실행되지않는다.
                System.out.println("이메일유통 테이블(EMAIL_LTF_INFO_TB)에 입력 __________________________________________________");
                bl = bq.email_ltf_info_insert(con);
     	    }
     	    if(bl){
                System.out.println("이메일유통 전송유무  FLG 변경__________________________________________________");
                //7.  IF_TAX_BILL_CONFIRM_INFO MAIL_FLG 값 업데이트 null --> 'Y'(대표이메일 있는경우),'N'(대표이메일 없는경우), 
                bl = bq.tb_xml_info_flg_update(con);
     	    }
     	    
     	    if(bl){
     		con.commit();
     	    }
     	    
         
     	    /*배치 프로시져 종료*/
     	}catch(SQLException e) {
     	    try {
     		con.rollback();
     		e.printStackTrace();
     		throw e;
     	    } catch (SQLException e1) {
     		e1.printStackTrace();
     	    }
     	}catch(Exception e1){
     	    try {
     		con.rollback();
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
        
    /**
     * 
     */
    public void updateNcis_tran() {
		BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
		boolean bl = false;
		boolean bm = false;
		Connection con = null;
		try{
	            Class.forName(driver);
	            con  = DriverManager.getConnection(url, user, pass);  
	            con.setAutoCommit(false);

	            bl = bq.trun_up_temp(con); // temp 
	                // 영업온라인매출 공급받는자  승인  강제 처리 대상 추출
	            	bl = bq.insertNcis(con); // TB_TAX_BILL_ON_BATCH 대상 insert 
		            // ERP매출 수정세금계산서 업체  공급받는자  승인  강제 처리 대상 추출(2011. 11. 1 added by mopuim)
	            	bm = bq.insertERPSellingMOD(con); // TB_TAX_BILL_ON_BATCH 대상 insert 
                  
	            	if(bl || bm){
	            		bl = true;
                    }
	            	
	            	// 해당 처리 건이 있는경우 true
		            if(bl){
		            	bl = bq.result_info_pro(con); // 
		            	bl = bq.tax_bill_info_pro(con); //
		            	bl = bq.status_hist_pro_u(con); //
		            	bl = bq.status_hist_pro_i(con); // 
		            }
		            
		            if(bl){
		    	    	con.commit();
		    	    }
	            /*배치 프로시져 종료*/
		}catch(SQLException e) {
		    try {
			con.rollback();
			e.printStackTrace();
			throw e;
		    } catch (SQLException e1) {
			e1.printStackTrace();
		    }
		}catch(Exception e1){
		    try {
			con.rollback();
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
	
    public boolean tax_info_etax_trans02_NEW(Connection con) throws SQLException, Exception{
		//------변경 후 기능 ---------------------------------------
		// 5-0. 인증서 유효성 검증
//		********* 인증서 유효성 검증을 직접호출하는 샘플 *****************//  
    	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;	
    	boolean tempRslt = false;
		TaxXmlService txs = new TaxXmlService();
//		String[] certInfo =null;
		
//		try {
//			host_ip = InetAddress.getLocalHost().getHostAddress();
//		}catch(Exception e3) {
//			e3.printStackTrace();
//		}
//		try {
//			certInfo = TaxXmlService.getTbCertInfo();
//		} catch (Exception e2) {
//			e2.printStackTrace();
//			//System.exit(-1);//종료
//			return false;
//		}
//		CertUtil cert = null;
//		try {
//			CommUtil.logWriter("인증서화일경로["+certInfo[0]+"]",1);
//			String path_temp = certInfo[0]+"/signCert.der";
//			CommUtil.logWriter("인증서 화일:["+path_temp+"]",1);
//			cert = new CertUtil(path_temp);
//		} catch (CertificateException e1) {
//			System.out.println(e1.getMessage());
//		} catch (Exception e1) {
//			System.out.println(e1.getMessage());
//		}
		
//		if(cert.isValid( certInfo[0])){
//			CommUtil.logWriter("인증서 유효성 검사 성공",1);
//		}else{
//			CommUtil.logWriter("인증서 유효성 검사 실패",1);
//			if(!"168.78.206.19".equals(host_ip)){//운영 서버만  정보인증서버 통신이 되므로 유효성검사 실패 처리 로직 태우기(개발서버는 skip됨)
//			   //System.exit(-1);
//				return false;
//			}   
//		}
		//********* 인증서 유효성 검증을 직접호출하는 샘플 *****************//
		
		
		//클래스 초기화시 체크된 인증서 유효성 값을 확인해 유효한 경우만 처리 되도록 함.
        if(!CertIsvalid){
           CommUtil.logWriter("유효한 인증서가 아닙니다.",4);
           return tempRslt;
        }
		// 5-0. 인증서 화일에서  sign 값 추출
		try {
			
//			MultiThreadClient multiThread = new MultiThreadClient(certInfo);
			
  		// 5-1. 전자세금계산서 기본정보  TB_TAX_BILL_INFO에서 추출해서  tbTaxBillList 에 담기
			//List tbTaxBillList = null;
			List tbTaxBillList = new ArrayList();
			TbTaxBillInfoVo taxBillInfo = new TbTaxBillInfoVo();
			List tbTradeItemlist = new ArrayList();

			tbTaxBillList = bq.tax_info_etax_trans_new02(con);
		// 6. TB_TAX_BILL_ON_BATCH, TB_TAX_BILL_INFO,TB_TRADE_ITEM_LIST 에서
    	//    대상 정보를 추출해서 전자세금계산서 xml 생성 및 전자서명 처리해서 ET_TIDOC_IF에 입력
		//    - 전자세금계산서 xml 생성 : 매입온라인과 마찬가지로 kr.co.kepco.etax30.buying.lib.TaxCreateXml 사용
		//    - 전자세금계산서 전자서명 : kr.co.kepco.etax30.selling.batchoffline.CreateXml METHOD 참조해서 새로 만든
		//                           CreateXmlOnline METHOD 사용
   		// 처리 순서  tbTaxBillList for문으로 돌리면서 item 정보 추출 -> 세금계산서 xml생성 -> 전자서명 처리 -> ET_TIDOC_IF에 저장 
			String TaxBillXmlStr = new String();
			String TaxBillXmlSigned = new String();
			SignPostionData signData = null;
			signData = new SignPostionData();
            TaxCreateXml taxcreateXml = new TaxCreateXml();
            int list_size = tbTaxBillList.size();
            CommUtil.logWriter("##list_size:"+list_size,1);
            for(int i=0;i<list_size;i++){
            	taxBillInfo = (TbTaxBillInfoVo)tbTaxBillList.get(i);
                // 해당 세금계산서 품목정보 가져오기
            	taxBillInfo.setIo_code("1");
            	tbTradeItemlist = bq.tax_info_item_trans_new02(con, taxBillInfo);
            	//품목이 있는경우만  처리 
            	if(tbTradeItemlist.size()>0){
                // 세금계산서 xml생성
            		
                    CommUtil.logWriter("issue_id:"+taxBillInfo.getIssue_id(),1);
                    TaxBillXmlStr = taxcreateXml.createXml(taxBillInfo,tbTradeItemlist);
	            	
	            	CommUtil.logWriter("taxBillInfo.getIssue_id():"+taxBillInfo.getIssue_id(),1);
	                CommUtil.logWriter("##1111 서명전 전자세금계산서 xml 생성 시작 [taxcreateXml.createXml]#############################",1);
	                CommUtil.logWriter(TaxBillXmlStr,1);
	                CommUtil.logWriter("##1111 서명전 전자세금계산서 xml 생성 완료 [taxcreateXml.createXml]#############################",1);
	            	// 세금계산서 전자서명 처리
	            	TaxBillXmlSigned = signData.createSign(TaxBillXmlStr, sign, cert);
	            	// ET_TIDOC_IF에 저장
	            	CommUtil.logWriter("taxBillInfo.getIssue_id():"+taxBillInfo.getIssue_id(),1);
	                CommUtil.logWriter("##2222 서명후 전자세금계산서 xml 생성 시작 [signData.createSign]#############################",1);
	                CommUtil.logWriter(TaxBillXmlSigned,1);
	                CommUtil.logWriter("##2222 서명후 전자세금계산서 xml 생성 완료 [signData.createSign]#############################",1);
	
	                CommUtil.logWriter("## TB_XML_INFO에 서명완료된 XML 저장 시작 [TaxXmlService.setXmlForSelling]#############################",1);
	                txs.setXmlForSelling(con, "1", taxBillInfo.getBiz_manage_id(), 
	                		             TaxBillXmlSigned, taxBillInfo.getIssue_day(), rValue, "BATCH");//TB_XML_INFO에 서명완료된 XML 및 RVALUE를 저장
	                CommUtil.logWriter("## TB_XML_INFO에 서명완료된 XML 저장 완료 [TaxXmlService.setXmlForSelling]#############################",1);
	    			//System.out.println("");
	            	tempRslt = true;
            	}
            }
        }catch(SQLException e1){
        	tempRslt = false;
			System.out.println("tax_info_etax_trans02_NEW SQLException : "+e1.toString());
			System.out.println(e1.getMessage());
            throw e1;
		} catch (Exception e) {
        	tempRslt = false;
			System.out.println("tax_info_etax_trans02_NEW Exception : "+e.toString());
			System.out.println(e.getMessage());
        	throw e;
//		    try {
//			conn.rollback();
//			e.printStackTrace();
//			throw e;
//		    } catch (SQLException e1) {
//			e1.printStackTrace();
//		    }
//		e.printStackTrace();
		}
		return tempRslt;
    }

    
	public static void main(String[] args) {}
}
