-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Erstellungszeit: 20. Jul 2022 um 23:02
-- Server-Version: 8.0.1-dmr
-- PHP-Version: 8.0.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `z8`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Deposit`
--

CREATE TABLE `Deposit` (
                           `id` bigint(20) NOT NULL,
                           `time` bigint(20) NOT NULL,
                           `device` varchar(32) NOT NULL,
                           `token` varchar(32) NOT NULL,
                           `amount` float NOT NULL,
                           `origin` int(11) NOT NULL,
                           `details` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Device`
--

CREATE TABLE `Device` (
                          `id` varchar(32) NOT NULL,
                          `time` bigint(20) NOT NULL,
                          `secret` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ExternalDevice`
--

CREATE TABLE `ExternalDevice` (
                                  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                  `time` bigint(20) NOT NULL,
                                  `target` varchar(40) CHARACTER SET utf8mb4 NOT NULL,
                                  `secret` varchar(40) CHARACTER SET utf8mb4 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `InternalDevice`
--

CREATE TABLE `InternalDevice` (
                                  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                  `time` bigint(20) NOT NULL,
                                  `secret` varchar(40) CHARACTER SET utf8mb4 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Payment`
--

CREATE TABLE `Payment` (
                           `id` bigint(20) NOT NULL,
                           `time` bigint(20) NOT NULL,
                           `internal` varchar(32) NOT NULL,
                           `external` varchar(32) NOT NULL,
                           `token` varchar(32) NOT NULL,
                           `amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `PaymentRequest`
--

CREATE TABLE `PaymentRequest` (
                                  `id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                  `time` bigint(20) NOT NULL,
                                  `source` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                  `target` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                  `amount` float NOT NULL,
                                  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Payout`
--

CREATE TABLE `Payout` (
                          `id` bigint(20) NOT NULL,
                          `time` bigint(20) NOT NULL,
                          `device` varchar(32) NOT NULL,
                          `token` varchar(32) NOT NULL,
                          `amount` float NOT NULL,
                          `target` int(11) NOT NULL COMMENT 'Target platform, could be cash, service, ...',
                          `details` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Token`
--

CREATE TABLE `Token` (
                         `id` varchar(32) NOT NULL,
                         `time` bigint(20) NOT NULL,
                         `device` varchar(32) NOT NULL,
                         `user` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `TokenAction`
--

CREATE TABLE `TokenAction` (
                               `id` int(11) NOT NULL,
                               `time` bigint(20) NOT NULL,
                               `token` varchar(32) NOT NULL,
                               `device` varchar(32) NOT NULL,
                               `user` varchar(32) NOT NULL,
                               `action` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

CREATE TABLE `User` (
                        `id` varchar(32) NOT NULL,
                        `time` bigint(20) NOT NULL,
                        `device` varchar(32) NOT NULL,
                        `mail` varchar(40) NOT NULL,
                        `recovery` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `UserActions`
--

CREATE TABLE `UserActions` (
                               `id` int(11) NOT NULL,
                               `time` bigint(20) NOT NULL,
                               `device` varchar(32) NOT NULL,
                               `user` varchar(32) NOT NULL,
                               `issuer` varchar(32) NOT NULL,
                               `target` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `Deposit`
--
ALTER TABLE `Deposit`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_Deposit_device__id` (`device`),
    ADD KEY `fk_Deposit_token__id` (`token`);

--
-- Indizes für die Tabelle `Device`
--
ALTER TABLE `Device`
    ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `ExternalDevice`
--
ALTER TABLE `ExternalDevice`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_ExternalDevice_target__id` (`target`);

--
-- Indizes für die Tabelle `InternalDevice`
--
ALTER TABLE `InternalDevice`
    ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `Payment`
--
ALTER TABLE `Payment`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_Payment_device__id` (`internal`),
    ADD KEY `fk_Payment_token__id` (`token`),
    ADD KEY `fk_Payment_external__id` (`external`);

--
-- Indizes für die Tabelle `PaymentRequest`
--
ALTER TABLE `PaymentRequest`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_PaymentRequest_source__id` (`source`),
    ADD KEY `fk_PaymentRequest_target__id` (`target`);

--
-- Indizes für die Tabelle `Payout`
--
ALTER TABLE `Payout`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_Payout_device__id` (`device`),
    ADD KEY `fk_Payout_token__id` (`token`);

--
-- Indizes für die Tabelle `Token`
--
ALTER TABLE `Token`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_Token_device__id` (`device`),
    ADD KEY `fk_Token_user__id` (`user`);

--
-- Indizes für die Tabelle `TokenAction`
--
ALTER TABLE `TokenAction`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_TokenAction_token__id` (`token`),
    ADD KEY `fk_TokenAction_device__id` (`device`),
    ADD KEY `fk_TokenAction_user__id` (`user`);

--
-- Indizes für die Tabelle `User`
--
ALTER TABLE `User`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_User_device__id` (`device`);

--
-- Indizes für die Tabelle `UserActions`
--
ALTER TABLE `UserActions`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_UserActions_device__id` (`device`),
    ADD KEY `fk_UserActions_user__id` (`user`),
    ADD KEY `fk_UserActions_issuer__id` (`issuer`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `Deposit`
--
ALTER TABLE `Deposit`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `Payment`
--
ALTER TABLE `Payment`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `Payout`
--
ALTER TABLE `Payout`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `TokenAction`
--
ALTER TABLE `TokenAction`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `UserActions`
--
ALTER TABLE `UserActions`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Deposit`
--
ALTER TABLE `Deposit`
    ADD CONSTRAINT `fk_Deposit_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
    ADD CONSTRAINT `fk_Deposit_token__id` FOREIGN KEY (`token`) REFERENCES `Token` (`id`);

--
-- Constraints der Tabelle `ExternalDevice`
--
ALTER TABLE `ExternalDevice`
    ADD CONSTRAINT `fk_ExternalDevice_target__id` FOREIGN KEY (`target`) REFERENCES `InternalDevice` (`id`);

--
-- Constraints der Tabelle `Payment`
--
ALTER TABLE `Payment`
    ADD CONSTRAINT `fk_Payment_external__id` FOREIGN KEY (`external`) REFERENCES `ExternalDevice` (`id`),
    ADD CONSTRAINT `fk_Payment_internal__id` FOREIGN KEY (`internal`) REFERENCES `InternalDevice` (`id`),
    ADD CONSTRAINT `fk_Payment_token__id` FOREIGN KEY (`token`) REFERENCES `Token` (`id`);

--
-- Constraints der Tabelle `PaymentRequest`
--
ALTER TABLE `PaymentRequest`
    ADD CONSTRAINT `fk_PaymentRequest_source__id` FOREIGN KEY (`source`) REFERENCES `ExternalDevice` (`id`),
    ADD CONSTRAINT `fk_PaymentRequest_target__id` FOREIGN KEY (`target`) REFERENCES `InternalDevice` (`id`);

--
-- Constraints der Tabelle `Payout`
--
ALTER TABLE `Payout`
    ADD CONSTRAINT `fk_Payout_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
    ADD CONSTRAINT `fk_Payout_token__id` FOREIGN KEY (`token`) REFERENCES `Token` (`id`);

--
-- Constraints der Tabelle `Token`
--
ALTER TABLE `Token`
    ADD CONSTRAINT `fk_Token_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
    ADD CONSTRAINT `fk_Token_user__id` FOREIGN KEY (`user`) REFERENCES `User` (`id`);

--
-- Constraints der Tabelle `TokenAction`
--
ALTER TABLE `TokenAction`
    ADD CONSTRAINT `fk_TokenAction_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
    ADD CONSTRAINT `fk_TokenAction_token__id` FOREIGN KEY (`token`) REFERENCES `Token` (`id`),
    ADD CONSTRAINT `fk_TokenAction_user__id` FOREIGN KEY (`user`) REFERENCES `User` (`id`);

--
-- Constraints der Tabelle `User`
--
ALTER TABLE `User`
    ADD CONSTRAINT `fk_User_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`);

--
-- Constraints der Tabelle `UserActions`
--
ALTER TABLE `UserActions`
    ADD CONSTRAINT `fk_UserActions_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
    ADD CONSTRAINT `fk_UserActions_issuer__id` FOREIGN KEY (`issuer`) REFERENCES `User` (`id`),
    ADD CONSTRAINT `fk_UserActions_user__id` FOREIGN KEY (`user`) REFERENCES `User` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;