package br.order.redis.impl.branch;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.branch.OrganizationBranch;
import br.crm.service.branch.OrgBranchService;
import br.order.redis.branch.OrgBranchRedis;
import br.order.redis.redis.RedisService;

/**
 * 机构门店redis()
 * 
 * @ClassName: OrgBranchRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年2月8日 下午3:19:26
 */
@Service
public class OrgBranchRedisImpl implements OrgBranchRedis {
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
    private OrgBranchService orgBranchService;

    @Override
    public void initData() {
        List<OrganizationBranch> branchList = orgBranchService.getOrganizationBranchAll();
        if (CollectionUtils.isNotEmpty(branchList)) {
            for (OrganizationBranch organizationBranch : branchList) {
                // 缓存机构门店（主键id）
                redisService.set(RedisConstant.br_order_orgbranch_orgBranchId.concat(organizationBranch.getBranchId()), JSONObject.toJSONString(organizationBranch));
                // 缓存机构的id,获取门店的集合
                /*
                 * List<String> branchIdsList = new ArrayList<String>(); if
                 * (redisService.exists(RedisConstant.br_order_orgbranch_orgId.
                 * concat(organizationBranch.getOrgId()))) { branchIdsList =
                 * JSONObject.parseArray(redisService.get(RedisConstant.
                 * br_order_orgbranch_orgId.concat(organizationBranch.getOrgId()
                 * )), String.class); } if
                 * (!branchIdsList.contains(organizationBranch.getBranchId())) {
                 * branchIdsList.add(organizationBranch.getBranchId()); }
                 * redisService.set(RedisConstant.br_order_orgbranch_orgId.
                 * concat(organizationBranch.getOrgId()),
                 * JSONObject.toJSONString(branchIdsList));
                 */
            }
        }
    }

    @Override
    public int setOrgBranch(OrganizationBranch organizationBranch) {
        redisService.set(RedisConstant.br_order_orgbranch_orgBranchId.concat(organizationBranch.getBranchId()), JSONObject.toJSONString(organizationBranch));
        return 1;
    }

    @Override
    public OrganizationBranch getOrgBranch(String branchId) {
        OrganizationBranch organizationBranch = null;
        if (redisService.exists(RedisConstant.br_order_orgbranch_orgBranchId.concat(branchId))) {
            organizationBranch = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgbranch_orgBranchId.concat(branchId)), OrganizationBranch.class);
        }
        return organizationBranch;
    }

    @Override
    public int deleteOrgBranch(String branchId) {
        if (redisService.exists(RedisConstant.br_order_orgbranch_orgBranchId.concat(branchId))) {
            redisService.delete(RedisConstant.br_order_orgbranch_orgBranchId.concat(branchId));
        }
        return 1;
    }

}
