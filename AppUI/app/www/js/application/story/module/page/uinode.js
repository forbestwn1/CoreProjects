/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("uinode");    

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
	var node_createComponentUserApps;
	var node_createComponentUserInfo;
	var node_createEventObject;
	var node_createMiniAppInfo;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModulePage = function(parm){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
			"mini-app" : node_createComponentMiniApp()
		},
		methods : {
			onSelectNode : function(nodeInfo){
				miniAppInfo.setGroupId(this.data.group.id);
				this.$emit("selectMiniApp", miniAppInfo);
			},
			onDeleteMiniApp : function(miniAppId){
				for(var i in this.miniapps){
					if(miniAppId==this.miniApps[i].id){
						break;
					}
				}
				this.miniapps.splice(i, 1);
			}
		},
		template :
			`
		    <li class="accordion-item-opened"><a href="#" class="item-content item-link">
				<div class="item-inner">
					<div class="item-title">{{data.group.name}}</div>
				</div></a>
				<div class="accordion-item-content">
				    <div class="block">
						<mini-app 
							v-for="miniapp in data.miniApp"
							v-bind:key="miniapp.id"
							v-bind:data="miniapp"
							v-on:selectMiniApp="onSelectMiniApp"
							v-on:deleteMiniApp="onDeleteMiniApp"
						></mini-app>
				    </div>
				</div>
		    </li>
			`
	};
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserApps", function(){node_createComponentUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserInfo", function(){node_createComponentUserInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.miniapp.createMiniAppInfo", function(){node_createMiniAppInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModulePage", node_createModulePage); 

})(packageObj);
