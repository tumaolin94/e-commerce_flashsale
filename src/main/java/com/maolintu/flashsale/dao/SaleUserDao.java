package com.maolintu.flashsale.dao;

import com.maolintu.flashsale.domain.SaleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SaleUserDao {
  @Select("select * from users where id = #{id}")
  public SaleUser getById(@Param("id")long id);

  @Update("update users set password = #{password} where id = #{id}")
  public void update(SaleUser toBeUpdate);
}
