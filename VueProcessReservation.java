package vue;

import dao.ReservationChambreDAO;
import dao.ReservationLogementDAO;
import modele.Chambre;
import modele.Logement;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VueProcessReservation extends JFrame {

    public VueProcessReservation(Utilisateur utilisateur, Chambre chambre, int quantite, Date dateDebut, Date dateFin) {
        this(utilisateur, chambre, quantite, dateDebut, dateFin, null);
    }

    public VueProcessReservation(Utilisateur utilisateur, Chambre chambre, int quantite, Date dateDebut, Date dateFin, Logement logement) {
        setTitle("Confirmation de réservation");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        panel.add(new JLabel("👤 Merci " + utilisateur.getMail()));
        panel.add(new JLabel("Votre réservation a été enregistrée en attente de paiement."));
        panel.add(Box.createVerticalStrut(15));

        if (chambre != null) {
            // 🛏 Réservation d’une chambre
            panel.add(new JLabel("🏨 Chambre : " + chambre.getNomChambre()));
            panel.add(new JLabel("🔢 Quantité : " + quantite));
        } else if (logement != null) {
            // 🏡 Réservation d’un logement
            panel.add(new JLabel("🏡 Logement : " + logement.getTitre()));
            panel.add(new JLabel("👥 Capacité : " + logement.getCapacite() + " personnes"));
        }

        panel.add(new JLabel("📅 Du " + sdf.format(dateDebut) + " au " + sdf.format(dateFin)));
        panel.add(Box.createVerticalStrut(30));

        JButton payerBtn = new JButton("💳 Valider le paiement");
        payerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        payerBtn.addActionListener(e -> {
            new VueAchatReservation(utilisateur, chambre, quantite, dateDebut, dateFin, logement).setVisible(true);
            dispose();
        });


        JButton retourBtn = new JButton("Retour à la recherche");
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.addActionListener(e -> {
            dispose();
            new VueRechercheBis(utilisateur, "", new Date(), new Date(), 1, 1, null).setVisible(true);
        });

        panel.add(payerBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(retourBtn);

        add(panel);
    }
}
