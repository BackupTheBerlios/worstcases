/* Generated by Together */

package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;

/**
 * Wird von einem AdminClient erzeugt,
 * legt einen neuen User an
 * ruft addUser beim AdminClientServant auf.
 */

public class AddUserCommand implements Command {

    /**Setzt die Attribute.*/
    public AddUserCommand(String paramName, String paramPassword,boolean paramIsAdmin, Vector paramAllowedChannelNames) {
        this.name = paramName;
        this.password=paramPassword;
        this.isAdmin = paramIsAdmin;
        this.allowedChannelNames = paramAllowedChannelNames;
    }
    /**Name des Users.*/
    private String name;
    /**Passwort des Users.*/
    private String password;
    /**Admin-Status des Users.*/
    private boolean isAdmin = false;
    /**Liste der Namen der Channels, die der Benutzer betreten darf.*/
    private Vector allowedChannelNames;

    /**F�hrt adminClientServant.addUser() mit den Attributen des Commands aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).addUser(name,password, isAdmin, allowedChannelNames);
        }
    }
}
