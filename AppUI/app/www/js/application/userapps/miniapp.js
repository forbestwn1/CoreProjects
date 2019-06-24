/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentMiniApp = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		methods : {
			onSelectMiniApp : function(event) {
				this.$emit("selectMiniApp", {app:this.data});
			},
			onDeleteMiniApp : function(event) {
				this.$emit("deleteMiniApp", this.data.id);
			}
		},
		template : `
			<div>
				<p>
					<a v-on:click.prevent="onSelectMiniApp">{{data.name}}</a>
					<a v-on:click.prevent="onDeleteMiniApp">XXX</a>
				</p>
			
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentMiniApp", node_createComponentMiniApp); 

})(packageObj);
