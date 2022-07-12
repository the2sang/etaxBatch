package kr.co.kepco.etax30.selling.batchoffline;


import java.net.InetAddress;
import java.security.cert.CertificateException;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;

//import org.apache.log4j.Logger;

import signgate.crypto.util.CertUtil;


/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : MultiThreadCaller
 * 프로그램 아이디      : MultiThreadCaller.java
 * 프로그램 개요        : Thread 생성 프로그램                                                
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - start()
 * </METHOD>
******************************************************************************/
public class MultiThreadCaller {
	//static Logger logger = Logger.getLogger(MultiThreadCaller.class);
	
	public void Caller( int threadNum, TbBatchJobHistVo batchHist, String targetMonth, String now_grp_no){
		
		long starttime = System.currentTimeMillis();
		String host_ip = "";
		String[] certInfo =null;
		try {
			certInfo = TaxXmlService.getTbCertInfo();
		} catch (Exception e2) {
			e2.printStackTrace();
			System.exit(-1);//종료
		}
		
		
//********* 인증서 유효성 검증을 직접호출하는 샘플 *****************//  
		
		CertUtil cert = null;
		try {
			host_ip = InetAddress.getLocalHost().getHostAddress();
			
			System.out.println("host_ip:"+host_ip);		

			CommUtil.logWriter(certInfo[0]+"인증서화일경로",4);	

			String path_temp = certInfo[0]+"/signCert.der";
			System.out.println("인증서 화일:["+path_temp+"]");		
			cert = new CertUtil(path_temp);
			//cert = new CertUtil("/data5/ebxml/kepcobill2/kepcobill2/certrepository/20111020192958/signCert.der");
			
			
		} catch (CertificateException e1) {
//			e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (Exception e1) {
//			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
		
		//if(cert.isValid(CRL_ADDR)){
		//if(cert.isValid( certInfo[0]+"/CRL")){
		if(cert.isValid( certInfo[0])){
			System.out.println("인증서 유효성 검사 성공");		
		}else{
			System.out.println("인증서 유효성 검사 실패");
			//if(!"168.78.206.19".equals(host_ip))//개발DB서버는 정보인증서버 통신이 안되므로 유효성검사 실패 skip처리
			if("168.78.201.56".equals(host_ip) || "168.78.201.57".equals(host_ip))//운영서버만 정보인증서버 통신이 되므로 체크 처리 
			   System.exit(-1);
		}
		//********* 인증서 유효성 검증을 직접호출하는 샘플 *****************//	
		
		TbBatchJobHistVo batch = new TbBatchJobHistVo();
		try {
			batch = new TaxXmlService().getTbBatchJobHist(batchHist);
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		
		try {
			String rValue = Rvalue.getRvalue();
			//logger.info("작업 시작!");
			MultiThreadClient[] multiThread = new MultiThreadClient[threadNum];
			for(int i=0; i< threadNum; i++){ 
				multiThread[i] = new MultiThreadClient(i, batch, targetMonth, now_grp_no, rValue, certInfo);
				multiThread[i].start();
			}
			for (int i = 0 ; i < threadNum ; i++ )
			{
				try{
					multiThread[i].join();
				}
				catch (InterruptedException ex){
					System.out.println("InterruptedException : "+ex.toString());
				}
			}		
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("MultiThreadCaller Exception : "+e.toString());
			System.out.println(e.getMessage());
		}
		//logger.info("작업 모두 끝!");
		
		long endtime = System.currentTimeMillis();
		System.out.println("전체 시작 시간 : " + starttime);
		System.out.println("전체 종료 시간 : " + endtime);
		System.out.println("전체 경과 시간 : " + (endtime - starttime)/1000.0 );
		
	}
}
