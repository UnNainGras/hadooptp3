package org.epf.hadoop.colfil2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CommonFriendsReducer extends Reducer<UserPair, Text, UserPair, Text> {

    private Text outputValue = new Text();

    @Override
    protected void reduce(UserPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> friends = new HashSet<>();
        boolean directlyConnected = false;

        for (Text value : values) {
            if (value.toString().equals("0")) {
                directlyConnected = true;
            } else {
                friends.add(value.toString());
            }
        }

        // Si les utilisateurs ne sont pas directement connectés
        if (!directlyConnected) {
            outputValue.set(String.valueOf(friends.size()));
            context.write(key, outputValue);
        }
    }
}
