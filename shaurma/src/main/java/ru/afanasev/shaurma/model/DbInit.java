package ru.afanasev.shaurma.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.helidon.common.reactive.Single;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbExecute;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DbInit {

    private final String MIGRATION = "/db/migration/db_init.sql";

    public void InitShema(DbClient dbClient) throws IOException, URISyntaxException {
        List<String> queryInit = readFile(MIGRATION);
        dbClient.inTransaction(executor -> initTables(executor, queryInit)).await();
    }

    private static Single<Long> initTables(DbExecute exec, List<String> list){
        Single<Long> stage = Single.just(0L);
            for (String pokemonValue : list) {
                stage = stage.flatMapSingle(result -> exec.dml(pokemonValue));
            }
        return stage;
    }

    private List<String> readFile(String fileName) throws URISyntaxException, IOException {
        List<String> builder = new ArrayList<>();
            URL resource = getClass().getClassLoader().getResource("db/migration/db_init.sql");
            File confFile = new File(resource.toURI());
            FileInputStream inputStream = new FileInputStream(confFile);
            BufferedReader reader  = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null) {
                System.err.println(line);
                builder.add(line);
                line = reader.readLine();
            }
            inputStream.close();
            reader.close();
        return builder;
    }
}