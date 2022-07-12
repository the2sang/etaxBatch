package kr.co.kepco.etax30.selling.batchoffline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : BatchQueryOff
 * 프로그램 아이디      : BatchQueryOff.java
 * 프로그램 개요        : 이관 배치작업 쿼리 메서드                                                    
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - tax_info_trans_temp()
 * - tax_info_trans()
 * - item_list_trans()
 * - tax_result_info_insert()
 * - tax_hist_info_insert()
 * - if_tax_bill_info_flg_update()
 * - trun_if_temp()
 * - getSumCntAmt()
 * - insertIfTaxBillResultInfo()
 * </METHOD>
******************************************************************************/

public class BatchQueryOff {
	final static String OFF_MODIFY_DT = CommProperties.getString("OFF_MODIFY_DT");    
    
    /**
     * Offline Batch Query
     */
	
	
    /**
     * IF_TAX_BILL_INFO 에서 IF_TAX_BILL_INFO_OFF_TEMP DATA 전송.
     * @param String 기준일자, Connection
     */    
    
    public boolean tax_info_trans_temp(Connection con, String targetMonth, String now_grp_no) throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_INFO_OFF_TEMP                                                                                      \n")
            .append("(MANAGE_ID,ISSUE_DAY,BILL_TYPE_CODE,PURPOSE_CODE,AMENDMENT_CODE,DESCRIPTION,UPPER_MANAGE_ID,                               \n")
            .append("INVOICER_PARTY_ID,INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                                            \n")
            .append("INVOICER_ADDR,INVOICER_TYPE,INVOICER_CLASS,INVOICER_CONTACT_DEPART,                                                        \n")
            .append("INVOICER_CONTACT_NAME,INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,INVOICEE_BUSINESS_TYPE_CODE,                           \n")
            .append("INVOICEE_PARTY_ID,INVOICEE_TAX_REGIST_ID,INVOICEE_PARTY_NAME,INVOICEE_CEO_NAME,                                            \n")
            .append("INVOICEE_ADDR,INVOICEE_TYPE,INVOICEE_CLASS,                                                                                \n")
            .append("INVOICEE_CONTACT_DEPART1,INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1,INVOICEE_CONTACT_EMAIL1,                           \n")
            .append("INVOICEE_CONTACT_DEPART2,INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2,INVOICEE_CONTACT_EMAIL2,                           \n")
            .append("PAYMENT_TYPE_CODE1,PAY_AMOUNT1,PAYMENT_TYPE_CODE2,PAY_AMOUNT2,                                                             \n")
            .append("PAYMENT_TYPE_CODE3,PAY_AMOUNT3,PAYMENT_TYPE_CODE4,PAY_AMOUNT4,                                                             \n")
            .append("CHARGE_TOTAL_AMOUNT,TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT,                                                                   \n")
            .append("JOB_GUB_CODE,REL_SYSTEM_ID,REGIST_DT,MODIFY_DT,CANCEL_DT,ISSUE_ID,ADD_TAX_YN,BILL_ISSUE_YYYYMM,ONLINE_GUB_CODE)            \n")
            .append("SELECT /*+ INDEX(IF_TAX_BILL_INFO IF_TAX_BILL_INFO_IX1) */                                                                 \n")
            .append("MANAGE_ID,ISSUE_DAY,BILL_TYPE_CODE,PURPOSE_CODE,AMENDMENT_CODE,DESCRIPTION,UPPER_MANAGE_ID,                                \n")
            .append("INVOICER_PARTY_ID,INVOICER_TAX_REGIST_ID,INVOICER_PARTY_NAME,INVOICER_CEO_NAME,                                            \n")
            .append("INVOICER_ADDR,INVOICER_TYPE,INVOICER_CLASS,INVOICER_CONTACT_DEPART,                                                        \n")
            .append("INVOICER_CONTACT_NAME,INVOICER_CONTACT_PHONE,INVOICER_CONTACT_EMAIL,INVOICEE_BUSINESS_TYPE_CODE,                           \n")
            .append("INVOICEE_PARTY_ID,INVOICEE_TAX_REGIST_ID,INVOICEE_PARTY_NAME,INVOICEE_CEO_NAME,                                            \n")
            .append("INVOICEE_ADDR,INVOICEE_TYPE,INVOICEE_CLASS,                                                                                \n")
            .append("INVOICEE_CONTACT_DEPART1,INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1,INVOICEE_CONTACT_EMAIL1,                           \n")
            .append("INVOICEE_CONTACT_DEPART2,INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2,INVOICEE_CONTACT_EMAIL2,                           \n")
            .append("PAYMENT_TYPE_CODE1,PAY_AMOUNT1,PAYMENT_TYPE_CODE2,PAY_AMOUNT2,                                                             \n")
            .append("PAYMENT_TYPE_CODE3,PAY_AMOUNT3,PAYMENT_TYPE_CODE4,PAY_AMOUNT4,                                                             \n")
            .append("CHARGE_TOTAL_AMOUNT,TAX_TOTAL_AMOUNT,GRAND_TOTAL_AMOUNT,                                                                   \n")
            .append("JOB_GUB_CODE,REL_SYSTEM_ID,REGIST_DT,MODIFY_DT,CANCEL_DT,ISSUE_ID,ADD_TAX_YN,BILL_ISSUE_YYYYMM,ONLINE_GUB_CODE             \n")
            .append("FROM IF_TAX_BILL_INFO                                                                                           			\n")
            .append("WHERE 1 > 0                                                               										 			\n")
            .append("AND GRP_NO = ?                                                                                           			        \n")
            .append("AND ISSUE_DAY BETWEEN ? || '01' AND TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM')), 'YYYYMMDD')         				 			\n")
            .append("AND MODIFY_DT BETWEEN TO_DATE(? || '01000000', 'YYYYMMDDHH24MISS')			                                     			\n") 
            .append("              AND  TO_DATE(TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYYMM'))+ ?, 'YYYYMMDD') || '235959', 'YYYYMMDDHH24MISS') 			\n")         				
            .append("AND ONLINE_GUB_CODE = '2'                                                                                       			\n")
            
            .append("AND CANCEL_DT IS NULL                                                                                           			\n")
            .append("AND FLG = 'N'                                                                                                   			\n");
            // 배치 처리 테스트 편의를 위해 FLG 체크 잠시 제외
            //.append("AND CANCEL_DT IS NULL                                                                                           			\n");
  
            
    		CommUtil.logWriter(sb.toString(),1);

    		pstm = con.prepareStatement(sb.toString());
            pstm.setString(1, now_grp_no);
            pstm.setString(2, targetMonth);
            pstm.setString(3, targetMonth);
            pstm.setString(4, targetMonth);
            pstm.setString(5, targetMonth);
            pstm.setString(6, OFF_MODIFY_DT);
            if(pstm.executeUpdate()>0){
                bl = true;
                con.commit();
            }else{
        		CommUtil.logWriter(sb.toString(),4);
        		CommUtil.logWriter("처리대상이 없습니다. ISSUE_DAY와 MODIFY_DT YYYMMDD가 같아야합니다.",4);
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
     * IF_TAX_BILL_INFO_OFF_TEMP의 값을 TB_TAX_BILL_INFO 로 이전
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean tax_info_trans(Connection con)throws SQLException, Exception{
	
	boolean bl = false;
	PreparedStatement pstm=null;
	
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  /*+ APPEND */ TB_TAX_BILL_INFO                                                                                            \n")
            .append("    (BIZ_MANAGE_ID,SVC_MANAGE_ID, ISSUE_DT, SIGNATURE, ISSUE_ID, ISSUE_DAY,                                                  \n")
            .append("    BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE, DESCRIPTION,                                                               \n")
            .append("    IMPORT_DOC_ID, IMPORT_PERIOD_START_DAY, IMPORT_PERIOD_END_DAY, IMPORT_ITEM_QUANTITY,                                     \n")
            .append("    INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                          \n")
            .append("    INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                         \n")
            .append("    INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                          \n")
            .append("    INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                          \n")
            .append("    INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                                  \n")
            .append("    INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                    \n")
            .append("    INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1,INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                       \n")
            .append("    INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2,INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                       \n")
            .append("    BROKER_PARTY_ID, BROKER_TAX_REGIST_ID, BROKER_PARTY_NAME,                                                                \n")
            .append("    BROKER_CEO_NAME, BROKER_ADDR, BROKER_TYPE, BROKER_CLASS,                                                                 \n")
            .append("    BROKER_CONTACT_DEPART, BROKER_CONTACT_NAME,                                                                              \n")
            .append("    BROKER_CONTACT_PHONE, BROKER_CONTACT_EMAIL,                                                                              \n")
            .append("    PAYMENT_TYPE_CODE1, PAY_AMOUNT1,PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                         \n")
            .append("    PAYMENT_TYPE_CODE3, PAY_AMOUNT3,PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                         \n")
            .append("    CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                               \n")
            .append("    STATUS_CODE, JOB_GUB_CODE, REL_SYSTEM_ID,REGIST_ID,MODIFY_ID,IO_CODE, ADD_TAX_YN,                                        \n")
            .append("    INVOICEE_GUB_CODE,ONLINE_GUB_CODE, UPPER_MANAGE_ID )                                                                     \n")
            .append("    SELECT /*+ FULL(A) PARALLEL(A 16) */                                                                                     \n")
            .append("        TO_CHAR(SYSDATE, 'YYYYMM')||'1'|| NVL(SUBSTR(REL_SYSTEM_ID, 1, 3), 'EDI')||JOB_GUB_CODE||LPAD(SQ_MNG_ID.NEXTVAL, 8, '0'),\n")
            .append("        MANAGE_ID ,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'',ISSUE_ID,ISSUE_DAY,                                                \n")
            .append("        BILL_TYPE_CODE, PURPOSE_CODE, AMENDMENT_CODE,DESCRIPTION,                                                            \n")
            .append("        '', '', '', '',                                                                                                      \n")
            .append("        INVOICER_PARTY_ID, INVOICER_TAX_REGIST_ID, INVOICER_PARTY_NAME,                                                      \n")
            .append("        INVOICER_CEO_NAME, INVOICER_ADDR, INVOICER_TYPE, INVOICER_CLASS,                                                     \n")
            .append("        INVOICER_CONTACT_DEPART, INVOICER_CONTACT_NAME,                                                                      \n")
            .append("        INVOICER_CONTACT_PHONE, INVOICER_CONTACT_EMAIL,                                                                      \n")
            .append("        INVOICEE_BUSINESS_TYPE_CODE, INVOICEE_PARTY_ID, INVOICEE_TAX_REGIST_ID,                                              \n")
            .append("        INVOICEE_PARTY_NAME, INVOICEE_CEO_NAME, INVOICEE_ADDR, INVOICEE_TYPE, INVOICEE_CLASS,                                \n")
            .append("        INVOICEE_CONTACT_DEPART1, INVOICEE_CONTACT_NAME1, INVOICEE_CONTACT_PHONE1, INVOICEE_CONTACT_EMAIL1,                  \n")
            .append("        INVOICEE_CONTACT_DEPART2, INVOICEE_CONTACT_NAME2, INVOICEE_CONTACT_PHONE2, INVOICEE_CONTACT_EMAIL2,                  \n")
            .append("         '','','','','','','','','','','',                                                                                   \n")
            .append("        PAYMENT_TYPE_CODE1, PAY_AMOUNT1, PAYMENT_TYPE_CODE2, PAY_AMOUNT2,                                                    \n")
            .append("        PAYMENT_TYPE_CODE3, PAY_AMOUNT3, PAYMENT_TYPE_CODE4, PAY_AMOUNT4,                                                    \n")
            .append("        CHARGE_TOTAL_AMOUNT, TAX_TOTAL_AMOUNT, GRAND_TOTAL_AMOUNT,                                                           \n")
            .append("        '01', JOB_GUB_CODE, REL_SYSTEM_ID, '', '', '1', ADD_TAX_YN,'00',ONLINE_GUB_CODE ,UPPER_MANAGE_ID                     n")
            .append("    FROM IF_TAX_BILL_INFO_OFF_TEMP A     \n");	
	
            //System.out.println(sb.toString());
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
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
            	e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
           }
	}
	return bl;
    }

    
    /**
     * IF_TAX_BILL_ITEM_LIST 에서 TB_TRADE_ITEM_LIST로 DATA 전송.
     * @param Connection
     */ 
    public boolean item_list_trans(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = true;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO  /*+ APPEND */ TB_TRADE_ITEM_LIST                                                           \n")
            .append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO, PURCHASE_DAY, ITEM_NAME, ITEM_INFO,      \n")
            .append("    ITEM_DESC, UNIT_QUANTITY,UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT)                    \n")
            
            //2011.8.5 강성역과장 수정요청 반영
            //.append("SELECT /*+ ORDERED  FULL(C) PARALLEL(C 16) */                                           \n")
            //.append("SELECT /*+ full(c) full(b) parallel(c 4) parallel(a 4) */                                \n")
            
            //NESTED JOIN hint로 처리 지연 강제발생 시킴==> RAC에서 gc busy 발생
            //.append("SELECT /*+ USE_NL(C B) ORDERED  FULL(C) PARALLEL(C 16) */                                           \n")
            //HASH JOIN hint로 처리 지연 방지
            //.append("SELECT /*+ USE_HASH(C B) ORDERED  FULL(C) PARALLEL(C 16) */                                           \n")

            //2011.8.30 강성역과장, 강석봉차장  수정요청 최종 반영
            .append("SELECT /*+ USE_HASH(C B) full(C) full(B) parallel(C 4) parallel(A 4) */                                \n")
            .append("    '1',B.ISSUE_DAY,B.BIZ_MANAGE_ID, A.SEQ_NO, A.PURCHASE_DAY, A.ITEM_NAME, A.ITEM_INFO, \n")
            .append("    A.ITEM_DESC, A.UNIT_QUANTITY,A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT           \n")
            .append("FROM  IF_TAX_BILL_INFO_OFF_TEMP C,  TB_TAX_BILL_INFO B, IF_TAX_BILL_ITEM_LIST A          \n")
            .append("WHERE A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                                  \n")
            .append("AND   A.JOB_GUB_CODE = B.JOB_GUB_CODE                                                    \n")
            .append("AND   A.MANAGE_ID = B.SVC_MANAGE_ID                                                      \n")
            .append("AND   B.ONLINE_GUB_CODE = '2'                                                            \n")
            .append("AND   B.IO_CODE = '1'                                                                    \n")
            .append("AND   B.JOB_GUB_CODE = C.JOB_GUB_CODE                                                    \n")
            .append("AND   B.REL_SYSTEM_ID = C.REL_SYSTEM_ID                                                  \n")
            .append("AND   B.ISSUE_ID = C.ISSUE_ID                                                            \n")
            .append("AND   B.ISSUE_DAY = C.ISSUE_DAY														\n");
            //System.out.println(sb.toString());
            
    		CommUtil.logWriter(sb.toString(),1);
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
            	e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
            }
    	}
	return bl;
    }
    
   
    
    /**
     * IF_TAX_BILL_RESULT_INFO에 접수 결과 반영.
     * @param Connection
     */ 
    public boolean tax_result_info_insert(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	boolean bl = false;
	try{
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO IF_TAX_BILL_RESULT_INFO                           			\n")
            .append("    (JOB_GUB_CODE, REL_SYSTEM_ID, MANAGE_ID, STATUS_CODE,     			\n")
            .append("    STATUS_DESC, REGIST_DT, MODIFY_DT, ISSUE_ID, EAI_STAT)             \n")
            .append("SELECT /*+ FULL(A) PARALLEL(A 16) */                                   \n")
            .append("    A.JOB_GUB_CODE, A.REL_SYSTEM_ID, A.MANAGE_ID, '01',       			\n")
            .append("    '접수및작성완료',A.REGIST_DT, A.MODIFY_DT, A.ISSUE_ID, 'X'         \n")
            .append("FROM IF_TAX_BILL_INFO_OFF_TEMP A      									\n");



           // System.out.println(sb.toString());
    		CommUtil.logWriter(sb.toString(),1);
           
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
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
            	e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
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
            .append("INSERT INTO TB_STATUS_HIST                                                                                    \n")
            .append("    (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, AVL_BEGIN_DT,AVL_END_DT,                                              \n")
            .append("     SEQ_NO, STATUS_CODE, REGIST_DT, REGIST_ID)                                                               \n")
            .append("SELECT /*+ ORDERED FULL(B) PARALLEL(B 16) */                                                                                                       \n")
            .append("    A.IO_CODE, A.ISSUE_DAY, A.BIZ_MANAGE_ID, TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'), '99991231235959',           \n")
            .append("    1, '01', A.REGIST_DT, A.REGIST_ID                                                                         \n")
            .append("FROM IF_TAX_BILL_INFO_OFF_TEMP B, TB_TAX_BILL_INFO A                                                          \n")
            .append("WHERE  1> 0                                                                                            	   \n") 
            .append("AND A.ISSUE_ID = B.ISSUE_ID       \n");
            
            //System.out.println(sb.toString());
    		CommUtil.logWriter(sb.toString(),1);
            pstm = con.prepareStatement(sb.toString());
            if(pstm.executeUpdate()>0){
                bl = true;
            }
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
            	e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
            }
    	}
	return bl;
    }
    
    /**
     * IF_TAX_BILL_INFO 처리구분 업데이트
     * @param Connection
     */ 
    public boolean if_tax_bill_info_flg_update(Connection con)throws SQLException, Exception{
	
	boolean bl = true;
	PreparedStatement pstm=null;
	try{  
            StringBuffer sb= new StringBuffer()
            .append(" UPDATE  IF_TAX_BILL_INFO  A                                       \n")
            .append(" SET A.FLG = 'Y'                                                   \n")
            //2012. 6. 5 HASH JOIN 즉 2개 테이블 모두 full table 하기위해 수정 
            // from 시스템기술팀 강성역과장
            //.append(" WHERE (A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID) IN           \n")
            //2012.10.03 수정 
            //.append(" WHERE (A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID, A.ISSUE_ID ) IN           \n")
            .append(" WHERE (A.ISSUE_ID ) IN           \n")

            //2012. 3. 5 parallel 끼리 경합으로 오라클 CPU 100%등 처리 지연 발생하여 힌트문 제거
            // from 시스템기술팀 강성역과장
            //.append("       (SELECT /*+ FULL(B) PARALLEL(B 16) */                       \n")
            .append("       (SELECT                                                     \n")
            //2012. 6. 5 HASH JOIN 즉 2개 테이블 모두 full table 하기위해 수정 
            // from 시스템기술팀 강성역과장
            //.append("               B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID        \n")
            //2012.10.03 수정 
            //.append("               B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID, B.ISSUE_ID \n")
            .append("             B.ISSUE_ID \n")
            .append("        FROM IF_TAX_BILL_INFO_OFF_TEMP B )                         \n");          

//2012. 6. 5 변경전후 쿼리 및 플랜 비교
////  변경전
//            select * from     IF_TAX_BILL_INFO A
//            WHERE (A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID) IN
//                     (SELECT B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID
//                        FROM IF_TAX_BILL_INFO_OFF_TEMP B)
//Plan
//SELECT STATEMENT  ALL_ROWSCost: 106,494  Bytes: 1,259,927,456  Cardinality: 2,035,424  		
//	3 HASH JOIN  Cost: 106,494  Bytes: 1,259,927,456  Cardinality: 2,035,424  	
//		1 INDEX FAST FULL SCAN INDEX (UNIQUE) EXEDIOFF.IF_TAX_BILL_INFO_OFF_TEMP_PK Cost: 4,523  Bytes: 89,021,868  Cardinality: 2,070,276  
//		2 TABLE ACCESS FULL TABLE EXEDIOFF.IF_TAX_BILL_INFO Cost: 38,946  Bytes: 1,193,453,568  Cardinality: 2,071,968  
//                        
////  변경 후             
//		 select 
//	     * from     IF_TAX_BILL_INFO A
//	 WHERE (A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID, A.ISSUE_ID) IN
//	          (SELECT /*+ FULL(IF_TAX_BILL_INFO_OFF_TEMP) */  
//	          B.REL_SYSTEM_ID, B.JOB_GUB_CODE, B.MANAGE_ID, B.ISSUE_ID
//	             FROM IF_TAX_BILL_INFO_OFF_TEMP B)            
//	             
//Plan
//SELECT STATEMENT  ALL_ROWSCost: 143,342  Bytes: 644  Cardinality: 1  		
//	3 HASH JOIN  Cost: 143,342  Bytes: 644  Cardinality: 1  	
//		1 TABLE ACCESS FULL TABLE EXEDIOFF.IF_TAX_BILL_INFO_OFF_TEMP Cost: 38,925  Bytes: 140,778,768  Cardinality: 2,070,276  
//		2 TABLE ACCESS FULL TABLE EXEDIOFF.IF_TAX_BILL_INFO Cost: 38,946  Bytes: 1,193,453,568  Cardinality: 2,071,968  
	             
	             
            CommUtil.logWriter(sb.toString(),1);
            
            pstm = con.prepareStatement(sb.toString());
            pstm.executeUpdate();
        }catch(SQLException e){
            bl = false;
            e.printStackTrace();
            CommUtil.logWriter(e.toString(),4);
            throw e;
        }catch(Exception e1){
            bl = false;            
    	    e1.printStackTrace();
            CommUtil.logWriter(e1.toString(),4);
    	    throw e1;
    	}finally{
            try{
            	if (pstm != null){
            	    pstm.close();
            	}
            }catch (SQLException e){
            	e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
            }
    	}
    	return bl;
    }
    /**
     * IF_TAX_BILL_INFO_OFF_TEMP 초기화
     * @param con
     * @return boolean
     * @throws SQLException
     * @throws Exception
     */
    public boolean trun_if_temp(Connection con)throws SQLException, Exception{
    	
    	boolean bl = true;
    	PreparedStatement pstm=null;
    	try{
                StringBuffer sb= new StringBuffer()
                .append("TRUNCATE TABLE IF_TAX_BILL_INFO_OFF_TEMP     \n");
                
                //System.out.println(sb.toString());		
        		CommUtil.logWriter(sb.toString(),1);
                
                pstm = con.prepareStatement(sb.toString());
                pstm.executeUpdate();
            }catch(SQLException e){
                bl = false;
                e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
                throw e;
            }catch(Exception e1){
                bl = false;            
        	    e1.printStackTrace();
                CommUtil.logWriter(e1.toString(),4);
        	    throw e1;
        	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                	e.printStackTrace();
                    CommUtil.logWriter(e.toString(),4);
                }
        	}
        	return bl;
        } 
    
    public String[] getSumCntAmt(Connection con)throws SQLException, Exception{
    	
    	PreparedStatement pstm = null;
    	ResultSet rs = null;
    	String[] value = new String[2];
    	try{
                StringBuffer sb= new StringBuffer()
                .append("SELECT /*+ FULL(A) PARALLEL(A 32) */      			\n")
                .append("       COUNT(1), SUM(A.GRAND_TOTAL_AMOUNT)      	\n")
                .append("FROM   IF_TAX_BILL_INFO_OFF_TEMP A     			\n");

                	
        		CommUtil.logWriter(sb.toString(),1);
               
                pstm = con.prepareStatement(sb.toString());
                rs = pstm.executeQuery();

    			if(rs.next()){
    				value[0] = rs.getString(1);
    				value[1] = rs.getString(2);
    			}
            }catch(SQLException e){
                e.printStackTrace();
                CommUtil.logWriter(e.toString(),4);
                throw e;
            }catch(Exception e1){           
        	    e1.printStackTrace();
                CommUtil.logWriter(e1.toString(),4);
        	    throw e1;
        	}finally{
                try{
                	if (pstm != null){
                	    pstm.close();
                	}
                }catch (SQLException e){
                	e.printStackTrace();
                    CommUtil.logWriter(e.toString(),4);
                }
        	}
        	return value;
        }
    /**
     * IF_TAX_BILL_RESULT_INFO에 상태코드 05로 인서트
     * @param conn
     * @param relSystemId
     * @param jobGubCode
     * @param manageId
     * @return
     * @throws Exception
     */
	public int insertIfTaxBillResultInfo(Connection conn, String relSystemId,
			String jobGubCode, String manageId, String issueId,
			String registDt, String code, String desc) throws Exception {
	
		PreparedStatement pstmt = null;  
		int result = 0;
		
		
		StringBuffer query = new StringBuffer()		
		.append("	INSERT INTO IF_TAX_BILL_RESULT_INFO                 	\n")
		.append("	    (REL_SYSTEM_ID                            			\n")
		.append("	    ,JOB_GUB_CODE                                       \n")
		.append("       ,MANAGE_ID                                          \n")
		.append("       ,STATUS_CODE                                        \n")
		.append("       ,STATUS_DESC                                        \n")
		.append("       ,REGIST_DT                                         	\n")
		.append("       ,MODIFY_DT                                         	\n")
		.append("       ,ISSUE_ID                                         	\n")
		.append("       ,EAI_STAT                                         	\n")
		.append("       ,EAI_CDATE                                         	\n")
		.append("       ,EAI_UDATE                                         	\n")
		.append("       )                                               	\n")
		.append("       VALUES                                        	 	\n")
		.append("       (?, ?, ?, ?, ?, SYSDATE, SYSDATE, ?, 'X', '',''           \n")
		.append("       )                 									\n");

		CommUtil.logWriter(query.toString(),1);
		
		try{  

			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, relSystemId);
			pstmt.setString(2, jobGubCode);
			pstmt.setString(3, manageId);
			pstmt.setString(4, code);
			pstmt.setString(5, desc);
			//pstmt.setString(6, registDt);
			pstmt.setString(6, issueId);

			result = pstmt.executeUpdate(); 
			   
		}catch(Exception e) {
            CommUtil.logWriter(e.toString(),4);
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){CommUtil.logWriter(e.toString(),4);}
		}
		
		return result;
		
	}
    
    
}