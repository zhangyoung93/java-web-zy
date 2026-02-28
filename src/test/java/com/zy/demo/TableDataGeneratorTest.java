package com.zy.demo;

import com.zy.demo.dao.mysql.IdUserBaseMapper;
import com.zy.demo.model.IdUserBase;
import com.zy.demo.util.SnowflakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * TableDataGenerator
 *
 * @author zy
 */
@SpringBootTest
public class TableDataGeneratorTest {

    private static final Random random = new Random();

    private static final int dataSize = 10000;

    @Autowired
    private IdUserBaseMapper idUserBaseMapper;

    @Test
    public void generateUserBase() {
        for (int i = 1; i <= dataSize; i++) {
            IdUserBase idUserBase = new IdUserBase();
            idUserBase.setUserId(SnowflakeIdGenerator.getInstance().nextId());
            idUserBase.setUserName("user" + i);
            idUserBase.setFullName("用户" + i);
            StringBuilder mobile = new StringBuilder("186");
            for (int j = 1; j <= 8; j++) {
                mobile.append(random.nextInt(10));
            }
            idUserBase.setMobile(mobile.toString());
            StringBuilder idCard = new StringBuilder("140102");
            LocalDate startDate = LocalDate.of(1950, 1, 1);
            LocalDate endDate = LocalDate.of(2025, 12, 31);
            long days = endDate.toEpochDay() - startDate.toEpochDay();
            LocalDate randomDate = startDate.plusDays(random.nextInt((int) days + 1));
            idCard.append(randomDate.format(DateTimeFormatter.BASIC_ISO_DATE));
            idCard.append(String.format("%03d", random.nextInt(999)));
            idCard.append("X");
            idUserBase.setIdCard(idCard.toString());
            idUserBase.setSex(true);
            idUserBase.setUserStatus("2000");
            this.idUserBaseMapper.insertOne(idUserBase);
        }
    }
}
