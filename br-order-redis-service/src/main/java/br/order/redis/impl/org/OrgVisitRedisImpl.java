package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationVisit;
import br.crm.service.org.OrgVisitService;
import br.order.redis.org.OrgVisitRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构拜访信息redis)
 * 
 * @ClassName: OrgVisitRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午6:20:09
 */
@Service
public class OrgVisitRedisImpl implements OrgVisitRedis {
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
    private OrgVisitService orgVisitService;

    @Override
    public void initData() {
        List<OrganizationVisit> list = orgVisitService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationVisit organizationVisit : list) {
                redisService.set(RedisConstant.br_order_orgVisit_orgVisitId.concat(organizationVisit.getOrgVisitId().toString()), JSONObject.toJSONString(organizationVisit));
            }
        }
    }

    @Override
    public int setOrgVisit(OrganizationVisit organizationVisit) {
        redisService.set(RedisConstant.br_order_orgVisit_orgVisitId.concat(organizationVisit.getOrgVisitId().toString()), JSONObject.toJSONString(organizationVisit));
        return 1;
    }

    @Override
    public OrganizationVisit getOrgVisit(Long orgVisitId) {
        OrganizationVisit organizationVisit = null;
        if (redisService.exists(RedisConstant.br_order_orgVisit_orgVisitId.concat(orgVisitId.toString()))) {
            organizationVisit = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgVisit_orgVisitId.concat(orgVisitId.toString())), OrganizationVisit.class);
        }
        return organizationVisit;
    }

    @Override
    public int deleteOrgVisit(Long orgVisitId) {
        if (redisService.exists(RedisConstant.br_order_orgVisit_orgVisitId.concat(orgVisitId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgVisit_orgVisitId.concat(orgVisitId.toString()));
        }
        return 1;
    }

}
