package br.order.redis.dict;

import br.crm.pojo.dict.Dictagegroup;

public interface DictagegroupRedis {
    void initData();

    int setDictagegroup(Dictagegroup dictagegroup);

    Dictagegroup getDictagegroup(int id);
    
    int deleteDictagegroup(int id);
}
