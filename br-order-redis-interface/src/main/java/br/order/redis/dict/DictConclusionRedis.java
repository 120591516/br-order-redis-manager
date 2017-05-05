package br.order.redis.dict;

import br.crm.pojo.dict.DictconclusionWithBLOBs;
import br.crm.vo.conclusion.DictconclusionVo;

public interface DictConclusionRedis {
	void initData();
	
	String addConclusion(DictconclusionWithBLOBs dictconclusionWithBLOBs);

	DictconclusionVo getConclusionById(String id);

	String updateConclusion(DictconclusionWithBLOBs dictconclusionWithBLOBs);
}
