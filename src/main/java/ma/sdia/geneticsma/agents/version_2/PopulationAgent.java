package ma.sdia.geneticsma.agents.version_2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ma.sdia.geneticsma.containers.Container_v2;
import ma.sdia.geneticsma.agents.version_2.IndividualAgent;

import java.util.*;

public class PopulationAgent extends Agent {
    public static final int SIZE_POP = 1;
    public static final int MAX_GENERATION = 40;
    public static final int OPTIMUM_FITNESS = 8;
    public static IndividualAgent optimumAgent;
    public IndividualAgent finestIndividual; //first rank
    public IndividualAgent secondIndividual; //sec rank
    public List<IndividualAgent> listIndividuals  = new ArrayList<>(SIZE_POP);

    @Override
    protected void setup() {
        int j = 0;
        do {
            //initialize the population with indivduals
            addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    listIndividuals = Container_v2.listAgents;
                    //initialize the IndividualAgent.PopulationAgent
                    IndividualAgent.populationAgent=(PopulationAgent) this.myAgent;

                }
            });

            //SELECT Operation
            int generation = j;

            addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    System.out.println("==============SELECT OPERATION ==============");
                    int i = 0;
                    //Sorting the list
                    sortPopulation();
                    for (IndividualAgent indiv : listIndividuals) {
                        indiv.calculateFitness();
                        System.out.println(i + " >> " + Arrays.toString(indiv.chromosome) + " | fitness : " + indiv.fitness);

                        i++;
                    }
                    System.out.println("#################### GENERATION [ "+ generation +"] ####################");

                }

            });

            //Stop condition
            /**addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    if(generation == MAX_GENERATION || optimumAgent != null) {
                        if (optimumAgent != null) {
                            System.out.println(">> Generation [" + (generation) + "] --> Best individual : " + Arrays.toString(optimumAgent.chromosome) + " | fitness : " + optimumAgent.fitness);
                        } else
                            System.out.println(" *** No optimal individual is founded ! Generation : " + generation);
                        System.exit(999);
                    }
                }
            });
            **/
            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    ACLMessage receivedMSG = receive();
                    if(receivedMSG!=null) {
                        optimumAgent = new IndividualAgent();
                        System.out.println(">> Generation [" + (generation) + "] --> Best individual : "+receivedMSG.getContent());
                        System.exit(999);
                    }
                    else{
                        block();
                    }
                    if (generation == MAX_GENERATION-1) {
                        System.out.println(" *** NO OPTIMUM INDIVIDUAL IS CAPTURED!");
                    }

                }

            });
            //CROSSOVER Operation
            addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    /** crossover : take the first two individuals with high fitness , then we point randomly at an index .
                     * for each individual , the left part of pointed index , will be exchange with the other individual's left part of pointed index.
                     * Ex : A = [1,1,2] | B[4,4,2] | index : 2 --> A = [4, 4,2] | B = [1 ,1,2]
                     */
                    int crossIndex = new Random().nextInt(IndividualAgent.CHROMOSOME_SIZE);
                    sortPopulation();
                    finestIndividual = listIndividuals.get(0); //agent with high fitness in first rank
                    secondIndividual = listIndividuals.get(1); //agent with high fitness in second rank
                    //Creation de 2 nouveaux individus et les initialiser successivement avec finestIndividual , secondIndividual
                    IndividualAgent firstIndiv = new IndividualAgent();
                    IndividualAgent secIndiv = new IndividualAgent();
                    for (int i = 0; i < IndividualAgent.CHROMOSOME_SIZE; i++) {
                        firstIndiv.chromosome[i] = finestIndividual.chromosome[i];
                        secIndiv.chromosome[i] = secondIndividual.chromosome[i];
                    }

                    //Permutation
                    for (int i = 1; i < crossIndex; i++) {
                        firstIndiv.chromosome[i] = secondIndividual.chromosome[i];
                        secIndiv.chromosome[i] = finestIndividual.chromosome[i];
                    }
                    //Calculate the new fitness
                    firstIndiv.calculateFitness();
                    secIndiv.calculateFitness();
                    //Set the last two individuals respectively with the new born indiv : firstIndiv and secIndiv
                    listIndividuals.set(listIndividuals.size() - 2, firstIndiv);
                    listIndividuals.set(listIndividuals.size() - 1, secIndiv);
                    System.out.println("==============CROSSOVER OPERATION ==============");
                    System.out.println(">> crossIndex: " + crossIndex + " | new Individuals : " + Arrays.toString(firstIndiv.chromosome) + " & " + Arrays.toString(secIndiv.chromosome));
                }
            });

            //MUTATION Operation
            addBehaviour(new OneShotBehaviour() {
                @Override
                public void action() {
                    System.out.println("==============MUTATION OPERATION ==============");
                    int index = new Random().nextInt(6);
                    for (int i = 1; i <= 2; i++) {
                        IndividualAgent individual = listIndividuals.get(listIndividuals.size() - i);
                        int[] chrom = individual.chromosome;
                        System.out.println(">> Before mutation at index " + index + ": DigitChromosome.Individual N°" + i + " --> " + Arrays.toString(listIndividuals.get(listIndividuals.size() - i).chromosome));

                        if (chrom[index] == 1)
                            chrom[index] = 0;
                        else
                            chrom[index] = 1;

                        System.out.println(">> After mutation --> " + Arrays.toString(listIndividuals.get(listIndividuals.size() - i).chromosome));
                        //Calculate the fitness
                        individual.calculateFitness();
                    }
                }
            });

            j++;

        }while (j<MAX_GENERATION);


    }

   private void sortPopulation() {
       Collections.sort(listIndividuals);
   }
}
