package dao;

import modele.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    public static Utilisateur seConnecter(String mail, String motDePasse) {
        Utilisateur utilisateur = null;

        try {
            Connection conn = ConnexionBDD.getConnexion();
            String sql = "SELECT * FROM users WHERE mail = ? AND mot_de_passe = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mail);
            stmt.setString(2, motDePasse);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_users");
                String type = rs.getString("type");
                String statut = rs.getString("statut");

                utilisateur = new Utilisateur(mail, motDePasse, type, statut);
                utilisateur.setId(id);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return utilisateur;
    }

    public static boolean creerCompte(Utilisateur utilisateur) {
        boolean success = false;

        try {
            Connection conn = ConnexionBDD.getConnexion();
            String sql = "INSERT INTO users (mail, mot_de_passe, type, statut) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, utilisateur.getMail());
            stmt.setString(2, utilisateur.getMotDePasse());
            stmt.setString(3, utilisateur.getType());
            stmt.setString(4, utilisateur.getStatut());

            int lignes = stmt.executeUpdate();
            success = (lignes > 0);

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
    public void mettreAJourStatutAncienClients() {
        String sql = "UPDATE users SET statut = 'ancien' WHERE id_users IN (" +
                "SELECT DISTINCT utilisateur_id FROM reservation WHERE statut = 'passée' " +
                "UNION " +
                "SELECT DISTINCT utilisateur_id FROM reservation_chambre WHERE statut = 'passée'" +
                ")";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int updated = stmt.executeUpdate();
            System.out.println("👴 Clients devenus anciens : " + updated);
        } catch (SQLException e) {
            System.err.println("❌ Erreur mise à jour clients anciens : " + e.getMessage());
        }
    }


}
