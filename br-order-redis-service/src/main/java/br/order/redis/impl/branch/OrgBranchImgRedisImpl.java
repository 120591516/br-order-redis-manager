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
import br.crm.pojo.branch.OrganizationBranchImg;
import br.crm.service.branch.OrgBranchImgService;
import br.order.redis.branch.OrgBranchImgRedis;
import br.order.redis.redis.RedisService;

@Service
public class OrgBranchImgRedisImpl implements OrgBranchImgRedis {
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
    private OrgBranchImgService orgBranchImgService;

    @Override
    public void initData() {
        List<OrganizationBranchImg> list = orgBranchImgService.getOrgBranchImg(null);
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationBranchImg organizationBranchImg : list) {
                redisService.set(RedisConstant.br_order_orgBranchimg_id.concat(organizationBranchImg.getOrgBranchImgId()), JSONObject.toJSONString(organizationBranchImg));
                List<String> branchImgList = new ArrayList<String>();
                if (null != redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(organizationBranchImg.getOrgBranchId()))) {
                    branchImgList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(organizationBranchImg.getOrgBranchId())), String.class);
                }
                if (!branchImgList.contains(organizationBranchImg.getOrgBranchId())) {
                    branchImgList.add(organizationBranchImg.getOrgBranchId());
                }
                redisService.set(RedisConstant.br_order_orgBranchimg_branchid.concat(organizationBranchImg.getOrgBranchId()), JSONObject.toJSONString(branchImgList));
            }
        }
    }

    @Override
    public int setOrgBranchImg(OrganizationBranchImg orgBranchImg) {
        if (null != orgBranchImg) {
            redisService.set(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImg.getOrgBranchImgId()), JSONObject.toJSONString(orgBranchImg));
            List<String> branchImgList = new ArrayList<String>();
            if (null != redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId()))) {
                branchImgList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId())), String.class);
            }
            if (!branchImgList.contains(orgBranchImg.getOrgBranchId())) {
                branchImgList.add(orgBranchImg.getOrgBranchId());
            }
            redisService.set(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId()), JSONObject.toJSONString(branchImgList));
        }
        return 0;
    }

    @Override
    public OrganizationBranchImg getOrgBranchImg(String orgBranchImgId) {
        OrganizationBranchImg organizationBranchImg = null;
        if (redisService.exists(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImgId))) {
            organizationBranchImg = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImgId)), OrganizationBranchImg.class);
        }
        return organizationBranchImg;
    }

    @Override
    public int deleteOrgBranchImg(String orgBranchImgId) {
        if (redisService.exists(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImgId))) {
            OrganizationBranchImg orgBranchImg = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImgId)), OrganizationBranchImg.class);
            redisService.delete(RedisConstant.br_order_orgBranchimg_id.concat(orgBranchImg.getOrgBranchImgId()));
            List<String> branchImgList = new ArrayList<String>();
            if (null != redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId()))) {
                branchImgList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId())), String.class);
            }
            if (!branchImgList.contains(orgBranchImg.getOrgBranchId())) {
                branchImgList.remove(orgBranchImg.getOrgBranchId());
            }
            redisService.set(RedisConstant.br_order_orgBranchimg_branchid.concat(orgBranchImg.getOrgBranchId()), JSONObject.toJSONString(branchImgList));
        }
        return 0;
    }

    @Override
    public List<String> getBranchImgListByBranchId(String branchId) {
        List<String> list = null;
        if (redisService.exists(RedisConstant.br_order_orgBranchimg_branchid + branchId)) {
            list = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgBranchimg_branchid + branchId), String.class);
        }
        return list;
    }

}
