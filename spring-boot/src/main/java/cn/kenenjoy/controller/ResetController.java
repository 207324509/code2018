package cn.kenenjoy.controller;

import cn.kenenjoy.bean.Result;
import cn.kenenjoy.service.CreditRiskService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by hefa on 2018/5/11.
 */
@RestController
@RequestMapping("/api")
public class ResetController {
    private static final Logger log = LoggerFactory.getLogger(ResetController.class);

    @Autowired
    private CreditRiskService creditRiskService;

    @RequestMapping("/queryCreditRisk")
    public String queryCreditRisk(String custIsn) {
        log.info("请求报文：" + custIsn);
        Gson gson = new Gson();
        Result result = new Result();
        if (StringUtils.isNotBlank(custIsn)) {
            try {
                List list = creditRiskService.queryCreditRisk(custIsn);
                result.setCode("0000");
                result.setMessage(gson.toJson(list));
            } catch (Exception e) {
                result.setCode("1000");
                result.setMessage("查询报错:" + e.getMessage());
            }
        } else {
            result.setCode("1000");
            result.setMessage("参数不正确！");
        }
        String response = gson.toJson(result);
        log.info("响应报文：" + response);
        return response;
    }

    @RequestMapping("/queryCreditRiskByDes")
    public String queryCreditRiskByDes(String custIsn, String des, Integer pageNum, Integer pageSize) {
        log.info("请求报文：[custIsn=" + custIsn + ",des=" + des + ",pageNum=" + pageNum + ",pageSize=" + pageSize + "]");
        Gson gson = new Gson();
        Result result = new Result();
        if (StringUtils.isNotBlank(custIsn) && StringUtils.isNotBlank(des) && pageNum != null && pageSize != null) {
            try {
                List list = creditRiskService.queryCreditRiskByDes(custIsn, des, pageNum, pageSize);
                result.setCode("0000");
                result.setMessage(gson.toJson(list));
            } catch (Exception e) {
                result.setCode("1000");
                result.setMessage("查询报错:" + e.getMessage());
            }
        } else {
            result.setCode("1000");
            result.setMessage("参数不正确！");
        }
        String response = gson.toJson(result);
        log.info("响应报文：" + response);
        return response;
    }

    @RequestMapping("/test")
    public String test() throws SQLException {
        Gson gson = new Gson();
        Result result = new Result();

        result.setCode("0000");
        result.setMessage(gson.toJson(creditRiskService.query()));

        String response = gson.toJson(result);
        log.info("响应报文：" + response);
        return response;
    }
}
