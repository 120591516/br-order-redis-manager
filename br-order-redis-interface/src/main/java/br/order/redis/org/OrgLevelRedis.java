package br.order.redis.org;

import br.crm.pojo.org.OrganizationLevel;

public interface OrgLevelRedis {
    void initData();
    
    OrganizationLevel getOrganizationLevel(String orgLevelId);
    
    int setOrganizationLevel(OrganizationLevel organizationLevel);
}
