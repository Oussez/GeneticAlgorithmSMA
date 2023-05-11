package ma.sdia.geneticsma.agents.version_2;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import ma.sdia.geneticsma.agents.Individual;
import ma.sdia.geneticsma.containers.Container_v2;

import java.util.Arrays;
import java.util.Random;

import static java.lang.System.exit;

public class IndividualAgent extends Agent implements Individual {

    public int fitness;
    public static final int CHROMOSOME_SIZE=8;
    int chromosome [] = new int[CHROMOSOME_SIZE];
    static PopulationAgent populationAgent;

    protected void setup() {
        Container_v2.appendIndividualAgent(this);

        //Initialize the current individualAgent
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                for(int i=0;i<CHROMOSOME_SIZE;i++){
                    chromosome[i]= new Random().nextInt(2); //0 or 1
                }
                fitness= calculateFitness();
            }
        });


    }

    @Override
    public int compareTo(Object o) {
        IndividualAgent nextIndividual = (IndividualAgent) o;
        if(this.fitness < nextIndividual.fitness)
            return 1;
        else if(this.fitness > nextIndividual.fitness)
            return -1;
        return 0;
    }

    public int calculateFitness(){
        int fitness=0;
        for(int gene : chromosome){
            if(gene == 1)
                fitness++;
        }
        this.fitness = fitness;
        return fitness;
    }
    public void sendMessageToPopulationAgent(AID receiver, int generation){
        ACLMessage aclMessage = new ACLMessage();
        aclMessage.addReceiver(receiver);
        aclMessage.setContent(String.valueOf(generation));
        send(aclMessage);
    }

    public void iamOptimumAgent(){
        addBehaviour(new Behaviour() {
            boolean stop=false;
            @Override
            public void action() {
                if(fitness >= PopulationAgent.OPTIMUM_FITNESS){
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(populationAgent.getAID());
                    message.setContent(this.myAgent.getLocalName()+" | "+Arrays.toString(chromosome)+" | Fitness: "+fitness);
                    //message.setContent(">> I'm the OPTIMUM INDIVIDUAL +"+this.myAgent.getLocalName()+" | fitness: "+fitness+"| "+ Arrays.toString(chromosome));
                    send(message);
                    stop=true;

                }
            }

            @Override
            public boolean done() {
                return stop;
            }
        });
    }
}
