package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.DictHighIncidenceDisease;
import br.crm.service.dict.DictHighIncidenceDiseaseService;
import br.order.redis.dict.DictHighIncidenceDiseaseRedis;
import br.order.redis.redis.RedisService;

/**
 * (高发疾病字典表redis)
 * 
 * @ClassName: DictHighIncidenceDiseaseRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月10日 下午2:46:14
 */
@Service
public class DictHighIncidenceDiseaseRedisImpl implements DictHighIncidenceDiseaseRedis {
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
    private DictHighIncidenceDiseaseService dictHighIncidenceDiseaseService;

    @Override
    public void initData() {
        List<DictHighIncidenceDisease> list = dictHighIncidenceDiseaseService.getHighIncidenceDiseases();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictHighIncidenceDisease dictHighIncidenceDisease : list) {
                redisService.set(RedisConstant.br_order_dict_hid_id + dictHighIncidenceDisease.getHighIncidenceDiseaseId(), JSONObject.toJSONString(dictHighIncidenceDisease));
            }

        }
    }

    @Override
    public DictHighIncidenceDisease getDictHighIncidenceDisease(String highIncidenceDiseaseId) {
        DictHighIncidenceDisease dictHighIncidenceDisease = null;
        if (redisService.exists(RedisConstant.br_order_dict_hid_id.concat(highIncidenceDiseaseId))) {
            dictHighIncidenceDisease = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_hid_id.concat(highIncidenceDiseaseId)), DictHighIncidenceDisease.class);
        }
        return dictHighIncidenceDisease;
    }

    @Override
    public int setDictHighIncidenceDisease(DictHighIncidenceDisease dictHighIncidenceDisease) {
        redisService.set(RedisConstant.br_order_dict_hid_id.concat(dictHighIncidenceDisease.getHighIncidenceDiseaseId()), JSONObject.toJSONString(dictHighIncidenceDisease));
        return 1;
    }

    @Override
    public int deleteDictHighIncidenceDisease(String highIncidenceDiseaseId) {
        if (redisService.exists(RedisConstant.br_order_dict_hid_id.concat(highIncidenceDiseaseId))) {
            redisService.delete(RedisConstant.br_order_dict_hid_id.concat(highIncidenceDiseaseId));
        }
        return 1;
    }

}
