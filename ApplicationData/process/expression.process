{
	page : [
		{
			name : "schoolListPage",
			uiResource : "Resource_MySchool_SchoolList"
		},
		{
			name : "schoolInfoPage",
			uiResource : "Resource_MySchool_SchoolData"
		}
	],
	
	context : {
		public : {
			element : {
				schoolList : {
					definition: {
						criteria ： "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%",
						defaultValue : {
							dataTypeId: "test.array;1.0.0",
							value: [
								{
									dataTypeId: "test.map;1.0.0",
									value: {
										schoolName : {
											dataTypeId: "test.string;1.0.0",
											value: "School1"
										},
										schoolRating : {
											dataTypeId: "test.float;1.0.0",
											value: 6.0
										},
										geo : {
											dataTypeId: "test.geo;1.0.0",
											value: {
												"latitude" :  43.651299,
												"longitude" : -79.579473
											}
										}
									}
								},
								{
									dataTypeId: "test.map;1.0.0",
									value: {
										schoolName : {
											dataTypeId: "test.string;1.0.0",
											value: "School2"
										},
										schoolRating : {
											dataTypeId: "test.float;1.0.0",
											value: 8.5
										},
										geo : {
											dataTypeId: "test.geo;1.0.0",
											value: {
												"latitude" :  43.649016, 
												"longitude" : -79.485059
											}
										}
									}
								}					
							]
						}	
					}									
				},
				schoolData: {
					definition : {
						criteria : "test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%",
					}
				}
			}
		}
	},
	process : [
		init : {
			activity : [
				{
					id:"presentSchoolListUI",
					name:"presentSchoolListUI",
					type:"presentUI",
					ui : "schoolListUI"
				}, 
				{
					name: "refreshSchoolList",
					type: "uiCommand",
					ui : "schoolListUI"
					command : "refresh",
					parms:{
						schoolData : {
							definition : {
								path : "schoolList"
							}
						}
					}
				},
			]
		}
	],
	ui : [
		{
			name : "schoolListUI",
			type : "list",
			page : "schoolListPage",
			serviceMapping : {
			
			},
			eventHandler : {
				"selectSchool : {
					activity : [
						{
							id:"presentSchoolDataUI",
							name:"presentSchoolDataUI",
							type:"presentUI",
							ui : "schoolInfoUI"
						}, 
						{
							name: "refreshSchoolInfo",
							type: "uiCommand",
							ui : "schoolInfoUI"
							command : "refresh",
							parms:{
								schoolData : {
									definition : {
										path: "event.parm.schoolData"
									}
								}
							}
						},
					]
				}
			}
		},
		{
			name : "schoolInfoUI",
			type : "info",
			page : "schoolInfoPage",
		}
	]
}
