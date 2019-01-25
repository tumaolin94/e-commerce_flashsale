package com.maolintu.flashsale.service;

import com.maolintu.flashsale.dao.GoodsDao;
import com.maolintu.flashsale.vo.GoodsVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

  @Autowired
  GoodsDao goodsDao;

  public List<GoodsVo> listGoodsVo(){
    return goodsDao.listGoodsVo();
  }

  public GoodsVo getGoodsVoByGoodsId(long goodsId) {

    return goodsDao.getGoodsVoByGoodsId(goodsId);
  }
}
