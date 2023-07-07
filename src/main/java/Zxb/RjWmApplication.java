package Zxb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@SpringBootApplication
@ServletComponentScan//扫描保证过滤器生效
@EnableTransactionManagement//开启事务注解
public class RjWmApplication {

    public static void main(String[] args) {
        SpringApplication.run(RjWmApplication.class, args);
        log.info("项目启动成功！");
    }

}
