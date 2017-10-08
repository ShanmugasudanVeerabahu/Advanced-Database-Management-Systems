/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpaccesslog;

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

/**
 *
 * @author shanmugasudan
 */
public class HttpAccessLog {

    /**
     * @param args the command line arguments
     */
    
    
     public static class AccessLogMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text ipAddress = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String readLine = value.toString();
      String[] accessLogValues=readLine.split("-");
      ipAddress.set(accessLogValues[0]);
        context.write(ipAddress, one);
      }
    }
  

  public static class AccessLogReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable resultSet = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sumOfVisits = 0;
      for (IntWritable val : values) {
        sumOfVisits += val.get();
      }
      resultSet.set(sumOfVisits);
      context.write(key, resultSet);
    }
  }
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code aConfiguration conf = new Configuration();
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "HttpAccessLog");
    job.setJarByClass(HttpAccessLog.class);
    job.setMapperClass(AccessLogMapper.class);
    job.setCombinerClass(AccessLogReducer.class);
    job.setReducerClass(AccessLogReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
        
    }
    
}
