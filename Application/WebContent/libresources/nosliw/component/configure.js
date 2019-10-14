//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_objectOperationUtility;
	
//*******************************************   Start Node Definition  ************************************** 	
//configure for component
//global is configure that apply to this configure and child configure
//parms is used as parameter for configure item which is function
//child configure has two parts: share and parts
//share is visible to every part
//parts is visible to particular part
//for particular part, the configure is merge between global and share and part, part overwrite share overwrite global
var node_createConfigure = function(definition, global, parms){
	//global configure, it will apply to child configure
	var loc_global;
	//current configure data, including global value
	var loc_configure = {};
	

	//apply global configure to value
	var loc_applyGlobalConfigure = function(value){
		var out = {};
		out = _.extend(out, loc_global, value);
		return out;
	};

	//find configure item of function and calculate the value 
	var loc_processConfigure = function(configure, parms){
		if(configure==undefined)  return;
		var out;
		if(Array.isArray(configure)){
			out = [];
			_.each(configure, function(ele, i){
				out.push(loc_processConfigure(ele, parms));
			});
		}
		else if(typeof configure === "function"){
			out = configure(parms);
		}
		else if(typeof configure === 'object'){
			out = {};
			_.each(configure, function(ele, name){
				out[name] = loc_processConfigure(ele, parms);
			});
		}
		else{
			out = configure;
		}
		return out;
	};
	
	var loc_init = function(definition, global, parms){
		//global value
		loc_global = parms==undefined? global : loc_processConfigure(global, parms);

		var configure;
		var valueType = node_getObjectType(definition);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE)	configure = definition.getConfigureValue();
		else if(parms==undefined)   configure = definition;
		else	configure = loc_processConfigure(definition, parms);
		
		//build configure data
		if(configure!=undefined)	loc_configure = loc_applyGlobalConfigure(configure); 
	}
	
	var loc_getChildConfigureValue = function(path, childId){
	    var childBase = node_objectOperationUtility.getObjectAttributeByPath(loc_configure, path);
		var childValue = {};
		if(childBase!=undefined){
			//merge share with part
			_.extend(childValue, childBase.share, childBase.parts==undefined?undefined : childBase.parts[childId]);
		}
		return childValue;
	}; 
	
	var loc_out = {
		getConfigureValue : function(){		return loc_configure;	},
		
		//get children configure value
		//path : path to child definition
		//childId : child Id
		getChildConfigureValue : function(path, childId){
			return loc_applyGlobalConfigure(loc_getChildConfigureValue(path, childId));
		},
		
		//get children configure options
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
		
		getChildConfigure : function(path, childId){
			return node_createConfigure(loc_getChildConfigureValue(path, childId), loc_global);
		}
	};
	
	loc_init(definition, global, parms);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfigure", node_createConfigure); 

})(packageObj);
