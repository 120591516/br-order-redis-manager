package br.order.redis.dict;

import br.crm.pojo.dict.Dictmarriage;

public interface DictmarriageRedis {
    void initData();
    
    int setDictmarriage(Dictmarriage dictmarriage);
    
    Dictmarriage getDictmarriage(Integer idMarriage);
    
    int deleteDictmarriage(Integer idMarriage);
}
