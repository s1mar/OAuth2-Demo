use sevenfivefive_oauth;

CREATE TABLE IF NOT EXISTS `meal` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user` VARCHAR(45) NULL,
  `time` DATETIME NULL,
  `mealname` VARCHAR(45) NULL,
  `caloriesintake` INT NULL,
  PRIMARY KEY (`id`));
