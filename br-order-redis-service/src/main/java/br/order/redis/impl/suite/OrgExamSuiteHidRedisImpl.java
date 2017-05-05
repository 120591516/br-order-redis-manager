/**   
* @Title: OrgExamSuiteHidRedisImpl.java 
* @Package br.order.redis.impl.suite 
* @Description: TODO
* @author kangting   
* @date 2017年2月10日 上午9:44:53 
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

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.suite.OrganizationExamSuiteHid;
import br.crm.service.suite.OrgExamSuiteHidService;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgExamSuiteHidRedis;

/**
 * @ClassName: OrgExamSuiteHidRedisImpl
 * @Description: TODO
 * @author kangting
 * @date 2017年2月10日 上午9:44:53
 * 
 */
@Service
public class OrgExamSuiteHidRedisImpl implements OrgExamSuiteHidRedis {

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
	private OrgExamSuiteHidService orgExamSuiteHidService;

	/**
	 * <p>
	 * Title: initData
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

		List<OrganizationExamSuiteHid> list = orgExamSuiteHidService.getAllOrganizationExamSuiteHid();
		if (CollectionUtils.isNotEmpty(list)) {
			for (OrganizationExamSuiteHid organizationExamSuiteHid : list) {
				setOrgExamSuiteHid(organizationExamSuiteHid);
			}

		}
	}

	/**
	 * <p>
	 * Title: setOrgExamSuiteHid
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param orgExamHidHid
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#setOrgExamSuiteHid(br.crm.pojo.suite.OrganizationExamSuiteHid)
	 */
	@Override
	public int setOrgExamSuiteHid(OrganizationExamSuiteHid orgExamHid) {
		// 插入套餐绑定的高发疾病
		List<String> hidList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(orgExamHid.getExamSuiteId()))) {
			hidList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(orgExamHid.getExamSuiteId())), String.class);
		}
		if (!hidList.contains(orgExamHid.getHighIncidenceDiseaseId())) {
			hidList.add(orgExamHid.getHighIncidenceDiseaseId());
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(orgExamHid.getExamSuiteId()), JSONObject.toJSONString(hidList));
		// 插入高发疾病绑定套餐
		List<String> suitList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(orgExamHid.getHighIncidenceDiseaseId()))) {
			suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(orgExamHid.getHighIncidenceDiseaseId())), String.class);
		}
		if (!suitList.contains(orgExamHid.getExamSuiteId())) {
			suitList.add(orgExamHid.getExamSuiteId());
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(orgExamHid.getHighIncidenceDiseaseId()), JSONObject.toJSONString(suitList));

		// 插入主表
		String reString = redisService.set(RedisConstant.br_order_orgExamSuiteHid_id.concat(orgExamHid.getOesHidId()), JSONObject.toJSONString(orgExamHid));
		return reString != null ? 1 : 0;
	}

	/**
	 * <p>
	 * Title: getOrgExamSuiteHid
	 * </p>
	 * <p>
	 * Description: 查看高发疾病
	 * </p>
	 * 
	 * @param examSuiteHidId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#getOrgExamSuiteHid(java.lang.String)
	 */
	@Override
	public OrganizationExamSuiteHid getOrgExamSuiteHid(String examSuiteHidId) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamSuiteHid_id.concat(examSuiteHidId)), OrganizationExamSuiteHid.class);
	}

	/**
	 * <p>
	 * Title: deleteOrgExamSuiteHid
	 * </p>
	 * <p>
	 * Description: 删除操作
	 * </p>
	 * 
	 * @param examSuiteHidId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#deleteOrgExamSuiteHid(java.lang.String)
	 */
	@Override
	public int deleteOrgExamSuiteHid(String examSuiteHidId) {

		OrganizationExamSuiteHid suiteHid = null;
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteHid_id.concat(examSuiteHidId))) {
			suiteHid = getOrgExamSuiteHid(examSuiteHidId);
		}

		// 删除套餐绑定的高发疾病
		List<String> hidList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(suiteHid.getExamSuiteId()))) {
			hidList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(suiteHid.getExamSuiteId())), String.class);
		}
		if (hidList.contains(suiteHid.getHighIncidenceDiseaseId())) {
			hidList.remove(suiteHid.getHighIncidenceDiseaseId());
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(suiteHid.getExamSuiteId()), JSONObject.toJSONString(hidList));
		// 删除高发疾病绑定套餐
		List<String> suitList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(suiteHid.getHighIncidenceDiseaseId()))) {
			suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(suiteHid.getHighIncidenceDiseaseId())), String.class);
		}
		if (suitList.contains(suiteHid.getExamSuiteId())) {
			suitList.remove(suiteHid.getExamSuiteId());
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteHid_Hid.concat(suiteHid.getHighIncidenceDiseaseId()), JSONObject.toJSONString(suitList));

		// 删除主表
		return (redisService.delete(RedisConstant.br_order_orgExamSuiteHid_id.concat(examSuiteHidId))).intValue();

	}

	/**
	 * <p>
	 * Title: getSuiteHidIdBySuite
	 * </p>
	 * <p>
	 * Description: 根据套餐id查询绑定高发病并List
	 * </p>
	 * 
	 * @param suiteId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#getSuiteHidIdBySuite(java.lang.String)
	 */
	@Override
	public List<String> getHidIdsBySuiteId(String suiteId) {
		// TODO Auto-generated method stub
		String suiteHid = redisService.get(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(suiteId));
		List<String> suiteHidList = JSONObject.parseArray(suiteHid, String.class);
		return suiteHidList;
	}

	/**
	 * <p>
	 * Title: getSuiteBySuiteHid
	 * </p>
	 * <p>
	 * Description: 根据高发病id获取绑定套餐id Lidt
	 * </p>
	 * 
	 * @param suiteHid
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteHidRedis#getSuiteBySuiteHid(java.lang.String)
	 */
	@Override
	public List<String> getSuiteIdsByHidId(String hidId) {
		// TODO Auto-generated method stub
		String suiteHids = redisService.get(RedisConstant.br_order_orgExamSuiteHid_suiteid_.concat(hidId));
		List<String> suiteHidList = JSONObject.parseArray(suiteHids, String.class);
		return suiteHidList;
	}

}
