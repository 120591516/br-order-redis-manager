package br.order.redis.impl.examitem;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examitem.OrganizationExamItem;
import br.crm.service.examitem.OrgExamItemService;
import br.order.redis.examitem.OrgExamItemRedis;
import br.order.redis.redis.RedisService;

@Service
public class OrgExamItemRedisImpl implements OrgExamItemRedis {
	
	
	@Autowired
	@Qualifier("RedisInnerService")
	private RedisService redisService;

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	@Autowired
	private OrgExamItemService orgExamItemService;

	/**
	 * <p>
	 * Title:initData
	 * </p>
	 * <p>
	 * Description: 体检机构体检项redis初始化缓存/p>
	 * 
	 * @see br.crm.redis.examitem.OrgExamItemRedis#initData()
	 */
	public void initData() {
		List<OrganizationExamItem> list = orgExamItemService.getAllOrgExamItem();
		if (CollectionUtils.isNotEmpty(list)) {
			for (OrganizationExamItem organizationExamItem : list) {
				redisService.set(RedisConstant.br_order_orgExamItem_id.concat(organizationExamItem.getExamItemId()), JSONObject.toJSONString(organizationExamItem));
			}
		}
	}

	public String updateOrgExamItem(OrganizationExamItem orgExamItem) {
		String result = redisService.set(RedisConstant.br_order_orgExamItem_id.concat(orgExamItem.getExamItemId()), JSONObject.toJSONString(orgExamItem));

		return result;
	}

	public String insertOrgExamItem(OrganizationExamItem orgExamItem) {
		String result = redisService.set(RedisConstant.br_order_orgExamItem_id.concat(orgExamItem.getExamItemId()), JSONObject.toJSONString(orgExamItem));
		return result;
	}

	public OrganizationExamItem getOrgExamItemById(String orgExamItemId) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamItem_id.concat(orgExamItemId)), OrganizationExamItem.class);
	}

}
