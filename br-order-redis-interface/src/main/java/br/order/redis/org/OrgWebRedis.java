package br.order.redis.org;

import br.crm.pojo.org.OrganizationWeb;

public interface OrgWebRedis {
    void initData();

    int setOrgWeb(OrganizationWeb organizationWeb);

    OrganizationWeb getOrgWeb(Long orgWebId);

    int deleteOrgWeb(Long orgWebId);
}
