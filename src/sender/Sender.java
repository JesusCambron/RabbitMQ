/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sender;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import persona.Persona;
/**
 *
 * @author pc
 */
public class Sender {
    
    private static String NOMBRE_DE_COLA = "ColaRabbitMQ";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        
        Persona persona = new Persona("persona1","Jesus","Cambron",21);
        
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();
            channel.queueDeclare(NOMBRE_DE_COLA, false, false, false, null);
            
            String message = persona.getId()+","+persona.getNombre()+","+persona.getApellido()+","+persona.getEdad();
            channel.basicPublish("", NOMBRE_DE_COLA, null, message.getBytes());
            System.out.println("[Sender]" + message);
        }
    }

}
