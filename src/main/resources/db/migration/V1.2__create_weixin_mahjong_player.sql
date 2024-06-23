CREATE TABLE IF NOT EXISTS `weixin_mahjong_player`
(

    `id`          int unsigned     not null auto_increment primary key,
    `user_id`     int unsigned     not null,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;