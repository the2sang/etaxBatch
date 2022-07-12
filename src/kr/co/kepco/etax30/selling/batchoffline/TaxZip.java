package kr.co.kepco.etax30.selling.batchoffline;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.util.Dbcon;
import signgate.xml.util.XmlUtil;

/******************************************************************************
 * 저작권               : Copyright⒞ 2009 by Kepco Corp. All Rights Reserved

 * 프로젝트 명          : KEPCO ETAX 프로젝트
 * 프로그램 명          : TaxZip
 * 프로그램 아이디      : TaxZip.java
 * 프로그램 개요        : 파일 생성, 파일 삭제                                                  
 * 관련 테이블          : 
 * 관련 모듈            : 
 * 작성자               : 양형공
 * 작성일자             : 2009-12-31

 * 개정이력(성명 | 일자 | 내용) : 양형공 | 2009-12-31 | (DEV TEAM), v1.0,    최초작성
 *
 * <METHOD>
 * - makeFiles()
 * - getFileList()
 * - deleteFile()
 * - deleteDir()
 * </METHOD>
******************************************************************************/

public class TaxZip {
	
	/**
	 * 파일생성
	 * @param targetMonth : 대상년월
	 * @param targetPath : 파일 저장 PATH
	 * @param keys : 처리할 대상키 리스트
	 * @return
	 */
	public boolean makeFiles(String targetMonth, String targetPath, String[] keys){
		
		Connection con = null;
		boolean result = true;
		TaxXmlService txs = null;
		try {
			con = new Dbcon().getConnection();
			String[] contents = null;
			File targetFolder = null;
			txs = new TaxXmlService();
		
			targetFolder = new File(targetPath+"/"+targetMonth);
			if(!targetFolder.exists()){
				if (!targetFolder.mkdirs()){
					System.out.println("폴더 생성 실패");
					System.exit(-1);
				}
			}
			for (int index = 0, maxIndex = keys.length; index < maxIndex; index++){
				if((index % 5000) == 0 ){
					System.out.println("file 생성: "+index +": "+ CommUtil.getKST("yyyy-MM-dd HH:mm:ss.SSS"));
				}
				contents = txs.getXML(con,keys[index]);

				XmlUtil.genFileCreate(targetFolder.getPath()+"/"+contents[1]+".xml", contents[0], "UTF-8");
			}
			System.out.println("file 생성완료");
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			
		}finally{
			if (con != null) try{Dbcon.close(con); }catch(Exception e){System.out.println(e);}
		}
		
		return result;
	}
	
	/**
	 * 삭제할 대상 폴더 리스트를 List에 추가
	 * @param targetPath : 파일 저장 경로
	 * @param arr : 삭제 대상 폴더 리스트
	 * @param targetMonth : 대상년월
	 */
	public void getFileList(File targetPath, List arr, String targetMonth){
		
		if(targetPath.isDirectory()){
			String[] fl = targetPath.list();
			File tmpFile = null;
			//System.out.println("fl.length"+fl.length);
			for(int i=0; i<fl.length; i++){
				tmpFile = new File(targetPath.getAbsoluteFile()+"/"+fl[i]);
				//System.out.println("fl[i]"+fl[i]);
				
				if(tmpFile.isDirectory()){
					if(!fl[i].equals(targetMonth)){
						arr.add(fl[i]);
						getFileList(tmpFile, arr, targetMonth);
					}
				}
			}
			
		}
		
	}
	
	/**
	 * 파일삭제
	 * @param file 
	 */
	private void deleteFile(File file) {
		try {
			file.delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 파일삭제
	 * @param path : 경로
	 */

	public void deleteDir(String path) {
		try {
			File file = new File(path);
			if (!file.isDirectory()) {
				return;
			}
			File[] files = file.listFiles();

			for (int idx = 0; idx < files.length; idx++) {
				if(files[idx].isDirectory()) {
					deleteDir(files[idx].toString());
				}else {
					deleteFile(files[idx]);
				}
			}

			file.delete();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
