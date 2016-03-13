package cmput301.elasticsearchtest;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Based on Lab07 ElasticSearchController.
 * Original created by esports on 2/17/16.
 */
public class ElasticsearchLoginController {
    // Allow class to work with jest droid client
    private static JestDroidClient client;
    private static String url = "http://cmput301.softwareprocess.es:8080";
    private static String searchIndex = "team17";
    private static String searchType = "login";

    //TODO: A function that gets logins
    public static class GetLoginsTask extends AsyncTask<String, Void, ArrayList<Login>> {
        ArrayList<Login> logins = new ArrayList<Login>();

        protected String generateLoginQuery(String... text){
            String result;
            text = text[0].split(" ");
            String username = text[0];
            String password = text[1];

            result = "{\"query\": " +
                     "    {\"bool\": " +
                     "        {\"must\": " +
                     "        [" +
                     "            {\"match\": {\"uname\" : \"" + username + "\"}}," +
                     "            {\"match\": {\"pword\" : \"" + password + "\"}}" +
                     "        ]" +
                     "        }" +
                     "    }" +
                     "}";

            return result;
        }

        protected void separateObjects(SearchResult result){
            if (result.isSucceeded()){
                List<SearchResult.Hit<Login, Void>> returned_logins = result.getHits(Login.class);
                for (int i = 0; i < returned_logins.size(); i++){
                    logins.add(returned_logins.get(i).source);
                }
            } else {
                Log.i("TODO", "We failed our search.");
            }
        }

        @Override
        protected ArrayList<Login> doInBackground(String... login_params){
            String query = generateLoginQuery(login_params);
            Search search = new Search.Builder(query).addIndex(searchIndex).addType(searchType).build();

            verifyClient();

            try {
                SearchResult execute = client.execute(search);
                separateObjects(execute);
            } catch (IOException e){
                e.printStackTrace();
            }
            return logins;
        }
    }

    public static class AddLoginTask extends AsyncTask<Login, Void, Void> {
        @Override
        protected Void doInBackground(Login... logins) {
            verifyClient();

            for (Login login : logins) {
                Index index = new Index.Builder(login).index(searchIndex).type(searchType).build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                    } else {
                        Log.i("TODO", "We failed to add a login.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static void verifyClient(){
        // 1. Verify that client exists.
        // 2. If it does not, then make it.
        if (client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(url);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);

            client = (JestDroidClient) factory.getObject();
        }
    }

}
