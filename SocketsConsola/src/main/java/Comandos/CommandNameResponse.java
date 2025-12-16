// Clase: CommandNameResponse.java

package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;
import com.mycompany.socketsconsola.Juego;
import java.awt.CardLayout;
import javax.swing.JOptionPane;

public class CommandNameResponse extends Command {

    // args[0] será "true" o "false"
    // args[1] será el mensaje de estado
    public CommandNameResponse(String[] args) {
        super(CommandType.NAME_RESPONSE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // Este comando solo se procesa en el cliente
    }

    @Override
    public void processInClient(Client client) {
        // Aquí el cliente revisa la respuesta antes de pasar a la interfaz de juego
        
        String estado = this.getParameters()[0];
        String mensaje = this.getParameters()[1];
        Juego refFrame = client.getRefFrame();
        
        if (Boolean.parseBoolean(estado)) {
            if (refFrame != null) {
                    // Accede al CardLayout para cambiar la vista
                    CardLayout cardLayout = (CardLayout) (refFrame.getContentPane().getLayout());
                    cardLayout.show(refFrame.getContentPane(), "card6");
                }
        } else {
            if (refFrame != null) {
                JOptionPane.showMessageDialog(
                    refFrame, 
                    "Error de Conexión: " + mensaje + "\nPor favor, intenta con otro nombre.",
                    "Nombre Denegado", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
            client.disconnect();
        }
    }
}