-- =================== Datos aplicacion base =================== --

-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');


-- Tipos de Ganado
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Porcino'); 	-- 1
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Ovino'); 	-- 2
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Vacuno'); 	-- 3
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Caprino'); 	-- 4
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Equino'); 	-- 5
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Asnal'); 	-- 6
INSERT INTO tipos_ganado(tipo_ganado) VALUES ('Avicola'); 	-- 7

-- =================== Indice =================== --

-- 1. Ganaderos
-- 2. Explotaciones Ganaderas
-- 3. Veterinarios y Contratos
-- 4. Lotes y Animales
-- 5. Animales históricos
-- 6. Citas
-- 7. Productos
-- 8. Recetas
-- 9. Facturas


-- ========================== Datos de prueba ========================== --
	-- 8 Ganaderos, 6 Veterinarios, 9 explotaciones --

-- 1. Ganaderos -----------------------------------------------------------------------

	-- Ganadero1 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero1','ganadero1',TRUE);
	INSERT INTO authorities VALUES ('ganadero1','ganadero');
	
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Candido','Medina Sandoval','754534011','candidoMedina@mail.com','Sevilla','Carmona','52874678D','Rio Segura, 93','31320','ganadero1');
	
	-- Ganadero2 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero2','ganadero2',TRUE);
	INSERT INTO authorities VALUES ('ganadero2','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Perpetuo','Melgar Rios','759697702','perpetuoMelgarRios@mail.com','Badajoz','Plasencia',
		'32874675T','Avda. de Andalucia, 24','29327','ganadero2');

	-- Ganadero3 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero3','ganadero3',TRUE);
	INSERT INTO authorities VALUES ('ganadero3','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Jocelin','Guajardo Briseno','689233408','jocelin@mail.com','Sevilla','Montellano','12874378O','Castilla, 12','50793','ganadero3');
	
	-- Ganadero4 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero4','ganadero4',TRUE);
	INSERT INTO authorities VALUES ('ganadero4','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Freya','Arredondo Zaragoza','714184104','freya@mail.com','Sevilla','El Pedroso','25674678K','Urzaiz, 26','50793','ganadero4');
	
	-- Ganadero5 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero5','ganadero5',TRUE);
	INSERT INTO authorities VALUES ('ganadero5','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Joad','Banda Segura','672440517','joad@mail.com','Sevilla','La Puebla de Cazalla','81974671M',
		'Reina Mercedes, 26','32840','ganadero5');
	
	-- Ganadero6 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero6','ganadero6',TRUE);
	INSERT INTO authorities VALUES ('ganadero6','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Clotilde','Corral Lopez','604067604','clotilde@mail.com','Caceres','Acebo','90174670P','Calle San Pedro, 11','37798','ganadero6');
	
	-- Ganadero7 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero7','ganadero7',TRUE);
	INSERT INTO authorities VALUES ('ganadero7','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Julio','Pineda Ceja','623258060','julio@mail.com','Caceres','Montehermoso','01014675N','Manuel Iradier, 92','03815','ganadero7');
	
	-- Ganadero8 ----------------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('ganadero8','ganadero8',TRUE);
	INSERT INTO authorities VALUES ('ganadero8','ganadero');
	INSERT INTO Ganadero(first_name,last_name,telephone,mail,province,city,dni,address,postal_code,username) 
		VALUES ('Lucero','Leyra Merino','722211986','lucero@mail.com','Caceres','Acebo','32174672W','Hijuela de Lojo, 90','37798','ganadero8');
	
-- 2. Explotaciones Ganaderas ----------------------------------------------------------------------
	
	-- Explotacion1 - Ganadero1 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Finca La Cueva','53728343','Carmona',false,1);
	
	-- Explotacion2 - Ganadero1 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('La Tablada','56728942','Carmona',false,1);
	
	-- Explotacion3 - Ganadero1 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Paraje El Moncayo','89012380','Gelves',false,1);

	-- Explotacion4 - Ganadero2 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Alaior','12389256','Plasencia',false,2);
	
	-- Explotacion5 ARCHIVADA - Ganadero2 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('El Ortigal','09837489',' Montehermoso',true,2);
	
	-- Explotacion6 - Ganadero3 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Las Rosas','78987982',' Montellano',false,3);
	
	-- Explotacion7 - Ganadero4 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Finca La Fortaleza ORIGINAL','08722721',' El Pedroso',false,4);
	
	-- Explotacion8 - Ganadero5 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('Finca La Fortaleza','98130983',' La Puebla de Cazalla',false,5);
	
	-- Explotacion9 ARCHIVADA - Ganadero6 -----------------------------------------------------------------
	INSERT INTO Explotacion_ganadera(name,numero_registro,termino_municipal,es_archivado,ganadero_id) 
	VALUES ('La granjita de Clotilde','32132102','Acebo',true,6);
	
-- 3. Veterinarios y Contratos ------------------------------------------------------------------------
	
	-- Veterinario1 - Especialidades: Porcino, Ovino, Vacuno, Caprino----------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario1','veterinario1',TRUE);
	INSERT INTO authorities VALUES ('veterinario1','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Emilio','Sevillano','345782938','emilio@gmail.com','Sevilla','Sevilla','45556952J',true,'veterinario1');
	
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (1,1); -- Porcino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (1,2); -- Ovino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (1,3); -- Vacuno
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (1,4); -- Caprino
	
		-- Contratos Veterinario1-----------------------------------------------------
			-- Contrato1 con Explotacion1 - Ganadero1 (No Vigente)--------------------
			INSERT INTO contrato(estado, fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(0, '2010-01-01','2010-01-25', '2015-01-25',1,1);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(1,1); -- Porcino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(1,2); -- Ovino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(1,3); -- Vacuno
			
			-- Contrato2 con Explotacion1 - Ganadero1 --------------------
			INSERT INTO contrato(estado, fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(0, '2015-01-26','2015-01-26', '2022-01-26',1,1);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(2,1); -- Porcino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(2,2); -- Ovino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(2,3); -- Vacuno
			
			-- Contrato3 Explotacion4 - Ganadero2 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(0, '2020-01-10','2020-01-15', '2022-01-26',4,1);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(3,4); -- Caprino
			
			-- Contrato4 (Pendiente) Explotacion6 - Ganadero3 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(2,'2020-01-10','2020-01-15', '2022-01-26',6,1);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(4,4); -- Caprino
			
		
	-- Veterinario2 - Especialidades: Porcino, Ovino, Vacuno----------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario2','veterinario2',TRUE);
	INSERT INTO authorities VALUES ('veterinario2','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Ligio','Villalpando Saenz','773015547','ligio@gmail.com','Sevilla','Sevilla','25625652A',true,'veterinario2');
		
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (2,1); -- Porcino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (2,2); -- Ovino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (2,3); -- Vacuno
	
	-- Contratos Veterinario2-----------------------------------------------------
			-- Contrato5 con Explotacion1 - Ganadero1 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(0,'2020-01-10','2020-01-15', '2025-01-26',4,2);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(5,1); -- Porcino

		
	-- Veterinario3 - Especialidades: Caprino, Equino, Asnal----------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario3','veterinario3',TRUE);
	INSERT INTO authorities VALUES ('veterinario3','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Ernesto','Conejero Rudo','900015547','ernesto@gmail.com','Caceres','Caceres','19026652Q',true,'veterinario3');
		
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (3,4); -- Caprino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (3,5); -- Equino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (3,6); -- Asnal

	-- Contratos Veterinario3-----------------------------------------------------
			-- Contrato6 con Explotacion1 - Ganadero1 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(1,'2020-01-10','2020-01-15', '2025-01-26',1,3);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(6,5); -- Equino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(6,6); -- Asnal
	
	-- Veterinario4 - Especialidades: Ovino, Caprino, Avicola ----------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario4','veterinario4',TRUE);
	INSERT INTO authorities VALUES ('veterinario4','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Ales','Moreno Molina','912315547','ales@gmail.com','Caceres','Caceres','82651236L',true,'veterinario4');
	
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (4,2); -- Ovino	
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (4,4); -- Caprino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (4,7); -- Avicola

	-- Contrato7 con Explotacion1 - Ganadero1 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(1,'2020-01-10','2020-01-15', '2025-01-26',1,4);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(7,2); -- Ovino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(7,4); -- Caprino
	-- Veterinario5 - Especialidades: Porcino, Ovino, Vacuno, Caprino, Equino, Asnal, Avicola ----------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario5','veterinario5',TRUE);
	INSERT INTO authorities VALUES ('veterinario5','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Niobe','Molina Solano','696066974','niobe@gmail.com','Badajoz','Badajoz','12345678Z',true,'veterinario5');
		
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,1); -- Porcino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,2); -- Ovino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,3); -- Vacuno
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,4); -- Caprino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,5); -- Equino
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,6); -- Asnal
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (5,7); -- Avicola
	
	-- Contrato8 con Explotacion1 - Ganadero1 --------------------
			INSERT INTO contrato(estado,fecha_peticion, fecha_inicial, fecha_final, explotacion_ganadera_id, veterinario_id)
				VALUES(2,'2020-01-10','2020-01-15', '2025-01-26',1,5);
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(6,5); -- Equino
			INSERT INTO contrato_ganados(contrato_id, ganados_id) VALUES(6,6); -- Asnal
	
	
		
	-- Veterinario6 (No Disponible) - Especialidades: Avicola -----------------------------------------------------------------
	INSERT INTO users(username,password,enabled) VALUES ('veterinario6','veterinario6',TRUE);
	INSERT INTO authorities VALUES ('veterinario6','veterinario');
	INSERT INTO Veterinario(first_name, last_name, telephone, mail, province, city, dni, es_disponible, username) 
		VALUES ('Blanca','Serrano Pineda','621621624','blanca@gmail.com','Caceres','Torrejoncillo','87654321B',false,'veterinario6');
		
	INSERT INTO VETERINARIO_TIPOS_GANADO(veterinario_id, tipos_ganado_id) VALUES (6,7); -- Avicola
	
-- 4. Lotes y Animales ----------------------------------------------------------------------

	-- Explotacion1 - Ganadero1 ----------------------------------------------------------
		-- Lote 1 - Porcino
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L019273862',false,1,1);
			-- Animales (Lote 1)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019273862-001','2020-04-01','2020-04-03', 1, false, 1,1,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019273862-002','2020-04-01','2020-04-03', 1, false, 1,1,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019273862-003','2020-04-01','2020-04-03', 0, false, 1,1,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019273862-004','2020-04-01','2020-04-03', 0, false, 1,1,1);		
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019273862-005','2020-04-01','2020-04-03', 0, false, 1,1,1);
		-- Lote 2 - Porcino
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L080972043',false,1,1);
			-- Animales (Lote 2)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L080972043-001','2015-04-01','2015-04-15', 0, false, 1,2,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L080972043-002','2015-04-01','2015-04-15', 0, false, 1,2,1);
			-- Animales ARCHIVADOS(Lote 2) 
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L080972043-003','2015-04-01','2015-04-15', 1, true, 1,2,1); -- Animal 8
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L080972043-004','2015-04-01','2015-04-15', 1, true, 1,2,1); -- Animal 9		
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L080972043-005','2015-04-01','2015-04-15', 1, true, 1,2,1); -- Animal 10
				-- Historicos (Lote 2)
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2018-10-20',null,'Se conviertio en un jamon serrano muy bueno.',8);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2017-05-07',null,'Se hizo matanza con el en las fiestas del pueblo.',9);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2016-08-08',null,'Tuvo que ser sacrificado por enfermedad.',10);
		-- Lote 3 ARCHIVADO - Porcino
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L547926372',true ,1,1);
			-- Animales ARCHIVADOS (Lote 2) 
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L547926372-001','2018-07-01','2018-07-18', 1, true, 1,3,1); -- Animal 11
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L547926372-002','2018-07-01','2018-07-18', 1, true, 1,3,1); -- Animal 12		
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L547926372-003','2018-07-01','2018-07-18', 1, true, 1,3,1); -- Animal 13
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L547926372-004','2018-07-01','2018-07-18', 1, true, 1,3,1); -- Animal 14		
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L547926372-005','2018-07-01','2018-07-18', 1, true, 1,3,1); -- Animal 15
				-- Historicos (Lote 2)
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Vendidos a HACENDADO para prodcutos carnicos',11);	
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Vendidos a HACENDADO para prodcutos carnicos',12);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Vendidos a HACENDADO para prodcutos carnicos',13);					
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Vendidos a HACENDADO para prodcutos carnicos',14);	
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Vendidos a HACENDADO para prodcutos carnicos',15);
		-- Animales (Porcino)
			-- Animal Porcino 16 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09809',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 0, false, 1,1);	
			-- Animal Porcino 17 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia,fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09810',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 0, false, 1,1);	
			-- Animal Porcino 18 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia,fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09811',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 0, false, 1,1);	
			-- Animal Porcino 19
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09812','2017-06-23','2017-06-23', 0, false, 1,1);	
			-- Animal Porcino 20
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09813','2017-06-23','2017-06-23', 0, false, 1,1);
		-- Animales ARCHIVADOS (Porcino)
			-- Animal Porcino 21 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09814',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 0, true, 1,1);	
			-- Animal Porcino 22 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia,fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09815',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 1, true, 1,1);	
			-- Animal Porcino 23 (Comprado)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia,fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09816',true,'2014-10-23','Explotacion La Tablada','2014-06-23','2014-06-23', 1, true, 1,1);	
			-- Animal Porcino 24
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09817','2017-06-23','2017-06-23', 1, true, 1,1);	
			-- Animal Porcino 25
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('POR09818','2017-06-23','2017-06-23', 1, true, 1,1);
				-- Historicos individuales (Porcino) Explotacion1
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Hubo que sacrificarla por su avanzada edad.',21);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Venta a la Finca La Fortaleza, en La Puebla de Cazalla',22);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2019-02-10','Venta a la Finca La Fortaleza, en La Puebla de Cazalla',23);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-03-12',null,'Se hizo matanza con el especimen',24);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-01-28','Regalo para la explotacion de al lado: la Tablada, aqui en Carmona',25);

		-- Lote 4 - Ovino
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L123919085',false,2,1);
			-- Animales (Lote 4)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L123919085-001','2020-03-15','2020-04-03', 0, false, 2,4,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L123919085-002','2020-03-15','2020-04-03', 0, false, 2,4,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L123919085-003','2020-03-15','2020-04-03', 0, false, 2,4,1);
		-- Lote 5 - Vacuno
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L019819884',false,3,1);
			-- Animales (Lote 5)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-001','2020-03-01','2020-04-03', 0, false, 3,5,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-002','2020-03-01','2020-04-03', 0, false, 3,5,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-003','2020-03-01','2020-04-03', 0, false, 3,5,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-004','2020-03-01','2020-04-03', 0, false, 3,5,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-005','2020-03-01','2020-04-03', 1, false, 3,5,1);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L019819884-006','2020-03-01','2020-04-03', 1, false, 3,5,1);
		-- Lote 6 ARCHIVADO - Ovino (Comprado)
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L409898085',true ,2,1);
			-- Animales (Lote 6)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-001',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 0, true, 2,6,1); -- Animal 35
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-002',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 0, true, 2,6,1); -- Animal 36
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-003',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 0, true, 2,6,1); -- Animal 37
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-004',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 1, true, 2,6,1); -- Animal 38
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-005',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 1, true, 2,6,1); -- Animal 39
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L409898085-005',true,'2010-10-23','Explotacion La Tablada','2010-10-10','2010-10-12', 1, true, 2,6,1); -- Animal 40
				-- Historicos (Lote 6)
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',35);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',36);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',37);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',38);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',39);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerte por la epidemia del virus COVID-19 GOAT edition',40);
		-- Animales (Equino)
			-- Animal Equino 41
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ09808','2017-05-24','2017-10-15', 1, false, 5,1);
			-- Animal Equino 42
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ09809',true,'2018-05-24','Explotacion La Tablada','2018-05-24','2018-05-15', 0, false, 5,1);
			-- Animal Equino 43
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ09810',true,'2018-05-24','Explotacion La Tablada','2018-05-24','2018-05-15', 0, false, 5,1);
			-- Animal Equino 44
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ09810',true,'2018-05-24','Explotacion La Tablada','2018-05-24','2018-05-15', 0, false, 5,1);
		-- Animales (Asnal)
			-- Animal Asnal 45
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09209872','2018-05-24','2018-10-15', 1, false, 6,1);
			-- Animal Asnal 46
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09209873','2018-05-24','2018-10-15', 1, false, 6,1);
			-- Animal Asnal 47
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09209889','2018-05-24','2018-10-15', 0, false, 6,1);

	-- Explotacion2 - Ganadero1 ----------------------------------------------------------
			-- Animales (Equino)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0920987235','2018-05-24','2018-10-15', 1, false, 5,2); -- Animal 48
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0989983021','2018-05-24','2018-10-15', 1, false, 5,2); -- Animal 49
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0938477831','2018-06-12','2018-10-15', 0, false, 5,2); -- Animal 50	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0938477892','2018-06-12','2018-10-15', 0, false, 5,2); -- Animal 51
			
			-- Animales ARCHIVADOS (Equino)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0938472344','2018-06-12','2018-10-15', 1, true, 5,2); -- Animal 52
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0938234234','2018-06-12','2018-10-15', 1, true, 5,2); -- Animal 53
				-- Historicos individuales (Equino) Explotacion2
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerto en una pelea con otro caballo.',52);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES('2020-02-10',null,'Muerto en una pelea con el otro caballo en cuestion.',53);					
			
			-- Animales (Asnal)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09209871','2018-08-24','2018-10-15', 1, false, 6,2); -- Animal 54
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09200392','2018-08-24','2018-10-15', 1, false, 6,2); -- Animal 55
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('AS09291827','2018-08-24','2018-10-15', 1, false, 6,2); -- Animal 56

	-- Explotacion3 - Ganadero1 ----------------------------------------------------------
		-- Lote 7 - Avicola
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L12398920',false,7,3);
			-- Animales (Lote 5)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-001','2020-02-01','2020-02-18', 0, false, 7,7,3);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-002','2020-02-01','2020-02-18', 0, false, 7,7,3);	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-003','2020-02-01','2020-02-18', 0, false, 7,7,3);	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-004','2020-02-01','2020-02-18', 0, false, 7,7,3);	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-005','2020-02-01','2020-02-18', 0, false, 7,7,3);	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-006','2020-02-01','2020-02-18', 0, false, 7,7,3);	
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L12398920-007','2020-02-01','2020-02-18', 0, false, 7,7,3);	
		-- Lote 8 ARCHIVADO - Avicola
		INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
			VALUES ('L11235345',true,7,3);
			-- Animales (Lote 8 ARCHIVADO)
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-001',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3);	-- Animal 64
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-002',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3);	-- Animal 65
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-003',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3); -- Animal 66
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-003',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3); -- Animal 67
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-003',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3); -- Animal 68
			INSERT INTO Animal(identificador_animal, comprado, fecha_entrada, procedencia, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L11235345-003',true,'2016-02-18','La granjita de Clotilde','2016-02-01','2016-02-18', 1, true, 7,8,3); -- Animal 69 nice
				-- Historicos (Lote 8)
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',64);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',65);	
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',66);
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',67);	
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',68);	
				INSERT INTO Animal_Historico(fecha_fallecimiento,fecha_venta,mas_info,animal_id)
					VALUES(null,'2020-05-24','Vendidos a KFC',69); -- nice
	
	-- Explotacion4 - Ganadero2 ----------------------------------------------------------
			-- Animales (Caprino)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812730','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 70
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812731','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 71
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812732','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 72
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812733','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 73
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812734','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 74
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812735','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 75
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812737','2016-01-15','2016-01-28', 0, false, 4,4); -- Animal 76
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812738','2016-01-15','2016-01-28', 1, false, 4,4); -- Animal 77
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812739','2016-01-15','2016-01-28', 1, false, 4,4); -- Animal 78
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812740','2016-01-15','2016-01-28', 1, false, 4,4); -- Animal 79
			-- Animales (Ovino)
				-- Lote 9 - Ovino
				INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
					VALUES ('L18273912',false,2,4);
					-- Animales (Lote 9)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L18273912-001','2020-03-15','2020-04-03', 0, false, 2,9,4);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L18273912-002','2020-03-15','2020-04-03', 0, false, 2,9,4);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L18273912-003','2020-03-15','2020-04-03', 0, false, 2,9,4);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L18273912-004','2020-03-15','2020-04-03', 0, false, 2,9,4);
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
				VALUES('L18273912-005','2020-03-15','2020-04-03', 0, false, 2,9,4);
				-- Sin lote (Ovino)
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812010','2010-01-15','2010-01-28', 0, false, 2,4); -- Animal 85
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812011','2010-01-15','2010-01-28', 0, false, 2,4); -- Animal 86
			INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('CA12739812012','2010-01-15','2010-01-28', 1, false, 2,4); -- Animal 87
				
	-- Explotacion6 - Ganadero3 ----------------------------------------------------------
			-- Animales (Caprino)
				-- Lote 10 - Caprino
				INSERT INTO Lote(identificador_lote, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
					VALUES ('L292820380492',false,4,6);
					-- Animales (Lote 10)
					INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
						VALUES('L292820380492-001','2019-03-15','2020-03-15', 1, false, 4,10,6);
					INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
						VALUES('L292820380492-002','2019-03-15','2020-03-15', 1, false, 4,10,6);
					INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
						VALUES('L292820380492-003','2019-03-15','2020-03-15', 0, false, 4,10,6);
					INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
						VALUES('L292820380492-004','2019-03-15','2020-03-15', 1, false, 4,10,6);
					INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, lote_id, explotacion_ganadera_id)
						VALUES('L292820380492-005','2019-03-15','2020-03-15', 0, false, 4,10,6);

-- 5. Animales históricos ----------------------------------------------------------------------

-- Explotacion1 - Ganadero1 ----------------------------------------------------------
			-- Animales históricos (Equino)
		INSERT INTO Animal(identificador_animal, fecha_nacimiento, fecha_identificacion, sexo, es_archivado, tipo_ganado_id, explotacion_ganadera_id)
				VALUES('EQ0981503485','2015-11-12','2015-11-13', 1, true, 5,1);
		INSERT INTO Animal_historico(fecha_fallecimiento,mas_info,animal_id)
			VALUES('2020-01-05','Sacrificado por lesión incurable',61);
			
-- 6. Citas ----------------------------------------------------------------------
	
	-- Veterinario1 --------------------------------------------------------------
		-- Cita1 para Contrato1 con Explotacion1 - Ganadero1 (No Vigente) --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2012-05-18 9:00','2012-05-18 10:00','Chequeo rutinario de los cerdos para posible venta', null, 2, 1); 
			-- Aceptada
		
		-- Cita2 para Contrato2 con Explotacion1 - Ganadero1 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-10-01 11:00','2020-10-01 12:00','Control anual del ganado Porcino', null, 0, 2); 
			-- Pendiente
			
		-- Cita3 para Contrato2 con Explotacion1 - Ganadero1 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-09-01 11:00','2020-09-01 12:00','Control anual del ganado Vacuno', 
			'Lamentablemente estaré de vacaciones, intente a partir de la primera semana de Octubre', 1, 2); 
			-- Rechazada
			
		-- Cita4 para Contrato2 con Explotacion1 - Ganadero1 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-08-01 11:00','2020-08-01 12:00','Control anual del ganado Ovino', null, 0, 2); 
			-- Pendiente
			
		-- Cita5 para Contrato2 con Explotacion1 - Ganadero1 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-07-01 8:00','2020-07-01 9:00','Preparacion para esquilar ganado Ovino', null, 2, 2); 
			-- Aceptada
		
		-- Cita6 para Contrato2 con Explotacion1 - Ganadero1 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-07-01 8:00','2020-07-01 10:00','Una oveja posiblemente con parasitos', null, 0, 2); 
			-- Pendiente
			
		-- Cita7 para Contrato3 con Explotacion4 - Ganadero2 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-07-05 8:00','2020-07-05 9:00','La cabra no esta dando leche', 
			'Por el decretado estado de alarma #YoMeQuedoEnCasa, solo atiendo urgencias', 1, 3);
			-- Rechazada
			
		-- Cita8 para Contrato3 con Explotacion4 - Ganadero2 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-07-06 9:00','2020-07-06 11:00','URGENTE: Creo que la cabra se esta muriendo (No da leche)', null, 0, 3); 
			-- Pendiente
			
		-- Cita9 para Contrato3 con Explotacion4 - Ganadero2 ---------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-05-26 10:00','2020-05-26 11:00','Varias cabras con manchas extrañas', null, 2, 3); 
			-- Aceptada
			
		-- Cita10 para Contrato2 con Explotacion1 - Ganadero1 --------------------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2019-08-20 16:00','2019-08-20 17:00','Se ha muerto una vaca y otras parecen estar mal', null, 2, 2); 
			-- Aceptada
			
	-- Veterinario2 --------------------------------------------------------------
		-- Cita11 para Contrato5 con Explotacion1 - Ganadero1 --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-10-01 11:00','2020-10-01 12:00','Chequeo rutinario de los cerdos para posible venta', null, 2, 5);
			-- Aceptada
			
		-- Cita12 para Contrato5 con Explotacion1 - Ganadero1 --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-10-01 11:00','2020-10-01 12:00','Chequeo rutinario de los cerdos para posible venta x2', null, 0, 5);
			-- Pendiente

		-- Cita13 para Contrato5 con Explotacion1 - Ganadero1 --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-08-05 8:00','2020-08-05 9:00','Pruebas antes de una matanza de Porcino, por favor, es importante.', null, 0, 5);
			-- Pendiente
			
		-- Cita14 para Contrato5 con Explotacion1 - Ganadero1 --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-08-02 8:00','2020-08-02 9:00','Pruebas antes de una matanza de Porcino', 
			'Soy vegano, le agradeceria que no vuelva a pedirme que acuda a un acto como ese.', 1, 5);
			-- Rechazada
		
		-- Cita15 para Contrato5 con Explotacion1 - Ganadero1 --------
		INSERT INTO Cita(fecha_hora_inicio,fecha_hora_fin, motivo, rechazo_justificacion, estado, contrato_id) 
			VALUES('2020-08-03 8:00','2020-08-03 9:00','Pruebas antes de una matanza de Porcino, por favor (Otro intento)', 
			'Te repito, soy vegano, no me gustan las mantanzas.', 1, 5);
			-- Rechazada

-- 7. Productos ----------------------------------------------------------------------

	-- Veterinario1 --------------------------------------------------------------
		-- Producto1 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Thrombocid', 22, false, 12.50, 1);
		-- Producto2 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Frontline', 13, false, 15, 1);
		-- Producto3 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Vetoquinol', 0, true, 10, 1);
		-- Producto4 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Prevender', 0, false, 8.75, 1);
			
	-- Veterinario2 --------------------------------------------------------------
		-- Producto5 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Artivet', 34, false, 13, 2);
		-- Producto6 --------
		INSERT INTO Producto(name,cantidad,necesita_receta,precio,veterinario_id)
			VALUES('Parasitel', 0, false, 18, 2);


-- 8. Recetas ----------------------------------------------------------------------

    -- Veterinario1 ----------------------------------------------------------------
      -- Cita1 con Contrato1 con Explotacion1 - Ganadero1 (No Vigente) -------------
    	-- Receta1 -----------------------------------------------------------------
        INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Medicamento para dar diariamente a los cerdos',true,'2012-05-18 12:03',1);
            -- LineaReceta1 para Receta1 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(3,10.50,1,1);
            -- LineaReceta2 para Receta1 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(2,15,2,1);
                
        -- Receta2 -----------------------------------------------------------------
        INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Otros medicamentos que necesita darles además de los de la anterior receta',true,'2012-05-18 15:48',1);
            -- LineaReceta3 para Receta2 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(1,10,3,2);
            -- LineaReceta4 para Receta2 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(1,9.50,4,2);
                
      -- Cita9 para Contrato3 con Explotacion4 - Ganadero2 ---------------------
        -- Receta3 -----------------------------------------------------------------------------
		INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Medicamento a dar cada 2 dias a las cabras con manchas',false,'2020-05-26 11:21',9);
            -- LineaReceta5 para Receta3 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(2,8.75,4,3);

      -- Cita10 con Contrato2 con Explotacion1 - Ganadero1 ---------------------
    	-- Receta4 -----------------------------------------------------------------
        INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Medicamento para curar la infección de orina de las vacas',true,'2019-08-20 17:20',10);
            -- LineaReceta6 para Receta4 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(4,10.50,1,4);
            -- LineaReceta7 para Receta4 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(1,16,2,4);
      
        -- Receta5 -----------------------------------------------------------------
        INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Otros medicamentos para las vacas que te serán útil',true,'2019-08-20 20:23',10);
            -- LineaReceta8 para Receta5 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(1,10,3,5);
                
        -- Receta6 (No facturada) -----------------------------------------------------------------
        INSERT INTO Receta(descripcion,es_facturado,fecha_realizacion,cita_id)
            VALUES('Últimos medicamentos para las vacas, perdone las molestias',false,'2019-08-21 21:01',10);
            -- LineaReceta10 para Receta6 -------------------------------------------------------
            INSERT INTO Linea_receta(cantidad,precio_venta,producto_id,receta_id)
                VALUES(2,9,4,6);
            
-- 9. Facturas ----------------------------------------------------------------------

    -- Veterinario1 -----------------------------------------------------------------
      -- Factura1 -------------------------------------------------------------------
      INSERT INTO Factura(es_pagado,fecha_emision,contrato_id)
          VALUES(true,'2012-05-18 15:10',1);
            -- Receta1 con Factura1 -------------------------------------------------------------
            INSERT INTO Factura_recetas(factura_id,recetas_id)
                VALUES(1,1);
                
      -- Factura2 -------------------------------------------------------------------
      INSERT INTO Factura(es_pagado,fecha_emision,contrato_id)
          VALUES(true,'2012-05-18 18:59',1);
            -- Receta2 con Factura2 -------------------------------------------------------------
            INSERT INTO Factura_recetas(factura_id,recetas_id)
                VALUES(2,2);
            -- LineaFactura1 con Factura2 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(2,10.50,2,1);
            -- LineaFactura2 con Factura2 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(1,15,2,2);
                
      -- Factura3 -------------------------------------------------------------------
      INSERT INTO Factura(es_pagado,fecha_emision,contrato_id)
          VALUES(true,'2019-09-02 18:13',2);
            -- Receta4 con Factura3 -------------------------------------------------------------
            INSERT INTO Factura_recetas(factura_id,recetas_id)
                VALUES(3,4);
                
      -- Factura4 -------------------------------------------------------------------
      INSERT INTO Factura(es_pagado,fecha_emision,contrato_id)
          VALUES(true,'2019-09-10 12:39',2);
            -- Receta5 con Factura4 -------------------------------------------------------------
            INSERT INTO Factura_recetas(factura_id,recetas_id)
                VALUES(4,5);
            -- LineaFactura3 con Factura4 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(2,10.50,4,1);
            -- LineaFactura4 con Factura4 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(1,15,4,2);

                
      -- Factura5 -------------------------------------------------------------------
      INSERT INTO Factura(es_pagado,fecha_emision,contrato_id)
          VALUES(false,'2020-04-25 12:04',3);
            -- LineaFactura5 con Factura3 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(4,12,5,1);
            -- LineaFactura6 con Factura3 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(2,8.75,5,4);
            -- LineaFactura7 con Factura3 -------------------------------------------------------
            INSERT INTO Linea_factura(cantidad,precio_venta,factura_id,producto_id)
                VALUES(1,10.50,5,3);

