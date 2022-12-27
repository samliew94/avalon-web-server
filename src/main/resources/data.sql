INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'MERL', 'Merlin', 0, 1, 1, 0, 'Sees all Evil.Must stay hidden', 'Evils:', '#0073cf', '#FF1C1C', 'ra-crystal-ball'); --1
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'ASSA', 'Assassin', 1, 1, 1, 0,'Seek and Slay Merlin', 'Allies:', '#FF1C1C', '#FF1C1C', 'ra-daggers'); --2
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'PERC', 'Percival', 0, 1, 1, 0,'Sees Merlin and Morgana', 'Merlin or Morgana:', '#0073cf', '#808080', 'ra-eye-shield'); --3
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'MORG', 'Morgana', 1, 1, 1, 0,'Appears to Percival as Merlin', 'Allies:', '#FF1C1C', '#FF1C1C', 'ra-raven'); --4
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'GENG', 'Loyal Servant of Arthur',0, 0, 1,0, 'Uncover Evil and Protect Merlin', null, '#0073cf', null, 'ra-knight-helmet'); --5
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'GENE', 'Minions of Mordred',1, 0, 1, 0,'Deceive Arthur-s Followers', 'Allies:', '#FF1C1C', '#FF1C1C', 'ra-helmet'); --6
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'OBER', 'Oberon', 1, 1, 0, 1,'Invisible to Evils', null, '#FF1C1C', null, 'ra-horns'); --7
INSERT INTO game_roles (role_code, role_title, loyalty, is_power, is_active, is_editable, role_description, other_players_title, role_title_color, other_players_name_color, role_icon) VALUES ( 'MORD', 'Mordred', 1, 1, 0, 1, 'Invisible to Merlin',  'Allies:', '#FF1C1C', '#FF1C1C', 'ra-flaming-claw'); --8

INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (1, 2); -- merlin sees assassin
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (1, 4); -- merlin sees morgana
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (1, 6); -- merlin sees minions geneirc evil
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (1, 7); -- merlin sees oberon
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (2, 4); -- assassin sees morgana
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (2, 6); -- assassin sees generic evil
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (2, 8); -- assassin sees mordred
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (3, 1); -- percival sees merlin
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (3, 4); -- percival sees morgana
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (4, 2); -- morgana sees assassin
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (4, 6); -- morgana sees generic evil
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (4, 8); -- morgana sees mordred
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (6, 2); -- generic evil sees assasin
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (6, 4); -- generic evil sees morgana
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (6, 6); -- generic evil sees other generic evils
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (6, 8); -- generic evil sees mordred
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (8, 2); -- mordred sees assassin
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (8, 4); -- mordred sees morgana
INSERT INTO game_roles_visibility(game_roles_id, can_see_game_roles_id) VALUES (8, 6); -- mordred sees generic evil

--INSERT INTO users (username, game_roles_id, is_host) VALUES ('sam', null, true);
--INSERT INTO users (username, game_roles_id) VALUES ('james', null);
--INSERT INTO users (username, game_roles_id) VALUES ('wong', null);
--INSERT INTO users (username, game_roles_id) VALUES ('benedict', null);
--INSERT INTO users (username, game_roles_id) VALUES ('stsange', null);
--INSERT INTO users (username, game_roles_id) VALUES ('chris', null);
--INSERT INTO users (username, game_roles_id) VALUES ('evans', null);
--INSERT INTO users (username, game_roles_id) VALUES ('steve', null);
--INSERT INTO users (username, game_roles_id) VALUES ('rogers', null);
--INSERT INTO users (username, game_roles_id) VALUES ('tony', null);

INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (5,3,2);
INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (6,4,2);
INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (7,4,3);
INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (8,5,3);
INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (9,6,3);
INSERT INTO ratio_rules(total_players, total_good, total_evil) VALUES (10,6,4);
