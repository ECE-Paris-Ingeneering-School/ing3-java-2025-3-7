package modele;

public class Utilisateur {
    private int id;
    private String mail;
    private String motDePasse;
    private String type;
    private String statut;

    // Constructeur sans ID (utilisé pour l'inscription)
    public Utilisateur(String mail, String motDePasse, String type, String statut) {
        this.mail = mail;
        this.motDePasse = motDePasse;
        this.type = type;
        this.statut = statut;
    }

    // Constructeur complet avec ID (utilisé pour la récupération depuis la BDD)
    public Utilisateur(int id, String mail, String motDePasse, String type, String statut) {
        this.id = id;
        this.mail = mail;
        this.motDePasse = motDePasse;
        this.type = type;
        this.statut = statut;
    }

    // Les Getters
    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getType() {
        return type;
    }

    public String getStatut() {
        return statut;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    
    // toString pour debug
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
