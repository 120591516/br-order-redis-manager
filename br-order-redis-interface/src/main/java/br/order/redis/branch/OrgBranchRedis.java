package br.order.redis.branch;

import br.crm.pojo.branch.OrganizationBranch;

public interface OrgBranchRedis {
    void initData();

    int setOrgBranch(OrganizationBranch organizationBranch);

    OrganizationBranch getOrgBranch(String branchId);

    int deleteOrgBranch(String branchId);

}
