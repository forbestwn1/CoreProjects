/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentResource = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
			onSelectResource : function(event) {
				this.$emit("selectResource", this.data);
			},
			onChildSelectResource : function(resourceInfo) {
				this.$emit("selectResource", resourceInfo);
			},
		},
		props : ['data'],
		template : `
			
			<div class="treeview-item">
			    <a v-on:click.prevent="onSelectResource" class="treeview-item-root treeview-item-selectable">
				      <div class="treeview-toggle"></div>
				      <div class="treeview-item-content">
							<div class="treeview-item-label">{{data.name}}</div>
				      </div>
			    </a>
			    <div class="treeview-item-children">
					<resource-children 
						v-bind:data="data.children"
						v-on:selectResource="onChildSelectResource"
					/>
			    </div>
		    </div>
		`
	};
	return loc_vueComponent;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentResource", node_createComponentResource); 

})(packageObj);
