package modele;

public class Chambre {
    private int id;
    private int logementId;
    private String nomChambre;
    private String description;
    private double prix;
    private int capacite;
    private String typeLit;
    private int surface;
    private int nbLits;
    private int nbSdb;
    private String img;

    private int quantite;
    private int quantiteDisponible;

    public Chambre(int id, int logementId, String nomChambre, String description, double prix,
                   int capacite, String typeLit, int surface, int nbLits, int nbSdb, String img, int quantite) {
        this.id = id;
        this.logementId = logementId;
        this.nomChambre = nomChambre;
        this.description = description;
        this.prix = prix;
        this.capacite = capacite;
        this.typeLit = typeLit;
        this.surface = surface;
        this.nbLits = nbLits;
        this.nbSdb = nbSdb;
        this.img = img;
        this.quantite = quantite;
    }

    public int getId() { return id; }
    public int getLogementId() { return logementId; }
    public String getNomChambre() { return nomChambre; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
    public int getCapacite() { return capacite; }
    public String getTypeLit() { return typeLit; }
    public int getSurface() { return surface; }
    public int getNbLits() { return nbLits; }
    public int getNbSdb() { return nbSdb; }
    public String getImg() { return img; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public int getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }
}
