import com.cmbchina.ccd.pluto.elasticsearchclient.ElasticSearchConfig;
import com.cmbchina.ccd.pluto.elasticsearchclient.owl.OwlAdaptClient;
import com.cmbchina.ccd.pluto.elasticsearchclient.owl.OwlClient;

/**
 * Created by root on 3/22/16.
 */
public class ClientUtils {
    public static OwlClient getClient() {
        ElasticSearchConfig config = new ElasticSearchConfig();
        config.setHost("99.48.236.182");
        config.setPort(9300);
        config.setIndex("owl");
        config.setType("log");
        config.setClusterName("FullTextQuery");
        config.setDateIndex(false);
        OwlClient client = new OwlAdaptClient(config);
        client.init();
        return client;
    }
}
