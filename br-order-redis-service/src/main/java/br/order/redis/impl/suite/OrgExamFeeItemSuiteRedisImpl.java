/**   
* @Title: OrgExamFeeItemSuiteRedisImpl.java 
* @Package br.order.redis.impl.suite 
* @Description: TODO
* @author kangting   
* @date 2017年1月9日 上午10:42:16 
* @version V1.0   
*/
package br.order.redis.impl.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.RedisConstant;
import br.crm.pojo.suite.OrganizationExamSuiteFeeItem;
import br.crm.pojo.suite.OrganizationExamSuiteFeeItemExample;
import br.crm.service.suite.OrgExamFeeItemSuiteService;
import br.crm.vo.suite.OrgExamSuiteVo;
import br.order.common.utils.JsonUtils;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgExamFeeItemSuiteRedis;

/** 
 * @ClassName: OrgExamFeeItemSuiteRedisImpl 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月9日 上午10:42:16 
 *  
 */
@Service
public class OrgExamFeeItemSuiteRedisImpl implements OrgExamFeeItemSuiteRedis {

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
	private OrgExamFeeItemSuiteService orgExamFeeItemSuiteService;
	
	
	/**
	* <p>Title: initData</p> 
	* <p>Description: </p>  
	* @see br.order.redis.suite.OrgExamFeeItemSuiteRedis#initData() 
	*/
	@Override
	public void initData() { 
        List<OrganizationExamSuiteFeeItem> list = orgExamFeeItemSuiteService.getAllOrgExamFeeItemSuite();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationExamSuiteFeeItem organizationExamFeeItemSuite : list) {
            	insertOrgExamFeeItemSuite(organizationExamFeeItemSuite);
            }
        }
	}

	/**
	* <p>Title: getOrgBranchSuiteByBranchId</p> 
	* <p>Description: </p> 
	* @param suiteId
	* @return 
	* @see br.order.redis.suite.OrgExamFeeItemSuiteRedis#getOrgBranchSuiteByBranchId(java.lang.String) 
	*/
	@Override
	public List<OrgExamSuiteVo> getOrgBranchSuiteBySuiteId(String suiteId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * 根据id获取收费-体检套餐信息
     * <p>
     * Title: getOrgExamFeeItemSuiteById
     * </p>
     * <p>
     * Description:
     * </p> 
     * 
     * @param examFisId
     *            主键id
     * @return
     * @see br.crm.service.suite.OrgExamFeeItemSuiteService#getOrgExamFeeItemSuiteById(java.lang.String)
     */
    @Override
    public OrganizationExamSuiteFeeItem getOrgExamFeeItemSuiteById(String examFisId) {
        return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamFeeItemSuite_examFISId.concat(examFisId)), OrganizationExamSuiteFeeItem.class);
    }

	/**
	* <p>Title: insertOrgExamFeeItemSuite</p> 
	* <p>Description: </p> 
	* @param orgExamSuiteFeeItem
	* @return 
	* @see br.order.redis.suite.OrgExamFeeItemSuiteRedis#insertOrgExamFeeItemSuite(br.crm.pojo.suite.OrganizationExamSuiteFeeItem) 
	*/
	@Override
	public String insertOrgExamFeeItemSuite(
			OrganizationExamSuiteFeeItem organizationExamFeeItemSuite) {
		 //缓存中间表的id
        redisService.set(RedisConstant.br_order_orgExamFeeItemSuite_examFISId.concat(organizationExamFeeItemSuite.getExamFisId()), JSONObject.toJSONString(organizationExamFeeItemSuite));
        //存储收费项目id
        List<String> feeItemList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId()))) {
            feeItemList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId())), String.class);
        }
        if (!feeItemList.contains(organizationExamFeeItemSuite.getExamFeeItemId())) {
            feeItemList.add(organizationExamFeeItemSuite.getExamFeeItemId());
        }
        redisService.set(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId()), JSONObject.toJSONString(feeItemList));
        //存储体检套餐id
        List<String> examSuiteList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamSuiteId()))) {
            examSuiteList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamSuiteId())), String.class);
        }
        if (!examSuiteList.contains(organizationExamFeeItemSuite.getExamSuiteId())) {
            examSuiteList.add(organizationExamFeeItemSuite.getExamSuiteId());
        }
       return redisService.set(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamFeeItemId()), JSONObject.toJSONString(examSuiteList));
	}

	/**
	* <p>Title: updateOrgExamFeeItemSuite</p> 
	* <p>Description: </p> 
	* @param orgExamSuiteFeeItem
	* @return 
	* @see br.order.redis.suite.OrgExamFeeItemSuiteRedis#updateOrgExamFeeItemSuite(br.crm.pojo.suite.OrganizationExamSuiteFeeItem) 
	*/
	@Override
	public String updateOrgExamFeeItemSuite(
			OrganizationExamSuiteFeeItem organizationExamFeeItemSuite) {
		 //缓存中间表的id
        redisService.set(RedisConstant.br_order_orgExamFeeItemSuite_examFISId.concat(organizationExamFeeItemSuite.getExamFisId()), JSONObject.toJSONString(organizationExamFeeItemSuite));
        //存储收费项目id
        List<String> feeItemList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId()))) {
            feeItemList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId())), String.class);
            if (feeItemList.contains(organizationExamFeeItemSuite.getExamFeeItemId())) {
                feeItemList.remove(organizationExamFeeItemSuite.getExamFeeItemId());
            }
        }
        
        redisService.set(RedisConstant.br_order_orgExamFeeItemSuite_examSuiteId.concat(organizationExamFeeItemSuite.getExamSuiteId()), JSONObject.toJSONString(feeItemList));
        //存储体检套餐id
        List<String> examSuiteList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamSuiteId()))) {
            examSuiteList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamSuiteId())), String.class);
            if (!examSuiteList.contains(organizationExamFeeItemSuite.getExamSuiteId())) {
                examSuiteList.remove(organizationExamFeeItemSuite.getExamSuiteId());
            }
        }
       return String.valueOf(redisService.delete(RedisConstant.br_order_orgExamFeeItemSuite_examFeeItemId.concat(organizationExamFeeItemSuite.getExamFeeItemId())));
	}
	

}
