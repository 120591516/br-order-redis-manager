package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictjobclass;
import br.crm.service.dict.DictJobClassService;
import br.order.redis.dict.DictjobclassRedis;
import br.order.redis.redis.RedisService;

/**
 * (工作类型字典表redis)
 * 
 * @ClassName: DictjobclassRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午10:50:37
 */
@Service
public class DictjobclassRedisImpl implements DictjobclassRedis {
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
    private DictJobClassService dictJobClassService;

    @Override
    public void initData() {
        List<Dictjobclass> list = dictJobClassService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictjobclass dictjobclass : list) {
                redisService.set(RedisConstant.br_order_dict_jobClass_jobClassId + dictjobclass.getIdJobclass(), JSONObject.toJSONString(dictjobclass));
            }
        }
    }

    @Override
    public int setDictjobclass(Dictjobclass dictjobclass) {
        redisService.set(RedisConstant.br_order_dict_jobClass_jobClassId.concat(dictjobclass.getIdJobclass() + ""), JSONObject.toJSONString(dictjobclass));
        return 1;
    }

    @Override
    public Dictjobclass getDictjobclass(Long idJobclass) {
        Dictjobclass dictjobclass = null;
        if (redisService.exists(RedisConstant.br_order_dict_jobClass_jobClassId.concat(idJobclass + ""))) {
            return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_jobClass_jobClassId.concat(idJobclass + "")), Dictjobclass.class);
        }
        return dictjobclass;
    }

    @Override
    public int deleteDictjobclass(Long idJobclass) {
        if (redisService.exists(RedisConstant.br_order_dict_jobClass_jobClassId.concat(idJobclass + ""))) {
            redisService.delete(RedisConstant.br_order_dict_jobClass_jobClassId.concat(idJobclass + ""));
        }
        return 1;
    }

}
