-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Lis 17, 2023 at 10:49 AM
-- Wersja serwera: 10.4.28-MariaDB
-- Wersja PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sklep`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownik`
--

CREATE TABLE `uzytkownik` (
  `id` int(11) NOT NULL,
  `login` varchar(100) NOT NULL,
  `haslo` varchar(100) NOT NULL,
  `imie` varchar(100) NOT NULL,
  `nazwisko` varchar(100) NOT NULL,
  `admin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS produkty (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nazwa_produktu VARCHAR(100) NOT NULL,
    data_waznosci DATE NOT NULL,
    kategoria VARCHAR(100),
    ilosc INT NOT NULL
    );


CREATE TABLE IF NOT EXISTS grafik_pracy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_uzytkownika INT,
    dzien_tygodnia DATE,
    godzina_rozpoczecia TIME,
    godzina_zakonczenia TIME
);



-- Insert sample data
INSERT INTO produkty (nazwa_produktu, data_waznosci, kategoria, ilosc)
VALUES
    ('Product1', '2023-12-31', 'Category1', 10),
    ('Product2', '2023-11-15', 'Category2', 5),
    ('Product3', '2024-02-28', 'Category1', 20);
--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `uzytkownik`
--


ALTER TABLE `uzytkownik`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

ALTER TABLE grafik_pracy
ADD FOREIGN KEY (id_uzytkownika) REFERENCES uzytkownik(id);


/*
--gdy jest 2 użytkowników
INSERT INTO grafik_pracy (id_uzytkownika, dzien_tygodnia, godzina_rozpoczecia, godzina_zakonczenia)
VALUES
    (1, '2024-01-14', '08:00:00', '16:00:00'),
    (2, '2024-01-14', '08:00:00', '16:00:00');

*/




COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
