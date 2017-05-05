/**   
* @Title: OrgBranchSuiteRedis.java 
* @Package OrgBranchSuiteService 
* @Description: TODO
* @author kangting   
* @date 2017年1月6日 下午1:49:22 
* @version V1.0   
*/
package br.order.redis.suite;

import java.util.List;

import br.crm.pojo.suite.OrganizationBranchSuite;
import br.crm.vo.branch.OrganizationBranchVo;
import br.crm.vo.suite.OrgExamSuiteVo;

/** 
 * @ClassName: OrgBranchSuiteRedis 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月6日 下午1:49:22 
 *  
 */
public interface OrgBranchSuiteRedis {

    void initData();

    OrganizationBranchSuite getOrgBranchSuiteById(String branchSuiteId);

    List<OrgExamSuiteVo> getOrgBranchSuiteByBranchId(String branchId);

    List<OrganizationBranchVo> getOrgBranchSuiteBySuiteId(String suiteId);

    String insertOrgBranchSuite(OrganizationBranchSuite orgBranchSuite);

    String updateOrgBranchSuite(OrganizationBranchSuite orgBranchSuite);

    List<String> getBranchSuiteBySuitId(String suiteId);
}
