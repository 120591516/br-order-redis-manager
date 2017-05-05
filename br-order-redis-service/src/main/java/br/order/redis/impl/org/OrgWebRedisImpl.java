package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationWeb;
import br.crm.service.org.OrgWebService;
import br.order.redis.org.OrgWebRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构网站信息redis)
 * 
 * @ClassName: OrgWebRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午6:20:48
 */
@Service
public class OrgWebRedisImpl implements OrgWebRedis {
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
    private OrgWebService orgWebService;

    @Override
    public void initData() {
        List<OrganizationWeb> list = orgWebService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationWeb organizationWeb : list) {
                redisService.set(RedisConstant.br_order_orgWeb_orgWebId.concat(organizationWeb.getOrgWebId().toString()), JSONObject.toJSONString(organizationWeb));
            }
        }
    }

    @Override
    public int setOrgWeb(OrganizationWeb organizationWeb) {
        redisService.set(RedisConstant.br_order_orgWeb_orgWebId.concat(organizationWeb.getOrgWebId().toString()), JSONObject.toJSONString(organizationWeb));
        return 1;
    }

    @Override
    public OrganizationWeb getOrgWeb(Long orgWebId) {
        OrganizationWeb organizationWeb = null;
        if (redisService.exists(RedisConstant.br_order_orgWeb_orgWebId.concat(orgWebId.toString()))) {
            organizationWeb = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgWeb_orgWebId.concat(orgWebId.toString())), OrganizationWeb.class);
        }
        return organizationWeb;
    }

    @Override
    public int deleteOrgWeb(Long orgWebId) {
        if (redisService.exists(RedisConstant.br_order_orgWeb_orgWebId.concat(orgWebId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgWeb_orgWebId.concat(orgWebId.toString()));
        }
        return 1;
    }

}
