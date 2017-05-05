package br.order.redis.suite;

import java.util.List;

import br.crm.pojo.suite.OrganizationExamSuiteType;

public interface OrgExamSuiteTypeRedis {
    void initData();

    int setOrgExamSuiteType(OrganizationExamSuiteType orgExamSuiteType);

    OrganizationExamSuiteType getOrgExamSuiteType(String examSuiteTypeId);

    int deleteOrgExamSuiteType(String examSuiteTypeId);

    List<String> getSuiteTypeIdBySuite(String suiteId);

    List<String> getSuiteBySuiteType(String suiteType);
}
