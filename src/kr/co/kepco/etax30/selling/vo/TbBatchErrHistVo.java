package kr.co.kepco.etax30.selling.vo;

public class TbBatchErrHistVo {
	
	private String yyyymm;
	private String server_no;
	private String batch_job_code;
	private String start_dt;
	private String occur_dt;
	private String err_data_key;
	private String err_desc;
	private String regist_dt;
	private String modify_dt;
	
	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}
	public String getYyyymm() {
		return yyyymm;
	}
	public void setServer_no(String server_no) {
		this.server_no = server_no;
	}
	public String getServer_no() {
		return server_no;
	}
	public void setBatch_job_code(String batch_job_code) {
		this.batch_job_code = batch_job_code;
	}
	public String getBatch_job_code() {
		return batch_job_code;
	}
	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}
	public String getStart_dt() {
		return start_dt;
	}
	public void setOccur_dt(String occur_dt) {
		this.occur_dt = occur_dt;
	}
	public String getOccur_dt() {
		return occur_dt;
	}
	public void setErr_data_key(String err_data_key) {
		this.err_data_key = err_data_key;
	}
	public String getErr_data_key() {
		return err_data_key;
	}
	public void setErr_desc(String err_desc) {
		this.err_desc = err_desc;
	}
	public String getErr_desc() {
		return err_desc;
	}
	public void setRegist_dt(String regist_dt) {
		this.regist_dt = regist_dt;
	}
	public String getRegist_dt() {
		return regist_dt;
	}
	public void setModify_dt(String modify_dt) {
		this.modify_dt = modify_dt;
	}
	public String getModify_dt() {
		return modify_dt;
	}
	

}
