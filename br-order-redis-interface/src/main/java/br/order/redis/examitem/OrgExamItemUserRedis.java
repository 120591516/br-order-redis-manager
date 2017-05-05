package br.order.redis.examitem;

import java.util.List;

import br.crm.pojo.examitem.OrganizationExamItemUser;

public interface OrgExamItemUserRedis {
    void initData();

    int setOrgExamItemUser(OrganizationExamItemUser orgExamItemUser);

    OrganizationExamItemUser getOrgExamItemUser(String orgExamItemUserId);

    int deleteOrgExamItemUser(String orgExamItemUserId);

    List<String> getUserListByItem(String itemId);

    List<String> getItemListByUser(String userId);
}
