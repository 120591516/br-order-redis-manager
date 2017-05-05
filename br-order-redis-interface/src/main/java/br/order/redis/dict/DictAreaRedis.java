package br.order.redis.dict;

import java.util.List;

import br.crm.pojo.dict.DictArea;

public interface DictAreaRedis {
    void initData();
    
    List<DictArea> getCityByProvinceId(Integer id);
    
    List<DictArea> getDistrictByCityId(Integer id);
    
    Integer setProvince(DictArea dictArea);
    
    Integer setCity(DictArea dictArea);
    
    Integer setDistrict(DictArea dictArea);
    
    DictArea getDictAreaByAreaId(Integer areaId);

	Integer setIp(String ip, String city);

	String isExistenceIp(String ip);
    
}
