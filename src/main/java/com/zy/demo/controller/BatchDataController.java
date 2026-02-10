package com.zy.demo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zy.demo.pojo.BaseResponse;
import com.zy.demo.pojo.UserDto;
import com.zy.demo.service.BatchDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 保存批量数据接口
 * 添加流控、熔断
 *
 * @author zy
 */
@Slf4j
@RestController
@RequestMapping("/batch/data")
public class BatchDataController {

    private final BatchDataService batchDataService;

    public BatchDataController(BatchDataService batchDataService) {
        this.batchDataService = batchDataService;
    }

    /**
     * 前置拦截：
     * 1、流量控制
     * 2、接口验签
     * 3、幂等校验
     */
    @PostMapping("/save/user")
    @SentinelResource(value = "batchDataApi")
    public ResponseEntity<Object> saveUser(@RequestBody List<UserDto> userDtoList) {
        BaseResponse<Object> baseResponse = BaseResponse.success(null);
        try {
            Assert.notEmpty(userDtoList, "requestBody must not be null!");
            if (userDtoList.size() > 500) {
                throw new IllegalArgumentException("data size must not great than 500");
            }
            List<UserDto> failList = this.batchDataService.doSaveUser(userDtoList);
            if (CollectionUtils.isNotEmpty(failList)) {
                baseResponse = BaseResponse.fail("部分数据处理失败，详情见data！", failList);
            }
        } catch (Exception e) {
            log.error("saveUser fail!", e);
            baseResponse = BaseResponse.fail("接口处理异常," + e.getMessage());
        }
        return ResponseEntity.ok(baseResponse);
    }
}
