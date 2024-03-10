CREATE TABLE IF NOT EXISTS uzytkownik (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            login VARCHAR(100) NOT NULL,
                            haslo VARCHAR(100) NOT NULL,
                            imie VARCHAR(100) NOT NULL,
                            nazwisko VARCHAR(100) NOT NULL,
                            admin TINYINT NOT NULL
);

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



-- Indexes for table uzytkownik
CREATE INDEX uzytkownik_login_index ON uzytkownik (login);

ALTER TABLE grafik_pracy
    ADD FOREIGN KEY (id_uzytkownika) REFERENCES uzytkownik(id);
