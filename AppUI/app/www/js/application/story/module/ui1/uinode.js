/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createEventObject;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createUINode = function(parm){

	var loc_vueComponent = {
		name : "story-uinode",
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
		},
		computed: {
			nodeName : function(){
				return "aaaa";
			}
		},
		methods : {
			onSelectNode : function(nodeInfo){
			},
			onDeleteMiniApp : function(miniAppId){
			}
		},
		template :
			`
		    <li class="accordion-item-opened"><a href="#" class="item-content item-link">
				<div class="item-inner">
					<div class="item-title">{{data.storyNodeId}}</div>
				</div></a>
				<div class="accordion-item-content">
				    <div class="block">
						<story-uinode 
							v-for="childNode in data.children"
							v-bind:key="childNode.storyNodeId"
							v-bind:data="childNode"
						></story-uinode>
				    </div>
				</div>
		    </li>
			`
	};
	
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUINode", node_createUINode); 

})(packageObj);
