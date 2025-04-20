import controleur.ControleurMiseAJour;
import vue.VueAccueil;
import vue.VueConnexion;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lancement de la fenêtre de connexion sans paramètre
        ControleurMiseAJour.lancerMisesAJour(); // 🔄 Appel auto à chaque démarrage
        SwingUtilities.invokeLater(() -> new VueAccueil().setVisible(true));


    }
}
