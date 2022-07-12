package kr.co.kepco.etax30.selling.statistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommCipher;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;

public class StatisticsDao {
	private String DRIVER = CommProperties.getString("DB_DRIVER");
	private String URL    = CommProperties.getString("DB_URL");
	private String USER   = CommProperties.getString("DB_USER");
	private String PASS   = CommCipher.StringDecipher(CommProperties.getString("DB_PASS"));
	

/**
 * 전송방식별 매출세금계산서 국세청 전송현황(온라인, CD)
 * 집계용(2개월 이전것 1개월치를 일단위로 생성)
 */	
    public int tranMethodStatistics(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	
        try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_GROUP_STATISTICS (                                                    \n")
            .append("  IO_CODE, ISSUE_DAY, CODE_GRP_ID, CODE, CNT,                                        \n")
            .append("  SUM_CHARGE_AMT, SUM_TAX_AMT, REGIST_ID, MODIFY_ID)                                 \n")
            .append("SELECT                                                                               \n")
            .append("      '1' --매출                                                                     \n")
            .append("      ,ISSUE_DAY                       --작성일자                                    \n")
            .append("      ,'ONLINE_GUB_CODE'               --코드그룹ID'                                 \n")
            .append("      ,ONLINE_GUB_CODE                 --온오프라인 구분코드                         \n")
            .append("      ,COUNT(1)  AS CNT                --전송건수                                    \n")
            .append("      ,SUM(NVL(CHARGE_TOTAL_AMOUNT,0)) AS CHARGE_AMT --공급가합계                    \n")
            .append("      ,SUM(NVL(TAX_TOTAL_AMOUNT,0))    AS TAX_AMT    --세액합계                      \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("FROM   TB_TAX_BILL_INFO                                                              \n")
            .append("WHERE  1 > 0                                                                         \n")
            .append("AND    IO_CODE = '1' --매출                                                          \n")
            .append("AND    ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')   \n")
            .append("                     AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')       \n")
            .append("AND    ELECTRONIC_REPORT_YN = 'Y'                                                    \n")
            .append("AND    (ONLINE_GUB_CODE, STATUS_CODE) IN (('1', '04'), ('2','05')) --신고완료된것만  \n")
            .append("GROUP BY ISSUE_DAY, ONLINE_GUB_CODE                                                  \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
    	
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }
	
    
/**
 * 한전 종사업자별 매출세금계산서 국세청 전송현황
 * 집계용(2개월 이전것 1개월치를 일단위로 생성)
 */	
    public int classTypeStatistics(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	
        try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_GROUP_STATISTICS (                                                    \n")
            .append("  IO_CODE, ISSUE_DAY, CODE_GRP_ID, CODE, CNT,                                        \n")
            .append("  SUM_CHARGE_AMT, SUM_TAX_AMT, REGIST_ID, MODIFY_ID)                                 \n")
            .append("SELECT                                                                               \n")
            .append("       '1' --매출                                                                    \n")
            .append("      ,ISSUE_DAY                       --작성일자                                    \n")
            .append("      ,'INVOICER_TAX_REGIST_ID'        --코드그룹ID'                                 \n")
            .append("      ,INVOICER_TAX_REGIST_ID          --종사업장 식별코드                           \n")
            .append("      ,COUNT(1)  AS CNT                --전송건수                                    \n")
            .append("      ,SUM(NVL(CHARGE_TOTAL_AMOUNT,0)) AS CHARGE_AMT --공급가합계                    \n")
            .append("      ,SUM(NVL(TAX_TOTAL_AMOUNT,0))    AS TAX_AMT    --세액합계                      \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("FROM   TB_TAX_BILL_INFO                                                              \n")
            .append("WHERE  1 > 0                                                                         \n")
            .append("AND    IO_CODE = '1' --매출                                                          \n")
            .append("AND    ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')   \n")
            .append("                     AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')       \n")
            .append("AND    ELECTRONIC_REPORT_YN = 'Y'                                                    \n")
            .append("AND    (ONLINE_GUB_CODE, STATUS_CODE) IN (('1', '04'), ('2','05')) --신고완료된것만  \n")
            .append("GROUP BY ISSUE_DAY, INVOICER_TAX_REGIST_ID                                           \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
    	
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }    

    
/**
 * 한전 매입세금계산서 시스템별 국세청 전송현황
 * 집계용(2개월 이전것 1개월치를 일단위로 생성)
 */	
    public int systemTypeStatistics(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	
        try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_GROUP_STATISTICS (                                                    \n")
            .append("  IO_CODE, ISSUE_DAY, CODE_GRP_ID, CODE, CNT,                                        \n")
            .append("  SUM_CHARGE_AMT, SUM_TAX_AMT, REGIST_ID, MODIFY_ID)                                 \n")
            .append("SELECT                                                                               \n")
            .append("       '2'                             --매입                                        \n")
            .append("      ,ISSUE_DAY                       --작성일자                                    \n")
            .append("      ,'REL_SYSTEM_ID'                 --코드그룹ID'                                 \n")
            .append("      ,REL_SYSTEM_ID                   --연계시스템ID                                \n")
            .append("      ,COUNT(1)  AS CNT                --전송건수                                    \n")
            .append("      ,SUM(NVL(CHARGE_TOTAL_AMOUNT,0)) AS CHARGE_AMT --공급가합계                    \n")
            .append("      ,SUM(NVL(TAX_TOTAL_AMOUNT,0))    AS TAX_AMT    --세액합계                      \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("FROM   TB_TAX_BILL_INFO                                                              \n")
            .append("WHERE  1 > 0                                                                         \n")
            .append("AND    IO_CODE = '2' --매입                                                          \n")
            .append("AND    ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')   \n")
            .append("                     AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')       \n")
            .append("AND    ELECTRONIC_REPORT_YN = 'Y'                                                    \n")
            .append("AND    (ONLINE_GUB_CODE, STATUS_CODE) = (('1', '04')) --신고완료된것만               \n")
            .append("GROUP BY ISSUE_DAY, REL_SYSTEM_ID                                                    \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
    	
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }      

    
/**
 * 매입처별 메인세금계산서 전송현황
 * 집계용(2개월 이전것 1개월치를 일단위로 생성)
 */	
    public int buyingStatistics(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	
        try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("INSERT INTO TB_GROUP_STATISTICS (                                                    \n")
            .append("  IO_CODE, ISSUE_DAY, CODE_GRP_ID, CODE, CNT,                                        \n")
            .append("  SUM_CHARGE_AMT, SUM_TAX_AMT, REGIST_ID, MODIFY_ID)                                 \n")
            .append("SELECT                                                                               \n")
            .append("       '2'                             --매입                                        \n")
            .append("      ,'INVOICEE_GUB_CODE'             --코드그룹ID'                                 \n")
            .append("      ,ISSUE_DAY                       --작성일자                                    \n")
            .append("      ,INVOICEE_GUB_CODE               --공급받는자 구분코드(00:한전, 기타:발전사)   \n")
            .append("      ,COUNT(1)  AS CNT                --전송건수                                    \n")
            .append("      ,SUM(NVL(CHARGE_TOTAL_AMOUNT,0)) AS CHARGE_AMT --공급가합계                    \n")
            .append("      ,SUM(NVL(TAX_TOTAL_AMOUNT,0))    AS TAX_AMT    --세액합계                      \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("      ,'BATCH'                                                                       \n")
            .append("FROM   TB_TAX_BILL_INFO                                                              \n")
            .append("WHERE  1 > 0                                                                         \n")
            .append("AND    IO_CODE = '2' --매입                                                          \n")
            .append("AND    ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')   \n")
            .append("                     AND TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')       \n")
            .append("AND    ELECTRONIC_REPORT_YN = 'Y'                                                    \n")
            .append("AND    (ONLINE_GUB_CODE, STATUS_CODE) = (('1', '04')) --신고완료된것만               \n")
            .append("GROUP BY ISSUE_DAY, INVOICEE_GUB_CODE                                                \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
    	
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }
    

/**
 * 2개월전의 한달분 데이터 백업
 */	
    public int makeSaveTaxBillInfo(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	String yyyymm = "";
    	yyyymm = CommUtil.addDate(CommUtil.getKST("yyyyMM"), 1, -2); //2개월전
    	String if_tax_bill_info_ = "IF_TAX_BILL_INFO_";
    	String if_tax_bill_item_list_ = "IF_TAX_BILL_ITEM_LIST_";
    	if_tax_bill_info_ = if_tax_bill_info_ + yyyymm;
    	if_tax_bill_item_list_ = if_tax_bill_item_list_ + yyyymm;
       
    	try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("CREATE TABLE "+if_tax_bill_info_+" AS                                                \n")
            .append("SELECT *                                                                             \n")
            .append("FROM   IF_TAX_BILL_INFO                                                              \n")
            .append("WHERE  ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')   \n")
            .append("                 AND     TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')       \n")
            .append("AND    FLG = 'Y' --처리완료된 것만                                                   \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
    	
            StringBuffer sb2= new StringBuffer()
            .append("CREATE TABLE "+if_tax_bill_item_list_+" AS                                                \n")
            .append("SELECT B.* \n")
            .append("FROM   IF_TAX_BILL_INFO A, IF_TAX_BILL_ITEM_LIST B\n")
            .append("WHERE  A.ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')\n")
            .append("                   AND     TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')\n")
            .append("AND    A.FLG = 'Y' --처리완료된 것만\n")
            .append("AND    A.REL_SYSTEM_ID = B.REL_SYSTEM_ID \n")
            .append("AND    A.JOB_GUB_CODE  = B.JOB_GUB_CODE \n")
            .append("AND    A.MANAGE_ID     = B.MANAGE_ID \n");
           
            pstm = con.prepareStatement(sb2.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }
            
            
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }    
    
    
/**
 * 2개월전의 한달분 데이터를 삭제 
 */	
    public int deleteTaxBillInfo(){
        int result = 0;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	
        try{
            Class.forName(DRIVER);
    	    con = DriverManager.getConnection(URL, USER, PASS);
    	    con.setAutoCommit(false);	
    	
            StringBuffer sb= new StringBuffer()
            .append("DELETE                                                                                       \n")
            .append("FROM   IF_TAX_BILL_ITEM_LIST                                                                 \n")
            .append("WHERE  (REL_SYSTEM_ID, JOB_GUB_CODE, MANAGE_ID) IN                                           \n")
            .append("       (SELECT /*+ LEADING(A B) */ A.REL_SYSTEM_ID, A.JOB_GUB_CODE, A.MANAGE_ID              \n")
            .append("        FROM   IF_TAX_BILL_INFO A, IF_TAX_BILL_ITEM_LIST B                                   \n")
            .append("        WHERE  A.ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD') \n")
            .append("                           AND     TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')     \n")
            .append("        AND    A.FLG = 'Y' --처리완료된 것만                                                 \n")
            .append("        AND    A.REL_SYSTEM_ID = B.REL_SYSTEM_ID                                             \n")
            .append("        AND    A.JOB_GUB_CODE  = B.JOB_GUB_CODE                                              \n")
            .append("        AND    A.MANAGE_ID     = B.MANAGE_ID                                                 \n")
            .append("       )                                                                                     \n");
    	
            pstm = con.prepareStatement(sb.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }

            StringBuffer sb2= new StringBuffer()
            .append("DELETE \n")
            .append("FROM   IF_TAX_BILL_INFO\n")
            .append("WHERE  ISSUE_DAY BETWEEN TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-3)) + 1, 'YYYYMMDD')\n")
            .append("                 AND     TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE,-2)), 'YYYYMMDD')\n")
            .append("AND    FLG = 'Y' --처리완료된 것만\n");
    	
            pstm = con.prepareStatement(sb2.toString());
            result = pstm.executeUpdate();
            if(result>0){
        	con.commit();
            }            
            
            
        }catch(SQLException e) {
    	    try {
    		con.rollback();
    		e.printStackTrace();
    		throw e;
    	    } catch (SQLException e1) {
    		e1.printStackTrace();
    	    }
    	}catch(Exception e1){
    	    try {
    		con.rollback();
    		e1.printStackTrace();
    		throw e1;
    	    } catch (Exception e) {
    		e.printStackTrace();
    	    }
    	}finally{
           try{
    	   	if (con != null){
    	   	    con.close();
    	   	}
    	   	if(pstm != null){
    	   	    pstm.close();
    	   	}
          }catch (SQLException e){
              e.printStackTrace();
           }
        }
        return result;
    }
        
    
    
}
