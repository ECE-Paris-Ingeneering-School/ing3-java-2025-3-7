package modele;

public class Logement {
    private int idLogement;
    private String titre;
    private String description;
    private String adresse;
    private String pays;
    private String region;
    private String ville;
    private double prixParNuit;
    private int offre;
    private int capacite;
    private String img;
    private boolean wifi;
    private boolean fumeur;
    private boolean petitDej;
    private boolean menage;
    private String aeroportProche;
    private boolean vueMer;
    private boolean procheCentreVille;
    private int nbChambres;
    private int nbSallesDeBains;
    private boolean parking;
    private boolean animauxAdmis;
    private boolean climatisation;
    private String typeLogement;
    private int surfaceM2;
    private String typeDeLit;
    private int nbEtages;
    private boolean ascenseur;
    private boolean piscinePrivee;
    private boolean balconTerrasse;
    private boolean accessibiliteHandicape;
    private String equipements;
    private int evaluation;

    public Logement(int idLogement, String titre, String description, String adresse, String pays, String region,
                    String ville, double prixParNuit, int offre, int capacite, String img, boolean wifi,
                    boolean fumeur, boolean petitDej, boolean menage, String aeroportProche, boolean vueMer,
                    boolean procheCentreVille, int nbChambres, int nbSallesDeBains, boolean parking,
                    boolean animauxAdmis, boolean climatisation, String typeLogement, int surfaceM2,
                    String typeDeLit, int nbEtages, boolean ascenseur, boolean piscinePrivee, boolean balconTerrasse,
                    boolean accessibiliteHandicape, int evaluation) {
        this.idLogement = idLogement;
        this.titre = titre;
        this.description = description;
        this.adresse = adresse;
        this.pays = pays;
        this.region = region;
        this.ville = ville;
        this.prixParNuit = prixParNuit;
        this.offre = offre;
        this.capacite = capacite;
        this.img = img;
        this.wifi = wifi;
        this.fumeur = fumeur;
        this.petitDej = petitDej;
        this.menage = menage;
        this.aeroportProche = aeroportProche;
        this.vueMer = vueMer;
        this.procheCentreVille = procheCentreVille;
        this.nbChambres = nbChambres;
        this.nbSallesDeBains = nbSallesDeBains;
        this.parking = parking;
        this.animauxAdmis = animauxAdmis;
        this.climatisation = climatisation;
        this.typeLogement = typeLogement;
        this.surfaceM2 = surfaceM2;
        this.typeDeLit = typeDeLit;
        this.nbEtages = nbEtages;
        this.ascenseur = ascenseur;
        this.piscinePrivee = piscinePrivee;
        this.balconTerrasse = balconTerrasse;
        this.accessibiliteHandicape = accessibiliteHandicape;
        this.equipements = equipements;
        this.evaluation = evaluation;
    }

    // Getters and setters
    public int getIdLogement() { return idLogement; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public String getAdresse() { return adresse; }
    public String getPays() { return pays; }
    public String getRegion() { return region; }
    public String getVille() { return ville; }
    public double getPrixParNuit() { return prixParNuit; }
    public int getOffre() { return offre; }
    public int getCapacite() { return capacite; }
    public String getImg() { return img; }
    public boolean isWifi() { return wifi; }
    public boolean isFumeur() { return fumeur; }
    public boolean isPetitDej() { return petitDej; }
    public boolean isMenage() { return menage; }
    public String getAeroportProche() { return aeroportProche; }
    public boolean isVueMer() { return vueMer; }
    public boolean isProcheCentreVille() { return procheCentreVille; }
    public int getNbChambres() { return nbChambres; }
    public int getNbSallesDeBains() { return nbSallesDeBains; }
    public boolean isParking() { return parking; }
    public boolean isAnimauxAdmis() { return animauxAdmis; }
    public boolean isClimatisation() { return climatisation; }
    public String getTypeLogement() { return typeLogement; }
    public int getSurfaceM2() { return surfaceM2; }
    public String getTypeDeLit() { return typeDeLit; }
    public int getNbEtages() { return nbEtages; }
    public boolean isAscenseur() { return ascenseur; }
    public boolean isPiscinePrivee() { return piscinePrivee; }
    public boolean isBalconTerrasse() { return balconTerrasse; }
    public boolean isAccessibiliteHandicape() { return accessibiliteHandicape; }
    public String getEquipements() { return equipements; }
    public int getEvaluation() { return evaluation; }

}
