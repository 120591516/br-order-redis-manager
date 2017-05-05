package br.order.redis.dict;

import br.crm.pojo.org.DictHighIncidenceDisease;

public interface DictHighIncidenceDiseaseRedis {
    void initData();
    
    DictHighIncidenceDisease getDictHighIncidenceDisease(String highIncidenceDiseaseId);
    
    int setDictHighIncidenceDisease(DictHighIncidenceDisease dictHighIncidenceDisease);
    
    int deleteDictHighIncidenceDisease(String highIncidenceDiseaseId);
}
