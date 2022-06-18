SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Tabellenstruktur für Tabelle `Deposit`
--

DROP TABLE IF EXISTS `Deposit`;
CREATE TABLE `Deposit` (
                           `id` bigint(20) NOT NULL,
                           `device` varchar(32) NOT NULL,
                           `token` varchar(32) NOT NULL,
                           `amount` float NOT NULL,
                           `time` bigint(20) NOT NULL,
                           `origin` int(11) NOT NULL,
                           `details` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `Deposit`:
--   `device`
--       `Device` -> `id`
--   `token`
--       `Token` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Device`
--

DROP TABLE IF EXISTS `Device`;
CREATE TABLE `Device` (
                          `id` varchar(32) NOT NULL,
                          `time` bigint(20) NOT NULL,
                          `secret` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `Device`:
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Payment`
--

DROP TABLE IF EXISTS `Payment`;
CREATE TABLE `Payment` (
                           `id` bigint(20) NOT NULL,
                           `device` varchar(32) NOT NULL,
                           `token` varchar(32) NOT NULL,
                           `amount` float NOT NULL,
                           `time` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `Payment`:
--   `device`
--       `Device` -> `id`
--   `token`
--       `Token` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Payout`
--

DROP TABLE IF EXISTS `Payout`;
CREATE TABLE `Payout` (
                          `id` bigint(20) NOT NULL,
                          `device` varchar(32) NOT NULL,
                          `token` varchar(32) NOT NULL,
                          `amount` float NOT NULL,
                          `time` bigint(20) NOT NULL,
                          `target` int(11) NOT NULL,
                          `details` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `Payout`:
--   `device`
--       `Device` -> `id`
--   `token`
--       `Token` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Token`
--

DROP TABLE IF EXISTS `Token`;
CREATE TABLE `Token` (
                         `id` varchar(32) NOT NULL,
                         `device` varchar(32) NOT NULL,
                         `user` varchar(32) NOT NULL,
                         `time` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `Token`:
--   `device`
--       `Device` -> `id`
--   `user`
--       `User` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `TokenAction`
--

DROP TABLE IF EXISTS `TokenAction`;
CREATE TABLE `TokenAction` (
                               `id` int(11) NOT NULL,
                               `token` varchar(32) NOT NULL,
                               `device` varchar(32) NOT NULL,
                               `time` bigint(20) NOT NULL,
                               `user` varchar(32) NOT NULL,
                               `action` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `TokenAction`:
--   `device`
--       `Device` -> `id`
--   `token`
--       `Token` -> `id`
--   `user`
--       `User` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
                        `id` varchar(32) NOT NULL,
                        `device` varchar(32) NOT NULL,
                        `time` bigint(20) NOT NULL,
                        `recovery` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `User`:
--   `device`
--       `Device` -> `id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `UserActions`
--

DROP TABLE IF EXISTS `UserActions`;
CREATE TABLE `UserActions` (
                               `id` int(11) NOT NULL,
                               `device` varchar(32) NOT NULL,
                               `user` varchar(32) NOT NULL,
                               `time` bigint(20) NOT NULL,
                               `issuer` varchar(32) NOT NULL,
                               `target` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONEN DER TABELLE `UserActions`:
--   `device`
--       `Device` -> `id`
--   `issuer`
--       `User` -> `id`
--   `user`
--       `User` -> `id`
--

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
-- Indizes für die Tabelle `Payment`
--
ALTER TABLE `Payment`
    ADD PRIMARY KEY (`id`),
  ADD KEY `fk_Payment_device__id` (`device`),
  ADD KEY `fk_Payment_token__id` (`token`);

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
-- Constraints der Tabelle `Payment`
--
ALTER TABLE `Payment`
    ADD CONSTRAINT `fk_Payment_device__id` FOREIGN KEY (`device`) REFERENCES `Device` (`id`),
  ADD CONSTRAINT `fk_Payment_token__id` FOREIGN KEY (`token`) REFERENCES `Token` (`id`);

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