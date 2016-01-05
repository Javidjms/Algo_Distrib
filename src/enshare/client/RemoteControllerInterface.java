/*
 * Copyright 2014 Gwénolé Lecorvé.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package enshare.client;

import document.DocumentInterface;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface qui définit toutes les méthodes appelables à distance sur un
 * contrôleur de client
 *
 * @author Gwénolé Lecorvé
 */
public interface RemoteControllerInterface extends Remote {

	/**
	 * Demande du jeton
	 * @param url l'url du client
	 * @param last l'url du père du client
	 * @return vrai si le client peut avoir le jeton, faux sinon
	 */
	public boolean demandToken(String url,String last) throws RemoteException, FileNotFoundException,MalformedURLException, NotBoundException;
	
	/**
	 * Retourne la liste des enfants du client
	 * @return la liste des url des enfants du client
	 */
    public ArrayList<String> getChild() throws RemoteException;
    /**
	 * Retourne le père du client
	 * @return l'url du père du client
	 */
    public String getLast() throws RemoteException;
	/**
	 * Retourne l'url du client
	 * @return l'url du client
	 */
	public String getUrl() throws RemoteException;
	/**
     * Réceptionne une notification de déconnexion
     *
     * @param sourceUrl URL du processus notifiant
     * @throws RemoteException Si un problème en rapport avec RMI survient
     */
    public void notifyDisconnection(String sourceUrl) throws RemoteException;
	
	/**
     * Change la valeur de last
     * @param lastUrl l'url du pere du client
     * @throws RemoteException
     */
	public void setLast(String lastUrl) throws RemoteException;
	
	/**
	 * Change la valeur du token
	 * @param b nouvelle valeur du token
	 * @throws RemoteException
	 */
	public void setToken(boolean b)throws RemoteException;
	
	/**
     * Met à jour la copie du document courant
     *
     * @param d Nouvelle version du document
     * @param sourceUrl URL du processus expéditeur
     * @throws RemoteException Si un problème en rapport avec RMI survient
     */
    public void updateDocument(String sourceUrl, DocumentInterface d) throws RemoteException;
    /**
	 * Retourne le client qui a le jeton
	 * @return l'url du client qui a le jeton
	 */
	public String whoHasToken() throws MalformedURLException, RemoteException, NotBoundException;
}
