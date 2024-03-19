package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPUtilityBrick {

	public static HAPTreeNode getDescdentTreeNode(HAPWrapperBrick rootBrickWrapper, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootBrickWrapper;
		}
		else {
			out = getDescendantAttribute(rootBrickWrapper.getBrick(), path);
		}
		return out;
	}
	
	public static HAPBrick getDescdentBrick(HAPWrapperBrick rootBrickWrapper, HAPPath path) {
		HAPBrick out = null;
		if(path==null || path.isEmpty()) {
			out = rootBrickWrapper.getBrick();
		}
		else {
			out = getDescendantBrick(rootBrickWrapper.getBrick(), path); 
		}
		return out;
	}
	
	public static HAPAttributeInBrick getDescendantAttribute(HAPBrick brick, HAPPath path) {
		HAPAttributeInBrick out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = brick.getAttribute(attribute);
			} else {
				HAPWrapperValueInAttribute attrValueInfo = out.getValueWrapper();
				if(attrValueInfo instanceof HAPWithBrick) {
					out = ((HAPWithBrick)attrValueInfo).getBrick().getAttribute(attribute);
				}
				else{
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	private static HAPBrick getDescendantBrick(HAPBrick brick, HAPPath path) {
		HAPBrick out = null;
		if(path==null||path.isEmpty()) {
			out = brick;
		} else {
			HAPWrapperValueInAttribute attrValueInfo = getDescendantAttribute(brick, path).getValueWrapper();
			if(attrValueInfo instanceof HAPWithBrick) {
				out = ((HAPWithBrick)attrValueInfo).getBrick();
			}
		}
		return out;
	}
	
	
	
	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManagerApplicationBrick brickAppMan) {
		return brickAppMan.getBrickTypeInfo(brickTypeId).getIsComplex();
	}
	
	public static HAPIdBrick parseBrickIdAgressive(Object obj, String defaultDivision, HAPManagerApplicationBrick brickMan) {
		HAPIdBrick out = new HAPIdBrick();
		
		if(obj instanceof String) {
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		
		out.setBrickTypeId(normalizeBrickTypeId(out.getBrickTypeId(), brickMan));
		if(out.getDivision()==null) {
			out.setDivision(defaultDivision);
		}
		
		return out;
	}
	
	public static HAPIdBrickType parseBrickTypeIdAggresive(Object obj, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType brickTypeId = parseBrickTypeId(obj);
		return normalizeBrickTypeId(brickTypeId, brickMan);
	}
	
	public static HAPIdBrickType parseBrickTypeId(Object obj) {
		HAPIdBrickType out = null;
		if(obj instanceof String) {
			out = new HAPIdBrickType((String)obj);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdBrickType();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPIdBrickType normalizeBrickTypeId(HAPIdBrickType brickTypeId, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType out = brickTypeId;
		if(out.getVersion()==null) {
			out = brickMan.getLatestVersion(brickTypeId.getBrickType());
		}
		return out;
		
	}

	public static HAPIdBrick parseBrickId(Object obj) {
		HAPIdBrick out = null;
		if(obj instanceof String) {
			out = new HAPIdBrick();
			out.buildObject(obj, HAPSerializationFormat.LITERATE);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdBrick();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		else if(obj instanceof HAPResourceIdSimple) {
			out = fromResourceId2BrickId((HAPResourceIdSimple)obj);
		}
		return out;
	}

	public static HAPIdBrick fromResourceId2BrickId(HAPResourceIdSimple resourceId) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(resourceId.getId());
		return new HAPIdBrick(new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion()), segs.length>1?segs[1]:null, segs[0]);
	}
	
	public static HAPResourceIdSimple fromBrickId2ResourceId(HAPIdBrick brickId) {
		return new HAPResourceIdSimple(brickId.getBrickTypeId().getBrickType(), brickId.getBrickTypeId().getVersion(), HAPUtilityNamingConversion.cascadeLevel1(brickId.getId(), brickId.getDivision()));
	}

	public static HAPIdBrickType getBrickTypeIdFromResourceId(HAPResourceId resourceId) {
		return new HAPIdBrickType(resourceId.getResourceType(), resourceId.getVersion());
	}
	
}
