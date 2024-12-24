package com.zhikaixu.servicedriveruser.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码工具类
 */
public class MysqlGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-driver-user?characterEncoding=utf-8&serverTimeZone=GMT%2B8",
                "root", "root")
                .globalConfig(builder -> {
                    builder.author("zhikaixu").fileOverride().outputDir("/Users/zhikaixu/Desktop/online-taxi-backup/service-driver-user/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhikaixu.servicedriveruser").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "/Users/zhikaixu/Desktop/online-taxi-backup/service-driver-user/src/main/java/com/zhikaixu/servicedriveruser/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("car");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
