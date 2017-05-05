package br.order.redis.impl.suite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import br.crm.common.utils.JsonUtils;
import br.crm.common.utils.RedisConstant;
import br.crm.pojo.customer.order.CustomerOrderCart;
import br.crm.pojo.dict.DictImg;
import br.crm.pojo.org.DictExamSuiteType;
import br.crm.pojo.org.DictHighIncidenceDisease;
import br.crm.pojo.suite.OrganizationExamSuite;
import br.crm.pojo.suite.OrganizationExamSuiteHid;
import br.crm.pojo.suite.OrganizationExamSuiteImg;
import br.crm.pojo.suite.OrganizationExamSuiteType;
import br.crm.service.customer.order.CustomerOrderCartService;
import br.crm.service.dict.DictExamSuiteTypeService;
import br.crm.service.dict.DictHighIncidenceDiseaseService;
import br.crm.service.dict.DictImgService;
import br.crm.service.suite.OrgExamSuiteHidService;
import br.crm.service.suite.OrgExamSuiteImgService;
import br.crm.service.suite.OrgExamSuiteService;
import br.crm.service.suite.OrgExamSuiteTypeService;
import br.crm.vo.suite.OrgExamSuiteHidVo;
import br.crm.vo.suite.OrgExamSuiteImgVo;
import br.crm.vo.suite.OrgExamSuiteTypeVo;
import br.crm.vo.suite.OrgExamSuiteVo;
import br.order.redis.dict.DictExamSuiteTypeRedis;
import br.order.redis.dict.DictHighIncidenceDiseaseRedis;
import br.order.redis.dict.DictImgRedis;
import br.order.redis.redis.RedisService;
import br.order.redis.suite.OrgExamSuiteHidRedis;
import br.order.redis.suite.OrgExamSuiteImgRedis;
import br.order.redis.suite.OrgExamSuiteRedis;
import br.order.redis.suite.OrgExamSuiteTypeRedis;

@Service
public class OrgExamSuiteRedisImpl implements OrgExamSuiteRedis {
    @Autowired
    @Qualifier("RedisInnerService")
    private RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    private OrgExamSuiteImgService orgExamSuiteImgService;

    @Autowired
    private OrgExamSuiteService orgExamSuiteService;

    @Autowired
    private CustomerOrderCartService customerOrderCartService;

    @Autowired
    private OrgExamSuiteTypeService orgExamSuiteTypeService;

    @Autowired
    private DictHighIncidenceDiseaseService dictHighIncidenceDiseaseService;

    @Autowired
    private DictExamSuiteTypeService dictExamSuiteTypeService;

    @Autowired
    private OrgExamSuiteHidService orgExamSuiteHidService;

    @Autowired
    private DictImgService dictImgService;
    
    @Autowired
    private  OrgExamSuiteHidRedis orgExamSuiteHidRedis;
    
    @Autowired
    private  OrgExamSuiteTypeRedis orgExamSuiteTypeRedis;
    
    @Autowired
    private  OrgExamSuiteImgRedis orgExamSuiteImgRedis;
    
    @Autowired
    private DictHighIncidenceDiseaseRedis dictHighIncidenceDiseaseRedis;
    
    @Autowired
    private DictImgRedis dictImgRedis;
    
    @Autowired
    private DictExamSuiteTypeRedis  dictExamSuiteTypeRedis;
   

    @Override
    public String updateOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo) {
    	if(orgExamSuiteVo!=null&&orgExamSuiteVo.getExamSuiteId()!=null){
    		delectOrgExamSuite(orgExamSuiteVo);
    		return insertOrgExamSuite(orgExamSuiteVo);
    	}
        return null;
    }

    @Override
    public String insertOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo) {
        // TODO Auto-generated method stub
        return redisService.set(RedisConstant.br_order_orgExamSuite_suiteId.concat(orgExamSuiteVo.getExamSuiteId()), JSONObject.toJSONString(orgExamSuiteVo));
    }

    @Override
    public OrgExamSuiteVo getOrgExamSuiteById(String idOrgExamSuite) {
    	
    	//获取套餐详情页
    	OrgExamSuiteVo suiteVo=JsonUtils.jsonToPojo(redisService.get(RedisConstant.br_order_orgExamSuite_suiteId.concat(idOrgExamSuite)), OrgExamSuiteVo.class);
    	 //获取高发疾病
    	if(suiteVo!=null){
    		List<OrgExamSuiteHidVo> orgExamSuiteHidVo=new ArrayList<OrgExamSuiteHidVo>();
        	List<String> hidList=orgExamSuiteHidRedis.getHidIdsBySuiteId(idOrgExamSuite);
        	if(CollectionUtils.isNotEmpty(hidList)){
        		OrgExamSuiteHidVo  suiteHidVo;
        		String hidName=null;
        		for(String id:hidList){
        			 suiteHidVo=new OrgExamSuiteHidVo(); 
        			if(id!=null){
        				 DictHighIncidenceDisease hid=dictHighIncidenceDiseaseRedis.getDictHighIncidenceDisease(id);
        				 if(hid!=null&&hid.getHighIncidenceDiseaseName()!=null){
        					 hidName=hid.getHighIncidenceDiseaseName();
        				 } 
        				suiteHidVo.setHighIncidenceDiseaseName(hidName);
             			suiteHidVo.setHighIncidenceDiseaseId(id);
             			orgExamSuiteHidVo.add(suiteHidVo);
        			}
        			
        		} 
        		if(CollectionUtils.isNotEmpty(orgExamSuiteHidVo)){
        			suiteVo.setOrgExamSuiteHidVo(orgExamSuiteHidVo);
        		} 
        	} 

        	//获取图片
        	List<OrgExamSuiteImgVo> suiteImgVoList=new ArrayList<OrgExamSuiteImgVo>();
        	List<String> ImgList=orgExamSuiteImgRedis.getImgIdsBySuiteId(idOrgExamSuite);
        	if(CollectionUtils.isNotEmpty(ImgList)){
        		OrgExamSuiteImgVo suiteImgVo;
        		String ImgLocation=null;
        		for(String id:ImgList){
        			suiteImgVo=new OrgExamSuiteImgVo();
        			if(id!=null){
        				DictImg img=dictImgRedis.getDictImg(Long.valueOf(id));
        				if(img!=null&&img.getImgLocation()!=null){
        					ImgLocation=img.getImgLocation();
        				} 
        				suiteImgVo.setDictImgId(Long.valueOf(id));
            			suiteImgVo.setImgLocation(ImgLocation);
            			suiteImgVoList.add(suiteImgVo);
        			} 
        		} 
        		if(CollectionUtils.isNotEmpty(suiteImgVoList)){
        			suiteVo.setOrgExamSuiteImgVo(suiteImgVoList);
        		} 
        	}  
        	//获取套餐类型
        	List<OrgExamSuiteTypeVo> suiteTypeVoList=new ArrayList<OrgExamSuiteTypeVo>();
        	List<String> typeList=orgExamSuiteTypeRedis.getSuiteTypeIdBySuite(idOrgExamSuite);
        	if(CollectionUtils.isNotEmpty(typeList)){
        		OrgExamSuiteTypeVo suiteTypeVo;
        		String typeName=null;
        		for(String id:typeList){
        			suiteTypeVo=new OrgExamSuiteTypeVo();
        			if(id!=null){
        				DictExamSuiteType type=dictExamSuiteTypeRedis.getDictExamSuiteType(id);
        				if(type!=null&&type.getExamTypeName()!=null){
        					typeName=type.getExamTypeName();
        				} 
        				suiteTypeVo.setExamSuiteTypeId(id);
            			suiteTypeVo.setExamTypeName(typeName);
            			suiteTypeVoList.add(suiteTypeVo);
        			}
        			
        		} 
        		if(CollectionUtils.isNotEmpty(suiteTypeVoList)){
        			suiteVo.setOrgExamSuiteTypeVo(suiteTypeVoList);
        		}
        	}
            return suiteVo;
    	}
    	return null;

    }

    @Override
    public Long delectOrgExamSuite(OrgExamSuiteVo orgExamSuiteVo) {
        // TODO Auto-generated method stub
    	if(redisService.exists(RedisConstant.br_order_orgExamSuite_suiteId.concat(orgExamSuiteVo.getExamSuiteId()))){
    		return redisService.delete(RedisConstant.br_order_orgExamSuite_suiteId.concat(orgExamSuiteVo.getExamSuiteId()));
		}
        return (long) 0;
    }

    @Override
    public void initData() {
        List<OrganizationExamSuite> list = orgExamSuiteService.getAllOrgExamSuite();
          OrgExamSuiteVo orgExamSuiteVo = null;
        try {
             if (CollectionUtils.isNotEmpty(list)) {
                for (OrganizationExamSuite organizationExamSuite : list) {
                    // 套餐id
                   orgExamSuiteVo = new OrgExamSuiteVo();
                    BeanUtils.copyProperties(orgExamSuiteVo, organizationExamSuite);
                     Integer sum = orgExamSuiteService.sumSuiteNum(organizationExamSuite.getExamSuiteId());
                    if(sum!=null){
                    	 orgExamSuiteVo.setSale_account_sum(sum.toString());
                    }else{
                    	 orgExamSuiteVo.setSale_account_sum(null);
                    } 
                    insertOrgExamSuite(orgExamSuiteVo); 
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
