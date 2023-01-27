package practica_ManejoDeConectores;

/* Importamos las librerias que vamos a utilizar */
import java.sql.*;

public class SwitchOpcion2 {

	/* Método comprueba datos con parámetros y lanza las excepciones SQL */
	public static boolean compruebaDatos (String datoIntroducido, int posicionDato, Connection conexion, Statement sentencia) throws SQLException {

		boolean comprobar = true; /* variable que devolverá el valor del método */ 

		/* Primer condicional if comprueba los datos que deben ser "INT UNSIGNED" */
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("INT UNSIGNED")) {

			/* Abrimos un try-catch, intentamos parsear y si no es posible capturamos la excepción */
			try {

				int dato = Integer.parseInt(datoIntroducido);

				/* En caso de que le dato se la columna "codigo" comprobaremos que no esté en la BBDD */
				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("codigo")) {

					/* Hacemos una consulta y comparamos los resultados con un bucle while, indicando en comprobar si es true o false */
					ResultSet resultado = sentencia.executeQuery("SELECT codigo FROM empleado;");

					/* El bucle se ejecuta mientras haya resultados */
					while (resultado.next()) {

						/* Guardamos en la variable el resultado de la consulta */ 
						int busqueda = resultado.getInt(1);

						/* Comparamos resultado con dato de usuario y si está en la BBDD mostramos mensaje indicándolo */
						if (busqueda == dato){
							comprobar = false;
							System.out.println("Código ya registrado");
							break;

						} else {
							comprobar = true;
						}
					}
					/* Cerramos ResultSet */
					resultado.close();
				}

				/* En el caso del codigo de departamento lo que se busca es que sí exista en la tabla, de forma prácticamente igual al método anterior */
				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("codigo_departamento")) {

					/* Creamos variable en el que se registrará la consulta de la sentencia */
					ResultSet resultado;
					resultado = sentencia.executeQuery("SELECT codigo FROM departamento;");

					/* El bucle se ejecuta mientras haya resultados */
					while (resultado.next()) {

						/* Guardamos en la variable el resultado de la consulta */
						int busqueda = resultado.getInt(1);

						/* Comparamos resultado con dato de usuario */
						if (busqueda == dato) {
							comprobar = true;
							break;

						} else {
							comprobar = false;							
						}
					}
					/* Cerramos ResultSet */
					resultado.close();
				}

			/* Si salta la excepción, el dato es incorrecto */
			} catch (NumberFormatException e) {
				comprobar = false;
			}
		}

		/* El segundo if comprueba los datos que deben ser tipo "FLOAT" */
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("FLOAT")) {

			try {

				/* Al igual que antes se intenta el parse a double */
				double dato = Double.parseDouble(datoIntroducido);

				/* Si el dato se corresponde con la columna salario, se compruea que se mayor que 0 */
				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("salario") && dato > 0) {
					comprobar = true;

				/* Si no es correcto "comprobar" se pone en false */
				} else {
					comprobar = false;
				}
				
			/* Si salta la excepción, el dato es incorrecto */
			} catch (NumberFormatException e) {
				comprobar = false;
			}
		}
		
		/* El tercer if comprueba los datos que deben ser fechas DATE y si el formato es el correcto */
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("DATE")) {
			
			/* Comprobamos con método matches() */
			if(datoIntroducido.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
				comprobar = true;

			/* Si no es correcto "comprobar" se pone en false */
			} else {
				comprobar = false;

			}			
		}
		
		/* En el cuarto if, validamos el formato de la columna nif */
		if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("NIF"))  {
			
			/* Comprobamos con método matches() */
			if (datoIntroducido.matches("\\d{8}[A-Z]")) {
				comprobar = true;
				
			/* Si no es correcto "comprobar" se pone en false */
			} else {
				comprobar = false;
			}
			
		}
		
		/* Por último, las columna nombre, apellido1 y apellido2 no admiten números ni caracteres especiales, sólo letras */
		if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("nombre")|| Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("apellido1")
				|| Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("apellido2"))  {
			
			/* Comprobamos con método matches() */
			if (datoIntroducido.matches("[A-Za-z]{1,100}")) {
				comprobar = true;
				
			/* Si no es correcto "comprobar" se pone en false */
			} else {
				comprobar = false;
			}			
		}
		/* Se devuelve el valor final */
		return comprobar;
	}
	
	
	/* Método comprueba datos con parámetros y capturamos la SQLException, tiene un contador que registra los errores al introducir los datos y lanza las excepciones SQL */
	public static int solicitarDatos (Connection conexion, Statement sentencia, ResultSet resultado) throws SQLException {
		int contadorError = 3;

		/* Con el bucle for, indicamos que solicite tantos datos como columnas tiene la tabla y evaluamos el límite de errores */ 
		for (int i = 0; i < Enunciado.numColumnas && contadorError > 0; i++) {

			/* Imprimimos el el dato y el tipo de dato que se solicita*/
			System.out.println("Introduce: " + Enunciado.nombreColumna.get(i) + " de tipo: " + Enunciado.tipoColumna.get(i) + " (" + Enunciado.tipoColumnaJava.get(i) + ")");

			/* Guardamos la respuesta del usuario en la variable */
			String guardaDatos = Enunciado.teclado.nextLine();

			/* Comprobamos que no se metan valores vacíos */
			if (guardaDatos.length() <= 0){
				
				System.out.println("El campo no puede estar vacio.");

				/* Restamos 1 a la variable iterativa y al contador de errores */
				i--;
				contadorError --;

			/* Comprobamos que no se metan valores nulos */
			} else if (guardaDatos.equalsIgnoreCase("null")){
				
				System.out.println("El dato introducido no puede ser null.");

				/* Restamos 1 a la variable iterativa y al contador de errores */
				i--;
				contadorError --;

			/* Comprobamos que no se metan valores erróneos con el método compruebaDatos() */
			} else if (!compruebaDatos(guardaDatos,i,conexion,sentencia)) {
				
				System.out.println("El dato introducido no es correcto.");

				/* Restamos 1 a la variable iterativa y al contador de errores */
				i--;
				contadorError --;

			/* Comprobamos que la fecha se la actual */
			}  else if (!guardaDatos.equals(Enunciado.fechaActual) && Enunciado.tipoColumna.get(i).equalsIgnoreCase("DATE")) {
				
				System.out.println("La fecha introducida no se corresponde con la actual.");

				/* Restamos 1 a la variable iterativa y al contador de errores */
				i--;
				contadorError --;

			/* Si pasa los filtros anteriores se guarda en el ArrayList guardarDatos de la clase Enunciado y se resetea el contador de errores */
			} else {
				
				Enunciado.guardarDatos.add(guardaDatos);
				contadorError = 3;
				System.out.println("¡El dato se ha guardado de forma correcta!");
			}
		}
		
		/* Se devuelve el valor final */
		return contadorError;
	}
	/* Método generarInsert que devuelve un String, se comprueba el tipo de datos para dar formato correcto */
	public static String generarInsert (Connection conexion, Statement sentencia) {

		/* Declaramos la variable que tendrá la consulta y la comenzamos */
		String guardar = "INSERT INTO empleado VALUES (";

		/* El bucle for recorre el ArrayList donde se encuentran los tipos de dato */
		for (int i = 0; i < Enunciado.numColumnas;  i++) {

			/* Si corresponde con "VARCHAR" o "DATE" se añade a la variable con comillas simples */
			if (Enunciado.tipoColumna.get(i).equalsIgnoreCase("VARCHAR") || Enunciado.tipoColumna.get(i).equalsIgnoreCase("DATE")) {

				guardar += "'" + Enunciado.guardarDatos.get(i) + "'";

			/* Si no, se añade sin comillas */
			} else {
				
				guardar += Enunciado.guardarDatos.get(i);
			}
			/*i no se trata del último dato, añadimos una coma para separar */
			if(i < Enunciado.numColumnas-1) {
				
				guardar += ",";
			}
		}
		/* finalizamos con el formato apropiado SQL y devolvemos el valor */
		guardar += ");";

		return guardar;

	} 

	/* Método muestraDatosInsertados, se le muestra la información introducida y devuelve un booleano en caso de que el usuario indique  que es correcto o no */
	public static boolean muestraDatosInsertados () {

		/* Declaramos variables, el boolean devolverá el valor del método, opción registra la respuesta y contador indica el máximo de errores */
		boolean opcionRespuesta = false;
		int opcion;
		int contador = 3;

		/* Imprimimos por pantalla la pregunta */
		System.out.println("¿Los datos introducidos son correctos?");

		/* Recorremos el ArrayList que tiene los datos guardados y los mostramos */
		for (int i = 0; i < Enunciado.guardarDatos.size(); i++) {

			System.out.println(Enunciado.nombreColumna.get(i) + " : " + Enunciado.guardarDatos.get(i));
		}
		do { /* Bucle do-while nos muestra las opciones de si/no, se repite si introduce una opción no válida */
			try {
				System.out.println("1. Si");
				System.out.println("2. No");

				/* Se registra la respuesta del usuario y se intenta el parseo */
				opcion = Integer.parseInt(Enunciado.teclado.nextLine());

				/* Condicional switch para validar si guardamos los datos o no */
				switch (opcion){

				case 1:
					opcionRespuesta = true;
					break;

				case 2:
					opcionRespuesta = false;
					break;

				/* Con el default indicamos por pantalla que la opción no es correcta y restamos 1 al contador */
				default: 	
					System.out.println("La opción introducida no es correcta.");
					contador --;
				}

			/* Capturamos la excepción NumberFormatException, por si no nos introduce un número y lo hace con letras o caracteres extraños */
			} catch (NumberFormatException e) {
				System.out.println("Opción no válida.");
				
				/* Restamos 1 al contador y ponemos valor por defecto a opcion */
				contador --;
				opcion = 0;
			}
		
		/* Mientras que las opciones sean distintas de 1 o 2 el método pide la respuesta hasta que falle 3 veces */
		} while (opcion != 1 && opcion !=2 && contador > 0);

		/* Devolvemos el valor del boolean */
		return opcionRespuesta;
	}	
}

