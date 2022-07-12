package kr.co.kepco.etax30.selling.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import kr.co.kepco.etax30.selling.util.CommProperties;

/**
 * @author JeongMyoungSoo
 * @since  2009.10.06
 * 
 */
public class CommTaxXmlSpec {
	
	private volatile static CommTaxXmlSpec m_instance;
	private static String etax_xml_30 = "";

	
	public CommTaxXmlSpec() {
		try {
			String template_filename = CommProperties.getString("ETAX_XML3.0_TEMPLATE");

			
			File f = new File(template_filename);
			
			FileInputStream is = new FileInputStream(f);
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			int readcnt;
			StringBuffer sBuffer = new StringBuffer();
			
			char[] buf = new char[2048];
			
			while((readcnt = in.read(buf, 0, 2048)) > 0 ) {
				//System.out.println(buf);
				sBuffer.append(buf, 0, readcnt);
			}
			
			is.close();
			in.close();
			
			if(!sBuffer.toString().substring(0,1).equals("<")){
				etax_xml_30 = sBuffer.toString().substring(1);
			}else{
				etax_xml_30 = sBuffer.toString();
			}
			
			//etax_xml_30 = sBuffer.toString();
			//.substring(1);

			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @param void
	 */
	public	String GetTaxXML()
	{
		return etax_xml_30;
	}
	
	/**
	 * @param void
	 */
	public static synchronized CommTaxXmlSpec getInstance()
	{
		if(m_instance == null)
		{
			synchronized(CommTaxXmlSpec.class)
			{
				if(m_instance == null)
				{
					m_instance = new CommTaxXmlSpec();
				}
			}
		}
	
		return m_instance;
	}
	
	/**
	 * @param void
	 */
/*	private void LoadReturnXMLSpec() 
	{
		try {
			String template_filename = CommProperties.getString("ETAX_XML3.0_TEMPLATE");

			
			File f = new File(template_filename);
			
			FileInputStream is = new FileInputStream(f);
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			int readcnt;
			StringBuffer sBuffer = new StringBuffer();
			
			char[] buf = new char[2048];
			
			while((readcnt = in.read(buf, 0, 2048)) > 0 ) {
				//System.out.println(buf);
				sBuffer.append(buf, 0, readcnt);
			}
			
			is.close();
			in.close();
			
			etax_xml_30 = sBuffer.toString();
			//.substring(1);

			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return ;
	}*/
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//CommTaxXmlSpec.getInstance();
		System.out.print(CommTaxXmlSpec.getInstance().GetTaxXML());
	}

}
