package com.nosliw.entity.dataaccess;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.utils.HAPEntityUtility;

public class HAPReferenceManager implements HAPStringable{

	Map<String, Set<HAPReferenceInfoAbsolute>> m_parentRefs;
	HAPEntityDataAccess m_dataAccess;
	
	public HAPReferenceManager(HAPEntityDataAccess dataAccess) {
		m_parentRefs = new LinkedHashMap<String, Set<HAPReferenceInfoAbsolute>>();
		this.m_dataAccess = dataAccess;
	}

	private HAPReferenceManager getUnderReferenceManager(){
		if(this.m_dataAccess.getUnderDataAccess()==null)  return null;
		return this.m_dataAccess.getUnderDataAccess().getReferenceManager();
	}
	
	public Set<HAPReferenceInfoAbsolute> getParentReferences(HAPEntityID entityID){
		Set<HAPReferenceInfoAbsolute> out = this.m_parentRefs.get(entityID.toString());
		if(out==null && this.getUnderReferenceManager()!=null){
			out = this.getUnderReferenceManager().getParentReferences(entityID);
		}
		if(out==null) out = new HashSet<HAPReferenceInfoAbsolute>();
		return out;
	}
	
	public Set<HAPEntityWraper> searchByReverseAttribute(HAPEntityID entityID, String reverseAttr){
		Set<HAPEntityWraper> out = new HashSet<HAPEntityWraper>();
		Set<HAPReferenceInfoAbsolute> paths = this.getReferenceSetByChildID(entityID);
		for(HAPReferenceInfoAbsolute path : paths){
			HAPEntityID a = new HAPEntityID(reverseAttr);
			if(path.getEntityID().getEntityType().equals(a.getEntityType())){
				if(Pattern.matches(HAPBasicUtility.wildcardToRegex(a.getAttributePath()), path.getAttrPath())){
					out.add((HAPEntityWraper)this.m_dataAccess.useEntityByID(path.getEntityID()).getData());
				}
			}
		}
		return out;
	}
	
	public void removeAllParentReferences(HAPEntityID entityID){
		this.m_parentRefs.put(entityID.toString(), new HashSet<HAPReferenceInfoAbsolute>());
	}
	
	public void mergeWith(HAPReferenceManager refMan){
		for(String ID : refMan.m_parentRefs.keySet()){
			Set<HAPReferenceInfoAbsolute> refs = refMan.m_parentRefs.get(ID);
			if(refs!=null){
				this.m_parentRefs.put(ID, refs);
			}
		}
	}
	
	private Set<HAPReferenceInfoAbsolute> getReferenceSetByChildID(HAPEntityID childID){
		Set<HAPReferenceInfoAbsolute> childRefs = this.m_parentRefs.get(childID.toString());
		if(childRefs==null){
			childRefs = new HashSet<HAPReferenceInfoAbsolute>();
			if(this.getUnderReferenceManager()!=null){
				Set<HAPReferenceInfoAbsolute> underRefs = this.getUnderReferenceManager().getParentReferences(childID);
				if(underRefs!=null){
					for(HAPReferenceInfoAbsolute p : underRefs){
						childRefs.add(p);
					}
				}
			}
			this.m_parentRefs.put(childID.toString(), childRefs);
		}
		return childRefs;
	}
	
	public void removeParentReference(HAPEntityID childID, HAPEntityID parentID, String parAttrPath){
		Set<HAPReferenceInfoAbsolute> refPaths = this.getReferenceSetByChildID(childID);
		if(refPaths!=null){
			refPaths.remove(new HAPReferenceInfoAbsolute(parentID, parAttrPath));
		}
	}
	
	public void addParentReference(HAPEntityID childID, HAPEntityID parentID, String parAttrPath){
		Set<HAPReferenceInfoAbsolute> refPaths = this.getReferenceSetByChildID(childID);
		refPaths.add(new HAPReferenceInfoAbsolute(parentID, parAttrPath));
	}

	public void addParentReference(HAPReferenceWraper refWraper){
		HAPEntityID childID = refWraper.getIDData();
		if(childID!=null){
			HAPEntityID parentID = refWraper.getRootEntityWraper().getID();
			String parAttrPath = refWraper.getRootEntityAttributePath();
			this.addParentReference(childID, parentID, parAttrPath);
		}
	}

	@Override
	public String toStringValue(String format) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
