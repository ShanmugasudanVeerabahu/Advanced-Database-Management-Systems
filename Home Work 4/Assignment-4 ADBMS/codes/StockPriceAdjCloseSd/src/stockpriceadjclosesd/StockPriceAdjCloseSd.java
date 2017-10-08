/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockpriceadjclosesd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author shanmugasudan
 */
public class StockPriceAdjCloseSd implements Writable{

            private double standardDeviation=0.0;
            private double median=0.0;

   
    public static class StockPriceAdjCloseSdMapper extends Mapper<Object,Text,IntWritable,SortedMapWritable>{
            private IntWritable year;
            private DoubleWritable stockPrice_AdjClose;
            private static final LongWritable ONE= new LongWritable(1);
            
        public void map(Object key,Text value,Context context) throws IOException, InterruptedException{
            String readLine[]=value.toString().split(",");
            String myYear[]=readLine[2].split("-");
            String myYearValue=myYear[0];
            String myStockPrice_AdjClose=readLine[8];
            year=new IntWritable(Integer.parseInt(myYearValue));
            stockPrice_AdjClose=new DoubleWritable(Double.parseDouble(myStockPrice_AdjClose));
            
            SortedMapWritable mapOutputValue= new SortedMapWritable();
            mapOutputValue.put(stockPrice_AdjClose, ONE);
             
            context.write(year, mapOutputValue);
        } 
        
    }
    public static class StockPriceAdjCloseSdReducer extends Reducer<IntWritable,SortedMapWritable,IntWritable,StockPriceAdjCloseSd>{
            private StockPriceAdjCloseSd result= new StockPriceAdjCloseSd();
            private TreeMap<Double,Long> treeMapResult= new TreeMap<Double,Long>();
            
        public void reduce(IntWritable key, Iterable<SortedMapWritable> value,Context context) throws IOException, InterruptedException{
            double sum=0.0;
            double count=0.0;
            treeMapResult.clear();
            result.setStandardDeviation(0.0);
            result.setMedian(0.0);
            
            for(SortedMapWritable val:value){
                for(Entry<WritableComparable,Writable> entry:val.entrySet()){
                    double stockPrice=((DoubleWritable)entry.getKey()).get();
                    long numOfOccurance=((LongWritable)entry.getValue()).get();
                    
                    count+=numOfOccurance;
                    sum+=stockPrice*count;
                    
                    Long mapSize=treeMapResult.get(stockPrice);
                if(mapSize==null){
                    treeMapResult.put(stockPrice,numOfOccurance);
                }
                else{
                    treeMapResult.put(stockPrice, mapSize+numOfOccurance);
                }
                    
            }
                
        }
            
           double medianIndex=count/2D;
           double prevCount=0.0;
           double actCount=0.0;
           
           double prevKey=0;
           for(Entry<Double,Long> entry:treeMapResult.entrySet()){
               actCount=prevCount+entry.getValue();
               if(prevCount<=medianIndex&&medianIndex<actCount){
                   if(count%2==0&&medianIndex<actCount){
                       System.out.println("Lets try this");
                       System.out.println("My entry value is :"+entry.getKey());
                       System.out.println("The typecasted result is : "+(double)(entry.getKey())+
                               " and the prev key is : "+prevKey);
                       result.setMedian((double)(entry.getKey()+prevKey)/2.0d);
                   }
                   else{
                       result.setMedian(entry.getKey());
                   }
                   break;
                   
               }
                    prevCount=actCount;
                    prevKey=entry.getKey();
           }
           double mean=sum/count;
           
           double sumOfSquares=0.0d;
           for(Entry<Double,Long>entry:treeMapResult.entrySet()){
               sumOfSquares+=(entry.getKey()-mean)*(entry.getKey()-mean)*entry.getValue();
           }
           result.setStandardDeviation((double)Math.sqrt(sumOfSquares/(count-1)));
           context.write(key, result);
        }
    }
    
    public static class StockPriceAdjCloseSdCombiner extends Reducer<IntWritable,SortedMapWritable,IntWritable,SortedMapWritable>{
        
        protected void reduce(IntWritable key,Iterable<SortedMapWritable> value,Context context) throws IOException, InterruptedException{
             SortedMapWritable outValue= new SortedMapWritable();
             
             for(SortedMapWritable v: value){
                 for(Entry<WritableComparable,Writable> entry:v.entrySet()){
                    LongWritable count=(LongWritable)outValue.get(entry.getKey());
                     
                     if(outValue.get(entry.getKey())!=null){
                         count.set(count.get()+((LongWritable)entry.getValue()).get());
                        // outValue.put(entry.getKey(),count);
                         
//                         outValue.put(entry.getKey(),outValue.get(entry.getKey()+1));
                     }
                     else{
//                         System.out.println("The Key in combiner is : "+entry.getKey());
                         outValue.put(entry.getKey(),new LongWritable(((LongWritable)entry.getValue()).get()));
                           
                     }
                     
                 }
             }
//             System.out.println("The value sent out is: "+outValue.getKey());
             context.write(key,outValue);
            
        }
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code application logic here
                Configuration conf = new Configuration();
                long milliSeconds = 1000*60*60; //<default is 600000, likewise can give any value)
                conf.setLong("mapred.task.timeout", milliSeconds);
                Job job = Job.getInstance(conf, "StockPriceAdjCloseSd");
                job.setJarByClass(StockPriceAdjCloseSd.class);
                job.setMapperClass(StockPriceAdjCloseSdMapper.class);
                job.setCombinerClass(StockPriceAdjCloseSdCombiner.class);
                job.setReducerClass(StockPriceAdjCloseSdReducer.class);
                job.setMapOutputKeyClass(IntWritable.class);
                job.setMapOutputValueClass(SortedMapWritable.class);
                job.setOutputKeyClass(IntWritable.class);
                job.setOutputValueClass(StockPriceAdjCloseSd.class);
                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));
                System.exit(job.waitForCompletion(true) ? 0 : 1);
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
    
     @Override
    public String toString(){
        return median+"\t"+standardDeviation;
    }
    
//    public static class SortedMapWritable extends Object {
//    @Override
//    public String toString(){
//        SortedMapWritable.class.getName().   .entrySet().getKey().get();
//    }
//}
    
   
}


