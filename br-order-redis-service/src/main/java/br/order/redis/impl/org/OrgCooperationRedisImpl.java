package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationCooperation;
import br.crm.service.org.OrgCooperationService;
import br.order.redis.org.OrgCooperationRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构合作意向redis)
 * 
 * @ClassName: OrgCooperationRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午4:48:13
 */
@Service
public class OrgCooperationRedisImpl implements OrgCooperationRedis {
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
    private OrgCooperationService orgCooperationService;

    @Override
    public void initData() {
        List<OrganizationCooperation> list = orgCooperationService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationCooperation orgCooperation : list) {
                redisService.set(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(orgCooperation.getOrgCooperationId().toString()), JSONObject.toJSONString(orgCooperation));
            }
        }
    }

    @Override
    public int setOrgCooperation(OrganizationCooperation organizationCooperation) {
        redisService.set(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(organizationCooperation.getOrgCooperationId().toString()), JSONObject.toJSONString(organizationCooperation));
        return 1;
    }

    @Override
    public OrganizationCooperation getOrgCooperation(Long orgCooperationId) {
        OrganizationCooperation organizationCooperation = null;
        if (redisService.exists(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(orgCooperationId.toString()))) {
            organizationCooperation = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(orgCooperationId.toString())), OrganizationCooperation.class);
        }
        return organizationCooperation;
    }

    @Override
    public int deleteOrgCooperation(Long orgCooperationId) {
        if (redisService.exists(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(orgCooperationId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgCooperation_orgCooperationId.concat(orgCooperationId.toString()));
        }
        return 1;
    }

}
