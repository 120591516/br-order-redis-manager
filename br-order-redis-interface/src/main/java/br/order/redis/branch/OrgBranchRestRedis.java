package br.order.redis.branch;

import java.util.List;

import br.crm.pojo.branch.OrganizationBranchRest;

public interface OrgBranchRestRedis {
    void initData();

    int setOrgBranchRest(OrganizationBranchRest organizationBranchRest);

    OrganizationBranchRest getOrgBranchRest(String branchRestId);

    int deleteOrgBranchRest(String branchRestId);

    List<String> getBranchRestByBranch(String branchId);
}
