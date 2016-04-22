CREATE TABLE `courseInfo` (
`id` int(10) NOT NULL AUTO_INCREMENT,
`catalogType` int(10) DEFAULT NULL,
`parentId` int(10) DEFAULT NULL,
`courseId` int(10) DEFAULT NULL,
`videoId` int(10) DEFAULT NULL,
`status` tinyint(4) DEFAULT NULL,
KEY `id` (`id`)
)
