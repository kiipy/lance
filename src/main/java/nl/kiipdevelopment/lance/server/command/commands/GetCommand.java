package nl.kiipdevelopment.lance.server.command.commands;

import com.google.gson.JsonElement;
import nl.kiipdevelopment.lance.network.LanceMessage;
import nl.kiipdevelopment.lance.network.LanceMessageBuilder;
import nl.kiipdevelopment.lance.network.StatusCode;
import nl.kiipdevelopment.lance.server.ServerConnectionHandler;
import nl.kiipdevelopment.lance.server.command.Command;
import nl.kiipdevelopment.lance.server.storage.Storage;
import nl.kiipdevelopment.lance.server.storage.StorageType;

public class GetCommand extends Command {
    public GetCommand() {
        super("get", "Gets a value.");
    }

    @Override
    public LanceMessage execute(ServerConnectionHandler handler, int id, String trigger, String[] args) {
        LanceMessageBuilder builder = new LanceMessageBuilder();

        builder.setId(id).setStatusCode(StatusCode.OK);

        if (args.length == 0) {
            builder
                .setStatusCode(StatusCode.ERROR)
                .setMessage("Usage: get <key>");
        } else {
            try (Storage<JsonElement> storage = Storage.getStorage(StorageType.JSON)) {
                Object value = storage.get(args[0]);

                if (value == null) builder
                    .setStatusCode(StatusCode.ERROR)
                    .setMessage("Value is not a JsonElement.");
                else builder.setMessage(value.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }
}
