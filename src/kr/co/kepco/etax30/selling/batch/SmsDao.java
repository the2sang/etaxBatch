package kr.co.kepco.etax30.selling.batch;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

//import com.sun.xml.internal.bind.v2.TODO;

import kr.co.kepco.etax30.selling.util.CommCipher;
import kr.co.kepco.etax30.selling.util.CommProperties;
import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.TaxInvSecurityMgr;
import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
/**
 * 
 * @author Yang,Hyungkong
 *
 */
public class SmsDao {
	
/**
     * SMS, E-MAIL 전송을 위한 데이터 추출  
     * @param Connection
     * 상태코드가 04 인 것 중  처리결과가 있는 것만 추출한다. 이후작업에서 완료상태로 업데이트하여 두번 추출되지 않는다.
     * 국세청에서 발행이 완료된 건 확인 하는 메세지
     */     
    
	public static String SMS_SENDER_NAME = "관리자";
	public static String SMS_SENDER_TEL = "0619317583";

    public Vector get_sms_data(Connection con)throws SQLException, Exception{
	PreparedStatement pstm=null;
	ResultSet rs = null;
	Vector vc =  new Vector();
	try{
		StringBuffer sb= new StringBuffer()
		.append("SELECT																			\n")
		//.append("C.IO_CODE, C.BIZ_MANAGE_ID, C.INVOICEE_PARTY_ID,C.INVOICER_PARTY_ID,			\n")
		.append("C.IO_CODE, C.SVC_MANAGE_ID, C.BIZ_MANAGE_ID,          			                \n")
		.append("C.INVOICEE_PARTY_ID,C.INVOICER_PARTY_ID,		                            	\n")
		.append("C.INVOICER_PARTY_NAME, C.INVOICEE_PARTY_NAME,									\n")
		.append("C.INVOICEE_CONTACT_PHONE1, C.INVOICEE_CONTACT_EMAIL1,C.INVOICEE_CONTACT_NAME1,	\n")
		.append("C.INVOICEE_CONTACT_PHONE2, C.INVOICEE_CONTACT_EMAIL2,C.INVOICEE_CONTACT_NAME2,	\n")
		.append("C.INVOICER_CONTACT_PHONE, C.INVOICER_CONTACT_EMAIL, C.INVOICER_CONTACT_NAME,	\n")
		.append("C.ISSUE_ID, C.ONLINE_GUB_CODE													\n")
		.append("FROM  TB_TAX_BILL_ON_BATCH A,TB_STATUS_HIST B,TB_TAX_BILL_INFO C				\n")
		.append("WHERE A.STATUS_CODE = '04'														\n")
		.append("AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID											\n")
		.append("AND A.IO_CODE = B.IO_CODE														\n")
		.append("AND A.ISSUE_DAY = B.ISSUE_DAY													\n")
		.append("AND B.STATUS_CODE  ='04'														\n")
		.append("AND B.AVL_END_DT = '99991231235959'											\n")
		.append("AND A.IO_CODE = C.IO_CODE														\n")
		.append("AND A.ONLINE_GUB_CODE = C.ONLINE_GUB_CODE										\n")
		.append("AND A.ISSUE_DAY = C.ISSUE_DAY													\n")
		.append("AND A.STATUS_CODE = C.STATUS_CODE												\n")
		//.append("AND C.ONLINE_GUB_CODE = '1' --20101013											\n")  
		.append(" \n");  
		
		pstm = con.prepareStatement(sb.toString());
		rs = pstm.executeQuery();
		
		while(rs.next()){
		    TbTaxBillInfoVo tbinfo = new TbTaxBillInfoVo();
		    tbinfo.setIo_code(CommUtil.SpaceChange(rs.getString("IO_CODE")));
		    tbinfo.setSvc_manage_id(CommUtil.SpaceChange(rs.getString("SVC_MANAGE_ID")));
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
		    tbinfo.setIssue_id1(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));
    	    tbinfo.setIssue_id2(CommCipher.StringCipher(CommUtil.SpaceChange(rs.getString("ISSUE_ID"))));
		    tbinfo.setOnline_gub_code(CommUtil.SpaceChange(rs.getString("ONLINE_GUB_CODE")));		    
		    vc.add(tbinfo);
		}
		con.commit();
        }catch(Exception e1){
    	}finally{
    	}
    	return vc;
    }
    /*20180122 sms->lms
     * 국세청에서 발행이 완료된 건 확인 하는 메세지
     * */
   
    public String tran_sms(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
	String short_uuid = "";
	try{
//		변경전 시작 20160420---------------------------------------------
//        	/*********************SMS서버 커넥션 맺기*****************************************/
//        	String DRIVER = CommProperties.getString("SMS_DB_DRIVER");
//        	String URL    = CommProperties.getString("SMS_DB_URL");
//        	String USER   = CommProperties.getString("SMS_DB_USER");
//        	String PASS   = CommCipher.StringDecipher(CommProperties.getString("SMS_DB_PASS"));
//        	Class.forName(DRIVER);
//        	con = DriverManager.getConnection(URL, USER, PASS);
//        	/**********************************************************************************/
//        	변경전 끝 20160420---------------------------------------------
//        	System.out.print("DRIVER==" +DRIVER);
//        	System.out.print("URL==" +URL);
//        	System.out.print("USER==" +USER);
//        	System.out.print("PASS==" +PASS);
//        	System.out.print(con);
        	
		
        	String in_sms_seditn  = CommProperties.getString("IN_SMS_SENDING");	//매출 SMS 전송 여부(1,0)
        	String out_sms_sending   = CommProperties.getString("OUT_SMS_SENDING"); //매입 SMS 전송 여부(1,0)
        	
        	String serverIP = InetAddress.getLocalHost().getHostAddress();	// server 구분위해20100621
        	
        	if("1".equals(in_sms_seditn)){//in_sms_seditn이   1(전송) 이면
        		System.out.println("===========================================&&&&&&&&&&매입&&&&&&&====================================================");
                    for(int i = 0; i<vc.size();i++){
                        TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                        if(tbinfo.getIo_code().equals("2")){//IO_CODE가 2인것중에서(매입인것중에서)
	                        if(tbinfo.getSvc_manage_id().length()== 16)
	                        	short_uuid = tbinfo.getSvc_manage_id().substring(9);
	                        else
	                        	short_uuid = tbinfo.getSvc_manage_id();
	                        String sms_body = "한전전자조달에서 귀사 매출세금계산서("+short_uuid+")를 확인하시기 바랍니다";
                    	try{
                            StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------
//                            .append("INSERT INTO EM_TRAN(                                                                  \n")
//                            .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
//                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
//                            .append("VALUES(                                                                               \n")
//                            .append("    EM_TRAN_PR.NEXTVAL, ?, ?, '1', SYSDATE,                                        \n")
//                            .append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '')  \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         
                            .append("INSERT INTO IF_EM_TRAN(                                                                  \n")
                            .append("    ID, TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                   \n")
                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4,                             \n")
                            .append("    TRAN_TYPE, TABLE_GB, IF_STATUS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT )      \n")
                            .append("VALUES(                                                                               \n")
                            .append("    'S'||IF_EM_TRAN_ID.NEXTVAL, '', ?, ?, '1', SYSDATE,                                        \n")
                            //.append("    '한전전자조달(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    ?,'한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    '4','','','SmsDao(tran_sms)',SYSDATE,'','')  \n");
                            
                            pstm = con.prepareStatement(sb.toString());
                            /* */
                            if("168.78.201.224".equals(serverIP)){
                            	pstm.setString(1,"01087348869"); //테스트전번
                            	System.out.println("한전매출 sms test server 입니다.");
                            }else{
                            	pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                            }
                			pstm.setString(2,SMS_SENDER_TEL);//TRAN_CALLBACK 실제 송신자번호 
                			pstm.setString(3,sms_body);
                			pstm.setString(4,SMS_SENDER_NAME);//송신자 이름
                			pstm.setString(5,SMS_SENDER_TEL);//송신자 번호
                        	
                        	if(pstm.executeUpdate()>0){
                                con.commit();
                            }
                    	}catch(Exception e1){}
                        }
                    }
        	}
        	
        	
        	if("1".equals(out_sms_sending)){//out_sms_sending   1(전송) 이면
                    for(int i = 0; i<vc.size();i++){
                    	System.out.println("===========================================&&&&&&&&&&매출&&&&&&&====================================================");
                        TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                        if(tbinfo.getIo_code().equals("1") && !tbinfo.getOnline_gub_code().equals("3")){//IO_CODE가 1인것중에서(매출인것중에서)
                        	short_uuid = tbinfo.getSvc_manage_id();
	                        String sms_body = "한전전자조달에서 귀사 매입세금계산서("+short_uuid+")를 확인하시기 바랍니다";
                        	
                    	try{
                            StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------                            
//                            .append("INSERT INTO EM_TRAN(                                                                  \n")
//                            .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
//                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
//                            .append("VALUES(                                                                               \n")
//                            .append("    EM_TRAN_PR.NEXTVAL, ?, ?, '1', SYSDATE,                                        \n")
//                            .append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '')  \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         
                            .append("INSERT INTO IF_EM_TRAN(                                                                  \n")
                            .append("    ID, TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                   \n")
                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4,                              \n")
                            .append("    TRAN_TYPE, TABLE_GB, IF_STATUS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT )      \n")
                            .append("VALUES(                                                                               \n")
                            .append("    'S'||IF_EM_TRAN_ID.NEXTVAL, '', ?, ?, '1', SYSDATE,                               \n")
                            //.append("    '한전전자조달(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    ?,'한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    '4','','','SmsDao(tran_sms)',SYSDATE,'','')  \n");
                            
                            pstm = con.prepareStatement(sb.toString());
                            if("168.78.201.224".equals(serverIP)){
                            	pstm.setString(1,"01087348869");
                            	System.out.println("한전매출 sms test server 입니다.");
                            }else{
                            	pstm.setString(1,tbinfo.getInvoicee_contact_phone1());
                            }
                			pstm.setString(2,SMS_SENDER_TEL);//TRAN_CALLBACK 실제 송신자번호 
                			pstm.setString(3,sms_body);
                			pstm.setString(4,SMS_SENDER_NAME);//송신자 이름
                			pstm.setString(5,SMS_SENDER_TEL);//송신자 번호
                            
                            if(pstm.executeUpdate()>0){
                                con.commit();
                            }
            		
                            sb.delete(0, sb.length());
 //변경전 시작 20160420---------------------------------------------                            
//                            sb.append("INSERT INTO EM_TRAN(                                                                \n")
//                            .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
//                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
//                            .append("VALUES(                                                                               \n")
//                            .append("    EM_TRAN_PR.NEXTVAL, ?, ?, '1', SYSDATE,                                        \n")
//                            .append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '')  \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         
                            sb.append("INSERT INTO IF_EM_TRAN(                                                                \n")
                            .append("    ID, TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4,                             \n")
                            .append("    TRAN_TYPE, TABLE_GB, IF_STATUS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT )      \n")
                            .append("VALUES(                                                                               \n")
                            .append("    'S'||IF_EM_TRAN_ID.NEXTVAL, '', ?, ?, '1', SYSDATE,                                        \n")
                            //.append("    '한전전자조달(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    ?,'한전전자세금계산서시스템', ?, ?, '',  \n")
                            .append("    '4','','','SmsDao(tran_sms)',SYSDATE,'','')  \n");

                            
                            pstm = con.prepareStatement(sb.toString());
                            
                            if("168.78.201.224".equals(serverIP)){
                            	pstm.setString(1,"01087348869");
                            	System.out.println("한전매출 sms test server 입니다.");
                            }else{
                            	pstm.setString(1,tbinfo.getInvoicee_contact_phone2());
                            }
                			pstm.setString(2,SMS_SENDER_TEL);//TRAN_CALLBACK 실제 송신자번호 
                			pstm.setString(3,sms_body);
                			pstm.setString(4,SMS_SENDER_NAME);//송신자 이름
                			pstm.setString(5,SMS_SENDER_TEL);//송신자 번호
                            
                            if(pstm.executeUpdate()>0){
                                con.commit();
                            }        		      
                    	}catch(Exception e1){}
                        }
                    }
        	}	
    }catch(Exception e1){
    }finally{
       try{
	   if(pstm != null){
	       pstm.close();
	   }
//변경전 시작 20160420---------------------------------------------                            
//	   if (con != null){
//	       con.close();
//	   }
//	 변경전 끝 20160420---------------------------------------------
           }catch (SQLException e){
               e.printStackTrace();
           }
    }
    	return remsg;
    }
    
   
    
    public String tran_email(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
	
	//String siteUrl = "http://cat.kepco.net/kepcobill2";
	String siteUrl = "";
	String contents ="";
    	
	try{
//		변경전 시작 20160420---------------------------------------------                            
	/*********************SMS서버 커넥션 맺기*****************************************/
//	String DRIVER = CommProperties.getString("EMAIL_DB_DRIVER");
//	String URL    = CommProperties.getString("EMAIL_DB_URL");
//	String USER   = CommProperties.getString("EMAIL_DB_USER");
//	String PASS   = CommCipher.StringDecipher(CommProperties.getString("EMAIL_DB_PASS"));
//	Class.forName(DRIVER);
//	con = DriverManager.getConnection(URL, USER, PASS);
	/**********************************************************************************/
//	 변경전 끝 20160420---------------------------------------------
		
	String in_email_sending  = CommProperties.getString("IN_EMAIL_SENDING");	//매출 SMS 전송 여부(1,0)
	String out_email_sending   = CommProperties.getString("OUT_EMAIL_SENDING"); //매입 SMS 전송 여부(1,0)
	String enc_issue_id2;
	TaxInvSecurityMgr SecuMgr = new TaxInvSecurityMgr();
	/*	*/
	String serverIP = InetAddress.getLocalHost().getHostAddress();
	if("168.78.201.224".equals(serverIP)){
		siteUrl = "https://168.78.201.224/kepcobill2";
	}else{
		siteUrl = "https://cat.kepco.net/kepcobill2";
	}
	
	if("1".equals(in_email_sending)){//in_email_sending   1(전송) 이면
	    	for(int i = 0; i<vc.size();i++){
	    	    TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
	    	    enc_issue_id2 = SecuMgr.TaxInvEncrypt(tbinfo.getIssue_id2());
	    	    enc_issue_id2 = SecuMgr.encodeURIComponent(enc_issue_id2);//POST 넘기는 경우 몇몇 문자열 깨짐 방지를 위한 encode처리 
		    	contents =
				"<html>\n"+
			    	"<head>\n"+
			    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
			    	"</head>\n"+
			    	"<body>\n"+
			    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"	<tr>\n"+
			    	"		<td align='CENTER' valign='TOP'><br>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
			    	"				</tr>\n"+
			    	"			</table><br>\n"+
			    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'>\n"+
			    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
			    	"							<tr>\n"+
			    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 세금계산서 정보입니다.</font></strong></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
			    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_name()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
			    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_id()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_name()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_phone()+"</font></td>\n"+
			    	"							</tr>\n"+
			    	"						</table>\n"+
			    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
//2016.10.20 링크주소에 AES암호화를 위한 AES암호화 값 추가  
			    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&enc_taxid="+enc_issue_id2+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
			    	"						</table><br>\n"+
			    	"					</td>\n"+
			    	"				</tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td valign='BOTTOM'>\n"+
			    	"						<font size=2> - 한전전자조달 : <font color=blue>https://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
			    	"				</td></tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
			    	"			</table>\n"+
			    	"		</td>\n"+
			    	"	</tr>\n"+
			    	"</table>\n"+
			    	"</body>\n"+
			    	"</html>\n";
			    Reader data = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));
	    	    if(tbinfo.getIo_code().equals("2")){//IO_CODE가 2인것중에서(매입인것중에서)
	    		try{
            			StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------            			
//            			.append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
//    	        	        .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
//    	        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
//    	       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
//    	       		        .append("VALUES (                                                                                          \n")
//    	       		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
//    	       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
//    	       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?)                                                 \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         
            			.append("INSERT INTO IF_IM_DMAIL_INFO(                                                                     \n")
	        	        .append("ID, SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,            \n")
	        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
	       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT,                           \n")
	       		        .append("TABLE_GB, IF_STATUS , CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT)                                 \n")
	       		        .append("VALUES (                                                                                          \n")
	       		        .append("'M'||IF_IM_DMAIL_INFO_ID.NEXTVAL,'', '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',    \n")
	       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
	       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?,                                                     \n")
            			.append("'13','','SmsDao(tran_email)',SYSDATE,'','')                                                       \n");

                  		pstm = con.prepareStatement(sb.toString());
                  		
                  		if("168.78.201.224".equals(serverIP)){
                  			pstm.setString(1,"SSV:"+"person1697@hanmail.net");
                  		}else{
                  			pstm.setString(1,"SSV:"+tbinfo.getInvoicee_contact_email1());
                  		}
                  		
                  		pstm.setString(2,  "한전 전자세금계산서");
                  		pstm.setString(3,"\""+tbinfo.getInvoicer_contact_name()+"\"<"+tbinfo.getInvoicer_contact_email()+">");
                  		pstm.setString(4, "1");
                    	pstm.setCharacterStream(5, data, contents.getBytes().length);
                		
        			if(pstm.executeUpdate()>0){
        			    con.commit();
        			}
    			}catch(Exception e1){
    			}finally{
			    pstm = null;
			    con.close();
    			}
	    	    }
	    	}
	}
	
	
	if("1".equals(out_email_sending)){//out_email_sending   1(전송) 이면
	    	for(int i = 0; i<vc.size();i++){
	    	    TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
	    	    enc_issue_id2 = SecuMgr.TaxInvEncrypt(tbinfo.getIssue_id2());
	    	    enc_issue_id2 = SecuMgr.encodeURIComponent(enc_issue_id2);//POST 넘기는 경우 몇몇 문자열 깨짐 방지를 위한 encode처리 

		    	contents =
				"<html>\n"+
			    	"<head>\n"+
			    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
			    	"</head>\n"+
			    	"<body>\n"+
			    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"	<tr>\n"+
			    	"		<td align='CENTER' valign='TOP'><br>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
			    	"				</tr>\n"+
			    	"			</table><br>\n"+
			    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'>\n"+
			    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
			    	"							<tr>\n"+
			    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 세금계산서 정보입니다.</font></strong></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
			    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_name()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
			    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_id()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_name()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_phone()+"</font></td>\n"+
			    	"							</tr>\n"+
			    	"						</table>\n"+
			    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
//2016.10.20 링크주소에 AES암호화를 위한 AES암호화 값 추가  
			    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&enc_taxid="+enc_issue_id2+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
			    	"						</table><br>\n"+
			    	"					</td>\n"+
			    	"				</tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td valign='BOTTOM'>\n"+
			    	"						<font size=2> - 한전전자조달 : <font color=blue>https://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
			    	"				</td></tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
			    	"			</table>\n"+
			    	"		</td>\n"+
			    	"	</tr>\n"+
			    	"</table>\n"+
			    	"</body>\n"+
			    	"</html>\n";
			    Reader data2 = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));	    	    
	    	    if(tbinfo.getIo_code().equals("1") && !tbinfo.getOnline_gub_code().equals("3")){//IO_CODE가 1인것중에서(매출인것중에서)
	    		try{
        			StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------            				
//            			.append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
//    	        	        .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
//    	        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
//    	       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
//    	       		        .append("VALUES (                                                                                          \n")
//    	       		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
//    	       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
//    	       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?)                                                 \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                                 			
        			.append("INSERT INTO IF_IM_DMAIL_INFO(                                                              \n")
        	        .append("ID, SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT,                           \n")
       		        .append("TABLE_GB, IF_STATUS , CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT)                                 \n")
       		        .append("VALUES (                                                                                          \n")
       		        .append("'M'||IF_IM_DMAIL_INFO_ID.NEXTVAL,'', '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?,                                                     \n")
       		        .append("'13','','SmsDao(tran_email)',SYSDATE,'','')                                                       \n");
  
        			pstm = con.prepareStatement(sb.toString());
	        			
        				if("168.78.201.224".equals(serverIP)){
	              			pstm.setString(1,"SSV:"+"person1697@hanmail.net");//박상종
	              			//pstm.setString(1,"SSV:"+"kimdy@kdn.com");//김도영
	              		}else{
	              			pstm.setString(1,"SSV:"+tbinfo.getInvoicee_contact_email1());
	              		}                  		
                  		pstm.setString(2,  "한전 전자세금계산서");
                  		pstm.setString(3,"\""+tbinfo.getInvoicer_contact_name()+"\"<"+tbinfo.getInvoicer_contact_email()+">");
                  		pstm.setString(4, "1");
                    	pstm.setCharacterStream(5, data2, contents.getBytes().length);
                		
        			if(pstm.executeUpdate()>0){
        			    con.commit();
        			}
  
        		    	contents =
    				"<html>\n"+
    			    	"<head>\n"+
    			    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
    			    	"</head>\n"+
    			    	"<body>\n"+
    			    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
    			    	"	<tr>\n"+
    			    	"		<td align='CENTER' valign='TOP'><br>\n"+
    			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
    			    	"				<tr>\n"+
    			    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
    			    	"				</tr>\n"+
    			    	"			</table><br>\n"+
    			    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
    			    	"				<tr>\n"+
    			    	"					<td align='LEFT' valign='TOP'>\n"+
    			    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
    			    	"							<tr>\n"+
    			    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 세금계산서 정보입니다.</font></strong></td>\n"+
    			    	"							</tr><tr>\n"+
    			    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
    			    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_name()+"</font></td>\n"+
    			    	"							</tr><tr>\n"+
    			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
    			    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_id()+"</font></td>\n"+
    			    	"							</tr><tr>\n"+
    			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
    			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_name()+"</font></td>\n"+
    			    	"							</tr><tr>\n"+
    			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
    			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_phone()+"</font></td>\n"+
    			    	"							</tr>\n"+
    			    	"						</table>\n"+
    			    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
//2016.10.20 링크주소에 AES암호화를 위한 AES암호화 값 추가  
    			    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&enc_taxid="+enc_issue_id2+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
    			    	"						</table><br>\n"+
    			    	"					</td>\n"+
    			    	"				</tr>\n"+
    			    	"			</table>\n"+
    			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
    			    	"				<tr>\n"+
    			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
    			    	"				</tr><tr>\n"+
    			    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
    			    	"				</tr><tr>\n"+
    			    	"					<td valign='BOTTOM'>\n"+
    			    	"						<font size=2> - 한전전자조달 : <font color=blue>https://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
    			    	"				</td></tr>\n"+
    			    	"			</table>\n"+
    			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
    			    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
    			    	"			</table>\n"+
    			    	"		</td>\n"+
    			    	"	</tr>\n"+
    			    	"</table>\n"+
    			    	"</body>\n"+
    			    	"</html>\n";
    			   // Reader data3 = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));	        			
        			
        		      sb.delete(0, sb.length());
 //변경전 시작 20160420---------------------------------------------            			        		      
//          		      sb.append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
//    	        	        .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
//    	        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
//    	       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
//    	       		        .append("VALUES (                                                                                          \n")
//    	       		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
//    	       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
//    	       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?)                                                 \n");
 //변경전 끝 20160420---------------------------------------------
 //메일 SMS 저장위치, 필드추가  따른 변경    20160420                         

          		      sb.append("INSERT INTO IF_IM_DMAIL_INFO(                                                              \n")
         	                .append("ID, SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
        	                .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
       		                .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT,                           \n")
	       		            .append("TABLE_GB, IF_STATUS , CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT)                                 \n")
       		                .append("VALUES (                                                                                          \n")
       		                .append("'M'||IF_IM_DMAIL_INFO_ID.NEXTVAL,'', '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
       		                .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
       		                .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?,                                                     \n")
            	    	    .append("'13','','SmsDao(tran_email)',SYSDATE,'','')                                                       \n");
          		      
          		      	pstm = con.prepareStatement(sb.toString());
          		      	
          		      	if("168.78.201.224".equals(serverIP)){
                			pstm.setString(1,"SSV:"+"person1697@hanmil.net");//박상종
	              			//pstm.setString(1,"SSV:"+"kimdy@kdn.com");//김도영
                		}else{
                			pstm.setString(1,"SSV:"+tbinfo.getInvoicee_contact_email1());
                		}
                  		pstm.setString(2,  "한전 전자세금계산서");
                  		pstm.setString(3,"\""+tbinfo.getInvoicer_contact_name()+"\"<"+tbinfo.getInvoicer_contact_email()+">");
                  		pstm.setString(4, "1");
                    	pstm.setCharacterStream(5, data2, contents.getBytes().length);
        			if(pstm.executeUpdate()>0){
        			    con.commit();
        			}        		      
        		}catch(Exception e1){
			}finally{
			   if(pstm != null){
			       pstm.close();
			   }
//			변경전 시작 20160420---------------------------------------------                            
//				   if (con != null){
//				       con.close();
//				   }
//				 변경전 끝 20160420---------------------------------------------
			}
	    }
	  }
	}	
	
	
    }catch(Exception e1){}
    	return remsg;
    }    
    
    
    
    
    
    
    
    
    /*
     * 매출 세금계산서 접수 후 sms 보내기
     * */
  
    public String acc_sms(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
	String short_uuid = "";
	try{
	    
//변경전 시작 20160420---------------------------------------------            			
        	/*********************SMS서버 커넥션 맺기*****************************************/
//        	String DRIVER = CommProperties.getString("SMS_DB_DRIVER");
//        	String URL    = CommProperties.getString("SMS_DB_URL");
//        	String USER   = CommProperties.getString("SMS_DB_USER");
//        	String PASS   = CommCipher.StringDecipher(CommProperties.getString("SMS_DB_PASS"));
//        	Class.forName(DRIVER);
//        	con = DriverManager.getConnection(URL, USER, PASS); 
		/**********************************************************************************/
//변경전 끝 20160420---------------------------------------------
        	
        	String out_sms_sending   = CommProperties.getString("OUT_SMS_SENDING"); //매출 SMS 전송 여부(1,0)
        	String serverIP = InetAddress.getLocalHost().getHostAddress();
        	int vc_size = vc.size();
        	
        	if("1".equals(out_sms_sending)){//out_sms_sending   1(전송) 이면
                System.out.println("매출 SMS 전송(총"+vc_size+"건)________________________________________________");        	    
                    for(int i = 0; i<vc_size;i++){
                        TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                        
                        short_uuid = tbinfo.getSvc_manage_id();
                        
                        //String sms_body = "한전전자조달(srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다"+tbinfo.getIssue_id().substring(18);
                        String sms_body = "한전전자조달에서 귀사 매입세금계산서("+short_uuid+")를 확인하시기 바랍니다";
                        
                        if(tbinfo.getIo_code().equals("1") && !tbinfo.getOnline_gub_code().equals("3")){//IO_CODE가 1인것중에서(매출인것중에서)
                    	try{
                            StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------            			
//                            .append("INSERT INTO EM_TRAN(                                                                  \n")
//                            .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
//                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
//                            .append("VALUES(                                                                               \n")
//                            .append("    EM_TRAN_PR.NEXTVAL, ?, ?, '1', SYSDATE,                                \n")
//                            //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '하지언', '0234296583', '5009')  \n");
//                            .append("    ?,'한전전자세금계산서시스템', ?, ?, '5009')  \n");
//                            //.append("    EM_TRAN_PR.NEXTVAL, ?, '', '1', SYSDATE,                                          \n")
//                            //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '', '', '')  \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         

                            .append("INSERT INTO IF_EM_TRAN(                                                                \n")
                            .append("    ID, TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                    \n")
                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 ,                             \n")
                            .append("    TRAN_TYPE, TABLE_GB, IF_STATUS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT )      \n")                           
                            .append("VALUES(                                                                               \n")
                            .append("    'S'||IF_EM_TRAN_ID.NEXTVAL, '', ?, ?, '1', SYSDATE,                               \n")
                            .append("    ?,'한전전자세금계산서시스템', ?, ?, '5009',                                           \n")
                            .append("    '4','','','SmsDao(acc_sms)',SYSDATE,'','')                                        \n");
                          
                            
                            pstm = con.prepareStatement(sb.toString());
                            
                            if("168.78.201.224".equals(serverIP)){
                      			pstm.setString(1,"01087348869");//박상종
                      		}else{
                      			pstm.setString(1,tbinfo.getInvoicee_contact_phone1());
                      		}                      
                			pstm.setString(2,SMS_SENDER_TEL);//TRAN_CALLBACK 실제 송신자번호 
                			pstm.setString(3,sms_body);
                			pstm.setString(4,SMS_SENDER_NAME);//송신자 이름
                			pstm.setString(5,SMS_SENDER_TEL);//송신자 번호

                            if(pstm.executeUpdate()>0){
                                con.commit();
                            }
                            /*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*
                             * 불필요하게 동일 SMS를 2번 보내도록 되어 있어 comment처리함
                             * 2013.11.11 빼빼로DAY
                            --*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*/        			
                            /*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*            		
                            sb.delete(0, sb.length());
                            sb.append("INSERT INTO EM_TRAN(                                                                \n")
                            .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
                            .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
                            .append("VALUES(                                                                               \n")
                            .append("    EM_TRAN_PR.NEXTVAL, ?, '0234296583', '1', SYSDATE,                                          \n")
                            //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '하지언', '0234296583', '5009')  \n");
                            .append("    ?,'한전전자세금계산서시스템', '심윤보', '0234296583', '5009')  \n");
                                                  //.append("    EM_TRAN_PR.NEXTVAL, ?, '', '1', SYSDATE,                                          \n")
                            //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '', '', '')  \n");
                            
                            pstm = con.prepareStatement(sb.toString());
                            
                            if("168.78.201.224".equals(serverIP)){
                            	pstm.setString(1,"01087348869");//박상종
                      		}else{
                      			pstm.setString(1,tbinfo.getInvoicee_contact_phone2());
                      		}
                			pstm.setString(2,sms_body);
                            
                            if(pstm.executeUpdate()>0){
                                con.commit();
                            }  
                            --*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*/      		      
                    	}catch(Exception e1){
                            e1.printStackTrace();
                    	}
                    	
                        }
                    }
        	}	
    }catch(Exception e1){
        e1.printStackTrace();
    }finally{
       try{
	   if(pstm != null){
	       pstm.close();
	   }
//	 변경전 시작 20160420--------------------------------------------            			
//	   if (con != null){
//	       con.close();
//	   }
//	 변경전 끝 20160420---------------------------------------------
	   
           }catch (SQLException e){
               e.printStackTrace();
           }
    }
    	return remsg;
    }
        
   //20180212 윤규미 sms->lms
    public String acc_lms(Vector vc, Connection con)throws SQLException, Exception{
    	String remsg = null;
    	PreparedStatement pstm=null;
    	String short_uuid = ""; //승인번호
    	String telno="";//전자세금계산서 전화번호
		String toMail="";//업체 email
		String docyear="";//작성일자 년도
		String docMonth="";//작성일자 월
		String docday="";//작성일자 일
		String systemName="";//시스템 이름(전자세금계산서/한전EDI구분)
		String comName = "";//업체명
		String tax_Amount_st="";//세액
		String grand_Amount_st="";//총금액
		String charge_Amount_st ="";//공급가액
		String item_Name="";//품명
    	try{

            	String out_sms_sending   = CommProperties.getString("OUT_SMS_SENDING"); //매출 SMS 전송 여부(1,0)
            	String serverIP = InetAddress.getLocalHost().getHostAddress();
            	String sms_body="";
            	String sms_body1="";
            	String sms_body2="";
            	String sms_body3="";

            	
            	int vc_size = vc.size();
            	
            	if("1".equals(out_sms_sending)){//out_sms_sending   1(전송) 이면
                    System.out.println("매출 LMS 전송(총"+vc_size+"건)________________________________________________");        	    
                        for(int i = 0; i<vc_size;i++){
                            TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                            docyear =tbinfo.getIssue_day().substring(0, 4)+"년";
                            docMonth =tbinfo.getIssue_day().substring(4, 6)+"월";
                            docday = tbinfo.getIssue_day().substring(6,8)+"일";
                            item_Name = tbinfo.getItem_list();//품명
                            charge_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getCharge_total_amount()))+"원";//공급가액
                            grand_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getGrand_total_amount()))+"원";//총금액
                          	tax_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getTax_total_amount()))+"원";  //세액
                          	short_uuid = tbinfo.getSvc_manage_id();
                          	
                                                                             
                            telno="0619317583";
                            systemName ="전자세금계산서시스템";
                            comName= tbinfo.getInvoicee_party_name();
                            
                            System.out.println("tbinfo.getInvoicee_contact_phone1()=======================>>>>>>>>"+tbinfo.getInvoicee_contact_phone1());
                            System.out.println("tbinfo.getInvoicee_contact_email1()============"+tbinfo.getInvoicee_contact_email1());
                            if("168.78.201.224".equals(serverIP)){
                      			toMail="persono1697@hanmail.net";
                      		}else{
                      			toMail = tbinfo.getInvoicee_contact_email1();
                      		} 
                            
                            
                            sms_body1 = comName+" 귀사에 발행된 매입세금계산서 승인요청 드립니다.";
                            sms_body2 = "#세금계산서 내역 \n - 작성일자 : "+docyear+docMonth+docday+"\n - 대상품목 : "+item_Name+"\n - 금액 : "+grand_Amount_st+"("+charge_Amount_st+"+"+tax_Amount_st+")\n - 관리번호 : "+short_uuid;
                            sms_body3="#세금계산서 승인 방법\n  (2가지중 선택해서 사용하세요)\n - 이메일 승인 방법\n   PC에서 귀하의 "+toMail+"에 도착한 메일 본문에서 승인\n - 시스템 승인 방법\n  PC에서 한전전자조달(https://srm.kepco.net)>>전자세금계산서>>매출>>미승인문서함에서 승인\n  (해당 메뉴에서 검색조건 작성일자를 조정한 후 조회 바랍니다.)";
                            
                            sms_body = sms_body1+"\n\n"+sms_body2+"\n\n"+sms_body3;
                            //System.out.println("sms_body"+sms_body);
                            if(tbinfo.getIo_code().equals("1") && !tbinfo.getOnline_gub_code().equals("3")){//IO_CODE가 1인것중에서(매출인것중에서)
                        	try{
                        		System.out.println("----------------------QUERY START------------------------");
                                StringBuffer sb= new StringBuffer()
                                .append(" INSERT INTO IF_MMS_MSG  \n ")
                                .append(" (PROCESS_DT, PROCESS_ID, MSGKEY, SUBJECT, PHONE,   \n ")
                                .append(" CALLBACK, STATUS, REQDATE, MSG,FILE_CNT,   \n ")
                                .append(" FILE_CNT_REAL, FILE_PATH1, FILE_PATH1_SIZ,FILE_PATH2, FILE_PATH2_SIZ,   \n ")
                                .append(" FILE_PATH3, FILE_PATH3_SIZ,FILE_PATH4, FILE_PATH4_SIZ, FILE_PATH5,   \n ")
                                .append(" FILE_PATH5_SIZ, EXPIRETIME, SENTDATE, RSLTDATE, REPORTDATE,   \n ")
                                .append(" TERMINATEDDATE, RSLT, REPCNT, TYPE, TELCOINFO,   \n ")
                                .append(" ID, POST, ETC1, ETC2,ETC3,   \n ")
                                .append(" ETC4, SKT_PATH1, SKT_PATH2, KT_PATH1, KT_PATH2,  \n ")
                                .append(" LGT_PATH1, LGT_PATH2, REPLACE_CNT, REPLACE_MSG, TRANS_YN,   \n ")
                                .append(" CREATE_DT, TRANS_DT)  \n ")
                                .append(" VALUES(TO_CHAR(SYSDATE,'YYYYMMDD') , MMS_MSG_P_ID.NEXTVAL,  MMS_MSG_SEQ.NEXTVAL@CYBER_LMS_LNK, '한국전력공사 매입전자세금계산서 발행 안내', ?,   \n ")
                                .append(" ?, '0', SYSDATE, ?, '0',  \n ")
                                .append(" '0', '', '', '', '',  \n ")
                                .append(" '', '', '', '', '' ,  \n ")
                                .append(" '', '43200', '', '', '',  \n ")
                                .append(" '', '', '0', '0', '',  \n ")
                                .append(" '', '', ?, '관리자', ?,  \n ")
                                .append(" '', '', '', '', '',  \n ")
                                .append(" '', '', '0', '', 'N',  \n ")
                                .append(" SYSDATE, '' )  \n "  );     
                                
                                pstm = con.prepareStatement(sb.toString());
                                
                                if("168.78.201.224".equals(serverIP)){
                          			pstm.setString(1,"01087348869");
                          		}else{
                          			pstm.setString(1,tbinfo.getInvoicee_contact_phone1());
                          		}                      
                                pstm.setString(2, telno);
    							pstm.setString(3, sms_body);
    							pstm.setString(4, systemName);
    							pstm.setString(5, telno);

                                if(pstm.executeUpdate()>0){
                                    con.commit();
                                }
                        	}catch(Exception e1){
                                e1.printStackTrace();
                        	}
                        	
                            }
                        }
            	}	
        }catch(Exception e1){
            e1.printStackTrace();
        }finally{
           try{
    	   if(pstm != null){
    	       pstm.close();
    	   }
    	   
               }catch (SQLException e){
                   e.printStackTrace();
               }
        }
        	return remsg;
        }    
    
    
    
    /*
     * 매출 세금계산서 접수 후 email 보내기
     * */
     
    
    public String acc_email(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
    	
	//String siteUrl = "http://cat.kepco.net/kepcobill2";
	String siteUrl ="";
	String contents ="";
	String enc_issue_id2;
	TaxInvSecurityMgr SecuMgr = new TaxInvSecurityMgr();
 	
	try{
//		 변경전 시작 20160420--------------------------------------------            			
	/*********************SMS서버 커넥션 맺기*****************************************/
//	String DRIVER = CommProperties.getString("EMAIL_DB_DRIVER");
//	String URL    = CommProperties.getString("EMAIL_DB_URL");
//	String USER   = CommProperties.getString("EMAIL_DB_USER");
//	String PASS   = CommCipher.StringDecipher(CommProperties.getString("EMAIL_DB_PASS"));
//	Class.forName(DRIVER);
//	con = DriverManager.getConnection(URL, USER, PASS);
//	 변경전 끝 20160420---------------------------------------------
	int vc_size = vc.size();
	/**********************************************************************************/
	System.out.println("acc_email()==========..");		
	String out_email_sending   = CommProperties.getString("OUT_EMAIL_SENDING"); //매출 email 전송 여부(1,0)	
	System.out.println("CommProperties.getString(OUT_EMAIL_SENDING) 설정값:"+out_email_sending);	
	
	/*	*/
	String serverIP = InetAddress.getLocalHost().getHostAddress();
	if("168.78.201.224".equals(serverIP)){
		siteUrl = "https://168.78.201.224/kepcobill2";
	}else{
		siteUrl = "https://cat.kepco.net/kepcobill2";
	}	
	
	if("1".equals(out_email_sending)){//out_email_sending   1(전송) 이면
		System.out.println("out_email_sending() 송신 총"+vc_size+"건=========================");	    
	    	for(int i = 0; i<vc_size;i++){
	    	    
	    	    TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
	    	    enc_issue_id2 = SecuMgr.TaxInvEncrypt(tbinfo.getIssue_id2());
	    	    enc_issue_id2 = SecuMgr.encodeURIComponent(enc_issue_id2);//POST 넘기는 경우 몇몇 문자열 깨짐 방지를 위한 encode처리 
	    	contents =
			"<html>\n"+
		    	"<head>\n"+
		    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
		    	"</head>\n"+
		    	"<body>\n"+
		    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"	<tr>\n"+
		    	"		<td align='CENTER' valign='TOP'><br>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
		    	"				</tr>\n"+
		    	"			</table><br>\n"+
		    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'>\n"+
		    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
		    	"							<tr>\n"+
		    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 세금계산서 정보입니다.</font></strong></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
		    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_name()+"</font></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
		    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_id()+"</font></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_name()+"</font></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_phone()+"</font></td>\n"+
		    	"							</tr>\n"+
		    	"						</table>\n"+
		    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	// 2011.11.30 변경 by conan
		    	//"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
//2016.10.20 링크주소에 AES암호화를 위한 AES암호화 값 추가  
		    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&mail=Y"+"&enc_taxid="+enc_issue_id2+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
		    	"						</table><br>\n"+
		    	"					</td>\n"+
		    	"				</tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td valign='BOTTOM'>\n"+
		    	"						<font size=2> - 한전전자조달 : <font color=blue>https://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
		    	"				</td></tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
		    	"			</table>\n"+
		    	"		</td>\n"+
		    	"	</tr>\n"+
		    	"</table>\n"+
		    	"</body>\n"+
		    	"</html>\n";
		    Reader data = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));
	    	    if(tbinfo.getIo_code().equals("1") && !tbinfo.getOnline_gub_code().equals("3")){//IO_CODE가 1인것중에서(매출인것중에서)
	    		try{
        			StringBuffer sb= new StringBuffer()
        			//변경전 시작 20160420---------------------------------------------            			
//    	      	    .append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
//    	            .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
//    	            .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
//    	       	    .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
//    	       	    .append("VALUES (                                                                                          \n")
//    	       	    .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
//    	       	    .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
//    	       	    .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?)                                                 \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420                         

            	    .append("INSERT INTO  IF_IM_DMAIL_INFO(                                                                    \n")
                    .append("ID, SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,            \n")
        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT,                           \n")
	       		    .append("TABLE_GB, IF_STATUS , CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT)                                 \n")       		        
       		        .append("VALUES (                                                                                          \n")
       		        .append("'M'||IF_IM_DMAIL_INFO_ID.NEXTVAL,'', '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',    \n")
       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?,                                                     \n")
            	    .append("'13','','SmsDao(acc_email)',SYSDATE,'','')                                                        \n");
                                
                  		pstm = con.prepareStatement(sb.toString());
                  		if("168.78.201.224".equals(serverIP)){
                  			//pstm.setString(1,"SSV:"+"kimdy@kdn.com");
                  			//pstm.setString(1,"SSV:"+"moone32@hanmail.net");
                  			pstm.setString(1,"SSV:"+"person1697@hanmail.net");
                  		}else{
                  			pstm.setString(1,"SSV:"+tbinfo.getInvoicee_contact_email1());
                  		}
                  		pstm.setString(2,  "\"한전세금계산서[송신전용]\"<kepcobill@kepco.co.kr>");
                  		pstm.setString(3,"\""+tbinfo.getInvoicee_contact_name1()+"\"<"+tbinfo.getInvoicee_contact_email1()+">");
                  		pstm.setString(4, "1");
                    		pstm.setCharacterStream(5, data, contents.getBytes().length);
                		
                                
        			if(pstm.executeUpdate()>0){
        			    con.commit();
        			}
 
                    /*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*
                     * 불필요하게 동일 메일을  2번 보내도록 되어 있어 comment처리함
                     * 2013.11.11 빼빼로DAY
                    --*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*/        			
					/*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*
        			   contents =
        				"<html>\n"+
        			    	"<head>\n"+
        			    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
        			    	"</head>\n"+
        			    	"<body>\n"+
        			    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	"	<tr>\n"+
        			    	"		<td align='CENTER' valign='TOP'><br>\n"+
        			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	"				<tr>\n"+
        			    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
        			    	"				</tr>\n"+
        			    	"			</table><br>\n"+
        			    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	"				<tr>\n"+
        			    	"					<td align='LEFT' valign='TOP'>\n"+
        			    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
        			    	"							<tr>\n"+
        			    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 세금계산서 정보입니다.</font></strong></td>\n"+
        			    	"							</tr><tr>\n"+
        			    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
        			    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_name()+"</font></td>\n"+
        			    	"							</tr><tr>\n"+
        			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
        			    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicer_party_id()+"</font></td>\n"+
        			    	"							</tr><tr>\n"+
        			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
        			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_name()+"</font></td>\n"+
        			    	"							</tr><tr>\n"+
        			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
        			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicer_contact_phone()+"</font></td>\n"+
        			    	"							</tr>\n"+
        			    	"						</table>\n"+
        			    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	// 2011.11.30 변경 by conan
        			    	//"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
        			    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&mail=Y"+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
        			    	"						</table><br>\n"+
        			    	"					</td>\n"+
        			    	"				</tr>\n"+
        			    	"			</table>\n"+
        			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	"				<tr>\n"+
        			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
        			    	"				</tr><tr>\n"+
        			    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
        			    	"				</tr><tr>\n"+
        			    	"					<td valign='BOTTOM'>\n"+
        			    	"						<font size=2> - SRM 시스템 : <font color=blue>http://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
        			    	"				</td></tr>\n"+
        			    	"			</table>\n"+
        			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
        			    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
        			    	"			</table>\n"+
        			    	"		</td>\n"+
        			    	"	</tr>\n"+
        			    	"</table>\n"+
        			    	"</body>\n"+
        			    	"</html>\n";	        			
     			    Reader data2 = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));        			
        		      sb.delete(0, sb.length());
          		      sb.append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
    	        	        .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
    	        	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
    	       		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
    	       		        .append("VALUES (                                                                                          \n")
    	       		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
    	       		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
    	       		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?)                                                 \n");
        
          		        pstm = con.prepareStatement(sb.toString());
          		        
          		        if("168.78.201.224".equals(serverIP)){
                			//pstm.setString(1,"SSV:"+"kimdy@kdn.com");
                  			//pstm.setString(1,"SSV:"+"moone32@hanmail.net");
                  			pstm.setString(1,"SSV:"+"latent@kdn.com");
                		}else{
                			pstm.setString(1,"SSV:"+tbinfo.getInvoicee_contact_email2());
                		}
          		        
                  		pstm.setString(2,  "\"한전세금계산서[송신전용]\"<kepcobill@kepco.co.kr>");
                  		pstm.setString(3,"\""+tbinfo.getInvoicee_contact_name2()+"\"<"+tbinfo.getInvoicee_contact_email2()+">");
                  		pstm.setString(4, " ");
                    		pstm.setCharacterStream(5, data2, contents.getBytes().length);  			
        			if(pstm.executeUpdate()>0){
        			    con.commit();
        			}
                    --*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*--*#*/        			        		      
        		}catch(Exception e1){
        		    e1.printStackTrace();
			    }
	    	    }
	    	}
	}	
	
    }catch(Exception e1){
	e1.printStackTrace();
	
    }finally{
	       try{
		   if(pstm != null){
		       pstm.close();
		   }
//		변경전 시작 20160420---------------------------------------------                            
//		   if (con != null){
//		       con.close();
//		   }
//		 변경전 끝 20160420---------------------------------------------		   
	           }catch (SQLException e){
	               e.printStackTrace();
	           }
	    }
    	return remsg;
    }      
    

    /*
     * 매입세금계산서 2ndSign 요청 알림  sms 보내기
     * executeupdate를 executebatch로 변경
     * 변경 사유 : executeupdate시 ORA-01000 커서 초과 에러가 발생하므로
     * execute처리를 한꺼번에 한번만 처리하는 executebatch로 변경
     * */
/*
     public String SecondSign_sms(Vector vc)throws SQLException, Exception{
    	String remsg = null;
    	Connection con =  null;
    	PreparedStatement pstm=null;
    	String short_uuid;
    	int[] rs;
    	int k = 0;
    	
    	try{
            	//--------------------------------------------------------------------------------/
            	String DRIVER = CommProperties.getString("SMS_DB_DRIVER");
            	String URL    = CommProperties.getString("SMS_DB_URL");
            	String USER   = CommProperties.getString("SMS_DB_USER");
            	String PASS   = CommCipher.StringDecipher(CommProperties.getString("SMS_DB_PASS"));
            	Class.forName(DRIVER);
            	con = DriverManager.getConnection(URL, USER, PASS);
            	//--------------------------------------------------------------------------------/
            	
            	String in_sms_sending   = CommProperties.getString("IN_2NDSIGN_SMS_SENDING"); // SMS 전송 여부(1,0)
            	System.out.println("IN_2NDSIGN_SMS_SENDING : "+in_sms_sending);	
            	String serverIP = InetAddress.getLocalHost().getHostAddress();
            	
            	if("1".equals(in_sms_sending)){//out_sms_sending   1(전송) 이면
                    System.out.println("매입세금계산서 2ndSign 요청 알림  SMS 전송____________________________________________________________");        	    
                    con.setAutoCommit(false);
                    StringBuffer sb= new StringBuffer()
                    .append("INSERT INTO EM_TRAN(                                                                  \n")
                    .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
                    .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
                    .append("VALUES(                                                                               \n")
                    .append("    EM_TRAN_PR.NEXTVAL, ?, '0234296583', '1', SYSDATE,                                \n")
                    .append("    ?,'한전전자세금계산서시스템', '심윤보', '0234296583', '5009')  \n");
                    pstm = con.prepareStatement(sb.toString());
                    
                        for(int i = 0; i<vc.size();i++){
                            TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                            
                            if(tbinfo.getSvc_manage_id().length()== 16)
                            	short_uuid = tbinfo.getSvc_manage_id().substring(9);
                            else
                            	short_uuid = tbinfo.getSvc_manage_id();
                            
                            String sms_body = "한전공급자포탈(srm.kepco.net)에서 귀사의 세금계산서를 발급하시기 바랍니다"+short_uuid;
                            
                            if(tbinfo.getIo_code().equals("2")&& !tbinfo.getInvoicer_contact_phone().equals(" ")){//IO_CODE가 2인 매입만 처리
                                if("168.78.201.224".equals(serverIP)){
                          			if(tbinfo.getInvoicer_contact_phone().equals("010-2733-2512")||tbinfo.getInvoicer_contact_phone().equals("01095524125"))//안남준과장, 박현철
                              			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                          			else
                                	    pstm.setString(1,"01093717264");//박상종
                          		}else{
                          			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                          		}                      
                    			pstm.setString(2,sms_body);
                    	        k++;
                    			pstm.addBatch();
                    			//pstm.clearParameters();
                    	        //100개씩 excutebatch처리 
                    	        if(k%100==0){
                    	           rs = pstm.executeBatch();	
                    	           pstm.clearBatch();
                    	           con.commit();
                    	           //rs[0], rs[1]... 개별값이 (0~n:성공 row수, -2:성공 했으나 row수를 알수 없음 -3:실패)
                                   CommUtil.logWriter("100개씩 pstm.executeBatch 갯수:"+rs.length,4);
                    	        }
                            }
                        }
            	        //나머지 excutebatch처리 
                        rs = pstm.executeBatch();
         	            pstm.clearBatch();
                        con.commit();
         	           //rs[0], rs[1]... 개별값이 (0~n:성공 row수, -2:성공 했으나 row수를 알수 없음 -3:실패)
                        CommUtil.logWriter("나머지 pstm.executeBatch 갯수:"+rs.length,4);
            	}	
        }catch(Exception e1){
        	con.rollback();
            e1.printStackTrace();
        }finally{
           try{
        	   if(pstm != null){
        	       pstm.close();
        	   }
	    	   if (con != null){
          		   con.setAutoCommit(true);
	    	       con.close();
	    	   }
               }catch (SQLException e){
                   e.printStackTrace();
               }
        }
   	return remsg;
   }
 */
    /*
     * 매입세금계산서 2ndSign 요청 알림  lms 보내기
     * 20180122 윤규미
     * 영문/한글 상관없이 4000byte까지 가능 [web발신]이라는 문구가 항상 처음에 붙어서 송신됨 
     * */
    public String SecondSign_lms(Vector vc, Connection con)throws SQLException, Exception{
    	String remsg = null;
    	PreparedStatement pstm=null;
    	String short_uuid;
    	int rs;
		String telno=""; //0619317583
		String phone=""; //0619317583
		String toMail="";//email
		String docyear="";//issue_day 년
		String docMonth="";//issue_day 월
		String docday="";//issue_day 일
		String systemName="";//시스템명
		String comName = "";//업체명
		String charge_Amount_st="";//공급가액
		String tax_Amount_st="";//세액
		String grand_Amount_st="";//총금액
		String item_Name ="";//품목명
    	try{
            	String in_sms_sending   = CommProperties.getString("IN_2NDSIGN_SMS_SENDING"); // SMS 전송 여부(1,0)
            	System.out.println("IN_2NDSIGN_SMS_SENDING : "+in_sms_sending);	
            	String serverIP = InetAddress.getLocalHost().getHostAddress();
            	String sms_body="";
            	String sms_body1="";
            	String sms_body2="";
            	String sms_body3="";
            	String sms_body4="";
             	Date today = new Date();                
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                int Int_to_day = Integer.parseInt(date.format(today));

            	
    CommUtil.logWriter("SecondSign_lms 1",1);
            	if("1".equals(in_sms_sending)){//out_sms_sending   1(전송) 이면        	    
                        for(int i = 0; i<vc.size();i++){
                        	
                            TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);          
                        	  System.out.println("________________________________________________한전매입lms____________________________________________");
                              docyear =tbinfo.getIssue_day().substring(0, 4)+"년";
                              docMonth =tbinfo.getIssue_day().substring(4, 6)+"월";
                              docday = tbinfo.getIssue_day().substring(6,8)+"일";
                              item_Name = tbinfo.getItem_list();
                              charge_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getCharge_total_amount()))+"원";
                              grand_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getGrand_total_amount()))+"원";
                            	  tax_Amount_st=CommUtil.Comma_Conv(Long.toString(tbinfo.getTax_total_amount()))+"원";
                            	  System.out.println("tbinfo.getInvoicer_contact_phone()========>"+tbinfo.getInvoicer_contact_phone());
                                  System.out.println("tbinfo.getInvoicer_contact_email()###########>>>>>"+tbinfo.getInvoicer_contact_email());
                              CommUtil.logWriter("tbinfo.getEdi_returned_date():"+tbinfo.getEdi_returned_date(),1);
                            if("168.78.201.224".equals(serverIP)){
                      			//pstm.setString(1,"SSV:"+"kimdy@kdn.com");
                      			//pstm.setString(1,"SSV:"+"moone32@hanmail.net");
                      			toMail="persono1697@hanmail.net";
                      			phone = "01087348869";
                      		}else{
                      			toMail = tbinfo.getInvoicer_contact_email();
                      			phone=tbinfo.getInvoicer_contact_phone();
                      		}
                            CommUtil.logWriter("SecondSign_sms 2",1);
                            if(tbinfo.getSvc_manage_id().length()== 10){
                            	short_uuid = tbinfo.getSvc_manage_id();
                            	telno="0619317584";
                            	systemName ="한전전력EDI시스템";                            	
                            }else{
                            	short_uuid = tbinfo.getSvc_manage_id().substring(9);
                            	telno="0619317583";
                            	systemName ="전자세금계산서시스템";
                            	
                            	}
                            comName =tbinfo.getInvoicer_party_name();
                            
                            
                            sms_body1 = comName+" 귀사에서 작성하신 대금청구에 대한 세금계산서 발행 요청 드립니다.";
                            sms_body2 = "#세금계산서 내역 \n - 작성일자 : "+docyear+docMonth+docday+"\n - 대상품목 : "+item_Name+"\n - 금액 : "+grand_Amount_st+"("+charge_Amount_st+"+"+tax_Amount_st+")\n - 문서번호 : "+short_uuid;
                            sms_body3="#세금계산서 발행 방법\n  (2가지중 선택해서 사용하세요)\n - 이메일 발행 방법\n  PC에서 귀하의 "+toMail+"에 도착한 메일 본문에서  발행\n - 시스템 발행 방법\n  PC에서 한전전자조달(https://srm.kepco.net)>>전자세금계산서>>세금계산서발행 메뉴에서 발행\n (해당 메뉴에서 검색조건 작성일자를 조정한  후 조회 바랍니다.)\n\n * 세금계산서 발행은 입금보다 먼저 진행 될수 있습니다.\n\n - 입금 관련 문의는 대금청구서상의 계약자 회계부서로 문의 하시기 바랍니다.";
                            
                            sms_body = sms_body1 +"\n\n"+ sms_body2+"\n\n"+sms_body3; 
                            
                            if(tbinfo.getEdi_returned_date().length()==8){
                            	sms_body1 = comName+" 귀사에서 작성하신 대금청구에 대한 세금계산서 발행 요청 드립니다.";
                               	sms_body2 = "#세금계산서 내역 \n - 작성일자 : "+docyear+docMonth+docday+"\n - 대상품목 : "+item_Name+"\n - 금액 : "+grand_Amount_st+"("+charge_Amount_st+"+"+tax_Amount_st+")\n - 문서번호 : "+short_uuid;
                               	sms_body3="#세금계산서 발행 방법\n  (2가지중 선택해서 사용하세요)\n - 이메일 발행 방법\n  PC에서 귀하의 "+toMail+"에 도착한 메일 본문에서 발행\n - 시스템 발행 방법\n  PC에서 한전전자조달(https://srm.kepco.net)>>한전EDI>>보낸문서함에서 발행\n (해당 메뉴에서 검색조건 작성일자를 조정한 후 조회 바랍니다.)";
                                if(Integer.parseInt(tbinfo.getEdi_returned_date())>=Int_to_day){//지급예정일 과거이면 SMS문자에 포함시키지 않으려고....
                            		sms_body4= "[입금예정일:"+Integer.parseInt(tbinfo.getEdi_returned_date().substring(4,6))+"."+ Integer.parseInt(tbinfo.getEdi_returned_date().substring(6,8))+"일]";
                            		sms_body = sms_body1 +"\n\n"+ sms_body2+"\n\n"+sms_body4+"\n\n"+sms_body3;
                            	}else{
                            	sms_body = sms_body1 +"\n\n"+ sms_body2+"\n\n"+sms_body3;
                            	} 
                            }
                            
                           // CommUtil.logWriter("sms_body:"+sms_body,1);
                            
                            if(tbinfo.getIo_code().equals("2")&& !phone.equals(" ")){//IO_CODE가 2인 매입만 처리
                            	System.out.println("-------------------QUREY START----------------------------");
    	                        StringBuffer sb= new StringBuffer()  
    	                        .append(" INSERT INTO IF_MMS_MSG  \n ")
                                .append(" (PROCESS_DT, PROCESS_ID, MSGKEY, SUBJECT, PHONE,   \n ")
                                .append(" CALLBACK, STATUS, REQDATE, MSG,FILE_CNT,   \n ")
                                .append(" FILE_CNT_REAL, FILE_PATH1, FILE_PATH1_SIZ,FILE_PATH2, FILE_PATH2_SIZ,   \n ")
                                .append(" FILE_PATH3, FILE_PATH3_SIZ,FILE_PATH4, FILE_PATH4_SIZ, FILE_PATH5,   \n ")
                                .append(" FILE_PATH5_SIZ, EXPIRETIME, SENTDATE, RSLTDATE, REPORTDATE,   \n ")
                                .append(" TERMINATEDDATE, RSLT, REPCNT, TYPE, TELCOINFO,   \n ")
                                .append(" ID, POST, ETC1, ETC2,ETC3,   \n ")
                                .append(" ETC4, SKT_PATH1, SKT_PATH2, KT_PATH1, KT_PATH2,  \n ")
                                .append(" LGT_PATH1, LGT_PATH2, REPLACE_CNT, REPLACE_MSG, TRANS_YN,   \n ")
                                .append(" CREATE_DT, TRANS_DT)  \n ")
                                .append(" VALUES(TO_CHAR(SYSDATE,'YYYYMMDD') , MMS_MSG_P_ID.NEXTVAL,  MMS_MSG_SEQ.NEXTVAL@CYBER_LMS_LNK, '한국전력공사 전자세금계산서 발행 안내', ?,   \n ")
                                .append(" ?, '0', SYSDATE, ?, '0',  \n ")
                                .append(" '0', '', '', '', '',  \n ")
                                .append(" '', '', '', '', '' ,  \n ")
                                .append(" '', '43200', '', '', '',  \n ")
                                .append(" '', '', '0', '0', '',  \n ")
                                .append(" '', '', ?, '관리자', ?,  \n ")
                                .append(" '', '', '', '', '',  \n ")
                                .append(" '', '', '0', '', 'N',  \n ")
                                .append(" SYSDATE, '' )  \n "  );    
    	                        
    	                        
    	                        
    	                        
                        	try{
                                pstm = con.prepareStatement(sb.toString());
                                if("168.78.201.224".equals(serverIP)){
                          			if(tbinfo.getInvoicer_contact_phone().equals("010-8734-8869")||tbinfo.getInvoicer_contact_phone().equals("01087348869"))//안남준과장, 박현철
                              			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                          			else
                                	    pstm.setString(1,"01087348869");//박상종
                          		}else{
                          			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                          		}
                                CommUtil.logWriter("SecondSign_sms 3",1);
    							pstm.setString(2, telno);
    							pstm.setString(3, sms_body);
    							pstm.setString(4, systemName);
    							pstm.setString(5, telno);

                    	        rs = pstm.executeUpdate();
                    	        
                                if(rs>0){
                                    CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
                                	con.commit();
                                }
                        	}catch(Exception e1){
                                CommUtil.logWriter(sb.toString(),4);
                                e1.printStackTrace();
                        	}
                        	
                            }
                        }
                      }
               
            		
        }catch(Exception e1){
            e1.printStackTrace();
        }finally{
           try{
    	   if(pstm != null){
    	       pstm.close();
    	   }
           }catch (SQLException e){
                   e.printStackTrace();
               }
        }
        	return remsg;
        }
    
    
    /*
     * 매입세금계산서 2ndSign 요청 알림  sms 보내기
     * executeupdate를 executebatch로 변경
     * 영문/한글 상관없이 64자까지 가능 [web발신]이라는 문구가 항상 처음에 붙어서 송신됨 
     * */
    
   public String SecondSign_sms(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
	String short_uuid;
	int rs;
	
	try{
//		변경전 시작 20160420---------------------------------------------                            
  		    //---------------------SMS서버 커넥션 맺기-----------------------------------------/
//        	String DRIVER = CommProperties.getString("SMS_DB_DRIVER");
//        	String URL    = CommProperties.getString("SMS_DB_URL");
//        	String USER   = CommProperties.getString("SMS_DB_USER");
//        	String PASS   = CommCipher.StringDecipher(CommProperties.getString("SMS_DB_PASS"));
//        	Class.forName(DRIVER);
//        	con = DriverManager.getConnection(URL, USER, PASS);
        	//------------------------------------------------------------------------------/
//        	변경전 끝 20160420---------------------------------------------
        	
        	String in_sms_sending   = CommProperties.getString("IN_2NDSIGN_SMS_SENDING"); // SMS 전송 여부(1,0)
        	System.out.println("IN_2NDSIGN_SMS_SENDING : "+in_sms_sending);	
        	String serverIP = InetAddress.getLocalHost().getHostAddress();
        	String sms_body="";
        	String sms_body1="";
        	String sms_body2="";
         	Date today = new Date();                
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
            int Int_to_day = Integer.parseInt(date.format(today));

        	
CommUtil.logWriter("SecondSign_sms 1",1);
        	if("1".equals(in_sms_sending)){//out_sms_sending   1(전송) 이면
                System.out.println("매입세금계산서 2ndSign 요청 알림  SMS 전송____________________________________________________________");        	    
                    for(int i = 0; i<vc.size();i++){
                        TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
                        
CommUtil.logWriter("SecondSign_sms 2",1);
                        if(tbinfo.getSvc_manage_id().length()== 16)
                        	short_uuid = tbinfo.getSvc_manage_id().substring(9);
                        else
                        	short_uuid = tbinfo.getSvc_manage_id();
                        
CommUtil.logWriter("tbinfo.getEdi_returned_date():"+tbinfo.getEdi_returned_date(),1);
                        //sms_body1 = "한전SRM에서 귀사의 세금계산서를 발급하시기 바랍니다.->srm.kepco.net 문서번호:"+short_uuid;
                        //sms_body1 = "한전SRM에서 귀사 세금계산서(문서번호:"+short_uuid+")를 발급하시기 바랍니다.";
                        sms_body1 = "한전전자조달에서 귀사 매출세금계산서("+short_uuid+")를 발급하시기 바랍니다.";
                        sms_body2 = "";
                        if(tbinfo.getEdi_returned_date().length()==8){
                        	if(Integer.parseInt(tbinfo.getEdi_returned_date())>=Int_to_day){//지급예정일 과거이면 SMS문자에 포함시키지 않으려고....
                         	   //sms_body2 = tbinfo.getEdi_returned_date().substring(4,6)+"월"+ tbinfo.getEdi_returned_date().substring(6,8)+"일 입금예정";
                       	       sms_body2 = "[입금예정일:"+Integer.parseInt(tbinfo.getEdi_returned_date().substring(4,6))+"."+ Integer.parseInt(tbinfo.getEdi_returned_date().substring(6,8))+"일]";
                        	}
                        }
                        sms_body = sms_body1 + sms_body2; 

CommUtil.logWriter("sms_body:"+sms_body,1);
                        
                        if(tbinfo.getIo_code().equals("2")&& !tbinfo.getInvoicer_contact_phone().equals(" ")){//IO_CODE가 2인 매입만 처리
	                        StringBuffer sb= new StringBuffer()
//변경전 시작 20160420---------------------------------------------            				                        
//	                        .append("INSERT INTO EM_TRAN(                                                                  \n")
//	                        .append("    TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
//	                        .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4 )                            \n")
//	                        .append("VALUES(                                                                               \n")
//	                        .append("    EM_TRAN_PR.NEXTVAL, ?, ?, '1', SYSDATE,                                \n")
//	                        //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '하지언', '0234296583', '5009')  \n");
//	                        .append("    ?,'한전전자세금계산서시스템', ?, ?, '5009')  \n");
//	                        //.append("    EM_TRAN_PR.NEXTVAL, ?, '', '1', SYSDATE,                                          \n")
//	                        //.append("    '한전공급자포탈(http://srm.kepco.net)에서 귀사의 세금계산서를 확인하시기 바랍니다','한전전자세금계산서시스템', '', '', '')  \n");
//변경전 끝 20160420---------------------------------------------
//메일 SMS 저장위치, 필드추가  따른 변경    20160420    
	                        .append("INSERT INTO IF_EM_TRAN(                                                                  \n")
	                        .append("    ID, TRAN_PR, TRAN_PHONE, TRAN_CALLBACK, TRAN_STATUS, TRAN_DATE,                       \n")
	                        .append("    TRAN_MSG, TRAN_ETC1, TRAN_ETC2, TRAN_ETC3, TRAN_ETC4,                             \n")
                            .append("    TRAN_TYPE, TABLE_GB, IF_STATUS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT )      \n")	                        
	                        .append("VALUES(                                                                               \n")
	                        .append("    'S'||IF_EM_TRAN_ID.NEXTVAL, '', ?, ?, '1', SYSDATE,                               \n")
	                        .append("    ?,'한전전자세금계산서시스템', ?, ?, '5009',                                           \n")
                            .append("    '4','','','SmsDao(SecondSign_sms)',SYSDATE,'','')  \n");

	                        
	                        
	                        
	                        
                    	try{
                            pstm = con.prepareStatement(sb.toString());
                            if("168.78.201.224".equals(serverIP)){
                      			if(tbinfo.getInvoicer_contact_phone().equals("010-8734-8869")||tbinfo.getInvoicer_contact_phone().equals("01087348869"))//안남준과장, 박현철
                          			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                      			else
                            	    pstm.setString(1,"01087348869");//박상종
                      		}else{
                      			pstm.setString(1,tbinfo.getInvoicer_contact_phone());
                      		}                      
CommUtil.logWriter("SecondSign_sms 3",1);
                            pstm.setString(2,SMS_SENDER_TEL);//TRAN_CALLBACK 실제 송신자번호
                			pstm.setString(3,sms_body);
                			pstm.setString(4,SMS_SENDER_NAME);//송신자 이름
                			pstm.setString(5,SMS_SENDER_TEL);//송신자 번호

                	        rs = pstm.executeUpdate();                   
                            if(rs>0){
                                CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
                            	con.commit();
                            }
                    	}catch(Exception e1){
                            CommUtil.logWriter(sb.toString(),4);
                            e1.printStackTrace();
                    	}
                    	
                        }
                    }
        	}	
    }catch(Exception e1){
        e1.printStackTrace();
    }finally{
       try{
	   if(pstm != null){
	       pstm.close();
	   }
//		변경전 시작 20160420---------------------------------------------                            
//	   if (con != null){
//	       con.close();
//	   }
//	 변경전 끝 20160420---------------------------------------------	
       }catch (SQLException e){
               e.printStackTrace();
           }
    }
    	return remsg;
    }
     
    
    /*
     * 매입세금계산서 2ndSign 요청 알림  email 보내기
     * executeupdate를 executebatch로 변경
     * 변경 사유 : executeupdate시 ORA-01000 커서 초과 에러가 발생하므로
     * execute처리를 한꺼번에 한번만 처리하는 executebatch로 변경
     * */
/*    public String SecondSign_email(Vector vc)throws SQLException, Exception{
	String remsg = null;
	Connection con =  null;
	PreparedStatement pstm=null;
    	
	//String siteUrl = "http://cat.kepco.net/kepcobill2";
	String siteUrl ="";
	String contents ="";
	int[] rs;
 	int k = 0;
	try{
	/----------------------------------------------------------------------------------/
	String DRIVER = CommProperties.getString("EMAIL_DB_DRIVER");
	String URL    = CommProperties.getString("EMAIL_DB_URL");
	String USER   = CommProperties.getString("EMAIL_DB_USER");
	String PASS   = CommCipher.StringDecipher(CommProperties.getString("EMAIL_DB_PASS"));
	Class.forName(DRIVER);
	con = DriverManager.getConnection(URL, USER, PASS);
	/----------------------------------------------------------------------------------/
	//System.out.println("SecondSign_email()==========..");		
	String in_email_sending   = CommProperties.getString("IN_2NDSIGN_EMAIL_SENDING"); // email 전송 여부(1,0)	
	System.out.println("IN_2NDSIGN_EMAIL_SENDING : "+in_email_sending);	
	
	String serverIP = InetAddress.getLocalHost().getHostAddress();
	if("168.78.201.224".equals(serverIP)){
		siteUrl = "http://168.78.201.224/kepcobill2";
	}else{
		siteUrl = "http://cat.kepco.net/kepcobill2";
	}	
	
	if("1".equals(in_email_sending)){//out_email_sending   1(전송) 이면
        System.out.println("매입세금계산서 2ndSign 요청 알림  메일 전송____________________________________________________________");        	    
        con.setAutoCommit(false);
	    StringBuffer sb= new StringBuffer()
		        .append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
	            .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
	            .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
		        .append("VALUES (                                                                                          \n")
		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '한전 전자세금계산서', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
		        .append("0, 0, '전자세금계산서', '심윤보', 0, 1, 1, 0, ?)                                                 \n");
        pstm = con.prepareStatement(sb.toString());

	    	for(int i = 0; i<vc.size();i++){
	    	    
	    	    TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
	    	    
	    	contents =
			"<html>\n"+
		    	"<head>\n"+
		    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
		    	"</head>\n"+
		    	"<body>\n"+
		    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"	<tr>\n"+
		    	"		<td align='CENTER' valign='TOP'><br>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
		    	"				</tr>\n"+
		    	"			</table><br>\n"+
		    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'>\n"+
		    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
		    	"							<tr>\n"+
		    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 대금청구서응답서 정보입니다.</font></strong></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
		    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_name()+"</font></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
		    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_id()+"</font></td>\n"+
// 		    	"							</tr><tr>\n"+
//		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
//		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_name1()+"</font></td>\n"+
//		    	"							</tr><tr>\n"+
//		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
//		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_phone1()+"</font></td>\n"+
		    	"							</tr>\n"+
		    	"						</table>\n"+
		    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	//"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&mail=Y"+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
		    	//http://168.78.201.224/kepcobill2/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid=1003017983&doc_date=20120508&sup_biz_id=2048200888&buy_biz_id=1208200052   
		    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid="+tbinfo.getSvc_manage_id()+"&doc_date="+tbinfo.getIssue_day()+"&sup_biz_id="+tbinfo.getInvoicer_party_id()+"&buy_biz_id="+tbinfo.getInvoicee_party_id()+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
		    	"						</table><br>\n"+
		    	"					</td>\n"+
		    	"				</tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td valign='BOTTOM'>\n"+
		    	"						<font size=2> - SRM 시스템 : <font color=blue>http://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
		    	"				</td></tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
		    	"			</table>\n"+
		    	"		</td>\n"+
		    	"	</tr>\n"+
		    	"</table>\n"+
		    	"</body>\n"+
		    	"</html>\n";
		    Reader data = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));
	    	    if(tbinfo.getIo_code().equals("2") && !tbinfo.getInvoicer_contact_email().equals(" ")){//IO_CODE가 2인 매입만 처리
	          		if("168.78.201.224".equals(serverIP)){
	          			if(tbinfo.getInvoicer_contact_email().equals("maskman@kdn.com") || tbinfo.getInvoicer_contact_email().equals("d12600@kdn.com"))//안남준과장, 박현철
	              			pstm.setString(1,"SSV:"+tbinfo.getInvoicer_contact_email());
	          			else
	          				pstm.setString(1,"SSV:"+"latent@kdn.com");
	          		}else{
	          			pstm.setString(1,"SSV:"+tbinfo.getInvoicer_contact_email());
	          		}
	          		pstm.setString(2,  "\"한전세금계산서[송신전용]\"<deliver@cat.kepco.net>");
	          		pstm.setString(3,"\""+tbinfo.getInvoicer_contact_name()+"\"<"+tbinfo.getInvoicer_contact_email()+">");
	          		pstm.setString(4, " ");
            		pstm.setCharacterStream(5, data, contents.getBytes().length);
                	k++;
                	pstm.addBatch();
                	//pstm.clearParameters();
        	        //100개씩 excutebatch처리 
        	        if(k%100==0){
        	           rs = pstm.executeBatch();	
        	           pstm.clearBatch();
        	           con.commit();
        	           //rs[0], rs[1]... 개별값이 (0~n:성공 row수, -2:성공 했으나 row수를 알수 없음 -3:실패)
                       CommUtil.logWriter("100개씩 pstm.executeBatch 갯수:"+rs.length,4);
        	        }
	    	   }
	    	}
	        //나머지 excutebatch처리 
            rs = pstm.executeBatch();
	            pstm.clearBatch();
            con.commit();
	           //rs[0], rs[1]... 개별값이 (0~n:성공 row수, -2:성공 했으나 row수를 알수 없음 -3:실패)
            CommUtil.logWriter("나머지 pstm.executeBatch 갯수:"+rs.length,4);
	}	
	
    }catch(Exception e1){
    	con.rollback();
	    e1.printStackTrace();
    }finally{
	       try{
			   if(pstm != null){
			       pstm.close();
			   }
			   if (con != null){
				   con.setAutoCommit(true);
			       con.close();
			   }
	           }catch (SQLException e){
	               e.printStackTrace();
	           }
	    }
    	return remsg;
    }      
*/
    /*
     * 매입세금계산서 2ndSign 요청 알림  email 보내기
     * */
     
    
    public String SecondSign_email(Vector vc, Connection con)throws SQLException, Exception{
	String remsg = null;
	//Connection con =  null;
	PreparedStatement pstm=null;
    	
	//String siteUrl = "http://cat.kepco.net/kepcobill2";
	String siteUrl ="";
	String contents ="";
	int rs = 0;
	String enc_uuid;
	TaxInvSecurityMgr SecuMgr = new TaxInvSecurityMgr();
	
	try{
//		변경전 시작 20160420---------------------------------------------                            
	//---------------SMS서버 커넥션 맺기------------------------------------/
//	String DRIVER = CommProperties.getString("EMAIL_DB_DRIVER");
//	String URL    = CommProperties.getString("EMAIL_DB_URL");
//	String USER   = CommProperties.getString("EMAIL_DB_USER");
//	String PASS   = CommCipher.StringDecipher(CommProperties.getString("EMAIL_DB_PASS"));
//	Class.forName(DRIVER);
//	con = DriverManager.getConnection(URL, USER, PASS);
	//-----------------------------------------------------------------------/
//	변경전 끝 20160420---------------------------------------------                            
	System.out.println("SecondSign_email()==========..");		
	String in_email_sending   = CommProperties.getString("IN_2NDSIGN_EMAIL_SENDING"); // email 전송 여부(1,0)	
	System.out.println("IN_2NDSIGN_EMAIL_SENDING : "+in_email_sending);	
	
	String serverIP = InetAddress.getLocalHost().getHostAddress();
	if("168.78.201.224".equals(serverIP)){
		siteUrl = "https://168.78.201.224/kepcobill2";
	}else{
		siteUrl = "https://cat.kepco.net/kepcobill2";
	}	
	
	if("1".equals(in_email_sending)){//out_email_sending   1(전송) 이면
		System.out.println("in_email_sending()==========..");	    
	    	for(int i = 0; i<vc.size();i++){
	    	    
	    	    TbTaxBillInfoVo tbinfo = (TbTaxBillInfoVo)vc.get(i);
	    	    enc_uuid = SecuMgr.TaxInvEncrypt(tbinfo.getSvc_manage_id());
	    	    enc_uuid = SecuMgr.encodeURIComponent(enc_uuid);//POST 넘기는 경우 몇몇 문자열 깨짐 방지를 위한 encode처리 
	    	    
	    	/*contents =
			"<html>\n"+
		    	"<head>\n"+
		    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
		    	"</head>\n"+
		    	"<body>\n"+
		    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"	<tr>\n"+
		    	"		<td align='CENTER' valign='TOP'><br>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
		    	"				</tr>\n"+
		    	"			</table><br>\n"+
		    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td align='LEFT' valign='TOP'>\n"+
		    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
		    	"							<tr>\n"+
		    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 대금청구서응답서 정보입니다.</font></strong></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
		    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_name()+"</font></td>\n"+
		    	"							</tr><tr>\n"+
		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
		    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_id()+"</font></td>\n"+
// 		    	"							</tr><tr>\n"+
//		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
//		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_name1()+"</font></td>\n"+
//		    	"							</tr><tr>\n"+
//		    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
//		    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_phone1()+"</font></td>\n"+
		    	"							</tr>\n"+
		    	"						</table>\n"+
		    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	//"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&mail=Y"+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
		    	//http://168.78.201.224/kepcobill2/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid=1003017983&doc_date=20120508&sup_biz_id=2048200888&buy_biz_id=1208200052   
		    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid="+tbinfo.getSvc_manage_id()+"&doc_date="+tbinfo.getIssue_day()+"&sup_biz_id="+tbinfo.getInvoicer_party_id()+"&buy_biz_id="+tbinfo.getInvoicee_party_id()+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
		    	"						</table><br>\n"+
		    	"					</td>\n"+
		    	"				</tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
		    	"				</tr><tr>\n"+
		    	"					<td valign='BOTTOM'>\n"+
		    	"						<font size=2> - SRM 시스템 : <font color=blue>http://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산]</font>시스템 클릭</font>\n"+
		    	"				</td></tr>\n"+
		    	"			</table>\n"+
		    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
		    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
		    	"			</table>\n"+
		    	"		</td>\n"+
		    	"	</tr>\n"+
		    	"</table>\n"+
		    	"</body>\n"+
		    	"</html>\n";*/
	    	contents =
				"<html>\n"+
			    	"<head>\n"+
			    	"<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>\n"+
			    	"</head>\n"+
			    	"<body>\n"+
			    	"<table width='720' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"	<tr>\n"+
			    	"		<td align='CENTER' valign='TOP'><br>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'><img src='"+siteUrl+"/images/tax_img.gif' width='685' height='83' align='ABSMIDDLE'></td>\n"+
			    	"				</tr>\n"+
			    	"			</table><br>\n"+
			    	"			</table>\n"+
			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;● 귀하께서 청구하신 대금결재가 완료되었습니다.\n" +
			    	"                                  <br>&nbsp;&nbsp;● 세금계산서를 발급하여 주시기 바랍니다. " +
			    	"                                  <br>&nbsp;&nbsp;● 공급일이 속하는 달의 다음달 10일까지 세금계산서를 발급(매출자 전자서명)하지 않아\n" +
			    	"                                  <br>&nbsp;&nbsp;&nbsp;&nbsp;발생되는 가산세 등 제반 문제점은 전적으로 매출자에게 있음을 알려드립니다.\n" +
			    	"               </td></tr>\n"+
			    	"			<table width='600' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td align='LEFT' valign='TOP'>\n"+
			    	"						<table width='100%' border='0' cellpadding='1' cellspacing='1' bgcolor='#DCD9D9'>\n"+
			    	"							<tr>\n"+
			    	"								<td height='21' colspan='2' align='CENTER' bgcolor='#C8E0E9'><strong> <font size=2>수신한 대금청구서응답서 정보입니다.</font></strong></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td width='35%' height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사명 </font></td>\n"+
			    	"								<td width='65%' >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_name()+"</font></td>\n"+
			    	"							</tr><tr>\n"+
			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 사업자번호</font></td>\n"+
			    	"								<td  >&nbsp;<font size=2>"+tbinfo.getInvoicee_party_id()+"</font></td>\n"+
//	 		    	"							</tr><tr>\n"+
//			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자이름</font></td>\n"+
//			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_name1()+"</font></td>\n"+
//			    	"							</tr><tr>\n"+
//			    	"								<td height='21' bgcolor='#E9E9E9'>&nbsp;&nbsp;&nbsp;<img src='"+siteUrl+"/images/bullet08.gif' width='14' height='3' align='ABSMIDDLE'><font size=2>송신회사 담당자 전화번호</font></td>\n"+
//			    	"								<td >&nbsp;<font size=2>"+tbinfo.getInvoicee_contact_phone1()+"</font></td>\n"+
			    	"							</tr>\n"+
			    	"						</table>\n"+
			    	"						<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	//"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/etax/etax_view.jsp?taxid="+tbinfo.getIssue_id2()+"&mail=Y"+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
			    	//http://168.78.201.224/kepcobill2/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid=1003017983&doc_date=20120508&sup_biz_id=2048200888&buy_biz_id=1208200052   
//			    	2016.10.20 링크주소에 AES암호화를 위한 AES암호화 값 추가 
			    	"							<tr><td height='40' align='CENTER' valign='BOTTOM'><a href='"+siteUrl+"/kepcobill3/buyw/TaxInvRemote2ndSign.jsp?uuid="+tbinfo.getSvc_manage_id()+"&doc_date="+tbinfo.getIssue_day()+"&sup_biz_id="+tbinfo.getInvoicer_party_id()+"&buy_biz_id="+tbinfo.getInvoicee_party_id()+"&enc_uuid="+enc_uuid+"' target='_blank'><img src='"+siteUrl+"/images/b_taxsee.gif' width='145' height='20' align='ABSMIDDLE' border=0></a></td></tr>\n"+
			    	"						</table><br>\n"+
			    	"					</td>\n"+
			    	"				</tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='100%' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>&nbsp;&nbsp;&nbsp;※ [세금계산서 상세정보 보기] 버튼이 작동하지 않을 경우 팝업 차단을 하용으로 변경하시기 바랍니다.<br>&nbsp;&nbsp;&nbsp;<font color=blue>(설정 방법 : 브라우저메뉴 중 도구 > 팝업차단> 팝업차단 사용 안함 선택)</font></font></td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td height='40' valign='BOTTOM'><font size=2>※ 세금계산서는 [전자세금계산서시스템]으로 들어가셔도 확인하실 수 있습니다.</td>\n"+
			    	"				</tr><tr>\n"+
			    	"					<td valign='BOTTOM'>\n"+
			    	"						<font size=2> - 한전전자조달 : <font color=blue>https://srm.kepco.net</font> 로 로그인하여 하단의 <font color=blue>[전자세금계산서]</font>시스템 클릭</font>\n"+
			    	"				</td></tr>\n"+
			    	"			</table>\n"+
			    	"			<table width='685' border='0' cellspacing='0' cellpadding='0'>\n"+
			    	"				<tr><td align='RIGHT' valign='BOTTOM'><img src='"+siteUrl+"/images/copyright.gif' width='416' height='45' align='ABSMIDDLE'></td></tr>\n"+
			    	"			</table>\n"+
			    	"		</td>\n"+
			    	"	</tr>\n"+
			    	"</table>\n"+
			    	"</body>\n"+
			    	"</html>\n";
			    Reader data = new InputStreamReader(new ByteArrayInputStream(contents.getBytes()));
	    	    if(tbinfo.getIo_code().equals("2") && !tbinfo.getInvoicer_contact_email().equals(" ")){//IO_CODE가 2인 매입만 처리
	     	 	   StringBuffer sb= new StringBuffer()
	     	 	   
	     	 	//변경전 시작 20160420---------------------------------------------            						  	

// 	     	 	    .append("INSERT INTO WEBADM.IM_DMAIL_INFO_13(                                                              \n")
//        	        .append("SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
//	    	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
//	   		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT)                           \n")
//	   		        .append("VALUES (                                                                                          \n")
//	   		        .append("WEBADM.IM_DMAIL_SEQ_13.NEXTVAL, '[중요] 한전 전자세금계산서 발행요청', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
//	   		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
//	   		        .append("0, 0, '전자세금계산서', '심윤보', 0, 1, 1, 0, ?)                                                 \n");
	     	 	   
	     	 	//변경전 끝 20160420---------------------------------------------
	     	 	//메일 SMS 저장위치, 필드추가  따른 변경    20160420
	     	 	   
	 	            .append("INSERT INTO IF_IM_DMAIL_INFO(                                                              \n")
	    	        .append("ID, SEQIDX, SUBJECT, SQL, REJECT_SLIST_IDX, BLOCK_GROUP_IDX, MAILFROM,MAILTO, REPLYTO,                \n")
	    	        .append("ERRORSTO, HTML, ENCODING, CHARSET, SDATE, TDATE, DURATION_SET, CLICK_SET,                         \n")
	   		        .append("SITE_SET, ATC_SET, GUBUN, RNAME, MTYPE, U_IDX, G_IDX, MSGFLAG, CONTENT,                           \n")
	       		    .append("TABLE_GB, IF_STATUS , CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT)                                 \n")
	   		        .append("VALUES (                                                                                          \n")
	   		        .append("'M'||IF_IM_DMAIL_INFO_ID.NEXTVAL,'', '[중요] 한전 전자세금계산서 발행요청', ?, 0, 0, ?, ?, ?, '1', 1, 0, 'euc-kr',         \n")
	   		        .append("TO_CHAR(SYSDATE - 60/24/60, 'YYYYMMDDHH24MISS'), TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), 0, 0,      \n")
	   		        .append("0, 0, '전자세금계산서', '관리자', 0, 1, 1, 0, ?,                                                     \n")
            	    .append("'13','','SmsDao(SecondSign_email)',SYSDATE,'','')                                                \n");
	     	 	   
	     	 	   
	     	 	   
    		      try{
                                
                  		pstm = con.prepareStatement(sb.toString());
                  		if("168.78.201.224".equals(serverIP)){
                  			if(tbinfo.getInvoicer_contact_email().equals("maskman@kdn.com") || tbinfo.getInvoicer_contact_email().equals("d12600@kdn.com"))//안남준과장, 박현철
                      			pstm.setString(1,"SSV:"+tbinfo.getInvoicer_contact_email());
                  			else
                  				pstm.setString(1,"SSV:"+"person1697@hanmail.net");
                  		}else{
                  			pstm.setString(1,"SSV:"+tbinfo.getInvoicer_contact_email());
                  		}
                  		pstm.setString(2,  "\"한전세금계산서[송신전용]\"<deliver@cat.kepco.net>");
                  		pstm.setString(3,"\""+tbinfo.getInvoicer_contact_name()+"\"<"+tbinfo.getInvoicer_contact_email()+">");
                  		pstm.setString(4, "1");
                    		pstm.setCharacterStream(5, data, contents.getBytes().length);
                		
        			rs = pstm.executeUpdate();
                    if(rs>0){
                            CommUtil.logWriter("pstm.executeUpdate 갯수:"+rs,4);
        			    con.commit();
        			}
      		      }catch(Exception e1){
                   CommUtil.logWriter(sb.toString(),4);
        		    e1.printStackTrace();
			     }
	    	  }
	    	}
	}	
	
    }catch(Exception e1){
	e1.printStackTrace();
	
    }finally{
	       try{
		   if(pstm != null){
		       pstm.close();
		   }
//			변경전 시작 20160420---------------------------------------------                            
//		   if (con != null){
//		       con.close();
//		   }
//		 변경전 끝 20160420---------------------------------------------	
	           }catch (SQLException e){
	               e.printStackTrace();
	           }
	    }
    	return remsg;
    }      

}


		
																																									