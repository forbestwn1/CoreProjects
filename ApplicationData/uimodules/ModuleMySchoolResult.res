{
	pages : {
		list : "Example_App_Result_School",
		info : "page1",
	},
	status : [
		{
			name : "main",
			type : "list",
			page : "list",
			init : [
				
			],
			event : {
				"select" : {
					_data : {},
					action : "transfer.information",
					nextstatus : {
						name : "info",
						init : [
							{
								type: "service",
								name: "getSchoolById",
								parms:{
									id : ""
								},
								output:{
									schoolData : ""
								}
							},
							{
								type: "command",
								name: "refresh",
								parms:{
									path : "school"
								}
							},
							
						]
						commands : [
							{
								name:"refresh"
								data:{
									path : "school"
								}
							}
						]
					}
				}
			}
		},
		{
			name : "info",
			type : "info",
			page : "info",
		}
	]
}
