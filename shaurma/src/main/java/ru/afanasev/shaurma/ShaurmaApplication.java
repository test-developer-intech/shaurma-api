package ru.afanasev.shaurma;

import java.io.IOException;
import java.net.URISyntaxException;

import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.media.jsonb.JsonbSupport;
import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

import ru.afanasev.shaurma.Service.CustomerService;
import ru.afanasev.shaurma.model.DbInit;

public final class ShaurmaApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        startServer();
    }

    private static WebServer startServer() throws IOException, URISyntaxException {
        Config config = Config.create();
        Routing routing = createRouting(config);
        WebServer server = WebServer.builder(routing).addMediaSupport(JsonpSupport.create())
                .addMediaSupport(JsonbSupport.create()).config(config.get("server")).build();
        server.start().thenAccept(ws -> {
            System.out.println("Сервер запущен по адресу http://localhost:" + ws.port());
            ws.whenShutdown().thenRun(() -> System.out.println("сервер завершил работу"));
        }).exceptionally(t -> {
            System.err.println("Запуск прерван: " + t.getMessage());
            t.printStackTrace(System.err);
            return null;
        });
        return server;
    }

    private static Routing createRouting(Config config) throws IOException, URISyntaxException {
         Config dbconfig = config.get("db");
        DbClient dbClient = DbClient.builder(dbconfig).build();
        new DbInit().InitShema(dbClient);
        Routing routing = Routing.builder()
        .register("/customers", new CustomerService(dbClient))
        .build();
        return routing;
    }

}