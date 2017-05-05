package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictagegroup;
import br.crm.service.dict.DictagegroupService;
import br.order.redis.dict.DictagegroupRedis;
import br.order.redis.redis.RedisService;

/**
 * (年龄分组字典表缓存实现接口)
 * 
 * @ClassName: DictagegroupRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月9日 上午9:20:20
 */
@Service
public class DictagegroupRedisImpl implements DictagegroupRedis {
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
    private DictagegroupService dictagegroupService;

    @Override
    public void initData() {
        List<Dictagegroup> list = dictagegroupService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictagegroup dictagegroup : list) {
                redisService.set(RedisConstant.br_order_dict_agegroup_id + dictagegroup.getAgegroupId(), JSONObject.toJSONString(dictagegroup));
            }
        }
    }

    @Override
    public int setDictagegroup(Dictagegroup dictagegroup) {
        redisService.set(RedisConstant.br_order_dict_agegroup_id.concat(dictagegroup.getAgegroupId() + ""), JSONObject.toJSONString(dictagegroup));
        return 1;
    }

    @Override
    public Dictagegroup getDictagegroup(int id) {
        Dictagegroup dictagegroup = null;
        if (redisService.exists(RedisConstant.br_order_dict_agegroup_id + id)) {
            dictagegroup = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_agegroup_id + id), Dictagegroup.class);
        }
        return dictagegroup;
    }

    @Override
    public int deleteDictagegroup(int id) {
        if (redisService.exists(RedisConstant.br_order_dict_agegroup_id + id)) {
            redisService.delete(RedisConstant.br_order_dict_agegroup_id + id);
        }
        return 1;
    }

}
