//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;
	var node_createVariableManager;

//*******************************************   Start Node Definition  ************************************** 	

//variable domain, it response to value structure domain in complex resource
var nod_createVariableDomain = function(variableDomainDef){

	//value structure definition domain
	var loc_variableDomainDefinition = variableDomainDef;
	
	var loc_rootContextId;
	
	var loc_valueContextById = {};

	var loc_valueContextIdIndex = 0;
	
	//variable pool
	var loc_variableMan = node_createVariableManager();
	
	var loc_out = {
		//create variable group according to
		//value structure complex
		//parent group
		//return group id
		creatValueContext : function(valueContextDef, parentValueContextId){
			loc_valueContextIdIndex++;
			var valueContext = loc_createValueContext(loc_valueContextIdIndex+"", valueContextDef, loc_variableDomainDefinition, parentValueContextId, loc_variableMan);
			loc_valueContextById[valueContext.getId()] = valueContext;
			return valueContext;
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

//variable group responding to value structure complex
//valueStructureComplex value structure complex definition under complex entity
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
	
	var loc_createSolidValueStructure = function(valueStructureRuntimeId, parentValueContext, variableDomain){

		var variableDomainDef = variableDomain.getVariableDomainDefinition();
		
		//build context element first
		var valueStructureElementInfosArray = [];
		
		loc_runtimeId = valueStructureRuntimeId;
		var valueStructureDefId = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_DEFINITIONBYRUNTIME][valueStructureRuntimeId];
		var valueStructureDefinitionInfo = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURE][valueStructureDefId];
		_.each(valueStructureDefinitionInfo[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURE_VALUESTRUCTURE], function(valueStructureDefRootObj, rootName){
			var valueStructureDefRootEle = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION];
			
			var info = {
				matchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS],
				reverseMatchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]
			};
			var type = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
			var valueStructureInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			//if context.info.instantiate===manual, context does not need to create in the framework
			if(valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && 
						valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL){
					//physical relative
					if(valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_PATH][node_COMMONATRIBUTECONSTANT.INFOPATHREFERENCE_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
//							if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
						//process relative that  refer to element in parent context
						
						var resolveInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.RESOLVEDINFO];

						var parentValueStructureRuntimeId = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_STRUCTUREID];
						var parentValueStructure = parentValueContext.getValueStructure(parentValueStructureRuntimeId);
						
						var pathObj = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_PATH];
						var rootName = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
						var path = pathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
						
						valueStructureElementInfosArray.push(node_createValueStructureElementInfo(eleName, parentValueStructure, node_createValueStructureVariableInfo(rootName, path), undefined, info));
					}
				}
				else{
					//not relative or logical relative variable
					var defaultValue = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFAULT];
					
					var criteria;
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_CRITERIA];
					else  criteria = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_CRITERIA]; 
					if(criteria!=undefined){
						//app data, if no default, empty variable with wrapper type
						if(defaultValue!=undefined) 	valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
						else  valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
					}
					else{
						//object, if no default, empty variable with wrapper type
						if(defaultValue!=undefined)		valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
						else valueStructureElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
					}
				}
			}
		});
		
		return node_createValueStructure(id, valueStructureElementInfosArray, requestInfo);
	};	
	
	
	var loc_init = function(id, valueContextDef, variableDomainDef, parentValueContext, variableDomain){
		loc_id = id;
		loc_parentValueContext = parentValueContext;
		loc_variableMan = variableMan;

		var valueStructureRuntimeIds = valueContextDef==undefined?[] : valueContextDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXVALUESTRUCTURE_VALUESTRUCTURE];
		_.each(valueStructureRuntimeIds, function(valueStructureRuntimeId){
			if(loc_parentValueContext==undefined || loc_parentValueContext.getVariableInfosByValueStructure(valueStructureRuntimeId)==undefined){
				//value structure not found in parent, then build in current group
				var valueStructure = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef, loc_parentValueContext);
				loc_valueStructures[valueStructure.getId()] = valueStructure;
			}
			else{
				//value structure from parent
				var valueStructure = loc_createSoftValueStructure(valueStructureRuntimeId);
				loc_valueStructures[valueStructure.getId()] = valueStructure;
			}
		});
	};
	
	var loc_out = {
		
		prv_getSolidValueStrucute : function(valueStructureRuntimeId){
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

		//
		createVariable : function(varResolve){
			var valueStructure = this.getValueStructure(varResolve[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_STRUCTUREID]);
			return valueStructure.createVariable(node_createValueStructureVariableInfo(varResolve[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_PATH]));
		},
			
		getValueStructure : function(valueStructureRuntimeId){   return loc_valueStructures[valueStructureRuntimeId];   },
		
		
		
		
			
		getId : function(){  return loc_id;   },
		
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
		getVariableInfo : function(variableId){
			
		},
		
	};
	
	loc_init(id, valueContextDef, variableDomainDef, parentValueContext, variableMan);
	return loc_out;
};


var loc_createSoftValueStructure = function(valueStructureRuntimeId, parentValueContext){
	
	var loc_runtimeId = valueStructureRuntimeId;
	
	var loc_parentValueContext = parentValueContext;
	
	var loc_usedCount = 1;
	
	var loc_out = {
		
		createVariable : function(valueStructureVariableInfo){
			return loc_parentValueContext.getValueStructure(loc_runtimeId).createVariable(valueStructureVariableInfo);
		},
			
		getRuntimeId : function(){   return loc_runtimeId;    },
		
		isSolid : function(){   return false;   },

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
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableManager", function(){node_createVariableManager = this.getData();});


//Register Node by Name
packageObj.createChildNode("createVariableDomain", nod_createVariableDomain); 

})(packageObj);
