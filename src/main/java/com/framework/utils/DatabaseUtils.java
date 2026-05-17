package com.framework.utils;

import com.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC database utility for test data setup/validation.
 * Connection is opened per operation and closed immediately (no pooling needed for tests).
 */
public final class DatabaseUtils {

    private static final Logger log = LogManager.getLogger(DatabaseUtils.class);

    private DatabaseUtils() {}

    private static Connection getConnection() throws SQLException {
        ConfigReader cfg = ConfigReader.getInstance();
        try {
            Class.forName(cfg.getDbDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("DB driver not found: " + cfg.getDbDriver(), e);
        }
        Connection conn = DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUsername(), cfg.getDbPassword());
        log.info("DB connection established: {}", cfg.getDbUrl());
        return conn;
    }

    // ─── Query ───────────────────────────────────────────────────────────────

    public static List<Map<String, Object>> executeQuery(String sql) {
        List<Map<String, Object>> results = new ArrayList<>();
        log.info("Executing query: {}", sql);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            log.error("Query execution failed: {}", e.getMessage(), e);
            throw new RuntimeException("DB query failed", e);
        }

        log.info("Query returned {} rows", results.size());
        return results;
    }

    public static String getCellValue(String sql, String columnName) {
        List<Map<String, Object>> rows = executeQuery(sql);
        if (rows.isEmpty()) return "";
        Object val = rows.get(0).get(columnName);
        return val == null ? "" : val.toString();
    }

    // ─── DML ─────────────────────────────────────────────────────────────────

    public static int executeUpdate(String sql) {
        log.info("Executing update: {}", sql);
        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            log.info("Rows affected: {}", rows);
            return rows;
        } catch (SQLException e) {
            log.error("Update execution failed: {}", e.getMessage(), e);
            throw new RuntimeException("DB update failed", e);
        }
    }

    public static int executePreparedUpdate(String sql, Object... params) {
        log.info("Executing prepared statement: {}", sql);
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            int rows = ps.executeUpdate();
            log.info("Rows affected: {}", rows);
            return rows;
        } catch (SQLException e) {
            log.error("Prepared statement failed: {}", e.getMessage(), e);
            throw new RuntimeException("DB prepared statement failed", e);
        }
    }

    public static boolean recordExists(String sql) {
        return !executeQuery(sql).isEmpty();
    }
}
