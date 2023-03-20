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
		//parent value context
		//return value context id
		creatValueContext : function(valueContextDef, parentValueContextId){
			loc_valueContextIdIndex++;
			var valueContext = loc_createValueContext(loc_valueContextIdIndex+"", valueContextDef, loc_variableDomainDefinition, this.getValueContext(parentValueContextId), loc_variableMan);
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
	
	var loc_createSolidValueStructure = function(valueStructureRuntimeId, variableDomainDef){

		//build context element first
		var valueStructureElementInfosArray = [];
		
		var valueStructureDefId = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_DEFINITIONBYRUNTIME][valueStructureRuntimeId];
		var valueStructureDefinitionInfo = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURE][valueStructureDefId];
		var roots = valueStructureDefinitionInfo[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURE_VALUESTRUCTURE]
							[node_COMMONATRIBUTECONSTANT.DEFINITIONENTITYINDOMAIN_ATTRIBUTE]
							[node_COMMONATRIBUTECONSTANT.DEFINITIONENTITYVALUESTRUCTURE_VALUE]
							[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_VALUE]
							[node_COMMONATRIBUTECONSTANT.EMBEDED_VALUE];
		_.each(roots, function(valueStructureDefRootObj, rootName){
			var valueStructureDefRootEle = valueStructureDefRootObj[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION];
			
			var info = {
				matchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_MATCHERS],
				reverseMatchers : valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_REVERSEMATCHERS]
			};
			var type = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
			var valueStructureInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			//if context.info.instantiate===manual, context does not need to create in the framework
			if(valueStructureInfo==undefined || valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE && 
						(valueStructureInfo==undefined||valueStructureInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL)){
					//physical relative
//					if(valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_PATH][node_COMMONATRIBUTECONSTANT.INFOPATHREFERENCE_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
//							if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
					{
						//process relative that  refer to element in parent context
						
						var resolveInfo = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_RESOLVEDINFO];

						var parentValueStructureRuntimeId = valueStructureDefRootEle[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_STRUCTUREID];
						var parentValueStructure = parentValueContext.getValueStructure(parentValueStructureRuntimeId);
						
						var resolvePathObj = resolveInfo[node_COMMONATRIBUTECONSTANT.INFORELATIVERESOLVE_PATH];
						var resolveRootName = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_ROOT];
						var resolvePath = resolvePathObj[node_COMMONATRIBUTECONSTANT.COMPLEXPATH_PATH];
						
						valueStructureElementInfosArray.push(node_createValueStructureElementInfo(rootName, parentValueStructure, node_createValueStructureVariableInfo(resolveRootName, resolvePath), undefined, info));
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
		
		return loc_createSolidValueStructureWrapper(valueStructureRuntimeId, node_createValueStructure(id, valueStructureElementInfosArray));
	};	
	
	
	var loc_init = function(id, valueContextDef, variableDomainDef, parentValueContext, variableDomain){
		loc_id = id;
		loc_parentValueContext = parentValueContext;
		loc_variableMan = variableMan;

		var valueStructureRuntimeIds = valueContextDef==undefined?[] : valueContextDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYVALUECONTEXT_VALUESTRUCTURE];
		_.each(valueStructureRuntimeIds, function(valueStructureRuntimeId){
			var valueStructure;
			if(loc_parentValueContext==undefined || loc_parentValueContext.getValueStructure(valueStructureRuntimeId)==undefined){
				//value structure not found in parent, then build in current group
				valueStructure = loc_createSolidValueStructure(valueStructureRuntimeId, variableDomainDef);
			}
			else{
				//value structure from parent
				valueStructure = loc_createSoftValueStructureWrapper(valueStructureRuntimeId, parentValueContext);
			}
			loc_valueStructures[valueStructureRuntimeId] = valueStructure;
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
			var valueStructure = this.getValueStructure(varResolve[node_COMMONATRIBUTECONSTANT.INFOREFERENCERESOLVE_STRUCTUREID]);
			return valueStructure.createVariable(node_createValueStructureVariableInfo(varResolve[node_COMMONATRIBUTECONSTANT.INFOREFERENCERESOLVE_ELEREFERENCE][node_COMMONATRIBUTECONSTANT.REFERENCEELEMENTINVALUECONTEXT_ELEMENTPATH]));
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
		
		getValueStructure : function(valueStructureRuntimeId){   return loc_valueStructures[valueStructureRuntimeId];   },
		
		
		
		
			
		getId : function(){  return loc_id;   },
		
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
		getVariableInfo : function(variableId){
			
		},
		
	};
	
	loc_init(id, valueContextDef, variableDomainDef, parentValueContext, variableMan);
	return loc_out;
};


var loc_createSolidValueStructureWrapper = function(valueStructureRuntimeId, valueStrucutre){
	
	var loc_runtimeId = valueStructureRuntimeId;

	var loc_valueStrucutre = valueStrucutre;
	
	var loc_out = {
			
		isSolid : function(){   return true;   },

		getRuntimeId : function(){   return loc_runtimeId;    },

		getValueStructure : function(){   return loc_valueStrucutre;   },
		
		createVariable : function(valueStructureVariableInfo){
			return loc_out.getValueStructure().createVariable(valueStructureVariableInfo);
		},
			
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
nosliw.registerSetNodeDataEvent("uidata.valuestructure.createValueStructure", function(){node_createValueStructure = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.valuestructure.createValueStructureElementInfo", function(){node_createValueStructureElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){  node_complexEntityUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createVariableDomain", nod_createVariableDomain); 

})(packageObj);
