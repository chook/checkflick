---------------------------------------------------
--   DATA FOR TABLE COLOR_INFO
--   FILTER = none used
---------------------------------------------------
REM INSERTING into COLOR_INFO
Insert into COLOR_INFO (COLOR_INFO_ID,COLOR_INFO_TEXT) values (1,'Color');
Insert into COLOR_INFO (COLOR_INFO_ID,COLOR_INFO_TEXT) values (2,'B&W');

---------------------------------------------------
--   END DATA FOR TABLE COLOR_INFO
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE CONNECTIONS_RELATIONS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into CONNECTIONS_RELATIONS
Insert into CONNECTIONS_RELATIONS (CONNECTION_RELATION_ID,CONNECTION_RELATION_NAME) values (1,'Spoofed in');
Insert into CONNECTIONS_RELATIONS (CONNECTION_RELATION_ID,CONNECTION_RELATION_NAME) values (2,'References');
Insert into CONNECTIONS_RELATIONS (CONNECTION_RELATION_ID,CONNECTION_RELATION_NAME) values (3,'Follows');
Insert into CONNECTIONS_RELATIONS (CONNECTION_RELATION_ID,CONNECTION_RELATION_NAME) values (4,'Followed By');

---------------------------------------------------
--   END DATA FOR TABLE CONNECTIONS_RELATIONS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE COUNTRIES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into COUNTRIES
Insert into COUNTRIES (COUNTRY_ID,COUNTRY_NAME) values (1,'Israel');
Insert into COUNTRIES (COUNTRY_ID,COUNTRY_NAME) values (2,'USA');
Insert into COUNTRIES (COUNTRY_ID,COUNTRY_NAME) values (3,'India');
Insert into COUNTRIES (COUNTRY_ID,COUNTRY_NAME) values (4,'Japan');

---------------------------------------------------
--   END DATA FOR TABLE COUNTRIES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE GENRES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into GENRES
Insert into GENRES (GENRE_ID,GENRE_NAME) values (1,'Action');
Insert into GENRES (GENRE_ID,GENRE_NAME) values (2,'Comedy');
Insert into GENRES (GENRE_ID,GENRE_NAME) values (3,'Drama');
Insert into GENRES (GENRE_ID,GENRE_NAME) values (4,'Sci-Fi');

---------------------------------------------------
--   END DATA FOR TABLE GENRES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE GOOFS_TYPES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into GOOFS_TYPES
Insert into GOOFS_TYPES (GOOF_TYPE_ID,GOOF_NAME) values (1,'Continuity');
Insert into GOOFS_TYPES (GOOF_TYPE_ID,GOOF_NAME) values (2,'Factual Error');
Insert into GOOFS_TYPES (GOOF_TYPE_ID,GOOF_NAME) values (3,'Visible Boom Mic');

---------------------------------------------------
--   END DATA FOR TABLE GOOFS_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE LANGUAGES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into LANGUAGES
Insert into LANGUAGES (LANGUAGE_ID,LANGUAGE_NAME) values (1,'English');
Insert into LANGUAGES (LANGUAGE_ID,LANGUAGE_NAME) values (2,'Hebrew');
Insert into LANGUAGES (LANGUAGE_ID,LANGUAGE_NAME) values (3,'Japanese Sign Language (Ya Ta Hand Gestures)');
Insert into LANGUAGES (LANGUAGE_ID,LANGUAGE_NAME) values (4,'Ancient Syrian Aramaic');

---------------------------------------------------
--   END DATA FOR TABLE LANGUAGES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PRODUCTION_ROLES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PRODUCTION_ROLES
Insert into PRODUCTION_ROLES (PRODUCTION_ROLE_ID,PRODUCTION_ROLE_NAME) values (1,'Actors');
Insert into PRODUCTION_ROLES (PRODUCTION_ROLE_ID,PRODUCTION_ROLE_NAME) values (2,'Writers');
Insert into PRODUCTION_ROLES (PRODUCTION_ROLE_ID,PRODUCTION_ROLE_NAME) values (3,'Producers');
Insert into PRODUCTION_ROLES (PRODUCTION_ROLE_ID,PRODUCTION_ROLE_NAME) values (4,'Directors');

---------------------------------------------------
--   END DATA FOR TABLE PRODUCTION_ROLES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIES
Insert into MOVIES (MOVIE_ID,MOVIE_NAME,MOVIE_YEAR,MOVIE_ROMAN_NOTATION,MOVIE_MADE_FOR,MOVIE_COLOR_INFO_ID,MOVIE_RUNNING_TIME,MOVIE_TAGLINE,MOVIE_PLOT_TEXT,MOVIE_FILMING_LOCATION_NAME) values (1,'Iron Man 2',2010,'I',null,1,null,'Because you always need a sequal','Boy meets girl.
Boy loves girl.
Boy accidently spills toxic waste on girl and oblitirate her.','Morroco');
Insert into MOVIES (MOVIE_ID,MOVIE_NAME,MOVIE_YEAR,MOVIE_ROMAN_NOTATION,MOVIE_MADE_FOR,MOVIE_COLOR_INFO_ID,MOVIE_RUNNING_TIME,MOVIE_TAGLINE,MOVIE_PLOT_TEXT,MOVIE_FILMING_LOCATION_NAME) values (2,'Iron Man 3',2012,null,'TV',null,156,'Robert Dawny Jr. was free, so we did another one.',null,null);
Insert into MOVIES (MOVIE_ID,MOVIE_NAME,MOVIE_YEAR,MOVIE_ROMAN_NOTATION,MOVIE_MADE_FOR,MOVIE_COLOR_INFO_ID,MOVIE_RUNNING_TIME,MOVIE_TAGLINE,MOVIE_PLOT_TEXT,MOVIE_FILMING_LOCATION_NAME) values (3,'Chen Harryel Potter and The Visualization Stone',2009,'XI','VG',2,null,'One man, many APIs/X\This is another tagline, and it''s seperated from the first with this interesting X inside two slashes.',null,null);

---------------------------------------------------
--   END DATA FOR TABLE MOVIES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PERSONS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PERSONS
Insert into PERSONS (PERSON_ID,PERSON_NAME,REAL_NAME,NICKNAMES,DATE_OF_BIRTH,YEAR_OF_BIRTH,CITY_OF_BIRTH,COUNTRY_OF_BIRTH_ID,DATE_OF_DEATH,YEAR_OF_DEATH,HEIGHT,TRADEMARK,BIOGRAPHY_TEXT) values (1,'Chen Harel','Chen Harelovich','Chook/X\The Architect/X\The Merger Founder',to_timestamp('29-ιεπι   -1985 00:00:00.000000000','DD-MON-RRRR HH24:MI:SS.FF'),null,'Ramat Gan',1,null,null,180,'Points his hands as if they were pistols, and does a firing gesture while saying "choo! choo-choo choo!"','Chen is nice.');
Insert into PERSONS (PERSON_ID,PERSON_NAME,REAL_NAME,NICKNAMES,DATE_OF_BIRTH,YEAR_OF_BIRTH,CITY_OF_BIRTH,COUNTRY_OF_BIRTH_ID,DATE_OF_DEATH,YEAR_OF_DEATH,HEIGHT,TRADEMARK,BIOGRAPHY_TEXT) values (2,'Peter Petrelli',null,'The One',null,null,null,null,null,null,null,null,null);
Insert into PERSONS (PERSON_ID,PERSON_NAME,REAL_NAME,NICKNAMES,DATE_OF_BIRTH,YEAR_OF_BIRTH,CITY_OF_BIRTH,COUNTRY_OF_BIRTH_ID,DATE_OF_DEATH,YEAR_OF_DEATH,HEIGHT,TRADEMARK,BIOGRAPHY_TEXT) values (3,'Haim Banay',null,null,null,null,null,null,null,2008,null,null,'One of the Banay clan.');

---------------------------------------------------
--   END DATA FOR TABLE PERSONS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PERSON_MOVIE_CREDITS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PERSON_MOVIE_CREDITS
Insert into PERSON_MOVIE_CREDITS (PERSON_ID,MOVIE_ID,PRODUCTION_ROLE_ID,IS_ACTOR,ACTOR_ROLE,ACTOR_CREDITS_RANK) values (1,3,1,'Y','Snape',1);
Insert into PERSON_MOVIE_CREDITS (PERSON_ID,MOVIE_ID,PRODUCTION_ROLE_ID,IS_ACTOR,ACTOR_ROLE,ACTOR_CREDITS_RANK) values (3,1,3,'N',null,null);
Insert into PERSON_MOVIE_CREDITS (PERSON_ID,MOVIE_ID,PRODUCTION_ROLE_ID,IS_ACTOR,ACTOR_ROLE,ACTOR_CREDITS_RANK) values (3,2,1,'Y','3',1);
Insert into PERSON_MOVIE_CREDITS (PERSON_ID,MOVIE_ID,PRODUCTION_ROLE_ID,IS_ACTOR,ACTOR_ROLE,ACTOR_CREDITS_RANK) values (3,2,3,'N',null,null);

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_APPEARANCES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_CONNECTIONS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_CONNECTIONS
Insert into MOVIE_CONNECTIONS (MOVIE_ID,CONNECTED_MOVIE_ID,MOVIE_CONNECTION_RELATION_ID) values (1,2,4);
Insert into MOVIE_CONNECTIONS (MOVIE_ID,CONNECTED_MOVIE_ID,MOVIE_CONNECTION_RELATION_ID) values (2,1,3);
Insert into MOVIE_CONNECTIONS (MOVIE_ID,CONNECTED_MOVIE_ID,MOVIE_CONNECTION_RELATION_ID) values (2,3,1);

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_CONNECTIONS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_COUNTRIES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_COUNTRIES
Insert into MOVIE_COUNTRIES (MOVIE_ID,COUNTRY_ID) values (1,2);
Insert into MOVIE_COUNTRIES (MOVIE_ID,COUNTRY_ID) values (2,3);
Insert into MOVIE_COUNTRIES (MOVIE_ID,COUNTRY_ID) values (2,2);
Insert into MOVIE_COUNTRIES (MOVIE_ID,COUNTRY_ID) values (3,1);

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_COUNTRIES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_CRAZY_CREDITS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_CRAZY_CREDITS
Insert into MOVIE_CRAZY_CREDITS (MOVIE_ID,CRAZY_CREDIT_TEXT) values (3,'After the credits end, you can see Chen Harryel Potter''s hands on the right side of the screen, and then you can hear the famous "choo! choo-choo choo!"');
Insert into MOVIE_CRAZY_CREDITS (MOVIE_ID,CRAZY_CREDIT_TEXT) values (3,'Before the end of the credits, the credits read:
"No Friedmans were harmed during the production of this motion picture"');

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_CRAZY_CREDITS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_GENRES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_GENRES
Insert into MOVIE_GENRES (MOVIE_ID,GENRE_ID) values (2,4);
Insert into MOVIE_GENRES (MOVIE_ID,GENRE_ID) values (2,1);
Insert into MOVIE_GENRES (MOVIE_ID,GENRE_ID) values (3,1);
Insert into MOVIE_GENRES (MOVIE_ID,GENRE_ID) values (3,2);
Insert into MOVIE_GENRES (MOVIE_ID,GENRE_ID) values (3,3);

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_GENRES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_GOOFS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_GOOFS
Insert into MOVIE_GOOFS (MOVIE_ID,GOOF_TYPE_ID,GOOF_TEXT) values (3,1,'In the last semester, Chen Harryel Potter says he won''t come to Tapi classes, but the fifth week''s lesson,  we can clearly see him in the background.');
Insert into MOVIE_GOOFS (MOVIE_ID,GOOF_TYPE_ID,GOOF_TEXT) values (3,2,'Chen Harryel Potter states his average grade at the end of the degree would be 80. In reality, Chen graduated with 65.');
Insert into MOVIE_GOOFS (MOVIE_ID,GOOF_TYPE_ID,GOOF_TEXT) values (3,2,'Every morning, Chen Harryel Potter says he wakes up at 6:30 to drive his girlfriend Shachar to her base, and then comes to the university.
In fact, during rush hour, the average time to get from Rehovot to Glilot and back to Ramat Aviv is about 55 minutes,
which means Chen should have been in the university by 8 o''clock and finish all of our projects.');

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_GOOFS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_LANGUAGES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_LANGUAGES
Insert into MOVIE_LANGUAGES (MOVIE_ID,LANGUAGE_ID) values (1,1);
Insert into MOVIE_LANGUAGES (MOVIE_ID,LANGUAGE_ID) values (2,1);
Insert into MOVIE_LANGUAGES (MOVIE_ID,LANGUAGE_ID) values (3,2);
Insert into MOVIE_LANGUAGES (MOVIE_ID,LANGUAGE_ID) values (3,4);
Insert into MOVIE_LANGUAGES (MOVIE_ID,LANGUAGE_ID) values (3,3);

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_LANGUAGES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_QUOTES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_QUOTES
Insert into MOVIE_QUOTES (MOVIE_ID,QUOTE_TEXT) values (3,'Chen: What do you think of Eyal from Shitot Mechkar?
Nadav: He is the devil!');
Insert into MOVIE_QUOTES (MOVIE_ID,QUOTE_TEXT) values (3,'Chen: We''ll just write down Sheker Kolshehu.');

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_QUOTES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_AKA_NAMES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_AKA_NAMES
Insert into MOVIE_AKA_NAMES (MOVIE_ID,MOVIE_AKA_NAME,MOVIE_AKA_NAME_YEAR,MOVIE_AKA_NAME_COUNTRY_ID) values (1,'Iron Man Reloaded',2010,3);
Insert into MOVIE_AKA_NAMES (MOVIE_ID,MOVIE_AKA_NAME,MOVIE_AKA_NAME_YEAR,MOVIE_AKA_NAME_COUNTRY_ID) values (3,'Iron Man Revolutions',2012,4);

---------------------------------------------------
--   END DATA FOR TABLE MOVIES_AKA_NAMES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE MOVIE_TRIVIA
--   FILTER = none used
---------------------------------------------------
REM INSERTING into MOVIE_TRIVIA
Insert into MOVIE_TRIVIA (MOVIE_ID,TRIVIA_TEXT) values (3,'The movie was supposed to be released on 2009, but production lasted another semester (because of the tragic death of the production assistant Modela Hishuvia).');
Insert into MOVIE_TRIVIA (MOVIE_ID,TRIVIA_TEXT) values (3,'In order to acheive the effect of good students, extras were used in the movie during the parts where the actual actors had to ask the professor questions.');
Insert into MOVIE_TRIVIA (MOVIE_ID,TRIVIA_TEXT) values (3,'All the iPhones in the movie were made of cardboard, since so many of them were used and it was heavy on cost.');
Insert into MOVIE_TRIVIA (MOVIE_ID,TRIVIA_TEXT) values (3,'A special studio was built to simulate Nezer''s natural habitat.
Soundmen were sent to the forest to record the original voices and calls and those were integrated into the film.');
Insert into MOVIE_TRIVIA (MOVIE_ID,TRIVIA_TEXT) values (3,'Gilman has its own national anthem and flag.');

---------------------------------------------------
--   END DATA FOR TABLE MOVIE_TRIVIA
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PERSON_AKA_NAMES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PERSON_AKA_NAMES
Insert into PERSON_AKA_NAMES (PERSON_ID,PERSON_AKA_NAME) values (1,'Chook');
Insert into PERSON_AKA_NAMES (PERSON_ID,PERSON_AKA_NAME) values (3,'Albert Perot');

---------------------------------------------------
--   END DATA FOR TABLE PERSONS_AKA_NAMES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PERSON_QUOTES
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PERSON_QUOTES
Insert into PERSON_QUOTES (PERSON_ID,QUOTE_TEXT) values (1,'Sheker Kolshehu');
Insert into PERSON_QUOTES (PERSON_ID,QUOTE_TEXT) values (2,'Save the cheerleader, Save the world!');

---------------------------------------------------
--   END DATA FOR TABLE PERSONS_QUOTES
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE PERSON_TRIVIA
--   FILTER = none used
---------------------------------------------------
REM INSERTING into PERSON_TRIVIA
Insert into PERSON_TRIVIA (PERSON_ID,TRIVIA_TEXT) values (1,'Chen has a lightning bolt symbol on his forehead. Chen also played in the movie (/movie:43)');

---------------------------------------------------
--   END DATA FOR TABLE PERSONS_TRIVIA
---------------------------------------------------
