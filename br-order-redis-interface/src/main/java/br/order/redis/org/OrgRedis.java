package br.order.redis.org;

import br.crm.vo.org.RegistOrgInfoVo;

public interface OrgRedis {
    void initData();

    RegistOrgInfoVo getOrganization(String orgId);

    String setOrganization(RegistOrgInfoVo organization);

    int deleteOrganization(String orgId);

    Boolean redisExists(String orgId);
}
