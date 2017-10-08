package movielenstopmovies;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author Shanmugasudan Veerabahu
 * 
 */
public class MovieLensTopMovies extends Configured implements Tool {

    
 private static final String OUTPUT_PATH = "intermediate_output_17";
 
  public static class AggregateMapper1
       extends Mapper<Object, Text, IntWritable, FloatWritable>{

    private  static IntWritable movieId;
    private static FloatWritable movieRating;

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
        String readRatingsLine= value.toString();                       // parse each line in csv file as a string
        String[] ratingValues = readRatingsLine.split("::");          // split the string into comma seperated values and 
                                                                    //save as a string array
        movieId= new IntWritable(Integer.parseInt(ratingValues[1]));
        Float temp_movieRating=Float.parseFloat(ratingValues[2]);
        movieRating=  new FloatWritable(temp_movieRating);   
        context.write(movieId,movieRating); 
      } 
    }
  

  public static class AggregateReducer1
       extends Reducer<IntWritable, FloatWritable,IntWritable, FloatWritable> {
      private FloatWritable result = new FloatWritable();
    
    public void reduce(IntWritable key, Iterable<FloatWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      float movieRatingAverage;
      int   countofmovieRatings= 0;
      float sumOfMovieRatings=0;
      for (FloatWritable val : values) {
        sumOfMovieRatings += val.get();
        countofmovieRatings+=1;
      }
      
      movieRatingAverage=sumOfMovieRatings/countofmovieRatings;
      result.set(movieRatingAverage);
      context.write(key, result);
    }
  }
  
   public static class SortMapper2
       extends Mapper<Object, Text, FloatWritable,IntWritable>{

  //     private static IntWritable movieId_Input;
  //     private static FloatWritable averageRating_Input;
       private static IntWritable movieId_SortedOutput;
       private static FloatWritable averageRating_SortedOutput;
       
       
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
        String readRatingsLine= value.toString();  
        // parse each line in csv file as a string
        String[] ratingValues = readRatingsLine.split("\t"); 
       averageRating_SortedOutput=new FloatWritable(Float.parseFloat(ratingValues[1]));
       movieId_SortedOutput=new IntWritable(Integer.parseInt(ratingValues[0]));
       context.write(averageRating_SortedOutput, movieId_SortedOutput);
       
      }
   }
  
public static class SortKeyComparator extends WritableComparator {

	public SortKeyComparator() {
		super(FloatWritable.class, true);
	}

	/**
	 * Need to implement our sorting mechanism.
      * @param a
      * @param b
      * @return 
	 */
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		FloatWritable key1 = (FloatWritable) a;
		FloatWritable key2 = (FloatWritable) b;

		// Implemet sorting in descending order
	//	int result = key1.get() < key2.get() ? 1 : key1.get() == key2.get() ? 0 : -1;
		return -1 * key1.compareTo(key2);
	}
 }
  public static class SortReducer2
       extends Reducer<FloatWritable,IntWritable,FloatWritable,IntWritable> {
    private IntWritable movieId_Result;
    private FloatWritable movieRating_Result;
    
   int counter=0;
    public void reduce(FloatWritable key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
        
     for(IntWritable wr : values){ 
         if(counter<25){
      context.write(key, wr);
         }
         counter+=1;
     }
    }
  }

 @Override
 public int run(String[] args) throws Exception {
  /*
   * Job 1
   */
  Configuration conf = getConf();
//  FileSystem fs = FileSystem.get(conf);
  Job job = Job.getInstance(conf, "Job1");
  job.setJarByClass(MovieLensTopMovies.class);

  job.setMapperClass(AggregateMapper1.class);
  job.setReducerClass(AggregateReducer1.class);

  job.setOutputKeyClass(IntWritable.class);
  job.setOutputValueClass(FloatWritable.class);

//  job.setInputFormatClass(TextInputFormat.class);
//  job.setOutputFormatClass(TextOutputFormat.class);

  FileInputFormat.addInputPath(job, new Path(args[0]));
  FileOutputFormat.setOutputPath(job, new Path(args[1]));

  job.waitForCompletion(true);

  /*
   * Job 2
   */
//    String infile = "/user/shanmugasudan/intermediate_output_11/part-r-00000";
//    Path ofile = new Path(infile);
 //   FileSystem fs = ofile.getFileSystem(getConf());
  Job job2 = Job.getInstance(conf, "Job 2");
  job2.setJarByClass(MovieLensTopMovies.class);

  job2.setMapperClass(SortMapper2.class);
  job2.setSortComparatorClass(SortKeyComparator.class);
  job2.setReducerClass(SortReducer2.class);
  job2.setNumReduceTasks(1);

  job2.setOutputKeyClass(FloatWritable.class);
  job2.setOutputValueClass(IntWritable.class);

//  job2.setInputFormatClass(TextInputFormat.class);
//  job2.setOutputFormatClass(TextOutputFormat.class);

  FileInputFormat.addInputPath(job2, new Path(args[1]));
  FileOutputFormat.setOutputPath(job2, new Path(args[2]));

  return job2.waitForCompletion(true) ? 0 : 1;
 }

 /**
  * Method Name: main Return type: none Purpose:Read the arguments from
  * command line and run the Job till completion
  * 
  */
 public static void main(String[] args) throws Exception {
  // TODO Auto-generated method stub
  if (args.length != 3) {
   System.err.println("Enter valid number of arguments <Inputdirectory>  <Outputlocation>");
   System.exit(0);
  }
  ToolRunner.run(new Configuration(), new MovieLensTopMovies(), args);
 }
}