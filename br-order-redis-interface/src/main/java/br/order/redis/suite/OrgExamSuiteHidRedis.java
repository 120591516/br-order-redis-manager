/**   
* @Title: OrgExamSuiteHidRedis.java 
* @Package br.order.redis.suite 
* @Description: TODO
* @author kangting   
* @date 2017年2月10日 上午9:42:52 
* @version V1.0   
*/
package br.order.redis.suite;

import java.util.List;

import br.crm.pojo.suite.OrganizationExamSuiteHid;

/** 
 * @ClassName: OrgExamSuiteHidRedis 
 * @Description: TODO
 * @author kangting
 * @date 2017年2月10日 上午9:42:52 
 *  
 */
public interface OrgExamSuiteHidRedis {
	 	void initData();

	    int setOrgExamSuiteHid(OrganizationExamSuiteHid orgExamHidHid);

	    OrganizationExamSuiteHid getOrgExamSuiteHid(String examSuiteHidId);

	    int deleteOrgExamSuiteHid(String examSuiteHidId);

	    List<String> getHidIdsBySuiteId(String suiteId);

	    List<String> getSuiteIdsByHidId(String hidIds);
		
}
