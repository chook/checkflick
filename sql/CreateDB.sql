--------------------------------------------------------
--  DDL for Sequence COUNTRIES_SEQ
--------------------------------------------------------
   CREATE SEQUENCE  "COUNTRIES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence GENRES_SEQ
--------------------------------------------------------
   CREATE SEQUENCE  "GENRES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence LANGUAGES_SEQ
--------------------------------------------------------
   CREATE SEQUENCE  "LANGUAGES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence MOVIES_SEQ
--------------------------------------------------------
   CREATE SEQUENCE  "MOVIES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Sequence PERSONS_SEQ
--------------------------------------------------------
   CREATE SEQUENCE  "PERSONS_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

--------------------------------------------------------
--  DDL for Table COUNTRIES
--------------------------------------------------------
  CREATE TABLE "COUNTRIES" 
   (	"COUNTRY_ID" NUMBER, 
	"COUNTRY_NAME" VARCHAR2(100 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table GENRES
--------------------------------------------------------
  CREATE TABLE "GENRES" 
   (	"GENRE_ID" NUMBER, 
	"GENRE_NAME" VARCHAR2(100 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table LANGUAGES
--------------------------------------------------------
  CREATE TABLE "LANGUAGES" 
   (	"LANGUAGE_ID" NUMBER, 
	"LANGUAGE_NAME" VARCHAR2(100 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table PERSON_MOVIE_CREDITS
--------------------------------------------------------
  CREATE TABLE "PERSON_MOVIE_CREDITS" 
   (	"PERSON_ID" NUMBER, 
	"MOVIE_ID" NUMBER, 
	"PRODUCTION_ROLE_ID" NUMBER, 
	"IS_ACTOR" CHAR(1 CHAR), 
	"ACTOR_ROLE" VARCHAR2(1000 CHAR), 
	"ACTOR_CREDITS_RANK" NUMBER
   ) ;
 
   COMMENT ON COLUMN "PERSON_MOVIE_CREDITS"."IS_ACTOR" IS 'Acts as a boolean field: Receives the values Y/N';
   
--------------------------------------------------------
--  DDL for Table MOVIE_COUNTRIES
--------------------------------------------------------
  CREATE TABLE "MOVIE_COUNTRIES" 
   (	"MOVIE_ID" NUMBER, 
	"COUNTRY_ID" NUMBER
   ) ;

--------------------------------------------------------
--  DDL for Table MOVIE_GENRES
--------------------------------------------------------
  CREATE TABLE "MOVIE_GENRES" 
   (	"MOVIE_ID" NUMBER, 
	"GENRE_ID" NUMBER
   ) ;

--------------------------------------------------------
--  DDL for Table MOVIE_LANGUAGES
--------------------------------------------------------
  CREATE TABLE "MOVIE_LANGUAGES" 
   (	"MOVIE_ID" NUMBER, 
	"LANGUAGE_ID" NUMBER
   ) ;

--------------------------------------------------------
--  DDL for Table MOVIE_QUOTES
--------------------------------------------------------
  CREATE TABLE "MOVIE_QUOTES" 
   (	"MOVIE_ID" NUMBER, 
	"QUOTE_TEXT" VARCHAR2(2000 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table MOVIES
--------------------------------------------------------
  CREATE TABLE "MOVIES" 
   (	"MOVIE_ID" NUMBER, 
	"MOVIE_NAME" VARCHAR2(250 CHAR), 
	"MOVIE_YEAR" NUMBER, 
	"MOVIE_ROMAN_NOTATION" VARCHAR2(5 CHAR),
	"MOVIE_MADE_FOR" VARCHAR2(5 CHAR),
  "MOVIE_RUNNING_TIME" NUMBER,
	"MOVIE_PLOT_TEXT" VARCHAR2(4000 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table PERSON_AKA_NAMES
--------------------------------------------------------
  CREATE TABLE "PERSON_AKA_NAMES" 
   (	"PERSON_ID" NUMBER, 
	"PERSON_AKA_NAME" VARCHAR2(150 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table PERSON_QUOTES
--------------------------------------------------------
  CREATE TABLE "PERSON_QUOTES" 
   (	"PERSON_ID" NUMBER, 
	"QUOTE_TEXT" VARCHAR2(2000 CHAR)
   ) ;

--------------------------------------------------------
--  DDL for Table PERSONS
--------------------------------------------------------
  CREATE TABLE "PERSONS" 
   (	"PERSON_ID" NUMBER, 
	"PERSON_NAME" VARCHAR2(200 CHAR), 
	"YEAR_OF_BIRTH" NUMBER, 
	"CITY_OF_BIRTH" VARCHAR2(200 CHAR), 
	"COUNTRY_OF_BIRTH_ID" NUMBER, 
	"YEAR_OF_DEATH" NUMBER
   ) ;
 

   COMMENT ON COLUMN "PERSONS"."YEAR_OF_BIRTH" IS 'In case only the year is known, we don''t want the application to think 01/01/YY was the exact date...';
   COMMENT ON COLUMN "PERSONS"."YEAR_OF_DEATH" IS 'In case only the year is known, we don''t want the application to think 01/01/YY was the exact date...';

--------------------------------------------------------
--  DDL for Table PRODUCTION_ROLES
--------------------------------------------------------
  CREATE TABLE "PRODUCTION_ROLES" 
   (	"PRODUCTION_ROLE_ID" NUMBER PRIMARY KEY, 
	"PRODUCTION_ROLE_NAME" VARCHAR2(100 CHAR)
   ) ;

--------------------------------------------------------
--  Constraints for Table MOVIE_QUOTES
--------------------------------------------------------
  ALTER TABLE "MOVIE_QUOTES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "MOVIE_QUOTES" MODIFY ("QUOTE_TEXT" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PERSON_QUOTES
--------------------------------------------------------
  ALTER TABLE "PERSON_QUOTES" MODIFY ("PERSON_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSON_QUOTES" MODIFY ("QUOTE_TEXT" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PERSON_AKA_NAMES
--------------------------------------------------------
  ALTER TABLE "PERSON_AKA_NAMES" MODIFY ("PERSON_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSON_AKA_NAMES" MODIFY ("PERSON_AKA_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table LANGUAGES
--------------------------------------------------------
  ALTER TABLE "LANGUAGES" MODIFY ("LANGUAGE_ID" NOT NULL ENABLE);
  ALTER TABLE "LANGUAGES" MODIFY ("LANGUAGE_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PRODUCTION_ROLES
--------------------------------------------------------
  ALTER TABLE "PRODUCTION_ROLES" MODIFY ("PRODUCTION_ROLE_ID" NOT NULL ENABLE);
  ALTER TABLE "PRODUCTION_ROLES" MODIFY ("PRODUCTION_ROLE_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table MOVIES
--------------------------------------------------------
  ALTER TABLE "MOVIES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "MOVIES" MODIFY ("MOVIE_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table COUNTRIES
--------------------------------------------------------
  ALTER TABLE "COUNTRIES" MODIFY ("COUNTRY_ID" NOT NULL ENABLE);
  ALTER TABLE "COUNTRIES" MODIFY ("COUNTRY_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table MOVIE_GENRES
--------------------------------------------------------
  ALTER TABLE "MOVIE_GENRES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "MOVIE_GENRES" MODIFY ("GENRE_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table MOVIE_COUNTRIES
--------------------------------------------------------
  ALTER TABLE "MOVIE_COUNTRIES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "MOVIE_COUNTRIES" MODIFY ("COUNTRY_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table GENRES
--------------------------------------------------------
  ALTER TABLE "GENRES" MODIFY ("GENRE_ID" NOT NULL ENABLE);
  ALTER TABLE "GENRES" MODIFY ("GENRE_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table MOVIE_LANGUAGES
--------------------------------------------------------
  ALTER TABLE "MOVIE_LANGUAGES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "MOVIE_LANGUAGES" MODIFY ("LANGUAGE_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PERSONS
--------------------------------------------------------
  ALTER TABLE "PERSONS" MODIFY ("PERSON_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSONS" MODIFY ("PERSON_NAME" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PERSON_MOVIE_CREDITS
--------------------------------------------------------
  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_CHK1" CHECK (
IS_ACTOR IN ('Y','N')
) ENABLE;
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("PERSON_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("PRODUCTION_ROLE_ID" NOT NULL ENABLE);
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("IS_ACTOR" NOT NULL ENABLE);

--------------------------------------------------------
--  DDL for Trigger BI_COUNTRIES
--------------------------------------------------------
  CREATE OR REPLACE TRIGGER "BI_COUNTRIES" 
BEFORE INSERT ON COUNTRIES
FOR EACH ROW 
BEGIN
    SELECT 	COUNTRIES_SEQ.NEXTVAL
		INTO		:NEW.COUNTRY_ID
		FROM		dual;
END;

/
ALTER TRIGGER "BI_COUNTRIES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger BI_GENRES
--------------------------------------------------------
  CREATE OR REPLACE TRIGGER "BI_GENRES" 
BEFORE INSERT ON GENRES
FOR EACH ROW 
BEGIN
    SELECT  GENRES_SEQ.NEXTVAL
		INTO		:NEW.GENRE_ID
		FROM		dual;
END;

/
ALTER TRIGGER "BI_GENRES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger BI_LANGUAGES
--------------------------------------------------------
  CREATE OR REPLACE TRIGGER "BI_LANGUAGES" 
BEFORE INSERT ON LANGUAGES
FOR EACH ROW 
BEGIN
    SELECT  LANGUAGES_SEQ.NEXTVAL
		INTO		:NEW.LANGUAGE_ID
		FROM		dual;
END;

/
ALTER TRIGGER "BI_LANGUAGES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger BI_MOVIES
--------------------------------------------------------
  CREATE OR REPLACE TRIGGER "BI_MOVIES" 
BEFORE INSERT ON MOVIES
FOR EACH ROW 
BEGIN
    SELECT  MOVIES_SEQ.NEXTVAL
		INTO		:NEW.MOVIE_ID
		FROM		dual;
END;

/
ALTER TRIGGER "BI_MOVIES" ENABLE;

--------------------------------------------------------
--  DDL for Trigger BI_PERSONS
--------------------------------------------------------
  CREATE OR REPLACE TRIGGER "BI_PERSONS" 
BEFORE INSERT ON PERSONS
FOR EACH ROW 
BEGIN
    SELECT  PERSONS_SEQ.NEXTVAL
		INTO		:NEW.PERSON_ID
		FROM		dual;
END;

/
ALTER TRIGGER "BI_PERSONS" ENABLE;

--------------------------------------------------------
--  Hard Coded data for PRODUCTION_ROLES
--------------------------------------------------------
INSERT INTO "PRODUCTION_ROLES" 
(PRODUCTION_ROLE_ID, PRODUCTION_ROLE_NAME)
VALUES (1, 'Actors');

INSERT INTO "PRODUCTION_ROLES" 
(PRODUCTION_ROLE_ID, PRODUCTION_ROLE_NAME)
VALUES (2, 'Producers');

INSERT INTO "PRODUCTION_ROLES" 
(PRODUCTION_ROLE_ID, PRODUCTION_ROLE_NAME)
VALUES (3, 'Directors');

INSERT INTO "PRODUCTION_ROLES" 
(PRODUCTION_ROLE_ID, PRODUCTION_ROLE_NAME)
VALUES (4, 'Writers');

