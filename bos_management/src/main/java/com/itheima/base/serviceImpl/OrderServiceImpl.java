package com.itheima.base.serviceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.AreaDao;
import com.itheima.base.dao.FixedAreaDao;
import com.itheima.base.dao.OrderDao;
import com.itheima.base.dao.WorkBillDao;
import com.itheima.base.service.take_delivery.OrderService;
import com.itheima.domain.Area;
import com.itheima.domain.Courier;
import com.itheima.domain.FixedArea;
import com.itheima.domain.SubArea;
import com.itheima.domain.take_delivery.Order;
import com.itheima.domain.take_delivery.WorkBill;

/**
 * 订单信息业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月9日
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	/** 获得定区持久化接口代理对象 */
	@Autowired
	private FixedAreaDao fixedAreaDao;

	/** 获得订单持久化接口代理对象 */
	@Autowired
	private OrderDao orderDao;

	/** 获得区域持久化代理对象 */
	@Autowired
	private AreaDao areaDao;

	/** 获得工单持久化代理对象 */
	@Autowired
	private WorkBillDao workBillDao;

	/** 获得mq模板对象 */
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void save(Order order) {

		// 给快递编号赋值
		order.setOrderNum(UUID.randomUUID().toString());
		// 设置订单时间
		order.setOrderTime(new Date());
		// 设置待取件
		order.setStatus("1");

		// 根据发件人提供的省市区信息进行查询
		Area sendArea = order.getSendArea();

		// 调用区域持久化对象进行查询
		Area persistArea = areaDao.findByProvinceAndCityAndDistrict(
				sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());

		// 根据收件人提供的省市区信息进行查询
		Area recArea = order.getRecArea();

		// 调用区域持久化对象进行查询
		Area persistRecArea = areaDao.findByProvinceAndCityAndDistrict(
				sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());

		// 给订单添加发件人区域
		order.setSendArea(persistArea);
		// 给订单添加收件人区域
		order.setRecArea(persistRecArea);

		// 根据用户地址查询区域ID
		String fixedAreaId = WebClient
				.create("http://localhost:9998/services/customerService/"
						+ "customer_findfixedAreaId?address="
						+ order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		// 若获得了区域ID
		if (fixedAreaId != null) {

			// 根据定区ID查询定区
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);

			// 根据定区获取相关联的快递员
			Set<Courier> couriers = fixedArea.getCouriers();

			// 遍历获得快递员
			for (Courier courier : couriers) {

				// 若拿到了快递员
				if (courier != null) {
					// 若订单没有关联快递员
					if (order.getCourier() == null) {
						System.out.println("第一种方式分单成功");
						// 保存订单
						saveOrder(order, courier);
						// 生成工单及发短信
						generateWorkBill(order);
						return;
					}

				}
			}

		}

		// 遍历获得区域里的所有分区
		for (SubArea subArea : persistArea.getSubareas()) {

			// 判断用户提交的地址数据是否包含分区辅助关键字
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {

				// 若包含根据分区查找对应的快递员
				Iterator<Courier> iterator = subArea.getFixedArea()
						.getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();

					// 若快递员存在
					if (courier != null) {
						// 若订单没有关联快递员
						if (order.getCourier() == null) {
							System.out.println("第二种方式分单成功=====");
							// 保存订单
							saveOrder(order, courier);
							// 生成工单及发短信
							generateWorkBill(order);

							return;
						}
					}
				}
			}
		}
		
		// 遍历获得区域里的所有分区根据分区关键字查询
		for (SubArea subArea : persistArea.getSubareas()) {

			// 判断用户提交的地址数据是否包含分区关键字
			if (order.getSendAddress().contains(subArea.getKeyWords())) {

				// 若包含根据分区查找对应的快递员
				Iterator<Courier> iterator = subArea.getFixedArea()
						.getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();

					// 若快递员存在
					if (courier != null) {
						// 若订单没有关联快递员
						if (order.getCourier() == null) {
							System.out.println("第二种方式分单成功======");
							// 保存订单
							saveOrder(order, courier);
							// 生成工单及发短信
							generateWorkBill(order);

							return;
						}
					}
				}

			}
		}

		// 人工分单
		// 设置分单类型
		order.setOrderType("2");
		System.out.println("第三种方式人工分单======");
		// 保存订单
		orderDao.save(order);
	}

	/**
	 * 生成工单并发送短信
	 */
	public void generateWorkBill(final Order order) {
		// 创建一个工单对象
		WorkBill workBill = new WorkBill();
		// 设置工单类型 新,追,销
		workBill.setType("新");
		// 取件状态
		workBill.setPickstate("新单");
		// 生成工单时间
		workBill.setBuildtime(new Date());
		// 工单备注
		workBill.setRemark(order.getRemark());
		// 生成4位随机数
		final String random = RandomStringUtils.randomNumeric(4);
		// 短信序号
		workBill.setSmsNumber(random);

		// 工单关联的订单
		workBill.setOrder(order);
		// 关联的快递员
		workBill.setCourier(order.getCourier());
		// 调用工单数据操作接口进行保存
		workBillDao.save(workBill);

		// 调用模板发送短信
		jmsTemplate.send("bos_msm", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 创建数据发送形式
				MapMessage mapMessage = session.createMapMessage();
				// 设置mapMessage 传输的参数
				mapMessage.setString("telephone", order.getCourier()
						.getTelephone());
				// 发送的短信内容
				mapMessage.setString(
						"msg",
						"短信序号:" + random + "收件地址:" + order.getSendAddress()
								+ "联系人:" + order.getSendName() + "发件人电话:"
								+ order.getSendMobile() + "给快递员捎的话:"
								+ order.getSendMobileMsg());
				return mapMessage;
			}
		});
		// 修改工单状态
		workBill.setPickstate("已通知");
	}

	/**
	 * 抽取保存订单
	 * 
	 * @param order
	 * @param courier
	 */
	private void saveOrder(Order order, Courier courier) {
		// 将快地员关联到订单上
		order.setCourier(courier);
		// 设置分单类型
		order.setOrderType("1");
		// 保存订单信息
		orderDao.save(order);
	}

	/**
	 * 根据订单号查询订单
	 */
	@Override
	public Order findByOrderNum(String orderNum) {
		return orderDao.findByOrderNum(orderNum);
	}

}
