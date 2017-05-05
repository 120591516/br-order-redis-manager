package br.order.redis.dict;

import br.crm.pojo.dict.Dictsex;

public interface DictsexRedis {
    void initData();
    
    int setDictsex(Dictsex dictsex);
    
    Dictsex getDictsex(Integer idSex);
    
    int deleteDictsex(Integer idSex);
}
