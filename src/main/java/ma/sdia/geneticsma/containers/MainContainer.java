package ma.sdia.geneticsma.containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {
    public static void main(String[] args) throws ControllerException {
        Runtime runtime = Runtime.instance();//Define the runtime of the plateforme
        ProfileImpl profile = new ProfileImpl(true); //the profile of the plateforme
        profile.setParameter(ProfileImpl.GUI,"true");

        AgentContainer mainContainer = runtime.createMainContainer(profile);//defining the main container of the plateforme
        mainContainer.start();

    }
}
