package br.order.redis.dict;

import br.crm.pojo.dict.DictImg;

public interface DictImgRedis {
    void initData();
    
    int setDictImg(DictImg dictImg);
    
    DictImg getDictImg(Long imgId);
}   
