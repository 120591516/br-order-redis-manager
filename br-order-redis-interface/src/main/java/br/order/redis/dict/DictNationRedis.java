package br.order.redis.dict;

import br.crm.pojo.dict.DictNation;

public interface DictNationRedis {
    void initData();
    
    int setDictNation(DictNation dictNation);
    
    DictNation getDictNation(Integer id);
    
    int deleteDictNation(Integer id);
}
