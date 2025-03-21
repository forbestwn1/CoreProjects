//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;
	var node_createVariableManager;
	var node_createValueStructure;
	var node_createValueStructureVariableInfo;
	var node_createValueStructureElementInfo;
	var node_dataUtility;
	var node_complexEntityUtility;
	var node_createValuePortElementInfo;
	var node_createEmptyValue;

//*******************************************   Start Node Definition  ************************************** 	

//variable domain, it response to value structure domain in complex resource
var nod_createVariableDomain = function(variableDomainDef){

	//value structure definition domain
	var loc_variableDomainDefinition = variableDomainDef;
	
	var loc_rootContextId;
	
	var loc_valueContextById = {};

	var loc_valueContextIdIndex = 0;
	
	//variable pool
	var loc_variableMan = nosliw.runtime.getVariableManager();
	
	var loc_out = {
		//create variable group according to
		//value structure complex
		//parent value context
		//return value context id
		creatValueContext : function(valueContextDef, parentValueContextId){
			loc_valueContextIdIndex++;
			var id = loc_valueContextIdIndex+"";
			var parentValueContext = this.getValueContext(parentValueContextId);
			var valueContext; 
			if(valueContextDef!=undefined){
				valueContext = loc_createValueContext(id, valueContextDef, loc_variableDomainDefinition, parentValueContext, loc_variableMan);
			}
			else{
				valueContext = loc_createEmptyValueContext(id, parentValueContext);
			}
			
			loc_valueContextById[valueContext.getId()] = valueContext;
			return valueContext.getId();
		},
		
		getValueContext : function(valueContextId){   return loc_valueContextById[valueContextId];  },

		getVariableDomainDefinition : function(){   return loc_variableDomainDefinition;	},

		getVariableValue : function(contextId, variableId){
			
		},
		
		setVariableValue : function(contextId, variableId, value){
			
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VAIRABLEDOMAIN);

	return loc_out;
};

var loc_createEmptyValueContext = function(id, parentValueContext){
	
	//value context id
	var loc_id = id;
	
	//parent context which some variable can get from
	var loc_parentValueContext = parentValueContext;
	
	var loc_out = {

		getParentValueContext : function(){   return loc_parentValueContext;    },
		
		getId : function(){  return loc_id;   },

	};

	return loc_out;	
};


//value context responding to data structure under complex entity
//id id assigned to valuecontext
//
//it has parent group, so that some variable is from parent
var loc_createValueContext = function(id, valueContextDef, variableDomainDef, parentValueContext, variableMan){
	
	var loc_variableMan;

	//value context id
	var loc_id;
	
	//parent context which some variable can get from
	var loc_parentValueContext;
	
	//valueStructures in the context
	var loc_valueStructures = {};
	
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
							var parentValueStructure = parentValueContext.getValueStructure(parentValueStructureRuntimeId);
							
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
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_CRITERIA];
						else  criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_CRITERIA]; 
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
		
		var valueStructureName = valueContextDef[node_COMMONATRIBUTECONSTANT.VALUECONTEXT_VALUESTRUCTURERUNTIMENAMEBYID][valueStructureRuntimeId];

		return loc_createSolidValueStructureWrapper(valueStructureRuntimeId, valueStructureName, node_createValueStructure(id, valueStructureElementInfosArray));
	};	
	
	
	var loc_init = function(id, valueContextDef, variableDomainDef, parentValueContext, variableDomain){
		loc_id = id;
		loc_parentValueContext = parentValueContext;
		loc_variableMan = variableMan;

		loc_valueStructureRuntimeIds = valueContextDef==undefined?[] : valueContextDef[node_COMMONATRIBUTECONSTANT.VALUECONTEXT_VALUESTRUCTURE];
		_.each(loc_valueStructureRuntimeIds, function(valueStructureRuntimeId){
			var valueStructure;
			if(loc_parentValueContext==undefined || loc_parentValueContext.getValueStructure(valueStructureRuntimeId)==undefined){
				//value structure not found in parent, then build in current group
				var valueStructureRuntime = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURERUNTIME][valueStructureRuntimeId];
				var valueStructureRuntimeInitValue = valueStructureRuntime[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURERUNTIME_INITVALUE];
				var valueStructureRuntimeInfo = valueStructureRuntime[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURERUNTIME_INFO];
				var initMode = valueStructureRuntimeInfo==undefined?undefined:valueStructureRuntimeInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE];
				if(initMode==undefined)   initMode = node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_AUTO;
				
				if(initMode == node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_AUTO){
					//build with all variable
					valueStructure = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef, valueStructureRuntimeInitValue);
				}
				else{
					//build empty value structure
					valueStructure = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef, valueStructureRuntimeInitValue, false);
				}
			}
			else{
				//value structure from parent
				valueStructure = loc_createSoftValueStructureWrapper(valueStructureRuntimeId, parentValueContext);
			}
			loc_valueStructures[valueStructureRuntimeId] = valueStructure;
		});
	};
	
	var loc_out = {
		
		prv_getSolidValueStrucute1 : function(valueStructureRuntimeId){
			var out = loc_valueStructures[valueStructureRuntimeId];
			if(out!=undefined){
				if(!out.isSold()){
					if(loc_parentVariableGroup!=undefined){
						out = loc_parentVariableGroup.prv_getSolidValueStrucute(valueStructureRuntimeId);
					}
				}
			}
			return out;
		},

		getParentValueContext : function(){   return loc_parentValueContext;    },

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
				var valueStructure = loc_valueStructures[loc_valueStructureRuntimeIds[i]].getValueStructure();
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
		
		getValueStructure : function(valueStructureRuntimeId){
			var wrapper = this.getValueStructureWrapper(valueStructureRuntimeId);
			if(wrapper!=undefined)  return wrapper.getValueStructure();
		},
		
		getValueStructureWrapper : function(valueStructureRuntimeId){   return loc_valueStructures[valueStructureRuntimeId];   },
		
		getSolidValueStrcutreWrapper : function(valueStructureRuntimeId){
			var out = this.getValueStructureWrapper(valueStructureRuntimeId);
			if(out.isSolid()==true)  return out;
		},
		
			
		getId : function(){  return loc_id;   },
		
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
		getVariableInfo : function(variableId){
			
		},
		
	};
	
	loc_init(id, valueContextDef, variableDomainDef, parentValueContext, variableMan);
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
			
		getName : function(){   return loc_runtimeName;     },
	};
	
	return loc_out;
};

var loc_createSoftValueStructureWrapper = function(valueStructureRuntimeId, parentValueContext){
	
	var loc_runtimeId = valueStructureRuntimeId;
	
	var loc_parentValueContext = parentValueContext;
	
	var loc_usedCount = 1;
	
	var loc_out = {
		
		isSolid : function(){   return false;   },

		getRuntimeId : function(){   return loc_runtimeId;    },
		
		getValueStructure : function(){   return loc_parentValueContext.getValueStructure(loc_runtimeId);   },
		
		createVariable : function(valueStructureVariableInfo){
			return loc_parentValueContext.getValueStructure(loc_runtimeId).createVariable(valueStructureVariableInfo);
		},
			
		getName : function(){    
			var currentValueContext = loc_parentValueContext;
			while(currentValueContext!=undefined){
				var valueStructureWrapper = currentValueContext.getSolidValueStrcutreWrapper(loc_runtimeId);
				if(valueStructureWrapper!=null)   return valueStructureWrapper.getName();
				else{
					currentValueContext = currentValueContext.getParentValueContext();
				}
			}
		},
	};
	
	return loc_out;
};

//variable info
var loc_createVariableInfo = function(name, variableInfo){
	var loc_name = name;
	
	//variable info
	var loc_variableInfo;
	
	//current value for variable
	var loc_value;
	
	var loc_init = function(variableInfo){
		loc_variableInfo = variableInfo;
		loc_value = loc_variableInfo[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFAULT];
	};
	
	var loc_out = {
		
		getName : function(){   return loc_name;    },
			
		getVariableInfo : function(){   return loc_variableInfo;    },
	
		getValue : function(){   return loc_value;    },
		
		setValue : function(value){  loc_value = value;    },
	};
	
	loc_init(variableInfo);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.createVariableManager", function(){node_createVariableManager = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructure", function(){node_createValueStructure = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureElementInfo", function(){node_createValueStructureElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){  node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.empty.createEmptyValue", function(){node_createEmptyValue = this.getData();});


//Register Node by Name
packageObj.createChildNode("createVariableDomain11", nod_createVariableDomain); 

})(packageObj);
