package org.epf.hadoop.colfil2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ColFilJob2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Common Friends Job");

        job.setJarByClass(ColFilJob2.class);

        job.setMapperClass(CommonFriendsMapper.class);
        job.setReducerClass(CommonFriendsReducer.class);

        job.setMapOutputKeyClass(UserPair.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(UserPair.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setNumReduceTasks(2); // 2 reducers comme demandé

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}