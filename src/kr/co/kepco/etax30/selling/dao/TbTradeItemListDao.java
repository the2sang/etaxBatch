package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.kepco.etax30.selling.vo.ItemListVo;
//import kr.co.kepco.etax30.selling.vo.TbTaxBillInfoVo;
import kr.co.kepco.etax30.selling.util.CommUtil;

/**
 * 
 * @author Yang,Hyungkong
 *
 */
public class TbTradeItemListDao {
	
	public List select(Connection conn, String rowId) throws SQLException{
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer query = new StringBuffer()
		//2011. 9. 6 강성역과장 요청으로 변경
		//.append("SELECT /*+ INDEX(A TB_TRADE_ITEM_LIST_PK) */                                                           \n")
		.append("SELECT /*+ INDEX(A TB_TRADE_ITEM_LIST_PK) no_index(b) */                                               \n")
		.append("A.BIZ_MANAGE_ID, A.SEQ_NO, A.PURCHASE_DAY, A.ITEM_NAME, A.ITEM_INFO, A.ITEM_DESC, A.UNIT_QUANTITY,     \n")
		.append("A.UNIT_AMOUNT, A.INVOICE_AMOUNT, A.TAX_AMOUNT                                                          \n")
		.append("FROM TB_TRADE_ITEM_LIST A,    TB_TAX_BILL_INFO B                                                       \n")										 			                   
		.append("WHERE B.ROWID = CHARTOROWID(?)                                                                         \n")
		.append("AND A.BIZ_MANAGE_ID = B.BIZ_MANAGE_ID                                                                  \n")
		.append("AND A.IO_CODE = B.IO_CODE                                                                              \n")
		.append("AND A.ISSUE_DAY = B.ISSUE_DAY		              													    \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		
		List tbTradeItemList = null;
		try{
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, rowId);

			rs = pstmt.executeQuery();
			
			tbTradeItemList = new ArrayList();
			
			while(rs.next()){
				tbTradeItemList.add(makeTbTradeItem(rs));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}finally{
			if (rs != null) try{rs.close();}catch(SQLException e){System.out.println(e);}  
			if (pstmt != null) try{pstmt.close();}catch(SQLException e){System.out.println(e);} 
		}
		return tbTradeItemList;
	}
	
	protected ItemListVo makeTbTradeItem(ResultSet rs) throws SQLException{
		
		ItemListVo tbTradeItem = new ItemListVo();
		tbTradeItem.setBiz_manage_id(rs.getString("BIZ_MANAGE_ID"));
		tbTradeItem.setSeq_no(rs.getInt("SEQ_NO"));
		tbTradeItem.setPurchase_day(rs.getString("PURCHASE_DAY"));
		tbTradeItem.setItem_name(rs.getString("ITEM_NAME"));
		tbTradeItem.setItem_info(rs.getString("ITEM_INFO"));
		tbTradeItem.setItem_desc(rs.getString("ITEM_DESC"));
		tbTradeItem.setUnit_quantity(rs.getDouble("UNIT_QUANTITY"));
		tbTradeItem.setUnit_amount(rs.getDouble("UNIT_AMOUNT"));
		tbTradeItem.setInvoice_amount(rs.getLong("INVOICE_AMOUNT"));
		tbTradeItem.setTax_amount(rs.getLong("TAX_AMOUNT"));
		
		return tbTradeItem;
	}
	
	public ItemListVo makeTbTradeItemPb(ResultSet rs) throws SQLException{
		ItemListVo tbTradeItemPb = new ItemListVo();
		tbTradeItemPb = makeTbTradeItem(rs);
		return tbTradeItemPb;
	}

	/**
	 * 매입 상품 목록 
	 * @param conn
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public int insert(Connection conn, ItemListVo item) throws  Exception { 
		PreparedStatement pstmt = null;
		int result = 0;
		int i = 1;
		
		StringBuffer query = new StringBuffer()
		.append(" INSERT INTO TB_TRADE_ITEM_LIST                                                                            \n")                                         
		.append("     (IO_CODE, ISSUE_DAY, BIZ_MANAGE_ID, SEQ_NO, PURCHASE_DAY, ITEM_NAME, ITEM_INFO,                       \n")
		.append("     ITEM_DESC, UNIT_QUANTITY,UNIT_AMOUNT, INVOICE_AMOUNT, TAX_AMOUNT                                     \n")
		.append(" )                                                                                                         \n")
		.append(" VALUES (                                                                                                  \n")
		.append(" '2', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?																		\n")
		.append(" )                                                                                                         \n");
		
		CommUtil.logWriter(String.valueOf(query),1);
		try{  
			pstmt = conn.prepareStatement(query.toString());
			
			/*System.out.println(query.toString());
			System.out.println("==================================================");
			System.out.println("item.getIssue_day()     "+item.getIssue_day()     );
			System.out.println("item.getSeq_no()        "+item.getSeq_no()        );
			System.out.println("item.getBiz_manage_id() "+item.getBiz_manage_id() );
			System.out.println("item.getPurchase_day()  "+item.getPurchase_day()  );
			System.out.println("item.getItem_name()     "+item.getItem_name()     );
			System.out.println("item.getItem_info()     "+item.getItem_info()     );
			System.out.println("item.getItem_desc()     "+item.getItem_desc()     );
			System.out.println("item.getUnit_quantity() "+item.getUnit_quantity() );
			System.out.println("item.getUnit_amount()   "+item.getUnit_amount()   );
			System.out.println("item.getInvoice_amount()"+item.getInvoice_amount());
			System.out.println("item.getTax_amount()    "+item.getTax_amount()    );
			System.out.println("==================================================");*/
			
			//pstmt.setString(i++, item.getIssue_day() );
			//pstmt.setString(i++, item.getIssue_day() );
			pstmt.setString(i++, item.getIssue_day()      )    ;
			pstmt.setString(i++, item.getBiz_manage_id() );
			pstmt.setLong(i++,   item.getSeq_no()         )       ;
			pstmt.setString(i++, item.getPurchase_day()  ) ;
			pstmt.setString(i++, item.getItem_name()     )    ;
			pstmt.setString(i++, item.getItem_info()     )    ;
			pstmt.setString(i++, item.getItem_desc()     )    ;
			pstmt.setDouble(i++, item.getUnit_quantity() );
			pstmt.setDouble(i++, item.getUnit_amount()   )  ;
			pstmt.setLong(i++,   item.getInvoice_amount() );
			pstmt.setLong(i++,   item.getTax_amount()    );
			
			
			result = pstmt.executeUpdate(); 
			  
		}catch(Exception e) {
			conn.rollback(); 
			System.out.println(e.getMessage());
			throw e;
		}finally{
			if ( pstmt != null ) try{pstmt.close();}catch(Exception e){System.out.println(getClass().getName()+":"+e.toString());}
		}
		
		return result;
	
	}
}
