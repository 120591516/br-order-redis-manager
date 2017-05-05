package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.org.DictExamSuiteType;
import br.crm.service.dict.DictExamSuiteTypeService;
import br.order.redis.dict.DictExamSuiteTypeRedis;
import br.order.redis.redis.RedisService;

/**
 * (套餐类型字典表redis)
 * 
 * @ClassName: DictExamSuiteTypeRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月10日 下午2:15:05
 */
@Service
public class DictExamSuiteTypeRedisImpl implements DictExamSuiteTypeRedis {
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
    private DictExamSuiteTypeService dictExamSuiteTypeService;

    /**
     * <p>Title:initData</p> 
     * <p>Description: 初始化套餐类型redis</p> 
     * @see br.order.redis.dict.DictExamSuiteTypeRedis#initData()
     */
    @Override
    public void initData() {
        List<DictExamSuiteType> list = dictExamSuiteTypeService.getExamSuiteTypes();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictExamSuiteType dictExamSuiteType : list) {
                redisService.set(RedisConstant.br_order_dict_examSuiteType_id + dictExamSuiteType.getExamTypeId(), JSONObject.toJSONString(dictExamSuiteType));
            }
        }
    }

    @Override
    public DictExamSuiteType getDictExamSuiteType(String examTypeId) {
        DictExamSuiteType dictExamSuiteType = null;
        if (redisService.exists(RedisConstant.br_order_dict_examSuiteType_id.concat(examTypeId))) {
            dictExamSuiteType = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_examSuiteType_id.concat(examTypeId)), DictExamSuiteType.class);
        }
        return dictExamSuiteType;
    }

    @Override
    public int setDictExamSuiteType(DictExamSuiteType dictExamSuiteType) {
        redisService.set(RedisConstant.br_order_dict_examSuiteType_id.concat(dictExamSuiteType.getExamTypeId()), JSONObject.toJSONString(dictExamSuiteType));
        return 1;
    }

    @Override
    public int deleteDictExamSuiteType(String examTypeId) {
        if (redisService.exists(RedisConstant.br_order_dict_examSuiteType_id.concat(examTypeId))) {
            redisService.delete(RedisConstant.br_order_dict_examSuiteType_id.concat(examTypeId));
        }
        return 1;
    } 

}
