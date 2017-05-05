package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dicteducation;
import br.crm.service.dict.DictEducationService;
import br.order.redis.dict.DicteducationRedis;
import br.order.redis.redis.RedisService;

/**
 * (教育程度字典表redis)
 * 
 * @ClassName: DicteducationRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月10日 下午1:52:41
 */
@Service
public class DicteducationRedisImpl implements DicteducationRedis {
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
    private DictEducationService dictEducationService;

    @Override
    public void initData() {
        List<Dicteducation> list = dictEducationService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dicteducation dicteducation : list) {
                redisService.set(RedisConstant.br_order_dict_education_id + dicteducation.getIdEducation(), JSONObject.toJSONString(dicteducation));
            }
        }
    }

    @Override
    public Dicteducation getDicteducation(Integer idEducation) {
        Dicteducation dicteducation = null;
        if (redisService.exists(RedisConstant.br_order_dict_education_id.concat(idEducation + ""))) {
            dicteducation = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_education_id.concat(idEducation + "")), Dicteducation.class);
        }
        return dicteducation;
    }

    @Override
    public int setDicteducation(Dicteducation dicteducation) {
        redisService.set(RedisConstant.br_order_dict_education_id.concat(dicteducation.getIdEducation() + ""), JSONObject.toJSONString(dicteducation));
        return 1;
    }

    @Override
    public int deleteDicteducation(Integer idEducation) {
        if (redisService.exists(RedisConstant.br_order_dict_education_id.concat(idEducation + ""))) {
            redisService.delete(RedisConstant.br_order_dict_education_id.concat(idEducation + ""));
        }
        return 1;
    }

}
