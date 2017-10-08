
/*******************************************************************************
   Drop database if it exists
********************************************************************************/
DROP DATABASE IF EXISTS `moviedb`;


/*******************************************************************************
   Create database
********************************************************************************/
CREATE DATABASE `moviedb`;


USE `moviedb`;

CREATE TABLE `movies`
(
    `movieid` INT NOT NULL AUTO_INCREMENT,
    `title` NVARCHAR(80) NOT NULL,
    `actor` NVARCHAR(30) NOT NULL,
    `actress` NVARCHAR(30) NOT NULL,
    `genre` NVARCHAR(20)NOT NULL,
    `year` INT NOT NULL,
    CONSTRAINT `PK_movies` PRIMARY KEY  (`movieid`)
);

INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'Avatar',N'Sam Worthington',N'Zoe Saldana',N'Fantansy-Adventure', 2009);
INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'The Dark Knight',N'Christian Bale',N'Maggie Gyllenhaal',N'Crime-Thriller', 2008);
INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'Titanic',N'Leonardo DiCaprio',N'Kate Winslet',N'Drama-Romance', 1997);
INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'Inception',N'Leonardo DiCaprio',N'Ellen Page',N'Mystery-SciFi', 2010);
INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'The Avengers',N'Mark Ruffalo',N'Scarlett Johansson',N'Action-SciFi', 2012);
INSERT INTO `movies` (`title`, `actor`, `actress`, `genre`, `year` ) VALUES (N'Fight Club',N'Brad Pitt',N'elena Bonham Carter',N'Drama', 1999);