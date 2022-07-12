package kr.co.kepco.etax30.selling.batchoffline;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.ArrayList;

//import kr.co.kepco.etax30.selling.util.CommProperties;

//import org.apache.log4j.Logger;

import signgate.crypto.util.FileUtil;
import signgate.xml.util.XmlSecu;
import signgate.xml.util.XmlSignature;
import kr.co.kepco.etax30.selling.util.CommUtil;

//import signgate.crypto.util.KicaUtil;//2014. 4. 1. 추가
import signgate.xml.util.XmlUtil;//2014. 4. 1. 추가

public class SignPostionData {
	//Logger logger = Logger.getLogger(SignPostionData.class);
	//final static String DER = CommProperties.getString("DER");
	//final static String KEY = CommProperties.getString("KEY");
	//final static String PASSWD = CommProperties.getString("PASSWD");
	
	// 매출 온라인배치, CD배치 공통 METHOD
/*	
	public String createSign(String inputData, XmlSignature sign, String[] certInfo) throws Exception{
		
		String signData = "";
	    try {
    	
//	        signData = sign.signData(inputData, 
//					        		der, 
//					        		key, 
//					        		pwd, 
//					        		"//TaxInvoice/ExchangedDocument", 
//					        		XmlSecu.NextSibling
//	        						);

	    	//inputData = XmlUtil.stripNonValidXMLCharacters(inputData);
  		    //CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이후 inputData 시작============" ,0);
  		    //CommUtil.logWriter(inputData, 0);
  		    //CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이후 inputData 끝 =============",0);
	    	
	        signData = sign.signData(inputData,	
	        		//FileUtil.readBytesFromFileName(DER), 
	        		FileUtil.readBytesFromFileName(certInfo[0]+"/signCert.der"), 
	        		//FileUtil.readBytesFromFileName(KEY), 
	        		FileUtil.readBytesFromFileName(certInfo[0]+"/signPri.key"), 
//	        		FileUtil.readStringFromFileName("C:/KICASecuXML/cert/web/passwd"), 
	        		//PASSWD, 
	        		certInfo[1], 
	        		"//TaxInvoice/ExchangedDocument", 
	        		XmlSecu.NextSibling
//	        		signData = sign.signData(inputData,	
//	        				FileUtil.readBytesFromFileName("C:/KICASecuXML/cert/web/signCert.der"), 
//	        				FileUtil.readBytesFromFileName("C:/KICASecuXML/cert/web/signPri.key"), 
////	        		FileUtil.readStringFromFileName("C:/KICASecuXML/cert/web/passwd"), 
//	        				"3XVR87b3LwZRXqYSxNTruQbOOF7UiD++fj0hGqAvS28=", 
//	        				"//TaxInvoice/ExchangedDocument", 
//	        				XmlSecu.NextSibling
//	        
	        		);

//	        System.out.println("signData"+ signData);
	        //System.out.println("[Sign ERROR] : " + sign.getErrorMsg());
	        //String base64R  = "";
	        if (signData != null) {
	  		   //System.out.println("전자서명 생성 성공: " + signData);
	        	//System.out.println("전자서명 생성 성공: ");
	     		   
	  		   ArrayList list = new ArrayList();		   
	  		   boolean chkBoolean = sign.vrfSoapSignData(signData, list);
	  		   if (chkBoolean)
	  		   {
	  			// System.out.println("전자서명 검증 성공");			 
	  	        //CommUtil.logWriter("###############XML INPUT 데이타######################\n"+inputData,0);
			  	//CommUtil.logWriter("###############전자서명 XML 데이타######################\n"+signData,0);
		  		CommUtil.logWriter("전자서명 검증 성공",0);
	  			   
	  		   } 
	  		   else 
	  		   {
	  			 //System.out.println("전자서명 검증 실패 : "+sign.getErrorMsg());
		  	     CommUtil.logWriter("###############XML INPUT 데이타 1######################\n"+inputData,4);
		  	     CommUtil.logWriter("###############전자서명 XML 데이타 1######################\n"+signData,4);
	  			 CommUtil.logWriter("전자서명 검증 실패1 : "+sign.getErrorMsg(),4);
	  			 
	  			 throw new Exception("전자서명 검증 실패1");
	  		   }
	        } else {
	        	//System.out.println("전자서명 생성 실패");
	        	//System.out.println("[ERROR] : "+sign.getErrorMsg());
		  	    CommUtil.logWriter("###############XML INPUT 데이타 2######################\n"+inputData,4);
		  	    CommUtil.logWriter("###############전자서명 XML 데이타 2######################\n"+signData,4);
	  			CommUtil.logWriter("전자서명 검증 실패2 : "+sign.getErrorMsg(),4);
	        	throw new Exception("전자서명 생성 실패2");
	        }

	      } catch (Exception e){
	       e.printStackTrace();
	       System.out.println("#############################################################################################");
	       //System.out.println("sign.getErrorMsg() : "+sign.getErrorMsg());
	  	   CommUtil.logWriter("###############XML INPUT 데이타 3######################\n"+inputData,4);
	  	   CommUtil.logWriter("###############전자서명 XML 데이타 3######################\n"+signData,4);
		   CommUtil.logWriter("sign.getErrorMsg() : "+sign.getErrorMsg(),4);
	       System.out.println("#############################################################################################");
	       
	       throw e;
	      }
	      
	      return signData;
	}
*/
	// 매출 온라인배치, CD배치 공통 METHOD	
	public String createSign(String inputData, XmlSignature sign, String[] certInfo) throws Exception{
		
		String signData = "";
	    try {
	        signData = sign.signData(inputData,	
	        		FileUtil.readBytesFromFileName(certInfo[0]+"/signCert.der"), 
	        		FileUtil.readBytesFromFileName(certInfo[0]+"/signPri.key"), 
	        		certInfo[1], 
	        		"//TaxInvoice/ExchangedDocument", 
	        		XmlSecu.NextSibling
	        		);

	        //------------------------------------------------------------------------
	        // inputData에 널값(hex값 00)이 포함되는경우 제거하고 다시 서명 처리
	        // 2014. 4. 4. 추가
	        if (signData == null) {
  		        CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이전 inputData 시작============" ,4);
  		        CommUtil.logWriter(inputData, 4);
  		        CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이전 inputData 끝 =============",4);
  		        // xml데이타에 널값(hex값 00)이 포함된 경우 제거히는 method 2014.4.4 추가 
	    	    inputData = XmlUtil.stripNonValidXMLCharacters(inputData);
  		        CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이후 inputData 시작============" ,4);
  		        CommUtil.logWriter(inputData, 4);
  		        CommUtil.logWriter("정보인증  stripNonValidXMLCharacters METHOD 이후 inputData 끝 =============",4);
		        signData = sign.signData(inputData,	
		        		FileUtil.readBytesFromFileName(certInfo[0]+"/signCert.der"), 
		        		FileUtil.readBytesFromFileName(certInfo[0]+"/signPri.key"), 
		        		certInfo[1], 
		        		"//TaxInvoice/ExchangedDocument", 
		        		XmlSecu.NextSibling
		        		);
	        }
	        //------------------------------------------------------------------------

	        if (signData != null) {
	  		   ArrayList list = new ArrayList();		   
	  		   boolean chkBoolean = sign.vrfSoapSignData(signData, list);
	  		   if(chkBoolean){
		  		  CommUtil.logWriter("전자서명 검증 성공",0);
	  		   }else{
	  			 //System.out.println("전자서명 검증 실패 : "+sign.getErrorMsg());
		  	      CommUtil.logWriter("###############XML INPUT 데이타 1######################\n"+inputData,4);
		  	      CommUtil.logWriter("###############전자서명 XML 데이타 1######################\n"+signData,4);
	  			  CommUtil.logWriter("전자서명 검증 실패1 : "+sign.getErrorMsg(),4);	  			 
	  			  throw new Exception("전자서명 검증 실패1");
	  		   }
	        }else{
	        	//System.out.println("전자서명 생성 실패");
	        	//System.out.println("[ERROR] : "+sign.getErrorMsg());
		  	    CommUtil.logWriter("###############XML INPUT 데이타 2######################\n"+inputData,4);
		  	    CommUtil.logWriter("###############전자서명 XML 데이타 2######################\n"+signData,4);
	  			CommUtil.logWriter("전자서명 검증 실패2 : "+sign.getErrorMsg(),4);
	        	throw new Exception("전자서명 생성 실패2");
	        }
	      } catch (Exception e){
	       e.printStackTrace();
	       System.out.println("#############################################################################################");
	  	   CommUtil.logWriter("###############XML INPUT 데이타 3######################\n"+inputData,4);
	  	   CommUtil.logWriter("###############전자서명 XML 데이타 3######################\n"+signData,4);
		   CommUtil.logWriter("sign.getErrorMsg() : "+sign.getErrorMsg(),4);
	       System.out.println("#############################################################################################");
	       
	       throw e;
	      }
	      
	      return signData;
	}
}
