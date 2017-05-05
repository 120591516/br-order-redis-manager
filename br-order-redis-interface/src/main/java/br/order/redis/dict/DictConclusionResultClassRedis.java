package br.order.redis.dict;

import br.crm.pojo.dict.Dictconclusionresultclass;

public interface DictConclusionResultClassRedis {
	void initData();
	
	String addConclusionResultClass(Dictconclusionresultclass dictconclusionresultclass);

	Dictconclusionresultclass getConclusionResultClassById(String idConclusionresultclass);

	String updateConclusionResultClass(Dictconclusionresultclass dictconclusionresultclass);
}
