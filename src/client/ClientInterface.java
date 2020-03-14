package client;

import java.util.Vector;

import entity.User;

public interface ClientInterface {

	User getUser();

	void setUser(User user);

	/** Permet à un chatter de pouvoir se connecter
	 * @param pseudo
	 * @param password
	 * @return
	 */
	boolean login(String pseudo, String password);

	/** Permet à un chatter de pouvoir se deconnecter
	 * @param pseudo
	 * @param password
	 * @return
	 */
	boolean logout(String pseudo, String password);

	/** Permet à un chatter de pouvoir se désincrire du room
	 * @param password
	 * @return
	 */
	boolean unsubscribe(String password);

	/** Permet à un nouveau de pouvoir s'inscrire au room
	 * 
	 * @param nom
	 * @param prenom
	 * @param pseudo
	 * @param password
	 * @return
	 */
	boolean subscribe(String nom, String prenom, String pseudo, String password);

	/** Permet au chatter de connaitre l'ensemble des membres du room
	 * @return
	 */
	Object chatters();

	/** Permet au chatter de diffuser un message dans le room
	 * @param message
	 * @return
	 */
	boolean broadcasting(String message);

	/** Permet au chatter de consulter sa boite de reception
	 * @return
	 */
	Object discover();

	/** Permet au client d'envoyer des requetes RPC au server
	 * 
	 * @param serverAlias
	 * @param methodName
	 * @param params
	 * @return
	 */
	Object request(String serverAlias, String methodName, Vector params);

}