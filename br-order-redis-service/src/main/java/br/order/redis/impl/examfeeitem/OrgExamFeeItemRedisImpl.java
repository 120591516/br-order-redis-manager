package br.order.redis.impl.examfeeitem;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examfeeitem.OrganizationExamFeeItem;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemService;
import br.order.redis.examfeeitem.OrgExamFeeItemRedis;
import br.order.redis.redis.RedisService;
@Service
public class OrgExamFeeItemRedisImpl implements OrgExamFeeItemRedis {
	@Autowired
	private OrgExamFeeItemService orgExamFeeItemService;
	
	@Autowired
	@Qualifier("RedisInnerService")
	private RedisService redisService;

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}
	@Override
	public void initData() {
		List<OrganizationExamFeeItem> list = orgExamFeeItemService.getAllOrgExamFeeItem();
		if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationExamFeeItem organizationExamFeeItem : list) {
                redisService.set(RedisConstant.br_order_orgExamFeeItem_id.concat(organizationExamFeeItem.getId()), JSONObject.toJSONString(organizationExamFeeItem));
            }
        }
	}

	@Override
	public String insertOrganizationExamFeeItem(OrganizationExamFeeItem organizationExamFeeItem) {
		
		return redisService.set(RedisConstant.br_order_orgExamFeeItem_id.concat(organizationExamFeeItem.getId()), JSONObject.toJSONString(organizationExamFeeItem));
	}

	@Override
	public OrganizationExamFeeItem getOrganizationExamFeeItemById(String id) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamFeeItem_id.concat(id)), OrganizationExamFeeItem.class);
	}

	@Override
	public String updateOrganizationExamFeeItemById(OrganizationExamFeeItem organizationExamFeeItem) {
		return redisService.set(RedisConstant.br_order_orgExamFeeItem_id.concat(organizationExamFeeItem.getId()), JSONObject.toJSONString(organizationExamFeeItem));
	}
	

}
