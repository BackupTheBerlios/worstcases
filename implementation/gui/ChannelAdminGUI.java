/*
 * ChannelAdminGUI.java
 *
 * Created on May 24, 2001, 9:32 PM
 */

package gui;

import Client.*;
import java.util.Vector;

/**
 * Der Frame ChannelAdminGUI enthält die Funktionen der GUI, um alle channelbezogenen
 * Daten abzufragen, zu ändern und neu zu setzen. Dieser Frame kann von der ChatGui
 * aufgerufen werden, falls es sich bei dem Benutzer um einen Admin handelt.
 */
public class ChannelAdminGUI extends java.awt.Frame {
    
    gui.ChatGui chatGui;

    /** 
     * Konstruktor, setzt Attribute, initialisiert die grafischen Komponenten
     * und fügt einen WindowListener hinzu, der das Schließen des Frames ermöglicht.
     */
    public ChannelAdminGUI(gui.ChatGui paramChatGui) {
        this.chatGui = paramChatGui;
        initComponents();
        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            });
        pack();
    }

    /**
     * Diese Methode wird vom Konstruktor aufgerufen, um die grafische
     * Oberfläche zu initialisieren.
     */
    private void initComponents() { //GEN-BEGIN:initComponents
        channelNameLabel = new java.awt.Label();
        channelName = new java.awt.TextField();
        channelListLabel = new java.awt.Label();
        channelList = new java.awt.List();
        editChannel = new java.awt.Button();
        newChannel = new java.awt.Button();
        copyChannel = new java.awt.Button();
        saveChannel = new java.awt.Button();
        deleteChannel = new java.awt.Button();
        passiveUsersLabel = new java.awt.Label();
        passiveUsers = new java.awt.List();
        addToChannel = new java.awt.Button();
        removeFromChannel = new java.awt.Button();
        activeUsersLabel = new java.awt.Label();
        activeUsers = new java.awt.List();
        allowedForGuests = new java.awt.Checkbox();
        setLayout(
            new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        setFont(new java.awt.Font ("SansSerif", 0, 12));
        setTitle(gui.ChatGui.PRODUCT_NAME + " - ChannelAdministration");
        setBackground(new java.awt.Color(204, 204, 204));
        channelNameLabel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        channelNameLabel.setName("channelNameLabel");
        channelNameLabel.setBackground(
            new java.awt.Color(204, 204, 204));
        channelNameLabel.setForeground(java.awt.Color.black);
        channelNameLabel.setText("Channelname");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 15);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        add(channelNameLabel, gridBagConstraints1);
        channelName.setBackground(java.awt.Color.white);
        channelName.setName("channelNameField");
        channelName.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        channelName.setForeground(java.awt.Color.black);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 4;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = 8;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        add(channelName, gridBagConstraints1);
        channelListLabel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        channelListLabel.setName("channelListLabel");
        channelListLabel.setBackground(
            new java.awt.Color(204, 204, 204));
        channelListLabel.setForeground(java.awt.Color.black);
        channelListLabel.setText("Channelliste");
        channelListLabel.setAlignment(java.awt.Label.CENTER);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 12;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        add(channelListLabel, gridBagConstraints1);
        channelList.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        channelList.setName("channelList");
        channelList.setBackground(java.awt.Color.white);
        channelList.setForeground(java.awt.Color.black);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 12;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 1.0;
        add(channelList, gridBagConstraints1);
        editChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        editChannel.setLabel("Bearbeiten");
        editChannel.setName("editChannelButton");
        editChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        editChannel.setForeground(java.awt.Color.black);
        editChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    editChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 15, 0);
        gridBagConstraints1.weightx = 1.0;
        add(editChannel, gridBagConstraints1);
        newChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        newChannel.setLabel("Neu");
        newChannel.setName("newChannelButton");
        newChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        newChannel.setForeground(java.awt.Color.black);
        newChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    newChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 6;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 15, 0);
        gridBagConstraints1.weightx = 1.0;
        add(newChannel, gridBagConstraints1);
        copyChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        copyChannel.setLabel("Kopieren");
        copyChannel.setName("copyChannelButton");
        copyChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        copyChannel.setForeground(java.awt.Color.black);
        copyChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    copyChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 4;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 15, 0);
        gridBagConstraints1.weightx = 1.0;
        add(copyChannel, gridBagConstraints1);
        saveChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        saveChannel.setLabel("Speichern");
        saveChannel.setName("saveChannelButton");
        saveChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        saveChannel.setForeground(java.awt.Color.black);
        saveChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    saveChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 6;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 6;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 0, 15, 10);
        gridBagConstraints1.weightx = 1.0;
        add(saveChannel, gridBagConstraints1);
        deleteChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        deleteChannel.setLabel("L\u00f6schen");
        deleteChannel.setName("deleteChannelButton");
        deleteChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        deleteChannel.setForeground(java.awt.Color.black);
        deleteChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    deleteChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 8;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 15, 10);
        gridBagConstraints1.weightx = 1.0;
        add(deleteChannel, gridBagConstraints1);
        passiveUsersLabel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        passiveUsersLabel.setName("passiveUsersLabel");
        passiveUsersLabel.setBackground(
            new java.awt.Color(204, 204, 204));
        passiveUsersLabel.setForeground(java.awt.Color.black);
        passiveUsersLabel.setText("Nicht berechtigte User");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(15, 10, 0, 0);
        add(passiveUsersLabel, gridBagConstraints1);
        passiveUsers.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        passiveUsers.setMultipleMode(true);
        passiveUsers.setName("passiveUsersList");
        passiveUsers.setBackground(java.awt.Color.white);
        passiveUsers.setForeground(java.awt.Color.black);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 0);
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        add(passiveUsers, gridBagConstraints1);
        addToChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        addToChannel.setLabel(">>>");
        addToChannel.setName("addToChannelButton");
        addToChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        addToChannel.setForeground(java.awt.Color.black);
        addToChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    addToChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 5;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(10, 15, 10, 15);
        add(addToChannel, gridBagConstraints1);
        removeFromChannel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        removeFromChannel.setLabel("<<<");
        removeFromChannel.setName("removeFromChannelButton");
        removeFromChannel.setBackground(
            new java.awt.Color(204, 204, 204));
        removeFromChannel.setForeground(java.awt.Color.black);
        removeFromChannel.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    removeFromChannelMouseClicked(evt);
                }
            });
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 5;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(10, 15, 10, 15);
        add(removeFromChannel, gridBagConstraints1);
        activeUsersLabel.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        activeUsersLabel.setName("activeUsersLabel");
        activeUsersLabel.setBackground(
            new java.awt.Color(204, 204, 204));
        activeUsersLabel.setForeground(java.awt.Color.black);
        activeUsersLabel.setText("Berechtigte User");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 7;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(15, 0, 0, 10);
        add(activeUsersLabel, gridBagConstraints1);
        activeUsers.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        activeUsers.setMultipleMode(true);
        activeUsers.setName("activeUsersList");
        activeUsers.setBackground(java.awt.Color.white);
        activeUsers.setForeground(java.awt.Color.black);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 7;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        add(activeUsers, gridBagConstraints1);
        allowedForGuests.setBackground(
            new java.awt.Color(204, 204, 204));
        allowedForGuests.setName("allowedForGuests");
        allowedForGuests.setFont(
            new java.awt.Font("SansSerif", 0, 11));
        allowedForGuests.setForeground(java.awt.Color.black);
        allowedForGuests.setLabel("\u00f6ffentlich");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 4;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.gridwidth = 8;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        add(allowedForGuests, gridBagConstraints1);
    } //GEN-END:initComponents

    /**
     * Diese Methode entfernt den/die ausgewählten Benutzer aus der Liste der diesem
     * Channel zugeordneten Benutzer und fügt sie der Liste der nicht zugeordneten hinzu.
     */
    private void removeFromChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_removeFromChannelMouseClicked
      String[] tmpString = this.activeUsers.getSelectedItems();
      for (int i=0;i<tmpString.length;i++){
       this.activeUsers.remove(tmpString[i]);
       this.passiveUsers.add(tmpString[i]);
      }
    } //GEN-LAST:event_removeFromChannelMouseClicked

    /**
     * Diese Methode fügt den/die ausgewählten Benutzer zur Liste der diesem Channel 
     * zugeordneten Benutzer hinzu und entfernt ihn/sie aus der Liste der nicht zugeordneten.
     */
    private void addToChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_addToChannelMouseClicked
      String[] tmpString = this.passiveUsers.getSelectedItems();
      for (int i=0;i<tmpString.length;i++){
       this.passiveUsers.remove(tmpString[i]);
       this.activeUsers.add(tmpString[i]);
      }
    } //GEN-LAST:event_addToChannelMouseClicked

    /**
     * Diese Methode entfernt den in channelList ausgewählten Channel und fordert über den
     * AdminClient die neue Version der Channelliste an. Zusätzlich werden mittels 
     * newChannelMouseClicked() die Felder der grafischen Oberfläche geleert.
     */
    private void deleteChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_deleteChannelMouseClicked
      String name=this.channelList.getSelectedItem();
      this.chatGui.adminClient.deleteChannel(name);
      this.chatGui.adminClient.getChannelList();
      this.newChannelMouseClicked(evt);
    } //GEN-LAST:event_deleteChannelMouseClicked

    /**
     * Diese Methode ermöglicht die Verwendung von angezeigten Channeldaten für das Anlegen
     * eines neuen Channels.
     */
    private void copyChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_copyChannelMouseClicked
      isNewChannel=true; //Attribut wird für das Speichern benötigt
      this.channelName.setText(this.channelName.getText() + " [Kopie]");
    } //GEN-LAST:event_copyChannelMouseClicked

    /**
     * Diese Methode ermöglicht das Editieren eines vorhandenen Channels und wird außerdem
     * verwendet, um sich die Channeldaten anzeigen zu lassen.
     */
    private void editChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_editChannelMouseClicked
      isNewChannel=false; //Attribut wird für das Speichern benötigt
        String name = this.channelList.getSelectedItem();
        this.chatGui.adminClient.getChannelData(name);
    } //GEN-LAST:event_editChannelMouseClicked

    /**
     * Diese Methode dient dem Speichern von geänderten respektive neu angelegten Channels mit
     * den aktuell sichtbaren Daten. Das Attribut isNewChannel dient hierbei dazu festzustellen,
     * welcher dieser Fälle zutrifft.
     */
    private void saveChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_saveChannelMouseClicked
      if (isNewChannel==true) {
        this.chatGui.adminClient.addChannel(this.channelName.getText(),this.allowedForGuests.getState(),this.chatGui.stringToVector(this.activeUsers.getItems()));
      } else {
        this.chatGui.adminClient.editChannel(this.chatGui.adminClient.getTmpOldChannelName(),this.channelName.getText(),this.allowedForGuests.getState(),this.chatGui.stringToVector(this.activeUsers.getItems()));
      }
      this.chatGui.adminClient.getChannelList();
    } //GEN-LAST:event_saveChannelMouseClicked
    
    /**
     * Diese Methode dient dazu, einen neuen Channel beginnend mit leeren Feldern anzulegen.
     * Hierzu werden die Felder auf ihre Default-Werte gesetzt und isNewChannel gesetzt.
     */
    private void newChannelMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_newChannelMouseClicked
      newChannel();
    } //GEN-LAST:event_newChannelMouseClicked

    private void newChannel() {
      isNewChannel=true;
      Vector tmpVector = this.chatGui.stringToVector( this.chatGui.userAdminGUI.userList.getItems());
      this.chatGui.setChannelData("",false,new Vector(),tmpVector);
    }

    /** Schließen des Applikationsframes */
    private void exitForm(java.awt.event.WindowEvent evt) {
        this.setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    java.awt.Label channelNameLabel;
    java.awt.TextField channelName;
    java.awt.Label channelListLabel;
    java.awt.List channelList;
    java.awt.Button editChannel;
    java.awt.Button newChannel;
    java.awt.Button copyChannel;
    java.awt.Button saveChannel;
    java.awt.Button deleteChannel;
    java.awt.Label passiveUsersLabel;
    java.awt.List passiveUsers;
    java.awt.Button addToChannel;
    java.awt.Button removeFromChannel;
    java.awt.Label activeUsersLabel;
    java.awt.List activeUsers;
    java.awt.Checkbox allowedForGuests;
    // End of variables declaration//GEN-END:variables
    private boolean isNewChannel = true;
 }
