package com.maolintu.flashsale.dao;

import com.maolintu.flashsale.domain.FlashsaleOrder;
import com.maolintu.flashsale.domain.OrderInfo;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderDao {

  @Select("select * from flashsale_order where user_id=#{userId} and goods_id=#{goodsId}")
  FlashsaleOrder getSaleOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

  @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
      + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
  @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
  long insert(OrderInfo orderInfo);

  @Insert("insert into flashsale_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
  int insertFlashsaleOrder(FlashsaleOrder flashsaleOrder);

  @Select("select * from order_info where id = #{orderId}")
  OrderInfo getOrderById(@Param("orderId") long orderId);
}
