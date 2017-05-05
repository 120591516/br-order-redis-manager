package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictreceipttype;
import br.crm.service.dict.DictreceipttypeService;
import br.order.redis.dict.DictreceipttypeRedis;
import br.order.redis.redis.RedisService;

/**
 * (发票类型redis)
 * 
 * @ClassName: DictreceipttypeRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 下午1:58:31
 */
@Service
public class DictreceipttypeRedisImpl implements DictreceipttypeRedis {
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
    private DictreceipttypeService dictreceipttypeService;

    @Override
    public void initData() {
        List<Dictreceipttype> list = dictreceipttypeService.getDictreceipttypeList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Dictreceipttype dictreceipttype : list) {
                redisService.set(RedisConstant.br_order_dict_receipttype_id + dictreceipttype.getIdReceipttype(), JSONObject.toJSONString(dictreceipttype));
            }
        }
    }

    @Override
    public int setDictreceipttype(Dictreceipttype dictreceipttype) {
        redisService.set(RedisConstant.br_order_dict_receipttype_id.concat(dictreceipttype.getIdReceipttype() + ""), JSONObject.toJSONString(dictreceipttype));
        return 1;
    }

    @Override
    public Dictreceipttype getDictreceipttype(Long idReceipttype) {
        Dictreceipttype dictreceipttype = null;
        if (redisService.exists(RedisConstant.br_order_dict_receipttype_id.concat(idReceipttype + ""))) {
            dictreceipttype = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_receipttype_id.concat(idReceipttype + "")), Dictreceipttype.class);
        }
        return dictreceipttype;
    }

    @Override
    public int deleteDictreceipttype(Long idReceipttype) {
        if (redisService.exists(RedisConstant.br_order_dict_receipttype_id.concat(idReceipttype + ""))) {
            redisService.delete(RedisConstant.br_order_dict_receipttype_id.concat(idReceipttype + ""));
        }
        return 1;
    }

}
