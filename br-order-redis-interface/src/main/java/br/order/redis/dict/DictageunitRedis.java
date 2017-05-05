package br.order.redis.dict;

import br.crm.pojo.dict.Dictageunit;

public interface DictageunitRedis {
    void initData();
    
    Dictageunit getDictageunit(int id);
    
    int setDictageunit(Dictageunit dictageunit);
    
    int deleteDictageunit(int id);
}
