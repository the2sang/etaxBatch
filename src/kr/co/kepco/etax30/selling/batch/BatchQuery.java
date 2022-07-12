package kr.co.kepco.etax30.selling.batch;

import kr.co.kepco.etax30.selling.dao.TbTaxBillInfoDao;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
//import java.util.Calendar;

import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.util.CommCipher;
import kr.co.kepco.etax30.selling.vo.CodeVO;

import kr.co.kepco.etax30.selling.dao.TbTradeItemListDao;

public class BatchQuery {
    
	 //final static int NOTAXBILL_ESERO_REG_DAY = Integer.parseInt(CommProperties.getString("NOTAXBILL_ESERO_REG_DAY"));
	final static String NOTAXBILL_ESERO_REG_DAY = CommProperties.getString("NOTAXBILL_ESERO_REG_DAY");
          
    /**
     * 접수 데이터중 item정보가 없는 것 체크 . 
     * 대상이 존재하면 임시테이블로 저장
     */    
    public boolean error_info_trans_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_INFO_TEMP(                        \n")
            .append("    REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID,               \n")
            .append("    REGIST_DT, MODIFY_DT, ISSUE_ID,                       \n")
            .append("    ISSUE_DAY, BILL_TYPE_CODE, INVOICER_PARTY_ID,         \n") 
            .append("    INVOICER_PARTY_NAME, INVOICER_CEO_NAME, INVOICEE_BUSINESS_TYPE_CODE, \n")
            .append("    INVOICEE_PARTY_ID, INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME,           \n")
            .append("    INVOICEE_ADDR, CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, \n") 
            .append("        GRAND_TOTAL_AMOUNT)                               \n")
            .append("SELECT                                                    \n")
            .append("    A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID,         \n")
            .append("    A.REGIST_DT, A.MODIFY_DT, A.ISSUE_ID,                 \n")
            .append("    A.ISSUE_DAY, A.BILL_TYPE_CODE, A.INVOICER_PARTY_ID,                        \n")
            .append("    A.INVOICER_PARTY_NAME, A.INVOICER_CEO_NAME, A.INVOICEE_BUSINESS_TYPE_CODE, \n")
            .append("    A.INVOICEE_PARTY_ID, A.INVOICEE_PARTY_NAME, A.INVOICEE_CEO_NAME,           \n")  
            .append("    A.INVOICEE_ADDR, A.CHARGE_TOTAL_AMOUNT, A.TAX_TOTAL_AMOUNT,                \n")
            .append("    A.GRAND_TOTAL_AMOUNT                 \n")
            .append("FROM IF_TAX_BILL_INFO A                                   \n")
            .append("WHERE 1>0                                                 \n")
            .append("AND A.FLG = 'N'                                           \n")
            //.append("AND A.ONLINE_GUB_CODE='1'                               \n")
            .append("AND A.ONLINE_GUB_CODE IN ('1','3')                        \n")
            .append("AND NOT EXISTS                                            \n")
            .append("        (                                                 \n")
            .append("         SELECT 1                                         \n")
            .append("         FROM   IF_TAX_BILL_ITEM_LIST B                   \n")
            .append("         WHERE 1>0                                        \n")
            .append("           AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID          \n")
            .append("           AND A.JOB_GUB_CODE = B.JOB_GUB_CODE            \n")
            .append("           AND A.MANAGE_ID = B.MANAGE_ID                  \n")
            .append("        )                                                 \n");

            CommUtil.logWriter(sb.toString(),1);
            
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
                con.commit();
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    }
    
    /**
     * 접수 데이터중 item정보가 없는 것 체크 .
     * 대상이 존재하면 임시테이블로 저장
     */    
    public boolean error_result_info_insert(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO(                              \n")
            .append("    REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE,          \n")
            .append("    STATUS_DESC, REGIST_DT, MODIFY_DT, ISSUE_ID,                  \n")
            .append("    EAI_STAT, EAI_CDATE, EAI_UDATE)                               \n")
            .append("SELECT                                                            \n")
            .append("    A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID, '99',           \n")
            .append("    'ITEM정보 없음', SYSDATE, SYSDATE, A.ISSUE_ID,                  \n")
          //.append("    DECODE(A.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X'),NULL, NULL   \n")
            .append("    DECODE(A.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(A.ONLINE_GUB_CODE,'3',NULL),'X'),NULL, NULL   \n")
            .append("FROM IF_TAX_BILL_INFO_TEMP A                                      \n");

            CommUtil.logWriter(sb.toString(),1);
 
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    } 
    
    
    
    
    
    
    
    
    /**
     * IF_TAX_BILL_INFO 에서 IF_TAX_BILL_INFO_TEMP DATA 전송.
     * 처리대상을 조회하여 TEMP에 임시 저장
     * @param String 기준일자, Connection
     */    
    
 
    public boolean tax_info_trans_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_INFO_TEMP                                                                   \n")
            .append("(MANAGE_ID,ISSUE_DAY,BILL_TYPE_CODE,PURPOSE_CODE,AMENDMENT_CODE,DESCRIPTION,UPPER_MANAGE_ID,        \n")
            .append("INVOICER_PARTY_ID,INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                     \n")
            .append("INVOICER_ADDR,INVOICER_TYPE,INVOICER_CLASS,INVOICER_CONTACT_DEPART,                                 \n")
            .append("INVOICER_CONTACT_NAME,INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,INVOICEE_BUSINESS_TYPE_CODE,    \n")
            .append("INVOICEE_PARTY_ID,INVOICEE_TAX_REGIST_ID,INVOICEE_PARTY_NAME,INVOICEE_CEO_NAME,                     \n")
            .append("INVOICEE_ADDR,INVOICEE_TYPE,INVOICEE_CLASS,                                                         \n")
            .append("INVOICEE_CONTACT_DEPART1,INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1,INVOICEE_CONTACT_EMAIL1,    \n")
            .append("INVOICEE_CONTACT_DEPART2,INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2,INVOICEE_CONTACT_EMAIL2,    \n")
            .append("PAYMENT_TYPE_CODE1,PAY_AMOUNT1,PAYMENT_TYPE_CODE2,PAY_AMOUNT2,                                      \n")
            .append("PAYMENT_TYPE_CODE3,PAY_AMOUNT3,PAYMENT_TYPE_CODE4,PAY_AMOUNT4,                                      \n")
            .append("CHARGE_TOTAL_AMOUNT,TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT,                                            \n")
            .append("JOB_GUB_CODE,REL_SYSTEM_ID,REGIST_DT,MODIFY_DT,CANCEL_DT,ISSUE_ID,ADD_TAX_YN,BILL_ISSUE_YYYYMM,     \n")
            .append("ONLINE_GUB_CODE, ERP_EVIDENCE_CODE, VAT_GUB_CODE, RECEIPT_GUB_CODE, REGIST_ID, BELNR, ASP_ISSUE_ID )\n")
            .append("SELECT /*+ INDEX(IF_TAX_BILL_INFO IF_TAX_BILL_INFO_IX1) */                                          \n")
            .append("MANAGE_ID,ISSUE_DAY,BILL_TYPE_CODE,PURPOSE_CODE,AMENDMENT_CODE,DESCRIPTION,UPPER_MANAGE_ID,         \n")
            .append("INVOICER_PARTY_ID,INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                     \n")
            .append("INVOICER_ADDR,INVOICER_TYPE,INVOICER_CLASS,INVOICER_CONTACT_DEPART,                                 \n")
            .append("INVOICER_CONTACT_NAME,INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,INVOICEE_BUSINESS_TYPE_CODE,    \n")
            .append("INVOICEE_PARTY_ID,INVOICEE_TAX_REGIST_ID,INVOICEE_PARTY_NAME,INVOICEE_CEO_NAME,                     \n")
            .append("INVOICEE_ADDR,INVOICEE_TYPE,INVOICEE_CLASS,                                                         \n")
            .append("INVOICEE_CONTACT_DEPART1,INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1,INVOICEE_CONTACT_EMAIL1,    \n")
            .append("INVOICEE_CONTACT_DEPART2,INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2,INVOICEE_CONTACT_EMAIL2,    \n")
            .append("PAYMENT_TYPE_CODE1,PAY_AMOUNT1,PAYMENT_TYPE_CODE2,PAY_AMOUNT2,                                      \n")
            .append("PAYMENT_TYPE_CODE3,PAY_AMOUNT3,PAYMENT_TYPE_CODE4,PAY_AMOUNT4,                                      \n")
            .append("CHARGE_TOTAL_AMOUNT,TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT,                                            \n")
            .append("JOB_GUB_CODE,REL_SYSTEM_ID,REGIST_DT,MODIFY_DT,CANCEL_DT,ISSUE_ID,ADD_TAX_YN,BILL_ISSUE_YYYYMM,     \n")
            .append("ONLINE_GUB_CODE, ERP_EVIDENCE_CODE, VAT_GUB_CODE, RECEIPT_GUB_CODE, REGIST_ID, BELNR, ASP_ISSUE_ID  \n")
            .append("FROM IF_TAX_BILL_INFO	                                                                             \n")
            .append("WHERE 1>0	                                                                                         \n")
            .append("AND ONLINE_GUB_CODE IN('1','3')	                                                                 \n")
            .append("AND FLG = 'N'	                                                                                     \n")
            //2014. 4. 27 added by 박상종
            //수정 사유 : 공가매출에대해 공가에서 보내와서 ERP에 정상적으로 전달된것만 처리하기 위함
            //.append("AND ( JOB_GUB_CODE <> '350010' OR (JOB_GUB_CODE = '350010' AND EAI_STAT = 'C') )                    \n");
            //2018.02.22  유종일  PPA코드  (350020) 추가  , 공가매출과 동일하게 처리 
            .append("AND ( JOB_GUB_CODE <> '350010' and   JOB_GUB_CODE <> '350020'  OR (JOB_GUB_CODE = '350010' AND EAI_STAT = 'C') OR (JOB_GUB_CODE = '350020' AND EAI_STAT = 'C') OR (JOB_GUB_CODE='350040'))                    \n");            
            //.append("AND REL_SYSTEM_ID = 'K1ERP11000' --20100820 \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
                con.commit();
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
           	e.printStackTrace();
            }
	}
	return bl;
    }  
    

    public boolean tax_confirm_info_trans_temp(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	PreparedStatement pstm=null;
        StringBuffer sb= new StringBuffer()
		.append(" INSERT INTO IF_TAX_BILL_CONFIRM_INFO_TEMP                                   \n")          
		.append(" (REL_SYSTEM_ID,    JOB_GUB_CODE,    MANAGE_ID,     STATUS_CODE,             \n")          
		.append("  STATUS_DESC,      REGIST_DT,       ISSUE_ID,      EAI_STAT,                \n")          
		.append("  EAI_CDATE,        EAI_UDATE,       ERP_ACC_YEAR,  ERP_SLIP_NO,             \n")          
		.append("  MAIL_FLG     )                                                             \n")          
		.append("  (SELECT                                                                    \n")          
		.append("  A.REL_SYSTEM_ID,    A.JOB_GUB_CODE, A.MANAGE_ID,      A.STATUS_CODE,       \n")          
		.append("  A.STATUS_DESC,      A.REGIST_DT,    A.ISSUE_ID,       A.EAI_STAT,          \n")          
		.append("  A.EAI_CDATE,        A.EAI_UDATE,    A.ERP_ACC_YEAR,   A.ERP_SLIP_NO,       \n")          
		.append("  A.MAIL_FLG                                                                 \n")          
		.append("  FROM IF_TAX_BILL_CONFIRM_INFO A, TB_TAX_BILL_INFO B                        \n")          
		.append("  WHERE  A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                   \n")          
		.append(" AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                         \n")          
		.append(" AND A.MANAGE_ID = B.SVC_MANAGE_ID                                           \n")          
		.append(" AND A.STATUS_CODE = '01'                                                    \n")          
		.append(" AND B.IO_CODE = 1  -- 매출만 해당                                                                                           \n")          
		//테이블 full scan 오류 수정 조건 추가  2012.07.23 
		.append(" AND B.ISSUE_ID = A.ISSUE_ID                                                 \n")
		.append(" AND B.ONLINE_GUB_CODE IN ('1') -- 매출  KEPCOBILL 발행 온라인만 해당                            \n")
		/* 제   목 : 공가 매출전자세금계산서연계 관련 
		 * 수정일 : 2013.11.11 빼빼로DAY
         * 수정 내용 : ERP 매출만 보내고 공가매출은 제외 되도록 JOB_GUB_CODE 예외조건 추가 */ 
     	// 공가매출 프로세스 2차 변경 적용  2013.12.26
        // comment처리함
		//.append(" AND B.JOB_GUB_CODE <> '350010'  -- 350010은 공가매출                                                     \n")          
		.append(" AND A.ERP_ACC_YEAR IS NOT NULL                                              \n")          
		.append(" AND A.ERP_SLIP_NO IS NOT NULL                                               \n")
		.append(" AND A.MAIL_FLG IS NULL )                                                    \n");                      
        try{                
               CommUtil.logWriter("SQL:"+sb.toString(),1);
                pstm = con.prepareStatement(sb.toString());
                int cnts = 0;
                cnts = pstm.executeUpdate();
                CommUtil.logWriter("매출 전표처리한 메일/sms 송신 처리 대상 건수 :"+cnts,3);
                if(cnts > 0){
                    bl = true;
                    con.commit();
                }
    	}catch(SQLException e){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e.toString(),4);
    	    throw e;
    	}catch(Exception e1){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter("SQL:"+sb.toString(),4);
                    CommUtil.logWriter(e.toString(),4);
               	e.printStackTrace();
                }
    	}
    	return bl;
        }  
   
    
    
    
    public boolean email_tb_xml_info_temp(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	PreparedStatement pstm=null;
        StringBuffer sb= new StringBuffer()
        
        .append("INSERT INTO TB_XML_INFO_TEMP                                                 \n") 
        .append("(  IO_CODE,      ISSUE_DAY,     BIZ_MANAGE_ID,  R_VALUE,                     \n")
        .append("   REGIST_DT,    MODIFY_DT,     REGIST_ID,      MODIFY_ID,                   \n")
        .append("   ISSUE_ID,     EMAIL_ADDRESS                                               \n")
        .append(")                                                                            \n")
        .append("(SELECT B.IO_CODE,       B.ISSUE_DAY,     B.BIZ_MANAGE_ID,        B.R_VALUE, \n")     
        .append("             B.REGIST_DT,     B.MODIFY_DT,     B.REGIST_ID,    B.MODIFY_ID,  \n")
        .append("             A.ISSUE_ID, A.INVOICEE_CONTACT_EMAIL2                           \n")
        .append("             FROM  TB_TAX_BILL_INFO_ENC A, TB_XML_INFO B                     \n")
        .append("             WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                         \n")
        .append("             AND A.ISSUE_DAY = B.ISSUE_DAY                                   \n")
        .append("             AND B.IO_CODE = '1'                                             \n")
        .append("             AND B.EMAILTRANS_FLG IS NULL                                    \n")
        .append("             AND A.IO_CODE = '1'            -- 매출세금계산서만                                  \n")
        .append("             AND A.ONLINE_GUB_CODE IN('1','3') -- ERP, 요금 온라인만                       \n")
        //2014. 12. 9 수정계산서도 추가로 처리 로직은 bill_type_code 구분없이 처리 왜냐하면 
        //            기존에 STATUS_CODE = '04'만 처리하므로 bill_type_code 구분이 무의미함 by 박상종 
        //.append("             AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')--계산서는 제외 즉, 세금계산서만 전송 \n")
        .append("             AND A.STATUS_CODE = '04'                                        \n")
        .append("             AND ROWNUM < 100)  -- BLOB_TO_LONG procedure를 위해 갯수 제한              \n"); 
        try{                
               CommUtil.logWriter("SQL:"+sb.toString(),1);
                pstm = con.prepareStatement(sb.toString());
                int cnts = 0;
                cnts = pstm.executeUpdate();
                CommUtil.logWriter("매출 국세청 신고완료후 메일 송신 대상 건수 :"+cnts,3);
                if(cnts > 0){
                    bl = true;
                    con.commit();
                }
    	}catch(SQLException e){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e.toString(),4);
    	    throw e;
    	}catch(Exception e1){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter("SQL:"+sb.toString(),4);
                    CommUtil.logWriter(e.toString(),4);
               	e.printStackTrace();
                }
    	}
    	return bl;
        }      
    public boolean email_ltf_info_insert(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	int InstCnt = 0;
    	CallableStatement cs= null;
        StringBuffer sb= new StringBuffer()
        .append("{call BLOB_TO_LONG(?)}\n"); 
        
        try{                
               CommUtil.logWriter("SQL:"+sb.toString(),1);
               //prepareCall : JAVA에서 Procedure 호출하는 method
               cs = con.prepareCall(sb.toString());
               cs.registerOutParameter(1,Types.INTEGER);
               bl = cs.execute();
               CommUtil.logWriter("bl:"+bl,1);
               InstCnt = cs.getInt(1);
               CommUtil.logWriter("EMAIL_LTF_INFO_TB테이블 INSERT 갯수 [BLOB_TO_LONG OUT(InstCnt)]:"+InstCnt,1);
                if(InstCnt>0){bl = true;}
                CommUtil.logWriter("bl:"+bl,1);
                
    	}catch(SQLException e){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e.toString(),4);
            bl = false;
    	    throw e;
    	}catch(Exception e1){
            CommUtil.logWriter("SQL:"+sb.toString(),4);
            CommUtil.logWriter(e1.toString(),4);
            bl = false;
    	    throw e1;
    	}finally{
                try{
                	if (cs != null){
                		cs.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter("SQL:"+sb.toString(),4);
                    CommUtil.logWriter(e.toString(),4);
               	    e.printStackTrace();
                }
    	}
    	return bl;
        }      
  
    /**
     * IF_TAX_BILL_INFO_TEMP 에서 TB_TAX_BILL_INFO로  DATA 전송.
     * 처리대상을 관리테이블로 이관
     * @param String 기준일자, Connection
     */     
    public boolean tax_info_trans(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null; //  
	         
	try{                    
            StringBuffer sb= new StringBuffer()            
//            2013.11.11 공가매출 연계 추가로 INSERT항목 추가작업 적용 전
//            .append("INSERT INTO TB_TAX_BILL_INFO \n") 
//            .append("    (BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,                                                   \n")
//            .append("    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,                                                                \n")
//            .append("    IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                      \n")
//            .append("    INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                           \n")
//            .append("    INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                          \n")
//            .append("    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                           \n")
//            .append("    INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                           \n")
//            .append("    INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                                   \n")
//            .append("    INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                     \n")
//            .append("    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                        \n")
//            .append("    INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                        \n")
//            .append("    BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,                                                                 \n")
//            .append("    BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,                                                                  \n")
//            .append("    BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,                                                                               \n")
//            .append("    BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                                                                               \n")
//            .append("    PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                          \n")
//            .append("    PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                          \n")
//            .append("    CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                                \n")
//            .append("    STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID,REGIST_ID,MODIFY_ID,IO_CODE, ADD_TAX_YN,                                         \n")
//            .append("    INVOICEE_GUB_CODE,ONLINE_GUB_CODE)                                                                                        \n")
//            .append("    SELECT /*+ FULL(A) PARALLEL(A 16) */                                                                                      \n")
//            .append("    TO_CHAR(SYSDATE, 'YYYYMM')||'1'|| NVL(SUBSTR(REL_SYSTEM_ID, 1, 3), 'EDI')||JOB_GUB_CODE||LPAD(SQ_MNG_ID.NEXTVAL, 8, '0'), \n")
//            //.append("        MANAGE_ID ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                                 \n")
//            .append("        MANAGE_ID ,TO_CHAR(REGIST_DT,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                                 \n")
//            .append("        BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE,DESCRIPTION,                                                             \n")
//            .append("        '', '', '', '',                                                                                                       \n")
//            .append("        INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                       \n")
//            .append("        INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                      \n")
//            .append("        INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                       \n")
//            .append("        INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                       \n")
//            .append("        INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                               \n")
//            .append("        INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                 \n")
//            .append("        INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                   \n")
//            .append("        INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                   \n")
//            .append("         '','','','','','','','','','','',                                                                                    \n")
//            .append("        PAYMENT_TYPE_CODE1, PAY_AMOUNT1, PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                     \n")
//            .append("        PAYMENT_TYPE_CODE3, PAY_AMOUNT3, PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                     \n")
//            .append("        CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                            \n")
//            .append("        '01', JOB_GUB_CODE, REL_SYSTEM_ID, '', '', '1', ADD_TAX_YN,'00',ONLINE_GUB_CODE                                       \n")
//            .append("    FROM IF_TAX_BILL_INFO_TEMP  A                                                                                             \n");	
  
            //2013.11.11 공가매출 연계 추가로 INSERT항목 추가됨
            .append("INSERT INTO TB_TAX_BILL_INFO \n") 
            .append("    (BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,                                                   \n")
            .append("    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,                                                                \n")
            .append("    IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                      \n")
            .append("    INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                           \n")
            .append("    INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                          \n")
            .append("    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                           \n")
            .append("    INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                           \n")
            .append("    INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                                   \n")
            .append("    INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                     \n")
            .append("    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                        \n")
            .append("    INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                        \n")
            .append("    BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,                                                                 \n")
            .append("    BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,                                                                  \n")
            .append("    BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,                                                                               \n")
            .append("    BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                                                                               \n")
            .append("    PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                          \n")
            .append("    PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                          \n")
            .append("    CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                                \n")
            .append("    STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID, VAT_GUB_CODE, REGIST_ID, MODIFY_ID,IO_CODE, ADD_TAX_YN,                         \n")
            .append("    INVOICEE_GUB_CODE,ONLINE_GUB_CODE, ERP_SEND_CODE, TAX_TYPE_CODE, ESERO_ISSUE_ID,                                          \n")
            .append("    UPPER_MANAGE_ID )                                                                                                         \n")
            .append("    SELECT /*+ FULL(A) PARALLEL(A 16) */                                                                                      \n")
            .append("    TO_CHAR(SYSDATE, 'YYYYMM')||'1'|| NVL(SUBSTR(REL_SYSTEM_ID, 1, 3), 'EDI')||JOB_GUB_CODE||LPAD(SQ_MNG_ID.NEXTVAL, 8, '0'), \n")
            //.append("        MANAGE_ID ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                               \n")
            //제   목 : 매출세금계산서 자체 생성처리(TID사용안함)
            //수정일 : 2013.12.11
            //xml생성시 작성일로 사용하는 issue_dt를 배치가 돌아가서 xml을 생성하는 sysdate로 변경
            // 예전에는 ET_TID_MAIN으로 넣을때 sysdate로 넣어서 tid툴이 sysdate로 생성 처리되어 문제가 없었음
            //.append("        MANAGE_ID ,TO_CHAR(REGIST_DT,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                               \n")
            .append("        MANAGE_ID ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                               \n")
            .append("        BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE,DESCRIPTION,                                                             \n")
            .append("        '', '', '', '',                                                                                                       \n")
            .append("        INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                       \n")
            .append("        INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                      \n")
            .append("        INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                       \n")
            .append("        INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                       \n")
            .append("        INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                               \n")
            //-------------------------------------------------------------------------------------------------------
            // 2014. 4. 1 변경
            // 영업시스템에서 연계되는 공급자 주소항목에 공백(HEX값 00(null)이 포함되면서 
            // "An invalid XML character (Unicode: 0x0)  --> 잘못된 XML 문자 (유니 코드 : 0x0으로)xml" 에러나 나면서 전자서명 오류 발생
            // 그래서 오라클 function으로 HEX값 00 제거 처리함
            // 원복함(정보인증 method 사용으로 대체)
            .append("        INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                 \n")
            //.append("        INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, UTL_I18N.RAW_TO_CHAR(replace(rawtohex(invoicee_addr),'00','')),                 \n")
            //.append("        INVOICEE_TYPE, INVOICEE_CLASS,                                 \n")
            //-------------------------------------------------------------------------------------------------------
            .append("        INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                   \n")
            .append("        INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                   \n")
            .append("         '','','','','','','','','','','',                                                                                    \n")
            .append("        PAYMENT_TYPE_CODE1, PAY_AMOUNT1, PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                     \n")
            .append("        PAYMENT_TYPE_CODE3, PAY_AMOUNT3, PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                     \n")
            .append("        CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                            \n")
 		    // 제   목 : 공가 매출전자세금계산서연계 관련
		    // 수정일 : 2013.11.11 빼빼로DAY
            // 공가매출인경우 STATUS_CODE=00, REGIST_ID 사번 등록, ERP_SEND_CODE=Y, TAX_TYPE_CODE=01
            //.append("        DECODE( JOB_GUB_CODE, '350010', '00', '01'), JOB_GUB_CODE, REL_SYSTEM_ID, VAT_GUB_CODE, DECODE( JOB_GUB_CODE, '350010', REGIST_ID, NULL), '', '1', ADD_TAX_YN,     \n")
            //2018.02.22  유종일  PPA코드  (350020) 추가  , 공가매출과 동일하게 처리
            .append("        DECODE(JOB_GUB_CODE, '350010', '00', '350020', '00','350040','00',  '01'), JOB_GUB_CODE, REL_SYSTEM_ID, VAT_GUB_CODE, DECODE( JOB_GUB_CODE, '350010', REGIST_ID,'350020', REGIST_ID,'350040',REGIST_ID, NULL), '', '1', ADD_TAX_YN,     \n")
            //.append("        '00',ONLINE_GUB_CODE, DECODE( JOB_GUB_CODE, '350010', 'Y', NULL), DECODE( JOB_GUB_CODE, '350010', '01', NULL), ASP_ISSUE_ID, \n")
            //2018.02.22  유종일  PPA코드  (350020) 추가  , 공가매출과 동일하게 처리
            .append("        '00',ONLINE_GUB_CODE, DECODE(JOB_GUB_CODE, '350010', 'Y','350020', 'Y','350040','Y', NULL) , DECODE( JOB_GUB_CODE, '350010', '01', '350020', '01','350040','01', NULL) , ASP_ISSUE_ID, \n")
            
            .append("        UPPER_MANAGE_ID                                                                                                       \n")
            .append("    FROM IF_TAX_BILL_INFO_TEMP  A                                                                                             \n");	
            
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    }


    /**
     * 공가시스템에서 들어온 IF_TAX_BILL_INFO 정보를 ERP FI에 연계 하기 위해 
     * ERP FI 연계에 맞게 추가로 IF_TAX_BILL_INFO INSERT
     * 2013. 11. 11 빼빼로DAY
     */
    
//    public boolean tax_info_copy4FI(Connection con, String rel_system_id)throws SQLException, Exception{
//	
//	boolean bl = false;
//	PreparedStatement pstm=null; //  
//	         
//	try{                    
//            StringBuffer sb= new StringBuffer()            
//            .append("INSERT INTO IF_TAX_BILL_INFO \n")
//            .append("    (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID,                                                \n")    
//            .append("    ADD_TAX_YN, ISSUE_ID, ISSUE_DAY,                                                       \n")
//			.append("    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE,                                          \n")
//			.append("    UPPER_MANAGE_ID, DESCRIPTION, INVOICER_PARTY_ID,                                       \n")
//			.append("    INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME, INVOICER_CEO_NAME,                        \n")
//			.append("    INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                          \n")
//			.append("    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, INVOICER_CONTACT_PHONE,                \n")
//			.append("    INVOICER_CONTACT_EMAIL, INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID,                \n")
//			.append("    INVOICEE_TAX_REGIST_ID, INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME,                        \n") 
//			.append("    INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                          \n")
//			.append("    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1,             \n")
//			.append("    INVOICEE_CONTACT_EMAIL1, INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,             \n") 
//			.append("    INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2, PAYMENT_TYPE_CODE1,                  \n")
//			.append("    PAY_AMOUNT1, PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                          \n")
//			.append("    PAYMENT_TYPE_CODE3, PAY_AMOUNT3, PAYMENT_TYPE_CODE4,                                   \n")
//			.append("    PAY_AMOUNT4, CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,                                    \n")
//			.append("    GRAND_TOTAL_AMOUNT, CUST_NO, BILL_YYYYMM,                                              \n")
//			.append("    BILL_ISSUE_YYYYMM, PAY_DEADLINE, ONLINE_GUB_CODE,                                      \n")
//			.append("    FLG, REGIST_DT, MODIFY_DT,                                                             \n")
//			.append("    CANCEL_DT, EAI_STAT, EAI_CDATE,                                                        \n")
//			.append("    EAI_UDATE, ERP_EVIDENCE_CODE, VAT_GUB_CODE,                                            \n")
//			.append("    RECEIPT_GUB_CODE, REGIST_ID, BELNR,                                                    \n")
//			.append("    ASP_ISSUE_ID)                                                                          \n")
//            .append("    SELECT /*+ FULL(A) PARALLEL(A 16) */                                                   \n")
//            .append("    (?, JOB_GUB_CODE, MANAGE_ID,                                               \n")    
//            .append("    ADD_TAX_YN, ISSUE_ID, ISSUE_DAY,                                                       \n")
//			.append("    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE,                                          \n")
//			.append("    UPPER_MANAGE_ID, DESCRIPTION, INVOICER_PARTY_ID,                                       \n")
//			.append("    INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME, INVOICER_CEO_NAME,                        \n")
//			.append("    INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                          \n")
//			.append("    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, INVOICER_CONTACT_PHONE,                \n")
//			.append("    INVOICER_CONTACT_EMAIL, INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID,                \n")
//			.append("    INVOICEE_TAX_REGIST_ID, INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME,                        \n") 
//			.append("    INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                          \n")
//			.append("    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1,             \n")
//			.append("    INVOICEE_CONTACT_EMAIL1, INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,             \n") 
//			.append("    INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2, PAYMENT_TYPE_CODE1,                  \n")
//			.append("    PAY_AMOUNT1, PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                          \n")
//			.append("    PAYMENT_TYPE_CODE3, PAY_AMOUNT3, PAYMENT_TYPE_CODE4,                                   \n")
//			.append("    PAY_AMOUNT4, CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,                                    \n")
//			.append("    GRAND_TOTAL_AMOUNT, CUST_NO, BILL_YYYYMM,                                              \n")
//			.append("    BILL_ISSUE_YYYYMM, PAY_DEADLINE, ONLINE_GUB_CODE,                                      \n")
//			.append("    FLG, SYSDATE, SYSDATE,                                                                 \n")
//			.append("    CANCEL_DT, NULL, NULL,                                                                 \n")
//			.append("    NULL, ERP_EVIDENCE_CODE, VAT_GUB_CODE,                                                 \n")
//			.append("    RECEIPT_GUB_CODE, REGIST_ID, BELNR,                                                    \n")
//			.append("    ASP_ISSUE_ID)                                                                          \n")
//            .append("    FROM IF_TAX_BILL_INFO_TEMP  A                                                          \n")
//            .append("    WHERE REL_SYSTEM_ID = 'K1NCIS100o' AND JOB_GUB_CODE='350010'                           \n");//공가 매출만 해당됨	
//	
//            CommUtil.logWriter(sb.toString(),1);
//            pstm = con.prepareStatement(sb.toString());
//			pstm.setString(1, rel_system_id);
//            if(pstm.executeUpdate()>0){
//                bl = true;
//            }
//	}catch(SQLException e){
//        CommUtil.logWriter(e.toString(),4);
//	    throw e;
//	}catch(Exception e1){
//        CommUtil.logWriter(e1.toString(),4);
//	    throw e1;
//	}finally{
//            try{
//            	if (pstm != null){
//            	    pstm.close();
//            	}
//            }catch (SQLException e){
//                CommUtil.logWriter(e.toString(),4);
//            	e.printStackTrace();
//            }
//	}
//	return bl;
//    }

    
    /**
     * 공가시스템에서 들어온 IF_TAX_BILL_INFO 정보를 ERP FI에 연계 하기 위해 
     * ERP FI 연계에 맞게 추가로 IF_TAX_BILL_INFO INSERT
     * 2013. 11. 11 빼빼로DAY
     */     
    public boolean item_list_trans(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = true;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_TRADE_ITEM_LIST                                                           \n")
            .append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO, PURCHASE_DAY, ITEM_NAME, ITEM_INFO,      \n")
            .append("    ITEM_DESC, UNIT_QUANTITY,UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT)                    \n")
            .append("SELECT                                                                                   \n")
            .append("    '1',B.ISSUE_DAY,B.BIZ_MANAGE_ID, A.SEQ_NO, A.PURCHASE_DAY, A.ITEM_NAME, A.ITEM_INFO, \n")
            .append("    A.ITEM_DESC, A.UNIT_QUANTITY,A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT           \n")
            .append("FROM IF_TAX_BILL_ITEM_LIST A,TB_TAX_BILL_INFO B ,IF_TAX_BILL_INFO_TEMP C                 \n")
            .append("WHERE A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                  \n")
            .append("AND   A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                    \n")
            .append("AND   A.MANAGE_ID = B.SVC_MANAGE_ID                                                      \n")
            //.append("AND   B.ONLINE_GUB_CODE = '1'                                                            \n")
            .append("AND   B.ONLINE_GUB_CODE IN ('1','3')                                                     \n")
            .append("AND   B.IO_CODE = '1'                                                                    \n")
            .append("AND   B.JOB_GUB_CODE = C.JOB_GUB_CODE                                                    \n")
            .append("AND   B.REL_SYSTEM_ID = C.REL_SYSTEM_ID                                                  \n")
            .append("AND   B.ISSUE_ID = C.ISSUE_ID                                                            \n")
            .append("AND   B.ISSUE_DAY = C.ISSUE_DAY                                                          \n")
            .append("AND   B.SVC_MANAGE_ID = C.MANAGE_ID                                                      \n");
            
            CommUtil.logWriter(sb.toString(),1);
  //          System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            CommUtil.logWriter(e.toString(),4);
            throw e;
        }catch(Exception e1){
            bl = false;
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
	return bl;
    }
    

//    public boolean item_list_copy4FI(Connection con, String rel_system_id)throws SQLException, Exception{
//    	PreparedStatement pstm=null;
//    	boolean bl = true;
//    	try{
//                StringBuffer sb= new StringBuffer()
//                .append("INSERT INTO IF_TAX_BILL_ITEM_LIST                                                        \n")
//				.append("    (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID,                                             \n")
//				.append("    SEQ_NO, PURCHASE_DAY, ITEM_NAME,                                                     \n")
//				.append("    ITEM_INFO, ITEM_DESC, UNIT_QUANTITY,                                                 \n")
//				.append("    UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT,                                             \n")
//				.append("    BILL_ISSUE_YYYYMM)                                                                   \n")
//                .append("SELECT                                                                                   \n")
//				.append("    (?, JOB_GUB_CODE, MANAGE_ID,                                                         \n")
//				.append("    SEQ_NO, PURCHASE_DAY, ITEM_NAME,                                                     \n")
//				.append("    ITEM_INFO, ITEM_DESC, UNIT_QUANTITY,                                                 \n")
//				.append("    UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT,                                             \n")
//				.append("    BILL_ISSUE_YYYYMM)                                                                   \n")
//                .append("FROM IF_TAX_BILL_ITEM_LIST A, IF_TAX_BILL_INFO_TEMP C                                    \n")
//                .append("WHERE A.REL_SYSTEM_ID = C.REL_SYSTEM_ID                                                  \n")
//                .append("AND   A.JOB_GUB_CODE = C.JOB_GUB_CODE                                                    \n")
//                .append("AND   A.MANAGE_ID = C.MANAGE_ID                                                          \n")
//                .append("AND   A.REL_SYSTEM_ID = 'K1NCIS100o'                                                     \n")//공가 매출만 해당됨	
//                .append("AND   A.JOB_GUB_CODE='350010'                                                            \n");//공가 매출만 해당됨	
//                
//                CommUtil.logWriter(sb.toString(),1);
//                
//                pstm = con.prepareStatement(sb.toString());
//    			pstm.setString(1, rel_system_id);
//                pstm.executeUpdate();
//            }catch(SQLException e){
//                bl = false;
//                CommUtil.logWriter(e.toString(),4);
//                throw e;
//            }catch(Exception e1){
//                bl = false;
//                CommUtil.logWriter(e1.toString(),4);
//        	    throw e1;
//        	}finally{
//                try{
//                	if (pstm != null){
//                	    pstm.close();
//                	}
//                }catch (SQLException e){
//                    CommUtil.logWriter(e.toString(),4);
//                	e.printStackTrace();
//                }
//        	}
//    	return bl;
//        }

    
    /**
     * IF_TAX_BILL_RESULT_INFO에 접수 결과 반영.
     * @param Connection
     */ 
    public boolean tax_result_info_insert(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = false;
	try{
			
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO                                                                           \n")
            .append("    (JOB_GUB_CODE, REL_SYSTEM_ID, MANAGE_ID, STATUS_CODE,                                                     \n")
            .append("    STATUS_DESC, REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT)                                                    \n")
            .append("SELECT /*+ FULL(A) PARALLEL(A 16) */                                                                          \n")
 		    // 제   목 : 공가 매출전자세금계산서연계 관련
		    // 수정일 : 2013.11.11 빼빼로DAY
            //.append("    A.JOB_GUB_CODE, A.REL_SYSTEM_ID, A.MANAGE_ID, '01',                                                       \n")
            //.append("    A.JOB_GUB_CODE, A.REL_SYSTEM_ID, A.MANAGE_ID, DECODE( A.JOB_GUB_CODE, '350010', '00', '01'),                                                       \n")
            //2018.02.22  유종일  PPA코드  (350020) 추가  , 공가매출과 동일하게 처리
			//2021.02.24  EVC (350040) 추가 , jar 파일 생성
            .append("    A.JOB_GUB_CODE, A.REL_SYSTEM_ID, A.MANAGE_ID, DECODE( A.JOB_GUB_CODE, '350010', '00','350020', '00','350040','00', '01'),                                                       \n")
            //.append("    '접수및작성완료',A.REGIST_DT, A.MODIFY_DT, A.ISSUE_ID,DECODE(A.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X')    \n")
            .append("    '접수및작성완료',A.REGIST_DT, A.MODIFY_DT, A.ISSUE_ID, DECODE(A.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(A.ONLINE_GUB_CODE,'3',NULL), 'X')    \n")
            .append("FROM IF_TAX_BILL_INFO_TEMP A                                                                                  \n");

     //       System.out.println(sb.toString());
            
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
        }catch(SQLException e){
            CommUtil.logWriter(e.toString(),4);
           throw e;
        }catch(Exception e1){
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
             	e.printStackTrace();
            }
    	}
	return bl;
    }
    
    

    /**
     * TB_STATUS_HIST 접수 결과 반영.
     * @param Connection
     */ 
    public boolean tax_hist_info_insert(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = false;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_STATUS_HIST                                                                          \n")
            .append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, AVL_BEGIN_DT,AVL_END_DT,                                    \n")
            .append("     SEQ_NO, STATUS_CODE, REGIST_DT, REGIST_ID, STATUS_DESC)                                        \n")
            .append("SELECT /*+ ORDERED FULL(B) PARALLEL(B 16) */                                                        \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), '99991231235959', \n")
 		    // 제   목 : 공가 매출전자세금계산서연계 관련
		    // 수정일 : 2013.11.11 빼빼로DAY
            //.append("    1, '01', A.REGIST_DT, 'BATCH' ,                                                                 \n")
            //.append("    1, DECODE( A.JOB_GUB_CODE, '350010', '00', '01'), A.REGIST_DT, 'BATCH' ,                                                                 \n")
            //2018.02.22  유종일  PPA코드  (350020) 추가  , 공가매출과 동일하게 처리
            .append("    1, DECODE( A.JOB_GUB_CODE, '350010', '00','350020', '00','350040','00', '01'), A.REGIST_DT, 'BATCH' ,                                                                 \n")
            .append("    (SELECT CODE_VALUE FROM TB_CODE_INFO                                                            \n")
            .append("        WHERE CODE='01'                                                                             \n")
            .append("        AND CODE_GRP_ID = 'STATUS_CODE')                                                            \n")
            .append("FROM IF_TAX_BILL_INFO_TEMP B, TB_TAX_BILL_INFO A                                                    \n")
            .append("WHERE  1> 0                                                                                         \n")
            .append("AND A.ISSUE_ID = B.ISSUE_ID                                                                         \n");
            
            CommUtil.logWriter(sb.toString(),1);
          //  System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
        }catch(SQLException e){
            CommUtil.logWriter(e.toString(),4);
            throw e;
        }catch(Exception e1){
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
	return bl;
    }
        
    
    public boolean tax_hist_info_update(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = false;
	try{
            StringBuffer sb= new StringBuffer()
            .append("UPDATE IF_TAX_BILL_RESULT_INFO SET EAI_STAT = 'X'       \n")
            .append("WHERE REL_SYSTEM_ID LIKE 'K1NCIS100%'                   \n");
//            .append("AND STATUS_CODE = '01'                                  \n");
            
            CommUtil.logWriter(sb.toString(),1);
      //      System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
        }catch(SQLException e){
            CommUtil.logWriter(e.toString(),4);
            throw e;
        }catch(Exception e1){
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
	return bl;
    }
        
    

    
 /**
  * 현재월 9일부터는 승인 없이 전월 등록된 세금계선서 국세청 전송
  * @param con
  * @return
  * @throws SQLException
  * @throws Exception
  */   
    public boolean tax_info_etax_trans(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO ET_TID_MAIN(                                                                                           \n")
            .append("TID_ISSUE_ID,TID_SEQ_NUM,REG_STATUS,REG_ERROR_REASON,ECH_DOC_ID,ECH_DOC_REF_ID,ECH_ISSUE_DT,                       \n")
            .append("TID_ISSUE_DT,TID_TYPE_CODE,TID_PUR_CODE,TID_AMD_STATUS,TID_DESC,TID_IMPD_ID,                                       \n")
            .append("TID_IMPD_START,TID_IMPD_END,TID_IMPD_QUANTITY,                                                                     \n")
            .append("INVOICER_ID,INVOICER_REG_ID,INVOICER_NAME,INVOICER_SP_NAME,                                                        \n")
            .append("INVOICER_ADDRESS,INVOICER_TYPE_CODE,INVOICER_CLASS_CODE,                                                           \n")
            .append("INVOICER_CONTACT_DEP,INVOICER_CONTACT_PERSON,INVOICER_CONTACT_TEL,INVOICER_CONTACT_EMAIL,                          \n")
            .append("INVOICEE_ID,INVOICEE_REG_ID,INVOICEE_BIZ_CODE,                                                                     \n")
            .append("INVOICEE_NAME,INVOICEE_SP_NAME,INVOICEE_ADDRESS,INVOICEE_TYPE_CODE,INVOICEE_CLASS_CODE,                            \n")
            .append("INVOICEE_CONTACT_DEP_1,INVOICEE_CONTACT_PERSON_1,INVOICEE_CONTACT_TEL_1,INVOICEE_CONTACT_EMAIL_1,                  \n")
            .append("INVOICEE_CONTACT_DEP_2,INVOICEE_CONTACT_PERSON_2,INVOICEE_CONTACT_TEL_2,INVOICEE_CONTACT_EMAIL_2,                  \n")
            .append("BROKER_ID,BROKER_REG_ID,BROKER_NAME,BROKER_SP_NAME,BROKER_ADDRESS,BROKER_TYPE_CODE,BROKER_CLASS_CODE,              \n")
            .append("BROKER_CONTACT_DEP,BROKER_CONTACT_PERSON,BROKER_CONTACT_TEL,BROKER_CONTACT_EMAIL,                                  \n")
            .append("SP_PAID_AMOUNT_1,SP_PAID_AMOUNT_2,SP_PAID_AMOUNT_3,SP_PAID_AMOUNT_4,                                               \n")
            .append("MP_CHG_TOTAL_AMOUNT,MP_TAX_TOTAL_AMOUNT,MP_GD_TOTAL_AMOUNT)                                                        \n")
            .append("SELECT                                                                                                             \n")
            // 2011. 11. 30 세금계산서 신고 마감 변경 적용 으로
            // 세금계산서 생성시간을 전송전으로 변경
            //.append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, ISSUE_DT,                                                      \n")
            .append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),                                                      \n")
            .append("ISSUE_DAY,    BILL_TYPE_CODE,PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,IMPORT_DOC_ID,                              \n")
            .append("IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                              \n")
            .append("INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                                   \n")
            .append("INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                                      \n")
            .append("INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,                     \n")
            .append("INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,INVOICEE_BUSINESS_TYPE_CODE,                                             \n")
            .append("INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                              \n")
            .append("INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                \n")
            .append("INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                \n")
            .append("BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME, BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS, \n")
            .append("BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME, BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                            \n")
            .append("PAY_AMOUNT1, PAY_AMOUNT2, PAY_AMOUNT3, PAY_AMOUNT4,                                                                \n")
            .append("CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT                                                           \n")
            .append("FROM TB_TAX_BILL_INFO                                                                                              \n")
            .append("WHERE ONLINE_GUB_CODE = '1'                                                                                        \n")	
            //2011.2.25 수정세금계산서 날짜체크해제로 수정 KDY
            //.append("AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD')    \n")
            .append("AND STATUS_CODE = '01'                                                                                             \n")
            .append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')             \n")
            .append("AND IO_CODE = '1'                                                                                                  \n");
            
            CommUtil.logWriter(sb.toString(),1);
         //   System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    e.printStackTrace();
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    e1.printStackTrace();
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    }
  
    //승인 정보가 있는 전월 데이터 국세청 전송 
    public boolean tax_info_etax_trans02(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO ET_TID_MAIN(                                                                                           \n")
            .append("TID_ISSUE_ID,TID_SEQ_NUM,REG_STATUS,REG_ERROR_REASON,ECH_DOC_ID,ECH_DOC_REF_ID,ECH_ISSUE_DT,                       \n")
            .append("TID_ISSUE_DT,TID_TYPE_CODE,TID_PUR_CODE,TID_AMD_STATUS,TID_DESC,TID_IMPD_ID,                                       \n")
            .append("TID_IMPD_START,TID_IMPD_END,TID_IMPD_QUANTITY,                                                                     \n")
            .append("INVOICER_ID,INVOICER_REG_ID,INVOICER_NAME,INVOICER_SP_NAME,                                                        \n")
            .append("INVOICER_ADDRESS,INVOICER_TYPE_CODE,INVOICER_CLASS_CODE,                                                           \n")
            .append("INVOICER_CONTACT_DEP,INVOICER_CONTACT_PERSON,INVOICER_CONTACT_TEL,INVOICER_CONTACT_EMAIL,                          \n")
            .append("INVOICEE_ID,INVOICEE_REG_ID,INVOICEE_BIZ_CODE,                                                                     \n")
            .append("INVOICEE_NAME,INVOICEE_SP_NAME,INVOICEE_ADDRESS,INVOICEE_TYPE_CODE,INVOICEE_CLASS_CODE,                            \n")
            .append("INVOICEE_CONTACT_DEP_1,INVOICEE_CONTACT_PERSON_1,INVOICEE_CONTACT_TEL_1,INVOICEE_CONTACT_EMAIL_1,                  \n")
            .append("INVOICEE_CONTACT_DEP_2,INVOICEE_CONTACT_PERSON_2,INVOICEE_CONTACT_TEL_2,INVOICEE_CONTACT_EMAIL_2,                  \n")
            .append("BROKER_ID,BROKER_REG_ID,BROKER_NAME,BROKER_SP_NAME,BROKER_ADDRESS,BROKER_TYPE_CODE,BROKER_CLASS_CODE,              \n")
            .append("BROKER_CONTACT_DEP,BROKER_CONTACT_PERSON,BROKER_CONTACT_TEL,BROKER_CONTACT_EMAIL,                                  \n")
            .append("SP_PAID_AMOUNT_1,SP_PAID_AMOUNT_2,SP_PAID_AMOUNT_3,SP_PAID_AMOUNT_4,                                               \n")
            .append("MP_CHG_TOTAL_AMOUNT,MP_TAX_TOTAL_AMOUNT,MP_GD_TOTAL_AMOUNT)                                                        \n")
            .append("SELECT                                                                                                             \n")
            // 2011. 11. 30 세금계산서 신고 마감 변경 적용 으로
            // 세금계산서 생성시간을 전송전으로 변경
            //.append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, ISSUE_DT,                                                      \n")
            .append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),                                                      \n")
            .append("ISSUE_DAY,    BILL_TYPE_CODE,PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,IMPORT_DOC_ID,                              \n")
            .append("IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                              \n")
            .append("INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                                   \n")
            .append("INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                                      \n")
            .append("INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,                     \n")
            .append("INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,INVOICEE_BUSINESS_TYPE_CODE,                                             \n")
            .append("INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                              \n")
            .append("INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                \n")
            .append("INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                \n")
            .append("BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME, BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS, \n")
            .append("BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME, BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                            \n")
            .append("PAY_AMOUNT1, PAY_AMOUNT2, PAY_AMOUNT3, PAY_AMOUNT4,                                                                \n")
            .append("CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT                                                           \n")
            .append("FROM TB_TAX_BILL_INFO                                                                                              \n")
            //.append("WHERE (ONLINE_GUB_CODE = '1' OR ONLINE_GUB_CODE = '3')--20101011  영업                                                                                     \n")	
            //2011.2.25 수정세금계산서 날짜체크해제로 수정 KDY
            //.append("AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD') \n")	
            //.append("AND (STATUS_CODE = '02' OR STATUS_CODE = '06') --20100616 or 06 추가함   \n")
            //.append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')             \n")//계산서는 제외 즉, 세금계산서만 전송
            //.append("AND IO_CODE = '1'                                                                                                  \n");
            .append("WHERE (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID) 																			\n")
            .append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)							    			\n");            
            CommUtil.logWriter(sb.toString(),1);
         //   System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    e.printStackTrace();
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    e1.printStackTrace();
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    }  
    
    
	public List tax_info_etax_trans_new02(Connection conn) throws SQLException{
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TbTaxBillInfoDao tbTaxbilldao = new TbTaxBillInfoDao();
        StringBuffer sb= new StringBuffer()
		.append("SELECT 				                                                                                    \n")
		.append("BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,					                    \n")
		.append("BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,									                \n")
		.append("IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,			            \n")
		.append("INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,						                    \n")
		.append("INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,						                    \n")
		.append("INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,								                            \n")
		.append("INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,								                            \n")
		.append("INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,					                \n")
		.append("INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,		                \n")
		.append("INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,         \n")
		.append("INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,         \n")
		.append("BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,					                                \n")
		.append("BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,					                                \n")
		.append("BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,								                                \n")
		.append("BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,								                                \n")
		.append("PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,			                                \n")
		.append("PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,			                                \n")
		.append("CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,					                                \n")
		.append("STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID, 									                                \n")
		.append("ELECTRONIC_REPORT_YN,REGIST_DT, MODIFY_DT, REGIST_ID,MODIFY_ID, UPPER_MANAGE_ID                            \n")
        .append("FROM TB_TAX_BILL_INFO                                                                                      \n")
        //.append("WHERE (ONLINE_GUB_CODE = '1' OR ONLINE_GUB_CODE = '3')--20101011  영업                                                                         \n")	
        //2011.2.25 수정세금계산서 날짜체크해제로 수정 KDY
        //.append("AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD') \n")	
        //.append("AND (STATUS_CODE = '02' OR STATUS_CODE = '06') --20100616 or 06 추가함   \n")
        //.append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')             \n")//계산서는 제외 즉, 세금계산서만 전송
        //.append("AND IO_CODE = '1'                                                                                                  \n");
        .append("WHERE (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID) 																			\n")
        .append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)							    			\n");            
        CommUtil.logWriter(sb.toString(),1);
     //   System.out.println(sb.toString());
	    List tbTaxBillList = null;
		try{
			pstmt = conn.prepareStatement(sb.toString());
			//pstmt.setString(1, rowId);

			rs = pstmt.executeQuery();
			
			tbTaxBillList = new ArrayList();
			
			while(rs.next()){
				tbTaxBillList.add(tbTaxbilldao.makeTbTaxBillPb(rs));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return tbTaxBillList;
	}

	public List tax_info_item_trans_new02(Connection conn, TbTaxBillInfoVo taxBillInfo) throws SQLException{
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//TbTaxBillInfoDao tbTaxbilldao = new TbTaxBillInfoDao();
		TbTradeItemListDao tbItemListdao = new TbTradeItemListDao(); 
        StringBuffer sb= new StringBuffer()
			.append("SELECT /*+ INDEX(A TB_TRADE_ITEM_LIST_PK) no_index(b) */            \n")
			.append("A.BIZ_MANAGE_ID, A.SEQ_NO, A.PURCHASE_DAY, A.ITEM_NAME, A.ITEM_INFO,\n")
			.append("A.ITEM_DESC, A.UNIT_QUANTITY,                                       \n")
			.append("A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT                       \n")
            .append("FROM TB_TRADE_ITEM_LIST  A, TB_TAX_BILL_ON_BATCH B                  \n")
            .append("WHERE  A.IO_CODE = B.IO_CODE     								   	 \n")
            .append("AND A.ISSUE_DAY = B.ISSUE_DAY    									 \n")
            .append("AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID 								 \n")
	        .append("AND A.IO_CODE = ?             									     \n")
	        .append("AND A.ISSUE_DAY = ?    	  								         \n")
	        .append("AND A.BIZ_MANAGE_ID = ? 								             \n");
        CommUtil.logWriter(sb.toString(),1);
     //   System.out.println(sb.toString());
        List tbTradeItemList =  null;
		try{
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, taxBillInfo.getIo_code());
			pstmt.setString(2, taxBillInfo.getIssue_day());
			pstmt.setString(3, taxBillInfo.getBiz_manage_id());

			rs = pstmt.executeQuery();
			
			tbTradeItemList = new ArrayList();
			
			while(rs.next()){
				tbTradeItemList.add(tbItemListdao.makeTbTradeItemPb(rs));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return tbTradeItemList;
	}
	
    public boolean tax_info_etax_trans_now(Connection con)throws SQLException, Exception {
	boolean bl = false;
	PreparedStatement pstm=null;
	
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO ET_TID_MAIN(                                                                                           \n")
            .append("TID_ISSUE_ID,TID_SEQ_NUM,REG_STATUS,REG_ERROR_REASON,ECH_DOC_ID,ECH_DOC_REF_ID,ECH_ISSUE_DT,                       \n")
            .append("TID_ISSUE_DT,TID_TYPE_CODE,TID_PUR_CODE,TID_AMD_STATUS,TID_DESC,TID_IMPD_ID,                                       \n")
            .append("TID_IMPD_START,TID_IMPD_END,TID_IMPD_QUANTITY,                                                                     \n")
            .append("INVOICER_ID,INVOICER_REG_ID,INVOICER_NAME,INVOICER_SP_NAME,                                                        \n")
            .append("INVOICER_ADDRESS,INVOICER_TYPE_CODE,INVOICER_CLASS_CODE,                                                           \n")
            .append("INVOICER_CONTACT_DEP,INVOICER_CONTACT_PERSON,INVOICER_CONTACT_TEL,INVOICER_CONTACT_EMAIL,                          \n")
            .append("INVOICEE_ID,INVOICEE_REG_ID,INVOICEE_BIZ_CODE,                                                                     \n")
            .append("INVOICEE_NAME,INVOICEE_SP_NAME,INVOICEE_ADDRESS,INVOICEE_TYPE_CODE,INVOICEE_CLASS_CODE,                            \n")
            .append("INVOICEE_CONTACT_DEP_1,INVOICEE_CONTACT_PERSON_1,INVOICEE_CONTACT_TEL_1,INVOICEE_CONTACT_EMAIL_1,                  \n")
            .append("INVOICEE_CONTACT_DEP_2,INVOICEE_CONTACT_PERSON_2,INVOICEE_CONTACT_TEL_2,INVOICEE_CONTACT_EMAIL_2,                  \n")
            .append("BROKER_ID,BROKER_REG_ID,BROKER_NAME,BROKER_SP_NAME,BROKER_ADDRESS,BROKER_TYPE_CODE,BROKER_CLASS_CODE,              \n")
            .append("BROKER_CONTACT_DEP,BROKER_CONTACT_PERSON,BROKER_CONTACT_TEL,BROKER_CONTACT_EMAIL,                                  \n")
            .append("SP_PAID_AMOUNT_1,SP_PAID_AMOUNT_2,SP_PAID_AMOUNT_3,SP_PAID_AMOUNT_4,                                               \n")
            .append("MP_CHG_TOTAL_AMOUNT,MP_TAX_TOTAL_AMOUNT,MP_GD_TOTAL_AMOUNT)                                                        \n")
            .append("SELECT                                                                                                             \n")
            // 2011. 11. 30 세금계산서 신고 마감 변경 적용 으로
            // 세금계산서 생성시간을 전송전으로 변경
            //.append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, ISSUE_DT,                                                      \n")
            .append("ISSUE_ID,'1', '0', '', SVC_MANAGE_ID,BIZ_MANAGE_ID, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),                           \n")            
            .append("ISSUE_DAY,    BILL_TYPE_CODE,PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,IMPORT_DOC_ID,                              \n")
            .append("IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                              \n")
            .append("INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                                   \n")
            .append("INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                                      \n")
            .append("INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME, INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,                     \n")
            .append("INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,INVOICEE_BUSINESS_TYPE_CODE,                                             \n")
            .append("INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                              \n")
            .append("INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                \n")
            .append("INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                \n")
            .append("BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME, BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS, \n")
            .append("BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME, BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                            \n")
            .append("PAY_AMOUNT1, PAY_AMOUNT2, PAY_AMOUNT3, PAY_AMOUNT4,                                                                \n")
            .append("CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT                                                           \n")
            .append("FROM TB_TAX_BILL_INFO                                                                                              \n")
            //.append("WHERE ONLINE_GUB_CODE = '1'                                                                                        \n")	
            .append("WHERE (ONLINE_GUB_CODE = '1' OR ONLINE_GUB_CODE = '3') --20101013                                                  \n")	
            .append("AND ISSUE_DAY BETWEEN TO_CHAR(SYSDATE,'YYYYMM')||'01' AND TO_CHAR(SYSDATE,'YYYYMMDD')                  \n")	
            .append("AND (STATUS_CODE = '02' OR STATUS_CODE = '06') --20100616 or 06 추가함                                                                                            \n")
            .append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')             \n")//계산서는 제외 즉, 세금계산서만 전송
            .append("AND IO_CODE = '1'                                                                                                  \n");
            
            CommUtil.logWriter(sb.toString(),1);
         //   System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
	}catch(SQLException e){
        CommUtil.logWriter(e.toString(),4);
	    e.printStackTrace();
	    throw e;
	}catch(Exception e1){
        CommUtil.logWriter(e1.toString(),4);
	    e1.printStackTrace();
	    throw e1;
	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
	}
	return bl;
    }     
    
    
    /**
     * TB_TRADE_ITEM_LIST 에서 ET_TID_ITEM로 DATA 전송.
     * @param Connection
     */ 
    public boolean item_etax_trans(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO ET_TID_ITEM(                                                                                 \n")
            .append("TID_ISSUE_ID, TID_SEQ_NUM, ITEM_SEQ_NUM, EXPIRY_DT, ITEM_NAME,                                           \n")
            .append("ITEM_INF, ITEM_DESC, CHARGE_QUANTITY, UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT)                           \n")
            .append("SELECT                                                                                                   \n")
            .append("B.ISSUE_ID, '1',A.SEQ_NO, A.PURCHASE_DAY,A.ITEM_NAME,                                                    \n")
            .append("A.ITEM_INFO, A.ITEM_DESC,A.UNIT_QUANTITY,A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT                   \n")
            .append("FROM TB_TRADE_ITEM_LIST  A, TB_TAX_BILL_INFO B                                                           \n")
            .append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
            .append("AND A.IO_CODE = B.IO_CODE                                                                                \n")
            .append("AND B.IO_CODE = '1'                                                                                      \n")
            .append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                                            \n")
            .append("AND B.STATUS_CODE = '01'                                                                                 \n")
            .append("AND B.ONLINE_GUB_CODE = '1'                                                                              \n")
            .append("AND B.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")//계산서는 제외 즉, 세금계산서만 전송
            .append("AND B.ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD')\n");
            
            CommUtil.logWriter(sb.toString(),1);
        //    System.out.println(sb.toString());		
            
            pstm = con.prepareStatement(sb.toString());
            
            if(pstm.executeUpdate()>0){
                bl = true;
            }
        }catch(SQLException e){
            bl = false;
            CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
            CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }   
   
  
 public boolean item_etax_trans02(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO ET_TID_ITEM(                                                                                 \n")
            .append("TID_ISSUE_ID, TID_SEQ_NUM, ITEM_SEQ_NUM, EXPIRY_DT, ITEM_NAME,                                           \n")
            .append("ITEM_INF, ITEM_DESC, CHARGE_QUANTITY, UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT)                           \n")
            .append("SELECT                                                                                                   \n")
            .append("B.ISSUE_ID, '1',A.SEQ_NO, A.PURCHASE_DAY,A.ITEM_NAME,                                                    \n")
            .append("A.ITEM_INFO, A.ITEM_DESC,A.UNIT_QUANTITY,A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT                   \n")
            .append("FROM TB_TRADE_ITEM_LIST  A, TB_TAX_BILL_ON_BATCH B  									  	 			  \n")
            .append("WHERE  A.IO_CODE = B.IO_CODE     																		  \n")
            .append("AND A.ISSUE_DAY = B.ISSUE_DAY    																		  \n")
            .append("AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID 																	  \n");
            //.append("FROM TB_TRADE_ITEM_LIST  A, TB_TAX_BILL_INFO B                                                           \n")
            //.append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
            //.append("AND A.IO_CODE = B.IO_CODE                                                                                \n")
            //.append("AND B.IO_CODE = '1'                                                                                      \n")
            //.append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                                            \n")
            //.append("AND (B.STATUS_CODE = '02' OR B.STATUS_CODE = '06') --20100616 or 06 추가함								\n")
            //2011.2.25 수정세금계산서 날짜체크해제로 수정 KDY
            //.append("AND B.ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD') \n")
            //.append("AND B.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")//계산서는 제외 즉, 세금계산서만 전송
            //.append("AND B.ONLINE_GUB_CODE = '1'                                                                               \n");
            //.append("AND (B.ONLINE_GUB_CODE = '1' OR B.ONLINE_GUB_CODE = '3')      -- 20101011 영업정보 추가                                                                        \n");
            
            CommUtil.logWriter(sb.toString(),1);
        //    System.out.println(sb.toString());		
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            
            if(rs>0){
        	bl=true;
            }
            
        }catch(SQLException e){
            bl = false;
            CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
            CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }  
    

 
 /**
  * 현재월 승인된 아이템 정보 국세청 전송
  * @param con
  * @return
  * @throws SQLException
  * @throws Exception
  */
 public boolean item_etax_trans_now(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	int rs = 0;
	try{
         StringBuffer sb= new StringBuffer()
         .append("INSERT INTO ET_TID_ITEM(                                                                                 \n")
         .append("TID_ISSUE_ID, TID_SEQ_NUM, ITEM_SEQ_NUM, EXPIRY_DT, ITEM_NAME,                                           \n")
         .append("ITEM_INF, ITEM_DESC, CHARGE_QUANTITY, UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT)                           \n")
         .append("SELECT /*+ ORDERED USE_NL(B A) INDEX(B TB_TAX_BILL_INFO_IX1) */                                          \n")
         .append("B.ISSUE_ID, '1',A.SEQ_NO, A.PURCHASE_DAY,A.ITEM_NAME,                                                    \n")
         .append("A.ITEM_INFO, A.ITEM_DESC,A.UNIT_QUANTITY,A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT                   \n")
         .append("FROM TB_TAX_BILL_INFO B, TB_TRADE_ITEM_LIST  A                                                           \n")
         .append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
         .append("AND A.IO_CODE = B.IO_CODE                                                                                \n")
         .append("AND B.IO_CODE = '1'                                                                                      \n")
         .append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                                            \n")
         .append("AND (B.STATUS_CODE = '02' OR STATUS_CODE = '06') --20100616 or 06 추가함                                                                                 \n")
         .append("AND B.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE,'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(SYSDATE),'YYYYMMDD')        \n")
         .append("AND B.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")
         .append("AND (B.ONLINE_GUB_CODE = '1' OR B.ONLINE_GUB_CODE = '3') --20101013                                      \n");
         
         CommUtil.logWriter(sb.toString(),1);
     //    System.out.println(sb.toString());		
         pstm = con.prepareStatement(sb.toString());
         rs = pstm.executeUpdate();
         
         if(rs>0){
     		bl=true;
         }
     }catch(SQLException e){
         bl = false;
         CommUtil.logWriter(e.toString(),4);
         e.printStackTrace();
         throw e;
     }catch(Exception e1){
         bl = false;            
         CommUtil.logWriter(e1.toString(),4);
 	    e1.printStackTrace();
 	    throw e1;
 	}finally{
         try{
         	if (pstm != null){
         	    pstm.close();
         	}
         }catch (SQLException e){
             CommUtil.logWriter(e.toString(),4);
         	e.printStackTrace();
         }
 	}
 	return bl;
 }  
 
    /**
     * IF_TAX_BILL_INFO 처리구분 업데이트
     * @param Connection
     */ 
    public boolean if_tax_bill_info_flg_update(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("UPDATE /*+ bypass_ujvc */                                \n")
            .append("(                                                        \n")
            .append("    SELECT /*+ ORDERED USE_NL(B A) */                    \n")
            .append("    A.FLG                                                \n")
            .append("    FROM  IF_TAX_BILL_INFO_TEMP B, IF_TAX_BILL_INFO A    \n")
            .append("    WHERE 1>0                                            \n")
            .append("    AND A.MANAGE_ID = B.MANAGE_ID                        \n")
            .append("    AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                \n")
            .append("    AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                  \n")
            .append(")                                                        \n")
            .append("SET FLG = 'Y'                                            \n");

            CommUtil.logWriter(sb.toString(),1);

            pstm = con.prepareStatement(sb.toString());
            
            if(pstm.executeUpdate()>0){
                bl = true;
            }

            
        }catch(SQLException e){
            bl = false;
            CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
            CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }          
    
    
    /**
     * IF_TAX_BILL_CONFIRM_INFO 메일/SMS 송신완료 업데이트
     * @param Connection
     */ 
    public boolean if_tax_bill_confirm_info_mail_flg_update(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
    StringBuffer sb= new StringBuffer()
		.append("UPDATE /*+ bypass_ujvc */                      \n")        
		.append("(                                              \n")        
		.append("    SELECT /*+ ORDERED USE_NL(B A) */          \n")        
		.append("    B.MAIL_FLG                                 \n")             
		.append("    FROM  IF_TAX_BILL_CONFIRM_INFO_TEMP A,     \n")
		.append("          IF_TAX_BILL_CONFIRM_INFO B           \n")
		.append("    WHERE A.REL_SYSTEM_ID = B.REL_SYSTEM_ID    \n")           
		.append("    AND A.JOB_GUB_CODE = B.JOB_GUB_CODE        \n")        
		.append("    AND A.MANAGE_ID = B.MANAGE_ID              \n")        
		.append(")                                              \n")        
		.append("SET MAIL_FLG = 'Y'                             \n");
	try{
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
            
        }catch(SQLException e){
            bl = false;
            CommUtil.logWriter(sb.toString(),1);
            CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
            CommUtil.logWriter(sb.toString(),1);
            CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(sb.toString(),1);
                CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }          
        
    public boolean tb_xml_info_flg_update(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	PreparedStatement pstm=null;
        StringBuffer sb= new StringBuffer()
		.append(" UPDATE /*+ bypass_ujvc */                                \n")
		.append("(                                                         \n")
		.append("    SELECT /*+ ORDERED USE_NL(B A) */                     \n")
		.append("    A.EMAILTRANS_FLG,                                     \n")
		.append("    B.EMAIL_ADDRESS                                       \n")       
		.append("    FROM  TB_XML_INFO_TEMP B, TB_XML_INFO A               \n")
		.append("    WHERE  A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID              \n")          
		.append("    )                                                     \n")   
		.append("SET EMAILTRANS_FLG = DECODE(EMAIL_ADDRESS, NULL, 'N','Y') \n");
        
    	try{
                CommUtil.logWriter(sb.toString(),1);
                pstm = con.prepareStatement(sb.toString());
                if(pstm.executeUpdate()>0){
                    bl = true;
                }
                
            }catch(SQLException e){
                bl = false;
                CommUtil.logWriter(sb.toString(),1);
                CommUtil.logWriter(e.toString(),4);
                e.printStackTrace();
                throw e;
            }catch(Exception e1){
                bl = false;            
                CommUtil.logWriter(sb.toString(),1);
                CommUtil.logWriter(e1.toString(),4);
        	    e1.printStackTrace();
        	    throw e1;
        	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter(sb.toString(),1);
                    CommUtil.logWriter(e.toString(),4);
                	e.printStackTrace();
                }
        	}
        	return bl;
        }          
            
    
    
    /**
     * 매출 세금계산서 회계년도, 전표번호 수신후 이메일, SMS 전송 대상 잡기
     * 2011. 11. 30 by mopuim
     */   
   public boolean get_sms_email2(Connection con)throws SQLException, Exception{
	boolean bl = true;
	PreparedStatement pstm=null;
	ResultSet rs = null;
	Vector vc =  new Vector();
	try{
		StringBuffer sb= new StringBuffer()
		/*.append("SELECT                                                                          \n") 
        //.append("B.IO_CODE, B.BIZ_MANAGE_ID, B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,            \n")
        .append("B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,                                    \n")
        .append("B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,                                        \n")
        .append("B.IO_CODE, B.BIZ_MANAGE_ID, B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,            \n")
        .append("B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                   \n") 
        .append("B.INVOICEE_CONTACT_PHONE1, B.INVOICEE_CONTACT_EMAIL1,B.INVOICEE_CONTACT_NAME1,  \n") 
        .append("B.INVOICEE_CONTACT_PHONE2, B.INVOICEE_CONTACT_EMAIL2,B.INVOICEE_CONTACT_NAME2,  \n") 
        .append("B.INVOICER_CONTACT_PHONE, B.INVOICER_CONTACT_EMAIL, B.INVOICER_CONTACT_NAME,    \n") 
        .append("B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT, 									 \n") 
        .append("B.GRAND_TOTAL_AMOUNT, C.ITEM_NAME, B.BILL_TYPE_CODE, B.ISSUE_DAY,    			 \n")
        .append("B.ISSUE_ID, B.ONLINE_GUB_CODE                                                   \n") 
        .append("FROM IF_TAX_BILL_CONFIRM_INFO_TEMP A ,TB_TAX_BILL_INFO B ,TB_TRADE_ITEM_LIST C  \n")        
        .append("WHERE  A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                        \n")    
        .append("AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID										  	 \n")
        .append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                             \n") 
        .append("AND A.MANAGE_ID = B.SVC_MANAGE_ID                                               \n")
        .append("AND A.ISSUE_ID = B.ISSUE_ID                                                     \n")
		.append("AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID                                           \n")
		.append("AND C.SEQ_NO = '1'                                                              \n");		*/
		
		//변경된 쿼리20180227 윤규미
		.append(" SELECT																				\n")                                                               
		.append(" B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,											\n")
		.append(" B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,												\n")
		.append(" B.IO_CODE, B.BIZ_MANAGE_ID, B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,					\n")
		.append(" B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,											\n")
		.append(" B.INVOICEE_CONTACT_PHONE1, B.INVOICEE_CONTACT_EMAIL1,B.INVOICEE_CONTACT_NAME1,		\n")
		.append(" B.INVOICEE_CONTACT_PHONE2, B.INVOICEE_CONTACT_EMAIL2,B.INVOICEE_CONTACT_NAME2,		\n")
		.append(" B.INVOICER_CONTACT_PHONE, B.INVOICER_CONTACT_EMAIL, B.INVOICER_CONTACT_NAME,			\n")
		.append(" B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT,											\n")
		.append(" B.GRAND_TOTAL_AMOUNT, C.ITEM_NAME, B.BILL_TYPE_CODE, B.ISSUE_DAY,						\n")
		.append(" B.ISSUE_ID, B.ONLINE_GUB_CODE															\n")
		.append(" FROM IF_TAX_BILL_CONFIRM_INFO_TEMP A ,TB_TAX_BILL_INFO B ,TB_TRADE_ITEM_LIST C        \n")
		.append(" WHERE  A.REL_SYSTEM_ID = B.REL_SYSTEM_ID												\n")
		.append(" AND B.IO_CODE = C.IO_CODE     							                            \n")//추가                 
		.append(" AND B.ISSUE_DAY = C.ISSUE_DAY 														\n")//추가              
		.append(" AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID													\n")
		.append(" AND A.JOB_GUB_CODE = B.JOB_GUB_CODE													\n")
		.append(" AND A.MANAGE_ID = B.SVC_MANAGE_ID														\n")
		.append(" AND A.ISSUE_ID = B.ISSUE_ID															\n")
		.append(" AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID													\n")
		.append(" AND C.SEQ_NO = '1'																	\n");

 		
		
		
        CommUtil.logWriter(sb.toString(),1);
			
		
		pstm = con.prepareStatement(sb.toString());
		rs = pstm.executeQuery();
		while(rs.next()){		//이메일 전송 대상이 있으면..
		    TbTaxBillInfoVo tbinfo = new TbTaxBillInfoVo();
		    tbinfo.setIo_code(CommUtil.SpaceChange(rs.getString("IO_CODE")));
		    tbinfo.setBiz_manage_id(CommUtil.SpaceChange(rs.getString("BIZ_MANAGE_ID")));
		    tbinfo.setSvc_manage_id(CommUtil.SpaceChange(rs.getString("SVC_MANAGE_ID")));
		    tbinfo.setInvoicee_party_name(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_NAME")));
		    tbinfo.setInvoicer_party_name(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_NAME")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicee_contact_phone1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_PHONE1")));
		    tbinfo.setInvoicee_contact_email1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_EMAIL1")));
		    tbinfo.setInvoicee_contact_name1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_NAME1")));
		    tbinfo.setInvoicee_contact_phone2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_PHONE2")));
		    tbinfo.setInvoicee_contact_email2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_EMAIL2")));
		    tbinfo.setInvoicee_contact_name2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_NAME2")));
		    tbinfo.setInvoicer_contact_phone(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_PHONE")));
		    tbinfo.setInvoicer_contact_email(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_EMAIL")));
		    tbinfo.setInvoicer_contact_name(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_NAME")));
		    tbinfo.setIssue_id2(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));	//ISSUE_ID를 암호화하여 저장한다	    
		    tbinfo.setIssue_id(CommUtil.SpaceChange(rs.getString("ISSUE_ID")));
		    tbinfo.setCharge_total_amount(rs.getLong("CHARGE_TOTAL_AMOUNT"));
		    tbinfo.setTax_total_amount(rs.getLong("TAX_TOTAL_AMOUNT"));
		    tbinfo.setGrand_total_amount(rs.getLong("GRAND_TOTAL_AMOUNT"));
		    tbinfo.setItem_list(CommUtil.SpaceChange(rs.getString("ITEM_NAME")));
		    tbinfo.setIssue_day(CommUtil.SpaceChange(rs.getString("ISSUE_DAY")));
		    tbinfo.setBill_type_code(CommUtil.SpaceChange(rs.getString("BILL_TYPE_CODE")));
		    tbinfo.setOnline_gub_code(CommUtil.SpaceChange(rs.getString("ONLINE_GUB_CODE")));
		    
		    vc.add(tbinfo);
		}
		if(vc.size()>0){
		    SmsDao sd = new SmsDao();
		    //sd.acc_sms(vc,con);
		    //20180212 윤규미 sms->lms 변경
		    sd.acc_lms(vc, con);
		    sd.acc_email(vc,con);
		}
        }catch(Exception e1){
            e1.printStackTrace();
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }

    /**
     * 매입세금계산서 2ndSign 요청 알림 메일, SMS 전송 대상 잡기
     * 메일/SMS 전송 처리 유무는 TAX_2ND_SIGN 값이 있으면 처리된것으로 함.
     * XMLEDI_USER_INFO와TP_EXUSER_TBL@SUP설정 사용
     * 2012. 06. 14 by mopuim
     */   
  // 해당 함수 사용하지 않아 혼선을 막기 위해 막음  2013. 5. 15 by mopuim
  // 주의 코멘트 헤제시 /*+ driving_site(AA) /* ==> /*+ driving_site(AA) */   변경 필수
  /*
    public boolean get_sms_email3(Connection con)throws SQLException, Exception{
	boolean bl = true;
	PreparedStatement pstm=null;
	ResultSet rs = null;
	Vector vc =  new Vector();
	//String host_ip = InetAddress.getLocalHost().getHostAddress();
	//String lnk = "@USER_LINK";
	//if("168.78.201.224".equals(host_ip))lnk = "";
	
	StringBuffer sb= new StringBuffer()
	.append("   SELECT -- 한전EDI 세금계산서 2ndSign 요청 메일/SMS 대상용                                                                                                    \n")      
	.append("           B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,                                                 \n")
    .append("           B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,                                                     \n")   
	.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                                \n") 
	.append("           B.INVOICER_CONTACT_NAME INVOICER_CONTACT_NAME,                                               \n")
	.append("           DECODE(C.TAX_SMS, 'Y', C.SMS_NUMBER, '')  INVOICER_CONTACT_PHONE,                            \n")
	.append("           DECODE(C.TAX_EMAIL, 'Y', C.EMAIL_ADDRESS, '')  INVOICER_CONTACT_EMAIL,                       \n")
	.append("           B.ISSUE_ID, B.ISSUE_DAY, B.ONLINE_GUB_CODE                                                                \n")
	.append("           FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B,                                             \n")
	.append("                     XMLEDI_USER_INFO C                                                                 \n")
	.append("           WHERE  A.IO_CODE = B.IO_CODE                                                                 \n")
	.append("           AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                        \n")
	.append("           AND A.ISSUE_ID = B.ISSUE_ID                                                                  \n")
	.append("           AND B.INVOICER_PARTY_ID = C.SUP_CODE                                                         \n")
	.append("           AND A.TAX_2ND_SIGN = 'N'                                                                     \n")
	.append("           AND B.ELECTRONIC_REPORT_YN = 'N'                                                             \n")
	.append("           AND B.TAX_2ND_SIGN IS NULL                                                                   \n")
	.append("           AND  LENGTH(B.SVC_MANAGE_ID) = 10                                                            \n")
    .append("           AND (C.TAX_EMAIL = 'Y' OR C.TAX_SMS = 'Y')                                                   \n")
    .append("          UNION ALL                                                                                     \n")
	.append("   SELECT  -- 전자세금계산서 2ndSign 요청 메일/SMS 대상용                                                                                                          \n")
	.append("           B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,                                                 \n")
    .append("           B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,                                                     \n")   
	.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                                \n")
	.append("            ( SELECT/*+ driving_site(AA) /*  CC.USER_NAME                                                                   \n")
	.append("                                 FROM TP_SUPPLIER_TBL@USER_LINK AA                                      \n")
    .append("                                    , TP_EXUSER_TBL@USER_LINK CC                                        \n")
//	.append("                                 FROM TP_SUPPLIER_TBL"+lnk+" AA                                      \n")
//  .append("                                    , TP_EXUSER_TBL"+lnk+" CC                                        \n")
	.append("                                  WHERE AA.SUPPLIER_NO = CC.SUPPLIER_NO                                 \n")
	.append("                                  AND AA.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.TAX_SMS_YN = 'Y'                                               \n")
	.append("                                  AND AA.BUSINESS_NO = ENCODE_SF@USER_LINK(B.INVOICER_PARTY_ID)         \n")
	.append("                                  AND ROWNUM = 1) INVOICER_CONTACT_NAME,                                \n")
	.append("            ( SELECT/*+ driving_site(AA) /*     CC.MOBILE                                                                      \n")
	.append("                                 FROM TP_SUPPLIER_TBL@USER_LINK AA                                      \n")
	.append("                                          , TP_EXUSER_TBL@USER_LINK CC                                  \n")
//	.append("                                 FROM TP_SUPPLIER_TBL"+lnk+" AA                                      \n")
//  .append("                                    , TP_EXUSER_TBL"+lnk+" CC                                        \n")
	.append("                                  WHERE AA.SUPPLIER_NO = CC.SUPPLIER_NO                                 \n")
	.append("                                  AND AA.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.TAX_SMS_YN = 'Y'                                               \n")
	.append("                                  AND AA.BUSINESS_NO = ENCODE_SF@USER_LINK(B.INVOICER_PARTY_ID)         \n")
	.append("                                  AND ROWNUM = 1) INVOICER_CONTACT_PHONE,                               \n")
	.append("            ( SELECT/*+ driving_site(AA) /*   CC.EMAIL                                                                      \n")
	.append("                                 FROM TP_SUPPLIER_TBL@USER_LINK AA                                      \n")
	.append("                                          , TP_EXUSER_TBL@USER_LINK CC                                  \n")
//	.append("                                 FROM TP_SUPPLIER_TBL"+lnk+" AA                                      \n")
//  .append("                                    , TP_EXUSER_TBL"+lnk+" CC                                        \n")
	.append("                                  WHERE AA.SUPPLIER_NO = CC.SUPPLIER_NO                                 \n")
	.append("                                  AND AA.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.STATUS != 'D'                                                  \n")
	.append("                                  AND CC.TAX_SMS_YN = 'Y'                                               \n")
	.append("                                  AND AA.BUSINESS_NO = ENCODE_SF@USER_LINK(B.INVOICER_PARTY_ID)         \n")
	.append("                                  AND ROWNUM = 1) INVOICER_CONTACT_EMAIL,                               \n")
	.append("           B.ISSUE_ID, B.ISSUE_DAY, B.ONLINE_GUB_CODE                                                                \n")
	.append("           FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B                                              \n")
	.append("           WHERE  A.IO_CODE = B.IO_CODE                                                                 \n")
	.append("           AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                        \n")
	.append("           AND A.ISSUE_ID = B.ISSUE_ID                                                                  \n")
	.append("           AND A.TAX_2ND_SIGN = 'N'                                                                     \n")
	.append("           AND B.ELECTRONIC_REPORT_YN = 'N'                                                             \n")
	.append("           AND B.TAX_2ND_SIGN IS NULL                                                                   \n")
	.append("           AND  LENGTH(B.SVC_MANAGE_ID) = 16                                                            \n");
		
	try{
        CommUtil.logWriter(sb.toString(),1);

        pstm = con.prepareStatement(sb.toString());
		rs = pstm.executeQuery();
		while(rs.next()){		//이메일 전송 대상이 있으면..
		    TbTaxBillInfoVo tbinfo = new TbTaxBillInfoVo();
		    tbinfo.setIo_code(CommUtil.SpaceChange(rs.getString("IO_CODE")));
		    tbinfo.setBiz_manage_id(CommUtil.SpaceChange(rs.getString("BIZ_MANAGE_ID")));
		    tbinfo.setSvc_manage_id(CommUtil.SpaceChange(rs.getString("SVC_MANAGE_ID")));
		    tbinfo.setInvoicer_party_name(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_NAME")));
		    tbinfo.setInvoicee_party_name(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_NAME")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicee_party_id(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_ID")));
		    tbinfo.setInvoicer_contact_name(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_NAME")));
		    tbinfo.setInvoicer_contact_phone(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_PHONE")));
		    tbinfo.setInvoicer_contact_email(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_EMAIL")));
		    tbinfo.setIssue_id2(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));	//ISSUE_ID를 암호화하여 저장한다	    
		    tbinfo.setIssue_id(CommUtil.SpaceChange(rs.getString("ISSUE_ID")));
		    tbinfo.setIssue_day(CommUtil.SpaceChange(rs.getString("ISSUE_DAY")));
		    
		    tbinfo.setOnline_gub_code(CommUtil.SpaceChange(rs.getString("ONLINE_GUB_CODE")));
		    
		    vc.add(tbinfo);
		} 
		if(vc.size()>0){
		    SmsDao sd = new SmsDao();
		    sd.SecondSign_sms(vc);
		    sd.SecondSign_email(vc);
		}
        }catch(Exception e1){
            CommUtil.logWriter(sb.toString(),4);
            e1.printStackTrace();
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(sb.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
*/
    /**
     * 매입세금계산서 2ndSign 요청 알림 메일, SMS 전송 대상 잡기
     * 메일/SMS 전송 처리 유무는 TAX_2ND_SIGN 값이 있으면 처리된것으로 함.
     * 한전EDI는 TB_TAX_BILL_INFO 설정 사용
     * 2012. 07. 26 by mopuim
     */   
    public boolean get_sms_email4(Connection con)throws SQLException, Exception{
	boolean bl = true;
	PreparedStatement pstm=null;
	ResultSet rs = null;
	Vector vc =  new Vector();
	StringBuffer sb= new StringBuffer();
	sb.append("   SELECT  -- 한전EDI 2ndSign 요청 메일/SMS 대상용                                                                 \n");  
	sb.append("               B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,                                                  								\n");
	sb.append(" 			  B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT, B.GRAND_TOTAL_AMOUNT, D.ITEM_NAME,    B.BILL_TYPE_CODE, 							\n");		
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_ID',B.INVOICER_PARTY_ID_ENC,'select') INVOICER_PARTY_ID,  \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICEE_PARTY_ID',B.INVOICEE_PARTY_ID_ENC,'select') INVOICEE_PARTY_ID,  \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_NAME',B.INVOICER_PARTY_NAME_ENC,'select') INVOICER_PARTY_NAME, \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICEE_PARTY_NAME',B.INVOICEE_PARTY_NAME_ENC,'select') INVOICEE_PARTY_NAME, \n");  
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("           B.INVOICER_PARTY_ID,B.INVOICEE_PARTY_ID,                                                      \n");  
    	sb.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                                 \n");
    }else{
    	sb.append("           B.INVOICER_PARTY_ID,B.INVOICEE_PARTY_ID,                                                      \n");  
    	sb.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                                 \n");
    }
	sb.append("               B.INVOICER_CONTACT_NAME INVOICER_CONTACT_NAME,                                                \n");
	sb.append("               DECODE(B.INVOICER_CONTACT_PHONE, NULL, '', B.INVOICER_CONTACT_PHONE)  INVOICER_CONTACT_PHONE, \n");                           
	sb.append("               DECODE(B.INVOICER_CONTACT_EMAIL,NULL,'', B.INVOICER_CONTACT_EMAIL)  INVOICER_CONTACT_EMAIL,   \n");                    
	sb.append("               B.ISSUE_ID, B.ISSUE_DAY, B.ONLINE_GUB_CODE,                                                   \n");           
    sb.append("               (SELECT NVL(MAX(RETURNED_DATE),'') FROM XMLEDI_TAX_RSP                                        \n");
    sb.append("                      WHERE MANAGEMENT_ID = B.SVC_MANAGE_ID AND DEL_FLAG='4') EDI_RETURNED_DATE              \n");
	if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO_ENC B, 				                   	\n");
    	sb.append(" 				   XMLEDI_TAX_MST C, TB_TRADE_ITEM_LIST D 												\n");
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B,                                       \n");
    	sb.append(" 				   XMLEDI_TAX_MST C, TB_TRADE_ITEM_LIST D 												\n");
    }else{
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B ,                                       \n");
    	sb.append(" 				   XMLEDI_TAX_MST C, TB_TRADE_ITEM_LIST D 												\n");
    }
	
	//쿼리변경  20180227 윤규미
	sb.append("           WHERE  A.IO_CODE = B.IO_CODE                                          \n");                     
	sb.append("               AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                             \n");                         
	sb.append("               AND B.BIZ_MANAGE_ID = D.BIZ_MANAGE_ID                             \n");                             
	sb.append("               AND B.ISSUE_DAY = C.BLDAT											\n"); 
	sb.append("               AND B.ISSUE_DAY = D.ISSUE_DAY										\n");
	sb.append("               AND B.IO_CODE = D.IO_CODE											\n");
	sb.append("               AND B.BIZ_MANAGE_ID = D.BIZ_MANAGE_ID								\n");
	sb.append("               AND B.SVC_MANAGE_ID = C.MANAGEMENT_ID								\n");                 
	sb.append("               AND A.ISSUE_ID = B.ISSUE_ID										\n");                     
	sb.append("               AND A.TAX_2ND_SIGN = 'N'                                          \n");                          
	sb.append("               AND B.ELECTRONIC_REPORT_YN = 'N'                                  \n");                          
	sb.append("               AND B.TAX_2ND_SIGN IS NULL                                        \n");                          
	sb.append("               AND LENGTH(B.SVC_MANAGE_ID) = 10                                  \n");                          
	sb.append("               AND  D.SEQ_NO = '1'                                               \n");                
	sb.append("               AND  C.GISUNG_DOCID IS NULL										\n"); 	
    sb.append("          UNION ALL                                                                                        \n");
	sb.append("   SELECT  -- 전자세금계산서 2ndSign 요청 메일/SMS 대상용                                                                                                          \n");
	sb.append("           B.IO_CODE, B.BIZ_MANAGE_ID, B.SVC_MANAGE_ID,                                                    \n");
	sb.append(" 			  B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT, B.GRAND_TOTAL_AMOUNT, C.ITEM_NAME,    B.BILL_TYPE_CODE, 							\n");
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_ID',B.INVOICER_PARTY_ID_ENC,'select') INVOICER_PARTY_ID,  \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICEE_PARTY_ID',B.INVOICEE_PARTY_ID_ENC,'select') INVOICEE_PARTY_ID,  \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_NAME',B.INVOICER_PARTY_NAME_ENC,'select') INVOICER_PARTY_NAME, \n");  
    	sb.append("           inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICEE_PARTY_NAME',B.INVOICEE_PARTY_NAME_ENC,'select') INVOICEE_PARTY_NAME, \n");  
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("           B.INVOICER_PARTY_ID,B.INVOICEE_PARTY_ID,                                                    \n");  
    	sb.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                               \n");
    }else{
    	sb.append("           B.INVOICER_PARTY_ID,B.INVOICEE_PARTY_ID,                                                    \n");  
    	sb.append("           B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME,                                               \n");
    }
    sb.append("            ( SELECT AA.USER_NAME                                                                          \n");
	sb.append("                     FROM TB_SRM_USER_TBL_ON_BATCH AA                                                      \n");
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("                     WHERE AA.BUSINESS_NO = inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_ID',B.INVOICER_PARTY_ID_ENC,'select') \n");
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }else{
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }
	sb.append("                     AND ROWNUM = 1) INVOICER_CONTACT_NAME,                                                \n");

    sb.append("            ( SELECT AA.MOBILE                                                                             \n");
	sb.append("                     FROM TB_SRM_USER_TBL_ON_BATCH AA                                                      \n");
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("                     WHERE AA.BUSINESS_NO = inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_ID',B.INVOICER_PARTY_ID_ENC,'select') \n");
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }else{
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }
	sb.append("                     AND ROWNUM = 1) INVOICER_CONTACT_PHONE,                                               \n");

    sb.append("            ( SELECT AA.EMAIL                                                                              \n");
	sb.append("                     FROM TB_SRM_USER_TBL_ON_BATCH AA                                                      \n");
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("                     WHERE AA.BUSINESS_NO = inisafedb.decrypt_varchar2('EXEDI.TB_TAX_BILL_INFO','INVOICER_PARTY_ID',B.INVOICER_PARTY_ID_ENC,'select') \n");
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }else{
    	sb.append("                     WHERE AA.BUSINESS_NO = B.INVOICER_PARTY_ID                                        \n");
    }
	sb.append("                     AND ROWNUM = 1) INVOICER_CONTACT_EMAIL,                                               \n");
	sb.append("           B.ISSUE_ID, B.ISSUE_DAY, B.ONLINE_GUB_CODE, '' EDI_RETURNED_DATE                                \n");
    if(RunningScheduler.DB_ENC.equals("1")){         
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO_ENC B, TB_TRADE_ITEM_LIST C                                    \n");
    }else if(RunningScheduler.DB_ENC.equals("0")){                 
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B,   TB_TRADE_ITEM_LIST C                                      \n");
    }else{
    	sb.append("               FROM TB_TAX_BILL_ON_BATCH A ,TB_TAX_BILL_INFO B ,   TB_TRADE_ITEM_LIST C                                     \n");
    	
    }
  //쿼리변경  20180227 윤규미
	sb.append("           WHERE  A.IO_CODE = B.IO_CODE                                                                    \n");
	sb.append(" 		  AND B.IO_CODE = C.IO_CODE     																  \n");   //추가
	sb.append("           AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                           \n");
	sb.append("           AND A.ISSUE_ID = B.ISSUE_ID                                                                     \n");
	sb.append("           AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID                                                       	  \n");
	sb.append(" 		  AND B.ISSUE_DAY = C.ISSUE_DAY																	  \n");//추가
	sb.append("           AND A.TAX_2ND_SIGN = 'N'                                                                        \n");
	sb.append("           AND B.ELECTRONIC_REPORT_YN = 'N'                                                                \n");
	sb.append("           AND B.TAX_2ND_SIGN IS NULL                                                                      \n");
	sb.append("           AND  LENGTH(B.SVC_MANAGE_ID) = 16                                                               \n");
	sb.append("			  	  AND  C.SEQ_NO = '1'																		  \n");

	try{
	    CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
        CommUtil.logWriter(sb.toString(),1);

        pstm = con.prepareStatement(sb.toString());
		rs = pstm.executeQuery();
		while(rs.next()){		//이메일 전송 대상이 있으면..
		    TbTaxBillInfoVo tbinfo = new TbTaxBillInfoVo();
		    tbinfo.setIo_code(CommUtil.SpaceChange(rs.getString("IO_CODE")));
		    tbinfo.setBiz_manage_id(CommUtil.SpaceChange(rs.getString("BIZ_MANAGE_ID")));
		    tbinfo.setSvc_manage_id(CommUtil.SpaceChange(rs.getString("SVC_MANAGE_ID")));
		    tbinfo.setInvoicer_party_name(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_NAME")));
		    tbinfo.setInvoicee_party_name(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_NAME")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicee_party_id(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_ID")));
		    tbinfo.setInvoicer_contact_name(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_NAME")));
		    tbinfo.setInvoicer_contact_phone(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_PHONE")));
		    tbinfo.setInvoicer_contact_email(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_EMAIL")));
		    tbinfo.setIssue_id2(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));	//ISSUE_ID를 암호화하여 저장한다	    
		    tbinfo.setIssue_id(CommUtil.SpaceChange(rs.getString("ISSUE_ID")));
		    tbinfo.setIssue_day(CommUtil.SpaceChange(rs.getString("ISSUE_DAY")));
		    tbinfo.setItem_list(CommUtil.SpaceChange(rs.getString("ITEM_NAME")));
		    tbinfo.setCharge_total_amount(rs.getLong("CHARGE_TOTAL_AMOUNT"));
		    tbinfo.setTax_total_amount(rs.getLong("TAX_TOTAL_AMOUNT"));
		    tbinfo.setGrand_total_amount(rs.getLong("GRAND_TOTAL_AMOUNT"));
		    tbinfo.setBill_type_code(CommUtil.SpaceChange(rs.getString("BILL_TYPE_CODE")));
		    tbinfo.setOnline_gub_code(CommUtil.SpaceChange(rs.getString("ONLINE_GUB_CODE")));
		    tbinfo.setEdi_returned_date(CommUtil.SpaceChange(rs.getString("EDI_RETURNED_DATE")));
		    vc.add(tbinfo);
		} 
        CommUtil.logWriter("get_sms_email4 송신대상 건수"+vc.size(),1);
		if(vc.size()>0){
		    SmsDao sd = new SmsDao();
		    //sd.SecondSign_sms(vc, con);
		  //20180212 윤규미 sms->lms 변경
		    System.out.println("------------------------------한전 매입 LMS,EMAIL Start------------------------");
		    sd.SecondSign_lms(vc, con);
		    sd.SecondSign_email(vc, con);
		    System.out.println("------------------------------한전 매입 LMS,EMAIL END------------------------");
		}
        }catch(Exception e1){
            CommUtil.logWriter(sb.toString(),4);
            e1.printStackTrace();
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
                CommUtil.logWriter(sb.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    /**
     * 매출 세금계산서 접수후 이메일 전송 대상 잡기
     * @param Connection
     */   
    public boolean get_sms_email(Connection con)throws SQLException, Exception{
	boolean bl = true;
	PreparedStatement pstm=null;
	ResultSet rs = null;
	Vector vc =  new Vector();
	try{
		StringBuffer sb= new StringBuffer()
		/*.append("SELECT                                                                         \n")    
		.append("B.IO_CODE, B.BIZ_MANAGE_ID, B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,           \n")
		.append("B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME, C.ITEM_NAME,                     \n")
		.append("B.INVOICEE_CONTACT_PHONE1, B.INVOICEE_CONTACT_EMAIL1,B.INVOICEE_CONTACT_NAME1, \n")    
		.append("B.INVOICEE_CONTACT_PHONE2, B.INVOICEE_CONTACT_EMAIL2,B.INVOICEE_CONTACT_NAME2, \n")    
		.append("B.INVOICER_CONTACT_PHONE, B.INVOICER_CONTACT_EMAIL, B.INVOICER_CONTACT_NAME,   \n")    
		.append("B.GRAND_TOTAL_AMOUNT, C.ITEM_NAME, B.BILL_TYPE_CODE,                           \n")	
		.append("B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT,                                     \n")
		.append("B.ISSUE_ID, B.ONLINE_GUB_CODE                                                  \n")    
		.append("FROM IF_TAX_BILL_INFO_TEMP A ,TB_TAX_BILL_INFO B                              	\n")    
		.append("WHERE A.ISSUE_ID = B.ISSUE_ID                                                  \n")  
		.append("AND A.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID                                          \n")
		.append("AND A.MANAGE_ID = B.SVC_MANAGE_ID                                              \n")
		.append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                            \n")    
		.append("AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                          \n")    
		.append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                  \n")    
		.append("AND B.STATUS_CODE  ='01'                                                       \n")    
		.append("AND B.IO_CODE = '1'                                                            \n")
		.append("AND  C.SEQ_NO = '1'                                                            \n");
		// 요금매출은 메일 송신 제외 시키기 추가 2013.11.11
		// 공가매출 프로세스 2차 변경 적용  2013.12.26
		// 아래 코드 comment처리 
		//.append("AND B.ONLINE_GUB_CODE = '1'                                                    \n")
		//.append("AND A.JOB_GUB_CODE = '350010'--공가매출                                                                                   \n");*/
		//변경 쿼리 윤규미 20180227
		.append(" SELECT																				\n")                      
		.append(" B.IO_CODE, B.BIZ_MANAGE_ID, B.INVOICEE_PARTY_ID,B.INVOICER_PARTY_ID,					\n")
		.append(" B.INVOICER_PARTY_NAME, B.INVOICEE_PARTY_NAME, C.ITEM_NAME,							\n")
		.append(" B.INVOICEE_CONTACT_PHONE1, B.INVOICEE_CONTACT_EMAIL1,B.INVOICEE_CONTACT_NAME1,		\n")
		.append(" B.INVOICEE_CONTACT_PHONE2, B.INVOICEE_CONTACT_EMAIL2,B.INVOICEE_CONTACT_NAME2,		\n")
		.append(" B.INVOICER_CONTACT_PHONE, B.INVOICER_CONTACT_EMAIL, B.INVOICER_CONTACT_NAME,			\n")
		.append(" B.GRAND_TOTAL_AMOUNT, B.BILL_TYPE_CODE,												\n")
		.append(" B.CHARGE_TOTAL_AMOUNT, B.TAX_TOTAL_AMOUNT,											\n")
		.append(" B.ISSUE_ID, B.ONLINE_GUB_CODE															\n")
		.append(" FROM IF_TAX_BILL_INFO_TEMP A ,TB_TAX_BILL_INFO B,  TB_TRADE_ITEM_LIST C				\n")  //   TB_TRADE_ITEM_LIST 추가             
		.append(" WHERE A.ISSUE_ID = B.ISSUE_ID															\n")                                 
		.append(" AND A.MANAGE_ID = B.SVC_MANAGE_ID														\n")
		.append(" AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID													\n")//추가
		.append(" AND B.IO_CODE = C.IO_CODE																\n")//추가
		.append(" AND B.ISSUE_DAY = C.ISSUE_DAY															\n")//추가
		.append(" AND A.JOB_GUB_CODE = B.JOB_GUB_CODE													\n")
		.append(" AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID													\n")
		.append(" AND A.ISSUE_DAY = B.ISSUE_DAY															\n")
		.append(" AND B.STATUS_CODE  ='01'																\n")
		.append(" AND B.IO_CODE = '1'																	\n")
		.append(" AND C.SEQ_NO = '1'																	\n");//추가

		
		
        CommUtil.logWriter(sb.toString(),1);
		
		pstm = con.prepareStatement(sb.toString());
		rs = pstm.executeQuery();
		while(rs.next()){		//이메일 전송 대상이 있으면..
		    TbTaxBillInfoVo tbinfo = new TbTaxBillInfoVo();
		    tbinfo.setIo_code(CommUtil.SpaceChange(rs.getString("IO_CODE")));
		    tbinfo.setBiz_manage_id(CommUtil.SpaceChange(rs.getString("BIZ_MANAGE_ID")));
		    tbinfo.setInvoicee_party_name(CommUtil.SpaceChange(rs.getString("INVOICEE_PARTY_NAME")));
		    tbinfo.setInvoicer_party_name(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_NAME")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicer_party_id(CommUtil.SpaceChange(rs.getString("INVOICER_PARTY_ID")));
		    tbinfo.setInvoicee_contact_phone1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_PHONE1")));
		    tbinfo.setInvoicee_contact_email1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_EMAIL1")));
		    tbinfo.setInvoicee_contact_name1(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_NAME1")));
		    tbinfo.setInvoicee_contact_phone2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_PHONE2")));
		    tbinfo.setInvoicee_contact_email2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_EMAIL2")));
		    tbinfo.setInvoicee_contact_name2(CommUtil.SpaceChange(rs.getString("INVOICEE_CONTACT_NAME2")));
		    tbinfo.setInvoicer_contact_phone(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_PHONE")));
		    tbinfo.setInvoicer_contact_email(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_EMAIL")));
		    tbinfo.setInvoicer_contact_name(CommUtil.SpaceChange(rs.getString("INVOICER_CONTACT_NAME")));
		    tbinfo.setIssue_id2(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));	//ISSUE_ID를 암호화하여 저장한다	    
		    tbinfo.setIssue_id(CommUtil.SpaceChange(rs.getString("ISSUE_ID")));
		    tbinfo.setItem_list(CommUtil.SpaceChange(rs.getString("ITEM_NAME")));
		    tbinfo.setCharge_total_amount(rs.getLong("CHARGE_TOTAL_AMOUNT"));
		    tbinfo.setTax_total_amount(rs.getLong("TAX_TOTAL_AMOUNT"));
		    tbinfo.setGrand_total_amount(rs.getLong("GRAND_TOTAL_AMOUNT"));
		    tbinfo.setBill_type_code(CommUtil.SpaceChange(rs.getString("BILL_TYPE_CODE")));
		    
		    tbinfo.setOnline_gub_code(CommUtil.SpaceChange(rs.getString("ONLINE_GUB_CODE")));
		    
		    vc.add(tbinfo);
		}
		if(vc.size()>0){
		    SmsDao sd = new SmsDao();
		    //sd.acc_sms(vc,con);
		  //20180212 윤규미 sms->lms 변경
		    System.out.println("----------------------한전 매출 LMS,EMAIL Start------------------------");
		    sd.acc_lms(vc,con);
		    sd.acc_email(vc,con);
		    System.out.println("----------------------한전 매출 LMS,EMAIL End------------------------");
		}
        }catch(Exception e1){
            e1.printStackTrace();
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    
    /**
     * IF_TAX_BILL_INFO_TEMP TRUNCATE 처리.
     * @param Connection
     */ 
    public boolean trun_if_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("TRUNCATE TABLE IF_TAX_BILL_INFO_TEMP     \n");
            
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }   

    /**
     * IF_TAX_BILL_CONFIRM_INFO_TEMP TRUNCATE 처리.
     * @param Connection
     */
    public boolean trun_if_confirm_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("TRUNCATE TABLE IF_TAX_BILL_CONFIRM_INFO_TEMP     \n");
            
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }   

    /**
     * TB_XML_INFO_TEMP TRUNCATE 처리.
     * @param Connection
     */
    public boolean trun_tb_xml_info_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("TRUNCATE TABLE TB_XML_INFO_TEMP     \n");
            
            CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }   
 /**********************************************************************/
 /******************이하 부터는 상태 변경 쿼리**************************/
 /**********************************************************************/
    
    /**
     * 솔루션 이관후 처리상태 변경을 위한 대상 잡기 , 전송 기준일자가 지난 강제 전송건
     * @param Connection
     */ 
    
    public boolean after_trans_pro_a(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(					         \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		\n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE		\n")
            .append(")					\n")
            .append(" SELECT							\n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		\n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE AS STATUS_CODE_ORG,'03' --솔루션 전송후 상태를 접수로 바꿔준다.	\n")
            .append("FROM TB_TAX_BILL_INFO\n")
            .append("WHERE ONLINE_GUB_CODE = '1'\n")
            .append("AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD')	\n")
            .append("AND STATUS_CODE = '01'\n")
            .append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')	\n")
            .append("AND IO_CODE = '1'			\n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }       
  
    
    /**
     * 솔루션 이관후 처리상태 변경을 위한 대상 잡기 , 전송 기준일자가 지난 강제 전송건
     * @param Connection
     */ 
    
    public boolean after_trans_pro_b(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(					                                                                 			 \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		                                                         \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	                                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE		                                                                         \n")
            .append(")					                                                                                                 			 \n")
            .append(" SELECT							                                                                         					 \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		                                                         \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	                                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE AS STATUS_CODE_ORG,'03' --솔루션 전송후 상태를 접수로 바꿔준다.	                                     \n")
            .append("FROM TB_TAX_BILL_INFO                                                                                                           \n")
            //.append("WHERE ONLINE_GUB_CODE = '1'                                                                                                   \n")
            .append("WHERE (ONLINE_GUB_CODE = '1' OR ONLINE_GUB_CODE = '3' ) --20101013                    									\n")
            //2011.2.25 수정세금계산서 날짜체크해제로 수정 KDY
            //.append("AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)),'YYYYMMDD')	 \n")
            .append("AND (STATUS_CODE = '02' OR STATUS_CODE = '06') --20100616 or 06 추가함		\n")
            //2014. 12. 9 수정계산서도 추가로 처리 by 박상종 
            //.append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')	                         \n")
            .append("AND ( BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')                       \n")
            .append("      OR ( BILL_TYPE_CODE IN ('0301', '0303', '0304') AND ISSUE_DAY >= ? ) \n") // 계산서는 작성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
            .append("      OR ( BILL_TYPE_CODE IN ('0401', '0403', '0404') AND ISSUE_DT >= ? )) \n")  // 수정계산서는 생성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
            .append("AND IO_CODE = '1'			\n")
            .append("AND ISSUE_DAY > 0          \n");

            CommUtil.logWriter("NOTAXBILL_ESERO_REG_DAY:"+NOTAXBILL_ESERO_REG_DAY,1);
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, NOTAXBILL_ESERO_REG_DAY);
			pstm.setString(2, NOTAXBILL_ESERO_REG_DAY+"000000" );
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }  
    
    
    
    
    /**
     * 솔루션 이관후 처리상태 변경을 위한 대상 잡기 , 전송 기준일자가 지난 강제 전송건
     * @param Connection
     */ 
    
    public boolean after_trans_pro_c(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(					                                                                 \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		                                             \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	                                             \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE		                                                             \n")
            .append(")					                                                                                                 \n")
            .append(" SELECT							                                                                         		 \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,		                                             \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	                                             \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE AS STATUS_CODE_ORG,'03' --솔루션 전송후 상태를 접수로 바꿔준다.	                         \n")
            .append("FROM TB_TAX_BILL_INFO                                                                                               \n")
            //.append("WHERE ONLINE_GUB_CODE = '1'                                                                          \n")
            .append("WHERE (ONLINE_GUB_CODE = '1' OR ONLINE_GUB_CODE = '3')   --20101013                                       			\n")
            .append("AND ISSUE_DAY  BETWEEN TO_CHAR(SYSDATE,'YYYYMM')||'01' AND TO_CHAR(LAST_DAY(SYSDATE),'YYYYMMDD')                   \n")
            .append("AND (STATUS_CODE = '02' OR STATUS_CODE = '06')																		\n")
            .append("AND BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')	            \n")
            .append("AND IO_CODE = '1'																									\n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    }  
        
  
    
    
    /**
     * IF_TAX_BILL_RESULT_INFO에 이력갱신 , 전송 기준일자가 지난 강제 전송건
     * @param Connection
     */ 
        
    public boolean after_trans_pro1(Connection con)throws SQLException, Exception{
    	
    	boolean bl = true;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO(                                                     		\n")
            .append("    REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, STATUS_DESC,                     		\n")
            .append("    REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT, EAI_CDATE, EAI_UDATE                       		\n")
            .append(")                                                                                             	\n")
            .append("SELECT /*+ ordered */                                                                          \n")
            .append("    C.REL_SYSTEM_ID, C.JOB_GUB_CODE, C.MANAGE_ID,                                          	\n")
            .append("    B.STATUS_CODE,                                                                       		\n")
            .append("    '전송완료' AS STATUS_DESC,                                                            		\n")
            //.append("    SYSDATE, SYSDATE, C.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X'), NULL,NULL  \n")
            .append("    SYSDATE, SYSDATE, C.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(B.ONLINE_GUB_CODE,'3',NULL , 'X'),'X'), NULL,NULL  \n")
            .append("FROM  TB_TAX_BILL_ON_BATCH B, IF_TAX_BILL_INFO C                                				\n")
            .append("WHERE 1>0																						\n")
            .append("--AND B.REL_SYSTEM_ID = C.REL_SYSTEM_ID                                                 			\n")
            .append("AND B.JOB_GUB_CODE = C.JOB_GUB_CODE                                                   			\n")
            .append("AND B.ONLINE_GUB_CODE = C.ONLINE_GUB_CODE                                                 		\n")
            .append("AND B.ISSUE_ID = C.ISSUE_ID                                                              		\n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    } 
        
    
    /**
     * TB_TAX_BILL_INFO에 상태 이력갱신 , 전송 기준일자가 지난 강제 전송건
     * @param Connection
     */ 
        
    public boolean after_trans_pro2(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer();
            if(RunningScheduler.DB_ENC.equals("1")){         
            	sb.append("UPDATE TB_TAX_BILL_INFO_ENC SET STATUS_CODE='03'                    \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE='03'                        \n");
            }else{
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE='03'                        \n");
            }
            sb.append("WHERE 1>0                                                               \n");
            sb.append("AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)                                 \n");
            sb.append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH) \n");
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    } 
        
    

    
    /**
     * TB_TAX_BILL_INFO에 상태 이력갱신 , 매입 전송건
     * @param Connection
     */ 
        
   public boolean after_trans_pro3(Connection con,String code)throws SQLException, Exception{
    	
    	boolean bl = false;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer();
            if(RunningScheduler.DB_ENC.equals("1")){         
                sb.append("UPDATE TB_TAX_BILL_INFO_ENC SET STATUS_CODE= ?                          \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE= ?                              \n");
            }else{
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE= ?                              \n");
            }
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            sb.append("WHERE 1>0                                                               \n");
            sb.append("AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)                                 \n");
            sb.append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH) \n");
            
            CommUtil.logWriter(sb.toString(),1);
            
            pstm = con.prepareStatement(sb.toString());
            pstm.setString(1, code);
            rs = pstm.executeUpdate();
            CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,1);
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    } 
   /**
    * TB_TAX_BILL_INFO에 내선계기 세금계산서 TAX_2ND_SIGN=Y, ISSUE_DT2 = ISSUE_DT 갱신
    * @param Connection
    */ 
       
  public boolean after_trans_pro6(Connection con)throws SQLException, Exception{
   	
   	boolean bl = false;
   	int rs = 0;
   	PreparedStatement pstm=null;
   	try{
           StringBuffer sb= new StringBuffer();
           if(RunningScheduler.DB_ENC.equals("1")){         
               sb.append("UPDATE TB_TAX_BILL_INFO_ENC                                              \n");
           }else if(RunningScheduler.DB_ENC.equals("0")){                 
               sb.append("UPDATE TB_TAX_BILL_INFO                                                  \n");
           }else{
               sb.append("UPDATE TB_TAX_BILL_INFO                                                  \n");
           }
           CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
           sb.append("       SET TAX_2ND_SIGN = DECODE(ELECTRONIC_REPORT_YN, 'N','Y','F','Y',NULL)  \n");
           sb.append("          ,STATUS_CODE  = DECODE(ELECTRONIC_REPORT_YN, 'F','02',STATUS_CODE)  \n");
           sb.append("          ,ISSUE_DT2  = ISSUE_DT                                              \n");
           sb.append("WHERE 1>0                                                                     \n");
           sb.append("AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)                                       \n");
           sb.append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)       \n");
           
           CommUtil.logWriter(sb.toString(),4);
           
           pstm = con.prepareStatement(sb.toString());
           //pstm.setString(1, code);
           rs = pstm.executeUpdate();
           CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
           if(rs>0){
       	bl = true;
           }
           //System.out.println("after_trans_pro1====>"+bl);
       }catch(SQLException e){
           bl = false;
           e.printStackTrace();
           throw e;
       }catch(Exception e1){
           bl = false;            
   	    e1.printStackTrace();
   	    throw e1;
   	}finally{
   	    close(pstm);
   	}
   	return bl;
   } 
    
   /**
    * TB_TAX_BILL_INFO에 2차 전자서명 대상 세금계산서 상태값(TAX_2ND_SIGN=N) 갱신
    * @param Connection
    */ 
       
  public boolean after_trans_pro5(Connection con)throws SQLException, Exception{
   	
   	boolean bl = false;
   	int rs = 0;
   	PreparedStatement pstm=null;
   	try{
           StringBuffer sb= new StringBuffer();
           if(RunningScheduler.DB_ENC.equals("1")){         
               sb.append("UPDATE TB_TAX_BILL_INFO_ENC                                              \n");
           }else if(RunningScheduler.DB_ENC.equals("0")){                 
               sb.append("UPDATE TB_TAX_BILL_INFO                                                  \n");
           }else{
               sb.append("UPDATE TB_TAX_BILL_INFO                                                  \n");
           }
           CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
           sb.append("       SET TAX_2ND_SIGN = DECODE(ELECTRONIC_REPORT_YN, 'N','N','F','Y',NULL)  \n");
           sb.append("          ,STATUS_CODE  = DECODE(ELECTRONIC_REPORT_YN, 'F','02',STATUS_CODE)  \n");
           sb.append("WHERE 1>0                                                                     \n");
           sb.append("AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)                                       \n");
           sb.append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)       \n");
           
           CommUtil.logWriter(sb.toString(),4);
           
           pstm = con.prepareStatement(sb.toString());
           //pstm.setString(1, code);
           rs = pstm.executeUpdate();
           CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
           if(rs>0){
       	bl = true;
           }
           //System.out.println("after_trans_pro1====>"+bl);
       }catch(SQLException e){
           bl = false;
           e.printStackTrace();
           throw e;
       }catch(Exception e1){
           bl = false;            
   	    e1.printStackTrace();
   	    throw e1;
   	}finally{
   	    close(pstm);
   	}
   	return bl;
   } 
   
    /**
     * TB_TAX_BILL_INFO에 회계년도, 전표번호 UPDATE
     * @param Connection
     * 2012. 11. 1 update_erp_slip method로 변경 함. DELETE BY mopuim
     */ 
   public boolean after_trans_pro4(Connection con)throws SQLException, Exception{
   	
   	boolean bl = false;
   	int rs = 0;
   	PreparedStatement pstm=null;
   	try{
           StringBuffer sb= new StringBuffer();
           if(RunningScheduler.DB_ENC.equals("1")){         
   			  sb.append(" UPDATE TB_TAX_BILL_INFO_ENC  TBI                                                 \n"); 
           }else if(RunningScheduler.DB_ENC.equals("0")){                 
   			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                     \n"); 
           }else{
   			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                     \n"); 
           }
           CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
			sb.append(" SET (ERP_ACC_YEAR, ERP_SLIP_NO)                                                     \n");
			sb.append("     = (SELECT ERP_ACC_YEAR, ERP_SLIP_NO                                             \n");
			sb.append("          FROM (                                                                     \n");
			sb.append("                      SELECT  MI.NTS_ISSUE_ID ISSUE_ID,                              \n");
			sb.append("                              MT.ERP_ACC_YEAR ERP_ACC_YEAR,                          \n"); 
			sb.append("                              MT.ERP_SLIP_NO ERP_SLIP_NO                             \n");
			sb.append("                              FROM ETS_TAX_META_INFO_TB MT, ETS_TAX_MAIN_INFO_TB MI, \n");
			sb.append("                                       TB_TAX_BILL_ON_BATCH TBO                      \n");
			sb.append("                              WHERE MT.UUID = MI.UUID                                \n");
			sb.append("                              AND TBO.ISSUE_ID = MI.NTS_ISSUE_ID                     \n");
			sb.append("                              AND  MI.NTS_ISSUE_ID IS NOT NULL                       \n");
			sb.append("                              AND  MT.COMP_CODE    = '00'  --한전것만                                   \n");
			sb.append("                              AND  MT.DOC_STATE    = 'END' --전송대상                                   \n");
			sb.append("                    UNION ALL                                                        \n");
			sb.append("                         SELECT  MST.ISSUE_ID ISSUE_ID,                              \n");
			sb.append("                              RSP.ERP_ACC_YEAR ERP_ACC_YEAR,                         \n");
			sb.append("                              RSP.ERP_SLIP_NO ERP_SLIP_NO                            \n");
			sb.append("                              FROM XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP,           \n");
			sb.append("                                       TB_TAX_BILL_ON_BATCH TBO                      \n");
			sb.append("                              WHERE MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID            \n");
			sb.append("                              AND TBO.ISSUE_ID = MST.ISSUE_ID                        \n");
			sb.append("                              AND    MST.BLDAT >= '20100101'                         \n");
			sb.append("                              AND    RSP.DEL_FLAG  = '2'                             \n");
			sb.append("                   ) CC                                                              \n");
			sb.append("                   WHERE CC.ISSUE_ID = TBI.ISSUE_ID )                                \n");
			sb.append("  WHERE (TBI.IO_CODE, TBI.ISSUE_DAY, TBI.BIZ_MANAGE_ID)  IN                          \n");
			sb.append("              (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID                              \n");
			sb.append("                            FROM TB_TAX_BILL_ON_BATCH)			                    \n");     
           CommUtil.logWriter(sb.toString(),1);
           pstm = con.prepareStatement(sb.toString());
           rs = pstm.executeUpdate();
           CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,1);
           if(rs>0){
       	     bl = true;
           }
           //System.out.println("after_trans_pro1====>"+bl);
       }catch(SQLException e){
           bl = false;
           e.printStackTrace();
           throw e;
       }catch(Exception e1){
           bl = false;            
   	    e1.printStackTrace();
   	    throw e1;
   	}finally{
   	    close(pstm);
   	}
   	return bl;
   }  

   /**
    * TB_TAX_BILL_INFO에 한전EDI세금계산서에 대해 회계년도, 전표번호, 한전담당자 이름, 부서명, 메일, 전화 UPDATE
    * @param Connection
    * 2013. 6. 25로 after_trans_pro4를 한전EDI용, 세금계산서용으로 분리함. BY mopuim
    */ 
   public boolean after_trans_pro4_KEDI(Connection con)throws SQLException, Exception{
 	
 	boolean bl = false;
 	int rs = 0;
 	PreparedStatement pstm=null;
 	try{
         StringBuffer sb= new StringBuffer();
         sb.append(" UPDATE /*+ bypass_ujvc */                                                          \n");
         sb.append("        (                                                                           \n");
         sb.append("                 SELECT  MST.ISSUE_ID ISSUE_ID,                                     \n");
         sb.append("                        RSP.ENAME ENAME,                                            \n");
         sb.append("                        RSP.GTEXT GTEXT,                                            \n");
         sb.append("                        RSP.MAILAD MAILAD,                                          \n");
         sb.append("                        RSP.TELNO TELNO,                                            \n");
         sb.append("                        TBI.INVOICEE_CONTACT_NAME1 INVOICEE_CONTACT_NAME1,          \n");
         sb.append("                        TBI.INVOICEE_CONTACT_DEPART1 INVOICEE_CONTACT_DEPART1,      \n");
         sb.append("                        TBI.INVOICEE_CONTACT_EMAIL1 INVOICEE_CONTACT_EMAIL1,        \n");
         sb.append("                        TBI.INVOICEE_CONTACT_PHONE1 INVOICEE_CONTACT_PHONE1         \n"); 
         sb.append("                        FROM XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP,                \n");
         if(RunningScheduler.DB_ENC.equals("1")){         
             sb.append("                         TB_TAX_BILL_INFO_ENC TBI, TB_TAX_BILL_ON_BATCH TBO     \n");
        }else if(RunningScheduler.DB_ENC.equals("0")){                 
            sb.append("                          TB_TAX_BILL_INFO TBI, TB_TAX_BILL_ON_BATCH TBO         \n");
        }else{
            sb.append("                          TB_TAX_BILL_INFO TBI, TB_TAX_BILL_ON_BATCH TBO         \n");
        }
        //CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
         sb.append("                        WHERE MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                 \n");
         sb.append("                        AND RSP.MANAGEMENT_ID = TBI.SVC_MANAGE_ID                   \n");
         sb.append("                        AND TBI.ISSUE_ID = TBO.ISSUE_ID                             \n");
         sb.append("                        AND TBO.ISSUE_ID = MST.ISSUE_ID                             \n");
         sb.append("                        AND LENGTH(TBI.SVC_MANAGE_ID) = 10                          \n");
         sb.append("                        AND    MST.BLDAT >= '20100101'                              \n");
         sb.append("                        AND    RSP.DEL_FLAG  = '2'                                  \n");
         sb.append("       )                                                                            \n");
         sb.append("       SET INVOICEE_CONTACT_NAME1 = ENAME,                                          \n");
         sb.append("           INVOICEE_CONTACT_DEPART1 = GTEXT,                                        \n");
         sb.append("           INVOICEE_CONTACT_EMAIL1 = MAILAD,                                        \n");
         sb.append("           INVOICEE_CONTACT_PHONE1 = TELNO                                          \n");
         CommUtil.logWriter(sb.toString(),1);
         pstm = con.prepareStatement(sb.toString());
         rs = pstm.executeUpdate();
         CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,1);
         if(rs>0){
     	     bl = true;
         }
         //System.out.println("after_trans_pro1====>"+bl);
     }catch(SQLException e){
         bl = false;
         e.printStackTrace();
         throw e;
     }catch(Exception e1){
         bl = false;            
 	    e1.printStackTrace();
 	    throw e1;
 	}finally{
 	    close(pstm);
 	}
 	return bl;
 }   
  /*public boolean after_trans_pro4_KEDI(Connection con)throws SQLException, Exception{
  	
  	boolean bl = false;
  	int rs = 0;
  	PreparedStatement pstm=null;
  	try{
          StringBuffer sb= new StringBuffer();
          if(RunningScheduler.DB_ENC.equals("1")){         
  			  sb.append(" UPDATE TB_TAX_BILL_INFO_ENC  TBI                                                 \n"); 
          }else if(RunningScheduler.DB_ENC.equals("0")){                 
  			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                     \n"); 
          }else{
  			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                     \n"); 
          }
          //CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
			sb.append(" SET (INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_DEPART1,                             \n");
			sb.append("      INVOICEE_CONTACT_EMAIL1, INVOICEE_CONTACT_PHONE1 )                            \n");
			sb.append("     = (SELECT INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_DEPART1,                    \n");
			sb.append("                INVOICEE_CONTACT_EMAIL1, INVOICEE_CONTACT_PHONE1                    \n");
			sb.append("          FROM (                                                                    \n");
			sb.append("                SELECT  MST.ISSUE_ID ISSUE_ID,                                      \n");
			sb.append("                        RSP.ENAME INVOICEE_CONTACT_NAME1,                           \n");
			sb.append("                        RSP.GTEXT INVOICEE_CONTACT_DEPART1,                         \n");
			sb.append("                        RSP.MAILAD INVOICEE_CONTACT_EMAIL1,                         \n");
			sb.append("                        RSP.TELNO INVOICEE_CONTACT_PHONE1                           \n");
			sb.append("                        FROM XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP,                \n");
			sb.append("                             TB_TAX_BILL_ON_BATCH TBO                               \n");
			sb.append("                        WHERE MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                 \n");
			sb.append("                        AND TBO.ISSUE_ID = MST.ISSUE_ID                             \n");
			sb.append("                        AND    MST.BLDAT >= '20100101'                              \n");
			sb.append("                        AND    RSP.DEL_FLAG  = '2'                                  \n");
			sb.append("                   ) CC                                                             \n");
			sb.append("                   WHERE CC.ISSUE_ID = TBI.ISSUE_ID )                               \n");
			sb.append("  WHERE (TBI.IO_CODE, TBI.ISSUE_DAY, TBI.BIZ_MANAGE_ID)  IN                         \n");
			sb.append("              (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID                             \n");
			sb.append("                            FROM TB_TAX_BILL_ON_BATCH)			                   \n");     
          CommUtil.logWriter(sb.toString(),3);
          pstm = con.prepareStatement(sb.toString());
          rs = pstm.executeUpdate();
          CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,1);
          if(rs>0){
      	     bl = true;
          }
          //System.out.println("after_trans_pro1====>"+bl);
      }catch(SQLException e){
          bl = false;
          e.printStackTrace();
          throw e;
      }catch(Exception e1){
          bl = false;            
  	    e1.printStackTrace();
  	    throw e1;
  	}finally{
  	    close(pstm);
  	}
  	return bl;
  } */   
  /**
   * TB_TAX_BILL_INFO에 KEPCOBILL세금계산서에 대해 회계년도, 전표번호 UPDATE
   * @param Connection
   * 2013. 6. 25로 after_trans_pro4를 한전EDI용, 세금계산서용으로 분리함. BY mopuim
   */ 
 /*public boolean after_trans_pro4_KBILL(Connection con)throws SQLException, Exception{
 	
 	boolean bl = false;
 	int rs = 0;
 	PreparedStatement pstm=null;
 	try{
         StringBuffer sb= new StringBuffer();
         if(RunningScheduler.DB_ENC.equals("1")){         
 			  sb.append(" UPDATE TB_TAX_BILL_INFO_ENC  TBI                                                  \n"); 
         }else if(RunningScheduler.DB_ENC.equals("0")){                 
 			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                      \n"); 
         }else{
 			  sb.append(" UPDATE TB_TAX_BILL_INFO  TBI                                                      \n"); 
         }
         //CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
			sb.append(" SET (ERP_ACC_YEAR, ERP_SLIP_NO)                                                     \n");
			sb.append("     = (SELECT ERP_ACC_YEAR, ERP_SLIP_NO                                             \n");
			sb.append("          FROM (                                                                     \n");
			sb.append("                SELECT  MI.NTS_ISSUE_ID ISSUE_ID,                                    \n");
			sb.append("                        MT.ERP_ACC_YEAR ERP_ACC_YEAR,                                \n"); 
			sb.append("                        MT.ERP_SLIP_NO ERP_SLIP_NO                                   \n");
			sb.append("                        FROM ETS_TAX_META_INFO_TB MT, ETS_TAX_MAIN_INFO_TB MI,       \n");
			sb.append("                             TB_TAX_BILL_ON_BATCH TBO                                \n");
			sb.append("                        WHERE MT.UUID = MI.UUID                                      \n");
			sb.append("                        AND TBO.ISSUE_ID = MI.NTS_ISSUE_ID                           \n");
			sb.append("                        AND  MI.NTS_ISSUE_ID IS NOT NULL                             \n");
			sb.append("                        AND  MT.COMP_CODE    = '00'  --한전것만                                               \n");
			sb.append("                        AND  MT.DOC_STATE    = 'END' --전송대상                                               \n");
			sb.append("                   ) CC                                                              \n");
			sb.append("                   WHERE CC.ISSUE_ID = TBI.ISSUE_ID )                                \n");
			sb.append("  WHERE (TBI.IO_CODE, TBI.ISSUE_DAY, TBI.BIZ_MANAGE_ID)  IN                          \n");
			sb.append("              (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID                              \n");
			sb.append("                            FROM TB_TAX_BILL_ON_BATCH)			                    \n");     
         CommUtil.logWriter(sb.toString(),3);
         pstm = con.prepareStatement(sb.toString());
         rs = pstm.executeUpdate();
         CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,1);
         if(rs>0){
     	     bl = true;
         }
         //System.out.println("after_trans_pro1====>"+bl);
     }catch(SQLException e){
         bl = false;
         e.printStackTrace();
         throw e;
     }catch(Exception e1){
         bl = false;            
 	    e1.printStackTrace();
 	    throw e1;
 	}finally{
 	    close(pstm);
 	}
 	return bl;
 }   */ 
   
   /**
    * TB_TAX_BILL_INFO에 회계년도, 전표번호 UPDATE
    * @param Connection
    * 2012. 11. 1 ADD BY mopuim
    */ 
  public boolean update_erp_slip(Connection con)throws SQLException, Exception{
  	
  	boolean bl = false;
  	int rs = 0;
  	PreparedStatement pstm=null;
  	try{
          StringBuffer sb= new StringBuffer()
		    .append(" UPDATE TB_TAX_BILL_INFO TBI2                                                        \n")
		    .append("    SET (ERP_ACC_YEAR, ERP_SLIP_NO) =                                                \n")
		    .append("           (SELECT ERP_ACC_YEAR, ERP_SLIP_NO                                         \n")
		    .append("              FROM (                                                                 \n")
		    .append("                     SELECT /*+ INDEX(MI ETS_TAX_MAIN_INFO_TB_IDX1) */               \n")
		    .append("                          MI.NTS_ISSUE_ID ISSUE_ID,                                  \n")
		    .append("                           MT.ERP_ACC_YEAR ERP_ACC_YEAR,                             \n")
		    .append("                           MT.ERP_SLIP_NO ERP_SLIP_NO                                \n")
		    .append("                      FROM ETS_TAX_MAIN_INFO_TB MI, ETS_TAX_META_INFO_TB MT          \n")
		    .append("                     WHERE     MI.UUID = MT.UUID                                     \n")
		    .append("                           AND MT.COMPLETE_TIME > SYSDATE - 40                       \n")
		    .append("                           AND MT.COMP_CODE = '00'                       --한전것만       \n")
		    .append("                           AND MT.DOC_STATE = 'END'                                  \n")
		    .append("                    UNION ALL                                                        \n")
		    .append("                    SELECT /*+ INDEX(RSP XMLEDI_TAX_RSP_IDX1) */                     \n")
		    .append("                          MST.ISSUE_ID ISSUE_ID,                                     \n")
		    .append("                           RSP.ERP_ACC_YEAR ERP_ACC_YEAR,                            \n")
		    .append("                           RSP.ERP_SLIP_NO ERP_SLIP_NO                               \n")
		    .append("                      FROM XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP                    \n")
		    .append("                     WHERE     MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                 \n")
		    .append("                           AND MST.DOC_ID > TO_CHAR(SYSDATE - 40, 'YYYYMMDD')        \n")
		    .append("                           AND RSP.DEL_FLAG = '2'                                    \n")
		    .append("                           AND RSP.SEQNO > 0               ) CC                      \n")
		    .append("             WHERE CC.ISSUE_ID = TBI2.ISSUE_ID)                                      \n")
		    .append("  WHERE TBI2.ISSUE_DT > TO_CHAR(SYSDATE - 40, 'YYYYMMDD')                            \n")
		    .append("        AND TBI2.ONLINE_GUB_CODE='1'                                                 \n")
		    .append("        AND TBI2.IO_CODE = '2'                                                       \n")
		    .append("        AND TBI2.ERP_ACC_YEAR IS NULL                                                \n")
		    .append("        AND TBI2.ERP_SLIP_NO IS NULL                                                 \n")          
		    .append("        AND ROWNUM < 100  -- 대량건수일경우 시간이 오래걸려 100건으로 제한함.                 \n");          
          CommUtil.logWriter(sb.toString(),1);
          pstm = con.prepareStatement(sb.toString());
          rs = pstm.executeUpdate();
          CommUtil.logWriter("update_erp_slip METHOD pstm.executeUpdate 갯수:"+rs,1);
          if(rs>0){
      	     bl = true;
          }
      }catch(SQLException e){
          bl = false;
          e.printStackTrace();
          throw e;
      }catch(Exception e1){
          bl = false;            
  	    e1.printStackTrace();
  	    throw e1;
  	}finally{
  	    close(pstm);
  	}
  	return bl;
  }    
   
   
    /**
     * TB_TAX_BILL_INFO 매입 
     * @param Connection
     */ 
        
    public boolean del_tb_tax_info(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer();
            if(RunningScheduler.DB_ENC.equals("1")){         
                sb.append("UPDATE TB_TAX_BILL_INFO_ENC SET STATUS_CODE = '98' \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE = '98'     \n");
            }else{
                sb.append("UPDATE TB_TAX_BILL_INFO SET STATUS_CODE = '98'     \n");
            }
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            sb.append("WHERE ISSUE_ID IN                                  \n");
            sb.append("(SELECT ISSUE_ID FROM TB_TAX_BILL_ON_BATCH)        \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    } 
        
 
    /**
     * TB_TRADE_ITEM_LIST 매입 
     * @param Connection
     */ 
   
    
/**
 * 인터페이스에 전송결과 인서트(접수후 이후 과정 반영 )
 * @param Connection
 */ 

    public boolean tax_result_info_update(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO(                                                           \n")
            .append("REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, STATUS_DESC,                              \n")
            .append("REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT, EAI_CDATE, EAI_UDATE,                               \n")
            .append("ESERO_CREATE_TS,  ESERO_FINISH_TS                                                              \n")
            .append(")                                                                                              \n")
            .append("SELECT /*+ ordered */                                                                          \n")
            .append("B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID,                                                  \n")
            .append("A.STATUS_CODE,                                                                                 \n")
            .append("DECODE(A.STATUS_CODE,'04','세금계산서 신고완료','99','세금계산서 국세청오류') AS STATUS_DESC,          \n")
            //.append("SYSDATE, SYSDATE, A.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X'), NULL, NULL     \n")
            .append("SYSDATE, SYSDATE, A.ISSUE_ID,                                                                  \n")
            .append("DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(B.ONLINE_GUB_CODE,'3',NULL , 'X'),'X'),     \n")
            .append("NULL, NULL,                                                                                    \n")
            .append(" TO_CHAR(A.ESERO_CREATE_TS, 'YYYYMMDD'),                                                       \n")
            .append(" TO_CHAR(A.ESERO_FINISH_TS, 'YYYYMMDD')                                                        \n")
            //.append("decode(TO_CHAR(A.ESERO_CREATE_TS, 'YYYYMMDD'),null, '',TO_CHAR(A.ESERO_CREATE_TS, 'YYYYMMDD')),\n")
            //.append("decode(TO_CHAR(A.ESERO_FINISH_TS, 'YYYYMMDD'),null,'',TO_CHAR(A.ESERO_FINISH_TS, 'YYYYMMDD'))  \n")
            .append("FROM  TB_TAX_BILL_ON_BATCH A, IF_TAX_BILL_INFO B                                               \n")
            .append("WHERE A.IO_CODE = '1'                                                                          \n")
            .append("AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')                                           \n")
            .append("--AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                          \n")
            .append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                            \n")
            .append("AND A.ONLINE_GUB_CODE = B.ONLINE_GUB_CODE                                                      \n")
            .append("AND A.ISSUE_ID = B.ISSUE_ID -- 국세청으로부터 결과값을 받은 상태                               \n");
            
            CommUtil.logWriter(sb.toString(),1); 
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }       
    
           
    /**
     * 인터페이스에 처리결과 업데이트(국세청전송 후 처리결과 반영)
     * @param Connection
     */ 
    public boolean tax_result_info_update_(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO(                                                        \n")
            .append("REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, STATUS_DESC,                           \n")
            .append("REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT, EAI_CDATE, EAI_UDATE                              \n")
            .append(")                                                                                           \n")
            .append("SELECT /*+ ordered */                                                                       \n")
            .append("B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID,                                               \n")
            .append("A.STATUS_CODE,                                                                              \n")
            .append("DECODE(A.STATUS_CODE,'03','전송완료','96','세금계산서 전송실패') AS STATUS_DESC,            \n")
            //.append("SYSDATE, SYSDATE, A.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X'), NULL, NULL  \n")
            .append("SYSDATE, SYSDATE, A.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(B.ONLINE_GUB_CODE,'3',NULL , 'X'),'X'), NULL, NULL  \n")
            .append("FROM  TB_TAX_BILL_ON_BATCH A, IF_TAX_BILL_INFO B                                            \n")
            .append("WHERE A.IO_CODE = '1'                                                                       \n")
            .append("AND (A.ONLINE_GUB_CODE='1' OR  A.ONLINE_GUB_CODE='3')                                       \n")
            .append("--AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                       \n")
            .append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                         \n")
            .append("AND A.ONLINE_GUB_CODE = B.ONLINE_GUB_CODE                                                   \n")
            .append("AND A.ISSUE_ID = B.ISSUE_ID                                                                 \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            
            
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }       
       
    /**
     * 인터페이스에 처리결과 업데이트(국세청전송 후 처리결과 반영)
     * @param Connection
     */ 
    public boolean tax_result_info_update_error(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO(                                                       \n")
            .append("REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, STATUS_DESC,                          \n")
            .append("REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT, EAI_CDATE, EAI_UDATE                             \n")
            .append(")                                                                                          \n")
            .append("SELECT /*+ ordered */                                                                      \n")
            .append("B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID,                                              \n")
            .append("A.STATUS_CODE,A.STATUS_DESC,                                                               \n")
            //.append("SYSDATE, SYSDATE, A.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, 'X'), NULL, NULL \n")
            .append("SYSDATE, SYSDATE, A.ISSUE_ID, DECODE(B.REL_SYSTEM_ID, 'K1ERP11000', NULL, DECODE(B.ONLINE_GUB_CODE,'3',NULL , 'X'),'X'), NULL, NULL \n")
            .append("FROM  TB_TAX_BILL_ON_BATCH A, IF_TAX_BILL_INFO B                                           \n")
            .append("WHERE A.IO_CODE = '1'                                                                      \n")
            .append("AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')                                      \n")
            .append("--AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                      \n")
            .append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                        \n")
            .append("AND A.ONLINE_GUB_CODE = B.ONLINE_GUB_CODE                                                  \n")
            .append("AND A.ISSUE_ID = B.ISSUE_ID                                                                \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            
            
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    } 
    
    
    /******************관리 태이블  상태 변경 쿼리**************************/  
    
    /**
     * 업데이트 대상 잡기 - 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_ready(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                               \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                  \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                             \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE                                               \n")
            .append("    )                                                                                            \n")
            .append("     SELECT/*+ ORDERED INDEX(A TB_TAX_BILL_INFO_IX2) */                                          \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                        \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                     \n")
            .append("    A.INVOICEE_GUB_CODE,A.STATUS_CODE AS STATUS_CODE_ORG,                                        \n")
            //2012.10.17 etaxconn에서 송신실패인경우는 반영 제외 즉, 재처리한후 반영되도록 수정
            //.append("    DECODE(B.SEND_STATUS,'0','03','1','03','2','03','3','96') AS STATUS_CODE  --전송 결과 상태            \n")
            .append("    DECODE(B.SEND_STATUS,'0','03','1','03','2','03','3','03') AS STATUS_CODE  --전송 결과 상태            \n")
            .append("    FROM  TB_TAX_BILL_INFO A,ET_TIDOC_RESULT_IF B                                                \n")
            .append("    WHERE 1>0                                                                                    \n")
            //.append("    AND A.ONLINE_GUB_CODE='1'                                                                  \n")
            .append("    AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')                                         \n")
            //2011.07.19 수정세금계산서 처리를 위해 처리대상에서  날짜 제한 제거를 위해 조건 변경 처리 PSJ
            //없애는것보다는 있는게 sql plan cost가 7분의1로 줄어듦
            .append("    AND A.ISSUE_DAY > 0       \n")
            //.append("    AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-60,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')       \n")
            .append("    AND A.ELECTRONIC_REPORT_YN = 'N' -- 신고 완료되지 않은 것                                                                                  \n")
            .append("    AND A.ISSUE_ID = B.ISSUE_ID                                                                  \n")
            .append("    AND A.STATUS_CODE <> DECODE(B.SEND_STATUS,'0','03','1','03','2','03','3','96')               \n")
            //2012.10.17 etaxconn에서 송신실패인경우는 반영 제외 즉, 재처리한후 반영되도록 수정
            .append("    AND B.SEND_STATUS <> '3'                                                                     \n")
            .append("    AND B.RESULT_CODE IS NULL  -- 국세청으로부터 결과값을 받지 않은 상태                                                                  \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }             
    

    
    /**
     * 업데이트 대상 잡기 - 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * 솔루션 전송후 XML 파일 

     */ 
    public boolean tb_tax_bill_info_update_topido(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                          \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                             \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                        \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE,STATUS_DESC                              \n")
            .append("    )                                                                                       \n")
            .append("     SELECT/*+ ORDERED INDEX(A TB_TAX_BILL_INFO_IX2) */                                     \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                   \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                \n")
            .append("    A.INVOICEE_GUB_CODE,A.STATUS_CODE, DECODE(B.STATUS,'2','96') AS STATUS_CODE,            \n")
            .append("    ('전송서버오류-'||SUBSTR(B.ERROR_REASON,'1','70')) AS STATUS_DESC                       \n")
            .append("    FROM  TB_TAX_BILL_INFO A,ET_TIDOC_IF B                                                  \n")
            .append("    WHERE 1>0                                                                               \n")
            //.append("    AND A.ONLINE_GUB_CODE='1'                                                               \n")
            .append("    AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3') --20101013                         \n")
            //2011.07.19 수정세금계산서 처리를 위해 처리대상에서  날짜 제한 제거를 위해 조건 변경 처리 PSJ
            //없애는것보다는 있는게 sql plan cost가 7분의1로 줄어듦
            .append("    AND A.ISSUE_DAY > 0       \n")
            //.append("    AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-60,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')  \n")
            .append("    AND A.ELECTRONIC_REPORT_YN = 'N' -- 신고 완료되지 않은 것                               \n")
            .append("    AND A.ISSUE_ID = B.ISSUE_ID                                                             \n")
            .append("    AND A.STATUS_CODE ='03'                                                                 \n")
            .append("    AND B.STATUS = '2'                                                       \n");// ET_TIDOC_IF.STATUS 2:등록실패 
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }             

    
    /**
     * 공가매출에 대해 IF_TAX_BILL_RESULT_INFO 정보를 ERP FI에 연계 하기 위해 
     * ERP FI 연계에 맞게 추가로 IF_TAX_BILL_RESULT_INFO INSERT
     * 2013.11.11 빼빼로DAY
     * 2013.11.15 프로세스 변경으로 필요없게됨 
     */     
//    public boolean tax_result_info_update_copy4FI(Connection con, String rel_system_id)throws SQLException, Exception{
//	
//	boolean bl = true;
//	int rs = 0;
//	PreparedStatement pstm=null;
//	try{
//            StringBuffer sb= new StringBuffer()
//            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO                                                        \n")
//			.append("      (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID,                                             \n")
//			.append("       STATUS_CODE, STATUS_DESC, REGIST_DT,                                                \n")
//			.append("       MODIFY_DT, ISSUE_ID, EAI_STAT,                                                      \n")
//			.append("       EAI_CDATE, EAI_UDATE, TAX_CANCEL_DATA,                                              \n")
//			.append("       ESERO_CREATE_TS, ESERO_FINISH_TS  )                                                 \n")
//   
//            .append("SELECT /*+ ordered */                                                                      \n")
//			.append("      (?, JOB_GUB_CODE, MANAGE_ID,                                             \n")
//			.append("       STATUS_CODE, STATUS_DESC, REGIST_DT,                                                \n")
//			.append("       MODIFY_DT, ISSUE_ID, EAI_STAT,                                                      \n")
//			.append("       EAI_CDATE, EAI_UDATE, TAX_CANCEL_DATA,                                              \n")
//			.append("       ESERO_CREATE_TS, ESERO_FINISH_TS  )                                                 \n")
//            .append("FROM  TB_TAX_BILL_ON_BATCH A, IF_TAX_BILL_RESULT_INFO B                                    \n")
//            .append("WHERE A.IO_CODE = '1'                                                                      \n")
//            //.append("AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')                                     \n")
//            .append("AND A.ONLINE_GUB_CODE='3'                                                                  \n")
//            .append("--AND A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                    \n")
//            .append("AND A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                        \n")
//            .append("AND A.ONLINE_GUB_CODE = B.ONLINE_GUB_CODE                                                  \n")
//            .append("AND A.ISSUE_ID = B.ISSUE_ID                                                                \n")
//            .append("AND B.REL_SYSTEM_ID = 'K1NCIS100o'                                                         \n")
//            .append("AND B.JOB_GUB_CODE='350010'                                                                \n");
//            
//            CommUtil.logWriter(sb.toString(),1);
//            //System.out.println(sb.toString());
//            
//            pstm = con.prepareStatement(sb.toString());
//			pstm.setString(1, rel_system_id);
//            rs = pstm.executeUpdate();
//            if(rs>0){
//        	bl = true;
//            }
//            
//            
//        }catch(SQLException e){
//            bl = false;
//            e.printStackTrace();
//            throw e;
//        }catch(Exception e1){
//            bl = false;            
//    	    e1.printStackTrace();
//    	    throw e1;
//    	}finally{
//            try{
//            	if (pstm != null){
//            	    pstm.close();
//            	}
//            }catch (SQLException e){
//            	e.printStackTrace();
//            }
//    	}
//    	return bl;
//    } 

    
    
    /**
     * 업데이트 대상 잡기 - 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * 솔루션 전송후 XML 파일 생성오류건
     */ 
    public boolean xml_info_tran(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;    
	PreparedStatement pstm=null;

	System.out.println("BatchQuery.java xml_info_tran");

	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_XML_INFO                                                                                        \n")
            .append("(IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ORG_XML_DOC, R_VALUE, REGIST_DT, MODIFY_DT, REGIST_ID, MODIFY_ID)          \n")
            .append("SELECT /*+ ORDERED USE_NL(A C) INDEX(A TB_TAX_BILL_INFO_IX1) INDEX(C ET_TIDOC_ISSUE_UK) */                     \n")
            .append("       A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, C.FILE_CONTENT, C.RVALUE, SYSDATE, SYSDATE, 'BATCH', 'BATCH'   \n")
            .append("  FROM TB_TAX_BILL_INFO A, ET_TIDOC_IF C                                                                       \n")
            .append(" WHERE 1>0                                                                                                     \n")
            .append("   AND A.IO_CODE              = '1'                                                                            \n")
            //.append("   AND A.ONLINE_GUB_CODE      = '1'                                                                            \n")
            .append("   AND (A.ONLINE_GUB_CODE      = '1' OR  A.ONLINE_GUB_CODE = '3') --20101013                                  \n")
            .append("   AND A.STATUS_CODE          = '03'                                                                           \n")
            //2011.07.19 수정세금계산서 처리를 위해 처리대상에서  날짜 제한 제거를 위해 조건 변경 처리 PSJ
            //없애는것보다는 있는게 sql plan cost가 7분의1로 줄어듦
            .append("    AND A.ISSUE_DAY > 0                                                                                        \n")
            //.append("   AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-60,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')                      \n")
            .append("   AND NOT EXISTS (                                                                                            \n")
            .append("                SELECT /*+ NDEX(B TB_XML_INFO_PK) */                                                           \n")
            .append("                       1                                                                                       \n")
            .append("                  FROM TB_XML_INFO B                                                                           \n")
            .append("                 WHERE A.IO_CODE       = B.IO_CODE                                                             \n")
            .append("                   AND A.ISSUE_DAY     = B.ISSUE_DAY                                                           \n")
            .append("                   AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                       \n")
            .append("                )                                                                                              \n")
            .append("   AND A.ISSUE_ID = C.ISSUE_ID                                                                                 \n")
            .append("   AND LENGTH(C.FILE_CONTENT) > 0                                                                              \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }             
    
    
    /**
     * 업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_in_300(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
           StringBuffer sb= new StringBuffer()
           //  ELECTRONIC_REPORT_YN = N 전송동의 되어져있음
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                                           \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                              \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG                                                                        \n")
            .append("    )                                                                                                        \n")
            .append("SELECT /*+ INDEX(A TB_TAX_BILL_INFO_IX1) */                                                                  \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                                    \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                                 \n")
            .append("    A.INVOICEE_GUB_CODE, A.STATUS_CODE                                                                       \n")
            .append("FROM TB_TAX_BILL_INFO A                                                                                      \n")
            .append("WHERE 1>0                                                                                                    \n")
            .append("AND A.ONLINE_GUB_CODE='1'                                                                                    \n")
            .append("AND A.IO_CODE = '2'                                                                                          \n")
            .append("AND (A.ELECTRONIC_REPORT_YN = 'N' OR A.ELECTRONIC_REPORT_YN = 'F')  -- OR 'F' : 미동의건도 검색 20100323추가           \n")
            .append("AND A.STATUS_CODE = '01'                                                                                     \n")
            //2014. 12. 9 수정계산서도 추가로 처리 by 박상종 
            //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')     \n")
            .append("AND (A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')     \n")
            .append("     OR ( A.BILL_TYPE_CODE IN ('0301', '0303', '0304') AND A.ISSUE_DAY >= ? ) \n") // 계산서는 작성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
            .append("     OR ( A.BILL_TYPE_CODE IN ('0401', '0403', '0404') AND A.ISSUE_DT >= ? )) \n")  // 수정계산서는 생성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
            
            
            //* issue_dt 2012.7.31 00:00:00 이후 부터 적용     
            .append("AND A.ISSUE_DT > '20120918000000'                                                                            \n")
            // 서명후 익일까지만 처리 가능 
            .append("AND A.ISSUE_DT > TO_CHAR(SYSDATE - 1 , 'YYYYMMDD')||'000000'                                                 \n")
            .append("AND A.TAX_2ND_SIGN IS NULL                                                                                   \n")
            .append("AND A.INVOICEE_GUB_CODE = '00'                                                                               \n")
            .append("AND EXISTS (                                                                                                 \n")
            .append("            SELECT /*+ ORDERED USE_NL(BB AA) INDEX(BB ETS_TAX_MAIN_INFO_TB_IDX3) */                          \n")
            .append("                   1                                                                                         \n")
            .append("            FROM   ETS_TAX_MAIN_INFO_TB BB,                                                                  \n")
            .append("                   ETS_TAX_META_INFO_TB AA                                                                   \n")
            .append("            WHERE  AA.UUID = BB.UUID                                                                         \n")
            .append("            AND    BB.NTS_ISSUE_ID IS NOT NULL                                                               \n")
            .append("            AND    AA.COMP_CODE    = '00'  --한전것만                                                                                                                   \n")
            .append("            AND    AA.DOC_STATE IN ('CFS','END') --전송대상                                                                                                       \n")
            .append("            AND    AA.EXT_SYSTEM_TYPE = '300' --내선계기                                                                                                             \n")
            .append("            AND    BB.NTS_ISSUE_ID = A.ISSUE_ID                                                              \n")
            .append("            )                                                                                                \n");
            
            //System.out.println(sb.toString());
    		CommUtil.logWriter(sb.toString(),4);

            pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, NOTAXBILL_ESERO_REG_DAY);
			pstm.setString(2, NOTAXBILL_ESERO_REG_DAY+"000000" );
            
            //pstm.setString(1,code);
            rs = pstm.executeUpdate();
            CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }               
    
    
    
    
    
  
    
    /**
     * 업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_in(Connection con, String code)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
           StringBuffer sb= new StringBuffer()
           //  ELECTRONIC_REPORT_YN = N 전송동의 되어져있음
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                                           \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                              \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE                                                            \n")
            .append("    )                                                                                                        \n")
            .append("SELECT /*+ INDEX(A TB_TAX_BILL_INFO_IX1) */                                                                  \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                                    \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                                 \n")
            .append("    A.INVOICEE_GUB_CODE, A.STATUS_CODE, ?                                                                  \n")
            .append("FROM TB_TAX_BILL_INFO A                                                                                      \n")
            .append("WHERE 1>0                                                                                                    \n")
            .append("AND A.ONLINE_GUB_CODE='1'                                                                                    \n")
            .append("AND A.IO_CODE = '2'                                                                                          \n")
            .append("AND (A.ELECTRONIC_REPORT_YN = 'N' OR A.ELECTRONIC_REPORT_YN = 'F')  -- OR 'F' : 미동의건도 검색 20100323추가         \n")
            .append("AND A.STATUS_CODE = '01'                                                                                     \n")
            //2014. 12. 9 계산서도 추가로 처리하면서 조건 삭제 by 박상종 
            //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')     \n")
            //-------------------------------------------------------------------------------------------------------------------------------
            //* ~ 2012.7.1 기준으로 구분 처리 로직 추가  (2012. 7. 10일 쯤 이후 로직 빼도  됨)     
            //.append("AND (   (A.BILL_TYPE_CODE < '0200' AND ((A.ISSUE_DAY < ?) OR (A.ISSUE_DAY >= ? AND A.TAX_2ND_SIGN = 'Y')))   \n")
            //.append("     OR (A.BILL_TYPE_CODE > '0200' AND ((A.ISSUE_DT < ?)  OR (A.ISSUE_DT >= ?  AND A.TAX_2ND_SIGN = 'Y'))))  \n")
            //-------------------------------------------------------------------------------------------------------------------------------
            //2014. 12. 9 계산서도 추가로 처리하면서 조건 변경  by 박상종 
            //.append("AND (   (A.BILL_TYPE_CODE < '0200' AND ((A.ISSUE_DAY < ?) OR (A.ISSUE_DAY >= ? AND A.TAX_2ND_SIGN = 'Y' AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000')))   \n")
            //.append("     OR (A.BILL_TYPE_CODE > '0200' AND ((A.ISSUE_DT < ?)  OR (A.ISSUE_DT >= ?  AND A.TAX_2ND_SIGN = 'Y'  AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000'))))  \n")
            //-------------------------------------------------------------------------------------------------------------------------------
            //2014.12.9 1차 수정 
            //.append("AND (   (A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0301', '0303', '0304' ) AND ((A.ISSUE_DAY < ?) OR (A.ISSUE_DAY >= ? AND A.TAX_2ND_SIGN = 'Y' AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000')))   \n")//(세금)계산서 
            //.append("     OR (A.BILL_TYPE_CODE IN ('0201', '0202', '0203', '0204', '0205', '0401', '0403', '0404' ) AND ((A.ISSUE_DT < ?)  OR (A.ISSUE_DT >= ?  AND A.TAX_2ND_SIGN = 'Y'  AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000'))))  \n")//수정(세금)계산서 
            //-------------------------------------------------------------------------------------------------------------------------------
            //2014.12.9 2차 수정(익일전송이 이미 시행된지 오래 되므로 조건이 필요없음 
            //.append("AND A.TAX_2ND_SIGN = 'Y' AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000'                          \n")
            //-------------------------------------------------------------------------------------------------------------------------------
            //2014.12.29 3차 수정(2차 수정분으로 처리시 송신대상이 아닌 2015.1.1 이전 계산서의 상태값도 03으로 변경되는 찜찜한 상태가됨.
            .append("AND (    A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') -- 일반(수정)세금계산서 \n")
            .append("     OR (A.BILL_TYPE_CODE IN ('0301', '0303', '0304' ) AND A.ISSUE_DAY >= ? ) -- 계산서                              \n")
            .append("     OR (A.BILL_TYPE_CODE IN ('0401', '0403', '0404' ) AND A.ISSUE_DT >= ?  ) -- 수정계산서                \n")
            .append("     )                                                                                                        \n")  
            .append("AND A.TAX_2ND_SIGN = 'Y'                                                                                      \n")
            .append("AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000'                                                  \n")
            //-------------------------------------------------------------------------------------------------------------------------------
            //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                               \n")
            .append("AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                        \n")
            .append("AND EXISTS (                                                                                                 \n")
            .append("            SELECT /*+ ORDERED USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                           \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID AS ISSUE_ID  -- 승인번호                                                   \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP                                                    \n")
            .append("            WHERE  MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                     \n")
            .append("            AND    MST.BLDAT        >= '20100101'                                                            \n")
            .append("            AND    RSP.DEL_FLAG      = '2' --전송대상                                                        \n")
            .append("            AND    MST.ISSUE_ID      =  A.ISSUE_ID                                                           \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                                   \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID ISSUE_ID,                                                                  \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP, XMLEDI_TAX_REP REP                                \n")
            .append("            WHERE  MST.MANAGEMENT_ID   = REP.MANAGEMENT_ID                                                   \n")
            .append("            AND    REP.P_MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                   \n")
            .append("            AND    MST.BLDAT          >= '20100101'                                                          \n")
            .append("            AND    RSP.DEL_FLAG        = '2' --전송대상                                                      \n")
            .append("            AND    MST.ISSUE_ID        = A.ISSUE_ID                                                          \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ ORDERED USE_NL(BB AA) INDEX(BB ETS_TAX_MAIN_INFO_TB_IDX3) */                          \n")
            .append("                   1                                                                                         \n")
            .append("                   --BB.NTS_ISSUE_ID AS ISSUE_ID -- 승인번호                                                 \n")
            .append("                   --DECODE(A.DOC_STATE ,'END','전송대상', 'WM', '진행중', 'SM','진행중', 'SED','진행중', 'CFR','진행중','CFS','진행중', 'DEL', '취소','REJ','취소')  STATE \n")
            .append("            FROM   ETS_TAX_MAIN_INFO_TB BB,                                                                  \n")
            .append("                   ETS_TAX_META_INFO_TB AA                                                                   \n")
            .append("            WHERE  AA.UUID = BB.UUID                                                                         \n")
            .append("            AND    BB.NTS_ISSUE_ID IS NOT NULL                                                               \n")
            //.append("            AND    AA.COMP_CODE    = '00'  --한전것만                                                        \n")
            .append("            AND    AA.COMP_CODE IN ('00','01','03','04','05')                    \n")
            //.append("            AND    AA.DOC_STATE    = 'END' --전송대상                                                        \n")
            .append("            AND    (   (AA.DOC_STATE    = 'END' AND AA.EXT_SYSTEM_TYPE <> '300' ) -- 내선계기 이외는 END 상태만       \n")
            .append("                    OR (AA.DOC_STATE IN( 'CFS','END') AND AA.EXT_SYSTEM_TYPE = '300')) --내선계기는 CFS, END 상태만  \n")
            .append("            AND    BB.NTS_ISSUE_ID = A.ISSUE_ID                                                               \n")
            .append("            )                                                                                                \n");
            
            //System.out.println(sb.toString());
    		CommUtil.logWriter(sb.toString(),4);

            pstm = con.prepareStatement(sb.toString());
            
            pstm.setString(1,code);
            //2014. 12. 9 계산서도 추가로 처리하면서 삭제 by 박상종 
            //pstm.setString(2,RunningScheduler.TAX2NDSIGN_STARTDAY);
            //pstm.setString(3,RunningScheduler.TAX2NDSIGN_STARTDAY);
            //pstm.setString(4,RunningScheduler.TAX2NDSIGN_STARTDAY+"000000");
            //pstm.setString(5,RunningScheduler.TAX2NDSIGN_STARTDAY+"000000");
            
            //2014. 12. 29 3차 수정 계산서도 추가로 처리하면서 수정  by 박상종 
			pstm.setString(2, NOTAXBILL_ESERO_REG_DAY);
			pstm.setString(3, NOTAXBILL_ESERO_REG_DAY+"000000" );

            rs = pstm.executeUpdate();
            CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }               
    

    /**
     * 업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_in_2ndSign(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	int idx = 1;
	PreparedStatement pstm=null;
	try{
           StringBuffer sb= new StringBuffer()
           //  ELECTRONIC_REPORT_YN = N 전송동의 되어져있음
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                                           \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                              \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE , TAX_2ND_SIGN,                                           \n")
            .append("    INVOICER_PARTY_ID,INVOICEE_PARTY_ID                                                                      \n")
            .append("    )                                                                                                        \n")
            .append("SELECT /*+ INDEX(A TB_TAX_BILL_INFO_IX1) */                                                                  \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                                    \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                                 \n")
            .append("    A.INVOICEE_GUB_CODE, A.STATUS_CODE, A.STATUS_CODE,  DECODE(A.ELECTRONIC_REPORT_YN, 'N','N','F','Y',NULL),\n")
            .append("    A.INVOICER_PARTY_ID, A.INVOICEE_PARTY_ID                                                                 \n")
            .append("FROM TB_TAX_BILL_INFO A                                                                                      \n")
            .append("WHERE 1>0                                                                                                    \n")
            .append("AND A.ONLINE_GUB_CODE='1'                                                                                    \n")
            .append("AND A.IO_CODE = '2'                                                                                          \n")
            .append("AND (A.ELECTRONIC_REPORT_YN = 'N' OR A.ELECTRONIC_REPORT_YN = 'F')  -- OR 'F' : 미동의건도 검색 20100323추가         \n")
            .append("AND A.STATUS_CODE = '01'                                                                                     \n")
            //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')     \n")
            //* ~ 2012.7.1 기준으로 구분 처리 로직 추가(2012. 7. 10일 쯤 이후 로직 빼도  됨)     
            .append("AND (   (A.BILL_TYPE_CODE > '0000' AND A.BILL_TYPE_CODE < '0200' AND A.ISSUE_DAY >= ? )--일반세금계산서                 \n")
            .append("     OR (A.BILL_TYPE_CODE > '0200' AND A.BILL_TYPE_CODE < '0300' AND A.ISSUE_DT >= ?  )--수정세금계산서                 \n")
            .append("     OR (A.BILL_TYPE_CODE > '0300' AND A.BILL_TYPE_CODE < '0400' AND A.ISSUE_DAY >= ? )--일반계산서                       \n")
            .append("     OR (A.BILL_TYPE_CODE > '0400' AND A.BILL_TYPE_CODE < '0500' AND A.ISSUE_DT >= ?  ))--수정계산서                     \n")
            .append("AND A.TAX_2ND_SIGN IS NULL                                                                                   \n")
            //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                               \n")
            .append("AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                        \n")
            .append("AND EXISTS (                                                                                                 \n")
            .append("            SELECT /*+ ORDERED USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                           \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID AS ISSUE_ID  -- 승인번호                                                   \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP                                                    \n")
            .append("            WHERE  MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                     \n")
            .append("            AND    MST.BLDAT        >= '20100101'                                                            \n")
            .append("            AND    RSP.DEL_FLAG      = '2' --전송대상                                                        \n")
            .append("            AND    MST.ISSUE_ID      =  A.ISSUE_ID                                                           \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                                   \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID ISSUE_ID,                                                                  \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP, XMLEDI_TAX_REP REP                                \n")
            .append("            WHERE  MST.MANAGEMENT_ID   = REP.MANAGEMENT_ID                                                   \n")
            .append("            AND    REP.P_MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                   \n")
            .append("            AND    MST.BLDAT          >= '20100101'                                                          \n")
            .append("            AND    RSP.DEL_FLAG        = '2' --전송대상                                                      \n")
            .append("            AND    MST.ISSUE_ID        = A.ISSUE_ID                                                          \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ ORDERED USE_NL(BB AA) INDEX(BB ETS_TAX_MAIN_INFO_TB_IDX3) */                          \n")
            .append("                   1                                                                                         \n")
            .append("                   --BB.NTS_ISSUE_ID AS ISSUE_ID -- 승인번호                                                 \n")
            .append("                   --DECODE(A.DOC_STATE ,'END','전송대상', 'WM', '진행중', 'SM','진행중', 'SED','진행중', 'CFR','진행중','CFS','진행중', 'DEL', '취소','REJ','취소')  STATE \n")
            .append("            FROM   ETS_TAX_MAIN_INFO_TB BB,                                                                  \n")
            .append("                   ETS_TAX_META_INFO_TB AA                                                                   \n")
            .append("            WHERE  AA.UUID = BB.UUID                                                                         \n")
            .append("            AND    BB.NTS_ISSUE_ID IS NOT NULL                                                               \n")
            //.append("            AND    AA.COMP_CODE    = '00'  --한전것만                                                         \n")
            .append("            AND    AA.COMP_CODE IN ('00','01','03','04','05')                     \n")
            .append("            AND    AA.DOC_STATE    = 'END' --전송대상                                                             \n")
            //.append("            AND    AA.EXT_SYSTEM_TYPE <> '300' --내선계기는 제외                                          \n")
            .append("            AND   ( AA.EXT_SYSTEM_TYPE <> '300' OR                                                           \n")
       		.append("                  ( AA.EXT_SYSTEM_TYPE = '300' AND A.ISSUE_DT <'20120918000000' ))                           \n")
            .append("            AND    BB.NTS_ISSUE_ID = A.ISSUE_ID                                                              \n")
            .append("            )                                                                                                \n")
            .append("            AND ROWNUM < 200 -- 사이버지점 테이블 insert 커서 에러를 막기위해 제한  2016.10.25 100 => 200으로 늘림                                                                        \n");
            //System.out.println(sb.toString());
    		CommUtil.logWriter(sb.toString(),4);

            pstm = con.prepareStatement(sb.toString());
            
    		CommUtil.logWriter("TAX2NDSIGN_STARTDAY:"+RunningScheduler.TAX2NDSIGN_STARTDAY,4);
            //pstm.setString(idx++,code);
            pstm.setString(idx++,RunningScheduler.TAX2NDSIGN_STARTDAY);
            pstm.setString(idx++,RunningScheduler.TAX2NDSIGN_STARTDAY+"000000");
            pstm.setString(idx++,RunningScheduler.TAX2NDSIGN_STARTDAY);
            pstm.setString(idx++,RunningScheduler.TAX2NDSIGN_STARTDAY+"000000");
            
            rs = pstm.executeUpdate();
            CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }               
    
    /**
     * 한전전자조달에 등록된 사용자의 메일/SMS 수신 체크한 정보를 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean insert_srm_info(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;        
	try{
           StringBuffer sb= new StringBuffer();
           //  ELECTRONIC_REPORT_YN = N 전송동의 되어져있음

           sb.append("INSERT INTO  TB_SRM_USER_TBL_ON_BATCH(                                    \n");
            sb.append("    BUSINESS_NO, USER_NAME, MOBILE, EMAIL, REG_DT                         \n");
            sb.append("    )                                                                     \n");
            //sb.append("SELECT/*+ driving_site(B) */                                              \n");
            //sb.append("    DISTINCT A.INVOICER_PARTY_ID, C.USER_NAME, C.MOBILE, C.EMAIL, SYSDATE \n");
            //2016.1.11 DBLINK -->VIEW  변경전 
            //  sb.append("    FROM TB_TAX_BILL_ON_BATCH A, TP_SUPPLIER_TBL@USER_LINK B              \n");
            //  sb.append("       , TP_EXUSER_TBL@USER_LINK C                                        \n");
            //2016.1.11 LINK 변경후 
            //sb.append("    FROM TB_TAX_BILL_ON_BATCH A, TP_SUPPLIER_TBL_VIEW B              \n");
            //sb.append("       , TP_EXUSER_TBL_VIEW C                                        \n");
            
            //sb.append("    WHERE B.SUPPLIER_NO = C.SUPPLIER_NO                                   \n");
    	    //sb.append("      AND B.STATUS != 'D'                                                 \n");
    	    //sb.append("      AND C.STATUS != 'D'                                                 \n");
    	    //sb.append("      AND C.TAX_SMS_YN = 'Y'                                              \n");
            //2016.1.11 DBLINK -->VIEW  변경전 
    	    //sb.append("      AND B.BUSINESS_NO = ENCODE_SF@USER_LINK(A.INVOICER_PARTY_ID)        \n");

    	    // 차세대 업체정보 TP_SUPPLIER_TBL_VIEW 의  B.BUSINESS_NO
    	    //if(RunningScheduler.BUSI_ENC.equals("1")){
    	       //2016.1.11 LINK 변경후
    	    //  sb.append("      AND B.BUSINESS_NO = ENCODE_SF64@D_EDI2SRM(A.INVOICER_PARTY_ID)        \n");    

    	    //}else{
    	     //  sb.append("      AND B.BUSINESS_NO = A.INVOICER_PARTY_ID        \n");
    	   // }   
            
            
            
           
          //20180219  srm 쿼리 변경  ( srm 테이블로 직접  조회 )  유종일
            sb.append("SELECT/*+ driving_site(V) */	\n");                                              
            sb.append("    DISTINCT A.INVOICER_PARTY_ID , UA.NAME USER_NAME, UA.MOBILENO MOBILE, UA.EMAIL EMAIL, SYSDATE	\n");
            sb.append("    FROM  vendor@D_EDI2SRM V,			\n");
            sb.append("       useraccount@D_EDI2SRM UA ,	\n");
            sb.append("         TB_TAX_BILL_ON_BATCH A                \n");
            sb.append("    WHERE UA.USERTYPE='External'		\n");
            sb.append("       AND UA.ACCEPTSMSYN = 1	\n");
            sb.append("       AND UA.MEMBERSTATE != 'D'		\n");
            sb.append("       AND UA.vendor = V.id	\n");
            sb.append("       AND V.STATUS != 'D'	\n");
    	    
    	    if(RunningScheduler.BUSI_ENC.equals("1")){
     	       //2016.1.11 LINK 변경후
    	    	sb.append("        AND V.registrationno = ENCODE_SF64@D_EDI2SRM(A.INVOICER_PARTY_ID)		\n");
     	    }else{
     	    	sb.append("        AND V.registrationno = A.INVOICER_PARTY_ID	\n");
     	    } 
    	    
    	    
    	    System.out.println("BUSI_ENC 1:암호화  0:비암호화 :"+RunningScheduler.BUSI_ENC);
    	    
    	             //System.out.println(sb.toString());
    		CommUtil.logWriter("=============================================",4);
    		CommUtil.logWriter("한전전자조달에 등록된 사용자의 메일/SMS 수신 체크한 정보를 TEMP에 넣는다",4);
    		CommUtil.logWriter("=============================================",4);
    	    CommUtil.logWriter(sb.toString(),4);

            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            CommUtil.logWriter("insert_srm_info pstm.executeUpdate 갯수:"+rs,4);
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }               
    
    
    
    /**
     * 업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_in1(Connection con, String code)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()

            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                                           \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                              \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                                         \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE                                                           \n")
            .append("    )                                                                                                        \n")
            .append("SELECT /*+ INDEX(A TB_TAX_BILL_INFO_IX1) */                                                                  \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                                    \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                                 \n")
            .append("    A.INVOICEE_GUB_CODE, A.STATUS_CODE, ?                                                                    \n")
            .append("FROM TB_TAX_BILL_INFO A                                                                                      \n")
            .append("WHERE 1>0                                                                                                    \n")
            .append("AND A.ONLINE_GUB_CODE='1'                                                                                    \n")
            .append("AND A.IO_CODE = '2'                                                                                          \n")
            .append("AND A.ELECTRONIC_REPORT_YN = 'N'                                                                             \n")
            .append("AND A.STATUS_CODE = '02'                                                                                     \n")
            //.append("AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-30,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')                       \n")
            //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                               \n")
            .append("AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                        \n")
            .append("AND EXISTS (                                                                                                 \n")
            .append("            SELECT /*+ ORDERED USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                           \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID AS ISSUE_ID  -- 승인번호                                                   \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP                                                    \n")
            .append("            WHERE  MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                     \n")
            .append("            AND    MST.BLDAT        >= '20100101'                                                            \n")
            .append("            AND    RSP.DEL_FLAG      = '2' --전송대상                                                        \n")
            .append("            AND    MST.ISSUE_ID      =  A.ISSUE_ID                                                           \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                                   \n")
            .append("                   1                                                                                         \n")
            .append("                   --MST.ISSUE_ID ISSUE_ID,                                                                  \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP, XMLEDI_TAX_REP REP                                \n")
            .append("            WHERE  MST.MANAGEMENT_ID   = REP.MANAGEMENT_ID                                                   \n")
            .append("            AND    REP.P_MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                   \n")
            .append("            AND    MST.BLDAT          >= '20100101'                                                          \n")
            .append("            AND    RSP.DEL_FLAG        = '2' --전송대상                                                      \n")
            .append("            AND    MST.ISSUE_ID        = A.ISSUE_ID                                                          \n")
            .append("            UNION ALL                                                                                        \n")
            .append("            SELECT /*+ ORDERED USE_NL(BB AA) INDEX(BB ETS_TAX_MAIN_INFO_TB_IDX3) */                          \n")
            .append("                   1                                                                                         \n")
            .append("                   --BB.NTS_ISSUE_ID AS ISSUE_ID -- 승인번호                                                 \n")
            .append("                   --DECODE(A.DOC_STATE ,'END','전송대상', 'WM', '진행중', 'SM','진행중', 'SED','진행중', 'CFR','진행중','CFS','진행중', 'DEL', '취소','REJ','취소')  STATE \n")
            .append("            FROM   ETS_TAX_MAIN_INFO_TB BB,                                                                  \n")
            .append("                   ETS_TAX_META_INFO_TB AA                                                                   \n")
            .append("            WHERE  AA.UUID = BB.UUID                                                                         \n")
            .append("            AND    BB.NTS_ISSUE_ID IS NOT NULL                                                               \n")
            //.append("            AND    AA.COMP_CODE    = '00'  --한전것만                                                        \n")
            .append("            AND    AA.COMP_CODE IN ('00','01','03','04','05')                    \n")
            //.append("            AND    AA.DOC_STATE    = 'END' --전송대상                                                        \n")
            .append("            AND    (   (AA.DOC_STATE    = 'END' AND AA.EXT_SYSTEM_TYPE <> '300' ) -- 내선계기 이외는 END 상태만       \n")
            .append("                    OR (AA.DOC_STATE IN( 'CFS','END') AND AA.EXT_SYSTEM_TYPE = '300')) --내선계기는 CFS, END 상태만  \n")
            .append("            AND    BB.NTS_ISSUE_ID = A.ISSUE_ID                                                              \n")
            .append("            )                                                                                                \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            
            pstm.setString(1,code);
            
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }               

    
    /**
     * 업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_delete(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()

            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                                             \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                                \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                                           \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE                                                             \n")
            .append("    )                                                                                                          \n")
            .append("SELECT /*+ INDEX(A TB_TAX_BILL_INFO_IX1) */                                                                    \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                                      \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                                   \n")
            .append("    A.INVOICEE_GUB_CODE, A.STATUS_CODE, '98'                                                                   \n")
            .append("FROM TB_TAX_BILL_INFO A                                                                                        \n")
            .append("WHERE 1>0                                                                                                      \n")
            .append("AND A.ONLINE_GUB_CODE='1'                                                                                      \n")
            .append("AND A.IO_CODE = '2'                                                                                            \n")
            .append("AND (A.ELECTRONIC_REPORT_YN = 'N' OR A.ELECTRONIC_REPORT_YN = 'F')                                             \n")
            //.append("AND A.ELECTRONIC_REPORT_YN = 'N'                                                                               \n")
            .append("AND A.STATUS_CODE = '01'                                                                                       \n")
             //2015. 1. 14 계산서도 추가로 처리 아래  BILL_TYPE_CODE 구분 처리 comment처리 by 박상종 
            //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205')       \n")
            .append("AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-60,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')                         \n")
            //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                                 \n")
            .append("AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                        \n")
            .append("AND EXISTS (                                                                                                   \n")
            .append("            SELECT /*+ ORDERED USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                             \n")
            .append("                   1                                                                                           \n")
            .append("                   --MST.ISSUE_ID AS ISSUE_ID  -- 승인번호                                                     \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE   \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP                                                      \n")
            .append("            WHERE  MST.MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                       \n")
            .append("            AND    MST.BLDAT        >= '20100101'                                                              \n")
            .append("            AND    RSP.DEL_FLAG      IN ('1','3') --취소대상                                                   \n")
            .append("            AND    MST.ISSUE_ID      =  A.ISSUE_ID                                                             \n")
            .append("            UNION ALL                                                                                          \n")
            .append("            SELECT /*+ USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */                                     \n")
            .append("                   1                                                                                           \n")
            .append("                   --MST.ISSUE_ID ISSUE_ID,                                                                    \n")
            .append("                   --DECODE(RSP.DEL_FLAG, '1', '취소', '2', '전송대상', '3', '취소', '4', '진행중') AS STATE   \n")
            .append("            FROM   XMLEDI_TAX_MST MST, XMLEDI_TAX_RSP RSP, XMLEDI_TAX_REP REP                                  \n")
            .append("            WHERE  MST.MANAGEMENT_ID   = REP.MANAGEMENT_ID                                                     \n")
            .append("            AND    REP.P_MANAGEMENT_ID = RSP.MANAGEMENT_ID                                                     \n")
            .append("            AND    MST.BLDAT          >= '20100101'                                                            \n")
            .append("            AND    RSP.DEL_FLAG       IN ('1','3') --취소대상                                                  \n")
            .append("            AND    MST.ISSUE_ID        = A.ISSUE_ID                                                            \n")
            .append("            UNION ALL                                                                                          \n")
            .append("            SELECT /*+ ORDERED USE_NL(BB AA) INDEX(BB ETS_TAX_MAIN_INFO_TB_IDX3) */                            \n")
            .append("                   1                                                                                           \n")
            .append("                   --BB.NTS_ISSUE_ID AS ISSUE_ID -- 승인번호                                                   \n")
            .append("                   --DECODE(A.DOC_STATE ,'END','전송대상', 'WM', '진행중', 'SM','진행중', 'SED','진행중', 'CFR','진행중','CFS','진행중', 'DEL', '취소','REJ','취소')  STATE \n")
            .append("            FROM   ETS_TAX_MAIN_INFO_TB BB,                                                                    \n")
            .append("                   ETS_TAX_META_INFO_TB AA                                                                     \n")
            .append("            WHERE  AA.UUID = BB.UUID                                                                           \n")
            .append("            AND    BB.NTS_ISSUE_ID IS NOT NULL                                                                 \n")
            //.append("            AND    AA.COMP_CODE    = '00'  --한전것만                                                          \n")
            .append("            AND    AA.COMP_CODE IN ('00','01','03','04','05')                    \n")
            .append("            AND    AA.DOC_STATE    IN ('DEL','REJ') --취소대상                                                 \n")
            .append("            AND    BB.NTS_ISSUE_ID = A.ISSUE_ID                                                                \n")                                                                                              
            .append("			UNION ALL                                                                   	\n")
            .append("			-- EDI 전송오류  20110127                                                                       											\n") 
			.append("			SELECT 1                                                                    	\n")                        
			.append("			FROM XMLEDI_TAX_MST_TEMP MST                                                	\n")
			.append("			WHERE MST.BLDAT >= '20100101'                                               	\n")               
			.append("			AND MST.ISSUE_ID = A.ISSUE_ID                                               	\n")
			.append("			UNION ALL                                                                   	\n")
			.append("			--조합 반려   20110127                                                                  												\n")
			.append("			SELECT 1  /*+ ORDERED USE_NL(MST RSP) INDEX(MST XMLEDI_TAX_MST_IDX_04) */   	\n")                                                                                         
			.append("			FROM XMLEDI_TAX_MST MST                                                     	\n")
			.append("			WHERE MST.BLDAT >= '20100101'                                               	\n")
			.append("			AND VIEW_DOC_STAT = '반려' -- 조합이 반려한 세금계산서                                  					\n")
			.append("			AND MST.ISSUE_ID = A.ISSUE_ID                                              		\n")
			.append("           )                                                                              \n");                                                                                              
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }       
    
    
    
    
    
    
    
    
    public boolean trun_up_temp(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("TRUNCATE TABLE TB_TAX_BILL_ON_BATCH     \n");
            
            CommUtil.logWriter(sb.toString(),1);
    //        System.out.println(sb.toString());		
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
            	
    	}
    	return bl;
    }   
    
    public boolean trun_up_srm_user_temp(Connection con)throws SQLException, Exception{
    	
    	boolean bl = true;
    	PreparedStatement pstm=null;
    	try{
                StringBuffer sb= new StringBuffer()
                .append("TRUNCATE TABLE TB_SRM_USER_TBL_ON_BATCH     \n");
                
                CommUtil.logWriter(sb.toString(),1);
        //        System.out.println(sb.toString());		
                
                pstm = con.prepareStatement(sb.toString());
                pstm.executeUpdate();
            }catch(SQLException e){
                bl = false;
                e.printStackTrace();
                throw e;
            }catch(Exception e1){
                bl = false;            
        	    e1.printStackTrace();
        	    throw e1;
        	}finally{
        	    close(pstm);
                	
        	}
        	return bl;
        }   
    
    


    /**
     * 업데이트 대상 잡기 - 업데이트 할 내용이 있으면 TEMP에 넣는다.
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_ready2(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TB_TAX_BILL_ON_BATCH(                                                               \n")
            .append("    IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,                                  \n")
            .append("    JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,                             \n")
            //.append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE                                             \n")
            .append("    INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE, STATUS_DESC,                                 \n")
            .append("    TAX_2ND_SIGN, ESERO_CREATE_TS, ESERO_FINISH_TS )                                                                               \n")
            .append("     SELECT /*+ ordered */                                                                       \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, A.ISSUE_ID, A.REL_SYSTEM_ID,                        \n")
            .append("    A.JOB_GUB_CODE, A.ELECTRONIC_REPORT_YN, A.ADD_TAX_YN, A.ONLINE_GUB_CODE,                     \n")
            .append("    A.INVOICEE_GUB_CODE,A.STATUS_CODE AS STATUS_CODE_ORG,                                        \n")
            .append("    DECODE(B.RESULT_CODE,'SUC001','04','99') AS STATUS_CODE,  --전송 결과 상태                                             \n")
            .append("    B.RESULT_CODE AS STATUS_DESC,  --전송 결과 상태 국세청 실제코드값                                                                       \n")
            .append("    A.TAX_2ND_SIGN AS TAX_2ND_SIGN,                                                               \n")
            // 국세청 신고 성공인 경우만 신고일시 등록(신고 실패인경우 신고일시 등록시 신고성공으로 오해할까봐)
            .append("    DECODE(B.RESULT_CODE,'SUC001',B.CREATE_TS, NULL) AS ESERO_CREATE_TS,                                                                     \n")
            .append("    DECODE(B.RESULT_CODE,'SUC001',B.FINISH_TS, NULL) AS ESERO_FINISH_TS                                                                     \n")
            .append("    FROM  TB_TAX_BILL_INFO A,ET_TIDOC_RESULT_IF B                                                \n")
            .append("    WHERE A.IO_CODE IN ('1','2')                                                                 \n")
            .append("    AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3') --20101013                              \n")
            .append("    AND A.STATUS_CODE NOT IN('04')  --신고 완료되지 않은 것                                      \n")
            //.append("    AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-60,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')       \n") // 30 -> 40 일로 변경
            //2011.07.19 수정세금계산서 처리를 위해 처리대상에서  날짜 제한 제거를 위해 조건 변경 처리 PSJ
            //없애는것보다는 있는게 sql plan cost가 7분의1로 줄어듦
            .append("    AND A.ISSUE_DAY > 0       \n")
            //.append("    AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-210,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')       \n") // 60 -> 210 일로 변경(수정세금계산서처리)
            .append("    AND A.ELECTRONIC_REPORT_YN = 'N' -- 신고 완료되지 않은 것                                    \n")
            .append("    AND A.ISSUE_ID = B.ISSUE_ID                                                                  \n")
            .append("    AND A.STATUS_CODE <> DECODE(B.RESULT_CODE,'SUC001','04','99')                                \n")
            .append("    AND B.RESULT_CODE IS NOT NULL  -- 국세청으로부터 결과값을 받은 상태                          \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            
            if(rs>0){
        	bl = true;
            }
            System.out.println(bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }  
    
    
    
    
    
    
    
    
    
    
    /**
     * 관리테이블에 전송결과 업데이트
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer();
            sb.append("UPDATE /*+ bypass_ujvc */                                   \n");
            sb.append("(                                                           \n");
            sb.append("    SELECT /*+ ordered */                                   \n");
            sb.append("    A.STATUS_CODE AS STATUS,                                \n");
            sb.append("    A.STATUS_DESC AS STATUS_DESC,                           \n");
            sb.append("    B.STATUS_CODE,                                          \n");
            sb.append("    DECODE(A.STATUS_CODE,'04','Y','N') AS YN,               \n");
            sb.append("    B.ELECTRONIC_REPORT_YN,                                 \n");
            sb.append("    B.ESERO_RTN_CODE,                                       \n");
            sb.append("    A.ESERO_CREATE_TS AS E_CREATE,                          \n");
            sb.append("    A.ESERO_FINISH_TS AS E_FINISH,                          \n");
            sb.append("    B.ESERO_CREATE_TS,                                      \n");
            sb.append("    B.ESERO_FINISH_TS                                       \n");
            if(RunningScheduler.DB_ENC.equals("1")){         
                sb.append("    FROM TB_TAX_BILL_ON_BATCH A,TB_TAX_BILL_INFO_ENC B  \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("    FROM TB_TAX_BILL_ON_BATCH A,TB_TAX_BILL_INFO B      \n");
            }else{
                sb.append("    FROM TB_TAX_BILL_ON_BATCH A,TB_TAX_BILL_INFO B      \n");
            }
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            sb.append("    WHERE A.IO_CODE IN('1','2')                             \n");
            sb.append("    AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')    \n");
            sb.append("    AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                   \n");
            sb.append("    AND A.IO_CODE = B.IO_CODE                               \n");
            sb.append("    AND A.ISSUE_DAY = B.ISSUE_DAY                           \n");
            sb.append("    AND B.STATUS_CODE NOT IN('04')                          \n");
            sb.append("    AND A.STATUS_CODE_ORG = B.STATUS_CODE                   \n");
            sb.append(")                                                           \n");
            sb.append("SET STATUS_CODE  = STATUS, ELECTRONIC_REPORT_YN = YN,       \n");
            sb.append(" ESERO_RTN_CODE = STATUS_DESC,                              \n");
            sb.append(" ESERO_CREATE_TS = E_CREATE,                                \n");
            sb.append(" ESERO_FINISH_TS = E_FINISH                                 \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }          
    
 
    
    /**
     * 전송결과 업데이트   [ 사용 안하는 것 같음 ]
     * @param Connection
     */ 
    public boolean tb_tax_bill_info_update_(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer();
            sb.append("UPDATE /*+ bypass_ujvc */                                                                     \n");
            sb.append("(                                                                                             \n");
            sb.append("    SELECT/*+ ordered */                                                                      \n");
            sb.append("    DECODE(B.RESULT_CODE,'SUC001','04','99') AS STATUS,                                       \n");
            sb.append("    A.STATUS_CODE                                                                             \n");
            if(RunningScheduler.DB_ENC.equals("1")){         
                sb.append("    FROM  TB_TAX_BILL_INFO_ENC A, ET_TIDOC_RESULT_IF B                                    \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("    FROM  TB_TAX_BILL_INFO A, ET_TIDOC_RESULT_IF B                                        \n");
            }else{
                sb.append("    FROM  TB_TAX_BILL_INFO A, ET_TIDOC_RESULT_IF B                                        \n");
            }
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            sb.append("    WHERE A.IO_CODE IN ('1','2')                                                              \n");
            sb.append("    AND A.ONLINE_GUB_CODE='1'                                                                 \n");
            sb.append("    AND A.STATUS_CODE NOT IN('04')                                                            \n");
            sb.append("    AND A.ISSUE_DAY BETWEEN TO_CHAR(SYSDATE-30,'YYYYMMDD') AND TO_CHAR(SYSDATE,'YYYYMMDD')    \n");
            sb.append("    AND A.ELECTRONIC_REPORT_YN = 'N'                                                          \n");
            sb.append("    AND A.ISSUE_ID = B.ISSUE_ID                                                               \n");
            sb.append("    AND B.RESULT_CODE IS NOT NULL                                                             \n");
            sb.append(")                                                                                             \n");
            sb.append("SET STATUS_CODE = STATUS                                                                      \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
            
            sb.delete(0, sb.length());
            sb.append("");
		
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }     
    

    
    /**
     * 관리 이력 테이블에 업데이트 
     * @param Connection
     * 상태코드가 01 인 접수완료한 것만 업데이트 한다.
     */ 
    public boolean tb_status_hist_up01(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("UPDATE /*+ bypass_ujvc */                                        \n")
            .append("(                                                                \n")
            .append("    SELECT /*+ ordered */                                        \n")
            .append("    TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') END_DT,B.AVL_END_DT      \n")
            .append("    FROM  TB_TAX_BILL_ON_BATCH A, TB_STATUS_HIST B               \n")
            .append("    WHERE A.IO_CODE = B.IO_CODE                                  \n")
            .append("    AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                        \n")
            //.append("    AND B.ISSUE_DAY = B.ISSUE_DAY                                \n")
            .append("    AND A.ISSUE_DAY = B.ISSUE_DAY                                \n")
            .append("    AND B.AVL_END_DT = '99991231235959'                          \n")
            .append(")                                                                \n")
            .append("SET AVL_END_DT = END_DT                                          \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs<1){
                bl=false;
            }
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }     
        
    
    
    /**
     * 관리 이력 테이블에 인서트 
     * @param Connection
     * 상태코드 03 or 99상태를 인서트 한다.
     */ 
    public boolean tb_status_hist_in03(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
    StringBuffer sb= new StringBuffer()
    .append("INSERT INTO TB_STATUS_HIST                                                                     \n")
    .append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, AVL_BEGIN_DT,AVL_END_DT,                               \n")
    .append("    SEQ_NO, STATUS_CODE, REGIST_DT, REGIST_ID)                                                 \n")
    .append("SELECT /*+ ordered */                                                                          \n")
    .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID,                                                   \n")
    .append("    TO_CHAR(SYSDATE + INTERVAL '00:01' MINUTE TO SECOND,'YYYYMMDDHH24MISS') , '99991231235959',\n")
    .append("    MAX(B.SEQ_NO)+1 AS SEQ_NO, A.STATUS_CODE, SYSDATE, 'BATCH'                                 \n")
    .append("FROM  TB_TAX_BILL_ON_BATCH A, TB_STATUS_HIST B                                                 \n")
    .append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                        \n")
    .append("AND A.IO_CODE = B.IO_CODE                                                                      \n")
    .append("AND B.ISSUE_DAY = A.ISSUE_DAY																	\n")
    .append("GROUP BY A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID,A.STATUS_CODE									\n");
	try{
            
            //System.out.println(sb.toString());
    		CommUtil.logWriter(String.valueOf(sb),1);
           
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
    		CommUtil.logWriter(String.valueOf(sb),4);
    		CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    		CommUtil.logWriter(String.valueOf(sb),4);
    		CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
        		CommUtil.logWriter(String.valueOf(sb),4);
        		CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }     
     

    
    
    /**
     * 관리 이력 테이블에 업데이트 
     * @param Connection
     * 상태코드가 01 아 아닌 것 중 처리결과가 있는 것만 업데이트 한다.
     */ 
    public boolean tb_status_hist_up03(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	int rs = 0;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer();
            sb.append("UPDATE /*+ bypass_ujvc */                                                  \n");
            sb.append("(                                                                          \n");
            sb.append("    SELECT                                                                 \n");
            sb.append("    TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') END_DT,B.AVL_END_DT                \n");
       		if(RunningScheduler.DB_ENC.equals("1")){         
       		 sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO_ENC C    \n");
  		    }else if(RunningScheduler.DB_ENC.equals("0")){                  
  		    	 sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C    \n");
    		}else{
    			 sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C    \n");
    		}
            // sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C        \n");
            sb.append("    WHERE A.ISSUE_ID = C.ISSUE_ID                                          \n");
            sb.append("    AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID                                  \n");
            sb.append("    AND B.IO_CODE = C.IO_CODE                                              \n");
            sb.append("    AND B.ISSUE_DAY = C.ISSUE_DAY                                          \n");
            sb.append("    AND A.RESULT_CODE IS NOT NULL                                          \n");
            sb.append("    AND B.STATUS_CODE  IN ('03','96','99')                                 \n");
            sb.append("    AND B.AVL_END_DT = '99991231235959'                                    \n");
            sb.append("    AND A.CREATE_TS BETWEEN (SYSDATE - 7) AND SYSDATE                      \n");
            sb.append(")                                                                          \n");
            sb.append("SET AVL_END_DT = END_DT                                                    \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs<1){
                bl=false;
            }		
		
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    
           
    
 
    /**
     * 관리 이력 테이블에 인서트 
     * @param Connection
     * 상태코드 04 or 99상태를 인서트 한다.
     */ 
    public boolean tb_status_hist_in04(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer();
            sb.append("INSERT INTO TB_STATUS_HIST                                                                            \n");
            sb.append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, AVL_BEGIN_DT,AVL_END_DT,                                      \n");
            sb.append("    SEQ_NO, STATUS_CODE, REGIST_DT, REGIST_ID)                                                        \n");
            sb.append("SELECT                                                                                                \n");
            sb.append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID,                                                          \n");
            sb.append("    TO_CHAR(SYSDATE + INTERVAL '00:01' MINUTE TO SECOND,'YYYYMMDDHH24MISS') , '99991231235959',       \n");
            sb.append("    MAX(B.SEQ_NO)+1 AS SEQ_NO, DECODE(C.RESULT_CODE,'SUC001','04','99'), A.REGIST_DT, A.REGIST_ID     \n");
    		if(RunningScheduler.DB_ENC.equals("1")){         
    			sb.append("FROM  TB_TAX_BILL_INFO_ENC A, TB_STATUS_HIST B,ET_TIDOC_RESULT_IF C                                       \n");
  		    }else if(RunningScheduler.DB_ENC.equals("0")){                  
  		    	sb.append("FROM  TB_TAX_BILL_INFO A, TB_STATUS_HIST B,ET_TIDOC_RESULT_IF C                                       \n");
    		}else{
    			sb.append("FROM  TB_TAX_BILL_INFO A, TB_STATUS_HIST B,ET_TIDOC_RESULT_IF C                                       \n");
    		}
    		sb.append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                               \n");
    		sb.append("AND A.ISSUE_ID = C.ISSUE_ID                                                                           \n");
            sb.append("AND A.IO_CODE = B.IO_CODE                                                                             \n");
            sb.append("AND C.RESULT_CODE IS NOT NULL                                                                         \n");
            sb.append("AND B.ISSUE_DAY = A.ISSUE_DAY                                                                         \n");
            sb.append("AND B.STATUS_CODE NOT IN ('04')                                                                       \n");
            sb.append("AND B.BIZ_MANAGE_ID NOT IN                                                                            \n");
            sb.append("(SELECT BIZ_MANAGE_ID FROM TB_STATUS_HIST WHERE AVL_END_DT = '99991231235959' GROUP BY BIZ_MANAGE_ID) \n");	
            sb.append("GROUP BY A.BIZ_MANAGE_ID,A.IO_CODE, A.ISSUE_DAY,                                                      \n");
            sb.append("    A.BIZ_MANAGE_ID,                                                                                  \n");
            sb.append("    DECODE(C.RESULT_CODE,'SUC001','04','99'),A.REGIST_DT, A.REGIST_ID                                 \n");
            CommUtil.logWriter(sb.toString(),1);
           // System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }     
    
 
    /**
     * 관리 이력 테이블에 업데이트 
     * @param Connection
     * 상태코드가 04 인 것 중  처리결과가 있는 것만 업데이트 한다.
     */ 
    public boolean tb_status_hist_up04(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("UPDATE /*+ bypass_ujvc */									\n")
            .append("(															\n")
            .append("SELECT /*+ ordered */										\n")
            .append("TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') END_DT,B.AVL_END_DT	\n")
            .append("FROM  TB_TAX_BILL_ON_BATCH A, TB_STATUS_HIST B				\n")
            .append("WHERE A.IO_CODE = B.IO_CODE								\n")
            .append("AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID						\n")
            .append("AND A.STATUS_CODE = '04'									\n")
            .append("AND A.ISSUE_DAY = B.ISSUE_DAY								\n")
            .append("AND B.AVL_END_DT = '99991231235959'						\n")
            .append(")															\n")
            .append("SET AVL_END_DT = END_DT									\n"); 
           
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }    
    
 
    
    /**
     * 관리 이력 테이블에 업데이트 
     * @param Connection
     * 상태코드가 99 인 것 중  처리결과가 있는 것만 업데이트 한다.
     */ 
    public boolean tb_status_hist_up99(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer();
            sb.append("UPDATE /*+ bypass_ujvc */                                                  \n");
            sb.append("(                                                                          \n");
            sb.append("    SELECT                                                                 \n");
            sb.append("    TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') END_DT,B.AVL_END_DT                \n");
    		if(RunningScheduler.DB_ENC.equals("1")){         
    			sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO_ENC C   \n");
  		    }else if(RunningScheduler.DB_ENC.equals("0")){                  
  		    	sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C       \n");
    		}else{
    			sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C       \n");
    		}
            //sb.append("    FROM  ET_TIDOC_RESULT_IF A, TB_STATUS_HIST B,TB_TAX_BILL_INFO C        \n");
            sb.append("    WHERE A.ISSUE_ID = C.ISSUE_ID                                          \n");
            sb.append("    AND B.BIZ_MANAGE_ID = C.BIZ_MANAGE_ID                                  \n");
            sb.append("    AND B.IO_CODE = C.IO_CODE                                              \n");
            sb.append("    AND B.ISSUE_DAY = C.ISSUE_DAY                                          \n");
            sb.append("    AND A.RESULT_CODE = 'SUC001'                                           \n");
            sb.append("    AND B.STATUS_CODE  IN ('96','99')                                      \n");
            sb.append("    AND B.AVL_END_DT = '99991231235959'                                    \n");
            sb.append("    AND A.CREATE_TS BETWEEN (SYSDATE - 7) AND SYSDATE                      \n");
            sb.append(")                                                                          \n");
            sb.append("SET AVL_END_DT = END_DT                                                    \n");
            
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
            }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }    
 
    /**
     * 국세청신고 결과 폐업사업자 오류시 TAX_BIZ_CONFIRM_T(폐업사업자 관리 정보)에 등록 
	 * 2011.10.21 ADD BY CONAN
     * @param Connection
     */ 
    public boolean tax_biz_confirm_insert(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	int rs = 0; 
	PreparedStatement pstm=null;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  TAX_BIZ_CONFIRM_T(                               \n")
            .append("    CON_SEQ, CON_DATE, CON_BIZ_NO, CON_RSLT_CODE, CON_RECV_IP,\n")
            .append("    CON_RECV_CODE, REGIST_DT, CON_DESC )                      \n")
            .append("    SELECT                                                    \n")
            .append("    SQ_CONFIRM_ID.NEXTVAL,                                    \n")
            .append("    SYSDATE,                                                  \n")
            .append("    B.INVOICER_PARTY_ID,                                      \n")
            .append("    '10', -- ERR009는 폐업자임                                                                     \n")
            .append("    '168.78.201.56',                                          \n")
            .append("    '8',                                                      \n")
            .append("    SYSDATE,                                                  \n")
            .append("    B.ISSUE_ID||'['||B.ESERO_RTN_CODE||']'                    \n")
            .append("    FROM TB_TAX_BILL_ON_BATCH A,TB_TAX_BILL_INFO B            \n")
            .append("    WHERE A.IO_CODE IN('2') -- 매입만 해당                                             \n")
            .append("    AND (A.ONLINE_GUB_CODE='1' OR A.ONLINE_GUB_CODE='3')      \n")
            .append("    AND B.ESERO_RTN_CODE = 'ERR009' --폐업자 오류만 등록                \n")
            .append("    AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                     \n")
            .append("    AND A.IO_CODE = B.IO_CODE                                 \n")
            .append("    AND A.ISSUE_DAY = B.ISSUE_DAY                             \n");
            // ????  .append("    AND B.STATUS_CODE NOT IN('04')                   \n")
            // ????  .append("    AND A.STATUS_CODE_ORG = B.STATUS_CODE            \n");
            
            //System.out.println(sb.toString());
    		CommUtil.logWriter(String.valueOf(sb),1);

            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();

            CommUtil.logWriter("pstm.executeUpdate() RETURN VALUE:"+String.valueOf(rs),1);
           
            //if(rs>0){
        	//bl = true;
            //}
            System.out.println(bl);
            
        }catch(SQLException e){
            bl = false;
    		CommUtil.logWriter(e.toString(),4);
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    		CommUtil.logWriter(e1.toString(),4);
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
        		CommUtil.logWriter(e.toString(),4);
            	e.printStackTrace();
            }
    	}
    	return bl;
    }  

     /**
      * 배전공사 세금계산서 국세청 신고결과 연계 INSERT KLT0340.STATUS 
 	 * 2012.06.14 ADD BY CONAN
      * @param Connection
      */ 
     public boolean KBillKLT0340Status_insert(Connection con)throws SQLException, Exception{
 	
 	boolean bl = false;
 	int rs = 0; 
 	String urls = "";
 	
 	String host_ip = InetAddress.getLocalHost().getHostAddress();
	if("168.78.201.224".equals(host_ip)){
		urls = "https://168.78.201.224/kepcobill/docView/TaxInvHanjunReceiveView.jsp?uuid=";
	}else{
		urls = "https://cat.kepco.net/kepcobill/docView/TaxInvHanjunReceiveView.jsp?uuid=";
	}
	
	
 	PreparedStatement pstm=null;
 	try{
             StringBuffer sb= new StringBuffer()
            .append(" INSERT INTO KLT0340                                                                         \n")
			.append("              (UUID, VOUCHER_BUSEO, VOUCHER_YEARMONTH,                                       \n")
			.append("               VOUCHER_SEQ, CONFIRMURL, STATUS,                                              \n")
			.append("               STATUS_RSN, INSERT_YMD,  INSERT_TIME,                                         \n")
			.append("               CONS_NO)                                                                      \n")
			.append("      ( SELECT A.UUID UUID, A.VOUCHER_BUSEO VOUCHER_BUSEO,                                   \n")
			.append("               A.VOUCHER_YEARMONTH VOUCHER_YEARMONTH,                                        \n")
			.append("               A.VOUCHER_SEQ VOUCHER_SEQ,                                                    \n")
			.append("               ?||A.UUID CONFIRMURL, \n") 
			.append("               C.STATUS_CODE STATUS,                                                         \n")
			.append("               C.STATUS_DESC STATUS_RSN ,  TO_CHAR(SYSDATE,'YYYYMMDD') INSERT_YMD,           \n")
			.append("               TO_CHAR(SYSDATE,'HH24MISS') INSERT_TIME, A.CONTRACT_NO CONS_NO                \n")
			.append("               FROM ETS_TAX_META_INFO_TB A, ETS_TAX_MAIN_INFO_TB B                           \n")
			.append("               ,    TB_TAX_BILL_ON_BATCH C                                                   \n")
			.append("               WHERE A.UUID = B.UUID                                                         \n")
			.append("               AND B.NTS_ISSUE_ID = C.ISSUE_ID                                               \n")
   			.append("               AND C.TAX_2ND_SIGN = 'Y'                                                     \n")
			.append("               AND A.EXT_SYSTEM_TYPE IN ('100','200','400','500') ) -- 배전공사 업무코드                 \n");

             
             //System.out.println(sb.toString());
     		CommUtil.logWriter(String.valueOf(sb),1);

             
             pstm = con.prepareStatement(sb.toString());
             pstm.setString(1, urls);
             rs = pstm.executeUpdate();

             CommUtil.logWriter("pstm.executeUpdate() RETURN VALUE:"+String.valueOf(rs),4);
            
             //if(rs>0){
         	//bl = true;
             //}
             System.out.println(bl);
             
         }catch(SQLException e){
             bl = false;
     		CommUtil.logWriter(e.toString(),4);
             e.printStackTrace();
             throw e;
         }catch(Exception e1){
             bl = false;            
     		CommUtil.logWriter(e1.toString(),4);
     	    e1.printStackTrace();
     	    throw e1;
     	}finally{
             try{
             	if (pstm != null){
             	    pstm.close();
             	}
             }catch (SQLException e){
         		CommUtil.logWriter(e.toString(),4);
             	e.printStackTrace();
             }
     	}
     	return bl;
     }  

      /**
       * 내선계기 세금계산서 국세청 신고결과 연계 UPDATE KEY_POWEREDI.EAI_TAX_STATUS_INFO_TB
  	 * 2012.06.14 ADD BY CONAN
       * @param Connection
       */ 
      public boolean KBillEAI_TAX_Status_update(Connection con)throws SQLException, Exception{
  	
  	boolean bl = false;
  	int rs = 0; 
  	PreparedStatement pstm=null;
  	try{
              StringBuffer sb= new StringBuffer()
				.append("   MERGE INTO EAI_TAX_STATUS_INFO_TB A                               \n")
				.append("   USING (SELECT AA.UUID UUID,                                       \n")
				.append("             CC.STATUS_CODE STATUS,                                  \n")
				.append("             CC.STATUS_DESC STATUS_MSG                               \n")
				.append("             FROM  ETS_TAX_META_INFO_TB AA, ETS_TAX_MAIN_INFO_TB BB  \n")
				.append("             ,        TB_TAX_BILL_ON_BATCH CC                        \n")
				.append("             WHERE AA.UUID = BB.UUID                                 \n")
				.append("             AND BB.NTS_ISSUE_ID = CC.ISSUE_ID                       \n")
				.append("             AND AA.EXT_SYSTEM_TYPE = '300'                          \n")
   			    .append("             AND CC.TAX_2ND_SIGN = 'Y'                               \n")
				.append("            ) D                                                      \n")
				.append("            ON (A.UUID = D.UUID)                                     \n")
				.append("   WHEN MATCHED THEN                                                 \n")
				.append("   UPDATE SET STATUS = D.STATUS, STATUS_MSG = D.STATUS_MSG,          \n")
				.append("                EAI_STAT = NULL                                      \n");
              //System.out.println(sb.toString());
      		CommUtil.logWriter(String.valueOf(sb),1);
              pstm = con.prepareStatement(sb.toString());
              rs = pstm.executeUpdate();

              CommUtil.logWriter("pstm.executeUpdate() RETURN VALUE:"+String.valueOf(rs),4);
              //if(rs>0){
          	//bl = true;
              //}
              System.out.println(bl);
              
          }catch(SQLException e){
              bl = false;
      		CommUtil.logWriter(e.toString(),4);
              e.printStackTrace();
              throw e;
          }catch(Exception e1){
              bl = false;            
      		CommUtil.logWriter(e1.toString(),4);
      	    e1.printStackTrace();
      	    throw e1;
      	}finally{
              try{
              	if (pstm != null){
              	    pstm.close();
              	}
              }catch (SQLException e){
          		CommUtil.logWriter(e.toString(),4);
              	e.printStackTrace();
              }
      	}
      	return bl;
      }  

       /**
      * 한전EDI 매입세금계산서 국세청신고 결과(성공) XMLEDI_STATUS.TX2_RST_QTY 에 반영 
 	 * 2012.06.14 ADD BY CONAN
      * @param Connection
      */ 
     public boolean XmlediStatusTx2Qty04_update(Connection con)throws SQLException, Exception{
 	
 	boolean bl = false;
 	int rs = 0; 
 	PreparedStatement pstm=null;
 	try{
             StringBuffer sb= new StringBuffer()
			.append("    MERGE INTO XMLEDI_STATUS A                                                    \n")
			.append("    USING (SELECT                                                                 \n")
			.append("                              AA.ORDERS_NO ORDERS_NO,                             \n")
			.append("                              AA.MEM_CODE MEM_CODE,                               \n")
			.append("                              AA.ORDERS_ITEM_NO ORDERS_ITEM_NO,                   \n")
			.append("                              SUM(AA.TX2_REQ_QTY) TX2_REQ_QTY,                    \n")
			.append("                              BB.ORDERS_TYPE_CODE  ORDERS_TYPE_CODE,              \n")
			.append("                              SUM(CC.DEL_QTY) DEL_QTY,                            \n")
			.append("                              SUM(CC.ITEM_AMOUNT) ITEM_AMOUNT                     \n")
			.append("                              FROM  XMLEDI_STATUS AA,    XMLEDI_ORDER_MST BB,     \n")
			.append("                                        XMLEDI_TAX_DET CC,   XMLEDI_TAX_MST DD,   \n")
			.append("                                        TB_TAX_BILL_ON_BATCH EE                   \n")
			.append("                              WHERE AA.ORDERS_NO = BB.ORDERS_NO                   \n")
			.append("                              AND AA.ORDERS_ITEM_NO = CC.ORDERS_ITEM_NO           \n")
			.append("                              AND AA.MEM_CODE = CC.SUP_CODE                       \n")
			.append("                              AND AA.ORDERS_NO = DD.ORDERS_NO                     \n")
			.append("                              AND BB.ORDERS_NO = CC.ORDERS_NO                     \n")
			.append("                              AND BB.ORDERS_NO = DD.ORDERS_NO                     \n")
			.append("                              AND CC.MANAGEMENT_ID = DD.MANAGEMENT_ID             \n")
			.append("                              AND DD.ISSUE_ID = EE.ISSUE_ID                       \n")
			.append("                              AND EE.STATUS_CODE = '04'                           \n")
			.append("                              AND EE.TAX_2ND_SIGN = 'Y'                           \n")
	    //                        물가변동주문서는 일반, 수정세금계산서 모두 조건에 포함하고
	    //                        물가변동 이외 주문서는 일반세금계산서만 조건에 포함시킴
			.append("                              AND ( SUBSTR(DD.DOC_TYPE_DETAIL,0,2) IN ('01','03') \n")
 			.append("                                  OR BB.ORDERS_TYPE_CODE = 'PC' )                 \n")   			
        //-------------------------------------------------------------------------------------------------------
 			.append("                              GROUP BY AA.ORDERS_NO                               \n")
		    .append("                              , BB.ORDERS_TYPE_CODE                               \n")
		    .append("                              , AA.MEM_CODE                                       \n")
		    .append("                              , AA.ORDERS_ITEM_NO                                 \n")
			.append("                ) D                                                               \n")
			.append("                ON (      A.ORDERS_NO = D.ORDERS_NO                               \n")
			.append("                      AND A.MEM_CODE = D.MEM_CODE                                 \n")
			.append("                      AND A.ORDERS_ITEM_NO =D.ORDERS_ITEM_NO)                     \n")
			.append("      WHEN MATCHED THEN                                                           \n")
			.append("      UPDATE SET  A.TX2_RST_QTY = (A.TX2_RST_QTY +                                \n")
			.append("              DECODE(D.ORDERS_TYPE_CODE, 'GS', D.ITEM_AMOUNT, D.DEL_QTY))         \n");
             
             //System.out.println(sb.toString());
     		CommUtil.logWriter(String.valueOf(sb),4);

             
             pstm = con.prepareStatement(sb.toString());
             rs = pstm.executeUpdate();

             CommUtil.logWriter("pstm.executeUpdate() RETURN VALUE:"+String.valueOf(rs),4);
            
             //if(rs>0){
         	//bl = true;
             //}
             System.out.println(bl);
             
         }catch(SQLException e){
             bl = false;
     		CommUtil.logWriter(e.toString(),4);
             e.printStackTrace();
             throw e;
         }catch(Exception e1){
             bl = false;            
     		CommUtil.logWriter(e1.toString(),4);
     	    e1.printStackTrace();
     	    throw e1;
     	}finally{
             try{
             	if (pstm != null){
             	    pstm.close();
             	}
             }catch (SQLException e){
         		CommUtil.logWriter(e.toString(),4);
             	e.printStackTrace();
             }
     	}
     	return bl;
     }  

    
     /**
      * 한전EDI 매입세금계산서 국세청신고 결과(실패) XMLEDI_STATUS.TX2_RST_QTY 에 반영 
 	 * 2012.06.14 ADD BY CONAN
      * @param Connection
      */ 
     public boolean XmlediStatusTx2Qty96_update(Connection con)throws SQLException, Exception{
 	
 	boolean bl = false;
 	int rs = 0; 
 	PreparedStatement pstm=null;
 	try{
             StringBuffer sb= new StringBuffer()
			.append("    MERGE INTO XMLEDI_STATUS A                                                    \n")
			.append("    USING (SELECT                                                                 \n")
			.append("                              AA.ORDERS_NO ORDERS_NO,                             \n")
			.append("                              AA.MEM_CODE MEM_CODE,                               \n")
			.append("                              AA.ORDERS_ITEM_NO ORDERS_ITEM_NO,                   \n")
			.append("                              AA.TX2_REQ_QTY TX2_REQ_QTY,                         \n")
			.append("                              BB.ORDERS_TYPE_CODE  ORDERS_TYPE_CODE,              \n")
			.append("                              CC.DEL_QTY DEL_QTY, CC.ITEM_AMOUNT ITEM_AMOUNT      \n")
			.append("                              FROM  XMLEDI_STATUS AA,    XMLEDI_ORDER_MST BB,     \n")
			.append("                                        XMLEDI_TAX_DET CC,   XMLEDI_TAX_MST DD,   \n")
			.append("                                        TB_TAX_BILL_ON_BATCH EE                   \n")
			.append("                              WHERE AA.ORDERS_NO = BB.ORDERS_NO                   \n")
			.append("                              AND AA.ORDERS_ITEM_NO = CC.ORDERS_ITEM_NO           \n")
			.append("                              AND AA.MEM_CODE = CC.SUP_CODE                       \n")
			.append("                              AND AA.ORDERS_NO = DD.ORDERS_NO                     \n")
			.append("                              AND BB.ORDERS_NO = CC.ORDERS_NO                     \n")
			.append("                              AND BB.ORDERS_NO = DD.ORDERS_NO                     \n")
			.append("                              AND CC.MANAGEMENT_ID = DD.MANAGEMENT_ID             \n")
			.append("                              AND DD.ISSUE_ID = EE.ISSUE_ID                       \n")
			.append("                              AND EE.STATUS_CODE = '96'                           \n")
	    //                        물가변동주문서는 일반, 수정세금계산서 모두 조건에 포함하고
	    //                        물가변동 이외 주문서는 일반세금계산서만 조건에 포함시킴
			.append("                              AND ( SUBSTR(DD.DOC_TYPE_DETAIL,0,2) IN ('01','03') \n")
 			.append("                                  OR BB.ORDERS_TYPE_CODE = 'PC' )                 \n")   			
        //-------------------------------------------------------------------------------------------------------
			.append("                ) D                                                               \n")
			.append("                ON (      A.ORDERS_NO = D.ORDERS_NO                               \n")
			.append("                      AND A.MEM_CODE = D.MEM_CODE                                 \n")
			.append("                      AND A.ORDERS_ITEM_NO =D.ORDERS_ITEM_NO)                     \n")
			.append("      WHEN MATCHED THEN                                                           \n")
			.append("      UPDATE SET  A.TX2_RST_QTY = (A.TX2_RST_QTY -                                \n")
			.append("              DECODE(D.ORDERS_TYPE_CODE, 'GS', D.ITEM_AMOUNT, D.DEL_QTY))         \n")
            .append("              ,  A.TX2_REQ_QTY = (A.TX2_REQ_QTY -                                 \n")
         	.append("              DECODE(D.ORDERS_TYPE_CODE, 'GS', D.ITEM_AMOUNT, D.DEL_QTY))         \n");
                      
             //System.out.println(sb.toString());
     		CommUtil.logWriter(String.valueOf(sb),4);

             
             pstm = con.prepareStatement(sb.toString());
             rs = pstm.executeUpdate();

             CommUtil.logWriter("pstm.executeUpdate() RETURN VALUE:"+String.valueOf(rs),4);
            
             //if(rs>0){
         	//bl = true;
             //}
             System.out.println(bl);
             
         }catch(SQLException e){
             bl = false;
     		CommUtil.logWriter(e.toString(),4);
             e.printStackTrace();
             throw e;
         }catch(Exception e1){
             bl = false;            
     		CommUtil.logWriter(e1.toString(),4);
     	    e1.printStackTrace();
     	    throw e1;
     	}finally{
             try{
             	if (pstm != null){
             	    pstm.close();
             	}
             }catch (SQLException e){
         		CommUtil.logWriter(e.toString(),4);
             	e.printStackTrace();
             }
     	}
     	return bl;
     }  

    
    /**
     * 한전 매입 세금 계산서중 취소된것을 찾아 관리테이블에서 삭제한다 
     * @param Connection
     */     
    public boolean search_del_state(Connection con)throws SQLException, Exception{
	
	
	boolean bl = true;
	PreparedStatement pstm=null;
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;
	
	try{
	    
	    con.setAutoCommit(false);
	    bl = bq.trun_up_temp(con);
	    System.out.println("매입 삭제대상 체크(반려처리)____________________________");
            bl = bq.tb_tax_bill_info_delete(con);
            
            if(bl){
        	bl = bq.del_tb_tax_info(con);
		bl = bq.tb_status_hist_up01(con);
		bl = bq.tb_status_hist_in03(con);        	
        	System.out.println("매입 삭제대상 삭제 완료(반려처리)____________________________");        	
               
            }
            con.commit();
            
        }catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }    
    
    
    /***********************************************************************************************/    
    /*********한전 매입 세금계산서중 승인된것을 찾아 처리*******************************************/    
    /***********************************************************************************************/    
    
    public boolean search_update_state(Connection con)throws SQLException, Exception{
	
	PreparedStatement pstm=null;
	BatchQuery bq = new BatchQuery();//  실제로  Query를 통해 작업이 일어나는 Class;
	boolean bl = false; 
	
	try{ 
	    con.setAutoCommit(false);
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//2012. 8. 8 ADD BY mopium
// 내선계기 CFS, END인경우 TAX_2ND_SIGN = Y, ISSUE_DT2 = ISSUE_DT 갱신 처리 	    
	    bl = bq.trun_up_temp(con);
	    System.out.println("내선계기 전송대상 정보 갱신 시작____________________________");
	    System.out.println(" 내선계기 CFS, END 대상 TEMP에 넣기 start ");
        bl = bq.tb_tax_bill_info_update_in_300(con);
	    System.out.println(" 내선계기 CFS, END 대상 TEMP에 넣기 end ");
	    if(bl){
		   System.out.println("내선계기 CFS, END인경우 TAX_2ND_SIGN = Y, ISSUE_DT2 = ISSUE_DT 갱신 처리   START ");
		   bl = bq.after_trans_pro6(con);
		   System.out.println("내선계기 CFS, END인경우 TAX_2ND_SIGN = Y, ISSUE_DT2 = ISSUE_DT 갱신 처리   END ");
	    }
	    System.out.println("내선계기 전송대상 정보 갱신 완료____________________________");
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//-------------------------------------------------------------------------------------
// 익월 미처리 세금계산서 처리 요청 메일/SMS 송신을 위해 해당 대상건 TAX_2ND_SIGN = NULL 처리
//-------------------------------------------------------------------------------------
// 2012. 07.    최초작성 
// 2012. 09. 04 수정         테스트 및 보완
/*
	    CodeVO Cvo1 = new CodeVO();
	    CodeVO Cvo2 = new CodeVO();
	    SimpleDateFormat hourFmt = new SimpleDateFormat("kkmm");
	    SimpleDateFormat dayFmt = new SimpleDateFormat("dd");
	    SimpleDateFormat yyyyMMFmt = new SimpleDateFormat("yyyyMM");
	    SimpleDateFormat YMDhmsFmt = new SimpleDateFormat("yyyyMMdd kk:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String currentTime = ""	;      // System 시분		
		String currentDay = ""	;      // System 일		
		String currentYMDhms = ""	;  // System 년월일 시분		
		String currentyyyyMM = "";     // System 년월
		int MAX_mail_sms_Cnt = 5000;
		currentTime = hourFmt.format(date);
		currentDay = dayFmt.format(date);
		currentyyyyMM = yyyyMMFmt.format(date);
		currentYMDhms = YMDhmsFmt.format(date);
	    
		//이전 년월 구하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String beforeyyyyMM = yyyyMMFmt.format(cal.getTime());
		
		CommUtil.logWriter("currentTime;"+currentTime,1);
		CommUtil.logWriter("currentDay;"+currentDay,1);
		CommUtil.logWriter("currentYMDhms;"+currentYMDhms,1);
		CommUtil.logWriter("currentyyyyMM;"+currentyyyyMM,1);
		CommUtil.logWriter("beforeyyyyMM;"+beforeyyyyMM,1);
		//System.out.println("현재일자 시간 정보    일자(currentDay):"+currentDay+"  시분(currentTime):"+currentTime);
		
		// 01시 부터 02시 사이에만 처리(상태가 일시적으로 변경되어 매입현황 화면에서 TI미발급이 0건으로 리셋되므로 가능한 업무시간 이외에 처리)
		//if( (Integer.parseInt(currentTime) > 100 && Integer.parseInt(currentTime) < 200))
		if(true)//테스트용 
		{    
			 Cvo1.setCode_grp_id("ETC_CODE");
			 Cvo1.setCode("MAIL_RESEND_DAY");
		     Cvo2 = get_code_info(con,Cvo1);
		     if(Integer.parseInt(currentDay) == Integer.parseInt(Cvo2.getCode_value())){//현재일과 처리일이 같은날만 처리
		    	 Cvo2.setRef1(beforeyyyyMM);// isSmsMailResended method에서 이미 처리 했는지 여부를 확인하기 위해 대상월 set
		 	     int Cnts2 = MailSms_Resend_Select(con, currentyyyyMM, beforeyyyyMM); // 처리 대상 건수 체크
		 	     CommUtil.logWriter("대상월:"+Cvo2.getRef1()+"은"+Cvo2.getRef2()+"에 이미 처리했습니다.",1);
		 	     CommUtil.logWriter("대상건수:"+Integer.toString(Cnts2)+"은 최대 전송 건수 "+Integer.toString(MAX_mail_sms_Cnt)+"건 보다 작아야 처리 할수 있습니다.",1);
		    	 if(!Cvo2.getRef1().equals(beforeyyyyMM) && // 대상월(전월) 처리 여부 확인 처리 안한 경우에만 처리해야함.
		 	    	Cnts2 < MAX_mail_sms_Cnt ){ // 처리건수가 너무 많은경우 대량으로 메일을 보내지 않도록 제한함.
		 	       int Cnts1 = 0;
 		           Cnts1 = MailSms_Resend_Update(con, currentyyyyMM, beforeyyyyMM);
		           Cvo2.setRef1(beforeyyyyMM);   //참고1 마지막 처리 대상세금계산서 월(익월) 
		           Cvo2.setRef2(currentYMDhms);   //참고2 마지막 처리 처리일시
		           Cvo2.setRef3(Integer.toString(Cnts1)); 	 //참고3 마지막 처리 건수	         
 		           update_code_info(con,Cvo2);
		 	     }
		     }
        }
*/ 
//-------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
//2012. 06. 18 2차 전자서명 대상 전자세금계산서 상태값(TAX_2ND_SIGN=N, STATUS_CODE는 변경 없음) 갱신  및 메일/SMS 알림
	    bl = bq.trun_up_temp(con);
	    System.out.println("매입 승인대상 체크____________________________");
	    	System.out.println(" 매입 2ndSign이 가능한 대금청구 승인완료 업데이트 할 내용이 있으면 TEMP에 넣는다. start ");
    		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        	//2012. 8. 8 ADD BY mopium 내선계기 대상 제외
            bl = bq.tb_tax_bill_info_update_in_2ndSign(con);
            System.out.println(" 매입 2ndSign이 가능한 대금청구 승인완료 업데이트 할 내용이 있으면 TEMP에 넣는다. end ");
            if(bl){
	        	// 한전EDI 세금계산서는  XMLEDI_TAX_RSP 전기 시 한전담당자 이름, 부서명, 메일, 전화 UPDATE
	        	System.out.println(" TB_TAX_BILL_INFO에 한전EDI 한전 담당자정보 UPDATE start ");
	        	bl = bq.after_trans_pro4_KEDI(con);
	        	System.out.println(" TB_TAX_BILL_INFO에 한전EDI 한전 담당자정보 UPDATE end ");
            	
            	
            	// 처리 순서를 메일/SMS 알림 => TAX_2ND_SIGN update로 하는 이유는
            	// 메일/SMS 알림 처리 여부를 별도 테이블로 관리하지 않고 TAX_2ND_SIGN이 
            	// null 아닌경우 처리하기 위함 
	        	// 메일/SMS 알림
	    		//System.out.println("매입세금계산서 2ndSign 요청 메일/SMS 송신1  START ");
	    		//System.out.println("(XMLEDI_USER_INFO와TP_EXUSER_TBL@SUP설정 사용)");
	    		//bl = bq.get_sms_email3(con);
	    		//System.out.println("매입세금계산서 2ndSign 요청 메일/SMS 송신1 END ");
	    		System.out.println("매입세금계산서 2ndSign 요청 메일/SMS 송신2  START ");
	    		System.out.println("(한전EDI는 TB_TAX_BILL_INFO 설정 사용)");
	    	    bq.trun_up_srm_user_temp(con);
                bq.insert_srm_info(con); //2013.5.15 추가 by mopuim 
	    		bl = bq.get_sms_email4(con);
	    		System.out.println("매입세금계산서 2ndSign 요청 메일/SMS 송신2 END ");

            	System.out.println(" TB_TAX_BILL_INFO에 매입 2ndSign이 가능한 대금청구 승인완료  UPDATE start ");
	            // TAX_2ND_SIGN에 ELECTRONIC_REPORT_YN이 N이면 N, F이면 Y
	        	bl = bq.after_trans_pro5(con);
	        	System.out.println(" TB_TAX_BILL_INFO에 매입 2ndSign이 가능한 대금청구 승인완료  UPDATE end ");
            }
//------------------------------------------------------------------------------------
	    bl = bq.trun_up_temp(con);
	    System.out.println("매입 2차전자서명 완료 체크____________________________");
	    	System.out.println(" 매입 2차전자서명 완료된 세금계산서  업데이트 할 내용이 있으면 TEMP에 넣는다. start ");
    		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        	//2012. 8. 8 ADD BY mopium 내선계기 대상 추가 
            bl = bq.tb_tax_bill_info_update_in(con,"02");
            System.out.println(" 매입 2차전자서명 완료된 세금계산서  업데이트 할 내용이 있으면 TEMP에 넣는다. end ");

            if(bl){
	            System.out.println(" TB_TAX_BILL_INFO에 상태 이력갱신 , 매입 전송건 start ");
	        	bl = bq.after_trans_pro3(con,"02");
	        	System.out.println(" TB_TAX_BILL_INFO에 상태 이력갱신 , 매입 전송건 end ");
	            
	        	//2011. 11. 30 ADD BY mopuim
	        	//2012. 11. 1 아래 반영 스텝을 현재 method 맨 아래로 update_erp_slip method로 변경 이동 MODIFY BY mopuim
	        	//            변경사유 : 내선계기 CFS이후 바로 전송으로 변경에 따름 
	            // 2012. 11.22 쿼리 성능 때문에 원복하기 위해 여기는 uncomment
	        	//             트리거로 변경 예정

                //회계년도, 전표번호  UPDATE
	        	System.out.println(" TB_TAX_BILL_INFO에 회계년도, 전표번호 UPDATE start ");
	        	bl = bq.after_trans_pro4(con);
	        	//bl = bq.after_trans_pro4_KBILL(con);
	        	//System.out.println(" TB_TAX_BILL_INFO에 KEPCOBILL 회계년도, 전표번호 UPDATE end ");
	        	//System.out.println(" TB_TAX_BILL_INFO에 KEDI 회계년도, 전표번호, 한전 담당자정보 UPDATE start ");
	        	//bl = bq.after_trans_pro4_KEDI(con);
	        	//System.out.println(" TB_TAX_BILL_INFO에 KEDI 회계년도, 전표번호, 한전 담당자정보 UPDATE end ");
	        	System.out.println(" 1 - 이력 관련 start ");
	       	    bl = bq.tb_status_hist_up01(con);
	    	    bl = bq.tb_status_hist_in03(con);
	    		System.out.println(" 1 - 이력관련 end");
                //솔루션에 전송한다...
        		System.out.println(" 솔루션에 전송 ------------> INSERT ET_TIDOC_IF");
                StringBuffer sb = new StringBuffer()
                .append("INSERT INTO ET_TIDOC_IF                                                                                  \n")
                .append("(ISSUE_ID, SEQ_NUM, CREATE_TS, STATUS, RVALUE, STORAGE_TYPE, FILE_CONTENT)                               \n")
                .append("SELECT A.ISSUE_ID, '1', SYSDATE, '0',  B.R_VALUE, '1', B.ORG_XML_DOC                                     \n")
                .append("FROM TB_TAX_BILL_INFO A, TB_XML_INFO B                                                                   \n")
                .append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
                .append("AND A.IO_CODE = '2'                                                                                      \n")
                .append("AND A.IO_CODE = B.IO_CODE                                                                                \n")
                .append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                                            \n")
                .append("AND A.ONLINE_GUB_CODE = '1'                                                                              \n")
                .append("AND A.STATUS_CODE = '02'                                                                                 \n")
                .append("AND A.ELECTRONIC_REPORT_YN = 'N' --20100323 추가														  \n")
                //2014. 12. 9 계산서도 추가로 처리 by 박상종 
                //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")//계산서는 제외 즉, 세금계산서만 전송
                .append("AND ( A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")
                .append("      OR ( A.BILL_TYPE_CODE IN ('0301', '0303', '0304') AND A.ISSUE_DAY >= ? ) \n") // 계산서는 작성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
                .append("      OR ( A.BILL_TYPE_CODE IN ('0401', '0403', '0404') AND A.ISSUE_DT >= ? ) ) \n")  // 수정계산서는 생성일자 기준 계산서 국세청 신고 시작일(NOTAXBILL_ESERO_REG_DAY)이후 건 부터 등록
                
                //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                           \n")
                .append("AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                     \n")
                //2012.06.18 익일 전송관련하여 2차전자서명후 2일이상 지난경우  전송대상 제외 시키기
                //.append("AND (A.ISSUE_DT2 IS NULL OR (A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000' AND A.TAX_2ND_SIGN='Y'))  \n");
                //2013.12.11 이전 소스에서 내선계기 2일이상 지난경우 전송 제한이 누락되어 수정함.
                //.append("AND ((A.ISSUE_DT2 IS NULL AND A.ISSUE_DT > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000') OR (A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000' AND A.TAX_2ND_SIGN='Y'))  \n");
                // 위 쿼리는 2012.7. 15일쯤 이후 아래와 같이 변경 가능
                .append("AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000' AND A.TAX_2ND_SIGN='Y'  \n");
                CommUtil.logWriter(sb.toString(),1);
//                System.out.println(sb.toString());
                pstm = con.prepareStatement(sb.toString());
    			pstm.setString(1, NOTAXBILL_ESERO_REG_DAY);
    			pstm.setString(2, NOTAXBILL_ESERO_REG_DAY+"000000" );
                
                pstm.executeUpdate();         
                pstm.close();
                
                bl = bq.trun_up_temp(con);
                
                System.out.println(" 02 - 상태인것  업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다. start ");
	    		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            	//2012. 8. 8 ADD BY mopium 내선계기 대상 추가 
                bl = bq.tb_tax_bill_info_update_in1(con,"03");
                System.out.println(" 02 - 상태인것  업데이트 대상 잡기 - 매입 용 업데이트 할 내용이 있으면 TEMP에 넣는다. end ");
                
                System.out.println(" 03 - 상태로 변경 - TB_TAX_BILL_INFO에 상태 이력갱신 , 매입 전송건  start ");
                bl = bq.after_trans_pro3(con,"03");
                System.out.println(" 03 - 상태로 변경 - TB_TAX_BILL_INFO에 상태 이력갱신 , 매입 전송건  end  ");
                
                System.out.println(" 2 - 이력 관련 start ");
				bl = bq.tb_status_hist_up01(con);
				bl = bq.tb_status_hist_in03(con);
				System.out.println(" 2 - 이력 관련 end ");
				System.out.println("매입 승인대상 처리완료____________________________");                
            }
        	//2012. 11. 1 ADD BY mopuim
            // 2012. 11.22 쿼리 성능 때문에 원복하기 위해 여기는 comment
        	//System.out.println(" TB_TAX_BILL_INFO에 회계년도, 전표번호 UPDATE start ");
        	//bl = bq.update_erp_slip(con);   
        	//System.out.println(" TB_TAX_BILL_INFO에 회계년도, 전표번호 UPDATE end ");
                  
          con.commit();          
            
        } catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            CommUtil.logWriter("SQLException:"+e.getMessage(),0);
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
            CommUtil.logWriter("Exception:"+e1.getMessage(),0);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }

    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean insertNcis(Connection con) throws SQLException, Exception{

    	PreparedStatement pstm = null;
    	boolean bl = false;
    	try{
    		con.setAutoCommit(false);
    		
    		StringBuffer sql = new StringBuffer();
    		sql.append("  INSERT INTO TB_TAX_BILL_ON_BATCH(													\n");
    		sql.append("  				IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,			\n");
    		sql.append(" 				JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n");
    		sql.append(" 				INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE						\n");
    		sql.append(" 				)																	\n");
    		sql.append(" 			SELECT																	\n");
    		sql.append(" 				IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,			\n");
    		sql.append(" 				JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n");
    		sql.append(" 				INVOICEE_GUB_CODE,STATUS_CODE AS STATUS_CODE_ORG,'02' 				\n");
    		sql.append(" 				FROM TB_TAX_BILL_INFO  												\n");
    		sql.append(" 			WHERE ONLINE_GUB_CODE = '3' 											\n");
    		//2011.2.25 수정세금계산서 처리되도록 수정함 KDY
    		//sql.append(" 			AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(SYSDATE,'YYYYMMDD')	 \n");
    		sql.append(" 			AND STATUS_CODE = '01' 													\n");
    		sql.append(" 			AND IO_CODE = '1'														\n");    		
            CommUtil.logWriter(sql.toString(),1);
   		pstm = con.prepareStatement(sql.toString());
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean insertERPSellingMOD(Connection con) throws SQLException, Exception{

    	PreparedStatement pstm = null;
    	boolean bl = false;
    	try{
    		con.setAutoCommit(false);
    		
    		StringBuffer sql = new StringBuffer();
    		sql.append("  INSERT INTO TB_TAX_BILL_ON_BATCH(													\n");
    		sql.append("  				IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,			\n");
    		sql.append(" 				JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n");
    		sql.append(" 				INVOICEE_GUB_CODE,STATUS_CODE_ORG, STATUS_CODE						\n");
    		sql.append(" 				)																	\n");
    		sql.append(" 			SELECT																	\n");
    		sql.append(" 				IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, ISSUE_ID, REL_SYSTEM_ID,			\n");
    		sql.append(" 				JOB_GUB_CODE, ELECTRONIC_REPORT_YN, ADD_TAX_YN, ONLINE_GUB_CODE,	\n");
    		sql.append(" 				INVOICEE_GUB_CODE,STATUS_CODE AS STATUS_CODE_ORG,'02' 				\n");
    		sql.append(" 				FROM TB_TAX_BILL_INFO  												\n");
    		sql.append(" 			WHERE ONLINE_GUB_CODE = '1' 											\n");
    		//2011.2.25 수정세금계산서 처리되도록 수정함 KDY
    		//sql.append(" 			AND ISSUE_DAY BETWEEN TO_CHAR(ADD_MONTHS(SYSDATE, -1),'YYYYMM')||'01' AND TO_CHAR(SYSDATE,'YYYYMMDD')	 \n");
    		sql.append(" 			AND STATUS_CODE = '01' 													\n");
    		sql.append(" 			AND IO_CODE = '1'														\n");
    		//2014. 12. 9 수정계산서도 추가로 처리 by 박상종 
    		//sql.append(" 			AND BILL_TYPE_CODE IN('0201','0202','0203','0204','0205')				\n");
    		sql.append(" 			AND BILL_TYPE_CODE IN('0201','0202','0203','0204','0205','0401','0403','0404')	\n");
    		
            CommUtil.logWriter(sql.toString(),1);
    		CommUtil.logWriter(sql.toString(),1);
    		
    		pstm = con.prepareStatement(sql.toString());
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
   
    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean result_info_pro(Connection con) throws SQLException, Exception{
    	
    	PreparedStatement pstm = null;
    	boolean bl = false;
    	try{
    		con.setAutoCommit(false);
    		StringBuffer sql = new StringBuffer();
    		sql.append(" 	INSERT INTO IF_TAX_BILL_RESULT_INFO( 												\n");    		
    		sql.append(" 				REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID, STATUS_CODE, STATUS_DESC,		\n");    		
    		sql.append(" 				REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT, EAI_CDATE, EAI_UDATE 			\n");    		
    		sql.append(" 				)																		\n");    		
    		sql.append(" 				SELECT /*+ ordered */													\n");    		
    		sql.append(" 						C.REL_SYSTEM_ID, C.JOB_GUB_CODE, C.MANAGE_ID, 					\n");    		
    		sql.append(" 						B.STATUS_CODE, '영업한전승인처리' AS STATUS_DESC, 					\n");    		
    		sql.append(" 						SYSDATE, SYSDATE, C.ISSUE_ID,									\n");    		
    		sql.append(" 						DECODE(B.ONLINE_GUB_CODE, '3', NULL, 'X'), NULL,NULL  			\n");    		
    		sql.append(" 				FROM  TB_TAX_BILL_ON_BATCH B, IF_TAX_BILL_INFO C						\n");    		
    		sql.append(" 				WHERE 1>0																\n");    		
    		sql.append(" 				--AND B.REL_SYSTEM_ID = C.REL_SYSTEM_ID      								\n");
    		sql.append(" 				AND B.JOB_GUB_CODE = C.JOB_GUB_CODE										\n");
    		sql.append(" 				AND B.ONLINE_GUB_CODE = C.ONLINE_GUB_CODE								\n");
    		sql.append(" 				AND B.ISSUE_ID = C.ISSUE_ID												\n");
    		sql.append("	\n");
    		
            CommUtil.logWriter(sql.toString(),1);
    		//System.out.println("sql ==============>>>>>>>>>>>>>>>> "+sql.toString());
    		pstm = con.prepareStatement(sql.toString());
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	
    	return bl;
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean tax_bill_info_pro(Connection con) throws SQLException, Exception{
    	// TODO Auto-generated method stub
    	PreparedStatement pstm = null;
    	int rs = 0;
    	boolean bl = false;
    	
    	try{
    		con.setAutoCommit(false);
    		
    		StringBuffer sql = new StringBuffer();
    		if(RunningScheduler.DB_ENC.equals("1")){         
        		sql.append("	UPDATE TB_TAX_BILL_INFO_ENC SET STATUS_CODE='02' 			   			   \n");
    		}else if(RunningScheduler.DB_ENC.equals("0")){                  
        		sql.append("	UPDATE TB_TAX_BILL_INFO SET STATUS_CODE='02' 								\n");
    		}else{
        		sql.append("	UPDATE TB_TAX_BILL_INFO SET STATUS_CODE='02' 								\n");
    		}
    		CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
    		sql.append("	WHERE 1>0 																			\n");
    		sql.append("		AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)      									\n");
    		sql.append("			IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH) 	\n");
    		sql.append(" 	\n");
    		
            CommUtil.logWriter(sql.toString(),1);
    		//System.out.println("sql ===================>>>>>>>>>>>>>> "+sql.toString());
    		pstm = con.prepareStatement(sql.toString());
    		rs = pstm.executeUpdate();
    		if(rs>0){
    			bl = true;
    		}
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }  
    
    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean status_hist_pro_u (Connection con)throws SQLException, Exception{
    	
    	PreparedStatement pstm = null;
    	boolean bl = false;
    	try{
    		con.setAutoCommit(false);
    		StringBuffer sql = new StringBuffer();
    		sql.append(" 	UPDATE /*+ bypass_ujvc */    										\n");
    		sql.append(" 		(																\n");
    		sql.append(" 			SELECT /*+ ordered */   									\n");
    		sql.append(" 			TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') END_DT,B.AVL_END_DT		\n");
    		sql.append(" 			FROM  TB_TAX_BILL_ON_BATCH A, TB_STATUS_HIST B   			\n");
    		sql.append(" 			WHERE A.IO_CODE = B.IO_CODE 								\n");
    		sql.append(" 			AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID						\n");
    		//sql.append(" 			AND B.ISSUE_DAY = B.ISSUE_DAY								\n");
    		sql.append(" 			AND A.ISSUE_DAY = B.ISSUE_DAY								\n");
    		sql.append(" 			AND B.AVL_END_DT = '99991231235959'							\n");
    		sql.append(" 		)																\n");
    		sql.append(" 	SET AVL_END_DT = END_DT 											\n");
    		sql.append(" 	\n");

            CommUtil.logWriter(sql.toString(),1);
            pstm = con.prepareStatement(sql.toString());
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    		
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean status_hist_pro_i (Connection con)throws SQLException, Exception{
    	
    	PreparedStatement pstm = null;
    	boolean bl = false;
    	try{
    		con.setAutoCommit(false);
    		StringBuffer sql = new StringBuffer();
    		sql.append(" 	INSERT INTO TB_STATUS_HIST    												\n");
    		sql.append(" 		(IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, AVL_BEGIN_DT,AVL_END_DT, SEQ_NO, STATUS_CODE, REGIST_DT, REGIST_ID) \n");
    		sql.append(" 			SELECT /*+ ordered */   											\n");
    		sql.append(" 				A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID,						\n");
    		sql.append(" 				TO_CHAR(SYSDATE + INTERVAL '00:01' MINUTE TO SECOND,'YYYYMMDDHH24MISS') , '99991231235959',   	\n");
    		sql.append(" 				MAX(B.SEQ_NO)+1 AS SEQ_NO, A.STATUS_CODE, SYSDATE, 'BATCH'  	\n");
    		sql.append(" 			FROM  TB_TAX_BILL_ON_BATCH A, TB_STATUS_HIST B 						\n");
    		sql.append(" 			WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID 							\n");
    		sql.append(" 			AND A.IO_CODE = B.IO_CODE											\n");
    		sql.append(" 			AND B.ISSUE_DAY = A.ISSUE_DAY										\n");
    		sql.append(" 	 		GROUP BY A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID,A.STATUS_CODE		\n");
    		sql.append(" 	\n");

            CommUtil.logWriter(sql.toString(),1);
    		pstm = con.prepareStatement(sql.toString());
    		
    		if(pstm.executeUpdate()>0){
    			bl = true;
    		}
    		
    	}catch(SQLException e){
            con.rollback();
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            con.rollback();
            bl = false;           
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
            }
    	}
    	return bl;
    }
    
    
    private void close(Statement pstm) {
	if (pstm != null)
	    try {
		pstm.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

    }
    
    /**
     * 세금계산서 코드정보 조회
     * 2013. 05. 27 by mopuim
     */   
    public CodeVO get_code_info(Connection con,CodeVO Cvo1)throws SQLException, Exception{

    	PreparedStatement pstm=null;
	    ResultSet rs = null;
 		CodeVO Cvo2 = new CodeVO();
		try{
			StringBuffer sb= new StringBuffer()
			.append("SELECT                                                   \n") 
	        .append("CODE_GRP_ID, CODE, CODE_VALUE, CODE_DESC,                \n")
	        .append(" REF1, REF2, REF3                                        \n")
	        .append("FROM TB_CODE_INFO                                        \n")        
	        .append("WHERE CODE_GRP_ID = ?                                    \n")    
	        .append("AND CODE = ?                                             \n");
	 		
	        CommUtil.logWriter(sb.toString(),4);
	        CommUtil.logWriter("code_grp_id"+Cvo1.getCode_grp_id(),4);
	        CommUtil.logWriter("code"+Cvo1.getCode(),4);
			
			pstm = con.prepareStatement(sb.toString());
			pstm.setString(1, Cvo1.getCode_grp_id());
            pstm.setString(2, Cvo1.getCode());
            
			rs = pstm.executeQuery();
			while(rs.next()){		//이메일 전송 대상이 있으면..
			    Cvo2.setCode_grp_id(CommUtil.SpaceChange(rs.getString("CODE_GRP_ID")));
			    Cvo2.setCode(CommUtil.SpaceChange(rs.getString("CODE")));
			    Cvo2.setCode_value(CommUtil.SpaceChange(rs.getString("CODE_VALUE")));
			    Cvo2.setCode_desc(CommUtil.SpaceChange(rs.getString("CODE_DESC")));
			    Cvo2.setRef1(CommUtil.SpaceChange(rs.getString("REF1")));
			    Cvo2.setRef2(CommUtil.SpaceChange(rs.getString("REF2")));
			    Cvo2.setRef3(CommUtil.SpaceChange(rs.getString("REF3")));
			}

	        CommUtil.logWriter("CODE_GRP_ID:"+Cvo2.getCode_grp_id(),4);
	        CommUtil.logWriter("CODE:"+Cvo2.getCode(),4);
	        CommUtil.logWriter("CODE_VALUE:"+Cvo2.getCode_value(),4);
	        CommUtil.logWriter("CODE_DESC:"+Cvo2.getCode_desc(),4);
	        CommUtil.logWriter("REF1:"+Cvo2.getRef1(),4);
	        CommUtil.logWriter("REF2:"+Cvo2.getRef2(),4);
	        CommUtil.logWriter("REF3:"+Cvo2.getRef3(),4);

		}catch(Exception e1){
	            e1.printStackTrace();
	     }finally{
	            try{
	            	if (pstm != null){
	            	    pstm.close();
	            	}
	            }catch (SQLException e){
	            	e.printStackTrace();
	            }
    	}
    	return Cvo2;
    }

    /**
     * 세금계산서 코드정보 조회
     * 2013. 05. 27 by mopuim
     */   
//    public boolean IsSmsMailResended(Connection con, CodeVO Cvo2)throws SQLException, Exception{
//
//    	PreparedStatement pstm=null;
//	    ResultSet rs = null;
//	    String cnts = "";
//	    boolean rslt = true;// 아래 조회 오류시 메일 송신이 안되도록 true로 초기화
// 		try{
//			StringBuffer sb= new StringBuffer()
//			.append("SELECT COUNT(CODE_VALUE) CNTS                            \n")
//	        .append("FROM TB_CODE_INFO                                        \n")        
//	        .append("WHERE CODE_GRP_ID = ?                                    \n")    
//	        .append("AND CODE = ?                                             \n")
//	        .append("AND REF1 = ?                                             \n");
//	 		
//	        CommUtil.logWriter(sb.toString(),4);
//	        CommUtil.logWriter("code_grp_id"+Cvo2.getCode_grp_id(),4);
//	        CommUtil.logWriter("code"+Cvo2.getCode(),4);
//	        CommUtil.logWriter("ref1"+Cvo2.getRef1(),4);
//			
//			pstm = con.prepareStatement(sb.toString());
//			pstm.setString(1, Cvo2.getCode_grp_id());
//            pstm.setString(2, Cvo2.getCode());
//            pstm.setString(3, Cvo2.getRef1());
//            
//			rs = pstm.executeQuery();
//			while(rs.next()){		//이메일 전송 대상이 있으면..
//			   cnts = CommUtil.SpaceChange(rs.getString("CNTS"));
//			}
//	        CommUtil.logWriter("cnts:"+cnts,4);
//            if(cnts.equals("1")){
//            	rslt = true;
//            }else{
//            	rslt = false;
//            }
//		}catch(Exception e1){
//	            e1.printStackTrace();
//	     }finally{
//	            try{
//	            	if (pstm != null){
//	            	    pstm.close();
//	            	}
//	            }catch (SQLException e){
//	            	e.printStackTrace();
//	            }
//    	}
//    	return rslt;
//    }

    /**
     * 세금계산서 코드정보 갱신
     * 2013. 05. 27 by mopuim
     */   
    public boolean update_code_info(Connection con, CodeVO Cvo )throws SQLException, Exception{
    	PreparedStatement pstm=null;
    	boolean bl = false;
    	try{
                StringBuffer sb= new StringBuffer()
                .append("UPDATE TB_CODE_INFO SET REF1 = ? , REF2 = ?, REF3 = ?  \n")
	            .append("       WHERE CODE_GRP_ID = ?                           \n")    
	            .append("       AND CODE = ?                                    \n"); 
                
                CommUtil.logWriter(sb.toString(),1);
    	        CommUtil.logWriter("REF1:"+Cvo.getRef1(),1);
    	        CommUtil.logWriter("REF2:"+Cvo.getRef2(),1);
    	        CommUtil.logWriter("REF3:"+Cvo.getRef3(),1);
    	        CommUtil.logWriter("CODE_GRP_ID:"+Cvo.getCode_grp_id(),1);
    	        CommUtil.logWriter("CODE:"+Cvo.getCode(),1);
        
    	        pstm = con.prepareStatement(sb.toString());
                pstm.setString(1, Cvo.getRef1());
                pstm.setString(2, Cvo.getRef2());
                pstm.setString(3, Cvo.getRef3());
                pstm.setString(4, Cvo.getCode_grp_id());
                pstm.setString(5, Cvo.getCode());

                if(pstm.executeUpdate()>0){
                    bl = true;
                }
            }catch(SQLException e){
                CommUtil.logWriter(e.toString(),4);
                throw e;
            }catch(Exception e1){
                CommUtil.logWriter(e1.toString(),4);
        	    throw e1;
        	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter(e.toString(),4);
                	e.printStackTrace();
                }
        	}
    	return bl;
        }
         
    /**
     * 세금계산서 코드정보 갱신
     * 2013. 05. 27 by mopuim
     */   
    public int MailSms_Resend_Select(Connection con, String currentyyyyMM, String beforeyyyyMM)throws SQLException, Exception{

    	PreparedStatement pstm=null;
	    ResultSet rs = null;
	    int cnts = 0;
	    try{
			StringBuffer sb= new StringBuffer()
	        .append("SELECT COUNT(ISSUE_ID) CNTS                               \n")
	        .append(" FROM TB_TAX_BILL_INFO A                                  \n") 
            .append(" WHERE A.ONLINE_GUB_CODE='1'                              \n")
            .append("    AND ( A.ISSUE_DAY >= ?||'01' -- 전월 1일                               \n")    
            .append("    AND A.ISSUE_DAY < ?||'01' --전월 마지막일                               \n")
            .append("    AND A.IO_CODE = '2'                                   \n")
            .append("    AND A.ELECTRONIC_REPORT_YN = 'N'                      \n")
            .append("    AND SUBSTR(SVC_MANAGE_ID,0,1) <> 'V'                  \n")
	        .append("    AND A.STATUS_CODE = '01'                              \n")
            .append("    AND A.TAX_2ND_SIGN = 'N'                              \n")
            .append("    AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05') \n");
            
	        CommUtil.logWriter(sb.toString(),1);
	        CommUtil.logWriter("beforeyyyyMM:"+beforeyyyyMM,1);
	        CommUtil.logWriter("currentyyyyMM:"+currentyyyyMM,1);
			
	        pstm = con.prepareStatement(sb.toString());
	        pstm.setString(1, beforeyyyyMM);
            pstm.setString(2, currentyyyyMM);
            
			rs = pstm.executeQuery();
			while(rs.next()){		//이메일 전송 대상이 있으면..
			   cnts = Integer.parseInt(CommUtil.SpaceChange(rs.getString("CNTS")));
			}
	        CommUtil.logWriter("cnts:"+cnts,4);
		}catch(Exception e1){
	            e1.printStackTrace();
	     }finally{
	            try{
	            	if (pstm != null){
	            	    pstm.close();
	            	}
	            }catch (SQLException e){
	            	e.printStackTrace();
	            }
    	}
    	return cnts;
    }

    
    public boolean InsertEtaxconn(Connection con) throws SQLException, Exception{
    	
    	boolean bl = false;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer()
                .append("INSERT INTO ET_TIDOC_IF                                                                                  \n")
                .append("(ISSUE_ID, SEQ_NUM, CREATE_TS, STATUS, RVALUE, STORAGE_TYPE, FILE_CONTENT)                               \n")
                .append("SELECT A.ISSUE_ID, '1', SYSDATE, '0',  B.R_VALUE, '1', B.ORG_XML_DOC                                     \n")
                .append("FROM TB_TAX_BILL_INFO A, TB_XML_INFO B                                                                   \n")
                .append("WHERE A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
                .append("AND A.IO_CODE = '1'                                                                                      \n")
                .append("AND A.IO_CODE = B.IO_CODE                                                                                \n")
                .append("AND A.ISSUE_DAY = B.ISSUE_DAY                                                                            \n")
                //.append("AND ( A.ONLINE_GUB_CODE = '1' OR A.ONLINE_GUB_CODE = '3' )                                                                         \n")
                //.append("AND ( A.STATUS_CODE = '02' OR A.STATUS_CODE = '06')                                                                                 \n")
                //.append("AND A.ELECTRONIC_REPORT_YN = 'N' --20100323 추가														      \n")
                //.append("AND A.BILL_TYPE_CODE IN ('0101', '0102', '0103', '0104', '0105', '0201', '0202', '0203', '0204', '0205') \n")//계산서는 제외 즉, 세금계산서만 전송
                //.append("AND A.INVOICEE_GUB_CODE = '00'                                                                           \n")
                //.append("AND A.INVOICEE_GUB_CODE IN ('00')                                                     \n")
                //2012.06.18 익일 전송관련하여 2차전자서명후 2일이상 지난경우  전송대상 제외 시키기
                // 위 쿼리는 2012.7. 15일쯤 이후 아래와 같이 변경 가능
                //.append("AND A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000' AND A.TAX_2ND_SIGN='Y'  \n");
                .append("AND (A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID) 																  \n")
                .append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)							      \n")            
                //.append("AND (A.ISSUE_DT2 IS NULL OR (A.ISSUE_DT2 > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000' AND A.TAX_2ND_SIGN='Y'))  \n");
                .append("AND A.ISSUE_DT > TO_CHAR(SYSDATE - 1, 'YYYYMMDD')||'000000'                                            \n");

                
                CommUtil.logWriter(sb.toString(),4);
                pstm = con.prepareStatement(sb.toString());
                rs = pstm.executeUpdate();         
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
    	    close(pstm);
    	}
    	return bl;
    } 
    
    /**
     * 세금계산서 메일SMS 재전송을 위한 갱신
     * 2013. 05. 27 by mopuim
     */   
    public int MailSms_Resend_Update(Connection con, String currentyyyyMM, String beforeyyyyMM)throws SQLException, Exception{
    	PreparedStatement pstm=null;
    	//boolean bl = false;
    	int cnts = 0;
    	try{
                StringBuffer sb= new StringBuffer()
                .append("UPDATE TB_TAX_BILL_INFO A  SET A.TAX_2ND_SIGN = NULL                                                               \n")
	            .append("    WHERE A.ONLINE_GUB_CODE='1'                                                                                    \n")
                //.append("       AND ( A.ISSUE_DAY >= TO_CHAR(TO_DATE(TO_CHAR(SYSDATE ,'YYYYMM'), 'YYYYMM') - 1, 'YYYYMM')||'01' -- 전월 1일  \n")    
	            //.append("        AND A.ISSUE_DAY <= TO_CHAR(TO_DATE(TO_CHAR(SYSDATE ,'YYYYMM'), 'YYYYMM') - 1, 'YYYYMMDD') ) --전월 마지막일  \n")
	            .append("    AND ( A.ISSUE_DAY >= ?||'01' -- 전월 1일                                                                                                                                                  \n")    
	            .append("    AND A.ISSUE_DAY < ?||'01' --전월 마지막일                                                                                                                                                  \n")
	            .append("    AND A.IO_CODE = '2'                                                                                            \n")
	            .append("    AND A.ELECTRONIC_REPORT_YN = 'N'                                                                               \n")
	            .append("    AND SUBSTR(SVC_MANAGE_ID,0,1) <> 'V'                                                                           \n")
	            .append("    AND A.STATUS_CODE = '01'                                                                                       \n")
	            .append("    AND A.TAX_2ND_SIGN = 'N'                                                                                       \n")
	            .append("    AND A.INVOICEE_GUB_CODE IN ('00','01','03','04','05')                                                          \n");
                
                CommUtil.logWriter(sb.toString(),1);
    	        
    	        pstm = con.prepareStatement(sb.toString());
    	        pstm.setString(1, beforeyyyyMM);
                pstm.setString(2, currentyyyyMM);
                
                cnts = pstm.executeUpdate();
               
            }catch(SQLException e){
                CommUtil.logWriter(e.toString(),4);
                throw e;
            }catch(Exception e1){
                CommUtil.logWriter(e1.toString(),4);
        	    throw e1;
        	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                    CommUtil.logWriter(e.toString(),4);
                	e.printStackTrace();
                }
        	}
    	return cnts;
        }
    
       public boolean tax_info_Update_IssueDt(Connection con)throws SQLException, Exception{
    	
    	boolean bl = false;
    	int rs = 0;
    	PreparedStatement pstm=null;
    	try{
            StringBuffer sb= new StringBuffer();
            if(RunningScheduler.DB_ENC.equals("1")){         
            	sb.append("UPDATE TB_TAX_BILL_INFO_ENC SET ISSUE_DT = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')   \n");
            }else if(RunningScheduler.DB_ENC.equals("0")){                 
                sb.append("UPDATE TB_TAX_BILL_INFO SET ISSUE_DT = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')       \n");
            }else{
                sb.append("UPDATE TB_TAX_BILL_INFO SET ISSUE_DT = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS')       \n");
            }
            sb.append("WHERE 1>0                                                                            \n");
            sb.append("AND (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID)                                              \n");
            sb.append("IN (SELECT IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID FROM TB_TAX_BILL_ON_BATCH)              \n");
            CommUtil.logWriter("암호화 설정정보(1 암호화, 0 : 비암호화) 값:"+RunningScheduler.DB_ENC,1);
            CommUtil.logWriter(sb.toString(),1);
            //System.out.println(sb.toString());
            
            pstm = con.prepareStatement(sb.toString());
            rs = pstm.executeUpdate();
            if(rs>0){
        	bl = true;
            }
            //System.out.println("after_trans_pro1====>"+bl);
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
    	    throw e1;
    	}finally{
//    	    close(pstm);
    	}
    	return bl;
    } 
    
}