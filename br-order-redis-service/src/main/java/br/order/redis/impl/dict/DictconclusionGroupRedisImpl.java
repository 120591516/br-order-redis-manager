package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictconclusiongroup;
import br.crm.service.dict.DictconclusionGroupService;
import br.order.redis.dict.DictconclusionGroupRedis;
import br.order.redis.redis.RedisService;

public class DictconclusionGroupRedisImpl implements DictconclusionGroupRedis {
    @Autowired
    private DictconclusionGroupService dictconclusionGroupService;

    @Autowired
    @Qualifier("RedisInnerService")
    private RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void initData() {
        List<Dictconclusiongroup> list = dictconclusionGroupService.getAllDictconclusionGroup();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictconclusiongroup dictconclusiongroup : list) {
                redisService.set(RedisConstant.br_order_dict_dictconclusiongroup_id.concat(dictconclusiongroup.getKeyconclusiongroupid()), JSONObject.toJSONString(dictconclusiongroup));
            }
        }
    }

    @Override
    public String addConclusionGroup(Dictconclusiongroup dictconclusiongroup) {

        return redisService.set(RedisConstant.br_order_dict_dictconclusiongroup_id.concat(dictconclusiongroup.getKeyconclusiongroupid()), JSONObject.toJSONString(dictconclusiongroup));
    }

    @Override
    public Dictconclusiongroup getConclusionGroupById(String keyconclusiongroupid) {
        return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_dictconclusiongroup_id.concat(keyconclusiongroupid)), Dictconclusiongroup.class);
    }

    @Override
    public String updateConclusionGroup(Dictconclusiongroup dictconclusiongroup) {
        return redisService.set(RedisConstant.br_order_dict_dictconclusiongroup_id.concat(dictconclusiongroup.getKeyconclusiongroupid()), JSONObject.toJSONString(dictconclusiongroup));
    }

}
