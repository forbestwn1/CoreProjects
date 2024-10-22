	
	let childComponent = {
		data : function(){
			return {
				services : [],
				currentServiceId : "",
				currentService : {}
			};
		},
		props : ['pro1', 'pro2'],
		components : {
		},
		methods : {
		},
		created : function(){
		},
		computed: {
		},
		watch : {
		},

		template : `
			<div>
				Child {{pro1}}
			</div>
		`
	};
	
	