package br.order.redis.dict;

import br.crm.pojo.dict.DictDeptType;

public interface DictDeptTypeRedis {
    void initData();

    DictDeptType getDictDeptType(String dictDeptTypeId);
    
    int setDictDeptType(DictDeptType dictDeptType);
    
    int deleteDictDeptType(String dictDeptTypeId);
}
