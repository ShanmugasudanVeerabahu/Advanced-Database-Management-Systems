/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userratingformovies;

/**
 *
 * @author shanmugasudan
 */
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class UserRatingForMovies {

  public static class UserRatingMapper
       extends Mapper<Object, Text, IntWritable, IntWritable>{

        private final static IntWritable movieCount = new IntWritable(1);
        private int userId;
        private IntWritable uniqueUserId;
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String readLine = value.toString();
      String[] accessLogValues=readLine.split("::");
       userId=Integer.parseInt(accessLogValues[0]);
      uniqueUserId=new IntWritable(userId);
        context.write(uniqueUserId, movieCount);
      }
    }
  
  
  public static class UserRatingReducer
       extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable> {
    private IntWritable resultSet = new IntWritable();

    public void reduce(IntWritable key, Iterable<IntWritable> values,
                      Context context
                       ) throws IOException, InterruptedException {
      int sumOfmovieRatings = 0;
      for (IntWritable val : values) {
        sumOfmovieRatings += val.get();
      }
      resultSet.set(sumOfmovieRatings);
      context.write(key, resultSet);
    }
  }
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Configuration conf = new Configuration();
    Job job = Job.getInstance(conf,"User Rating");
    job.setJarByClass(UserRatingForMovies.class);
    job.setMapperClass(UserRatingMapper.class);
    job.setCombinerClass(UserRatingReducer.class);
    job.setReducerClass(UserRatingReducer.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    
}
