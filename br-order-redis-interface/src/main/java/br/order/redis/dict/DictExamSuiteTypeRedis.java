package br.order.redis.dict;

import br.crm.pojo.org.DictExamSuiteType;

public interface DictExamSuiteTypeRedis {
    void initData();

    DictExamSuiteType getDictExamSuiteType(String examTypeId);

    int setDictExamSuiteType(DictExamSuiteType dictExamSuiteType);

    int deleteDictExamSuiteType(String examTypeId); 
}
