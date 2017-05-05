package br.order.redis.dept;

import br.crm.pojo.dept.OrganizationDept;

public interface OrgDeptRedis {
    void initData();

    int setOrgDept(OrganizationDept organizationDept);

    OrganizationDept getOrgDept(String orgDeptId);

    int deleteOrgDept(String orgDeptId);
}
