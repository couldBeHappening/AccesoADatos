package practica_ManejoDeConectores;

import java.sql.*;
import java.text.DecimalFormat;

public class Enunciado {

	public static void main(String[] args) {

		if (args.length > 0) {

			try {

				Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/empleados","vespertino","password");

				String departamento = args [0];
				int departamentoParseo = Integer.parseInt(departamento);
				DecimalFormat formatoDecimal = new DecimalFormat("##,##0.00");

				String sql = "SELECT apellido1,nif,salario FROM empleado WHERE codigo_departamento = (?) ";
				PreparedStatement sentencia = conexion.prepareStatement(sql);

				sentencia.setInt(1, departamentoParseo);
				ResultSet resultado = sentencia.executeQuery();

				String sql2 = "SELECT AVG(salario),dep.nombre FROM empleado emple,departamento dep WHERE dep.codigo = (?) AND emple.codigo_departamento=dep.codigo"; 
				PreparedStatement sentencia2 = conexion.prepareStatement(sql2);

				sentencia2.setInt(1, departamentoParseo);
				ResultSet resultado2 = sentencia2.executeQuery();

				if (resultado.next() == true) {

					resultado2.next();

					Float media = resultado2.getFloat(1);
					String nombreDepartamento = resultado2.getString(2);


					System.out.println("Los empleados del departamento " + nombreDepartamento + ", con código " + departamento + " son:");


					do {

						String apellido1 = resultado.getString(1);
						String nif = resultado.getString(2);
						Float salario = resultado.getFloat(3);

						System.out.println("El primer apellido del empleado es: " + apellido1 + " con NIF: " + nif + ", y tiene un salario de: " + salario + "€");

					} while (resultado.next());

					System.out.println("El salario medio del departamento " + nombreDepartamento + " es: " + formatoDecimal.format(media) + "€");

				} else {

					System.out.println("El departamento " + departamentoParseo + " no existe en la BBDD.");

				} 

				conexion.close();
				sentencia.close();
				sentencia2.close();
				resultado.close();
				resultado2.close();

			} catch (SQLException e){
				System.out.println("¡Error! no se han encontardo los datos consultados");

			} catch (NumberFormatException e) {
				System.out.println("El valor introducido en los arguemntos no es correcto. ¡Vuelva a intentarlo!");
			}



		} else {

			System.out.println("No se han introducido argumentos de busqueda.");
		}
	}
}
