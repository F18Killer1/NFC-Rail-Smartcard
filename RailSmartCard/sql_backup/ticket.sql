-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2016 at 09:35 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `etickets`
--

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE IF NOT EXISTS `ticket` (
  `ticketID` int(10) NOT NULL,
  `cardID` int(10) NOT NULL,
  `validFromStation` varchar(100) NOT NULL,
  `validToStation` varchar(100) NOT NULL,
  `ticketType` varchar(100) NOT NULL,
  `class` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `purchaseDateTime` datetime NOT NULL,
  `serviceID` int(10) NOT NULL,
  `validityDate` date NOT NULL,
  `validityTime` time NOT NULL,
  `seatReservation` varchar(100) NOT NULL,
  `ageGroup` varchar(100) NOT NULL,
  `isUsed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ticketID`),
  KEY `fk_card` (`cardID`),
  KEY `fk_service` (`serviceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`ticketID`, `cardID`, `validFromStation`, `validToStation`, `ticketType`, `class`, `price`, `purchaseDateTime`, `serviceID`, `validityDate`, `validityTime`, `seatReservation`, `ageGroup`, `isUsed`) VALUES
(1, 1099, 'Nottingham', 'Derby', 'Advance Single', 'Standard', '5.98', '2015-12-14 19:45:13', 1003, '2016-01-06', '19:30:00', 'Coach A, Seat 12', 'Adult', 0),
(2, 1099, 'Nottingham', 'Derby', 'Advance Single', 'Standard', '5.98', '2015-12-14 19:45:13', 1003, '2016-01-06', '19:30:00', 'Coach A, Seat 11', 'Adult', 0),
(3, 94, 'Nottingham', 'Birmingham New St', 'Advance Single', 'First', '6.35', '2015-12-15 20:26:45', 1001, '2016-01-06', '19:30:00', 'Coach B, Seat 34', 'Adult', 1),
(4, 94, 'Birmingham New St', 'Derby', 'Advance Single', 'First', '16.35', '2015-12-14 19:45:13', 1006, '2016-01-06', '20:30:00', 'Coach D, Seat 07', 'Adult', 1);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ticket`
--
ALTER TABLE `ticket`
  ADD CONSTRAINT `fk_service` FOREIGN KEY (`serviceID`) REFERENCES `service` (`serviceID`),
  ADD CONSTRAINT `fk_card` FOREIGN KEY (`cardID`) REFERENCES `card` (`cardID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
