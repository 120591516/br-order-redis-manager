package br.order.redis.dict;

import br.crm.pojo.dict.Dictinformway;

public interface DictinformwayRedis {
    void initData();
    
    Dictinformway getDictinformway(Integer idInformway);
    
    int setDictinformway(Dictinformway dictinformway);
    
    int deleteDictinformway(Integer idInformway);
}
