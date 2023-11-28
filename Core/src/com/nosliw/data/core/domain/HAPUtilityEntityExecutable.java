package com.nosliw.data.core.domain;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
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
	public static void traverseExecutableEntityTree(HAPExecutableEntityComplex complexEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		traverseExecutableTree(
			complexEntity, 
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
	public static void traverseExecutableTree(HAPExecutableEntityComplex complexEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		processor.processComplexRoot(complexEntity, processContext);
		traverseExecutableTreeLeaves(complexEntity, processor, processContext);
		processor.postProcessComplexRoot(complexEntity, processContext);
	}
	
	private static void traverseExecutableTreeLeaves(HAPExecutableEntity parentEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 
		List<HAPAttributeEntityExecutable> attrsExe = parentEntity.getAttributes();
		for(HAPAttributeEntityExecutable attrExe : attrsExe) {
			if(processor.processAttribute(parentEntity, attrExe.getName(), processContext)) {
				HAPEmbededExecutable embeded = attrExe.getValue();
				Object attrValue = embeded.getValue();
				if(attrValue instanceof HAPExecutableEntity) {
					traverseExecutableTreeLeaves((HAPExecutableEntity)attrValue, processor, processContext);
				}
			}
			processor.postProcessAttribute(parentEntity, attrExe.getName(), processContext);
		}
	}
}

abstract class HAPProcessorEntityExecutableWrapper extends HAPProcessorEntityExecutableDownward{

	private HAPProcessorEntityExecutableDownward m_processor;
	
	public HAPProcessorEntityExecutableWrapper(HAPProcessorEntityExecutableDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPAttributeEntityExecutable attr);
	
	@Override
	public void processComplexRoot(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		this.m_processor.processComplexRoot(complexEntity, processContext);
	}

	@Override
	public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
		HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
		if(this.isValidAttribute(attr)) {
			return this.m_processor.processAttribute(parentEntity, attribute, processContext);
		}
		return false;
	}

	@Override
	public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
		HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
		if(this.isValidAttribute(attr)) {
			this.m_processor.postProcessAttribute(parentEntity, attribute, processContext);
		}
	}

	@Override
	public void postProcessComplexRoot(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		this.m_processor.postProcessComplexRoot(complexEntity, processContext);
	}
	
}

