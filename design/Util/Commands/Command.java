package Util.Commands;

import java.io.Serializable;

/** Interface f�r alle Befehle, die beispielsweise  zwischen Client und ClientServant ausgetauscht werden. */
public interface Command extends Serializable {
    /**
     * Diese Methode wird ausgef�hrt, sowie ein Befehl (Command) bei seinem Empf�nger ankommt. Ein Befehl enth�lt die zu seiner
     * Ausf�hrung notwendigen Informationen.
     * @param target das Objekt, bei dem der Befehl ausgef�hrt wird.
     * @exception CommandException falls der Befehl beim Empf�nger nicht ausgef�hrt werden kann.
     */
    public void execute(Object target) /* throws CommandException*/;
    // FIXME: Ausnahme-Behandlung muss noch implementiert werden.
}
