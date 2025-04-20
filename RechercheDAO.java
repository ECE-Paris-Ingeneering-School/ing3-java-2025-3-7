package dao;

import modele.Logement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RechercheDAO {

    public List<Logement> rechercherLogementsAvance(
            Date dateDebut, Date dateFin,
            String pays, String region, String ville, String aeroport,
            int capacite, String prixMin, String prixMax,
            boolean wifi, boolean fumeur, boolean petitDej, boolean menage,
            boolean vueMer, boolean procheCentre,
            int nbChambres, int nbSallesDeBains,
            boolean parking, boolean animauxAdmis, boolean climatisation,
            String typeLogement, int surfaceM2, String typeDeLit, int nbEtages,
            boolean ascenseur, boolean piscinePrivee, boolean balconTerrasse, boolean accessibiliteHandicape,
            int evaluationMin
    ) {
        List<Logement> logements = new ArrayList<>();

        try (Connection conn = ConnexionBDD.getConnexion()) {

            // 👉 Ne filtre que les logements NON hôtels par indisponibilité
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM logement l WHERE " +
                            "(l.type_logement = 'Hôtel' OR l.id_logement NOT IN (" +
                            "SELECT r.logement_id FROM reservation r " +
                            "WHERE (? < r.date_fin AND ? > r.date_debut))) " +
                            "AND 1=1 "
            );

            // Filtres dynamiques
            if (!isEmpty(pays)) sql.append(" AND l.pays = ?");
            if (!isEmpty(region)) sql.append(" AND l.region = ?");
            if (!isEmpty(ville)) sql.append(" AND l.ville = ?");
            if (!isEmpty(aeroport)) sql.append(" AND l.aeroport_proche = ?");
            if (capacite > 0) sql.append(" AND l.capacite >= ?");
            if (!isEmpty(prixMin)) sql.append(" AND l.prix_par_nuit >= ?");
            if (!isEmpty(prixMax)) sql.append(" AND l.prix_par_nuit <= ?");
            if (wifi) sql.append(" AND l.wifi = true");
            if (fumeur) sql.append(" AND l.fumeur = true");
            if (petitDej) sql.append(" AND l.petit_dej = true");
            if (menage) sql.append(" AND l.menage = true");
            if (vueMer) sql.append(" AND l.vue_mer = true");
            if (procheCentre) sql.append(" AND l.proche_centre_ville = true");
            if (nbChambres > 0) sql.append(" AND l.nb_chambres >= ?");
            if (nbSallesDeBains > 0) sql.append(" AND l.nb_salles_de_bains >= ?");
            if (parking) sql.append(" AND l.parking = true");
            if (animauxAdmis) sql.append(" AND l.animaux_admis = true");
            if (climatisation) sql.append(" AND l.climatisation = true");
            if (!isEmpty(typeLogement)) sql.append(" AND l.type_logement = ?");
            if (surfaceM2 > 0) sql.append(" AND l.surface_m2 >= ?");
            if (!isEmpty(typeDeLit)) sql.append(" AND l.type_de_lit = ?");
            if (nbEtages > 0) sql.append(" AND l.nb_etages >= ?");
            if (ascenseur) sql.append(" AND l.ascenseur = true");
            if (piscinePrivee) sql.append(" AND l.piscine_privee = true");
            if (balconTerrasse) sql.append(" AND l.balcon_terrasse = true");
            if (accessibiliteHandicape) sql.append(" AND l.accessibilite_handicape = true");

            sql.append(" AND l.evaluation >= ?");

            PreparedStatement stmt = conn.prepareStatement(sql.toString());

            // Binding
            int i = 1;
            stmt.setDate(i++, new java.sql.Date(dateDebut.getTime()));
            stmt.setDate(i++, new java.sql.Date(dateFin.getTime()));

            if (!isEmpty(pays)) stmt.setString(i++, pays);
            if (!isEmpty(region)) stmt.setString(i++, region);
            if (!isEmpty(ville)) stmt.setString(i++, ville);
            if (!isEmpty(aeroport)) stmt.setString(i++, aeroport);
            if (capacite > 0) stmt.setInt(i++, capacite);
            if (!isEmpty(prixMin)) stmt.setDouble(i++, Double.parseDouble(prixMin));
            if (!isEmpty(prixMax)) stmt.setDouble(i++, Double.parseDouble(prixMax));
            if (nbChambres > 0) stmt.setInt(i++, nbChambres);
            if (nbSallesDeBains > 0) stmt.setInt(i++, nbSallesDeBains);
            if (!isEmpty(typeLogement)) stmt.setString(i++, typeLogement);
            if (surfaceM2 > 0) stmt.setInt(i++, surfaceM2);
            if (!isEmpty(typeDeLit)) stmt.setString(i++, typeDeLit);
            if (nbEtages > 0) stmt.setInt(i++, nbEtages);

            stmt.setInt(i++, evaluationMin);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logements.add(new Logement(
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
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur RechercheDAO : " + e.getMessage());
        }

        return logements;
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
