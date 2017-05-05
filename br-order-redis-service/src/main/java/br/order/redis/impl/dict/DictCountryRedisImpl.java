package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictCountry;
import br.crm.service.dict.CountryManagerService;
import br.order.redis.dict.DictCountryRedis;
import br.order.redis.redis.RedisService;

/**
 * (国家字典表接口实现)
 * 
 * @ClassName: DictCountryRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月9日 上午9:19:49
 */
@Service
public class DictCountryRedisImpl implements DictCountryRedis {
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
    private CountryManagerService countryManagerService;

    /**
     * <p>Title:initData</p> 
     * <p>Description:初始化数据 </p> 
     * @see br.order.redis.dict.DictCountryRedis#initData()
     */
    @Override
    public void initData() {
        List<DictCountry> list = countryManagerService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictCountry dictCountry : list) {
                redisService.set(RedisConstant.br_order_dict_countryManager_id + dictCountry.getCountryId(), JSONObject.toJSONString(dictCountry));
            }
        }
    }

    /**
     * <p>Title:getDictCountry</p> 
     * <p>Description:获取国家数据缓存 </p> 
     * @param countryId
     * @return
     * @see br.order.redis.dict.DictCountryRedis#getDictCountry(java.lang.Long)
     */
    @Override
    public DictCountry getDictCountry(Long countryId) {
        DictCountry dictCountry = null;
        if (redisService.exists(RedisConstant.br_order_dict_countryManager_id.concat(countryId.toString()))) {
            dictCountry = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_countryManager_id.concat(countryId.toString())), DictCountry.class);
        }
        return dictCountry;
    }

    /**
     * <p>Title:setDictCountry</p> 
     * <p>Description: 插入国家数据缓存</p> 
     * @param dictCountry
     * @return
     * @see br.order.redis.dict.DictCountryRedis#setDictCountry(br.crm.pojo.dict.DictCountry)
     */
    @Override
    public int setDictCountry(DictCountry dictCountry) {
        redisService.set(RedisConstant.br_order_dict_countryManager_id.concat(dictCountry.getCountryId().toString()), JSONObject.toJSONString(dictCountry));
        return 1;
    }

}
