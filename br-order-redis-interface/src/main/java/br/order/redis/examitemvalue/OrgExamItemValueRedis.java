package br.order.redis.examitemvalue;

import br.crm.pojo.examitemvalue.OrganizationExamItemValue;

public interface OrgExamItemValueRedis {
	void initData();
	
	OrganizationExamItemValue getOrgExamItemValueById(String orgExamItemValueId);
	
	String insertOrgExamItemValue(OrganizationExamItemValue orgExamItemValue);
	
	String updateOrgExamItemValue(OrganizationExamItemValue orgExamItemValue);
	
}
