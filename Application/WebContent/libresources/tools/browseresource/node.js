/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.node");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentResourceNode = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
			"resource-group" : node_createComponentResourceGroup(),
		},
		methods : {
			onSelectResource : function(resourceInfo) {
				this.$emit("selectMiniApp", miniAppId);
			},
		},
		template : `
		
			<div>
				<p>
					<a v-on:click.prevent="onSelectMiniApp">{{data.name}}</a>
				</p>
			</div>
		
			<div class="list accordion-list">
				<ul>
					<resource-group 
						v-for="miniAppGroup in data.children"
						v-bind:key="miniAppGroup.group.id"
						v-bind:data="miniAppGroup"
						v-on:selectMiniApp="onSelectMiniApp"
					>
					</group>
				</ul>
			</div>
		`
	};
	return loc_vueComponent;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUserApps", node_createComponentResourceNode); 

})(packageObj);
