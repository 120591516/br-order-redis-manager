package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationLevel;
import br.crm.service.org.OrgLevelService;
import br.order.redis.org.OrgLevelRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构等级redis)
 * 
 * @ClassName: OrgLevelRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午4:48:45
 */
@Service
public class OrgLevelRedisImpl implements OrgLevelRedis {
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
    private OrgLevelService orgLevelService;

    /**
     * <p>Title:initData</p> 
     * <p>Description:初始化体检机构等级缓存 </p> 
     * @see br.order.redis.org.OrgLevelRedis#initData()
     */
    @Override
    public void initData() {
        List<OrganizationLevel> list = orgLevelService.getAllOrgLevel();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationLevel organizationLevel : list) {
                redisService.set(RedisConstant.br_order_orgLevel_orgLevelId.concat(organizationLevel.getOrgLevelId().toString()), JSONObject.toJSONString(organizationLevel));
            }
        }
    }

    @Override
    public OrganizationLevel getOrganizationLevel(String orgLevelId) {
        OrganizationLevel organizationLevel = null;
        if (redisService.exists(RedisConstant.br_order_orgLevel_orgLevelId.concat(orgLevelId))) {
            organizationLevel = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgLevel_orgLevelId.concat(orgLevelId)), OrganizationLevel.class);
        }
        return organizationLevel;
    }

    @Override
    public int setOrganizationLevel(OrganizationLevel organizationLevel) {
        redisService.set(RedisConstant.br_order_orgLevel_orgLevelId.concat(organizationLevel.getOrgLevelId().toString()), JSONObject.toJSONString(organizationLevel));
        return 1;
    }

}
