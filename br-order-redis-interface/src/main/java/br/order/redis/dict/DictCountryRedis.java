package br.order.redis.dict;

import br.crm.pojo.dict.DictCountry;

public interface DictCountryRedis {
    void initData();
    
    DictCountry getDictCountry(Long countryId);
    
    int setDictCountry(DictCountry dictCountry);
    
}
