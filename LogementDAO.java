package dao;

import modele.Logement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogementDAO {

    public static List<Logement> getTousLesLogements(
            String pays, String region, String ville, String aeroport, int capacite, String prixMin,
            String prixMax, boolean wifi, boolean fumeur, boolean petitDej, boolean menage,
            boolean vueMer, boolean procheCentre, int nbChambres, int nbSallesDeBains,
            boolean parking, boolean animauxAdmis, boolean climatisation, String typeLogement,
            int surfaceM2, String typeDeLit, int nbEtages, boolean ascenseur,
            boolean piscinePrivee, boolean balconTerrasse, boolean accessibiliteHandicape,
            int evaluationMin
    ) {
        List<Logement> logements = new ArrayList<>();
        try (Connection conn = ConnexionBDD.getConnexion()) {
            String sql = "SELECT * FROM logement WHERE 1=1";

            if (pays != null && !pays.isEmpty()) sql += " AND pays = ?";
            if (region != null && !region.isEmpty()) sql += " AND region = ?";
            if (ville != null && !ville.isEmpty()) sql += " AND ville = ?";
            if (aeroport != null && !aeroport.isEmpty()) sql += " AND aeroport_proche = ?";
            if (capacite > 0) sql += " AND capacite >= ?";
            if (!prixMin.isEmpty()) sql += " AND prix_par_nuit >= ?";
            if (!prixMax.isEmpty()) sql += " AND prix_par_nuit <= ?";
            if (wifi) sql += " AND wifi = ?";
            if (fumeur) sql += " AND fumeur = ?";
            if (petitDej) sql += " AND petit_dej = ?";
            if (menage) sql += " AND menage = ?";
            if (vueMer) sql += " AND vue_mer = ?";
            if (procheCentre) sql += " AND proche_centre_ville = ?";
            if (nbChambres > 0) sql += " AND nb_chambres >= ?";
            if (nbSallesDeBains > 0) sql += " AND nb_salles_de_bains >= ?";
            if (parking) sql += " AND parking = ?";
            if (animauxAdmis) sql += " AND animaux_admis = ?";
            if (climatisation) sql += " AND climatisation = ?";
            if (typeLogement != null && !typeLogement.isEmpty()) sql += " AND type_logement = ?";
            if (surfaceM2 > 0) sql += " AND surface_m2 >= ?";
            if (typeDeLit != null && !typeDeLit.isEmpty()) sql += " AND type_de_lit = ?";
            if (nbEtages > 0) sql += " AND nb_etages >= ?";
            if (ascenseur) sql += " AND ascenseur = ?";
            if (piscinePrivee) sql += " AND piscine_privee = ?";
            if (balconTerrasse) sql += " AND balcon_terrasse = ?";
            if (accessibiliteHandicape) sql += " AND accessibilite_handicape = ?";
            sql += " AND evaluation >= ?"; // ⭐ toujours présent

            // Debug : affiche la requête finale
            System.out.println("[DEBUG SQL] Requête générée : " + sql);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int i = 1;
                if (pays != null && !pays.isEmpty()) stmt.setString(i++, pays);
                if (region != null && !region.isEmpty()) stmt.setString(i++, region);
                if (ville != null && !ville.isEmpty()) stmt.setString(i++, ville);
                if (aeroport != null && !aeroport.isEmpty()) stmt.setString(i++, aeroport);
                if (capacite > 0) stmt.setInt(i++, capacite);
                if (!prixMin.isEmpty()) stmt.setDouble(i++, Double.parseDouble(prixMin));
                if (!prixMax.isEmpty()) stmt.setDouble(i++, Double.parseDouble(prixMax));
                if (wifi) stmt.setBoolean(i++, true);
                if (fumeur) stmt.setBoolean(i++, true);
                if (petitDej) stmt.setBoolean(i++, true);
                if (menage) stmt.setBoolean(i++, true);
                if (vueMer) stmt.setBoolean(i++, true);
                if (procheCentre) stmt.setBoolean(i++, true);
                if (nbChambres > 0) stmt.setInt(i++, nbChambres);
                if (nbSallesDeBains > 0) stmt.setInt(i++, nbSallesDeBains);
                if (parking) stmt.setBoolean(i++, true);
                if (animauxAdmis) stmt.setBoolean(i++, true);
                if (climatisation) stmt.setBoolean(i++, true);
                if (typeLogement != null && !typeLogement.isEmpty()) stmt.setString(i++, typeLogement);
                if (surfaceM2 > 0) stmt.setInt(i++, surfaceM2);
                if (typeDeLit != null && !typeDeLit.isEmpty()) stmt.setString(i++, typeDeLit);
                if (nbEtages > 0) stmt.setInt(i++, nbEtages);
                if (ascenseur) stmt.setBoolean(i++, true);
                if (piscinePrivee) stmt.setBoolean(i++, true);
                if (balconTerrasse) stmt.setBoolean(i++, true);
                if (accessibiliteHandicape) stmt.setBoolean(i++, true);

                stmt.setInt(i++, evaluationMin); // ⭐ toujours à la fin

                System.out.println("[DEBUG SQL] Évaluation minimum : " + evaluationMin);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Logement logement = new Logement(
                                rs.getInt("id_logement"),
                                rs.getString("titre"),
                                rs.getString("description"),
                                rs.getString("adresse"),
                                rs.getString("pays"),
                                rs.getString("region"),
                                rs.getString("ville"),
                                rs.getDouble("prix_par_nuit"),
                                rs.getInt("offre"),
                                rs.getInt("capacite"),
                                rs.getString("img"),
                                rs.getBoolean("wifi"),
                                rs.getBoolean("fumeur"),
                                rs.getBoolean("petit_dej"),
                                rs.getBoolean("menage"),
                                rs.getString("aeroport_proche"),
                                rs.getBoolean("vue_mer"),
                                rs.getBoolean("proche_centre_ville"),
                                rs.getInt("nb_chambres"),
                                rs.getInt("nb_salles_de_bains"),
                                rs.getBoolean("parking"),
                                rs.getBoolean("animaux_admis"),
                                rs.getBoolean("climatisation"),
                                rs.getString("type_logement"),
                                rs.getInt("surface_m2"),
                                rs.getString("type_de_lit"),
                                rs.getInt("nb_etages"),
                                rs.getBoolean("ascenseur"),
                                rs.getBoolean("piscine_privee"),
                                rs.getBoolean("balcon_terrasse"),
                                rs.getBoolean("accessibilite_handicape"),
                                rs.getInt("evaluation")
                        );
                        logements.add(logement);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logements;
    }
}
