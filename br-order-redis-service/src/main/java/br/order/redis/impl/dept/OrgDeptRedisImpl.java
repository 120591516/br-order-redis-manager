package br.order.redis.impl.dept;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dept.OrganizationDept;
import br.crm.service.dept.OrgDeptService;
import br.order.redis.dept.OrgDeptRedis;
import br.order.redis.redis.RedisService;

@Service
public class OrgDeptRedisImpl implements OrgDeptRedis {
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
    private OrgDeptService orgDeptService;

    @Override
    public void initData() {
        List<OrganizationDept> list = orgDeptService.getOrgDeptAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationDept organizationDept : list) {
                if (!redisService.exists(RedisConstant.br_order_orgDept_orgDeptId.concat(organizationDept.getOrgDeptId()))) {
                    redisService.set(RedisConstant.br_order_orgDept_orgDeptId.concat(organizationDept.getOrgDeptId()), JSONObject.toJSONString(organizationDept));
                }
            }

        }
    }

    @Override
    public int setOrgDept(OrganizationDept organizationDept) {
        redisService.set(RedisConstant.br_order_orgDept_orgDeptId.concat(organizationDept.getOrgDeptId()), JSONObject.toJSONString(organizationDept));
        return 1;
    }

    @Override
    public OrganizationDept getOrgDept(String orgDeptId) {
        OrganizationDept organizationDept = null;
        if (redisService.exists(RedisConstant.br_order_orgDept_orgDeptId.concat(orgDeptId))) {
            organizationDept = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgDept_orgDeptId.concat(orgDeptId)), OrganizationDept.class);
        }
        return organizationDept;
    }

    @Override
    public int deleteOrgDept(String orgDeptId) {
        if (redisService.exists(RedisConstant.br_order_orgDept_orgDeptId.concat(orgDeptId))) {
            redisService.delete(RedisConstant.br_order_orgDept_orgDeptId.concat(orgDeptId));
        }
        return 1;
    }

}
