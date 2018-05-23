package cn.kenenjoy.service;

import cn.kenenjoy.util.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hefa on 2018/5/22.
 */
@Service
public class CreditRiskService {

    @Autowired
    private DataSource dataSource;

    public List queryCreditRisk(String custIsn) throws SQLException  {
        String sql = "SELECT RULE_LEVEL_DES2 AS ruledesc2, COUNT (RULE_LEVEL_DES2) AS riskCount FROM CRMS.RULE_CREDIT WHERE CUST_ID = ? GROUP BY RULE_LEVEL_DES2";
        List list = dataSource.query(sql, new String[]{custIsn});
        return list;
    }

    public List queryCreditRiskByDes(String custIsn, String des, Integer pageNum, Integer pageSize) throws SQLException {
        String sql = "SELECT RULE_LEVEL_DES2 AS ruledesc2,OUTPUT_REMARK AS outputRemark,CST_NO AS cstNo,CUST_ID AS custId FROM CRMS.RULE_CREDIT WHERE cust_id =? AND RULE_LEVEL_DES2 =? limit ?,? ";
        List list = dataSource.query(sql, new Object[]{custIsn, des, pageNum, pageSize});
        return list;
    }

    public List query() throws SQLException {
        String sql = "SELECT count(1) AS counts FROM CRMS.RULE_CREDIT";
        List list = dataSource.query(sql, new String[]{});
        return list;
    }
}
