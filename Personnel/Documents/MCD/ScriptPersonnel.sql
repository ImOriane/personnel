CREATE TABLE Ligues(
                       id_ligue VARCHAR(10),
                       nom_ligue_ VARCHAR(30),
                       PRIMARY KEY(id_ligue)
);

CREATE TABLE Personnels(
                           id_personnel_ VARCHAR(10),
                           nom_perso VARCHAR(15),
                           prenom_perso VARCHAR(20),
                           mail_perso VARCHAR(40),
                           role_perso VARCHAR(10),
                           password_perso VARCHAR(50),
                           date_arrivée VARCHAR(10),
                           date_depart_ VARCHAR(10),
                           id_ligue VARCHAR(10),
                           PRIMARY KEY(id_personnel_),
                           FOREIGN KEY(id_ligue) REFERENCES Ligues(id_ligue)
);
