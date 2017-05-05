package br.order.redis.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictbloodtype;
import br.crm.service.dict.DictBloodTypeService;
import br.order.redis.dict.DictbloodtypeRedis;
import br.order.redis.redis.RedisService;

/**
 * (血型字典表缓存接口实现)
 * 
 * @ClassName: DictbloodtypeRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月9日 上午9:20:54
 */
@Service
public class DictbloodtypeRedisImpl implements DictbloodtypeRedis {
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
    private DictBloodTypeService dictBloodTypeService;

    /**
     * <p>Title:initDate</p> 
     * <p>Description: 初始化数据</p> 
     * @see br.order.redis.dict.DictbloodtypeRedis#initData()
     */
    @Override
    public void initData() {
        List<Dictbloodtype> list = dictBloodTypeService.getAllBloodType();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictbloodtype dictbloodtype : list) {
                redisService.set(RedisConstant.br_order_dict_bloodType_bloodTypeId + dictbloodtype.getIdBloodtype(), JSONObject.toJSONString(dictbloodtype));
            }
        }
    }

    @Override
    public Dictbloodtype getDictbloodtype(Integer idBloodtype) {
        Dictbloodtype dictbloodtype = null;
        if (redisService.exists(RedisConstant.br_order_dict_bloodType_bloodTypeId.concat(idBloodtype.toString()))) {
            dictbloodtype = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_bloodType_bloodTypeId.concat(idBloodtype.toString())), Dictbloodtype.class);
        }
        return dictbloodtype;
    }

    @Override
    public int setDictbloodtype(Dictbloodtype dictbloodtype) {
        redisService.set(RedisConstant.br_order_dict_bloodType_bloodTypeId.concat(dictbloodtype.getIdBloodtype().toString()), JSONObject.toJSONString(dictbloodtype));
        return 1;
    }

    @Override
    public int deleteDictbloodtype(Integer idBloodtype) {
        if (redisService.exists(RedisConstant.br_order_dict_bloodType_bloodTypeId.concat(idBloodtype.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_bloodType_bloodTypeId.concat(idBloodtype.toString()));
        }
        return 1;
    }

}
