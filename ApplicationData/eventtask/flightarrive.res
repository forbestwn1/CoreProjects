{
	"source" : {
		"pollSchedule" : {
			"interval" : 60,
			"start" : ??,
			"end" : ??		
		},
		
		"pollTask" : {
			"input" : {
				"flightNumber : ": "",
				
			}
			"process" : "",	
		
		}
	},
	
	"eventInfo" : {
		"context" : {
		
		}
	
	},
	
	"handler" : {
			
		"process" : "",	
	
	},
	
	"attachment" : {
		"process" : [
			{
				"name": "pollTask",
				"entity" : {
					"activity": [{
							"id": "startActivityId",
							"name": "startActivity",
							"type": "start",
							"flow": {
								"target": "debugId"
							}
						},
						{
							"id": "debugId",
							"name": "debug",
							"type": "debug",
							"result": [{
								"name": "success",
								"flow": {
									"target": "presentSchoolDataUI"
								}
							}]
						},
						{
							"id": "presentSchoolDataUI",
							"name": "presentSchoolDataUI",
							"type": "UI_presentUI",
							"ui": "schoolInfoUI",
							"result": [{
								"name": "success",
								"flow": {
									"target": "refreshSchoolInfo"
								}
							}]
						},
						{
							"id": "refreshSchoolInfo",
							"name": "refreshSchoolInfo",
							"type": "UI_executeCommand",
							"partId": "ui.schoolInfoUI",
							"ui11": "schoolInfoUI",
							"command": "nosliw_update_data",
							"inputMapping": {
								"element": {
									"schoolData": {
										"definition": { 
											"path": "nosliw_EVENT.data"
										}
									}
								}
							},
							"result": [{
								"name": "success",
								"flow": {
									"target": "successEndId"
								}
							}]
						},
						{
							"id": "successEndId",
							"name": "successEnd",
							"type": "end"
						}
					]
				}
			}
		
		], 

		"service" : [
			{
				"name": "getPearsonFlightArrivalService",
				"id" : "pearsonFlightArrivalService"
			}	
		]
	}
}
