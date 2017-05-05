package br.order.redis.dict;

import br.crm.pojo.dict.Dictconclusiondepttype;

public interface DictConclusionDeptTypeRedis {
	void initData();
	
	String addConclusionDeptType(Dictconclusiondepttype dictconclusiondepttype);

	Dictconclusiondepttype getConclusionDeptTypeById(String idConclusionDeptType);

	String updateConclusionDeptType(Dictconclusiondepttype dictconclusiondepttype);
	

}
