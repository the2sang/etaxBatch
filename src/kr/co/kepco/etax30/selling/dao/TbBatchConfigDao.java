package kr.co.kepco.etax30.selling.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kepco.etax30.selling.util.CommUtil;
import kr.co.kepco.etax30.selling.vo.TbBatchConfigVo;

public class TbBatchConfigDao {

	public TbBatchConfigVo select(Connection conn) {

		StringBuffer query = new StringBuffer()
				.append(" SELECT  											\n")
				.append(" SVR0_EXEC_YN,										\n")
				.append(" SVR1_EXEC_YN,										\n")
				.append(" SVR2_EXEC_YN,										\n")
				.append(" SVR3_EXEC_YN,										\n")
				.append(" THREAD_CNT,										\n")
				.append(
						" TO_CHAR(NEXT_EXEC_DT, 'YYYYMMDDHH24MISS') NEXT_EXEC_DT,		\n")
				.append(
						" TO_CHAR(NEXT_EXEC_DT1, 'YYYYMMDDHH24MISS') NEXT_EXEC_DT1,		\n")
				.append(
						" TO_CHAR(NEXT_EXEC_DT2, 'YYYYMMDDHH24MISS') NEXT_EXEC_DT2,		\n")
				.append(
						" TO_CHAR(NEXT_EXEC_DT3, 'YYYYMMDDHH24MISS') NEXT_EXEC_DT3,		\n")
				.append(" XML_DEL_CODE,										\n")
				.append(" EXEC_CYCLE_MINUTE, 								\n")
				.append(" GRP_CNT,			            					\n")
				.append(" NOW_GRP_NO		            					\n")
				.append(" FROM   TB_BATCH_CONFIG							\n");

		CommUtil.logWriter(String.valueOf(query), 1);

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		TbBatchConfigVo config = new TbBatchConfigVo();
		try {
			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				config.setSvr0_exec_yn(rs.getString("SVR0_EXEC_YN"));
				config.setSvr1_exec_yn(rs.getString("SVR1_EXEC_YN"));
				config.setSvr2_exec_yn(rs.getString("SVR2_EXEC_YN"));
				config.setSvr3_exec_yn(rs.getString("SVR3_EXEC_YN"));
				config.setThread_cnt(rs.getInt("THREAD_CNT"));
				config.setNext_exec_dt(rs.getString("NEXT_EXEC_DT"));
				config.setNext_exec_dt1(rs.getString("NEXT_EXEC_DT1"));
				config.setNext_exec_dt2(rs.getString("NEXT_EXEC_DT2"));
				config.setNext_exec_dt3(rs.getString("NEXT_EXEC_DT3"));
				config.setXml_del_yn(rs.getString("XML_DEL_CODE"));
				config.setExec_cycle_minute(rs.getInt("EXEC_CYCLE_MINUTE"));
				config.setGrp_cnt(rs.getString("GRP_CNT"));
				config.setNow_grp_no(rs.getString("NOW_GRP_NO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
		}

		return config;

	}

	public int update(Connection conn, int server) throws Exception {

		PreparedStatement pstmt = null;
		int result = 0;
		StringBuffer query = null;

		switch (server) {
		case 0:
			query = new StringBuffer()
					.append(
							"	 UPDATE TB_BATCH_CONFIG            																	 \n")
					.append(
							"	 SET NEXT_EXEC_DT = TO_DATE(TO_CHAR(ADD_MONTHS(SYSDATE,1), 'YYYYMM') || '060010' , 'YYYYMMDDHH24MI') \n");
			break;
		case 1:
			query = new StringBuffer()
					.append(
							"	 UPDATE TB_BATCH_CONFIG            																	 \n")
					.append(
							"	 SET NEXT_EXEC_DT1 = TO_DATE(TO_CHAR(ADD_MONTHS(SYSDATE,1), 'YYYYMM') || '060010' , 'YYYYMMDDHH24MI') \n");
			break;
		case 2:
			query = new StringBuffer()
					.append(
							"	 UPDATE TB_BATCH_CONFIG            																	 \n")
					.append(
							"	 SET NEXT_EXEC_DT2 = TO_DATE(TO_CHAR(ADD_MONTHS(SYSDATE,1), 'YYYYMM') || '060010' , 'YYYYMMDDHH24MI') \n");
			break;
		case 3:
			query = new StringBuffer()
					.append(
							"	 UPDATE TB_BATCH_CONFIG            																	 \n")
					.append(
							"	 SET NEXT_EXEC_DT3 = TO_DATE(TO_CHAR(ADD_MONTHS(SYSDATE,1), 'YYYYMM') || '060010' , 'YYYYMMDDHH24MI') \n");
			break;

		default:
			break;
		}

		CommUtil.logWriter(String.valueOf(query), 1);

		try {
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());

			result = pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
			conn.setAutoCommit(true);
		}

		return result;

	}

	public int udpateGrpNo(Connection conn, String now_grp_no ) throws Exception {

		PreparedStatement pstmt = null;
		int result = 0;
		StringBuffer query = null;
		query = new StringBuffer().append("	 UPDATE TB_BATCH_CONFIG \n")
				.append("	 SET now_grp_no = ?     \n");

		CommUtil.logWriter(String.valueOf(query), 1);

		try {
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, now_grp_no);

			result = pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
					System.out.println(getClass().getName() + ":"
							+ e.toString());
				}
			conn.setAutoCommit(true);
		}

		return result;

	}

}
