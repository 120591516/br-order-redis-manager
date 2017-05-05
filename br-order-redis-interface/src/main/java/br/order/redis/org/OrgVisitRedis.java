package br.order.redis.org;

import br.crm.pojo.org.OrganizationVisit;

public interface OrgVisitRedis {
    void initData();

    int setOrgVisit(OrganizationVisit organizationVisit);

    OrganizationVisit getOrgVisit(Long orgVisitId);

    int deleteOrgVisit(Long orgVisitId);
}
