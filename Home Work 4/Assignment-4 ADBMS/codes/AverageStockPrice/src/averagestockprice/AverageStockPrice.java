/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averagestockprice;
import java.io.DataInput;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class AverageStockPrice implements Writable {
    
        private double stockPriceAdjClose=0.0;
        private long  countOfStockPrice=0;
        
       
        
        
    public static class AverageStockPriceMapper extends Mapper<Object,Text,IntWritable,AverageStockPrice>{
            private IntWritable year= new IntWritable();
            private AverageStockPrice calculateAvg= new AverageStockPrice();
        
        public void map(Object key, Text value,Context context) throws IOException, InterruptedException {
             
                
            String readInputLine[]=value.toString().split(",");
            String yearOfStockPrice[]=readInputLine[2].split("-");
            String myStockPriceAdjClose=readInputLine[8];
            int yearValue=Integer.parseInt(yearOfStockPrice[0]);
            year.set(yearValue);
//            System.out.println("The year is :"+year);
            calculateAvg.setStockPriceAdjClose(Double.parseDouble(myStockPriceAdjClose));
//            System.out.println("The value of stockPriceAdjclose : "+calculateAvg.getStockPriceAdjClose());
            calculateAvg.setCountOfStockPrice(1);
            System.out.println("The year is : "+year+" The value is : "+calculateAvg);
            context.write(year, calculateAvg); 
               
                   
        }
    }
        
        public static class AverageStockPriceReducer extends Reducer<IntWritable,AverageStockPrice,IntWritable,DoubleWritable>{
                
                private AverageStockPrice result= new AverageStockPrice();
                
                
            public void reduce(IntWritable key,Iterable<AverageStockPrice> value, Context context) throws IOException, InterruptedException{
                
                        double sum=0.0;
                        long count=0;
                        
                        
                        for(AverageStockPrice val: value){
                            System.out.println("The stockPrice is: "+val.getStockPriceAdjClose());
                            sum+=val.getStockPriceAdjClose();
                            count+=val.getCountOfStockPrice(); //
                            
                        }
                        result.setCountOfStockPrice(count);
                        result.setStockPriceAdjClose(sum/(double)count);
                        System.out.println("The Key is : "+key+" The count is: "+count+" The sum is: "+sum);
                        context.write(key, new DoubleWritable(result.getStockPriceAdjClose()));
                
            }
            
        }
    

  
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
            
                // TODO code application logic here
                
                
                Configuration conf = new Configuration();
                Job job = Job.getInstance(conf, "AverageStockPrice");
                job.setJarByClass(AverageStockPrice.class);
                job.setMapperClass(AverageStockPriceMapper.class);
//                job.setCombinerClass(AverageStockPriceReducer.class);
                job.setReducerClass(AverageStockPriceReducer.class);
//                Log log=LogFactory.getLog(AverageStockPriceReducer.class);
                job.setMapOutputKeyClass(IntWritable.class);
                job.setMapOutputValueClass(AverageStockPrice.class);
                job.setOutputKeyClass(IntWritable.class);
                job.setOutputValueClass(DoubleWritable.class);
                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));
               
                    System.exit(job.waitForCompletion(true) ? 0 : 1);
                    
    }

    public double getStockPriceAdjClose() {
        return stockPriceAdjClose;
    }

    public void setStockPriceAdjClose(double stockPriceAdjClose) {
        this.stockPriceAdjClose = stockPriceAdjClose;
    }

    public long getCountOfStockPrice() {
        return countOfStockPrice;
    }

    public void setCountOfStockPrice(long countOfStockPrice) {
        this.countOfStockPrice = countOfStockPrice;
    }
    
    @Override
    public void readFields(DataInput di) throws IOException{
           
                stockPriceAdjClose= di.readDouble();
                countOfStockPrice=di.readLong();
           
    }
    
    @Override
    public void write(DataOutput d) throws IOException {
        d.writeDouble(stockPriceAdjClose);
        d.writeLong(countOfStockPrice);
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        @Override
    public String toString(){
        return countOfStockPrice+"\t"+stockPriceAdjClose;
    }
    
    
}
