package br.order.redis.dict;

import br.crm.pojo.dict.Dictpayway;

public interface DictpaywayRedis {
    void initData();
    
    int setDictpayway(Dictpayway dictpayway);
    
    Dictpayway getDictpayway(Long idPayway);
    
    int deleteDictpayway(Long idPayway);
}
