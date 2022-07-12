package kr.co.kepco.etax30.buying.lib;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : TaxCreateXml
 * 프로그램 아이디      : TaxCreateXml.java
 * 프로그램 개요        : XML 생성  및 테이블에 저장.                                                 
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-11-27

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-11-27 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - createXml()
 * - saveXmlData()
 * </METHOD>
******************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kr.co.kepco.etax30.selling.batchoffline.TaxXmlService;
import kr.co.kepco.etax30.selling.util.CommTaxMapper;
import kr.co.kepco.etax30.selling.util.CommTaxXmlSpec;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;
import kr.co.kepco.etax30.selling.vo.ItemListVo;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;


public class TaxCreateXml {

	/**
	 * 상품목록 템플릿
	 */

	public static StringBuffer ITEM = new StringBuffer()
	.append(" <TaxInvoiceTradeLineItem>                         							\n")
	.append(" <SequenceNumeric>@SequenceNumeric</SequenceNumeric>                         	\n")
	.append(" @DescriptionText                         		                                \n")
	.append(" @InvoiceAmount                              	                                \n")
	.append(" @ChargeableUnitQuantity    					                                \n")
	.append(" @InformationText                         		                                \n")
	.append(" @NameText                                                                     \n")
	.append(" @PurchaseExpiryDateTime    					                                \n")
	.append("   @TotalTax/CalculatedAmount           		                                \n")
	.append("   @UnitPrice/UnitAmount                                                       \n")
	.append("   </TaxInvoiceTradeLineItem>                           	\n");

	/**
	 * 결제방법 템플릿
	 */
	public static StringBuffer PAYMENT = new StringBuffer()
	.append(" @TypeCode                       												\n")
	.append(" @PaidAmount                     												\n");

	/**
	 * 설명: 세금계산서 정보와 상품목록리스트를 파라미터로 받아서 XML형식의 String 값을 리턴한다.
	 * @param taxBillInfo : 세금계산서 정보
	 * @param tbTradeItemList : 상품목록 리스트
	 * @return String
	 * @throws Exception
	 */
	public String createXml(TbTaxBillInfoVo taxBillInfo, List tbTradeItemList) throws Exception{
		
		String template = "";
		String commonInfo = "";
//		String commonInfo_temp = "";
		
		try {			
			template = CommTaxXmlSpec.getInstance().GetTaxXML();
			//상품목록
			ItemListVo tbTradeItem = null;

			CommTaxMapper mapper = new CommTaxMapper();
			
			StringBuffer payInfo  = new StringBuffer();
//			StringBuffer payInfo_temp  = new StringBuffer();
			StringBuffer itemInfo = new StringBuffer();
			//StringBuffer originIssueidInfo = new StringBuffer();

				//기본 세금계산서 정보 추가
				commonInfo = mapper.getString(template, mapper.makeCommonMapping(taxBillInfo));

				//결제방법값이 있을경우 추가	
				if(taxBillInfo.getPayment_type_code1() != null){
					String[] pay = {taxBillInfo.getPayment_type_code1(), String.valueOf(taxBillInfo.getPay_amount1())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));
					
					//System.out.println("p1 :  "+ p+"\n");
					
					//20160407추가
					//p = mapper.getString(p, mapper.makePayMapping(p.toString()));
			//		payInfo_temp.append(p); 
					
					
//					commonInfo      = mapper.getString(commonInfo     , mapper.makePayMapping(payInfo_temp.toString()));
					//commonInfo_temp = mapper.getString(commonInfo_temp, mapper.makePayMapping(p.toString()));
					
					//System.out.println("commonInfo 1번 결과 :  "+ commonInfo+"\n");
					
					//payInfo_temp.delete(0,0);
 
					
					payInfo.append(p);
				}
				if(taxBillInfo.getPayment_type_code2() != null){
					String[] pay = {taxBillInfo.getPayment_type_code2(), String.valueOf(taxBillInfo.getPay_amount2())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));

//					System.out.println("p2 :  "+ p+"\n");
					
					//20160407추가
					//payInfo_temp.append(p); 
					
					//commonInfo     = mapper.getString(commonInfo     , mapper.makePayMapping(payInfo_temp.toString()));
					//commonInfo_temp = mapper.getString(commonInfo_temp, mapper.makePayMapping(p.toString()));
					
					//System.out.println("commonInfo 2번 결과 :  "+ commonInfo+"\n");
					
					//payInfo_temp.delete(0,0);


					payInfo.append(p);
					
					
				}
				if(taxBillInfo.getPayment_type_code3() != null){
					String[] pay = {taxBillInfo.getPayment_type_code3(), String.valueOf(taxBillInfo.getPay_amount3())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));
					
					//System.out.println("p3 :  "+ p+"\n");
					//20160407추가
					//payInfo_temp.append(p); 
					//commonInfo      = mapper.getString(commonInfo     , mapper.makePayMapping(payInfo_temp.toString()));
					//commonInfo_temp = mapper.getString(commonInfo_temp, mapper.makePayMapping(p.toString()));
					
					//System.out.println("commonInfo 3번 결과 :  "+ commonInfo_temp+"\n");
					
					//payInfo_temp.delete(0,0);


					payInfo.append(p);
					
					
				}
				if(taxBillInfo.getPayment_type_code4() != null){
					String[] pay = {taxBillInfo.getPayment_type_code4(), String.valueOf(taxBillInfo.getPay_amount4())};
					String p = mapper.getString(PAYMENT.toString(), mapper.makePayMapping(pay));

					//System.out.println("p4 :  "+ p+"\n");
					
					
					//20160407추가

					//payInfo_temp.append(p); 
					//commonInfo      = mapper.getString(commonInfo     , mapper.makePayMapping(payInfo_temp.toString()));
					//commonInfo_temp = mapper.getString(commonInfo_temp, mapper.makePayMapping(p.toString()));
					
					//System.out.println("commonInfo 4번 결과 :  "+ commonInfo_temp+"\n");
					
//					payInfo_temp.delete(0,0);

					payInfo.append(p);
				}

				
				//20160407 변경전							
				//결제방법을 추가
                CommUtil.logWriter("commonInfo mapping 전 :  "+ commonInfo,0);
				commonInfo = mapper.getString(commonInfo, mapper.makePayMapping(payInfo.toString()));
                CommUtil.logWriter("commonInfo mapping 후 :  "+ commonInfo,0);
				
				//20160407 변경후						
				//결제방법을 추가
				//commonInfo = payInfo.toString();
				
				
//				System.out.println("test  test : rowId :  : "+CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS")+"\n");
				
				//해당 세금계산서의 상품 목록을 가져옴
				Iterator i = tbTradeItemList.iterator();
				while(i.hasNext()){
					tbTradeItem = (ItemListVo)i.next();
					String temp = mapper.getString(ITEM.toString(), mapper.makeItemMapping(tbTradeItem));
					itemInfo.append(temp);	
				}
				//상품목록을 추가
				commonInfo = mapper.getString(commonInfo, mapper.makeItemMapping(itemInfo.toString()));
				
		} catch (Exception e) {
			System.out.println("Exception : " + e.toString());
			e.printStackTrace();
			throw e;
		}
 //System.out.println("########################################################");
 //System.out.println(commonInfo);
 //System.out.println("########################################################");
		return commonInfo;
	}

	/**
	 * 설명: 생성된 XML값에 대한 정보를 TB_TAX_BILL_INFO, TB_TRADE_ITEM_LIST,
	 *  TB_XML_INFO, TB_STATUS_HIST에 저장한다.
	 * @param taxBillInfo : 세금계산서 정보
	 * @param tbTradeItemList : 상품목록 정보
	 * @param xml : 서명된 XML 
	 * @param rValue : R VALUE
	 * @throws Exception
	 */
	public void saveXmlData(TbTaxBillInfoVo taxBillInfo, List tbTradeItemList, String xml, String rValue) throws Exception {
		
		TaxXmlService txs = new TaxXmlService();
		String bizManageId =  taxBillInfo.getBiz_manage_id();
		String issueDay = taxBillInfo.getIssue_day();
		Connection conn = null;
		try {
			conn = new Dbcon().getConnection();
			conn.setAutoCommit(false);
			txs.setTbTaxBillInfo(conn, taxBillInfo);//TB_TAX_BILL_INFO에 세금계산서 정보 저장
			System.out.println("TB_TAX_BILL_INFO에 세금계산서 정보 저장 완료");
			
			txs.setTbTradeItem(conn, tbTradeItemList);//TB_TRADE_ITEM_LIST에 상품목록을 저장
			System.out.println("TB_TRADE_ITEM_LIST에 상품목록을 저장 완료");
			
			txs.setXml2(conn, bizManageId, xml, issueDay, rValue);//TB_XML_INFO에 서명완료된 XML 및 RVALUE를 저장
			System.out.println("TB_XML_INFO에 서명완료된 XML 및 RVALUE를 저장 완료");
			
			txs.insertStatus(conn, bizManageId, "2", issueDay, "01", "매입세금계산서등록");//이력을 저장
			System.out.println("이력을 저장 완료");
			
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			//System.out.println(e.getMessage());
			throw e;
		} finally{
			if ( conn != null) try{ conn.setAutoCommit(true);}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
			if (conn != null) try{conn.close(); }catch(Exception e){System.out.println(e);}
		}
	}
	/**
	 * 매입쪽 BIZ_MANAGE_ID 생성 METHOD
	 * @param realSystemId
	 * @param jobGubCode
	 * @return
	 */
	public String makeBizManageId(String realSystemId, String jobGubCode) throws Exception{
		
		StringBuffer query = new StringBuffer()     
		
		.append(" SELECT 				                                                                               				\n") 
		.append(" TO_CHAR(SYSDATE, 'YYYYMM')||'2'|| NVL(SUBSTR(?, 1, 3), 'EDI')||?||LPAD(SQ_MNG_ID.NEXTVAL, 8, '0') BIZ_MANAGE_ID 	\n") 
		.append(" FROM DUAL										 			                        								\n");								            	          
		                                                                                                                 
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String bizManageId = "";
		
		try {
			conn = new Dbcon().getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, realSystemId);
			pstmt.setString(2, jobGubCode);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bizManageId = rs.getString("BIZ_MANAGE_ID");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.getMessage());}
			if (conn != null) try{conn.close(); }catch(Exception e){System.out.println(e);}
		}
		return bizManageId;
		
	}
	
	/**
	 * 
	 */
	public TaxCreateXml(){	
	}
	
	private List item_newList;
	
	public List getItem_newList() {
		return item_newList;
	}

	public void setItem_newList(List itemNewList) {
		item_newList = itemNewList;
	}

	public static void main(String[] args) {
		
		TbTaxBillInfoVo billInfo = new TbTaxBillInfoVo();
		billInfo.setIo_code("2");
		billInfo.setIssue_day("20091201");
		billInfo.setIssue_id("1111111111111111111");
		billInfo.setBiz_manage_id("aaaaaaaaaa20091201");
		billInfo.setBill_type_code("2222");
		billInfo.setInvoicer_party_id("01");
		billInfo.setInvoicee_party_id("01");
		billInfo.setInvoicer_party_name("박봄");
		billInfo.setInvoicee_party_name("박봄");
		billInfo.setInvoicer_ceo_name("박봄");
		billInfo.setInvoicee_ceo_name("박봄");
		billInfo.setInvoicee_business_type_code("1");
		billInfo.setElectronic_report_yn("N");
		billInfo.setAdd_tax_yn("N");
		
		
		
		List itemList = new ArrayList();		
		for(int ii=0;ii < 2;ii++){
			ItemListVo item = new ItemListVo();
			item.setIo_code("2");
			item.setIssue_day("20091230");
			item.setBiz_manage_id("1234567890");
			item.setSeq_no(ii);
			item.setPurchase_day("20091130");
			item.setItem_name("아이템");
			item.setItem_info("아이템");
			item.setItem_desc("아이템");
			itemList.add(item);
		}	
		System.out.println(itemList.size());
		System.out.println(((ItemListVo) itemList.get(0)).getSeq_no());
		System.out.println(((ItemListVo) itemList.get(1)).getSeq_no());
				
		/*ItemListVo item2 = new ItemListVo();
		item2.setIo_code("2");
		item2.setIssue_day("20091230");
		item2.setBiz_manage_id("1234567890");
		item2.setSeq_no(2);
		item2.setPurchase_day("20091130");
		item2.setItem_name("아이템");
		item2.setItem_info("아이템");
		item2.setItem_desc("아이템");
		
		itemList.add(item2);
		*/
		/*TaxXmlService txs = new TaxXmlService();
		try {
			txs.setTbTaxBillInfo(billInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			new TaxXmlService().setTbTradeItem(itemList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			String rValue = "r";
			String xml =new TaxCreateXml().createXml(billInfo, itemList);
			new TaxCreateXml().saveXmlData(billInfo, itemList, xml, rValue);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
