package br.order.redis.examfeeitem;

import br.crm.pojo.examfeeitem.OrganizationExamFeeItem;

public interface OrgExamFeeItemRedis {
	void initData();
	
	String insertOrganizationExamFeeItem(OrganizationExamFeeItem organizationExamFeeItem);

    OrganizationExamFeeItem getOrganizationExamFeeItemById(String id);

    String updateOrganizationExamFeeItemById(OrganizationExamFeeItem organizationExamFeeItem);

}
