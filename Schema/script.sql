create table customer
(
  customerId   int(10) auto_increment
    primary key,
  customerName varchar(45)                         not null,
  active       tinyint(1) default '1'              not null,
  createDate   datetime default CURRENT_TIMESTAMP  null,
  createdBy    varchar(50)                         null,
  lastUpdate   timestamp default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP,
  lastUpdateBy varchar(50)                         null
)
  charset = latin1;

create table address
(
  customerId   int(10)                             not null
    primary key,
  address      varchar(50)                         not null,
  address2     varchar(50)                         null,
  cityId       int(10)                             null,
  postalCode   varchar(10)                         null,
  phone        varchar(20)                         not null,
  createDate   datetime default CURRENT_TIMESTAMP  null,
  createdBy    varchar(50)                         null,
  lastUpdate   timestamp default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP,
  lastUpdateBy varchar(50)                         null,
  constraint cascade_address_customerId
  foreign key (customerId) references customer (customerid)
    on delete cascade
)
  charset = latin1;

create trigger address_BEFORE_INSERT
  before INSERT
  on address
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing INSERT into table
    SELECT USER()
    INTO currentUser;

    -- Update createdBy field to the username of the person performing the INSERT
    SET NEW.createdBy = currentUser;

    -- Update lastUpdateBy field to the username of the person performing the INSERT
    SET NEW.lastUpdateBy = currentUser;
  END;

create trigger address_BEFORE_UPDATE
  before UPDATE
  on address
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing UPDATE
    SELECT USER()
    INTO currentUser;

    -- Update lastUpdateBy field to the username of the person performing the UPDATE
    SET NEW.lastUpdateBy = currentUser;
  END;

create table appointment
(
  appointmentId int(10) auto_increment
    primary key,
  customerId    int(10)                             not null,
  title         varchar(255)                        not null,
  description   text                                null,
  location      text                                null,
  contact       text                                null,
  url           varchar(255)                        null,
  start         datetime                            not null,
  end           datetime                            not null,
  createDate    datetime default CURRENT_TIMESTAMP  null,
  createdBy     varchar(50)                         null,
  lastUpdate    timestamp default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP,
  lastUpdateBy  varchar(50)                         null,
  constraint cascade_appointment_customerId
  foreign key (customerId) references customer (customerId)
    on delete cascade
)
  charset = latin1;

create index cascade_appointment_customerId
  on appointment (customerId);

create trigger appointment_BEFORE_INSERT
  before INSERT
  on appointment
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing INSERT into table
    SELECT USER()
    INTO currentUser;

    -- Update createdBy field to the username of the person performing the INSERT
    SET NEW.createdBy = currentUser;

    -- Update lastUpdateBy field to the username of the person performing the INSERT
    SET NEW.lastUpdateBy = currentUser;
  END;

create trigger appointment_BEFORE_UPDATE
  before UPDATE
  on appointment
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing UPDATE
    SELECT USER()
    INTO currentUser;

    -- Update lastUpdateBy field to the username of the person performing the UPDATE
    SET NEW.lastUpdateBy = currentUser;
  END;

create trigger customer_BEFORE_INSERT
  before INSERT
  on customer
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing INSERT into table
    SELECT USER()
    INTO currentUser;

    -- Update createdBy field to the username of the person performing the INSERT
    SET NEW.createdBy = currentUser;

    -- Update lastUpdateBy field to the username of the person performing the INSERT
    SET NEW.lastUpdateBy = currentUser;
  END;

create trigger customer_BEFORE_UPDATE
  before UPDATE
  on customer
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing UPDATE
    SELECT USER()
    INTO currentUser;

    -- Update lastUpdateBy field to the username of the person performing the UPDATE
    SET NEW.lastUpdateBy = currentUser;
  END;

create table user
(
  userId       int(10) auto_increment
    primary key,
  userName     varchar(50)                         not null,
  password     varchar(50)                         not null,
  active       tinyint default '1'                 not null,
  createdBy    varchar(50)                         null,
  createDate   datetime default CURRENT_TIMESTAMP  not null,
  lastUpdate   timestamp default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP,
  lastUpdateBy varchar(50)                         null,
  constraint userName_UNIQUE
  unique (userName)
)
  charset = latin1;

create trigger user_BEFORE_INSERT
  before INSERT
  on user
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing INSERT into table
    SELECT USER()
    INTO currentUser;

    -- Update createdBy field to the username of the person performing the INSERT
    SET NEW.createdBy = currentUser;

    -- Update lastUpdateBy field to the username of the person performing the INSERT
    SET NEW.lastUpdateBy = currentUser;
  END;

create trigger user_BEFORE_UPDATE
  before UPDATE
  on user
  for each row
  BEGIN
    DECLARE currentUser varchar(50);

    -- Find userName of person performing UPDATE
    SELECT USER()
    INTO currentUser;

    -- Update lastUpdateBy field to the username of the person performing the UPDATE
    SET NEW.lastUpdateBy = currentUser;
  END;

create view vw_appointment as
  select
    `u04ts4`.`appointment`.`appointmentId` AS `appointmentId`,
    `u04ts4`.`appointment`.`customerId`    AS `customerId`,
    `u04ts4`.`appointment`.`title`         AS `title`,
    `u04ts4`.`appointment`.`start`         AS `start`,
    `u04ts4`.`appointment`.`end`           AS `end`
  from `u04ts4`.`appointment`;

create view vw_customer as
  select
    `u04ts4`.`customer`.`customerId`   AS `customerId`,
    `u04ts4`.`customer`.`customerName` AS `customerName`,
    `u04ts4`.`address`.`address`       AS `address`,
    `u04ts4`.`address`.`phone`         AS `phone`
  from (`u04ts4`.`customer`
    join `u04ts4`.`address` on ((`u04ts4`.`customer`.`customerId` = `u04ts4`.`address`.`customerId`)));

create view vw_user as
  select
    `u04ts4`.`user`.`userName` AS `userName`,
    `u04ts4`.`user`.`password` AS `password`
  from `u04ts4`.`user`;

create procedure sp_appointment_DeleteById(IN var_appointmentId int(10))
  BEGIN
    DELETE FROM appointment
    WHERE appointmentId = var_appointmentId;
  END;

create procedure sp_appointment_Insert(IN var_customerId int(10), IN var_title varchar(255), IN var_start datetime,
                                       IN var_end        datetime)
  BEGIN
    /*Insert an appointment into the database*/
    INSERT INTO appointment (customerId, title, start, end)
    VALUES (var_customerId, var_title, var_start, var_end);
  END;

create procedure sp_appointment_Insert_pk(IN var_customerId int(10), IN var_title varchar(255), IN var_start datetime,
                                          IN var_end        datetime)
  BEGIN
    CALL `u04ts4`.`sp_appointment_Insert`(var_customerId, var_title, var_start, var_end);

    SELECT LAST_INSERT_ID();
  END;

create procedure sp_appointment_SelectClose()
  BEGIN
    /*Select appointments that start within 15 minutes of now*/
    SELECT *
    FROM appointment
    WHERE (now() < start)
          AND (start < now() + INTERVAL 15 MINUTE);
  END;

create procedure sp_appointment_SelectOverlapped(IN var_customerId int(10), IN var_appointmentId int(10),
                                                 IN var_start      datetime, IN var_end datetime)
  BEGIN
    SELECT count(*)
    FROM appointment
    WHERE customerId = var_customerId
          AND appointmentId != var_appointmentId
          AND (
            (var_start > start AND var_start < end)
            OR (var_end > start AND var_end < end)
            OR (var_start <= start AND var_end >= end)
          );
  END;

create procedure sp_appointment_UpdateById(IN var_title         varchar(255), IN var_start datetime,
                                           IN var_end           datetime, IN var_appointmentId int(10))
  BEGIN
    UPDATE appointment
    SET title = var_title, start = var_start, end = var_end
    WHERE appointmentId = var_appointmentId;
  END;

create procedure sp_customer_DeleteById(IN var_customerId int(10))
  BEGIN
    DELETE FROM customer
    WHERE customerId = var_customerId;
  END;

create procedure sp_customer_Insert(IN var_customerName varchar(45), IN var_address varchar(50),
                                    IN var_phone        varchar(20))
  BEGIN
    /*Insert customer*/
    INSERT INTO customer (customerName) VALUES (var_customerName);

    /*Insert address*/
    INSERT INTO address (customerId, address, phone) VALUES (LAST_INSERT_ID(), var_address, var_phone);
  END;

create procedure sp_customer_Insert_pk(IN var_customerName varchar(45), IN var_address varchar(50),
                                       IN var_phone        varchar(20))
  BEGIN

    CALL `u04ts4`.`sp_customer_Insert`(var_customerName, var_address, var_phone);

    SELECT LAST_INSERT_ID();
  END;

create procedure sp_customer_Update(IN var_customerId int(10), IN var_customerName varchar(45),
                                    IN var_address    varchar(50), IN var_phone varchar(20))
  BEGIN
    /*Update customer*/
    UPDATE customer
    SET customerName = var_customerName
    WHERE customerId = var_customerId;

    /*Update address*/
    UPDATE address
    SET address = var_address, phone = var_phone
    WHERE customerId = var_customerId;
  END;

create procedure sp_user_Insert(IN var_userName varchar(50), IN var_password varchar(50))
  BEGIN
    /*Insert user*/
    INSERT INTO `u04ts4`.`user` (userName, password) VALUES (var_userName, var_password);
  END;

create procedure sp_user_SelectByLogin(IN var_userName varchar(50), IN var_password varchar(50))
  BEGIN
    SELECT *
    FROM user
    WHERE userName = var_userName AND password = var_password;
  END;

create procedure sp_user_Update(IN var_userId int(10), IN var_userName varchar(50), IN var_password varchar(50))
  BEGIN
    /*Update user*/
    UPDATE user
    SET userName = var_userName,
      password   = var_password
    WHERE userId = var_userId;
  END;


