package entity;

/** <b>Utilisateur representant un chatter</b> 
 * @since 17/10/2019
 * @author Alhamdoulilah!
 * @author thiandoummohammed@gmail.com
 *
 */
public class User implements UserInterface {

	/**Nom du chatter */
	private String nom;
	
	/**Prenom du chatter*/
	private String prenom;
	
	/**Pseudo unique du chatter*/
	private String pseudo;
	
	/**Mot de passe du chatter*/
	private String password;
	
	/**Indicateur de disponibilite du chatter*/
	private Boolean connected;

	@Override
	public Boolean getConnected() {
		return connected;
	}

	@Override
	public void setConnected(Boolean connected) {
		this.connected = connected;
	}


	@Override
	public String getNom() {
		return nom;
	}


	@Override
	public void setNom(String nom) {
		this.nom = nom;
	}


	@Override
	public String getPrenom() {
		return prenom;
	}


	@Override
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	@Override
	public String getPseudo() {
		return this.pseudo;
	}


	@Override
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}


	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	

	public User(String nom, String prenom, String pseudo, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.pseudo = pseudo;
		this.password = password;
		this.setConnected(false);
	}

	public User() {
		// TODO Auto-generated constructor stub
		this.setConnected(false);
	}
}
