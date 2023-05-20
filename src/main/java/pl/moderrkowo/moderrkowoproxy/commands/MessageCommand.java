package pl.moderrkowo.moderrkowoproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pl.moderrkowo.moderrkowoproxy.ModerrkowoProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MessageCommand implements SimpleCommand {

    private final ModerrkowoProxy proxy;

    public MessageCommand(ModerrkowoProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();
        if (!(source instanceof Player)) {
            source.sendMessage(Component.text("Nie jesteś graczem!").color(NamedTextColor.RED));
            return;
        }
        final Player player = (Player) source;
        if (args.length < 2) {
            player.sendMessage(Component.text("/msg <nick> <wiadomość>").color(NamedTextColor.RED));
            return;
        }

        // Getting Player
        final String playerReceiveName = args[0];
        final ProxyServer server = proxy.server;
        final Optional<Player> playerReceive = server.getPlayer(playerReceiveName);

        // Player Not Found
        if (playerReceive.isEmpty()) {
            final Component component = Component.text().content("Podany gracz jest offline!").color(NamedTextColor.RED).build();
            player.sendMessage(component);
            return;
        }

        // Building Message
        final String senderName = player.getUsername();
        final ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text().content(senderName + ":").color(NamedTextColor.GOLD);

        for (int i = 1; i < args.length; i += 1) {
            final String arg = args[i];
            builder.appendSpace().append(Component.text(arg).color(NamedTextColor.WHITE));
        }
        final Component component = builder.build();

        // Sending Message
        player.sendMessage(component);
        playerReceive.get().sendMessage(component);
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
    public @NotNull CompletableFuture<List<String>> suggestAsync(final @NotNull Invocation invocation) {
        if (invocation.arguments().length != 0) return CompletableFuture.completedFuture(List.of());
        List<String> playerNames = new ArrayList<>();
        ProxyServer server = proxy.server;
        server.getAllPlayers().forEach(player -> {
            playerNames.add(player.getUsername());
        });
        return CompletableFuture.completedFuture(playerNames);
    }

}
