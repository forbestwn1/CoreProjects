
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_getEntityTreeNodeInterface;
	var node_getObjectType;
	var node_getEntityObjectInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_utilityDebug = function(){
	
	var loc_exportBrickCore = function(brickCore){
		var out = {};

		//value port container infor		
		out.valuePortContainerIdInternal = node_getEntityObjectInterface(brickCore).getInternalValuePortContainer().getId();
		out.valuePortContainerIdExternal = node_getEntityObjectInterface(brickCore).getExternalValuePortContainer().getId();
		
		//attributes
		var outAttrs = {};
		out.attributes = outAttrs;
		
		var brickNode = node_getEntityTreeNodeInterface(brickCore);
		_.each(brickNode.getChildrenName(), function(attrName){
			var attrOut = {};
			
			var attrNode = brickNode.getChild(attrName);
			var attrEntityRuntime = attrNode.getChildValue();
			var attrEntityCore = attrEntityRuntime.getCoreEntity();
			
			attrOut.value = loc_exportNode(attrNode);
			
			outAttrs[attrName] = attrOut;
		});
		return out;
	};
	
	var loc_exportBundleCore = function(bundleCore){
		var out = {};
		
		var bundleNode = node_getEntityTreeNodeInterface(bundleCore);
		_.each(bundleNode.getChildrenName(), function(childName){
			out[childName] = loc_exportNode(bundleNode.getChild(childName));
		});
		return out;
	};
	
	var loc_exportNode = function(node){
		var out;		
		
		var nodeEntityRuntime = node.getChildValue();
		var nodeEntityCore = nodeEntityRuntime.getCoreEntity();
		var coreEntityType = node_getObjectType(nodeEntityCore);
		if(coreEntityType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
			out = loc_exportBundleCore(nodeEntityCore);
		}
		else{
			out = loc_exportBrickCore(nodeEntityCore);
		}
		
		return _.extend(
			{
				"entityType":coreEntityType
			}, 
			{}, out);
	};
	
	var loc_out = {
		
		exportBundle : function(bundle){
			return loc_exportBundleCore(bundle);
		},		
		
	};
	
	return loc_out;		
		
}();


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("utilityDebug", node_utilityDebug); 

})(packageObj);
