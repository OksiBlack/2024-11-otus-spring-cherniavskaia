--liquibase formatted sql

--changeset chern:2025-01-25--genres-add-data

INSERT INTO genres (name, description) VALUES ('Fantasy', 'A genre that uses magic and other supernatural elements as a primary plot element, theme, or setting.');
INSERT INTO genres (name, description) VALUES ('Fiction', 'A genre that includes imaginative narratives, novels, and stories.');

INSERT INTO genres (name, description) VALUES ('Adventure', 'A genre focusing on exciting journeys and exploits, often featuring quests and heroic struggles.');
INSERT INTO genres (name, description) VALUES ('Science Fiction', 'A genre that explores futuristic concepts, advanced science, and technological innovations.');

INSERT INTO genres (name, description) VALUES ('Classic Literature', 'Timeless literary works that have been acknowledged for their cultural, historical, and artistic significance.');
INSERT INTO genres (name, description) VALUES ('Poetry', 'A literary genre that uses aesthetic and rhythmic qualities of language to evoke meanings in addition to, or in place of, the prosaic ostensible meaning.');
INSERT INTO genres (name, description) VALUES ('Psychological Fiction', 'A genre that emphasizes the inner thoughts and emotional states of its characters.');

INSERT INTO genres (name, description) VALUES ('Non-Fiction', 'A genre based on factual information, including biographies and essays.');
INSERT INTO genres (name, description) VALUES ('Mystery', 'A genre that involves solving a crime or uncovering secrets.');
INSERT INTO genres (name, description) VALUES ('Romance', 'A genre that focuses on romantic relationships and love stories.');
INSERT INTO genres (name, description) VALUES ('Horror', 'A genre designed to evoke fear, dread, and suspense.');
INSERT INTO genres (name, description) VALUES ('Thriller', 'A genre that features exciting plots with suspenseful or high-stakes situations.');
INSERT INTO genres (name, description) VALUES ('Biography', 'A non-fiction genre that tells the life story of an individual.');
INSERT INTO genres (name, description) VALUES ('Self-Help', 'A genre aimed at personal development and improvement.');


