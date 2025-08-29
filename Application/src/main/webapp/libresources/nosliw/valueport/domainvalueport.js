//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSet;
	var node_makeObjectWithType;
	var node_createVariableManager;
	var node_createValueStructure;
	var node_createValueStructureVariableInfo;
	var node_createValueStructureElementInfo;
	var node_dataUtility;
	var node_complexEntityUtility;
	var node_createValuePortElementInfo;
	var node_createEmptyValue;
	var node_ServiceInfo;
	var node_valueInVarOperationServiceUtility;

//*******************************************   Start Node Definition  ************************************** 	

//variable domain, it response to value structure domain in complex resource
var nod_createValuePortDomain = function(variableDomainDef){

	//value structure definition domain
	var loc_variableDomainDefinition = variableDomainDef;
	
	var loc_valuePortContainerById = {};

	var loc_valuePortContainerIdIndex = 0;
	
	//variable pool
	var loc_variableMan = nosliw.runtime.getVariableManager();
	
	var loc_out = {
		//create variable group according to
		//value structure complex
		//parent value context
		//return value context id
		creatValuePortContainer : function(valuePortContainerDef, parentValuePortContainerId){
			loc_valuePortContainerIdIndex++;
			var id = loc_valuePortContainerIdIndex+"";
			var parentValuePortContainer = this.getValuePortContainer(parentValuePortContainerId);
			var valuePortContainer; 
			if(valuePortContainerDef!=undefined){
				valuePortContainer = loc_createValuePortContainer(id, valuePortContainerDef, loc_variableDomainDefinition, parentValuePortContainer, loc_variableMan);
			}
			else{
				valuePortContainer = loc_createEmptyValuePortContainer(id, parentValuePortContainer);
			}
			
			loc_valuePortContainerById[valuePortContainer.getId()] = valuePortContainer;
			return valuePortContainer.getId();
		},
		
		getValuePortContainer : function(valuePortContainerId){   return loc_valuePortContainerById[valuePortContainerId];  },

		getVariableDomainDefinition : function(){   return loc_variableDomainDefinition;	},

	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VAIRABLEDOMAIN);

	return loc_out;
};

var loc_createEmptyValuePortContainer = function(id, parentValuePortContainer){
	
	//value context id
	var loc_id = id;
	
	//parent context which some variable can get from
	var loc_parentValuePortContainer = parentValuePortContainer;
	
	var loc_out = {

		getParentValuePortContainer : function(){   return loc_parentValuePortContainer;    },
		
		getId : function(){  return loc_id;   },

		getValueStructure : function(valueStructureRuntimeId){   return loc_parentValuePortContainer==undefined?undefined : loc_parentValuePortContainer.getValueStructure(valueStructureRuntimeId);	}

	};

	return loc_out;	
};


//value context responding to data structure under complex entity
//id id assigned to valuecontext
//
//it has parent group, so that some variable is from parent
var loc_createValuePortContainer = function(id, valuePortContainerDef, variableDomainDef, parentValuePortContainer, variableMan){
	
	var loc_variableMan;

	//value context id
	var loc_id;
	
	//parent context which some variable can get from
	var loc_parentValuePortContainer;
	
	//valueStructures in the context
	var loc_valueStructures = {};
	var loc_valueStructureInfoById = {};
	var loc_valuePortInfoByGroupTypeAndValuePortId = {};

	//value structure id in sequence
	var loc_valueStructureRuntimeIds = [];


	var loc_createSolidValueStructure = function(valueStructureRuntimeId, variableDomainDef, initValue, buildRootEle){

		//build context element first
		var valueStructureElementInfosArray = [];
		
		if(buildRootEle!=false){
			var valueStructureDefId = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_DEFINITIONBYRUNTIME][valueStructureRuntimeId];
			var valueStructureDefinitionInfo = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTUREDEFINITION][valueStructureDefId];
			var roots = valueStructureDefinitionInfo[node_COMMONATRIBUTECONSTANT.STRUCTURE_ROOT];
								
			_.each(roots, function(valueStructureDefRootObj, rootName){
				var valueStructureDefRootEle = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTINSTRUCTURE_DEFINITION];
				
				var info = {
					matchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS],
					reverseMatchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]
				};
				var type = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
				var valueStructureInfo = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				
				if(valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]==node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					//variable placeholder
					valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, node_createEmptyValue(), undefined, undefined, info));
				}
				else{
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE && 
							(valueStructureInfo==undefined||valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL)){
						//physical relative
						{
							//process relative that  refer to element in parent context
							
							var resolveInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_RESOLVEDINFO];
	
							var parentValueStructureRuntimeId = resolveInfo[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_STRUCTUREID];
							var parentValueStructure = loc_parentValuePortContainer.getValueStructure(parentValueStructureRuntimeId);
							
							var resolvePathObj = resolveInfo[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_PATH];
							var resolveRootName = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
							var resolvePath = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
							
							valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, parentValueStructure, node_createValueStructureVariableInfo(resolveRootName, resolvePath), undefined, info));
						}
					}
					else{
						//not relative or logical relative variable
						var defaultValue = initValue!=undefined?initValue[rootName]:undefined;  
						
						var criteria;
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
						else  criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA]; 
						if(criteria!=undefined){
							//app data, if no default, empty variable with wrapper type
							if(defaultValue!=undefined) 	valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
							else  valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
						}
						else{
							//object, if no default, empty variable with wrapper type
							if(defaultValue!=undefined)		valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, defaultValue, "", undefined, info));
							else valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
						}
					}
				}
			});
		}
		
		var valueStructureName = valueStructureRuntimeId;// = valueContextDef[node_COMMONATRIBUTECONSTANT.VALUECONTEXT_VALUESTRUCTURERUNTIMENAMEBYID][valueStructureRuntimeId];

		return loc_createSolidValueStructureWrapper(valueStructureRuntimeId, valueStructureName, node_createValueStructure(id, valueStructureElementInfosArray));
	};	
	
	
	var loc_init = function(id, valuePortContainerDef, variableDomainDef, parentValuePortContainer, variableDomain){
		loc_id = id;
		loc_parentValuePortContainer = parentValuePortContainer;
		loc_variableMan = variableMan;

		_.each(valuePortContainerDef[node_COMMONATRIBUTECONSTANT.CONTAINERVALUEPORTS_VALUEPORTGROUP], function(valuePortGroupDef){
			var valuePortGroupName = valuePortGroupDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
			var valuePortGroupType = valuePortGroupDef[node_COMMONATRIBUTECONSTANT.GROUPVALUEPORTS_GROUPTYPE];
			var belongToGroup = loc_valueStructures[valuePortGroupName];
			if(belongToGroup==undefined){
				belongToGroup = {};
				loc_valueStructures[valuePortGroupName] = belongToGroup;
			}
			
			var valuePortsInfoBelongToGroupType = loc_valuePortInfoByGroupTypeAndValuePortId[valuePortGroupType];
			if(valuePortsInfoBelongToGroupType==undefined){
				valuePortsInfoBelongToGroupType = {};
				loc_valuePortInfoByGroupTypeAndValuePortId[valuePortGroupType] = valuePortsInfoBelongToGroupType;
			}
			
			_.each(valuePortGroupDef[node_COMMONATRIBUTECONSTANT.GROUPVALUEPORTS_VALUEPORT], function(valuePortDef, i){
				var valuePortName = valuePortDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
				var valuePortType = valuePortDef[node_COMMONATRIBUTECONSTANT.VALUEPORT_TYPE];
				var belongToValuePort = belongToGroup[valuePortName];
				if(belongToValuePort==undefined){
					belongToValuePort = {};
					belongToGroup[valuePortName] = belongToValuePort;
				}

				var valuePortInfosBelongValuePortName = valuePortsInfoBelongToGroupType[valuePortName];
				if(valuePortInfosBelongValuePortName==undefined){
					valuePortInfosBelongValuePortName = [];
					valuePortsInfoBelongToGroupType[valuePortName] = valuePortInfosBelongValuePortName;
				}
				valuePortInfosBelongValuePortName.push({
					groupName : valuePortGroupName,
					valuePortName : valuePortName
				});

				_.each(valuePortDef[node_COMMONATRIBUTECONSTANT.VALUEPORT_VALUESTRUCTURE], function(valueStructureRuntimeId, i){
					var valueStructureWrapper;
					if(loc_parentValuePortContainer==undefined || loc_parentValuePortContainer.getValueStructure(valueStructureRuntimeId)==undefined){
						//value structure not found in parent, then build in current group
						var valueStructureRuntime = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURERUNTIME][valueStructureRuntimeId];
						var valueStructureRuntimeInitValue = valueStructureRuntime[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURERUNTIME_INITVALUE];
						var valueStructureRuntimeInfo = valueStructureRuntime[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURERUNTIME_INFO];
						var initMode = valueStructureRuntimeInfo==undefined?undefined:valueStructureRuntimeInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE];
						if(initMode==undefined)   initMode = node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_AUTO;
						
						if(initMode == node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_AUTO){
							//build with all variable
							valueStructureWrapper = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef, valueStructureRuntimeInitValue);
						}
						else{
							//build empty value structure
							valueStructureWrapper = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef, valueStructureRuntimeInitValue, false);
						}
					}
					else{
						//value structure from parent
						valueStructureWrapper = loc_createSoftValueStructureWrapper(valueStructureRuntimeId, loc_parentValuePortContainer);
					}
					belongToValuePort[valueStructureRuntimeId] = valueStructureWrapper;
					loc_valueStructureInfoById[valueStructureRuntimeId] = {
						groupName : valuePortGroupName,
						valuePortName : valuePortName
					};
					loc_valueStructureRuntimeIds.push(valueStructureRuntimeId);
				});
			});
		});
	};
	
	var loc_out = {

		prv_idDebug : nosliw.runtime.getIdService().generateId(),

		getId : function(){  return loc_id;   },
		
		getValueStructures : function(){
			return loc_valueStructures;
		},
		
		createValuePort : function(valuePortGroupName, valuePortName){		return loc_createValuePort(this, valuePortGroupName, valuePortName);		},

		createValuePortByGroupType : function(valuePortGroupType, valuePortName){    
			var valuePortInfo = this.getValuePortInfoByGroupTypeAndValuePortName(valuePortGroupType, valuePortName);
			return this.createValuePort(valuePortInfo.groupName, valuePortInfo.valuePortName);
		},

		getParentValuePortContainer : function(){   return loc_parentValuePortContainer;    },

		getValuePortInfoByGroupTypeAndValuePortName : function(groupType, valuePortName){	return loc_valuePortInfoByGroupTypeAndValuePortId[groupType][valuePortName][0];	},

		getValueStructuresByGroupTypeAndValuePortName : function(groupType, valuePortName){
			var valuePortInfo = this.getValuePortInfoByGroupTypeAndValuePortName(groupType, valuePortName);
			return this.getValueStructuresByGroupNameAndValuePortName(valuePortInfo.groupName, valuePortInfo.valuePortName);
		},

		getValueStructuresByGroupNameAndValuePortName : function(groupName, valuePortName){
			var out = {};
			_.each(loc_valueStructures[groupName][valuePortName], function(valueStructureWrapper, vsId){
				out[vsId] = valueStructureWrapper.getValueStructure();
			});
			return out;	
		},

		getValueStructure : function(valueStructureRuntimeId){
			var wrapper = this.getValueStructureWrapper(valueStructureRuntimeId);
			if(wrapper!=undefined)  return wrapper.getValueStructure();
			else if(loc_parentValuePortContainer!=undefined){
				return loc_parentValuePortContainer.getValueStructure(valueStructureRuntimeId);
			}
		},
		
		getValueStructureWrapper : function(valueStructureRuntimeId){
			var valueStructureInfo = loc_valueStructureInfoById[valueStructureRuntimeId];
			return valueStructureInfo==undefined?undefined : loc_valueStructures[valueStructureInfo.groupName][valueStructureInfo.valuePortName][valueStructureRuntimeId];   
		},
		
		getVariableByName : function(groupType, valuePortName, variableName){
			var out;
			var valueStructures = this.getValueStructuresByGroupTypeAndValuePortName(groupType, valuePortName);
			for(var i in valueStructures){
				var valueStructure = valueStructures[i];
				out = valueStructure.getElement(variableName);
				if(out!=undefined)  return out;
			}
		},
		
		createVariable : function(structureRuntimeId, varPathSeg1, varPathSeg2){
			var valueStructure = this.getValueStructure(structureRuntimeId);
			return valueStructure.createVariable(node_createValueStructureVariableInfo(varPathSeg1, varPathSeg2));
		},

		createVariableById : function(variableIdEntity){
			var variableInfo = node_createValuePortElementInfo(variableIdEntity);
			return this.createVariable(variableInfo.getValueStructureId(), variableInfo.getRootName(), variableInfo.getElementPath());
		},
		
		createVariableByName : function(varName){
			for(var i in loc_valueStructureRuntimeIds){
				var variable = this.createVariable(loc_valueStructureRuntimeIds[i], varName);
				if(variable!=undefined)   return variable;
			}
		},
		
		//
		createResolvedVariable : function(varResolve){
			var valueStructure = this.getValueStructure(varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_STRUCTUREID]);
			return valueStructure.createVariable(node_createValueStructureVariableInfo(varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_ELEREFERENCE][node_COMMONATRIBUTECONSTANT.IDEELEMENT_ELEMENTPATH]));
		},

		populateVariable : function(varName, variable){
			for(var i in loc_valueStructureRuntimeIds){
				var valueStructure = loc_out.getValueStructure(loc_valueStructureRuntimeIds[i]);
				var varWrapper = valueStructure.getElement(varName);
				if(varWrapper!=undefined && varWrapper.getVariable()==undefined){
					varWrapper.setVariable(variable);
					return;
				}
			}
		},




		





		
		getValueStructureRuntimeIdByName : function(valueStructureName){
			var out;
			out = valueContextDef[node_COMMONATRIBUTECONSTANT.VALUECONTEXT_VALUESTRUCTURERUNTIMEIDBYNAME][valueStructureName];
			if(out==undefined){
				if(loc_parentValueContext!=undefined){
					out = loc_parentValueContext.getValueStructureRuntimeIdByName(valueStructureName);
				}
			}
			return out;
		},
		
		getValueStructureRuntimeIds : function(){
			var solid = [];
			var soft = [];
			_.each(loc_valueStructures, function(valueStructure, runtimeId){
				if(valueStructure.isSolid())  solid.push(runtimeId);
				else soft.push(runtimeId);
			});
			return {
				solid : solid,
				soft : soft
			};
		},
		
		getSolidValueStrcutreWrapper : function(valueStructureRuntimeId){
			var out = this.getValueStructureWrapper(valueStructureRuntimeId);
			if(out.isSolid()==true)  return out;
		},
		
			
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
	};
	
	loc_init(id, valuePortContainerDef, variableDomainDef, parentValuePortContainer, variableMan);
	return loc_out;
};

var loc_createValuePort = function(valuePortContainer, valuePortGroup, valuePortName){

	var loc_valuePortContainer = valuePortContainer;
	var loc_valuePortGroup = valuePortGroup;
	var loc_valuePortName = valuePortName;
	
	var loc_getValueStructure = function(valueStructureRuntimeId){
		return loc_valuePortContainer.getValueStructure(valueStructureRuntimeId);
	};
	
	var loc_getValueStructureId = function(varId){
		var out = varId.getValueStructureId();
		if(out==undefined){
			var valueStructures = loc_valuePortContainer.getValueStructuresByGroupNameAndValuePortName(loc_valuePortGroup, loc_valuePortName);
			for(var vsId in valueStructures){
				var valueStructure = valueStructures[vsId];
				if(valueStructure.getElement(varId.getRootName())!=undefined){
					out = vsId;
					break;
				}
			}
		}
		return out;
	};
	
	
	var loc_out = {

		prv_id : nosliw.generateId(),
		
		createVariable : function(elementId){
			return loc_valuePortContainer.createVariableById(elementId);			
		},
		
		getValueRequest : function(elementId, handlers, request){        
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			out.addRequest(loc_getValueStructure(loc_getValueStructureId(elementId)).getDataOperationRequest(elementId.getRootName(), node_valueInVarOperationServiceUtility.createGetOperationService(elementId.getElementPath()), {
				success: function(request, dataValue){
					var kkkk = elementId;
					return dataValue==undefined?undefined:dataValue.value;
				}
			}));
			return out;
		},

		setValueRequest : function(elementId, value, handlers, request){
			var valueStructure = loc_getValueStructure(loc_getValueStructureId(elementId));
			if(valueStructure!=undefined){
				return valueStructure.getDataOperationRequest(elementId.getRootName(), node_valueInVarOperationServiceUtility.createSetOperationService(elementId.getElementPath(), value), handlers, request);
			}        
		},
		
		setValuesRequest : function(setValueInfos, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			_.each(setValueInfos, function(setValueInfo, i){
				var elementId = setValueInfo.elementId;
				out.addRequest(loc_out.setValueRequest(elementId, setValueInfo.value));
				
//				out.addRequest(loc_getValueStructure(loc_getValueStructureId(elementId)).getDataOperationRequest(elementId.getRootName(), node_valueInVarOperationServiceUtility.createSetOperationService(elementId.getElementPath(), setValueInfo.value)));
			});
			return out;			
		},
		
	};
	
	return loc_out;
};




var loc_createSolidValueStructureWrapper = function(valueStructureRuntimeId, valueStructureName, valueStrucutre){
	
	var loc_runtimeId = valueStructureRuntimeId;
	var loc_runtimeName = valueStructureName;

	var loc_valueStrucutre = valueStrucutre;
	
	var loc_out = {
			
		isSolid : function(){   return true;   },

		getRuntimeId : function(){   return loc_runtimeId;    },

		getValueStructure : function(){   return loc_valueStrucutre;   },
		
		createVariable : function(valueStructureVariableInfo){
			return loc_out.getValueStructure().createVariable(valueStructureVariableInfo);
		},
			
		getName : function(){   return "solid__"+loc_runtimeName;     },
	};
	
	return loc_out;
};

var loc_createSoftValueStructureWrapper = function(valueStructureRuntimeId, parentValuePortContainer){
	
	var loc_runtimeId = valueStructureRuntimeId;
	
	var loc_parentValuePortContainer = parentValuePortContainer;
	
	var loc_usedCount = 1;
	
	var loc_out = {
		
		isSolid : function(){   return false;   },

		getRuntimeId : function(){   return loc_runtimeId;    },
		
		getValueStructure : function(){   return loc_parentValuePortContainer.getValueStructure(loc_runtimeId);   },
		
		createVariable : function(valueStructureVariableInfo){
			return loc_parentValuePortContainer.getValueStructure(loc_runtimeId).createVariable(valueStructureVariableInfo);
		},
			
		getName : function(){
			var parentSymbol = "__parent__";
			var out = "soft__"+parentSymbol;
			var currentValuePortContainer = loc_parentValuePortContainer;
			while(currentValuePortContainer!=undefined){
				var valueStructureWrapper = currentValuePortContainer.getValueStructureWrapper(loc_runtimeId);
				if(valueStructureWrapper!=null){
					out = out + valueStructureWrapper.getName();
					break;
				}
				else{
					currentValuePortContainer = currentValuePortContainer.getParentValuePortContainer();
					out = out + loc_parentSymbol;
				}
			}
			
			return out;
		},
		
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.createVariableManager", function(){node_createVariableManager = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuestructure.createValueStructure", function(){node_createValueStructure = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuestructure.createValueStructureElementInfo", function(){node_createValueStructureElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){  node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.empty.createEmptyValue", function(){node_createEmptyValue = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createValuePortDomain", nod_createValuePortDomain); 

})(packageObj);
