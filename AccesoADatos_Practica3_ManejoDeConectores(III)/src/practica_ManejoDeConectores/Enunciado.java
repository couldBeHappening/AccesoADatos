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
			
			String departamento = args [0];
			int departamentoPareseo = Integer.parseInt(departamento);
			DecimalFormat formatoDecimal = new DecimalFormat("##,##0.00");
			
			String sql = "SELECT apellido1,nif,salario FROM empleados WHERE codigo_departamento = (?) ";
			PreparedStatement sentencia = conexion.prepareStatement(sql);

			sentencia.setInt(1, departamentoPareseo);
			ResultSet resultado = sentencia.executeQuery();
			
			
		if (resultado.next() == false) {
				
				System.out.println("El departamento " + departamentoPareseo + " no existe en la BBDD.");
			
			} else {
				resultado.beforeFirst();
				
				while (resultado.next()) {
					
					String apellido1 = resultado.getString(1);
					String nif = resultado.getString(2);
					Float salario = resultado.getFloat(3);
					
					System.out.println("apellido " + apellido1 + "nif" + nif + "salario" + salario);
					
				}
			} 
			
		} catch (SQLException e){
			
		}
			
		

	}

}
