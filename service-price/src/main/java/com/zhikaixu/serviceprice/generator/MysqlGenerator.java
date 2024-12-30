package com.zhikaixu.serviceprice.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码工具类
 */
public class MysqlGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-price?characterEncoding=utf-8&serverTimeZone=GMT%2B8",
                "root", "root")
                .globalConfig(builder -> {
                    builder.author("zhikaixu").fileOverride().outputDir("/Users/zhikaixu/Desktop/online-taxi-backup/service-price/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhikaixu.serviceprice").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "/Users/zhikaixu/Desktop/online-taxi-backup/service-price/src/main/java/com/zhikaixu/serviceprice/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("price_rule");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
