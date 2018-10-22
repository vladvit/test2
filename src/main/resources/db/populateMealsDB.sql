DELETE FROM meals;
ALTER TABLE meals AUTO_INCREMENT=200000;

INSERT INTO meals (description, date_time, calories, user_id) VALUES
  ('Breakfast User', '2018-10-20 10:23:00', 500, (SELECT id FROM users WHERE name='User' LIMIT 1)),
  ('Lunch User', '2018-10-20 14:00:00', 1500, (SELECT id FROM users WHERE name='User' LIMIT 1)),
  ('Dinner User', '2018-10-21 20:15:00', 500, (SELECT id FROM users WHERE name='User' LIMIT 1)),
  ('Breakfast Admin', '2018-10-21 10:24:00', 1000, (SELECT id FROM users WHERE name='Admin' LIMIT 1)),
  ('Lunch Admin', '2018-10-21 15:23:08', 2000, (SELECT id FROM users WHERE name='Admin' LIMIT 1)),
  ('Dinner Admin', '2018-10-22 20:58:25', 500, (SELECT id FROM users WHERE name='Admin' LIMIT 1));
