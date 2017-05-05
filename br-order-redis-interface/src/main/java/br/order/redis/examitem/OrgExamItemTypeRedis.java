package br.order.redis.examitem;

import br.crm.pojo.examitem.OrganizationExamItemType;

public interface OrgExamItemTypeRedis {
    void initData();

    int setOrgExamItemType(OrganizationExamItemType organizationExamItemType);

    OrganizationExamItemType getOrgExamItemType(String examItemTypeId);

    int deleteOrgExamItemType(String examItemTypeId);
}
