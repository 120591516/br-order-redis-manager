/**   
* @Title: OrgExamSuiteImgRedis.java 
* @Package br.order.redis.suite 
* @Description: TODO
* @author kangting   
* @date 2017年2月10日 上午11:29:29 
* @version V1.0   
*/
package br.order.redis.suite;

import java.util.List;

import br.crm.pojo.suite.OrganizationExamSuiteImg;

/** 
 * @ClassName: OrgExamSuiteImgRedis 
 * @Description: TODO
 * @author kangting
 * @date 2017年2月10日 上午11:29:29 
 *  
 */
public interface OrgExamSuiteImgRedis {
	void initData();  

    int setOrgExamSuiteImg(OrganizationExamSuiteImg orgExamImgId);

    OrganizationExamSuiteImg getOrgExamSuiteImg(String examSuiteImgId);

    int deleteOrgExamSuiteImg(String examSuiteImgId);

    List<String> getImgIdsBySuiteId(String suiteId);

    List<String> getSuiteIdsByImgId(String imgId);
}
