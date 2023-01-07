package practica_ManejoDeConectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Enunciado {

	public static void main(String[] args) {
		
		try {
			
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/empleados","vespertino","password");
			Statement sentencia = conexion.createStatement();
			String consulta = "SELECT * FROM empleado";
			ResultSet resultado = sentencia.executeQuery(consulta);
			ResultSetMetaData resultadoMeta = resultado.getMetaData();
			
			String departamento = args [0];
			int departamento = Integer.parseInt(departamento);
			DecimalFormat formato = new DecimalFormat("##,##0.00");
			
			PreparedStatement sentenci = conexión.prepareStatement(sql);


			String primerApellido = args[1];
			String nif = args[2];
			String salarioEmpleados = args[3];
			
			if (resultado.next() == false) {
				
				System.out.println("El departamento " + depatamento + " no existe en la BBDD ");
			
			} else {
				
			}
			
		} catch (SQLException e){
			
		}
			
		

	}

}
