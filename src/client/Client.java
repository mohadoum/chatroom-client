package client;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.xmlrpc.XmlRpcClient;

import entity.User;

/**<b>Point d'entree de l'application cliente</b>
 * <b>Represente l'application cliente du chatroom qui appelle des methodes distantes du server</b>
 * <i>Cliente RPC</i>
 * <i> Bien faire attention à la lecture de donnees entre scNext.next(), scNextLine.nextLine() et sc.findWithinHorizon() </i>
 * @author Alhamdoulilah!
 * @author thiandoummohammed@gmail.com
 * @since 17/10/2019
 * @version 1.0
 *
 */
public class Client implements ClientInterface{
	
	/** Constante nom du Room du server*/
	String ROOM_NAME = "MT98SAPP";
	/** Constante port du server*/
	int SERVER_PORT = 80;
	/** Constante @IP ou DNS du server*/
	String SERVER_DNS = "localhost";
	/** Constante alias du server servant aux appelles de methodes distantes */
	String SERVER_ALIAS = "chatroom";
	
	/** Un chatter */
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/** Colorie les message affiches sur le console grace a jansi
	 */
	public static void printlnColor(String message, String ansiColor)
	{
		System.out.println(ansiColor + message + ConsoleColors.RESET);
	}
	public static void printColor(String message, String ansiColor)
	{
		System.out.print(ansiColor + message + ConsoleColors.RESET);
	}
	/** Point d'entree de l'application cliente
	 * Permet aux utilisateurs d'executer un ensemble de commandes en console afin d'utiliser le chatroom
	 */
	public static void main (String args[])
	{
		/* For outputting data with colors */
		if (System.console() == null) System.setProperty("jansi.passthrough", "true");
		
		Scanner scNext = new Scanner(System.in);
		Scanner scNextLine = new Scanner(System.in);
		Scanner scFind = new Scanner(System.in);
		menu();
		sautDeLigne(2);
		boolean quit = false;
		Client client = new Client();
		do
		{
			sautDeLigne(2);
			printColor("\n-> ", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
			switch(scNext.next())
			{
				case "login": 
					System.out.println("pseudo: ");
					String pseudo = scNext.next();
					System.out.println("password: ");
					String password = scNextLine.nextLine();
					
					if(client.login(pseudo, password))
					{
						printlnColor( "Successfully connected to "+pseudo+"!", ConsoleColors.GREEN_BACKGROUND_BRIGHT);
						client.getUser().setPseudo(pseudo);
						client.getUser().setPassword(password);
						client.getUser().setConnected(true);
					}else
					{
						printlnColor("bad credentials!",  ConsoleColors.RED_BACKGROUND_BRIGHT);
					}
					break;
					
				case "logout": 
					System.out.println("loging out ...");
					if(client.logout(client.getUser().getPseudo(), client.getUser().getPassword()))
					{
						printlnColor("Logout success to "+client.getUser().getPseudo(), ConsoleColors.GREEN_BACKGROUND_BRIGHT);
						client.setUser(new User());
					}else
					{
						printlnColor("You were no longer connected!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
					}
					break;
					
				case "unsubscribe": 
					System.out.println("Etes-vous sûre de vouloir vous désinscrire?(O/n)");
					String res = scNextLine.nextLine();
					if(res.equalsIgnoreCase("O"))
					{
						System.out.println("Saisissez votre mot de passe!");
						if(client.unsubscribe(scNextLine.nextLine()))
						{
							printlnColor("Chaoo "+client.getUser().getPseudo()+"!", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
							client.setUser(new User());;
						}else
						{
							if(client.getUser().getConnected() == true)
							{
								printlnColor("bad credentials!",  ConsoleColors.RED_BACKGROUND_BRIGHT);
							}else
							{
								printlnColor("You were no longer connected!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
							}
						}
					}else
					{
						printlnColor("Continuons à chatter avec nos amis!", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
					}
					
					break;
					
				case "subscribe": 
					System.out.println("Bienvenue dans le Chatting room");
					System.out.println("Quels sont vos coordonnées?");
					System.out.println("Prenom: ");
					String prenom = scNextLine.nextLine();
					System.out.println("Nom: ");
					String nom = scNext.next();
					System.out.println("Pseudo: ");
					String pseud = scNext.next();
					System.out.println("Password: ");
					String pwd = scNextLine.nextLine();
					
					while(!client.subscribe(nom, prenom, pseud, pwd)){
						printlnColor("Ce pseudo existe déjà! Changez-le!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
						System.out.println("Pseudo: ");
						pseud = scNext.next();
					}
					
					System.out.println("Bienvenue dans le ChatRoom "+pseud+"!");
					printlnColor("Connectez-vous maintenant!", ConsoleColors.GREEN_BACKGROUND_BRIGHT);
					
					break;
					
				case "chatters": 
					if(client.getUser().getConnected() == true)
					{
						Vector<String> chatters = (Vector<String>) client.chatters();
						if(chatters.size() > 0)
						{
							for(int i=0; i<chatters.size(); i++)
							{
								String pseu = chatters.get(i);
								System.out.print("\n"+i+". "+pseu);
								if(pseu.equals(client.getUser().getPseudo()))
								{
									System.out.print(" (Vous)");
								}
							}
				
						}else
						{
							printlnColor("There is no one here", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
						}
						
					}else
					{
						printlnColor("Connectez-vous D'abord!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
					}
					
					break;
					
				case "broadcasting": 
					if(client.getUser().getConnected())
					{
						System.out.println("Votre message: ");
						String message = scFind.findWithinHorizon(Pattern.compile("(.+)"), 0);
						if(client.broadcasting(message))
						{
							printlnColor("Sent", ConsoleColors.GREEN_BACKGROUND_BRIGHT);
						}else
						{
							printlnColor("Not Sent", ConsoleColors.RED_BACKGROUND_BRIGHT);
						}
					}else
					{
						printlnColor("Vous devez d'abord vous connecter!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
					}
					
					
					break;
					
				case "discover": 
					if(client.getUser().getConnected())
					{
						Object resu = client.discover();
						if(resu != null)
						{
							Vector<Object> result = (Vector<Object>) resu;						
							int size = result.size();
							if(result.size()>0)
							{
								for(int i=0; i<size; i++)
								{
									Vector<Object> vec = (Vector<Object>)result.get(i);
									
									printlnColor(((Vector<Object>)vec.get(0)).get(0)+":", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
									System.out.println(((Vector<Object>)vec.get(0)).get(1));
									System.out.println("Date:" + ((Vector<Object>)vec.get(0)).get(2).toString());
									if(((Boolean)vec.get(1)).booleanValue() == true)
									{
										printlnColor("Message Vu", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
									}else
									{
										printlnColor("Nouveau Message", ConsoleColors.PURPLE_BACKGROUND_BRIGHT);
									}
									sautDeLigne(2);
										
								}
							}else
							{
								printlnColor("Il n'y a aucun message!", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
							}
						}
						else
						{
							printlnColor("Il n'y a aucun message!", ConsoleColors.CYAN_BACKGROUND_BRIGHT);
						}
						
					}else
					{
						printlnColor("Vous devez vous connecter d'abord!", ConsoleColors.YELLOW_BACKGROUND_BRIGHT);
					}
					break;
					
				case "menu": 
					menu();
					break;
					
				case "quit": 
					quit = true;
					break;
					
				default:
					menu();
			}
			
			
		}while(!quit);
	}
	
	public Client() {
		this.user = new User();
	}

	/** <i>Methode de classe et non d'instance</i>
	 * Permet d'afficher le menu de commandes disponibles pour les utilisateurs 
	 */
	private static void menu()
	{
		System.out.println("Voici les commandes pour utiliser MT98SAPP, votre ChatRoom");
		System.out.println("1. login");
		System.out.println("2. logout");
		System.out.println("3. unsubscribe");
		System.out.println("4. subscribe");
		System.out.println("5. chatters");
		System.out.println("6. broadcasting");
		System.out.println("7. discover");
		System.out.println("8. menu");
		System.out.println("9. quit");
	}
	
	/** <i>Methode de classe et non d'instance</i>
	 * Permet de sauter un nombre donne de ligne
	 */
	private static void sautDeLigne(int pas)
	{
		for(int i=0; i<pas; i++)
		{
			System.out.println("");
		}
	}

	/** Permet à un chatter de pouvoir se connecter
	 * @param pseudo
	 * @param password
	 * @return
	 */
	@Override
	public boolean login(String pseudo, String password)
	{
		Vector params = new Vector();
		params.addElement(pseudo);
		params.addElement(password);
		
		return ((Boolean) request(SERVER_ALIAS, "login", params)).booleanValue();
	}
	
	/** Permet à un chatter de pouvoir se deconnecter
	 * @param pseudo
	 * @param password
	 * @return
	 */
	@Override
	public boolean logout (String pseudo, String password)
	{
		Vector params = new Vector();
		params.addElement(pseudo);
		params.addElement(password);
		
		return ((Boolean)request(SERVER_ALIAS, "logout", params)).booleanValue();
	}
	
	/** Permet à un chatter de pouvoir se désincrire du room
	 * @param password
	 * @return
	 */
	@Override
	public boolean unsubscribe(String password)
	{
		if(this.getUser().getConnected()==true && password.equals(this.getUser().getPassword()))
		{
			Vector params = new Vector();
			params.addElement(this.getUser().getPseudo());
			params.addElement(this.getUser().getPassword());
			
			return ((Boolean) request(SERVER_ALIAS, "unsubscribe", params)).booleanValue();
		}
		return false;
		
	}
	
	/** Permet à un nouveau de pouvoir s'inscrire au room
	 * 
	 * @param nom
	 * @param prenom
	 * @param pseudo
	 * @param password
	 * @return
	 */
	@Override
	public boolean subscribe(String nom, String prenom, String pseudo, String password)
	{
		Vector params = new Vector();
		params.addElement(nom);
		params.addElement(prenom);
		params.addElement(pseudo);
		params.addElement(password);
		
		return ((Boolean) request(SERVER_ALIAS, "subscribe", params)).booleanValue();
	}
	
	/** Permet au chatter de connaitre l'ensemble des membres du room
	 * @return
	 */
	@Override
	public Object chatters()
	{
		Vector params = new Vector();
		params.addElement(this.getUser().getPseudo());
		return  request(SERVER_ALIAS, "chatters", params);
	}
	
	/** Permet au chatter de diffuser un message dans le room
	 * @param message
	 * @return
	 */
	@Override
	public boolean broadcasting(String message)
	{
		Vector params = new Vector();
		params.addElement(message);
		params.addElement(this.getUser().getPseudo());
		Object result = request(SERVER_ALIAS, "broadcasting", params);
		if(result != null)
		{
			return ((Boolean) result).booleanValue();
		}else
		{
			return false;
		}
		
	}
	
	/** Permet au chatter de consulter sa boite de reception
	 * @return
	 */
	@Override
	public Object discover()
	{
		Vector params = new Vector();
		params.addElement(this.getUser().getPseudo());
		return request(SERVER_ALIAS, "discover", params);
	}
	
	
	/** Permet au client d'envoyer des requetes RPC au server
	 * 
	 * @param serverAlias
	 * @param methodName
	 * @param params
	 * @return
	 */
	@Override
	public Object request(String serverAlias, String methodName, Vector params)
	{
		try
		{
			XmlRpcClient server = new XmlRpcClient("http://"+SERVER_DNS+":"+SERVER_PORT+"/RPC2");
			Object result = server.execute(serverAlias+"."+methodName, params);  
			return result;  
		}catch(Exception exception)
		{
			System.err.println("JavaClient: " + exception); 
			return null;
		}		
	}
}
