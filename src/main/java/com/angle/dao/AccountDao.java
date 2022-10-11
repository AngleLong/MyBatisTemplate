package com.angle.dao;

import com.angle.domain.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
