DROP TABLE IF EXISTS nosliw.CRONJOB_STATE;

CREATE TABLE nosliw.CRONJOB_STATE(
	ID   				VARCHAR(20)			NOT NULL ,

	CRONJOBID			VARCHAR(20)			NOT NULL ,

	POLLTIME			DATETIME			NOT NULL,

	STATE				VARCHAR(2000)		NOT NULL ,

	PRIMARY KEY (ID)	
);
