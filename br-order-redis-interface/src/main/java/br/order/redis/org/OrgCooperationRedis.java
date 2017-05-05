package br.order.redis.org;

import br.crm.pojo.org.OrganizationCooperation;

public interface OrgCooperationRedis {
    void initData();
    
    int setOrgCooperation(OrganizationCooperation organizationCooperation);
    
    OrganizationCooperation getOrgCooperation(Long orgCooperationId);
    
    int deleteOrgCooperation(Long orgCooperationId);
}
