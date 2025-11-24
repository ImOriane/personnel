CREATE TABLE Ligues(
                       id_ligue VARCHAR(50),
                       nom_ligue_ VARCHAR(50),
                       PRIMARY KEY(id_ligue)
);

CREATE TABLE Personnels(
                           id_personnel_ VARCHAR(50),
                           nom_perso VARCHAR(50),
                           prenom_perso VARCHAR(50),
                           mail_perso VARCHAR(50),
                           role_perso VARCHAR(50),
                           password_perso VARCHAR(50),
                           date_arrivée VARCHAR(50),
                           date_depart_ VARCHAR(50),
                           id_ligue VARCHAR(50),
                           PRIMARY KEY(id_personnel_),
                           FOREIGN KEY(id_ligue) REFERENCES Ligues(id_ligue)
);
