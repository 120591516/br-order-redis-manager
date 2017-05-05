package br.order.redis.impl.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.suite.OrganizationExamSuiteType;
import br.crm.service.suite.OrgExamSuiteTypeService;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgExamSuiteTypeRedis;

@Service
public class OrgExamSuiteTypeRedisImpl implements OrgExamSuiteTypeRedis {
    @Autowired
    @Qualifier("RedisInnerService")
    private RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    private OrgExamSuiteTypeService orgExamSuiteTypeService;

    @Override
    public void initData() {
        List<OrganizationExamSuiteType> list = orgExamSuiteTypeService.getOrgExamSuiteTypeList(null);
        if (CollectionUtils.isNotEmpty(list)) {
            //缓存中间表id
            for (OrganizationExamSuiteType organizationExamSuiteType : list) {
            	setOrgExamSuiteType(organizationExamSuiteType);
            }

        }
    }

    @Override
    public int setOrgExamSuiteType(OrganizationExamSuiteType orgExamSuiteType) {
        redisService.set(RedisConstant.br_order_orgExamSuiteType_id.concat(orgExamSuiteType.getExamSuiteTypeId()), JSONObject.toJSONString(orgExamSuiteType));
        //缓存套餐id
        List<String> typeList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(orgExamSuiteType.getExamSuiteId()))) {
            typeList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(orgExamSuiteType.getExamSuiteId())), String.class);
        }
        if (!typeList.contains(orgExamSuiteType.getExamTypeId())) {
            typeList.add(orgExamSuiteType.getExamTypeId());
        }
        redisService.set(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(orgExamSuiteType.getExamSuiteId()), JSONObject.toJSONString(typeList));
        //缓存类型id
        List<String> suitList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamSuiteType_typeId.concat(orgExamSuiteType.getExamTypeId()))) {
            suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteType_typeId.concat(orgExamSuiteType.getExamTypeId())), String.class);
        }
        if (!suitList.contains(orgExamSuiteType.getExamSuiteId())) {
            suitList.add(orgExamSuiteType.getExamSuiteId());
        }
        redisService.set(RedisConstant.br_order_orgExamSuiteType_typeId.concat(orgExamSuiteType.getExamTypeId()), JSONObject.toJSONString(suitList));
        return 1;
    }

    @Override
    public OrganizationExamSuiteType getOrgExamSuiteType(String examSuiteTypeId) {
    	if(redisService.exists(RedisConstant.br_order_orgExamSuiteType_id.concat(examSuiteTypeId))){
    		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamSuiteType_id.concat(examSuiteTypeId)), OrganizationExamSuiteType.class);
    	}
    	return null;
    }

    @Override
    public int deleteOrgExamSuiteType(String examSuiteTypeId) {
    	if(redisService.exists(RedisConstant.br_order_orgExamSuiteType_id.concat(examSuiteTypeId))){
    		OrganizationExamSuiteType suiteType=getOrgExamSuiteType(examSuiteTypeId);
    		if(suiteType!=null){
    			List<String> typeList = new ArrayList<String>();
    	        if (redisService.exists(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(suiteType.getExamSuiteId()))) {
    	            typeList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(suiteType.getExamSuiteId())), String.class);
    	        }
    	        if (typeList.contains(suiteType.getExamTypeId())) {
    	            typeList.remove(suiteType.getExamTypeId());
    	        }
    	        redisService.set(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(suiteType.getExamSuiteId()), JSONObject.toJSONString(typeList));
    	        //缓存类型id
    	        List<String> suitList = new ArrayList<String>();
    	        if (redisService.exists(RedisConstant.br_order_orgExamSuiteType_typeId.concat(suiteType.getExamTypeId()))) {
    	            suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteType_typeId.concat(suiteType.getExamTypeId())), String.class);
    	        }
    	        if (suitList.contains(suiteType.getExamSuiteId())) {
    	            suitList.remove(suiteType.getExamSuiteId());
    	        }
    	        redisService.set(RedisConstant.br_order_orgExamSuiteType_typeId.concat(suiteType.getExamTypeId()), JSONObject.toJSONString(suitList));
    		}       
    	   return  redisService.delete(RedisConstant.br_order_orgExamSuiteType_id.concat(examSuiteTypeId)).intValue();	
    	} 
        return 0;
    }

    /**
     * <p>Title:getSuiteTypeIdBySuite</p> 
     * <p>Description:根据套餐id获取套餐类型id集合 </p> 
     * @param suiteId 套餐id
     * @return
     * @see br.order.redis.dict.DictExamSuiteTypeRedis#getSuiteTypeIdBySuite(java.lang.String)
     */
    @Override
    public List<String> getSuiteTypeIdBySuite(String suiteId) {
        String string = redisService.get(RedisConstant.br_order_orgExamSuiteType_suiteId.concat(suiteId));
        List<String> suiteTypeList = JSONObject.parseArray(string, String.class);
        return suiteTypeList;
    }

    /**
     * <p>Title:getSuiteBySuiteType</p> 
     * <p>Description:根据用户选择套餐类型获取套餐</p> 
     * @param suiteType 套餐类型
     * @return
     * @see br.order.redis.dict.DictExamSuiteTypeRedis#getSuiteBySuiteType(java.lang.String)
     */
    @Override
    public List<String> getSuiteBySuiteType(String suiteType) {
        String string = redisService.get(RedisConstant.br_order_orgExamSuiteType_typeId.concat(suiteType));
        List<String> suiteList = JSONObject.parseArray(string, String.class);
        return suiteList;
    }

}
