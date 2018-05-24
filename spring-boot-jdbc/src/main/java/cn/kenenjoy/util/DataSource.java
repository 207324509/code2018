package cn.kenenjoy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hefa on 2018/5/22.
 */
@ConfigurationProperties(prefix = "hive")
@Repository
public class DataSource {
    private static final Logger log = LoggerFactory.getLogger(DataSource.class);

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            log.error("Class.forName(\"org.apache.hadoop.hive.jdbc.HiveDriver\")", e);
        }
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接", e);
            }
        }
    }

    public List query(String sql, Object[] args) throws SQLException {
        log.debug("SQL:" + sql);
        Connection connection = null;
        List list = new ArrayList();
        try {
            connection = this.getConnection();

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setQueryTimeout(60);
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);
            }

            ResultSet resultSet = prepareStatement.executeQuery();
            log.info(resultSet.toString());

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Map rowData = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            log.error(sql + "查询异常：", e);
            throw e;
        } finally {
            this.closeConnection(connection);
        }

        return list;
    }
}
