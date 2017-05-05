package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationInvest;
import br.crm.service.org.OrgInvestService;
import br.order.redis.org.OrgInvestRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构投资人redis)
 * 
 * @ClassName: OrgLevelRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午5:10:23
 */
@Service
public class OrgInvestRedisImpl implements OrgInvestRedis {
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
    private OrgInvestService orgInvestService;

    @Override
    public void initData() {
        List<OrganizationInvest> list = orgInvestService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationInvest organizationInvest : list) {
                redisService.set(RedisConstant.br_order_orgInvest_orgInvestId.concat(organizationInvest.getOrgInvestId().toString()), JSONObject.toJSONString(organizationInvest));
            }
        }
    }

    @Override
    public int setOrgInvest(OrganizationInvest organizationInvest) {
        redisService.set(RedisConstant.br_order_orgInvest_orgInvestId.concat(organizationInvest.getOrgInvestId().toString()), JSONObject.toJSONString(organizationInvest));
        return 1;
    }

    @Override
    public OrganizationInvest getOrgInvest(Long orgInvestId) {
        OrganizationInvest organizationInvest = null;
        if (redisService.exists(RedisConstant.br_order_orgInvest_orgInvestId.concat(orgInvestId.toString()))) {
            organizationInvest = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgInvest_orgInvestId.concat(orgInvestId.toString())), OrganizationInvest.class);
        }
        return organizationInvest;
    }

    @Override
    public int deleteOrgInvest(Long orgInvestId) {
        if (redisService.exists(RedisConstant.br_order_orgInvest_orgInvestId.concat(orgInvestId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgInvest_orgInvestId.concat(orgInvestId.toString()));
        }
        return 1;
    }

}
