package br.order.redis.impl.examitem;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examitem.OrganizationExamItemType;
import br.crm.service.examitem.OrgExamItemTypeService;
import br.order.redis.examitem.OrgExamItemTypeRedis;
import br.order.redis.redis.RedisService;

@Service
public class OrgExamItemTypeRedisImpl implements OrgExamItemTypeRedis {
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
    private OrgExamItemTypeService orgExamItemTypeService;

    @Override
    public void initData() {
        List<OrganizationExamItemType> list = orgExamItemTypeService.getAllOrgExamItemType();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationExamItemType organizationExamItemType : list) {
                redisService.set(RedisConstant.br_order_orgExamItemType_id.concat(organizationExamItemType.getExamItemTypeId()), JSONObject.toJSONString(organizationExamItemType));
            }
        }
    }

    @Override
    public int setOrgExamItemType(OrganizationExamItemType organizationExamItemType) {
        redisService.set(RedisConstant.br_order_orgExamItemType_id.concat(organizationExamItemType.getExamItemTypeId()), JSONObject.toJSONString(organizationExamItemType));
        return 1;
    }

    @Override
    public OrganizationExamItemType getOrgExamItemType(String examItemTypeId) {
        return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamItemType_id.concat(examItemTypeId)), OrganizationExamItemType.class);
    }

    @Override
    public int deleteOrgExamItemType(String examItemTypeId) {
        redisService.delete(RedisConstant.br_order_orgExamItemType_id.concat(examItemTypeId));
        return 1;
    }

}
