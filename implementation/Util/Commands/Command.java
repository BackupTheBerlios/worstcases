package Util.Commands;

import java.io.Serializable;

/** Interface für alle Befehle, die beispielsweise  zwischen Client und ClientServant ausgetauscht werden. */
public interface Command extends Serializable {
    /**
     * Diese Methode wird ausgeführt, sowie ein Befehl (Command) bei seinem Empfänger ankommt. Ein Befehl enthält die zu seiner
     * Ausführung notwendigen Informationen.
     * @param target das Objekt, bei dem der Befehl ausgeführt wird.
     * @exception CommandException falls der Befehl beim Empfänger nicht ausgeführt werden kann.
     */
    public void execute(Object target) /* throws CommandException*/;
    // FIXME: Ausnahme-Behandlung muss noch implementiert werden.
}
