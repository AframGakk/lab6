import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.DataSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Main {

	private static final String FIELDSEPERATOR = " ";

	/**
	 * setup the specification for the data set
	 */
	public static DataSetSpecification getSpecification() {
		// the dataset contains 6 attributes (a1, a2, ..., a6) with varying numbers of classes
		DataSetSpecification dss = new DataSetSpecification();
		dss.defineStringAttribute("class", new String[] { "0", "1" });
		dss.defineStringAttribute("a1", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a2", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a3", new String[] { "1", "2" });
		dss.defineStringAttribute("a4", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a5", new String[] { "1", "2", "3", "4" });
		dss.defineStringAttribute("a6", new String[] { "1", "2" });
		dss.setTarget("class");
		return dss;
	}

	/**
	 * load at most maxNbLines lines from a file named filename + ".csv" and return a data set made from those lines
	 */
	public static DataSet loadDataSet(String filename, DataSetSpecification spec, int maxNbLines) throws Exception {
		DataSet ds = new DataSet(spec);
		// open file
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename + ".csv")));
		String line;
		// read file line by line and parse each line into an example that is added to the data set
		while (maxNbLines>0 && (line = reader.readLine()) != null) {
			ds.add(DataSetFactory.exampleFromString(line, spec, FIELDSEPERATOR));
			maxNbLines--;
		}
		reader.close();
		return ds;
	}

	/**
	 * like loadDataSet, but loads all lines in a file
	 */
	public static DataSet loadCompleteDataSet(String filename, DataSetSpecification spec) throws Exception {
		return loadDataSet(filename, spec, Integer.MAX_VALUE);
	}

	/**
	 * main procedure
	 */
	public static void main(String[] args) throws Exception {
		testIncreasing();
	}

	private static void testIncreasing() throws Exception {
		// setup the DataSetSpecification
		DataSetSpecification dss = getSpecification();

		/* load training and test data sets
		   There are three data sets describing different concepts:
		     monks-1 (124 training examples), monks-2 (169 training examples), monks-3 (122 training examples).
		   Look at the end of monks.names, to see which concepts the data sets describe.
		*/
		String dataSetName = "monks-1"; // other sets are "monks-2" and "monks-3"
		DataSet trainingData = loadCompleteDataSet(dataSetName + ".train", dss);
		DataSet testData = loadCompleteDataSet(dataSetName + ".test", dss);


		int totalSize = testData.size();

		for (int i = 1; i < totalSize; i++) {
			trainingData = loadDataSet(dataSetName + ".train", dss, i);

			DecisionTreeLearner dtl = new DecisionTreeLearner();

			// learn the decision tree using the training data
			System.out.println("Learning decision tree for " + dataSetName + " from " + trainingData.size() + " examples ...");
			dtl.train(trainingData);

			// test the decision tree using the test data
			int[] testResult = dtl.test(testData);
			int nbCorrect = testResult[0];
			int nbIncorrect = testResult[1];
			System.out.println("test result: " + nbCorrect + " of " + (nbCorrect + nbIncorrect) + " examples classified correctly (" + (nbCorrect * 100.0 / (nbCorrect + nbIncorrect)) + "%)");

		}

	}

	private static void testFull() throws Exception {
		// setup the DataSetSpecification
		DataSetSpecification dss = getSpecification();

		/* load training and test data sets
		   There are three data sets describing different concepts:
		     monks-1 (124 training examples), monks-2 (169 training examples), monks-3 (122 training examples).
		   Look at the end of monks.names, to see which concepts the data sets describe.
		*/
		String dataSetName = "monks-1"; // other sets are "monks-2" and "monks-3"
		DataSet testData = loadCompleteDataSet(dataSetName + ".test", dss);
		DataSet trainingData = loadCompleteDataSet(dataSetName + ".train", dss);
		// Hint: use loadDataSet(...) instead of loadCompleteDataSet(...) to load only some of the examples from a file

		// initialize the learner
		DecisionTreeLearner dtl = new DecisionTreeLearner();

		// learn the decision tree using the training data
		System.out.println("Learning decision tree for " + dataSetName + " from " + trainingData.size() + " examples ...");
		dtl.train(trainingData);

		// test the decision tree using the test data
		int[] testResult = dtl.test(testData);
		int nbCorrect = testResult[0];
		int nbIncorrect = testResult[1];
		System.out.println("test result: " + nbCorrect + " of " + (nbCorrect + nbIncorrect) + " examples classified correctly (" + (nbCorrect * 100.0 / (nbCorrect + nbIncorrect)) + "%)");

		// print out the learned tree
		System.out.println("the learned tree:");
		System.out.println(dtl.getDecisionTree());
	}
}
