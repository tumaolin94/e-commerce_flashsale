CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'goods ID',
  `goods_name` varchar(16) DEFAULT NULL COMMENT 'goods name',
  `goods_title` varchar(64) DEFAULT NULL COMMENT 'goods title',
  `goods_img` varchar(64) DEFAULT NULL COMMENT 'goods image',
  `goods_detail` longtext COMMENT 'goods detail',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT 'goods price',
  `goods_stock` int(11) DEFAULT '0' COMMENT 'goods stock',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

INSERT INTO `goods` VALUES (1,'iphoneX','Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机','/img/iphonex.png','Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机',8765.00,10000),(2,'华为Meta9','华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待','/img/meta10.png','华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待',3212.00,-1),(3,'iphone8','Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机','/img/iphone8.png','Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机',5589.00,10000),(4,'小米6','小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待','/img/mi6.png','小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待',3212.00,10000);

CREATE TABLE `flashsale_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Sale ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT 'goods ID',
  `sale_price` decimal(10,2) DEFAULT '0.00' COMMENT 'sale price',
  `stock_count` int(11) DEFAULT NULL COMMENT 'stock count',
  `start_date` datetime DEFAULT NULL COMMENT 'start date',
  `end_date` datetime DEFAULT NULL COMMENT 'end date',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

INSERT INTO `flashsale_goods` VALUES (1,1,0.01,9,'2017-12-04 21:51:23','2017-12-31 21:51:27'),(2,2,0.01,9,'2017-12-04 21:40:14','2017-12-31 14:00:24'),(3,3,0.01,9,'2017-12-04 21:40:14','2017-12-31 14:00:24'),(4,4,0.01,9,'2017-12-04 21:40:14','2017-12-31 14:00:24');

CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT 'goods ID',
  `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT 'address ID',
  `goods_name` varchar(16) DEFAULT NULL COMMENT 'goods name',
  `goods_count` int(11) DEFAULT '0' COMMENT 'goods count',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT 'price',
  `order_channel` tinyint(4) DEFAULT '0' COMMENT '1pc，2android，3ios',
  `status` tinyint(4) DEFAULT '0' COMMENT 'order status，0 created but no paid，1 paid，2 sent，3 got，4 refund，5 completed',
  `create_date` datetime DEFAULT NULL COMMENT 'create time',
  `pay_date` datetime DEFAULT NULL COMMENT 'pay time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `flashsale_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT 'order ID',
  `goods_id` bigint(20) DEFAULT NULL COMMENT 'good ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1551 DEFAULT CHARSET=utf8mb4;