/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movielensratingsd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author shanmugasudan
 */
public class MovieLensRatingSd implements Writable {
        private double standardDeviation=0.0;
        private double median=0.0;
        
    public static class MovieLensRatingSdMapper extends Mapper<Object,Text,IntWritable,DoubleWritable>{
            private IntWritable movieId;
            private DoubleWritable movieRating;
            
        public void map(Object key,Text value,Context context) throws IOException, InterruptedException{
            String readLine[]=value.toString().split("::");
            String myMovieRating=readLine[2];
            String myMovieId=readLine[1];
            movieId=new IntWritable(Integer.parseInt(myMovieId));
            movieRating=new DoubleWritable(Double.parseDouble(myMovieRating));
            context.write(movieId, movieRating);
        } 
        
    }
    
    public static class MovieLensRatingSdReducer extends Reducer<IntWritable,DoubleWritable,IntWritable,MovieLensRatingSd>{
            private MovieLensRatingSd result= new MovieLensRatingSd();
            private ArrayList<Double> sdMovieRating= new ArrayList<>();
            
        public void reduce(IntWritable key, Iterable<DoubleWritable> value,Context context) throws IOException, InterruptedException{
            double sum=0.0;
            double count=0.0;
            sdMovieRating.clear();
            result.setStandardDeviation(0.0);
            
            for(DoubleWritable val:value){
//                System.out.println("The value present here is : "+val.get());
                sdMovieRating.add(val.get());
                sum+=val.get();
                ++count;  
//                System.out.println("The sum is : "+sum+" The count is: "+count);
                
            }
            
            Collections.sort(sdMovieRating);
            
        if(count%2==0){
            result.setMedian((sdMovieRating.get((int)count/2-1)+
                    sdMovieRating.get((int)count/2))/2.0d);
        }
        else{
            result.setMedian(sdMovieRating.get((int)count/2));
        }
        double mean=sum/count;
        double sumOfSquares=0.0d;
        for(Double d:sdMovieRating){
            sumOfSquares+=(d-mean)*(d-mean);
        }
        result.setStandardDeviation((double)Math.sqrt(sumOfSquares/(count-1)));
        context.write(key, result);
            
        }
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code application logic here
         Configuration conf = new Configuration();
                Job job = Job.getInstance(conf, "MovieLensRatingSd");
                job.setJarByClass(MovieLensRatingSd.class);
                job.setMapperClass(MovieLensRatingSdMapper.class);
                job.setReducerClass(MovieLensRatingSdReducer.class);
                job.setMapOutputKeyClass(IntWritable.class);
                job.setMapOutputValueClass(DoubleWritable.class);
                job.setOutputKeyClass(IntWritable.class);
                job.setOutputValueClass(MovieLensRatingSd.class);
                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));
                System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    @Override
    public void write(DataOutput d) throws IOException {
        d.writeDouble(median);
        d.writeDouble(standardDeviation);
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        standardDeviation=di.readDouble();
        median=di.readDouble();
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }
    
        @Override
    public String toString(){
        return String.valueOf(median)+"\t"+String.valueOf(standardDeviation);
    }
    
}
