package entity;

interface UserInterface {

	Boolean getConnected();

	void setConnected(Boolean connected);

	String getNom();

	void setNom(String nom);

	String getPrenom();

	void setPrenom(String prenom);

	String getPseudo();

	void setPseudo(String pseudo);

	String getPassword();

	void setPassword(String password);

}