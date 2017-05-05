/**   
* @Title: OrgExamFeeItemDetailRedisImpl.java 
* @Package br.order.redis.impl.examfeeitem 
* @Description: TODO
* @author kangting   
* @date 2017年1月9日 下午3:31:52 
* @version V1.0   
*/
package br.order.redis.impl.examfeeitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.examitem.OrganizationExamFeeItemDetail;
import br.crm.service.orgexamfeeitem.OrgExamFeeItemDetailService;
import br.order.redis.examfeeitem.OrgExamFeeItemDetailRedis;
import br.order.redis.redis.RedisService;

/** 
 * @ClassName: OrgExamFeeItemDetailRedisImpl 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月9日 下午3:31:52 
 *  
 */
@Service
public class OrgExamFeeItemDetailRedisImpl implements OrgExamFeeItemDetailRedis{
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
	private OrgExamFeeItemDetailService orgExamFeeItemDetailService;
	
	/**
	* <p>Title: initData</p> 
	* <p>Description: </p>  
	* @see br.order.redis.examfeeitem.OrgExamFeeItemDetailRedis#initData() 
	*/
	@Override
	public void initData() { 
	     List<OrganizationExamFeeItemDetail> selectByExample = orgExamFeeItemDetailService.getAllOrgExamFeeItemDetail();
		        if (CollectionUtils.isNotEmpty(selectByExample)) {
		            for (OrganizationExamFeeItemDetail organizationExamFeeItemDetail : selectByExample) {
		            	insertOrgExamFeeItemDetail(organizationExamFeeItemDetail);
		            }

		        }
	}

	/**
	* <p>Title: insertOrgExamFeeItemDetail</p> 
	* <p>Description: </p> 
	* @param orgExamFeeItemDetail
	* @return 
	* @see br.order.redis.examfeeitem.OrgExamFeeItemDetailRedis#insertOrgExamFeeItemDetail(br.crm.pojo.examitem.OrganizationExamFeeItemDetail) 
	*/
	@Override
	public String insertOrgExamFeeItemDetail(
			OrganizationExamFeeItemDetail organizationExamFeeItemDetail) {
	        
            //缓存收费项目id
            List<String> examItemUserList = new ArrayList<String>();
            if (redisService
                    .exists(RedisConstant.br_order_orgExamFeeItemDetail_feeItemId
                            .concat(organizationExamFeeItemDetail
                                    .getExamFeeItemId()))) {
                examItemUserList = JSONObject.parseArray(
                        redisService
                                .get(RedisConstant.br_order_orgExamFeeItemDetail_feeItemId
                                        .concat(organizationExamFeeItemDetail
                                                .getExamFeeItemId())),
                        String.class);
            }
            if (!examItemUserList.contains(
                    organizationExamFeeItemDetail.getExamItemUserId())) {
                examItemUserList.add(
                        organizationExamFeeItemDetail.getExamItemUserId());
            }
            redisService.set(
                    RedisConstant.br_order_orgExamFeeItemDetail_feeItemId
                            .concat(organizationExamFeeItemDetail
                                    .getExamFeeItemId()),
                    JSONObject.toJSONString(examItemUserList));
            //缓存检查项目id
            List<String> examFeeItemList = new ArrayList<String>();
            if (redisService
                    .exists(RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                            .concat(organizationExamFeeItemDetail
                                    .getExamItemUserId()))) {
                examFeeItemList = JSONObject.parseArray(
                        redisService
                                .get(RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                                        .concat(organizationExamFeeItemDetail
                                                .getExamItemUserId())),
                        String.class);
            }
            if (!examFeeItemList.contains(
                    organizationExamFeeItemDetail.getExamFeeItemId())) {
                examFeeItemList.add(
                        organizationExamFeeItemDetail.getExamFeeItemId());
            }
            redisService.set(
                    RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                            .concat(organizationExamFeeItemDetail
                                    .getExamItemUserId()),
                    JSONObject.toJSONString(examFeeItemList));
          //缓存中间表id
          return  redisService.set(RedisConstant.br_order_orgExamFeeItemDetail_id.concat(
                            organizationExamFeeItemDetail
                                    .getExamFeeItemDetailId()),
                    JSONObject.toJSONString(organizationExamFeeItemDetail));
	}

	/**
	* <p>Title: updateOrgExamFeeItemDetail</p> 
	* <p>Description: </p> 
	* @param orgExamFeeItemDetail
	* @return 
	* @see br.order.redis.examfeeitem.OrgExamFeeItemDetailRedis#updateOrgExamFeeItemDetail(br.crm.pojo.examitem.OrganizationExamFeeItemDetail) 
	*/
	@Override
	public String updateOrgExamFeeItemDetail(
			String examFeeItemId) {
		 //缓存收费项目id
        List<String> examItemUserList = new ArrayList<String>();
        if (redisService.exists(RedisConstant.br_order_orgExamFeeItemDetail_feeItemId.concat(examFeeItemId))) {
            examItemUserList = JSONObject.parseArray(
                   redisService .get(RedisConstant.br_order_orgExamFeeItemDetail_feeItemId
                                    .concat(examFeeItemId)),  String.class);
            if (!examItemUserList.contains( examFeeItemId)) {
                examItemUserList.remove(examFeeItemId);
            }
        }
        
        redisService.set(
                RedisConstant.br_order_orgExamFeeItemDetail_feeItemId
                        .concat(examFeeItemId),
                JSONObject.toJSONString(examItemUserList));
        //缓存检查项目id
        List<String> examFeeItemList = new ArrayList<String>();
        if (redisService
                .exists(RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                        .concat(examFeeItemId))) {
            examFeeItemList = JSONObject.parseArray(
                    redisService
                            .get(RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                                    .concat(examFeeItemId)),
                    String.class);
            if (!examFeeItemList.contains(examFeeItemId)) {
                examFeeItemList.remove( examFeeItemId);
            }
        }
        
        redisService.set(
                RedisConstant.br_order_orgExamFeeItemDetail_examItemUserId
                        .concat(examFeeItemId),
                JSONObject.toJSONString(examFeeItemList));
      //缓存中间表id
      return String.valueOf(redisService.delete(RedisConstant.br_order_orgExamFeeItemDetail_id.concat(examFeeItemId)));
	}

	/**
	* <p>Title: SelectOrgExamFeeItemDetailByPrimaryKey</p> 
	* <p>Description: </p> 
	* @param examFeeItemDetailId
	* @return 
	* @see br.order.redis.examfeeitem.OrgExamFeeItemDetailRedis#SelectOrgExamFeeItemDetailByPrimaryKey(java.lang.String) 
	*/
	@Override
	public OrganizationExamFeeItemDetail selectOrgExamFeeItemDetailByPrimaryKey(
			String examFeeItemDetailId) {
		return  JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamFeeItemDetail_id.concat(examFeeItemDetailId))
				,OrganizationExamFeeItemDetail.class);
		
	}  

}
