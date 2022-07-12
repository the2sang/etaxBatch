
package kr.co.kepco.etax30.selling.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kr.co.kepco.etax30.selling.vo.ItemListVo;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : CommTaxMapper
 * 프로그램 아이디      : CommTaxMapper.java
 * 프로그램 개요        : XML 템플릿에 해당 값을 치환
 * 관련 테이블          :
 * 관련 모듈            :
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - getString()
 * - makeCommonMapping()
 * - ...
 * </METHOD>
******************************************************************************/
public class CommTaxMapper {

	 final static int ORIGIN_ISSUEID_APPLY_DAY = Integer.parseInt(CommProperties.getString("ORIGIN_ISSUEID_APPLY_DAY"));

	 public CommTaxMapper(){
	 }
	 /**
	  *
	  * @param template 템플릿 문자열
	  * @param params 치환할 값들
	  * @return 치환된 문자열
	  */
	 public String getString(String template, Map params){
	  String body = template;

	  String key = "";
	  String rValue = "";

	  for(Iterator it = params.keySet().iterator(); it.hasNext(); ) {

	    key = String.valueOf(it.next());
		//targetStr = targetStr.replaceAll("\\$","\\\\\\$");
        //rValue = body.replaceAll(key, this.nvl(String.valueOf(params.get(key))));
        
        try{
        	rValue = body.replaceAll(key, this.nvl(String.valueOf(params.get(key))));
        }catch(Exception e1){
        	CommUtil.logWriter("Exception : " + e1.toString(),4);
        	CommUtil.logWriter("==================================================================",4);
        	CommUtil.logWriter("==================================================================",4);
        	CommUtil.logWriter("문자열내에 특수문자가 포함되어 replaceAll METHOD 처리 오류 발생",4);
        	CommUtil.logWriter("==================================================================",4);
        	CommUtil.logWriter("==================================================================",4);
        	CommUtil.logWriter("BEFORE body["+body+"]",4);
        	CommUtil.logWriter("key["+key+"]",4);
            CommUtil.logWriter("this.nvl(String.valueOf(params.get(key)))["+this.nvl(String.valueOf(params.get(key)))+"]",4);
            CommUtil.logWriter("this.nvl(String.valueOf(params.get(key)).replaceAll with $)["+this.nvl(String.valueOf(params.get(key)).replaceAll("\\$","\\\\\\$"))+"]",4);
            rValue = body.replaceAll(key, this.nvl(String.valueOf(params.get(key)).replaceAll("\\$","\\\\\\$")));
            CommUtil.logWriter("rValue["+rValue+"]",4);
        }
	    body = rValue;
	  }
	  return body;
	 }

	 public String nvl(Object obj){
	  if(obj != null){
	   return obj.toString();
	  }
	  return "";
	 }

	 public Map makeCommonMapping(TbTaxBillInfoVo value){

		 Map params = new HashMap();
		 boolean check = false;
		 boolean check2 = false;
		 
		 int curDay = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));

		 //************************관리정보*******************************************************
		 //서비스사업자관리번호
		 if( nvl(value.getSvc_manage_id())!=null && !nvl(value.getSvc_manage_id()).equals("")){
			 params.put("@ExchangedDocument/ID", "<ID>"+nvl(value.getSvc_manage_id())+"</ID>");
		 }else{params.put("@ExchangedDocument/ID", "");}

		//전자세금계산서 발행일시
		 params.put("@ExchangedDocument/IssueDateTime", nvl(value.getIssue_dt()));

		//사업자 관리번호
		 if( nvl(value.getBiz_manage_id())!=null && !nvl(value.getBiz_manage_id()).equals("")){
			 params.put("@ExchangedDocument/ReferencedDocument/ID", "<ReferencedDocument><ID>"+nvl(value.getBiz_manage_id())+"</ID></ReferencedDocument>");
		 }else{params.put("@ExchangedDocument/ReferencedDocument/ID", "");}

		//************************관리정보*******************************************************
		 //************************기본정보*******************************************************
		//세금계산서 승인번호
		 params.put("@TaxInvoiceDocument/IssueID", nvl(value.getIssue_id()));

		//전자세금계산서 종류 구분자
		 params.put("@TaxInvoiceDocument/TypeCode", nvl(value.getBill_type_code()));

		//비고
		 if( nvl(value.getDescription())!=null && !nvl(value.getDescription()).equals("")){
			 params.put("@TaxInvoiceDocument/DescriptionText", "<DescriptionText>"+"<![CDATA["+nvl(value.getDescription())+"]]>"+"</DescriptionText>");
		 }else{params.put("@TaxInvoiceDocument/DescriptionText", "");}

		//전자세금계산서  작성일자
		 params.put("@TaxInvoiceDocument/IssueDateTime", nvl(value.getIssue_day()));

		 //전자세금계산서 수정 사유코드
		 if( nvl(value.getAmendment_code())!=null && !nvl(value.getAmendment_code()).equals("")){
			 params.put("@TaxInvoiceDocument/AmendmentStatusCode", "<AmendmentStatusCode>"+nvl(value.getAmendment_code())+"</AmendmentStatusCode>");
		 }else{params.put("@TaxInvoiceDocument/AmendmentStatusCode", "");}

		 //세금계산서의 영수/청구 구분 지시자  01:영수 02:청구
		 if( nvl(value.getPurpose_code())!=null && !nvl(value.getPurpose_code()).equals("")){
			 params.put("@TaxInvoiceDocument/PurposeCode", "<PurposeCode>"+nvl(value.getPurpose_code())+"</PurposeCode>");
		 }else{params.put("@TaxInvoiceDocument/PurposeCode", "");}

		 //제목 : 수정전자세금계산서용 당총승인번호 추가 
		 //일자 : 2013.11.20
		 //사유 : 13년 10월 표준전자세금계산서(v3.0) 개발지침 v1.0 변경 적용일 2014.1.1
		 
  	     //System.out.println("# curDay:" + curDay+"# ORIGIN_ISSUEID_APPLY_DAY:" + ORIGIN_ISSUEID_APPLY_DAY);
  	     //System.out.println("value.getUpper_manage_id():" + value.getUpper_manage_id());
		 
		 if( curDay >= ORIGIN_ISSUEID_APPLY_DAY && nvl(value.getUpper_manage_id())!=null && !nvl(value.getUpper_manage_id()).equals("")){
			 params.put("@TaxInvoiceDocument/OriginalIssueID", "<OriginalIssueID>"+nvl(value.getUpper_manage_id())+"</OriginalIssueID>");
		 }else{params.put("@TaxInvoiceDocument/OriginalIssueID", "");}
		 
		//************************수입세금계산서관련정보*******************************************************
		 //수입신고서번호
		 if(nvl(value.getImport_doc_id())!=null && !nvl(value.getImport_doc_id()).equals("")){
			 check = true;
			 params.put("@TaxInvoiceDocument/ReferencedImportDocument/ID", "<ReferencedImportDocument><ID>"+nvl(value.getImport_doc_id())+"</ID>");
		 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/ID", "");}

		//일괄발급 수입총건
		 if(value.getImport_item_quantity()>0){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceDocument/ReferencedImportDocument/ItemQuantity", "<ReferencedImportDocument><ItemQuantity>"+nvl(String.valueOf(value.getImport_item_quantity()))+"</ItemQuantity>");
			 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/ItemQuantity", "<ItemQuantity>"+nvl(String.valueOf(value.getImport_item_quantity()))+"</ItemQuantity>");}
		 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/ItemQuantity", "");}

		//일괄발급 시작일자
		 if(nvl(value.getImport_period_start_day())!=null && !nvl(value.getImport_period_start_day()).equals("")){
			 if(!check){
				 check = true;
				 check2 = true;
				 params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/StartDateTime", "<ReferencedImportDocument><AcceptablePeriod><StartDateTime>"+nvl(value.getImport_period_start_day())+"</StartDateTime>");
			 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/StartDateTime", "<AcceptablePeriod><StartDateTime>"+nvl(value.getImport_period_start_day())+"</StartDateTime>");}
		 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/StartDateTime", "");}

		//일괄발급 종료일자
		 if(nvl(value.getImport_period_end_day())!=null && !nvl(value.getImport_period_end_day()).equals("")){
			 params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/EndDateTime", "<EndDateTime>"+nvl(value.getImport_period_end_day())+"</EndDateTime>\n</AcceptablePeriod>\n</ReferencedImportDocument>");
		 }else{
			 if(!check){
				 params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/EndDateTime", "");
			 }else{
				 if(check2){
					 params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/EndDateTime", "</AcceptablePeriod>\n</ReferencedImportDocument>");
				 }else{params.put("@TaxInvoiceDocument/ReferencedImportDocument/AcceptablePeriod/EndDateTime", "</ReferencedImportDocument>");
				 }
			}
		 }
		//************************수입세금계산서관련정보*******************************************************
		//************************공급자정보*******************************************************
		 //공급자 사업자등록번호
		 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/ID", nvl(value.getInvoicer_party_id()));
		 
		 //###########################   20171120 유종일  - 공급업체의 업테 CDATA처리 안되있어 특수문자 입력시 전자서명시 오류    
		 //공급업체의 업태
		 if(nvl(value.getInvoicer_type())!=null && !nvl(value.getInvoicer_type()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/TypeCode", "<TypeCode>"+"<![CDATA["+nvl(value.getInvoicer_type())+"]]>"+"</TypeCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/TypeCode", "");}

		 //공급업체 사업체명
		 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/NameText", "<![CDATA["+nvl(value.getInvoicer_party_name())+"]]>");

		 //공급업체의 업종
		 if(nvl(value.getInvoicer_class())!=null && !nvl(value.getInvoicer_class()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/ClassificationCode", "<ClassificationCode>"+"<![CDATA["+nvl(value.getInvoicer_class())+"]]>"+"</ClassificationCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/ClassificationCode", "");}

		 //종사업장 식별코드
		 if(nvl(value.getInvoicer_tax_regist_id())!=null && !nvl(value.getInvoicer_tax_regist_id()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/SpecifiedOrganization/TaxRegistrationID", "<SpecifiedOrganization>\n<TaxRegistrationID>"+nvl(value.getInvoicer_tax_regist_id())+"</TaxRegistrationID>\n</SpecifiedOrganization>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/SpecifiedOrganization/TaxRegistrationID", "");}

		 //공급업체 대표자명
		 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/SpecifiedPerson/NameText", "<![CDATA["+nvl(value.getInvoicer_ceo_name())+"]]>");


		 //공급업체담당부서
		 check = false;
		 if(nvl(value.getInvoicer_contact_depart())!=null && !nvl(value.getInvoicer_contact_depart()).equals("")){
			 check = true;
			 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/DepartmentNameText", "<DefinedContact>\n<DepartmentNameText>"+"<![CDATA["+nvl(value.getInvoicer_contact_depart())+"]]>"+"</DepartmentNameText>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/DepartmentNameText", "");}

		 //공급업체담당자명
		 if(nvl(value.getInvoicer_contact_name())!=null && !nvl(value.getInvoicer_contact_name()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/PersonNameText", "<DefinedContact>\n<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicer_contact_name())+"]]>"+"</PersonNameText>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/PersonNameText", "<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicer_contact_name())+"]]>"+"</PersonNameText>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/PersonNameText", "");}

		 //공급업체담당자 전화번호
		 if(nvl(value.getInvoicer_contact_phone())!=null && !nvl(value.getInvoicer_contact_phone()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/TelephoneCommunication", "<DefinedContact>\n<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicer_contact_phone())+"]]>"+"</TelephoneCommunication>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/TelephoneCommunication", "<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicer_contact_phone())+"]]>"+"</TelephoneCommunication>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/TelephoneCommunication", "");}

		 //공급업체담당자 이메일
		 if(nvl(value.getInvoicer_contact_email())!=null && !nvl(value.getInvoicer_contact_email()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/URICommunication", "<DefinedContact>\n<URICommunication>"+"<![CDATA["+nvl(value.getInvoicer_contact_email())+"]]>"+"</URICommunication>\n</DefinedContact>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/URICommunication", "<URICommunication>"+"<![CDATA["+nvl(value.getInvoicer_contact_email())+"]]>"+"</URICommunication>\n</DefinedContact>");}
		 }else{
			 if(!check){
				 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/URICommunication", "");
			 }else{
				 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/DefinedContact/URICommunication", "</DefinedContact>");
			 }
		 }
		 //공급업체의 주소
		 if(nvl(value.getInvoicer_addr())!=null && !nvl(value.getInvoicer_addr()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoicerParty/SpecifiedAddress/LineOneText", "<SpecifiedAddress>\n<LineOneText>"+"<![CDATA["+nvl(value.getInvoicer_addr())+"]]>"+"</LineOneText>\n</SpecifiedAddress>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoicerParty/SpecifiedAddress/LineOneText", "");}
		//************************공급자정보*******************************************************
		//************************공급받는자정보*******************************************************
		 
		 
		 
		//###########################   20171120 유종일  - 공급업체의 업테 CDATA처리 안되있어 특수문자 입력시 전자서명시 오류   
		 //공급받는자 사업자등록번호
		 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/ID", nvl(value.getInvoicee_party_id()));
		 if(nvl(value.getInvoicee_type())!=null && !nvl(value.getInvoicee_type()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/TypeCode", "<TypeCode>"+"<![CDATA["+nvl(value.getInvoicee_type())+"]]>"+"</TypeCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/TypeCode", "");}

		 //공급받는자 사업체명
		 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/NameText", "<![CDATA["+nvl(value.getInvoicee_party_name())+"]]>");

		 //공급받는바 업종
		 if(nvl(value.getInvoicee_class())!=null && !nvl(value.getInvoicee_class()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/ClassificationCode", "<ClassificationCode>"+"<![CDATA["+nvl(value.getInvoicee_class())+"]]>"+"</ClassificationCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/ClassificationCode", "");}

		 //공급받는자 종사업장 식별코드
		 if(nvl(value.getInvoicee_tax_regist_id())!=null && !nvl(value.getInvoicee_tax_regist_id()).equals("")){
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedOrganization/TaxRegistrationID", "<TaxRegistrationID>"+nvl(value.getInvoicee_tax_regist_id())+"</TaxRegistrationID>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedOrganization/TaxRegistrationID", "");}

		 //공급받는자 사업자등록번호 구분코드
		 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedOrganization/BusinessTypeCode", nvl(value.getInvoicee_business_type_code()));

		 //공급받는자 대표자명
		 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedPerson/NameText", "<![CDATA["+nvl(value.getInvoicee_ceo_name())+"]]>");

		 //공급받는자 담당부서1
		 check = false;
		 if(nvl(value.getInvoicee_contact_depart1())!=null && !nvl(value.getInvoicee_contact_depart1()).equals("")){
			 check = true;
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/DepartmentNameText", "<PrimaryDefinedContact>\n<DepartmentNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_depart1())+"]]>"+"</DepartmentNameText>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/DepartmentNameText", "");}

		 //공급받는자 담당자명1
		 if(nvl(value.getInvoicee_contact_name1())!=null && !nvl(value.getInvoicee_contact_name1()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/PersonNameText", "<PrimaryDefinedContact>\n<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_name1())+"]]>"+"</PersonNameText>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/PersonNameText", "<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_name1())+"]]>"+"</PersonNameText>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/PersonNameText", "");}

		 //공급받는자 전화번호1
		 if(nvl(value.getInvoicee_contact_phone1())!=null && !nvl(value.getInvoicee_contact_phone1()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/TelephoneCommunication", "<PrimaryDefinedContact>\n<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone1())+"]]>"+"</TelephoneCommunication>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/TelephoneCommunication", "<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone1())+"]]>"+"</TelephoneCommunication>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/TelephoneCommunication", "");}

		//공급받는자 이메일1
		 if(nvl(value.getInvoicee_contact_email1())!=null && !nvl(value.getInvoicee_contact_email1()).equals("")){
			 if(!check){
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/URICommunication", "<PrimaryDefinedContact>\n<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email1())+"]]>"+"</URICommunication>\n</PrimaryDefinedContact>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/URICommunication", "<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email1())+"]]>"+"</URICommunication>\n</PrimaryDefinedContact>");}
		 }else{
			 if(!check){
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/URICommunication", "");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/PrimaryDefinedContact/URICommunication", "</PrimaryDefinedContact>");}
		 }

		 //공급받는자 담당부서2
		 check = false;
		 if(nvl(value.getInvoicee_contact_depart2())!=null && !nvl(value.getInvoicee_contact_depart2()).equals("")){
			 check = true;
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/DepartmentNameText", "<SecondaryDefinedContact>\n<DepartmentNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_depart2())+"]]>"+"</DepartmentNameText>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/DepartmentNameText", "");}

		 //공급받는자 담당자명2
		 if(nvl(value.getInvoicee_contact_name2())!=null && !nvl(value.getInvoicee_contact_name2()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/PersonNameText", "<SecondaryDefinedContact>\n<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_name2())+"]]>"+"</PersonNameText>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/PersonNameText", "<PersonNameText>"+"<![CDATA["+nvl(value.getInvoicee_contact_name2())+"]]>"+"</PersonNameText>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/PersonNameText", "");}

		 //공급받는자 전화번호2
		 if(nvl(value.getInvoicee_contact_phone2())!=null && !nvl(value.getInvoicee_contact_phone2()).equals("")){
			 if(!check){
				 check = true;
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/TelephoneCommunication", "<SecondaryDefinedContact>\n<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone2())+"]]>"+"</TelephoneCommunication>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/TelephoneCommunication", "<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone2())+"]]>"+"</TelephoneCommunication>");}
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/TelephoneCommunication", "");}

		 //공급받는자 이메일2
		 if(nvl(value.getInvoicee_contact_email2())!=null && !nvl(value.getInvoicee_contact_email2()).equals("")){
			 if(!check){
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/URICommunication", "<SecondaryDefinedContact>\n<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email2())+"]]>"+"</URICommunication>\n</SecondaryDefinedContact>");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/URICommunication", "<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email2())+"]]>"+"</URICommunication>\n</SecondaryDefinedContact>");}
		 }else{
			 if(!check){
			 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/URICommunication", "");
			 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SecondaryDefinedContact/URICommunication", "</SecondaryDefinedContact>");}
		 }

		 //공급받는자 주소
		 if(nvl(value.getInvoicee_addr())!=null && !nvl(value.getInvoicee_addr()).equals("")){
				 params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedAddress/LineOneText", "<SpecifiedAddress>\n<LineOneText>"+"<![CDATA["+nvl(value.getInvoicee_addr())+"]]>"+"</LineOneText>\n</SpecifiedAddress>");
		 }else{params.put("@TaxInvoiceTradeSettlement/InvoiceeParty/SpecifiedAddress/LineOneText", "");}
		//************************공급받는자정보*******************************************************
		 //************************수탁사업자정보*******************************************************
		 //수탁사업자 사업자등록번호
		 check = false;
		 if(nvl(value.getBroker_party_id())!=null && !nvl(value.getBroker_party_id()).equals("")){
			 check =  true;
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/ID", "<BrokerParty>\n<ID>"+nvl(value.getBroker_party_id())+"</ID>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/ID", "");}

		 //수탁사업자 업태
		 if(check){
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/TypeCode", "<TypeCode>"+"<![CDATA["+nvl(value.getBroker_type())+"]]>"+"</TypeCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/TypeCode", "");}

		 //수탁사업자 사업체명
		 if(check){
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/NameText", "<NameText>"+"<![CDATA["+nvl(value.getBroker_party_name())+"]]>"+"</NameText>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/NameText", "");}

		 //수탁사업자 업종
		 if(check){
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/ClassificationCode", "<ClassificationCode>"+nvl(value.getBroker_class())+"</ClassificationCode>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/ClassificationCode", "");}

		 //수탁사업자 종사업장 식별코드
		 if(check){
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedOrganization/TaxRegistrationID", "<SpecifiedOrganization>\n<TaxRegistrationID>"+nvl(value.getBroker_tax_regist_id())+"</SpecifiedOrganization>\n</TaxRegistrationID>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedOrganization/TaxRegistrationID", "");}

		 //수탁사업자 대표자명
		 if(check){
			 params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedPerson/NameText", "<SpecifiedPerson>\n<NameText>"+"<![CDATA["+nvl(value.getBroker_ceo_name())+"]]>"+"</NameText>\n</SpecifiedPerson>");
		 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedPerson/NameText", "");}


		 if(check){
			 check2 = false;

			 //수탁사업자 담당부서
			 if(nvl(value.getBroker_contact_depart())!=null && !nvl(value.getBroker_contact_depart()).equals("")){
				 check2 = true;
				 params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/DepartmentNameText", "<DefinedContact>\n<DepartmentNameText>"+"<![CDATA["+nvl(value.getBroker_contact_depart())+"]]>"+"</DepartmentNameText>");
			 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/DepartmentNameText", "");}

			//수탁사업자 담당자명
			 if(nvl(value.getBroker_contact_name())!=null && !nvl(value.getBroker_contact_name()).equals("")){
				 if(!check2){
					 check2 = true;
					 params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/PersonNameText", "<DefinedContact>\n<PersonNameText>"+"<![CDATA["+nvl(value.getBroker_contact_name())+"]]>"+"</PersonNameText>");
				 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/PersonNameText", "<PersonNameText>"+"<![CDATA["+nvl(value.getBroker_contact_name())+"</PersonNameText>");}
			 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/PersonNameText", "");}

			//수탁사업자 전화번호
			 if(nvl(value.getInvoicee_contact_phone2())!=null && !nvl(value.getInvoicee_contact_phone2()).equals("")){
				 if(!check2){
					 check2 = true;
					 params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/TelephoneCommunication", "<DefinedContact>\n<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone2())+"]]>"+"</TelephoneCommunication>");
				 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/TelephoneCommunication", "<TelephoneCommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_phone2())+"]]>"+"</TelephoneCommunication>");}
			 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/TelephoneCommunication", "");}

			//수탁사업자 이메일
			 if(nvl(value.getInvoicee_contact_email2())!=null && !nvl(value.getInvoicee_contact_email2()).equals("")){
				 if(!check2){
					 params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/URICommunication", "<DefinedContact>\n<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email2())+"]]>"+"</URICommunication>\n</DefinedContact>");
				 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/URICommunication", "<URICommunication>"+"<![CDATA["+nvl(value.getInvoicee_contact_email2())+"]]>"+"</URICommunication>\n</DefinedContact>");}
			 }else{
				 if(!check2){
					 params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/URICommunication", "");
				 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/URICommunication", "</DefinedContact>");}
		 	 }

			//수탁사업자 주소
			 if(nvl(value.getInvoicee_addr())!=null && !nvl(value.getInvoicee_addr()).equals("")){
				 params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedAddress/LineOneText", "<SpecifiedAddress>\n<LineOneText>"+"<![CDATA["+nvl(value.getInvoicee_addr())+"]]>"+"</LineOneText>\n</SpecifiedAddress>");
			 }else{params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedAddress/LineOneText", "</BrokerParty>");}
		 	}else{
		 		params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/DepartmentNameText", "");
		 		params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/PersonNameText", "");
		 		params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/TelephoneCommunication", "");
		 		params.put("@TaxInvoiceTradeSettlement/BrokerParty/DefinedContact/URICommunication", "");
		 		params.put("@TaxInvoiceTradeSettlement/BrokerParty/SpecifiedAddress/LineOneText", "");
		 }
		//************************수탁사업자정보*******************************************************

		//************************합계*****************************************************************
		 //총 공급가액 합계
		 params.put("@TaxInvoiceTradeSettlement/SpecifiedMonetarySummation/ChargeTotalAmount", nvl(String.valueOf(value.getCharge_total_amount())));

		 //총 세액 합계
		 //if(value.getTax_total_amount()>0){ //NOT NULL 컬럼이며, 또한 (-) 금액이 입력될 수도 있음.
			 params.put("@TaxInvoiceTradeSettlement/SpecifiedMonetarySummation/TaxTotalAmount", "<TaxTotalAmount>"+nvl(String.valueOf(value.getTax_total_amount()))+"</TaxTotalAmount>");
		 //}else{params.put("@TaxInvoiceTradeSettlement/SpecifiedMonetarySummation/TaxTotalAmount", "");}

		 //총액(공급가액+세액)
		 params.put("@TaxInvoiceTradeSettlement/SpecifiedMonetarySummation/GrandTotalAmount", nvl(String.valueOf(value.getGrand_total_amount())));
		//************************합계*****************************************************************

		 return params;

	 }
	 public Map makePayMapping(String[] value ){

		 Map params = new HashMap();

		 if(!value[0].equals("")){
		 params.put("@TypeCode", "<TypeCode>"+value[0]+"</TypeCode>");
		 }else{params.put("@TypeCode", "");}
		 if(!value[0].equals("")){
		 params.put("@PaidAmount", "<PaidAmount>"+value[1]+"</PaidAmount>");
		 }else{params.put("@PaidAmount", "");}

		return params;
	 }
	 public Map makePayMapping(String value ){

		 Map params = new HashMap();
		 if(!value.equals("")){
		 params.put("@SpecifiedPaymentMeans", "<SpecifiedPaymentMeans>"+value+"</SpecifiedPaymentMeans>");
		 }else{params.put("@SpecifiedPaymentMeans", "");}

		 return params;
	 }
	 public Map makeItemMapping(ItemListVo value ){

		 //2012.03.14 수량,단가 소수점 허용 자리수 변경에 따라 수정
		 // 소수1자리에서 소수2자리로 변경
		 //final String pattern = "#########################.0";
		 final String pattern = "#########################.##";

		 Map params = new HashMap();
		 DecimalFormat twoPlaces = new DecimalFormat(pattern);

		 params.put("@SequenceNumeric", nvl(String.valueOf(value.getSeq_no())));

		 //비고
		 if(nvl(value.getItem_desc())!=null && !nvl(value.getItem_desc()).equals("")){
			 params.put("@DescriptionText", "<DescriptionText>"+"<![CDATA["+nvl(value.getItem_desc())+"]]>"+"</DescriptionText>");
		 }else{params.put("@DescriptionText", "");}

		 //물품 공급가액
		 if(value.getInvoice_amount()!=0){
			 params.put("@InvoiceAmount", "<InvoiceAmount>"+nvl(String.valueOf(value.getInvoice_amount()))+"</InvoiceAmount>");
		 }else{params.put("@InvoiceAmount", "");}

		 //물품 수량
		 if(value.getUnit_quantity()!=0){
			 params.put("@ChargeableUnitQuantity", "<ChargeableUnitQuantity>"+nvl(twoPlaces.format(new BigDecimal(value.getUnit_quantity())))+"</ChargeableUnitQuantity>");
		 }else{params.put("@ChargeableUnitQuantity", "");}

		 //물품에 대한 규격
		 if(nvl(value.getItem_info())!=null && !nvl(value.getItem_info()).equals("")){
			 params.put("@InformationText", "<InformationText>"+"<![CDATA["+nvl(value.getItem_info())+"]]>"+"</InformationText>");
		 }else{params.put("@InformationText", "");}

		 //물품명
		 if(nvl(value.getItem_name())!=null && !nvl(value.getItem_name()).equals("")){
			 params.put("@NameText", "<NameText>"+"<![CDATA["+nvl(value.getItem_name())+"]]>"+"</NameText>");
		 }else{params.put("@NameText", "");}

		 //물품 공급일자
		 if(nvl(value.getPurchase_day())!=null && !nvl(value.getPurchase_day()).equals("")){
			 params.put("@PurchaseExpiryDateTime", "<PurchaseExpiryDateTime>"+"<![CDATA["+nvl(value.getPurchase_day())+"]]>"+"</PurchaseExpiryDateTime>");
		 }else{params.put("@PurchaseExpiryDateTime", "");}

		 //물품세액
		 if(value.getTax_amount()!=0){
			 params.put("@TotalTax/CalculatedAmount", "<TotalTax>\n<CalculatedAmount>"+nvl(String.valueOf(value.getTax_amount()))+"</CalculatedAmount>\n</TotalTax>");
		 }else{params.put("@TotalTax/CalculatedAmount", "");}

		 //물품 단가
		 if(value.getUnit_amount()!=0){
			 params.put("@UnitPrice/UnitAmount", "<UnitPrice>\n<UnitAmount>"+nvl(twoPlaces.format(new BigDecimal(value.getUnit_amount())))+"</UnitAmount>\n</UnitPrice>");
		 }else{params.put("@UnitPrice/UnitAmount", "");}

		 return params;
	 }
	 public Map makeItemMapping(String value ){

		 Map params = new HashMap();

		 params.put("@TaxInvoiceTradeLineItem", value);

		 return params;
	 }

	 /**
	  * @param args
	  */
/*	 public static void main(String[] args) {
	  CommTaxMapper mapper = new CommTaxMapper();
	  CommTaxXmlSpec.getInstance();
	  //String template = CommTaxXmlSpec.getInstance().GetTaxXML();
	  ItemListVo item = new ItemListVo();
	  item.setUnit_amount(123456789111111.0);

	//  String result = String.valueOf(mapper.getString(template, mapper.makePayMapping()));
	    String result = String.valueOf(mapper.getString(ITEM.toString(), mapper.makeItemMapping(item)));

	  System.out.println(result);
	 }
*/
}