package coursework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import model.Fitness;
import model.Individual;
import model.LunarParameters.DataSet;
import model.NeuralNetwork;

/**
 * Implements a basic Evolutionary Algorithm to train a Neural Network
 * 
 * You Can Use This Class to implement your EA or implement your own class that extends {@link NeuralNetwork} 
 * 
 */
public class OwnEvolutionaryAlgorithm extends NeuralNetwork {
	
	ArrayList<Individual> newPopulation = new ArrayList<Individual>();
	/**
	 * The Main Evolutionary Loop
	 */
	@Override
	public void run() {		
		//Initialise a population of Individuals with random weights
		population = initialise();

		//Record a copy of the best Individual in the population
		best = getBest();
		System.out.println("Best From Initialisation " + best);

		/**
		 * main EA processing loop
		 */		
		
		while (evaluations < Parameters.maxEvaluations) {

			/**
			 * this is a skeleton EA - you need to add the methods.
			 * You can also change the EA if you want 
			 * You must set the best Individual at the end of a run
			 * 
			 */

			// Select 2 Individuals from the current population. Currently returns random Individual
			//Individual parent1 = select(); 
			//Individual parent2 = select();
			
			

			// Generate a child by crossover. Not Implemented		
			newPopulation.addAll(getGoodParents());
			
			ArrayList<Individual> children = reproduce();			
			
			//mutate the offspring
			mutate(children);
			
			// Evaluate the children
			evaluateIndividuals(children);	
			
			

			// Replace children in population
			//replace(children);
			
			newPopulation.addAll(children);

			// check to see if the best has improved
			best = getBest();
			
			// Implemented in NN class. 
			outputStats();
			
			//Increment number of completed generations			
		}

		//save the trained network to disk
		saveNeuralNetwork();
	}

	
	
	
	

	/**
	 * Sets the fitness of the individuals passed as parameters (whole population)
	 * 
	 */
	private void evaluateIndividuals(ArrayList<Individual> individuals) {
		for (Individual individual : individuals) {
			individual.fitness = Fitness.evaluate(individual, this);
		}
	}


	/**
	 * Returns a copy of the best individual in the population
	 * 
	 */
	private Individual getBest() {
		best = null;;
		for (Individual individual : population) {
			if (best == null) {
				best = individual.copy();
			} else if (individual.fitness < best.fitness) {
				best = individual.copy();
			}
		}
		return best;
	}

	/**
	 * Generates a randomly initialised population
	 * 
	 */
	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for (int i = 0; i < Parameters.popSize; ++i) {
			//chromosome weights are initialised randomly in the constructor
			Individual individual = new Individual();
			population.add(individual);
		}
		evaluateIndividuals(population);
		return population;
	}

	/**
	 * Selection --
	 * 
	 * NEEDS REPLACED with proper selection this just returns a copy of a random
	 * member of the population
	 */
	//@SuppressWarnings("null")
	private Individual select() {	
		
		//return rouletteSelect();
		//return tournamentSelect();
		return null;
	}
	
	


	private void setNewPop(ArrayList<Individual> test) {
		population = test;
	}
	

private Individual tournamentSelect(ArrayList<Individual> newPop) {
	
	Individual parent = null;
	int tnSize = newPop.size()/3;
	//int tnSize = population.size()/10;
	Individual[] potParent = new Individual[tnSize];
	
	
	Random r = new Random();
	
	
	for (int i=0;i<tnSize;i++) {
		potParent[i] = population.get(r.nextInt(population.size()) );
	}
	
	double bestFitness = 1 - potParent[0].fitness;
	parent = potParent[0];
	
	for (int i = 1; i<tnSize;i++) {
		if (1 - potParent[i].fitness> bestFitness) {
			bestFitness = potParent[i].fitness;
			parent = potParent[i];
		}
	}
	
	
	return parent;
	

}


private ArrayList<Individual> myUniformCrossover(ArrayList<Individual> parents){	//Its replaces the part before or after the weights of the hidden nodes
	
	ArrayList<Individual> children = new ArrayList<>();
	Individual temp = new Individual();
	
	Random r = new Random();
	
	int childNum = 2;
	int potChild = 10;
	
	for(int k = 0;k<parents.size();k=k+2) {
		
		ArrayList<Individual> initChildren = new ArrayList<>();
		
		for (int i =0;i<potChild;i++) {
			
			for (int j = 0; j<parents.get(0).chromosome.length;j++) {		//Chromosome length is the same for every chromosome
				
				if (r.nextDouble()>0.5D) {
					temp.chromosome[j]=parents.get(k).chromosome[j];
				}
				else {
					temp.chromosome[j]=parents.get(k+1).chromosome[j];
				}
				
			}
			
			
			initChildren.add(temp);
		}
		
		Collections.sort(initChildren);
		
		for (int i=0;i<childNum;i++) {
			children.add(initChildren.get(i));
		}
		
		
	}
	

	
	return children;
}



private ArrayList<Individual> getGoodParents(){
	
	
	
	
	ArrayList<Individual> tempPop = population;
	ArrayList<Individual> newPop = new ArrayList<Individual>();
	Individual temp = new Individual();
	//int originalPopSize = population.size();
	
	
	Collections.sort(tempPop);
	
	for (int i=0;i<population.size()/2;i++) {
		temp = tempPop.get(i);
		newPop.add(temp);
	}
	
	
	//System.out.println(newPop.size());
	return newPop;
	
}


	/**
	 * Crossover / Reproduction
	 * 
	 * NEEDS REPLACED with proper method this code just returns exact copies of the
	 * parents. 
	 */
	private ArrayList<Individual> reproduce() {
		//return ArithmeticRecombination(parent1, parent2);
		//return uniformCrossover(parent1, parent2);
		return myUniformCrossover(newPopulation);
	} 
	
	/**
	 * Mutation
	 * 
	 * 
	 */
	private void mutate(ArrayList<Individual> individuals) {		
		for(Individual individual : individuals) {
			for (int i = 0; i < individual.chromosome.length; i++) {
				if (Parameters.random.nextDouble() < Parameters.mutateRate) {
					if (Parameters.random.nextBoolean()) {
						individual.chromosome[i] += (Parameters.mutateChange);
					} else {
						individual.chromosome[i] -= (Parameters.mutateChange);
					}
				}
			}
		}
		
	}

	/**
	 * 
	 * Replaces the worst member of the population 
	 * (regardless of fitness)
	 * 
	 */
	private void replace(ArrayList<Individual> individuals) {
		//ArrayList<Individual> newPop = getGoodParents();
		
		for(Individual individual : individuals) {
			int idx = getWorstIndex();		
			population.set(idx, individual);
		}		
		
		//int i = 0;
	}

	
	

	/**
	 * Returns the index of the worst member of the population
	 * @return
	 */
	private int getWorstIndex() {
		Individual worst = null;
		int idx = -1;
		for (int i = 0; i < population.size(); i++) {
			Individual individual = population.get(i);
			if (worst == null) {
				worst = individual;
				idx = i;
			} else if (individual.fitness > worst.fitness) {
				worst = individual;
				idx = i; 
			}
		}
		return idx;
	}	

	@Override
	public double activationFunction(double x) {
		if (x < -20.0) {
			return -1.0;
		} else if (x > 20.0) {
			return 1.0;
		}
		return Math.tanh(x);
	}
}
