/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_storyUtility;
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
		props : ['data', 'story'],
		components : {
		},
		methods : {
		},
		computed: {
			serviceId : {
				get : function(){
					var element = node_storyUtility.getQuestionTargetElement(this.story, this.data);
					return element[node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID];
				},
				
				set : function(serviceId){
					node_storyChangeUtility.applyPatchFromQuestion(this.story, this.data, node_COMMONATRIBUTECONSTANT.STORYNODESERVICE_REFERENCEID, serviceId, this.data.changes);
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
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemService", node_createComponentQuestionItemService); 

})(packageObj);
