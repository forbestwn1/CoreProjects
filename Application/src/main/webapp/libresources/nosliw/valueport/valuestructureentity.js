//get/create package
var packageObj = library.getChildPackage("valuestructure");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createVariableWrapper;

//*******************************************   Start Node Definition  ************************************** 	

var node_createValueStructureElementDefinition = function(definition, initValue){
	
	var loc_definition = definition;
	var loc_initValue = initValue;
	
	var loc_out = {
		
		getDefinition : function(){   return loc_definition;    },
		
		getInitValue : function(){   return loc_initValue;   }
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTUREELEMENTDEFINITION);
	return loc_out;
	
};

/*
 * object to describe value structure element info:
 * 		1. name + parent value structure + value structure Variable + info
 * 		2. name + data/value + path + info
 * 		3. name + parent variable + path + info
 * 		4. name + undefined + value type
 */
var node_createValueStructureElementCreateInfo = function(name, data1, data2, adapterInfo, variableInfo){
	var loc_out = {
		name : name,
		variableCreateInfo : node_createVariableCreateInfo(data1, data2, adapterInfo, variableInfo),
	};
	return loc_out;
};

var node_createVariableCreateInfo = function(data1, data2, adapterInfo, variableInfo){

	var loc_out = {	};

	var type = node_getObjectType(data1);
	if(type==node_CONSTANT.TYPEDOBJECT_TYPE_EMPTY){
		loc_out.placeholder = true;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTUREELEMENTDEFINITION){
		loc_out.valueStructureElementDefinition = data1;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTURE){
		//input is value structure + value structure variable
		loc_out.valueStructure = data1;
		loc_out.valueStructureVariableRef = node_createValueStructureVariableRef(data2);
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_EXTENDEDCONTEXT){
		//input is extended context + context variable
		var contextVarInfo = node_createValueStructureVariableRef(data2);
		loc_out.variable = data1.findeVariable(contextVarInfo.name);
		loc_out.path = contextVarInfo.path;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
		//input is variable
		loc_out.variable = data1;
		loc_out.path = data2;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER){
		//input is variable wrapper
		loc_out.variable = data1.prv_getVariable();
		loc_out.path = data2;
	}
	else{
		//input is data/value
		loc_out.data1 = data1;
		loc_out.data2 = data2;
	}
	
	loc_out.adapterInfo = adapterInfo;
	loc_out.variableInfo = variableInfo;
	
	return loc_out;
};

/*
 * entity for context based variable description
 * It contains : 
 * 		name : valuestructure root element name
 * 		path : path from context element
 * possible parms: 
 * 		name + path
 * 		contextVariable
 * 		string
 */
var node_createValueStructureVariableRef = function(n, p){
	var name = n;
	var path = p;
	
	if(path==undefined){
		//if second parms does not exist, then try to parse name to get path info
		if(node_getObjectType(n)==node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTUREVARIABLE){
			//if firs parm is context variable object
			path = n.path;
			name = n.name;
		}
		else{
			path="";
			var index = name.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
			if(index!=-1){
				path = name.substring(index+1);
				name = name.substring(0, index);
			}
		}
	}
	
	path = node_basicUtility.emptyStringIfUndefined(path)+"";
	
	var loc_out = {
		//context item name
		name : name,
		//path
		path : path,
		//key
		key : node_namingConvensionUtility.cascadePath(name, path),
		getFullPath : function(){
			return this.key;
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTUREVARIABLE);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});

//Register Node by Name
packageObj.createChildNode("createValueStructureElementDefinition", node_createValueStructureElementDefinition); 
packageObj.createChildNode("createValueStructureElementCreateInfo", node_createValueStructureElementCreateInfo); 
packageObj.createChildNode("createVariableCreateInfo", node_createVariableCreateInfo); 
packageObj.createChildNode("createValueStructureVariableRef", node_createValueStructureVariableRef); 

})(packageObj);
