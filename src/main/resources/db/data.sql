INSERT INTO wizard (name) VALUES
('Albus Dumbledore'),
('Harry Potter'),
('Neville Longbottom');

INSERT INTO artifact (name, description, image_url, wizard_id) VALUES
('Deluminator', 'A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.', 'ImageUrl', 1),
('Invisibility Cloak', 'An invisibility cloak is used to make the wearer invisible.', 'ImageUrl', 2),
('Elder Wand', 'The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.', 'ImageUrl', 1),
('The Marauder''s Map', 'A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.', 'ImageUrl', 2),
('The Sword Of Gryffindor', 'A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.', 'ImageUrl', 3),
('Resurrection Stone', 'The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.', 'ImageUrl', NULL);

INSERT INTO hogwards_user(username, password, enabled, roles) VALUES
('john', '123456', true, 'admin user'),
('eric', '654321', true, 'user'),
('tom', 'qwerty', false, 'user');