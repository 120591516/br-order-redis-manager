package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictmarriage;
import br.crm.service.dict.DictMarriageService;
import br.order.redis.dict.DictmarriageRedis;
import br.order.redis.redis.RedisService;

/**
 * (婚姻字典表redis)
 * 
 * @ClassName: DictmarriageRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午11:04:51
 */
@Service
public class DictmarriageRedisImpl implements DictmarriageRedis {
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
    private DictMarriageService dictMarriageService;

    @Override
    public void initData() {
        List<Dictmarriage> list = dictMarriageService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictmarriage dictmarriage : list) {
                redisService.set(RedisConstant.br_order_dict_marriage_id + dictmarriage.getIdMarriage(), JSONObject.toJSONString(dictmarriage));
            }

        }
    }

    @Override
    public int setDictmarriage(Dictmarriage dictmarriage) {
        redisService.set(RedisConstant.br_order_dict_marriage_id.concat(dictmarriage.getIdMarriage().toString()), JSONObject.toJSONString(dictmarriage));
        return 1;
    }

    @Override
    public Dictmarriage getDictmarriage(Integer idMarriage) {
        Dictmarriage dictmarriage = null;
        if (redisService.exists(RedisConstant.br_order_dict_marriage_id.concat(idMarriage.toString()))) {
            dictmarriage = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_marriage_id.concat(idMarriage.toString())), Dictmarriage.class);
        }
        return dictmarriage;
    }

    @Override
    public int deleteDictmarriage(Integer idMarriage) {
        if (redisService.exists(RedisConstant.br_order_dict_marriage_id.concat(idMarriage.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_marriage_id.concat(idMarriage.toString()));
        }
        return 1;
    }

}
