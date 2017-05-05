package br.order.redis.impl.dict;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.RedisConstant;
import br.crm.pojo.dict.DictArea;
import br.crm.service.branch.OrgBranchService;
import br.crm.service.dict.DictAreaService;
import br.order.redis.dict.DictAreaRedis;
import br.order.redis.redis.RedisService;

/**
 * (省市区字典表缓存接口实现)
 * 
 * @ClassName: DictAreaRedisImpl
 * @Description: TODO
 * 
 * @author 王文腾
 * @date 2017年1月9日 上午9:19:01
 */
@Service
public class DictAreaRedisImpl implements DictAreaRedis {

	@Autowired
	private OrgBranchService orgBranchService;

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
	private DictAreaService dictAreaService;

	@Override
	public void initData() {
		List<DictArea> list = dictAreaService.getAllCity();
		if (CollectionUtils.isNotEmpty(list)) {
			for (DictArea dictArea : list) {
				if (!redisService.exists(RedisConstant.br_order_dict_area_id + dictArea.getId())) {
					redisService.set(RedisConstant.br_order_dict_area_id + dictArea.getId(), JSONObject.toJSONString(dictArea));
				}
				if (dictArea.getAreaLevel().intValue() == 1) {
					// 省集合
					Set<DictArea> provinceList = new HashSet<DictArea>();
					if (redisService.exists(RedisConstant.br_order_dict_area_provinceList)) {
						List<DictArea> list2 = JSONObject.parseArray(redisService.get(RedisConstant.br_order_dict_area_provinceList), DictArea.class);
						if (CollectionUtils.isNotEmpty(list2)) {
							for (DictArea area : list2) {
								provinceList.add(area);
							}
						}
						if (!provinceList.contains(dictArea)) {
							provinceList.add(dictArea);
						}
					}
					redisService.set(RedisConstant.br_order_dict_area_provinceList, JSONObject.toJSONString(provinceList));
				}
				// 根据省ID存储市集合
				Set<DictArea> cityList = new HashSet<DictArea>();
				if (dictArea.getAreaLevel().intValue() == 2) {
					if (redisService.exists(RedisConstant.br_order_dict_area_provinceId + dictArea.getProvinceId())) {
						List<DictArea> list2 = JSONObject.parseArray(redisService.get(RedisConstant.br_order_dict_area_provinceId + dictArea.getProvinceId()), DictArea.class);
						if (CollectionUtils.isNotEmpty(list2)) {
							for (DictArea area : list2) {
								cityList.add(area);
							}
						}
					}
					if (!cityList.contains(dictArea)) {
						cityList.add(dictArea);
					}
					redisService.set(RedisConstant.br_order_dict_area_provinceId + dictArea.getProvinceId(), JSONObject.toJSONString(cityList));
				}
				// 根据市ID存储区县集合
				List<DictArea> districtList = new LinkedList<DictArea>();
				if (dictArea.getAreaLevel().intValue() == 3) {
					if (redisService.exists(RedisConstant.br_order_dict_area_cityId + dictArea.getCityId())) {
						districtList = JSONObject.parseArray(redisService.get(RedisConstant.br_order_dict_area_cityId + dictArea.getCityId()), DictArea.class);
					}
					if (!districtList.contains(dictArea)) {
						districtList.add(dictArea);
					}
					redisService.set(RedisConstant.br_order_dict_area_cityId + dictArea.getCityId(), JSONObject.toJSONString(districtList));
				}
			}

		}
		// 初始化门店中市id的
		Set<String> branchIdList = orgBranchService.getCityByBranch();
		redisService.set(RedisConstant.br_order_user_city_list, JSONObject.toJSONString(branchIdList));
	}

	/**
	 * <p>
	 * Title:getCityByProvinceId
	 * </p>
	 * <p>
	 * Description:根据省id获取市信息
	 * </p>
	 * 
	 * @param id
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#getCityByProvinceId(java.lang.Integer)
	 */
	@Override
	public List<DictArea> getCityByProvinceId(Integer id) {
		List<DictArea> list = null;
		if (redisService.exists(RedisConstant.br_order_dict_area_provinceId.concat(id.toString()))) {
			list = JSONObject.parseArray(redisService.get(RedisConstant.br_order_dict_area_provinceId.concat(id.toString())), DictArea.class);
		}
		return list;
	}

	/**
	 * <p>
	 * Title:getDistrictByCityId
	 * </p>
	 * <p>
	 * Description:根据cityid获取县
	 * </p>
	 * 
	 * @param id
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#getDistrictByCityId(java.lang.Integer)
	 */
	@Override
	public List<DictArea> getDistrictByCityId(Integer id) {
		List<DictArea> list = null;
		if (redisService.exists(RedisConstant.br_order_dict_area_cityId.concat(id.toString()))) {
			list = JSONObject.parseArray(redisService.get(RedisConstant.br_order_dict_area_cityId.concat(id.toString())), DictArea.class);
		}
		return list;
	}

	/**
	 * <p>
	 * Title:setProvince
	 * </p>
	 * <p>
	 * Description: 添加省
	 * </p>
	 * 
	 * @param dictArea
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#setProvince(br.crm.pojo.dict.DictArea)
	 */
	@Override
	public Integer setProvince(DictArea dictArea) {
		redisService.set(RedisConstant.br_order_dict_area_provinceId.concat(dictArea.getProvinceId().toString()), JSONObject.toJSONString(dictArea));
		return 1;
	}

	/**
	 * <p>
	 * Title:setCity
	 * </p>
	 * <p>
	 * Description:添加市
	 * </p>
	 * 
	 * @param dictArea
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#setCity(br.crm.pojo.dict.DictArea)
	 */
	@Override
	public Integer setCity(DictArea dictArea) {
		redisService.set(RedisConstant.br_order_dict_area_cityId.concat(dictArea.getCityId().toString()), JSONObject.toJSONString(dictArea));
		return 1;
	}

	/**
	 * <p>
	 * Title:setDistrict
	 * </p>
	 * <p>
	 * Description: 添加县
	 * </p>
	 * 
	 * @param dictArea
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#setDistrict(br.crm.pojo.dict.DictArea)
	 */
	@Override
	public Integer setDistrict(DictArea dictArea) {
		redisService.set(RedisConstant.br_order_dict_area_districtName.concat(dictArea.getDistrictName()), JSONObject.toJSONString(dictArea));
		return 1;
	}

	/**
	 * <p>
	 * Title:getDictAreaByAreaId
	 * </p>
	 * <p>
	 * Description:根据areaId 获取省市区信息
	 * </p>
	 * 
	 * @param areaId
	 * @return
	 * @see br.order.redis.dict.DictAreaRedis#getDictAreaByAreaId(int)
	 */
	@Override
	public DictArea getDictAreaByAreaId(Integer areaId) {
		DictArea dictArea = null;
		if (redisService.exists(RedisConstant.br_order_dict_area_id + areaId)) {
			dictArea = JSONObject.parseObject(redisService.get(RedisConstant.br_order_dict_area_id + areaId), DictArea.class);
		}
		return dictArea;
	}

	// 存储ip
	@Override
	public Integer setIp(String ip, String city) {

		redisService.set(ip, city,86400);

		return 1;
	}

	// 根据ip拿城市
	@Override
	public String isExistenceIp(String ip) {

		if (redisService.exists(ip)) {
			return redisService.get(ip);
		}
		return null;
	}

}
