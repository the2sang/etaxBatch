package kr.co.kepco.etax30.selling.vo;

public class TbXmlDownInfoVo {
	
	private String date;
	private String file_name;
	private long total_amount;
	private long total_count;
	private long file_size;
	private String file_path;
	private String reg_dt;
	private String modify_dt;
	
	private String grp_no;
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setTotal_amount(long total_amount) {
		this.total_amount = total_amount;
	}
	public long getTotal_amount() {
		return total_amount;
	}
	public void setTotal_count(long total_count) {
		this.total_count = total_count;
	}
	public long getTotal_count() {
		return total_count;
	}
	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}
	public long getFile_size() {
		return file_size;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setModify_dt(String modify_dt) {
		this.modify_dt = modify_dt;
	}
	public String getModify_dt() {
		return modify_dt;
	}
	public String getGrp_no() {
		return grp_no;
	}
	public void setGrp_no(String grp_no) {
		this.grp_no = grp_no;
	}
	

}
