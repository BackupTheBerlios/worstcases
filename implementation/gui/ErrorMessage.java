/*
 * ErrorMessage.java
 *
 * Created on June 13, 2001, 8:42 PM
 */

package gui;

/**
 * Der Frame Errormessage dient zur Ausgabe von Fehlermeldungen.
 */
public class ErrorMessage extends java.awt.Frame {

    /** Konstruktor, initalisiert die grafischen Komponenten. */
    public ErrorMessage() {
        initComponents ();
        pack ();
    }

    /**
     * Diese Methode wird vom Konstruktor aufgerufen, um die grafische
     * Oberfl‰che des Frames zu initialisieren.
     */
    private void initComponents() {
        okButton = new java.awt.Button();
        messageArea = new java.awt.TextArea();
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        setResizable(true);
        setFont(new java.awt.Font ("SansSerif", 0, 12));
        setTitle(gui.ChatGui.PRODUCT_NAME + " - Fehler aufgetreten");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        }
        );

        okButton.setFont(new java.awt.Font ("SansSerif", 0, 11));
        okButton.setLabel("Weiter");
        okButton.setName("okButton");
        okButton.setBackground(new java.awt.Color (204, 204, 204));
        okButton.setForeground(java.awt.Color.black);
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okButtonMouseClicked(evt);
            }
        }
        );

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(0, 15, 15, 15);
        add(okButton, gridBagConstraints1);


        messageArea.setBackground(new java.awt.Color (196, 196, 196));
        messageArea.setName("messageArea");
        messageArea.setEditable(false);
        messageArea.setFont(new java.awt.Font ("SansSerif", 0, 11));
        messageArea.setColumns(40);
        messageArea.setForeground(new java.awt.Color (0, 0, 0));
        messageArea.setRows(4);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(15, 15, 10, 15);
        add(messageArea, gridBagConstraints1);

    }

    /** Nach Klick auf den okButton wird der Error-Message-Frame geschlossen */
    private void okButtonMouseClicked(java.awt.event.MouseEvent evt) {
        this.setVisible(false);
    }

    /** Schlieﬂen des Applikationsframes */
    private void exitForm(java.awt.event.WindowEvent evt) {
        this.setVisible(false);
    }

    // Variables declaration - do not modify
    private java.awt.Button okButton;
    java.awt.TextArea messageArea;
    // End of variables declaration

}
