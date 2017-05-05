/**   
* @Title: OrgExamSuiteImgRedisImpl.java 
* @Package br.order.redis.impl.suite 
* @Description: TODO
* @author kangting   
* @date 2017年2月10日 上午11:31:57 
* @version V1.0   
*/
package br.order.redis.impl.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.detDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.suite.OrganizationExamSuiteImg;
import br.crm.service.suite.OrgExamSuiteImgService;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgExamSuiteImgRedis;

/**
 * @ClassName: OrgExamSuiteImgRedisImpl
 * @Description: TODO
 * @author kangting
 * @date 2017年2月10日 上午11:31:57
 * 
 */
@Service
public class OrgExamSuiteImgRedisImpl implements OrgExamSuiteImgRedis {

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
	private OrgExamSuiteImgService orgExamSuiteImgService;

	/**
	 * <p>
	 * Title: initData
	 * </p>
	 * <p>
	 * Description: 初始化图片
	 * </p>
	 * 
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		List<OrganizationExamSuiteImg> list = orgExamSuiteImgService.getAllOrgExamSuiteImg();
		if (CollectionUtils.isNotEmpty(list)) {
			for (OrganizationExamSuiteImg orgExamImg : list) {
				setOrgExamSuiteImg(orgExamImg);
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
	 * @param orgExamImgHid
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#setOrgExamSuiteHid(br.crm.pojo.suite.OrganizationExamSuiteImg)
	 */
	@Override
	public int setOrgExamSuiteImg(OrganizationExamSuiteImg orgExamImg) {
		// 添加套餐绑定的图片
		List<String> imgList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(orgExamImg.getOrgExamSuiteId()))) {
			imgList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(orgExamImg.getOrgExamSuiteId())), String.class);
		}
		if (!imgList.contains(orgExamImg.getDictImgId())) {
			imgList.add(String.valueOf(orgExamImg.getDictImgId()));
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(orgExamImg.getOrgExamSuiteId()), JSONObject.toJSONString(imgList));
		// 添加高发疾病绑定套餐
		List<String> suitList = new ArrayList<String>();
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(String.valueOf(orgExamImg.getDictImgId())))) {
			suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(orgExamImg.getOrgExamSuiteId())), String.class);
		}
		if (suitList!=null&&!suitList.contains(orgExamImg.getOrgExamSuiteId())) {
			suitList.add(orgExamImg.getOrgExamSuiteId());
		}
		redisService.set(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(String.valueOf(orgExamImg.getDictImgId())), JSONObject.toJSONString(suitList));

		// 添加主表
		String redis = redisService.set(RedisConstant.br_order_orgExamSuiteImg_id.concat(orgExamImg.getOrgExamSuiteImgId()), JSONObject.toJSONString(orgExamImg));
		return redis != null ? 1 : 0;
	}

	/**
	 * <p>
	 * Title: getOrgExamSuiteHid
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param examSuiteImgId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#getOrgExamSuiteHid(java.lang.String)
	 */
	@Override
	public OrganizationExamSuiteImg getOrgExamSuiteImg(String examSuiteImgId) {
		return JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamSuiteImg_id.concat(examSuiteImgId)), OrganizationExamSuiteImg.class);
	}

	/**
	 * <p>
	 * Title: deleteOrgExamSuiteImg
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param examSuiteImgId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#deleteOrgExamSuiteImg(java.lang.String)
	 */
	@Override
	public int deleteOrgExamSuiteImg(String examSuiteImgId) {

		OrganizationExamSuiteImg suiteImg = null;
		if (redisService.exists(RedisConstant.br_order_orgExamSuiteImg_id.concat(examSuiteImgId))) {
			suiteImg = getOrgExamSuiteImg(examSuiteImgId);
		}
		if(suiteImg!=null){
		// 删除套餐绑定的图片
			List<Long> imgList = new ArrayList<Long>();
			if (redisService.exists(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(suiteImg.getOrgExamSuiteId()))) {
				imgList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(suiteImg.getOrgExamSuiteId())), Long.class);
			}
			if (imgList.contains(suiteImg.getDictImgId())) {
				imgList.remove(suiteImg.getDictImgId());
			}
			redisService.set(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(suiteImg.getOrgExamSuiteId()), JSONObject.toJSONString(imgList));
			// 图片绑定套餐
			List<String> suitList = new ArrayList<String>();
			if (redisService.exists(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(String.valueOf(suiteImg.getDictImgId())))) {
				suitList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(String.valueOf(suiteImg.getDictImgId()))), String.class);
			}
			if (suitList!=null&&suitList.contains(suiteImg.getOrgExamSuiteId())) {
				suitList.remove(suiteImg.getOrgExamSuiteId());
			} 
			redisService.set(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(String.valueOf(suiteImg.getDictImgId())), JSONObject.toJSONString(suitList));
		}

		// 删除主表
		return (redisService.delete(RedisConstant.br_order_orgExamSuiteImg_id.concat(examSuiteImgId))).intValue();
	}

	/**
	 * <p>
	 * Title: getImgIdsBySuiteId
	 * </p>
	 * <p>
	 * Description: 套餐绑定图片的
	 * </p>
	 * 
	 * @param suiteId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#getImgIdsBySuiteId(java.lang.String)
	 */
	@Override
	public List<String> getImgIdsBySuiteId(String suiteId) {
		String suiteImgs = redisService.get(RedisConstant.br_order_orgExamSuiteImg_suiteId.concat(suiteId));
		List<String> suiteImgList = JSONObject.parseArray(suiteImgs, String.class);
		return suiteImgList;
	}

	/**
	 * <p>
	 * Title: getSuiteIdsByImgId
	 * </p>
	 * <p>
	 * Description: 图片绑定套餐的
	 * </p>
	 * 
	 * @param imgId
	 * @return
	 * @see br.order.redis.suite.OrgExamSuiteImgRedis#getSuiteIdsByImgId(java.lang.String)
	 */
	@Override
	public List<String> getSuiteIdsByImgId(String imgId) {
		// TODO Auto-generated method stub
		String suiteImgs = redisService.get(RedisConstant.br_order_orgExamSuiteImg_imgid.concat(imgId));
		List<String> suiteImgList = JSONObject.parseArray(suiteImgs, String.class);
		return suiteImgList;
	} 
	
	public static void main(String[] args) {
		

		List<Long> imgListStr = new ArrayList<Long>();
		imgListStr.add((long)123);
		imgListStr.add((long)125);
	
		
		List<String> imgList = new ArrayList<String>();
		 imgList = JSONObject.parseArray(imgListStr.toString(), String.class);
		 System.out.println(imgList.contains( 125));
		if (imgList.contains(  125)) {
			 
			imgList.remove(125);  
		}
		System.out.println(imgList);
	}
}
