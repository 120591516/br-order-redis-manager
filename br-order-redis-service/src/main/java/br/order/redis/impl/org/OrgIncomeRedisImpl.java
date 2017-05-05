package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationIncome;
import br.crm.service.org.OrgIncomeService;
import br.order.redis.org.OrgIncomeRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构体检信息redis)
 * 
 * @ClassName: OrgIncomeRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午4:47:42
 */
@Service
public class OrgIncomeRedisImpl implements OrgIncomeRedis {
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
    private OrgIncomeService orgIncomeService;

    @Override
    public void initData() {
        List<OrganizationIncome> list = orgIncomeService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationIncome organizationIncome : list) {
                redisService.set(RedisConstant.br_order_orgIncome_orgIncomeId.concat(organizationIncome.getOrgIncomeId().toString()), JSONObject.toJSONString(organizationIncome));
            }
        }
    }

    @Override
    public int setOrgIncome(OrganizationIncome organizationIncome) {
        redisService.set(RedisConstant.br_order_orgIncome_orgIncomeId.concat(organizationIncome.getOrgIncomeId().toString()), JSONObject.toJSONString(organizationIncome));
        return 1;
    }

    @Override
    public OrganizationIncome getOrgIncome(Long orgIncomeId) {
        OrganizationIncome organizationIncome = null;
        if (redisService.exists(RedisConstant.br_order_orgIncome_orgIncomeId.concat(orgIncomeId.toString()))) {
            organizationIncome = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgIncome_orgIncomeId.concat(orgIncomeId.toString())), OrganizationIncome.class);
        }
        return organizationIncome;
    }

    @Override
    public int deleteOrgIncome(Long orgIncomeId) {
        if (redisService.exists(RedisConstant.br_order_orgIncome_orgIncomeId.concat(orgIncomeId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgIncome_orgIncomeId.concat(orgIncomeId.toString()));
        }
        return 1;
    }

}
