//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;

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
		creatValueContext : function(valueStructureComplex, parentValueContextId){
			loc_valueContextIdIndex++;
			var valueContext = loc_createValueContext(loc_valueContextIdIndex+"", valueStructureComplex, loc_variableDomainDefinition, parentValueContextId, loc_out);
			loc_valueContextById[valueContext.getId()] = valueContext;
			return valueContext;
		},
		
		getValueContext : function(valueContextId){   return loc_valueContextById[valueContextId];  },
		
		getVariableValue : function(contextId, variableId){
			
		},
		
		setVariableValue : function(contextId, variableId, value){
			
		},
		
		getVariableDomainDefinition : function(){   return loc_variableDomainDefinition;	}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VAIRABLEDOMAIN);

	return loc_out;
};

//variable group responding to value structure complex
//valueStructureComplex value structure complex definition under complex entity
//
//it has parent group, so that some variable is from parent
var loc_createValueContext = function(id, valueStructureComplexDef, variableDomainDef, parentValueContext, variableMan){
	
	var loc_variableMan;
		
	//var group id
	var loc_id;
	
	//parent context which some variable can get from
	var loc_parentValueContext;
	
	//valueStructures in the context
	var loc_valueStructures = {};
	
	var loc_init = function(id, valueStructureComplexDef, variableDomainDef, parentValueContext, variableDomain){
		loc_id = id;
		loc_parentValueContext = parentValueContext;
		loc_variableMan = variableMan;

		var valueStructureRuntimeIds = valueStructureComplexDef==undefined?[] : valueStructureComplexDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXVALUESTRUCTURE_VALUESTRUCTURE];
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
			
		},
			
		
		
		
			
		getId : function(){  return loc_id;   },
		
		getValueStructure : function(valueStructureId){      },
		
		getVariableInfosByValueStructure : function(valueStructureId){   return loc_variablesByValueStructure[valueStructureId];  },
		
		getVariableInfo : function(variableId){
			
		},
		
	};
	
	loc_init(id, valueStructureComplex, variableDomainDef, parentVariableGroup, variableMan);
	return loc_out;
};

var loc_createSolidValueStructure = function(valueStructureRuntimeId, variableDomainDef, parentValueContext){
	
	var loc_runtimeId;
	
	var loc_variables = {};

	var loc_addVariable = function(varInfo){   
		loc_variables[varInfo.getName()] = varInfo;    
	};

	var loc_init = function(valueStructureRuntimeId, variableDomainDef, parentVariableGroup){
		loc_runtimeId = runtimeId;
		var valueStructureDefId = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_DEFINITIONBYRUNTIME][valueStructureRuntimeId];
		var valueStructureDefinitionInfo = variableDomainDef[node_COMMONATRIBUTECONSTANT.DOMAINVALUESTRUCTURE_VALUESTRUCTURE][valueStructureDefId];
		_.each(valueStructureDefinitionInfo[node_COMMONATRIBUTECONSTANT.INFOVALUESTRUCTURE_VALUESTRUCTURE], function(rootDef, rootName){
			loc_addVariable(loc_createVariableInfo(rootName, rootDef));
		});
		
	};
	
	var loc_out = {
		
		getRuntimeId : function(){   return loc_runtimeId;    },
		
		isSolid : function(){   return true;   },

		
	};
	
	loc_init(valueStructureRuntimeId, variableDomainDef, parentVariableGroup);
	return loc_out;
};

var loc_createSoftValueStructure = function(valueStructureRuntimeId, parentValueContext){
	
	var loc_runtimeId = runtimeId;
	
	var loc_usedCount = 1;
	
	var loc_out = {
		
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

//Register Node by Name
packageObj.createChildNode("createVariableDomain", nod_createVariableDomain); 

})(packageObj);
