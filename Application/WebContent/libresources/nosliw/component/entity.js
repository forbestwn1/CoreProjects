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
var node_createConfigure = function(value, global){
	//global configure, it will apply to child configure
	var loc_global = global;
	//current configure data, including global value
	var loc_configure = {};

	//apply global configure to value
	var loc_applyGlobalConfigure = function(value){
		var out = {};
		out = _.extend(out, loc_global, value);
		return out;
	};

	var loc_init = function(value){
		//build configure data
		if(value!=undefined){
			var valueType = node_getObjectType(value);
			if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE){
				value = value.getConfigureData();
			}
			loc_configure = loc_applyGlobalConfigure(value); 
		}
	}
	
	var loc_out = {
		getConfigureValue : function(){
			return loc_configure;
		},
		
		getChildConfigureValue : function(path, childId){
		    var childBase = node_objectOperationUtility.getObjectAttributeByPath(loc_configure, path);
			var childValue = {};
			if(childBase!=undefined){
				_.extend(childValue, childBase.share, childBase.parts==undefined?undefined : childBase.parts[childId]);
			}
			loc_applyGlobalConfigure(childValue);
		},
		
		getChildrenIdSet : function(path){
		    var segs = path.split('.');
		    var childBase = node_objectOperationUtility.getObjectAttributeByPathSegs(loc_configure, segs);
			if(childBase!=undefined){
				var out = [];
				_.each(childBase.parts, function(part, id){
					out.push(id);
				});
			}
			return out;
		},
	};
	
	loc_init(value);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE);

	return loc_out;
};

	
var node_CommandResult = function(requestResult, commandInfo){
	this.requestResult = requestResult;
	this.commandInfo = commandInfo;
};

var node_CommandInfo = function(commandName, commandParm){
	this.commandName = commandName;
	this.commandParm = commandParm;
};

var node_DecorationInfo = function(type, id, name, configure){
	this.id = id;
	this.name = name;
	this.configure = configure;
	this.resource = undefined;
	this.decoration = undefined;
	if(this.name==undefined)   this.name = this.id;   
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfigure", node_createConfigure); 
packageObj.createChildNode("CommandResult", node_CommandResult); 
packageObj.createChildNode("DecorationInfo", node_DecorationInfo); 

})(packageObj);
