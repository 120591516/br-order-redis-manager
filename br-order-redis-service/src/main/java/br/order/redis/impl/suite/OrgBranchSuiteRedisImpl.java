/**   
* @Title: OrgBranchSuiteRedisImpl.java 
* @Package br.order.redis.suite 
* @Description: TODO
* @author kangting   
* @date 2017年1月6日 下午1:56:21 
* @version V1.0   
*/
package br.order.redis.impl.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.branch.OrganizationBranch;
import br.crm.pojo.dict.DictArea;
import br.crm.pojo.org.DictExamSuiteType;
import br.crm.pojo.suite.OrganizationBranchSuite;
import br.crm.service.suite.OrgBranchSuiteService;
import br.crm.vo.branch.OrganizationBranchVo;
import br.crm.vo.suite.OrgExamSuiteVo;
import br.order.redis.branch.OrgBranchRedis;
import br.order.redis.dict.DictAreaRedis;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgBranchSuiteRedis;

/** 
 * @ClassName: OrgBranchSuiteRedisImpl 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月6日 下午1:56:21 
 *  
 */
@Service
public class OrgBranchSuiteRedisImpl implements OrgBranchSuiteRedis {

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
    private OrgBranchSuiteService orgBranchSuiteService;

    @Autowired
    private DictAreaRedis dictAreaRedis;

    @Autowired
    private OrgBranchRedis orgBranchRedis;

    /**
    * <p>Title: initData</p> 
    * <p>Description: </p>  
    * 初始化套餐绑定门店信息
    * 1. 根据id存储
    * 2. 根据套餐id存储
    * 3. 根据门店id存储
    * @see br.order.redis.suite.OrgBranchSuiteRedis#initData() 
    */
    @Override
    public void initData() {
        System.out.println("开始初始化门店套餐缓存");
        // 门店-套餐(主键id)
        try {
            List<OrganizationBranchSuite> list = orgBranchSuiteService.getAllOrgBranchSuiteByExample();
            if (CollectionUtils.isNotEmpty(list)) {
                for (OrganizationBranchSuite organizationBranchSuite : list) {
                    insertOrgBranchSuite(organizationBranchSuite);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("开始初始化门店套餐缓存");

    }

    /**
    * <p>Title: getOrgBranchSuiteById</p> 
    * <p>Description: </p> 
    * @param branchSuiteId
    * @return 
    * @see br.order.redis.suite.OrgBranchSuiteRedis#getOrgBranchSuiteById(java.lang.String) 
    */
    @Override
    public OrganizationBranchSuite getOrgBranchSuiteById(String branchSuiteId) {
        if (redisService.exists(RedisConstant.br_order_orgBranchSuite_id.concat(branchSuiteId))) {
            return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgBranchSuite_id.concat(branchSuiteId)), OrganizationBranchSuite.class);
        }
        return null;
    }

    /**
    * <p>Title: getOrgBranchSuiteByBranchId</p> 
    * <p>Description:根据门店id 获取套餐信息 </p> 
    * @param branchId
    * @return 
    * @see br.order.redis.suite.OrgBranchSuiteRedis#getOrgBranchSuiteByBranchId(java.lang.String) 
    */
    @Override
    public List<OrgExamSuiteVo> getOrgBranchSuiteByBranchId(String branchId) {

        List<OrgExamSuiteVo> result = new ArrayList<OrgExamSuiteVo>();
        if (redisService.exists(RedisConstant.br_order_orgBranchSuite_branchId + branchId)) {
            String str = redisService.get(RedisConstant.br_order_orgBranchSuite_branchId + branchId);
            if (StringUtils.isNotEmpty(str)) {
                List<String> suiteIdList = JSONObject.parseArray(str, String.class);
                if (CollectionUtils.isNotEmpty(suiteIdList)) {
                    for (String suiteId : suiteIdList) {
                        String suite = redisService.get(RedisConstant.br_order_orgExamSuite_suiteId + suiteId);
                        if (StringUtils.isNotBlank(suite)) {
                            OrgExamSuiteVo organizationExamSuite = JSONObject.parseObject(suite, OrgExamSuiteVo.class);
                            if (null != organizationExamSuite) {
                                String suiteTypeStr = redisService.get(RedisConstant.br_order_orgExamSuiteType_suiteId + organizationExamSuite.getExamSuiteId());
                                if (suiteTypeStr != null) {
                                    List<String> typeIds = JSONObject.parseArray(suiteTypeStr, String.class);
                                    if (CollectionUtils.isNotEmpty(typeIds)) {
                                        for (String typeid : typeIds) {
                                            String type = redisService.get(RedisConstant.br_order_dict_examSuiteType_id + typeid);
                                            if (type != null) {
                                                organizationExamSuite.setTypeNameList(organizationExamSuite.getTypeNameList() != null ? organizationExamSuite.getTypeNameList() : "" + " " + JSONObject.parseObject(type, DictExamSuiteType.class).getExamTypeName() + " ");
                                            }
                                        }
                                    }
                                }
                                result.add(organizationExamSuite);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
    * <p>Title: getOrgBranchSuiteBySuiteId</p> 
    * <p>Description: 根据套餐id 获取中间表信息</p> 
    * @param suiteId
    * @return 
    * @see br.order.redis.suite.OrgBranchSuiteRedis#getOrgBranchSuiteBySuiteId(java.lang.String) 
    */
    @Override
    public List<OrganizationBranchVo> getOrgBranchSuiteBySuiteId(String suiteId) {
        try {
            List<OrganizationBranchVo> result = new ArrayList<OrganizationBranchVo>();
            List<String> list = JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgBranchSuite_suiteId.concat(suiteId)), String.class);
            for (String branchId : list) {
                OrganizationBranchVo orgBranchVo = new OrganizationBranchVo();
                OrganizationBranch orgBranch = orgBranchRedis.getOrgBranch(branchId);
                BeanUtils.copyProperties(orgBranchVo, orgBranch);
                DictArea dictArea = dictAreaRedis.getDictAreaByAreaId(orgBranch.getBranchAreaId());
                orgBranchVo.setDictArea(dictArea);
                result.add(orgBranchVo);
            }
            return result;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * <p>Title: insertOrgBranchSuite</p> 
    * <p>Description: </p> 
    * @param orgBranchSuite
    * @return 
    * @see br.order.redis.suite.OrgBranchSuiteRedis#insertOrgBranchSuite(br.crm.pojo.suite.OrganizationBranchSuite) 
    */
    @Override
    public String insertOrgBranchSuite(OrganizationBranchSuite orgBranchSuite) {

        // 存储门店ID
        List<String> suiteList = new ArrayList<String>();

        if (redisService.exists(RedisConstant.br_order_orgBranchSuite_branchId.concat(orgBranchSuite.getBranchId()))) {
            suiteList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchSuite_branchId.concat(orgBranchSuite.getBranchId())), String.class);
        }

        if (!suiteList.contains(orgBranchSuite.getSuiteId())) {
            suiteList.add(orgBranchSuite.getSuiteId());
        }
        redisService.set(RedisConstant.br_order_orgBranchSuite_branchId.concat(orgBranchSuite.getBranchId()), JSONObject.toJSONString(suiteList));

        // 存儲套餐ID
        List<String> branchList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgBranchSuite_suiteId.concat(orgBranchSuite.getSuiteId()))) {
            branchList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchSuite_suiteId.concat(orgBranchSuite.getSuiteId())), String.class);
        }
        if (!branchList.contains(orgBranchSuite.getBranchId())) {
            branchList.add(orgBranchSuite.getBranchId());
        }
        redisService.set(RedisConstant.br_order_orgBranchSuite_suiteId.concat(orgBranchSuite.getSuiteId()), JSONObject.toJSONString(branchList));
        //存储中间表id
        return redisService.set(RedisConstant.br_order_orgBranchSuite_id.concat(orgBranchSuite.getBranchSuiteId()), JSONObject.toJSONString(orgBranchSuite));
    }

    /**
    * <p>Title: updateOrgBranchSuite</p> 
    * <p>Description:删除 只是删除根据id存储 </p> 
    * @param orgBranchSuite
    * @return 
    * @see br.order.redis.suite.OrgBranchSuiteRedis#updateOrgBranchSuite(br.crm.pojo.suite.OrganizationBranchSuite) 
    */
    @Override
    public String updateOrgBranchSuite(OrganizationBranchSuite orgBranchSuite) {
        if (redisService.exists(RedisConstant.br_order_orgBranchSuite_id.concat(orgBranchSuite.getBranchSuiteId()))) {

            OrganizationBranchSuite organizationBranchSuite = JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgBranchSuite_id.concat(orgBranchSuite.getBranchSuiteId())), OrganizationBranchSuite.class);
            List<String> suiteList = new ArrayList<String>();
            // 根据门店id 删除套餐
            if (redisService.exists(RedisConstant.br_order_orgBranchSuite_branchId.concat(organizationBranchSuite.getBranchId()))) {
                //获取list
                suiteList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchSuite_branchId.concat(orgBranchSuite.getBranchId())), String.class);
                //删除绑定套餐
                if (suiteList.contains(orgBranchSuite.getSuiteId())) {
                    suiteList.remove(orgBranchSuite.getSuiteId());
                }
            }
            // 根据套餐id 删除门店ID
            List<String> branchList = new ArrayList<String>();
            if (redisService.exists(RedisConstant.br_order_orgBranchSuite_suiteId.concat(orgBranchSuite.getSuiteId()))) {
                branchList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgBranchSuite_suiteId.concat(orgBranchSuite.getSuiteId())), String.class);
                if (branchList.contains(orgBranchSuite.getBranchId())) {
                    branchList.remove(orgBranchSuite.getBranchId());
                }
            }
            //删除绑定id项
            return String.valueOf(redisService.delete(RedisConstant.br_order_orgBranchSuite_id.concat(orgBranchSuite.getBranchSuiteId())));
        }
        return null;
    }

    @Override
    public List<String> getBranchSuiteBySuitId(String suiteId) {
        return JsonUtils.jsonToList(redisService.get(RedisConstant.br_order_orgBranchSuite_suiteId + suiteId), String.class);
    }

}
