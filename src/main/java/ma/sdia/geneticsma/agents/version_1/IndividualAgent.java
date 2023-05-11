package ma.sdia.geneticsma.agents.version_1;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import ma.sdia.geneticsma.agents.Individual;
import ma.sdia.geneticsma.containers.Container_v1;

import java.util.Random;

public class IndividualAgent extends Agent implements Individual {

    public int fitness;
    public static final int CHROMOSOME_SIZE=8;
    int chromosome [] = new int[CHROMOSOME_SIZE];
    static PopulationAgent populationAgent;

    protected void setup() {
        Container_v1.appendIndividualAgent(this);

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
    public static void hello(AID aid){
        System.out.println("-----------------------real: "+aid.getLocalName()+" |fake "+aid.getLocalName());

    }
}
