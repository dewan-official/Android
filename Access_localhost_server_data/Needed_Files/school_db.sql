-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 05, 2020 at 06:27 AM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `school_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_class`
--

CREATE TABLE `tbl_class` (
  `class_id` int(11) NOT NULL,
  `class_name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_class`
--

INSERT INTO `tbl_class` (`class_id`, `class_name`) VALUES
(1, 'Batch 1'),
(2, 'Batch2'),
(3, 'Batch3'),
(4, 'Batch4'),
(9, 'class2'),
(10, 'class3'),
(12, 'class4'),
(13, 'class5');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_student`
--

CREATE TABLE `tbl_student` (
  `id` int(11) NOT NULL,
  `student_name` varchar(30) DEFAULT NULL,
  `class_id` tinyint(4) DEFAULT NULL,
  `student_id` int(11) NOT NULL,
  `student_f_name` varchar(30) NOT NULL,
  `student_m_name` varchar(30) NOT NULL,
  `student_address` varchar(150) NOT NULL,
  `date_of_birth` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_student`
--

INSERT INTO `tbl_student` (`id`, `student_name`, `class_id`, `student_id`, `student_f_name`, `student_m_name`, `student_address`, `date_of_birth`) VALUES
(1, 'Kawsar', 1, 1, 'Father name', 'Mother name', 'Bhola', NULL),
(2, 'Pinki', 1, 2, 'Father name', 'Mother name', 'Barisal', '1999-01-01'),
(3, 'Farzana', 1, 3, 'Father name', 'Mother name', 'Chandpur', '1998-05-05'),
(4, 'Oli Ullah', 3, 4, 'Father name', 'Mother name', 'Tangail', '1995-01-01'),
(5, 'Arman', 3, 5, 'Father name', 'Mother name', 'Tangail', '1995-02-01'),
(6, 'Fahim', 3, 6, 'Father name', 'Mother name', 'Narayanganj', '1995-03-01'),
(7, 'Emon', 1, 4, 'Father name', 'Mother name', 'Tangail', '1995-04-01'),
(8, 'Shakhawat', 2, 1, 'Father name', 'Mother name', 'Noakhali', '1995-05-01'),
(9, 'Sagar', 1, 5, 'Father name', 'Mother name', 'Uganda', '1995-06-01'),
(10, 'Bipul', 2, 2, 'Father name', 'Mother name', 'Dhaka', '1995-07-01'),
(11, 'Promel', 3, 7, 'Father name', 'Mother name', 'Khulna', '1995-08-01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_class`
--
ALTER TABLE `tbl_class`
  ADD PRIMARY KEY (`class_id`);

--
-- Indexes for table `tbl_student`
--
ALTER TABLE `tbl_student`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `class_id` (`class_id`,`student_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_class`
--
ALTER TABLE `tbl_class`
  MODIFY `class_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tbl_student`
--
ALTER TABLE `tbl_student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
