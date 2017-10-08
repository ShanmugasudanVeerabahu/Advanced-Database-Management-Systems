/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minmaxstockvolume;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
public class MinMaxStockVolume implements Writable {
    
        private Date minStockVolume_Date;
        private Date maxStockVolume_Date;
        private long actStock_Volume;
        private double stockPriceAdjClose;
        private static final SimpleDateFormat dateFormat= new SimpleDateFormat("YYYY-MM-DD");

    
    public static class MinMaxStockVolumeMapper extends Mapper<Object,Text,Text,MinMaxStockVolume>{
            private Text mykeyStockName= new Text();
            private MinMaxStockVolume stockVolume= new MinMaxStockVolume();
            private static final SimpleDateFormat dateFormat= new SimpleDateFormat("YYYY-MM-DD");
            
        public void map(Object key, Text value,Context context)throws IOException{
           
            try {
                String myRow=value.toString();
                String[] myValues= myRow.split(",");
                String myKey=myValues[1];
                String myDate=myValues[2];
                String myActStockVolume=myValues[7];
                String myStockPriceAjClose=myValues[8];
                Date stockVolumeDate=dateFormat.parse(myDate);
                
                stockVolume.setMaxStockVolume_Date(stockVolumeDate);
                stockVolume.setMinStockVolume_Date(stockVolumeDate);
                stockVolume.setActStock_Volume(Long.parseLong(myActStockVolume));
                stockVolume.setStockPriceAdjClose(Double.parseDouble(myStockPriceAjClose));
                mykeyStockName.set(myKey);
               // mykeyStockName.set((myKey));
                
                try {
                    context.write(mykeyStockName, stockVolume);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MinMaxStockVolume.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                } catch (ParseException ex) {
                    Logger.getLogger(MinMaxStockVolume.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        }
        
    }
    public static class MinMaxStockVolumeReducer extends Reducer<Text,MinMaxStockVolume,Text,MinMaxStockVolume>{
    
            private MinMaxStockVolume result= new MinMaxStockVolume();
            
        public void reduce(Text key,Iterable<MinMaxStockVolume> values,Context context) throws IOException{
                try {
                    result.setMaxStockVolume_Date(null);
                    result.setMinStockVolume_Date(null);
                    result.setActStock_Volume(0);
                    result.setStockPriceAdjClose(0.0);
                    
                    ;
                    for(MinMaxStockVolume value:values){
                        if(result.getActStock_Volume()==0||
                                Long.compare(value.getActStock_Volume(),result.getActStock_Volume())<0){   
                            result.setMinStockVolume_Date(value.getMinStockVolume_Date());
                        }
                           if(result.getActStock_Volume()==0||
                                 Long.compare(value.getActStock_Volume(),result.getActStock_Volume())>0){
                            result.setMaxStockVolume_Date(value.getMaxStockVolume_Date());
                        }
                              if(result.getStockPriceAdjClose()==0||
                               Double.compare(value.getStockPriceAdjClose(),result.getStockPriceAdjClose())>0){
                            result.setStockPriceAdjClose(value.getStockPriceAdjClose());
                        }
                    }
                    
//                    for(MinMaxStockVolume value:values){
//                     
//                    }
//                    
//                    for(MinMaxStockVolume value:values){
//                     
//                    }
                    
                    context.write(key, result);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MinMaxStockVolume.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code application logic here
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "MinMaxStockVolume");
            job.setJarByClass(MinMaxStockVolume.class);
            job.setMapperClass(MinMaxStockVolumeMapper.class);
            job.setCombinerClass(MinMaxStockVolumeReducer.class);
            job.setReducerClass(MinMaxStockVolumeReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(MinMaxStockVolume.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    @Override
    public void write(DataOutput d) throws IOException {
        d.writeLong(minStockVolume_Date.getTime());
        d.writeLong(maxStockVolume_Date.getTime());
        d.writeLong(actStock_Volume);
        d.writeDouble(stockPriceAdjClose);
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        minStockVolume_Date= new Date(di.readLong());
        maxStockVolume_Date= new Date(di.readLong());
        actStock_Volume=di.readLong();
        stockPriceAdjClose= di.readDouble();
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
 

    public Date getMinStockVolume_Date() {
        return minStockVolume_Date;
    }

    public void setMinStockVolume_Date(Date minStockVolume_Date) {
        this.minStockVolume_Date = minStockVolume_Date;
    }

    public Date getMaxStockVolume_Date() {
        return maxStockVolume_Date;
    }

    public void setMaxStockVolume_Date(Date maxStockVolume_Date) {
        this.maxStockVolume_Date = maxStockVolume_Date;
    }

    public double getStockPriceAdjClose() {
        return stockPriceAdjClose;
    }

    public void setStockPriceAdjClose(double stockPriceAdjClose) {
        this.stockPriceAdjClose = stockPriceAdjClose;
    }
    
    public long getActStock_Volume() {
        return actStock_Volume;
    }

    public void setActStock_Volume(long actStock_Volume) {
        this.actStock_Volume = actStock_Volume;
    }
    public String toString(){
        return dateFormat.format(minStockVolume_Date)+"\t"+dateFormat.format(maxStockVolume_Date)+"\t"+stockPriceAdjClose;
    }
    
}
