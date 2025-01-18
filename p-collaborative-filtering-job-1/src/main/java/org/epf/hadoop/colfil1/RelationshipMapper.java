package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;

public class RelationshipMapper extends Mapper<LongWritable, Relationship, Text, Text> {
    private Text outputKey = new Text();
    private Text outputValue = new Text();

    @Override
    protected void map(LongWritable key, Relationship value, Context context) throws IOException, InterruptedException {
        // Émettre la relation A -> B
        outputKey.set(value.getId1());
        outputValue.set(value.getId2());
        context.write(outputKey, outputValue);

        // Émettre la relation B -> A
        outputKey.set(value.getId2());
        outputValue.set(value.getId1());
        context.write(outputKey, outputValue);
    }
}
