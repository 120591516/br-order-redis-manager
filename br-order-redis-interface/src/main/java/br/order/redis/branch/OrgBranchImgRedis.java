package br.order.redis.branch;

import java.util.List;

import br.crm.pojo.branch.OrganizationBranchImg;

public interface OrgBranchImgRedis {
    void initData();

    int setOrgBranchImg(OrganizationBranchImg orgBranchImg);

    OrganizationBranchImg getOrgBranchImg(String orgBranchImgId);

    int deleteOrgBranchImg(String orgBranchImgId);

    List<String> getBranchImgListByBranchId(String branchId);
}
