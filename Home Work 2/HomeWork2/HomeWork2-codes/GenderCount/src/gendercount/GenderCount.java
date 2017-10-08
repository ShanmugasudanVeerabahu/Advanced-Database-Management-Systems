/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gendercount;

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
public class GenderCount {

   public static class GenderMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text genderType = new Text();
        private int v;
   private IntWritable two;
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String readLine = value.toString();
      String[] accessLogValues=readLine.split("::");
      genderType.set(accessLogValues[1]);
        context.write(genderType, one);
      }
    }
   
    public static class GenderReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable resultSet = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sumOfGenderCount = 0;
      for (IntWritable val : values) {
        sumOfGenderCount += val.get();
      }
      resultSet.set(sumOfGenderCount);
      context.write(key, resultSet);
    }
  }

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
          Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Gender Count");
    job.setJarByClass(GenderCount.class);
    job.setMapperClass(GenderMapper.class);
    job.setCombinerClass(GenderReducer.class);
    job.setReducerClass(GenderReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
