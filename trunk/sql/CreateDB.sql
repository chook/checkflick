--------------------------------------------------------
--  File created - שבת-ינואר-24-2009   
--------------------------------------------------------

DROP TABLE "COLOR_INFO" cascade constraints;
  DROP TABLE "CONNECTIONS_RELATIONS" cascade constraints;
  DROP TABLE "COUNTRIES" cascade constraints;
  DROP TABLE "GENRES" cascade constraints;
  DROP TABLE "GOOFS_TYPES" cascade constraints;
  DROP TABLE "LANGUAGES" cascade constraints;
  DROP TABLE "MOVIE_AKA_NAMES" cascade constraints;
  DROP TABLE "MOVIE_APPEARANCES" cascade constraints;
  DROP TABLE "PERSON_MOVIE_CREDITS" cascade constraints;
  DROP TABLE "MOVIE_CONNECTIONS" cascade constraints;
  DROP TABLE "MOVIE_COUNTRIES" cascade constraints;
  DROP TABLE "MOVIE_CRAZY_CREDITS" cascade constraints;
  DROP TABLE "MOVIE_GENRES" cascade constraints;
  DROP TABLE "MOVIE_GOOFS" cascade constraints;
  DROP TABLE "MOVIE_LANGUAGES" cascade constraints;
  DROP TABLE "MOVIE_QUOTES" cascade constraints;
  DROP TABLE "MOVIES" cascade constraints;
  DROP TABLE "MOVIE_TRIVIA" cascade constraints;
  DROP TABLE "PERSON_AKA_NAMES" cascade constraints;
  DROP TABLE "PERSON_QUOTES" cascade constraints;
  DROP TABLE "PERSONS" cascade constraints;
  DROP TABLE "PERSON_TRIVIA" cascade constraints;
  DROP TABLE "PRODUCTION_ROLES" cascade constraints;
  DROP SEQUENCE "COLOR_INFO_SEQ";
  DROP SEQUENCE "CONNECTIONS_RELATIONS_SEQ";
  DROP SEQUENCE "COUNTRIES_SEQ";
  DROP SEQUENCE "GENRES_SEQ";
  DROP SEQUENCE "GOOFS_TYPE_SEQ";
  DROP SEQUENCE "LANGUAGES_SEQ";
  DROP SEQUENCE "MOVIES_SEQ";
  DROP SEQUENCE "PERSONS_SEQ";
  DROP SEQUENCE "PRODUCTION_ROLES_SEQ";

--------------------------------------------------------
--  DDL for Sequence COLOR_INFO_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "COLOR_INFO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CONNECTIONS_RELATIONS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CONNECTIONS_RELATIONS_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence COUNTRIES_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "COUNTRIES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence GENRES_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "GENRES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence GOOFS_TYPE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "GOOFS_TYPE_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
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
--  DDL for Sequence PRODUCTION_ROLES_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PRODUCTION_ROLES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table COLOR_INFO
--------------------------------------------------------

  CREATE TABLE "COLOR_INFO" 
   (	"COLOR_INFO_ID" NUMBER, 
	"COLOR_INFO_TEXT" VARCHAR2(100 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table CONNECTIONS_RELATIONS
--------------------------------------------------------

  CREATE TABLE "CONNECTIONS_RELATIONS" 
   (	"CONNECTION_RELATION_ID" NUMBER, 
	"CONNECTION_RELATION_NAME" VARCHAR2(100 CHAR)
   ) ;
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
--  DDL for Table GOOFS_TYPES
--------------------------------------------------------

  CREATE TABLE "GOOFS_TYPES" 
   (	"GOOF_TYPE_ID" NUMBER, 
	"GOOF_NAME" VARCHAR2(100 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table LANGUAGES
--------------------------------------------------------

  CREATE TABLE "LANGUAGES" 
   (	"LANGUAGE_ID" NUMBER, 
	"LANGUAGE_NAME" VARCHAR2(100 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_AKA_NAMES
--------------------------------------------------------

  CREATE TABLE "MOVIE_AKA_NAMES" 
   (	"MOVIE_ID" NUMBER, 
	"MOVIE_AKA_NAME" VARCHAR2(200 CHAR), 
	"MOVIE_AKA_NAME_YEAR" NUMBER, 
	"MOVIE_AKA_NAME_COUNTRY_ID" NUMBER
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
--  DDL for Table MOVIE_CONNECTIONS
--------------------------------------------------------

  CREATE TABLE "MOVIE_CONNECTIONS" 
   (	"MOVIE_ID" NUMBER, 
	"CONNECTED_MOVIE_ID" NUMBER, 
	"MOVIE_CONNECTION_RELATION_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_COUNTRIES
--------------------------------------------------------

  CREATE TABLE "MOVIE_COUNTRIES" 
   (	"MOVIE_ID" NUMBER, 
	"COUNTRY_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_CRAZY_CREDITS
--------------------------------------------------------

  CREATE TABLE "MOVIE_CRAZY_CREDITS" 
   (	"MOVIE_ID" NUMBER, 
	"CRAZY_CREDIT_TEXT" VARCHAR2(2000 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_GENRES
--------------------------------------------------------

  CREATE TABLE "MOVIE_GENRES" 
   (	"MOVIE_ID" NUMBER, 
	"GENRE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_GOOFS
--------------------------------------------------------

  CREATE TABLE "MOVIE_GOOFS" 
   (	"MOVIE_ID" NUMBER, 
	"GOOF_TYPE_ID" NUMBER, 
	"GOOF_TEXT" VARCHAR2(2000 CHAR)
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
	"MOVIE_COLOR_INFO_ID" NUMBER, 
	"MOVIE_RUNNING_TIME" NUMBER, 
	"MOVIE_TAGLINE" VARCHAR2(4000 CHAR), 
	"MOVIE_PLOT_TEXT" VARCHAR2(4000 CHAR), 
	"MOVIE_FILMING_LOCATION_NAME" VARCHAR2(2000 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table MOVIE_TRIVIA
--------------------------------------------------------

  CREATE TABLE "MOVIE_TRIVIA" 
   (	"MOVIE_ID" NUMBER, 
	"TRIVIA_TEXT" VARCHAR2(2000 CHAR)
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
	"REAL_NAME" VARCHAR2(200 CHAR), 
	"NICKNAMES" VARCHAR2(1000 CHAR), 
	"DATE_OF_BIRTH" DATE, 
	"YEAR_OF_BIRTH" NUMBER, 
	"CITY_OF_BIRTH" VARCHAR2(200 CHAR), 
	"COUNTRY_OF_BIRTH_ID" NUMBER, 
	"DATE_OF_DEATH" DATE, 
	"YEAR_OF_DEATH" NUMBER, 
	"HEIGHT" NUMBER, 
	"TRADEMARK" VARCHAR2(2000 CHAR), 
	"BIOGRAPHY_TEXT" VARCHAR2(4000 CHAR)
   ) ;
 

   COMMENT ON COLUMN "PERSONS"."YEAR_OF_BIRTH" IS 'In case only the year is known, we don''t want the application to think 01/01/YY was the exact date...';
 
   COMMENT ON COLUMN "PERSONS"."YEAR_OF_DEATH" IS 'In case only the year is known, we don''t want the application to think 01/01/YY was the exact date...';
 
   COMMENT ON COLUMN "PERSONS"."HEIGHT" IS 'Value in cm.';
--------------------------------------------------------
--  DDL for Table PERSON_TRIVIA
--------------------------------------------------------

  CREATE TABLE "PERSON_TRIVIA" 
   (	"PERSON_ID" NUMBER, 
	"TRIVIA_TEXT" VARCHAR2(4000 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table PRODUCTION_ROLES
--------------------------------------------------------

  CREATE TABLE "PRODUCTION_ROLES" 
   (	"PRODUCTION_ROLE_ID" NUMBER, 
	"PRODUCTION_ROLE_NAME" VARCHAR2(100 CHAR)
   ) ;
--------------------------------------------------------
--  Constraints for Table MOVIE_QUOTES
--------------------------------------------------------

  ALTER TABLE "MOVIE_QUOTES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_QUOTES" MODIFY ("QUOTE_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONNECTIONS_RELATIONS
--------------------------------------------------------

  ALTER TABLE "CONNECTIONS_RELATIONS" ADD CONSTRAINT "CONNECTIONS_RELATIONS_PK" PRIMARY KEY ("CONNECTION_RELATION_ID") ENABLE;
 
  ALTER TABLE "CONNECTIONS_RELATIONS" MODIFY ("CONNECTION_RELATION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "CONNECTIONS_RELATIONS" MODIFY ("CONNECTION_RELATION_NAME" NOT NULL ENABLE);
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
--  Constraints for Table COLOR_INFO
--------------------------------------------------------

  ALTER TABLE "COLOR_INFO" ADD CONSTRAINT "COLOR_INFO_PK" PRIMARY KEY ("COLOR_INFO_ID") ENABLE;
 
  ALTER TABLE "COLOR_INFO" MODIFY ("COLOR_INFO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "COLOR_INFO" MODIFY ("COLOR_INFO_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LANGUAGES
--------------------------------------------------------

  ALTER TABLE "LANGUAGES" ADD CONSTRAINT "LANGUAGES_PK" PRIMARY KEY ("LANGUAGE_ID") ENABLE;
 
  ALTER TABLE "LANGUAGES" MODIFY ("LANGUAGE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "LANGUAGES" MODIFY ("LANGUAGE_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIE_CONNECTIONS
--------------------------------------------------------

  ALTER TABLE "MOVIE_CONNECTIONS" ADD CONSTRAINT "MOVIE_CONNECTIONS_PK" PRIMARY KEY ("MOVIE_ID", "CONNECTED_MOVIE_ID", "MOVIE_CONNECTION_RELATION_ID") ENABLE;
 
  ALTER TABLE "MOVIE_CONNECTIONS" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_CONNECTIONS" MODIFY ("CONNECTED_MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_CONNECTIONS" MODIFY ("MOVIE_CONNECTION_RELATION_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PRODUCTION_ROLES
--------------------------------------------------------

  ALTER TABLE "PRODUCTION_ROLES" ADD CONSTRAINT "PRODUCTION_ROLES_PK" PRIMARY KEY ("PRODUCTION_ROLE_ID") ENABLE;
 
  ALTER TABLE "PRODUCTION_ROLES" MODIFY ("PRODUCTION_ROLE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PRODUCTION_ROLES" MODIFY ("PRODUCTION_ROLE_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIE_CRAZY_CREDITS
--------------------------------------------------------

  ALTER TABLE "MOVIE_CRAZY_CREDITS" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_CRAZY_CREDITS" MODIFY ("CRAZY_CREDIT_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIES
--------------------------------------------------------

  ALTER TABLE "MOVIES" ADD CONSTRAINT "MOVIES_PK" PRIMARY KEY ("MOVIE_ID") ENABLE;
 
  ALTER TABLE "MOVIES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIES" MODIFY ("MOVIE_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table COUNTRIES
--------------------------------------------------------

  ALTER TABLE "COUNTRIES" ADD CONSTRAINT "COUNTRIES_PK" PRIMARY KEY ("COUNTRY_ID") ENABLE;
 
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
--  Constraints for Table MOVIE_AKA_NAMES
--------------------------------------------------------

  ALTER TABLE "MOVIE_AKA_NAMES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_AKA_NAMES" MODIFY ("MOVIE_AKA_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table GENRES
--------------------------------------------------------

  ALTER TABLE "GENRES" ADD CONSTRAINT "GENRES_PK" PRIMARY KEY ("GENRE_ID") ENABLE;
 
  ALTER TABLE "GENRES" MODIFY ("GENRE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "GENRES" MODIFY ("GENRE_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PERSON_TRIVIA
--------------------------------------------------------

  ALTER TABLE "PERSON_TRIVIA" MODIFY ("PERSON_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PERSON_TRIVIA" MODIFY ("TRIVIA_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIE_LANGUAGES
--------------------------------------------------------

  ALTER TABLE "MOVIE_LANGUAGES" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_LANGUAGES" MODIFY ("LANGUAGE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PERSONS
--------------------------------------------------------

  ALTER TABLE "PERSONS" ADD CONSTRAINT "PERSONS_PK" PRIMARY KEY ("PERSON_ID") ENABLE;
 
  ALTER TABLE "PERSONS" MODIFY ("PERSON_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PERSONS" MODIFY ("PERSON_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIE_TRIVIA
--------------------------------------------------------

  ALTER TABLE "MOVIE_TRIVIA" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_TRIVIA" MODIFY ("TRIVIA_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOVIE_GOOFS
--------------------------------------------------------

  ALTER TABLE "MOVIE_GOOFS" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_GOOFS" MODIFY ("GOOF_TYPE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "MOVIE_GOOFS" MODIFY ("GOOF_TEXT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table GOOFS_TYPES
--------------------------------------------------------

  ALTER TABLE "GOOFS_TYPES" ADD CONSTRAINT "GOOFS_TYPE_PK" PRIMARY KEY ("GOOF_TYPE_ID") ENABLE;
 
  ALTER TABLE "GOOFS_TYPES" MODIFY ("GOOF_TYPE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "GOOFS_TYPES" MODIFY ("GOOF_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PERSON_MOVIE_CREDITS
--------------------------------------------------------

  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_CHK1" CHECK (
IS_ACTOR IN ('Y','N')
) ENABLE;
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_PK" PRIMARY KEY ("PERSON_ID", "MOVIE_ID", "PRODUCTION_ROLE_ID") ENABLE;
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("PERSON_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("MOVIE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("PRODUCTION_ROLE_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" MODIFY ("IS_ACTOR" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Index COLOR_INFO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "COLOR_INFO_PK" ON "COLOR_INFO" ("COLOR_INFO_ID") 
  ;
--------------------------------------------------------
--  DDL for Index COUNTRIES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "COUNTRIES_PK" ON "COUNTRIES" ("COUNTRY_ID") 
  ;
--------------------------------------------------------
--  DDL for Index CONNECTIONS_RELATIONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONNECTIONS_RELATIONS_PK" ON "CONNECTIONS_RELATIONS" ("CONNECTION_RELATION_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PERSONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PERSONS_PK" ON "PERSONS" ("PERSON_ID") 
  ;
--------------------------------------------------------
--  DDL for Index GOOFS_TYPE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "GOOFS_TYPE_PK" ON "GOOFS_TYPES" ("GOOF_TYPE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index MOVIE_CONNECTIONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MOVIE_CONNECTIONS_PK" ON "MOVIE_CONNECTIONS" ("MOVIE_ID", "CONNECTED_MOVIE_ID", "MOVIE_CONNECTION_RELATION_ID") 
  ;
--------------------------------------------------------
--  DDL for Index LANGUAGES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LANGUAGES_PK" ON "LANGUAGES" ("LANGUAGE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PRODUCTION_ROLES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PRODUCTION_ROLES_PK" ON "PRODUCTION_ROLES" ("PRODUCTION_ROLE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index GENRES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "GENRES_PK" ON "GENRES" ("GENRE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PERSON_MOVIE_CREDITS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PERSON_MOVIE_CREDITS_PK" ON "PERSON_MOVIE_CREDITS" ("PERSON_ID", "MOVIE_ID", "PRODUCTION_ROLE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index MOVIES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MOVIES_PK" ON "MOVIES" ("MOVIE_ID") 
  ;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_AKA_NAMES
--------------------------------------------------------

  ALTER TABLE "MOVIE_AKA_NAMES" ADD CONSTRAINT "MOVIES_AKA_NAMES_COUNTRIE_FK1" FOREIGN KEY ("MOVIE_AKA_NAME_COUNTRY_ID")
	  REFERENCES "COUNTRIES" ("COUNTRY_ID") ENABLE;
 
  ALTER TABLE "MOVIE_AKA_NAMES" ADD CONSTRAINT "MOVIES_AKA_NAMES_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PERSON_MOVIE_CREDITS
--------------------------------------------------------

  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_PERSONS_FK1" FOREIGN KEY ("PERSON_ID")
	  REFERENCES "PERSONS" ("PERSON_ID") ENABLE;
 
  ALTER TABLE "PERSON_MOVIE_CREDITS" ADD CONSTRAINT "PERSON_MOVIE_CREDITS_PRODUCT_FK1" FOREIGN KEY ("PRODUCTION_ROLE_ID")
	  REFERENCES "PRODUCTION_ROLES" ("PRODUCTION_ROLE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_CONNECTIONS
--------------------------------------------------------

  ALTER TABLE "MOVIE_CONNECTIONS" ADD CONSTRAINT "MOVIE_CONNECTIONS_CONNECT_FK1" FOREIGN KEY ("MOVIE_CONNECTION_RELATION_ID")
	  REFERENCES "CONNECTIONS_RELATIONS" ("CONNECTION_RELATION_ID") ENABLE;
 
  ALTER TABLE "MOVIE_CONNECTIONS" ADD CONSTRAINT "MOVIE_CONNECTIONS_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
 
  ALTER TABLE "MOVIE_CONNECTIONS" ADD CONSTRAINT "MOVIE_CONNECTIONS_MOVIES_FK2" FOREIGN KEY ("CONNECTED_MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_COUNTRIES
--------------------------------------------------------

  ALTER TABLE "MOVIE_COUNTRIES" ADD CONSTRAINT "MOVIE_COUNTRIES_COUNTRIES_FK1" FOREIGN KEY ("COUNTRY_ID")
	  REFERENCES "COUNTRIES" ("COUNTRY_ID") ENABLE;
 
  ALTER TABLE "MOVIE_COUNTRIES" ADD CONSTRAINT "MOVIE_COUNTRIES_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_CRAZY_CREDITS
--------------------------------------------------------

  ALTER TABLE "MOVIE_CRAZY_CREDITS" ADD CONSTRAINT "MOVIE_CRAZY_CREDITS_MOVIE_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_GENRES
--------------------------------------------------------

  ALTER TABLE "MOVIE_GENRES" ADD CONSTRAINT "MOVIE_GENRES_GENRES_FK1" FOREIGN KEY ("GENRE_ID")
	  REFERENCES "GENRES" ("GENRE_ID") ENABLE;
 
  ALTER TABLE "MOVIE_GENRES" ADD CONSTRAINT "MOVIE_GENRES_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_GOOFS
--------------------------------------------------------

  ALTER TABLE "MOVIE_GOOFS" ADD CONSTRAINT "MOVIE_GOOFS_GOOFS_TYPE_FK1" FOREIGN KEY ("GOOF_TYPE_ID")
	  REFERENCES "GOOFS_TYPES" ("GOOF_TYPE_ID") ENABLE;
 
  ALTER TABLE "MOVIE_GOOFS" ADD CONSTRAINT "MOVIE_GOOFS_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_LANGUAGES
--------------------------------------------------------

  ALTER TABLE "MOVIE_LANGUAGES" ADD CONSTRAINT "MOVIE_LANGUAGES_LANGUAGES_FK1" FOREIGN KEY ("LANGUAGE_ID")
	  REFERENCES "LANGUAGES" ("LANGUAGE_ID") ENABLE;
 
  ALTER TABLE "MOVIE_LANGUAGES" ADD CONSTRAINT "MOVIE_LANGUAGES_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_QUOTES
--------------------------------------------------------

  ALTER TABLE "MOVIE_QUOTES" ADD CONSTRAINT "MOVIE_QUOTES_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIES
--------------------------------------------------------

  ALTER TABLE "MOVIES" ADD CONSTRAINT "MOVIES_COLOR_INFO_FK1" FOREIGN KEY ("MOVIE_COLOR_INFO_ID")
	  REFERENCES "COLOR_INFO" ("COLOR_INFO_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PERSON_AKA_NAMES
--------------------------------------------------------

  ALTER TABLE "PERSON_AKA_NAMES" ADD CONSTRAINT "PERSONS_AKA_NAMES_PERSONS_FK1" FOREIGN KEY ("PERSON_ID")
	  REFERENCES "PERSONS" ("PERSON_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOVIE_TRIVIA
--------------------------------------------------------

  ALTER TABLE "MOVIE_TRIVIA" ADD CONSTRAINT "MOVIE_TRIVIA_MOVIES_FK1" FOREIGN KEY ("MOVIE_ID")
	  REFERENCES "MOVIES" ("MOVIE_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PERSON_QUOTES
--------------------------------------------------------

  ALTER TABLE "PERSON_QUOTES" ADD CONSTRAINT "PERSONS_QUOTES_PERSONS_FK1" FOREIGN KEY ("PERSON_ID")
	  REFERENCES "PERSONS" ("PERSON_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PERSONS
--------------------------------------------------------

  ALTER TABLE "PERSONS" ADD CONSTRAINT "PERSONS_COUNTRIES_FK1" FOREIGN KEY ("COUNTRY_OF_BIRTH_ID")
	  REFERENCES "COUNTRIES" ("COUNTRY_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PERSON_TRIVIA
--------------------------------------------------------

  ALTER TABLE "PERSON_TRIVIA" ADD CONSTRAINT "PERSONS_TRIVIA_PERSONS_FK1" FOREIGN KEY ("PERSON_ID")
	  REFERENCES "PERSONS" ("PERSON_ID") ENABLE;
--------------------------------------------------------
--  DDL for Trigger BI_COLOR_INFO
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "BI_COLOR_INFO" 
BEFORE INSERT ON COLOR_INFO
FOR EACH ROW 
BEGIN
  SELECT 	COLOR_INFO_SEQ.NEXTVAL
		INTO		:NEW.COLOR_INFO_ID
		FROM		dual;
END;


/
ALTER TRIGGER "BI_COLOR_INFO" ENABLE;
--------------------------------------------------------
--  DDL for Trigger BI_CONNECTIONS_RELATIONS
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "BI_CONNECTIONS_RELATIONS" 
BEFORE INSERT ON CONNECTIONS_RELATIONS
FOR EACH ROW 
BEGIN
  SELECT 	CONNECTIONS_RELATIONS_SEQ.NEXTVAL
		INTO		:NEW.CONNECTION_RELATION_ID
		FROM		dual;
END;


/
ALTER TRIGGER "BI_CONNECTIONS_RELATIONS" ENABLE;
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
--  DDL for Trigger BI_GOOFS_TYPE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "BI_GOOFS_TYPE" 
BEFORE INSERT ON GOOFS_TYPES
FOR EACH ROW 
BEGIN
    SELECT  GOOFS_TYPE_SEQ.NEXTVAL
		INTO		:NEW.GOOF_TYPE_ID
		FROM		dual;
END;


/
ALTER TRIGGER "BI_GOOFS_TYPE" ENABLE;
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
--  DDL for Trigger BI_PRODUCTION_ROLES
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "BI_PRODUCTION_ROLES" 
BEFORE INSERT ON PRODUCTION_ROLES
FOR EACH ROW 
BEGIN
    SELECT  PRODUCTION_ROLES_SEQ.NEXTVAL
		INTO		:NEW.PRODUCTION_ROLE_ID
		FROM		dual;
END;


/
ALTER TRIGGER "BI_PRODUCTION_ROLES" ENABLE;