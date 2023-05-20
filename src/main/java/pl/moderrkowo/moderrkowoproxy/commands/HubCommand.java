package pl.moderrkowo.moderrkowoproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class HubCommand implements SimpleCommand {

    private final ModerrkowoProxy proxy;

    public HubCommand(ModerrkowoProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Nie jesteś graczem!").color(NamedTextColor.RED));
            return;
        }
        final Player player = (Player) source;
        try {
            RegisteredServer current = player.getCurrentServer().orElseThrow().getServer();
            if (current.getServerInfo().getName().equalsIgnoreCase("lobby")) {
                player.sendMessage(Component.text("Już jesteś na HUB'ie!").color(NamedTextColor.RED));
                return;
            }
            RegisteredServer target = proxy.server.getServer("lobby").orElseThrow();
            player.createConnectionRequest(target).connect();
            source.sendMessage(Component.text("Połączono z HUB'em.").color(NamedTextColor.GREEN));
        } catch (Exception ignored) {
            player.sendMessage(Component.text("Wystąpił błąd.").color(NamedTextColor.RED));
            return;
        }
    }

    @Override
    public boolean hasPermission(final @NotNull Invocation invocation) {
        return true;
    }

    @Contract(pure = true)
    @Override
    public @Unmodifiable List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    @Override
    public @NotNull CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }

}
