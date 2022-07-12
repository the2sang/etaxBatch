package kr.co.kepco.etax30.selling.vo;

public class TbBatchJobHistVo {

	private String yyyymm;
	private String server_no;
	private String batch_job_code;
	private String start_dt;
	private String end_dt;
	private long dest_cnt;
	private long work_cnt;
	private long dest_amt;
	private long work_amt;
	private String work_stat;
	private String regist_dt;
	private String modify_dt;
	
	private String grp_no;
	
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
	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}
	public String getEnd_dt() {
		return end_dt;
	}
	public void setDest_cnt(long dest_cnt) {
		this.dest_cnt = dest_cnt;
	}
	public long getDest_cnt() {
		return dest_cnt;
	}
	public void setWork_cnt(long work_cnt) {
		this.work_cnt = work_cnt;
	}
	public long getWork_cnt() {
		return work_cnt;
	}
	public void setDest_amt(long dest_amt) {
		this.dest_amt = dest_amt;
	}
	public long getDest_amt() {
		return dest_amt;
	}
	public void setWork_amt(long work_amt) {
		this.work_amt = work_amt;
	}
	public long getWork_amt() {
		return work_amt;
	}
	public void setWork_stat(String work_stat) {
		this.work_stat = work_stat;
	}
	public String getWork_stat() {
		return work_stat;
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
	public String getGrp_no() {
		return grp_no;
	}
	public void setGrp_no(String grp_no) {
		this.grp_no = grp_no;
	}
}
