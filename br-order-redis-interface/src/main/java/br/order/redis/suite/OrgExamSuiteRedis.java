package br.order.redis.suite;

import br.crm.vo.suite.OrgExamSuiteVo;

public interface OrgExamSuiteRedis {
	String updateOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo);

	String insertOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo);

	OrgExamSuiteVo getOrgExamSuiteById(String idOrgExamSuite);

	Long delectOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo); 
	
	void initData();


}
