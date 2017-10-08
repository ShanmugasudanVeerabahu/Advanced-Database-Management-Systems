package lab.secondary;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Sink;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class BloomFilterGauva extends Configured implements Tool {
	public static class BloomFilterMapper extends Mapper<Object, Text, Text, NullWritable> {
		Funnel<Person> p = new Funnel<Person>() {

			public void funnel(Person person, Sink into) {
				// TODO Auto-generated method stub
				into.putInt(person.id).putString(person.firstName, Charsets.UTF_8)
						.putString(person.lastName, Charsets.UTF_8).putInt(person.birthYear);
			}

		};
		private BloomFilter<Person> friends = BloomFilter.create(p, 500, 0.1);

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
                        
			Person p1 = new Person(1, "Abby", "Lahm", 3);
			Person p2 = new Person(2, "Jamie", "Scott", 4);
			ArrayList<Person> friendsList = new ArrayList<Person>();
			friendsList.add(p1);
			friendsList.add(p2);

			for (Person pr : friendsList) {
				friends.put(pr);
			}
		}

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String values[] = value.toString().split(",");
			Person p = new Person(Integer.parseInt(values[0]), values[1], values[2], Integer.parseInt(values[3]));
			if (friends.mightContain(p)) {
				context.write(value, NullWritable.get());
			}

		}

	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new BloomFilterGauva(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Bloom Filter");
		job.setJarByClass(BloomFilterGauva.class);
		job.setMapperClass(BloomFilterMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(0);
                
                job.setInputFormatClass(TextInputFormat.class);
                job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(
				args[0]));
		FileOutputFormat.setOutputPath(job,
				new Path(args[1]));
		return (job.waitForCompletion(true) ? 0 : 1);
	}
}