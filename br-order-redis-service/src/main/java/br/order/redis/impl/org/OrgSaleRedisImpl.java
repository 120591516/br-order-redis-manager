package br.order.redis.impl.org;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.OrganizationSale;
import br.crm.service.org.OrgSaleService;
import br.order.redis.org.OrgSaleRedis;
import br.order.redis.redis.RedisService;

/**
 * (体检机构销售redis)
 * 
 * @ClassName: OrgSaleRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午5:32:46
 */
@Service
public class OrgSaleRedisImpl implements OrgSaleRedis {
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
    private OrgSaleService orgSaleService;

    @Override
    public void initData() {
        List<OrganizationSale> list = orgSaleService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationSale organizationSale : list) {
                redisService.set(RedisConstant.br_order_orgSale_orgSaleId.concat(organizationSale.getOrgSaleId().toString()), JSONObject.toJSONString(organizationSale));
            }
        }

    }

    @Override
    public int setOrganizationSale(OrganizationSale organizationSale) {
        redisService.set(RedisConstant.br_order_orgSale_orgSaleId.concat(organizationSale.getOrgSaleId().toString()), JSONObject.toJSONString(organizationSale));
        return 1;
    }

    @Override
    public OrganizationSale getOrganizationSale(Long orgSaleId) {
        OrganizationSale organizationSale = null;
        if (redisService.exists(RedisConstant.br_order_orgSale_orgSaleId.concat(orgSaleId.toString()))) {
            organizationSale = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgSale_orgSaleId.concat(orgSaleId.toString())), OrganizationSale.class);
        }
        return organizationSale;
    }

    @Override
    public int deleteOrganizationSale(Long orgSaleId) {
        if (redisService.exists(RedisConstant.br_order_orgSale_orgSaleId.concat(orgSaleId.toString()))) {
            redisService.delete(RedisConstant.br_order_orgSale_orgSaleId.concat(orgSaleId.toString()));
        }
        return 1;
    }

}
