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

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant d'afficher et de sélectionner un nom dans une liste de noms
 *
 * @author Gwénolé Lecorvé
 */
public class DocumentSelectorView extends javax.swing.JDialog {

    /**
     * Liste des noms de fichiers
     */
    protected List<String> fileNames;

    /**
     * Nom de fichier actuellement sélectionné, null si aucun
     */
    protected String selected;

    /**
     * Construit un nouveau sélectionneur de fichier vide
     */
    public DocumentSelectorView() {
        fileNames = new ArrayList();
        selected = null;
        initComponents();
    }

    /**
     * Construit un nouveau sélectionneur de fichier à partir d'une liste de
     * noms
     *
     * @param list Liste de noms
     */
    public DocumentSelectorView(List<String> list) {
        fileNames = list;
        selected = null;
        initComponents();
    }

    /**
     * Affiche le sélectionneur de fichier et retourne le nom sélectionné
     * lorsque ce dernier est validé par l'utilisateur
     *
     * @return
     */
    public String showAndSelect() {
        setVisible(true);
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new JList(fileNames.toArray());
        blank = new javax.swing.JPanel();
        loadButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Sélection du document"); // NOI18N
        setLocationByPlatform(true);
        setModal(true);

        list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(list);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        blank.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        loadButton.setText("Charger");
        if (fileNames.isEmpty()) {
            loadButton.setEnabled(false);
        }
        else {
            list.setSelectedIndex(0);
        }
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });
        blank.add(loadButton);

        cancelButton.setText("Annuler");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        blank.add(cancelButton);

        getContentPane().add(blank, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Applique les actions à effectuer lorsque le bouton "Charger" est cliqué
     *
     * @param evt L'évènement associé
     */
    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_loadButtonActionPerformed
    /**
     * Applique les actions à effectuer lorsque le bouton "Annuler" est cliqué
     *
     * @param evt L'évènement associé
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        selected = null;
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_cancelButtonActionPerformed
    /**
     * Applique les actions à effectuer lorsqu'un élément de la liste est
     * sélectionné
     *
     * @param evt L'évènement associé
     */
    private void listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listValueChanged
        selected = (String) list.getSelectedValue();
    }//GEN-LAST:event_listValueChanged

    /**
     * Méthode principale
     *
     * @param args Arguments de la liste de commandes
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DocumentSelectorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DocumentSelectorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DocumentSelectorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DocumentSelectorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        List<String> list = new ArrayList();
        list.add("toto");
        list.add("titi");

        DocumentSelectorView selector = new DocumentSelectorView(list);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel blank;
    protected javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JList list;
    protected javax.swing.JButton loadButton;
    // End of variables declaration//GEN-END:variables
}
