package br.order.redis.dict;

import br.crm.pojo.dict.Dictsection;

public interface DictsectionRedis {
    void initData();
    
    int setDictsection(Dictsection dictsection);
    
    Dictsection getDictsection(Integer idSection);
    
    int deleteDictsection(Integer idSection);
}
