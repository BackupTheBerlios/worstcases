/* Generated by Together */

package Util.Commands;

import Client.Client;

public class NewUserInChannelCommand implements Command {
    private String name;

    public NewUserInChannelCommand(String paramName) {
        this.name = paramName;
    }

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).newUserInChannel(name);
        } // XXX: else Exception ausl�sen?
    }
}