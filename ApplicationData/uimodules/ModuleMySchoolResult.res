{
	context : {
		schools : {
			definition : "pages.schoolList.schoolList",
		}
	},
	init : [
		{
			name:"presentSchoolList",
			type:"presentUI",
			ui : "schoolListUI"
		}, 
	],
	uis : [
		{
			name : "schoolListUI",
			type : "list",
			page : "schoolList",
			service : {
			
			},
			event : {
				"selectSchool : {
					task : [
						{
							name:"presentUI",
							type:"presentUI",
							action : "present.information",
							ui : "schoolInfoUI"
						}, 
						{
							name: "refreshSchoolInfo",
							type: "uiCommand",
							ui : "schoolInfoUI"
							command : "refresh",
							parms:{
								schoolList : "event.parm.schoolData"
							}
						},
					]
				}
			}
		},
		{
			name : "schoolInfoUI",
			type : "info",
			page : "schoolInfo",
		}
	]
}
