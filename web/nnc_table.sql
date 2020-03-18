create table if not exists `music`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) default null,
	`maxlength` varchar(255) default null,
	`motion` varchar(255) default null,
	`beat` varchar(255) default null,
	`basicbeat` varchar(255) default null,
	`Mode` varchar(255) default null,
	 primary key (`id`)
	)ENGINE=InnoDB DEFAULT CHARSET=utf8;