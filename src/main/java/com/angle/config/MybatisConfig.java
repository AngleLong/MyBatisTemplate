package com.angle.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class MybatisConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {

        //创建sql的链接对象,其实这个就是对应之前MyBatis中的配置文件的

        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();

        ssfb.setTypeAliasesPackage("com.angle");

        ssfb.setDataSource(dataSource);

        return ssfb;
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {

        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        //扫描dao下的内容
        msc.setBasePackage("com.angle.dao");

        return msc;
    }
}
