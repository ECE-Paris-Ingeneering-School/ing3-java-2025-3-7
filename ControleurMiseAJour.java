package controleur;

import dao.ReservationChambreDAO;
import dao.ReservationLogementDAO;
import dao.UtilisateurDAO;

/**
 * Ce contrôleur s’occupe de synchroniser les statuts :
 * - passe les réservations passées en "passée"
 * - met à jour le statut des utilisateurs : "ancien" si au moins une résa passée
 */
public class ControleurMiseAJour {

    /**
     * 🔄 Exécute toutes les mises à jour automatiques
     */
    public static void lancerMisesAJour() {
        System.out.println("🕒 [SYNC] Début des mises à jour...");

        ReservationChambreDAO daoChambre = new ReservationChambreDAO();
        daoChambre.mettreAJourReservationsPassees();

        ReservationLogementDAO daoLogement = new ReservationLogementDAO();
        daoLogement.mettreAJourReservationsPassees();

        UtilisateurDAO daoUtilisateur = new UtilisateurDAO();
        daoUtilisateur.mettreAJourStatutAncienClients();

        System.out.println("✅ [SYNC] Mises à jour terminées.");
    }
}
