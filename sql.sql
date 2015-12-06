
CREATE TABLE t_bus_relationship(
    RELATION_ID INT PRIMARY KEY AUTO_INCREMENT,
    BUS_NUMBER_FROM VARCHAR(20),
    BUS_NUMBER_TO VARCHAR(20)
);

ALTER TABLE t_bus_parents ADD COLUMN PARENT_TEL VARCHAR(50);
ALTER TABLE t_bus_parents ADD COLUMN SMS VARCHAR(100);

-----------------------------------------------------
SELECT * FROM t_bus_parents WHERE BUS_NUMBER

