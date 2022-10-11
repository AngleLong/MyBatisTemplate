package com.angle;

import com.angle.config.SpringConfig;
import com.angle.domain.Account;
import com.angle.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        //获取ioc容器
//        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml.ktx");
//        ApplicationContext act = new AnnotationConfigApplicationContext(SpringConfig.class);
//        BookDao bookDao = (BookDao) act.getBean("bookDao");
//        bookDao.save();

//        BookService bookService  = (BookService) act.getBean("bookService");
//        bookService.save();

//        DataSource dataSource = (DataSource) act.getBean("dataSource");
//        System.out.println(dataSource);


        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        AccountService accountService = ctx.getBean(AccountService.class);
//        Account account = accountService.findById(2);
//        System.out.println(account);

        accountService.save(new Account("王五","4000"));

    }
}
