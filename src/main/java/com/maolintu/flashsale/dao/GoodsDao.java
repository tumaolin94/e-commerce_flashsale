package com.maolintu.flashsale.dao;

import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GoodsDao {

  @Select("select g.*,fg.stock_count, fg.start_date, fg.end_date,fg.sale_price from flashsale_goods fg left join goods g on fg.goods_id = g.id")
  public List<GoodsVo> listGoodsVo();

  @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.sale_price from flashsale_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
  GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);
}
