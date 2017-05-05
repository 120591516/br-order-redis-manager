package br.order.redis.impl.dict;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictconclusionWithBLOBs;
import br.crm.service.dict.DictConclusionService;
import br.crm.vo.conclusion.DictconclusionVo;
import br.order.redis.dict.DictConclusionRedis;
import br.order.redis.redis.RedisService;
@Service
public class DictConclusionRedisImpl implements DictConclusionRedis{
	@Autowired
	private DictConclusionService dictConclusionService;
	/**
		 * <p>Title:init</p> 
		 * <p>Description: 初始化结论词</p> 
	     * @see br.order.redis.dict.DictConclusionRedis#init()
		 */
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
		List<DictconclusionWithBLOBs> list = dictConclusionService.getAllDictconclusionWithBLOBs();
		if(CollectionUtils.isNotEmpty(list)){
			for (DictconclusionWithBLOBs dictconclusionWithBLOBs : list) {
				redisService.set(RedisConstant.br_order_dict_dictconclusion_id.concat(dictconclusionWithBLOBs.getIdConclusion()), JSONObject.toJSONString(dictconclusionWithBLOBs));
			}
			
		}
	}

	@Override
	public String addConclusion(DictconclusionWithBLOBs dictconclusionWithBLOBs) {
		return redisService.set(RedisConstant.br_order_dict_dictconclusion_id.concat(dictconclusionWithBLOBs.getIdConclusion()), JSONObject.toJSONString(dictconclusionWithBLOBs));
	}

	@Override
	public DictconclusionVo getConclusionById(String id) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_dict_dictconclusion_id.concat(id)),DictconclusionVo.class);
	}

	@Override
	public String updateConclusion(DictconclusionWithBLOBs dictconclusionWithBLOBs) {
		return redisService.set(RedisConstant.br_order_dict_dictconclusion_id.concat(dictconclusionWithBLOBs.getIdConclusion()), JSONObject.toJSONString(dictconclusionWithBLOBs));
	}

}
