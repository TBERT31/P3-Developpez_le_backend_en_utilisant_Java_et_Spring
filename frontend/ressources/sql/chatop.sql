-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 12 juin 2024 à 11:37
-- Version du serveur : 8.0.36
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `chatop`
--

-- --------------------------------------------------------

--
-- Structure de la table `messages`
--

CREATE TABLE `messages` (
  `id` bigint NOT NULL,
  `rental_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `message` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `messages`
--

INSERT INTO `messages` (`id`, `rental_id`, `user_id`, `message`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'mon message', '2022-02-01 23:00:00', '2022-08-01 22:00:00');

-- --------------------------------------------------------

--
-- Structure de la table `rentals`
--

CREATE TABLE `rentals` (
  `id` bigint NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `surface` double NOT NULL,
  `price` double NOT NULL,
  `picture` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `owner_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `rentals`
--

INSERT INTO `rentals` (`id`, `name`, `surface`, `price`, `picture`, `description`, `owner_id`, `created_at`, `updated_at`) VALUES
(1, 'test house 1', 432, 300, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', 1, '2012-12-01 23:00:00', '2014-12-01 23:00:00'),
(2, 'test house 2', 154, 200, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', 2, '2012-12-01 23:00:00', '2014-12-01 23:00:00'),
(3, 'test house 3', 234, 100, 'https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.', 1, '2012-12-01 23:00:00', '2014-12-01 23:00:00');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `email` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `email`, `name`, `password`, `created_at`, `updated_at`) VALUES
(1, 'test1@test.com', 'test TEST', '$2a$10$qtdEyh.uyzhtTJJTZuDyF.2qvP7pFAgkM9f771rNkEJv.dIssTKDW', '2024-06-12 09:08:55', NULL),
(2, 'test2@test.com', 'test2 TEST2', '$2a$10$FkhwCEAA7Kxc.Q0Gn7m.beYNINuWwQK3dCWQWC2OAZFo1rQjm7crG', '2024-06-12 09:09:10', NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `rental_id` (`rental_id`);

--
-- Index pour la table `rentals`
--
ALTER TABLE `rentals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `owner_id` (`owner_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `USERS_index` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `FK3ce1i9w1rtics9wjwj8y5y3md` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`),
  ADD CONSTRAINT `FKpsmh6clh3csorw43eaodlqvkn` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Contraintes pour la table `rentals`
--
ALTER TABLE `rentals`
  ADD CONSTRAINT `FKf462yhxa9vd3m2qdmcoixg1fv` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
