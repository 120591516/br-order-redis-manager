package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictpayway;
import br.crm.service.dict.DictpaywayService;
import br.order.redis.dict.DictpaywayRedis;
import br.order.redis.redis.RedisService;

/**
 * (支付方式redis)
 * 
 * @ClassName: DictpaywayRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午1:44:17
 */
@Service
public class DictpaywayRedisImpl implements DictpaywayRedis {
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
    private DictpaywayService dictpaywayService;

    @Override
    public void initData() {
        List<Dictpayway> list = dictpaywayService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictpayway dictpayway : list) {
                redisService.set(RedisConstant.br_order_dict_payWay_payWayId + dictpayway.getIdPayway(), JSONObject.toJSONString(dictpayway));
            }
        }
    }

    @Override
    public int setDictpayway(Dictpayway dictpayway) {
        redisService.set(RedisConstant.br_order_dict_payWay_payWayId.concat(dictpayway.getIdPayway() + ""), JSONObject.toJSONString(dictpayway));
        return 1;
    }

    @Override
    public Dictpayway getDictpayway(Long idPayway) {
        Dictpayway dictpayway = null;
        if (redisService.exists(RedisConstant.br_order_dict_payWay_payWayId.concat(idPayway + ""))) {
            dictpayway = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_payWay_payWayId.concat(idPayway + "")), Dictpayway.class);
        }
        return dictpayway;
    }

    @Override
    public int deleteDictpayway(Long idPayway) {
        if (redisService.exists(RedisConstant.br_order_dict_payWay_payWayId.concat(idPayway + ""))) {
            redisService.delete(RedisConstant.br_order_dict_payWay_payWayId.concat(idPayway + ""));
        }
        return 1;
    }

}
