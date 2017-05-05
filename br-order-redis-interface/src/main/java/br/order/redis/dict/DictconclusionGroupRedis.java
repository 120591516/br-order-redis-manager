package br.order.redis.dict;

import br.crm.pojo.dict.Dictconclusiongroup;

public interface DictconclusionGroupRedis {
	void initData();
	
	String addConclusionGroup(Dictconclusiongroup dictconclusiongroup);

	Dictconclusiongroup getConclusionGroupById(String keyconclusiongroupid);

	String updateConclusionGroup(Dictconclusiongroup dictconclusiongroup);

}
