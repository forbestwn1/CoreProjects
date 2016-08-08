package com.nosliw.entity.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.HAPData;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * store complex entity path information
 * attribute path may go through some referenced object which make things complex
 * therefore, one entity path may be divided into some segment each represent on solid entity path
 */
public class HAPEntityPathInfo implements HAPStringable{
	//original entity path --- entity + path
	private HAPEntityPath m_entityPath;

	//calculated out : entity path segments
	private List<HAPEntityPathSegment> m_pathSegments;
	//calculated out : data calculated out based on the entity path
	private HAPData m_data;
	
	private HAPEntityDataAccess m_dataAccess;	
	
	public HAPEntityPathInfo(HAPEntityPath entityPath, HAPEntityDataAccess dataAccess){
		this.m_entityPath = entityPath;
		this.m_dataAccess = dataAccess;
		m_pathSegments = new ArrayList<HAPEntityPathSegment>();
		
		this.process();
	}

	public HAPData getData(){return this.m_data;}

	public String getPath(){return this.m_entityPath.getPath();}
	
	public HAPEntityPath getEntityPath(){return this.m_entityPath;}
	
	public Set<HAPEntityID> getRelatedIDs(){
		Set<HAPEntityID> out = new HashSet<HAPEntityID>();
		for(HAPEntityPathSegment seg : this.m_pathSegments){
			if(seg.getEntityID()!=null){
				out.add(seg.getEntityID());
			}
		}
		return out;
	}
	
	private List<HAPEntityPathSegment> process(){
		HAPEntityPathSegment segment = new HAPEntityPathSegment(this.m_entityPath.getEntityID(), this.m_entityPath.getPath(), this.m_entityPath.getPath());
		this.m_pathSegments.add(segment);
		m_data = this.processSegment(0);
		return this.m_pathSegments;
	}
	
	private HAPData processSegment(int index){
		HAPEntityPathSegment part = this.m_pathSegments.get(index);
		String expectPath = part.getExpectPath();
		part.setPath(part.getExpectPath());
		
		if(part.getEntityID()!=null && part.getEntityWraper()==null){
			part.setEntityWraper((HAPEntityWraper)m_dataAccess.readEntityByID(part.getEntityID()).getData());
		}
		
		if(HAPBasicUtility.isStringEmpty(expectPath)){
			return part.getEntityWraper().getData();
		}
		
		HAPDataWraper wraper = part.getEntityWraper();
		HAPSegmentParser pathSegs = new HAPSegmentParser(expectPath);
		while(pathSegs.hasNext()){
			String attribute = pathSegs.next();
			wraper = wraper.getChildWraperByPath(attribute);
			if(HAPConstant.CONS_DATATYPE_CATEGARY_REFERENCE.equals(wraper.getDataTypeDefInfo().getCategary())){
				//entity reference attribute, create another entity attribute segment for it whether reference have data
				part.setPath(pathSegs.getPreviousPath());
				String restPath = pathSegs.getRestPath();
				if(wraper.getData()!=null){
					HAPEntityPathSegment nextPart = new HAPEntityPathSegment(((HAPReferenceWraper)wraper).getIDData(), 
																					restPath, restPath );
					this.m_pathSegments.add(nextPart);
					return processSegment(this.m_pathSegments.size()-1);
				}
				else{
					HAPEntityPathSegment nextPart = new HAPEntityPathSegment(null, restPath, restPath );
					this.m_pathSegments.add(nextPart);
					return null;
				}
			}
		}
		return wraper.getData();
	}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYPATHINFO_ENTITYPATH, this.m_entityPath.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYPATHINFO_PATHSEGMENTS, HAPJsonUtility.getListObjectJson(this.m_pathSegments));
		if(this.m_data!=null)		jsonMap.put(HAPAttributeConstant.ATTR_ENTITYPATHINFO_DATA, this.m_data.toStringValue(format));
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
}
