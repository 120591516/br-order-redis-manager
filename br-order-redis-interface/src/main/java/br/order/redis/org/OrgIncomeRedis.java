package br.order.redis.org;

import br.crm.pojo.org.OrganizationIncome;

public interface OrgIncomeRedis {
    void initData();
    
    int setOrgIncome(OrganizationIncome organizationIncome);
    
    OrganizationIncome getOrgIncome(Long orgIncomeId);
    
    int deleteOrgIncome(Long orgIncomeId);
}
