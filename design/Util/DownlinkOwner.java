/** Interface f�r alle Klassen, die einen Downlink benutzen wollen. */

package Util;

import Util.Commands.Command;

public interface DownlinkOwner {
    /** Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht erh�lt. */
    public void processMsg(Command msg);

    public void downlinkError();

    public void setDownlink(Downlink paramDownlink);
}
