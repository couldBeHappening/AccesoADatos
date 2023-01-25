package practica_ManejoDeConectores;

/* Importamos las librerias que vamos a utilizar */
import java.sql.*;

public class SwitchOpcion1 {
	
	/* Método que nos muestra la información de las tablas con parámetros */
	public static void informacionTabla (Connection conexion, Statement sentencia, ResultSetMetaData resultadoMeta) throws SQLException {
		
		/* */
		Enunciado.numColumnas = resultadoMeta.getColumnCount();
		
		/* */
		for (int i = 1; i <= Enunciado.numColumnas; i++) {
			
			String nombre = resultadoMeta.getColumnName(i).toString();
			String tipo = resultadoMeta.getColumnTypeName(i).toString();
			String tipoJava = "";
			if(tipo.equalsIgnoreCase("VARCHAR")) {
				tipoJava = "Cadena de Texto";
			}
			else if(tipo.equalsIgnoreCase("INT UNSIGNED")) {
				tipoJava = "Número Entero";
			}
			else if(tipo.equalsIgnoreCase("FLOAT")) {
				tipoJava = "Número Decimal";
			}
			else if(tipo.equalsIgnoreCase("DATE")) {
				tipoJava = "Fecha actual en formato AAAA-MM-DD";
			}
			else {
				tipoJava = tipo;
			}
			
			Enunciado.nombreColumna.add(nombre);
			Enunciado.tipoColumna.add(tipo);
			Enunciado.tipoColumnaJava.add(tipoJava);
		}
	}
	
	/* Método que consulta la información de los tipos de datos de la tabla con parámetros */
	public static void consultaTabla (Connection conexion, Statement sentencia, ResultSetMetaData resultadoMeta) throws SQLException {
		
		System.out.println("La tabla empleados, tiene " + Enunciado.numColumnas + " columnas.");
		
		for (int i = 1; i <= Enunciado.numColumnas; i++) {
			
			String nombre = resultadoMeta.getColumnName(i).toString();
			String tipo = Enunciado.tipoColumna.get(i-1);
			String tipoJava = Enunciado.tipoColumnaJava.get(i-1);
			
			System.out.println("Nombre Columna: " + nombre + " Tipo Columna: " + tipo + " (" + tipoJava + ")");
		}
	}
	
}
