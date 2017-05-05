package br.order.redis.dict;

import br.crm.pojo.dict.Dictoccupation;

public interface DictoccupationRedis {
    void initData();
    
    int setDictoccupation(Dictoccupation dictoccupation);
    
    Dictoccupation getDictoccupation(Integer idOccupation);
    
    int deleteDictoccupation(Integer idOccupation);
}
