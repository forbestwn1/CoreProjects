//import parentComponent from './js/parent.js'

var init1 = function(root){
//	Vue.component('parent-component', parentComponent);
	let vue = new Vue({
		el: root,
		data: {
			num : 555
		},
		components : {
		},
		computed : {
		},
		methods : {
		},
		template : 
			`
			<div>
				aaaaa{{num}}
			</div>
			`
	});
	
}



