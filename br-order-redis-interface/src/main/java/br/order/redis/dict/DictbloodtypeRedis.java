package br.order.redis.dict;

import br.crm.pojo.dict.Dictbloodtype;

public interface DictbloodtypeRedis {
    void initData();
    
    Dictbloodtype getDictbloodtype(Integer idBloodtype);
    
    int setDictbloodtype(Dictbloodtype dictbloodtype);
    
    int deleteDictbloodtype(Integer idBloodtype);
}
