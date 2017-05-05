package br.order.redis.org;

import br.crm.pojo.org.OrganizationInvest;

public interface OrgInvestRedis {
    void initData();

    int setOrgInvest(OrganizationInvest organizationInvest);

    OrganizationInvest getOrgInvest(Long orgInvestId);

    int deleteOrgInvest(Long orgInvestId);
}
