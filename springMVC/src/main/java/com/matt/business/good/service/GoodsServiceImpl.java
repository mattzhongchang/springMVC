package com.matt.business.good.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.matt.business.good.entities.Goods;
import com.matt.business.good.mapper.GoodsDao;


/**
 * 商品业务实现
 * 
 */
//自动添加到Spring容器中
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService{
	//自动装配
	@Resource
	GoodsDao goodsdao;
	
	//分页
	@Override
	public List<Goods> getGoodsPager(int pageNO, int size) {
		int skip=(pageNO-1)*size;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("skip", skip);
		params.put("size", size);
		return goodsdao.getGoodsPager(params);
	}

	//获得单个产品对象
	@Override
	public Goods getGoodsById(int id) {
		return goodsdao.getGoodsById(id);
	}
	
	//获得商品总数
	@Override
	public int getGoodsCount() {
		return goodsdao.getGoodsCount();
	}

	//添加
	@Override
	public int insert(Goods entity) {
		return goodsdao.insert(entity);
	}

	//删除单个
	@Override
	public int delete(int id) {
		return goodsdao.delete(id);
	}
	
	//删除多个
	@Override
	public int deletes(int[] ids) {
		int rows=0;
		for (int id : ids) {
			rows+=delete(id);
		}
		return rows;
	}

	//更新
	@Override
	public int update(Goods entity) {
		return goodsdao.update(entity);
	}

}
