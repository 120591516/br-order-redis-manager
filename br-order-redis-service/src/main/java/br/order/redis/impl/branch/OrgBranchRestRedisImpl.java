package br.order.redis.impl.branch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.branch.OrganizationBranchRest;
import br.crm.service.branch.OrgRestService;
import br.order.redis.branch.OrgBranchRestRedis;
import br.order.redis.redis.RedisService;

/**
 * 门店休息日()
 * 
 * @ClassName: OrgBranchRestRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年2月8日 下午3:16:57
 */
@Service
public class OrgBranchRestRedisImpl implements OrgBranchRestRedis {
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
    private OrgRestService orgRestService;

    /**
     * <p>Title:initData</p> 
     * <p>Description:初始化门店休息日 </p> 
     * @see br.order.redis.branch.OrgBranchRestRedis#initData()
     */
    @Override
    public void initData() {
        List<OrganizationBranchRest> list = orgRestService.getOrgBranchRestList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationBranchRest orgBranchRest : list) {
                //初始化休息日单表信息
                redisService.set(RedisConstant.br_order_orgBranchRest_restid.concat(orgBranchRest.getBranchRestId()), JSONObject.toJSONString(orgBranchRest));
                List<String> branchRestList = new ArrayList<String>();
                if (null != redisService.get(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId()))) {
                    branchRestList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId())), String.class);
                }
                if (!branchRestList.contains(orgBranchRest.getBranchRestId())) {
                    branchRestList.add(orgBranchRest.getBranchRestId());
                }
                redisService.set(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId()), JSONObject.toJSONString(branchRestList));
            }
        }
    }

    @Override
    public int setOrgBranchRest(OrganizationBranchRest orgBranchRest) {
        redisService.set(RedisConstant.br_order_orgBranchRest_restid.concat(orgBranchRest.getBranchRestId()), JSONObject.toJSONString(orgBranchRest));
        List<String> branchRestList = new ArrayList<String>();
        if (null != redisService.get(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId()))) {
            branchRestList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId())), String.class);
        }
        if (!branchRestList.contains(orgBranchRest.getBranchRestId())) {
            branchRestList.add(orgBranchRest.getBranchRestId());
        }
        redisService.set(RedisConstant.br_order_orgBranchRest_banchid.concat(orgBranchRest.getBranchId()), JSONObject.toJSONString(branchRestList));
        return 1;
    }

    @Override
    public OrganizationBranchRest getOrgBranchRest(String branchRestId) {
        OrganizationBranchRest organizationBranchRest = null;
        if (redisService.exists(RedisConstant.br_order_orgBranchRest_restid.concat(branchRestId))) {
            organizationBranchRest = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgBranchRest_restid.concat(branchRestId)), OrganizationBranchRest.class);
        }
        return organizationBranchRest;
    }

    @Override
    public int deleteOrgBranchRest(String branchRestId) {
        if (redisService.exists(RedisConstant.br_order_orgBranchRest_restid.concat(branchRestId))) {
            redisService.delete(RedisConstant.br_order_orgBranchRest_restid.concat(branchRestId));
        }
        return 1;
    }

    @Override
    public List<String> getBranchRestByBranch(String branchId) {
        List<String> list = null;
        if (redisService.exists(RedisConstant.br_order_orgBranchRest_banchid + branchId)) {
            list = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgBranchRest_banchid + branchId), String.class);
        }
        return list;
    }

}
