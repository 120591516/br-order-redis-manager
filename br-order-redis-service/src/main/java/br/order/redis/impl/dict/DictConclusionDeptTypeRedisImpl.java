package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictconclusiondepttype;
import br.crm.service.dict.DictConclusionDeptTypeService;
import br.order.redis.dict.DictConclusionDeptTypeRedis;
import br.order.redis.redis.RedisService;

@Service
public class DictConclusionDeptTypeRedisImpl implements DictConclusionDeptTypeRedis {
    @Autowired
    private DictConclusionDeptTypeService dictConclusionDeptTypeService;

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
        List<Dictconclusiondepttype> list = dictConclusionDeptTypeService.getAllDictconclusiondepttype();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictconclusiondepttype dictconclusiondepttype : list) {
                redisService.set(RedisConstant.br_order_dict_DictConclusionDeptType_id.concat(dictconclusiondepttype.getIdConclusionDeptType()), JSONObject.toJSONString(dictconclusiondepttype));
            }
        }
    }

    @Override
    public String addConclusionDeptType(Dictconclusiondepttype dictconclusiondepttype) {

        return redisService.set(RedisConstant.br_order_dict_DictConclusionDeptType_id.concat(dictconclusiondepttype.getIdConclusionDeptType()), JSONObject.toJSONString(dictconclusiondepttype));
    }

    @Override
    public Dictconclusiondepttype getConclusionDeptTypeById(String idConclusionDeptType) {
        Dictconclusiondepttype dictconclusiondepttype = null;
        if (redisService.exists(RedisConstant.br_order_dict_DictConclusionDeptType_id.concat(idConclusionDeptType))) {
            dictconclusiondepttype = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_DictConclusionDeptType_id.concat(idConclusionDeptType)), Dictconclusiondepttype.class);
        }
        return dictconclusiondepttype;
    }

    @Override
    public String updateConclusionDeptType(Dictconclusiondepttype dictconclusiondepttype) {
        return redisService.set(RedisConstant.br_order_dict_DictConclusionDeptType_id.concat(dictconclusiondepttype.getIdConclusionDeptType()), JSONObject.toJSONString(dictconclusiondepttype));
    }

}
