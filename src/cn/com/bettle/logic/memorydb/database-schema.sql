--    Copyright 2010 The myBatis Team

--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at

--       http://www.apache.org/licenses/LICENSE-2.0

--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.

--    version: $Id: db.sql 2398 2010-08-29 15:16:24Z simone.tripodi $

create table bus (
    BUS_ID VARCHAR(50) not null,
    BUS_NUMBER VARCHAR(20) not null,
    BUS_PLATE_NUMBER VARCHAR(20) not null,
    IMEI varchar(50) not null,
    constraint pk_bus primary key (BUS_ID)
);


create table bus_gps (
    BUS_ID VARCHAR(50) not null,
    LATITUDE double,
    LONGITUDE double,
    ALTITUDE double,
    UTIME timestamp,
    SPEED float,
    DIRECT float,
    FING_ID VARCHAR(50),
    constraint pk_bus_gps primary key (BUS_ID)
);


create table driver (
    DRIVER_ID VARCHAR(50) not null,
    DRIVER_NUMBER VARCHAR(100),
    DRIVER_FIRST_NAME VARCHAR(200),
    DRIVER_LAST_NAME VARCHAR(200),
    DRIVER_SEX VARCHAR(10),
    DRIVER_MOBILE VARCHAR(30),
    CREATE_TIME timestamp,
    constraint pk_driver primary key (DRIVER_ID)
);

create table bus_driver (
    DRIVER_ID VARCHAR(50) not null,
    BUS_ID VARCHAR(50) not null,
    CREATE_TIME timestamp,
    END_TIME timestamp
);
