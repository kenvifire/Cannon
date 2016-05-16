CREATE TABLE `courseInfo_ph13175496035` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `catalogType` int(10) DEFAULT NULL,
  `parentId` int(10) DEFAULT NULL,
  `courseId` int(10) DEFAULT NULL,
  `videoId` int(10) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  KEY `id` (`id`)
)

insert into courseInfo_ph13175496035(catalogType,parentId,courseId,videoId,status)
select catalogType,parentId,courseId,videoId,status from courseInfo;
