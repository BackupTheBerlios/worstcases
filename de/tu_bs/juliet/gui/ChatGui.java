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
import java.util.Enumeration;

import de.tu_bs.juliet.client.AdminClient;
import de.tu_bs.juliet.util.debug.Debug;

/**
 * Das GUI, mit dem der Benutzer chatten kann.
 */
public class ChatGui extends java.applet.Applet {

  /** Der Produktname. Wird in Titelleisten etc. angezeigt. */
  public static final String PRODUCT_NAME = "Juliet";

  /** Der AdminClient. */
  AdminClient adminClient = new AdminClient();
  /** Das ChannelAdminGUI. */
  ChannelAdminGUI channelAdminGUI = new ChannelAdminGUI(this);
  /** Das UserAdminGUI. */
  UserAdminGUI userAdminGUI = new UserAdminGUI(this);

  // Die GUI-Elemente.
  private java.awt.Panel mainpanel;
  private java.awt.Panel loginpanel;
  private java.awt.TextField loginName;
  private java.awt.TextField password;
  private java.awt.Label loginNameLabel;
  private java.awt.Label passwordLabel;
  private java.awt.Checkbox isGuest;
  private java.awt.Button login;
  private java.awt.Panel chatpanel;
  private java.awt.TextArea chatText;
  private java.awt.TextField msg;
  private java.awt.Button sendMsg;
  private java.awt.Choice channelChoice;
  private java.awt.Label channelChoiceLabel;
  private java.awt.List userList;
  private java.awt.Label userListLabel;
  private java.awt.Button logout;
  private java.awt.Button channelMsgBuffer;
  private java.awt.Button channelAdmin;
  private java.awt.Button userAdmin;
  private java.awt.CardLayout cardLayout = new java.awt.CardLayout();
  /** Fenster für das Gesprächsprotokoll. */
  private LogText logText = new LogText();
  private boolean cardFlipped = false;
  /** Breite des chatText-Elements. */
  private int chatTextColumns = 70;
  /** Höhe deschatText-Elements. */
  private int chatTextRows = 20;

  /** Initialisiert das GUI. */
  public void init() {

    this.adminClient.setServerIP(this.getCodeBase().getHost());

    // adminClient.SERVER_IP = "134.169.42.3";
    Debug.println("host set to " + this.adminClient.getServerIP());
    Debug.println(Debug.MEDIUM,
                  this + "this codebase: " + this.getCodeBase());

    this.adminClient.gui = this;

    initComponents();
    loginName.requestFocus();
  }

  /**
   * Zeigt eine beliebige Fehlermeldung an.
   * @param msg - die anzuzeigende Fehlermeldung.
   */
  public void displayError(String msg) {

    ErrorMessage errormessage = new ErrorMessage(msg);

    errormessage.show();
  }

  /**
   * Zeigt eine Fehlermeldung an, wenn beim Anmelden ein Fehler auftritt. Das
   * GUI wird auf den Anmelde-Screen zurückgesetzt.
   */
  public synchronized void loginError() {

    displayError(
      "Das Anmelden ist fehlgeschlagen.\nBitte versuchen Sie es erneut.");
    this.cardLayout.first(mainpanel);
  }

  /**
   * Hilsmethode, wandelt einen Array von Strings in einen Vector um.
   */
  public static Vector stringToVector(String[] arr) { //FIXME: auslagern?

    if (arr != null) {
      Vector tmpVector = new Vector();

      for (int i = 0; i < arr.length; i++) {
        tmpVector.addElement(arr[i]);
      }

      return tmpVector;
    } else {
      return (new Vector());
    }
  }

  /**
   * Zeigt die im Channel befindlichen User im GUI an.
   * @param name - Name des Channels (hier noch unbenutzt).
   * @param userNames - Vector mit den User-Namen.
   */
  public synchronized void setCurrentChannelData(String name,
          Vector userNames) { //FIXME: name wird nicht benutzt

    String selectedUser = new String();

    if (this.userList.getSelectedItem() != null) {
      selectedUser = this.userList.getSelectedItem();
    }

    this.userList.removeAll();

    Enumeration enum;

    if (userNames != null) {
      enum = userNames.elements();
    } else {
      enum = (new Vector()).elements();
    }

    this.userList.add("Alle");

    while (enum.hasMoreElements()) {
      this.userList.add((String) enum.nextElement());
    }

    this.userList.select(0);

    for (int i = 0; i < userList.getItemCount(); i++) {
      if (userList.getItem(i).compareTo(selectedUser) == 0) {
        this.userList.select(i);
      }
    }
  }

  /**
   * Zeigt alle Channels im AdminGUI an.
   * @param channelNames - Liste alle Channels.
   */
  //FIXME: gehört eigentlich in die Klasse AdminGUI.
  public synchronized void setChannelList(Vector channelNames) {

    this.channelAdminGUI.channelList.removeAll();

    Enumeration enum = channelNames.elements();

    while (enum.hasMoreElements()) {
      this.channelAdminGUI.channelList.add((String) enum.nextElement());
    }

    this.userAdminGUI.userList.select(0);
  }

  /**
   * Zeigt alle User im AdminGUI an.
   * @param userNames - Liste aller User.
   */
  //FIXME: gehört eigentlich in die Klasse AdminGUI.
  public synchronized void setUserList(Vector userNames) {

    this.userAdminGUI.userList.removeAll();

    Enumeration enum = userNames.elements();

    while (enum.hasMoreElements()) {
      this.userAdminGUI.userList.add((String) enum.nextElement());
    }

    this.userAdminGUI.userList.select(0);
  }

  /**
   * Zeigt die Daten eines Channels im AdminGUI an.
   * @param channelName - der Name des Channels.
   * @param isAllowedForGuest - darf Channel von Gästen betreten werden.
   * @param userNames - User, die diesen Channel betreten dürfen.
   * @param passiveUserNames - User, die diesen Channel nicht betreten dürfen.
   */
  //FIXME: gehört eigentlich in die Klasse AdminGUI.
  public synchronized void setChannelData(String channelName,
                                          boolean isAllowedForGuest,
                                          Vector userNames,
                                          Vector passiveUserNames) {

    this.channelAdminGUI.channelName.setText(channelName);
    this.channelAdminGUI.allowedForGuests.setState(isAllowedForGuest);
    this.channelAdminGUI.activeUsers.removeAll();

    if (userNames == null) {
      userNames = new Vector();
    }

    Enumeration enum = userNames.elements();

    while (enum.hasMoreElements()) {
      this.channelAdminGUI.activeUsers.add((String) enum.nextElement());
    }

    this.channelAdminGUI.passiveUsers.removeAll();

    Enumeration enum2 = passiveUserNames.elements();

    while (enum2.hasMoreElements()) {
      this.channelAdminGUI.passiveUsers.add((String) enum2.nextElement());
    }
  }

  /**
   * Zeigt die Daten eines Users im AdminGUI an.
   * @param userName - der Name des Users.
   * @param password - das Kennwort des Users.
   * @param isAdmin - hat der Benutzer Admin-Rechte?
   * @param channelNames - Channels, die der User betreten darf.
   * @param passiveChannelNames -  Channels, die der User nicht betreten darf.
   */
  //FIXME: gehört eigentlich in die Klasse AdminGUI.
  public void setUserData(String userName, String password, boolean isAdmin,
                          Vector channelNames, Vector passiveChannelNames) {

    this.userAdminGUI.loginName.setText(userName);
    this.userAdminGUI.password.setText(password);
    this.userAdminGUI.passwordVerify.setText(password);
    this.userAdminGUI.isAdmin.setState(isAdmin);
    this.userAdminGUI.activeChannels.removeAll();

    if (channelNames == null) {
      channelNames = new Vector();
    }

    Enumeration enum = channelNames.elements();

    while (enum.hasMoreElements()) {
      this.userAdminGUI.activeChannels.add((String) enum.nextElement());
    }

    this.userAdminGUI.passiveChannels.removeAll();

    Enumeration enum2 = passiveChannelNames.elements();

    while (enum2.hasMoreElements()) {
      this.userAdminGUI.passiveChannels.add((String) enum2.nextElement());
    }
  }

  /**
   * Zeigt eine empfangene Nachricht im GUI an.
   * @param fromName - Name des Users, von dem die Nachricht stammt.
   * @param msg - die Nachricht.
   */
  public synchronized void sendMsgFromChannel(String fromName, String msg) {
    this.chatText.append(fromName + ": " + msg + "\n");
    this.chatText.setCaretPosition(this.chatText.getText().length());
  }

  /**
   * Zeigt eine empfangene geflüsterte Nachricht an.
   * @param fromName - Name des Users, von dem die Nachricht stammt.
   * @param msg - die Nachricht.
   */
  public synchronized void sendMsgFromUser(String fromName, String msg) {
    this.chatText.append(fromName + " flüstert: " + msg + "\n");
    this.chatText.setCaretPosition(this.chatText.getText().length());
  }

  /**
   * Zeigt die aktuellen Daten des Users im GUI an.
   * @param name - der Name des Benutzers (wird noch nicht benutzt).
   * @param isAdmin - hat der Benutzer Admin-Rechte.
   * @param channelNames - aktuell verfügbare Channel.
   * @param currentChannelName - der Channel, in dem der User gerade ist.
   */
  public synchronized void setCurrentUserData(String name, boolean isAdmin,
                                              Vector channelNames,
                                              String currentChannelName) {
    //FIXME: name wird (noch) nicht benutzt.
    String oldSelectedChannelName = this.channelChoice.getSelectedItem();
    Enumeration enum;
    String tmpName;

    this.channelChoice.removeAll();

    if (channelNames != null) {
      enum = channelNames.elements();
    } else {
      enum = (new Vector()).elements();
    }

    while (enum.hasMoreElements()) {
      tmpName = (String) enum.nextElement();

      this.channelChoice.add(tmpName);
      Debug.println(tmpName + " added to channelist");
    }

    this.channelChoice.setSize(this.channelChoice.getPreferredSize());
    this.repaint();

    if (isAdmin) {
      this.channelAdmin.setEnabled(true);
      this.userAdmin.setEnabled(true);
    } else {
      this.channelAdmin.setEnabled(false);
      this.userAdmin.setEnabled(false);
    }

    if (currentChannelName != null) {

      // leert das ChatTextFeld, wenn serverseitig der aktuelle Channel verändert wurde
      if (oldSelectedChannelName.compareTo(currentChannelName) != 0) {
        this.chatText.setText("");
      }

      // setzt die Channelauswahl auf den aktuellen Channelnamen
      this.channelChoice.select(currentChannelName);
    } else {
      this.adminClient.joinChannel("Foyer");
    }

    if (!cardFlipped) {
      this.cardFlipped = true;

      this.cardLayout.next(mainpanel);
    }
  }

  /**
   * Diese Methode wird von init() aufgerufen, um das GUI zu initialisieren.
   */
  private void initComponents() {

    mainpanel = new java.awt.Panel();
    loginpanel = new java.awt.Panel();
    loginName = new java.awt.TextField();
    password = new java.awt.TextField();
    loginNameLabel = new java.awt.Label();
    passwordLabel = new java.awt.Label();
    isGuest = new java.awt.Checkbox();
    login = new java.awt.Button();
    chatpanel = new java.awt.Panel();
    chatText =
      new java.awt.TextArea("", chatTextRows, chatTextColumns,
                            java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
    msg = new java.awt.TextField();
    sendMsg = new java.awt.Button();
    channelChoice = new java.awt.Choice();
    channelChoiceLabel = new java.awt.Label();
    userList = new java.awt.List();
    userListLabel = new java.awt.Label();
    logout = new java.awt.Button();
    channelMsgBuffer = new java.awt.Button();
    channelAdmin = new java.awt.Button();
    userAdmin = new java.awt.Button();

    setLayout(new java.awt.GridBagLayout());

    java.awt.GridBagConstraints gridBagConstraints1;

    mainpanel.setLayout(this.cardLayout);
    mainpanel.setFont(new java.awt.Font("SansSerif", 0, 11));
    mainpanel.setName("mainpanel");
    mainpanel.setBackground(new java.awt.Color(204, 204, 204));
    mainpanel.setForeground(java.awt.Color.black);
    loginpanel.setLayout(new java.awt.GridBagLayout());

    java.awt.GridBagConstraints gridBagConstraints2;

    loginpanel.setFont(new java.awt.Font("SansSerif", 0, 11));
    loginpanel.setName("loginpanel");
    loginpanel.setBackground(new java.awt.Color(204, 204, 204));
    loginpanel.setForeground(java.awt.Color.black);
    loginName.setBackground(java.awt.Color.white);
    loginName.setName("loginName");
    loginName.setFont(new java.awt.Font("SansSerif", 0, 11));
    loginName.setColumns(20);
    loginName.setForeground(java.awt.Color.black);
    loginName.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
          login();
        }
      }
    });

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 1;
    gridBagConstraints2.gridy = 0;

    loginpanel.add(loginName, gridBagConstraints2);
    password.setBackground(java.awt.Color.white);
    password.setName("password");
    password.setFont(new java.awt.Font("SansSerif", 0, 11));
    password.setEchoChar('*');
    password.setColumns(20);
    password.setForeground(java.awt.Color.black);
    password.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
          login();
        }
      }
    });

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 1;
    gridBagConstraints2.gridy = 1;
    gridBagConstraints2.insets = new java.awt.Insets(10, 0, 0, 0);

    loginpanel.add(password, gridBagConstraints2);
    loginNameLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    loginNameLabel.setName("loginNameLabel");
    loginNameLabel.setBackground(new java.awt.Color(204, 204, 204));
    loginNameLabel.setForeground(java.awt.Color.black);
    loginNameLabel.setText("Benutzername:");

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 0;
    gridBagConstraints2.gridy = 0;
    gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;

    loginpanel.add(loginNameLabel, gridBagConstraints2);
    passwordLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    passwordLabel.setName("passwordLabel");
    passwordLabel.setBackground(new java.awt.Color(204, 204, 204));
    passwordLabel.setForeground(java.awt.Color.black);
    passwordLabel.setText("Kennwort:");

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 0;
    gridBagConstraints2.gridy = 1;
    gridBagConstraints2.insets = new java.awt.Insets(10, 0, 0, 0);

    loginpanel.add(passwordLabel, gridBagConstraints2);
    isGuest.setBackground(new java.awt.Color(204, 204, 204));
    isGuest.setName("isGuest");
    isGuest.setFont(new java.awt.Font("SansSerif", 0, 11));
    isGuest.setForeground(java.awt.Color.black);
    isGuest.setLabel("Gast");
    isGuest.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        isGuestItemStateChanged(evt);
      }
    });
    isGuest.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
          login();
        }
      }
    });

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 2;
    gridBagConstraints2.gridy = 0;
    gridBagConstraints2.insets = new java.awt.Insets(0, 10, 0, 0);

    loginpanel.add(isGuest, gridBagConstraints2);
    login.setFont(new java.awt.Font("SansSerif", 0, 11));
    login.setLabel("Anmelden");
    login.setName("login");
    login.setBackground(java.awt.Color.lightGray);
    login.setForeground(java.awt.Color.black);
    login.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
          login();
        }
      }
    });
    login.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        loginActionPerformed(e);
      }
    });

    gridBagConstraints2 = new java.awt.GridBagConstraints();
    gridBagConstraints2.gridx = 1;
    gridBagConstraints2.gridy = 2;
    gridBagConstraints2.insets = new java.awt.Insets(10, 0, 0, 0);

    loginpanel.add(login, gridBagConstraints2);
    mainpanel.add(loginpanel, "card1");
    chatpanel.setLayout(new java.awt.GridBagLayout());

    java.awt.GridBagConstraints gridBagConstraints3;

    chatpanel.setFont(new java.awt.Font("SansSerif", 0, 11));
    chatpanel.setName("chatpanel");
    chatpanel.setBackground(new java.awt.Color(204, 204, 204));
    chatpanel.setForeground(java.awt.Color.black);
    chatText.setBackground(java.awt.Color.lightGray);
    chatText.setName("chatText");
    chatText.setEditable(false);
    chatText.setFont(new java.awt.Font("SansSerif", 0, 11));
    chatText.setForeground(java.awt.Color.black);

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 0;
    gridBagConstraints3.gridy = 1;
    gridBagConstraints3.gridwidth = 2;
    gridBagConstraints3.gridheight = 3;

    chatpanel.add(chatText, gridBagConstraints3);
    msg.setBackground(java.awt.Color.white);
    msg.setName("msg");
    msg.setFont(new java.awt.Font("SansSerif", 0, 11));
    msg.setColumns(70);
    msg.setForeground(java.awt.Color.black);
    msg.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
          sendMsg();
        }
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 0;
    gridBagConstraints3.gridy = 4;
    gridBagConstraints3.gridwidth = 2;
    gridBagConstraints3.insets = new java.awt.Insets(10, 0, 0, 0);
    gridBagConstraints3.weightx = 1.0;

    chatpanel.add(msg, gridBagConstraints3);
    sendMsg.setFont(new java.awt.Font("SansSerif", 0, 11));
    sendMsg.setLabel("Nachricht senden");
    sendMsg.setName("sendMsg");
    sendMsg.setBackground(java.awt.Color.lightGray);
    sendMsg.setForeground(java.awt.Color.black);
    sendMsg.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        sendMsgActionPerformed(e);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 4;
    gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints3.insets = new java.awt.Insets(10, 10, 0, 0);

    chatpanel.add(sendMsg, gridBagConstraints3);
    channelChoice.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelChoice.setName("channelChoice");
    channelChoice.setBackground(java.awt.Color.white);
    channelChoice.setForeground(java.awt.Color.black);
    channelChoice.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        channelChoiceItemStateChanged(evt);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 1;
    gridBagConstraints3.gridy = 0;
    gridBagConstraints3.insets = new java.awt.Insets(0, 10, 10, 0);
    gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTHWEST;
    gridBagConstraints3.weightx = 1.0;

    chatpanel.add(channelChoice, gridBagConstraints3);
    channelChoiceLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelChoiceLabel.setName("channelChoiceLabel");
    channelChoiceLabel.setBackground(new java.awt.Color(204, 204, 204));
    channelChoiceLabel.setForeground(java.awt.Color.black);
    channelChoiceLabel.setText("Channel:");

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 0;
    gridBagConstraints3.gridy = 0;
    gridBagConstraints3.insets = new java.awt.Insets(0, 0, 10, 0);
    gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTHEAST;

    chatpanel.add(channelChoiceLabel, gridBagConstraints3);
    userList.setFont(new java.awt.Font("SansSerif", 0, 11));
    userList.setName("userList");
    userList.setBackground(java.awt.Color.white);
    userList.setForeground(java.awt.Color.black);
    userList.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(java.awt.event.ItemEvent e) {
        msg.requestFocus();
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 3;
    gridBagConstraints3.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints3.insets = new java.awt.Insets(0, 10, 0, 0);
    gridBagConstraints3.weighty = 1.0;

    chatpanel.add(userList, gridBagConstraints3);
    userListLabel.setFont(new java.awt.Font("SansSerif", 0, 11));
    userListLabel.setName("userListLabel");
    userListLabel.setBackground(new java.awt.Color(204, 204, 204));
    userListLabel.setForeground(java.awt.Color.black);
    userListLabel.setText("Empf\u00e4nger:");

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 2;
    gridBagConstraints3.insets = new java.awt.Insets(0, 10, 0, 0);

    chatpanel.add(userListLabel, gridBagConstraints3);
    logout.setFont(new java.awt.Font("SansSerif", 0, 11));
    logout.setLabel("Abmelden");
    logout.setName("logout");
    logout.setBackground(java.awt.Color.lightGray);
    logout.setForeground(java.awt.Color.black);
    logout.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        logoutActionPerformed(e);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 0;
    gridBagConstraints3.gridy = 5;
    gridBagConstraints3.insets = new java.awt.Insets(10, 0, 0, 0);
    gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;

    chatpanel.add(logout, gridBagConstraints3);
    channelMsgBuffer.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelMsgBuffer.setLabel("Protokoll");
    channelMsgBuffer.setName("channelMsgBuffer");
    channelMsgBuffer.setBackground(java.awt.Color.lightGray);
    channelMsgBuffer.setForeground(java.awt.Color.black);
    channelMsgBuffer.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        channelMsgBufferActionPerformed(e);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 5;
    gridBagConstraints3.insets = new java.awt.Insets(10, 0, 0, 0);
    gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;

    chatpanel.add(channelMsgBuffer, gridBagConstraints3);
    channelAdmin.setFont(new java.awt.Font("SansSerif", 0, 11));
    channelAdmin.setLabel("ChannelAdmin");
    channelAdmin.setName("channelAdmin");
    channelAdmin.setEnabled(false);
    channelAdmin.setBackground(java.awt.Color.lightGray);
    channelAdmin.setForeground(java.awt.Color.black);
    channelAdmin.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        channelAdminActionPerformed(e);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 0;
    gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints3.insets = new java.awt.Insets(0, 10, 5, 0);

    chatpanel.add(channelAdmin, gridBagConstraints3);
    userAdmin.setFont(new java.awt.Font("SansSerif", 0, 11));
    userAdmin.setLabel("UserAdmin");
    userAdmin.setName("userAdmin");
    userAdmin.setEnabled(false);
    userAdmin.setBackground(java.awt.Color.lightGray);
    userAdmin.setForeground(java.awt.Color.black);
    userAdmin.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent e) {
        userAdminActionPerformed(e);
      }
    });

    gridBagConstraints3 = new java.awt.GridBagConstraints();
    gridBagConstraints3.gridx = 2;
    gridBagConstraints3.gridy = 1;
    gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints3.insets = new java.awt.Insets(0, 10, 10, 0);

    chatpanel.add(userAdmin, gridBagConstraints3);
    mainpanel.add(chatpanel, "card2");

    gridBagConstraints1 = new java.awt.GridBagConstraints();

    add(mainpanel, gridBagConstraints1);
  }

  private void logoutActionPerformed(java.awt.event.ActionEvent evt) {
    this.logout();
  }

  /** Meldet den Benutzer ab. */
  private void logout() {
    this.adminClient.logout();
    this.clearGui();
  }

  /** Setzt das GUI zurück. */
  public void clearGui() {

    cardFlipped = false;

    cardLayout.first(mainpanel);
    chatText.setText("");
    msg.setText("");
    this.channelAdminGUI.setVisible(false);
    this.userAdminGUI.setVisible(false);
    this.logText.setVisible(false);
    this.adminClient.clearChannelMsgBuffer();
  }

  private void channelMsgBufferActionPerformed(java.awt.event
          .ActionEvent evt) {

    logText.logTextArea.setText("");

    Enumeration enum = this.adminClient.getChannelMsgBuffer().elements();

    while (enum.hasMoreElements()) {
      logText.logTextArea.append((String) enum.nextElement() + "\n");
    }

    logText.show();
  }

  private void userAdminActionPerformed(java.awt.event.ActionEvent evt) {

    if (userAdmin.isEnabled()) {
      this.userAdminGUI.show();
      this.adminClient.getUserList();
      this.adminClient.getChannelList();
    }
  }

  private void channelAdminActionPerformed(java.awt.event.ActionEvent evt) {

    if (channelAdmin.isEnabled()) {
      this.channelAdminGUI.show();
      this.adminClient.getChannelList();
      this.adminClient.getUserList();
    }
  }

  private void channelChoiceItemStateChanged(java.awt.event.ItemEvent evt) {
    this.chatText.setText("");
    this.adminClient.joinChannel(this.channelChoice.getSelectedItem());
  }

  private void sendMsgActionPerformed(java.awt.event.ActionEvent evt) {
    this.sendMsg();
  }

  /**
   * Sendet eine Nachricht.
   */
  private void sendMsg() {
    if (this.userList.getSelectedItem().compareTo("Alle") == 0) {
      this.adminClient.sendMsgToChannel(this.msg.getText());
    } else {
      this.adminClient.sendMsgToUser(this.userList.getSelectedItem(),
                                     this.msg.getText());
      this.chatText.append("Flüstern an " + this.userList.getSelectedItem()
                           + ": " + this.msg.getText() + "\n");
      this.userList.select(0);
    }

    this.msg.setText("");
    this.msg.requestFocus();
  }

  /**
   * Meldet einen User an.
   */
  private void login() {

    this.adminClient.startClient();

    if (isGuest.getState()) {
      this.adminClient.loginAsGuest(this.loginName.getText());
    } else {
      this.adminClient.login(this.loginName.getText(),
                             this.password.getText());
    }
  }

  private void loginActionPerformed(java.awt.event.ActionEvent evt) {
    this.login();
  }

  private void isGuestItemStateChanged(java.awt.event.ItemEvent evt) {

    if (isGuest.getState()) {
      this.password.setEnabled(false);
    } else {
      this.password.setEnabled(true);
    }
  }
}
