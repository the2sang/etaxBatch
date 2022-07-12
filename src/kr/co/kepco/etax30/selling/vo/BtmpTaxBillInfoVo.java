package kr.co.kepco.etax30.selling.vo;

public class BtmpTaxBillInfoVo {
	
	private String row_no;
	private String machine_idx;
	private String thread_idx;
	private String row_id;
	private String charge_total_amount;
	private String tax_total_amount;
	
	public void setRow_no(String row_no) {
		this.row_no = row_no;
	}
	public String getRow_no() {
		return row_no;
	}
	public void setMachine_idx(String machine_idx) {
		this.machine_idx = machine_idx;
	}
	public String getMachine_idx() {
		return machine_idx;
	}
	public void setThread_idx(String thread_idx) {
		this.thread_idx = thread_idx;
	}
	public String getThread_idx() {
		return thread_idx;
	}
	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}
	public String getRow_id() {
		return row_id;
	}
	public void setCharge_total_amount(String charge_total_amount) {
		this.charge_total_amount = charge_total_amount;
	}
	public String getCharge_total_amount() {
		return charge_total_amount;
	}
	public void setTax_total_amount(String tax_total_amount) {
		this.tax_total_amount = tax_total_amount;
	}
	public String getTax_total_amount() {
		return tax_total_amount;
	}

}
