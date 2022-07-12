package kr.co.kepco.etax30.selling.vo;
//상품관
public class ItemListVo {
    
    private String rel_system_id	;			//연계시스템ID
    private String job_gub_code;			//업무구분코드
    private String manage_id	;			//관리번호
    private String issue_day	;			//작성일자
    private String io_code	;			
    
    private int seq_no      ;					//일련번호
    
    private String biz_manage_id      ;		//사업자관리번호
    private String purchase_day      ;			//공급일자
    private String item_name      ;			//품목명
    private String item_info      ;			//규격
    private String item_desc      ;			//비고
    private double unit_quantity      ;			//수량
    private double unit_amount      ;			//단가
    private long invoice_amount      ;			//공급가액
    private long tax_amount      ;    			//세액
    
    public String getBiz_manage_id() {
        return biz_manage_id;
    }
    public void setBiz_manage_id(String biz_manage_id) {
        this.biz_manage_id = biz_manage_id;
    }
    public long getInvoice_amount() {
        return invoice_amount;
    }
    public void setInvoice_amount(long invoice_amount) {
        this.invoice_amount = invoice_amount;
    }
    public String getItem_desc() {
        return item_desc;
    }
    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }
    public String getItem_info() {
        return item_info;
    }
    public void setItem_info(String item_info) {
        this.item_info = item_info;
    }
    public String getItem_name() {
        return item_name;
    }
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public String getJob_gub_code() {
        return job_gub_code;
    }
    public void setJob_gub_code(String job_gub_code) {
        this.job_gub_code = job_gub_code;
    }
    public String getManage_id() {
        return manage_id;
    }
    public void setManage_id(String manage_id) {
        this.manage_id = manage_id;
    }
    public String getPurchase_day() {
        return purchase_day;
    }
    public void setPurchase_day(String purchase_day) {
        this.purchase_day = purchase_day;
    }
    public String getRel_system_id() {
        return rel_system_id;
    }
    public void setRel_system_id(String rel_system_id) {
        this.rel_system_id = rel_system_id;
    }
    public int getSeq_no() {
        return seq_no;
    }
    public void setSeq_no(int seq_no) {
        this.seq_no = seq_no;
    }
    public long getTax_amount() {
        return tax_amount;
    }
    public void setTax_amount(long tax_amount) {
        this.tax_amount = tax_amount;
    }
    public double getUnit_amount() {
        return unit_amount;
    }
    public void setUnit_amount(double unit_amount) {
        this.unit_amount = unit_amount;
    }
    public double getUnit_quantity() {
        return unit_quantity;
    }
    public void setUnit_quantity(double unit_quantity) {
        this.unit_quantity = unit_quantity;
    }
	public void setIssue_day(String issue_day) {
		this.issue_day = issue_day;
	}
	public String getIssue_day() {
		return issue_day;
	}
	public void setIo_code(String io_code) {
		this.io_code = io_code;
	}
	public String getIo_code() {
		return io_code;
	}


}
