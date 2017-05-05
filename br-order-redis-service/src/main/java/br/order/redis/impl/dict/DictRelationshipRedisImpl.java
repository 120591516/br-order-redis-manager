package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.order.redis.dict.DictRelationshipRedis;
import br.order.redis.redis.RedisService;
import br.order.user.pojo.dict.DictRelationship;
import br.order.user.service.dict.DictRelationshipService;

@Service
public class DictRelationshipRedisImpl implements DictRelationshipRedis {
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
    private DictRelationshipService dictRelationshipService;

    @Override
    public void initData() {
        List<DictRelationship> list = dictRelationshipService.cusInfoListByStatus();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictRelationship dictRelationship : list) {
                redisService.set(RedisConstant.br_order_dict_relationShip_id + dictRelationship.getDictRelationId(), JSONObject.toJSONString(dictRelationship));
            }
        }
    }

    @Override
    public int setDictRelationship(DictRelationship dictRelationship) {
        redisService.set(RedisConstant.br_order_dict_relationShip_id + dictRelationship.getDictRelationId(), JSONObject.toJSONString(dictRelationship));
        return 1;
    }

    @Override
    public DictRelationship getDictRelationship(Long dictRelationId) {
        DictRelationship dictRelationship = null;
        if (redisService.exists(RedisConstant.br_order_dict_relationShip_id + dictRelationId)) {
            dictRelationship = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_relationShip_id + dictRelationId), DictRelationship.class);
        }
        return dictRelationship;
    }

    @Override
    public int deleteDictRelationship(Long dictRelationId) {
        if (redisService.exists(RedisConstant.br_order_dict_relationShip_id + dictRelationId)) {
            redisService.delete(RedisConstant.br_order_dict_relationShip_id + dictRelationId);
        }
        return 1;
    }

}
