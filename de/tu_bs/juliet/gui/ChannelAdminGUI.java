/* This file is part of Juliet, a chat system.
   Copyright (C) 2001 Andreas Büthe <a.buethe@tu-bs.de>
             (C) 2001 Jan-Henrik Grobe <j-h.grobe@tu-bs.de>
             (C) 2001 Frithjof Hummes <f.hummes@tu-bs.de>
             (C) 2001 Malte Knörr <m.knoerr@tu-bs.de>
	     (C) 2001 Fabian Rotte <f.rotte@tu-bs.de>
	     (C) 2001 Quoc Thien Vu <q.vu@tu-bs.de>

   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package de.tu_bs.juliet.gui;

import java.util.Vector;

import de.tu_bs.juliet.client.*;

/**
 * Der Frame ChannelAdminGUI enthält die Funktionen das GUI, um alle
 * channelbezogenen Daten abzufragen, zu ändern und neu zu setzen.
 * Dieser Frame kann von dem ChatGui aufgerufen werden, falls es sich bei
 * dem Benutzer um einen Admin handelt.
 */
public class ChannelAdminGUI extends java.awt.Frame {

  /** Wurde der Channel neu angelegt? */
  private boolean isNewChannel = true;
  /** Maximale Länge eines Channel-Namens. */
  private int maxLength = 50;
  /** Das ChatGui, zu dem dieses ChannelAdminGUI gehört. */
  ChatGui chatGui;

  // Elemente der GUI
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

  /**
   * Konstruktor, setzt Attribute, initialisiert die grafischen Komponenten
   * und fügt einen WindowListener hinzu, der das Schließen des Frames
   * ermöglicht.
   */
  public ChannelAdminGUI(ChatGui paramChatGui) {

    this.chatGui = paramChatGui;

    initComponents();
    addWindowListener(new java.awt.event.WindowAdapter() {

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
  private void initComponents() {

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

    setLayout(new java.awt.GridBagLayout());

    java.awt.GridBagConstraints gridBagConstraints1;

    setFont(new java.awt.Font("SansSerif", 0, 12));
    setTitle(ChatGui.PRODUCT_NAME + " - ChannelAdministration");
    setBackground(new java.awt.Color(204, 204, 204));
    channelNameLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelNameLabel.setName("channelNameLabel");
    channelNameLabel.setBackground(new java.awt.Color(204, 204, 204));
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
    channelName.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelName.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 4;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);

    add(channelName, gridBagConstraints1);
    channelListLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelListLabel.setName("channelListLabel");
    channelListLabel.setBackground(new java.awt.Color(204, 204, 204));
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
    channelList.setFont(new java.awt.Font("SansSerif", 0, 11));
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
    editChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    editChannel.setLabel("Bearbeiten");
    editChannel.setName("editChannelButton");
    editChannel.setBackground(new java.awt.Color(204, 204, 204));
    editChannel.setForeground(java.awt.Color.black);
    editChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editChannel();
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
    newChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    newChannel.setLabel("Neu");
    newChannel.setName("newChannelButton");
    newChannel.setBackground(new java.awt.Color(204, 204, 204));
    newChannel.setForeground(java.awt.Color.black);
    newChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newChannel();
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
    copyChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    copyChannel.setLabel("Kopieren");
    copyChannel.setName("copyChannelButton");
    copyChannel.setBackground(new java.awt.Color(204, 204, 204));
    copyChannel.setForeground(java.awt.Color.black);
    copyChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyChannel();
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
    saveChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    saveChannel.setLabel("Speichern");
    saveChannel.setName("saveChannelButton");
    saveChannel.setBackground(new java.awt.Color(204, 204, 204));
    saveChannel.setForeground(java.awt.Color.black);
    saveChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveChannel();
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
    deleteChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    deleteChannel.setLabel("L\u00f6schen");
    deleteChannel.setName("deleteChannelButton");
    deleteChannel.setBackground(new java.awt.Color(204, 204, 204));
    deleteChannel.setForeground(java.awt.Color.black);
    deleteChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteChannel();
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
    passiveUsersLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    passiveUsersLabel.setName("passiveUsersLabel");
    passiveUsersLabel.setBackground(new java.awt.Color(204, 204, 204));
    passiveUsersLabel.setForeground(java.awt.Color.black);
    passiveUsersLabel.setText("Nicht berechtigte User");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(15, 10, 0, 0);

    add(passiveUsersLabel, gridBagConstraints1);
    passiveUsers.setFont(new java.awt.Font("SansSerif", 0, 11));
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
    addToChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    addToChannel.setLabel(">>>");
    addToChannel.setName("addToChannelButton");
    addToChannel.setBackground(new java.awt.Color(204, 204, 204));
    addToChannel.setForeground(java.awt.Color.black);
    addToChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addToChannel();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 5;
    gridBagConstraints1.gridy = 7;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.insets = new java.awt.Insets(10, 15, 10, 15);

    add(addToChannel, gridBagConstraints1);
    removeFromChannel.setFont(new java.awt.Font("SansSerif", 0, 11));
    removeFromChannel.setLabel("<<<");
    removeFromChannel.setName("removeFromChannelButton");
    removeFromChannel.setBackground(new java.awt.Color(204, 204, 204));
    removeFromChannel.setForeground(java.awt.Color.black);
    removeFromChannel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        removeFromChannel();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 5;
    gridBagConstraints1.gridy = 8;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.insets = new java.awt.Insets(10, 15, 10, 15);

    add(removeFromChannel, gridBagConstraints1);
    activeUsersLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    activeUsersLabel.setName("activeUsersLabel");
    activeUsersLabel.setBackground(new java.awt.Color(204, 204, 204));
    activeUsersLabel.setForeground(java.awt.Color.black);
    activeUsersLabel.setText("Berechtigte User");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 7;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(15, 0, 0, 10);

    add(activeUsersLabel, gridBagConstraints1);
    activeUsers.setFont(new java.awt.Font("SansSerif", 0, 11));
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
    allowedForGuests.setBackground(new java.awt.Color(204, 204, 204));
    allowedForGuests.setName("allowedForGuests");
    allowedForGuests.setFont(new java.awt.Font("SansSerif", 0, 11));
    allowedForGuests.setForeground(java.awt.Color.black);
    allowedForGuests.setLabel("\u00f6ffentlich");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 5;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;

    add(allowedForGuests, gridBagConstraints1);
  }

  /**
   * Diese Methode entfernt den/die ausgewählten Benutzer aus der Liste 
   * der diesem Channel zugeordneten Benutzer und fügt sie der Liste der
   * nicht zugeordneten hinzu.
   */
  private void removeFromChannel() {

    String[] tmpString = this.activeUsers.getSelectedItems();

    for (int i = 0; i < tmpString.length; i++) {
      this.activeUsers.remove(tmpString[i]);
      this.passiveUsers.add(tmpString[i]);
    }
  }

  /**
   * Diese Methode fügt den/die ausgewählten Benutzer zur Liste der 
   * diesem Channel zugeordneten Benutzer hinzu und entfernt ihn/sie aus der
   * Liste der nicht zugeordneten.
   */
  private void addToChannel() {

    String[] tmpString = this.passiveUsers.getSelectedItems();

    for (int i = 0; i < tmpString.length; i++) {
      this.passiveUsers.remove(tmpString[i]);
      this.activeUsers.add(tmpString[i]);
    }
  }

  /**
   * Diese Methode entfernt den in channelList ausgewählten Channel und
   * fordert über den AdminClient die neue Version der Channelliste an.
   * Zusätzlich werden mittels newChannel() die Felder der grafischen 
   * Oberfläche geleert.
   */
  private void deleteChannel() {

    String name = this.channelList.getSelectedItem();

    this.chatGui.adminClient.deleteChannel(name);
    this.chatGui.adminClient.getChannelList();
    this.newChannel();
  }

  /**
   * Diese Methode ermöglicht die Verwendung von angezeigten Channeldaten 
   * für das Anlegen eines neuen Channels.
   */
  private void copyChannel() {

    isNewChannel = true;  // Attribut wird für das Speichern benötigt

    this.channelName.setText(this.channelName.getText() + " [Kopie]");
  }

  /**
   * Diese Methode ermöglicht das Editieren eines vorhandenen Channels 
   * und wird außerdem verwendet, um sich die Channeldaten anzeigen zu lassen.
   */
  private void editChannel() {

    isNewChannel = false;  // Attribut wird für das Speichern benötigt

    String name = this.channelList.getSelectedItem();

    this.chatGui.adminClient.getChannelData(name);
  }

  /**
   * Diese Methode dient dazu, einen neuen Channel beginnend mit 
   * leeren Feldern anzulegen. Hierzu werden die Felder auf ihre Standard-Werte
   * gesetzt und isNewChannel gesetzt.
   */
  private void newChannel() {

    isNewChannel = true;

    Vector tmpVector =
      this.chatGui
        .stringToVector(this.chatGui.userAdminGUI.userList.getItems());

    this.chatGui.setChannelData("", false, new Vector(), tmpVector);
  }

  /**
   * Diese Methode dient dem Speichern von geänderten bzw. neu angelegten 
   * Channels mit den aktuell sichtbaren Daten.
   * Das Attribut isNewChannel dient hierbei dazu, festzustellen,
   * welcher dieser Fälle zutrifft.
   */
  private void saveChannel() {

    if (this.channelName.getText().indexOf("#") == -1) {
      if (this.channelName.getText().length() <= maxLength) {
        if (isNewChannel) {
          this.chatGui.adminClient
            .addChannel(this.channelName.getText(),
                        this.allowedForGuests.getState(),
                        this.chatGui
                          .stringToVector(this.activeUsers.getItems()));
        } else {
          this.chatGui.adminClient
            .editChannel(this.chatGui.adminClient.getTmpOldChannelName(),
                         this.channelName.getText(),
                         this.allowedForGuests.getState(),
                         this.chatGui
                           .stringToVector(this.activeUsers.getItems()));
        }

        this.isNewChannel = false;

        this.chatGui.adminClient.getChannelList();
        this.chatGui.setChannelData("", false, new Vector(), new Vector());
      } else {
        chatGui.displayError(
          "Die Länge des Channelnamen überschreitet 50 Zeichen.\nDie Daten wurden nicht gespeichert.");
        this.channelName.requestFocus();
      }
    } else {
      chatGui.displayError(
        "Der Channelname enthält das Zeichen \"#\".\nDa dieses für die zugrundeliegende Datenbank reserviert ist,\nwurden die Daten nicht gespeichert.");
      this.channelName.requestFocus();
    }
  }

  /** Schließen des Applikationsframes. */
  private void exitForm(java.awt.event.WindowEvent evt) {
    this.setVisible(false);
  }

}

