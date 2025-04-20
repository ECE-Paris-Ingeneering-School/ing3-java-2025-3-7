package vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class Accueil2 extends JFrame {
    private Utilisateur utilisateur;
    private JComboBox<String> categorieCombo;

    public Accueil2(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        initialiserFenetre();
        setJMenuBar(creerMenuBar());
        ajouterContenuComplet(); // Fusion VueAccueil + Menu utilisateur
    }

    private void initialiserFenetre() {
        setTitle("Accueil - " + utilisateur.getType());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 82, 204));
        setLayout(new BorderLayout());
    }

    private JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuCompte = new JMenu("Mon compte");

        menuCompte.add(creerItemMail());
        ajouterElementsSpecifiques(menuCompte);
        menuCompte.addSeparator();
        menuCompte.add(creerItemDeconnexion());

        menuBar.add(menuCompte);
        return menuBar;
    }

    private JMenuItem creerItemMail() {
        JMenuItem itemMail = new JMenuItem("Mail : " + utilisateur.getMail());
        itemMail.setEnabled(false);
        return itemMail;
    }

    private void ajouterElementsSpecifiques(JMenu menuCompte) {
        if ("client".equalsIgnoreCase(utilisateur.getType())) {
            ajouterElementsClient(menuCompte);
        } else if ("admin".equalsIgnoreCase(utilisateur.getType())) {
            ajouterElementsAdmin(menuCompte);
        }
    }

    private void ajouterElementsClient(JMenu menuCompte) {
        JMenuItem itemMesReservations = new JMenuItem("Mes réservations");
        itemMesReservations.addActionListener(e -> {
            // Exemple : new VueReservations(utilisateur).setVisible(true);
        });
        menuCompte.add(itemMesReservations);
    }

    private void ajouterElementsAdmin(JMenu menuCompte) {
        JMenuItem itemToutesReservations = new JMenuItem("Toutes les réservations");
        itemToutesReservations.addActionListener(e -> {
            // Exemple : new VueToutesReservations().setVisible(true);
        });
        menuCompte.add(itemToutesReservations);

        JMenuItem itemAjoutLogement = new JMenuItem("Ajouter un logement");
        itemAjoutLogement.addActionListener(e -> {
            // Exemple : new VueAjoutLogement().setVisible(true);
        });
        menuCompte.add(itemAjoutLogement);

        JMenuItem itemTousLogements = new JMenuItem("Tous les logements");
        itemTousLogements.addActionListener(e -> {
            // Exemple : new VueTousLogements().setVisible(true);
        });
        menuCompte.add(itemTousLogements);
    }

    private JMenuItem creerItemDeconnexion() {
        JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
        itemDeconnexion.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(
                    Accueil2.this,
                    "Voulez-vous vraiment vous déconnecter ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmation == JOptionPane.YES_OPTION) {
                // Exemple : new VueAccueil().setVisible(true);
                dispose();
                new VueAccueil().setVisible(true);
            }
        });
        return itemDeconnexion;
    }

    private void ajouterContenuComplet() {
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
            new VueAccueil().setVisible(true);
        });
        headerPanel.add(hebergementButton);

        JButton reservationsButton = createHeaderButton("Mes réservations");
        reservationsButton.setBounds(180, 20, 180, 40);
        reservationsButton.addActionListener(e -> {
            new VueReservations().setVisible(true);
            dispose();
        });
        headerPanel.add(reservationsButton);

        add(headerPanel, BorderLayout.NORTH);

        // ===== CONTENU PRINCIPAL =====
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(0, 123, 255));

        JLabel searchLabel = new JLabel("Trouvez votre prochain séjour");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 26));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setBounds(230, 50, 500, 30);
        mainPanel.add(searchLabel);

        JTextField destinationField = new JTextField("Où allez-vous ?");
        destinationField.setFont(new Font("Arial", Font.PLAIN, 16));
        destinationField.setBounds(100, 150, 200, 40);
        destinationField.setToolTipText("Où allez-vous ?");
        destinationField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (destinationField.getText().equals("Où allez-vous ?")) {
                    destinationField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (destinationField.getText().isEmpty()) {
                    destinationField.setText("Où allez-vous ?");
                }
            }
        });
        mainPanel.add(destinationField);

        // 🏷️ CATEGORIE ➕ ComboBox
        categorieCombo = new JComboBox<>(new String[]{"", "Hôtel", "Appartement", "Villa"});
        categorieCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categorieCombo.setBounds(100, 200, 200, 30);
        mainPanel.add(categorieCombo);

        // Date pickers
        JSpinner departureSpinner = createDateSpinner(470, 150, "Date d'arrivée", mainPanel);
        JSpinner arrivalSpinner = createDateSpinner(310, 150, "Date de départ", mainPanel);

        // Adultes
        JLabel peopleLabel = new JLabel("Adultes: 0");
        JPanel peoplePanel = createCounterPanel(peopleLabel, "Adultes", 630, 150);
        mainPanel.add(peoplePanel);

        // Chambres
        JLabel roomsLabel = new JLabel("Chambres: 0");
        JPanel roomsPanel = createCounterPanel(roomsLabel, "Chambres", 630, 200);
        mainPanel.add(roomsPanel);

        // Bouton recherche
        JButton searchButton = new JButton("Rechercher");
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(new Color(0, 82, 204));
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBounds(370, 250, 150, 45);
        searchButton.addActionListener(e -> {
            String destination = destinationField.getText().equals("Où allez-vous ?") ? "" : destinationField.getText();
            Date arrivalDate = (Date) arrivalSpinner.getValue();
            Date departureDate = (Date) departureSpinner.getValue();
            int numAdults = Integer.parseInt(peopleLabel.getText().split(": ")[1]);
            int numRooms = Integer.parseInt(roomsLabel.getText().split(": ")[1]);

            // 🆕 Lire la catégorie sélectionnée
            String typeLogement = (String) categorieCombo.getSelectedItem();

            // 🚀 Appel modifié pour inclure la catégorie
            new VueRechercheBis(utilisateur, destination, arrivalDate, departureDate, numRooms, numAdults, typeLogement).setVisible(true);
            dispose();
        });
        mainPanel.add(searchButton);


        // SECTION DESTINATIONS
        JPanel southPanel = createDestinationsPanel();

        // Ajout au layout principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0, 123, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setPreferredSize(new Dimension(900, 300));
        southPanel.setPreferredSize(new Dimension(900, 300));
        contentPanel.add(mainPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(southPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JSpinner createDateSpinner(int x, int y, String labelText, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y - 30, 150, 20);
        panel.add(label);

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
        spinner.setBounds(x, y, 150, 40);
        panel.add(spinner);
        // ======= HEADER =======
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

// 🆕 Bouton "Mon compte" à la place du bouton "Se connecter"
        JButton monCompteButton = createHeaderButton("Mon compte ⏷");
        monCompteButton.setBounds(700, 20, 160, 40);

        JPopupMenu popupCompte = creerPopupMenuCompte(); // Crée le menu dynamique

        monCompteButton.addActionListener(e -> {
            popupCompte.show(monCompteButton, 0, monCompteButton.getHeight());
        });
        headerPanel.add(monCompteButton);

        add(headerPanel, BorderLayout.NORTH);


        return spinner;
    }
    private JPanel createCounterPanel(JLabel label, String prefix, int x, int y) {
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton plusButton = new JButton("+");
        JButton minusButton = new JButton("-");
        plusButton.setPreferredSize(new Dimension(30, 30));
        minusButton.setPreferredSize(new Dimension(30, 30));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        panel.setBounds(x, y, 250, 40);
        panel.add(label);
        panel.add(minusButton);
        panel.add(plusButton);

        plusButton.addActionListener(e -> {
            int value = Integer.parseInt(label.getText().split(": ")[1]);
            label.setText(prefix + ": " + (value + 1));
        });

        minusButton.addActionListener(e -> {
            int value = Integer.parseInt(label.getText().split(": ")[1]);
            if (value > 0) label.setText(prefix + ": " + (value - 1));
        });

        return panel;
    }

    private JPanel createDestinationsPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(0, 123, 255));

        JPanel gradientTitlePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 90, 150),
                        getWidth(), getHeight(), new Color(70, 160, 180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientTitlePanel.setLayout(new BorderLayout());
        gradientTitlePanel.setPreferredSize(new Dimension(900, 60));

        JLabel title = new JLabel("Nos destinations phares", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gradientTitlePanel.add(title, BorderLayout.CENTER);
        southPanel.add(gradientTitlePanel, BorderLayout.NORTH);

        JPanel destinationsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        destinationsPanel.setBackground(new Color(0, 123, 255));
        destinationsPanel.setPreferredSize(new Dimension(900, 180));
        destinationsPanel.add(createDestination("Paris", "Image/Paris.png"));
        destinationsPanel.add(createDestination("New York", "Image/NewYork.png"));
        destinationsPanel.add(createDestination("Barcelone", "Image/Barcelone.png"));
        southPanel.add(destinationsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 90, 150),
                        getWidth(), getHeight(), new Color(70, 160, 180));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomPanel.setPreferredSize(new Dimension(900, 60));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JButton logementsButton = new JButton("Tous nos logements...");
        logementsButton.setFont(new Font("Georgia", Font.BOLD, 16));
        logementsButton.setForeground(Color.WHITE);
        logementsButton.setBackground(new Color(0, 100, 180));
        logementsButton.setFocusPainted(false);
        logementsButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logementsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(logementsButton);

        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        return southPanel;
    }

    private JPanel createDestination(String name, String imagePath) {
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));

        JLabel image = new JLabel(new ImageIcon(imagePath), SwingConstants.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 123, 255));
        panel.add(label, BorderLayout.NORTH);
        panel.add(image, BorderLayout.CENTER);

        return panel;
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

        JMenuItem itemMail = creerItemMail();
        itemMail.setEnabled(false);
        popup.add(itemMail);

        if ("client".equalsIgnoreCase(utilisateur.getType())) {
            JMenuItem itemMesReservations = new JMenuItem("Mes réservations");
            itemMesReservations.addActionListener(e -> {
                // Exemple : new VueReservations(utilisateur).setVisible(true);
            });
            popup.add(itemMesReservations);
        } else if ("admin".equalsIgnoreCase(utilisateur.getType())) {
            JMenuItem itemToutesReservations = new JMenuItem("Toutes les réservations");
            itemToutesReservations.addActionListener(e -> {
                // Exemple : new VueToutesReservations().setVisible(true);
            });
            popup.add(itemToutesReservations);

            JMenuItem itemAjoutLogement = new JMenuItem("Ajouter un logement");
            itemAjoutLogement.addActionListener(e -> {
                // Exemple : new VueAjoutLogement().setVisible(true);
            });
            popup.add(itemAjoutLogement);

            JMenuItem itemTousLogements = new JMenuItem("Tous les logements");
            itemTousLogements.addActionListener(e -> {
                // Exemple : new VueTousLogements().setVisible(true);
            });
            popup.add(itemTousLogements);
        }

        popup.addSeparator();

        JMenuItem itemDeconnexion = creerItemDeconnexion();
        popup.add(itemDeconnexion);

        return popup;
    }

}
