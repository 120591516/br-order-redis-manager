package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationConn;
import br.crm.service.org.OrgConnService;
import br.order.redis.org.OrgConnRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构联系人redis)
 * 
 * @ClassName: OrgConnRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午4:48:26
 */
@Service
public class OrgConnRedisImpl implements OrgConnRedis {
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
    private OrgConnService orgConnService;

    @Override
    public void initData() {
        List<OrganizationConn> list = orgConnService.getAllConn();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationConn organizationConn : list) {
                redisService.set(RedisConstant.br_order_orgConn_orgConnId.concat(organizationConn.getOrgConnId().toString()), JSONObject.toJSONString(organizationConn));

            }
        }
    }

    @Override
    public int setOrganizationConn(OrganizationConn organizationConn) {
        redisService.set(RedisConstant.br_order_orgConn_orgConnId.concat(organizationConn.getOrgConnId().toString()), JSONObject.toJSONString(organizationConn));
        return 1;
    }

    @Override
    public OrganizationConn getOrganizationConn(Long orgConnId) {
        OrganizationConn organizationConn = null;
        if (redisService.exists(RedisConstant.br_order_orgConn_orgConnId + orgConnId.toString())) {
            organizationConn = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgConn_orgConnId + orgConnId.toString()), OrganizationConn.class);
        }
        return organizationConn;
    }

    @Override
    public int deleteOrganizationConn(Long orgConnId) {
        if (redisService.exists(RedisConstant.br_order_orgConn_orgConnId + orgConnId.toString())) {
            redisService.delete(RedisConstant.br_order_orgConn_orgConnId + orgConnId.toString());
        }
        return 1;
    }

}
