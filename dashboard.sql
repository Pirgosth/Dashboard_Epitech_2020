-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : Dim 20 déc. 2020 à 21:50
-- Version du serveur :  10.4.17-MariaDB
-- Version de PHP : 7.4.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `dashboard`
--

-- --------------------------------------------------------

--
-- Structure de la table `github_users`
--

CREATE TABLE `github_users` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `local_users`
--

CREATE TABLE `local_users` (
  `id` int(11) NOT NULL,
  `username` varchar(20) CHARACTER SET utf8 NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 NOT NULL,
  `password` varchar(300) CHARACTER SET utf8 NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `saved_widgets`
--

CREATE TABLE `saved_widgets` (
  `id` int(11) NOT NULL,
  `widget_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parameters` varchar(100) CHARACTER SET utf8 NOT NULL,
  `url_parameters` varchar(500) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `usertype_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `user_types`
--

CREATE TABLE `user_types` (
  `id` int(11) NOT NULL,
  `type` varchar(30) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `user_types`
--

INSERT INTO `user_types` (`id`, `type`) VALUES
(2, 'GITHUB'),
(1, 'LOCAL');

-- --------------------------------------------------------

--
-- Structure de la table `widgets`
--

CREATE TABLE `widgets` (
  `id` int(11) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `route` varchar(200) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `widgets`
--

INSERT INTO `widgets` (`id`, `name`, `route`) VALUES
(1, 'STEAM_HOURS', '/widgets/steam/hours'),
(2, 'STEAM_VALUE', '/widgets/steam/value'),
(3, 'STEAM_PROFILE', '/widgets/steam/profile'),
(4, 'YOUTUBE_CHANNEL_STATS', '/widgets/youtube/profile'),
(5, 'YOUTUBE_LATEST_VIDEO', '/widgets/youtube/latest'),
(6, 'STEAM_ID', ''),
(7, 'GITHUB_REPOS', '/widgets/github/repos'),
(8, 'GITHUB_FOLLOWERS', '/widgets/github/followers');

-- --------------------------------------------------------

--
-- Structure de la table `widget_cache`
--

CREATE TABLE `widget_cache` (
  `id` int(11) NOT NULL,
  `parameters` varchar(200) CHARACTER SET utf8 NOT NULL,
  `response` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `widget_id` int(11) NOT NULL,
  `expiration_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `github_users`
--
ALTER TABLE `github_users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_GITHUB_USERS_USER_ID` (`user_id`);

--
-- Index pour la table `local_users`
--
ALTER TABLE `local_users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_LOCAL_USERS_USER_ID` (`user_id`);

--
-- Index pour la table `saved_widgets`
--
ALTER TABLE `saved_widgets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_SAVED_WIDGET_USER_ID` (`user_id`),
  ADD KEY `FK_SAVED_WIDGET_WIDGET_ID` (`widget_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_USERS_USERTYPE_ID` (`usertype_id`);

--
-- Index pour la table `user_types`
--
ALTER TABLE `user_types`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `USERTYPE_UNIQUE` (`type`) USING BTREE;

--
-- Index pour la table `widgets`
--
ALTER TABLE `widgets`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `NAME_UNIQUE` (`name`),
  ADD UNIQUE KEY `ROUTE_UNIQUE` (`route`);

--
-- Index pour la table `widget_cache`
--
ALTER TABLE `widget_cache`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `per_widget_parameters_unique` (`parameters`,`widget_id`),
  ADD KEY `FK_WIDGET_CACHE_WIDGET_ID` (`widget_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `github_users`
--
ALTER TABLE `github_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `local_users`
--
ALTER TABLE `local_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `saved_widgets`
--
ALTER TABLE `saved_widgets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `user_types`
--
ALTER TABLE `user_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `widgets`
--
ALTER TABLE `widgets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `widget_cache`
--
ALTER TABLE `widget_cache`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `github_users`
--
ALTER TABLE `github_users`
  ADD CONSTRAINT `FK_GITHUB_USERS_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `local_users`
--
ALTER TABLE `local_users`
  ADD CONSTRAINT `FK_LOCAL_USERS_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `saved_widgets`
--
ALTER TABLE `saved_widgets`
  ADD CONSTRAINT `FK_SAVED_WIDGET_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_SAVED_WIDGET_WIDGET_ID` FOREIGN KEY (`widget_id`) REFERENCES `widgets` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK_USERS_USERTYPE_ID` FOREIGN KEY (`usertype_id`) REFERENCES `user_types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `widget_cache`
--
ALTER TABLE `widget_cache`
  ADD CONSTRAINT `FK_WIDGET_CACHE_WIDGET_ID` FOREIGN KEY (`widget_id`) REFERENCES `widgets` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
