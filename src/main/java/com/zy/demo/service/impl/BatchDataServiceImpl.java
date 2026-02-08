package com.zy.demo.service.impl;

import com.zy.demo.pojo.UserDto;
import com.zy.demo.service.BatchDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BatchDataServiceImpl
 *
 * @author zy
 */
@Slf4j
@Service
public class BatchDataServiceImpl implements BatchDataService {

    @Override
    public List<UserDto> doSaveUser(List<UserDto> userDtoList) {
        //当前批次数据去重

        return null;
    }
}
