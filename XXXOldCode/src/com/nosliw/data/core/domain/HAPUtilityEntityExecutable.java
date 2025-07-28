package com.nosliw.data.core.domain;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableDownward;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableUpward;
import com.nosliw.data.core.domain.entity.HAPReferenceExternal;

public class HAPUtilityEntityExecutable {

	public static boolean isAttributeLocal(HAPExecutableEntity parentEntity, String attributeName, HAPContextProcessor processContext) {
		Object attrValue = parentEntity.getAttribute(attributeName).getValue().getValue();
		if(attrValue instanceof HAPReferenceExternal) return false;
		return true;
	}

	public static void trasversExecutableEntityTreeUpward(HAPExecutableEntity entity, HAPProcessorEntityExecutableUpward processor, HAPContextProcessor processContext, Object object) {
		HAPPath path = new HAPPath();
		boolean result =  processor.process(entity, null, processContext, object);
		while(result) {
			HAPExecutableEntity parent = entity.getParent();
			if(parent==null)  break;
			else {
				result = processor.process(parent, path.appendSegment(HAPConstantShared.NAME_PARENT), processContext, object);
			}
		}
	}
	
	//traverse only leaves that is local complex entity
	public static void traverseExecutableLocalComplexEntityTree(HAPExecutableEntityComplex complexEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(
			complexEntity, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeEntityExecutable attr) {
					Object attrValue = attr.getValue().getValue();
					if(attrValue instanceof HAPExecutableEntityComplex) {
						return true;
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse only leafs that is complex entity
	public static void traverseExecutableComplexEntityTree(HAPExecutableEntityComplex complexEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		traverseExecutableTree(
			complexEntity, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeEntityExecutable attr) {
					return attr.getValueTypeInfo().getIsComplex();
				}
			}, 
			processContext);
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseExecutableEntityTree(HAPExecutableEntity rootEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		traverseExecutableTree(
			rootEntity, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeEntityExecutable attr) {
					if(attr.isAttributeAutoProcess()) {
						return true;
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseExecutableTree(HAPExecutableEntity rootEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		traverseExecutableTreeLeaves(rootEntity, null, processor, processContext);
	}
	
	private static void traverseExecutableTreeLeaves(HAPExecutableEntity rootEntity, HAPPath path, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		if(path==null)  path = new HAPPath();
		if(processor.processEntityNode(rootEntity, path, processContext)) {
			HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
			HAPExecutableEntity leafEntity = null;
			if(path.isEmpty())  leafEntity = rootEntity;
			else leafEntity = rootEntity.getDescendantEntity(path);
			
			List<HAPAttributeEntityExecutable> attrsExe = leafEntity.getAttributes();
			for(HAPAttributeEntityExecutable attrExe : attrsExe) {
				HAPPath attrPath = path.appendSegment(attrExe.getName());
				HAPEmbededExecutable embeded = attrExe.getValue();
				Object attrValue = embeded.getValue();
				if(attrValue instanceof HAPExecutableEntity) {
					traverseExecutableTreeLeaves(rootEntity, attrPath, processor, processContext);
				}
			}
		}
		processor.postProcessEntityNode(rootEntity, path, processContext);
	}
}

abstract class HAPProcessorEntityExecutableWrapper extends HAPProcessorEntityExecutableDownward{

	private HAPProcessorEntityExecutableDownward m_processor;
	
	public HAPProcessorEntityExecutableWrapper(HAPProcessorEntityExecutableDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPAttributeEntityExecutable attr);
	
	@Override
	public boolean processEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext) {
		if(this.isRoot(path)) {
			return this.m_processor.processEntityNode(rootEntity, path, processContext);
		}
		else {
			HAPAttributeEntityExecutable attr = rootEntity.getDescendantAttribute(path);
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processEntityNode(rootEntity, path, processContext);
			}
			return false;
		}
	}

	@Override
	public void postProcessEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessEntityNode(rootEntity, path, processContext);
		}
		else {
			HAPAttributeEntityExecutable attr = rootEntity.getDescendantAttribute(path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessEntityNode(rootEntity, path, processContext);
			}
		}
	}
}
