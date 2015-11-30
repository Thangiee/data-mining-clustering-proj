Thang Le
1000787155

Language used: Scala

REQUIRE java-6 or higher


Option 1 ----- run the included pre-compiled jar

Option 2 ----- compiling and executing it

1. Download and install Scala 2.11.7 binaries: http://www.scala-lang.org/index.html

2. Download and install SBT (Simple Build tool): http://www.scala-sbt.org/download.html

3. Open a terminal/command line and navigate to the root of the project folder.
   You know you're in the correct directory if you also see this README file.

4. and run "sbt assembly" (it many take a while on the first run).

5. After finishing, it should create a new project2.jar in the same directory.

------- How to run/use the program -------

 java -jar project2.jar <data_set_file.csv> svd <dimen>
 example: java -jar project2.jar iris.csv svd 2

 java -jar project2.jar <data_set_file.csv> plot <label_file.csv>
 example: java -jar project2.jar iris-svd.csv plot irisLabel.csv

 java -jar project2.jar <data_set_file.csv> kmeans <k>
 example: java -jar project2.jar iris-svd.csv kmeans 3

 java -jar project2.jar <data_set_file.csv> dbscan <label_file.csv> <eps> <min_pts>
 example: java -jar project2.jar iris-svd.csv dbscan irisLabel.csv .4835 15

 The arguments provide to the program the following information:
   *  The first argument is the name of the data set file, it should
      be a csv file and it should not contains label information.
   *  The second argument can have four possible values: SVD, plot, kmeans, or dbscan.

   *  <dimen> argument is an integer that specify the target dimensions to reduce to.
   *  <label_file.csv> argument is the name of the file with labels information.
   *  <k> argument is the number of clusters for KMeans algorithm.
   *  <eps> argument is the distance threshold.
   *  <min_pts> is the minimum number of points within eps.