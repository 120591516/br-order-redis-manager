package br.order.redis.dict;

import br.crm.pojo.dict.Dictidentity;

public interface DictidentityRedis {
    void initData();
    
    Dictidentity getDictidentity(Integer idIdentity);
    
    int setDictidentity(Dictidentity dictidentity);
    
    int deleteDictidentity(Integer idIdentity);
}
