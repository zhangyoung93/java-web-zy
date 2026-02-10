package com.zy.demo.service;

import com.zy.demo.pojo.UserDto;

import java.util.List;

/**
 * BatchDataService
 *
 * @author zy
 */
public interface BatchDataService {

    List<UserDto> doSaveUser(List<UserDto> userDtoList);
}
