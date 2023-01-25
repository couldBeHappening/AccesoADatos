package practica_ManejoDeConectores;

/* Importamos las librerias que vamos a utilizar */
import java.sql.*;

public class SwitchOpcion2 {

	/* Método comprueba datos con parámetros */
	public static boolean compruebaDatos (String datoIntroducido, int posicionDato, Connection conexion, Statement sentencia) throws SQLException {

		boolean comprobar = true; /* variable que devolverá el valor del método */ 

		/* Primer condicional if comprueba en los datos que deben ser "INT UNSIGNED" */
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("INT UNSIGNED")) {

			/* Abrimos un try-catch, intentamos parsear y si no es posible capturamos la excepción */
			try {

				int dato = Integer.parseInt(datoIntroducido);

				/* En caso de que le dato se la columna "codigo" comprobaremos que no esté en la BBDD */
				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("codigo")) {

					/* Hacemos una consulta y comparamos lo resultados con un bucle while, indicando en comprobar si es true o false */
					ResultSet resultado = sentencia.executeQuery("SELECT codigo FROM empleado;");

					while (resultado.next()) {

						int busqueda = resultado.getInt(1);

						if (busqueda == dato){
							comprobar = false;
							System.out.println("Código ya registrado");
							break;

						} else {
							comprobar = true;
						}
					}
					resultado.close();
				}

				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("codigo_departamento")) {

					ResultSet resultado;
					resultado = sentencia.executeQuery("SELECT codigo FROM departamento;");

					while (resultado.next()) {

						int busqueda = resultado.getInt(1);

						if (busqueda == dato) {
							comprobar = true;
							break;

						} else {
							comprobar = false;							
						}
					}
					resultado.close();
				}

			} catch (NumberFormatException e) {
				comprobar = false;
			}
		}

		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("FLOAT")) {

			try {

				double dato = Double.parseDouble(datoIntroducido);

				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("salario") && dato > 0) {
					comprobar = true;

				} else {
					comprobar = false;
				}
			} catch (NumberFormatException e) {
				comprobar = false;
			}
		}
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("DATE")) {
			if(datoIntroducido.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
				comprobar = true;

			} else {
				comprobar = false;

			}			
		}
		
		if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("NIF"))  {
			if (datoIntroducido.matches("\\d{8}[A-Z]")) {
				comprobar = true;
			} else {
				comprobar = false;
			}
			
		}
		
		if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("nombre")|| Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("apellido1")
				|| Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("apellido2"))  {
			if (datoIntroducido.matches("[A-Za-z]{1,100}")) {
				comprobar = true;
			} else {
				comprobar = false;
			}
			
		}
		return comprobar;
	}
	
	
	/* Método comprueba datos con parámetros y capturamos la SQLException */
	public static int solicitarDatos (Connection conexion, Statement sentencia, ResultSet resultado) throws SQLException {
		int contadorError = 3;

		for (int i = 0; i < Enunciado.numColumnas && contadorError > 0; i++) {

			System.out.println("Introduce: " + Enunciado.nombreColumna.get(i) + " de tipo: " + Enunciado.tipoColumna.get(i) + " (" + Enunciado.tipoColumnaJava.get(i) + ")");

			String guardaDatos = Enunciado.teclado.nextLine();

			if (guardaDatos.length() <= 0){
				System.out.println("El campo no puede estar vacio.");

				i--;
				contadorError --;

			} else if (guardaDatos.equalsIgnoreCase("null")){
				System.out.println("El dato introducido no puede ser null.");

				i--;
				contadorError --;

			} else if (!compruebaDatos(guardaDatos,i,conexion,sentencia)) {
				System.out.println("El dato introducido no es correcto.");

				i--;
				contadorError --;

			}  else if (!guardaDatos.equals(Enunciado.fechaActual) && Enunciado.tipoColumna.get(i).equalsIgnoreCase("DATE")) {
				System.out.println("La fecha introducida no se corresponde con la actual.");

				i--;
				contadorError --;

			} else {

				Enunciado.guardarDatos.add(guardaDatos);
				contadorError = 3;
				System.out.println("¡El dato se ha guardado de forma correcta!");
			}
		}
		return contadorError;
	}
	/* Método comprueba datos con parámetros */
	public static String generarInsert (Connection conexion, Statement sentencia) {

		String guardar = "INSERT INTO empleado VALUES (";

		for (int i = 0; i < Enunciado.numColumnas;  i++) {

			if (Enunciado.tipoColumna.get(i).equalsIgnoreCase("VARCHAR") || Enunciado.tipoColumna.get(i).equalsIgnoreCase("DATE")) {

				guardar += "'" + Enunciado.guardarDatos.get(i) + "'";

			} else {
				guardar += Enunciado.guardarDatos.get(i);
			}
			if(i < Enunciado.numColumnas-1) {
				guardar += ",";
			}
		}
		guardar += ");";

		return guardar;

	} 

	/* Método muestraDatosInsertados  */
	public static boolean muestraDatosInsertados () {

		boolean opcionRespuesta = false;
		int opcion;
		int contador = 3;

		System.out.println("¿Los datos introducidos son correctos?");

		for (int i = 0; i < Enunciado.guardarDatos.size(); i++) {

			System.out.println(Enunciado.nombreColumna.get(i) + " : " + Enunciado.guardarDatos.get(i));
		}
		do { /* Bucle do-while haz mientras, nos muestra las opciones de si/no */
			try {
				System.out.println("1. Si");
				System.out.println("2. No");

				opcion = Integer.parseInt(Enunciado.teclado.nextLine());

				/* Condicional switch para validar si guardamos los datos o no */
				switch (opcion){

				case 1:
					opcionRespuesta = true;
					break;

				case 2:
					opcionRespuesta = false;
					break;

					/* Con el default indicamos por pantalla que la opción no es correcta */
				default: 	
					System.out.println("La opción introducida no es correcta.");
					contador --;
				}

				/* Capturamos la excepción NumberFormatException, por si no nos introduce un número y lo hace con letras o caracteres extraños */
			} catch (NumberFormatException e) {
				System.out.println("Opción no válida.");
				contador --;
				opcion = 0;
			}
			/* Mientras que las opciones sean distintas de 1 o 2 el */
		} while (opcion != 1 && opcion !=2 && contador > 0);

		return opcionRespuesta;
	}	
}

