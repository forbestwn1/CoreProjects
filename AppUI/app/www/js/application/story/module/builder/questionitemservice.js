/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemService = function(availableService){

	var loc_availableService = availableService;
	
	loc_availableService = [
		{
			id : 'TestTemplateService',
			name : 'TestTemplateService'
		},
		{
			id : 'schoolService',
			name : 'schoolService'
		}
	];
	
	var loc_vueComponent = {
		data : function(){
			return {
				allService : loc_availableService
			};
		},
		props : ['data'],
		components : {
		},
		methods : {
			onSelectMiniApp : function(miniAppId) {
				this.$emit("selectMiniApp", miniAppId);
			},
		},
		computed: {
			serviceId : {
				get : function(){
					return this.data.element.entity.referenceId;
				},
				
				set : function(serviceId){
					var changeItem = node_storyChangeUtility.createChangeItemPatch(this.data.element, node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID, serviceId);
					node_storyChangeUtility.applyChangeToElement(this.data.element, changeItem);
					this.data.changes.push(changeItem);
				}
			}
			
		},
		template : `
			<div>
				aaaa
				<select v-model="serviceId">
				  <option v-for="service in allService" v-bind:value="service.id">
				    {{ service.name }}
				  </option>
				</select>			
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemService", node_createComponentQuestionItemService); 

})(packageObj);
