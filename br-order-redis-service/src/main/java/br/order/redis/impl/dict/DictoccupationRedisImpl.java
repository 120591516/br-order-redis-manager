package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictoccupation;
import br.crm.service.dict.DictOccupationService;
import br.order.redis.dict.DictoccupationRedis;
import br.order.redis.redis.RedisService;

/**
 * (职业字典表redis)
 * 
 * @ClassName: DictoccupationRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午11:45:33
 */
@Service
public class DictoccupationRedisImpl implements DictoccupationRedis {
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
    private DictOccupationService dictOccupationService;

    @Override
    public void initData() {
        List<Dictoccupation> list = dictOccupationService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictoccupation dictoccupation : list) {
                redisService.set(RedisConstant.br_order_dict_occupation_occupationId + dictoccupation.getIdOccupation(), JSONObject.toJSONString(dictoccupation));
            }
        }
    }

    @Override
    public int setDictoccupation(Dictoccupation dictoccupation) {
        redisService.set(RedisConstant.br_order_dict_occupation_occupationId.concat(dictoccupation.getIdOccupation().toString()), JSONObject.toJSONString(dictoccupation));
        return 1;
    }

    @Override
    public Dictoccupation getDictoccupation(Integer idOccupation) {
        Dictoccupation dictoccupation = null;
        if (redisService.exists(RedisConstant.br_order_dict_occupation_occupationId.concat(idOccupation.toString()))) {
            dictoccupation = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_occupation_occupationId.concat(idOccupation.toString())), Dictoccupation.class);
        }
        return dictoccupation;
    }

    @Override
    public int deleteDictoccupation(Integer idOccupation) {
        if (redisService.exists(RedisConstant.br_order_dict_occupation_occupationId.concat(idOccupation.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_occupation_occupationId.concat(idOccupation.toString()));
        }
        return 1;
    }

}
