/**   
* @Title: OrgExamFeeItemSuiteRedis.java 
* @Package br.order.redis.suite 
* @Description: TODO
* @author kangting   
* @date 2017年1月9日 上午10:41:47 
* @version V1.0   
*/
package br.order.redis.suite;

import java.util.List;

 

import br.crm.pojo.suite.OrganizationExamSuiteFeeItem;
import br.crm.vo.suite.OrgExamSuiteVo;

/** 
 * @ClassName: OrgExamFeeItemSuiteRedis 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月9日 上午10:41:47 
 *  
 */
public interface OrgExamFeeItemSuiteRedis {
	
	void initData(); 
	
	List<OrgExamSuiteVo> getOrgBranchSuiteBySuiteId(String suiteId); 
	
	OrganizationExamSuiteFeeItem getOrgExamFeeItemSuiteById(String examFisId);
	
	String insertOrgExamFeeItemSuite(OrganizationExamSuiteFeeItem orgExamSuiteFeeItem);
	
	String updateOrgExamFeeItemSuite(OrganizationExamSuiteFeeItem orgExamSuiteFeeItem);
}
