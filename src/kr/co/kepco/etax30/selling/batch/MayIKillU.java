/******************************************************************************
 * 저작권            : Copyright⒞ 2009 by KEPCO Corp. All Rights Reserved

 * 프로젝트 명       : 한전 매출-세금계산서 시스템 개발 프로젝트
 * 프로그램 명       : MayIKillU
 * 프로그램 아이디   : MayIKillU
 * 프로그램 개요     : 배치 프로그램 cycle 상태 체크                     
 * 관련 테이블       : 
 * 관련 모듈         : 
 * 작성자            : 박상종
 * 작성일자          : 2011.10.05

 * 개정이력(성명 | 일자 | 내용) : 박상종 | 2011-10-05 | (DEV TEAM), v1.0, 최초작성

 ******************************************************************************/

package kr.co.kepco.etax30.selling.batch;
    
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

//import kr.co.kepco.etax30.selling.batchoffline.BatchOffline;
//import kr.co.kepco.etax30.selling.batchoffline.BatchProcess;
//import kr.co.kepco.etax30.selling.batchoffline.TaxXmlService;
//import kr.co.kepco.etax30.selling.util.CommCipher;
//import kr.co.kepco.etax30.selling.util.CommProperties;
//import kr.co.kepco.etax30.selling.util.CommUtil;
//import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;
   
/**   
 * @author conan
 * 배치 처리 cycle 처리중인지 완료된 상태인지 체크하여
 * 배치 cycle이 완료되어야 배치 프로그램으로 종료 시킬수 있도록 함.
 */
public class MayIKillU {  
	
    public static void main(String[] args) {
		try {
			//CommUtil.logWriter("Batch cycle 종료 유무 확인",4);
			//CommUtil.logWriter("BatchOffline.OnOffbatch:"+BatchOffline.OnOffbatch,4);
			if(RunningScheduler.amIAlive.equals("YES")){
		   	   System.exit(10);		
			}else{
		  	   System.exit(20);		
			}
		} catch (Exception e1) {
			System.out.println("Batch cycle 종료 유무 확인 에러");
			e1.printStackTrace();
			System.out.println(e1.toString());
			System.exit(-1);
		}
     }
}
