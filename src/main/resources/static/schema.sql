DROP DATABASE IF EXISTS TrainFleetManager;
CREATE DATABASE TrainFleetManager;

CREATE USER IF NOT EXISTS 'manager'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON TrainFleetManager.* TO 'manager'@'localhost';

FLUSH PRIVILEGES;