package br.order.redis.dict;

import br.crm.pojo.dict.Dicteducation;

public interface DicteducationRedis {
    void initData();
    
    Dicteducation getDicteducation(Integer idEducation);
    
    int setDicteducation(Dicteducation dicteducation);
    
    int deleteDicteducation(Integer idEducation);
}
