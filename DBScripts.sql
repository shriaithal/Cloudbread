CREATE TABLE cloudbread.mldata (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ratings` decimal(30,10) NOT NULL,
  `distance` decimal(30,10) NOT NULL,
  `day` varchar(45) NOT NULL,
  `occupancy` decimal(30,10) NOT NULL,
  `capacity` int(11) NOT NULL,
  `weather` varchar(45) NOT NULL,
  `foodtype` varchar(45) NOT NULL,
  `goodreviews` int(11) NOT NULL,
  `badreviews` int(11) NOT NULL,
  `pricerating` decimal(30,10) NOT NULL,
  `amountcooked` decimal(30,10),
  `wastage` decimal(30,10) NOT NULL,
  `predicted` varchar(2) NOT NULL,
  `createdTime` Timestamp NOT NULL,
  `actualTime` Timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE cloudbread.scheduleData (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modelId` varchar(45) NOT NULL,
  `modelCreationDate` Timestamp NOT NULL,
  `schedulerLastRunDate` Timestamp NOT NULL,
  `newModelCreated` varchar(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into scheduleData (modelId, modelCreationDate, schedulerLastRunDate, newModelCreated) values ("ds-yVjJUQ0kqD0", sysdate(), sysdate(), "N");