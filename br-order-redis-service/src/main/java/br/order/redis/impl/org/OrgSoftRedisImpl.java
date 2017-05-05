package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationSoft;
import br.crm.service.org.OrgSoftService;
import br.order.redis.org.OrgSoftRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构软件信息redis)
 * 
 * @ClassName: OrgSoftRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午5:50:17
 */
@Service
public class OrgSoftRedisImpl implements OrgSoftRedis {
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
    private OrgSoftService orgSoftService;

    @Override
    public void initData() {
        List<OrganizationSoft> list = orgSoftService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationSoft organizationSoft : list) {
                redisService.set(RedisConstant.br_order_orgSoft_orgSoftId.concat(organizationSoft.getOrgSoftId().toString()), JSONObject.toJSONString(organizationSoft));

            }
        }
    }

    @Override
    public int setOrgSoft(OrganizationSoft organizationSoft) {
        redisService.set(RedisConstant.br_order_orgSoft_orgSoftId.concat(organizationSoft.getOrgSoftId().toString()), JSONObject.toJSONString(organizationSoft));
        return 1;
    }

    @Override
    public OrganizationSoft getOrgSoft(Long orgSoftId) {
        OrganizationSoft organizationSoft = null;
        if (redisService.exists(RedisConstant.br_order_orgSoft_orgSoftId.concat(orgSoftId.toString()))) {
            organizationSoft = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgSoft_orgSoftId.concat(orgSoftId.toString())), OrganizationSoft.class);
        }
        return organizationSoft;
    }

    @Override
    public int deleteOrgSoft(Long orgSoftId) {
        if (redisService.exists(RedisConstant.br_order_orgSoft_orgSoftId.concat(orgSoftId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgSoft_orgSoftId.concat(orgSoftId.toString()));
        }
        return 1;
    }

}
