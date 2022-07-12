package kr.co.kepco.etax30.selling.vo;

public class TbTaxBillInfoVo {

    private String io_code;				//매입매출코드 1:매출, 2:매입					
    private String biz_manage_id;			//사업자관리번호	
    private String svc_manage_id;			//서비스관리번호	
    private String issue_dt;				//발행일시	
    private String signature      ;			//전자서명	
    private String issue_id      ;			//승인번호	
    private String issue_id1      ;			//이메일포함	
    private String issue_id2      ;			//이메일포함	
    private String issue_day      ;			//작성일자	
    private String bill_type_code      ;		//세금계산서종류
    private String purpose_code      ;			//영수청구코드
    private String amendment_code      ;		//전자세금계산서 수정 사유코드
    private String description      ;			//비고
    private String import_doc_id      ;		//수입 신고서 번호	
    private String import_period_start_day      ;	//일괄발급 시작일자
    private String import_period_end_day      ;	//일괄발급 종료일자	
    private int import_item_quantity      ;		//일괄발급 수입 총건	
    private String invoicer_party_id      ;		//공급자 사업자등록번호	
    private String invoicer_tax_regist_id      ;	//종사업장 식별코드
    private String invoicer_party_name      ;		//공급업체 사업체명
    private String invoicer_ceo_name      ;		//공급업체 대표자명	
    private String invoicer_addr      ;		//공급업체의 주소		
    private String invoicer_type      ;		//공급업체의 업태		
    private String invoicer_class      ;		//공급업체의 업종	
    private String invoicer_contact_depart      ;	//공급업체 담당부서
    private String invoicer_contact_name      ;	//공급업체 담당자명	
    private String invoicer_contact_phone      ;	//공급업체 담당자 전화번호
    private String invoicer_contact_email      ;	//공급업체 담당자 이메일	
    private String invoicee_business_type_code      ;	//공급받는자 사업자등록번호 구분코드
    private String invoicee_party_id      ;		//공급받는자 사업자등록번호	
    private String invoicee_tax_regist_id      ;	//공급받는자 종사업장 식별코드
    private String invoicee_party_name      ;		//공급받는자 사업체명
    private String invoicee_ceo_name      ;		//공급받는자 대표자명	
    private String invoicee_addr      ;		//공급받는자 주소		
    private String invoicee_type      ;		//공급받는자 업태		
    private String invoicee_class      ;		//공급받는자 업종	
    private String invoicee_contact_depart1      ;	//공급받는자 담당부서1
    private String invoicee_contact_name1      ;	//공급받는자 담당자명1
    private String invoicee_contact_phone1      ;	//공급받는자 담당자 전화번호1
    private String invoicee_contact_email1      ;	//공급받는자 담당자 이메일1
    private String invoicee_contact_depart2      ;	//공급받는자 담당부서2
    private String invoicee_contact_name2      ;	//공급받는자 담당자명2
    private String invoicee_contact_phone2      ;	//공급받는자 담당자 전화번호2
    private String invoicee_contact_email2      ;	//공급받는자 담당자 이메일2
    private String broker_party_id      ;		//수탁사업자 사업자등록번호	
    private String broker_tax_regist_id      ;		//수탁사업자 종사업장 식별코드
    private String broker_party_name      ;		//수탁사업자 사업체명	
    private String broker_ceo_name      ;		//수탁사업자 대표자명	
    private String broker_addr      ;			//수탁사업자 주소	
    private String broker_type      ;			//수탁사업자 업태	
    private String broker_class      ;			//수탁사업자 업종	
    private String broker_contact_depart      ;	//수탁사업자 담당부서	
    private String broker_contact_name      ;		//수탁사업자 담당자명
    private String broker_contact_phone      ;		//수탁사업자 담당자 전화번호
    private String broker_contact_email      ;		//수탁사업자 담당자 이메일
    private String payment_type_code1      ;		//결제방법코드1
    private long pay_amount1      ;			//결제방법별금액1	
    private String payment_type_code2      ;		//결제방법코드2
    private long pay_amount2      ;			//결제방법별금액2	
    private String payment_type_code3      ;		//결제방법코드3
    private long pay_amount3      ;			//결제방법별금액3	
    private String payment_type_code4      ;		//결제방법코드4
    private long pay_amount4      ;			//결제방법별금액4	
    private long tax_total_amount       ;		//총 공급가액 합계
    private long grand_total_amount      ;		//총 세액 합계
    private long charge_total_amount      ;		//총액(공급가액+세액)
    private String status_code      ;			//진행상태
    private String job_gub_code      ;			//업무구분코드(전력매출,공사매출 등)
    private String electronic_report_yn      ;		//전자신고완료여부
    private String regist_dt      ;			//등록일시	
    private String modify_dt      ;			//수정일시	
    private String regist_id      ;			//등록자ID	
    private String modify_id      ;			//수정자ID	
    private String rel_system_id      ;		//연계시스템ID	
    private String manage_id ; 			//사업자관리번호		
    private String upper_manage_id;			//부모관리번호 : 수정세금계산서 발행시 사용	
    private String cancel_dt;				//취소일시	
    private String add_tax_yn;				//가산세 여부
    private String online_gub_code;			//온,오프라인 구분코드
    private String invoicee_gub_code;			//공급받는자 구분코드(매입만 사용. 00:한전, 기타:발전사)
    // 2012.4.25 추가
    private String esero_issue_id;			//국세청 등록 승인번호 - 국세청 ESERO에 등록된 실제 승인번호
    // 2012.6.15 추가
    private String tax_2nd_sign;			//승인세금계산서 2차전자서명 유무 NULL:한전 승인전, N:한전 승인후, Y:업체 2차서명완료
    private String issue_dt2;			//세금계산서 2차전자서명 생성일자
    // 2015.9.17 추가
    private String edi_returned_date;	//한전EDI용 지급예정일 날짜로 SMS 세금계산서발행요청시 사용 
    //2018.01.30 추가
    private String edi_check;
    private String item_name      ;	
    
    public String getEdi_check(){
    	return edi_check;
    }
    public void setEdi_check(String edi_check) {
    	this.edi_check=edi_check;
    }
    public String getItem_list(){
    	return item_name;
    }
    public void setItem_list(String item_name) {
    	this.item_name=item_name;
    }
    
    public String getEdi_returned_date() {
		return edi_returned_date;
	}
	public void setEdi_returned_date(String edi_returned_date) {
		this.edi_returned_date = edi_returned_date;
	}
	
	public void setIo_code(String io_code) {
		this.io_code = io_code;
	}
	public String getIo_code() {
		return io_code;
	}
	public String getCancel_dt() {
        return cancel_dt;
    }
    public void setCancel_dt(String cancel_dt) {
        this.cancel_dt = cancel_dt;
    }
    public String getAmendment_code() {
        return amendment_code;
    }
    public void setAmendment_code(String amendment_code) {
        this.amendment_code = amendment_code;
    }
    public String getBill_type_code() {
        return bill_type_code;
    }
    public void setBill_type_code(String bill_type_code) {
        this.bill_type_code = bill_type_code;
    }
    public String getBiz_manage_id() {
        return biz_manage_id;
    }
    public void setBiz_manage_id(String biz_manage_id) {
        this.biz_manage_id = biz_manage_id;
    }
    public String getBroker_addr() {
        return broker_addr;
    }
    public void setBroker_addr(String broker_addr) {
        this.broker_addr = broker_addr;
    }
    public String getBroker_ceo_name() {
        return broker_ceo_name;
    }
    public void setBroker_ceo_name(String broker_ceo_name) {
        this.broker_ceo_name = broker_ceo_name;
    }
    public String getBroker_class() {
        return broker_class;
    }
    public void setBroker_class(String broker_class) {
        this.broker_class = broker_class;
    }
    public String getBroker_contact_depart() {
        return broker_contact_depart;
    }
    public void setBroker_contact_depart(String broker_contact_depart) {
        this.broker_contact_depart = broker_contact_depart;
    }
    public String getBroker_contact_email() {
        return broker_contact_email;
    }
    public void setBroker_contact_email(String broker_contact_email) {
        this.broker_contact_email = broker_contact_email;
    }
    public String getBroker_contact_name() {
        return broker_contact_name;
    }
    public void setBroker_contact_name(String broker_contact_name) {
        this.broker_contact_name = broker_contact_name;
    }
    public String getBroker_contact_phone() {
        return broker_contact_phone;
    }
    public void setBroker_contact_phone(String broker_contact_phone) {
        this.broker_contact_phone = broker_contact_phone;
    }
    public String getBroker_party_id() {
        return broker_party_id;
    }
    public void setBroker_party_id(String broker_party_id) {
        this.broker_party_id = broker_party_id;
    }
    public String getBroker_party_name() {
        return broker_party_name;
    }
    public void setBroker_party_name(String broker_party_name) {
        this.broker_party_name = broker_party_name;
    }
    public String getBroker_tax_regist_id() {
        return broker_tax_regist_id;
    }
    public void setBroker_tax_regist_id(String broker_tax_regist_id) {
        this.broker_tax_regist_id = broker_tax_regist_id;
    }
    public String getBroker_type() {
        return broker_type;
    }
    public void setBroker_type(String broker_type) {
        this.broker_type = broker_type;
    }
    public long getCharge_total_amount() {
        return charge_total_amount;
    }
    public void setCharge_total_amount(long charge_total_amount) {
        this.charge_total_amount = charge_total_amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getElectronic_report_yn() {
        return electronic_report_yn;
    }
    public void setElectronic_report_yn(String electronic_report_yn) {
        this.electronic_report_yn = electronic_report_yn;
    }
    public long getGrand_total_amount() {
        return grand_total_amount;
    }
    public void setGrand_total_amount(long grand_total_amount) {
        this.grand_total_amount = grand_total_amount;
    }
    public String getImport_doc_id() {
        return import_doc_id;
    }
    public void setImport_doc_id(String import_doc_id) {
        this.import_doc_id = import_doc_id;
    }
    public int getImport_item_quantity() {
        return import_item_quantity;
    }
    public void setImport_item_quantity(int import_item_quantity) {
        this.import_item_quantity = import_item_quantity;
    }
    public String getImport_period_end_day() {
        return import_period_end_day;
    }
    public void setImport_period_end_day(String import_period_end_day) {
        this.import_period_end_day = import_period_end_day;
    }
    public String getImport_period_start_day() {
        return import_period_start_day;
    }
    public void setImport_period_start_day(String import_period_start_day) {
        this.import_period_start_day = import_period_start_day;
    }
    public String getInvoicee_addr() {
        return invoicee_addr;
    }
    public void setInvoicee_addr(String invoicee_addr) {
        this.invoicee_addr = invoicee_addr;
    }
    public String getInvoicee_business_type_code() {
        return invoicee_business_type_code;
    }
    public void setInvoicee_business_type_code(String invoicee_business_type_code) {
        this.invoicee_business_type_code = invoicee_business_type_code;
    }
    public String getInvoicee_ceo_name() {
        return invoicee_ceo_name;
    }
    public void setInvoicee_ceo_name(String invoicee_ceo_name) {
        this.invoicee_ceo_name = invoicee_ceo_name;
    }
    public String getInvoicee_class() {
        return invoicee_class;
    }
    public void setInvoicee_class(String invoicee_class) {
        this.invoicee_class = invoicee_class;
    }
    public String getInvoicee_contact_depart1() {
        return invoicee_contact_depart1;
    }
    public void setInvoicee_contact_depart1(String invoicee_contact_depart1) {
        this.invoicee_contact_depart1 = invoicee_contact_depart1;
    }
    public String getInvoicee_contact_depart2() {
        return invoicee_contact_depart2;
    }
    public void setInvoicee_contact_depart2(String invoicee_contact_depart2) {
        this.invoicee_contact_depart2 = invoicee_contact_depart2;
    }
    public String getInvoicee_contact_email1() {
        return invoicee_contact_email1;
    }
    public void setInvoicee_contact_email1(String invoicee_contact_email1) {
        this.invoicee_contact_email1 = invoicee_contact_email1;
    }
    public String getInvoicee_contact_email2() {
        return invoicee_contact_email2;
    }
    public void setInvoicee_contact_email2(String invoicee_contact_email2) {
        this.invoicee_contact_email2 = invoicee_contact_email2;
    }
    public String getInvoicee_contact_name1() {
        return invoicee_contact_name1;
    }
    public void setInvoicee_contact_name1(String invoicee_contact_name1) {
        this.invoicee_contact_name1 = invoicee_contact_name1;
    }
    public String getInvoicee_contact_name2() {
        return invoicee_contact_name2;
    }
    public void setInvoicee_contact_name2(String invoicee_contact_name2) {
        this.invoicee_contact_name2 = invoicee_contact_name2;
    }
    public String getInvoicee_contact_phone1() {
        return invoicee_contact_phone1;
    }
    public void setInvoicee_contact_phone1(String invoicee_contact_phone1) {
        this.invoicee_contact_phone1 = invoicee_contact_phone1;
    }
    public String getInvoicee_contact_phone2() {
        return invoicee_contact_phone2;
    }
    public void setInvoicee_contact_phone2(String invoicee_contact_phone2) {
        this.invoicee_contact_phone2 = invoicee_contact_phone2;
    }
    public String getInvoicee_party_id() {
        return invoicee_party_id;
    }
    public void setInvoicee_party_id(String invoicee_party_id) {
        this.invoicee_party_id = invoicee_party_id;
    }
    public String getInvoicee_party_name() {
        return invoicee_party_name;
    }
    public void setInvoicee_party_name(String invoicee_party_name) {
        this.invoicee_party_name = invoicee_party_name;
    }
    public String getInvoicee_tax_regist_id() {
        return invoicee_tax_regist_id;
    }
    public void setInvoicee_tax_regist_id(String invoicee_tax_regist_id) {
        this.invoicee_tax_regist_id = invoicee_tax_regist_id;
    }
    public String getInvoicee_type() {
        return invoicee_type;
    }
    public void setInvoicee_type(String invoicee_type) {
        this.invoicee_type = invoicee_type;
    }
    public String getInvoicer_addr() {
        return invoicer_addr;
    }
    public void setInvoicer_addr(String invoicer_addr) {
        this.invoicer_addr = invoicer_addr;
    }
    public String getInvoicer_ceo_name() {
        return invoicer_ceo_name;
    }
    public void setInvoicer_ceo_name(String invoicer_ceo_name) {
        this.invoicer_ceo_name = invoicer_ceo_name;
    }
    public String getInvoicer_class() {
        return invoicer_class;
    }
    public void setInvoicer_class(String invoicer_class) {
        this.invoicer_class = invoicer_class;
    }
    public String getInvoicer_contact_depart() {
        return invoicer_contact_depart;
    }
    public void setInvoicer_contact_depart(String invoicer_contact_depart) {
        this.invoicer_contact_depart = invoicer_contact_depart;
    }
    public String getInvoicer_contact_email() {
        return invoicer_contact_email;
    }
    public void setInvoicer_contact_email(String invoicer_contact_email) {
        this.invoicer_contact_email = invoicer_contact_email;
    }
    public String getInvoicer_contact_name() {
        return invoicer_contact_name;
    }
    public void setInvoicer_contact_name(String invoicer_contact_name) {
        this.invoicer_contact_name = invoicer_contact_name;
    }
    public String getInvoicer_contact_phone() {
        return invoicer_contact_phone;
    }
    public void setInvoicer_contact_phone(String invoicer_contact_phone) {
        this.invoicer_contact_phone = invoicer_contact_phone;
    }
    public String getInvoicer_party_id() {
        return invoicer_party_id;
    }
    public void setInvoicer_party_id(String invoicer_party_id) {
        this.invoicer_party_id = invoicer_party_id;
    }
    public String getInvoicer_party_name() {
        return invoicer_party_name;
    }
    public void setInvoicer_party_name(String invoicer_party_name) {
        this.invoicer_party_name = invoicer_party_name;
    }
    public String getInvoicer_tax_regist_id() {
        return invoicer_tax_regist_id;
    }
    public void setInvoicer_tax_regist_id(String invoicer_tax_regist_id) {
        this.invoicer_tax_regist_id = invoicer_tax_regist_id;
    }
    public String getInvoicer_type() {
        return invoicer_type;
    }
    public void setInvoicer_type(String invoicer_type) {
        this.invoicer_type = invoicer_type;
    }
    public String getIssue_day() {
        return issue_day;
    }
    public void setIssue_day(String issue_day) {
        this.issue_day = issue_day;
    }
    public String getIssue_dt() {
        return issue_dt;
    }
    public void setIssue_dt(String issue_dt) {
        this.issue_dt = issue_dt;
    }
    public String getIssue_id() {
        return issue_id;
    }
    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }
    public String getJob_gub_code() {
        return job_gub_code;
    }
    public void setJob_gub_code(String job_gub_code) {
        this.job_gub_code = job_gub_code;
    }
    public String getModify_dt() {
        return modify_dt;
    }
    public void setModify_dt(String modify_dt) {
        this.modify_dt = modify_dt;
    }
    public String getModify_id() {
        return modify_id;
    }
    public void setModify_id(String modify_id) {
        this.modify_id = modify_id;
    }
    public long getPay_amount1() {
        return pay_amount1;
    }
    public void setPay_amount1(long pay_amount1) {
        this.pay_amount1 = pay_amount1;
    }
    public long getPay_amount2() {
        return pay_amount2;
    }
    public void setPay_amount2(long pay_amount2) {
        this.pay_amount2 = pay_amount2;
    }
    public long getPay_amount3() {
        return pay_amount3;
    }
    public void setPay_amount3(long pay_amount3) {
        this.pay_amount3 = pay_amount3;
    }
    public long getPay_amount4() {
        return pay_amount4;
    }
    public void setPay_amount4(long pay_amount4) {
        this.pay_amount4 = pay_amount4;
    }
    public String getPayment_type_code1() {
        return payment_type_code1;
    }
    public void setPayment_type_code1(String payment_type_code1) {
        this.payment_type_code1 = payment_type_code1;
    }
    public String getPayment_type_code2() {
        return payment_type_code2;
    }
    public void setPayment_type_code2(String payment_type_code2) {
        this.payment_type_code2 = payment_type_code2;
    }
    public String getPayment_type_code3() {
        return payment_type_code3;
    }
    public void setPayment_type_code3(String payment_type_code3) {
        this.payment_type_code3 = payment_type_code3;
    }
    public String getPayment_type_code4() {
        return payment_type_code4;
    }
    public void setPayment_type_code4(String payment_type_code4) {
        this.payment_type_code4 = payment_type_code4;
    }
    public String getPurpose_code() {
        return purpose_code;
    }
    public void setPurpose_code(String purpose_code) {
        this.purpose_code = purpose_code;
    }
    public String getRegist_dt() {
        return regist_dt;
    }
    public void setRegist_dt(String regist_dt) {
        this.regist_dt = regist_dt;
    }
    public String getRegist_id() {
        return regist_id;
    }
    public void setRegist_id(String regist_id) {
        this.regist_id = regist_id;
    }
    public String getRel_system_id() {
        return rel_system_id;
    }
    public void setRel_system_id(String rel_system_id) {
        this.rel_system_id = rel_system_id;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getStatus_code() {
        return status_code;
    }
    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
    public String getSvc_manage_id() {
        return svc_manage_id;
    }
    public void setSvc_manage_id(String svc_manage_id) {
        this.svc_manage_id = svc_manage_id;
    }
    public long getTax_total_amount() {
        return tax_total_amount;
    }
    public void setTax_total_amount(long tax_total_amount) {
        this.tax_total_amount = tax_total_amount;
    }
    public String getManage_id() {
        return manage_id;
    }
    public void setManage_id(String manage_id) {
        this.manage_id = manage_id;
    }
    public String getUpper_manage_id() {
        return upper_manage_id;
    }
    public void setUpper_manage_id(String upper_manage_id) {
        this.upper_manage_id = upper_manage_id;
    }
	public void setAdd_tax_yn(String add_tax_yn) {
		this.add_tax_yn = add_tax_yn;
	}
	public String getAdd_tax_yn() {
		return add_tax_yn;
	}
	public void setOnline_gub_code(String online_gub_code) {
		this.online_gub_code = online_gub_code;
	}
	public String getOnline_gub_code() {
		return online_gub_code;
	}
	public void setInvoicee_gub_code(String invoicee_gub_code) {
		this.invoicee_gub_code = invoicee_gub_code;
	}
	public String getInvoicee_gub_code() {
		return invoicee_gub_code;
	}
	public String getIssue_id1() {
	    return issue_id1;
	}
	public void setIssue_id1(String issue_id1) {
	    this.issue_id1 = issue_id1;
	}
	public String getIssue_id2() {
	    return issue_id2;
	}
	public void setIssue_id2(String issue_id2) {
	    this.issue_id2 = issue_id2;
	}
	

	public String getEsero_issue_id() {
	    return esero_issue_id;
	}
	public void setEsero_issue_id(String esero_issue_id) {
	    this.esero_issue_id = esero_issue_id;
	}

	public String getTax_2nd_sign() {
	    return tax_2nd_sign;
	}
	public void setTax_2nd_sign(String tax_2nd_sign) {
	    this.tax_2nd_sign = tax_2nd_sign;
	}
    public String getIssue_dt2() {
        return issue_dt2;
    }
    public void setIssue_dt2(String issue_dt2) {
        this.issue_dt2 = issue_dt2;
    }
}