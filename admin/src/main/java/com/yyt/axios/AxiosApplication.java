package com.yyt.axios;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
@MapperScan(basePackages = "com.yyt.axios.mapper")
public class AxiosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxiosApplication.class, args);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("OSSConfig.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
