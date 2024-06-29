CREATE TABLE IF NOT EXISTS `weixin_user`
(

    `id`          int unsigned     not null auto_increment primary key,
    `open_id`     varchar(128)     not null unique,
    `nickname`    varchar(128),
    `avatar`      varchar(512),
    `session_key` varchar(128),
    `comment`     varchar(128),
    `is_admin`    tinyint unsigned not null default 0,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS `weixin_user_asset`
(

    `id`          int unsigned not null auto_increment primary key,
    `user_id`     int unsigned not null unique,
    `copper_coin` int          not null default 0,
    `silver_coin` int          not null default 0,
    `gold_coin`   int          not null default 0,
    `create_time` datetime              default current_timestamp,
    `update_time` datetime              default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS `weixin_mahjong_game`
(
    `id`              int unsigned not null auto_increment primary key,
    `referee_user_id` int unsigned not null,
    `base_score`      int unsigned not null,
    `fan_list`        varchar(128) not null,
    `win_case`        varchar(512) not null,
    `create_time`     datetime default current_timestamp,
    `update_time`     datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS `weixin_user_score`
(
    `id`          int unsigned not null auto_increment primary key,
    `game_id`     int unsigned,
    `user_id`     int unsigned not null,
    `type`        varchar(128) not null,
    `score`       int          not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS `weixin_mahjong_player`
(

    `id`          int unsigned     not null auto_increment primary key,
    `user_id`     int unsigned     not null,
    `is_deleted`  tinyint unsigned not null default 0,
    `create_time` datetime                  default current_timestamp,
    `update_time` datetime                  default current_timestamp on update current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;


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
