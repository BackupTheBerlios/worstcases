/** Interface für alle Klassen, die einen Downlink benutzen wollen. */

package Util;

import Util.Commands.Command;

public interface DownlinkOwner {
    /** Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht erhält. */
    void processMsg(Command msg);
    void stopOwner();
}
