package org.epf.hadoop.colfil2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class CommonFriendsMapper extends Mapper<Object, Text, UserPair, Text> {

    private UserPair userPair = new UserPair();
    private Text friend = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split(":");
        if (parts.length == 2) {
            String user = parts[0].trim();
            String[] friends = parts[1].trim().split(",");

            // Pour chaque paire d'amis
            Arrays.sort(friends); // Assurer un ordre cohérent
            for (int i = 0; i < friends.length; i++) {
                for (int j = i + 1; j < friends.length; j++) {
                    userPair = new UserPair(friends[i], friends[j]);
                    friend.set(user);
                    context.write(userPair, friend);
                }
            }

            // Signaler les relations directes
            for (String friend : friends) {
                userPair = new UserPair(user, friend);
                context.write(userPair, new Text("0"));
            }
        }
    }
}
