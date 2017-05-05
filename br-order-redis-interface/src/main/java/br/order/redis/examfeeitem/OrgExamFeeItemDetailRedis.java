/**   
* @Title: OrgExamFeeItemDetailRedis.java 
* @Package br.order.redis.examfeeitem 
* @Description: TODO
* @author kangting   
* @date 2017年1月9日 下午3:29:45 
* @version V1.0   
*/
package br.order.redis.examfeeitem; 

import br.crm.pojo.examitem.OrganizationExamFeeItemDetail;

/** 
 * @ClassName: OrgExamFeeItemDetailRedis 
 * @Description: TODO
 * @author kangting
 * @date 2017年1月9日 下午3:29:45 
 *  
 */
public interface OrgExamFeeItemDetailRedis {
	
    void initData(); 
	
    String insertOrgExamFeeItemDetail(OrganizationExamFeeItemDetail orgExamFeeItemDetail); 
    
    String updateOrgExamFeeItemDetail(String examFeeItemId);
    
    OrganizationExamFeeItemDetail selectOrgExamFeeItemDetailByPrimaryKey(String examFeeItemDetailId);
    
}
