/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockpricecalculator;



/**
 *
 * @author shanmugasudan
 */
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public class StockPriceCalculator {

    /**
     * @param args the command line arguments
     */public static class StockMapper
       extends Mapper<Object, Text, Text, FloatWritable>{
         

 //   private final static IntWritable one = new IntWritable(1);
 //   private Text word = new Text();
    private FloatWritable stockHighPrice;

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
        String readCSVLine= value.toString();                       // parse each line in csv file as a string
        String[] stockListValues = readCSVLine.split(",");          // split the string into comma seperated values and 
                                                                    //save as a string array
        Text stockName= new Text(stockListValues[1]);
     //   String stockPrice=stockListValues[4];
        Float temp_stockListVaulue=Float.parseFloat(stockListValues[4]);
        stockHighPrice=  new FloatWritable(temp_stockListVaulue);   
        context.write(stockName,stockHighPrice);                        // pass stockPrice( 1st value after comma) and
                                                                    //StockHighPrice(4th Value after comma)
      }
    }
     
     
     
  public static class StockCombiner  
        extends Reducer<Text,FloatWritable,Text,FloatWritable> {
      
     private FloatWritable result = new FloatWritable();
    public void reduce(Text key, Iterable<FloatWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
       float sumOfStockPrice=0;
      for (FloatWritable val : values) {
        sumOfStockPrice += val.get();
        
      }
      result.set(sumOfStockPrice);
      context.write(key, result);
    } 
  } 
  

  public static class StockReducer
       extends Reducer<Text,FloatWritable,Text,FloatWritable> {
    private FloatWritable result = new FloatWritable();
    
    public void reduce(Text key, Iterable<FloatWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      float stockHighPriceAverage;
      float averageStockHighPrice= 0;
      int   countofEntries= 0;
      float sumOfStockPrice=0;
      for (FloatWritable val : values) {
        sumOfStockPrice += val.get();
        countofEntries+=1;
      }
      
      averageStockHighPrice=sumOfStockPrice/countofEntries;
      result.set(averageStockHighPrice);
      context.write(key, result);
    }
  }
    
    
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code application logic here
        Configuration conf= new Configuration();
        Job job= Job.getInstance(conf,"StockPriceCalculator");
        job.setJarByClass(StockPriceCalculator.class);
        job.setMapperClass(StockMapper.class);
        job.setCombinerClass(StockReducer.class);
        job.setReducerClass(StockReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    
}
