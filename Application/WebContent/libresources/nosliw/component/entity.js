//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createConfigure = function(value){
	
	if(value!=undefined){
		var valueType = node_getObjectType(value);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE){
			return value;
		}
		else{
			var loc_configure = value;
		}
	}
	
	var loc_out = {
		
		getConfigureData : function(component){
			var out = {};

			_.extend(out, loc_configure);
			delete out.global;
			delete out.components;
			
			if(component!=undefined)	_.extend(out, loc_configure.global, loc_configure.components==undefined?undefined : loc_configure.components[component]);
			else  _.extend(out, loc_configure.global);
			return out;
		}
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE);

	return loc_out;
};

	
var node_commandResult = function(requestResult, commandInfo){
	this.requestResult = requestResult;
	this.commandInfo = commandInfo;
};

var node_commandRequestInfo = function(name, parms, handlers, request){
	this.name = name;
	this.parms = parms;
	this.handlers = handlers;
	this.request = request;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfigure", node_createConfigure); 
packageObj.createChildNode("commandResult", node_commandResult); 
packageObj.createChildNode("commandRequestInfo", node_commandRequestInfo); 

})(packageObj);
