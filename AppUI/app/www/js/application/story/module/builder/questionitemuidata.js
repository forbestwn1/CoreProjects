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

var node_createComponentQuestionItemUIData = function(){

	var loc_vueComponent = {
		data : function(){
			return {
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
			selectUI : function(event){
				
			}
			
		},
		template : `
			<div>
				{{question.question}}:
				<a v-on:click="selectUI">Select</a>
				<a class="button popup-open" href="#" data-popup=".popup-about">Open About Popup</a>

				<div class="popup popup-about">
				    <div class="block">
				      <p>About</p>
				      <!-- Close Popup -->
				      <p><a class="link popup-close" href="#">Close popup</a></p>
				      <p>Lorem ipsum dolor sit amet...</p>
				    </div>
				  </div>
			
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
packageObj.createChildNode("createComponentQuestionItemUIData", node_createComponentQuestionItemUIData); 

})(packageObj);
