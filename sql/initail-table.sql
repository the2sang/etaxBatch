truncate table  btmp_tax_bill_info;
truncate table  if_tax_bill_info_off_temp;
truncate table  if_tax_bill_result_info;
truncate table  tb_status_hist;
truncate table  tb_tax_bill_info;
truncate table  tb_trade_item_list;
truncate table  tb_batch_job_hist;

update if_tax_bill_info
set FLG = 'N';
commit;
