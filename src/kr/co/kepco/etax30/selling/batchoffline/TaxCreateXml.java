package kr.co.kepco.etax30.selling.batchoffline;

//import java.io.File;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import kr.co.kepco.etax30.selling.util.*;
import signgate.xml.util.XmlSignature;
import signgate.xml.util.XmlUtil;

//import kr.co.kepco.etax30.selling.util.Dbcon;
import kr.co.kepco.etax30.selling.vo.ItemListVo;
import kr.co.kepco.etax30.selling.vo.TbBatchErrHistVo;
import kr.co.kepco.etax30.selling.vo.TbBatchJobHistVo;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;

/******************************************************************************
 * 저작권				: 

 * 프로젝트	명          : 전자세금계산서 시스템 개발
 * 프로그램	명          : TaxCreateXml
 * 프로그램 아이디      : TaxCreateXml.java
 * 프로그램	개요	    : 전자서명된 전자세금계산서 XML 생성
 * 관련	테이블		    : TB_TAX_BILL_INFO, TB_TRADE_ITEM_LIST, TB_XML_INFO, TB_STATUS_HIST
 * 관련 모듈			: 
 * 작성자				: 양형공
 * 작성일자			    : 2008-12-31

 * 개정이력(성명 | 일자 | 내용)	: 양형공 | 2009-12-31

 * <METHOD>
 * .....
 * </METHOD>
******************************************************************************/
public class TaxCreateXml {
	
	final static String FILEPATH = CommProperties.getString("FILEPATH");
	
	/**
	 * 상품목록 템플릿
	 */

	public static StringBuffer ITEM = new StringBuffer()
	.append(" <TaxInvoiceTradeLineItem>                         	    \n")
	.append(" <SequenceNumeric>@SequenceNumeric</SequenceNumeric>      	\n")
	.append(" @DescriptionText                                       	\n")
	.append(" @InvoiceAmount                              		    	\n")
	.append(" @ChargeableUnitQuantity    								\n")
	.append(" @InformationText                         					\n")
	.append(" @NameText                                              	\n")
	.append(" @PurchaseExpiryDateTime    								\n")
	.append("   @TotalTax/CalculatedAmount           					\n")
	.append("   @UnitPrice/UnitAmount                           		\n")
	.append("   </TaxInvoiceTradeLineItem>                           	\n");

	/**
	 * 결제방법 템플릿
	 */
	public static StringBuffer PAYMENT = new StringBuffer()
	.append(" @TypeCode                       							\n")
	.append(" @PaidAmount                     							\n");
	
	int local_i = 0;
	

	
	/**
	 * 
	 * @param rowId : 전자세금정보 테이블의 처리할 데이터 row id 
	 * @param con : Connection
	 * @param sign : XmlSignature 객체(전자서명)
	 * @param batchHist : 배치이력객체
	 * @param targetMonth : 대상년월
	 * @param certInfo : 인증서 저장경로 위치 및 패스워드 정보
	 * @param rValue : R value
	 * @return : boolean
	 */
	public boolean createXml( String rowId , Connection con, XmlSignature sign, 
			TbBatchJobHistVo batchHist, String targetMonth, String now_grp_no, String[] certInfo, String rValue,
			int MAXDbConRecursiveCallCnt, int DbConErrorRetryInterval, int now_DbConRecursiveCallCnt){
		
		String flag = "05"; //file 생성완료
		String statusDesc = "CD파일생성완료"; //XML 생성완료
		String issueDay = null;
		String bizId = null;
		String relSystemId = null;
		String jobGubCode = null;
		String manageId = null;
		String issueId = null;
		String IOcode = "1";
		String registDt = null;

		//boolean result = false;
		
		TbTaxBillInfoVo taxBillInfo = null;
		StringBuffer payInfo = null;
		StringBuffer itemInfo = null;
		CommTaxMapper mapper = null;
		SignPostionData signData = null;
		String commonInfo = null;
		String temp = null;
		String template = null;
		List tbTradeItemList = null;
		ItemListVo tbTradeItem = null;
		TaxXmlService txs = new TaxXmlService();
		
	    
		
		try {
			taxBillInfo = txs.getTbTaxBillInfo(rowId, con, targetMonth );
			//#########################################################################
			// 2015.7.8 수정 by 박상종 
			//일시적 DB연결 장애시 db connection을 다시 맺어 처리될수 있게 수정
			/*
			  
			 while(local_i <= MAXDbConRecursiveCallCnt){
				//XML 테이블에 INSERT, STATUS 01 완료, STATUS 05 INSERT 
				//UPDATE ELECTRONIC_REPORT_YN = Y, STATUS= 05
				taxBillInfo = txs.getTbTaxBillInfo(rowId, con, targetMonth );
                if(taxBillInfo==null){//처리 실패
                	CommUtil.logWriter("getTbTaxBillInfo( rowId= "+rowId+" )",4);
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
			*/
			//#########################################################################
			
			
			template = CommTaxXmlSpec.getInstance().GetTaxXML();

			bizId = taxBillInfo.getBiz_manage_id();
			issueId = taxBillInfo.getIssue_id();
			issueDay = taxBillInfo.getIssue_day();
			relSystemId = taxBillInfo.getRel_system_id();
			jobGubCode = taxBillInfo.getJob_gub_code();
			manageId = taxBillInfo.getSvc_manage_id();
			registDt = taxBillInfo.getRegist_dt();

			payInfo  = new StringBuffer();
			itemInfo = new StringBuffer();
			
			mapper = new CommTaxMapper();	
			signData = new SignPostionData();
			//boolean rslt = false;
				
				//기본 세금계산서 정보 추가
				commonInfo = mapper.getString(template, mapper.makeCommonMapping(taxBillInfo));
				//System.out.println("세금계산서 템플릿에 정보추가 끝 : rowId : "+rowId+" : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS")+"\n");
				
				//결제방법값이 있을경우 추가	
				if(taxBillInfo.getPayment_type_code1() != null){
					String[] pay = {taxBillInfo.getPayment_type_code1(), String.valueOf(taxBillInfo.getPay_amount1())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));

					payInfo.append(p);
				}
				if(taxBillInfo.getPayment_type_code2() != null){
					String[] pay = {taxBillInfo.getPayment_type_code2(), String.valueOf(taxBillInfo.getPay_amount2())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));
					payInfo.append(p);
				}
				if(taxBillInfo.getPayment_type_code3() != null){
					String[] pay = {taxBillInfo.getPayment_type_code3(), String.valueOf(taxBillInfo.getPay_amount3())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));
					
					payInfo.append(p);
				}
				if(taxBillInfo.getPayment_type_code4() != null){
					String[] pay = {taxBillInfo.getPayment_type_code4(), String.valueOf(taxBillInfo.getPay_amount4())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));
					
					payInfo.append(p);
				}
				
				
				//20160407 변경전							
				//결제방법을 추가
				commonInfo = mapper.getString(commonInfo, mapper.makePayMapping(payInfo.toString()));
				
				//20160407 변경후						
				//결제방법을 추가
				//commonInfo = payInfo.toString();


				System.out.println("create XML File:" +now_grp_no + "\n");
				
				//해당 세금계산서의 상품 목록을 가져옴
				tbTradeItemList = txs.getTbTradeItemList(rowId, con);
				//#########################################################################
				// 2015.7.8 수정 by 박상종 
				//일시적 DB연결 장애시 db connection을 다시 맺어 처리될수 있게 수정
				/*
				 
				 while(local_i <= MAXDbConRecursiveCallCnt){
					//XML 테이블에 INSERT, STATUS 01 완료, STATUS 05 INSERT 
					//UPDATE ELECTRONIC_REPORT_YN = Y, STATUS= 05
					tbTradeItemList = txs.getTbTradeItemList(rowId, con);
	                if(tbTradeItemList==null){//처리 실패
	                	CommUtil.logWriter("tbTradeItemList( rowId= "+rowId+" )",4);
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
				*/
				//#########################################################################
				
				
				Iterator i = tbTradeItemList.iterator();
				while(i.hasNext()){
					tbTradeItem = (ItemListVo)i.next();
					temp = mapper.getString(ITEM.toString() , mapper.makeItemMapping(tbTradeItem));
					itemInfo.append(temp);	
				}
				
				//상품목록을 추가
				commonInfo = mapper.getString(commonInfo, mapper.makeItemMapping(itemInfo.toString()));
				//System.out.println("상품목록을 추가끝 : rowId : "+rowId+" : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS")+"\n");
				
				//전자서명 추가(String)
			    //synchronized (java.lang.Object.class) {commonInfo = signData.createSign(commonInfo);}
			    
				//System.out.println("전자서명 시작 : rowId : "+rowId+" : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS")+"\n");
				commonInfo = signData.createSign(commonInfo, sign, certInfo);
				//System.out.println("전자서명 끝 : rowId : "+rowId+" : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS")+"\n");
				
				//  전자서명 오류 인위적으러 발생시키는 테스트
//				CommUtil.logWriter(issueId+" 전자서명시작",0);
//				if(issueId.equals("2011072550000001f0006979") || issueId.equals("2011071550000001f0003663") || issueId.equals("2011071450000001h0012772")){
//					throw new Exception("issueId:"+issueId+"전자서명 실패 테스트를 위해 강제 오류 발생");
//				}
//				CommUtil.logWriter(issueId+" 전자서명끝",0);
				XmlUtil.genFileCreate(FILEPATH+"/"+targetMonth+now_grp_no+"/"+issueId+".xml", commonInfo, "UTF-8");

				// 새로운 파일 쓰기 성능 테스트 (2022.07.13 - 강희철)
//				EtaxUtil.genFileCreate3(FILEPATH+"/"+targetMonth+now_grp_no+"/"+issueId+".xml", commonInfo, "UTF-8");

				//XML 테이블에 INSERT, STATUS 01 완료, STATUS 05 INSERT 
				//UPDATE ELECTRONIC_REPORT_YN = Y, STATUS= 05
				txs.setResultXml(con, IOcode, bizId, commonInfo,issueDay, rValue, flag, statusDesc, rowId, relSystemId, jobGubCode, manageId, issueId, registDt);
				//#########################################################################
				// 2015.7.8 수정 by 박상종 
				//일시적 DB연결 장애시 db connection을 다시 맺어 처리될수 있게 수정
				/*
				 
				while(local_i <= MAXDbConRecursiveCallCnt){
					//XML 테이블에 INSERT, STATUS 01 완료, STATUS 05 INSERT 
					//UPDATE ELECTRONIC_REPORT_YN = Y, STATUS= 05
					rslt = txs.setResultXml(con, IOcode, bizId, commonInfo,issueDay, rValue, flag, statusDesc, rowId, relSystemId, jobGubCode, manageId, issueId, registDt);
                    if(!rslt){//처리 실패
                    	CommUtil.logWriter("setResultXml( issueId= "+issueId+" bizId= "+bizId+" relSystemId= "+relSystemId+" jobGubCode= "+jobGubCode+" manageId= "+manageId+" )",4);
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
 			   */
				//#########################################################################

		} catch (Exception e) {
			CommUtil.logWriter("now_DbConRecursiveCallCnt/MAXDbConRecursiveCallCnt : "+now_DbConRecursiveCallCnt+"/"+MAXDbConRecursiveCallCnt,0);
						
			 if(now_DbConRecursiveCallCnt>=MAXDbConRecursiveCallCnt){	
				System.out.println("Exception : " + e.getMessage());
				
	            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++1111");
				e.printStackTrace();
				
				System.out.println(" 95 실패 처리함>>>1.bizId,2.IOcode,3.issueDay : 1."+bizId+" 2."+IOcode+" 3."+issueDay);
				
				flag = "95";
				statusDesc = "실패:"+e.getMessage();
				//STATUS 01 완료
				txs.endStatus(con, bizId, IOcode, issueDay);
				//STATUS 95 시작
				txs.startStatus(con, bizId, IOcode, issueDay, flag, statusDesc);
				//STATUS 95 업데이트
				txs.setStatusError(con, bizId, IOcode, issueDay);
				//이력테이블 IF_TAX_BILL_RESULT_INFO 에STATUS 95 업데이트 
				txs.setStatus(relSystemId, jobGubCode, manageId, issueId, registDt, flag);
				
				//배치이력객체 생성
				TbBatchErrHistVo batchErrHist = new TbBatchErrHistVo();
					batchErrHist.setYyyymm(batchHist.getYyyymm());
					batchErrHist.setBatch_job_code(batchHist.getBatch_job_code());
					batchErrHist.setServer_no(batchHist.getServer_no());
					batchErrHist.setStart_dt(batchHist.getStart_dt());
					batchErrHist.setErr_data_key("issue id:"+issueId);
					batchErrHist.setErr_desc("실패:"+e.getMessage());
				try {
					//배치 에러 이력 테이블에 기록
					txs.setBatchErr(con, batchErrHist);
					//배치작업 이력 테이블에 fail 기록
					txs.setStatBatchJob(con,batchHist);
				} catch (Exception e1) {
					 e1.printStackTrace();
					 return false;
				}
				
			 }			
			 return false;
	
		}finally{
			taxBillInfo = null;
			payInfo = null;
			mapper = null;
			commonInfo = null;
			issueDay = null;	
			bizId = null;	
			signData = null;
			temp = null;
			txs = null;
			
		}
		return true;
	}

}
