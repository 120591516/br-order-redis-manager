package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictsex;
import br.crm.service.dict.DictsexService;
import br.order.redis.dict.DictsexRedis;
import br.order.redis.redis.RedisService;

@Service
public class DictsexRedisImpl implements DictsexRedis {
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
    private DictsexService dictsexService;

    @Override
    public void initData() {
        List<Dictsex> list = dictsexService.dictSexListByStatus();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictsex dictsex : list) {
                redisService.set(RedisConstant.br_order_dict_sex_sexId + dictsex.getIdSex(), JSONObject.toJSONString(dictsex));
            }
        }
    }

    @Override
    public int setDictsex(Dictsex dictsex) {
        redisService.set(RedisConstant.br_order_dict_sex_sexId.concat(String.valueOf(dictsex.getIdSex())), JSONObject.toJSONString(dictsex));
        return 1;
    }

    @Override
    public Dictsex getDictsex(Integer idSex) {
        Dictsex dictsex = null;
        if (redisService.exists(RedisConstant.br_order_dict_sex_sexId.concat(String.valueOf(idSex)))) {
            dictsex = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_sex_sexId.concat(String.valueOf(idSex))), Dictsex.class);
        }
        return dictsex;
    }

    @Override
    public int deleteDictsex(Integer idSex) {
        if (redisService.exists(RedisConstant.br_order_dict_sex_sexId.concat(String.valueOf(idSex)))) {
            redisService.delete(RedisConstant.br_order_dict_sex_sexId.concat(String.valueOf(idSex)));
        }
        return 1;
    }

}
