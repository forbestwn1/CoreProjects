/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.node");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentResourceGroup = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
			"node" : node_createComponentResourceNode(),
		},
		methods : {
			onSelectMiniApp : function(miniAppId) {
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
					<node 
						v-for="node in data"
						v-bind:key="node.name"
						v-bind:data="node"
						v-on:selectMiniApp="onSelectMiniApp"
					>
					</node>
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
packageObj.createChildNode("createComponentResourceByType", node_createComponentResourceByType); 

})(packageObj);
