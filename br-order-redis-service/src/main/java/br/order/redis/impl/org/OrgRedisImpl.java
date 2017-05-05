package br.order.redis.impl.org;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.Organization;
import br.crm.service.org.OrganizationService;
import br.crm.vo.org.RegistOrgInfoVo;
import br.order.redis.org.OrgRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构redis)
 * 
 * @ClassName: OrgRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午4:49:01
 */
@Service
public class OrgRedisImpl implements OrgRedis {
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
    private OrganizationService organizationService;

    /**
     * <p>Title:initData</p> 
     * <p>Description: 初始化体检机构缓存</p> 
     * @see br.order.redis.org.OrgRedis#initData()
     */
    @Override
    public void initData() {
        List<Organization> list = organizationService.getAvailableOrgs();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Organization organization : list) {
                // 体检机构表（主键id）
                if (!redisService.exists(RedisConstant.br_order_organization_id.concat(organization.getOrgId()))) {
                    redisService.set(RedisConstant.br_order_organization_id.concat(organization.getOrgId()), JSONObject.toJSONString(organization));
                }
                // 机构表-等级id
                List<String> orglist = new ArrayList<String>();
                if (redisService.exists(RedisConstant.org_level_orglevelid.concat(organization.getOrgLevelId().toString()))) {
                    orglist = JSONObject.parseArray(redisService.get(RedisConstant.org_level_orglevelid.concat(organization.getOrgLevelId().toString())), String.class);
                }
                if (!orglist.contains(organization.getOrgId())) {
                    orglist.add(organization.getOrgId());
                }
                redisService.set(RedisConstant.org_level_orglevelid.concat(organization.getOrgLevelId().toString()), JSONObject.toJSONString(orglist));

            }
        }
    }

    /**
     * <p>Title:getOrganization</p> 
     * <p>Description:根据机构id获取机构缓存数据 </p> 
     * @param orgId 机构id
     * @return
     * @see br.order.redis.org.OrgRedis#getOrganization(java.lang.String)
     */
    @Override
    public RegistOrgInfoVo getOrganization(String orgId) {
        RegistOrgInfoVo org = null;
        if (redisService.exists(RedisConstant.br_order_organization_id.concat(orgId))) {
            org = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_organization_id.concat(orgId)), RegistOrgInfoVo.class);
        }
        return org;
    }

    /**
     * <p>Title:setOrganization</p> 
     * <p>Description:插入缓存 </p> 
     * @param organization
     * @return
     * @see br.order.redis.org.OrgRedis#setOrganization(br.crm.pojo.org.OrganizationVo)
     */
    @Override
    public String setOrganization(RegistOrgInfoVo organization) {
        redisService.set(RedisConstant.br_order_organization_id.concat(organization.getOrgId()), JSONObject.toJSONString(organization));
        return organization.getOrgId();
    }

    /**
     * <p>Title:deleteOrganization</p> 
     * <p>Description: 删除缓存 </p> 
     * @param orgId
     * @return
     * @see br.order.redis.org.OrgRedis#deleteOrganization(java.lang.String)
     */
    @Override
    public int deleteOrganization(String orgId) {
        if (redisService.exists(RedisConstant.br_order_organization_id.concat(orgId))) {
            redisService.delete(RedisConstant.br_order_organization_id.concat(orgId));
        }
        return 0;
    }

    /**
     * <p>Title:redisExists</p> 
     * <p>Description:根据缓存key判断是否存在缓存 </p> 
     * @param orgId 
     * @return
     * @see br.order.redis.org.OrgRedis#redisExists(java.lang.String)
     */
    @Override
    public Boolean redisExists(String orgId) {
        return redisService.exists(RedisConstant.br_order_organization_id.concat(orgId));
    }

}
