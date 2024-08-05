//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_basicUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createContainerPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createContainerCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		}
	};

	return loc_out;
};

var loc_createContainerCore = function(complexEntityDef, valueContextId, bundleCore, configure){
	var loc_complexEntityDef = complexEntityDef;
	var loc_envInterface = {};
	var loc_childrenEntity = {};
	
	var loc_out = {
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var attrNames = loc_complexEntityDef.getAllAttributesName();
			_.each(attrNames, function(attrName, i){
				if(node_basicUtility.getNosliwCoreName(attrName)==undefined){
					out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(attrName, undefined, {
						success : function(request, childNode){
							loc_childrenEntity[attrName] = childNode.getChildValue();
						}
					}));
				}
			});
			return out;
		},

		getChildrenEntity : function(){   return loc_childrenEntity;      },
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContainerPlugin", node_createContainerPlugin); 

})(packageObj);
