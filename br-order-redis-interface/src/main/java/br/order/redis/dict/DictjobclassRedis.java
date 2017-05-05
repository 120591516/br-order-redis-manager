package br.order.redis.dict;

import br.crm.pojo.dict.Dictjobclass;

public interface DictjobclassRedis {
    void initData();
    
    int setDictjobclass(Dictjobclass dictjobclass);
    
    Dictjobclass getDictjobclass(Long idJobclass);
    
    int deleteDictjobclass(Long idJobclass);
}
