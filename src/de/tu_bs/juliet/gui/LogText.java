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

import de.tu_bs.juliet.util.debug.Debug;

/**
 * Dieses Frame dient der Anzeige des Gesprächsprotokolls.
 */

public class LogText extends java.awt.Frame {
    // Die GUI-Elemente.
    java.awt.Button okButton;
    java.awt.Button clearButton;
    java.awt.TextArea logTextArea;

    private int logTextRows = 20;
    private int logTextColumns = 70;

    /** Konstruktor. */
    public LogText() {
        initComponents();
        pack();
    }

    /**
     * Diese Methode wird vom Konstruktor aufgerufen, um das GUI zu
     * initialisieren.
     */
    private void initComponents() {

        logTextArea =
            new java.awt.TextArea( "", logTextRows, logTextColumns,
                                   java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY );
        okButton = new java.awt.Button();
        clearButton = new java.awt.Button();

        setLayout( new java.awt.GridBagLayout() );
        setTitle( ChatGui.PRODUCT_NAME + " - Protokoll" );
        setBackground( new java.awt.Color( 204, 204, 204 ) );
        setResizable( false );
        addWindowListener( new java.awt.event.WindowAdapter() {

                               public void windowClosing( java.awt.event.WindowEvent evt ) {
                                   exitForm( evt );
                               }
                           }

                         );

        java.awt.GridBagConstraints gridBagConstraints1;

        logTextArea.setBackground( java.awt.Color.white );
        logTextArea.setName( "logTextArea" );
        logTextArea.setEditable( false );
        logTextArea.setFont( new java.awt.Font( "SansSerif", 0, 11 ) );
        logTextArea.setForeground( java.awt.Color.black );

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 1;

        add( logTextArea, gridBagConstraints1 );
        okButton.setFont( new java.awt.Font( "SansSerif", 0, 11 ) );
        okButton.setLabel( "OK" );
        okButton.setName( "channelMsgBuffer" );
        okButton.setBackground( java.awt.Color.lightGray );
        okButton.setForeground( java.awt.Color.black );
        okButton.addActionListener( new java.awt.event.ActionListener() {

                                        public void actionPerformed( java.awt.event.ActionEvent e ) {
                                            okButtonActionPerformed( e );
                                        }
                                    }

                                  );

        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets( 10, 0, 0, 0 );
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;

        add( okButton, gridBagConstraints1 );
        clearButton.setFont( new java.awt.Font( "SansSerif", 0, 11 ) );
        clearButton.setLabel( "Löschen" );
        clearButton.setName( "channelMsgBuffer" );
        clearButton.setBackground( java.awt.Color.lightGray );
        clearButton.setForeground( java.awt.Color.black );
        clearButton.addActionListener( new java.awt.event.ActionListener() {

                                           public void actionPerformed( java.awt.event.ActionEvent e ) {
                                               clearButtonActionPerformed( e );
                                           }
                                       }

                                     );

        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets( 10, 0, 0, 0 );
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;

        add( clearButton, gridBagConstraints1 );
    }

    private void clearButtonActionPerformed( java.awt.event.ActionEvent e ) {
        this.logTextArea.setText( "" );
    }

    private void okButtonActionPerformed( java.awt.event.ActionEvent e ) {
        this.setVisible( false );
    }

    /** Schlie˜en des Applikationsframes */
    private void exitForm( java.awt.event.WindowEvent evt ) {
        this.setVisible( false );
    }
}
