package practica_ManejoDeConectores;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Enunciado {

	public static Scanner teclado = new Scanner (System.in);
	public static ArrayList <String> nombreColumna = new ArrayList <String> ();
	public static ArrayList <String> tipoColumna = new ArrayList <String> ();
	public static ArrayList <String> guardarDatos;
	public static int numColumnas;


	public static void main(String[] args) {

		int opciones = 0;
		int contador = 3;
		int empleadosInsertados = 0;


		try {

			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/empleados","vespertino","password");

			Statement sentencia = conexion.createStatement();
			Statement sentencia2 = conexion.createStatement();

			String consulta = "SELECT * FROM empleado;";
			ResultSet resultado = sentencia.executeQuery(consulta);

			String consultaFecha = "SELECT CURDATE()";
			ResultSet resultadoFecha = sentencia2.executeQuery(consultaFecha);
			
			ResultSetMetaData resultadoMeta = resultado.getMetaData();
			SwitchOpcion1.informacionTabla (conexion, sentencia, resultadoMeta);

			resultadoFecha.next();

			String fechaActual = new SimpleDateFormat ("dd-MM-yyyy").format(resultadoFecha.getDate(1));			

			System.out.println("¡Hola! Hoy es día " + fechaActual +  ". Indica que opción quieres realizar: ");			

			do {
				System.out.println("1. Consulta tabla empleado.");
				System.out.println("2. Introduce nuevo empleado.");
				System.out.println("3. Salir");

				try {

					opciones = teclado.nextInt();

					switch (opciones) {

					case 1:
						SwitchOpcion1.consultaTabla(conexion, sentencia, resultadoMeta);
						System.out.println("¿Deseas realizar otra gestión?");
						break;

					case 2:
						guardarDatos = new ArrayList <String> ();
						teclado.nextLine();

						if(SwitchOpcion2.solicitarDatos (conexion, sentencia, resultado) == 0) {

							contador = 0;
							System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
							System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");
							break;
						}

						if (SwitchOpcion2.muestraDatosInsertados() == true) {

							String inTo = SwitchOpcion2.generarInsert(conexion, sentencia);
							teclado.nextLine();
							empleadosInsertados += sentencia.executeUpdate(inTo);
							
							empleadosInsertados ++;

							System.out.println("Los datos se han introducido de forma correcta con fecha de alta Gracias");

						} else {
							System.out.println("No se han introducido los datos");
						}

						break;

					case 3:
						System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado. ¡Gracias por usar el programa!");
						break;

					default:	

						contador --;

						if (contador == 0) {

							System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
							System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");

						} else {
							System.out.println("La opción introducida no es correcta, por favor revisa las opciones disponibles. Gracias.");
						} 

						break;
					}

				} catch (InputMismatchException e) {

					contador --;
					if (contador == 0) {

						System.out.println("¡Error! Has bloqueado el sistema, intentalo de nuevo más tarde con las opciones disponibles. Gracias.");
						System.out.println("Se han insertado " + empleadosInsertados + " empleados en la BBDD empleado.");

					} else {
						System.out.println("La opción introducida no es correcta, por favor revisa las opciones disponibles. Gracias.");
					}

					teclado.nextLine();

				}

			} while (opciones != 3 && contador > 0);

			conexion.close();
			sentencia.close();
			resultado.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		teclado.close();
	}
}


