

CREATE TABLE `blood_donation_record` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `user_id` int(255) NOT NULL,
  `last_donated` varchar(255) NOT NULL,
  `note` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

INSERT INTO blood_donation_record VALUES("15","2","1651088233","I donated to a Quantum Blood Bank willingly. 
They were on a campaign on April.");



CREATE TABLE `friend_requests` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `from_user` int(255) NOT NULL,
  `to_user` int(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;

INSERT INTO friend_requests VALUES("17","2","8","1672348636");
INSERT INTO friend_requests VALUES("18","2","2","1672350327");
INSERT INTO friend_requests VALUES("22","2","4","1672352452");
INSERT INTO friend_requests VALUES("23","8","2","1672611829");



CREATE TABLE `friends` (
  `user_1` int(255) NOT NULL,
  `user_2` int(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO friends VALUES("2","5","1661602442");
INSERT INTO friends VALUES("2","6","1661601111");



CREATE TABLE `userdata` (
  `user_id` int(255) NOT NULL,
  `height` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `EyeSaverTimer` int(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO userdata VALUES("2","87","1.7","900");



CREATE TABLE `users` (
  `user_id` int(255) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `blood_group` varchar(4) NOT NULL,
  `gender` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

INSERT INTO users VALUES("1","Tawsif","Torabi","tawsiftorabi","1234","tawsiftorabi.jr@gmail.com","O+","Male");
INSERT INTO users VALUES("2","John","Doe","user","pass","jhon@care.me","AB+","Male");
INSERT INTO users VALUES("3","Rakib","Hasan","rakib123","111111111","rakib@gmail.com","O-","Male");
INSERT INTO users VALUES("4","Abir","Ahmed","abirabir","111111111","abir@gmail.com","B+","Male");
INSERT INTO users VALUES("5","Rafid","Zaman","rafidrafid","111111111","rafid@mail.com","B+","Male");
INSERT INTO users VALUES("6","Kazi","Khalid","khalid","111111111","khalid@uiu.ac.bd","B+","Male");
INSERT INTO users VALUES("7","Tanvir","Ahad","ahad","111111111","ahad@uiu.ac.bd","AB+","Male");
INSERT INTO users VALUES("8","Abu","Sayed","sayed","111111111","sayed@gmail.com","O-","Male");



CREATE TABLE `weight_height_records` (
  `user_id` int(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL,
  `weight` varchar(255) NOT NULL,
  `height` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


