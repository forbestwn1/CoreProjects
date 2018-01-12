//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createWrapperVariable;

//*******************************************   Start Node Definition  ************************************** 	

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
var node_createContextVariable = function(n, p){
	var path = p;
	var name = n;
	if(path==undefined){
		//if second parms does not exist, then try to parse name to get path info
		if(node_getObjectType(name)==node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE){
			//if firs parm is context variable object
			path = name.path;
			name = name.name;
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
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE);
	
	return loc_out;
};


/*
 * object to describe context element info, two models:
 * 		1. name + parent context + parent context contextVariable
 * 		2. name + wrapper + info
 * 		3. name + data/value + path + info
 */
var node_createContextElementInfo = function(name, data1, data2, data3){
	var loc_out = {
		name : name,
	};
	var type = node_getObjectType(data1);
	if(type==node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXT){
		//input is context + context variable
		loc_out.context = data1;
		loc_out.contextVariable = node_createContextVariable(data2);
		loc_out.info = data3;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
		//input is wrapper
		loc_out.wrapper = data1;
		loc_out.info = data2;
	}
	else{
		//input is data/value
		loc_out.wrapper = node_wrapperFactory.createWrapper(data1, data2);
		loc_out.info = data3;
	}
	if(loc_out.info==undefined)  loc_out.info = {};
	return loc_out;
};

/*
 * create real context element based on element info 
 * it contains following attribute:
 * 		name
 * 		variable
 * 		info
 */
var node_createContextElement = function(elementInfo, requestInfo){
	var loc_out = {
		name : elementInfo.name,
		info : elementInfo.info,
	};

	//get variable
	if(elementInfo.context!=undefined){
		var context = elementInfo.context;
		var contextVar = elementInfo.contextVariable;
		loc_out.variable = context.createVariable(contextVar, requestInfo);
	}
	else{
		var wrapper = elementInfo.wrapper;
		loc_out.variable = node_createWrapperVariable(wrapper, requestInfo);
	}
	
	//get context type(public, internal, private)
	var contextType = node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC;
	if(loc_out.info!=undefined && loc_out.info.contextType!=undefined){
		contextType = loc_out.info.contextType;
	}
	loc_out.contextType = contextType;
	
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
nosliw.registerSetNodeDataEvent("uidata.variable.createWrapperVariable", function(){node_createWrapperVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContextVariable", node_createContextVariable); 
packageObj.createChildNode("createContextElementInfo", node_createContextElementInfo); 
packageObj.createChildNode("createContextElement", node_createContextElement); 

})(packageObj);
