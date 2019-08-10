//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	
//configure for component
//every configure has two parts: global and parts
//global is visible to every part
//parts is visible to particular part
//for particular part, the configure is merge between global and part, part overwrite global
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
		
		getConfigureData : function(part){
			var out = {};

			if(part!=undefined)	_.extend(out, loc_configure.global, loc_configure.parts==undefined?undefined : loc_configure.parts[part]);
			else  _.extend(out, loc_configure.global);

			var temp = {};
			_.extend(temp, loc_configure);
			delete temp.global;
			delete temp.parts;
			_.extend(out, temp);
			
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

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfigure", node_createConfigure); 
packageObj.createChildNode("commandResult", node_commandResult); 

})(packageObj);
