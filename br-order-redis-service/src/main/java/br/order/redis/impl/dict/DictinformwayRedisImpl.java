package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictinformway;
import br.crm.service.dict.DictinFormWayService;
import br.order.redis.dict.DictinformwayRedis;
import br.order.redis.redis.RedisService;

/**
 * (通知方式字典表redis)
 * 
 * @ClassName: DictinformwayRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午10:07:25
 */
@Service
public class DictinformwayRedisImpl implements DictinformwayRedis {
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
    private DictinFormWayService dictinFormWayService;

    @Override
    public void initData() {
        List<Dictinformway> list = dictinFormWayService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictinformway dictinformway : list) {
                redisService.set(RedisConstant.br_order_dict_informWay_informWayId + dictinformway.getIdInformway(), JSONObject.toJSONString(dictinformway));
            }

        }
    }

    @Override
    public Dictinformway getDictinformway(Integer idInformway) {
        Dictinformway dictinformway = null;
        if (redisService.exists(RedisConstant.br_order_dict_informWay_informWayId.concat(idInformway.toString()))) {
            dictinformway = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_informWay_informWayId.concat(idInformway.toString())), Dictinformway.class);
        }
        return dictinformway;
    }

    @Override
    public int setDictinformway(Dictinformway dictinformway) {
        redisService.set(RedisConstant.br_order_dict_informWay_informWayId.concat(dictinformway.getIdInformway().toString()), JSONObject.toJSONString(dictinformway));
        return 1;
    }

    @Override
    public int deleteDictinformway(Integer idInformway) {
        if (redisService.exists(RedisConstant.br_order_dict_informWay_informWayId.concat(idInformway.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_informWay_informWayId.concat(idInformway.toString()));
        }
        return 0;
    }

}
