package practica_ManejoDeConectores;
import java.sql.*;

public class SwitchOpcion1 {
	
	
	public static void informacionTabla (Connection conexion, Statement sentencia, ResultSetMetaData resultadoMeta) throws SQLException {
		
		Enunciado.numColumnas = resultadoMeta.getColumnCount();
		
		for (int i = 1; i <= Enunciado.numColumnas; i++) {
			
			String nombre = resultadoMeta.getColumnName(i).toString();
			String tipo = resultadoMeta.getColumnTypeName(i).toString();
			
			Enunciado.nombreColumna.add(nombre);
			Enunciado.tipoColumna.add(tipo);
		}
	}
	
	public static void consultaTabla (Connection conexion, Statement sentencia, ResultSetMetaData resultadoMeta) throws SQLException {
		
		System.out.println("La tabla empleados, tiene " + Enunciado.numColumnas + " columnas.");
		
		for (int i = 1; i <= Enunciado.numColumnas; i++) {
			
			String nombre = resultadoMeta.getColumnName(i).toString();
			String tipo = resultadoMeta.getColumnTypeName(i).toString();
			
			System.out.println("Nombre Columna: " + nombre + " Tipo Columna: " + tipo);
		}
	}
	
}
