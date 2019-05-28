/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createComponentMiniApp;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentGroup = function(){

	var loc_groupModel = {
//		group : {},
	};
	
	var loc_vueComponent = {
		data : function(){
			return loc_groupModel;
		},
		props : ['group', 'miniapps'],
		components : {
			miniapp : node_createComponentMiniApp()
		},
		methods : {
			onSelectMiniApp : function(miniAppId){
				console.log(miniAppId);
			},
			onDeleteMiniApp : function(miniAppId){
				for(var i in this.miniapps){
					if(miniAppId==this.miniapps[i].id){
						break;
					}
				}
				this.miniapps.splice(i, 1);
			}
		},
		template :
			`
		    <li class="accordion-item"><a href="#" class="item-content item-link">
				<div class="item-inner">
					<div class="item-title">{{group.name}}</div>
				</div></a>
				<div class="accordion-item-content">
				    <div class="block">
						<miniapp 
							v-for="miniapp in miniapps"
							v-bind:miniapp="miniapp"
							v-on:selectMiniApp="onSelectMiniApp"
							v-on:deleteMiniApp="onDeleteMiniApp"
						></miniapp>
				    </div>
				</div>
		    </li>
			`
			
			
	};
	
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentMiniApp", function(){node_createComponentMiniApp = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentGroup", node_createComponentGroup); 

})(packageObj);
