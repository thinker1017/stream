package com.pukai.stream.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhoenixUtil {
	private static Logger logger = LoggerFactory.getLogger(PhoenixUtil.class);

	private static PhoenixUtil instance;
	
	private PhoenixUtil() { }
	
	public static PhoenixUtil getInstance() {
		if (null == instance) {
			syncInit();
		}
		return instance;
	}
	
	private static synchronized void syncInit() {
        if (instance == null) {
            instance = new PhoenixUtil();
        }
    }
	
	public Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(Constant.phoenixDriver);
            conn = DriverManager.getConnection(Constant.phoenixURL, Constant.phoenixUsername, Constant.phoenixPassword);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return conn;
    }
	
	public PreparedStatement getStatement(Connection conn, String sql) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return pstmt;
    }

	public void executeNonQueryBatch(List<String> sqlList) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = getConn();
			conn.setAutoCommit(false);
			
			if (ValidateUtil.isValid(sqlList)) {
				for (String sql : sqlList) {
					pstmt = conn.prepareStatement(sql);
					logger.debug(sql);
					pstmt.executeUpdate();
				}
				conn.commit();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			release(pstmt, conn);
		}
	}
	
	public void release(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
