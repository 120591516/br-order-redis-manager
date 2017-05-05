package br.order.redis.impl.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.permission.OrganizationUser;
import br.crm.service.permission.OrganizationUserService;
import br.order.redis.redis.RedisService;
import br.order.redis.user.OrgUserRedis;

@Service
public class OrgUserRedisImpl implements OrgUserRedis {
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
    private OrganizationUserService organizationUserService;

    @Override
    public void initData() {
        List<OrganizationUser> list = organizationUserService.getOrgUserAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationUser organizationUser : list) {
                // 缓存用户表的主键id
                redisService.set(RedisConstant.br_order_orgUser_userId.concat(organizationUser.getUserId()), JSONObject.toJSONString(organizationUser));
                // 缓存体检机构id
                List<String> userList = new ArrayList<String>();
                if (redisService.exists(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()), JSONObject.toJSONString(userList));

                // 缓存门店id
                userList.clear();
                if (redisService.exists(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()), JSONObject.toJSONString(userList));
                //缓存科室id
                userList.clear();
                if (redisService.exists(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()), JSONObject.toJSONString(userList));
            }
        }
    }

    @Override
    public int setOrgUser(OrganizationUser organizationUser) {
        if (null != organizationUser) {
            redisService.set(RedisConstant.br_order_orgUser_userId.concat(organizationUser.getUserId()), JSONObject.toJSONString(organizationUser));
            List<String> userList = new ArrayList<String>();
            // 缓存体检机构id
            if (organizationUser.getOrgId() != null) {
                if (redisService.exists(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()), JSONObject.toJSONString(userList));
            }
            if (null != organizationUser.getOrgBranchId()) {
                userList.clear();
                // 缓存门店id
                if (redisService.exists(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()), JSONObject.toJSONString(userList));

            }
            if (null != organizationUser.getOrgBranchDeptId()) {
                userList.clear();
                //缓存科室id
                if (redisService.exists(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId())), String.class);
                }
                if (!userList.contains(organizationUser.getUserId())) {
                    userList.add(organizationUser.getUserId());
                }
                redisService.set(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()), JSONObject.toJSONString(userList));

            }

        }
        return 1;
    }

    @Override
    public OrganizationUser getOrgUser(String userId) {
        return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgUser_userId.concat(userId)), OrganizationUser.class);
    }

    @Override
    public int deleteOrgUser(String userId) {
        OrganizationUser organizationUser = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgUser_userId.concat(userId)), OrganizationUser.class);
        redisService.delete(RedisConstant.br_order_orgUser_userId.concat(userId));
        // 缓存体检机构id
        List<String> userList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId())), String.class);
        }
        if (userList.contains(organizationUser.getUserId())) {
            userList.remove(organizationUser.getUserId());
        }
        redisService.set(RedisConstant.br_order_orgUser_orgId.concat(organizationUser.getOrgId()), JSONObject.toJSONString(userList));

        // 缓存门店id
        if (redisService.exists(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId())), String.class);
        }
        if (userList.contains(organizationUser.getUserId())) {
            userList.remove(organizationUser.getUserId());
        }
        redisService.set(RedisConstant.br_order_orgUser_orgBranchId.concat(organizationUser.getOrgBranchId()), JSONObject.toJSONString(userList));
        //缓存科室id
        // 缓存门店id
        if (redisService.exists(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId())), String.class);
        }
        if (userList.contains(organizationUser.getUserId())) {
            userList.remove(organizationUser.getUserId());
        }
        redisService.set(RedisConstant.br_order_orgUser_orgDeptId.concat(organizationUser.getOrgBranchDeptId()), JSONObject.toJSONString(userList));
        return 1;
    }

    /**
     * <p>Title:getOrgUserListByOrg</p> 
     * <p>Description:根据机构id获取用户集合 </p> 
     * @param orgId
     * @return
     * @see br.order.redis.user.OrgUserRedis#getOrgUserListByOrg(java.lang.String)
     */
    @Override
    public List<String> getOrgUserListByOrg(String orgId) {
        List<String> userList = null;
        if (redisService.exists(RedisConstant.br_order_orgUser_orgId.concat(orgId))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgId.concat(orgId)), String.class);
        }
        return userList;
    }

    /**
     * <p>Title:getOrgUserListByBranch</p> 
     * <p>Description: 获取门店下的用户</p> 
     * @param branchId
     * @return
     * @see br.order.redis.user.OrgUserRedis#getOrgUserListByBranch(java.lang.String)
     */
    @Override
    public List<String> getOrgUserListByBranch(String branchId) {
        List<String> userList = null;
        if (redisService.exists(RedisConstant.br_order_orgUser_orgBranchId.concat(branchId))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgBranchId.concat(branchId)), String.class);
        }
        return userList;
    }

    /**
     * <p>Title:getOrgUserListByDept</p> 
     * <p>Description:获取科室下的用户 </p> 
     * @param deptId
     * @return
     * @see br.order.redis.user.OrgUserRedis#getOrgUserListByDept(java.lang.String)
     */
    @Override
    public List<String> getOrgUserListByDept(String deptId) {
        List<String> userList = null;
        if (redisService.exists(RedisConstant.br_order_orgUser_orgDeptId.concat(deptId))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgUser_orgDeptId.concat(deptId)), String.class);
        }
        return userList;
    }

}
