CREATE TABLE `USERS` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Depuis MySQL 5.6, il est utile de préciser si le timestamp peut-être NULL ou non.
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP  -- Depuis MySQL 5.6, il est utile de préciser si le timestamp peut-être NULL ou non.
);



CREATE TABLE `RENTALS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` integer NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP 
);

CREATE TABLE `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, 
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

CREATE INDEX owner_id ON `RENTALS` (owner_id); -- Obligatoire pour pouvoir créer la FK associée
CREATE INDEX user_id ON `MESSAGES` (user_id); -- Obligatoire pour pouvoir créer la FK associée
CREATE INDEX rental_id ON `MESSAGES` (rental_id); -- Obligatoire pour pouvoir créer la FK associée

ALTER TABLE `USERS` ADD FOREIGN KEY (`id`) REFERENCES `RENTALS` (`owner_id`);

ALTER TABLE `USERS` ADD FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`user_id`);

ALTER TABLE `RENTALS` ADD FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`rental_id`);

