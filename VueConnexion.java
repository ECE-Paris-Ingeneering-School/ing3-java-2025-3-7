package vue;

import controleur.ControleurMiseAJour;
import controleur.ControleurRecherche;
import dao.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class  VueConnexion extends JFrame {
    private JTextField champMail;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private JButton boutonCreerCompte;

    public VueConnexion() {
        setTitle("Connexion");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel labelMail = new JLabel("Adresse mail :");
        champMail = new JTextField();

        JLabel labelMotDePasse = new JLabel("Mot de passe :");
        champMotDePasse = new JPasswordField();

        boutonConnexion = new JButton("Se connecter");
        boutonCreerCompte = new JButton("Créer un compte");

        panel.add(labelMail);
        panel.add(champMail);
        panel.add(labelMotDePasse);
        panel.add(champMotDePasse);
        panel.add(boutonConnexion);
        panel.add(boutonCreerCompte);

        add(panel);

        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mail = champMail.getText();
                String motDePasse = new String(champMotDePasse.getPassword());

                Utilisateur utilisateur = UtilisateurDAO.seConnecter(mail, motDePasse);

                if (utilisateur != null) {
                    JOptionPane.showMessageDialog(null, "Connexion réussie en tant que " + utilisateur.getType());


                    dispose();
                    new Accueil2(utilisateur).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Identifiants incorrects !");
                }
            }
        });

        boutonCreerCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherBoiteDeDialogueCreationCompte();
            }
        });
    }

    
    private void afficherBoiteDeDialogueCreationCompte() {
        JTextField mailField = new JTextField();
        JPasswordField motDePasseField = new JPasswordField();
        String[] types = {"client", "admin"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Email :"));
        panel.add(mailField);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(motDePasseField);
        panel.add(new JLabel("Type :"));
        panel.add(typeBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Créer un compte", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String mail = mailField.getText();
            String motDePasse = new String(motDePasseField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            Utilisateur nouvelUtilisateur = new Utilisateur(mail, motDePasse, type, "nouveau");

            boolean success = UtilisateurDAO.creerCompte(nouvelUtilisateur);

            if (success) {
                JOptionPane.showMessageDialog(null, "Compte créé avec succès !");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur : compte non créé (email déjà utilisé ?)");
            }
        }
    }

    public static void main(String[] args) {
        ControleurMiseAJour.lancerMisesAJour(); // 🔄 Appel auto à chaque démarrage
        SwingUtilities.invokeLater(() -> new VueConnexion().setVisible(true));
    }
}
