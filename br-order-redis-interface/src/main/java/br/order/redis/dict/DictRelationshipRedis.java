package br.order.redis.dict;

import br.order.user.pojo.dict.DictRelationship;

public interface DictRelationshipRedis {
    void initData();

    int setDictRelationship(DictRelationship dictRelationship);

    DictRelationship getDictRelationship(Long dictRelationId);

    int deleteDictRelationship(Long dictRelationId);
}
