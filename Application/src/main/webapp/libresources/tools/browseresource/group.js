/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentGroup = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
		},
		methods : {
			onChildSelectResource : function(resourceInfo) {
				this.$emit("selectResource", resourceInfo);
			},
		},
		template : `
		<div class="treeview-item">
		    <div class="treeview-item-root">
			      <div class="treeview-toggle"></div>
			      <div class="treeview-item-content">
						<i class="icon f7-icons">folder_fill</i>
						<div class="treeview-item-label">{{data.type}}</div>
			      </div>
		    </div>
		    <div class="treeview-item-children">
				<resource-resource 
					v-for="resource in data.elements"
					v-bind:key="resource.name"
					v-bind:data="resource"
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
packageObj.createChildNode("createComponentGroup", node_createComponentGroup); 

})(packageObj);
