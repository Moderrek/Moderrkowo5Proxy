package pl.moderrkowo.moderrkowoproxy.services;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;
import pl.moderrkowo.moderrkowoproxy.commands.AdminChatCommand;
import pl.moderrkowo.moderrkowoproxy.commands.HubCommand;
import pl.moderrkowo.moderrkowoproxy.commands.MessageCommand;
import pl.moderrkowo.moderrkowoproxy.commands.PlayersCommand;

public class CommandService implements ModerrkowoService {

    private void registerCommands(ModerrkowoProxy proxy) {
        registerCommand(new HubCommand(proxy), "hub", "lobby");
        registerCommand(new PlayersCommand(proxy), "players", "online");
        registerCommand(new AdminChatCommand(proxy), "adminchat", "ac");
        registerCommand(new MessageCommand(proxy), "msg", "message");
    }

    private ProxyServer server;

    private void registerCommand(Command command, String name, String... aliases) {
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(name)
                .aliases(aliases)
                .plugin(this)
                .build();
        commandManager.register(commandMeta, command);
    }

    @Override
    public void start(ModerrkowoProxy proxy, ProxyServer server) {
        this.server = server;
        registerCommands(proxy);
        proxy.logger.info("Loaded CommandService");
    }
}
