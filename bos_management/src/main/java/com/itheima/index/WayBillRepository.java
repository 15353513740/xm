package com.itheima.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.itheima.domain.take_delivery.WayBill;

/**
 * 索引搜索引擎持久化接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月15日
 */
public interface WayBillRepository extends ElasticsearchRepository<WayBill, Integer> {

}
