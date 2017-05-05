package br.order.redis.org;

import br.crm.pojo.org.OrganizationSoft;

public interface OrgSoftRedis {
    void initData();

    int setOrgSoft(OrganizationSoft organizationSoft);

    OrganizationSoft getOrgSoft(Long orgSoftId);

    int deleteOrgSoft(Long orgSoftId);
}
