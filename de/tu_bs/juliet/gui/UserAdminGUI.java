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
import de.tu_bs.juliet.util.debug.Debug;

/**
 * Das Frame UserAdminGUI enthält die Funktionen der GUI, um alle
 * benutzerbezogenen Daten abzufragen, zu ändern und neu zu setzen. Dieses Frame
 * kann von der ChatGui aufgerufen werden, falls es sich bei dem Benutzer um
 * einen Admin handelt.
 */
public class UserAdminGUI extends java.awt.Frame {

  /** Das ChatGui, zu dem dieses UserAdminGUI gehört.*/
  ChatGui chatGui;

  // Die GUI-Elemente.
  java.awt.Label passwordLabel;
  java.awt.TextField loginName;
  java.awt.Label passwordVerifyLabel;
  java.awt.Label loginNameLabel;
  java.awt.List userList;
  java.awt.TextField password;
  java.awt.TextField passwordVerify;
  java.awt.Checkbox isAdmin;
  java.awt.Label userListLabel;
  java.awt.List passiveChannels;
  java.awt.Button addToActive;
  java.awt.Button removeFromActive;
  java.awt.List activeChannels;
  java.awt.Label passiveChannelsLabel;
  java.awt.Label activeChannelsLabel;
  java.awt.Button editUser;
  java.awt.Button copyUser;
  java.awt.Button newUser;
  java.awt.Button saveUser;
  java.awt.Button deleteUser;

  private boolean isNewUser = true;
  private int maxLength = 20;


  /**
   * Konstruktor, setzt Attribute, initialisiert die grafischen Komponenten
   * und fügt einen WindowListener hinzu, der das Schließen des Frames
   * ermöglicht.
   */
  public UserAdminGUI(ChatGui paramChatGui) {

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
    passwordLabel = new java.awt.Label();
    loginName = new java.awt.TextField();
    passwordVerifyLabel = new java.awt.Label();
    loginNameLabel = new java.awt.Label();
    userList = new java.awt.List();
    password = new java.awt.TextField();
    passwordVerify = new java.awt.TextField();
    isAdmin = new java.awt.Checkbox();
    userListLabel = new java.awt.Label();
    passiveChannels = new java.awt.List();
    addToActive = new java.awt.Button();
    removeFromActive = new java.awt.Button();
    activeChannels = new java.awt.List();
    passiveChannelsLabel = new java.awt.Label();
    activeChannelsLabel = new java.awt.Label();
    editUser = new java.awt.Button();
    copyUser = new java.awt.Button();
    newUser = new java.awt.Button();
    saveUser = new java.awt.Button();
    deleteUser = new java.awt.Button();

    setLayout(new java.awt.GridBagLayout());

    java.awt.GridBagConstraints gridBagConstraints1;

    setFont(new java.awt.Font("SansSerif", 0, 12));
    setTitle(ChatGui.PRODUCT_NAME + " - UserAdministration");
    setBackground(new java.awt.Color(204, 204, 204));
    passwordLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    passwordLabel.setName("passwordLabel");
    passwordLabel.setBackground(new java.awt.Color(204, 204, 204));
    passwordLabel.setForeground(java.awt.Color.black);
    passwordLabel.setText("Passwort");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 3;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;

    add(passwordLabel, gridBagConstraints1);
    loginName.setBackground(java.awt.Color.white);
    loginName.setName("loginField");
    loginName.setFont(new java.awt.Font("SansSerif", 0, 11));
    loginName.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 7;
    gridBagConstraints1.gridy = 2;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

    add(loginName, gridBagConstraints1);
    passwordVerifyLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    passwordVerifyLabel.setName("passwordVerifyLabel");
    passwordVerifyLabel.setBackground(new java.awt.Color(204, 204, 204));
    passwordVerifyLabel.setForeground(java.awt.Color.black);
    passwordVerifyLabel.setText("Wiederholung");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 4;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;

    add(passwordVerifyLabel, gridBagConstraints1);
    loginNameLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    loginNameLabel.setName("loginLabel");
    loginNameLabel.setBackground(new java.awt.Color(204, 204, 204));
    loginNameLabel.setForeground(java.awt.Color.black);
    loginNameLabel.setText("Login Name");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 2;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.insets = new java.awt.Insets(1, 0, 0, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;

    add(loginNameLabel, gridBagConstraints1);
    userList.setFont(new java.awt.Font("SansSerif", 0, 11));
    userList.setName("userList");
    userList.setBackground(java.awt.Color.white);
    userList.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 2;
    gridBagConstraints1.gridwidth = 4;
    gridBagConstraints1.gridheight = 10;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.ipadx = 3;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 30);
    gridBagConstraints1.weightx = 0.5;
    gridBagConstraints1.weighty = 1.0;

    add(userList, gridBagConstraints1);
    password.setBackground(java.awt.Color.white);
    password.setName("passwordField");
    password.setFont(new java.awt.Font("SansSerif", 0, 11));
    password.setEchoChar('*');
    password.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 7;
    gridBagConstraints1.gridy = 3;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

    add(password, gridBagConstraints1);
    passwordVerify.setBackground(java.awt.Color.white);
    passwordVerify.setName("passwordVerifyField");
    passwordVerify.setFont(new java.awt.Font("SansSerif", 0, 11));
    passwordVerify.setEchoChar('*');
    passwordVerify.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 7;
    gridBagConstraints1.gridy = 4;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

    add(passwordVerify, gridBagConstraints1);
    isAdmin.setBackground(new java.awt.Color(204, 204, 204));
    isAdmin.setName("isAdminCheckbox");
    isAdmin.setFont(new java.awt.Font("SansSerif", 0, 11));
    isAdmin.setForeground(java.awt.Color.black);
    isAdmin.setLabel(" ist Admin");

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 7;
    gridBagConstraints1.gridy = 5;
    gridBagConstraints1.gridwidth = 8;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;

    add(isAdmin, gridBagConstraints1);
    userListLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    userListLabel.setName("userListLabel");
    userListLabel.setBackground(new java.awt.Color(204, 204, 204));
    userListLabel.setForeground(java.awt.Color.black);
    userListLabel.setText("Userliste");
    userListLabel.setAlignment(java.awt.Label.CENTER);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 1;
    gridBagConstraints1.gridwidth = 4;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 30);

    add(userListLabel, gridBagConstraints1);
    passiveChannels.setFont(new java.awt.Font("SansSerif", 0, 11));
    passiveChannels.setMultipleMode(true);
    passiveChannels.setName("passiveChannelList");
    passiveChannels.setBackground(java.awt.Color.white);
    passiveChannels.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 7;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.gridheight = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 10, 15);
    gridBagConstraints1.weightx = 1.0;

    add(passiveChannels, gridBagConstraints1);
    addToActive.setFont(new java.awt.Font("SansSerif", 0, 11));
    addToActive.setLabel(">>>");
    addToActive.setName("addToChannels");
    addToActive.setBackground(new java.awt.Color(204, 204, 204));
    addToActive.setForeground(java.awt.Color.black);
    addToActive.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addToActive();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 9;
    gridBagConstraints1.gridy = 9;

    add(addToActive, gridBagConstraints1);
    removeFromActive.setFont(new java.awt.Font("SansSerif", 0, 11));
    removeFromActive.setLabel("<<<");
    removeFromActive.setName("removeFromChannels");
    removeFromActive.setBackground(new java.awt.Color(204, 204, 204));
    removeFromActive.setForeground(java.awt.Color.black);
    removeFromActive.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        removeFromActive();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 9;
    gridBagConstraints1.gridy = 11;

    add(removeFromActive, gridBagConstraints1);
    activeChannels.setFont(new java.awt.Font("SansSerif", 0, 11));
    activeChannels.setMultipleMode(true);
    activeChannels.setName("activeChannelList");
    activeChannels.setBackground(java.awt.Color.white);
    activeChannels.setForeground(java.awt.Color.black);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 10;
    gridBagConstraints1.gridy = 7;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.gridheight = 8;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.insets = new java.awt.Insets(0, 15, 10, 10);
    gridBagConstraints1.weightx = 1.0;

    add(activeChannels, gridBagConstraints1);
    passiveChannelsLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    passiveChannelsLabel.setName("passiveChannelListField");
    passiveChannelsLabel.setBackground(new java.awt.Color(204, 204, 204));
    passiveChannelsLabel.setForeground(java.awt.Color.black);
    passiveChannelsLabel.setText("Inaktive Channels");
    passiveChannelsLabel.setAlignment(java.awt.Label.CENTER);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 4;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.insets = new java.awt.Insets(15, 0, 0, 10);

    add(passiveChannelsLabel, gridBagConstraints1);
    activeChannelsLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    activeChannelsLabel.setName("activeChannelListField");
    activeChannelsLabel.setBackground(new java.awt.Color(204, 204, 204));
    activeChannelsLabel.setForeground(java.awt.Color.black);
    activeChannelsLabel.setText("Aktive Channels");
    activeChannelsLabel.setAlignment(java.awt.Label.CENTER);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 10;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 5;
    gridBagConstraints1.insets = new java.awt.Insets(15, 0, 0, 10);

    add(activeChannelsLabel, gridBagConstraints1);
    editUser.setFont(new java.awt.Font("SansSerif", 0, 11));
    editUser.setLabel("Bearbeiten");
    editUser.setName("editUser");
    editUser.setBackground(new java.awt.Color(204, 204, 204));
    editUser.setForeground(java.awt.Color.black);
    editUser.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editUser();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 12;
    gridBagConstraints1.gridwidth = 4;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 30);

    add(editUser, gridBagConstraints1);
    copyUser.setFont(new java.awt.Font("SansSerif", 0, 11));
    copyUser.setLabel("Kopieren");
    copyUser.setName("copyUserData");
    copyUser.setBackground(new java.awt.Color(204, 204, 204));
    copyUser.setForeground(java.awt.Color.black);
    copyUser.setForeground(java.awt.Color.black);
    copyUser.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyUser();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 13;
    gridBagConstraints1.gridwidth = 4;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 30);

    add(copyUser, gridBagConstraints1);
    newUser.setFont(new java.awt.Font("SansSerif", 0, 11));
    newUser.setLabel("Neu");
    newUser.setName("newUser");
    newUser.setBackground(new java.awt.Color(204, 204, 204));
    newUser.setForeground(java.awt.Color.black);
    newUser.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newUser();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 0;
    gridBagConstraints1.gridwidth = 7;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 15, 0);
    gridBagConstraints1.weightx = 1.0;

    add(newUser, gridBagConstraints1);
    saveUser.setFont(new java.awt.Font("SansSerif", 0, 11));
    saveUser.setLabel("Speichern");
    saveUser.setName("saveUser");
    saveUser.setBackground(new java.awt.Color(204, 204, 204));
    saveUser.setForeground(java.awt.Color.black);
    saveUser.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveUser();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 8;
    gridBagConstraints1.gridy = 0;
    gridBagConstraints1.gridwidth = 7;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 0, 15, 10);
    gridBagConstraints1.weightx = 1.0;

    add(saveUser, gridBagConstraints1);
    deleteUser.setFont(new java.awt.Font("SansSerif", 0, 11));
    deleteUser.setLabel("L\u00f6schen");
    deleteUser.setName("deleteUser");
    deleteUser.setBackground(new java.awt.Color(204, 204, 204));
    deleteUser.setForeground(java.awt.Color.black);
    deleteUser.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteUser();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 14;
    gridBagConstraints1.gridwidth = 4;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 30);
    gridBagConstraints1.weightx = 1.0;

    add(deleteUser, gridBagConstraints1);
  }

  /**
   * Diese Methode entfernt den/die ausgewählten Channels aus der Liste der für
   * diesen Benutzer zugelassenen Channels und fügt sie der Liste der nicht
   * zugelassenen hinzu.
   */
  private void removeFromActive() {

    String[] tmpString = this.activeChannels.getSelectedItems();

    for (int i = 0; i < tmpString.length; i++) {
      this.activeChannels.remove(tmpString[i]);
      this.passiveChannels.add(tmpString[i]);
    }
  }

  /**
   * Diese Methode fügt den/die ausgewählten Channels zur Liste der für diesen
   * Benutzer zugelassenen Channels hinzu und entfernt ihn/sie aus der Liste der
   * nicht zugelassenen.
   */
  private void addToActive() {

    String[] tmpString = this.passiveChannels.getSelectedItems();

    for (int i = 0; i < tmpString.length; i++) {
      this.passiveChannels.remove(tmpString[i]);
      this.activeChannels.add(tmpString[i]);
    }
  }

  /**
   * Diese Methode entfernt den in userList ausgewählten Benutzer und fordert
   * über den AdminClient die neue Version der Benutzerliste an. Zusätzlich
   * werden mittels newUserMouseClicked() die Felder der grafischen Oberfläche
   * geleert.
   */
  private void deleteUser() {

    String name = this.userList.getSelectedItem();

    this.chatGui.adminClient.deleteUser(name);
    this.chatGui.adminClient.getUserList();
    this.newUser();
  }

  /**
   * Diese Methode ermöglicht die Verwendung von angezeigten Benutzerdaten für
   * das Anlegen eines neuen Benutzers.
   */
  private void copyUser() {

    isNewUser = true;  // Attribut wird für das Speichern benötigt

    this.loginName.setText(this.loginName.getText() + " [Kopie]");
  }

  /**
   * Diese Methode ermöglicht das Editieren eines vorhandenen Benutzers und
   * wird außerdem verwendet, um sich die Benutzerdaten anzeigen zu lassen.
   */
  private void editUser() {

    isNewUser = false;  // Attribut wird für das Speichern benötigt

    String name = this.userList.getSelectedItem();

    this.chatGui.adminClient.getUserData(name);
  }

  /**
   * Diese Methode dient dem Speichern von geänderten respektive neu angelegten
   * Benutzern mit den aktuell sichtbaren Daten. Das Attribut isNewUser dient
   * hierbei dazu festzustellen, welcher dieser Fälle zutrifft. Zunächst wird
   * allerdings überprüft, ob das eingegebene Passwort und die Wiederholung
   * übereinstimmen, sonst wird ein neuer Frame mit einer Fehlermeldung
   * geöffnet.
   */
  private void saveUser() {

    if ((this.loginName.getText().indexOf("#") == -1)
            && (this.password.getText().indexOf("#") == -1)) {
      if (this.loginName.getText().length() <= maxLength) {
        if (this.password.getText().compareTo(this.passwordVerify.getText())
                == 0) {
          if (isNewUser) {
            this.chatGui.adminClient
              .addUser(this.loginName.getText(), this.password.getText(),
                       this.isAdmin.getState(),
                       this.chatGui
                         .stringToVector(this.activeChannels.getItems()));
          } else {
            this.chatGui.adminClient
              .editUser(this.chatGui.adminClient.getTmpOldUserName(),
                        this.loginName.getText(), this.password.getText(),
                        this.isAdmin.getState(),
                        this.chatGui
                          .stringToVector(this.activeChannels.getItems()));
          }

          this.isNewUser = false;

          this.chatGui.setUserData("", "", false, new Vector(), new Vector());
          this.chatGui.adminClient.getUserList();
        } else {
          chatGui.displayError(
            "Passwort und Wiederholung stimmen nicht überein.\nDie Daten wurden nicht gespeichert.");
          this.passwordVerify.requestFocus();
        }
      } else {
        chatGui.displayError(
          "Die Länge des Loginnamen überschreitet 20 Zeichen.\nDie Daten wurden nicht gespeichert.");
        this.loginName.requestFocus();
      }
    } else {
      chatGui.displayError(
        "Loginname respektive Password enthalten das Zeichen \"#\".\nDa dieses für die zugrundeliegende Datenbank reserviert ist,\nwurden die Daten nicht gespeichert.");
      this.loginName.requestFocus();
    }
  }

  /**
   * Diese Methode dient dazu, beginnend mit leeren Feldern einen neuen
   * Benutzer anzulegen. Hierzu werden die Felder auf ihre Standard-Werte
   * gesetzt und isNewUser gesetzt.
   */
  private void newUser() {

    isNewUser = true;

    Vector tmpVector =
      this.chatGui
        .stringToVector(this.chatGui.channelAdminGUI.channelList.getItems());

    this.chatGui.setUserData("", "", false, new Vector(), tmpVector);
  }

  /** Schließt das Applikationsframe. */
  private void exitForm(java.awt.event.WindowEvent evt) {
    this.setVisible(false);
  }
}
