package br.order.redis.impl.examitemvalue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examitemvalue.OrganizationExamItemValue;
import br.crm.service.examitemvalue.OrgExamItemValueService;
import br.crm.vo.examitemvalue.OrgExamItemValueVo;
import br.order.redis.examitem.OrgExamItemRedis;
import br.order.redis.examitemvalue.OrgExamItemValueRedis;
import br.order.redis.redis.RedisService;

@Service
public class OrgExamItemValueRedisImpl implements OrgExamItemValueRedis {

    @Autowired
    private OrgExamItemValueService orgExamItemValueService;;

    @Autowired
    @Qualifier("RedisInnerService")
    private RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void initData() {
        List<OrganizationExamItemValue> list = orgExamItemValueService.getAllOrganizationExamItemValue();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationExamItemValue organizationExamItemValue : list) {
                redisService.set(
                        RedisConstant.br_order_orgExamItemValue_id
                                .concat(organizationExamItemValue.getExamItemValueId()),
                        JSONObject.toJSONString(organizationExamItemValue));
            }
        }

    }

    @Override
    public OrganizationExamItemValue getOrgExamItemValueById(String orgExamItemValueId) {
        return JsonUtils.jsonToPojo(
                redisService.get(RedisConstant.br_order_orgExamItemValue_id.concat(orgExamItemValueId)),
                OrganizationExamItemValue.class);
    }

    @Override
    public String insertOrgExamItemValue(OrganizationExamItemValue orgExamItemValue) {
        return redisService.set(
                RedisConstant.br_order_orgExamItemValue_id.concat(orgExamItemValue.getExamItemValueId()),
                JSONObject.toJSONString(orgExamItemValue));
    }

    @Override
    public String updateOrgExamItemValue(OrganizationExamItemValue orgExamItemValue) {
        return redisService.set(
                RedisConstant.br_order_orgExamItemValue_id.concat(orgExamItemValue.getExamItemValueId()),
                JSONObject.toJSONString(orgExamItemValue));
    }

}
