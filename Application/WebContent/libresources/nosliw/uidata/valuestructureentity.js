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

/*
 * object to describe value structure element info:
 * 		1. name + parent value structure + value structure Variable + info
 * 		2. name + data/value + path + info
 * 		3. name + parent variable + path + info
 * 		4. name + undefined + value type
 */
var node_createValueStructureElementInfo = function(name, data1, data2, adapterInfo, info){
	
	var loc_out = {
		name : name,
	};

	var type = node_getObjectType(data1);
	if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTURE){
		//input is value structure + value structure variable
		loc_out.valueStructure = data1;
		loc_out.valueStructureVariable = node_createContextVariableInfo(data2);
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_EXTENDEDCONTEXT){
		//input is extended context + context variable
		var contextVarInfo = node_createContextVariableInfo(data2);
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
	
	loc_out.info = info==undefined ? {} : info;
	loc_out.adapterInfo = adapterInfo;
	return loc_out;
};

/*
 * entity for context based variable description
 * It contains : 
 * 		name : context element name
 * 		path : path from context element
 * possible parms: 
 * 		name + path
 * 		contextVariable
 * 		string
 */
var node_createValueStructureVariableInfo = function(n, p){
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

/*
 * create real context element based on element info 
 * it contains following attribute:
 * 		name
 * 		variable
 * 		info
 */
var node_createValueStructureElement = function(elementInfo, requestInfo){
	var loc_out = {
		info : elementInfo.info,
	};

	//if have mutiple name, use the first one in array as main variable
	var adapterInfo = elementInfo.adapterInfo;
	//get variable
	if(elementInfo.valueStructure!=undefined){
		var valueStructure = elementInfo.valueStructure;
		var valueStructureVar = elementInfo.valueStructureVariable;
		var eleVariable = valueStructure.createVariable(valueStructureVar, adapterInfo, requestInfo);
		//cannot create context element variable
		if(eleVariable==undefined)   return;
		loc_out.variable = {
			name : elementInfo.name,
			variable : eleVariable,
		};
	}
	else if(elementInfo.variable!=undefined)		loc_out.variable = {name:elementInfo.name, variable:node_createVariableWrapper(elementInfo.variable, elementInfo.path, adapterInfo, requestInfo)};
	else		loc_out.variable = {name:elementInfo.name, variable:node_createVariableWrapper(elementInfo.data1, elementInfo.data2, adapterInfo, requestInfo)};
	
	return loc_out;
};






//extended context is context + extra variables
var node_createExtendedContext = function(context, exVars){
	var loc_context = context;
	var loc_exVars = exVars;
	
	var loc_out = {
		findeVariable : function(eleName){
			var out = loc_context.getContextElement(eleName);
			if(out==undefined)  out = exVars[eleName].variable.prv_getVariable();
			return out;
		}
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_EXTENDEDCONTEXT);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});

//Register Node by Name
packageObj.createChildNode("createValueStructureElementInfo", node_createValueStructureElementInfo); 
packageObj.createChildNode("createValueStructureVariableInfo", node_createValueStructureVariableInfo); 
packageObj.createChildNode("createValueStructureElement", node_createValueStructureElement); 

})(packageObj);
