package br.order.redis.impl.examitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examitem.OrganizationExamItemUser;
import br.crm.service.examitem.OrgExamItemUserService;
import br.order.redis.examitem.OrgExamItemUserRedis;
import br.order.redis.redis.RedisService;

/**
 * (医生绑定检查项redis)
 * 
 * @ClassName: OrgExamItemUserRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月17日 下午2:26:54
 */
@Service
public class OrgExamItemUserRedisImpl implements OrgExamItemUserRedis {
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
    private OrgExamItemUserService orgExamItemUserService;

    @Override
    public void initData() {
        List<OrganizationExamItemUser> list = orgExamItemUserService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationExamItemUser organizationExamItemUser : list) {
                //缓存体检项和医生关联表（主键id）
                redisService.set(RedisConstant.br_order_orgExamItemUser_id.concat(organizationExamItemUser.getOrganizationExamItemUserId()), JSONObject.toJSONString(organizationExamItemUser));
                //缓存医生id
                List<String> examItemList = new ArrayList<String>();
                if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(organizationExamItemUser.getOrganizationExamItemUserId()))) {
                    examItemList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(organizationExamItemUser.getOrganizationExamItemUserId())), String.class);
                }
                if (!examItemList.contains(organizationExamItemUser.getOrganizationExamItemId())) {
                    examItemList.add(organizationExamItemUser.getOrganizationExamItemId());
                }
                redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(organizationExamItemUser.getOrganizationExamItemUserId()), JSONObject.toJSONString(examItemList));
                //缓存体检项
                List<String> userList = new ArrayList<String>();
                if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_itemId.concat(organizationExamItemUser.getOrganizationExamItemId()))) {
                    userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(organizationExamItemUser.getOrganizationExamItemId())), String.class);
                }
                if (!examItemList.contains(organizationExamItemUser.getOrganizationExamItemUserId())) {
                    examItemList.add(organizationExamItemUser.getOrganizationExamItemUserId());
                }
                redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(organizationExamItemUser.getOrganizationExamItemId()), JSONObject.toJSONString(userList));

            }

        }
    }

    @Override
    public int setOrgExamItemUser(OrganizationExamItemUser orgExamItemUser) {
        //缓存体检项和医生关联表（主键id）
        redisService.set(RedisConstant.br_order_orgExamItemUser_id.concat(orgExamItemUser.getOrganizationExamItemUserId()), JSONObject.toJSONString(orgExamItemUser));
        //缓存医生id
        List<String> examItemList = new ArrayList<String>();
        if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId()))) {
            examItemList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId())), String.class);
        }
        if (!examItemList.contains(orgExamItemUser.getOrganizationExamItemId())) {
            examItemList.add(orgExamItemUser.getOrganizationExamItemId());
        }
        redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId()), JSONObject.toJSONString(examItemList));
        //缓存体检项
        List<String> userList = new ArrayList<String>();
        if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_itemId.concat(orgExamItemUser.getOrganizationExamItemId()))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemId())), String.class);
        }
        if (!examItemList.contains(orgExamItemUser.getOrganizationExamItemUserId())) {
            examItemList.add(orgExamItemUser.getOrganizationExamItemUserId());
        }
        redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemId()), JSONObject.toJSONString(userList));
        return 1;
    }

    @Override
    public OrganizationExamItemUser getOrgExamItemUser(String orgExamItemUserId) {

        return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamItem_id.concat(orgExamItemUserId)), OrganizationExamItemUser.class);
    }

    @Override
    public int deleteOrgExamItemUser(String orgExamItemUserId) {
        OrganizationExamItemUser orgExamItemUser = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamItemUser_id.concat(orgExamItemUserId)), OrganizationExamItemUser.class);
        redisService.delete(RedisConstant.br_order_orgExamItemUser_id.concat(orgExamItemUserId));
        List<String> examItemList = new ArrayList<String>();
        if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId()))) {
            examItemList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId())), String.class);
        }
        if (examItemList.contains(orgExamItemUser.getOrganizationExamItemId())) {
            examItemList.remove(orgExamItemUser.getOrganizationExamItemId());
        }
        redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemUserId()), JSONObject.toJSONString(examItemList));
        //缓存体检项
        List<String> userList = new ArrayList<String>();
        if (null != redisService.get(RedisConstant.br_order_orgExamItemUser_itemId.concat(orgExamItemUser.getOrganizationExamItemId()))) {
            userList = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemId())), String.class);
        }
        if (!examItemList.contains(orgExamItemUser.getOrganizationExamItemUserId())) {
            examItemList.remove(orgExamItemUser.getOrganizationExamItemUserId());
        }
        redisService.set(RedisConstant.br_order_orgExamItemUser_userId.concat(orgExamItemUser.getOrganizationExamItemId()), JSONObject.toJSONString(userList));
        return 1;
    }

    @Override
    public List<String> getUserListByItem(String itemId) {
        return JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(itemId)), String.class);
    }

    @Override
    public List<String> getItemListByUser(String userId) {
        return JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgExamItemUser_userId.concat(userId)), String.class);
    }

}
