package com.itheima.base.serviceImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.jpa.repository.query.StringQueryParameterBinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.WayBillDao;
import com.itheima.base.service.take_delivery.WayBillService;
import com.itheima.domain.take_delivery.Order;
import com.itheima.domain.take_delivery.WayBill;
import com.itheima.index.WayBillRepository;

/**
 * 运单业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月12日
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	// 获得运单数据操作接口代理对象
	@Autowired
	private WayBillDao wayBillDao;

	@Autowired
	private WayBillRepository wayBillRepository;

	/**
	 * 运单保存操作
	 */
	@Override
	public void save(WayBill wayBill) {

		// 查询订单号是否存在
		WayBill persistWayBill = wayBillDao.findByWayBillNum(wayBill
				.getWayBillNum());

		if (persistWayBill == null || persistWayBill.getId() == null) {
			// 若不存在进行保存
			wayBillDao.save(wayBill);

			// 保存索引
			wayBillRepository.save(wayBill);

		} else {
			try {

				// 保存ID
				Integer id = persistWayBill.getId();
				// 进行数据更新到持久太对象
				BeanUtils.copyProperties(persistWayBill, wayBill);
				// 赋值ID
				persistWayBill.setId(id);
				// 保存索引
				wayBillRepository.save(persistWayBill);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}

	}

	/**
	 * 条件分页查询
	 */
	@Override
	public Page<WayBill> fandAll(WayBill wayBill, Pageable pageable) {

		// 判断查询条件
		if (StringUtils.isBlank(wayBill.getWayBillNum())
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null
				|| wayBill.getSignStatus() == 0)) {

			// 无条件分页查询
			return wayBillDao.findAll(pageable);
		} else {
			// 创建条件对象
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

			// 若运单号不为空
			if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
				// 设置全匹配查询条件
				QueryBuilder termQuery = new TermQueryBuilder("wayBillNum",
						wayBill.getWayBillNum());
				
				//添加查询条件
				boolQueryBuilder.must(termQuery);
			}
			
			//若发货地址不为空
			if(StringUtils.isNotBlank(wayBill.getSendAddress())){
				//第一种设置模糊条件查询 单个字
				QueryBuilder wildcardBuilder=new WildcardQueryBuilder("sendAddress", 
						"*"+wayBill.getSendAddress()+"*");
				
				//第二种多个词条组合查询 将查询条件进行分词进行查询
				QueryBuilder stringQueryBuilder=new QueryStringQueryBuilder(
						wayBill.getSendAddress()).field("sendAddress")
						.defaultOperator(Operator.AND);
				
				//两个条件and链接
				BoolQueryBuilder queryBuilder=new BoolQueryBuilder();
				queryBuilder.should(wildcardBuilder);
				queryBuilder.should(stringQueryBuilder);
				
				//添加条件
				boolQueryBuilder.must(queryBuilder);
			} 
			
			//收货地址不为空
			if(StringUtils.isNotBlank(wayBill.getRecAddress())){
				//模糊条件查询
				QueryBuilder wildcardQuery=new WildcardQueryBuilder("recAddress",
						"*"+wayBill.getRecAddress()+"*");
				//添加条件
				boolQueryBuilder.must(wildcardQuery);
			}
			
			//快递产品类型
			if(StringUtils.isNotBlank(wayBill.getSendProNum())){
				
				//分词全匹配查询
				QueryBuilder stringBuilder=new QueryStringQueryBuilder(
						wayBill.getSendProNum()).field("sendProNum")
						.defaultOperator(Operator.AND);
				
				//添加条件
				boolQueryBuilder.must(stringBuilder);
			}
			
			//运单状态等值查询
			if(wayBill.getSignStatus()!=null&&wayBill.getSignStatus()!=0){
				
				//等值查询
				QueryBuilder tweyquery=new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				
				//添加条件
				boolQueryBuilder.must(tweyquery);
			}
			
			//创建分页组合查询
			SearchQuery searchQuery=new NativeSearchQuery(boolQueryBuilder);
			searchQuery.setPageable(pageable);
			
			//查询返回
			return wayBillRepository.search(searchQuery);
		}

	}

	/**
	 * 根据运单号进行查询
	 */
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillDao.findByWayBillNum(wayBillNum);
	}

	/**
	 * 根据订单ID查询运单
	 */
	@Override
	public WayBill findByOrder(Integer id) {
		return wayBillDao.findByOrder(id);
	}

}
