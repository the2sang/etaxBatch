package kr.co.kepco.etax30.selling.vo;

public class TbTaxBillInfoVo {

    private String io_code;				//���Ը����ڵ� 1:����, 2:����					
    private String biz_manage_id;			//����ڰ�����ȣ	
    private String svc_manage_id;			//���񽺰�����ȣ	
    private String issue_dt;				//�����Ͻ�	
    private String signature      ;			//���ڼ���	
    private String issue_id      ;			//���ι�ȣ	
    private String issue_id1      ;			//�̸�������	
    private String issue_id2      ;			//�̸�������	
    private String issue_day      ;			//�ۼ�����	
    private String bill_type_code      ;		//���ݰ�꼭����
    private String purpose_code      ;			//����û���ڵ�
    private String amendment_code      ;		//���ڼ��ݰ�꼭 ���� �����ڵ�
    private String description      ;			//���
    private String import_doc_id      ;		//���� �Ű� ��ȣ	
    private String import_period_start_day      ;	//�ϰ��߱� ��������
    private String import_period_end_day      ;	//�ϰ��߱� ��������	
    private int import_item_quantity      ;		//�ϰ��߱� ���� �Ѱ�	
    private String invoicer_party_id      ;		//������ ����ڵ�Ϲ�ȣ	
    private String invoicer_tax_regist_id      ;	//������� �ĺ��ڵ�
    private String invoicer_party_name      ;		//���޾�ü ���ü��
    private String invoicer_ceo_name      ;		//���޾�ü ��ǥ�ڸ�	
    private String invoicer_addr      ;		//���޾�ü�� �ּ�		
    private String invoicer_type      ;		//���޾�ü�� ����		
    private String invoicer_class      ;		//���޾�ü�� ����	
    private String invoicer_contact_depart      ;	//���޾�ü ���μ�
    private String invoicer_contact_name      ;	//���޾�ü ����ڸ�	
    private String invoicer_contact_phone      ;	//���޾�ü ����� ��ȭ��ȣ
    private String invoicer_contact_email      ;	//���޾�ü ����� �̸���	
    private String invoicee_business_type_code      ;	//���޹޴��� ����ڵ�Ϲ�ȣ �����ڵ�
    private String invoicee_party_id      ;		//���޹޴��� ����ڵ�Ϲ�ȣ	
    private String invoicee_tax_regist_id      ;	//���޹޴��� ������� �ĺ��ڵ�
    private String invoicee_party_name      ;		//���޹޴��� ���ü��
    private String invoicee_ceo_name      ;		//���޹޴��� ��ǥ�ڸ�	
    private String invoicee_addr      ;		//���޹޴��� �ּ�		
    private String invoicee_type      ;		//���޹޴��� ����		
    private String invoicee_class      ;		//���޹޴��� ����	
    private String invoicee_contact_depart1      ;	//���޹޴��� ���μ�1
    private String invoicee_contact_name1      ;	//���޹޴��� ����ڸ�1
    private String invoicee_contact_phone1      ;	//���޹޴��� ����� ��ȭ��ȣ1
    private String invoicee_contact_email1      ;	//���޹޴��� ����� �̸���1
    private String invoicee_contact_depart2      ;	//���޹޴��� ���μ�2
    private String invoicee_contact_name2      ;	//���޹޴��� ����ڸ�2
    private String invoicee_contact_phone2      ;	//���޹޴��� ����� ��ȭ��ȣ2
    private String invoicee_contact_email2      ;	//���޹޴��� ����� �̸���2
    private String broker_party_id      ;		//��Ź����� ����ڵ�Ϲ�ȣ	
    private String broker_tax_regist_id      ;		//��Ź����� ������� �ĺ��ڵ�
    private String broker_party_name      ;		//��Ź����� ���ü��	
    private String broker_ceo_name      ;		//��Ź����� ��ǥ�ڸ�	
    private String broker_addr      ;			//��Ź����� �ּ�	
    private String broker_type      ;			//��Ź����� ����	
    private String broker_class      ;			//��Ź����� ����	
    private String broker_contact_depart      ;	//��Ź����� ���μ�	
    private String broker_contact_name      ;		//��Ź����� ����ڸ�
    private String broker_contact_phone      ;		//��Ź����� ����� ��ȭ��ȣ
    private String broker_contact_email      ;		//��Ź����� ����� �̸���
    private String payment_type_code1      ;		//��������ڵ�1
    private long pay_amount1      ;			//����������ݾ�1	
    private String payment_type_code2      ;		//��������ڵ�2
    private long pay_amount2      ;			//����������ݾ�2	
    private String payment_type_code3      ;		//��������ڵ�3
    private long pay_amount3      ;			//����������ݾ�3	
    private String payment_type_code4      ;		//��������ڵ�4
    private long pay_amount4      ;			//����������ݾ�4	
    private long tax_total_amount       ;		//�� ���ް��� �հ�
    private long grand_total_amount      ;		//�� ���� �հ�
    private long charge_total_amount      ;		//�Ѿ�(���ް���+����)
    private String status_code      ;			//�������
    private String job_gub_code      ;			//���������ڵ�(���¸���,������� ��)
    private String electronic_report_yn      ;		//���ڽŰ�ϷῩ��
    private String regist_dt      ;			//����Ͻ�	
    private String modify_dt      ;			//�����Ͻ�	
    private String regist_id      ;			//�����ID	
    private String modify_id      ;			//������ID	
    private String rel_system_id      ;		//����ý���ID	
    private String manage_id ; 			//����ڰ�����ȣ		
    private String upper_manage_id;			//�θ������ȣ : �������ݰ�꼭 ����� ���	
    private String cancel_dt;				//����Ͻ�	
    private String add_tax_yn;				//���꼼 ����
    private String online_gub_code;			//��,�������� �����ڵ�
    private String invoicee_gub_code;			//���޹޴��� �����ڵ�(���Ը� ���. 00:����, ��Ÿ:������)
    // 2012.4.25 �߰�
    private String esero_issue_id;			//����û ��� ���ι�ȣ - ����û ESERO�� ��ϵ� ���� ���ι�ȣ
    // 2012.6.15 �߰�
    private String tax_2nd_sign;			//���μ��ݰ�꼭 2�����ڼ��� ���� NULL:���� ������, N:���� ������, Y:��ü 2������Ϸ�
    private String issue_dt2;			//���ݰ�꼭 2�����ڼ��� ��������
    // 2015.9.17 �߰�
    private String edi_returned_date;	//����EDI�� ���޿����� ��¥�� SMS ���ݰ�꼭�����û�� ��� 
    //2018.01.30 �߰�
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