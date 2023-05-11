package ma.sdia.geneticsma.containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import ma.sdia.geneticsma.agents.version_1.IndividualAgent;

import java.util.ArrayList;
import java.util.List;

public class Container_v1 {
    public static List<IndividualAgent> listAgents = new ArrayList<>();
    public static void main(String[] args) throws ControllerException {

        Runtime runtime = Runtime.instance();//Define the runtime of the plateforme
        ProfileImpl profile= new ProfileImpl(false); //the profile of the plateforme
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer subContainer = runtime.createAgentContainer(profile);
        subContainer.start();
        System.out.println(">> The IndivudalContainer is ON...");
        //create Individuals
        for(int i =0;i<20;i++) {
            String name = "individual_"+i;
            AgentController agent = subContainer.createNewAgent(name, "ma.sdia.geneticsma.agents.version_1.IndividualAgent", new Object[]{});

            agent.start();
        }
        //Create Population Agent
        AgentController agent = subContainer.createNewAgent("populationAgent2", "ma.sdia.geneticsma.agents.version_1.PopulationAgent", new Object[]{});
        agent.start();


    }

    public static void appendIndividualAgent(IndividualAgent agent){
        listAgents.add(agent);
        System.out.println("* [APPEND-AGENT] >> New individual "+agent.getLocalName()+" has been inserted to the list ");
    }


}
