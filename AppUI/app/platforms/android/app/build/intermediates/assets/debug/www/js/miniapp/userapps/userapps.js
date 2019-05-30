/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createComponentGroup;
	var node_createComponentMiniApp;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentUserApps = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['user-info'],
		components : {
			group : node_createComponentGroup(),
			mini-app : node_createComponentMiniApp()
		},
		template : `
			<div class="list accordion-list">
				<ul>
					<group 
						v-for="miniAppGroup in user-info.groupMiniApp"
						v-bind:group="miniAppGroup.group"
						v-bind:mini-apps="miniAppGroup.miniApp"
					>
					</group>
				
					<mini-app 
						v-for="miniApp in user-info.miniApp"
						v-bind:mini-app="miniApp"
					>
					</miniapp>
				</ul>
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentGroup", function(){node_createComponentGroup = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentMiniApp", function(){node_createComponentMiniApp = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUserApps", node_createComponentUserApps); 

})(packageObj);
