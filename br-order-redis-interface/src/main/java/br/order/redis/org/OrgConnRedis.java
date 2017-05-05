package br.order.redis.org;

import br.crm.pojo.org.OrganizationConn;

public interface OrgConnRedis {
    void  initData();
    
    int setOrganizationConn(OrganizationConn organizationConn);
    
    OrganizationConn getOrganizationConn(Long orgConnId);
    
    int deleteOrganizationConn(Long orgConnId);
}

