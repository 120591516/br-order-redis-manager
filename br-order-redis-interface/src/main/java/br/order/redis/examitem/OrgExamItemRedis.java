package br.order.redis.examitem;

import br.crm.pojo.examitem.OrganizationExamItem;

public interface OrgExamItemRedis {
	void initData();
	
	OrganizationExamItem getOrgExamItemById(String orgExamItemId);
	
	String updateOrgExamItem(OrganizationExamItem orgExamItem);
	
	String insertOrgExamItem(OrganizationExamItem orgExamItem);
	

}
