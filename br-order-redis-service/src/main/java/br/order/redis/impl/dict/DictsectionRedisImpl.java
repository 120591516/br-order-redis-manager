package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictsection;
import br.crm.service.dict.DictSectionService;
import br.order.redis.dict.DictsectionRedis;
import br.order.redis.redis.RedisService;

/**
 * (总检科室redis)
 * 
 * @ClassName: DictsectionRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午2:13:17
 */
@Service
public class DictsectionRedisImpl implements DictsectionRedis {
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
    private DictSectionService dictSectionService;

    @Override
    public void initData() {
        List<Dictsection> list = dictSectionService.getDictSectionList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictsection dictsection : list) {
                redisService.set(RedisConstant.br_order_dict_section_sectionId + dictsection.getIdSection(), JSONObject.toJSONString(dictsection));
            }
        }
    }

    @Override
    public int setDictsection(Dictsection dictsection) {
        redisService.set(RedisConstant.br_order_dict_section_sectionId.concat(dictsection.getIdSection().toString()), JSONObject.toJSONString(dictsection));
        return 1;
    }

    @Override
    public Dictsection getDictsection(Integer idSection) {
        Dictsection dictsection = null;
        if (redisService.exists(RedisConstant.br_order_dict_section_sectionId.concat(idSection.toString()))) {
            dictsection = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_section_sectionId.concat(idSection.toString())), Dictsection.class);
        }
        return dictsection;
    }

    @Override
    public int deleteDictsection(Integer idSection) {
        if (redisService.exists(RedisConstant.br_order_dict_section_sectionId.concat(idSection.toString()))) {
            redisService.delete(RedisConstant.br_order_dict_section_sectionId.concat(idSection.toString()));
        }
        return 1;
    }

}
