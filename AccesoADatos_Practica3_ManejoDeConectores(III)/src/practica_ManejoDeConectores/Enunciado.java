package practica_ManejoDeConectores;

/* Importamos las librerias necesarias */
import java.sql.*;
import java.text.DecimalFormat;

/* Creamos clase enunciado con main para ejecutarse*/
public class Enunciado {

	public static void main(String[] args) {

		/* Abrimos condicional if - else para comprobar que se han introducido argumentos */
		if (args.length > 0) {

			try {

				/* Inicializamos la conexión con la BBDD */
				Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/empleados","vespertino","password");

				/* Inicializamos las variable que vamos a utilizar */
				String departamento = args [0];
				int departamentoParseo = Integer.parseInt(departamento);
				DecimalFormat formatoDecimal = new DecimalFormat("##,##0.00");

				/* Preparación de la primera consulta que vamos a realizar */
				String sql = "SELECT apellido1,nif,salario FROM empleado WHERE codigo_departamento = (?) ";
				PreparedStatement sentencia = conexion.prepareStatement(sql);

				sentencia.setInt(1, departamentoParseo);
				ResultSet resultado = sentencia.executeQuery();

				/* Preparación de la segunda consulta que vamos a realizar */
				String sql2 = "SELECT AVG(salario),dep.nombre FROM empleado emple,departamento dep WHERE dep.codigo = (?) AND emple.codigo_departamento=dep.codigo"; 
				PreparedStatement sentencia2 = conexion.prepareStatement(sql2);

				sentencia2.setInt(1, departamentoParseo);
				ResultSet resultado2 = sentencia2.executeQuery();

				/* Condicional if - else para comprobar */
				
				if (resultado.next() == true) {

					resultado2.next();

					Float media = resultado2.getFloat(1);
					String nombreDepartamento = resultado2.getString(2);


					System.out.println("Los empleados del departamento " + nombreDepartamento + ", con código " + departamento + " son:");

					/* Bucle do - while para que haga */
					do { 

						String apellido1 = resultado.getString(1);
						String nif = resultado.getString(2);
						Float salario = resultado.getFloat(3);

						System.out.println("El primer apellido del empleado es: " + apellido1 + " con NIF: " + nif + ", y tiene un salario de: " + salario + "€");

					} while (resultado.next()); /* Mientras */

					String salarioMedio = formatoDecimal.format(media);
					System.out.println("El salario medio del departamento " + nombreDepartamento + " es: " + salarioMedio + "€");

				} else { /* Cerramos condicional if - else mostrando por pantalla que el departamento introducido no existe */

					System.out.println("El departamento " + departamentoParseo + " no existe en la BBDD.");

				} 
				
				/* Cerramos todos los objetos */
				conexion.close();
				sentencia.close();
				sentencia2.close();
				resultado.close();
				resultado2.close();

				/* Capturamos la SQLException para evitar los errores que nos surjan con las querys */
			} catch (SQLException e){
				System.out.println("¡Error! no se han encontardo los datos consultados");

				/* Capturamos la NumberFormatException para evitar los errores que nos surjan si no se introduce un argumento diferente a un número */
			} catch (NumberFormatException e) {
				System.out.println("El valor introducido en los arguemntos no es correcto. ¡Vuelva a intentarlo!");
			}

		} else { /* Cerramos condicional if - else mostrando por pantalla en este caso que no se han introducido argumentos */ 

			System.out.println("No se han introducido argumentos de busqueda.");
		}
	}
}
