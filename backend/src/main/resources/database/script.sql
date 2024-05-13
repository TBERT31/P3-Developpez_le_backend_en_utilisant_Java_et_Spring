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

/*
  --# ALTER TABLE `USERS` ADD FOREIGN KEY (`id`) REFERENCES `RENTALS` (`owner_id`); 
  Le requête au dessus, initialement proposée est un peu étrange, la FK ne doit-elle pas être dans l'autre sens ? 
  C'est-à-dire que l'utilisateur doit exister avant de pouvoir lui associer une propriétée.
  Relation 1 User --> n Rentals, la FK doit-être dans la table RENTALS.
*/
ALTER TABLE `RENTALS`
ADD CONSTRAINT `FK_RENTALS_OWNERID_USERS_ID` -- On donne un nom plus compréhensible que ceux généré automatiquement par MySQL
FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`)
ON DELETE CASCADE -- Lorsque un user est supprimé, on supprimer les rentals qui lui sont liées
ON UPDATE CASCADE; -- Lorsque un user est mise à jour, on met à jours les rentals qui lui sont liées



/* 
  --# ALTER TABLE `USERS` ADD FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`user_id`);
  Même réflexion pour créer le message l'utilisateur doit-être référencé et pas l'inverse ...
  Relation 1 User --> n Message, la FK doit-être dans la table MESSAGES.
*/
ALTER TABLE `MESSAGES` 
ADD CONSTRAINT `FK_MESSAGES_USERID_USERS_ID` -- On donne un nom plus compréhensible que ceux généré automatiquement par MySQL
FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`)
ON DELETE CASCADE -- Lorsque un user est supprimé, on supprimer les rentals qui lui sont liées
ON UPDATE CASCADE; -- Lorsque un user est mise à jour, on met à jours les rentals qui lui sont liées


/* 
  --# ALTER TABLE `RENTALS` ADD FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`rental_id`);
  Même réflexion pour créer le message la propriété doit-être référencée et pas l'inverse ...
  Relation 1 Rental --> n Message, la FK doit-être dans la table MESSAGES.
*/
ALTER TABLE `MESSAGES` 
ADD CONSTRAINT `FK_MESSAGES_RENTALID_USERS_ID` -- On donne un nom plus compréhensible que ceux généré automatiquement par MySQL
FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`)
ON DELETE CASCADE -- Lorsque un user est supprimé, on supprimer les rentals qui lui sont liées
ON UPDATE CASCADE; -- Lorsque un user est mise à jour, on met à jours les rentals qui lui sont liées

/* 
  Après analyse du fichier /ressources/mockoon/rental-oc.json, et lors de la requête sur la route suivante localhost:3001/api/auth/me qui renvoie cet objet :
  
  {
    "id": 1,
    "name": "Test TEST",
    "email": "test@test.com",
    "created_at": "2022/02/02",
    "updated_at": "2022/08/02"
  }

  Il semble pertinent d'insérer le user "Test TEST" dans la BDD afin de reproduire l'environnement mockoon. 
  Un deuxième id utilisateur est également créé ajoutons en un second
*/
INSERT INTO `USERS` 
(id, email, name, password, created_at, updated_at) 
VALUES 
(1, 'test@test.com', 'Test TEST', 'test!31', '2022/02/02', '2022/08/02'),
(2, 'test2@test.com', 'Test2 TEST2', 'test!32', '2022/02/02', '2022/08/02');


/*
  Après analyse du fichier /ressources/mockoon/rental-oc.json, et lors de la requête sur la route suivante localhost:3001/api/rentals qui renvoie cet objet :

  {
      "rentals": [
          {
              "id": 1,
              "name": "test house 1",
              "surface": 432,
              "price": 300,
              "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
              "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.",
              "owner_id": 1,
              "created_at": "2012/12/02",
              "updated_at": "2014/12/02"
          },
          {
              "id": 1,
              "name": "test house 2",
              "surface": 154,
              "price": 200,
              "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
              "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.",
              "owner_id": 2,
              "created_at": "2012/12/02",
              "updated_at": "2014/12/02"
          },
          {
              "id": 3,
              "name": "test house 3",
              "surface": 234,
              "price": 100,
              "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
              "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.",
              "owner_id": 1,
              "created_at": "2012/12/02",
              "updated_at": "2014/12/02"
          }
      ]
  }

  Il semble pertinent d'insérer ces trois rentals afin de reproduire l'environnement mockoon.
*/
INSERT INTO `RENTALS` 
(id, name, surface, price, picture, 
description, 
owner_id, created_at, updated_at) 
VALUES 

(1, 'test house 1', 432, 300, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.',
1, '2012/12/02', '2014/12/02'),

(2, 'test house 2', 154, 200, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.',
2, '2012/12/02', '2014/12/02'),

(3, 'test house 3', 234, 100, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 
'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.',
1, '2012/12/02', '2014/12/02');