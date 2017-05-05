package br.order.redis.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictDeptType;
import br.crm.service.dict.DictDeptTypeService;
import br.order.redis.dict.DictDeptTypeRedis;
import br.order.redis.redis.RedisService;

/**
 * (部门类型字典表redis)
 * 
 * @ClassName: DictDeptTypeRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月10日 上午11:54:55
 */
@Service
public class DictDeptTypeRedisImpl implements DictDeptTypeRedis {
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
    private DictDeptTypeService dictDeptTypeService;

    @Override
    public void initData() {
        List<DictDeptType> list = dictDeptTypeService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictDeptType dictDeptType : list) {
                redisService.set(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptType.getDictDeptTypeId()), JSONObject.toJSONString(dictDeptType));
            }
        }
    }

    @Override
    public DictDeptType getDictDeptType(String dictDeptTypeId) {
        DictDeptType dictDeptType = null;
        if (redisService.exists(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptTypeId))) {
            dictDeptType = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptTypeId)), DictDeptType.class);
        }
        return dictDeptType;
    }

    @Override
    public int setDictDeptType(DictDeptType dictDeptType) {
        redisService.set(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptType.getDictDeptTypeId()), JSONObject.toJSONString(dictDeptType));
        return 1;
    }

    @Override
    public int deleteDictDeptType(String dictDeptTypeId) {
        if (redisService.exists(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptTypeId))) {
            redisService.delete(RedisConstant.br_order_dict_dictDeptType_id.concat(dictDeptTypeId));
        }
        return 1;
    }

}
