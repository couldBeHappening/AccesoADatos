package practica_ManejoDeConectores;
import java.sql.*;



import java.util.InputMismatchException;

public class SwitchOpcion2 {
	
	public static int solicitarDatos (Connection conexion, Statement sentencia, ResultSet resultado) throws SQLException {
		int contadorError = 3;

		for (int i = 0; i < Enunciado.numColumnas -1 && contadorError > 0; i++) {

			System.out.println("Introduce: " + Enunciado.nombreColumna.get(i) + " de tipo: " + Enunciado.tipoColumna.get(i));

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

			} else {

				Enunciado.guardarDatos.add(guardaDatos);
				contadorError = 3;
				System.out.println("¡El dato se ha guardado de forma correcta!");
			}
		}
		return contadorError;
	}
	
	
	public static String generarInsert (Connection conexion, Statement sentencia) {

		String guardar = "INSERT INTO empleado VALUES (";

		for (int i = 0; i < Enunciado.numColumnas -1;  i++) {

			if (Enunciado.tipoColumna.get(i).equalsIgnoreCase("VARCHAR")) {

				guardar += "'" + Enunciado.guardarDatos.get(i) + "',";

			} else
				guardar += Enunciado.guardarDatos.get(i) + ",";

		}
		guardar += "CURDATE());";
		return guardar;

	} 

	
	public static boolean muestraDatosInsertados () {
		
		boolean opcionRespuesta = false;
		int opcion;
		
		System.out.println("¿Los datos introducidos son correctos?");
		
		for (int i = 0; i < Enunciado.guardarDatos.size(); i++) {
			
			System.out.println(Enunciado.nombreColumna.get(i) + " : " + Enunciado.guardarDatos.get(i));
		}
		do {
			System.out.println("1. Si");
			System.out.println("2. No");
			
			opcion = Enunciado.teclado.nextInt();
			
			try {
				
				switch (opcion){
				
				case 1:
					opcionRespuesta = true;
					break;
					
				case 2:
					opcionRespuesta = false;
					break;
					
				default: 	
					System.out.println("La opción introducida no es correcta.");
				}
				
			} catch (InputMismatchException e) {
				System.out.println("Opción no válida.");
			}
			
		} while (opcion != 1 && opcion !=2);
		
		return opcionRespuesta;
	}
	
	
	public static boolean compruebaDatos (String datoIntroducido, int posicionDato, Connection conexion, Statement sentencia) throws SQLException {
		
		boolean comprobar = true;
		
		if (Enunciado.tipoColumna.get(posicionDato).equalsIgnoreCase("INT UNSIGNED")) {
			
			try {
				
				int dato = Integer.parseInt(datoIntroducido);
				
				if (Enunciado.nombreColumna.get(posicionDato).equalsIgnoreCase("codigo")) {
					
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
		return comprobar;
	}
}

