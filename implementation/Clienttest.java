import Client.*;


class Clienttest {

  public static void main(String[] args) {

    AdminClient client = new AdminClient();

    client.startClient();
    client.login("mauper", "passwd");
    client.addUser("mauper2", "passwd", false, null);
  }
}
