/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import control.PersonaJpaController;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import persona.Persona;

/**
 *
 * @author pc
 */
public class Consumer {
private static String NOMBRE_DE_COLA = "ColaRabbitMQ";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("-----CONSUMER-----");
        ConnectionFactory factory = new ConnectionFactory();
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(NOMBRE_DE_COLA, false, false, false, null);
        
        System.out.println("[Consumer] Waiting for messages.....");

        channel.basicConsume(NOMBRE_DE_COLA, true,(consumerTag, message)-> {
            String m = new String(message.getBody(),"UTF-8");
            String[] mensaje = leerMensaje(m);
            try {
                guardarPersona(crearPersona(mensaje));
            } catch (Exception ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, consumerTag->{});
    }
    
    public static String[] leerMensaje(String mensaje) {
        System.out.println("Recibido:");
        System.out.println("-------------------------------");
        String[] m = mensaje.split(",");
        System.out.println(m[0]);
        System.out.println(m[1]);
        System.out.println(m[2]);
        System.out.println(m[3]);
        System.out.println("-------------------------------");
        return m;
    }
    
    public static Persona crearPersona(String[] m) {
        Persona persona = new Persona(m[0], m[1], m[2], Integer.parseInt(m[3]));
        return persona;
    }
    
    public static void guardarPersona(Persona persona) throws Exception{
        PersonaJpaController pjc = new PersonaJpaController();
        pjc.create(persona);
    }

}
