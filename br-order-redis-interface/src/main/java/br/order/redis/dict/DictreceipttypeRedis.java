package br.order.redis.dict;

import br.crm.pojo.dict.Dictreceipttype;

public interface DictreceipttypeRedis {
    void initData();
    
    int setDictreceipttype(Dictreceipttype dictreceipttype);
    
    Dictreceipttype getDictreceipttype(Long idReceipttype);
    
    int deleteDictreceipttype(Long idReceipttype);
}
