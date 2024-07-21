# 应用配置表
CREATE TABLE IF NOT EXISTS `weixin_app_config`
(

    `id`          int unsigned     not null auto_increment primary key,
    `key`         varchar(512)     not null,
    `value`       varchar(512)     not null,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

# 用户表
CREATE TABLE IF NOT EXISTS `weixin_user`
(

    `id`          int unsigned     not null auto_increment primary key,
    `open_id`     varchar(128)     not null unique,
    `nickname`    varchar(128),
    `avatar`      mediumblob,
    `coin`        int              not null default 0,
    `comment`     varchar(128),
    `is_admin`    tinyint unsigned not null default 0,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

# 麻将流水表
CREATE TABLE IF NOT EXISTS `weixin_mj_log`
(
    `id`              int unsigned     not null auto_increment primary key,
    `game_id`         varchar(128)     not null,
    `game_type`        int unsigned     not null,
    `user_id`         int unsigned     not null,
    `user_type`       int unsigned     not null,
    `point`           int unsigned     not null,
    `point_operators` varchar(128)     not null,
    `total_points`    int unsigned     not null,
    `score`           int unsigned     not null,
    `is_deleted`      tinyint unsigned not null default 0,
    `create_time`     datetime                  default current_timestamp,
    `update_time`     datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;
