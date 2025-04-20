package vue;

import dao.ChambreDAO;
import modele.Chambre;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class TestDisponibiliteChambre {

    public static void main(String[] args) {

        try {
            ChambreDAO dao = new ChambreDAO();

            // 💡 Définir les dates à tester (ajuste selon ta BDD)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebut = sdf.parse("2025-04-19");
            Date dateFin = sdf.parse("2025-04-21");

            int logementId = 1; // Hôtel de test dans ta BDD

            List<Chambre> chambres = dao.getChambresDisponiblesPourLogement(logementId, dateDebut, dateFin);

            System.out.println("🔍 Résultats pour les chambres du logement ID = " + logementId);
            System.out.println("📅 Période : du " + sdf.format(dateDebut) + " au " + sdf.format(dateFin));
            System.out.println("----------------------------------------------------");

            for (Chambre c : chambres) {
                System.out.println("🛏️ Chambre: " + c.getNomChambre());
                System.out.println("   Quantité totale      : " + c.getQuantite());
                System.out.println("   Quantité disponible  : " + c.getQuantiteDisponible());
                System.out.println("   Prix / nuit          : " + c.getPrix() + " €");
                System.out.println("----------------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
