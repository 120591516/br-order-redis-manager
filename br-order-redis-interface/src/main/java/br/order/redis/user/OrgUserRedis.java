package br.order.redis.user;

import java.util.List;

import br.crm.pojo.permission.OrganizationUser;

public interface OrgUserRedis {
    void initData();

    int setOrgUser(OrganizationUser organizationUser);

    OrganizationUser getOrgUser(String userId);

    int deleteOrgUser(String userId);

    List<String> getOrgUserListByOrg(String orgId);

    List<String> getOrgUserListByBranch(String branchId);

    List<String> getOrgUserListByDept(String deptId);
}
