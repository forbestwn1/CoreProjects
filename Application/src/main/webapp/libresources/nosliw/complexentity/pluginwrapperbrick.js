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

var node_createWrapperBrickPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createWrapperBrickCore(complexEntityDef, internalValuePortContainerId, bundleCore, configure);
			}, handlers, request);
		}
	};

	return loc_out;
};

var loc_createWrapperBrickCore = function(complexEntityDef, valueContextId, bundleCore, configure){
	var loc_complexEntityDef = complexEntityDef;
	var loc_envInterface = {};
	var loc_brick;
	
	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},

		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BRICKWRAPPERBRICK_BRICK, undefined, {
				success : function(request, node){
					loc_brick = node_complexEntityUtility.getBrickNode(node).getChildValue().getCoreEntity();
				}
			}));
			return out;
		},

		updateView : function(view){
			loc_brick.updateView(view);
			return view;
		}
		
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
packageObj.createChildNode("createWrapperBrickPlugin", node_createWrapperBrickPlugin); 

})(packageObj);
