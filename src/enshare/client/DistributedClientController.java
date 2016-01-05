package enshare.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import document.StorableDocument;

import enshare.AbstractIdentifiable;
import enshare.server.Server;
import enshare.server.ServerInterface;

public class DistributedClientController extends AbstractClientController {

    /**
     * Liste qui contient tous les fils du client
     */
    protected ArrayList<String> child;
    /**
     * Booléen pour savoir si le client fait une demande
     */
    protected boolean demand;
    /**
     * Père dans l'arbre
     */
    protected String lastUrl;
    /**
     * Booléen pour savoir si le client a effectué un verrouillage
     */
    protected boolean locked;
    /**
     * Serveur à qui est délégué le contrôle
     */	
    protected ServerInterface server;
    /**
     * Booléen pour savoir si le client possede le jeton
     */
    protected boolean token;
 
	/**
     * Constructeur
     *
     * @param _url URL du contrôleur
     * @param _server Serveur à qui est délégué le contrôle
     * @throws RemoteException Si un problème en rapport avec RMI survient
     * @throws MalformedURLException Si l'URL est mal formée
     */
    public DistributedClientController(String _url, ServerInterface _server) throws RemoteException, MalformedURLException {
        super(_url);
        child =new ArrayList<String>();
        server = _server;
        server.connectNotepad(url);
        demand = false;
    }
	@Override
    public synchronized void closeDocument() throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException {
    	int i;
        if (hasDocument()) {
           setLocked(false);
           String newLeader=electionNextTokenClient(); // Election d'un nouveau leader
           if(getLast()!=null){
        	   ((RemoteControllerInterface) Naming.lookup(getLast())).getChild().add(newLeader);
        	   ((RemoteControllerInterface) Naming.lookup(getLast())).getChild().remove(url);
           }
           if(getChild().size()!=0){
        	   for (i=0;i<getChild().size();i++){
        		   if (getChild().get(i)!=newLeader){
        			   ((RemoteControllerInterface) Naming.lookup(newLeader)).getChild().add(getChild().get(i));
        		   }
        	   }
           }
        
            server.closeDocument(url, fileName, getDocument());
        }
    }
	/**
	 * Demande du jeton
	 * @param url l'url du client
	 * @param last l'url du père du client
	 * @return vrai si le client peut avoir le jeton, faux sinon
	 */
	public synchronized boolean demandToken(String url,String last) throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException {
		
		if(!token){
			return ((RemoteControllerInterface) Naming.lookup(getLast())).demandToken(url,getLast());
		} else {
			if(!isLocked()){
				setToken(false);
				 ((RemoteControllerInterface) Naming.lookup(url)).setToken(true);
				setLast(url);
				((RemoteControllerInterface) Naming.lookup(url)).setLast(null);
				((RemoteControllerInterface) Naming.lookup(url)).getChild().add(getUrl());
				return true;
			} else {
				return false;
			}
		}
		
		
	}
	/**
	 * Election du prochain leader
	 * @return l'url du client élu
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	public String electionNextTokenClient() throws RemoteException, FileNotFoundException{
		if (child.size()!=0){
			String min =child.get(0); // Initialisation arbitraire
			for(String clientUrl : child){
				if(clientUrl.compareTo(min) < 0){
					min=clientUrl;
				}
			}
			return min;
		} else {
			return null;
		}
	}
	@Override
    public synchronized void finalize() {
        super.finalize();
        try {
            server.disconnectNotepad(url);
            Naming.unbind(url);
        } catch (RemoteException ex) {
            /* Nothing */
        } catch (NotBoundException ex) {
            /* Nothing */
        } catch (MalformedURLException ex) {
            /* Nothing */
        }
    }
	/**
	 * Retourne la liste des enfants du client
	 * @return la liste des url des enfants du client
	 */
	public ArrayList<String> getChild() throws RemoteException{
		return child;
	}
	@Override
	public List<String> getDocumentList() throws RemoteException {
		return server.getDocumentList();
	}
	/**
	 * Retourne le père du client
	 * @return l'url du père du client
	 */
	public String getLast() throws RemoteException {
		return lastUrl;
	}
    /**
     * Retourne le jeton
     * @return vrai si le client a le jeton, faux sinon
     */
    public boolean getToken(){
		return token;
	}
	/**
	 * Retourne l'url du client
	 * @return l'url du client
	 */
	public String getUrl() throws RemoteException{
		return url;
	}
	/**
	 * Retourne si le client a effectué un verrouillage
	 * @return vrai si le client a verouille un fichier, faux sinon
	 */
    public boolean isLocked(){
		return locked;
	}

    @Override
    protected synchronized void newDocument(String _fileName, boolean _isLocked) throws FileAlreadyExistsException, IOException {
        observedDocument.setDocument(server.newDocument(url, _fileName, _isLocked));
        setFileName(_fileName);
    }

    @Override
	public synchronized void notifyDisconnection(String sourceUrl) {
        fileName = null;
        observedDocument.setDocument(null);
        locked = false;
    }
	/**
	 * Notifie les autres clients concernant la modification d'un fichier
	 * @throws RemoteException
	 */
    protected synchronized void notifyModificationToClients() throws RemoteException {
    	if (server.getStoredDocuments().containsKey(fileName)) {
            for (RemoteControllerInterface controller : server.getReaders().get(fileName)) {
                if (controller != this) {
                    String clientUrl = controller.getUrl();
                    if (clientUrl != null) {
                        try {
                            System.err.println("Notification du controleur " + clientUrl + " pour le document " + fileName);
                            controller.updateDocument(url, server.getStoredDocuments().get(fileName).getDocument());
                        } catch (ConnectException ex) {
                            // Supprimer le client s'il n'existe plus
                            server.disconnectNotepad(url);
                        } catch (RemoteException ex) {
                            System.err.println("ERREUR: Impossible de notifier le controleur " + clientUrl);
                            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
	
	@Override
    public synchronized void openDocument(String _fileName) throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException {
        closeDocument();
        if(server.getReaders().get(_fileName).isEmpty()){
        	setLast(null);
        	setToken(true);
        }
        else{
        	String urlFirst=server.getReaders().get(_fileName).iterator().next().getUrl();
        	((RemoteControllerInterface) Naming.lookup(urlFirst)).getChild().add(url);
			setLast(urlFirst);
        	setToken(false);
        }
        observedDocument.setDocument(server.getDocument(url, _fileName));
        setFileName(_fileName);
    }
	
    
	@Override
    public synchronized void saveDocument() throws RemoteException {
        server.saveDocument(url, fileName, observedDocument.getDocument());
    }
	
	@Override
	public void setLast(String _lastUrl) throws RemoteException {
		lastUrl=_lastUrl;
	}
	
	/**
	 * Change la valeur de locked
	 * @param locked booléen pour le verouillage d'un fichier
	 */
	public void setLocked(boolean locked) {
		this.locked=locked;
	}

    /**
     * Change la valeur de token
	 * @param token booléen pour le jeton
	 */
	public void setToken(boolean token) throws RemoteException {
		this.token = token;
	}

	@Override
    public synchronized boolean tryLockDocument() throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException {
    	if(!locked&&(hasDocument())){
    		if (demandToken(url,getLast())==false){
    			return false;
    		} else {
    			setLocked(true);
    			if(getLast()!=null){
    				((RemoteControllerInterface) Naming.lookup(getLast())).getChild().remove(url);
    			}
    			return true;
    		}
    	} else {
    		return true;
    	}
    }
	
	@Override	
	public void unlockDocument() throws RemoteException, FileNotFoundException {
		if(hasDocument()&&(isLocked())){
			setLocked(false);
               server.unlockDocument(url, fileName, getDocument());
               Logger.getLogger(Server.class.getName()).log(Level.INFO, "Notepad " + url + " déverrouille document " + fileName);

		}
	}
	/**
	 * Retourne le client qui a le jeton
	 * @return l'url du client qui a le jeton
	 */
	public String whoHasToken() throws MalformedURLException, RemoteException, NotBoundException {
    	if(getToken()==false){
			return ((DistributedClientController) Naming.lookup(getLast())).whoHasToken();
		} 
    	else {
    		return url;
    	}
	}


}
