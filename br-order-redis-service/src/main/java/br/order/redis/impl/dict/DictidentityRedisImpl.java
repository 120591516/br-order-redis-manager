package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictidentity;
import br.crm.service.dict.DictidentityService;
import br.order.redis.dict.DictidentityRedis;
import br.order.redis.redis.RedisService;

/**
 * (身份字典表redis)
 * 
 * @ClassName: DictidentityRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月10日 下午3:19:13
 */
@Service
public class DictidentityRedisImpl implements DictidentityRedis {
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
    private DictidentityService dictidentityService;

    @Override
    public void initData() {
        List<Dictidentity> list = dictidentityService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictidentity dictidentity : list) {
                redisService.set(RedisConstant.br_order_dict_identity_identityId + dictidentity.getIdIdentity(), JSONObject.toJSONString(dictidentity));
            }

        }
    }

    @Override
    public Dictidentity getDictidentity(Integer idIdentity) {
        Dictidentity dictidentity = null;
        if (redisService.exists(RedisConstant.br_order_dict_identity_identityId.concat(idIdentity.toString()))) {
            dictidentity = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_identity_identityId.concat(idIdentity.toString())), Dictidentity.class);
        }
        return dictidentity;
    }

    @Override
    public int setDictidentity(Dictidentity dictidentity) {
        redisService.set(RedisConstant.br_order_dict_identity_identityId.concat(dictidentity.getIdIdentity().toString()), JSONObject.toJSONString(dictidentity));
        return 1;
    }

    @Override
    public int deleteDictidentity(Integer idIdentity) {
        if (redisService.exists(RedisConstant.br_order_dict_identity_identityId.concat(idIdentity.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_identity_identityId.concat(idIdentity.toString()));
        }
        return 1;
    }

}
