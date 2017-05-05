package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictImg;
import br.crm.service.dict.DictImgService;
import br.order.redis.dict.DictImgRedis;
import br.order.redis.redis.RedisService;

/**
 * (图片字典表redis)
 * 
 * @ClassName: DictImgRedisImpl
 * @Description: TODO
 * @author 王文腾
 * @date 2017年1月11日 上午9:43:04
 */
@Service
public class DictImgRedisImpl implements DictImgRedis {
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
    private DictImgService dictImgService;

    @Override
    public void initData() {
        List<DictImg> list = dictImgService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictImg dictImg : list) {
                redisService.set(RedisConstant.br_order_dict_img_imgId + dictImg.getImgId(), JSONObject.toJSONString(dictImg));
            }

        }
    }

    @Override
    public int setDictImg(DictImg dictImg) {
        redisService.set(RedisConstant.br_order_dict_img_imgId + dictImg.getImgId(), JSONObject.toJSONString(dictImg));
        return 1;
    }

    @Override
    public DictImg getDictImg(Long imgId) {
        DictImg dictImg = null;
        if (redisService.exists(RedisConstant.br_order_dict_img_imgId + imgId)) {
            dictImg = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_img_imgId + imgId), DictImg.class);
        }
        return dictImg;
    }

}
