package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.Dictconclusionresultclass;
import br.crm.service.dict.DictConclusionResultClassService;
import br.order.redis.dict.DictConclusionResultClassRedis;
import br.order.redis.redis.RedisService;

public class DictConclusionResultClassRedisImpl implements DictConclusionResultClassRedis{
	@Autowired
	private DictConclusionResultClassService dictConclusionResultClassService;
	@Autowired
	@Qualifier("RedisInnerService")
	private RedisService redisService;

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}
	@Override
	public void initData() {
		List<Dictconclusionresultclass> list = dictConclusionResultClassService.getAllDictConclusionResultClass();
		if(CollectionUtils.isNotEmpty(list)){
			for (Dictconclusionresultclass dictconclusionresultclass : list) {
				redisService.set(RedisConstant.br_order_dict_dictconclusionresultclass_id.concat(dictconclusionresultclass.getIdConclusionresultclass()), JSONObject.toJSONString(dictconclusionresultclass));
			}
			
		}
	}

	@Override
	public String addConclusionResultClass(Dictconclusionresultclass dictconclusionresultclass) {
		return redisService.set(RedisConstant.br_order_dict_dictconclusionresultclass_id.concat(dictconclusionresultclass.getIdConclusionresultclass()), JSONObject.toJSONString(dictconclusionresultclass));
	}

	@Override
	public Dictconclusionresultclass getConclusionResultClassById(String idConclusionresultclass) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_dictconclusionresultclass_id.concat(idConclusionresultclass)), Dictconclusionresultclass.class);
	}

	@Override
	public String updateConclusionResultClass(Dictconclusionresultclass dictconclusionresultclass) {
		return redisService.set(RedisConstant.br_order_dict_dictconclusionresultclass_id.concat(dictconclusionresultclass.getIdConclusionresultclass()), JSONObject.toJSONString(dictconclusionresultclass));
	}
	

}
