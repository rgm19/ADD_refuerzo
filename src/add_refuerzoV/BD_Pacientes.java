/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package add_refuerzoV;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author usuario5
 */
public class BD_Pacientes {
    
    static Scanner teclado = new Scanner(System.in);
    
    public static void main(String[]args) throws ClassNotFoundException, SQLException{
        
       Class.forName("com.mysql.jdbc.Driver");
        
        while(true){
            menu();
        }
    }

    private static void menu() throws SQLException {
        int op=0;
        System.out.println("1- Insertar, modificar o borrar datos de un paciente");
        System.out.println("2- Listado de pacientes");
        System.out.println("3- Datos de paciente hospitalizado anteriormente");
        System.out.println("4- Informacion BD");
        System.out.println("5º Numero de pacientes que hay por cada año");
        System.out.println("0- Salir");
        System.out.println("---------------------------------------------------");
        
        
        
        
        
        System.out.print("Opcion -> ");
        op=elecInt();
        
        
        
        switch(op){
            case 1:
                menuPacientes();
                break;
                
            case 2:
                listadoPacientes();
                break;
                
            case 3:
                datosPacientes();
                break;
                
            case 4:
                infoBD();
                break;
                
            case 5:
                registroanios();
                break;
                
            case 0:
                System.out.println("Cerrando Programa...");
                System.exit(0);
                break;
                
            default:
                System.out.println("Introduce una opcion valida!");
                break;
                
        }
    }
    //--------------------------------------------------------------------------
    private static void menuPacientes() throws SQLException {
        int op=0;
        System.out.println("1º Insertar datos de un paciente");
        System.out.println("2º Modificar datos de un paciente");
        System.out.println("3º Borrar datos de un paciente");
        System.out.println("0º Volver al menu anterior");
        System.out.println("---------------------------------------------------");
        
        System.out.print("Opcion -> ");
        op=elecInt();
        
        
        switch(op){
            case 1:
                insertarPaciente();
                break;
                
            case 2:
                modificarPaciente();
                break;
                
            case 3:
                BorrarPaciente();
                break;
                  
            case 0:
                System.out.println();
                break;
                
            default:
                System.out.println("Introduce una opcion valida!");
                break;
        }
    }
    //--------------------------------------------------------------------------
    public static int elecInt() {
         
        int aux=0;
        boolean control=true;
        
        do{       
                try{
                    aux= teclado.nextInt();
                    control=true;

                }catch(Exception e){
                    control=false;
                    System.out.println("Introduce un numero!");
                    teclado.nextLine();
                }  
        }while(control!=true);
      return aux;
    }
//------------------------------------------------------------------------------
    private static void insertarPaciente() {
           
        try(            
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");
            //Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","usuario");
            Statement sentencia = (Statement) conexion.createStatement();
            ResultSet resul = sentencia.executeQuery("select* from Pacientes");
            ){         
            System.out.print("ID:");
            int id=elecInt();
            System.out.println();
            teclado.nextLine();
            
            System.out.print("Nombre: ");
            String nombre=teclado.nextLine();
            System.out.println();
            
            System.out.print("Apellidos:");
            String apellidos=teclado.nextLine();
            System.out.println();
            
            
            System.out.print("DNI:");
            String dni=teclado.nextLine();
            System.out.println();
            
            String sql = "{call ingresos('"+id+"','"+nombre+"','"+apellidos+"','"+dni+"')}";
            CallableStatement llamada = conexion.prepareCall(sql);
            int filas  = llamada.executeUpdate();
            
            
            boolean bandera = false;
            while(resul.next()){
                
                if(dni.equalsIgnoreCase(resul.getString(5))){
                    bandera=true;
                }else{
                    bandera=false;
                }
            }
            
            if(bandera==true){
                System.out.println("El Paciente "+nombre+" "+apellidos+" con DNI "+dni+" , tiene una nueva entrada en su registro");
            }else{
                System.out.println("Se ha creado el registro del Paciente "+nombre+" "+apellidos+" con DNI "+dni);
            }
            
            
            //String insert="INSERT INTO Pacientes VALUES ('"+id+"','"+nombre+"','"+apellidos+"','"+fecha_baja+"','"+dni+"','"+fecha_ing+"');");     
           // int filas = sentencia.executeUpdate(insert);
           // System.out.println("Filas Modificas = "+filas);     
        }
        catch(Exception ex)
        {
            System.out.println("Error: "+ex.getMessage());
        }
    }
//------------------------------------------------------------------------------
    private static void modificarPaciente() {
        try(            
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");            
            Statement sentencia = (Statement) conexion.createStatement();
            ){ 
            
            System.out.println("¿Que paciente vas a modificar? Escribe su ID");
            int ID = teclado.nextInt();
            teclado.nextLine();
            
            System.out.println("¿Que campo deseas cambiar? ID, Nombre, Apellidos, Fecha");
            String campo = teclado.nextLine();
            
            System.out.println("Escribe el nuevo dato");
            String datoNuevo = teclado.nextLine();
            
            String modificarRegistroPaciente = "UPDATE Pacientes set "+ campo +" = '"+datoNuevo+"' where id= "+ID+";";
            int filasModificadas = sentencia.executeUpdate(modificarRegistroPaciente);
            System.out.println ("Filas modifidas: " + filasModificadas);
               
        }
        catch(Exception ex)
        {
            System.out.println("Error: "+ex.getMessage());
        }
    }
//------------------------------------------------------------------------------
    private static void BorrarPaciente() throws SQLException {
         try(            
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");            
            Statement sentencia = (Statement) conexion.createStatement();
            ){ 
            
            System.out.println("¿Que paciente vas a borrar? Escribe su ID");
            int ID = teclado.nextInt();
            teclado.nextLine();
            
            String borrarRegistroPaciente = "DELETE from Pacientes where id= "+ID+";";
            int filasModificadas = sentencia.executeUpdate(borrarRegistroPaciente);
            System.out.println ("Filas Borradas: " + filasModificadas);
               
        }
    }
//------------------------------------------------------------------------------
    private static void listadoPacientes() throws SQLException {
        try(            
            //Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","usuario");
            Statement sentencia = (Statement) conexion.createStatement();
            ){ 
            ResultSet resul =null;
            String listado = "Select * from Pacientes where year (fecha_alta)='2017';";
            //String listado = "Select * from Pacientes where fecha_alta='current_date()';";
            resul=sentencia.executeQuery(listado);
            
            while(resul.next()){
                System.out.println("ID: "+resul.getInt(1));
                System.out.println("Nombre: "+resul.getString(2));
                System.out.println("Apellidos: "+resul.getString(3));
                System.out.println("Fecha de Alta: "+resul.getDate(4));
                System.out.println("DNI: "+resul.getString(5));
            }
               
        }catch(Exception e){}
    }
//------------------------------------------------------------------------------    
    private static void datosPacientes() {
       try(
           Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");            
           Statement sentencia = (Statement) conexion.createStatement();  
           ){
           teclado.nextLine();
           System.out.println("¿Sobre que paciente deseas ver los datos? Escribe su DNI");
           String dni = teclado.nextLine();
           
           ResultSet resul =null;
           String sql ="Select nombre,apellidos, count(*) as veces from Pacientes where dni='"+dni+"' group by nombre, apellidos;";
           resul = sentencia.executeQuery(sql);
           
           
           //****No muestra los datos****
           while(resul.next()){
                System.out.println("ID: "+resul.getInt(1));
                System.out.println("Nombre: "+resul.getString(2));
                System.out.println("Apellidos: "+resul.getString(3));
                System.out.println("Fecha de Alta: "+resul.getDate(4));
                System.out.println("DNI: "+resul.getString(5));
                
           }
       }catch(Exception e){}
    }
//------------------------------------------------------------------------------    

    private static void infoBD() {
        try(
                Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");
                Statement sentencia = (Statement) conexion.createStatement();     
            ){
            
            DatabaseMetaData dbmd = conexion.getMetaData();
            String name = dbmd.getDatabaseProductName();
            String version = dbmd.getDatabaseProductVersion();
            String driver = dbmd.getDriverName();
            String nameDV = dbmd.getDriverVersion();
            
            System.out.println("Nombre: "+ name);
            System.out.println("Version: "+ version);
            System.out.println("Driver: "+ driver);
            System.out.println("Driver Version: "+nameDV);
            
            System.out.println("*****************************");
            System.out.println("Informacion de la tabla Pacientes ");
            System.out.println(dbmd.getColumnPrivileges(null, null, "Pacientes", null));
            
        }catch(Exception e){}
    } 
//------------------------------------------------------------------------------    

    private static void registroanios() throws SQLException {
    
        try(  
                //Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","root");
                Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/HOSPITAL","root","usuario");
                ){
            
               String sql = "{call listado_anios() }";
               CallableStatement llamada = conexion.prepareCall(sql);
               ResultSet resul = llamada.executeQuery();
               
               while(resul.next()){
                   
                    System.out.println("Fecha de Alta: "+resul.getString(1));
                    System.out.println("Pacientes Ingresados: "+resul.getInt(2));
                    
                    
               }
               
        }catch(Exception e){}
        

    }
//------------------------------------------------------------------------------






}//final de la clase
