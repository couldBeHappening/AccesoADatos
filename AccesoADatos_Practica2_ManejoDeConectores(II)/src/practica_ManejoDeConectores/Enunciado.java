package practica_ManejoDeConectores;

/* Importamos las librerias que vamos a utilizar */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class Enunciado {

	/* Declaramos como estáticos el teclado, los ArrayList y las variables que vamos a usar, son públicos y estáticos para poder usarlo en todas las clases */
	public static Scanner teclado = new Scanner (System.in);
	public static ArrayList <String> nombreColumna = new ArrayList <String> ();
	public static ArrayList <String> tipoColumna = new ArrayList <String> ();
	public static ArrayList <String> tipoColumnaJava = new ArrayList <String> ();
	public static ArrayList <String> guardarDatos;
	public static int numColumnas;
	public static String fechaActual;

	/* Método main donde lanzaremos el programa */
	public static void main(String[] args) {

		/* Iniciailizamos las variables que vamos a usar en este método main */
		int opciones = 0;
		int contador = 3;
		int empleadosInsertados = 0;

		/* Abrimos un try para poder manejar la excepciones que nos surjan */
		try {

			/* Driver que permite que Java se conecte con la BBDD, pasamos usuario, contraseña y nombre para crear la conexión */
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/empleados","vespertino","password");

			/* Declaramos las sentencias que vamos a utilizar */
			Statement sentencia = conexion.createStatement();
			Statement sentencia2 = conexion.createStatement();

			/* Con la variable String inicializamos las consultas y devolvemos el resultado de lo guardado con el ResultSet*/
			String consulta = "SELECT * FROM empleado;";
			ResultSet resultado = sentencia.executeQuery(consulta);

			String consultaFecha = "SELECT CURDATE()";
			ResultSet resultadoFecha = sentencia2.executeQuery(consultaFecha);
			
			/* */
			ResultSetMetaData resultadoMeta = resultado.getMetaData();
			SwitchOpcion1.informacionTabla (conexion, sentencia, resultadoMeta);

			resultadoFecha.next();

			fechaActual = new SimpleDateFormat ("yyyy-MM-dd").format(resultadoFecha.getDate(1));			

			/* Imprimimos por pantalla el mensaje de inicio del programa, mostrando la fecha del día y dando el menú de opciones */
			System.out.println("¡Hola! Hoy es día " + fechaActual +  ". Indica que opción quieres realizar: ");			

			/* Bucle do para que haga muestre el menú */
			do {
				System.out.println("1. Consulta tabla empleado.");
				System.out.println("2. Introduce nuevo empleado.");
				System.out.println("3. Salir");

				/* Declaramos un try para tener las excecpiones controladas */
				try {

					/* Declaramos la variable donde vamos a guardar la opción que el usuario pasa por teclado */
					opciones = Integer.parseInt(teclado.nextLine());

					/* Con el condicional switch declaramos las opciones del menú para que el usuario indique que quiere hacer */
					switch (opciones) {

					/* En el case 1 llamaremos al método creado en la clase SwitchOpcion1 */
					case 1:
						SwitchOpcion1.consultaTabla(conexion, sentencia, resultadoMeta);
						System.out.println("¿Deseas realizar otra gestión?"); /* Pasamos por consola si quiere realizar otra consulta */
						break; /* Salida del condicional */
						
					/* En el case 2 llamaremos a los métodos creado en la clase SwitchOpcion2 */
					case 2:
						guardarDatos = new ArrayList <String> (); /* Inicializamos el ArrayList guardarDatos con sus constructores */

						/* Creamos dentro un condicional if para manejar las posibles situaciones que se pueden dar al insertar los datos */
						if(SwitchOpcion2.solicitarDatos (conexion, sentencia, resultado) == 0) {

							contador = 0; /* Inicializamos el contador */
							/* Si se llega al límite de errores el programa se bloquea y nos lo muestra por pantalla al igual que nos muestra los empleados insertados */
							System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
							System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");
							break; /* Salida del condicional */
						}
						/* Con este if llamamos al método de SwitchOpción2 y lo igualamos a true */
						if (SwitchOpcion2.muestraDatosInsertados() == true) {

							/* */
							String inTo = SwitchOpcion2.generarInsert(conexion, sentencia);
							empleadosInsertados += sentencia.executeUpdate(inTo);
							
							System.out.println("Los datos se han introducido de forma correcta con fecha de alta " + fechaActual +  ". Gracias");

						} else { /* Si no se introducen los datos, nos lo muestra el programa por consola */
							System.out.println("No se han introducido los datos");
						}

						break; /* Salida del condicional case 2 */

						/* En el caso 3 imprimimos por consola como menaje final el número de empleados introducidos en la BBDD */
					case 3:
						System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado. ¡Gracias por usar el programa!");
						break; /* Salida del condicional case 3 */

					default: /* Con el default hacemos que el contador reste posibles errores al recoger la opción del menú*/

						contador --;

						if (contador == 0) {

							System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
							System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");

						} else {
							System.out.println("La opción introducida no es correcta, por favor revisa las opciones disponibles. Gracias.");
						} 

						break;
					}

				} catch (NumberFormatException e) { /* */

					contador --;
					if (contador == 0) {

						System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
						System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");

					} else {
						System.out.println("La opción introducida no es correcta, por favor revisa las opciones disponibles. Gracias.");
					}

				} catch (SQLException e) {
					System.out.println("¡Error! Comprueba que los datos introducidos son correctos");
				}

			} while (opciones != 3 && contador > 0);

			/*  */
			conexion.close();
			sentencia.close();
			resultado.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		teclado.close(); /* Cerramos teclado */
	}
}


