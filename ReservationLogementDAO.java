package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ReservationLogementDAO {

    public boolean reserverLogementEnAttente(int utilisateurId, int logementId, Date dateDebut, Date dateFin) {
        String sql = "INSERT INTO reservation (utilisateur_id, logement_id, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, 'en attente')";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, logementId);
            stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
            stmt.setDate(4, new java.sql.Date(dateFin.getTime()));
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Erreur INSERT logement : " + e.getMessage());
            return false;
        }
    }

    public boolean confirmerReservationLogement(int utilisateurId, int logementId, Date dateDebut, Date dateFin) {
        String sql = "UPDATE reservation SET statut = 'confirmée' WHERE utilisateur_id = ? AND logement_id = ? AND date_debut = ? AND date_fin = ?";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            stmt.setInt(2, logementId);
            stmt.setDate(3, new java.sql.Date(dateDebut.getTime()));
            stmt.setDate(4, new java.sql.Date(dateFin.getTime()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur UPDATE logement : " + e.getMessage());
            return false;
        }
    }
    public void mettreAJourReservationsPassees() {
        String sql = "UPDATE reservation SET statut = 'passée' WHERE date_fin < CURRENT_DATE AND statut = 'confirmée'";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int count = stmt.executeUpdate();
            System.out.println("🛏️ Logements passés : " + count);
        } catch (SQLException e) {
            System.err.println("❌ Erreur update logement passé : " + e.getMessage());
        }
    }
    public static int countReservationsPassees(int utilisateurId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM reservation WHERE utilisateur_id = ? AND statut = 'passée'";
        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur comptage réservation logement : " + e.getMessage());
        }
        return count;
    }




}
