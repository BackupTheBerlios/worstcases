/*
 * chat.java
 *
 * Created on May 24, 2001, 5:55 PM
 */

package ClientGUI;

/**
 * GUI f�r den Client.
 *
 * FIXME: Admin-Button sollte nur sichtbar sein, wenn der Benutzer auch 
 * wirklich Admin ist.
 *
 * @author  Malte
 * @version 
 */
public class ChatGUI extends java.applet.Applet {

    /** Liefert Informationen �ber das Applet an einen Browser.*/
    public String getAppletInfo() {
      return "Virtuelle Konferenz von den WorstCases"; //XXX
    }
    
    /** Initializes the applet chat */
    public void init () {
        initComponents ();
        channelChoice.add("Foyer");
        channelChoice.add("Mensa");
        //channelChoice.show(); //XXX deprecated
        
        
        userList.add("Alle");
        userList.add("Malte");
        userList.add("Foo");
        
        for (int i=0; i<20; i++) {
            chatTextArea.append(". \n");
        }
        
        chatTextArea.setRows(30);

        this.validate();
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        chatPanel = new java.awt.Panel();
        chatTextArea = new java.awt.TextArea();
        inputTextField = new java.awt.TextField();
        userList = new java.awt.List();
        channelPanel = new java.awt.Panel();
        channelLabel = new java.awt.Label();
        channelChoice = new java.awt.Choice();
        exitButton = new java.awt.Button();
        sendButton = new java.awt.Button();
        label1 = new java.awt.Label();
        setLayout(new java.awt.CardLayout());
        
        chatPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        chatPanel.setFont(new java.awt.Font ("Dialog", 0, 11));
        chatPanel.setName("panel3");
        chatPanel.setBackground(new java.awt.Color (153, 153, 153));
        chatPanel.setForeground(java.awt.Color.black);
        
        chatTextArea.setBackground(new java.awt.Color (223, 223, 223));
          chatTextArea.setName("chatTextArea");
          chatTextArea.setEditable(false);
          chatTextArea.setFont(new java.awt.Font ("Dialog", 0, 11));
          chatTextArea.setForeground(java.awt.Color.black);
          chatTextArea.setText("Willkommen zur Virtuellen Konferenz des Instituts f\u00fcr Computergrafik");
          chatTextArea.setRows(20);
          gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 0;
          gridBagConstraints1.gridy = 1;
          gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
          gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
          chatPanel.add(chatTextArea, gridBagConstraints1);
          
          
        inputTextField.setBackground(java.awt.Color.white);
          inputTextField.setName("inputTextField");
          inputTextField.setFont(new java.awt.Font ("Dialog", 0, 11));
          inputTextField.setForeground(java.awt.Color.black);
          inputTextField.setText("Hier Text eingeben");
          inputTextField.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  textField1ActionPerformed(evt);
              }
          }
          );
          gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 0;
          gridBagConstraints1.gridy = 2;
          gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
          gridBagConstraints1.insets = new java.awt.Insets(10, 0, 0, 0);
          chatPanel.add(inputTextField, gridBagConstraints1);
          
          
        userList.setFont(new java.awt.Font ("Dialog", 0, 11));
          userList.setName("userList");
          userList.setBackground(java.awt.Color.white);
          userList.setForeground(java.awt.Color.black);
          userList.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  list1ActionPerformed(evt);
              }
          }
          );
          gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 1;
          gridBagConstraints1.gridy = 1;
          gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
          gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 0);
          chatPanel.add(userList, gridBagConstraints1);
          
          
        channelPanel.setLayout(new java.awt.FlowLayout(0, 5, 5));
          channelPanel.setFont(new java.awt.Font ("Dialog", 0, 11));
          channelPanel.setName("channelPanel");
          channelPanel.setBackground(new java.awt.Color (153, 153, 153));
          channelPanel.setForeground(java.awt.Color.black);
          
          channelLabel.setFont(new java.awt.Font ("Dialog", 0, 11));
            channelLabel.setBackground(new java.awt.Color (153, 153, 153));
            channelLabel.setForeground(java.awt.Color.black);
            channelLabel.setText("Channel");
            channelPanel.add(channelLabel);
            
            
          channelChoice.setFont(new java.awt.Font ("Dialog", 0, 11));
            channelChoice.setName("channelChoice");
            channelChoice.setBackground(new java.awt.Color (153, 153, 153));
            channelChoice.setForeground(java.awt.Color.black);
            channelPanel.add(channelChoice);
            
            
          exitButton.setFont(new java.awt.Font ("Dialog", 0, 11));
            exitButton.setLabel("Abmelden");
            exitButton.setName("exitButton");
            exitButton.setBackground(new java.awt.Color (153, 153, 153));
            exitButton.setForeground(java.awt.Color.black);
            channelPanel.add(exitButton);
            
            gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 0;
          gridBagConstraints1.gridy = 0;
          gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
          chatPanel.add(channelPanel, gridBagConstraints1);
          
          
        sendButton.setFont(new java.awt.Font ("Dialog", 0, 11));
          sendButton.setLabel("Senden");
          sendButton.setName("sendbutton");
          sendButton.setBackground(new java.awt.Color (153, 153, 153));
          sendButton.setForeground(java.awt.Color.black);
          sendButton.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  button4ActionPerformed(evt);
              }
          }
          );
          gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 1;
          gridBagConstraints1.gridy = 2;
          gridBagConstraints1.insets = new java.awt.Insets(10, 10, 0, 0);
          gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
          chatPanel.add(sendButton, gridBagConstraints1);
          
          
        label1.setFont(new java.awt.Font ("Dialog", 0, 11));
          label1.setName("label2");
          label1.setBackground(new java.awt.Color (153, 153, 153));
          label1.setForeground(java.awt.Color.black);
          label1.setText("Empf\u00e4nger");
          gridBagConstraints1 = new java.awt.GridBagConstraints();
          gridBagConstraints1.gridx = 1;
          gridBagConstraints1.gridy = 0;
          chatPanel.add(label1, gridBagConstraints1);
          
          
        add(chatPanel, "card1");
        
    }//GEN-END:initComponents

  private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
// Add your handling code here:
  }//GEN-LAST:event_button4ActionPerformed

  private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
// Add your handling code here:
  }//GEN-LAST:event_button2ActionPerformed

  private void list1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list1ActionPerformed
// Add your handling code here:
  }//GEN-LAST:event_list1ActionPerformed

  private void textField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textField1ActionPerformed
// Add your handling code here:
  }//GEN-LAST:event_textField1ActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private java.awt.Panel chatPanel;
  private java.awt.TextArea chatTextArea;
  private java.awt.TextField inputTextField;
  private java.awt.List userList;
  private java.awt.Panel channelPanel;
  private java.awt.Label channelLabel;
  private java.awt.Choice channelChoice;
  private java.awt.Button exitButton;
  private java.awt.Button sendButton;
  private java.awt.Label label1;
  // End of variables declaration//GEN-END:variables

}