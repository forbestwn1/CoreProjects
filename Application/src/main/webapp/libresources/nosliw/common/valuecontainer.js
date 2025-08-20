//get/create package
var packageObj = library.getChildPackage("valuecontainer");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_newOrderedContainer;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;
	var node_getObjectType;

//*******************************************   Start Node Definition  **************************************

var node_createValueContainerSimple = function(categary){
	
	var loc_value = [];
	
	var loc_categary = categary;
	
	var loc_getGetValueRequest = function(index, categary, name, handlers, request){
		if(index>=loc_children.length)  return;

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_children[index].getGetValueRequest(categary, name, {
			success : function(request, value){
				if(value!=undefined)   return value;
				else{
					return loc_getGetValueRequest(index+1, categary, name);
				}
			}
		}));
		return out;		
	};
	
	var loc_out = {
		
		getGetValueRequest : function(categary, name, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				if(categary!=loc_categary)  return;
				else{
					return loc_value[name];
				}
			}));
			return out;
		},
		
		setValue : function(name, value){
			loc_value[name] = value;
		},
		
		//for debug purpose
		getAllValueInfoRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return {
					categary : categary,
					value : loc_value
				};			
			}));
			return out;
		},
		
	};

	return loc_out;	
};


var node_createValueContainerList = function(){
	var loc_children = [];
	
	var loc_getGetValueRequest = function(index, categary, name, handlers, request){
		if(index>=loc_children.length)  return;

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_children[index].getGetValueRequest(categary, name, {
			success : function(request, value){
				if(value!=undefined)   return value;
				else{
					return loc_getGetValueRequest(index+1, categary, name);
				}
			}
		}));
		return out;		
	};

	var loc_out = {
		
		addChild : function(child){
			loc_children.push(child);
		},

		getGetValueRequest : function(categary, name, handlers, request){   
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetValueRequest(0, categary, name));
			return out;
		},
		
		getAllValueInfoRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var valueInfos = [];
			_.each(loc_children, function(child, i){
				out.addRequest(child.getAllValueInfoRequest({
					success : function(request, valueInfo){
						valueInfos.push(valueInfo);
					}
				}));
			});
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return valueInfos;
			}));
			return out;
		},
	};

	return loc_out;	
};








var node_makeObjectValueContainerInterface = function(rawEntity, categary){

	var loc_rawEntity = rawEntity;

	var loc_interfaceEntity = {
		getGetValueRequest : function(categary, name, handlers, request){   return loc_rawEntity.getGetValueRequest==undefined?undefined:loc_rawEntity.getGetValueRequest(name, handlers, request);    }
	};
	
	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_VALUECONTAINER, loc_interfaceEntity);
	return loc_out;
};

var node_getValueContainerInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_VALUECONTAINER);
};



//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.newOrderedContainer", function(){node_newOrderedContainer = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});



//Register Node by Name
packageObj.createChildNode("createValueContainerList", node_createValueContainerList); 
packageObj.createChildNode("createValueContainerSimple", node_createValueContainerSimple); 

packageObj.createChildNode("makeObjectValueContainerInterface", node_makeObjectValueContainerInterface); 
packageObj.createChildNode("getValueContainerInterface", node_getValueContainerInterface); 


})(packageObj);
