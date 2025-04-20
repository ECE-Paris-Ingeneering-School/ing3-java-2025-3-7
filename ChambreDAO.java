package dao;

import modele.Chambre;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChambreDAO {

    public List<Chambre> getChambresDisponiblesPourLogement(int logementId, Date dateDebut, Date dateFin) {
        List<Chambre> chambres = new ArrayList<>();

        String sql = """
            SELECT c.*, 
                   (c.quantite - IFNULL(r.nb_reservations, 0)) AS quantite_disponible
            FROM chambre c
            LEFT JOIN (
                SELECT chambre_id, COUNT(*) AS nb_reservations
                FROM reservation_chambre
                WHERE (? < date_fin AND ? > date_debut)
                GROUP BY chambre_id
            ) r ON c.id_chambre = r.chambre_id
            WHERE c.logement_id = ?
        """;

        try (Connection conn = ConnexionBDD.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(dateDebut.getTime()));
            stmt.setDate(2, new java.sql.Date(dateFin.getTime()));
            stmt.setInt(3, logementId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Chambre chambre = new Chambre(
                        rs.getInt("id_chambre"),
                        rs.getInt("logement_id"),
                        rs.getString("nom_chambre"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("capacite"),
                        rs.getString("type_lit"),
                        rs.getInt("surface"),
                        rs.getInt("nb_lits"),
                        rs.getInt("nb_sdb"),
                        rs.getString("img"),
                        rs.getInt("quantite")
                );

                chambre.setQuantiteDisponible(rs.getInt("quantite_disponible"));
                chambres.add(chambre);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération chambres dispo : " + e.getMessage());
        }

        return chambres;
    }

    // Méthode de test simple
    public static void main(String[] args) {
        try {
            ChambreDAO dao = new ChambreDAO();
            // 🔁 Test sur logement ID 1, pour dates de test
            Date dateDebut = new Date(); // aujourd'hui
            Date dateFin = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000); // dans 3 jours

            List<Chambre> chambres = dao.getChambresDisponiblesPourLogement(1, dateDebut, dateFin);
            for (Chambre c : chambres) {
                System.out.println("🛏️ " + c.getNomChambre() +
                        " | Total: " + c.getQuantite() +
                        " | Dispo: " + c.getQuantiteDisponible());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
