// VueRechercheBis.java - version finale avec filtres fonctionnels et layout propre
package vue;

import dao.ChambreDAO;
import dao.RechercheDAO;
import dao.ReservationChambreDAO;
import dao.ReservationLogementDAO;
import modele.Chambre;
import modele.Logement;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VueRechercheBis extends JFrame {
    private Utilisateur utilisateur;

    private JTextField destinationField;
    private JSpinner arrivalSpinner;
    private JSpinner departureSpinner;
    private JLabel peopleLabel;
    private JLabel roomsLabel;
    private JPanel panelResultats;

    // Filtres avancés
    private JComboBox<String> paysBox;
    private JTextField regionField, villeField, aeroportField, prixMinField, prixMaxField;
    private JSpinner capaciteSpinner, nbChambresSpinner, nbSallesDeBainsSpinner;
    private JComboBox<Integer> evaluationCombo;
    private JCheckBox vueMerCheck, procheCentreCheck;
    // Nouveaux champs à déclarer (si pas déjà)
    private JCheckBox parkingCheck, animauxCheck, climCheck, ascenseurCheck, piscineCheck, balconCheck, accessCheck;
    private JTextField typeLogementField, typeLitField;
    private JSpinner surfaceSpinner, etagesSpinner;
    private JCheckBox wifiCheck, fumeurCheck, petitDejCheck, menageCheck;
    private JPanel topPanelRecherche;




    public VueRechercheBis(Utilisateur utilisateur, String ville, Date dateDebut, Date dateFin, int nbChambres, int nbAdultes, String typeLogementPrefere) {
        this.utilisateur = utilisateur;

        setTitle("Recherche de logements - " + utilisateur.getType());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔧 On construit une seule fois la barre de recherche, et on la réutilise !
        topPanelRecherche = creerBarreRecherche(ville, dateDebut, dateFin, nbChambres, nbAdultes, typeLogementPrefere);

        // ⬆️ Header complet = barre logo + barre recherche
        add(creerHeaderBar(), BorderLayout.NORTH);

        setJMenuBar(creerMenuBar());

        JPanel centrePanel = new JPanel(new BorderLayout());

        JScrollPane filtreScroll = new JScrollPane(creerPanelFiltres());
        filtreScroll.setPreferredSize(new Dimension(300, 0));
        filtreScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelResultats = new JPanel();
        panelResultats.setLayout(new BoxLayout(panelResultats, BoxLayout.Y_AXIS));
        panelResultats.setBackground(Color.WHITE);

        JScrollPane resultScroll = new JScrollPane(panelResultats);
        resultScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        centrePanel.add(filtreScroll, BorderLayout.WEST);
        centrePanel.add(resultScroll, BorderLayout.CENTER);

        add(centrePanel, BorderLayout.CENTER);

        executerRechercheAvance(); // ✅ lancement auto
    }



    private JComboBox<String> categorieCombo; // ➕ Ajout du champ global

    private JPanel creerBarreRecherche(String ville, Date dateDebut, Date dateFin, int nbChambres, int nbAdultes, String typeLogementPrefere) {
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(1200, 180));
        top.setBackground(new Color(0, 123, 255));

        JLabel titre = new JLabel("Trouvez votre prochain séjour");
        titre.setFont(new Font("Arial", Font.BOLD, 26));
        titre.setForeground(Color.WHITE);
        titre.setBounds(250, 10, 500, 30);
        top.add(titre);

        destinationField = new JTextField(ville.isBlank() ? "Où allez-vous ?" : ville);
        destinationField.setFont(new Font("Arial", Font.PLAIN, 16));
        destinationField.setBounds(50, 60, 200, 40);
        destinationField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (destinationField.getText().equals("Où allez-vous ?")) destinationField.setText("");
            }

            public void focusLost(FocusEvent e) {
                if (destinationField.getText().isEmpty()) destinationField.setText("Où allez-vous ?");
            }
        });
        top.add(destinationField);

        // ➕ Combo catégorie
        categorieCombo = new JComboBox<>(new String[]{"Tous", "Hôtel", "Appartement", "Villa"});
        categorieCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categorieCombo.setBounds(50, 110, 200, 30);

        // ✅ Appliquer la sélection automatique
        if (typeLogementPrefere != null && !typeLogementPrefere.isBlank()) {
            categorieCombo.setSelectedItem(typeLogementPrefere);
        }

        categorieCombo.addActionListener(e -> executerRechercheAvance());
        top.add(categorieCombo);

        // 📆 Dates
        arrivalSpinner = createDateSpinner(270, 60, dateDebut, "Date de départ", top);
        departureSpinner = createDateSpinner(470, 60, dateFin, "Date d'arrivée", top);

        // 👤👤
        peopleLabel = new JLabel("Adultes: " + nbAdultes);
        JPanel peoplePanel = createCounterPanel(peopleLabel, "Adultes", 670, 60);
        top.add(peoplePanel);

        roomsLabel = new JLabel("Chambres: " + nbChambres);
        JPanel roomsPanel = createCounterPanel(roomsLabel, "Chambres", 670, 110);
        top.add(roomsPanel);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBounds(370, 120, 150, 40);
        searchButton.addActionListener(e -> executerRechercheAvance());
        top.add(searchButton);

        return top;
    }


    private JPanel creerPanelFiltres() {
        JPanel panelFiltres = new JPanel();
        panelFiltres.setLayout(new BoxLayout(panelFiltres, BoxLayout.Y_AXIS));
        panelFiltres.setBackground(new Color(240, 240, 250));
        panelFiltres.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Initialisation des composants ===
        paysBox = new JComboBox<>(new String[]{"", "France", "Espagne", "Italie", "Suisse"});
        regionField = new JTextField();
        villeField = new JTextField();
        aeroportField = new JTextField();
        prixMinField = new JTextField();
        prixMaxField = new JTextField();
        capaciteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        nbChambresSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        nbSallesDeBainsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        evaluationCombo = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});

        vueMerCheck = new JCheckBox("🌊 Vue mer");
        procheCentreCheck = new JCheckBox("🏙️ Centre-ville");

        parkingCheck = new JCheckBox("🚗 Parking");
        animauxCheck = new JCheckBox("🐶 Animaux admis");
        climCheck = new JCheckBox("❄️ Climatisation");
        ascenseurCheck = new JCheckBox("🛗 Ascenseur");
        piscineCheck = new JCheckBox("🏊 Piscine privée");
        balconCheck = new JCheckBox("🪟 Balcon / Terrasse");
        accessCheck = new JCheckBox("♿ Accessible PMR");

        wifiCheck = new JCheckBox("📶 Wifi");
        fumeurCheck = new JCheckBox("🚬 Fumeur");
        petitDejCheck = new JCheckBox("🍳 Petit déjeuner inclus");
        menageCheck = new JCheckBox("🧼 Ménage inclus");

        typeLogementField = new JTextField();
        typeLitField = new JTextField();
        surfaceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        etagesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));

        // === Ajout des composants visuellement ===
        addChamp(panelFiltres, "Pays :", paysBox);
        addChamp(panelFiltres, "Région :", regionField);
        addChamp(panelFiltres, "Ville :", villeField);
        addChamp(panelFiltres, "Aéroport :", aeroportField);
        addChamp(panelFiltres, "Prix min (€) :", prixMinField);
        addChamp(panelFiltres, "Prix max (€) :", prixMaxField);
        addChamp(panelFiltres, "Nombre de personnes :", capaciteSpinner);
        addChamp(panelFiltres, "Chambres :", nbChambresSpinner);
        addChamp(panelFiltres, "Salles de bains :", nbSallesDeBainsSpinner);
        addChamp(panelFiltres, "Étoiles (min) :", evaluationCombo);
        addChamp(panelFiltres, "Type de logement :", typeLogementField);
        addChamp(panelFiltres, "Surface minimum (m²) :", surfaceSpinner);
        addChamp(panelFiltres, "Type de lit :", typeLitField);
        addChamp(panelFiltres, "Nombre d'étages min :", etagesSpinner);

        panelFiltres.add(vueMerCheck);
        panelFiltres.add(procheCentreCheck);
        panelFiltres.add(parkingCheck);
        panelFiltres.add(animauxCheck);
        panelFiltres.add(climCheck);
        panelFiltres.add(ascenseurCheck);
        panelFiltres.add(piscineCheck);
        panelFiltres.add(balconCheck);
        panelFiltres.add(accessCheck);
        panelFiltres.add(wifiCheck);
        panelFiltres.add(fumeurCheck);
        panelFiltres.add(petitDejCheck);
        panelFiltres.add(menageCheck);

        // === Listeners déclencheurs ===
        ActionListener trigger = e -> executerRechercheAvance();
        ChangeListener cl = e -> executerRechercheAvance();
        KeyAdapter ka = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                executerRechercheAvance();
            }
        };

        // Champs texte
        regionField.addKeyListener(ka);
        villeField.addKeyListener(ka);
        aeroportField.addKeyListener(ka);
        prixMinField.addKeyListener(ka);
        prixMaxField.addKeyListener(ka);
        typeLogementField.addKeyListener(ka);
        typeLitField.addKeyListener(ka);

        // ComboBox / CheckBox
        paysBox.addActionListener(trigger);
        evaluationCombo.addActionListener(trigger);

        vueMerCheck.addActionListener(trigger);
        procheCentreCheck.addActionListener(trigger);
        parkingCheck.addActionListener(trigger);
        animauxCheck.addActionListener(trigger);
        climCheck.addActionListener(trigger);
        ascenseurCheck.addActionListener(trigger);
        piscineCheck.addActionListener(trigger);
        balconCheck.addActionListener(trigger);
        accessCheck.addActionListener(trigger);
        wifiCheck.addActionListener(trigger);
        fumeurCheck.addActionListener(trigger);
        petitDejCheck.addActionListener(trigger);
        menageCheck.addActionListener(trigger);

        // Spinners
        capaciteSpinner.addChangeListener(cl);
        nbChambresSpinner.addChangeListener(cl);
        nbSallesDeBainsSpinner.addChangeListener(cl);
        surfaceSpinner.addChangeListener(cl);
        etagesSpinner.addChangeListener(cl);

        return panelFiltres;
    }


    private void addChamp(JPanel panel, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(l);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
    }

    private void executerRechercheAvance() {
        String pays = (String) paysBox.getSelectedItem();
        String region = regionField.getText();
        String villeFiltre = villeField.getText();
        String aeroport = aeroportField.getText();
        String prixMin = prixMinField.getText();
        String prixMax = prixMaxField.getText();
        int sdb = (int) nbSallesDeBainsSpinner.getValue();
        int evalMin = (int) evaluationCombo.getSelectedItem();

        // ✅ Lire les valeurs dans la barre de recherche (en haut)
        int nbAdultes = Integer.parseInt(peopleLabel.getText().split(": ")[1]);
        int nbChambres = Integer.parseInt(roomsLabel.getText().split(": ")[1]);
        int capacite = nbAdultes;
        int chambres = nbChambres;

        boolean vueMer = vueMerCheck.isSelected();
        boolean procheCentre = procheCentreCheck.isSelected();

        String ville = destinationField.getText().equals("Où allez-vous ?") ? "" : destinationField.getText();
        Date dateDebut = (Date) arrivalSpinner.getValue();
        Date dateFin = (Date) departureSpinner.getValue();

        // ✅ On récupère la valeur de la catégorie
        String categorieChoisie = (String) categorieCombo.getSelectedItem(); // Ex: "Appartement", "Hôtel", "Villa"
        String typeLogement = categorieChoisie != null && !categorieChoisie.isBlank() && !categorieChoisie.equals("Tous")
                ? categorieChoisie
                : typeLogementField.getText(); // fallback

        RechercheDAO dao = new RechercheDAO();
        List<Logement> logements = dao.rechercherLogementsAvance(
                dateDebut, dateFin,
                pays, region, villeFiltre.isBlank() ? ville : villeFiltre, aeroport,
                capacite, prixMin, prixMax,
                wifiCheck.isSelected(), fumeurCheck.isSelected(), petitDejCheck.isSelected(), menageCheck.isSelected(),
                vueMer, procheCentre,
                chambres, sdb,
                parkingCheck.isSelected(), animauxCheck.isSelected(), climCheck.isSelected(),
                typeLogement, // ← ici la catégorie est bien injectée
                (int) surfaceSpinner.getValue(),
                typeLitField.getText(),
                (int) etagesSpinner.getValue(),
                ascenseurCheck.isSelected(), piscineCheck.isSelected(), balconCheck.isSelected(), accessCheck.isSelected(),
                evalMin
        );

        // 🧹 Rafraîchissement de l'affichage
        panelResultats.removeAll();
        if (logements.isEmpty()) {
            JLabel rien = new JLabel("Aucun logement trouvé.");
            rien.setFont(new Font("Arial", Font.BOLD, 16));
            rien.setForeground(Color.RED);
            rien.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelResultats.add(rien);
        } else {
            for (Logement l : logements) {
                panelResultats.add(creerCarteLogement(l));
                panelResultats.add(Box.createVerticalStrut(10));
            }
        }

        panelResultats.revalidate();
        panelResultats.repaint();
    }



    private JPanel creerCarteLogement(Logement logement) {
        JPanel carte = new JPanel();
        carte.setLayout(new BorderLayout());
        carte.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        carte.setBackground(Color.WHITE);
        carte.setPreferredSize(new Dimension(800, 130));

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infos.setBackground(Color.WHITE);

        infos.add(new JLabel("🏡 " + logement.getTitre()));
        infos.add(new JLabel("📍 " + logement.getVille()));

        // 💰 Prix → uniquement si ce n'est pas un hôtel
        if (!logement.getTypeLogement().equalsIgnoreCase("Hôtel")) {
            infos.add(new JLabel("💰 " + logement.getPrixParNuit() + " € / nuit"));
        } else {
            infos.add(new JLabel("🏨 Plusieurs chambres disponibles"));
        }

        // ⭐ Étoiles
        StringBuilder etoiles = new StringBuilder();
        for (int i = 0; i < 5; i++) etoiles.append(i < logement.getEvaluation() ? "⭐" : "☆");
        infos.add(new JLabel("Étoile : " + etoiles));

        carte.add(infos, BorderLayout.CENTER);
        carte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carte.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                afficherDetailsLogement(logement);
            }
        });

        return carte;
    }


    private JSpinner createDateSpinner(int x, int y, Date date, String label, JPanel parent) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(x, y - 20, 100, 20);
        parent.add(lbl);

        SpinnerDateModel model = new SpinnerDateModel(date, null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setBounds(x, y, 150, 40);
        parent.add(spinner);
        return spinner;
    }

    private JPanel createCounterPanel(JLabel label, String prefix, int x, int y) {
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");
        plus.setPreferredSize(new Dimension(40, 30));
        minus.setPreferredSize(new Dimension(40, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBounds(x, y, 180, 40);
        panel.add(label);
        panel.add(minus);
        panel.add(plus);

        plus.addActionListener(e -> {
            int val = Integer.parseInt(label.getText().split(": ")[1]);
            label.setText(prefix + ": " + (val + 1));
            executerRechercheAvance(); // ✅ relancer la recherche
        });

        minus.addActionListener(e -> {
            int val = Integer.parseInt(label.getText().split(": ")[1]);
            if (val > 0) {
                label.setText(prefix + ": " + (val - 1));
                executerRechercheAvance(); // ✅ relancer aussi ici
            }
        });

        return panel;
    }


    private JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCompte = new JMenu("Mon compte");
        JMenuItem mail = new JMenuItem("Mail : " + utilisateur.getMail());
        mail.setEnabled(false);
        menuCompte.add(mail);
        menuCompte.addSeparator();

        JMenuItem deconnexion = new JMenuItem("Se déconnecter");
        deconnexion.addActionListener(e -> {
            dispose();
            new VueAccueil().setVisible(true);
        });
        menuCompte.add(deconnexion);

        menuBar.add(menuCompte);
        return menuBar;
    }
    private void afficherDetailsLogement(Logement logement) {
        JDialog dialog = new JDialog((Frame) null, "Détails du logement", true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(null);

        JPanel globalPanel = new JPanel(new BorderLayout());
        globalPanel.setBackground(Color.WHITE);

        // 🎞️ Carousel images
        String base = logement.getImg();
        List<String> imagePaths = new ArrayList<>();
        if (base != null && !base.isBlank()) {
            for (String path : base.split(";")) {
                imagePaths.add(path.trim());
            }
        }

        JPanel imageCarousel = createImageCarousel(imagePaths);
        globalPanel.add(imageCarousel, BorderLayout.NORTH);

        // 📋 Panneau détails
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        detailPanel.setBackground(Color.WHITE);

        detailPanel.add(new JLabel("🏡 Titre : " + logement.getTitre()));
        detailPanel.add(new JLabel("📍 Adresse : " + logement.getAdresse()));
        detailPanel.add(new JLabel("🌍 Ville / Région / Pays : " + logement.getVille() + " / " + logement.getRegion() + " / " + logement.getPays()));
        detailPanel.add(new JLabel("🏷️ Type : " + logement.getTypeLogement() + " (" + logement.getCapacite() + " pers)"));

        if (!logement.getTypeLogement().equalsIgnoreCase("Hôtel")) {
            detailPanel.add(new JLabel("💰 Prix par nuit : " + logement.getPrixParNuit() + " €"));
            detailPanel.add(new JLabel("📐 Surface : " + logement.getSurfaceM2() + " m²"));
        } else {
            detailPanel.add(new JLabel("🏨 Cet hôtel propose plusieurs types de chambres avec des prix et surfaces variables."));
            detailPanel.add(new JLabel("🔍 Consultez les chambres disponibles ci-dessous."));
        }

        detailPanel.add(new JLabel("🛌 Type de lit : " + logement.getTypeDeLit()));
        detailPanel.add(new JLabel("🏢 Étages : " + logement.getNbEtages() + (logement.isAscenseur() ? " (ascenseur)" : "")));
        detailPanel.add(new JLabel("🚪 Chambres : " + logement.getNbChambres()));
        detailPanel.add(new JLabel("🛁 Salles de bain : " + logement.getNbSallesDeBains()));
        detailPanel.add(new JLabel("📶 Wifi : " + boolToOuiNon(logement.isWifi())));
        detailPanel.add(new JLabel("🚬 Fumeur : " + boolToOuiNon(logement.isFumeur())));
        detailPanel.add(new JLabel("🍳 Petit-déjeuner inclus : " + boolToOuiNon(logement.isPetitDej())));
        detailPanel.add(new JLabel("🧼 Ménage inclus : " + boolToOuiNon(logement.isMenage())));
        detailPanel.add(new JLabel("🐶 Animaux admis : " + boolToOuiNon(logement.isAnimauxAdmis())));
        detailPanel.add(new JLabel("🚗 Parking : " + boolToOuiNon(logement.isParking())));
        detailPanel.add(new JLabel("❄️ Climatisation : " + boolToOuiNon(logement.isClimatisation())));
        detailPanel.add(new JLabel("🌊 Vue sur mer : " + boolToOuiNon(logement.isVueMer())));
        detailPanel.add(new JLabel("🏙️ Proche centre-ville : " + boolToOuiNon(logement.isProcheCentreVille())));
        detailPanel.add(new JLabel("🏊 Piscine privée : " + boolToOuiNon(logement.isPiscinePrivee())));
        detailPanel.add(new JLabel("🪟 Balcon / Terrasse : " + boolToOuiNon(logement.isBalconTerrasse())));
        detailPanel.add(new JLabel("♿ Accessible PMR : " + boolToOuiNon(logement.isAccessibiliteHandicape())));
        detailPanel.add(new JLabel("✈️ Aéroport à proximité : " + logement.getAeroportProche()));
        detailPanel.add(new JLabel("⭐ Étoile : " + logement.getEvaluation() + "/5"));
        detailPanel.add(Box.createVerticalStrut(10));

        // 📄 Description
        if (logement.getDescription() != null && !logement.getDescription().isEmpty()) {
            JTextArea descArea = new JTextArea(logement.getDescription());
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setBackground(new Color(245, 245, 245));
            descArea.setBorder(BorderFactory.createTitledBorder("📝 Description"));
            detailPanel.add(descArea);
        }

        // 🔧 Équipements
        if (logement.getEquipements() != null && !logement.getEquipements().isEmpty()) {
            JTextArea equipArea = new JTextArea(logement.getEquipements());
            equipArea.setLineWrap(true);
            equipArea.setWrapStyleWord(true);
            equipArea.setEditable(false);
            equipArea.setBackground(new Color(245, 245, 245));
            equipArea.setBorder(BorderFactory.createTitledBorder("🔧 Équipements"));
            detailPanel.add(Box.createVerticalStrut(10));
            detailPanel.add(equipArea);
        }

        // 🛏️ Si c’est un hôtel → afficher uniquement les chambres dispos
        // 🛏️ Si c’est un hôtel → afficher uniquement les chambres dispos
        if (logement.getTypeLogement().equalsIgnoreCase("Hôtel")) {
            Date dateDebutRecherche = (Date) arrivalSpinner.getValue();
            Date dateFinRecherche = (Date) departureSpinner.getValue();

            ChambreDAO chambreDAO = new ChambreDAO();
            List<Chambre> chambresDispo = chambreDAO.getChambresDisponiblesPourLogement(
                    logement.getIdLogement(), dateDebutRecherche, dateFinRecherche
            );

            detailPanel.add(Box.createVerticalStrut(20));
            JLabel chambreTitle = new JLabel("🛏️ Chambres disponibles pour vos dates");
            chambreTitle.setFont(new Font("Arial", Font.BOLD, 16));
            chambreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailPanel.add(chambreTitle);

            if (!chambresDispo.isEmpty()) {
                for (Chambre chambre : chambresDispo) {
                    JPanel panelChambre = new JPanel();
                    panelChambre.setLayout(new BoxLayout(panelChambre, BoxLayout.Y_AXIS));
                    panelChambre.setBackground(new Color(245, 245, 245));
                    panelChambre.setBorder(BorderFactory.createTitledBorder(chambre.getNomChambre()));
                    panelChambre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    panelChambre.add(new JLabel("💰 Prix : " + chambre.getPrix() + " € / nuit"));
                    panelChambre.add(new JLabel("📐 Surface : " + chambre.getSurface() + " m²"));
                    panelChambre.add(new JLabel("👥 Capacité : " + chambre.getCapacite() + " personnes"));
                    panelChambre.add(new JLabel("🛏️ Lit : " + chambre.getTypeLit()));
                    panelChambre.add(new JLabel("🛁 Salle(s) de bain : " + chambre.getNbSdb()));
                    panelChambre.add(new JLabel("🔢 Chambres disponibles : " + chambre.getQuantiteDisponible()));


                    if (chambre.getDescription() != null && !chambre.getDescription().isBlank()) {
                        JTextArea desc = new JTextArea(chambre.getDescription());
                        desc.setWrapStyleWord(true);
                        desc.setLineWrap(true);
                        desc.setEditable(false);
                        desc.setBackground(new Color(255, 255, 255));
                        desc.setBorder(BorderFactory.createTitledBorder("Description"));
                        panelChambre.add(Box.createVerticalStrut(5));
                        panelChambre.add(desc);
                    }

                    // ➕ Ajouter le clic vers la fiche détail
                    panelChambre.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            afficherDetailsChambre(chambre);
                        }
                    });

                    detailPanel.add(Box.createVerticalStrut(10));
                    detailPanel.add(panelChambre);
                }

            } else {
                JLabel label = new JLabel("❌ Aucune chambre disponible pour vos dates.");
                label.setForeground(Color.RED);
                label.setFont(new Font("Arial", Font.ITALIC, 14));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailPanel.add(label);
            }
        }
// ✅ Bouton réserver pour les non-hôtels
        if (!logement.getTypeLogement().equalsIgnoreCase("Hôtel")) {
            detailPanel.add(Box.createVerticalStrut(20));

            int nbPersonnes = Integer.parseInt(peopleLabel.getText().split(": ")[1]);

            if (nbPersonnes <= 0) {
                JLabel label = new JLabel("❌ Veuillez indiquer le nombre de personnes dans la barre de recherche.");
                label.setForeground(Color.RED);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailPanel.add(label);
            } else if (!utilisateur.getType().equalsIgnoreCase("client")) {
                JLabel label = new JLabel("❌ Seuls les clients peuvent effectuer une réservation.");
                label.setForeground(Color.RED);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailPanel.add(label);
            } else if (logement.getCapacite() < nbPersonnes) {
                JLabel label = new JLabel("❌ Ce logement n’a pas une capacité suffisante (" + logement.getCapacite() + " personnes).");
                label.setForeground(Color.RED);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                detailPanel.add(label);
            } else {
                JButton reserverLogementBtn = new JButton("Réserver ce logement");
                reserverLogementBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

                reserverLogementBtn.addActionListener(e -> {
                    Date dateDebut = (Date) arrivalSpinner.getValue();
                    Date dateFin = (Date) departureSpinner.getValue();
                    int nnbPersonnes = Integer.parseInt(peopleLabel.getText().split(": ")[1]);

                    // ✅ Vérification de capacité AVANT la réservation
                    if (logement.getCapacite() < nnbPersonnes) {
                        JOptionPane.showMessageDialog(dialog,
                                "❌ Ce logement n’a pas une capacité suffisante pour " + nnbPersonnes + " personne(s).\nVeuillez choisir un autre logement.",
                                "Capacité insuffisante",
                                JOptionPane.WARNING_MESSAGE);
                        return; // ❌ On ne continue pas
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "<html><b>Êtes-vous sûr de vouloir réserver ce logement</b><br>du "
                                    + sdf.format(dateDebut) + " au " + sdf.format(dateFin) + " ?</html>",
                            "Confirmation de réservation",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        ReservationLogementDAO dao = new ReservationLogementDAO();
                        boolean success = dao.reserverLogementEnAttente(utilisateur.getId(), logement.getIdLogement(), dateDebut, dateFin);

                        if (success) {
                            JOptionPane.showMessageDialog(dialog, "✅ Réservation en attente. Paiement requis.");
                            dialog.dispose();
                            new VueProcessReservation(utilisateur, null, 1, dateDebut, dateFin, logement).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "❌ Échec de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });


                detailPanel.add(reserverLogementBtn);
            }
        }

        // 🚪 Bouton Fermer
        JButton fermer = new JButton("Fermer");
        fermer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fermer.addActionListener(e -> dialog.dispose());
        detailPanel.add(Box.createVerticalStrut(20));
        detailPanel.add(fermer);

        // 🖱️ Scroll
        JScrollPane scrollPane = new JScrollPane(detailPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        globalPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.setContentPane(globalPanel);
        dialog.setVisible(true);
    }




    private String boolToOuiNon(boolean b) {
        return b ? "Oui" : "Non";
    }
    private JPanel createImageCarousel(List<String> imagePaths) {
        JPanel carouselPanel = new JPanel(new BorderLayout());
        carouselPanel.setBackground(Color.LIGHT_GRAY);
        carouselPanel.setPreferredSize(new Dimension(600, 350));
        carouselPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(600, 350));

        if (imagePaths == null || imagePaths.isEmpty()) {
            imageLabel.setText("Aucune image disponible");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 18));
            carouselPanel.add(imageLabel, BorderLayout.CENTER);
            return carouselPanel;
        }

        // Index courant
        final int[] index = {0};

        // Méthode pour afficher une image
        Runnable updateImage = () -> {
            try {
                ImageIcon icon = new ImageIcon(imagePaths.get(index[0]));
                Image img = icon.getImage().getScaledInstance(600, 350, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText(""); // clear any fallback text
            } catch (Exception ex) {
                imageLabel.setText("Image introuvable");
                imageLabel.setIcon(null);
            }
        };

        updateImage.run(); // Afficher la première image

        // Flèche gauche
        JButton prevButton = new JButton("←");
        styliseBouton(prevButton);
        prevButton.addActionListener(e -> {
            index[0] = (index[0] - 1 + imagePaths.size()) % imagePaths.size();
            updateImage.run();
        });

        // Flèche droite
        JButton nextButton = new JButton("→");
        styliseBouton(nextButton);
        nextButton.addActionListener(e -> {
            index[0] = (index[0] + 1) % imagePaths.size();
            updateImage.run();
        });

        carouselPanel.add(prevButton, BorderLayout.WEST);
        carouselPanel.add(imageLabel, BorderLayout.CENTER);
        carouselPanel.add(nextButton, BorderLayout.EAST);

        return carouselPanel;
    }

    private void styliseBouton(JButton btn) {
        btn.setPreferredSize(new Dimension(50, 100));
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setBackground(new Color(220, 220, 240));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    private void afficherDetailsChambre(Chambre chambre) {
        JDialog dialog = new JDialog(this, chambre.getNomChambre(), true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        Date dateDebut = (Date) arrivalSpinner.getValue();
        Date dateFin = (Date) departureSpinner.getValue();

        // 🔄 Recharge disponibilité
        ChambreDAO dao = new ChambreDAO();
        List<Chambre> chambresDispo = dao.getChambresDisponiblesPourLogement(chambre.getLogementId(), dateDebut, dateFin);
        Chambre chambreAjour = chambresDispo.stream()
                .filter(c -> c.getId() == chambre.getId())
                .findFirst()
                .orElse(null);

        int dispo = chambreAjour != null ? chambreAjour.getQuantiteDisponible() : 0;

        panel.add(new JLabel("🛌 Nom : " + chambre.getNomChambre()));
        panel.add(new JLabel("🔢 Chambres disponibles : " + dispo));
        panel.add(new JLabel("🛏️ Type de lit : " + chambre.getTypeLit()));
        panel.add(new JLabel("📐 Surface : " + chambre.getSurface() + " m²"));
        panel.add(new JLabel("🛁 Salle(s) de bain : " + chambre.getNbSdb()));
        panel.add(new JLabel("💰 Prix : " + chambre.getPrix() + " € / nuit"));
        panel.add(Box.createVerticalStrut(10));

        int nbPersonnes = Integer.parseInt(peopleLabel.getText().split(": ")[1]);
        if (nbPersonnes <= 0) {
            JLabel msg = new JLabel("❌ Veuillez indiquer le nombre de personnes dans la barre de recherche.");
            msg.setForeground(Color.RED);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(msg);
        } else if (!utilisateur.getType().equalsIgnoreCase("client")) {
            JLabel msg = new JLabel("❌ Seuls les clients peuvent effectuer une réservation.");
            msg.setForeground(Color.RED);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(msg);
        } else if (dispo == 0) {
            JLabel msg = new JLabel("❌ Aucune chambre disponible pour ces dates.");
            msg.setForeground(Color.RED);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(msg);
        } else {
            panel.add(new JLabel("🛏️ Nombre de chambres à réserver :"));
            JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, dispo, 1));
            panel.add(quantiteSpinner);

            JButton reserverBtn = new JButton("Réserver cette chambre");
            reserverBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            reserverBtn.addActionListener(e -> {
                int quantiteDemandee = (int) quantiteSpinner.getValue();

                // ✅ Vérif capacité cumulée
                int capaciteTotale = chambre.getCapacite() * quantiteDemandee;
                if (capaciteTotale < nbPersonnes) {
                    JOptionPane.showMessageDialog(dialog,
                            "❌ Nombre de personnes trop élevé pour cette réservation.\nVeuillez réserver plusieurs chambres.",
                            "Capacité insuffisante",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // ✅ Confirmation
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "<html><b>Êtes-vous sûr de vouloir réserver cette chambre</b><br>du "
                                + sdf.format(dateDebut) + " au " + sdf.format(dateFin) + " ?</html>",
                        "Confirmation de réservation",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    ReservationChambreDAO resDao = new ReservationChambreDAO();
                    boolean success = resDao.reserverChambresEnAttente(
                            utilisateur.getId(),
                            chambreAjour != null ? chambreAjour : chambre,
                            quantiteDemandee,
                            dateDebut,
                            dateFin
                    );

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "✅ Réservation en attente ! Paiement requis.");
                        dialog.dispose();
                        new VueProcessReservation(utilisateur, chambre, quantiteDemandee, dateDebut, dateFin).setVisible(true);
                        executerRechercheAvance();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "❌ Pas assez de chambres disponibles.",
                                "Erreur de réservation",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            panel.add(Box.createVerticalStrut(10));
            panel.add(reserverBtn);
        }

        if (chambre.getDescription() != null && !chambre.getDescription().isBlank()) {
            JTextArea desc = new JTextArea(chambre.getDescription());
            desc.setWrapStyleWord(true);
            desc.setLineWrap(true);
            desc.setEditable(false);
            desc.setBackground(new Color(245, 245, 245));
            desc.setBorder(BorderFactory.createTitledBorder("Description"));
            panel.add(Box.createVerticalStrut(10));
            panel.add(desc);
        }

        JButton fermer = new JButton("Fermer");
        fermer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fermer.addActionListener(e -> dialog.dispose());
        panel.add(Box.createVerticalStrut(20));
        panel.add(fermer);

        dialog.setContentPane(new JScrollPane(panel));
        dialog.setVisible(true);
    }
    private JPanel creerHeaderBar() {
        JPanel globalHeader = new JPanel();
        globalHeader.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(0, 82, 204));
        headerPanel.setPreferredSize(new Dimension(900, 80));

        JLabel titleLabel = new JLabel("Booking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(380, 20, 200, 40);
        headerPanel.add(titleLabel);

        JButton hebergementButton = createHeaderButton("Hébergements");
        hebergementButton.setBounds(20, 20, 150, 40);
        hebergementButton.addActionListener(e -> {
            new VueHebergement(utilisateur).setVisible(true);
            dispose();
        });
        headerPanel.add(hebergementButton);

        JButton reservationsButton = createHeaderButton("Mes réservations");
        reservationsButton.setBounds(180, 20, 180, 40);
        reservationsButton.addActionListener(e -> {
            new VueReservations().setVisible(true);
            dispose();
        });
        headerPanel.add(reservationsButton);

        JButton monCompteButton = createHeaderButton("Mon compte ⏷");
        monCompteButton.setBounds(700, 20, 160, 40);

        JPopupMenu popupCompte = creerPopupMenuCompte();
        monCompteButton.addActionListener(e -> {
            popupCompte.show(monCompteButton, 0, monCompteButton.getHeight());
        });
        headerPanel.add(monCompteButton);

        // 💥 Ici on ajoute le bon topPanel déjà initialisé, avec les bons composants
        globalHeader.add(headerPanel, BorderLayout.NORTH);
        globalHeader.add(topPanelRecherche, BorderLayout.CENTER);

        return globalHeader;
    }



    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 82, 204));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }
    private JPopupMenu creerPopupMenuCompte() {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem itemMail = new JMenuItem("Mail : " + utilisateur.getMail());
        itemMail.setEnabled(false);
        popup.add(itemMail);

        if ("client".equalsIgnoreCase(utilisateur.getType())) {
            JMenuItem itemMesReservations = new JMenuItem("Mes réservations");
            itemMesReservations.addActionListener(e -> {
                new VueReservations().setVisible(true);
                dispose();
            });
            popup.add(itemMesReservations);

            JMenuItem itemAvantages = new JMenuItem("🎁 Mes avantages");
            itemAvantages.addActionListener(e -> {
                int nbLogements = ReservationLogementDAO.countReservationsPassees(utilisateur.getId());
                int nbChambres = ReservationChambreDAO.countReservationsPassees(utilisateur.getId());
                int total = nbLogements + nbChambres;

                String code;
                int reduction;

                if (total >= 5) {
                    code = "VIP20";
                    reduction = 20;
                } else if (total >= 3) {
                    code = "LOYAL15";
                    reduction = 15;
                } else if (total >= 1) {
                    code = "WELCOME10";
                    reduction = 10;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Aucun avantage disponible pour l’instant.\nRéservez une première fois pour débloquer vos avantages.",
                            "Pas encore d’avantages",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(this,
                        "🎉 Bravo ! En tant qu'ancien client, vous bénéficiez de -" + reduction + "%\navec le code : " + code,
                        "Avantage fidélité",
                        JOptionPane.INFORMATION_MESSAGE);
            });
            popup.add(itemAvantages);
        }

        if ("admin".equalsIgnoreCase(utilisateur.getType())) {
            JMenuItem itemToutesReservations = new JMenuItem("Toutes les réservations");
            itemToutesReservations.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Page admin à venir !");
            });
            popup.add(itemToutesReservations);

            JMenuItem itemAjoutLogement = new JMenuItem("Ajouter un logement");
            itemAjoutLogement.addActionListener(e -> {
                // Affichage à implémenter
            });
            popup.add(itemAjoutLogement);

            JMenuItem itemTousLogements = new JMenuItem("Tous les logements");
            itemTousLogements.addActionListener(e -> {
                // Affichage à implémenter
            });
            popup.add(itemTousLogements);
        }

        popup.addSeparator();

        JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
        itemDeconnexion.addActionListener(e -> {
            dispose();
            new VueAccueil().setVisible(true);
        });
        popup.add(itemDeconnexion);

        return popup;
    }




}