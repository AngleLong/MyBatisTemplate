### [项目地址](https://github.com/AngleLong/MyBatisTemplate)

## 1. 导入相应的坐标

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>MyBatisTemplate</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.23</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.16</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.11</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>

        <!--jdbc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.23</version>
        </dependency>

        <!--整合MyBatis的-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.7</version>
        </dependency>

        <!--junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>

        <!--spring整合测试-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.23</version>
        </dependency>
    </dependencies>
</project>
```



提前准备一下本地连接sql的信息

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=hjl092810
```



## 2. 纯注解形式添加配置

### 2.1 创建SpringConfig文件

> 这个文件的主要作用就是添加一些基础的配置

```java
@Configuration
@ComponentScan("com.angle")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
public class SpringConfig {

}
```



这里解释一下:

1. Configuration 是表名这个类的主要作用是bean定义的源
2. ComponentScan 通过这个配置扫描哪个包
3. PropertySource 配置资源文件的(如果多个的情况下使用{}).
4. Import 导入一些其他的配置类

### 2.2 创建配置MyBatis的类

> 上面导入的 **mybatis-spring** 是mybatis为整合spring的类库.



```java
/**
 * Mybatis的配置
 */
public class MybatisConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {

        // SqlSessionFactoryBean这个类是mybatis-spring为我们提供简化赋值的一些操作,
        // 它把需要的操作都封装到了DruidDataSource(DataSource)的子类,因为有自动装填,
        // 所以这里直接传进来就行了.

        //创建sql的链接对象,其实这个就是对应之前MyBatis中的配置文件的

        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();

        // 别名
        ssfb.setTypeAliasesPackage("com.angle");

        // 设置dataSource 这里面的dataSource是JdbcConfig中提供的.
        ssfb.setDataSource(dataSource);

        return ssfb;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {

        // 这里对应的Mybatis中写的mapper因为扫描的是dao下面的所有的Mapper,
        // 所以这里就直接写的文件路径了

        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        //扫描dao下的内容
        msc.setBasePackage("com.angle.dao");

        return msc;
    }
}
```



```java
/**
 * 配置jdbc的管理文件
 * 这里面的value中指定的内容,是因为在SpringConfig中配置了@PropertySource("classpath:jdbc.properties")
 * 所以可以直接使用里面的内容
 */
public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        // 提供dataSource的方法

        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

### 2.3 快速创建相应的数据

1. 首先要创建一个数据库和数据库表(很简单的一张表)

```sql
create table account
(
    id    int auto_increment comment '主键ID' primary key,
    name  varchar(10) null comment '姓名',
    money int         null comment '余额'
) comment '账户表';
```



2. 创建表对应的实体类

```java
public class Account {
    public int id;
    public String name;
    public String money;

    public Account(int id, String name, String money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public Account(String name, String money) {
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money='" + money + '\'' +
                '}';
    }
}
```



3. 创建Dao对象(因为使用注解,所以这里写的都是简单的sql)

```java
public interface AccountDao {

    @Insert("insert into account (name, money) value (#{name},#{money})")
    void save(Account account);

    @Update("update account set name = #{name},money = #{money} where id = #{id}")
    void update(Account account);

    @Delete("delete  from  account where id = #{id}")
    void delete(int id);

    @Select("select * from account where id = #{id}")
    Account findById(int id);

    @Select("select * from account")
    List<Account> findAll();
}
```



4. 创建服务层代码

```java
public interface AccountService {

    void save(Account account);

    void update(Account account);

    void delete(int id);

    Account findById(int id);

    List<Account> findAll();
}

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public void save(Account account) {
        accountDao.save(account);
    }

    @Override
    public void update(Account account) {
        accountDao.update(account);
    }

    @Override
    public void delete(int id) {
        accountDao.delete(id);
    }

    @Override
    public Account findById(int id) {
        return   accountDao.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }
}
```

### 2.4 使用相应的服务

```java
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
```



### 2.5 关于JUnit的使用

> 这里其实就是上面的两个注解.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testFindById() {

        Account account = accountService.findById(1);

        System.out.println(account);
    }

}
```



## 3. 可能出现的问题

1. 关于数据库的连接.

> 在idea中连接数据库还是挺方便的.直接点右边的database关联上相应的sql就行了(你得有sql). 
