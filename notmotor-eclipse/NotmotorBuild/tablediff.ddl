-- Ändringar/tillägg efter Release 2007-11-19.b75 --

-- Lägg till attributet MIMETYP i MEDDELANDE
ALTER TABLE NOTMOTOR.MEDDELANDE
	ADD MIMETYP VARCHAR(100);
