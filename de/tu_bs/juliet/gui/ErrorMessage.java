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

/**
 * Der Frame Errormessage dient zur Ausgabe von Fehlermeldungen.
 */
public class ErrorMessage extends java.awt.Frame {
  /** OK-Button zum Schließen des Fensters. */
  private java.awt.Button okButton;
  /** Zum Anzeigen der Fehlermeldung. */
  private java.awt.TextArea messageArea;

  /** Konstruktor, initalisiert die grafischen Komponenten. */
  public ErrorMessage() {
    initComponents();
    pack();
  }

    /**
     * Konstrukor.
     * @param msg die anzuzeigende Fehlermeldung
     */
    public ErrorMessage(String msg) {
	this();
	setMessage(msg);
    }

    /**
     * Setzt die auszugebende Fehlermeldung.
     */
    public void setMessage(String msg) {
	messageArea.setText(msg);
    }

  /**
   * Diese Methode wird vom Konstruktor aufgerufen, um die grafische
   * Oberfläche des Frames zu initialisieren.
   */
  private void initComponents() {

    okButton = new java.awt.Button();
    messageArea = new java.awt.TextArea();

    setLayout(new java.awt.GridBagLayout());
    setBackground(new java.awt.Color(204, 204, 204));

    java.awt.GridBagConstraints gridBagConstraints1;

    //setResizable(false);
    setFont(new java.awt.Font("SansSerif", 0, 12));
    setTitle(ChatGui.PRODUCT_NAME + " - Fehler aufgetreten");
    addWindowListener(new java.awt.event.WindowAdapter() {

      public void windowClosing(java.awt.event.WindowEvent evt) {
        exitForm(evt);
      }
    });
    okButton.setFont(new java.awt.Font("SansSerif", 0, 11));
    okButton.setLabel("OK");
    okButton.setName("okButton");
    okButton.setBackground(new java.awt.Color(204, 204, 204));
    okButton.setForeground(java.awt.Color.black);
    okButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeWindow();
      }
    });

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 1;
    gridBagConstraints1.insets = new java.awt.Insets(0, 15, 15, 15);

    add(okButton, gridBagConstraints1);
    messageArea.setBackground(new java.awt.Color(196, 196, 196));
    messageArea.setName("messageArea");
    messageArea.setEditable(false);
    messageArea.setFont(new java.awt.Font("SansSerif", 0, 11));
    messageArea.setColumns(40);
    messageArea.setForeground(new java.awt.Color(0, 0, 0));
    messageArea.setRows(4);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.insets = new java.awt.Insets(15, 15, 10, 15);

    add(messageArea, gridBagConstraints1);
    setSize(300, 180);
  }

  /** Nach Klick auf den okButton wird der Error-Message-Frame geschlossen */
  private void closeWindow() {
    this.setVisible(false);
  }

  /** Schließen des Applikationsframes */
  private void exitForm(java.awt.event.WindowEvent evt) {
    this.setVisible(false);
  }
}
