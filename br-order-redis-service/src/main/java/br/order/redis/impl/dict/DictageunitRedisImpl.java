package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictageunit;
import br.crm.service.dict.DictAgeUnitService;
import br.order.redis.dict.DictageunitRedis;
import br.order.redis.redis.RedisService;

/**
 * ()
 * 年龄单位表redis接口实现
 * @ClassName: DictageunitRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月6日 下午5:14:12
 */
@Service
public class DictageunitRedisImpl implements DictageunitRedis {
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
    private DictAgeUnitService dictAgeUnitService;

    /**
     * <p>Title:initData</p> 
     * <p>Description:初始化年龄单位表缓存 </p> 
     * @see br.order.redis.dict.DictageunitRedis#initData()
     */
    @Override
    public void initData() {
        List<Dictageunit> list = dictAgeUnitService.getAllDictageunit();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictageunit dictageunit : list) {
                redisService.set(RedisConstant.br_order_dict_ageunit_id + dictageunit.getAgeunitId(), JSONObject.toJSONString(dictageunit));
            }
        }
    }

    /**
     * <p>Title:getDictageunit</p> 
     * <p>Description: 根据主键获取年龄单位缓存</p> 
     * @param id
     * @return
     * @see br.order.redis.dict.DictageunitRedis#getDictageunit(int)
     */
    @Override
    public Dictageunit getDictageunit(int id) {
        Dictageunit dictageunit = null;
        if (redisService.exists(RedisConstant.br_order_dict_ageunit_id + id)) {
            dictageunit = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_ageunit_id + id), Dictageunit.class);
        }
        return dictageunit;
    }

    @Override
    public int setDictageunit(Dictageunit dictageunit) {
        redisService.set(RedisConstant.br_order_dict_ageunit_id.concat(dictageunit.getAgeunitId() + ""), JSONObject.toJSONString(dictageunit));
        return 1;
    }

    @Override
    public int deleteDictageunit(int id) {
        if (redisService.exists(RedisConstant.br_order_dict_ageunit_id + id)) {
            redisService.delete(RedisConstant.br_order_dict_ageunit_id + id);
        }
        return 1;
    }

}
