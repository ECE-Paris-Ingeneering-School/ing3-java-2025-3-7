package dao;

import modele.Chambre;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationChambreDAO {

    public boolean reserverChambresEnAttente(int utilisateurId, Chambre chambre, int nbChambres,
                                             java.util.Date dateDebut, java.util.Date dateFin) {
        if (chambre.getQuantiteDisponible() < nbChambres) return false;

        String sql = "INSERT INTO reservation_chambre (chambre_id, utilisateur_id, date_debut, date_fin, statut, prix_total) " +
                "VALUES (?, ?, ?, ?, 'en attente', ?)";

        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < nbChambres; i++) {
                stmt.setInt(1, chambre.getId());
                stmt.setInt(2, utilisateurId);
                stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
                stmt.setDate(4, new java.sql.Date(dateFin.getTime()));

                long jours = Math.max((dateFin.getTime() - dateDebut.getTime()) / (1000 * 60 * 60 * 24), 1);
                double prixTotal = chambre.getPrix() * jours;
                stmt.setDouble(5, prixTotal);

                stmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmerReservations(int utilisateurId, int chambreId, java.util.Date dateDebut, java.util.Date dateFin) {
        String sql = """
        UPDATE reservation_chambre
        SET statut = 'confirmée'
        WHERE utilisateur_id = ? AND chambre_id = ? 
          AND date_debut = ? AND date_fin = ?
          AND statut = 'en attente'
    """;

        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, chambreId);
            stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
            stmt.setDate(4, new java.sql.Date(dateFin.getTime()));

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la confirmation de paiement : " + e.getMessage());
            return false;
        }
    }
    public void mettreAJourReservationsPassees() {
        String sql = "UPDATE reservation_chambre SET statut = 'passée' WHERE date_fin < CURRENT_DATE AND statut = 'confirmée'";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int count = stmt.executeUpdate();
            System.out.println("🏨 Chambres passées : " + count);
        } catch (SQLException e) {
            System.err.println("❌ Erreur update chambre passée : " + e.getMessage());
        }
    }
    public static int countReservationsPassees(int utilisateurId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM reservation_chambre WHERE utilisateur_id = ? AND statut = 'passée'";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur comptage réservation chambre : " + e.getMessage());
        }
        return count;
    }




}
