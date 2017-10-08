
/*******************************************************************************
   Drop database if it exists
********************************************************************************/
DROP DATABASE IF EXISTS `booksdb`;


/*******************************************************************************
   Create database
********************************************************************************/
CREATE DATABASE `booksdb`;


USE `booksdb`;

CREATE TABLE `books`
(
    `bookid` INT NOT NULL AUTO_INCREMENT,
    `isbn` NVARCHAR(12) NOT NULL,
    `title` NVARCHAR(60) NOT NULL,
    `authors` NVARCHAR(60) NOT NULL,
    `price` float NOT NULL,
    CONSTRAINT `PK_books` PRIMARY KEY  (`bookid`)
);

/*
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (123-456-789,Java Servlets,Yusuf Ozbek,49.55);
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (246-680-999,Data Warehousing,Rick Sherman,64.99);
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (012-111-222,Business Analytics,Dan Mariaman,71.95);
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (253-745-865,Ajax programming,Neil Daniels,52.63);
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (526-742-100,OOPS using C++,Joyce M.Farrell,85.45);
INSERT INTO `books` (`isbn`, `title`, `authors`, `price` ) VALUES (268-845-365,JavaScript and JQuery,Jon Duckett,25.19);   */