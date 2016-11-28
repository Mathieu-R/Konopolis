package src.konopolis.test;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.vue.KonopolisViewConsole;
import src.konopolis.vue.KonopolisViewGui;

public class KonopolisTest {

    public static void main(String[] args) {
        //Création du modèle
        KonopolisModel konMod = new KonopolisModel();
        //Création des contrôleurs : Un pour chaque vue
        //Chaque contrôleur doit avoir une référence vers le modèle pour pouvoir le commander
        KonopolisController konContConsole = new KonopolisController(konMod);
        KonopolisController konContGui = new KonopolisController(konMod);
        //Création des vues.
        //Chaque vue doit connaître son contrôleur et avoir une référence vers le modèle pour pouvoir l'observer
        KonopolisViewConsole console = new KonopolisViewConsole(konMod, konContConsole);
        KonopolisViewGui gui = new KonopolisViewGui(konMod, konContGui);
        //On donne la référence à la vue pour chaque contrôleur
        konContConsole.addView(console);
        konContGui.addView(gui);
    }

}
