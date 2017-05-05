package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictNation;
import br.crm.service.dict.DictNationService;
import br.order.redis.dict.DictNationRedis;
import br.order.redis.redis.RedisService;

/**
 * (民族字典表redis)
 * 
 * @ClassName: DictNationRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午11:45:38
 */
@Service
public class DictNationRedisImpl implements DictNationRedis {
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
    private DictNationService dictNationService;

    @Override
    public void initData() {
        List<DictNation> list = dictNationService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictNation dictNation : list) {
                redisService.set(RedisConstant.br_order_dict_nation_id + dictNation.getId(), JSONObject.toJSONString(dictNation));
            }

        }
    }

    @Override
    public int setDictNation(DictNation dictNation) {
        redisService.set(RedisConstant.br_order_dict_nation_id.concat(dictNation.getId().toString()), JSONObject.toJSONString(dictNation));
        return 1;
    }

    @Override
    public DictNation getDictNation(Integer id) {
        DictNation dictNation = null;
        if (redisService.exists(RedisConstant.br_order_dict_nation_id.concat(id.toString()))) {
            dictNation = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_nation_id.concat(id.toString())), DictNation.class);
        }
        return dictNation;
    }

    @Override
    public int deleteDictNation(Integer id) {
        if (redisService.exists(RedisConstant.br_order_dict_nation_id.concat(id.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_nation_id.concat(id.toString()));
        }
        return 0;
    }

}
