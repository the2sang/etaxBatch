package kr.co.kepco.etax30.selling.batchoffline;

import java.sql.Connection;
import java.sql.SQLException;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;
import kr.co.kepco.etax30.selling.util.CommProperties;

 
//import signgate.xml.util.XmlSecu;
//import signgate.xml.util.XmlSignature;
import signgate.xml.util.*;
//2011.2.21 추가 kdy
import signgate.crypto.util.*;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : MultiThreadClient
 * 프로그램 아이디      : MultiThreadClient.java
 * 프로그램 개요        : Thread 실행 프로그램                                               
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - run()
 * </METHOD>
******************************************************************************/
public class MultiThreadClient extends Thread 
												
{

	int idx;
	TbBatchJobHistVo batchHist;
	String targetMonth;
	String now_grp_no;
	String rValue;
	String[] cert;
	int MAXDbConRecursiveCallCnt; 
	int DbConErrorRetryInterval;
	int local_i = 0;
    
	// 2011.08.25 modified by 박상종 
	// static 변수 선언으로 때때로 전자서명시 sign 변수에 추가하는 
	//xml element(현재 소스 83~109line)가 누락되어 전자서명 검증 오류가 발생함.
	//static XmlSecu XmlSecu = null;
	//static XmlSignature sign = null;
	XmlSecu XmlSecu = null;
	public XmlSignature sign = null;

	
	public MultiThreadClient(int idx, TbBatchJobHistVo batchHist,
		String targetMonth, String now_grp_no, String rValue, String[] cert) {
		System.out.println("MultiThreadClient : "+idx);
		System.out.println("MultiThreadClient : "+batchHist);
		System.out.println("MultiThreadClient : "+targetMonth);
		System.out.println("MultiThreadClient : "+rValue);
		System.out.println("MultiThreadClient : "+cert);
		this.idx = idx;
		this.batchHist = batchHist;
		this.targetMonth = targetMonth;
		this.now_grp_no = now_grp_no;
		this.rValue = rValue;
		this.cert = cert;

 		String secu_cfg_path = CommProperties.getString("CERT_SECU_CFG_PATH");
		CommUtil.logWriter("인증모듈 설정화일경로:"+secu_cfg_path,4);		

 		this.MAXDbConRecursiveCallCnt = Integer.parseInt(CommProperties.getString("DBCON_RECURSIVECALL_CNT"));
 		this.DbConErrorRetryInterval = Integer.parseInt(CommProperties.getString("DBCON_ERRORRETRY_INTERVAL"));
 		
		CommUtil.logWriter("MAXDbConRecursiveCallCnt:"+MAXDbConRecursiveCallCnt,2);		
		CommUtil.logWriter("DbConErrorRetryInterval:"+DbConErrorRetryInterval,2);		
		
		XmlSecu = new XmlSecu(secu_cfg_path);
		XmlSecu.setEncTypeEncoding("UTF-8");
		XmlSecu.setEncEncoding("UTF-8");
		XmlSecu.setSignEncoding("UTF-8");
		XmlSecu.setFileEncoding("UTF-8");
		
		try {
			sign = new XmlSignature(XmlSecu);
			
		//###############################################################################
		// NEW 버젼 코드(2011.11.15)
			CommUtil.logWriter("인증서 화일경로(cert[0]):"+cert[0],4);		
			CertUtil certutil = new CertUtil(cert[0]+"/signCert.der");
       //###############################################################################
		// OLD 버젼 코드
			//2011.2.21 고도화작업 적용 KDY
			//int cert_index = 1; // 환경설정의 첫번째 인증서(한전기존sha1인증서)
			// 환경설정의 첫번째 인증서(테스트1111111119,sha256인증서,나중에 정식한전256인증서갱신때 바꾸어줘야함)
		/*	int cert_index = 1; 
			CertUtil certutil = new CertUtil(XmlSecu.getDerCertInfo(cert_index, XmlSecu.SIGN));*/
        //###############################################################################
            String signAlgo = certutil.getPublicKey().getAlgorithm().toLowerCase();
			String signatureAlgorithm = certutil.getSigAlgName().toLowerCase();
			
			CommUtil.logWriter("signAlgo:"+signAlgo,4);		
			CommUtil.logWriter("signatureAlgorithm:"+signatureAlgorithm,4);		
			
			//추가끝
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

//	static Connection con;
//
//	static {
//		Connection con = null;
//		try {
//			con = new Dbcon().getConnection();
//			//con = new Dbcon().getConnection(DbConRecursiveCallCnt, DbConErrorRetryInterval);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//			CommUtil.logWriter(e1.toString(),4);
//			System.exit(-1);
//		}
//	}


	//
	public MultiThreadClient(String[] cert) {
     try{
		TbBatchJobHistVo batch = new TbBatchJobHistVo();
	    batch = null; //임시로 설정
		String rValue = Rvalue.getRvalue();
		//new MultiThreadClient(0, batch, "000000", rValue, cert);
		new MultiThreadClient(0, batch, "000000", now_grp_no, rValue, cert);
        } catch (Exception e) {
			e.printStackTrace();
		}
     }
	// final static String SECU_ADDR = CommProperties.getString("SECU_ADDR");

	public void run() {
		long starttime = System.currentTimeMillis();
		Connection con = null;
		try {
			con = Dbcon.getConnection();
			//con = new Dbcon().getConnection(DbConRecursiveCallCnt, DbConErrorRetryInterval);
		} catch (SQLException e1) {
			e1.printStackTrace();
    		CommUtil.logWriter(e1.toString(),4);
			System.exit(-1);
		}



		TaxXmlService txs = null;

		String machine = System.getProperty("machine");
		String[][] rowIdlist = null;

		try {
			txs = new TaxXmlService();
			rowIdlist = txs.getBtmpRowIDList(con, machine, idx);

			//System.out.println("Thread" + idx + " 실행 작업수: " + rowIdlist.length);
			boolean result = false;
			long total = 0;
			long amount = 0;
			long cnt = 0;
			//String[] certInfo = null;
			//certInfo = getCertInfo();
			
			for (int i = 0; i < rowIdlist.length; i++) {
				
				TaxCreateXml tax_xml_create = new TaxCreateXml();
				while(local_i <= MAXDbConRecursiveCallCnt){
						CommUtil.logWriter("createXml 생성전"+rowIdlist[i][0],2);
						result = tax_xml_create.createXml(rowIdlist[i][0], con, sign, batchHist, targetMonth, now_grp_no, cert, rValue, MAXDbConRecursiveCallCnt, DbConErrorRetryInterval, local_i);
						CommUtil.logWriter("createXml 생성후"+rowIdlist[i][0],2);
                        if(!result){//처리 실패
		        	    	CommUtil.logWriter("[ "+local_i+"/"+MAXDbConRecursiveCallCnt+"] 회 에러 발생[MultiThreadClien run METHOD]\n",4);
	                          // Db connection을 새로 연결
	        	    		Dbcon.close(con);
	        	    		con = new Dbcon().getConnection();
	                          Thread.sleep(DbConErrorRetryInterval);//dbErrorRetryInterval 1/1000초
	               			 local_i++;
	               			 if(local_i>= MAXDbConRecursiveCallCnt){
	               				CommUtil.logWriter("최대 에러발생 한계점 "+MAXDbConRecursiveCallCnt+"회를 초과해서 비정상 종료합니다.["+local_i+"]회 에러 발생",1);
	               				break;
	               			 }
	               		  }else{//처리 성공
	               		       break;
	                       }
	             }
	 			 local_i = 0;

						
				if (result) {
					// 처리 금액 합
					amount = Long.parseLong(rowIdlist[i][1]);
					total = total + amount;
					cnt++;
					if (cnt % 10000 == 0) {
						txs.setAmountBatchJob(con, batchHist, total);
						txs.setCountBatchJob(con, batchHist, cnt);
						cnt = 0;
						total = 0;
					}
				}
				if ((i % 10000 == 0 && i != 0) || i == rowIdlist.length ) {
					CommUtil.logWriter("Thread" + idx + "의 " + i + "개 xml 생성 완료 [Thread별 10,000개마다 기록]",4);
				}
				tax_xml_create = null;
						
			}
			txs.setAmountBatchJob(con, batchHist, total);
			txs.setCountBatchJob(con, batchHist, cnt);

		} catch (Exception e) {
			System.out.println("Thread err?");
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
					con = null;
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		System.out.println("run()프로세스 종료 ");
		long endtime = System.currentTimeMillis();
		System.out.println("run()전체 시작 시간 : " + starttime);
		System.out.println("run()전체 종료 시간 : " + endtime);
		System.out.println("run()전체 경과 시간 : " + (endtime - starttime) / 1000.0);
	}



/*
	public void run() {
		long starttime = System.currentTimeMillis();
		Connection con = null;
		try {
			con = new Dbcon().getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
			CommUtil.logWriter(e1.toString(),4);
			System.exit(-1);
		} 

		TaxXmlService txs = null;

		String machine = System.getProperty("machine");
		String[][] rowIdlist = null;

		try {
			txs = new TaxXmlService();
			rowIdlist = txs.getBtmpRowIDList(con, machine, idx);

			//System.out.println("Thread" + idx + " 실행 작업수: " + rowIdlist.length);
			boolean result = false;
			long total = 0;
			long amount = 0;
			long cnt = 0;
			//String[] certInfo = null;
			//certInfo = getCertInfo();
			
			for (int i = 0; i < rowIdlist.length; i++) {
				TaxCreateXml tax_xml_create = new TaxCreateXml();
				
				//System.out.println("	createXml 시작 : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				
				CommUtil.logWriter("createXml 생성전"+rowIdlist[i][0],2);
				
				//result = tax_xml_create.createXml(rowIdlist[i][0], con, sign,
				//		batchHist, targetMonth, now_grp_no, cert, rValue);
			   result = tax_xml_create.createXml(rowIdlist[i][0], con, sign, batchHist, targetMonth, now_grp_no, cert, rValue, DbConRecursiveCallCnt, DbConErrorRetryInterval);
				

				CommUtil.logWriter("createXml 생성후"+rowIdlist[i][0],2);
				
				//System.out.println("result = "+result);
				
				if (result) {
					// 처리 금액 합
					amount = Long.parseLong(rowIdlist[i][1]);
					total = total + amount;
					cnt++;
					if (cnt % 10000 == 0) {
						txs.setAmountBatchJob(con, batchHist, total);
						txs.setCountBatchJob(con, batchHist, cnt);
						cnt = 0;
						total = 0;
					}
				}
				if ((i % 10000 == 0 && i != 0) || i == rowIdlist.length ) {
					CommUtil.logWriter("Thread" + idx + "의 " + i + "개 xml 생성 완료 [Thread별 10,000개마다 기록]",4);
					//System.out.println("Thread" + idx + " count : " + i + ":"
					//		+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				}
				tax_xml_create = null;
			}
			txs.setAmountBatchJob(con, batchHist, total);
			txs.setCountBatchJob(con, batchHist, cnt);

		} catch (Exception e) {
			System.out.println("Thread err?");
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
					con = null;
				} catch (Exception e) {
					System.out.println(e);
				}
		}
		System.out.println("run()프로세스 종료 ");
		long endtime = System.currentTimeMillis();
		System.out.println("run()전체 시작 시간 : " + starttime);
		System.out.println("run()전체 종료 시간 : " + endtime);
		System.out.println("run()전체 경과 시간 : " + (endtime - starttime) / 1000.0);
	}
*/	
	
	
	
	
/*	static synchronized String[] getCertInfo() {
	String[] info = null;
	try {
		info = TaxXmlService.getTbCertInfo();
	} catch (Exception e2) {
		e2.printStackTrace();
		System.exit(-1); //종료
	}
	return info;
}
*/

/*	static {
	// XmlSecu = XmlSecu.getInstance("C:/KICASecuXML/config/secu.cfg");
	//XmlSecu = XmlSecu.getInstance(getCertInfo()[0] + "/secu.cfg");
	// XmlSecu = XmlSecu.getInstance(certInfo[0]+"/secu.cfg");
	// XmlSecu = XmlSecu.getInstance(SECU_ADDR);
	//XmlSecu = XmlSecu.getInstance( getCertInfo()[0] + "/secu.cfg");
	
}*/


}
